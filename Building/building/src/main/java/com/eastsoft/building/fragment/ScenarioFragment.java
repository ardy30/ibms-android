package com.eastsoft.building.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.eastsoft.building.R;
import com.eastsoft.building.adapter.ScenarioAdapter;
import com.eastsoft.building.adapter.CommontAdapterData;
import com.eastsoft.building.sdk.BaseFragment;
import com.eastsoft.building.sdk.DataManeger;
import com.ehomeclouds.eastsoft.channel.http.CloudService.Iview;
import com.ehomeclouds.eastsoft.channel.http.response.ScenarioInfo;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by ll on 2016/3/31.
 */
public class ScenarioFragment extends BaseFragment implements Iview {
    private ListView listView;
    private List<CommontAdapterData> adapterList=new LinkedList<>();
    private ScenarioAdapter scenarioAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.f_scenario, container, false);
        listView = (ListView) view.findViewById(R.id.listview);
        getAdapterData();
        scenarioAdapter=new ScenarioAdapter(adapterList, new ScenarioAdapter.IOnStartScenario() {
            @Override
            public void onStartScenario(long id) {
                httpCloudService.startScenario(DataManeger.getInstance().userId,id,ScenarioFragment.this);
            }
        });
        listView.setAdapter(scenarioAdapter);
        return view;
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
    }
    @Override
    public void onFailed(String errorStr) {
        showToast(errorStr);
    }

    @Override
    public void showProgress(boolean show) {

    }
}
