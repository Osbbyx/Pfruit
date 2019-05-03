package Pfruit.osbbyx.P.passionfruit.mainModule.model;

import Pfruit.osbbyx.P.passionfruit.common.pojo.User;


public interface MainInteractor {
    void subscribeToUserList();
    void unsubscribeToUserList();

    void signOff();

    User getCurrentUser();
    void removeFriend(String friendUid);

    void acceptRequest(User user);
    void denyRequest(User user);
}
