package com.eastsoft.building.plugin.bodyinfrared;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.eastsoft.building.plugin.PluginActivity;
import com.eastsoft.building.plugin.R;
import com.ehomeclouds.eastsoft.channel.http.CloudService.Iview;
import com.ehomeclouds.eastsoft.channel.http.response.DeviceInfo;

/**
 * Created by 王熙斌 on 2016/4/13.
 */
public class BodyInfraredActivity extends PluginActivity implements Iview,View.OnClickListener {
    private TextView light,people,closeLightDelay,openLightThreshold,closeLightThreshold;
    private EditText settingCloselightDelay,settingOpenLightThreshold,settingCloseLightThreshold;
    private Button sensitivityHigh,sensitivityMiddle,sensitivityLow;
    private LinearLayout sensitivityLayout;
    private DeviceInfo deviceInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.body_infrared_control);
        initDate();
        initView();
        initViewListener();
    }

    private void initDate(){

    }

    private void initView(){
        light=(TextView)findViewById(R.id.light_value);
        people=(TextView)findViewById(R.id.people);
        closeLightDelay=(TextView)findViewById(R.id.close_light_delay_value);
        openLightThreshold=(TextView)findViewById(R.id.open_light_threshold_value);
        closeLightThreshold=(TextView)findViewById(R.id.close_light_delay_value);
        settingCloselightDelay=(EditText)findViewById(R.id.edit_close_delay);
        settingOpenLightThreshold=(EditText)findViewById(R.id.edit_open_light_threshold);
        settingCloseLightThreshold=(EditText)findViewById(R.id.edit_close_light_threshold);
        sensitivityLow=(Button)findViewById(R.id.sensitivity_low);
        sensitivityHigh=(Button)findViewById(R.id.sensitivity_high);
        sensitivityMiddle=(Button)findViewById(R.id.sensitivity_low);
        sensitivityLayout=(LinearLayout)findViewById(R.id.sensitivity_layout);
        if (deviceInfo.device_key.equals("370100001")){
                sensitivityLayout.setVisibility(View.VISIBLE);
        }else if (deviceInfo.device_key.equals("370100001")){
                sensitivityLayout.setVisibility(View.GONE);
        }
    }

    private void initViewListener(){
        sensitivityHigh.setOnClickListener(this);
        sensitivityMiddle.setOnClickListener(this);
        sensitivityLow.setOnClickListener(this);

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

    @Override
    public void onClick(View v) {

    }
}
