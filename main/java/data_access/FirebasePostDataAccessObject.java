package data_access;

import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.database.*;
import entity.Post;
import use_case.admin.AdminUserDataAccessInterface;
import use_case.dashboard.DashboardUserDataAccessInterface;
import use_case.delete_post.DeletePostDataAccessInterface;
import use_case.search.SearchUserDataAccessInterface;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Firebase implementation of data access for posts.
 */
public class FirebasePostDataAccessObject implements
        DashboardUserDataAccessInterface,
        SearchUserDataAccessInterface,
        AdminUserDataAccessInterface, DeletePostDataAccessInterface {

    private final DatabaseReference postsRef;
    private final DateTimeFormatter dateFormatter;
    private final FirebaseDatabase database;

    public FirebasePostDataAccessObject() {
        this.postsRef = FirebaseConfig.getDatabase().getReference("posts");
        this.dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        this.database = FirebaseConfig.getDatabase();
    }

    @Override
    public List<Post> getAllPosts() {
        CompletableFuture<List<Post>> future = new CompletableFuture<>();

        postsRef.orderByChild("timestamp").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Post> posts = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Post post = snapshot.getValue(Post.class);
                    if (post != null) {
                        posts.add(post);
                    }
                }
                future.complete(posts);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                future.completeExceptionally(new RuntimeException("Failed to load posts: " + databaseError.getMessage()));
            }
        });

        try {
            return future.get(5, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            System.err.println("Error fetching posts: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public List<Post> searchPosts(String query) {
        List<Post> allPosts = getAllPosts();
        List<Post> matchingPosts = new ArrayList<>();
        String lowerQuery = query.toLowerCase();

        for (Post post : allPosts) {
            // Search only in title and content (description) for now — tag search will be added later
            if (post.getTitle().toLowerCase().contains(lowerQuery) ||
                    post.getDescription().toLowerCase().contains(lowerQuery)) {
                matchingPosts.add(post);
            }
        }

        return matchingPosts;
    }

    @Override
    public Post getPostById(int postID) {
        CompletableFuture<Post> future = new CompletableFuture<>();

        postsRef.child(String.valueOf(postID)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Post post = dataSnapshot.getValue(Post.class);
                future.complete(post);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                future.completeExceptionally(new RuntimeException("Failed to load post: " + databaseError.getMessage()));
            }
        });

        try {
            return future.get(5, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            System.err.println("Error fetching post: " + e.getMessage());
            return null;
        }
    }

    @Override
    public Post addPost(String title, String content, List<String> tags, String location, boolean isLost, String author) {
        // Generate new post ID
        String postId = postsRef.push().getKey();
        System.out.println("Post ID: " + postId);
        System.out.println("Post ID: " + postId.hashCode());
        Post newPost = new Post(
                postId.hashCode(), // Use hash code instead of parsing
                title,
                content,
                tags != null ? tags : new ArrayList<>(),
                LocalDateTime.now(),
                author,
                location,
                null, // image URL
                isLost,
                0, // likes
                new HashMap<>() // reactions
        );

        // Save to Firebase
        postsRef.child(postId).setValue(newPost, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError != null) {
                    System.err.println("Error saving post: " + databaseError.getMessage());
                } else {
                    System.out.println("Post saved successfully!");
                }
            }
        });

        return newPost;
    }

    @Override
    public boolean editPost(String postId, String newTitle, String description,
                            String location, List<String> tags, boolean isLost) {
        CompletableFuture<Boolean> future = new CompletableFuture<>();
        int intPostId = Integer.parseInt(postId);
        // Query for the post with matching title
        postsRef.orderByChild("postID").equalTo(intPostId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Should only be one post with this title
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            Post existingPost = postSnapshot.getValue(Post.class);
                            if (existingPost != null) {
                                // Create updated post
                                Post updatedPost = new Post(
                                        existingPost.getPostID(),
                                        newTitle,
                                        description,
                                        tags != null ? tags : existingPost.getTags(),
                                        LocalDateTime.parse(existingPost.getTimestamp(), dateFormatter),
                                        existingPost.getAuthor(),
                                        location,
                                        existingPost.getImageURL(),
                                        isLost,
                                        existingPost.getNumberOfLikes(),
                                        existingPost.getReactions()
                                );

                                // Update in Firebase using the snapshot's key
                                postsRef.child(postSnapshot.getKey())
                                        .setValue(updatedPost, (databaseError, databaseReference) -> {
                                            if (databaseError != null) {
                                                System.err.println("Error updating post: " +
                                                        databaseError.getMessage());
                                                future.complete(false);
                                            } else {
                                                System.out.println("Post updated successfully!");
                                                future.complete(true);
                                            }
                                        });
                                return;  // Exit after finding and updating the post
                            }
                        }
                        // If we get here, no post was found
                        System.err.println("Post not found with postid: " + postId);
                        future.complete(false);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        System.err.println("Error fetching post for update: " +
                                databaseError.getMessage());
                        future.complete(false);
                    }
                });

        try {
            return future.get(5, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            System.err.println("Error during post update: " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<Post> searchPostsByCriteria(String title, String location, List<String> tags, Boolean isLost) {
        List<Post> allPosts = getAllPosts();
        List<Post> matchingPosts = new ArrayList<>();

        boolean allBlank = (title == null || title.isEmpty()) &&
                (location == null || location.isEmpty()) &&
                (tags == null || tags.isEmpty()) &&
                (isLost == null);

        if (allBlank) {
            // Return all posts sorted alphabetically by title
            allPosts.sort(Comparator.comparing(Post::getTitle, String.CASE_INSENSITIVE_ORDER));
            return allPosts;
        }

        for (Post post : allPosts) {
            boolean matches = true;

            if (title != null && !title.isEmpty() &&
                    !post.getTitle().toLowerCase().contains(title.toLowerCase())) {
                matches = false;
            }

            if (location != null && !location.isEmpty() &&
                    !post.getLocation().toLowerCase().contains(location.toLowerCase())) {
                matches = false;
            }

            if (tags != null && !tags.isEmpty()) {
                boolean hasMatchingTag = false;
                for (String searchTag : tags) {
                    for (String postTag : post.getTags()) {
                        if (postTag.toLowerCase().contains(searchTag.toLowerCase())) {
                            hasMatchingTag = true;
                            break;
                        }
                    }
                    if (hasMatchingTag) break;
                }
                if (!hasMatchingTag) {
                    matches = false;
                }
            }

            if (isLost != null && post.isLost() != isLost) {
                matches = false;
            }

            if (matches) {
                matchingPosts.add(post);
            }
        }

        // Always sort the result alphabetically by title
        matchingPosts.sort(Comparator.comparing(Post::getTitle, String.CASE_INSENSITIVE_ORDER));
        return matchingPosts;
    }

    @Override
    public void deletePost(String postId) {
        DatabaseReference postsRef = database.getReference("posts");
        DatabaseReference postRef = postsRef.child(postId);

        CountDownLatch latch = new CountDownLatch(1);
        postRef.removeValue((error, ref) -> {
            if (error != null) {
                throw new RuntimeException("Failed to delete post: " + error.getMessage());
            }
            latch.countDown();
        });

        try {
            latch.await(); // Wait for the operation to complete
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Operation interrupted while deleting post");
        }
    }


    @Override
    public boolean existsPost(String postId) {
        DatabaseReference postRef = database.getReference("posts").child(postId);
        CountDownLatch latch = new CountDownLatch(1);
        AtomicBoolean exists = new AtomicBoolean(false);

        postRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                exists.set(dataSnapshot.exists());
                latch.countDown();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                latch.countDown();
            }
        });

        try {
            latch.await(); // Wait for the operation to complete
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        }

        return exists.get();
    }
}
