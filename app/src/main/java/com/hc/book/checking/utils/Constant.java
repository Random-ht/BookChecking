package com.hc.book.checking.utils;

import android.os.Environment;

/**
 * Created by hutao on 2019/4/16.
 */

public class Constant {

    public final static String SDCard = Environment.getExternalStorageDirectory().getAbsolutePath();//文件根目录

    public final static int SHOW = 0;//显示progressDialog
    public final static int DISMISS = 1;//隐藏progressDialog
    public final static int REFRESHING = 2;//刷新进度条
    public final static int SHOW_TOAST = 3;//显示toast


    public final static int PAGE_SIZE = 20;//加载时候的分页个数
}
