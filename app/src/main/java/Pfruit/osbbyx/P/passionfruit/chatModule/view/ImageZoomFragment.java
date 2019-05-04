package Pfruit.osbbyx.P.passionfruit.chatModule.view;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import Pfruit.osbbyx.P.passionfruit.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.github.chrisbanes.photoview.PhotoView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class ImageZoomFragment extends DialogFragment implements DialogInterface.OnShowListener {

    @BindView(R.id.pvZoom)
    PhotoView pvZoom;
    @BindView(R.id.contentMain)
    RelativeLayout contentMain;
    Unbinder unbinder;

    public ImageZoomFragment() {
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);

        View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_image_zoom,
                null, false);
        unbinder = ButterKnife.bind(this, view);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.DialogFragmentTheme_FullScreen)
                .setTitle(R.string.app_name)
                .setPositiveButton(R.string.common_label_ok, null);
        builder.setView(view);
        AlertDialog dialog = builder.create();
        dialog.setOnShowListener(this);

        return dialog;
    }

    @Override
    public void onShow(DialogInterface dialog) {
        if (getActivity() != null){
            RequestOptions options = new RequestOptions()
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .placeholder(R.drawable.ic_timer_sand_160);

            Glide.with(getActivity())
                    .load(((OnImageZoom)getActivity()).getMessageSelected().getPhotoUrl())
                    .apply(options)
                    .into(pvZoom);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        if (window != null){
            window.setLayout(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
            window.setGravity(Gravity.CENTER);
        }
        contentMain.setGravity(Gravity.CENTER);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
