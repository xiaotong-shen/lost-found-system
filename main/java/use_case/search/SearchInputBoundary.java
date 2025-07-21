package use_case.search;

/**
 * Input boundary for the search use case.
 */
public interface SearchInputBoundary {
    /**
     * Executes the search use case.
     * @param searchInputData the input data for the search
     */
    void execute(SearchInputData searchInputData);
} 