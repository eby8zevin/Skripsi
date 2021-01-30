package com.ahmadabuhasan.skripsi.settings;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.ahmadabuhasan.skripsi.DashboardActivity;
import com.ahmadabuhasan.skripsi.R;
import com.ahmadabuhasan.skripsi.settings.backup.BackupActivity;
import com.ahmadabuhasan.skripsi.settings.categories.CategoriesActivity;
import com.ahmadabuhasan.skripsi.settings.payment_method.PaymentMethodActivity;
import com.ahmadabuhasan.skripsi.settings.shop.ShopInformationActivity;
import com.ahmadabuhasan.skripsi.settings.weight_unit.WeightActivity;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

/*
 * Created by Ahmad Abu Hasan on 30/01/2021
 */

public class SettingsActivity extends AppCompatActivity {

    CardView cardView_ShopInfo;
    CardView cardView_Category;
    CardView cardView_WeightUnit;
    CardView cardView_PaymentMethod;
    CardView cardView_Backup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
        new SettingsActivity().interstitialAdsShow(this);

        this.cardView_ShopInfo.setOnClickListener(v -> SettingsActivity.this.startActivity(new Intent(SettingsActivity.this, ShopInformationActivity.class)));
        this.cardView_Category.setOnClickListener(v -> SettingsActivity.this.startActivity(new Intent(SettingsActivity.this, CategoriesActivity.class)));
        this.cardView_WeightUnit.setOnClickListener(v -> SettingsActivity.this.startActivity(new Intent(SettingsActivity.this, WeightActivity.class)));
        this.cardView_PaymentMethod.setOnClickListener(v -> SettingsActivity.this.startActivity(new Intent(SettingsActivity.this, PaymentMethodActivity.class)));
        this.cardView_Backup.setOnClickListener(v -> SettingsActivity.this.startActivity(new Intent(SettingsActivity.this, BackupActivity.class)));
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
        startActivity(new Intent(this, DashboardActivity.class));
        finish();
        //super.onBackPressed();
    }

    public void interstitialAdsShow(Context context) {
        final InterstitialAd interstitialAd = new InterstitialAd(context);
        interstitialAd.setAdUnitId(context.getString(R.string.AdMob_Interstitial_Ads_ID));
        interstitialAd.loadAd(new AdRequest.Builder().build());
        interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                if (interstitialAd.isLoaded()) {
                    interstitialAd.show();
                }
            }
        });
    }
}