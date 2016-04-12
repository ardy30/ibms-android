package com.eastsoft.building.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.eastsoft.building.R;
import com.eastsoft.building.adapter.DeviceListAdapter;
import com.eastsoft.building.adapter.DeviceListData;
import com.eastsoft.building.sdk.BaseActivity;
import com.eastsoft.building.sdk.DataManeger;
import com.eastsoft.building.sdk.IntentUtil;
import com.eastsoft.building.sdk.UtilityInfo;
import com.ehomeclouds.eastsoft.channel.http.CloudService.Iview;
import com.ehomeclouds.eastsoft.channel.http.api.HttpCloudService;
import com.ehomeclouds.eastsoft.channel.http.response.DeviceInfo;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by ll on 2016/4/8.
 */
public class DeviceListActivity extends BaseActivity implements Iview{

    private long type,id;
    private HttpCloudService httpCloudService;
    private ListView listView;
    private DeviceListAdapter deviceListAdapter;
    private List<DeviceListData> deviceListDataList=new LinkedList<>();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_list);
        restoreActionBar("设备列表", R.drawable.array_left_default, 0, "", "");
        setOnclick(new IOnTitleClick() {
            @Override
            public void OnLeftClick(View view) {
                finish();
            }

            @Override
            public void OnRightClick(View view) {

            }
        });
        initData();
        initView();
    }

    private void initView() {

        listView= (ListView) findViewById(R.id.listview);
        deviceListAdapter=new DeviceListAdapter(deviceListDataList);
        listView.setAdapter(deviceListAdapter);
    }

    private void initData() {
        Intent intent=getIntent();
        type=intent.getLongExtra(IntentUtil.INTENT_TYPE,0);
        id=intent.getLongExtra(IntentUtil.INTENT_ID,0);
        httpCloudService=new HttpCloudService(this);
        if (type==UtilityInfo.TYPE_GROUP){
            httpCloudService.getGroupDeviceList(DataManeger.getInstance().userId,1,100,id,this);

        }else if (type==UtilityInfo.TYPE_SCENARIO){
            httpCloudService.getScenarioDeviceList(DataManeger.getInstance().userId,1,100,id,this);

        }
    }

    @Override
    public void onSuccess(Object object) {
        ArrayList<DeviceInfo> deviceInfoArrayList= (ArrayList<DeviceInfo>) object;
        deviceListDataList.clear();
        for (DeviceInfo deviceInfo:deviceInfoArrayList){

            deviceListDataList.add(new DeviceListData(deviceInfo.device_name, deviceInfo.area_name));
        }
        deviceListAdapter.notifyDataSetChanged();


    }

    @Override
    public void onFailed(String errorStr) {

    }

    @Override
    public void showProgress(boolean show) {

    }
}
