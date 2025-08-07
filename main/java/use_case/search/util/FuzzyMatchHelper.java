package use_case.search.util;

import entity.Post;
import java.util.ArrayList;
import java.util.List;

public class FuzzyMatchHelper {

    public static List<Post> fuzzyMatchPosts(List<Post> posts, String query) {
        List<Post> results = new ArrayList<>();
        String lowerQuery = query.toLowerCase();

        for (Post post : posts) {
            boolean matched = false;

            if (post.getTitle() != null && post.getTitle().toLowerCase().contains(lowerQuery)) {
                matched = true;
            } else if (post.getDescription() != null && post.getDescription().toLowerCase().contains(lowerQuery)) {
                matched = true;
            } else if (post.getLocation() != null && post.getLocation().toLowerCase().contains(lowerQuery)) {
                matched = true;
            } else if (post.getTags() != null) {
                for (String tag : post.getTags()) {
                    if (tag != null && tag.toLowerCase().contains(lowerQuery)) {
                        matched = true;
                        break;
                    }
                }
            }

            if (matched) {
                results.add(post);
            }
        }

        return results;
    }
}
