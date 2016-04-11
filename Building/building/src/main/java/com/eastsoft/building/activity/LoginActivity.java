package com.eastsoft.building.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.eastsoft.building.R;
import com.eastsoft.building.sdk.BaseActivity;
import com.eastsoft.building.sdk.UtilityInfo;
import com.ehomeclouds.eastsoft.channel.http.base.Util.StringStaticUtils;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends BaseActivity {
    private EditText mEmailView;
    private EditText mPasswordView;
    private SharedPreferences sharedPreferences;
    private  Button mEmailSignInButton;
    private String email,psd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);

//        initIntroduce();
        mEmailView = (EditText) findViewById(R.id.email);
        mPasswordView = (EditText) findViewById(R.id.password);

        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (UtilityInfo.networkIsConnected(LoginActivity.this)) {

                    attemptLogin();
                } else {
                    showToast("请检查网络连接");
                }
            }
        });

        sharedPreferences=getSharedPreferences(StringStaticUtils.SHAREP_NAME, Context.MODE_PRIVATE);
        String email=sharedPreferences.getString(StringStaticUtils.SHAREP_EMAIL, "");
        String psd=sharedPreferences.getString(StringStaticUtils.SHAREP_PSD,"");
//        autoLogin();
//        mEmailView.setText(email);
        mEmailView.setText("admin");
        mPasswordView.setText("admin");
//        mPasswordView.setText(psd);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private void autoLogin(){
         email=sharedPreferences.getString(StringStaticUtils.SHAREP_EMAIL, "");
         psd=sharedPreferences.getString(StringStaticUtils.SHAREP_PSD,"");
        if (!email.equals("")&&!psd.equals("")){
            mEmailView.setText(email);
            mPasswordView.setText(psd);
            attemptLogin();
        }else{
            mEmailSignInButton.setEnabled(false);
        }

    }
    public boolean isEmailValid(String email) {
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.login_username_can_not_empty));
            return false;
        }
//        if (!email.contains("@")) {
//            mEmailView.setError(getString(R.string.login_username_fomat_error));
//            return false;
//        }
        return true;
    }

    public boolean isPasswordValid(String psd) {
        if (TextUtils.isEmpty(psd)) {
            mPasswordView.setError(getString(R.string.login_psd_can_not_empty));
            return false;
        }
        return true;
    }
    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (isEmailValid(mEmailView.getText().toString()) && isPasswordValid(mPasswordView.getText().toString())){
            saveAccountInfo();
            Intent intent = new Intent();
            intent.putExtra("username", mEmailView.getText().toString());
            intent.putExtra("password", mPasswordView.getText().toString());
            intent.setClass(LoginActivity.this, LoginBackGroundActivity.class);
            startActivity(intent);
            finish();
        }
    }
    private void saveAccountInfo(){
        SharedPreferences sharedPreferences = getSharedPreferences(StringStaticUtils.SHAREP_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(StringStaticUtils.SHAREP_EMAIL,mEmailView.getText().toString());
        editor.putString(StringStaticUtils.SHAREP_PSD, mPasswordView.getText().toString());
        editor.commit();
    }
//    private void initIntroduce() {
//
//        SharedPreferences preferences = getSharedPreferences("count",MODE_PRIVATE);
//        int count = preferences.getInt("count", 0);
//        if (count == 0) {
//            Intent intent = new Intent(LoginActivity.this, IntroduceActivity.class);
//            startActivity(intent);
//        }
//        SharedPreferences.Editor editor = preferences.edit();
//        editor.putInt("count", ++count);
//        editor.commit();
//    }


}
