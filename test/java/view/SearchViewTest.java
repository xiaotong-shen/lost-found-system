package view;

import entity.Post;
import interface_adapter.search.SearchController;
import interface_adapter.search.SearchState;
import interface_adapter.search.SearchViewModel;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for SearchView.
 * No Mockito: we use lightweight stubs to avoid ByteBuddy/JDK version issues.
 */
class SearchViewTest {

    @BeforeAll
    static void headless() {
        System.setProperty("java.awt.headless", "true");
    }

    /**
     * A stub controller that records calls without touching real use cases.
     */
    static class StubSearchController extends SearchController {
        boolean executeCalled = false;
        String lastQuery = null;
        boolean backCalled = false;

        // super requires params; pass null and override methods to avoid NPE
        public StubSearchController() { super(null, null); }

        @Override
        public void execute(String searchQuery) {
            executeCalled = true;
            lastQuery = searchQuery;
        }

        @Override
        public void executeAdvancedSearch(String title, String location,
                                          java.util.List<String> tags, Boolean isLost) {
            // not used in these tests
        }

        @Override
        public void navigateBack() {
            backCalled = true;
        }
    }

    private SearchViewModel vm;        // use the real ViewModel (no mocks)
    private StubSearchController controller;
    private SearchState initialState;
    private SearchView view;

    @BeforeEach
    void setup() throws Exception {
        vm = new SearchViewModel();
        initialState = new SearchState();
        initialState.setSearchQuery("laptop");
        initialState.setLoading(false);
        vm.setState(initialState);

        controller = new StubSearchController();

        SwingUtilities.invokeAndWait(() -> {
            view = new SearchView(vm);
            view.setSearchController(controller);
        });
    }

    // ----- helpers -----
    private JButton findButton(Container root, String text) {
        for (Component c : root.getComponents()) {
            if (c instanceof JButton && text.equals(((JButton) c).getText())) return (JButton) c;
            if (c instanceof Container) {
                JButton b = findButton((Container) c, text);
                if (b != null) return b;
            }
        }
        return null;
    }

    private boolean treeContainsLabelText(Component root, String needle) {
        if (root instanceof JLabel) {
            String t = ((JLabel) root).getText();
            if (t != null && t.contains(needle)) return true;
        }
        if (root instanceof Container) {
            for (Component c : ((Container) root).getComponents()) {
                if (treeContainsLabelText(c, needle)) return true;
            }
        }
        return false;
    }

    // ----- tests -----

    @Test
    void clickSearch_executesWithQuery() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            JButton search = findButton(view, "Search");
            assertNotNull(search, "Search button not found");
            search.doClick();
        });

        assertTrue(controller.executeCalled, "Controller.execute() was not called");
        assertEquals("laptop", controller.lastQuery);
        assertFalse(controller.backCalled);
    }

    @Test
    void backButton_callsNavigateBack() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            JButton back = findButton(view, "Back");
            assertNotNull(back, "Back button not found");
            back.doClick();
        });

        assertTrue(controller.backCalled, "Controller.navigateBack() was not called");
        assertFalse(controller.executeCalled);
    }

    @Test
    void propertyChange_loadingAndNoResultsRendersMessages() throws Exception {
        // Loading → shows "Searching..."
        SearchState loading = new SearchState();
        loading.setSearchQuery("x");
        loading.setLoading(true);

        SwingUtilities.invokeAndWait(() -> {
            view.propertyChange(new PropertyChangeEvent(this, "state", initialState, loading));
            assertTrue(treeContainsLabelText(view, "Searching..."));
        });

        // Empty results → shows "No posts found"
        SearchState empty = new SearchState();
        empty.setSearchQuery("x");
        empty.setLoading(false);
        empty.setSearchResults(Collections.emptyList());

        SwingUtilities.invokeAndWait(() -> {
            view.propertyChange(new PropertyChangeEvent(this, "state", loading, empty));
            assertTrue(treeContainsLabelText(view, "No posts found"));
        });
    }

    @Test
    void typingUpdatesViewModelState() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            // Find the JTextField inside the LabelTextPanel
            JTextField tf = null;
            outer:
            for (Component c : view.getComponents()) {
                if (c instanceof Container) {
                    for (Component cc : ((Container) c).getComponents()) {
                        if (cc instanceof LabelTextPanel) {
                            for (Component ccc : ((LabelTextPanel) cc).getComponents()) {
                                if (ccc instanceof JTextField) { tf = (JTextField) ccc; break outer; }
                            }
                        }
                    }
                }
            }
            assertNotNull(tf, "Search input field not found");
            tf.setText("phone"); // triggers DocumentListener -> vm.setState(...)
        });

        // The real ViewModel now holds updated state
        assertEquals("phone", vm.getState().getSearchQuery());
    }

    @Test
    void propertyChange_withResultsDisplaysHeader() throws Exception {
        // Use a minimal stub Post to avoid depending on concrete constructor
        Post post = new Post();
        post.setTitle("Found laptop");
        post.setLost(false);

        SearchState withResults = new SearchState();
        withResults.setSearchQuery("lap");
        withResults.setLoading(false);
        withResults.setSearchResults(java.util.List.of(post));

        SwingUtilities.invokeAndWait(() -> {
            view.propertyChange(new PropertyChangeEvent(this, "state", initialState, withResults));
            assertTrue(treeContainsLabelText(view, "Search Results (1 posts found)"));
        });
    }
}
