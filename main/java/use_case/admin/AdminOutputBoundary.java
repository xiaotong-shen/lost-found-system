package use_case.admin;

/**
 * Output boundary for the admin use case.
 */
public interface AdminOutputBoundary {
    /**
     * Prepares the view for successful admin operations.
     * @param adminOutputData the output data containing results
     */
    void prepareSuccessView(AdminOutputData adminOutputData);

    /**
     * Prepares the view for failed admin operations.
     * @param adminOutputData the output data containing error information
     */
    void prepareFailView(AdminOutputData adminOutputData);
}