package com.eastsoft.building.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.eastsoft.building.R;
import com.eastsoft.building.activity.DeviceListActivity;
import com.eastsoft.building.adapter.GroupAdapter;
import com.eastsoft.building.adapter.ScenarioAdapter;
import com.eastsoft.building.adapter.CommontAdapterData;
import com.eastsoft.building.sdk.BaseFragment;
import com.eastsoft.building.sdk.DataManeger;
import com.eastsoft.building.sdk.IntentUtil;
import com.eastsoft.building.sdk.UtilityInfo;
import com.ehomeclouds.eastsoft.channel.http.CloudService.Iview;
import com.ehomeclouds.eastsoft.channel.http.base.Util.GenerateUtils;
import com.ehomeclouds.eastsoft.channel.http.response.GroupInfo;
import com.ehomeclouds.eastsoft.channel.http.response.ScenarioInfo;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by ll on 2016/3/31.
 */
public class GroupFragment extends FragmentSubClass {
    private ListView listView;
    private List<CommontAdapterData> adapterList=new LinkedList<>();
    private GroupAdapter groupAdapter;
    private Dialog dialog;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.f_scenario, container, false);
        TextView textTitle= (TextView) view.findViewById(R.id.title);
        textTitle.setText(getString(R.string.title_group));
        dialog=getStaticDialog(getActivity(),getString(R.string.in_control),null);
        listView = (ListView) view.findViewById(R.id.listview);

        groupAdapter =new GroupAdapter(adapterList, new GroupAdapter.IOnGroupClick() {
            @Override
            public void onClickGroup(long id, boolean on) {
                dialog.show();
                httpCloudService.ctrlGroup(DataManeger.getInstance().userId, id, on, new Iview() {
                    @Override
                    public void onSuccess(Object object) {
                        dialog.dismiss();
                        showToast(getString(R.string.success));
                    }

                    @Override
                    public void onFailed(String errorStr) {
                        dialog.dismiss();
                        showToast(errorStr);
                    }

                    @Override
                    public void showProgress(boolean show) {

                    }
                });
            }
        });
        listView.setAdapter(groupAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(getActivity(), DeviceListActivity.class);
                intent.putExtra(IntentUtil.INTENT_ID,adapterList.get(position).id);
                intent.putExtra(IntentUtil.INTENT_TYPE, UtilityInfo.TYPE_GROUP);
                startActivity(intent);
            }
        });
        return view;
    }
    private void handleData( ArrayList<GroupInfo> groupInfoArrayList) {

        if (groupInfoArrayList!=null){
            adapterList.clear();
            for (GroupInfo groupInfo:groupInfoArrayList){
                CommontAdapterData scenarioAdapterData=new CommontAdapterData(groupInfo.group_name,groupInfo.id);
                adapterList.add(scenarioAdapterData);
            }
            groupAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        httpCloudService.getGroupList(DataManeger.getInstance().userId, 1, GenerateUtils.PAGE_SIZE, new Iview() {
            @Override
            public void onSuccess(Object object) {
                ArrayList<GroupInfo> groupInfoArrayList= (ArrayList<GroupInfo>) object;
               handleData(groupInfoArrayList);
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


}
