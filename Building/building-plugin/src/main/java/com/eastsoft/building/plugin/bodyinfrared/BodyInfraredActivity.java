package com.eastsoft.building.plugin.bodyinfrared;

import android.os.Bundle;

import com.eastsoft.building.plugin.PluginActivity;
import com.ehomeclouds.eastsoft.channel.http.CloudService.Iview;

/**
 * Created by 王熙斌 on 2016/4/13.
 */
public class BodyInfraredActivity extends PluginActivity implements Iview {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.body_infrared_control);
        initDate();
        initView();
    }

    private void initDate(){

    }

    private void initView(){

    }

    private void initViewListener(){

    }

    @Override
    public void onSuccess(Object object) {

    }

    @Override
    public void onFailed(String errorStr) {

    }

    @Override
    public void showProgress(boolean show) {

    }
}
