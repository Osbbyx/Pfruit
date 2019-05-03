package Pfruit.osbbyx.P.passionfruit.mainModule.model.dataAccess;

import Pfruit.osbbyx.P.passionfruit.common.model.dataAccess.FirebaseAuthenticationAPI;


public class Authentication {
    private FirebaseAuthenticationAPI mAuthenticationAPI;

    public Authentication() {
        mAuthenticationAPI = FirebaseAuthenticationAPI.getInstance();
    }

    public FirebaseAuthenticationAPI getmAuthenticationAPI() {
        return mAuthenticationAPI;
    }

    public void signOff(){
        mAuthenticationAPI.getmFirebaseAuth().signOut();
    }
}
