package com.ahmadabuhasan.skripsi.settings.shop;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.internal.view.SupportMenu;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.ahmadabuhasan.skripsi.R;
import com.ahmadabuhasan.skripsi.database.DatabaseAccess;
import com.ahmadabuhasan.skripsi.database.DatabaseOpenHelper;
import com.ahmadabuhasan.skripsi.settings.SettingsActivity;
//import com.itextpdf.text.io.PagedChannelRandomAccessSource;

import java.util.HashMap;
import java.util.List;

import es.dmoral.toasty.Toasty;

/*
 * Created by Ahmad Abu Hasan on 07/02/2021
 */

public class ShopInformationActivity extends AppCompatActivity {

    EditText editText_ShopName;
    EditText editText_Contact;
    EditText editText_Email;
    EditText editText_Address;
    EditText editText_Currency;
    EditText editText_Tax;

    TextView textView_Edit;
    TextView textView_Update;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_information);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.shop_information);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        this.editText_ShopName = findViewById(R.id.et_shop_name);
        this.editText_Contact = findViewById(R.id.et_shop_contact);
        this.editText_Email = findViewById(R.id.et_shop_email);
        this.editText_Address = findViewById(R.id.et_shop_address);
        this.editText_Currency = findViewById(R.id.et_shop_currency);
        this.editText_Tax = findViewById(R.id.et_shop_tax);

        this.textView_Edit = findViewById(R.id.tv_shop_edit);
        this.textView_Update = findViewById(R.id.tv_shop_update);

        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(this);
        databaseAccess.open();
        List<HashMap<String, String>> shopData = databaseAccess.getShopInformation();

        this.editText_ShopName.setText(shopData.get(0).get(DatabaseOpenHelper.SHOP_NAME));
        this.editText_Contact.setText(shopData.get(0).get(DatabaseOpenHelper.SHOP_CONTACT));
        this.editText_Email.setText(shopData.get(0).get(DatabaseOpenHelper.SHOP_EMAIL));
        this.editText_Address.setText(shopData.get(0).get(DatabaseOpenHelper.SHOP_ADDRESS));
        this.editText_Currency.setText(shopData.get(0).get(DatabaseOpenHelper.SHOP_CURRENCY));
        this.editText_Tax.setText(shopData.get(0).get(DatabaseOpenHelper.SHOP_TAX));

        this.editText_ShopName.setEnabled(false);
        this.editText_Contact.setEnabled(false);
        this.editText_Email.setEnabled(false);
        this.editText_Address.setEnabled(false);
        this.editText_Currency.setEnabled(false);
        this.editText_Tax.setEnabled(false);
        this.textView_Update.setVisibility(View.GONE);

        this.textView_Edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShopInformationActivity.this.editText_ShopName.setEnabled(true);
                ShopInformationActivity.this.editText_Contact.setEnabled(true);
                ShopInformationActivity.this.editText_Email.setEnabled(true);
                ShopInformationActivity.this.editText_Address.setEnabled(true);
                ShopInformationActivity.this.editText_Currency.setEnabled(true);
                ShopInformationActivity.this.editText_Tax.setEnabled(true);

                ShopInformationActivity.this.editText_ShopName.setTextColor(SupportMenu.CATEGORY_MASK);
                ShopInformationActivity.this.editText_Contact.setTextColor(SupportMenu.CATEGORY_MASK);
                ShopInformationActivity.this.editText_Email.setTextColor(SupportMenu.CATEGORY_MASK);
                ShopInformationActivity.this.editText_Address.setTextColor(SupportMenu.CATEGORY_MASK);
                ShopInformationActivity.this.editText_Currency.setTextColor(SupportMenu.CATEGORY_MASK);
                ShopInformationActivity.this.editText_Tax.setTextColor(SupportMenu.CATEGORY_MASK);

                ShopInformationActivity.this.textView_Edit.setVisibility(View.GONE);
                ShopInformationActivity.this.textView_Update.setVisibility(View.VISIBLE);
            }
        });

        this.textView_Update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String shop_name = ShopInformationActivity.this.editText_ShopName.getText().toString().trim();
                String shop_contact = ShopInformationActivity.this.editText_Contact.getText().toString().trim();
                String shop_email = ShopInformationActivity.this.editText_Email.getText().toString().trim();
                String shop_address = ShopInformationActivity.this.editText_Address.getText().toString().trim();
                String shop_currency = ShopInformationActivity.this.editText_Currency.getText().toString().trim();
                String shop_tax = ShopInformationActivity.this.editText_Tax.getText().toString().trim();

                if (shop_name.isEmpty()) {
                    ShopInformationActivity.this.editText_ShopName.setError(ShopInformationActivity.this.getString(R.string.shop_name_cannot_be_empty));
                    ShopInformationActivity.this.editText_ShopName.requestFocus();
                } else if (shop_contact.isEmpty()) {
                    ShopInformationActivity.this.editText_Contact.setError(ShopInformationActivity.this.getString(R.string.shop_contact_cannot_be_empty));
                    ShopInformationActivity.this.editText_Contact.requestFocus();
                } else if (shop_email.isEmpty() || !shop_email.contains("@") || !shop_email.contains(".")) {
                    ShopInformationActivity.this.editText_Email.setError(ShopInformationActivity.this.getString(R.string.enter_valid_email));
                    ShopInformationActivity.this.editText_Email.requestFocus();
                } else if (shop_address.isEmpty()) {
                    ShopInformationActivity.this.editText_Address.setError(ShopInformationActivity.this.getString(R.string.shop_address_cannot_be_empty));
                    ShopInformationActivity.this.editText_Address.requestFocus();
                } else if (shop_currency.isEmpty()) {
                    ShopInformationActivity.this.editText_Currency.setError(ShopInformationActivity.this.getString(R.string.shop_currency_cannot_be_empty));
                    ShopInformationActivity.this.editText_Currency.requestFocus();
                } else if (shop_tax.isEmpty()) {
                    ShopInformationActivity.this.editText_Tax.setError(ShopInformationActivity.this.getString(R.string.tax_in_percentage));
                    ShopInformationActivity.this.editText_Tax.requestFocus();
                } else {
                    DatabaseAccess databaseAccess = DatabaseAccess.getInstance(ShopInformationActivity.this);
                    databaseAccess.open();
                    if (databaseAccess.updateShopInformation(shop_name, shop_contact, shop_email, shop_address, shop_currency, shop_tax)) {
                        Toasty.success(ShopInformationActivity.this, (int) R.string.shop_information_updated_successfully, Toasty.LENGTH_SHORT).show();
                        Intent intent = new Intent(ShopInformationActivity.this, SettingsActivity.class);
                        //intent.addFlags(PagedChannelRandomAccessSource.DEFAULT_TOTAL_BUFSIZE);
                        ShopInformationActivity.this.startActivity(intent);
                        return;
                    }
                    Toasty.error(ShopInformationActivity.this, (int) R.string.failed, Toasty.LENGTH_SHORT).show();
                }
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