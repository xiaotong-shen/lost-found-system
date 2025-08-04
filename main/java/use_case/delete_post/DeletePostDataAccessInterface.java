package use_case.delete_post;

import entity.Post;

import java.util.List;

/**
 * Interface for data access operations related to admin functionality.
 */
public interface DeletePostDataAccessInterface {
    void deletePost(String postId);
    boolean existsPost(String postId);
}