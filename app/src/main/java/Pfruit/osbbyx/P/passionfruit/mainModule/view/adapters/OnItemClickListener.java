package Pfruit.osbbyx.P.passionfruit.mainModule.view.adapters;

import Pfruit.osbbyx.P.passionfruit.common.pojo.User;


public interface OnItemClickListener {
    void onItemClick(User user);
    void onItemLongClick(User user);

    void onAcceptRequest(User user);
    void onDenyRequest(User user);
}
