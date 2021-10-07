package com.ahmadabuhasan.skripsi.cashier;

import android.Manifest;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;

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
 * Created by Ahmad Abu Hasan on 07/10/2021
 */

public class ScannerActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    int currentApiVersion = Build.VERSION.SDK_INT;
    public ZXingScannerView scannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);

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

    @Override
    public void handleResult(Result result) {
        PosActivity.editText_Search.setText(result.getText());
        Log.d("QRCodeScanner", result.getText());
        Log.d("QRCodeScanner", result.getBarcodeFormat().toString());
        onBackPressed();
    }

    private void requestCameraPermission() {
        Dexter.withContext(ScannerActivity.this)
                .withPermission(Manifest.permission.CAMERA)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        ScannerActivity.this.scannerView = new ZXingScannerView(ScannerActivity.this);
                        ScannerActivity scannerViewActivity = ScannerActivity.this;
                        scannerViewActivity.setContentView(scannerViewActivity.scannerView);
                        ScannerActivity.this.scannerView.startCamera();
                        ScannerActivity.this.scannerView.setResultHandler(ScannerActivity.this);
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        if (response.isPermanentlyDenied()) {
                            Toasty.info(ScannerActivity.this, R.string.camera_permission, Toasty.LENGTH_LONG).show();
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