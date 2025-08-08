package interface_adapter.user_profile;

import entity.Post;
import entity.User;
import interface_adapter.ViewModel;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.List;

/**
 * ViewModel for the user profile feature.
 */
public class UserProfileViewModel extends ViewModel {
    public static final String TITLE_LABEL = "User Profile";
    public static final String BACK_BUTTON_LABEL = "Back to Dashboard";

    private UserProfileState state = new UserProfileState();

    public UserProfileViewModel() {
        super("user_profile");
    }

    public void setState(UserProfileState state) {
        this.state = state;
    }

    private final PropertyChangeSupport support = new PropertyChangeSupport(this);

    @Override
    public void firePropertyChanged() {
        support.firePropertyChange("state", null, this.state);
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    public UserProfileState getState() {
        return state;
    }

    public void setUser(User user) {
        state.setUser(user);
    }

    public void setResolvedPosts(List<Post> resolvedPosts) {
        state.setResolvedPosts(resolvedPosts);
    }

    public void setError(String error) {
        state.setError(error);
    }

    public void setSuccessMessage(String successMessage) {
        state.setSuccessMessage(successMessage);
    }
}

