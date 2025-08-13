package use_case.dms;

import entity.Chat;
import entity.Message;
import entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DMsInteractorTest {

    private DMsUserDataAccessInterface dmsDataAccessObject;
    private DMsOutputBoundary dmsOutputBoundary;
    private DMsInteractor interactor;

    @BeforeEach
    void setUp() {
        dmsDataAccessObject = mock(DMsUserDataAccessInterface.class);
        dmsOutputBoundary = mock(DMsOutputBoundary.class);
        interactor = new DMsInteractor(dmsDataAccessObject, dmsOutputBoundary);
    }

    @Test
    @DisplayName("Load Chats - Success")
    void loadChats_Success() {
        // Arrange
        String username = "testUser";
        List<Chat> mockChats = mock(List.class);
        DMsInputData inputData = mock(DMsInputData.class);
        when(inputData.getUsername()).thenReturn(username);
        when(dmsDataAccessObject.getChatsForUser(username)).thenReturn(mockChats);

        // Act
        interactor.loadChats(inputData);

        // Assert
        verify(dmsDataAccessObject).getChatsForUser(username);
        ArgumentCaptor<DMsOutputData> outputDataCaptor = ArgumentCaptor.forClass(DMsOutputData.class);
        verify(dmsOutputBoundary).prepareLoadChatsView(outputDataCaptor.capture());

        DMsOutputData outputData = outputDataCaptor.getValue();
        assertEquals(mockChats, outputData.getChats());
        assertNull(outputData.getError());
    }

    @Test
    @DisplayName("Load Chats - Exception")
    void loadChats_Exception() {
        // Arrange
        String username = "testUser";
        DMsInputData inputData = mock(DMsInputData.class);
        when(inputData.getUsername()).thenReturn(username);
        when(dmsDataAccessObject.getChatsForUser(username))
                .thenThrow(new RuntimeException("Database error"));

        // Act
        interactor.loadChats(inputData);

        // Assert
        ArgumentCaptor<DMsOutputData> outputDataCaptor = ArgumentCaptor.forClass(DMsOutputData.class);
        verify(dmsOutputBoundary).prepareLoadChatsView(outputDataCaptor.capture());

        DMsOutputData outputData = outputDataCaptor.getValue();
        assertNull(outputData.getChats());
        assertEquals("Failed to load chats: Database error", outputData.getError());
    }

    @Test
    @DisplayName("Create Chat - Success")
    void createChat_Success() {
        // Arrange
        List<User> participants = Arrays.asList(
                mock(User.class),
                mock(User.class)
        );
        when(participants.get(0).getName()).thenReturn("user1");
        when(participants.get(1).getName()).thenReturn("user2");

        List<String> participantNames = Arrays.asList("user1", "user2");
        Chat mockChat = mock(Chat.class);
        List<Chat> updatedChats = Arrays.asList(mockChat);

        DMsInputData inputData = mock(DMsInputData.class);
        when(inputData.getParticipants()).thenReturn(participants);

        when(dmsDataAccessObject.createChat(participantNames)).thenReturn(mockChat);
        when(dmsDataAccessObject.getChatsForUser("user1")).thenReturn(updatedChats);

        // Act
        interactor.createChat(inputData);

        // Assert
        verify(dmsDataAccessObject).createChat(participantNames);
        verify(dmsOutputBoundary).prepareLoadChatsView(any(DMsOutputData.class));
    }

    @Test
    @DisplayName("Send Message - Success")
    void sendMessage_Success() {
        // Arrange
        String chatId = "chat123";
        String username = "user1";
        String messageContent = "Hello";
        Message mockMessage = mock(Message.class);
        List<Message> mockMessages = Arrays.asList(mockMessage);

        DMsInputData inputData = mock(DMsInputData.class);
        when(inputData.getChatId()).thenReturn(chatId);
        when(inputData.getUsername()).thenReturn(username);
        when(inputData.getMessageContent()).thenReturn(messageContent);

        when(dmsDataAccessObject.sendMessage(chatId, username, messageContent)).thenReturn(mockMessage);
        when(dmsDataAccessObject.getMessagesForChat(chatId)).thenReturn(mockMessages);

        // Act
        interactor.sendMessage(inputData);

        // Assert
        verify(dmsDataAccessObject).sendMessage(chatId, username, messageContent);

        ArgumentCaptor<DMsOutputData> outputDataCaptor = ArgumentCaptor.forClass(DMsOutputData.class);
        verify(dmsOutputBoundary).prepareSendMessageView(outputDataCaptor.capture());

        DMsOutputData outputData = outputDataCaptor.getValue();
        assertEquals(mockMessages, outputData.getMessages());
        assertNull(outputData.getError());
    }

    @Test
    @DisplayName("Send Message - Invalid Data")
    void sendMessage_InvalidData() {
        // Arrange
        DMsInputData inputData = mock(DMsInputData.class);
        when(inputData.getChatId()).thenReturn(null);
        when(inputData.getUsername()).thenReturn("user1");
        when(inputData.getMessageContent()).thenReturn("Hello");

        // Act
        interactor.sendMessage(inputData);

        // Assert
        verify(dmsDataAccessObject, never()).sendMessage(anyString(), anyString(), anyString());

        ArgumentCaptor<DMsOutputData> outputDataCaptor = ArgumentCaptor.forClass(DMsOutputData.class);
        verify(dmsOutputBoundary).prepareSendMessageView(outputDataCaptor.capture());

        DMsOutputData outputData = outputDataCaptor.getValue();
        assertEquals("Invalid message data", outputData.getError());
    }

    @Test
    @DisplayName("Load Messages - Success")
    void loadMessages_Success() {
        // Arrange
        String chatId = "chat123";
        String username = "user1";
        Chat mockChat = mock(Chat.class);
        List<Message> mockMessages = Arrays.asList(mock(Message.class));

        DMsInputData inputData = mock(DMsInputData.class);
        when(inputData.getChatId()).thenReturn(chatId);
        when(inputData.getUsername()).thenReturn(username);

        when(dmsDataAccessObject.getChatById(chatId)).thenReturn(mockChat);
        when(dmsDataAccessObject.getMessagesForChat(chatId)).thenReturn(mockMessages);

        // Act
        interactor.loadMessages(inputData);

        // Assert
        verify(dmsDataAccessObject).getChatById(chatId);
        verify(dmsDataAccessObject).getMessagesForChat(chatId);

        ArgumentCaptor<DMsOutputData> outputDataCaptor = ArgumentCaptor.forClass(DMsOutputData.class);
        verify(dmsOutputBoundary).prepareLoadMessagesView(outputDataCaptor.capture());

        DMsOutputData outputData = outputDataCaptor.getValue();
        assertEquals(mockMessages, outputData.getMessages());
        assertEquals(mockChat, outputData.getCurrentChat());
        assertNull(outputData.getError());
    }

    @Test
    @DisplayName("Get User By Username - Success")
    void getUserByUsername_Success() {
        // Arrange
        String username = "user1";
        User mockUser = mock(User.class);
        when(dmsDataAccessObject.getUserByUsername(username)).thenReturn(mockUser);

        // Act
        User result = interactor.getUserByUsername(username);

        // Assert
        verify(dmsDataAccessObject).getUserByUsername(username);
        assertEquals(mockUser, result);
    }

    @Test
    @DisplayName("Chat Exists Between Users - Success")
    void chatExistsBetweenUsers_Success() {
        // Arrange
        String user1 = "user1";
        String user2 = "user2";
        when(dmsDataAccessObject.chatExistsBetweenUsers(user1, user2)).thenReturn(true);

        // Act
        boolean result = interactor.chatExistsBetweenUsers(user1, user2);

        // Assert
        verify(dmsDataAccessObject).chatExistsBetweenUsers(user1, user2);
        assertTrue(result);
    }

    @Test
    @DisplayName("Update Chat Is Blocked - Success")
    void updateChatIsBlocked_Success() {
        // Arrange
        String chatId = "chat123";
        boolean isBlocked = true;

        // Act
        interactor.updateChatIsBlocked(chatId, isBlocked);

        // Assert
        verify(dmsDataAccessObject).updateChatIsBlocked(chatId, isBlocked);
    }

    @Test
    @DisplayName("Is Chat Blocked - Success")
    void isChatBlocked_Success() {
        // Arrange
        String chatId = "chat123";
        when(dmsDataAccessObject.isChatBlocked(chatId)).thenReturn(true);

        // Act
        boolean result = interactor.isChatBlocked(chatId);

        // Assert
        verify(dmsDataAccessObject).isChatBlocked(chatId);
        assertTrue(result);
    }
    @Test
    @DisplayName("Send Message - Database Error")
    void sendMessage_DatabaseError() {
        // Arrange
        String chatId = "chat123";
        String username = "user1";
        String messageContent = "Hello";

        DMsInputData inputData = mock(DMsInputData.class);
        when(inputData.getChatId()).thenReturn(chatId);
        when(inputData.getUsername()).thenReturn(username);
        when(inputData.getMessageContent()).thenReturn(messageContent);

        when(dmsDataAccessObject.sendMessage(chatId, username, messageContent))
                .thenThrow(new RuntimeException("Database error"));

        // Act
        interactor.sendMessage(inputData);

        // Assert
        ArgumentCaptor<DMsOutputData> outputDataCaptor = ArgumentCaptor.forClass(DMsOutputData.class);
        verify(dmsOutputBoundary).prepareSendMessageView(outputDataCaptor.capture());

        DMsOutputData outputData = outputDataCaptor.getValue();
        assertEquals("Failed to send message: Database error", outputData.getError());
    }

    @Test
    @DisplayName("Load Messages - Database Error")
    void loadMessages_DatabaseError() {
        // Arrange
        String chatId = "chat123";
        String username = "user1";

        DMsInputData inputData = mock(DMsInputData.class);
        when(inputData.getChatId()).thenReturn(chatId);
        when(inputData.getUsername()).thenReturn(username);

        when(dmsDataAccessObject.getChatById(chatId))
                .thenThrow(new RuntimeException("Database error"));

        // Act
        interactor.loadMessages(inputData);

        // Assert
        ArgumentCaptor<DMsOutputData> outputDataCaptor = ArgumentCaptor.forClass(DMsOutputData.class);
        verify(dmsOutputBoundary).prepareLoadMessagesView(outputDataCaptor.capture());

        DMsOutputData outputData = outputDataCaptor.getValue();
        assertEquals("Failed to load messages: Database error", outputData.getError());
    }

    @Test
    @DisplayName("Get User By Username - Exception")
    void getUserByUsername_Exception() {
        // Arrange
        String username = "user1";
        when(dmsDataAccessObject.getUserByUsername(username))
                .thenThrow(new RuntimeException("Database error"));

        // Act
        User result = interactor.getUserByUsername(username);

        // Assert
        verify(dmsDataAccessObject).getUserByUsername(username);
        assertNull(result);
    }

    @Test
    @DisplayName("Chat Exists Between Users - Exception")
    void chatExistsBetweenUsers_Exception() {
        // Arrange
        String user1 = "user1";
        String user2 = "user2";
        when(dmsDataAccessObject.chatExistsBetweenUsers(user1, user2))
                .thenThrow(new RuntimeException("Database error"));

        // Act
        boolean result = interactor.chatExistsBetweenUsers(user1, user2);

        // Assert
        verify(dmsDataAccessObject).chatExistsBetweenUsers(user1, user2);
        assertFalse(result);
    }

    @Test
    @DisplayName("Update Chat Is Blocked - Exception")
    void updateChatIsBlocked_Exception() {
        // Arrange
        String chatId = "chat123";
        boolean isBlocked = true;
        doThrow(new RuntimeException("Database error"))
                .when(dmsDataAccessObject).updateChatIsBlocked(chatId, isBlocked);

        // Act
        interactor.updateChatIsBlocked(chatId, isBlocked);

        // Assert
        verify(dmsDataAccessObject).updateChatIsBlocked(chatId, isBlocked);
    }

    @Test
    @DisplayName("Is Chat Blocked - Exception")
    void isChatBlocked_Exception() {
        // Arrange
        String chatId = "chat123";
        when(dmsDataAccessObject.isChatBlocked(chatId))
                .thenThrow(new RuntimeException("Database error"));

        // Act
        boolean result = interactor.isChatBlocked(chatId);

        // Assert
        verify(dmsDataAccessObject).isChatBlocked(chatId);
        assertFalse(result);
    }

    @Test
    @DisplayName("Create Chat - Database Error")
    void createChat_DatabaseError() {
        // Arrange
        List<User> participants = Arrays.asList(
                mock(User.class),
                mock(User.class)
        );
        when(participants.get(0).getName()).thenReturn("user1");
        when(participants.get(1).getName()).thenReturn("user2");

        DMsInputData inputData = mock(DMsInputData.class);
        when(inputData.getParticipants()).thenReturn(participants);

        when(dmsDataAccessObject.createChat(any()))
                .thenThrow(new RuntimeException("Database error"));

        // Act
        interactor.createChat(inputData);

        // Assert
        ArgumentCaptor<DMsOutputData> outputDataCaptor = ArgumentCaptor.forClass(DMsOutputData.class);
        verify(dmsOutputBoundary).prepareCreateChatView(outputDataCaptor.capture());

        DMsOutputData outputData = outputDataCaptor.getValue();
        assertEquals("Failed to create chat: Database error", outputData.getError());
    }

    @Test
    @DisplayName("Create Chat - Empty Participants List")
    void createChat_EmptyParticipants() {
        // Arrange
        DMsInputData inputData = mock(DMsInputData.class);
        when(inputData.getParticipants()).thenReturn(new ArrayList<>());

        // Act
        interactor.createChat(inputData);

        // Assert
        verify(dmsDataAccessObject, never()).createChat(any());
        
        ArgumentCaptor<DMsOutputData> outputDataCaptor = ArgumentCaptor.forClass(DMsOutputData.class);
        verify(dmsOutputBoundary).prepareCreateChatView(outputDataCaptor.capture());
        
        DMsOutputData outputData = outputDataCaptor.getValue();
        assertNull(outputData.getChats());
        assertEquals("No participants specified", outputData.getError());
    }
}