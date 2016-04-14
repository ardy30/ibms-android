package com.eastsoft.building.plugin.energymonitor;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.eastsoft.building.sdk.BaseActivity;
import com.eastsoft.building.sdk.DataManeger;
import com.eastsoft.building.sdk.KeyUtil;
import com.ehomeclouds.eastsoft.channel.http.CloudService.Iview;
import com.ehomeclouds.eastsoft.channel.http.response.DeviceInfo;
import  com.eastsoft.building.plugin.R;


/**
 * Created by ll on 2016/4/12.
 */
public class ElectricMonitorActivity extends BaseActivity implements Iview {
    private ElectricMonitorPresenter electricMonitorPresenter;

    private Button on, off,pOn,pOff,btn_over_v,btn_over_a,btn_under_v,btn_over_p;
    private TextView currentEle, v, a, power,text_over_v,text_under_v,text_over_a,text_over_p;
    private EditText et_maxvoltage, et_minvoltage, et_over_current, et_overpower;
    private float maxVoltage, minVoltage, current, setPower;
    private DeviceInfo curDevice;

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // 获取设备信息
        super.onCreate(savedInstanceState);
        // 读取布局文件
        setContentView(R.layout.electric_monitor_control);
        Intent intent = getIntent();
        String dk = intent.getStringExtra(KeyUtil.DEVICE_KEY);
        curDevice = DataManeger.getInstance().deviceInfoMap.get(dk);
        electricMonitorPresenter=new ElectricMonitorPresenter(this,this,curDevice.device_key,curDevice.gateway_device_key);
        // 构建基础视图
        restoreActionBar(curDevice.device_name, R.drawable.array_left_default, 0, "", "");
        // 获取界面上主要组件
        initView();
        // 为主要按钮设置监听
        initBtn();

        update();
        et_maxvoltage.setText(electricMonitorPresenter.getOverVoltage() + "V");
        et_minvoltage.setText(electricMonitorPresenter.getUnderVoltage() + "V");
        et_over_current.setText(electricMonitorPresenter.getOverCurrent() + "A");
        et_overpower.setText(electricMonitorPresenter.getOverPower() + "KW");

    }

    private void update() {
        // 监控器总开关
        on.setSelected(electricMonitorPresenter.isOpen());
        off.setSelected(!electricMonitorPresenter.isOpen());

        // 越限保护开关
        pOn.setSelected(electricMonitorPresenter.isOverProtected());
        pOff.setSelected(!electricMonitorPresenter.isOverProtected());
        // 获取当前电量、当前电压、当前电流和当前功率，并显示
        currentEle.setText(electricMonitorPresenter.getElectricity() + "");
        v.setText(electricMonitorPresenter.getVoltage() + "");
        a.setText(electricMonitorPresenter.getCurrent() + "");
        power.setText(electricMonitorPresenter.getPower() + "");


        // 获取阈值
//        maxVoltage = electricMonitorPresenter.getOverVoltage();
//        minVoltage = electricMonitorPresenter.getUnderVoltage();
//        current = electricMonitorPresenter.getOverCurrent();
//        setPower = electricMonitorPresenter.getOverPower();
    }

    @Override
    public void onSuccess(Object object) {
        update();
    }

    @Override
    public void onFailed(String errorStr) {

    }

    @Override
    public void showProgress(boolean show) {

    }

    private void initView() {
        on= (Button) findViewById(R.id.btn_on);
        off= (Button) findViewById(R.id.btn_off);
        pOn= (Button) findViewById(R.id.btn_p_on);
        pOff= (Button) findViewById(R.id.btn_p_off);
        btn_over_v= (Button) findViewById(R.id.btn_v_ok);
        btn_under_v= (Button) findViewById(R.id.btn_under_v_ok);
        btn_over_a= (Button) findViewById(R.id.btn_a_ok);
        btn_over_p= (Button) findViewById(R.id.btn_power_ok);
        currentEle= (TextView) findViewById(R.id.text_elc);
        v= (TextView) findViewById(R.id.text_v);
        a= (TextView) findViewById(R.id.text_a);
        power= (TextView) findViewById(R.id.text_p);
        et_maxvoltage= (EditText) findViewById(R.id.edit_v);
        et_minvoltage= (EditText) findViewById(R.id.edit_under_v);
        et_over_current= (EditText) findViewById(R.id.edit_a);
        et_overpower= (EditText) findViewById(R.id.edit_power);
        text_over_v= (TextView) findViewById(R.id.text_over_v);
        text_under_v= (TextView) findViewById(R.id.text_under_v);
        text_over_a= (TextView) findViewById(R.id.text_over_a);
        text_over_p= (TextView) findViewById(R.id.text_over_p);

    }


    private void initBtn() {
        on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                electricMonitorPresenter.on(true);
            }
        });
        off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                electricMonitorPresenter.on(false);
            }
        });
        pOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                electricMonitorPresenter.enableOverProtect(true);
            }
        });
        pOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                electricMonitorPresenter.enableOverProtect(false);
            }
        });
        btn_over_v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String value=et_maxvoltage.getText().toString();
                if (isLegalOverV(value)){
                    electricMonitorPresenter.setOverV(Float.parseFloat(value));
                }
            }
        });
        btn_over_a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String value=et_over_current.getText().toString();
                if (isLegalOverA(value)){
                    electricMonitorPresenter.setOverA(Float.parseFloat(value));
                }
            }
        });
        btn_under_v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String value=et_minvoltage.getText().toString();
                if (isLegalUnderV(value)){
                    electricMonitorPresenter.setUnderV(Float.parseFloat(value));
                }
            }
        });
        btn_over_p.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String value=et_overpower.getText().toString();
                if (isLegalOverP(value)){
                    electricMonitorPresenter.setOverp(Float.parseFloat(value));
                }
            }
        });
        findViewById(R.id.btn_refresh).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                electricMonitorPresenter.refresh();
            }
        });
        setOnclick(new IOnTitleClick() {
            @Override
            public void OnLeftClick(View view) {
                finish();
            }

            @Override
            public void OnRightClick(View view) {

            }
        });


    }


    private boolean isLegalOverV(String maxVoltage) {
        if (maxVoltage != null && (!maxVoltage.equals(""))) {
            if (Float.parseFloat(maxVoltage) > 264||Float.parseFloat(maxVoltage) < 254) {
                Toast.makeText(ElectricMonitorActivity.this, getString(R.string.style_error), Toast.LENGTH_SHORT).show();
                return false;
            }else {
                return true;
            }
        } else {
            Toast.makeText(ElectricMonitorActivity.this, getString(R.string.numberical_no_null), Toast.LENGTH_SHORT).show();
            return false;
        }
    }
    private boolean isLegalUnderV(String minVoltage) {
        if (minVoltage != null && (!minVoltage.equals(""))) {
            if (Float.parseFloat(minVoltage) > 176||Float.parseFloat(minVoltage) < 154) {
                Toast.makeText(ElectricMonitorActivity.this, getString(R.string.style_error), Toast.LENGTH_SHORT).show();
                return false;
            }else {
                return true;
            }
        } else {
            Toast.makeText(ElectricMonitorActivity.this, getString(R.string.numberical_no_null), Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private boolean isLegalOverA(String a) {
        if (a != null && (!a.equals(""))) {
            if (Float.parseFloat(a) > 32) {
                Toast.makeText(ElectricMonitorActivity.this, getString(R.string.style_error), Toast.LENGTH_SHORT).show();
                return false;
            }else {
                return true;
            }
        } else {
            Toast.makeText(ElectricMonitorActivity.this, getString(R.string.numberical_no_null), Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private boolean isLegalOverP(String p) {
        if (p != null && (!p.equals(""))) {
            if (Float.parseFloat(p) > 7040) {
                Toast.makeText(ElectricMonitorActivity.this, getString(R.string.style_error), Toast.LENGTH_SHORT).show();
                return false;
            }else {
                return true;
            }
        } else {
            Toast.makeText(ElectricMonitorActivity.this, getString(R.string.numberical_no_null), Toast.LENGTH_SHORT).show();
            return false;
        }
    }



}
