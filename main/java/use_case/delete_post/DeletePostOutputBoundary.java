package use_case.delete_post;

/**
 * Output boundary for the admin use case.
 */
public interface DeletePostOutputBoundary {
    /**
     * Prepares the view for successful admin operations.
     * @param deletePostOutputData the output data containing results
     */
    void prepareSuccessView(DeletePostOutputData deletePostOutputData);

    /**
     * Prepares the view for failed admin operations.
     * @param deletePostOutputData the output data containing error information
     */
    void prepareFailView(DeletePostOutputData deletePostOutputData);
}