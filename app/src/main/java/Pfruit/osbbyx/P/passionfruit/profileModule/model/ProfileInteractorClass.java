package Pfruit.osbbyx.P.passionfruit.profileModule.model;

import android.app.Activity;
import android.net.Uri;

import Pfruit.osbbyx.P.passionfruit.common.model.EventErrorTypeListener;
import Pfruit.osbbyx.P.passionfruit.common.model.StorageUploadImageCallback;
import Pfruit.osbbyx.P.passionfruit.common.pojo.User;
import Pfruit.osbbyx.P.passionfruit.profileModule.events.ProfileEvent;
import Pfruit.osbbyx.P.passionfruit.profileModule.model.dataAccess.Authentication;
import Pfruit.osbbyx.P.passionfruit.profileModule.model.dataAccess.RealtimeDatabase;
import Pfruit.osbbyx.P.passionfruit.profileModule.model.dataAccess.Storage;
import Pfruit.osbbyx.P.passionfruit.profileModule.model.dataAccess.UpdateUserListener;

import org.greenrobot.eventbus.EventBus;


public class ProfileInteractorClass implements ProfileInteractor {
    private Authentication mAuthentication;
    private RealtimeDatabase mDatabase;
    private Storage mStorage;

    private User mMyUser;

    public ProfileInteractorClass() {
        mAuthentication = new Authentication();
        mDatabase = new RealtimeDatabase();
        mStorage = new Storage();
    }

    private User getCurrentUser(){
        if (mMyUser == null){
            mMyUser = mAuthentication.getmAuthenticationAPI().getAuthUser();
        }
        return mMyUser;
    }

    @Override
    public void updateUsername(String username) {
        final User myUser = getCurrentUser();
        myUser.setUsername(username);
        mDatabase.changeUsername(myUser, new UpdateUserListener() {
            @Override
            public void onSuccess() {
                mAuthentication.updateUsernameFirebaseProfile(myUser, new EventErrorTypeListener() {
                    @Override
                    public void onError(int typeEvent, int resMsg) {
                        post(typeEvent, null, resMsg);
                    }
                });
            }

            @Override
            public void onNotifyContacts() {
                postUsernameSuccess();
            }

            @Override
            public void onError(int resMsg) {
                post(ProfileEvent.ERROR_USERNAME, null, resMsg);
            }
        });
    }

    @Override
    public void updateImage(Activity activity, Uri uri, final String oldPhotoUrl) {
        mStorage.uploadImageProfile(activity, uri, getCurrentUser().getEmail(), new StorageUploadImageCallback() {
            @Override
            public void onSuccess(Uri uri) {
                mDatabase.updatePhotoUrl(uri, getCurrentUser().getUid(), new StorageUploadImageCallback() {
                    @Override
                    public void onSuccess(Uri newUri) {
                        post(ProfileEvent.UPLOAD_IMAGE, newUri.toString(), 0);
                    }

                    @Override
                    public void onError(int resMsg) {
                        post(ProfileEvent.ERROR_SERVER, resMsg);
                    }
                });

                mAuthentication.updateImageFirebaseProfile(uri, new StorageUploadImageCallback() {
                    @Override
                    public void onSuccess(Uri newUri) {
                        mStorage.deleteOldImage(oldPhotoUrl, newUri.toString());
                    }

                    @Override
                    public void onError(int resMsg) {
                        post(ProfileEvent.ERROR_PROFILE, resMsg);
                    }
                });
            }

            @Override
            public void onError(int resMsg) {
                post(ProfileEvent.ERROR_IMAGE, resMsg);
            }
        });
    }

    private void post(int typeEvent, int resMsg) {
        post(typeEvent, null, resMsg);
    }

    private void postUsernameSuccess() {
        post(ProfileEvent.SAVE_USERNAME, null, 0);
    }

    private void post(int typeEvent, String photoUrl, int resMsg) {
        ProfileEvent event = new ProfileEvent();
        event.setPhotoUrl(photoUrl);
        event.setResMsg(resMsg);
        event.setTypeEvent(typeEvent);
        EventBus.getDefault().post(event);
    }
}
