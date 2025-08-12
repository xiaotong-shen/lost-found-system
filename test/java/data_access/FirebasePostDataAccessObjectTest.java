package data_access;

import com.google.firebase.database.*;
import entity.Post;
import entity.Comment;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class FirebasePostDataAccessObjectTest {

    private FirebasePostDataAccessObject dao;
    
    @Mock
    private DatabaseReference mockPostsRef;
    @Mock
    private FirebaseDatabase mockDatabase;
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(mockDatabase.getReference(anyString())).thenReturn(mockPostsRef);
        dao = new FirebasePostDataAccessObject();
        setPrivateField(dao, "postsRef", mockPostsRef);
        setPrivateField(dao, "database", mockDatabase);
    }

    @Test
    @DisplayName("getAllPosts - Database Error")
    void getAllPosts_DatabaseError() {
        // Arrange
        doAnswer(invocation -> {
            ValueEventListener listener = invocation.getArgument(0);
            listener.onCancelled(DatabaseError.fromException(new Exception("Database error")));
            return null;
        }).when(mockPostsRef).addListenerForSingleValueEvent(any(ValueEventListener.class));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> dao.getAllPosts());
    }

    @Test
    @DisplayName("getAllPosts - Success scenario")
    void getAllPosts_Success() {
        // Arrange
        DatabaseReference mockOrderByRef = mock(DatabaseReference.class);
        Query mockQuery = mock(Query.class);
        when(mockPostsRef.orderByChild("timestamp")).thenReturn(mockQuery);  // Using string literal instead of constant
        
        DataSnapshot mockSnapshot = mock(DataSnapshot.class);
        List<Post> expectedPosts = new ArrayList<>();
        Post post1 = new Post(
            1,                    // postId
            "Title1",            // title
            "Description1",      // description/content
            new ArrayList<>(),   // tags
            LocalDateTime.now(), // timestamp
            "Author1",          // author
            "Location1",        // location
            null,               // imageURL
            true,               // isLost
            0,                  // numberOfLikes
            new HashMap<>()     // reactions
        );

        expectedPosts.add(post1);
        
        when(mockSnapshot.exists()).thenReturn(true);
        when(mockSnapshot.getChildren()).thenReturn(Arrays.asList(mockSnapshot));
        when(mockSnapshot.getValue(Post.class)).thenReturn(expectedPosts.get(0));
        
        doAnswer(invocation -> {
            ValueEventListener listener = invocation.getArgument(0);
            listener.onDataChange(mockSnapshot);
            return null;
        }).when(mockQuery).addListenerForSingleValueEvent(any(ValueEventListener.class));
        
        // Act
        List<Post> actualPosts = dao.getAllPosts();
        
        // Assert
        assertFalse(actualPosts.isEmpty());
        assertEquals(expectedPosts.get(0).getTitle(), actualPosts.get(0).getTitle());
    }

    @Test
    @DisplayName("deletePost - Success")
    void deletePost_Success() {
        // Arrange
        String postId = "123";
        DatabaseReference mockOrderByRef = mock(DatabaseReference.class);
        Query mockQuery = mock(Query.class);
        Query mockEqualToQuery = mock(Query.class);

        // Mock the query chain
        when(mockPostsRef.orderByChild("postID")).thenReturn(mockQuery);
        when(mockQuery.equalTo(anyDouble())).thenReturn(mockEqualToQuery);

        // Mock data snapshot
        DataSnapshot mockSnapshot = mock(DataSnapshot.class);
        DataSnapshot mockChildSnapshot = mock(DataSnapshot.class);
        when(mockSnapshot.exists()).thenReturn(true);
        when(mockSnapshot.getChildrenCount()).thenReturn(1L);
        when(mockSnapshot.getChildren()).thenReturn(Collections.singletonList(mockChildSnapshot));
        when(mockChildSnapshot.getKey()).thenReturn("mockKey");

        // Mock database references
        DatabaseReference mockChildRef = mock(DatabaseReference.class);
        when(mockPostsRef.child(anyString())).thenReturn(mockChildRef);

        // Setup listener behavior
        doAnswer(invocation -> {
            ValueEventListener listener = invocation.getArgument(0);
            listener.onDataChange(mockSnapshot);
            return null;
        }).when(mockEqualToQuery).addListenerForSingleValueEvent(any(ValueEventListener.class));

        // Mock removal behavior
        doAnswer(invocation -> {
            DatabaseReference.CompletionListener listener = invocation.getArgument(0);
            listener.onComplete(null, mockChildRef);
            return null;
        }).when(mockChildRef).removeValue(any(DatabaseReference.CompletionListener.class));

        // Act & Assert
        assertDoesNotThrow(() -> dao.deletePost(postId));
    }

    @Test
    @DisplayName("addPost - Success")
    void addPost_Success() {
        // Arrange
        String title = "Test Title";
        String content = "Test Content";
        List<String> tags = Arrays.asList("tag1", "tag2");
        String location = "Test Location";
        boolean isLost = true;
        String author = "Test Author";

        // Mock for getAllPosts
        DatabaseReference mockOrderByRef = mock(DatabaseReference.class);
        when(mockPostsRef.orderByChild(anyString())).thenReturn(mockOrderByRef);

        // Mock the push operation
        DatabaseReference mockNewPostRef = mock(DatabaseReference.class);
        when(mockPostsRef.push()).thenReturn(mockNewPostRef);
        when(mockNewPostRef.getKey()).thenReturn("newPostId");

        // Mock the data snapshot for getAllPosts
        DataSnapshot mockSnapshot = mock(DataSnapshot.class);
        when(mockSnapshot.exists()).thenReturn(true);
        when(mockSnapshot.getChildren()).thenReturn(Collections.emptyList());

        // Setup listener behavior
        doAnswer(invocation -> {
            ValueEventListener listener = invocation.getArgument(0);
            listener.onDataChange(mockSnapshot);
            return null;
        }).when(mockOrderByRef).addListenerForSingleValueEvent(any(ValueEventListener.class));

        // Mock child and setValue behavior
        DatabaseReference mockChildRef = mock(DatabaseReference.class);
        when(mockPostsRef.child(anyString())).thenReturn(mockChildRef);
        doAnswer(invocation -> {
            DatabaseReference.CompletionListener listener = invocation.getArgument(1);
            listener.onComplete(null, mockChildRef);
            return mockChildRef;
        }).when(mockChildRef).setValue(any(), any(DatabaseReference.CompletionListener.class));

        // Act
        Post result = dao.addPost(title, content, tags, location, isLost, author);

        // Assert
        assertNotNull(result);
        assertEquals(title, result.getTitle());
        assertEquals(author, result.getAuthor());
        assertEquals(content, result.getDescription());
        assertEquals(location, result.getLocation());
        assertEquals(isLost, result.isLost());
        assertEquals(tags, result.getTags());
    }

    @Test
    @DisplayName("searchPostsByCriteria - Success")
    void searchPostsByCriteria_Success() {
        // Arrange
        String title = "Test";
        String location = "Location";
        List<String> tags = Arrays.asList("tag1");
        Boolean isLost = true;

        // Mock for getAllPosts
        DatabaseReference mockOrderByRef = mock(DatabaseReference.class);
        Query mockQuery = mock(Query.class);
        when(mockPostsRef.orderByChild("timestamp")).thenReturn(mockQuery);

        // Setup mock behavior for the query
        DataSnapshot mockSnapshot = mock(DataSnapshot.class);
        Post mockPost = new Post();
        mockPost.setTitle(title);
        mockPost.setLocation(location);
        mockPost.setTags(tags);
        mockPost.setLost(isLost);

        when(mockSnapshot.exists()).thenReturn(true);
        when(mockSnapshot.getChildren()).thenReturn(Arrays.asList(mockSnapshot));
        when(mockSnapshot.getValue(Post.class)).thenReturn(mockPost);

        doAnswer(invocation -> {
            ValueEventListener listener = invocation.getArgument(0);
            listener.onDataChange(mockSnapshot);
            return null;
        }).when(mockQuery).addListenerForSingleValueEvent(any(ValueEventListener.class));

        // Act
        List<Post> results = dao.searchPostsByCriteria(title, location, tags, isLost);

        // Assert
        assertFalse(results.isEmpty());
        assertEquals(mockPost.getTitle(), results.get(0).getTitle());
    }

    @Test
    @DisplayName("deletePost - Post Not Found")
    void deletePost_PostNotFound() {
        // Arrange
        String postId = "123";
        Query mockQuery = mock(Query.class);
        DatabaseReference mockOrderByRef = mock(DatabaseReference.class);
        when(mockPostsRef.orderByChild("postID")).thenReturn(mockQuery);
        when(mockQuery.equalTo(anyDouble())).thenReturn(mockQuery);

        // Mock empty snapshot
        DataSnapshot mockSnapshot = mock(DataSnapshot.class);
        when(mockSnapshot.exists()).thenReturn(false);
        when(mockSnapshot.getChildrenCount()).thenReturn(0L);

        doAnswer(invocation -> {
            ValueEventListener listener = invocation.getArgument(0);
            listener.onDataChange(mockSnapshot);
            return null;
        }).when(mockQuery).addListenerForSingleValueEvent(any(ValueEventListener.class));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> dao.deletePost(postId));
    }

    @Test
    @DisplayName("deletePost - Invalid ID Format")
    void deletePost_InvalidIdFormat() {
        // Arrange
        String invalidId = "invalid-id";

        // Act & Assert
        assertThrows(RuntimeException.class, () -> dao.deletePost(invalidId));
    }

    @Test
    @DisplayName("deletePost - Database Error")
    void deletePost_DatabaseError() {
        // Arrange
        String postId = "123";
        Query mockQuery = mock(Query.class);
        when(mockPostsRef.orderByChild(anyString())).thenReturn(mockQuery);
        when(mockQuery.equalTo(anyDouble())).thenReturn(mockQuery);

        // Create mock error with complete implementation
        DatabaseError mockError = mock(DatabaseError.class);
        when(mockError.getMessage()).thenReturn("Database error");
        when(mockError.getCode()).thenReturn(-1);
        when(mockError.getDetails()).thenReturn("Error details");
        when(mockError.toException()).thenReturn(new DatabaseException("Database error"));

        // Create CompletableFuture for synchronization
        CompletableFuture<Void> future = new CompletableFuture<>();

        // Setup listener behavior
        doAnswer(invocation -> {
            ValueEventListener listener = invocation.getArgument(0);
            listener.onCancelled(mockError);
            future.complete(null);
            return null;
        }).when(mockQuery).addListenerForSingleValueEvent(any(ValueEventListener.class));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            dao.deletePost(postId);
            future.get(5, TimeUnit.SECONDS); // Wait for async operation
        });
    }

    @Test
    @DisplayName("deletePost - Delete Operation Error")
    void deletePost_DeleteError() {
        // Arrange
        String postId = "123";
        Query mockQuery = mock(Query.class);
        when(mockPostsRef.orderByChild("postID")).thenReturn(mockQuery);
        when(mockQuery.equalTo(anyDouble())).thenReturn(mockQuery);

        // Mock snapshot with data
        DataSnapshot mockSnapshot = mock(DataSnapshot.class);
        DataSnapshot mockChildSnapshot = mock(DataSnapshot.class);
        when(mockSnapshot.exists()).thenReturn(true);
        when(mockSnapshot.getChildrenCount()).thenReturn(1L);
        when(mockSnapshot.getChildren()).thenReturn(Collections.singletonList(mockChildSnapshot));
        when(mockChildSnapshot.getKey()).thenReturn("mockKey");

        // Mock database references
        DatabaseReference mockChildRef = mock(DatabaseReference.class);
        when(mockPostsRef.child(anyString())).thenReturn(mockChildRef);

        // Setup listener behavior
        doAnswer(invocation -> {
            ValueEventListener listener = invocation.getArgument(0);
            listener.onDataChange(mockSnapshot);
            return null;
        }).when(mockQuery).addListenerForSingleValueEvent(any(ValueEventListener.class));

        // Mock removal error
        doAnswer(invocation -> {
            DatabaseReference.CompletionListener listener = invocation.getArgument(0);
            listener.onComplete(DatabaseError.fromException(new Exception("Delete error")), mockChildRef);
            return null;
        }).when(mockChildRef).removeValue(any(DatabaseReference.CompletionListener.class));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> dao.deletePost(postId));
    }

    @Test
    @DisplayName("deletePost - Timeout")
    void deletePost_Timeout() {
        // Arrange
        String postId = "123";
        Query mockQuery = mock(Query.class);
        when(mockPostsRef.orderByChild("postID")).thenReturn(mockQuery);
        when(mockQuery.equalTo(anyDouble())).thenReturn(mockQuery);

        // Setup listener that never calls back
        doAnswer(invocation -> {
            Thread.sleep(6000); // Longer than timeout
            return null;
        }).when(mockQuery).addListenerForSingleValueEvent(any(ValueEventListener.class));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> dao.deletePost(postId));
    }

    // Helper method to set private fields
    private void setPrivateField(Object target, String fieldName, Object value) {
        try {
            java.lang.reflect.Field field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
