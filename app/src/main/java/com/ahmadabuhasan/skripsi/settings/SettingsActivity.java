package com.ahmadabuhasan.skripsi.settings;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.ahmadabuhasan.skripsi.DashboardActivity;
import com.ahmadabuhasan.skripsi.R;
import com.ahmadabuhasan.skripsi.settings.categories.CategoriesActivity;
import com.ahmadabuhasan.skripsi.settings.shop.ShopInformationActivity;
import com.ahmadabuhasan.skripsi.settings.weight_unit.WeightActivity;

/*
 * Created by Ahmad Abu Hasan on 13/01/2021
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

        //new Utils().interstitialAdsShow(this);

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
            Intent intent = new Intent(this, DashboardActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}