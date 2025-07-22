package data_access;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOptions;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.WriteResult;
import com.google.api.core.ApiFuture;
import entity.User;
import entity.UserFactory;
import use_case.change_password.ChangePasswordUserDataAccessInterface;
import use_case.login.LoginUserDataAccessInterface;
import use_case.logout.LogoutUserDataAccessInterface;
import use_case.signup.SignupUserDataAccessInterface;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * Firebase Firestore implementation of the user data access object.
 */
public class FirebaseUserDataAccessObject implements SignupUserDataAccessInterface,
        LoginUserDataAccessInterface,
        ChangePasswordUserDataAccessInterface,
        LogoutUserDataAccessInterface {
    
    private final Firestore db;
    private final UserFactory userFactory;
    private String currentUsername;
    private static final String COLLECTION_NAME = "users";

    public FirebaseUserDataAccessObject(UserFactory userFactory) {
        this.userFactory = userFactory;
        this.currentUsername = null;
        
        // Initialize Firebase using the configuration helper
        this.db = FirebaseConfig.getFirestore();
    }

    @Override
    public User get(String username) {
        try {
            DocumentReference docRef = db.collection(COLLECTION_NAME).document(username);
            ApiFuture<DocumentSnapshot> future = docRef.get();
            DocumentSnapshot document = future.get();
            
            if (document.exists()) {
                String name = document.getString("username");
                String password = document.getString("password");
                return userFactory.create(name, password);
            } else {
                throw new RuntimeException("User not found: " + username);
            }
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Error retrieving user: " + e.getMessage(), e);
        }
    }

    @Override
    public void setCurrentUsername(String name) {
        this.currentUsername = name;
    }

    @Override
    public boolean existsByName(String username) {
        try {
            DocumentReference docRef = db.collection(COLLECTION_NAME).document(username);
            ApiFuture<DocumentSnapshot> future = docRef.get();
            DocumentSnapshot document = future.get();
            return document.exists();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Error checking if user exists: " + e.getMessage(), e);
        }
    }

    @Override
    public void save(User user) {
        try {
            Map<String, Object> userData = new HashMap<>();
            userData.put("username", user.getName());
            userData.put("password", user.getPassword());
            userData.put("createdAt", System.currentTimeMillis());
            
            DocumentReference docRef = db.collection(COLLECTION_NAME).document(user.getName());
            ApiFuture<WriteResult> future = docRef.set(userData);
            future.get(); // Wait for the write to complete
            
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Error saving user: " + e.getMessage(), e);
        }
    }

    @Override
    public void changePassword(User user) {
        try {
            Map<String, Object> updates = new HashMap<>();
            updates.put("password", user.getPassword());
            updates.put("updatedAt", System.currentTimeMillis());
            
            DocumentReference docRef = db.collection(COLLECTION_NAME).document(user.getName());
            ApiFuture<WriteResult> future = docRef.update(updates);
            future.get(); // Wait for the update to complete
            
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Error updating password: " + e.getMessage(), e);
        }
    }

    @Override
    public String getCurrentUsername() {
        return currentUsername;
    }
} 