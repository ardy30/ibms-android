package com.eastsoft.building.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.eastsoft.building.R;
import com.eastsoft.building.adapter.ScenarioAdapter;
import com.eastsoft.building.adapter.ScenarioAdapterData;
import com.eastsoft.building.sdk.BaseFragment;
import com.eastsoft.building.sdk.DataManeger;
import com.ehomeclouds.eastsoft.channel.http.CloudService.Iview;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by ll on 2016/3/31.
 */
public class ScenarioFragment extends BaseFragment implements Iview {
    private ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.f_scenario, container, false);
        listView = (ListView) view.findViewById(R.id.listview);
        listView.setAdapter(new ScenarioAdapter(getAdapterData(), new ScenarioAdapter.IOnStartScenario() {
            @Override
            public void onStartScenario(long id) {
                httpCloudService.startScenario(DataManeger.getInstance().userId,id,ScenarioFragment.this);
            }
        }));
        return view;
    }

    private List<ScenarioAdapterData> getAdapterData() {
        List<ScenarioAdapter> adapterList=new LinkedList<>();
        httpCloudService.getScenarioList(DataManeger.getInstance().userId,1,500,);
//        for ()
        return null;
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
