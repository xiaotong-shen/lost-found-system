package interface_adapter.adminloggedIn;

import interface_adapter.ViewModel;
import interface_adapter.change_password.LoggedInState;

public class AdminLoggedInViewModel extends ViewModel<AdminLoggedInState> {

    public AdminLoggedInViewModel() {
        super("admin logged in");
        setState(new AdminLoggedInState());
    }

}