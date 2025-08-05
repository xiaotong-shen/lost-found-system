package view;

import entity.User;
import entity.Chat;
import entity.Message;
import interface_adapter.ViewManagerModel;
import interface_adapter.dms.DMsController;
import interface_adapter.dms.DMsState;
import interface_adapter.dms.DMsViewModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

/**
 * The View for the DMs page.
 */
public class DMsView extends JPanel implements PropertyChangeListener {
    private final String viewName = "dms";
    private final JTextField newDMField = new JTextField(20);
    private final JButton newDMButton = new JButton("New DM");
    private final JButton backButton = new JButton("Back");
    private final JPanel dmsListPanel = new JPanel();
    private JScrollPane dmsScrollPane = new JScrollPane();
    private final JPanel chatPanel = new JPanel();
    private JScrollPane chatScrollPane = new JScrollPane();
    private final JTextArea chatArea = new JTextArea();
    private final JTextField chatInputField = new JTextField(30);
    private final JButton sendButton = new JButton("Send");

    private DMsController dmsController;
    private DMsViewModel dmsViewModel;
    private String currentUsername;
    private String selectedChatId;

    public DMsView(ViewManagerModel viewManagerModel, DMsViewModel dMsViewModel) {
        this.dmsViewModel = dMsViewModel;
        this.dmsViewModel.addPropertyChangeListener(this);

        // Set up the main layout
        this.setLayout(new BorderLayout());

        // Create top toolbar
        JPanel toolbarPanel = new JPanel(new BorderLayout());
        toolbarPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Left side - dm
        JPanel dmPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        dmPanel.add(new JLabel("Username:"));
        dmPanel.add(newDMField);
        dmPanel.add(newDMButton);

        // Right side - back button
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(backButton);

        toolbarPanel.add(dmPanel, BorderLayout.WEST);
        toolbarPanel.add(buttonPanel, BorderLayout.EAST);

        // DMs list on the left
        dmsListPanel.setLayout(new BoxLayout(dmsListPanel, BoxLayout.Y_AXIS));
        dmsListPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        dmsListPanel.setBackground(new Color(245, 245, 245)); // Light gray background

        dmsScrollPane = new JScrollPane(dmsListPanel);
        dmsScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        dmsScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        dmsScrollPane.setPreferredSize(new Dimension(300, 600));
        dmsScrollPane.setBorder(BorderFactory.createTitledBorder("Direct Messages"));
        dmsScrollPane.getViewport().setBackground(new Color(245, 245, 245));

        // Add component listener to detect when view becomes visible
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                // Load chats when the view becomes visible
                if (dmsController != null && currentUsername != null) {
                    dmsController.loadChats(currentUsername);
                }
            }
        });

        // Chat panel on the right
        chatPanel.setLayout(new BorderLayout());
        chatPanel.setBorder(BorderFactory.createTitledBorder("DM Chat"));
        chatPanel.setPreferredSize(new Dimension(500, 600));

        // Chat area (scrollable, non-editable)
        chatArea.setEditable(false);
        chatArea.setLineWrap(true);
        chatArea.setWrapStyleWord(true);
        chatScrollPane = new JScrollPane(chatArea);
        chatScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        chatScrollPane.setPreferredSize(new Dimension(500, 500));
        chatPanel.add(chatScrollPane, BorderLayout.CENTER);

        // Chat input area (bottom of chat panel)
        JPanel chatInputPanel = new JPanel(new BorderLayout());
        chatInputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        chatInputPanel.add(chatInputField, BorderLayout.CENTER);
        chatInputPanel.add(sendButton, BorderLayout.EAST); // Non-functional
        chatPanel.add(chatInputPanel, BorderLayout.SOUTH);

        // Main split panel (DMs list left, chat right)
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(dmsScrollPane, BorderLayout.WEST);
        mainPanel.add(chatPanel, BorderLayout.CENTER);

        // Set minimum sizes to ensure proper layout
        dmsScrollPane.setMinimumSize(new Dimension(250, 400));
        chatPanel.setMinimumSize(new Dimension(400, 400));

        // Add components to main panel
        this.add(toolbarPanel, BorderLayout.NORTH);
        this.add(mainPanel, BorderLayout.CENTER);

        backButton.addActionListener(e -> {viewManagerModel.popViewOrClose();});

        newDMButton.addActionListener(e -> {
            if (!newDMField.getText().trim().isEmpty() && dmsController != null && currentUsername != null) {
                String targetUsername = newDMField.getText().trim();

                // Check if target username is the same as current user
                if (targetUsername.equals(currentUsername)) {
                    JOptionPane.showMessageDialog(this, "You cannot create a DM with yourself!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Create a new chat with the current user and target user
                List<User> participants = new ArrayList<>();

                // Get current user from data access layer
                User currentUser = dmsController.getUserByUsername(currentUsername);
                if (currentUser == null) {
                    JOptionPane.showMessageDialog(this, "Current user not found!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                participants.add(currentUser);

                // Get target user from data access layer
                User targetUser = dmsController.getUserByUsername(targetUsername);
                if (targetUser == null) {
                    JOptionPane.showMessageDialog(this, "User '" + targetUsername + "' not found!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                participants.add(targetUser);

                dmsController.createChat(participants);
                newDMField.setText(""); // Clear the field
            }
        });

        sendButton.addActionListener(e -> {
            if (!chatInputField.getText().trim().isEmpty() && selectedChatId != null && currentUsername != null && dmsController != null) {
                String messageContent = chatInputField.getText().trim();
                dmsController.sendMessage(selectedChatId, currentUsername, messageContent);
                chatInputField.setText(""); // Clear the field
            }
        });
    }

    public String getViewName() {
        return viewName;
    }

    public void setDMsController(DMsController dmsController) {
        this.dmsController = dmsController;
    }

    public void setCurrentUsername(String username) {
        this.currentUsername = username;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        System.out.println("DEBUG: DMsView propertyChange called with property: " + evt.getPropertyName());
        DMsState state = (DMsState) evt.getNewValue();
        System.out.println("DEBUG: State received - chats: " + (state.getChats() != null ? state.getChats().size() : "null") +
                ", messages: " + (state.getMessages() != null ? state.getMessages().size() : "null") +
                ", currentChat: " + (state.getCurrentChat() != null ? state.getCurrentChat().getChatId() : "null"));

        if (state.getError() != null) {
            JOptionPane.showMessageDialog(this, state.getError(), "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (state.getChats() != null) {
            updateChatsList(state.getChats());
        }

        if (state.getMessages() != null) {
            updateChatArea(state.getMessages());
        }

        if (state.getCurrentChat() != null) {
            System.out.println("DEBUG: Setting current chat: " + state.getCurrentChat().getChatId());
            selectedChatId = state.getCurrentChat().getChatId();
        }
    }

    private void updateChatsList(List<Chat> chats) {
        System.out.println("DEBUG: updateChatsList called with " + (chats != null ? chats.size() : "null") + " chats");
        dmsListPanel.removeAll();

        if (chats == null || chats.isEmpty()) {
            System.out.println("DEBUG: No chats to display, showing placeholder");
            JLabel placeholderLabel = new JLabel("No DMs available.", SwingConstants.CENTER);
            placeholderLabel.setFont(new Font("Arial", Font.ITALIC, 14));
            placeholderLabel.setForeground(Color.GRAY);
            placeholderLabel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
            dmsListPanel.add(placeholderLabel);
        } else {
            System.out.println("DEBUG: Adding " + chats.size() + " chat items to the list");
            for (int i = 0; i < chats.size(); i++) {
                Chat chat = chats.get(i);
                System.out.println("DEBUG: Adding chat with ID: " + chat.getChatId() + ", participants: " + chat.getParticipants());
                JPanel chatItem = createChatListItem(chat);
                dmsListPanel.add(chatItem);

                // Add spacing between items (except after the last one)
                if (i < chats.size() - 1) {
                    dmsListPanel.add(Box.createVerticalStrut(2));
                }
            }
        }

        // Add some padding at the bottom
        dmsListPanel.add(Box.createVerticalStrut(10));

        dmsListPanel.revalidate();
        dmsListPanel.repaint();
        dmsScrollPane.revalidate();
        dmsScrollPane.repaint();
        System.out.println("DEBUG: updateChatsList completed");
    }

    private JPanel createChatListItem(Chat chat) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        panel.setBackground(Color.WHITE);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80)); // Fixed height for consistency
        panel.setPreferredSize(new Dimension(280, 80));

        // Get the other participant's name (not the current user)
        String otherParticipantName = "";
        for (String participant : chat.getParticipants()) {
            if (!participant.equals(currentUsername)) {
                otherParticipantName = participant;
                break;
            }
        }

        JLabel nameLabel = new JLabel(otherParticipantName);
        nameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        nameLabel.setForeground(new Color(50, 50, 50));

        // Since messages are now stored separately, show a placeholder
        String lastMessage = "Click to view messages";

        JLabel messageLabel = new JLabel(lastMessage);
        messageLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        messageLabel.setForeground(Color.GRAY);

        JPanel textPanel = new JPanel(new BorderLayout());
        textPanel.setOpaque(false);
        textPanel.add(nameLabel, BorderLayout.NORTH);
        textPanel.add(messageLabel, BorderLayout.SOUTH);

        panel.add(textPanel, BorderLayout.CENTER);

        // Add hover effect
        panel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                panel.setBackground(new Color(240, 248, 255)); // Light blue on hover
                panel.repaint();
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                panel.setBackground(Color.WHITE);
                panel.repaint();
            }

            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                selectedChatId = chat.getChatId();
                if (dmsController != null && currentUsername != null) {
                    dmsController.loadMessages(selectedChatId, currentUsername);
                }
            }
        });

        return panel;
    }

    private void updateChatArea(List<Message> messages) {
        chatArea.setText("");
        if (messages != null && !messages.isEmpty()) {
            StringBuilder chatText = new StringBuilder();

            for (Message message : messages) {
                String time = message.getSentAt();
                String senderName = message.getSender(); // Now directly a String username
                String content = message.getContent();

                chatText.append(String.format("[%s] %s: %s\n", time, senderName, content));
            }

            chatArea.setText(chatText.toString());
            // Scroll to bottom
            chatArea.setCaretPosition(chatArea.getDocument().getLength());
        }
    }
}