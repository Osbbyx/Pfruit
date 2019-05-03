package Pfruit.osbbyx.P.passionfruit.profileModule.view;

import android.content.Intent;


public interface ProfileView {
    void enableUIElements();
    void disableUIElements();

    void showProgress();
    void hideProgress();
    void showProgressImage();
    void hideProgressImage();

    void showUserData(String username, String email, String photoUrl);
    void launchGallery();
    void openDialogPreview(Intent data);

    void menuEditMode();
    void menuNormalMode();

    void saveUsernameSuccess();
    void updateImageSuccess(String photoUrl);
    void setResultOK(String username, String photoUrl);

    void onErrorUpload(int resMgs);
    void onError(int resMsg);
}
