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

    @Override
    public List<Post> searchPosts(String query) {
        List<Post> allPosts = getAllPosts();
        List<Post> matchingPosts = new ArrayList<>();

        String lowerQuery = query.toLowerCase();

        for (Post post : allPosts) {
            if (post.getTitle().toLowerCase().contains(lowerQuery) ||
                    post.getDescription().toLowerCase().contains(lowerQuery) ||
                    post.getLocation().toLowerCase().contains(lowerQuery)) {
                matchingPosts.add(post);
                continue;
            }

            for (String tag : post.getTags()) {
                if (tag.toLowerCase().contains(lowerQuery)) {
                    matchingPosts.add(post);
                    break;
                }
            }
        }

        return matchingPosts;
    }

    @Override
    public List<Post> searchPostsByCriteria(String title, String location, List<String> tags, Boolean isLost) {
        List<Post> allPosts = getAllPosts();
        List<Post> matchingPosts = new ArrayList<>();

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

        return matchingPosts;
    }

    @Override
    public List<Post> fuzzySearch(String query) {
        throw new UnsupportedOperationException("Fuzzy search not supported in APIPostDataAccessObject");
    }

    private List<Post> parsePostsFromJSON(String jsonResponse) throws JSONException {
        List<Post> posts = new ArrayList<>();
        JSONArray jsonArray = new JSONArray(jsonResponse);

        String[] lostItems = {"wallet", "keys", "phone", "laptop", "airpods", "water bottle", "backpack", "glasses"};
        String[] foundItems = {"wallet", "keys", "phone", "laptop", "airpods", "water bottle", "backpack", "glasses"};
        String[] locations = {"Library", "Science Wing", "Engineering Building", "Student Center", "Cafeteria", "Gym", "Parking Lot"};
        String[] authors = {"alex123", "sam456", "jamie789", "jim101", "adem202", "franz303"};

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonPost = jsonArray.getJSONObject(i);

            int postId = jsonPost.getInt("id");
            String title = jsonPost.getString("title");
            String description = jsonPost.getString("body");

            boolean isLost = postId % 2 == 0;
            String itemType = isLost ? lostItems[postId % lostItems.length] : foundItems[postId % foundItems.length];
            String location = locations[postId % locations.length];
            String author = authors[postId % authors.length];

            List<String> tags = Arrays.asList(itemType, location.toLowerCase(), isLost ? "lost" : "found");

            LocalDateTime timestamp = LocalDateTime.now().minusHours(postId * 2);

            Map<Integer, String> reactions = new HashMap<>();
            if (postId % 3 == 0) reactions.put(1, "like");
            if (postId % 5 == 0) reactions.put(2, "helpful");

            Post post = new Post(postId,
                    "Lost: " + itemType + " in " + location,
                    description,
                    tags,
                    timestamp,
                    author,
                    location,
                    null,
                    isLost,
                    reactions.size(),
                    reactions);

            posts.add(post);
        }

        return posts;
    }

    @Override
    public Post getPostById(int postID) {
        List<Post> allPosts = getAllPosts();
        for (Post post : allPosts) {
            if (post.getPostID() == postID) return post;
        }
        return null;
    }

    @Override
    public Post addPost(String title, String content, List<String> tags, String location, boolean isLost, String author) {
        int newPostID = getAllPosts().size() + 1;
        return new Post(newPostID, title, content, tags, LocalDateTime.now(), author, location, null, isLost, 0, new HashMap<>());
    }
}
