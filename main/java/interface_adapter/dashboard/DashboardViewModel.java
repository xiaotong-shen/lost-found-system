package interface_adapter.dashboard;

import interface_adapter.ViewModel;

/**
 * The ViewModel for the Dashboard View.
 */
public class DashboardViewModel extends ViewModel<DashboardState> {

    public DashboardViewModel() {
        super("dashboard");
        this.setState(new DashboardState());
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
