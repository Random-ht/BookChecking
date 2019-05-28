package com.hc.book.checking.utils;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.ClipboardManager;
import android.util.Log;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.List;


/**
 * Created by hutao on 2018/3/23.
 * 系统的帮助类   比如软键盘
 */

public class SystemUtils {

    private static SystemUtils systemUtils;

    public static SystemUtils getInstance() {
        if (systemUtils == null) {
            synchronized (SystemUtils.class) {
                if (systemUtils == null) {
                    systemUtils = new SystemUtils();
                }
            }
        }
        return systemUtils;
    }

    /***
     * app是否在运行
     * @param context
     * @return
     */
    public static boolean stackResumed(Context context) {
        ActivityManager manager = (ActivityManager) context
                .getApplicationContext().getSystemService(
                        Context.ACTIVITY_SERVICE);
        String packageName = context.getApplicationContext().getPackageName();
        List<ActivityManager.RunningTaskInfo> recentTaskInfos = manager.getRunningTasks(1);
        if (recentTaskInfos != null && recentTaskInfos.size() > 0) {
            ActivityManager.RunningTaskInfo taskInfo = recentTaskInfos.get(0);
            if (taskInfo.baseActivity.getPackageName().equals(packageName) && taskInfo.numActivities > 1) {
                return true;
            }
        }

        return false;
    }

    /**
     * EditText获取焦点并显示软键盘
     */
    public static void showSoftInputFromWindow(Context activity, EditText editText) {
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editText, 0);
    }

    /**
     * 收起软键盘
     *
     * @param v
     */
    public static void hindSoftInputFromWindow(Context context, View v) {
        InputMethodManager inputmanger = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputmanger.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    /**
     * 收起软键盘
     */
    public static void hindSoftInputFromWindow(Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Service.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * 是否是后台运行
     *
     * @param context
     * @return true  表示是在后台运行
     */
    public static boolean isBackground(Context context) {

        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(context.getPackageName())) {
                if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_BACKGROUND) {
                    Log.i("后台", appProcess.processName);
                    return true;
                } else {
                    Log.i("前台", appProcess.processName);
                    return false;
                }
            }
        }
        return false;
    }

    /**
     * 复制到粘贴板上
     *
     * @param content
     */
    public static void copyContent(Context context, String content) {
        //复制到粘贴板
        ClipboardManager copy = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        copy.setText(content);
    }


    /**
     * 是否有网络
     *
     * @param context
     * @return
     */
    public static boolean CheckNetworkState(Context context) {
        boolean flag = false;
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(
                Context.CONNECTIVITY_SERVICE);
        if (manager.getActiveNetworkInfo() != null) {
            flag = manager.getActiveNetworkInfo().isAvailable();
        }
        if (!flag) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setIcon(android.R.drawable.ic_dialog_alert);
            builder.setTitle("Network not avaliable");//
            builder.setMessage("Current network is not avaliable, set it?");//
            builder.setPositiveButton("Setting", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    context.startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS)); //直接进入手机中的wifi网络设置界面
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            builder.create();
            builder.show();
        }

        return flag;
    }

    /**
     * 保存图片到指定位置
     *
     * @param context
     * @param bmp
     */
    public static void saveImageToGallery(Context context, Bitmap bmp) {

//         首先保存图片
        File appDir = new File(Environment.getExternalStorageDirectory(), "WenKeImage");
        if (!appDir.exists()) {
            appDir.mkdirs();
        }
        String fileName = "IMG_" + System.currentTimeMillis() + ".jpg";

        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


//        try {
//            FileOutputStream fos = context.openFileOutput(fileName, context.MODE_PRIVATE);
//            //bitmap转文件对象
//            bmp.compress(Bitmap.CompressFormat.PNG, 100, fos);
//            fos.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

//         其次把文件插入到系统图库
        try {
            MediaStore.Images.Media.insertImage(context.getContentResolver(),
                    file.getAbsolutePath(), fileName, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

//         最后通知图库更新
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + appDir.getPath() + "/" + fileName)));
    }

    /**
     * 保存图片到指定位置
     *
     * @param bmp
     */
    public static String saveImageToChatImage(Context context, Bitmap bmp) {
        String path = "";
//         首先保存图片
        File appDir = new File(Environment.getExternalStorageDirectory(), "WenKeImage/cache");
        if (!appDir.exists()) {
            appDir.mkdirs();
        }
        String fileName = "IMG_" + System.currentTimeMillis() + ".jpg";

//        try {
//            FileOutputStream fos = context.openFileOutput(fileName, context.MODE_PRIVATE);
//            //bitmap转文件对象
//            bmp.compress(Bitmap.CompressFormat.PNG, 100, fos);
//            fos.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
            path = file.getPath();
            Log.i("-------=-=-=-path() ", path);
            return path;
        } catch (Exception e) {
            return path;
        }
    }

    /**
     * 保存图片到指定位置
     *
     * @param bmp
     */
    public static String saveImageByQrCode(Bitmap bmp) {
        String path = "";
        // 首先保存图片
        File appDir = new File(Environment.getExternalStorageDirectory(), "WenKeImage/cache");
        if (!appDir.exists()) {
            appDir.mkdirs();
        }
        String fileName = "IMG_CODE.jpg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
            path = file.getPath();
            Log.i("-------=-=-=-path() ", path);
            return path;
        } catch (Exception e) {
            return path;
        }
    }


    //获取虚拟按键的高度
    public static int getNavigationBarHeight(Context context) {
        int result = 0;
        if (hasNavBar(context)) {
            Resources res = context.getResources();
            int resourceId = res.getIdentifier("navigation_bar_height", "dimen", "android");
            if (resourceId > 0) {
                result = res.getDimensionPixelSize(resourceId);
            }
        }
        return result;
    }

    /**
     * 检查是否存在虚拟按键栏
     *
     * @param context
     * @return
     */
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public static boolean hasNavBar(Context context) {
        Resources res = context.getResources();
        int resourceId = res.getIdentifier("config_showNavigationBar", "bool", "android");
        if (resourceId != 0) {
            boolean hasNav = res.getBoolean(resourceId);
            // check override flag
            String sNavBarOverride = getNavBarOverride();
            if ("1".equals(sNavBarOverride)) {
                hasNav = false;
            } else if ("0".equals(sNavBarOverride)) {
                hasNav = true;
            }
            return hasNav;
        } else { // fallback
            return !ViewConfiguration.get(context).hasPermanentMenuKey();
        }
    }

    /**
     * 判断虚拟按键栏是否重写
     *
     * @return
     */
    private static String getNavBarOverride() {
        String sNavBarOverride = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                Class c = Class.forName("android.os.SystemProperties");
                Method m = c.getDeclaredMethod("get", String.class);
                m.setAccessible(true);
                sNavBarOverride = (String) m.invoke(null, "qemu.hw.mainkeys");
            } catch (Throwable e) {
            }
        }
        return sNavBarOverride;
    }


    /***
     * 跳转到权限设置界面
     * @param context
     */
    public static void getAppDetailSettingIntent(Context context) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 9) {
            intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            intent.setData(Uri.fromParts("package", context.getPackageName(), null));
        } else if (Build.VERSION.SDK_INT <= 8) {
            intent.setAction(Intent.ACTION_VIEW);
            intent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
            intent.putExtra("com.android.settings.ApplicationPkgName", context.getPackageName());
        }
        context.startActivity(intent);
    }


    /*
     * 计算两点之间距离
     * @return km
     */
    public static double getDistance(double startLongitude, double startDimensionality,
                                     double endLongitude, double endDimensionality) {
        double lon1 = (Math.PI / 180) * startLongitude;
        double lon2 = (Math.PI / 180) * endLongitude;
        double lat1 = (Math.PI / 180) * startDimensionality;
        double lat2 = (Math.PI / 180) * endDimensionality;
        // double Lat1r = (Math.PI/180)*(gp1.getLatitudeE6()/1E6);
        // double Lat2r = (Math.PI/180)*(gp2.getLatitudeE6()/1E6);
        // double Lon1r = (Math.PI/180)*(gp1.getLongitudeE6()/1E6);
        // double Lon2r = (Math.PI/180)*(gp2.getLongitudeE6()/1E6);

        // 地球半径
        double R = 6371;
        // 两点间距离 km，如果想要米的话，结果*1000就可以了
        double d = Math.acos(Math.sin(lat1) * Math.sin(lat2) + Math.cos(lat1) * Math.cos(lat2) * Math.cos(lon2 - lon1)) * R;

        BigDecimal bg = new BigDecimal(d - 0.6);
        double distance = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        return distance;
    }





}

