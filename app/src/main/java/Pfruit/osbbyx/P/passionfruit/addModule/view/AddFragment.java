package Pfruit.osbbyx.P.passionfruit.addModule.view;


import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import Pfruit.osbbyx.P.passionfruit.addModule.AddPresenter;
import Pfruit.osbbyx.P.passionfruit.addModule.AddPresenterClass;
import Pfruit.osbbyx.P.passionfruit.R;
import Pfruit.osbbyx.P.passionfruit.common.utils.UtilsCommon;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddFragment extends DialogFragment implements DialogInterface.OnShowListener, AddView {


    @BindView(R.id.etEmail)
    TextInputEditText etEmail;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.contentMain)
    FrameLayout contentMain;

    private Button positivButton;

    private AddPresenter mPresenter;

    Unbinder unbinder;

    public AddFragment() {
        // Required empty public constructor
        mPresenter = new AddPresenterClass(this);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setTitle(R.string.addFriend_title)
                .setPositiveButton(R.string.common_label_accept, null)
                .setNeutralButton(R.string.common_label_cancel, null);

        View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_add, null);
        builder.setView(view);
        unbinder = ButterKnife.bind(this, view);

        AlertDialog dialog = builder.create();
        dialog.setOnShowListener(this);
        return dialog;
    }

    @Override
    public void onShow(DialogInterface dialogInterface) {
        final AlertDialog dialog = (AlertDialog)getDialog();
        if (dialog != null){
            positivButton = dialog.getButton(Dialog.BUTTON_POSITIVE);
            Button negativeButton = dialog.getButton(Dialog.BUTTON_NEGATIVE);

            positivButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (UtilsCommon.validateEmail(getActivity(), etEmail)) {
                        mPresenter.addFriend(etEmail.getText().toString().trim());
                    }
                }
            });

            negativeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
        }
        mPresenter.onShow();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.onDestroy();
    }

    @Override
    public void enableUIElements() {
        etEmail.setEnabled(true);
        positivButton.setEnabled(true);
    }

    @Override
    public void disableUIElements() {
        etEmail.setEnabled(false);
        positivButton.setEnabled(false);
    }

    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void friendAdded() {
        Toast.makeText(getActivity(), R.string.addFriend_message_request_dispatched, Toast.LENGTH_SHORT)
                .show();
        dismiss();
    }

    @Override
    public void friendNotAdded() {
        etEmail.setError(getString(R.string.addFriend_error_message));
        etEmail.requestFocus();
    }
}
