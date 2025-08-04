package use_case.admin;

/**
 * Input boundary for the admin use case.
 */
public interface AdminInputBoundary {
    /**
     * Executes the admin use case.
     * @param adminInputData the input data for the admin
     */
    void execute(AdminInputData adminInputData);
}
