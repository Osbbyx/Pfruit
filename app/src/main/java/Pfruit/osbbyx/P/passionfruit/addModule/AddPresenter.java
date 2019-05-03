package Pfruit.osbbyx.P.passionfruit.addModule;

import Pfruit.osbbyx.P.passionfruit.addModule.events.AddEvent;


public interface AddPresenter {
    void onShow();
    void onDestroy();

    void addFriend(String email);
    void onEventListener(AddEvent event);
}
