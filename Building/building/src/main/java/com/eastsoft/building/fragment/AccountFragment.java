package com.eastsoft.building.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.eastsoft.building.R;
import com.eastsoft.building.sdk.BaseFragment;

/**
 * 
 * @author   ll
 * @version  1.0.0
 * @2012-5-31 下午4:24:32
 */
public class AccountFragment extends BaseFragment implements View.OnClickListener{
	private String userEmilName;
	private RelativeLayout aboutUs,advice,advanceSetting;
	private Intent intent;
	private Context context;
	private TextView title;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view=inflater.inflate(R.layout.f_account,container,false);
		initData(view);
		return view;
	}

	private void initData(View view){
//		aboutUs=(RelativeLayout)view.findViewById(R.id.about_app);
//		advice=(RelativeLayout)view.findViewById(R.id.feedback);
//		title=(TextView)view.findViewById(R.id.mtitle);
//		title.setText(R.string.person_center);
//		advanceSetting=(RelativeLayout)view.findViewById(R.id.adviance_setting);
//		aboutUs.setOnClickListener(this);
//		advice.setOnClickListener(this);
//		advanceSetting.setOnClickListener(this);
//		intent=new Intent();
//		context=this.getActivity();
	}

	@Override
	public void onClick(View v) {
//		switch (v.getId()){
//			case R.id.about_app:
////				startAccountActivity(context,new AboutUSActivity());
//				break;
//			case R.id.adviance_setting:
////				startAccountActivity(context,new AdvancedSettingActivity());
//				break;
//			case R.id.feedback:
////				startAccountActivity(context,new FeedBackActivity());
//				break;
//			default:
//				break;
//
//		}
	}

	private void  startAccountActivity(Context context,Object object){
		intent.setClass(context,object.getClass());
		startActivity(intent);
	}
}

