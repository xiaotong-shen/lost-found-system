package use_case.search;

/**
 * Output boundary for the search use case.
 */
public interface SearchOutputBoundary {
    /**
     * Prepares the view for successful search results.
     * @param searchOutputData the output data containing search results
     */
    void prepareSuccessView(SearchOutputData searchOutputData);

    /**
     * Prepares the view for failed search.
     * @param searchOutputData the output data containing error information
     */
    void prepareFailView(SearchOutputData searchOutputData);
} 