package com.eastsoft.building.sdk;

import android.app.ActionBar;
import android.app.Dialog;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v4.app.FragmentActivity;

import com.eastsoft.building.model.RxBus;
import com.ehomeclouds.eastsoft.channel.http.api.Cancel;
import com.ehomeclouds.eastsoft.channel.mqtt.model.PublishState;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;


/**
 * Created by Admin on 2016/1/20.
 */
public abstract class BaseActivity extends FragmentActivity {
    private TextView tvTitle;
    private ActionBar actionBar;
    private IOnTitleClick iOnTitleClick;
    private TextView textViewRight;
    private String leftText;
    private TextView textViewLeft;
    private ImageButton rightImageButton;
    private ImageButton leftImageButton;
    private Dialog dialog;
    private Subscription subscription;
    protected int pageNum=1;
    protected  boolean isRefresh=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dialog=MyDialog.getStaticDialog(this);
        subscription=RxBus.getDefault().toObserverable(PublishState.class).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<PublishState>() {
            @Override
            public void call(PublishState publishState) {

                if (publishState.publishState == PublishState.SUCCESS) {
                    showToast("下发成功");
                } else {
                    showToast("下发失败");

                }
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                Log.e("publish", "error");
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (subscription!=null&&!subscription.isUnsubscribed()){
            subscription.unsubscribe();
        }
    }
    protected void showToast(String toastStr){
        Toast.makeText(this,toastStr,Toast.LENGTH_SHORT).show();
    }

    public void restoreActionBar(String centerTitle, int leftImageRes,
                                 int rightImageRes, String leftText, String rightText) {
        tvTitle = (TextView) findViewById(R.id.title);
        tvTitle.setText(centerTitle);
        textViewRight = (TextView) findViewById(R.id.title_right_textView);
        textViewRight.setText(rightText);
        textViewLeft = (TextView) findViewById(R.id.title_left_textView);
        textViewLeft.setText(leftText);
        if (rightText != "") {
            textViewRight.setText(rightText);
            textViewRight.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    iOnTitleClick.OnRightClick(arg0);

                }
            });
        } else {
            textViewRight.setVisibility(View.GONE);
        }
        if (leftText != "") {
            textViewLeft.setText(leftText);
            textViewLeft.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    iOnTitleClick.OnLeftClick(arg0);

                }
            });
        } else {
            textViewLeft.setVisibility(View.GONE);
        }
        leftImageButton = (ImageButton) findViewById(R.id.title_left_btn);
        if (leftImageRes != 0) {
            leftImageButton.setImageResource(leftImageRes);
            leftImageButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {

                    iOnTitleClick.OnLeftClick(arg0);

                }
            });

        } else {
            leftImageButton.setVisibility(View.GONE);
        }

        rightImageButton = (ImageButton) findViewById(R.id.title_right_btn);
        if (rightImageRes != 0) {

            rightImageButton.setImageResource(rightImageRes);
            rightImageButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {

                    iOnTitleClick.OnRightClick(arg0);
                }
            });
        } else {
            rightImageButton.setVisibility(View.GONE);
        }

    }

    public void setOnclick(IOnTitleClick iOnTitleClick) {
        this.iOnTitleClick = iOnTitleClick;

    }

    public interface IOnTitleClick {
        void OnLeftClick(View view);

        void OnRightClick(View view);

    }

    public void setRightBtnClikable(boolean isRightBtnClickable) {
        if (rightImageButton!=null) {
            if (isRightBtnClickable) {
                rightImageButton.setAlpha(1);
            } else {
                rightImageButton.setAlpha(0.38F);
            }
            rightImageButton.setClickable(isRightBtnClickable);
        }
        if (textViewRight!=null) {
            if (isRightBtnClickable) {
                textViewRight.setAlpha(1);
            } else {
                textViewRight.setAlpha(0.38F);
            }
            textViewRight.setClickable(isRightBtnClickable);

        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode==KeyEvent.KEYCODE_BACK){
            RxBus.getDefault().post(new Cancel());
        }
        return super.onKeyDown(keyCode, event);
    }

}
