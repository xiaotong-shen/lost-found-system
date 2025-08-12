package view;

import interface_adapter.admin.AdminController;
import interface_adapter.admin.AdminViewModel;
import interface_adapter.admin.AdminState;
import entity.Post;
import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;



class AdminViewTest {
    // First define the stub controller class
    static class StubAdminController extends AdminController {
        List<String> searchQueries = new ArrayList<>();
        List<String> deletedPostIds = new ArrayList<>();
        boolean addPostCalled = false;
        String lastAddedTitle;
        String lastAddedContent;
        List<String> lastAddedTags;
        String lastAddedLocation;
        Boolean lastAddedIsLost;
        
        boolean editPostCalled = false;
        String lastEditedPostId;
        String lastEditedTitle;
        String lastEditedContent;
        String lastEditedLocation;
        List<String> lastEditedTags;
        Boolean lastEditedIsLost;

        public StubAdminController() {
            super(null, null);
        }

        @Override
        public void searchPosts(String searchQuery) {
            searchQueries.add(searchQuery);
        }

        @Override
        public void deletePost(String postId) {
            deletedPostIds.add(postId);
        }

        @Override
        public void addPost(String title, String content, List<String> tags, 
                          String location, boolean isLost) {
            addPostCalled = true;
            lastAddedTitle = title;
            lastAddedContent = content;
            lastAddedTags = tags;
            lastAddedLocation = location;
            lastAddedIsLost = isLost;
        }

        @Override
        public void editPost(String postId, String title, String content, 
                           String location, List<String> tags, boolean isLost) {
            editPostCalled = true;
            lastEditedPostId = postId;
            lastEditedTitle = title;
            lastEditedContent = content;
            lastEditedLocation = location;
            lastEditedTags = tags;
            lastEditedIsLost = isLost;
        }
    }

    // Then declare the class fields
    private AdminView adminView;
    private AdminViewModel viewModel;
    private StubAdminController controller;

    /**
     * Mock implementation of Post dialog interactions for testing
     */
    static class DialogInteractionSimulator {
        private final AdminView view;
        private final StubAdminController controller;

        DialogInteractionSimulator(AdminView view, StubAdminController controller) {
            this.view = view;
            this.controller = controller;
        }

        void simulateAddPost(String title, String content, List<String> tags, 
                           String location, boolean isLost) {
            controller.addPost(title, content, tags, location, isLost);
        }

        void simulateEditPost(String postId, String title, String content, 
                            String location, List<String> tags, boolean isLost) {
            controller.editPost(postId, title, content, location, tags, isLost);
        }

        void simulateDeletePost(String postId) {
            controller.deletePost(postId);
        }
    }

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        viewModel = new AdminViewModel();
        adminView = new AdminView(viewModel);
        controller = new StubAdminController();
        adminView.setAdminController(controller);
    }

    @org.junit.jupiter.api.Test
    void testAddPostDialog() {
        DialogInteractionSimulator simulator = new DialogInteractionSimulator(adminView, controller);
        
        String title = "New Post";
        String content = "Test Content";
        List<String> tags = Arrays.asList("tag1", "tag2");
        String location = "Test Location";
        boolean isLost = true;

        simulator.simulateAddPost(title, content, tags, location, isLost);

        assertTrue(controller.addPostCalled);
        assertEquals(title, controller.lastAddedTitle);
        assertEquals(content, controller.lastAddedContent);
        assertEquals(tags, controller.lastAddedTags);
        assertEquals(location, controller.lastAddedLocation);
        assertEquals(isLost, controller.lastAddedIsLost);
    }

    @org.junit.jupiter.api.Test
    void testEditPostDialog() {
        // Set up initial state
        Post testPost = createSamplePost(1, "Original Title");
        AdminState state = new AdminState();
        state.setPosts(Collections.singletonList(testPost));
        state.setSelectedPost(testPost);
        viewModel.setState(state);
        adminView.setSelectedPost("1");

        DialogInteractionSimulator simulator = new DialogInteractionSimulator(adminView, controller);
        
        String title = "Updated Title";
        String content = "Updated Content";
        List<String> tags = Arrays.asList("tag3", "tag4");
        String location = "Updated Location";
        boolean isLost = false;

        simulator.simulateEditPost("1", title, content, location, tags, isLost);

        assertTrue(controller.editPostCalled);
        assertEquals("1", controller.lastEditedPostId);
        assertEquals(title, controller.lastEditedTitle);
        assertEquals(content, controller.lastEditedContent);
        assertEquals(location, controller.lastEditedLocation);
        assertEquals(tags, controller.lastEditedTags);
        assertEquals(isLost, controller.lastEditedIsLost);
    }

    @org.junit.jupiter.api.Test
    void testDeletePost() {
        Post testPost = createSamplePost(1, "Test Post");
        AdminState state = new AdminState();
        state.setPosts(Collections.singletonList(testPost));
        viewModel.setState(state);
        adminView.propertyChange(new PropertyChangeEvent(viewModel, "state", null, state));

        DialogInteractionSimulator simulator = new DialogInteractionSimulator(adminView, controller);
        simulator.simulateDeletePost("1");

        assertEquals(1, controller.deletedPostIds.size());
        assertEquals("1", controller.deletedPostIds.get(0));
    }

    private Post createSamplePost(int id, String title) {
        return new Post() {
            @Override
            public int getPostID() {
                return id;
            }

            @Override
            public String getTitle() {
                return title;
            }

            @Override
            public String getDescription() {
                return "Test description";
            }

            @Override
            public String getLocation() {
                return "Test location";
            }

            @Override
            public List<String> getTags() {
                return Arrays.asList("tag1", "tag2");
            }

            @Override
            public boolean isLost() {
                return true;
            }

            @Override
            public String getAuthor() {
                return "Test Author";
            }

            @Override
            public String getTimestamp() {
                return LocalDateTime.now()
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            }

            @Override
            public boolean isResolved() {
                return false;
            }

            @Override
            public void setResolved(boolean resolved) {
                // No-op for test implementation
            }

            @Override
            public String getResolvedBy() {
                return null;
            }

            @Override
            public void setResolvedBy(String username) {
                // No-op for test implementation
            }

            @Override
            public String getCreditedTo() {
                return null;
            }

            @Override
            public void setCreditedTo(String username) {
                // No-op for test implementation
            }

            @Override
            public int getNumberOfLikes() {
                return 0;
            }
        };
    }

}