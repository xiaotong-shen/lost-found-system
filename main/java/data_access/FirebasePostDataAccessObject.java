package data_access;

import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.database.*;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import entity.Comment;
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
        System.out.println("\n=== DEBUG: getAllPosts() called ===");
        CompletableFuture<List<Post>> future = new CompletableFuture<>();
        postsRef.orderByChild("timestamp").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                System.out.println("DEBUG: Firebase onDataChange called");
                List<Post> posts = new ArrayList<>();
                int postCount = 0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Post post = snapshot.getValue(Post.class);
                    if (post != null) {
                        posts.add(post);
                        postCount++;
                    } else {
                        System.out.println("DEBUG: Warning - null post found in snapshot");
                    }
                }
                System.out.println("DEBUG: Retrieved " + postCount + " posts from Firebase");
                future.complete(posts);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("DEBUG: Firebase onCancelled called with error: " + databaseError.getMessage());
                future.completeExceptionally(new RuntimeException("Failed to load posts: " + databaseError.getMessage()));
            }
        });
        try {
            System.out.println("DEBUG: Waiting for Firebase response (timeout: 5 seconds)...");
            List<Post> result = future.get(5, TimeUnit.SECONDS);
            System.out.println("DEBUG: getAllPosts() returning " + result.size() + " posts");
            return result;
        } catch (InterruptedException e) {
            System.out.println("DEBUG: InterruptedException in getAllPosts(): " + e.getMessage());
            e.printStackTrace();
            System.err.println("Error fetching posts: " + e.getMessage());
            return new ArrayList<>();
        } catch (ExecutionException e) {
            System.out.println("DEBUG: ExecutionException in getAllPosts(): " + e.getMessage());
            System.out.println("DEBUG: ExecutionException cause: " + (e.getCause() != null ? e.getCause().getClass().getSimpleName() : "null"));
            e.printStackTrace();
            System.err.println("Error fetching posts: " + e.getMessage());
            return new ArrayList<>();
        } catch (TimeoutException e) {
            System.out.println("DEBUG: TimeoutException in getAllPosts(): " + e.getMessage());
            e.printStackTrace();
            System.err.println("Error fetching posts: " + e.getMessage());
            return new ArrayList<>();
        } catch (Exception e) {
            System.out.println("DEBUG: Unexpected Exception in getAllPosts(): " + e.getClass().getSimpleName() + " - " + e.getMessage());
            e.printStackTrace();
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
            // Search only in title and content (description) for now — tag search will be added later
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
        String postId = postsRef.push().getKey();
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
        System.out.println("\n=== DEBUG: searchPostsByCriteria() called ===");
        System.out.println("DEBUG: Search criteria:");
        System.out.println("  - Title: '" + title + "'");
        System.out.println("  - Location: '" + location + "'");
        System.out.println("  - Tags: " + (tags != null ? tags.toString() : "null"));
        System.out.println("  - IsLost: " + isLost);
        
        // SESSION CHANGE: If all criteria are blank, return all posts sorted alphabetically by title
        List<Post> allPosts = getAllPosts();
        System.out.println("DEBUG: Retrieved " + allPosts.size() + " total posts from database");
        
        List<Post> matchingPosts = new ArrayList<>();

        boolean allBlank = (title == null || title.isEmpty()) &&
                (location == null || location.isEmpty()) &&
                (tags == null || tags.isEmpty()) &&
                (isLost == null);

        System.out.println("DEBUG: All criteria blank? " + allBlank);

        if (allBlank) {
            // Return all posts sorted alphabetically by title
            System.out.println("DEBUG: All criteria blank, returning all posts sorted by title");
            allPosts.sort(Comparator.comparing(Post::getTitle, String.CASE_INSENSITIVE_ORDER));
            return allPosts;
        }

        System.out.println("DEBUG: Starting to filter posts...");
        int postIndex = 0;
        for (Post post : allPosts) {
            postIndex++;
            System.out.println("\nDEBUG: Checking post " + postIndex + ": '" + post.getTitle() + "'");
            boolean matches = true;

                    matches = false;
                }
            }

            if (location != null && !location.isEmpty()) {
                boolean locationMatches = post.getLocation().toLowerCase().contains(location.toLowerCase());
                System.out.println("DEBUG:   Location check - Query: '" + location + "' vs Post: '" + post.getLocation() + "' -> " + locationMatches);
                if (!locationMatches) {
                    matches = false;
                }
            }

            if (tags != null && !tags.isEmpty()) {
                System.out.println("DEBUG:   Tags check - Query tags: " + tags + " vs Post tags: " + post.getTags());
                boolean hasMatchingTag = false;
                List<String> postTags = post.getTags();
                if (postTags != null && !postTags.isEmpty()) {
                    for (String searchTag : tags) {
                        for (String postTag : postTags) {
                            if (postTag.toLowerCase().contains(searchTag.toLowerCase())) {
                                hasMatchingTag = true;
                                System.out.println("DEBUG:     Found matching tag: '" + searchTag + "' in post tag: '" + postTag + "'");
                                break;
                            }
                        }
                        if (hasMatchingTag) break;
                    }
                } else {
                    System.out.println("DEBUG:     Post has no tags to check against");
                }
                if (!hasMatchingTag) {
                    System.out.println("DEBUG:     No matching tags found");
                    matches = false;
                }
            }

            if (isLost != null) {
                boolean lostStatusMatches = post.isLost() == isLost;
                System.out.println("DEBUG:   Lost status check - Query: " + isLost + " vs Post: " + post.isLost() + " -> " + lostStatusMatches);
                if (!lostStatusMatches) {
                    matches = false;
                }
            }

            System.out.println("DEBUG:   Final match result: " + matches);
            if (matches) {
                matchingPosts.add(post);
                System.out.println("DEBUG:   ✓ Post added to results");
            } else {
                System.out.println("DEBUG:   ✗ Post excluded from results");
            }
        }

        System.out.println("\nDEBUG: Search completed. Found " + matchingPosts.size() + " matching posts out of " + allPosts.size() + " total posts");

        // Always sort the result alphabetically by title
        matchingPosts.sort(Comparator.comparing(Post::getTitle, String.CASE_INSENSITIVE_ORDER));
        System.out.println("DEBUG: Results sorted alphabetically by title");
        
        System.out.println("DEBUG: Returning " + matchingPosts.size() + " posts");
        return matchingPosts;
    }

