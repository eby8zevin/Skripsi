package com.ahmadabuhasan.skripsi.settings;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.ahmadabuhasan.skripsi.R;
import com.ahmadabuhasan.skripsi.WarehouseDashboard;
import com.ahmadabuhasan.skripsi.settings.categories.CategoriesActivity;
import com.ahmadabuhasan.skripsi.settings.weight_unit.WeightActivity;
import com.ahmadabuhasan.skripsi.utils.Utils;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

public class SettingsWarehouse extends AppCompatActivity {

    CardView cardView_ShopInfo;
    CardView cardView_Category;
    CardView cardView_WeightUnit;
    CardView cardView_PaymentMethod;
    CardView cardView_Backup;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.action_settings);

        this.cardView_ShopInfo = findViewById(R.id.card_shop_info);
        this.cardView_Category = findViewById(R.id.card_category);
        this.cardView_WeightUnit = findViewById(R.id.card_weight_unit);
        this.cardView_PaymentMethod = findViewById(R.id.card_payment_method);
        this.cardView_Backup = findViewById(R.id.card_backup);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {

            }
        });
        new Utils().interstitialAdsShow(this);

        this.cardView_ShopInfo.setVisibility(View.GONE);
        this.cardView_PaymentMethod.setVisibility(View.GONE);
        this.cardView_Backup.setVisibility(View.GONE);

        this.cardView_Category.setOnClickListener(v -> SettingsWarehouse.this.startActivity(new Intent(SettingsWarehouse.this, CategoriesActivity.class)));
        this.cardView_WeightUnit.setOnClickListener(v -> SettingsWarehouse.this.startActivity(new Intent(SettingsWarehouse.this, WeightActivity.class)));
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, WarehouseDashboard.class));
        finish();
    }
}