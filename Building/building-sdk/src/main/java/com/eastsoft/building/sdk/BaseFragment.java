package com.eastsoft.building.sdk;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ehomeclouds.eastsoft.channel.http.api.HttpCloudService;

/**
 * Created by ll on 2016/1/20.
 */
public class BaseFragment extends Fragment {
   protected HttpCloudService httpCloudService;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        httpCloudService=new HttpCloudService(getActivity());
    }

    protected void  showToast(String str){
        Toast.makeText(getActivity(),str,Toast.LENGTH_SHORT).show();
    }


}
