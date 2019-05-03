package Pfruit.osbbyx.P.passionfruit.loginModule.model.dataAccess;

import android.support.annotation.NonNull;

import Pfruit.osbbyx.P.passionfruit.common.model.dataAccess.FirebaseAuthenticationAPI;
import Pfruit.osbbyx.P.passionfruit.common.pojo.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class Authentication {
    private FirebaseAuthenticationAPI mAuthenticationAPI;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    public Authentication() {
        mAuthenticationAPI = FirebaseAuthenticationAPI.getInstance();
    }

    public void onResume(){
        mAuthenticationAPI.getmFirebaseAuth().addAuthStateListener(mAuthStateListener);
    }

    public void onPause(){
        if (mAuthStateListener != null){
            mAuthenticationAPI.getmFirebaseAuth().removeAuthStateListener(mAuthStateListener);
        }
    }

    public void getStatusAuth(final StatusAuthCallback callback){
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null){
                    callback.onGetUser(user);
                } else {
                    callback.onLauchUILogin();
                }
            }
        };
    }

    public User getCurrentUser(){
        return mAuthenticationAPI.getAuthUser();
    }
}
