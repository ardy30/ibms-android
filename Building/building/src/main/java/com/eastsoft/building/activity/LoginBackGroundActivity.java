package com.eastsoft.building.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.eastsoft.building.R;
import com.eastsoft.building.sdk.DataManeger;
import com.ehomeclouds.eastsoft.channel.http.CloudService.Iview;
import com.ehomeclouds.eastsoft.channel.http.api.HttpCloudService;
import com.ehomeclouds.eastsoft.channel.http.response.ScenarioInfo;


import java.util.ArrayList;

public class LoginBackGroundActivity extends Activity implements Iview {
    private String edtusername;
    private String edtpw;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_container);
        edtusername = getIntent().getStringExtra("username");
        edtpw = getIntent().getStringExtra("password");
        new HttpCloudService(this).loginIn(edtusername, edtpw, LoginBackGroundActivity.this);
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
}
