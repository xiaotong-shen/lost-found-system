package data_access;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOptions;

import java.io.FileInputStream;
import java.io.IOException;

/**
 * Firebase configuration helper class.
 */
public class FirebaseConfig {
    
    private static Firestore firestore;
    
    /**
     * Initialize Firebase with service account credentials.
     * @param serviceAccountPath Path to your Firebase service account JSON file
     * @param projectId Your Firebase project ID
     * @return Initialized Firestore instance
     */
    public static Firestore initializeFirestore(String serviceAccountPath, String projectId) {
        if (firestore == null) {
            try {
                GoogleCredentials credentials = GoogleCredentials.fromStream(
                    new FileInputStream(serviceAccountPath)
                );
                
                FirestoreOptions firestoreOptions = FirestoreOptions.getDefaultInstance().toBuilder()
                        .setProjectId(projectId)
                        .setCredentials(credentials)
                        .build();
                
                firestore = firestoreOptions.getService();
                System.out.println("Firebase Firestore initialized successfully!");
                
            } catch (IOException e) {
                throw new RuntimeException("Failed to initialize Firebase: " + e.getMessage(), e);
            }
        }
        return firestore;
    }
    
    /**
     * Initialize Firebase with default credentials (for development).
     * @param projectId Your Firebase project ID
     * @return Initialized Firestore instance
     */
    public static Firestore initializeFirestoreDefault(String projectId) {
        if (firestore == null) {
            try {
                FirestoreOptions firestoreOptions = FirestoreOptions.getDefaultInstance().toBuilder()
                        .setProjectId(projectId)
                        .build();
                
                firestore = firestoreOptions.getService();
                System.out.println("Firebase Firestore initialized with default credentials!");
                
            } catch (Exception e) {
                throw new RuntimeException("Failed to initialize Firebase: " + e.getMessage(), e);
            }
        }
        return firestore;
    }
    
    /**
     * Get the current Firestore instance.
     * @return Firestore instance
     */
    public static Firestore getFirestore() {
        if (firestore == null) {
            throw new RuntimeException("Firebase not initialized. Call initializeFirestore() first.");
        }
        return firestore;
    }
} 