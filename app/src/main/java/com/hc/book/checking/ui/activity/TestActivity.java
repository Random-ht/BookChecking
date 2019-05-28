package com.hc.book.checking.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.hc.book.checking.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TestActivity extends AppCompatActivity {
    @BindView(R.id.content)
    EditText content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        ButterKnife.bind(this);

        content.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((keyCode == 241 || keyCode == 240) && (event.getKeyCode() == 240 || event.getKeyCode() == 241)) {
                    if (event.getAction() == KeyEvent.ACTION_DOWN) {//按下
                        content.setText("");
                    } else {
                        String s = content.getText().toString().trim();
                        Log.i("----------", s);
                        if (!TextUtils.isEmpty(s)) {
                            Intent intent = new Intent(TestActivity.this, BookInfoActivity.class);
                            intent.putExtra("CODE", s);
                            startActivity(intent);
                        } else {
                            Toast.makeText(TestActivity.this, "扫描数据失败...", Toast.LENGTH_SHORT).show();
                        }
                    }
                    return true;
                }
                return false;
            }
        });
    }

    @OnClick({R.id.actionBarLeftImage})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.actionBarLeftImage:
                finish();
                break;
        }
    }
}
