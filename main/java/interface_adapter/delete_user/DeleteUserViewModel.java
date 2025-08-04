package interface_adapter.delete_user;

import interface_adapter.ViewModel;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class DeleteUserViewModel extends ViewModel {
    private DeleteUserState state = new DeleteUserState();

    public DeleteUserViewModel() {
        super("delete users");
    }

    public void setState(DeleteUserState state) {
        this.state = state;
        firePropertyChanged();
    }

    public DeleteUserState getState() {
        return state;
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
}