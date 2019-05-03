package Pfruit.osbbyx.P.passionfruit.loginModule.view;

import android.content.Intent;


public interface LoginView {
     void showProgress();
     void hideProgress();

     void openMainActivity();
     void openUILogin();

     void showLoginSuccessfully(Intent data);
     void showMessageStarting();
     void showError(int resMsg);
}
