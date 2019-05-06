package Pfruit.osbbyx.P.passionfruit.mainModule.view;

import android.app.ActivityOptions;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import Pfruit.osbbyx.P.passionfruit.addModule.view.AddFragment;
import Pfruit.osbbyx.P.passionfruit.R;
import Pfruit.osbbyx.P.passionfruit.chatModule.view.ChatActivity;
import Pfruit.osbbyx.P.passionfruit.common.pojo.User;
import Pfruit.osbbyx.P.passionfruit.common.utils.UtilsCommon;
import Pfruit.osbbyx.P.passionfruit.loginModule.view.LoginActivity;
import Pfruit.osbbyx.P.passionfruit.mainModule.MainPresenter;
import Pfruit.osbbyx.P.passionfruit.mainModule.MainPresenterClass;
import Pfruit.osbbyx.P.passionfruit.mainModule.view.adapters.OnItemClickListener;
import Pfruit.osbbyx.P.passionfruit.mainModule.view.adapters.RequestAdapter;
import Pfruit.osbbyx.P.passionfruit.mainModule.view.adapters.UserAdapter;
import Pfruit.osbbyx.P.passionfruit.profileModule.view.ProfileActivity;

import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseView;

public class MainActivity extends AppCompatActivity implements OnItemClickListener, MainView {

    private static final int RC_PROFILE = 23;

    @BindView(R.id.imgProfile)
    CircleImageView imgProfile;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.rvRequests)
    RecyclerView rvRequests;
    @BindView(R.id.rvUsers)
    RecyclerView rvUsers;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.contentMain)
    CoordinatorLayout contentMain;

    private UserAdapter mUserAdapter;
    private RequestAdapter mRequestAdapter;

    private MainPresenter mPresenter;

    private User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mPresenter = new MainPresenterClass(this);
        mPresenter.onCreate();
        mUser = mPresenter.getCurrentUser();

        configToolbar();
        configAdapter();
        configRecyclerView();
        configTutorial();
    }

    private void configToolbar() {
        toolbar.setTitle(mUser.getUsernameValid());
        UtilsCommon.loadImage(this, mUser.getPhotoUrl(), imgProfile);
        setSupportActionBar(toolbar);
    }

    private void configAdapter() {
        mUserAdapter = new UserAdapter(new ArrayList<User>(), this);
        mRequestAdapter = new RequestAdapter(new ArrayList<User>(), this);
    }

    private void configRecyclerView() {
        rvUsers.setLayoutManager(new LinearLayoutManager(this));
        rvUsers.setAdapter(mUserAdapter);

        rvRequests.setLayoutManager(new LinearLayoutManager(this));
        rvRequests.setAdapter(mRequestAdapter);
    }

    private void configTutorial(){
        new MaterialShowcaseView.Builder(this)
                .setTitleTextColor(ContextCompat.getColor(this, R.color.colorAccent))
                .setContentTextColor(ContextCompat.getColor(this, R.color.blue_a100))
                .setDismissTextColor(ContextCompat.getColor(this, android.R.color.white))
                .setMaskColour(ContextCompat.getColor(this, R.color.gray_900_t))
                .setTarget(fab)
                .setTargetTouchable(true)
                .setTitleText(R.string.app_name)
                .setContentText(R.string.main_tuto_message)
                .setDismissText(R.string.main_tuto_ok)
                .setDismissStyle(Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD_ITALIC))
                .singleUse(getString(R.string.main_tuto_fabAdd))
                .setDelay(2000)
                .setFadeDuration(600)
                .setDismissOnTargetTouch(true)
                .setDismissOnTouch(false)
                .show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                mPresenter.signOff();
                Intent intent = new Intent(this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                        | Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                break;
            case R.id.action_profile:
                Intent intentProfile = new Intent(this, ProfileActivity.class);
                intentProfile.putExtra(User.USERNAME, mUser.getUsername());
                intentProfile.putExtra(User.EMAIL, mUser.getEmail());
                intentProfile.putExtra(User.PHOTO_URL, mUser.getPhotoUrl());

                if (UtilsCommon.hasMaterialDesign()){
                    startActivityForResult(intentProfile, RC_PROFILE,
                            ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
                } else {
                    startActivityForResult(intentProfile, RC_PROFILE);
                }
                break;
            case R.id.action_about:
                openAbout();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            switch (requestCode){
                case RC_PROFILE:
                    if (data != null){
                        mUser.setUsername(data.getStringExtra(User.USERNAME));
                        mUser.setPhotoUrl(data.getStringExtra(User.PHOTO_URL));
                        configToolbar();
                    }
                    break;
            }
        }
    }

    private void openAbout() {
        LayoutInflater inflater = (LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dialog_about, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this, R.style.DialogFragmentTheme)
                .setTitle(R.string.main_menu_about)
                .setView(view)
                .setPositiveButton(R.string.common_label_ok, null)
                .setNeutralButton(R.string.about_privacy, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Intent.ACTION_VIEW,
                                Uri.parse("https://sites.google.com/view/secrethood/privacy-policy"));
                        startActivity(intent);
                    }
                });
        builder.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.onResume();

        clearNotifications();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPresenter.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.onDestroy();
    }

    private void clearNotifications() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            notificationManager.cancelAll();
        }
    }

    @OnClick(R.id.fab)
    public void onAddClicked() {
        new AddFragment().show(getSupportFragmentManager(), getString(R.string.addFriend_title));
    }

    /*
     *       MainView
     * */
    @Override
    public void friendAdded(User user) {
        mUserAdapter.add(user);
    }

    @Override
    public void friendUpdated(User user) {
        mUserAdapter.update(user);
    }

    @Override
    public void friendRemoved(User user) {
        mUserAdapter.remove(user);
    }

    @Override
    public void requestAdded(User user) {
        mRequestAdapter.add(user);
    }

    @Override
    public void requestUpdated(User user) {
        mRequestAdapter.update(user);
    }

    @Override
    public void requestRemoved(User user) {
        mRequestAdapter.remove(user);
    }

    @Override
    public void showRequestAccepted(String username) {
        Snackbar.make(contentMain, getString(R.string.main_message_request_accepted, username),
                Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void showRequestDenied() {
        Snackbar.make(contentMain, R.string.main_message_request_denied, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void showFriendRemoved() {
        Snackbar.make(contentMain, R.string.main_message_user_removed, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showError(int resMsg) {
        Snackbar.make(contentMain, resMsg, Snackbar.LENGTH_LONG).show();
    }

    /*
     *       OnItemClickListener
     * */
    @Override
    public void onItemClick(User user) {
        Intent intent = new Intent(this, ChatActivity.class);
        intent.putExtra(User.UID, user.getUid());
        intent.putExtra(User.USERNAME, user.getUsername());
        intent.putExtra(User.EMAIL, user.getEmail());
        intent.putExtra(User.PHOTO_URL, user.getPhotoUrl());

        if (UtilsCommon.hasMaterialDesign()) {
            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
        } else {
            startActivity(intent);
        }
    }

    @Override
    public void onItemLongClick(final User user) {
        new AlertDialog.Builder(this, R.style.DialogFragmentTheme)
                .setTitle(getString(R.string.main_dialog_title_confirmDelete))
                .setMessage(String.format(Locale.ROOT, getString(R.string.main_dialog_message_confirmDelete),
                        user.getUsernameValid()))
                .setPositiveButton(R.string.main_dialog_accept, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mPresenter.removeFriend(user.getUid());
                    }
                })
                .setNegativeButton(R.string.common_label_cancel, null)
                .show();
    }

    @Override
    public void onAcceptRequest(User user) {
        mPresenter.acceptRequest(user);
    }

    @Override
    public void onDenyRequest(User user) {
        mPresenter.denyRequest(user);
    }
}
