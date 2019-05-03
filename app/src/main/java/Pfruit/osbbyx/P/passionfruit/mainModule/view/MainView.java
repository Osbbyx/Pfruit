package Pfruit.osbbyx.P.passionfruit.mainModule.view;

import Pfruit.osbbyx.P.passionfruit.common.pojo.User;


public interface MainView {
    void friendAdded(User user);
    void friendUpdated(User user);
    void friendRemoved(User user);

    void requestAdded(User user);
    void requestUpdated(User user);
    void requestRemoved(User user);

    void showRequestAccepted(String username);
    void showRequestDenied();

    void showFriendRemoved();

    void showError(int resMsg);
}
