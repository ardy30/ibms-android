package com.eastsoft.building.activity;

import android.os.Bundle;
import android.os.PersistableBundle;

import com.eastsoft.building.R;
import com.eastsoft.building.sdk.BaseActivity;

/**
 * Created by ll on 2016/4/8.
 */
public class DeviceListActivity extends BaseActivity {
    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.activity_device_list);
    }
}
