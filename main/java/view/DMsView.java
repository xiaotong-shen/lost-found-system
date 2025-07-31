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
import java.time.format.DateTimeFormatter;
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
        dmsScrollPane = new JScrollPane(dmsListPanel);
        dmsScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        dmsScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        dmsScrollPane.setPreferredSize(new Dimension(300, 600));

        // Placeholder for DMs
        JLabel placeholderLabel = new JLabel("No DMs available.", SwingConstants.CENTER);
        placeholderLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        dmsListPanel.add(placeholderLabel);

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
        DMsState state = (DMsState) evt.getNewValue();

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
            selectedChatId = state.getCurrentChat().getChatId();
            if (dmsController != null && currentUsername != null) {
                dmsController.loadMessages(selectedChatId, currentUsername);
            }
        }
    }

    private void updateChatsList(List<Chat> chats) {
        dmsListPanel.removeAll();

        if (chats.isEmpty()) {
            JLabel placeholderLabel = new JLabel("No DMs available.", SwingConstants.CENTER);
            placeholderLabel.setFont(new Font("Arial", Font.ITALIC, 14));
            dmsListPanel.add(placeholderLabel);
        } else {
            for (Chat chat : chats) {
                JPanel chatItem = createChatListItem(chat);
                dmsListPanel.add(chatItem);
            }
        }

        dmsListPanel.revalidate();
        dmsListPanel.repaint();
    }

    private JPanel createChatListItem(Chat chat) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        panel.setBackground(Color.WHITE);

        // Get the other participant's name (not the current user)
        String otherParticipantName = "";
        for (User participant : chat.getParticipants()) {
            if (!participant.getName().equals(currentUsername)) {
                otherParticipantName = participant.getName();
                break;
            }
        }

        JLabel nameLabel = new JLabel(otherParticipantName);
        nameLabel.setFont(new Font("Arial", Font.BOLD, 14));

        // Get the last message if available
        String lastMessage = "No messages yet";
        if (chat.getMessages() != null && !chat.getMessages().isEmpty()) {
            Message lastMsg = chat.getMessages().get(chat.getMessages().size() - 1);
            lastMessage = lastMsg.getContent();
            if (lastMessage.length() > 30) {
                lastMessage = lastMessage.substring(0, 27) + "...";
            }
        }

        JLabel messageLabel = new JLabel(lastMessage);
        messageLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        messageLabel.setForeground(Color.GRAY);

        JPanel textPanel = new JPanel(new BorderLayout());
        textPanel.add(nameLabel, BorderLayout.NORTH);
        textPanel.add(messageLabel, BorderLayout.SOUTH);

        panel.add(textPanel, BorderLayout.CENTER);

        // Add click listener
        panel.addMouseListener(new java.awt.event.MouseAdapter() {
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
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

            for (Message message : messages) {
                String time = message.getSentAt().format(formatter);
                String senderName = message.getSender().getName();
                String content = message.getContent();

                chatText.append(String.format("[%s] %s: %s\n", time, senderName, content));
            }

            chatArea.setText(chatText.toString());
            // Scroll to bottom
            chatArea.setCaretPosition(chatArea.getDocument().getLength());
        }
    }
}