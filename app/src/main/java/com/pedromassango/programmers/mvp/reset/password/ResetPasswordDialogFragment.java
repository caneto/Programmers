package com.pedromassango.programmers.mvp.reset.password;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.pedromassango.programmers.R;
import com.pedromassango.programmers.extras.Util;

/**
 * Created by Pedro Massango on 27/06/2017 at 01:39.
 */

public class ResetPasswordDialogFragment extends AppCompatDialogFragment implements Contract.View {

    private EditText edtEmail;
    private ProgressDialog progressDialog;
    private Presenter presenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        presenter = new Presenter(this);

        //Prepare the Dialog
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);

        setStyle(AppCompatDialogFragment.STYLE_NO_TITLE, R.style.AppTheme);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_reset_password, container, false);

        edtEmail = view.findViewById(R.id.edt_email);
        Button btnSend = view.findViewById(R.id.btn_reset_password);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                presenter.sendVerificationClicked();
            }
        });

        return view;
    }

    @Override
    public String getEmail() {

        return edtEmail.getText().toString();
    }

    @Override
    public void setEmailError(String emailError) {

        edtEmail.setError(emailError);
    }

    @Override
    public void clearCurrentEmailText() {

        edtEmail.setText("");
    }

    @Override
    public void showProgress(String message) {

        progressDialog.setMessage(message);
        progressDialog.show();
    }

    @Override
    public void dismissProgress() {

        progressDialog.dismiss();
    }

    @Override
    public void showDialogNextStep(String title, String message) {

        Util.showAlertDialog(getActivity(), title, message);
        dismiss();
    }

    @Override
    public void showError(String title, String error) {

        Util.showAlertDialog(getActivity(), title, error);
    }
}
