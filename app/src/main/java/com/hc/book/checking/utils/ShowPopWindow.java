package com.hc.book.checking.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.annotation.StringRes;
import android.support.v7.app.AlertDialog;

import com.hc.book.checking.R;

/**
 * Created by hutao on 2019/4/18.
 * 弹框   查看预约中和完成预约的详情   升级的弹框   退出登陆
 */
public class ShowPopWindow {

    public static void showRationaleDialog(Context context, @StringRes int messageResId, CallBack callBack) {
        AlertDialog dialog = new AlertDialog.Builder(context)
                .setTitle("权限申请")
                .setMessage(messageResId)
                .setPositiveButton("确定", (DialogInterface dialogInterface, int which) -> {
                    dialogInterface.dismiss();
                    SystemUtils.getAppDetailSettingIntent(context);
                    callBack.yes();
                })
                .setNegativeButton("退出APP", (DialogInterface dialogInterface, int which) -> {
                    dialogInterface.dismiss();
                    callBack.no();
                })
                .create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(context.getResources().getColor(R.color.black));
        dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
    }

    public interface CallBack {
        void yes();

        void no();
    }
}
