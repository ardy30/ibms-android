package com.eastsoft.building.activity;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.eastsoft.building.R;
import com.eastsoft.building.sdk.BaseActivity;


public class AboutUSActivity extends BaseActivity {

	private RelativeLayout checkUpdateLayout;
	private TextView newVersionFlagTv;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aboutus_main);
		restoreActionBar(getString(R.string.about_us),R.drawable.array_left_default, 0,"","");
		setOnclick(new OnTitleClick());
//		initView();
	}

//	private void initView() {
//		TextView versionTextview=(TextView) findViewById(R.id.tv_version);
//		try {
//			PackageManager manager=getPackageManager();
//			PackageInfo info;
//			info = manager.getPackageInfo(getPackageName(),0);
//			String version=info.versionName;
//			versionTextview.setText(version);
//		} catch (NameNotFoundException e) {
//			e.printStackTrace();
//		}
//		newVersionFlagTv=(TextView)findViewById(R.id.new_version_flag_tv);
//		if(UpdateTool.hasNewVersion){
//			newVersionFlagTv.setVisibility(View.VISIBLE);
//		}else{
//			newVersionFlagTv.setVisibility(View.INVISIBLE);
//		}
//
//		checkUpdateLayout = (RelativeLayout) findViewById(R.id.check_update_relativelayout);
//
//		checkUpdateLayout.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View arg0) {
//				UpdateTool updateTool = new UpdateTool(AboutUSActivity.this);
//				updateTool.checkUpdate("mobile");
//			}
//		});
//	}

	@Override
	protected void onResume() {
		super.onResume();
		// check channel state

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
//		setOnclick(new OnTitleClick());
		return true;
	}

	@Override
	protected void onDestroy() {

		super.onDestroy();
	}

	class OnTitleClick implements IOnTitleClick {

		@Override
		public void OnLeftClick(View view) {

			AboutUSActivity.this.finish();
		}

		@Override
		public void OnRightClick(View view) {

		}

	}

}
