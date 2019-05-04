package Pfruit.osbbyx.P.passionfruit.chatModule.view;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import Pfruit.osbbyx.P.passionfruit.R;
import Pfruit.osbbyx.P.passionfruit.chatModule.ChatPresenter;
import Pfruit.osbbyx.P.passionfruit.chatModule.ChatPresenterClass;
import Pfruit.osbbyx.P.passionfruit.chatModule.view.adapters.ChatAdapter;
import Pfruit.osbbyx.P.passionfruit.chatModule.view.adapters.OnItemClickListener;
import Pfruit.osbbyx.P.passionfruit.common.Constants;
import Pfruit.osbbyx.P.passionfruit.common.pojo.Message;
import Pfruit.osbbyx.P.passionfruit.common.pojo.User;
import Pfruit.osbbyx.P.passionfruit.common.utils.UtilsCommon;
import Pfruit.osbbyx.P.passionfruit.common.utils.UtilsImage;
import Pfruit.osbbyx.P.passionfruit.common.utils.UtilsNetwork;
import Pfruit.osbbyx.P.passionfruit.mainModule.view.MainActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity implements OnItemClickListener, ChatView,
        OnImageZoom {

    @BindView(R.id.imgPhoto)
    CircleImageView imgPhoto;
    @BindView(R.id.tvName)
    TextView tvName;
    @BindView(R.id.tvStatus)
    TextView tvStatus;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.contentMain)
    CoordinatorLayout contentMain;
    @BindView(R.id.etMessage)
    AppCompatEditText etMessage;
    private ChatAdapter mAdapter;
    private ChatPresenter mPresenter;
    private Message messageSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);

        mPresenter = new ChatPresenterClass(this);
        mPresenter.onCreate();

        configAdapter();
        configRecyclerView();
        configToolbar(getIntent());
    }

    private void configAdapter() {
        mAdapter = new ChatAdapter(new ArrayList<Message>(), this);
    }

    private void configRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mAdapter);
    }

    private void configToolbar(Intent data) {
        String uid = data.getStringExtra(User.UID);
        String email = data.getStringExtra(User.EMAIL);
        mPresenter.setupFriend(uid, email);

        String photoUrl = data.getStringExtra(User.PHOTO_URL);
        tvName.setText(data.getStringExtra(User.USERNAME));
        tvStatus.setVisibility(View.VISIBLE);

        RequestOptions options = new RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .placeholder(R.drawable.ic_emoticon_happy)
                .centerCrop();
        Glide.with(this)
                .asBitmap()
                .load(photoUrl)
                .apply(options)
                .listener(new RequestListener<Bitmap>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                        imgPhoto.setImageDrawable(ContextCompat.getDrawable(ChatActivity.this,
                                R.drawable.ic_emoticon_sad));
                        return true;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                        imgPhoto.setImageBitmap(resource);
                        return true;
                    }
                })
                .into(imgPhoto);

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (UtilsNetwork.isOnline(this)) {
            mPresenter.onResume();
        } else {
            UtilsCommon.showSnackbar(contentMain, R.string.common_message_noInternet,
                    Snackbar.LENGTH_LONG);
        }
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, MainActivity.class));
        if (UtilsCommon.hasMaterialDesign()) {
            finishAfterTransition();
        } else {
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mPresenter.result(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            switch (requestCode) {
                case Constants.RP_STORAGE:
                    fromGallery();
                    break;
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void fromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, Constants.RC_PHOTO_PICKER);
    }

    private void checkPermissionToApp(String permissionStr, int requestPermission) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, permissionStr) !=
                    PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{permissionStr}, requestPermission);
                return;
            }
        }

        switch (requestPermission) {
            case Constants.RP_STORAGE:
                fromGallery();
                break;
        }
    }

    /*
     *   OnItemClick
     * */
    @Override
    public void onImageLoaded() {
        recyclerView.scrollToPosition(mAdapter.getItemCount() - 1);
    }

    @Override
    public void onClickImage(Message message) {
        new ImageZoomFragment().show(getSupportFragmentManager(), getString(R.string.app_name));
        messageSelected = message;
    }

    @Override
    public Message getMessageSelected() {
        return this.messageSelected;
    }

    /*
     *   ChatView
     * */
    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onStatusUser(boolean connected, long lastConnection) {
        if (connected) {
            tvStatus.setText(R.string.chat_status_connected);
        } else {
            tvStatus.setText(getString(R.string.chat_status_last_connection, (new SimpleDateFormat(
                    "dd-MM-yyyy - HH:mm", Locale.ROOT).format(new Date(lastConnection)))));
        }
    }

    @Override
    public void onError(int resMsg) {
        UtilsCommon.showSnackbar(contentMain, resMsg);
    }

    @Override
    public void onMessageReceived(Message msg) {
        mAdapter.add(msg);
        recyclerView.scrollToPosition(mAdapter.getItemCount() - 1);
    }

    @Override
    public void openDialogPreview(Intent data) {
        final String urlLocal = data.getDataString();

        final ViewGroup nullParent = null;
        View view = getLayoutInflater().inflate(R.layout.dialog_image_upload_preview, nullParent);
        final ImageView imgDialog = view.findViewById(R.id.imgDialog);
        final TextView tvMessage = view.findViewById(R.id.tvMessage);

        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.DialogFragmentTheme)
                .setTitle(R.string.chat_dialog_sendImage_title)
                .setPositiveButton(R.string.chat_dialog_sendImage_accept, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mPresenter.sendImage(ChatActivity.this, Uri.parse(urlLocal));
                    }
                })
                .setNeutralButton(R.string.common_label_cancel, null);
        builder.setView(view);

        AlertDialog alertDialog = builder.create();
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                int sizeImagePreview = getResources().getDimensionPixelSize(R.dimen.chat_size_img_preview);
                Bitmap bitmap = UtilsImage.reduceBitmap(ChatActivity.this, contentMain,
                        urlLocal, sizeImagePreview, sizeImagePreview);
                if (bitmap != null) {
                    imgDialog.setImageBitmap(bitmap);
                }
                tvMessage.setText(String.format(Locale.ROOT,
                        getString(R.string.chat_dialog_sendImage_message), tvName.getText()));
            }
        });
        alertDialog.show();
    }

    /*
     *   Click Events
     * */
    @OnClick(R.id.btnSendMessage)
    public void sendMessage() {
        if (UtilsCommon.validateMessage(etMessage)) {
            mPresenter.sendMessage(etMessage.getText().toString().trim());
            etMessage.setText("");
        }
    }

    @OnClick(R.id.btnGallery)
    public void onGallery() {
        checkPermissionToApp(Manifest.permission.READ_EXTERNAL_STORAGE, Constants.RP_STORAGE);
    }
}
