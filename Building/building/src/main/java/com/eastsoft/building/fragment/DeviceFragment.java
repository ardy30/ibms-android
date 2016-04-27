package com.eastsoft.building.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.eastsoft.building.sdk.MyDialog;
import com.ehomeclouds.eastsoft.channel.http.CloudService.Iview;
import com.ehomeclouds.eastsoft.channel.http.response.AreaInfo;
import com.ehomeclouds.eastsoft.channel.http.response.DeviceInfo;
import com.ehomeclouds.eastsoft.channel.http.response.DeviceTypeInfo;
import com.ehomeclouds.eastsoft.channel.mqtt.model.MqttConnectStatus;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import presenter.DevicePresenter;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by ll on 2016/3/31.
 */
public class DeviceFragment extends BaseFragment implements Iview {
    private PullToRefreshListView listView;
    private List<CommontAdapterData> adapterList = new LinkedList<>();
    private DeviceAdapter deviceAdapter;
    private View ly_area, ly_type, netbad;
    private PopupWindow popupWindow;
    private View popView;
    TextView text_area, text_type;
    private DevicePresenter devicePresenter;
    private long areaId;
    private String deviceTypeCode = "";
    int posArea, posType;
    View view;
    View arrayArea;
    View arrayType;
    private boolean isRefresh = true;
    private int pageNum = 1;
    private Dialog dialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        devicePresenter = new DevicePresenter(httpCloudService, new Iview() {
            @Override
            public void onSuccess(Object object) {
                String dk= (String) object;
                DeviceInfo deviceInfo=DataManeger.getInstance().deviceInfoMap.get(dk);

                for (CommontAdapterData commontAdapterData:adapterList){
                    if (commontAdapterData.dk.equals(dk)){
                        boolean sel = Boolean.parseBoolean(deviceInfo.paramMap.get(KeyUtil.KEY_SWITCH_CH + commontAdapterData.channel).toString());
                        commontAdapterData.selected=sel;
                    }
                }
                deviceAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailed(String errorStr) {

            }

            @Override
            public void showProgress(boolean show) {

            }
        });
        devicePresenter.connectBroker(getActivity());

        subMqtt();
        getAreaList();
        getTypeList();
    }

//        private void registerNetBad() {
//        RxBus.getDefault().toObserverable(MqttConnectStatus.class).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<MqttConnectStatus>() {
//            @Override
//            public void call(MqttConnectStatus s) {
//                if (s.getConnectStatus() == MqttConnectStatus.CONNECT_SUCCESS) {
//                    textNetbad.setVisibility(View.GONE);
//                } else {
//                    textNetbad.setVisibility(View.VISIBLE);
//                }
//            }
//        }, new Action1<Throwable>() {
//            @Override
//            public void call(Throwable throwable) {
//
//                Log.d("subscription", throwable.toString());
//
//            }
//        });


    //    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.f_device, container, false);
            TextView textTitle = (TextView) view.findViewById(R.id.title);
            textTitle.setText(getString(R.string.title_device));
            initTypeView(view);
            listView = (PullToRefreshListView) view.findViewById(R.id.listview);
            deviceAdapter = new DeviceAdapter(adapterList, new DeviceAdapter.IOnDeviceClick() {
                @Override
                public void onClickSwitch(int pos, boolean on) {
                    devicePresenter.publishSwitch(getActivity(),adapterList.get(pos).dk, adapterList.get(pos).channel, on);
                }

                @Override
                public void onClickDetail(String dk) {
                    devicePresenter.startPluginActivity(dk);
                }
            });
            listView.setAdapter(deviceAdapter);
            listView.setMode(PullToRefreshBase.Mode.BOTH);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    PluginLoad.load(getActivity(), DataManeger.getInstance().deviceInfoMap.get(adapterList.get((int) id).dk));
                }
            });
            listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
                @Override
                public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                    isRefresh = true;
                    pageNum = 1;
                    getDeviceList(1);
                }

                @Override
                public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                    pageNum++;
                    isRefresh = false;
                    getDeviceList(pageNum);
                }
            });
            arrayArea = view.findViewById(R.id.iv_area_array);
            arrayType = view.findViewById(R.id.iv_type_array);
        }
        return view;
    }

    public void getAreaList() {
        devicePresenter.getArea(new Iview() {
            @Override
            public void onSuccess(Object object) {

                showToast("获取区域列表成功");
//                getDeviceList();
            }

            @Override
            public void onFailed(String errorStr) {
                showToast(errorStr);
            }

            @Override
            public void showProgress(boolean show) {
            }
        });

    }

    public void getTypeList() {
        devicePresenter.getType(new Iview() {
            @Override
            public void onSuccess(Object object) {
                showToast("获取类型成功");
            }

            @Override
            public void onFailed(String errorStr) {

                showToast(errorStr);
            }

            @Override
            public void showProgress(boolean show) {

            }
        });
    }

    public void getDeviceList(int count) {

        devicePresenter.getDeviceList(getActivity(), areaId, deviceTypeCode, count, DataManeger.PAGESIZE, new Iview() {
            @Override
            public void onSuccess(Object object) {
                dialog.dismiss();
                listView.onRefreshComplete();
                update((ArrayList<DeviceInfo>) object);
            }

            @Override
            public void onFailed(String errorStr) {
                showToast(errorStr);
                dialog.dismiss();
                listView.onRefreshComplete();


            }

            @Override
            public void showProgress(boolean show) {

            }
        });
    }


    private void update(ArrayList<DeviceInfo> list) {
        if (isRefresh){
            adapterList.clear();
        }
        for (DeviceInfo deviceInfo : list) {
            if (!devicePresenter.typeMap.containsKey(deviceInfo.device_key)){
                continue;
            }
            CommontAdapterData commontAdapterData = new CommontAdapterData(deviceInfo.device_name, 0);
            commontAdapterData.dk = deviceInfo.device_key;
            commontAdapterData.showDetail = devicePresenter.showDetail(deviceInfo);
            commontAdapterData.channel= (int) deviceInfo.channel;
            if (DevicePresenter.switchTypeMap.containsKey(deviceInfo.device_type_code.substring(0, 7))) {
                boolean sel = Boolean.parseBoolean(deviceInfo.paramMap.get(DevicePresenter.channelMap.get(deviceInfo.channel)).toString());
                commontAdapterData.selected = sel;
            }
            adapterList.add(commontAdapterData);
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
                nam.add("全部");
                if (DataManeger.getInstance().areaInfoArrayList != null) {
                    for (AreaInfo areaInfo : DataManeger.getInstance().areaInfoArrayList) {
                        nam.add(areaInfo.areaName);
                    }
                }

                arrayArea.setSelected(true);
                showPopWind(nam, new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        arrayArea.setSelected(false);

                        popupWindow.dismiss();
                        posArea = position;
                        text_area.setText(nam.get(position));
                        if (position == 0) {
                            areaId = 0;
                        } else {
                            areaId = DataManeger.getInstance().areaInfoArrayList[position - 1].id;
                        }
                        getDeviceList(1);
                        pageNum=1;
                        isRefresh=true;
                        dialog= MyDialog.getStaticDialog(getActivity());
                        dialog.show();



                    }
                }, posArea);
                popupWindow.showAsDropDown(ly_area);
            }
        });
        ly_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                arrayType.setSelected(true);
                final List<String> nam = new ArrayList<>();
                nam.add("全部");
                if (DataManeger.getInstance().deviceTypeArrayList != null) {
                    for (DeviceTypeInfo deviceTypeInfo : DataManeger.getInstance().deviceTypeArrayList) {
                        nam.add(deviceTypeInfo.type_name);
                    }
                }

                showPopWind(nam, new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        arrayType.setSelected(false);

                        popupWindow.dismiss();
                        posType = position;
                        text_type.setText(nam.get(position));
                        if (position == 0) {
                            deviceTypeCode = "";
                        } else {

                            deviceTypeCode = DataManeger.getInstance().deviceTypeArrayList[position - 1].type_code;
                        }
                        getDeviceList(1);
                        pageNum=1;
                        isRefresh=true;
                        dialog= MyDialog.getStaticDialog(getActivity());
                        dialog.show();


                    }
                }, posType);
                popupWindow.showAsDropDown(ly_type);
            }
        });
    }


    private void showPopWind(List<String> names, AdapterView.OnItemClickListener listener, int checkPos) {

        ListView popListview = (ListView) popView
                .findViewById(R.id.pop_listview);
        SpinnerListAdapter adapter = new SpinnerListAdapter(
                getActivity(), names);
        adapter.setCheckedPos(checkPos);
        popListview.setAdapter(adapter);
        popListview.setOnItemClickListener(listener);
        popListview.setOnKeyListener(new View.OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                arrayArea.setSelected(false);
                arrayType.setSelected(false);
                popupWindow.dismiss();
                return false;
            }
        });
        popView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                arrayArea.setSelected(false);
                arrayType.setSelected(false);
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

                    getDeviceList(1);
                    pageNum=1;
                    dialog= MyDialog.getStaticDialog(getActivity());
                    dialog.show();

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

    @Override
    public void onSuccess(Object object) {

        update((ArrayList<DeviceInfo>) object);
    }

    @Override
    public void onFailed(String errorStr) {

    }

    @Override
    public void showProgress(boolean show) {

    }
}
