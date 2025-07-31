package interface_adapter.admin;

import interface_adapter.ViewModel;

/**
 * The ViewModel for the Dashboard View.
 */
public class AdminViewModel extends ViewModel<AdminState> {

    public AdminViewModel() {
        super("admin");
        this.setState(new AdminState());
    }

    @Override
    public void firePropertyChanged() {
        super.firePropertyChanged();
    }

    @Override
    public void firePropertyChanged(String propertyName) {
        super.firePropertyChanged(propertyName);
    }
}
