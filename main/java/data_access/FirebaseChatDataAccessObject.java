package data_access;

import com.google.firebase.database.*;
import use_case.dms.DMsUserDataAccessInterface;
import entity.User;
import entity.Chat;
import entity.Message;
import data_access.FirebaseUserDataAccessObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.time.LocalDateTime;

public class FirebaseChatDataAccessObject implements DMsUserDataAccessInterface {

    private final DatabaseReference chatsRef;
    private final DatabaseReference messagesRef;
    private final FirebaseUserDataAccessObject userDAO;

    public FirebaseChatDataAccessObject() {
        this.chatsRef = FirebaseDatabase.getInstance().getReference("chats");
        this.messagesRef = FirebaseDatabase.getInstance().getReference("messages");
        this.userDAO = new FirebaseUserDataAccessObject();
    }

    @Override
    public List<Chat> getChatsForUser(String username) {
        System.out.println("\n=== DEBUG: FirebaseChatDataAccessObject.getChatsForUser() called ===");
        System.out.println("DEBUG: Getting chats for user: '" + username + "'");
        CompletableFuture<List<Chat>> future = new CompletableFuture<>();

        // Get all chats and filter by participants
        chatsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Chat> chats = new ArrayList<>();
                for (DataSnapshot chatSnapshot : dataSnapshot.getChildren()) {
                    Chat chat = chatSnapshot.getValue(Chat.class);
                    if (chat != null && chat.getParticipants() != null) {
                        // Check if the user is a participant in this chat
                        boolean isParticipant = false;
                        for (User participant : chat.getParticipants()) {
                            if (participant.getName().equals(username)) {
                                isParticipant = true;
                                break;
                            }
                        }
                        if (isParticipant) {
                            chats.add(chat);
                        }
                    }
                }
                System.out.println("DEBUG: Firebase chats retrieved: " + chats.size());
                future.complete(chats);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("DEBUG: Firebase error getting chats: " + databaseError.getMessage());
                future.completeExceptionally(new RuntimeException("Failed to get chats: " + databaseError.getMessage()));
            }
        });

        try {
            return future.get(5, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            System.err.println("DEBUG: Error getting chats: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public Chat createChat(List<User> participants) {
        System.out.println("\n=== DEBUG: FirebaseChatDataAccessObject.createChat() called ===");
        System.out.println("DEBUG: Creating chat with participants: " + participants.size());
        
        for (User participant : participants) {
            System.out.println("DEBUG: Participant: " + participant.getName());
        }

        String chatId = generateChatId();
        LocalDateTime createdAt = LocalDateTime.now();
        Chat chat = new Chat(chatId, participants, new ArrayList<>(), createdAt);

        CompletableFuture<Chat> future = new CompletableFuture<>();

        chatsRef.child(chatId).setValue(chat, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError != null) {
                    System.err.println("DEBUG: Firebase error creating chat: " + databaseError.getMessage());
                    future.completeExceptionally(new RuntimeException("Failed to create chat: " + databaseError.getMessage()));
                } else {
                    System.out.println("DEBUG: Firebase chat created successfully: " + chatId);
                    future.complete(chat);
                }
            }
        });

        try {
            return future.get(5, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            System.err.println("DEBUG: Error creating chat: " + e.getMessage());
            return chat; // Return the chat object even if Firebase save fails
        }
    }

    @Override
    public Message sendMessage(String chatId, User sender, String content) {
        System.out.println("\n=== DEBUG: FirebaseChatDataAccessObject.sendMessage() called ===");
        System.out.println("DEBUG: Sending message to chat: '" + chatId + "' from: '" + sender.getName() + "'");

        String messageId = generateMessageId();
        LocalDateTime sentAt = LocalDateTime.now();
        Message message = new Message(messageId, sender, content, sentAt, false);

        messagesRef.child(chatId).child(messageId).setValue(message, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError != null) {
                    System.err.println("DEBUG: Firebase error sending message: " + databaseError.getMessage());
                } else {
                    System.out.println("DEBUG: Firebase message sent successfully: " + messageId);
                }
            }
        });

        return message;
    }

    @Override
    public List<Message> getMessagesForChat(String chatId) {
        System.out.println("\n=== DEBUG: FirebaseChatDataAccessObject.getMessagesForChat() called ===");
        System.out.println("DEBUG: Getting messages for chat: '" + chatId + "'");

        CompletableFuture<List<Message>> future = new CompletableFuture<>();

        messagesRef.child(chatId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Message> messages = new ArrayList<>();
                for (DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {
                    Message message = messageSnapshot.getValue(Message.class);
                    if (message != null) {
                        messages.add(message);
                    }
                }
                System.out.println("DEBUG: Firebase messages retrieved: " + messages.size());
                future.complete(messages);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("DEBUG: Firebase error getting messages: " + databaseError.getMessage());
                future.completeExceptionally(new RuntimeException("Failed to get messages: " + databaseError.getMessage()));
            }
        });

        try {
            return future.get(5, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            System.err.println("DEBUG: Error getting messages: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public Chat getChatById(String chatId) {
        System.out.println("\n=== DEBUG: FirebaseChatDataAccessObject.getChatById() called ===");
        System.out.println("DEBUG: Getting chat by ID: '" + chatId + "'");

        CompletableFuture<Chat> future = new CompletableFuture<>();

        chatsRef.child(chatId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Chat chat = dataSnapshot.getValue(Chat.class);
                System.out.println("DEBUG: Firebase chat retrieved: " + (chat != null ? chat.getChatId() : "null"));
                future.complete(chat);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("DEBUG: Firebase error getting chat: " + databaseError.getMessage());
                future.completeExceptionally(new RuntimeException("Failed to get chat: " + databaseError.getMessage()));
            }
        });

        try {
            return future.get(5, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            System.err.println("DEBUG: Error getting chat: " + e.getMessage());
            return null;
        }
    }

    private String generateChatId() {
        return "chat_" + System.currentTimeMillis() + "_" + (int) (Math.random() * 1000);
    }

    private String generateMessageId() {
        return "msg_" + System.currentTimeMillis() + "_" + (int) (Math.random() * 1000);
    }

    @Override
    public User getUserByUsername(String username) {
        return userDAO.get(username);
    }
}