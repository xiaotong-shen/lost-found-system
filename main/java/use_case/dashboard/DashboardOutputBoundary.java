package use_case.dashboard;

/**
 * Output boundary for the dashboard use case.
 */
public interface DashboardOutputBoundary {
    /**
     * Prepares the view for successful dashboard operations.
     * @param dashboardOutputData the output data containing results
     */
    void prepareSuccessView(DashboardOutputData dashboardOutputData);

    /**
     * Prepares the view for failed dashboard operations.
     * @param dashboardOutputData the output data containing error information
     */
    void prepareFailView(DashboardOutputData dashboardOutputData);
} 