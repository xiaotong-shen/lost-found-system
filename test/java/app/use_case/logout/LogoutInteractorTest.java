package app.use_case.logout;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import use_case.logout.*;

/**
 * Unit test for LogoutInteractor.
 */
class LogoutInteractorTest {

    @Test
    void testLogoutSuccess() {
        FakeDAO fakeDAO = new FakeDAO();
        FakePresenter fakePresenter = new FakePresenter();

        fakeDAO.setCurrentUsername("test_user");

        LogoutInteractor interactor = new LogoutInteractor(fakeDAO, fakePresenter);
        LogoutInputData inputData = new LogoutInputData("test_user");

        interactor.execute(inputData);

        assertTrue(fakePresenter.successCalled);
        assertEquals("test_user", fakePresenter.lastOutput.getUsername());
        assertFalse(fakePresenter.lastOutput.isUseCaseFailed());
        assertNull(fakeDAO.getCurrentUsername());
    }

    // ========== Fake classes ==========

    private static class FakeDAO implements LogoutUserDataAccessInterface {
        private String currentUsername;

        @Override
        public void setCurrentUsername(String username) {
            this.currentUsername = username;
        }

        @Override
        public String getCurrentUsername() {
            return currentUsername;
        }
    }

    private static class FakePresenter implements LogoutOutputBoundary {
        boolean successCalled = false;
        boolean failCalled = false;
        LogoutOutputData lastOutput;

        @Override
        public void prepareSuccessView(LogoutOutputData outputData) {
            successCalled = true;
            lastOutput = outputData;
        }

        @Override
        public void prepareFailView(String errorMessage) {
            failCalled = true;
            lastOutput = new LogoutOutputData(errorMessage, true);
        }
    }
}
