package com.eastsoft.building.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.eastsoft.building.R;
import com.eastsoft.building.sdk.BaseActivity;

/**
 * Created by ll on 2016/4/15.
 */
public class IpsetActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ip);
        final EditText editIp= (EditText) findViewById(R.id.ip);
        final EditText editPort= (EditText) findViewById(R.id.port);
        restoreActionBar("设置服务器IP", R.drawable.array_left_default, 0, "", "确定");
        SharedPreferences sharedPreferences=getSharedPreferences("setip",MODE_PRIVATE);
        final SharedPreferences.Editor editor=sharedPreferences.edit();

        setOnclick(new IOnTitleClick() {
            @Override
            public void OnLeftClick(View view) {
                finish();
            }

            @Override
            public void OnRightClick(View view) {

                if (checkIP(editIp.getText().toString())) {
                    if (editPort.getText().toString()==null||editPort.getText().toString().equals("")){
                        showToast("端口号不能为空");
                    }
                    editor.putString("ip",editIp.getText().toString());
                    editor.putString("port",editPort.getText().toString());
                    editor.commit();
                    finish();
                } else {
                    showToast("格式不正确");
                }


            }
        });
    }
    public static boolean checkIP(String ip)
    {
        if (ip == null)
        {
            return false;
        }
        String regex = "^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\."
                + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
                + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
                + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)$";
        if (ip.matches(regex))
        {
            return true;
        }
        return false;
    }
}
