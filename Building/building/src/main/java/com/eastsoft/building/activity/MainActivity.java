/**
 * Program  : ViewPagerActivity.java
 * Author   : qianj
 * Create   : 2012-5-31 下午2:02:15
 */

package com.eastsoft.building.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;


import com.eastsoft.building.R;
import com.eastsoft.building.fragment.AccountFragment;
import com.eastsoft.building.fragment.DeviceFragment;
import com.eastsoft.building.fragment.GroupFragment;
import com.eastsoft.building.fragment.ScenarioFragment;
import com.eastsoft.building.model.RxBus;
import com.eastsoft.building.sdk.BaseActivity;
import com.eastsoft.building.sdk.DataManeger;
import com.ehomeclouds.eastsoft.channel.mqtt.MqttManeger;
import com.ehomeclouds.eastsoft.channel.mqtt.model.MqttConnectStatus;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * @author qianj
 * @version 1.0.0
 * @2012-5-31 下午2:02:15
 */
public class MainActivity extends BaseActivity {
    //定义FragmentTabHost对象
    private FragmentTabHost mTabHost;

    //定义一个布局
    private LayoutInflater layoutInflater;

    //定义数组来存放Fragment界面
    private Class fragmentArray[] = { ScenarioFragment.class,GroupFragment.class, DeviceFragment.class,AccountFragment.class};

//	//定义数组来存放按钮图片
	private int mImageViewArray[] = {R.drawable.tab_scenario_sel,R.drawable.tab_group_sel,R.drawable.tab_device_sel,
			R.drawable.tab_account_sel};

    //Tab选项卡的文字
    private String mTextviewArray[] = {"情景模式","分组控制","单控","个人中心"};

    private TextView textNetbad;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
//        registerNetBad();

    }



//    private void registerNetBad() {
//        RxBus.getDefault().toObserverable(MqttConnectStatus.class).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<MqttConnectStatus>() {
//            @Override
//            public void call(MqttConnectStatus s) {
//                if (s.getConnectStatus() == MqttConnectStatus.CONNECT_SUCCESS) {
//                    textNetbad.setVisibility(View.GONE);
//                } else {
//                    textNetbad.setVisibility(View.VISIBLE);
//                }
//            }
//        }, new Action1<Throwable>() {
//            @Override
//            public void call(Throwable throwable) {
//
//                Log.d("subscription", throwable.toString());
//
//            }
//        });
//
//
//    }

    /**
     * 初始化组件
     */
    private void initView() {
        //实例化布局对象
        layoutInflater = LayoutInflater.from(this);
        textNetbad = (TextView) findViewById(R.id.net_bad);
        textNetbad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MqttManeger.getInstance(MainActivity.this).connect(DataManeger.getInstance().brokerUrl,
                        DataManeger.getInstance().brokerUsername, DataManeger.getInstance().brokerPassword);
            }
        });
        //实例化TabHost对象，得到TabHost
        mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);
        //取消掉分割栏
        mTabHost.getTabWidget().setDividerDrawable(R.color.white);
        //得到fragment的个数
        int count = fragmentArray.length;

        for (int i = 0; i < count; i++) {
            //为每一个Tab按钮设置图标、文字和内容
            TabHost.TabSpec tabSpec = mTabHost.newTabSpec(mTextviewArray[i]).setIndicator(getTabItemView(i));
            //将Tab按钮添加进Tab选项卡中
            mTabHost.addTab(tabSpec, fragmentArray[i], null);
            //设置Tab按钮的背景
//			mTabHost.getTabWidget().getChildAt(i).setBackgroundResource(mImageViewArray[i]);
        }
    }

    /**
     * 给Tab按钮设置图标和文字
     */
    private View getTabItemView(int index) {
        View view = layoutInflater.inflate(R.layout.tabwidget, null);

		ImageView imageView = (ImageView) view.findViewById(R.id.iv_mark);
		imageView.setImageResource(mImageViewArray[index]);

        TextView textView = (TextView) view.findViewById(R.id.tv_title);
        textView.setText(mTextviewArray[index]);

        return view;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MqttManeger.getInstance(this).destroy();
        DataManeger.getInstance().destroy();
        RxBus.getDefault().destroy();
    }

}

