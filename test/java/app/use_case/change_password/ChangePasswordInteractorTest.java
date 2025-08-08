package app.use_case.change_password;

import entity.User;
import entity.UserFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import use_case.change_password.*;

import static org.junit.jupiter.api.Assertions.*;

class ChangePasswordInteractorTest {

    private boolean changePasswordCalled;
    private boolean presenterCalled;
    private String receivedUsername;

    class FakeUser implements User {
        private final String name;
        private final String password;

        public FakeUser(String name, String password) {
            this.name = name;
            this.password = password;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public String getPassword() {
            return password;
        }
    }


    class FakeUserFactory implements UserFactory {
        @Override
        public User create(String name, String password) {
            return new FakeUser(name, password);
        }
    }

    class FakeDAO implements ChangePasswordUserDataAccessInterface {
        @Override
        public void changePassword(User user) {
            changePasswordCalled = true;
            receivedUsername = user.getName();
        }
    }

    class FakePresenter implements ChangePasswordOutputBoundary {
        @Override
        public void prepareSuccessView(ChangePasswordOutputData outputData) {
            presenterCalled = true;
            assertEquals("testuser", outputData.getUsername());
            assertFalse(outputData.isUseCaseFailed());
        }

        @Override
        public void prepareFailView(String errorMessage) {
            fail("Should not be called in success case.");
        }
    }

    @BeforeEach
    void resetFlags() {
        changePasswordCalled = false;
        presenterCalled = false;
        receivedUsername = null;
    }

    @Test
    void testExecute_CallsAllDependenciesCorrectly() {
        // Arrange
        ChangePasswordInteractor interactor = new ChangePasswordInteractor(
                new FakeDAO(),
                new FakePresenter(),
                new FakeUserFactory()
        );

        ChangePasswordInputData input = new ChangePasswordInputData("testpass", "testuser");

        // Act
        interactor.execute(input);

        // Assert
        assertTrue(changePasswordCalled, "DAO should be called.");
        assertTrue(presenterCalled, "Presenter should be called.");
        assertEquals("testuser", receivedUsername);
    }
}
