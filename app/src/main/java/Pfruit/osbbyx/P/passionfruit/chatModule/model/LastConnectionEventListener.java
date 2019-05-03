package Pfruit.osbbyx.P.passionfruit.chatModule.model;


public interface LastConnectionEventListener {
    void onSucces(boolean online, long lastConnection, String uidConnectedFriend);
}
