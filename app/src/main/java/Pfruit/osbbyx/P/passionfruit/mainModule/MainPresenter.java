package Pfruit.osbbyx.P.passionfruit.mainModule;

import Pfruit.osbbyx.P.passionfruit.common.pojo.User;
import Pfruit.osbbyx.P.passionfruit.mainModule.events.MainEvent;


public interface MainPresenter {
    void onCreate();
    void onDestroy();
    void onPause();
    void onResume();

    void signOff();
    User getCurrentUser();
    void removeFriend(String friendUid);

    void acceptRequest(User user);
    void denyRequest(User user);

    void onEventListener(MainEvent event);
}
