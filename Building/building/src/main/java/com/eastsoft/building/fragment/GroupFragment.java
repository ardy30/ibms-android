package com.eastsoft.building.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.eastsoft.building.R;
import com.eastsoft.building.activity.DeviceListActivity;
import com.eastsoft.building.adapter.GroupAdapter;
import com.eastsoft.building.adapter.CommontAdapterData;
import com.eastsoft.building.sdk.BaseFragment;
import com.eastsoft.building.sdk.DataManeger;
import com.eastsoft.building.sdk.IntentUtil;
import com.eastsoft.building.sdk.MyDialog;
import com.eastsoft.building.sdk.UtilityInfo;
import com.ehomeclouds.eastsoft.channel.http.CloudService.Iview;
import com.ehomeclouds.eastsoft.channel.http.base.Util.GenerateUtils;
import com.ehomeclouds.eastsoft.channel.http.response.GroupInfo;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by ll on 2016/3/31.
 */
public class GroupFragment extends BaseFragment {
    private PullToRefreshListView listView;
    private List<CommontAdapterData> adapterList=new LinkedList<>();
    private GroupAdapter groupAdapter;
    View view;
    private boolean isRefresh=true;
    private int pageNum=1;
    private Dialog dialog;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dialog= MyDialog.getStaticDialog(getActivity());
        dialog.show();
        getData(1);
    }

    private void getData(int pageNum) {
        httpCloudService.getGroupList(DataManeger.getInstance().userId, pageNum, GenerateUtils.PAGE_SIZE, new Iview() {
            @Override
            public void onSuccess(Object object) {
                dialog.dismiss();
                ArrayList<GroupInfo> groupInfoArrayList = (ArrayList<GroupInfo>) object;
                handleData(groupInfoArrayList);
                listView.onRefreshComplete();
            }

            @Override
            public void onFailed(String errorStr) {
                dialog.dismiss();
                listView.onRefreshComplete();
                showToast(errorStr);
            }

            @Override
            public void showProgress(boolean show) {

            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view==null){
            view = inflater.inflate(R.layout.f_scenario, container, false);
            TextView textTitle= (TextView) view.findViewById(R.id.title);
            textTitle.setText(getString(R.string.title_group));
            listView = (PullToRefreshListView) view.findViewById(R.id.listview);
            listView.setRefreshing();
            groupAdapter =new GroupAdapter(adapterList, new GroupAdapter.IOnGroupClick() {
                @Override
                public void onClickGroup(long id, boolean on) {
                    dialog= MyDialog.getStaticDialog(getActivity());
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
            listView.setMode(PullToRefreshBase.Mode.BOTH);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(getActivity(), DeviceListActivity.class);
                    intent.putExtra(IntentUtil.INTENT_ID, adapterList.get((int) id).id);
                    intent.putExtra(IntentUtil.INTENT_TYPE, UtilityInfo.TYPE_GROUP);
                    startActivity(intent);
                }
            });
            listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
                @Override
                public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                    isRefresh = true;
                    pageNum = 1;
                    getData(1);
                }

                @Override
                public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                    pageNum++;
                    isRefresh = false;
                    getData(pageNum);
                }
            });

        }

        return view;
    }
    private void handleData( ArrayList<GroupInfo> groupInfoArrayList) {

        if (groupInfoArrayList!=null){
            if (isRefresh){
                adapterList.clear();
            }
            for (GroupInfo groupInfo:groupInfoArrayList){
                CommontAdapterData scenarioAdapterData=new CommontAdapterData(groupInfo.group_name,groupInfo.id);
                adapterList.add(scenarioAdapterData);
            }
            groupAdapter.notifyDataSetChanged();
        }
    }


}
