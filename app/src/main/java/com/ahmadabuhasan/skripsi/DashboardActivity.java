package com.ahmadabuhasan.skripsi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.ahmadabuhasan.skripsi.data.ProductActivity;
import com.ahmadabuhasan.skripsi.settings.SettingsActivity;
import com.ahmadabuhasan.skripsi.suppliers.SuppliersActivity;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;

import es.dmoral.toasty.Toasty;

/*
 * Created by Ahmad Abu Hasan on 13/01/2021
 */

public class DashboardActivity extends AppCompatActivity {

    private static long back_pressed;

    CardView cardView_kaca;
    CardView cardView_pigura;
    CardView cardView_kasir;
    CardView cardView_data;
    CardView cardView_customers;
    CardView cardView_suppliers;
    CardView cardView_print;
    CardView cardView_settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        this.cardView_kaca = findViewById(R.id.card_kaca);
        this.cardView_pigura = findViewById(R.id.card_pigura);
        this.cardView_kasir = findViewById(R.id.card_kasir);
        this.cardView_data = findViewById(R.id.card_data);
        this.cardView_customers = findViewById(R.id.card_customers);
        this.cardView_suppliers = findViewById(R.id.card_suppliers);
        this.cardView_print = findViewById(R.id.card_print);
        this.cardView_settings = findViewById(R.id.card_settings);

        this.cardView_data.setOnClickListener(v -> DashboardActivity.this.startActivity(new Intent(DashboardActivity.this, ProductActivity.class)));
        this.cardView_suppliers.setOnClickListener(v -> DashboardActivity.this.startActivity(new Intent(DashboardActivity.this, SuppliersActivity.class)));
        this.cardView_settings.setOnClickListener(v -> DashboardActivity.this.startActivity(new Intent(DashboardActivity.this, SettingsActivity.class)));

        if (Build.VERSION.SDK_INT >= 23) {
            requestPermission();
        }
    }

    public void onBackPressed() {
        if (back_pressed + 2000 > System.currentTimeMillis()) {
            finishAffinity();
        } else {
            Toasty.info((Context) this, (int) R.string.press_once_again_to_exit, Toasty.LENGTH_SHORT).show();
        }
        back_pressed = System.currentTimeMillis();
    }

    /*
     * Requesting multiple permissions (camera and storage) at once
     * This uses multiple permission model from dexter
     * On permanent denial opens settings dialog
     */
    private void requestPermission() {
        Dexter.withContext(this)
                .withPermissions(
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            //Toast.makeText(getApplicationContext(), "All permissions are granted!", Toast.LENGTH_SHORT).show();
                            Log.d("Dexter", "All permissions are granted!");
                        } else if (report.isAnyPermissionPermanentlyDenied()) {
                            showSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).withErrorListener(new PermissionRequestErrorListener() {
            @Override
            public void onError(DexterError error) {
                Toast.makeText(DashboardActivity.this.getApplicationContext(), R.string.error, Toast.LENGTH_SHORT).show();
            }
        }).onSameThread().check();
    }

    /*
     * Showing Alert Dialog with Settings option
     * Navigates user to app settings
     * NOTE: Keep proper title and message depending on your app
     */
    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(DashboardActivity.this);
        builder.setTitle("Need Permissions");
        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.");
        builder.setPositiveButton("GOTO SETTINGS", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                openSettings();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        //builder.show();
        AlertDialog alert_show = builder.create();
        alert_show.show();

        Button button_positive = alert_show.getButton(DialogInterface.BUTTON_POSITIVE);
        button_positive.setTextColor(Color.BLACK);

        Button button_negative = alert_show.getButton(DialogInterface.BUTTON_NEGATIVE);
        button_negative.setTextColor(Color.RED);
    }

    // navigating user to app settings
    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }
}