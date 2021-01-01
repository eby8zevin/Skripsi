package com.ahmadabuhasan.skripsi.data;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.ahmadabuhasan.skripsi.R;
import com.google.zxing.Result;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.single.PermissionListener;

import es.dmoral.toasty.Toasty;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

/*
 * Created by Ahmad Abu Hasan on 02/01/2021
 */

public class ScannerViewActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    int currentApiVersion = Build.VERSION.SDK_INT;

    public ZXingScannerView scannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner_view);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.qr_barcode_scanner);

        if (this.currentApiVersion >= 23) {
            requestCameraPermission();
        }
        ZXingScannerView zXingScannerView = new ZXingScannerView(this);
        scannerView = zXingScannerView;
        setContentView(zXingScannerView);
        scannerView.startCamera();
        scannerView.setResultHandler(this);
    }

    public void onResume() {
        super.onResume();
        this.scannerView.setResultHandler(this);
        this.scannerView.startCamera();
    }

    public void onDestroy() {
        super.onDestroy();
        this.scannerView.stopCamera();
    }

    public void handleResult(Result result) {
        AddProductActivity.editText_Code.setText(result.getText());
        Log.d("QRCodeScanner", result.getText());
        Log.d("QRCodeScanner", result.getBarcodeFormat().toString());
        onBackPressed();
    }

    private void requestCameraPermission() {
        Dexter.withContext(ScannerViewActivity.this)
                .withPermission("android.permission.CAMERA")
                .withListener(new PermissionListener() {
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        ScannerViewActivity.this.scannerView = new ZXingScannerView(ScannerViewActivity.this);
                        ScannerViewActivity scannerViewActivity = ScannerViewActivity.this;
                        scannerViewActivity.setContentView(scannerViewActivity.scannerView);
                        ScannerViewActivity.this.scannerView.startCamera();
                        ScannerViewActivity.this.scannerView.setResultHandler(ScannerViewActivity.this);
                    }

                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        if (response.isPermanentlyDenied()) {
                            Toasty.info(ScannerViewActivity.this, R.string.camera_permission, Toasty.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(com.karumi.dexter.listener.PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }

                }).check();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() != android.R.id.home) {
            return super.onOptionsItemSelected(item);
        }
        finish();
        return true;
    }
}