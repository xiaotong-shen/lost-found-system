package use_case.dms;

/**
 * Interface for chat use case output operations.
 */
public interface DMsOutputBoundary {

    /**
     * Prepares the view for loading chats.
     * @param outputData the output data containing chat information
     */
    void prepareLoadChatsView(DMsOutputData outputData);

    /**
     * Prepares the view for loading messages.
     * @param outputData the output data containing message information
     */
    void prepareLoadMessagesView(DMsOutputData outputData);

    /**
     * Prepares the view for sending a message.
     * @param outputData the output data containing message information
     */
    void prepareSendMessageView(DMsOutputData outputData);

    /**
     * Prepares the view for creating a chat.
     * @param outputData the output data containing chat information
     */
    void prepareCreateChatView(DMsOutputData outputData);
}