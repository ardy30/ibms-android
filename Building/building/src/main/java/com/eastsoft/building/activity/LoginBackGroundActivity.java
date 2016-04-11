package com.eastsoft.building.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.eastsoft.building.R;
import com.eastsoft.building.model.RxBus;
import com.eastsoft.building.sdk.DataManeger;
import com.eastsoft.building.sdk.KeyUtil;
import com.ehomeclouds.eastsoft.channel.http.CloudService.Iview;
import com.ehomeclouds.eastsoft.channel.http.api.HttpCloudService;
import com.ehomeclouds.eastsoft.channel.http.base.LoginResponse;
import com.ehomeclouds.eastsoft.channel.http.base.Util.StringStaticUtils;
import com.ehomeclouds.eastsoft.channel.http.response.ScenarioInfo;
import com.ehomeclouds.eastsoft.channel.mqtt.MqttManeger;
import com.ehomeclouds.eastsoft.channel.mqtt.model.MqttConnectStatus;


import java.util.ArrayList;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

public class LoginBackGroundActivity extends Activity implements Iview {
    private String edtusername;
    private String edtpw;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_container);
        edtusername = getIntent().getStringExtra("username");
        edtpw = getIntent().getStringExtra("password");
        login();
    }

    @Override
    public void onSuccess(Object object) {

        if (object!=null){
            DataManeger.getInstance().scenarioInfoArrayList = (ArrayList<ScenarioInfo>) object;
        }
        startActivity(new Intent(LoginBackGroundActivity.this, MainActivity.class));
        finish();
    }

    @Override
    public void onFailed(String errorStr) {
        Toast.makeText(LoginBackGroundActivity.this, errorStr, Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    @Override
    public void showProgress(boolean show) {

    }

    private void login(){
        final HttpCloudService httpCloudService=new HttpCloudService(this);
        httpCloudService.loginIn(edtusername, edtpw, new Iview() {
            @Override
            public void onSuccess(Object object) {
                LoginResponse loginResponse = (LoginResponse) object;
                DataManeger.getInstance().userId = loginResponse.userId;
                DataManeger.getInstance().brokerDomain = loginResponse.brokerDomain;
                DataManeger.getInstance().brokerPassword = loginResponse.brokerPassword;
                DataManeger.getInstance().brokerUsername = loginResponse.brokerUsername;
                DataManeger.getInstance().brokerUrl = loginResponse.brokerUrl;
//                saveUrl(LoginBackGroundActivity.this, loginResponse.brokerUrl);


                httpCloudService.getScenarioList(DataManeger.getInstance().userId,1,100,LoginBackGroundActivity.this);



            }

            @Override
            public void onFailed(String errorStr) {

            }

            @Override
            public void showProgress(boolean show) {

            }
        });

    }
    private static  void saveUrl(Context context,String url){
        SharedPreferences sharedPreferences = context.getSharedPreferences(com.ehomeclouds.eastsoft.channel.mqtt.util.KeyUtil.SHARE_URL, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(com.ehomeclouds.eastsoft.channel.mqtt.util.KeyUtil.SHARE_URL_KEY,url);
        editor.commit();
    }

}
