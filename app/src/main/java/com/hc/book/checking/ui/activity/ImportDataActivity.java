package com.hc.book.checking.ui.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hc.book.checking.AppApplication;
import com.hc.book.checking.R;
import com.hc.book.checking.entity.BookInfo;
import com.hc.book.checking.entity.greenDao.BookInfoDao;
import com.hc.book.checking.ui.adapter.FileListAdapter;
import com.hc.book.checking.utils.Constant;
import com.hc.book.checking.utils.ExcelReader;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/*************
 * Created by hutao on 2019/4/16.
 * 进入筛选xls文件界面
 */
public class ImportDataActivity extends AppCompatActivity implements FileListAdapter.OnItemClickListener {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.ll_root)
    LinearLayout ll_root;

    private FileListAdapter mAdapter;
    private ProgressDialog progressDialog;

    public String currDir = Constant.SDCard;//文件根目录

    private List<String[]> list = new ArrayList<>();//Excel表格数据的集合（每一行作为一个对象）
    private List<File> mData = new ArrayList<>();//储存当前目录的文件集合

    private BookInfoDao bookInfoDao;


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Constant.SHOW://正在读取Excel数据
                    show();
                    break;
                case Constant.DISMISS://progressDialog消失
                    dismiss();
                    break;
                case Constant.REFRESHING://刷新progressDialog
                    String content = (String) msg.obj;
                    setProgressDialog(content);
                    break;
                case Constant.SHOW_TOAST:
                    Toast.makeText(ImportDataActivity.this, "数据导入完成", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import_data);
        ButterKnife.bind(this);

        bookInfoDao = AppApplication.getContext().getDaoSession().getBookInfoDao();
        createProgressDialog();
        initRecyclerView();
    }

    private void initRecyclerView() {
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new FileListAdapter(this, mData);
        recyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);

        getAllFiles();
    }

    private void createProgressDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("获取数据中...");
        progressDialog.setMessage("正在解析...");
        progressDialog.setCancelable(false);//设置点击返回按钮ProgressDialog不消失
        progressDialog.setCanceledOnTouchOutside(false);//设置点击黑色区域progressDialog不消失
    }

    //显示ProgressDialog
    private void show() {
        if (progressDialog != null) {
            progressDialog.show();
        }
    }

    //隐藏ProgressDialog
    private void dismiss() {
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog.setMessage("正在解析...");
        }
    }

    //设置ProgressDialog内容
    private void setProgressDialog(String content) {
        if (progressDialog != null) {
            progressDialog.setMessage(content);
        }
    }

    //获取当前目录的所有文件及文件夹
    public void getAllFiles() {
        mData.clear();
        File file = new File(currDir);
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null) {
                for (File file2 : files) {
                    if (!(file2.getName().startsWith(".") && file2.isDirectory())) {
                        mData.add(file2);
                    }
                }
            }
        }
        // 文件排序
        sort();
        mAdapter.notifyDataSetChanged();
    }

    private void sort() {
        //使用Collection.sort排序，给定一个比较器，使用匿名内部类实现比较器接口
        Collections.sort(mData, (File o1, File o2) -> {
            if (o1.isDirectory() && o2.isDirectory() || o1.isFile()
                    && o2.isFile()) {
                return o1.compareTo(o2);
            }
            //文件夹在前
            return o1.isDirectory() ? -1 : 1;
        });
    }

    @Override
    public void OnItemClick(File file) {
        if (file.isDirectory()) {
            // 下一层目录
            currDir = file.getAbsolutePath();
            //根目录名加上当前文件夹名
            addDirText(file);
            getAllFiles();
        } else {
            Log.i("-----------", file.getPath());
            if (file.getName().endsWith(".xls")) {
                showAlert(file);
            } else {
                Toast.makeText(this, "请选择正确的文件", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //弹框显示  是否清楚并覆盖以前的数据
    private void showAlert(File file) {
        AlertDialog.Builder delete = new AlertDialog.Builder(this);
        delete.setTitle("清除并覆盖以前的数据？");
        delete.setPositiveButton("确定", (DialogInterface dialog, int which) -> {
            dialog.dismiss();
            importData(file.getPath());
        });
        delete.setNegativeButton("取消", (DialogInterface dialog, int which) -> {
            dialog.dismiss();
        });
        Dialog dialog = delete.create();
        dialog.show();
    }

    private void importData(String filePath) {
        bookInfoDao.deleteAll();
        new Thread(() -> {
            try {
                handler.sendEmptyMessage(Constant.SHOW);//显示progressDialog
                ExcelReader er = new ExcelReader(filePath);
                list = er.getExcelContent();
                getData();

            } catch (Exception e) {
                handler.sendEmptyMessage(Constant.DISMISS);//隐藏progressDialog
                Log.i("-------------", "解析表格出错");
                e.printStackTrace();
            }
        }).start();
    }

    private void getData() {
        int bookName = -1;//书名
        int bookAuthor = -1;//作者
        int bookISBNCode = -1;//ISBN号
        int genreCode = -1;//索书号
        int pressName = -1;//出版社
        int purchaseDate = -1;//购买日期
        int pageNumber = -1;//页码
        int money = -1;//单价
        int bookCount = -1;//册数
        int totalMoney = -1;//总价
        Long date = System.currentTimeMillis();
        if (list != null && list.size() >= 1) {
            String[] strings = list.get(0);
            for (int i = 0; i < strings.length; i++) {
                String desc = strings[i];
                if (TextUtils.isEmpty(desc)) {
                    desc = "";
                }
                if ("正题名".equals(desc)) {
                    bookName = i;
                } else if ("责任者".equals(desc)) {
                    bookAuthor = i;
                } else if ("ISBN号".equals(desc)) {
                    bookISBNCode = i;
                } else if (("分类号").equals(desc) || ("索书号".equals(desc))) {
                    genreCode = i;
                } else if (("出版社").equals(desc) || ("出版者".equals(desc))) {
                    pressName = i;
                } else if (("出版日期").equals(desc)) {
                    purchaseDate = i;
                } else if (("页码").equals(desc)) {
                    pageNumber = i;
                } else if (("单价").equals(desc) || ("金额").equals(desc)) {
                    money = i;
                } else if (("订数").equals(desc) || ("册数".equals(desc))) {
                    bookCount = i;
                } else if (("总价").equals(desc)) {
                    totalMoney = i;
                }
            }
        }

//        Gson gson = new Gson();
        for (int i = 0; i < list.size(); i++) {
            if (i == 0) continue;
            Message message = new Message();
            message.what = Constant.REFRESHING;
            message.obj = "正在解析" + (i) + "/" + (list.size() - 1);
            handler.sendMessage(message);
            String[] strings = list.get(i);//当前行数的所有信息
            saveData(bookName, bookAuthor, bookISBNCode, genreCode, pressName, purchaseDate, pageNumber, money, bookCount, totalMoney, date, strings);
            if (i + 1 == list.size()) {
                handler.sendEmptyMessage(1);//progressDialog消失
                handler.sendEmptyMessage(Constant.SHOW_TOAST);//导入数据完成  给提示框
            }
//            Log.i("-------------", (i) + gson.toJson(list.get(i)));
        }
    }

    /*****
     * 得到数据并保存到数据库   一下数据都是获取表格每一行所对应的索引
     * @param bookName  书名
     * @param bookAuthor 作者
     * @param bookISBNCode ISBN码
     * @param genreCode 索书号
     * @param pressName 出版社
     * @param purchaseDate 出版日期
     * @param pageNumber 页码
     * @param money 单价
     * @param bookCount 册数
     * @param totalMoney  总价
     * @param date 日期
     * @param strings 每一行的数据的集合
     */
    private void saveData(int bookName, int bookAuthor, int bookISBNCode, int genreCode, int pressName, int purchaseDate, int pageNumber, int money, int bookCount, int totalMoney, Long date, String[] strings) {
        BookInfo bookInfo = new BookInfo();
        bookInfo.setBookName(content(strings, bookName));
        bookInfo.setBookAuthor(content(strings, bookAuthor));
        bookInfo.setBookISBNCode(content(strings, bookISBNCode));
        bookInfo.setGenreCode(content(strings, genreCode));
        bookInfo.setPressName(content(strings, pressName));
        bookInfo.setPurchaseDate(content(strings, purchaseDate));
        bookInfo.setPageNumber(content(strings, pageNumber));
        bookInfo.setMoney(content(strings, money));
        bookInfo.setBookCount(content(strings, bookCount));
        bookInfo.setTotalMoney(content(strings, totalMoney));
        bookInfo.setCreateDate(date);
        if (bookInfoDao != null) {
            bookInfoDao.insert(bookInfo);
        }
    }

    private String content(String[] strings, int index) {
        if (index == -1) return "";
        else if (TextUtils.isEmpty(strings[index]) || (strings[index].equals("null"))) return "";
        return strings[index];
    }

    private void addDirText(File file) {
        String name = file.getName();
        TextView tv = new TextView(this);
        tv.setText(name + ">");
        ll_root.addView(tv);
        //将当前的路径保存
        tv.setTag(file.getAbsolutePath());
        tv.setOnClickListener((View v) -> {
            String tag = v.getTag().toString();
            currDir = tag;
            getAllFiles();

            //将后面的所有TextView的tag移除
            //从后往前删，一个一个删
            for (int i = ll_root.getChildCount(); i > 1; i--) {
                View view = ll_root.getChildAt(i - 1);
                String currTag = view.getTag().toString();
                if (!currTag.equals(currDir)) {
                    ll_root.removeViewAt(i - 1);
                } else {
                    return;
                }
            }
        });
    }

    // Back键返回上一级
    @Override
    public void onBackPressed() {
        // 如果当前目录就是系统根目录，直接调用父类
        if (currDir.equals(Constant.SDCard)) {
            super.onBackPressed();
        } else {
            // 返回上一层，显示上一层所有文件
            currDir = new File(currDir).getParent();
            getAllFiles();

            //将当前TextView的tag移除
            //总是将最后一个TextView移除
            View view = ll_root.getChildAt(ll_root.getChildCount() - 1);
            ll_root.removeView(view);
        }
    }

    @OnClick({R.id.root, R.id.actionBarLeftImage})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.actionBarLeftImage:
                finish();
                break;
            case R.id.root:
                //SD卡根目录TextView监听
                currDir = Constant.SDCard;
                getAllFiles();

                //移除ll_root布局中的其他所有组件
                for (int i = ll_root.getChildCount(); i > 1; i--) {
                    ll_root.removeViewAt(i - 1);
                }
                break;
        }
    }
}
