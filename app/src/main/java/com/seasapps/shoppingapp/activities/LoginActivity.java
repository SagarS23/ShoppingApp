package com.seasapps.shoppingapp.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.seasapps.shoppingapp.R;
import com.seasapps.shoppingapp.ShoppingApp;
import com.seasapps.shoppingapp.utils.Utils;
import com.seasapps.shoppingapp.widget.CustomButtonRegular;
import com.seasapps.shoppingapp.widget.CustomTextViewRegular;
import com.seasapps.shoppingapp.widget.MaterialProgressBar.CustomProgressDialog;
import com.seasapps.shoppingapp.widget.PasswordEditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.et_email)
    MaterialEditText etEmail;
    @BindView(R.id.et_password)
    PasswordEditText etPassword;
    @BindView(R.id.btn_login)
    CustomButtonRegular btnLogin;
    @BindView(R.id.tv_register)
    CustomTextViewRegular tvRegister;
    @BindView(R.id.tv_name)
    CustomTextViewRegular tvName;

    ShoppingApp shoppingApp;
    Context context;
    CustomProgressDialog customProgressDialog;
    private FirebaseAuth auth;
    private long lastPress;
    Toast backpressToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        shoppingApp = (ShoppingApp) getApplicationContext();
        context = LoginActivity.this;
        customProgressDialog = new CustomProgressDialog(context);

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();

    }

    @OnClick(R.id.btn_login)
    public void onLoginClick() {

        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (email.isEmpty()) {
            etEmail.setError(getResources().getString(R.string.enter_email_address));
            etEmail.requestFocus();
            return;
        }

        if (!Utils.isValidEmail(email)) {
            etEmail.setError(getResources().getString(R.string.enter_valid_email_address));
            etEmail.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            etPassword.setError(getResources().getString(R.string.enter_password));
            etPassword.requestFocus();
            return;
        }

        if (password.length() < 6 || password.length() > 12) {
            etPassword.setError(getResources().getString(R.string.password_must_be));
            etPassword.requestFocus();
            return;
        }

        if (btnLogin.getText().toString().equalsIgnoreCase(getResources().getString(R.string.login))) {
            loginUser(email, password);
        } else if (btnLogin.getText().toString().equalsIgnoreCase(getResources().getString(R.string.register))) {
            registerUser(email, password);
        }
    }

    private void registerUser(String email, String password) {

        //register user
        customProgressDialog.show();
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        customProgressDialog.dismiss();
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Utils.showErrorToast(context, getString(R.string.failed_try_again));
                        } else {
                            Utils.showSuccessToast(context, getString(R.string.registration_success));
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            finish();
                        }
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (customProgressDialog.isShowing()) {
            customProgressDialog.dismiss();
        }
    }

    private void loginUser(String email, String password) {

        //authenticate user
        customProgressDialog.show();
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        customProgressDialog.dismiss();
                        if (!task.isSuccessful()) {
                            // there was an error
                            Utils.showErrorToast(context, getString(R.string.failed_try_again));
                        } else {
                            Utils.showSuccessToast(context, getString(R.string.login_successfully));
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                });

    }

    @OnClick(R.id.tv_register)
    public void onClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_register:

                //Handling registration condition
                if (tvRegister.getText().toString().equalsIgnoreCase(getResources().getString(R.string.don_t_have_an_account_signup))) {
                    tvName.setText(getResources().getString(R.string.signup));
                    etEmail.setText("");
                    etPassword.setText("");
                    etEmail.setHint(R.string.enter_email);
                    etPassword.setHint(R.string.choose_password);
                    btnLogin.setText(getResources().getString(R.string.register));
                    tvRegister.setText(getResources().getString(R.string.already_registered_login));
                } else {
                    tvName.setText(getResources().getString(R.string.login));
                    etEmail.setText("");
                    etPassword.setText("");
                    etEmail.setHint(R.string.enter_email_id);
                    etPassword.setHint(R.string.enter_password);
                    btnLogin.setText(getResources().getString(R.string.login));
                    tvRegister.setText(getResources().getString(R.string.don_t_have_an_account_signup));
                }

                break;
        }
    }

    @Override
    public void onBackPressed() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastPress > 5000) {
            backpressToast = Toast.makeText(getBaseContext(), getResources().getString(R.string.press_back_again_to_exit), Toast.LENGTH_LONG);
            backpressToast.show();
            lastPress = currentTime;
        } else {
            if (backpressToast != null) backpressToast.cancel();
            super.onBackPressed();
        }
    }
}
