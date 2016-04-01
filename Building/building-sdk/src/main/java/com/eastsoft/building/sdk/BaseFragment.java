package com.eastsoft.building.sdk;

import android.support.v4.app.Fragment;
import android.widget.Toast;

/**
 * Created by ll on 2016/1/20.
 */
public class BaseFragment extends Fragment {
    protected void  showToast(String str){
        Toast.makeText(getActivity(),str,Toast.LENGTH_SHORT).show();
    }


}
