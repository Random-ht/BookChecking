package com.hc.book.checking.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.hc.book.checking.AppApplication;
import com.hc.book.checking.R;
import com.hc.book.checking.entity.greenDao.BookInfoDao;
import com.hc.book.checking.utils.ShowPopWindow;
import com.tbruyelle.rxpermissions2.RxPermissions;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/****
 * Created by hutao on 2019/4/16.
 */
public class MainActivity extends AppCompatActivity {
    @BindView(R.id.content)
    EditText content;

    final RxPermissions rxPermissions = new RxPermissions(this);
    private Handler handler = new Handler();
    private BookInfoDao dao;
    private long count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        dao = AppApplication.getContext().getDaoSession().getBookInfoDao();

        getPermission();//获取权限
        initListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (dao != null) {
            new Thread(() -> {
                count = dao.count();
            }).start();
        }
    }

    private void initListener() {
        //在首页点击盘点机侧边的按钮之后获取详情
        content.setOnKeyListener((View v, int keyCode, KeyEvent event) -> {
            if ((keyCode == 241 || keyCode == 240) && (event.getKeyCode() == 240 || event.getKeyCode() == 241)) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {//按下
                    content.setText("");
                } else {
                    String s = content.getText().toString().trim();
                    Log.i("----------", s);
                    if (!TextUtils.isEmpty(s)) {
                        Intent intent = new Intent(MainActivity.this, BookInfoActivity.class);
                        intent.putExtra("CODE", s);
                        startActivity(intent);
                    } else {
                        Toast.makeText(MainActivity.this, "扫描数据失败...", Toast.LENGTH_SHORT).show();
                    }
                }
                return true;
            }
            return false;
        });
    }

    private void getPermission() {
        rxPermissions
                .request(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
                .subscribe(granted -> {
                    if (granted) { // Always true pre-M
                        Log.i("-----------------", "上,已经有权限了");
                    } else {
                        showOpenPermission();
                        Log.i("-----------------", "下,没有权限");
                    }
                });
    }

    private void showOpenPermission() {
        ShowPopWindow.showRationaleDialog(MainActivity.this, R.string.read_or_write_permission, new ShowPopWindow.CallBack() {
            @Override
            public void yes() {
                handler.postDelayed(() -> {
                    finish();
                    System.exit(0);
                }, 1000);
            }

            @Override
            public void no() {
                System.exit(0);
            }
        });
    }

    @OnClick({R.id.import_data, R.id.content_search, R.id.scan_search, R.id.pd})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.import_data://导入数据
                startActivity(new Intent(this, ImportDataActivity.class));
                break;
            case R.id.content_search://内容搜索界面
                if (havaData()) return;
                startActivity(new Intent(this, SearchByContentActivity.class));
                break;
            case R.id.scan_search://扫描条形码
                if (havaData()) return;
                new IntentIntegrator(this)
                        .setOrientationLocked(false)
                        .setCaptureActivity(CustomScannerActivity.class) // 设置自定义的activity是CustomActivity
                        .initiateScan(); // 初始化扫描
                break;
            case R.id.pd:
                startActivity(new Intent(this, TestActivity.class));
                break;
        }
    }

    //本地数据库是否有数据  没有的话就提示先去导入数据
    private boolean havaData() {
        if (count <= 0) {
            Toast.makeText(this, "暂无本地数据,请先导入数据...", Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() != null) {
                String resultkey = result.getContents();
                Log.i("+++++++++++", "扫描结果:" + resultkey);
                Intent intent = new Intent(this, BookInfoActivity.class);
                intent.putExtra("CODE", resultkey);
                startActivity(intent);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
