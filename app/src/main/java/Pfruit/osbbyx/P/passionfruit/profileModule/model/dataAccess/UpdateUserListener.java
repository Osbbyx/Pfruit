package Pfruit.osbbyx.P.passionfruit.profileModule.model.dataAccess;


public interface UpdateUserListener {
    void onSuccess();
    void onNotifyContacts();
    void onError(int resMsg);
}
