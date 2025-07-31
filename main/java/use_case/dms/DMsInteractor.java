package use_case.dms;

import entity.Chat;
import entity.Message;
import entity.User;

import java.util.List;

/**
 * Interactor for chat use case operations.
 */
public class DMsInteractor implements DMsInputBoundary {
    private final DMsUserDataAccessInterface dmsUserDataAccessInterface;
    private final DMsOutputBoundary dmsOutputBoundary;

    public DMsInteractor(DMsUserDataAccessInterface dmsUserDataAccessInterface, DMsOutputBoundary dmsOutputBoundary) {
        this.dmsUserDataAccessInterface = dmsUserDataAccessInterface;
        this.dmsOutputBoundary = dmsOutputBoundary;
    }

    @Override
    public void loadChats(DMsInputData inputData) {
        try {
            String username = inputData.getUsername();
            List<Chat> chats = dmsUserDataAccessInterface.getChatsForUser(username);
            DMsOutputData outputData = new DMsOutputData(chats, null, null, null);
            dmsOutputBoundary.prepareLoadChatsView(outputData);
        } catch (Exception e) {
            DMsOutputData outputData = new DMsOutputData(null, null, null, "Failed to load chats: " + e.getMessage());
            dmsOutputBoundary.prepareLoadChatsView(outputData);
        }
    }

    @Override
    public void createChat(DMsInputData inputData) {
        try {
            System.out.println("DEBUG: DMsInteractor.createChat() called");
            List<User> participants = inputData.getParticipants();
            if (participants == null || participants.isEmpty()) {
                System.out.println("DEBUG: No participants specified");
                DMsOutputData outputData = new DMsOutputData(null, null, null, "No participants specified");
                dmsOutputBoundary.prepareCreateChatView(outputData);
                return;
            }

            System.out.println("DEBUG: Creating chat with " + participants.size() + " participants");
            Chat chat = dmsUserDataAccessInterface.createChat(participants);
            System.out.println("DEBUG: Chat created with ID: " + (chat != null ? chat.getChatId() : "null"));

            // After creating the chat, load the updated chat list for the current user
            if (participants.size() > 0) {
                String currentUsername = participants.get(0).getName();
                System.out.println("DEBUG: Loading updated chats for user: " + currentUsername);
                List<Chat> updatedChats = dmsUserDataAccessInterface.getChatsForUser(currentUsername);
                System.out.println("DEBUG: Found " + updatedChats.size() + " chats for user");
                DMsOutputData outputData = new DMsOutputData(updatedChats, null, null, null);
                dmsOutputBoundary.prepareLoadChatsView(outputData);
            } else {
                DMsOutputData outputData = new DMsOutputData(null, null, chat, null);
                dmsOutputBoundary.prepareCreateChatView(outputData);
            }
        } catch (Exception e) {
            System.err.println("DEBUG: Exception in createChat: " + e.getMessage());
            e.printStackTrace();
            DMsOutputData outputData = new DMsOutputData(null, null, null, "Failed to create chat: " + e.getMessage());
            dmsOutputBoundary.prepareCreateChatView(outputData);
        }
    }


    @Override
    public void sendMessage(DMsInputData inputData) {
        try {
            String chatId = inputData.getChatId();
            String username = inputData.getUsername();
            String messageContent = inputData.getMessageContent();

            if (chatId == null || username == null || messageContent == null || messageContent.trim().isEmpty()) {
                DMsOutputData outputData = new DMsOutputData(null, null, null, "Invalid message data");
                dmsOutputBoundary.prepareSendMessageView(outputData);
                return;
            }

            // Get the current user from the data access layer
            User sender = dmsUserDataAccessInterface.getUserByUsername(username);
            if (sender == null) {
                DMsOutputData outputData = new DMsOutputData(null, null, null, "User not found");
                dmsOutputBoundary.prepareSendMessageView(outputData);
                return;
            }

            Message message = dmsUserDataAccessInterface.sendMessage(chatId, sender, messageContent);
            List<Message> messages = dmsUserDataAccessInterface.getMessagesForChat(chatId);
            DMsOutputData outputData = new DMsOutputData(null, messages, null, null);
            dmsOutputBoundary.prepareSendMessageView(outputData);
        } catch (Exception e) {
            DMsOutputData outputData = new DMsOutputData(null, null, null, "Failed to send message: " + e.getMessage());
            dmsOutputBoundary.prepareSendMessageView(outputData);
        }
    }

    @Override
    public void loadMessages(DMsInputData inputData) {
        try {
            String chatId = inputData.getChatId();
            String username = inputData.getUsername();

            if (chatId == null || username == null) {
                DMsOutputData outputData = new DMsOutputData(null, null, null, "Invalid chat data");
                dmsOutputBoundary.prepareLoadMessagesView(outputData);
                return;
            }

            Chat currentChat = dmsUserDataAccessInterface.getChatById(chatId);
            if (currentChat == null) {
                DMsOutputData outputData = new DMsOutputData(null, null, null, "Chat not found");
                dmsOutputBoundary.prepareLoadMessagesView(outputData);
                return;
            }

            List<Message> messages = dmsUserDataAccessInterface.getMessagesForChat(chatId);
            DMsOutputData outputData = new DMsOutputData(null, messages, currentChat, null);
            dmsOutputBoundary.prepareLoadMessagesView(outputData);
        } catch (Exception e) {
            DMsOutputData outputData = new DMsOutputData(null, null, null, "Failed to load messages: " + e.getMessage());
            dmsOutputBoundary.prepareLoadMessagesView(outputData);
        }
    }

    @Override
    public User getUserByUsername(String username) {
        try {
            return dmsUserDataAccessInterface.getUserByUsername(username);
        } catch (Exception e) {
            return null;
        }
    }
}