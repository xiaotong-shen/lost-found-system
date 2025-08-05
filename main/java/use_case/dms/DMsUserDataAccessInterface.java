package use_case.dms;

import entity.Chat;
import entity.Message;
import entity.User;

import java.util.List;

public interface DMsUserDataAccessInterface {
    /**
     * Gets all chats for a specific user.
     * @param username the username to get chats for
     * @return List of chats the user is a participant in
     */
    List<Chat> getChatsForUser(String username);

    /**
     * Creates a new chat with the specified participants.
     * @param participants list of users to include in the chat
     * @return the created chat
     */
    Chat createChat(List<String> participants);

    /**
     * Sends a message to a specific chat.
     * @param chatId the ID of the chat to send the message to
     * @param sender the user sending the message
     * @param content the message content
     * @return the sent message
     */
    Message sendMessage(String chatId, User sender, String content);

    /**
     * Gets all messages for a specific chat.
     * @param chatId the ID of the chat to get messages for
     * @return List of messages in the chat
     */
    List<Message> getMessagesForChat(String chatId);

    /**
     * Gets a specific chat by its ID.
     * @param chatId the ID of the chat to retrieve
     * @return the chat, or null if not found
     */
    Chat getChatById(String chatId);

    /**
     * Gets a user by their username.
     * @param username the username to search for
     * @return the user, or null if not found
     */
    User getUserByUsername(String username);
}
