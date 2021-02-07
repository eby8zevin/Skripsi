package com.ahmadabuhasan.skripsi.data;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.WindowManager;

import com.ahmadabuhasan.skripsi.R;
import com.google.zxing.Result;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import es.dmoral.toasty.Toasty;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

/*
 * Created by Ahmad Abu Hasan on 07/02/2021
 */

public class EditProductScannerViewActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    int currentApiVersion = Build.VERSION.SDK_INT;
    private ZXingScannerView scannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product_scanner_view);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.qr_barcode_scanner);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        if (this.currentApiVersion >= 23) {
            requestCameraPermission();
        }

        ZXingScannerView zXingScannerView = new ZXingScannerView(this);
        this.scannerView = zXingScannerView;
        setContentView(zXingScannerView);
        this.scannerView.startCamera();
        this.scannerView.setResultHandler(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        this.scannerView.setResultHandler(this);
        this.scannerView.startCamera();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.scannerView.stopCamera();
    }

    @Override
    public void handleResult(Result result) {
        EditProductActivity.editText_Code.setText(result.getText());
        Log.d("QRCodeScanner", result.getText());
        Log.d("QRCodeScanner", result.getBarcodeFormat().toString());
        onBackPressed();
    }

    private void requestCameraPermission() {
        Dexter.withActivity(this)
                .withPermission("android.permission.CAMERA")
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        EditProductScannerViewActivity.this.scannerView = new ZXingScannerView(EditProductScannerViewActivity.this);
                        EditProductScannerViewActivity editProductScannerViewActivity = EditProductScannerViewActivity.this;
                        editProductScannerViewActivity.setContentView(editProductScannerViewActivity.scannerView);
                        EditProductScannerViewActivity.this.scannerView.startCamera();
                        EditProductScannerViewActivity.this.scannerView.setResultHandler(EditProductScannerViewActivity.this);
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        if (response.isPermanentlyDenied()) {
                            Toasty.info(EditProductScannerViewActivity.this, (int) R.string.camera_permission, Toasty.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.refresh_camera, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        } else if (item.getItemId() == R.id.refresh) {
            finish();
            overridePendingTransition(0, 0);
            startActivity(getIntent());
            overridePendingTransition(0, 0);
        }
        return super.onOptionsItemSelected(item);
    }
}