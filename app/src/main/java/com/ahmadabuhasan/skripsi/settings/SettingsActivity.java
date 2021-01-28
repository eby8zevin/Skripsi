package com.ahmadabuhasan.skripsi.settings;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.ahmadabuhasan.skripsi.DashboardActivity;
import com.ahmadabuhasan.skripsi.R;
import com.ahmadabuhasan.skripsi.settings.categories.CategoriesActivity;
import com.ahmadabuhasan.skripsi.settings.shop.ShopInformationActivity;
import com.ahmadabuhasan.skripsi.settings.weight_unit.WeightActivity;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

/*
 * Created by Ahmad Abu Hasan on 28/01/2021
 */

public class SettingsActivity extends AppCompatActivity {

    private InterstitialAd interstitialAd;

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

        //new Utils().interstitialAdsShow(this);
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {

            }
        });
        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(this, getResources().getString(R.string.AdMob_Interstitial_Ads_ID), adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull InterstitialAd mInterstitialAd) {
                // The interstitialAd reference will be null until
                // an ad is loaded.
                interstitialAd = mInterstitialAd;
                Log.i("InterstitialAd", "onAdLoaded");
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                // Handle the error
                Log.i("InterstitialAd", loadAdError.getMessage());
                interstitialAd = null;
            }
        });

        this.cardView_ShopInfo.setOnClickListener(v -> SettingsActivity.this.startActivity(new Intent(SettingsActivity.this, ShopInformationActivity.class)));
        this.cardView_Category.setOnClickListener(v -> SettingsActivity.this.startActivity(new Intent(SettingsActivity.this, CategoriesActivity.class)));
        this.cardView_WeightUnit.setOnClickListener(v -> SettingsActivity.this.startActivity(new Intent(SettingsActivity.this, WeightActivity.class)));

        this.cardView_PaymentMethod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        this.cardView_Backup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
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
}