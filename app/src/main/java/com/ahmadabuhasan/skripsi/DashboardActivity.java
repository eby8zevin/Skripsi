package com.ahmadabuhasan.skripsi;

import android.Manifest;
import android.annotation.SuppressLint;
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
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.ahmadabuhasan.skripsi.customers.CustomersActivity;
import com.ahmadabuhasan.skripsi.data.ProductActivity;
import com.ahmadabuhasan.skripsi.expense.ExpenseActivity;
import com.ahmadabuhasan.skripsi.kasir.PosActivity;
import com.ahmadabuhasan.skripsi.print.OrdersActivity;
import com.ahmadabuhasan.skripsi.report.ReportActivity;
import com.ahmadabuhasan.skripsi.settings.SettingsActivity;
import com.ahmadabuhasan.skripsi.suppliers.SuppliersActivity;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;
import java.util.Objects;

import es.dmoral.toasty.Toasty;

/*
 * Created by Ahmad Abu Hasan on 07/10/2021
 */

public class DashboardActivity extends AppCompatActivity {

    private static long back_pressed;
    private AdView adView;
    ImageView imageView_Profile;

    CardView cardView_kaca;
    CardView cardView_pigura;
    CardView cardView_kasir;
    CardView cardView_data;
    CardView cardView_print;
    CardView cardView_settings;
    CardView cardView_expense;
    CardView cardView_report;
    CardView cardView_customers;
    CardView cardView_suppliers;

    TextView tvUserType;
    ImageView imgLogout;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        Objects.requireNonNull(getSupportActionBar()).hide();

        tvUserType = findViewById(R.id.welcome);
        imgLogout = findViewById(R.id.logout);
        progressBar = findViewById(R.id.progressBar);

        imageView_Profile = findViewById(R.id.profile);

        this.cardView_kaca = findViewById(R.id.card_kaca);
        this.cardView_pigura = findViewById(R.id.card_pigura);
        this.cardView_kasir = findViewById(R.id.card_kasir);
        this.cardView_data = findViewById(R.id.card_data);
        this.cardView_print = findViewById(R.id.card_print);
        this.cardView_settings = findViewById(R.id.card_settings);
        this.cardView_expense = findViewById(R.id.card_expense);
        this.cardView_report = findViewById(R.id.card_report);
        this.cardView_customers = findViewById(R.id.card_customers);
        this.cardView_suppliers = findViewById(R.id.card_suppliers);

        if (Build.VERSION.SDK_INT >= 23) {
            requestPermission();
        }

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {

            }
        });
        this.adView = findViewById(R.id.adView);
        this.adView.loadAd(new AdRequest.Builder().build());

        this.imageView_Profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.setAction(Intent.ACTION_VIEW);
                i.setData(Uri.parse("https://github.com/eby8zevin"));
                startActivity(i);
            }
        });

        tvUserType.setText(getResources().getString(R.string.welcome) + " Admin");

        this.progressBar.setVisibility(View.GONE);

        this.cardView_kaca.setVisibility(View.GONE);
        this.cardView_pigura.setVisibility(View.GONE);

        this.cardView_kaca.setOnClickListener(v -> {
            //DashboardActivity.this.startActivity(new Intent(DashboardActivity.this, DashboardActivity.class));
            Toast.makeText(getApplicationContext(), "Coming Soon", Toast.LENGTH_SHORT).show();
        });
        this.cardView_pigura.setOnClickListener(v -> {
            //DashboardActivity.this.startActivity(new Intent(DashboardActivity.this, DashboardActivity.class));
            Toast.makeText(getApplicationContext(), "Coming Soon", Toast.LENGTH_SHORT).show();
        });
        this.cardView_kasir.setOnClickListener(v -> DashboardActivity.this.startActivity(new Intent(DashboardActivity.this, PosActivity.class)));
        this.cardView_data.setOnClickListener(v -> DashboardActivity.this.startActivity(new Intent(DashboardActivity.this, ProductActivity.class)));
        this.cardView_print.setOnClickListener(v -> DashboardActivity.this.startActivity(new Intent(DashboardActivity.this, OrdersActivity.class)));
        this.cardView_settings.setOnClickListener(v -> DashboardActivity.this.startActivity(new Intent(DashboardActivity.this, SettingsActivity.class)));
        this.cardView_expense.setOnClickListener(v -> DashboardActivity.this.startActivity(new Intent(DashboardActivity.this, ExpenseActivity.class)));
        this.cardView_report.setOnClickListener(v -> DashboardActivity.this.startActivity(new Intent(DashboardActivity.this, ReportActivity.class)));
        this.cardView_customers.setOnClickListener(v -> DashboardActivity.this.startActivity(new Intent(DashboardActivity.this, CustomersActivity.class)));
        this.cardView_suppliers.setOnClickListener(v -> DashboardActivity.this.startActivity(new Intent(DashboardActivity.this, SuppliersActivity.class)));

        logout();
    }

    public void logout() {
        imgLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DashboardActivity.this, LoginActivity.class));
                Toast.makeText(getApplicationContext(), "Admin Logout", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.VISIBLE);
                progressBar.setIndeterminate(true);
                finish();
            }
        });
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
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
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