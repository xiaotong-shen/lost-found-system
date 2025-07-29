package data_access;

import com.google.firebase.database.*;
import entity.Post;
import use_case.dashboard.DashboardUserDataAccessInterface;
import use_case.search.SearchUserDataAccessInterface;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.ExecutionException;

/**
 * Firebase implementation of data access for posts.
 */
public class FirebasePostDataAccessObject implements 
        DashboardUserDataAccessInterface, 
        SearchUserDataAccessInterface {
    
    private final DatabaseReference postsRef;
    private final DateTimeFormatter dateFormatter;
    
    public FirebasePostDataAccessObject() {
        this.postsRef = FirebaseConfig.getDatabase().getReference("posts");
        this.dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    }
    
    @Override
    public List<Post> getAllPosts() {
        System.out.println("\n=== DEBUG: getAllPosts() called ===");
        CompletableFuture<List<Post>> future = new CompletableFuture<>();
        
        System.out.println("DEBUG: Setting up Firebase listener...");
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
        // Generate new post ID
        String postId = postsRef.push().getKey();
        
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
    public List<Post> searchPostsByCriteria(String title, String location, List<String> tags, Boolean isLost) {
        System.out.println("\n=== DEBUG: searchPostsByCriteria() called ===");
        System.out.println("DEBUG: Search criteria:");
        System.out.println("  - Title: '" + title + "'");
        System.out.println("  - Location: '" + location + "'");
        System.out.println("  - Tags: " + (tags != null ? tags.toString() : "null"));
        System.out.println("  - IsLost: " + isLost);
        
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

            if (title != null && !title.isEmpty()) {
                boolean titleMatches = post.getTitle().toLowerCase().contains(title.toLowerCase());
                System.out.println("DEBUG:   Title check - Query: '" + title + "' vs Post: '" + post.getTitle() + "' -> " + titleMatches);
                if (!titleMatches) {
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
} 