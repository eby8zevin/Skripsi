package com.ahmadabuhasan.skripsi.suppliers;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.ahmadabuhasan.skripsi.R;
import com.ahmadabuhasan.skripsi.database.DatabaseOpenHelper;

/*
 * Created by Ahmad Abu Hasan on 07/01/2021
 */

public class EditSuppliersActivity extends AppCompatActivity {

    EditText editText_Name;
    EditText editText_Address;
    EditText editText_Contact;
    EditText editText_Fax;
    EditText editText_Sales;
    EditText editText_Hp;
    EditText editText_Account;
    EditText editText_Information;
    EditText editText_LastUpdate;

    TextView textView_Edit;
    TextView textView_Update;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_suppliers);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.edit_suppliers);

        this.editText_Name = findViewById(R.id.et_supplier_name);
        this.editText_Address = findViewById(R.id.et_supplier_address);
        this.editText_Contact = findViewById(R.id.et_supplier_contact);
        this.editText_Fax = findViewById(R.id.et_supplier_fax);
        this.editText_Sales = findViewById(R.id.et_supplier_sales);
        this.editText_Hp = findViewById(R.id.et_supplier_hp);
        this.editText_Account = findViewById(R.id.et_supplier_account);
        this.editText_Information = findViewById(R.id.et_supplier_information);
        this.editText_LastUpdate = findViewById(R.id.et_supplier_last_update);

        this.textView_Edit = findViewById(R.id.tv_edit_supplier);
        this.textView_Update = findViewById(R.id.tv_update_supplier);

        final String get_supplier_id = getIntent().getExtras().getString(DatabaseOpenHelper.SUPPLIER_ID);
        String get_supplier_name = getIntent().getExtras().getString(DatabaseOpenHelper.SUPPLIER_NAME);
        String get_supplier_address = getIntent().getExtras().getString(DatabaseOpenHelper.SUPPLIER_ADDRESS);
        String get_supplier_contact = getIntent().getExtras().getString(DatabaseOpenHelper.SUPPLIER_CONTACT);
        String get_supplier_fax = getIntent().getExtras().getString(DatabaseOpenHelper.SUPPLIER_FAX);
        String get_supplier_sales = getIntent().getExtras().getString(DatabaseOpenHelper.SUPPLIER_SALES);
        String get_supplier_hp = getIntent().getExtras().getString(DatabaseOpenHelper.SUPPLIER_HP);
        String get_supplier_account = getIntent().getExtras().getString(DatabaseOpenHelper.SUPPLIER_ACCOUNT);
        String get_supplier_information = getIntent().getExtras().getString(DatabaseOpenHelper.SUPPLIER_INFORMATION);
        String get_supplier_last_update = getIntent().getExtras().getString(DatabaseOpenHelper.SUPPLIER_LAST_UPDATE);

        this.editText_Name.setText(get_supplier_name);
        this.editText_Address.setText(get_supplier_address);
        this.editText_Contact.setText(get_supplier_contact);
        this.editText_Fax.setText(get_supplier_fax);
        this.editText_Sales.setText(get_supplier_sales);
        this.editText_Hp.setText(get_supplier_hp);
        this.editText_Account.setText(get_supplier_account);
        this.editText_Information.setText(get_supplier_information);
        this.editText_LastUpdate.setText(get_supplier_last_update);

        this.editText_Name.setEnabled(false);
        this.editText_Address.setEnabled(false);
        this.editText_Contact.setEnabled(false);
        this.editText_Fax.setEnabled(false);
        this.editText_Sales.setEnabled(false);
        this.editText_Hp.setEnabled(false);
        this.editText_Account.setEnabled(false);
        this.editText_Information.setEnabled(false);
        this.editText_LastUpdate.setEnabled(false);
        this.textView_Update.setVisibility(View.INVISIBLE);
        this.textView_Update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(this, SuppliersActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}