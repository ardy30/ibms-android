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
import com.eastsoft.building.activity.MainActivity;
import com.eastsoft.building.adapter.ScenarioAdapter;
import com.eastsoft.building.adapter.CommontAdapterData;
import com.eastsoft.building.sdk.BaseFragment;
import com.eastsoft.building.sdk.DataManeger;
import com.eastsoft.building.sdk.IntentUtil;
import com.eastsoft.building.sdk.MyDialog;
import com.eastsoft.building.sdk.UtilityInfo;
import com.ehomeclouds.eastsoft.channel.http.CloudService.Iview;
import com.ehomeclouds.eastsoft.channel.http.response.ScenarioInfo;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by ll on 2016/3/31.
 */
public class ScenarioFragment extends BaseFragment implements Iview {
    private PullToRefreshListView listView;
    private List<CommontAdapterData> adapterList=new LinkedList<>();
    private ScenarioAdapter scenarioAdapter;
    View view;
    private boolean isRefresh=true;
    private int pageNum=1;
private Dialog dialog;
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view==null){
             view = inflater.inflate(R.layout.f_scenario, container, false);
            TextView textTitle= (TextView) view.findViewById(R.id.title);
            textTitle.setText(getString(R.string.title_scenario));
            listView = (PullToRefreshListView) view.findViewById(R.id.listview);
            getAdapterData();
            scenarioAdapter=new ScenarioAdapter(adapterList, new ScenarioAdapter.IOnStartScenario() {
                @Override
                public void onStartScenario(long id) {
                    dialog=MyDialog.getStaticDialog(getActivity());
                    dialog.show();
                    httpCloudService.startScenario(DataManeger.getInstance().userId,id,ScenarioFragment.this);
                }
            });
            listView.setAdapter(scenarioAdapter);
            listView.setMode(PullToRefreshBase.Mode.BOTH);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(getActivity(), DeviceListActivity.class);
                    intent.putExtra(IntentUtil.INTENT_ID, adapterList.get((int) id).id);
                    intent.putExtra(IntentUtil.INTENT_TYPE, UtilityInfo.TYPE_SCENARIO);
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

    private void getData(int i) {
        httpCloudService.getScenarioList(DataManeger.getInstance().userId, i, DataManeger.PAGESIZE, new Iview() {
            @Override
            public void onSuccess(Object object) {
                listView.onRefreshComplete();

                if (isRefresh){
                    adapterList.clear();
                }
                for(ScenarioInfo scenarioInfo:(ArrayList<ScenarioInfo>) object){
                    CommontAdapterData scenarioAdapterData=new CommontAdapterData(scenarioInfo.name,scenarioInfo.id);
                    adapterList.add(scenarioAdapterData);
                }
                scenarioAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailed(String errorStr) {
                listView.onRefreshComplete();

                showToast(errorStr);
            }

            @Override
            public void showProgress(boolean show) {

            }
        });
    }

    private void getAdapterData() {
        if (DataManeger.getInstance().scenarioInfoArrayList!=null){
            adapterList.clear();
            for(ScenarioInfo scenarioInfo:DataManeger.getInstance().scenarioInfoArrayList){
                CommontAdapterData scenarioAdapterData=new CommontAdapterData(scenarioInfo.name,scenarioInfo.id);
                adapterList.add(scenarioAdapterData);
            }
        }
    }
    @Override
    public void onSuccess(Object object) {
        showToast(getString(R.string.success));
        dialog.dismiss();
    }
    @Override
    public void onFailed(String errorStr) {
        showToast(errorStr);
        dialog.dismiss();
    }

    @Override
    public void showProgress(boolean show) {

    }
}
