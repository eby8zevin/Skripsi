package com.ahmadabuhasan.skripsi.customers;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.internal.view.SupportMenu;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.ahmadabuhasan.skripsi.R;
import com.ahmadabuhasan.skripsi.database.DatabaseAccess;
import com.ahmadabuhasan.skripsi.database.DatabaseOpenHelper;
//import com.itextpdf.text.io.PagedChannelRandomAccessSource;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import es.dmoral.toasty.Toasty;

/*
 * Created by Ahmad Abu Hasan on 05/02/2021
 */

public class EditCustomersActivity extends AppCompatActivity {

    EditText editText_CustomerName;
    EditText editText_Address;
    EditText editText_Hp;
    EditText editText_Wa;
    EditText editText_Account;
    EditText editText_Information;
    EditText editText_LastUpdate;

    String getCustomer_id;
    String getCustomer_name;
    String getCustomer_address;
    String getCustomer_hp;
    String getCustomer_wa;
    String getCustomer_account;
    String getCustomer_information;
    String getCustomer_last_update;

    ImageView imageView_Copy;
    TextView textView_Edit;
    TextView textView_Update;

    Calendar calendar;
    SimpleDateFormat simpleDateFormat;
    private String datetime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_customers);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.edit_customer);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        this.editText_CustomerName = findViewById(R.id.et_customer_name);
        this.editText_Address = findViewById(R.id.et_customer_address);
        this.editText_Hp = findViewById(R.id.et_customer_hp);
        this.editText_Wa = findViewById(R.id.et_customer_wa);
        this.editText_Account = findViewById(R.id.et_customer_account);
        this.editText_Information = findViewById(R.id.et_customer_information);
        this.editText_LastUpdate = findViewById(R.id.et_customer_last_update);
        this.textView_Edit = findViewById(R.id.tv_edit_customer);
        this.textView_Update = findViewById(R.id.tv_update_customer);
        this.imageView_Copy = findViewById(R.id.copy_hp);

        this.getCustomer_id = getIntent().getExtras().getString(DatabaseOpenHelper.CUSTOMER_ID);
        this.getCustomer_name = getIntent().getExtras().getString(DatabaseOpenHelper.CUSTOMER_NAME);
        this.getCustomer_address = getIntent().getExtras().getString(DatabaseOpenHelper.CUSTOMER_ADDRESS);
        this.getCustomer_hp = getIntent().getExtras().getString(DatabaseOpenHelper.CUSTOMER_HP);
        this.getCustomer_wa = getIntent().getExtras().getString(DatabaseOpenHelper.CUSTOMER_WA);
        this.getCustomer_account = getIntent().getExtras().getString(DatabaseOpenHelper.CUSTOMER_ACCOUNT);
        this.getCustomer_information = getIntent().getExtras().getString(DatabaseOpenHelper.CUSTOMER_INFORMATION);
        this.getCustomer_last_update = getIntent().getExtras().getString(DatabaseOpenHelper.CUSTOMER_LAST_UPDATE);

        this.editText_CustomerName.setText(this.getCustomer_name);
        this.editText_Address.setText(this.getCustomer_address);
        this.editText_Hp.setText(this.getCustomer_hp);
        this.editText_Wa.setText(this.getCustomer_wa);
        this.editText_Account.setText(this.getCustomer_account);
        this.editText_Information.setText(this.getCustomer_information);
        this.editText_LastUpdate.setText(this.getCustomer_last_update);

        this.editText_CustomerName.setEnabled(false);
        this.editText_Address.setEnabled(false);
        this.editText_Hp.setEnabled(false);
        this.editText_Wa.setEnabled(false);
        this.editText_Account.setEnabled(false);
        this.editText_Information.setEnabled(false);
        this.editText_LastUpdate.setEnabled(false);
        this.imageView_Copy.setEnabled(false);
        this.textView_Update.setVisibility(View.INVISIBLE);

        this.textView_Edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditCustomersActivity.this.editText_CustomerName.setEnabled(true);
                EditCustomersActivity.this.editText_Address.setEnabled(true);
                EditCustomersActivity.this.editText_Hp.setEnabled(true);
                EditCustomersActivity.this.editText_Wa.setEnabled(true);
                EditCustomersActivity.this.editText_Account.setEnabled(true);
                EditCustomersActivity.this.editText_Information.setEnabled(true);
                //EditCustomersActivity.this.editText_LastUpdate.setEnabled(false);
                EditCustomersActivity.this.imageView_Copy.setEnabled(true);
                EditCustomersActivity.this.editText_CustomerName.setTextColor(SupportMenu.CATEGORY_MASK);
                EditCustomersActivity.this.editText_Address.setTextColor(SupportMenu.CATEGORY_MASK);
                EditCustomersActivity.this.editText_Hp.setTextColor(SupportMenu.CATEGORY_MASK);
                EditCustomersActivity.this.editText_Wa.setTextColor(SupportMenu.CATEGORY_MASK);
                EditCustomersActivity.this.editText_Account.setTextColor(SupportMenu.CATEGORY_MASK);
                EditCustomersActivity.this.editText_Information.setTextColor(SupportMenu.CATEGORY_MASK);
                EditCustomersActivity.this.editText_LastUpdate.setTextColor(SupportMenu.CATEGORY_MASK);

                EditCustomersActivity.this.textView_Edit.setVisibility(View.GONE);
                EditCustomersActivity.this.textView_Update.setVisibility(View.VISIBLE);

                EditCustomersActivity.this.imageView_Copy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        editText_Wa.setText(editText_Hp.getText().toString());
                    }
                });
            }
        });

        this.textView_Update.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SimpleDateFormat")
            @Override
            public void onClick(View v) {
                calendar = Calendar.getInstance();
                simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                datetime = simpleDateFormat.format(calendar.getTime());

                String customer_name = EditCustomersActivity.this.editText_CustomerName.getText().toString().trim();
                String customer_address = EditCustomersActivity.this.editText_Address.getText().toString().trim();
                String customer_hp = EditCustomersActivity.this.editText_Hp.getText().toString().trim();
                String customer_wa = EditCustomersActivity.this.editText_Wa.getText().toString().trim();
                String customer_account = EditCustomersActivity.this.editText_Account.getText().toString().trim();
                String customer_information = EditCustomersActivity.this.editText_Information.getText().toString().trim();
                String customer_last_update = EditCustomersActivity.this.datetime;
                //String customer_last_update = EditCustomersActivity.this.editText_LastUpdate.getText().toString().trim();

                if (customer_name.isEmpty()) {
                    EditCustomersActivity.this.editText_CustomerName.setError(EditCustomersActivity.this.getString(R.string.enter_customer_name));
                    EditCustomersActivity.this.editText_CustomerName.requestFocus();
                } else if (customer_address.isEmpty()) {
                    EditCustomersActivity.this.editText_Address.setError(EditCustomersActivity.this.getString(R.string.enter_customer_address));
                    EditCustomersActivity.this.editText_Address.requestFocus();
                } else if (customer_hp.isEmpty()) {
                    EditCustomersActivity.this.editText_Hp.setError(EditCustomersActivity.this.getString(R.string.enter_customer_hp));
                    EditCustomersActivity.this.editText_Hp.requestFocus();
                } else if (customer_wa.isEmpty()) {
                    EditCustomersActivity.this.editText_Wa.setError(EditCustomersActivity.this.getString(R.string.enter_customer_wa));
                    EditCustomersActivity.this.editText_Wa.requestFocus();
                } else {
                    DatabaseAccess databaseAccess = DatabaseAccess.getInstance(EditCustomersActivity.this);
                    databaseAccess.open();
                    if (databaseAccess.updateCustomer(EditCustomersActivity.this.getCustomer_id, customer_name, customer_address, customer_hp, customer_wa, customer_account, customer_information, customer_last_update)) {
                        Toasty.success(EditCustomersActivity.this, (int) R.string.customer_successfully_added, Toasty.LENGTH_SHORT).show();
                        Intent intent = new Intent(EditCustomersActivity.this, CustomersActivity.class);
                        //intent.addFlags(PagedChannelRandomAccessSource.DEFAULT_TOTAL_BUFSIZE);
                        EditCustomersActivity.this.startActivity(intent);
                        return;
                    }
                    Toasty.error(EditCustomersActivity.this, (int) R.string.failed, Toasty.LENGTH_SHORT).show();
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