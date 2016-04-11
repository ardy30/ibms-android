package com.eastsoft.building.plugin.energymonitor;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.eastsoft.building.sdk.BaseActivity;
import com.ehomeclouds.eastsoft.channel.http.CloudService.Iview;
import com.ehomeclouds.eastsoft.plugin.R;
import com.ehomeclouds.eastsoft.plugin.activity.ActivitySubClass;
import com.ehomeclouds.eastsoft.plugin.customview.CustomTimePicker;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by wangyp on 2016/3/28.
 * Created by wangyp on 20:27.
 * This is a built-in template.
 * It contains a code fragment .
 */
public class ElectricMonitorActivity extends BaseActivity implements Iview {
    private ElectricMonitorPresenter electricMonitorPresenter;

    private View addTimer, main, timerManager, toTimer, toLimit, limitManager;
    private ImageButton on, off ;
    private TextView currentEle, v, a, power, lastEle;
    private ListView timers;
    private EditText et_maxvoltage, et_minvoltage, et_current, et_power;
    private SlipButton timerSlipBn, limitSlipBn;
    private List<Integer> days = new ArrayList<Integer>(),
            daysCache = new ArrayList<Integer>();
    private Dialog confirmDialog;
    private View confirmDialogView;
    private float maxVoltage, minVoltage, current, setPower;
    private Button clear, /* repeatConfirm, */
            sunday, monday, tuesday, wednesday,
            thursday, friday, saturday, addBn, complete, cancel, confirmAdd,
            cancelAdd, limit_confirm, limit_cancel;

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
        // 构建基础视图
        restoreActionBar(curDevice.getAlias(),getString(R.string.back),0,"",0);
        // 获取界面上主要组件
        initView();
        // 为主要按钮设置监听
        initBtn();

        update();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void update(){
        // 监控器总开关
        if (electricMonitorPresenter.isOpen()){
            on.setBackground(getDrawable(R.drawable.plugin_on_orange_sel));
            off.setBackground(getDrawable(R.drawable.plugin_off_gray_sel));
        }else {
            on.setBackground(getDrawable(R.drawable.plugin_on_gray_sel));
            off.setBackground(getDrawable(R.drawable.plugin_off_gray_sel));
        }
        // 越限保护开关
        limitSlipBn.setChecked(electricMonitorPresenter.ifOverProtected());
        // 获取当前电量、当前电压、当前电流和当前功率，并显示
        currentEle.setText(electricMonitorPresenter.getElectricity()+"");
        v.setText(electricMonitorPresenter.getVoltage()+"");
        a.setText(electricMonitorPresenter.getCurrent()+"");
        power.setText(electricMonitorPresenter.getPower()+"");
        // 获取阈值
        maxVoltage = electricMonitorPresenter.getOverVoltage();
        minVoltage = electricMonitorPresenter.getUnderVoltage();
        current = electricMonitorPresenter.getOverCurrent();
        setPower = electricMonitorPresenter.getOverPower();
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

    private void initView(){
        on = (ImageButton) findViewById(R.id.smartsocket_on);
        off = (ImageButton) findViewById(R.id.smartsocket_off);
        toTimer = findViewById(R.id.timer_right_bn);
        toLimit = findViewById(R.id.limit_right_bn);
        currentEle = (TextView) findViewById(R.id.current_ele_text);
        v = (TextView) findViewById(R.id.smartsocket_v);
        a = (TextView) findViewById(R.id.smartsocket_a);
        power = (TextView) findViewById(R.id.smartsocket_power);
        // light = (TextView) findViewById(R.id.smartsocket_light);
        clear = (Button) findViewById(R.id.clear);
        addTimer = findViewById(R.id.add_timer);
        limitManager = findViewById(R.id.limit_manager);
        main = findViewById(R.id.main);
        timerManager = findViewById(R.id.timer_manager);
        timerSlipBn = (SlipButton) findViewById(R.id.timer_slipbn);
        limitSlipBn = (SlipButton) findViewById(R.id.limit_slipbn);

        // repeatConfirm = (Button)
        // findViewById(R.id.smartsocket_confirm_bn);
        sunday = (Button) findViewById(R.id.sunday);
        monday = (Button) findViewById(R.id.Monday);
        tuesday = (Button) findViewById(R.id.Tuesday);
        wednesday = (Button) findViewById(R.id.Wednesday);
        thursday = (Button) findViewById(R.id.Thursday);
        friday = (Button) findViewById(R.id.Friday);
        saturday = (Button) findViewById(R.id.Saturday);
        addBn = (Button) findViewById(R.id.smartsocket_add_bn);
        timers = (ListView) findViewById(R.id.timers);
        complete = (Button) findViewById(R.id.smartsocket_complete_bn);
        cancel = (Button) findViewById(R.id.smartsocket_cancel_bn);


        confirmAdd = (Button) findViewById(R.id.add);
        cancelAdd = (Button) findViewById(R.id.cancel);
        lastEle = (TextView) findViewById(R.id.last_ele_text);

        limit_confirm = (Button) findViewById(R.id.limit_ok);
        limit_cancel = (Button) findViewById(R.id.limit_cancel);
        et_maxvoltage = (EditText) findViewById(R.id.maxVoltage);
        et_minvoltage = (EditText) findViewById(R.id.minVoltage);
        et_current = (EditText) findViewById(R.id.current);
        et_power = (EditText) findViewById(R.id.power);
    }


    private void initBtn(){
        // 电源监控器总开关
        on.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                electricMonitorPresenter.openMonitor(true);
            }
        });

        off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                electricMonitorPresenter.openMonitor(false);
            }
        });

        // 定时通断右侧的滑动开关
        timerSlipBn.setOnChangedListener(new SlipButton.OnChangedListener() {

            @Override
            public void OnChanged(final SlipButton wiperSwitch, final boolean checkState) {
                // TODO: 2016/3/29
            }
        });

        // 越限保护右侧的滑动开关
        limitSlipBn.setOnChangedListener(new SlipButton.OnChangedListener() {
            @Override
            public void OnChanged(SlipButton wiperSwitch, boolean checkState) {
                electricMonitorPresenter.enableOverProtect(checkState);
            }
        });

        // 定时通断跳转按钮
        toTimer.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // TODO: 2016/3/29
            }
        });
        // 越限保护跳转按钮
        toLimit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 设置确认跳转对话框的样式
                confirmDialog = new Dialog(ElectricMonitorActivity.this, R.style.dialog);
                confirmDialogView = LayoutInflater.from(ElectricMonitorActivity.this).inflate(
                        R.layout.confirm_dialog, null);
                confirmDialog.setContentView(confirmDialogView);
                Button confirm = (Button) confirmDialogView
                        .findViewById(R.id.confirm_dialog_confirm);
                Button cancel = (Button) confirmDialogView
                        .findViewById(R.id.confirm_dialog_cancle);
                // 为对话框中的确认和取消按钮设置事件监听
                confirm.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        confirmDialog.dismiss();
                        showLimitView();

                    }
                });

                cancel.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        confirmDialog.dismiss();
                    }
                });
                // 弹出对话框
                confirmDialog.show();
            }
        });

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                electricMonitorPresenter.clearCurrentElec();
            }
        });

    }

    private void showLimitView(){
        // 初始化越限保护界面，为限定值输入框置值
        main.setVisibility(View.GONE);
        addTimer.setVisibility(View.GONE);
        timerManager.setVisibility(View.GONE);
        limitManager.setVisibility(View.VISIBLE);
        et_maxvoltage.setText(maxVoltage + "");
        et_minvoltage.setText(minVoltage + "");
        et_current.setText(current + "");
        et_power.setText(setPower + "");
        // 界面确定按钮的事件监听
        limit_confirm.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (!isLegal(et_maxvoltage.getText().toString(), et_minvoltage.getText().toString()
                        , et_current.getText().toString(), et_power.getText().toString())) {
                    return;
                }
                if (!et_maxvoltage.getText().equals(maxVoltage + "") || !et_minvoltage.getText().equals(minVoltage + "")
                        || !et_current.getText().equals(current + "") || !et_power.getText().equals(setPower + "")) {
                    if (154 <= Float.parseFloat(et_minvoltage.getText().toString()) &&
                            Float.parseFloat(et_minvoltage.getText().toString()) <= 176) {
                        Toast.makeText(ElectricMonitorActivity.this, getString(R.string.cuation_under_pressure_low), Toast.LENGTH_SHORT)
                                .show();
                    }
                    if (254 <= Float.parseFloat(et_maxvoltage.getText().toString()) &&
                            Float.parseFloat(et_maxvoltage.getText().toString()) <= 264) {
                        Toast.makeText(ElectricMonitorActivity.this, getString(R.string.cuation_overpressure_value_pass), Toast.LENGTH_SHORT)
                                .show();
                    }

                    // 当前的越限保护输入值都合法，发送越限保护置值请求
                    electricMonitorPresenter.setLimitThreshold(Float.parseFloat(et_maxvoltage.getText().toString()),Float.parseFloat(et_minvoltage.getText().toString()),
                            Float.parseFloat(et_current.getText().toString()),Float.parseFloat(et_power.getText().toString()));
                    // 界面恢复
                    main.setVisibility(View.VISIBLE);
                    addTimer.setVisibility(View.GONE);
                    timerManager.setVisibility(View.GONE);
                    limitManager.setVisibility(View.GONE);
                }
            }
        });
        // 界面取消按钮的事件监听
        limit_cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // 界面恢复
                main.setVisibility(View.VISIBLE);
                addTimer.setVisibility(View.GONE);
                timerManager.setVisibility(View.GONE);
                limitManager.setVisibility(View.GONE);
            }
        });
    }

    private void initDaysBn(final List<Integer> days) {

        monday.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (days.contains(1)) {
                    v.setBackgroundColor(Color.rgb(224, 224, 224));
                    ((Button) v).setTextColor(Color.rgb(83, 83, 83));
                    days.remove((Object) 1);
                } else {
                    v.setBackgroundColor(Color.rgb(254, 166, 30));
                    ((Button) v).setTextColor(Color.rgb(255, 255, 255));
                    days.add(1);
                }
            }
        });

        tuesday.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (days.contains(2)) {
                    v.setBackgroundColor(Color.rgb(224, 224, 224));
                    ((Button) v).setTextColor(Color.rgb(83, 83, 83));
                    days.remove((Object) 2);
                } else {
                    v.setBackgroundColor(Color.rgb(254, 166, 30));
                    ((Button) v).setTextColor(Color.rgb(255, 255, 255));
                    days.add(2);
                }
            }
        });

        wednesday.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (days.contains(3)) {
                    v.setBackgroundColor(Color.rgb(224, 224, 224));
                    ((Button) v).setTextColor(Color.rgb(83, 83, 83));
                    days.remove((Object) 3);
                } else {
                    v.setBackgroundColor(Color.rgb(254, 166, 30));
                    ((Button) v).setTextColor(Color.rgb(255, 255, 255));
                    days.add(3);
                }
            }
        });

        thursday.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (days.contains(4)) {
                    v.setBackgroundColor(Color.rgb(224, 224, 224));
                    ((Button) v).setTextColor(Color.rgb(83, 83, 83));
                    days.remove((Object) 4);
                } else {
                    v.setBackgroundColor(Color.rgb(254, 166, 30));
                    ((Button) v).setTextColor(Color.rgb(255, 255, 255));
                    days.add(4);
                }
            }
        });

        friday.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (days.contains(5)) {
                    v.setBackgroundColor(Color.rgb(224, 224, 224));
                    ((Button) v).setTextColor(Color.rgb(83, 83, 83));
                    days.remove((Object) 5);
                } else {
                    v.setBackgroundColor(Color.rgb(254, 166, 30));
                    ((Button) v).setTextColor(Color.rgb(255, 255, 255));
                    days.add(5);
                }
            }
        });

        saturday.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (days.contains(6)) {
                    v.setBackgroundColor(Color.rgb(224, 224, 224));
                    ((Button) v).setTextColor(Color.rgb(83, 83, 83));
                    days.remove((Object) 6);
                } else {
                    v.setBackgroundColor(Color.rgb(254, 166, 30));
                    ((Button) v).setTextColor(Color.rgb(255, 255, 255));
                    days.add(6);
                }
            }
        });

        sunday.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (days.contains(7)) {
                    v.setBackgroundColor(Color.rgb(224, 224, 224));
                    ((Button) v).setTextColor(Color.rgb(83, 83, 83));
                    days.remove((Object) 7);
                } else {
                    v.setBackgroundColor(Color.rgb(254, 166, 30));
                    ((Button) v).setTextColor(Color.rgb(255, 255, 255));
                    days.add(7);
                }
            }
        });

    }

    private boolean isLegal(String maxVoltage, String minVoltage, String current, String power) {
        if(haveMorePoint(maxVoltage)||haveMorePoint(minVoltage)||haveMorePoint(current)||haveMorePoint(power)){
            Toast.makeText(ElectricMonitorActivity.this, getString(R.string.style_error), Toast.LENGTH_SHORT).show();
            return false;
        }
        if (maxVoltage != null && (!maxVoltage.equals("")) && minVoltage != null && (!minVoltage.equals(""))
                && current != null && (!current.equals("")) && power != null && (!power.equals(""))) {
            if (Float.parseFloat(maxVoltage) > 264) {
                Toast.makeText(ElectricMonitorActivity.this, getString(R.string.overpressure_value_no_pass)+"264V", Toast.LENGTH_SHORT).show();
                return false;
//            } else if (Float.parseFloat(maxVoltage) < 254) {
//                Toast.makeText(getActivity(),MyApplication.getStringFromResouse(R.string.overpressure_value_no_under)+"254V", Toast.LENGTH_SHORT).show();
//                return false;
//            } else if (Float.parseFloat(minVoltage) > 176) {
//                Toast.makeText(getActivity(),MyApplication.getStringFromResouse(R.string.under_pressure_no_pass)+"176V", Toast.LENGTH_SHORT).show();
//                return false;
            } else if (Float.parseFloat(minVoltage) < 90) {
                Toast.makeText(ElectricMonitorActivity.this,getString(R.string.under_pressure_no_low)+"90V", Toast.LENGTH_SHORT).show();
                return false;
            } else if (Float.parseFloat(current) > 32) {
                Toast.makeText(ElectricMonitorActivity.this,getString(R.string.current_value_no_pass)+"32A", Toast.LENGTH_SHORT).show();
                return false;
            } else if (Float.parseFloat(current) < 0.01) {
                Toast.makeText(ElectricMonitorActivity.this,getString(R.string.current_value_no_low)+"0.01A", Toast.LENGTH_SHORT).show();
                return false;
            } else if (Float.parseFloat(power) > 7.04) {
                Toast.makeText(ElectricMonitorActivity.this,getString(R.string.power_no_pass)+"7040W", Toast.LENGTH_SHORT).show();
                return false;
            } else if (Float.parseFloat(power) < 0.1) {
                Toast.makeText(ElectricMonitorActivity.this,getString(R.string.power_no_low)+"100W", Toast.LENGTH_SHORT).show();
                return false;
            } else if (Float.parseFloat(maxVoltage) < 0 || Float.parseFloat(minVoltage) < 0
                    || Float.parseFloat(current) < 0 || Float.parseFloat(power) < 0) {
                Toast.makeText(ElectricMonitorActivity.this,getString(R.string.numberical_no_negetive), Toast.LENGTH_SHORT).show();
                return false;
            } else {
                return true;
            }
        } else {
            Toast.makeText(ElectricMonitorActivity.this,getString(R.string.numberical_no_null), Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private boolean haveMorePoint(String str){
        int num = 0;
        for (int i = 0; i < str.length(); i++) {
            char item = str.charAt(i);
            if(item=='.'){
                num++;
            }
        }
        if(num>1){
            return true;
        } else if(num==1&&str.length()==1) {
            return true;
        } else {
            return false;
        }
    }

}
