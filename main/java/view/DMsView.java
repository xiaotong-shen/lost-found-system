package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * The View for the DMs page.
 */
public class DMsView extends JPanel {
    private final String viewName = "dms";
    private final JTextField searchField = new JTextField(20);
    private final JButton searchButton = new JButton("Search");
    private final JButton backButton = new JButton("Back");
    private final JPanel dmsListPanel = new JPanel();
    private JScrollPane dmsScrollPane = new JScrollPane();

    public DMsView() {
        // Set up the main layout
        this.setLayout(new BorderLayout());

        // Create top toolbar
        JPanel toolbarPanel = new JPanel(new BorderLayout());
        toolbarPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Left side - search
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.add(new JLabel("Search:"));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        // Right side - back button
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(backButton);

        toolbarPanel.add(searchPanel, BorderLayout.WEST);
        toolbarPanel.add(buttonPanel, BorderLayout.EAST);

        // DMs list on the left
        dmsListPanel.setLayout(new BoxLayout(dmsListPanel, BoxLayout.Y_AXIS));
        dmsScrollPane = new JScrollPane(dmsListPanel);
        dmsScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        dmsScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        dmsScrollPane.setPreferredSize(new Dimension(400, 600));

        // Placeholder for DMs
        JLabel placeholderLabel = new JLabel("No DMs available.", SwingConstants.CENTER);
        placeholderLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        dmsListPanel.add(placeholderLabel);

        // Add components to main panel
        this.add(toolbarPanel, BorderLayout.NORTH);
        this.add(dmsScrollPane, BorderLayout.WEST);
    }

    public String getViewName() {
        return viewName;
    }

    public JButton getBackButton() {
        return backButton;
    }
} 