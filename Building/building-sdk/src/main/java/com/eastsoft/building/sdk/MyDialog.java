package com.eastsoft.building.sdk;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by ll on 2016/4/15.
 */
public class MyDialog {


    public static Dialog curDialog;

    public static ImageView spaceshipImage;

    // 加载动画
    public static Animation hyperspaceJumpAnimation;


    public static Dialog getStaticDialog(Context context) {
        return getStaticDialog(context, "");
    }

    public static Dialog getStaticDialog(Context context, String msg) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.round_dialog, null);// 得到加载view
        RelativeLayout layout = (RelativeLayout) v
                .findViewById(R.id.dialog_view_rl);// 加载布局
        // main.xml中的ImageView
        spaceshipImage = (ImageView) v.findViewById(R.id.img_iv);
        TextView tipTextView = (TextView) v.findViewById(R.id.tipTextView);// 提示文字
        // 加载动画
        hyperspaceJumpAnimation = AnimationUtils.loadAnimation(
                context, R.anim.dialog_anim);
        // 使用ImageView显示动画
        spaceshipImage.startAnimation(hyperspaceJumpAnimation);
        tipTextView.setText(msg);// 设置加载信息

        Dialog dialog = new Dialog(context, R.style.loading_dialog);// 创建自定义样式dialog
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(layout, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.FILL_PARENT,
                LinearLayout.LayoutParams.FILL_PARENT));// 设置布局
        dialog.setCancelable(true);// 不可以用“返回键”取消

        curDialog = dialog;
        return dialog;

    }

    public static void getAnimation() {
        if (spaceshipImage != null && hyperspaceJumpAnimation != null) {
            spaceshipImage.startAnimation(hyperspaceJumpAnimation);
        }
    }

}
