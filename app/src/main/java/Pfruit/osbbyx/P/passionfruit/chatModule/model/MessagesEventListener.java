package Pfruit.osbbyx.P.passionfruit.chatModule.model;

import Pfruit.osbbyx.P.passionfruit.common.pojo.Message;


public interface MessagesEventListener {
    void onMessageReceived(Message message);
    void onError(int resMsg);
}
