package interface_adapter.dms;

import use_case.dms.DMsOutputBoundary;
import use_case.dms.DMsOutputData;
import entity.Chat;
import entity.Message;

import java.util.List;

/**
 * Presenter for the DMs use case.
 */
public class DMsPresenter implements DMsOutputBoundary {
    private final DMsViewModel dMsViewModel;

    public DMsPresenter(DMsViewModel dMsViewModel) {
        this.dMsViewModel = dMsViewModel;
    }

    @Override
    public void prepareLoadChatsView(DMsOutputData outputData) {
        DMsState dMsState = dMsViewModel.getState();
        dMsState.setChats(outputData.getChats());
        dMsState.setError(outputData.getError());
        dMsViewModel.setState(dMsState);
        dMsViewModel.firePropertyChanged();
    }

    @Override
    public void prepareLoadMessagesView(DMsOutputData outputData) {
        DMsState dMsState = dMsViewModel.getState();
        dMsState.setMessages(outputData.getMessages());
        dMsState.setCurrentChat(outputData.getCurrentChat());
        dMsState.setError(outputData.getError());
        dMsViewModel.setState(dMsState);
        dMsViewModel.firePropertyChanged();
    }

    @Override
    public void prepareSendMessageView(DMsOutputData outputData) {
        DMsState dMsState = dMsViewModel.getState();
        dMsState.setMessages(outputData.getMessages());
        dMsState.setError(outputData.getError());
        dMsViewModel.setState(dMsState);
        dMsViewModel.firePropertyChanged();
    }

    @Override
    public void prepareCreateChatView(DMsOutputData outputData) {
        DMsState dMsState = dMsViewModel.getState();
        dMsState.setCurrentChat(outputData.getCurrentChat());
        dMsState.setError(outputData.getError());
        dMsViewModel.setState(dMsState);
        dMsViewModel.firePropertyChanged();
    }
}