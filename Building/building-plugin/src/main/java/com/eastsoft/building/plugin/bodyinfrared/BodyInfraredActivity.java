package com.eastsoft.building.plugin.bodyinfrared;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.eastsoft.building.model.DeviceType;
import com.eastsoft.building.plugin.PluginActivity;
import com.eastsoft.building.plugin.R;
import com.eastsoft.building.sdk.DataManeger;
import com.eastsoft.building.sdk.KeyUtil;
import com.ehomeclouds.eastsoft.channel.http.CloudService.Iview;
import com.ehomeclouds.eastsoft.channel.http.response.DeviceInfo;

/**
 * Created by 王熙斌 on 2016/4/13.
 */
public class BodyInfraredActivity extends PluginActivity implements Iview {

    private DeviceInfo curDevice;
    private TextView textLight, textPeople;
    private EditText edCloseDelay, edOpen, edClose;
    private Button btnLow, btnMid, btnHight;
    BodyInfraredPresenter bodyInfraredPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.body_infrared_control);
        initDate();
        initView();
    }

    private void initDate() {
        Intent intent = getIntent();
        String dk = intent.getStringExtra(KeyUtil.DEVICE_KEY);
        curDevice = DataManeger.getInstance().deviceInfoMap.get(dk);
        bodyInfraredPresenter = new BodyInfraredPresenter(this, this, curDevice.device_key, curDevice.gateway_device_key);
    }

    private void initView() {
        restoreActionBar(curDevice.device_name, R.drawable.array_left_default, 0, "", "确定");

        textLight = (TextView) findViewById(R.id.light_value);
        textPeople = (TextView) findViewById(R.id.people);
        edCloseDelay = (EditText) findViewById(R.id.edit_close_delay);
        edOpen = (EditText) findViewById(R.id.edit_open_light_threshold);
        edClose = (EditText) findViewById(R.id.edit_close_light_threshold);
        btnLow = (Button) findViewById(R.id.sensitivity_low);
        btnMid = (Button) findViewById(R.id.sensitivity_middle);
        btnHight = (Button) findViewById(R.id.sensitivity_high);
        initViewListener();
        update();
    }

    private void update() {
        textLight.setText(bodyInfraredPresenter.getLight() + "");
        textPeople.setText(bodyInfraredPresenter.hasPeople() ? "有人" : "无人");
        edCloseDelay.setText(bodyInfraredPresenter.getCloseDelay() + "");
        edOpen.setText(bodyInfraredPresenter.getOpen() + "");
        edClose.setText(bodyInfraredPresenter.getClose() + "");
        if (curDevice.device_type_code.substring(7, 11).equals(DeviceType.EASTSOFT_DEVICE_BODY_INDUCTOR_SUBJOIN_01)) {
            findViewById(R.id.sensitivity_layout).setVisibility(View.GONE);
        } else if (curDevice.device_type_code.substring(7, 11).equals(DeviceType.EASTSOFT_DEVICE_BODY_INDUCTOR_SUBJOIN_02)) {
            if (bodyInfraredPresenter.getSensity() == BodyInfraredPresenter.LOW) {

                btnLow.setSelected(true);
                btnHight.setSelected(false);
                btnMid.setSelected(false);
            } else if (bodyInfraredPresenter.getSensity() == BodyInfraredPresenter.MIDDLE) {
                btnLow.setSelected(false);
                btnHight.setSelected(false);
                btnMid.setSelected(true);
            } else if (bodyInfraredPresenter.getSensity() == BodyInfraredPresenter.HIGH) {
                btnLow.setSelected(false);
                btnHight.setSelected(true);
                btnMid.setSelected(false);
            }
        }
    }


    private void initViewListener() {
        setOnclick(new IOnTitleClick() {
            @Override
            public void OnLeftClick(View view) {
                finish();
            }

            @Override
            public void OnRightClick(View view) {

                bodyInfraredPresenter.send(Integer.parseInt(edCloseDelay.getText().toString()),Integer.parseInt(edOpen.getText().toString()),Integer.parseInt(edClose.getText().toString()));
            }
        });

        btnHight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bodyInfraredPresenter.setSensor(KeyUtil.LIGHT_INDUCTION_SENSITY_HIGH);
            }
        });
        btnLow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bodyInfraredPresenter.setSensor(KeyUtil.LIGHT_INDUCTION_SENSITY_LOW);
            }
        });
        btnMid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bodyInfraredPresenter.setSensor(KeyUtil.LIGHT_INDUCTION_SENSITY_MIDDLE);
            }
        });
    }

    @Override
    public void onSuccess(Object object) {

        update();
    }

    @Override
    public void onFailed(String errorStr) {
        showToast(errorStr);
    }

    @Override
    public void showProgress(boolean show) {

    }
}
