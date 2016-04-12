package com.eastsoft.building.activity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.eastsoft.building.R;
import com.eastsoft.building.sdk.BaseActivity;


public class FeedBackActivity extends BaseActivity {

	private WebView feedBackWeb;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.feedback);
		restoreActionBar(getString(R.string.feedback),R.drawable.array_left_default,0, "", "");
		feedBackWeb = (WebView) findViewById(R.id.feedback_web);
		feedBackWeb.getSettings().setJavaScriptEnabled(true);
		feedBackWeb.setWebViewClient(new WebViewClient() {
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}
		});
		feedBackWeb.loadUrl("http://115.28.176.106:8080/CustomerFeedback/tocollect.action");
		setOnclick(new OnTitleClick());
	}

	class OnTitleClick implements IOnTitleClick {

		@Override
		public void OnLeftClick(View view) {

			FeedBackActivity.this.finish();
		}

		@Override
		public void OnRightClick(View view) {

		}

	}
	
	@Override
	protected void onResume() {
		super.onResume();
		// check channel state
		//ChannelManager.registerListener(channelListener);
	}

	@Override
	protected void onDestroy() {
		//ChannelManager.unRegisterListner(channelListener);
		super.onDestroy();
	}
}
