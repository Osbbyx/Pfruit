package Pfruit.osbbyx.P.passionfruit.loginModule.model.dataAccess;

import com.google.firebase.auth.FirebaseUser;


public interface StatusAuthCallback {
    void onGetUser(FirebaseUser user);
    void onLauchUILogin();
}
