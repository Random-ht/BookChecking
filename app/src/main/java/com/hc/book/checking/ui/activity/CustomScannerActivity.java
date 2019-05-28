package com.hc.book.checking.ui.activity;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hc.book.checking.R;
import com.journeyapps.barcodescanner.CaptureManager;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/****
 * Created by hutao on 2019/4/17.
 * 扫描二维码界面
 */
public class CustomScannerActivity extends AppCompatActivity {

    private CaptureManager capture;
    @BindView(R.id.zxing_barcode_scanner)
    DecoratedBarcodeView barcodeScannerView;
    @BindView(R.id.switch_flashlight)
    TextView switchFlashlightButton;
    @BindView(R.id.image)
    ImageView image;
    @BindView(R.id.createTemplate)
    TextView createTemplate;
    @BindView(R.id.toMine)
    TextView toMine;

    private boolean isOpen = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_scanner);
        ButterKnife.bind(this);

        // if the device does not have flashlight in its camera,
        // then remove the switch flashlight button...
        if (!hasFlash()) {
//            switchFlashlightButton.setVisibility(View.GONE);
            image.setVisibility(View.GONE);
        }

        capture = new CaptureManager(this, barcodeScannerView);
        capture.initializeFromIntent(getIntent(), savedInstanceState);
        capture.decode();

        initListener();
    }

    protected void initListener() {
        barcodeScannerView.setTorchListener(new DecoratedBarcodeView.TorchListener() {
            @Override
            public void onTorchOn() {
//                switchFlashlightButton.setText(R.string.close);
                isOpen = true;
                image.setImageDrawable(getResources().getDrawable(R.mipmap.icon_open));
            }

            @Override
            public void onTorchOff() {
//                switchFlashlightButton.setText(R.string.open);
                isOpen = false;
                image.setImageDrawable(getResources().getDrawable(R.mipmap.icon_close));
            }
        });
    }

    @OnClick({R.id.actionBarLeftLayout, R.id.actionBarRightLayout, R.id.createTemplate, R.id.toMine})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.actionBarLeftLayout:
                finish();
                break;
        }
    }

    @Override
    protected void onResume() {
        capture.onResume();
        super.onResume();
    }

    @Override
    protected void onPause() {
        capture.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        capture.onDestroy();
        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        capture.onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return barcodeScannerView.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event);
    }

    /***
     * Check if the device's camera has a Flashlight.
     * @return true if there is Flashlight, otherwise false.
     */
    private boolean hasFlash() {
        return getApplicationContext().getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
    }

    public void switchFlashlight(View view) {
        if (!isOpen) {
            barcodeScannerView.setTorchOn();
        } else {
            barcodeScannerView.setTorchOff();
        }
//        if (getString(R.string.open).equals(switchFlashlightButton.getText())) {
//            barcodeScannerView.setTorchOn();
//        } else {
//            barcodeScannerView.setTorchOff();
//        }
    }

}
