package com.eastsoft.building.plugin.lcd;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.eastsoft.building.plugin.R;
import com.eastsoft.building.sdk.BaseActivity;
import com.eastsoft.building.sdk.DataManeger;
import com.eastsoft.building.sdk.KeyUtil;
import com.ehomeclouds.eastsoft.channel.http.response.DeviceInfo;
import com.ehomeclouds.eastsoft.channel.mqtt.MqttManeger;
import com.ehomeclouds.eastsoft.channel.mqtt.util.MqttTopicManeger;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ll on 2016/4/14.
 */
public class LcdActivity extends BaseActivity {
    DeviceInfo curDevice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_lcd);
        Intent intent = getIntent();
        String dk = intent.getStringExtra(KeyUtil.DEVICE_KEY);
        curDevice = DataManeger.getInstance().deviceInfoMap.get(dk);
        restoreActionBar(curDevice.device_name, R.drawable.array_left_default, 0, "", "确定");
        final EditText editText = (EditText) findViewById(R.id.message);
        setOnclick(new IOnTitleClick() {
            @Override
            public void OnLeftClick(View view) {
                finish();
            }

            @Override
            public void OnRightClick(View view) {

                if (editText.getText().toString() == null||editText.getText().toString().equals("")) {
                    showToast("不能为空");
                } else {
                    publishMessage(editText.getText().toString());
                }
            }
        });

    }

    private void publishMessage(String message) {
        JSONObject limitRequest = new JSONObject();
        JSONObject function = new JSONObject();
        try {
            function.put(KeyUtil.MESSAGE, message);
            limitRequest.put(KeyUtil.FUNCTION, function);
            limitRequest.put(KeyUtil.DEVICE_KEY, curDevice.device_key);
            limitRequest.put(KeyUtil.CMD, KeyUtil.CMD_WRITE);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        MqttManeger.getInstance(this).publish(limitRequest.toString(), MqttTopicManeger.getPubTopic(DataManeger.getInstance().brokerDomain, curDevice.gateway_device_key, curDevice.device_key));


    }
}
