package use_case.dashboard;

/**
 * Input boundary for the dashboard use case.
 */
public interface DashboardInputBoundary {
    /**
     * Executes the dashboard use case.
     * @param dashboardInputData the input data for the dashboard
     */
    void execute(DashboardInputData dashboardInputData);
}
