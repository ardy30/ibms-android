package com.eastsoft.building.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.eastsoft.building.R;
import com.eastsoft.building.activity.AboutUSActivity;
import com.eastsoft.building.activity.FeedBackActivity;
import com.eastsoft.building.sdk.BaseFragment;
import com.ehomeclouds.eastsoft.channel.http.base.Util.StringStaticUtils;

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
	private TextView title;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view=inflater.inflate(R.layout.f_account,container,false);
		TextView textTitle= (TextView) view.findViewById(R.id.title);
		textTitle.setText(getString(R.string.title_account));
		initData(view);
		TextView account= (TextView) view.findViewById(R.id.account);
		account.setText(getUserEmilName());
		return view;
	}

	private void initData(View view){
		aboutUs=(RelativeLayout)view.findViewById(R.id.about_app);
		advice=(RelativeLayout)view.findViewById(R.id.feedback);
		aboutUs.setOnClickListener(this);
		advice.setOnClickListener(this);
	}
	private String getUserEmilName(){
		String userEmilName="";
		SharedPreferences sharedPreferences=getActivity().getSharedPreferences(StringStaticUtils.SHAREP_NAME, Context.MODE_PRIVATE);
		userEmilName=sharedPreferences.getString(StringStaticUtils.SHAREP_EMAIL,"");
		return  userEmilName;
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.about_app:
				startActivity(new Intent(getActivity(), AboutUSActivity.class));
				break;

			case R.id.feedback:
				startActivity(new Intent(getActivity(), FeedBackActivity.class));
				break;
			default:
				break;

		}
	}


}

