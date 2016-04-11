package com.eastsoft.building.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.eastsoft.building.R;
import com.eastsoft.building.adapter.CommontAdapterData;
import com.eastsoft.building.adapter.DeviceAdapter;
import com.eastsoft.building.adapter.SpinnerListAdapter;
import com.eastsoft.building.model.RxBus;
import com.eastsoft.building.plugin.PluginLoad;
import com.eastsoft.building.sdk.BaseFragment;
import com.eastsoft.building.sdk.DataManeger;
import com.eastsoft.building.sdk.KeyUtil;
import com.ehomeclouds.eastsoft.channel.http.CloudService.Iview;
import com.ehomeclouds.eastsoft.channel.http.response.AreaInfo;
import com.ehomeclouds.eastsoft.channel.http.response.DeviceInfo;
import com.ehomeclouds.eastsoft.channel.http.response.DeviceTypeInfo;
import com.ehomeclouds.eastsoft.channel.mqtt.model.MqttConnectStatus;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import presenter.DevicePresenter;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by ll on 2016/3/31.
 */
public class DeviceFragment extends BaseFragment {
    private ListView listView;
    private List<CommontAdapterData> adapterList = new LinkedList<>();
    private DeviceAdapter deviceAdapter;
    private View ly_area, ly_type, netbad;
    private PopupWindow popupWindow;
    private View popView;
    TextView text_area, text_type;
    private DevicePresenter devicePresenter;
    private long areaId;
    private String deviceTypeCode = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.f_device, container, false);
        devicePresenter = new DevicePresenter(httpCloudService);
        initTypeView(view);
        listView = (ListView) view.findViewById(R.id.listview);
        deviceAdapter = new DeviceAdapter(adapterList, new DeviceAdapter.IOnDeviceClick() {
            @Override
            public void onClickSwitch(String dk, boolean on) {
                devicePresenter.publishSwitch(getActivity(), dk, on);
            }

            @Override
            public void onClickDetail(String dk) {
                startActivity();
            }
        });
        listView.setAdapter(deviceAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                PluginLoad.load(DataManeger.getInstance().deviceInfoMap.get(adapterList.get(position).dk));
            }
        });

        devicePresenter.connectBroker(getActivity());
        subMqtt();
        return view;
    }

    public void getAreaList() {
        devicePresenter.getArea(new Iview() {
            @Override
            public void onSuccess(Object object) {

                getDeviceList();
            }

            @Override
            public void onFailed(String errorStr) {

            }

            @Override
            public void showProgress(boolean show) {

            }
        });

    }

    public void getDeviceList() {
        devicePresenter.getDeviceList(getActivity(),areaId, deviceTypeCode, 1, 100, new Iview() {
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
        });
    }

    private void update() {
        adapterList.clear();
        if (DataManeger.getInstance().deviceInfoArrayList != null) {
            for (DeviceInfo deviceInfo : DataManeger.getInstance().deviceInfoArrayList) {
                CommontAdapterData commontAdapterData = new CommontAdapterData(deviceInfo.device_name, 0);
                commontAdapterData.dk = deviceInfo.device_key;
                adapterList.add(commontAdapterData);
            }
        }
        deviceAdapter.notifyDataSetChanged();
    }

    private void initTypeView(View view) {
        ly_area = view.findViewById(R.id.ly_area);
        ly_type = view.findViewById(R.id.ly_devicetype);
        text_area = (TextView) view.findViewById(R.id.text_area);
        text_type = (TextView) view.findViewById(R.id.text_device_type);
        popView = getActivity().getLayoutInflater().inflate(
                R.layout.layout_popwindow, null);
        netbad = view.findViewById(R.id.net);

        popupWindow = new PopupWindow(popView,
                WindowManager.LayoutParams.FILL_PARENT, WindowManager.LayoutParams.FILL_PARENT,
                true);
        ly_area.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final List<String> nam = new ArrayList<>();
                for (AreaInfo areaInfo : DataManeger.getInstance().areaInfoArrayList) {
                    nam.add(areaInfo.area_name);
                }

                showPopWind(nam, new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        popupWindow.dismiss();
                        text_area.setText(nam.get(position));
                        areaId = DataManeger.getInstance().areaInfoArrayList.get(position).id;
                        getDeviceList();

                    }
                });
                popupWindow.showAsDropDown(ly_area);
            }
        });
        ly_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final List<String> nam = new ArrayList<>();
                for (DeviceTypeInfo deviceTypeInfo : DataManeger.getInstance().deviceTypeArrayList) {
                    nam.add(deviceTypeInfo.type_name);
                }
                showPopWind(nam, new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        popupWindow.dismiss();
                        text_type.setText(DataManeger.getInstance().deviceTypeArrayList.get(position).type_name);
                        deviceTypeCode = DataManeger.getInstance().deviceTypeArrayList.get(position).type_code;
                        getDeviceList();

                    }
                });
                popupWindow.showAsDropDown(ly_type);
            }
        });
    }


    private void showPopWind(List<String> names, AdapterView.OnItemClickListener listener) {

        ListView popListview = (ListView) popView
                .findViewById(R.id.pop_listview);
        SpinnerListAdapter adapter = new SpinnerListAdapter(
                getActivity(), names);
        popListview.setAdapter(adapter);
        popListview.setOnItemClickListener(listener);
        popListview.setOnKeyListener(new View.OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                popupWindow.dismiss();
                return false;
            }
        });
        popView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                popupWindow.dismiss();
                return false;
            }
        });
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);

    }

    private void subMqtt() {
        RxBus.getDefault().toObserverable(MqttConnectStatus.class).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<MqttConnectStatus>() {
            @Override
            public void call(MqttConnectStatus mqttConnectStatus) {
                if (mqttConnectStatus.getConnectStatus() == MqttConnectStatus.CONNECT_SUCCESS) {
                    getAreaList();
                    netbad.setVisibility(View.GONE);
                } else {
                    netbad.setVisibility(View.VISIBLE);

                }

            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                Log.e(KeyUtil.KEY_TAG, "mqtt throwable");
            }
        });

    }

    private void startActivity() {
//        startActivity(new Intent(getActivity(),));

    }
}
