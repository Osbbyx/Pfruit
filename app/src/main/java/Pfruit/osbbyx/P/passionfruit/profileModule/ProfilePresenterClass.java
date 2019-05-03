package Pfruit.osbbyx.P.passionfruit.profileModule;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

import Pfruit.osbbyx.P.passionfruit.common.pojo.User;
import Pfruit.osbbyx.P.passionfruit.profileModule.events.ProfileEvent;
import Pfruit.osbbyx.P.passionfruit.profileModule.model.ProfileInteractor;
import Pfruit.osbbyx.P.passionfruit.profileModule.model.ProfileInteractorClass;
import Pfruit.osbbyx.P.passionfruit.profileModule.view.ProfileActivity;
import Pfruit.osbbyx.P.passionfruit.profileModule.view.ProfileView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;


public class ProfilePresenterClass implements ProfilePresenter {
    private ProfileView mView;
    private ProfileInteractor mInteractor;

    private boolean isEdit = false;
    private User mUser;

    public ProfilePresenterClass(ProfileView mView) {
        this.mView = mView;
        this.mInteractor = new ProfileInteractorClass();
    }

    @Override
    public void onCreate() {
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        mView = null;
    }

    @Override
    public void setupUser(String username, String email, String photoUrl) {
        mUser = new User();
        mUser.setUsername(username);
        mUser.setEmail(email);
        mUser.setPhotoUrl(photoUrl);

        mView.showUserData(username, email, photoUrl);
    }

    @Override
    public void checkMode() {
        if (isEdit){
            mView.launchGallery();
        }
    }

    @Override
    public void updateUsername(String username) {
        if (isEdit){
            if (setProgress()){
                mView.showProgress();
                mInteractor.updateUsername(username);
                mUser.setUsername(username);
            }
        } else {
            isEdit = true;
            mView.menuEditMode();
            mView.enableUIElements();
        }
    }

    @Override
    public void updateImage(Activity activity, Uri uri) {
        if (setProgress()){
            mView.showProgressImage();
            mInteractor.updateImage(activity, uri, mUser.getPhotoUrl());
        }
    }

    @Override
    public void result(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK){
            switch (requestCode){
                case ProfileActivity.RC_PHOTO_PICKER: // TODO: 19/12/2018 move to Constants
                    mView.openDialogPreview(data);
                    break;
            }
        }
    }

    @Subscribe
    @Override
    public void onEventListener(ProfileEvent event) {
        if (mView != null){
            mView.hideProgress();

            switch (event.getTypeEvent()){
                case ProfileEvent.ERROR_USERNAME:
                    mView.enableUIElements();
                    mView.onError(event.getResMsg());
                    break;
                case ProfileEvent.ERROR_PROFILE:
                case ProfileEvent.ERROR_SERVER:
                case ProfileEvent.ERROR_IMAGE:
                    mView.enableUIElements();
                    mView.onErrorUpload(event.getResMsg());
                    break;
                case ProfileEvent.SAVE_USERNAME:
                    mView.saveUsernameSuccess();
                    saveSuccess();
                    break;
                case ProfileEvent.UPLOAD_IMAGE:
                    mView.updateImageSuccess(event.getPhotoUrl());
                    mUser.setPhotoUrl(event.getPhotoUrl());
                    saveSuccess();
                    break;
            }
        }
    }

    private void saveSuccess() {
        mView.menuNormalMode();
        mView.setResultOK(mUser.getUsername(), mUser.getPhotoUrl());
        isEdit = false;
    }

    private boolean setProgress() {
        if (mView != null){
            mView.disableUIElements();
            return true;
        }
        return false;
    }
}
