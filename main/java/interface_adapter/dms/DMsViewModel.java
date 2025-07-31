package interface_adapter.dms;

import interface_adapter.ViewModel;
import entity.Chat;
import entity.Message;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.List;

/**
 * ViewModel for the DMs view.
 */
public class DMsViewModel extends ViewModel {
    public static final String TITLE_LABEL = "Direct Messages";
    public static final String NEW_DM_BUTTON_LABEL = "New DM";
    public static final String SEND_BUTTON_LABEL = "Send";
    public static final String BACK_BUTTON_LABEL = "Back";
    public static final String USERNAME_LABEL = "Username:";
    public static final String MESSAGE_LABEL = "Message:";

    private DMsState state = new DMsState();

    public DMsViewModel() {
        super("dms");
    }

    public void setState(DMsState state) {
        this.state = state;
    }

    private final PropertyChangeSupport support = new PropertyChangeSupport(this);

    public void firePropertyChanged() {
        support.firePropertyChange("state", null, this.state);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    public DMsState getState() {
        return state;
    }

    public void setChats(List<Chat> chats) {
        state.setChats(chats);
        firePropertyChanged();
    }

    public void setMessages(List<Message> messages) {
        state.setMessages(messages);
        firePropertyChanged();
    }

    public void setCurrentChat(Chat chat) {
        state.setCurrentChat(chat);
        firePropertyChanged();
    }

    public void setCurrentUsername(String username) {
        state.setCurrentUsername(username);
        firePropertyChanged();
    }

    public void setError(String error) {
        state.setError(error);
        firePropertyChanged();
    }

    public void clearError() {
        state.setError(null);
        firePropertyChanged();
    }
}