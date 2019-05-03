package Pfruit.osbbyx.P.passionfruit.chatModule.view;

import android.content.Intent;

import Pfruit.osbbyx.P.passionfruit.common.pojo.Message;


public interface ChatView {
    void showProgress();
    void hideProgress();

    void onStatusUser(boolean connected, long lastConnection);

    void onError(int resMsg);

    void onMessageReceived(Message msg);

    void openDialogPreview(Intent data);
}
