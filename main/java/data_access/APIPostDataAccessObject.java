package data_access;

import entity.Post;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Data access object for fetching posts from an external API.
 * This implementation connects to a REST API to retrieve post data.
 */
public class APIPostDataAccessObject implements use_case.search.SearchUserDataAccessInterface,
                                               use_case.dashboard.DashboardUserDataAccessInterface {
    private static final String BASE_URL = "https://jsonplaceholder.typicode.com";
    private static final String POSTS_ENDPOINT = "/posts";
    private static final String CONTENT_TYPE_JSON = "application/json";
    
    private final OkHttpClient client;
    private final DateTimeFormatter dateFormatter;

    public APIPostDataAccessObject() {
        this.client = new OkHttpClient().newBuilder().build();
        this.dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    }

    /**
     * Fetches all posts from the API.
     * @return List of posts
     */
    @Override
    public List<Post> getAllPosts() {
        try {
            Request request = new Request.Builder()
                    .url(BASE_URL + POSTS_ENDPOINT)
                    .addHeader("Content-Type", CONTENT_TYPE_JSON)
                    .build();

            Response response = client.newCall(request).execute();
            String responseBody = response.body().string();
            
            return parsePostsFromJSON(responseBody);
        } catch (IOException | JSONException e) {
            System.err.println("Error fetching posts from API: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Searches posts by query string (searches in title, body, and tags).
     * @param query the search query
     * @return List of matching posts
     */
    @Override
    public List<Post> searchPosts(String query) {
        List<Post> allPosts = getAllPosts();
        List<Post> matchingPosts = new ArrayList<>();
        
        String lowerQuery = query.toLowerCase();
        
        for (Post post : allPosts) {
            // Search in title
            if (post.getTitle().toLowerCase().contains(lowerQuery)) {
                matchingPosts.add(post);
                continue;
            }
            
            // Search in description
            if (post.getDescription().toLowerCase().contains(lowerQuery)) {
                matchingPosts.add(post);
                continue;
            }
            
            // Search in tags
            for (String tag : post.getTags()) {
                if (tag.toLowerCase().contains(lowerQuery)) {
                    matchingPosts.add(post);
                    break;
                }
            }
            
            // Search in location
            if (post.getLocation().toLowerCase().contains(lowerQuery)) {
                matchingPosts.add(post);
            }
        }
        
        return matchingPosts;
    }

    /**
     * Searches posts by specific criteria.
     * @param title title to search for
     * @param location location to search for
     * @param tags tags to search for
     * @param isLost whether to search for lost or found items
     * @return List of matching posts
     */
    public List<Post> searchPostsByCriteria(String title, String location, List<String> tags, Boolean isLost) {
        List<Post> allPosts = getAllPosts();
        List<Post> matchingPosts = new ArrayList<>();
        
        for (Post post : allPosts) {
            boolean matches = true;
            
            // Check title
            if (title != null && !title.isEmpty() && 
                !post.getTitle().toLowerCase().contains(title.toLowerCase())) {
                matches = false;
            }
            
            // Check location
            if (location != null && !location.isEmpty() && 
                !post.getLocation().toLowerCase().contains(location.toLowerCase())) {
                matches = false;
            }
            
            // Check tags
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
            
            // Check lost/found status
            if (isLost != null && post.isLost() != isLost) {
                matches = false;
            }
            
            if (matches) {
                matchingPosts.add(post);
            }
        }
        
        return matchingPosts;
    }

    /**
     * Parses JSON response from API into Post objects.
     * Since we're using a mock API, we'll create realistic lost/found data.
     */
    private List<Post> parsePostsFromJSON(String jsonResponse) throws JSONException {
        List<Post> posts = new ArrayList<>();
        JSONArray jsonArray = new JSONArray(jsonResponse);
        
        // Sample lost and found data to make it more realistic
        String[] lostItems = {"wallet", "keys", "phone", "laptop", "airpods", "water bottle", "backpack", "glasses"};
        String[] foundItems = {"wallet", "keys", "phone", "laptop", "airpods", "water bottle", "backpack", "glasses"};
        String[] locations = {"Library", "Science Wing", "Engineering Building", "Student Center", "Cafeteria", "Gym", "Parking Lot"};
        String[] authors = {"alex123", "sam456", "jamie789", "jim101", "adem202", "franz303"};
        
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonPost = jsonArray.getJSONObject(i);
            
            int postId = jsonPost.getInt("id");
            String title = jsonPost.getString("title");
            String description = jsonPost.getString("body");
            
            // Create realistic lost/found data
            boolean isLost = postId % 2 == 0; // Alternate between lost and found
            String itemType = isLost ? lostItems[postId % lostItems.length] : foundItems[postId % foundItems.length];
            String location = locations[postId % locations.length];
            String author = authors[postId % authors.length];
            
            // Create tags based on the item
            List<String> tags = Arrays.asList(itemType, location.toLowerCase(), isLost ? "lost" : "found");
            
            // Create timestamp (recent posts)
            LocalDateTime timestamp = LocalDateTime.now().minusHours(postId * 2);
            
            // Create reactions (mock data)
            Map<Integer, String> reactions = new HashMap<>();
            if (postId % 3 == 0) {
                reactions.put(1, "like");
            }
            if (postId % 5 == 0) {
                reactions.put(2, "helpful");
            }
            
            Post post = new Post(postId, 
                               "Lost: " + itemType + " in " + location, 
                               description, 
                               tags, 
                               timestamp, 
                               author, 
                               location, 
                               null, // no image URL for now
                               isLost, 
                               reactions.size(), 
                               reactions);
            
            posts.add(post);
        }
        
        return posts;
    }


    /**
     * Fetches all posts from the API.
     * @return List of posts
     */
    private List<Post> fetchAllPostsFromAPI() {
        try {
            Request request = new Request.Builder()
                    .url(BASE_URL + POSTS_ENDPOINT)
                    .addHeader("Content-Type", CONTENT_TYPE_JSON)
                    .build();

            Response response = client.newCall(request).execute();
            String responseBody = response.body().string();
            
            return parsePostsFromJSON(responseBody);
        } catch (IOException | JSONException e) {
            System.err.println("Error fetching posts from API: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public Post getPostById(int postID) {
        List<Post> allPosts = getAllPosts();
        for (Post post : allPosts) {
            if (post.getPostID() == postID) {
                return post;
            }
        }
        return null;
    }

    @Override
    public Post addPost(String title, String content, List<String> tags, String location, boolean isLost, String author) {
        // In a real implementation, this would make an API call to create a post
        // For now, we'll create a mock post
        List<Post> allPosts = getAllPosts();
        int newPostID = allPosts.size() + 1;
        
        Post newPost = new Post(
            newPostID,
            title,
            content,
            tags != null ? tags : new ArrayList<>(),
            java.time.LocalDateTime.now(),
            author,
            location,
            null, // no image URL for now
            isLost,
            0, // no likes initially
            new HashMap<>() // no reactions initially
        );
        
        return newPost;
    }
    
    @Override
    public boolean updatePost(Post post) {
        // In a real implementation, this would make an API call to update a post
        // For now, we'll return true to simulate success
        System.out.println("DEBUG: APIPostDataAccessObject.updatePost() called for post ID: " + post.getPostID());
        return true;
    }
    
    @Override
    public boolean deletePost(int postId) {
        // In a real implementation, this would make an API call to delete a post
        // For now, we'll return true to simulate success
        System.out.println("DEBUG: APIPostDataAccessObject.deletePost() called for post ID: " + postId);
        return true;
    }
} 