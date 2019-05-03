package Pfruit.osbbyx.P.passionfruit.profileModule.model;

import android.app.Activity;
import android.net.Uri;


public interface ProfileInteractor {
    void updateUsername(String username);
    void updateImage(Activity activity, Uri uri, String oldPhotoUrl);
}
