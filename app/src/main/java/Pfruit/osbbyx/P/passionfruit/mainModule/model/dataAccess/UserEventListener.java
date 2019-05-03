package Pfruit.osbbyx.P.passionfruit.mainModule.model.dataAccess;

import Pfruit.osbbyx.P.passionfruit.common.pojo.User;

public interface UserEventListener {
    void onUserAdded(User user);
    void onUserUpdated(User user);
    void onUserRemoved(User user);

    void onError(int resMsg);
}
