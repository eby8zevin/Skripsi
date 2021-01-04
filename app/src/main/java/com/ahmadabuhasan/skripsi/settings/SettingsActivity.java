package com.ahmadabuhasan.skripsi.settings;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.ahmadabuhasan.skripsi.R;
import com.ahmadabuhasan.skripsi.settings.categories.CategoriesActivity;
import com.ahmadabuhasan.skripsi.settings.shop.ShopInformationActivity;

/*
 * Created by Ahmad Abu Hasan on 03/01/2021
 */

public class SettingsActivity extends AppCompatActivity {

    CardView cardView_ShopInfo;
    CardView cardView_Category;
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
        this.cardView_PaymentMethod = findViewById(R.id.card_payment_method);
        this.cardView_Backup = findViewById(R.id.card_backup);

        //new Utils().interstitialAdsShow(this);

        this.cardView_ShopInfo.setOnClickListener(v -> SettingsActivity.this.startActivity(new Intent(SettingsActivity.this, ShopInformationActivity.class)));

        this.cardView_Category.setOnClickListener(v -> SettingsActivity.this.startActivity(new Intent(SettingsActivity.this, CategoriesActivity.class)));

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
        if (item.getItemId() != android.R.id.home) {
            return super.onOptionsItemSelected(item);
        }
        finish();
        return true;
    }
}