package com.eastsoft.building.fragment;

import android.os.Bundle;
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
import com.eastsoft.building.model.DeviceType;
import com.eastsoft.building.sdk.BaseFragment;
import com.eastsoft.building.sdk.DataManeger;
import com.eastsoft.building.sdk.KeyUtil;
import com.ehomeclouds.eastsoft.channel.http.CloudService.Iview;
import com.ehomeclouds.eastsoft.channel.http.response.AreaInfo;
import com.ehomeclouds.eastsoft.channel.http.response.DeviceInfo;
import com.ehomeclouds.eastsoft.channel.http.response.DeviceTypeInfo;
import com.ehomeclouds.eastsoft.channel.mqtt.MqttManeger;
import com.ehomeclouds.eastsoft.channel.mqtt.util.MqttTopicManeger;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import presenter.DevicePresenter;

/**
 * Created by ll on 2016/3/31.
 */
public class DeviceFragment extends BaseFragment {
    private ListView listView;
    private List<CommontAdapterData> adapterList = new LinkedList<>();
    private DeviceAdapter deviceAdapter;
    private View ly_area,ly_type;
    private PopupWindow popupWindow;
    private  View popView;
    private int checkedPos=0;
    TextView text_area,text_type;
    private DevicePresenter devicePresenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.f_device, container, false);
        devicePresenter=new DevicePresenter(httpCloudService);
        initTypeView(view);
        listView = (ListView) view.findViewById(R.id.listview);
        deviceAdapter = new DeviceAdapter(adapterList, new DeviceAdapter.IOnDeviceClick() {
            @Override
            public void onClickSwitch(int pos, boolean on) {
               devicePresenter.publishSwitch(pos, on);
            }

            @Override
            public void onClickDetail(int pos) {
                startActivity();
            }
        });
        listView.setAdapter(deviceAdapter);
        return view;
    }

    private void initTypeView(View view) {
        ly_area=view.findViewById(R.id.ly_area);
        ly_type=view.findViewById(R.id.ly_devicetype);
        text_area= (TextView) view.findViewById(R.id.text_area);
        text_type= (TextView) view.findViewById(R.id.text_device_type);
        popView=getActivity().getLayoutInflater().inflate(
                R.layout.layout_popwindow, null);

        popupWindow = new PopupWindow(popView,
                WindowManager.LayoutParams.FILL_PARENT, WindowManager.LayoutParams.FILL_PARENT,
                true);
        ly_area.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showPopWind(areaNames, new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        checkedPos=position;
                        popupWindow.dismiss();
                        text_area.setText(areaNames.get(position));

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
                        checkedPos = position;
                        popupWindow.dismiss();
                        text_type.setText(nam.get(checkedPos));

                    }
                });
                popupWindow.showAsDropDown(ly_type);
            }
        });
    }



    private void update(ArrayList<DeviceInfo> deviceInfoArrayList) {
        adapterList.clear();
        if (deviceInfoArrayList!=null){
            for (DeviceInfo deviceInfo:deviceInfoArrayList){
                adapterList.add(new CommontAdapterData(deviceInfo.device_name,0));
            }
        }
        deviceAdapter.notifyDataSetChanged();
    }

    private void showPopWind(List<String> names,AdapterView.OnItemClickListener listener) {

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



    private void startActivity() {
//        startActivity(new Intent(getActivity(),));

    }
}
