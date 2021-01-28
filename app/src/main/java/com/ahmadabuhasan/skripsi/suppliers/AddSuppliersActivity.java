package com.ahmadabuhasan.skripsi.suppliers;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ahmadabuhasan.skripsi.DashboardActivity;
import com.ahmadabuhasan.skripsi.R;
import com.ahmadabuhasan.skripsi.database.DatabaseAccess;
import com.ahmadabuhasan.skripsi.database.DatabaseOpenHelper;
import com.ajts.androidmads.library.ExcelToSQLite;
import com.obsez.android.lib.filechooser.ChooserDialog;
//import com.itextpdf.text.io.PagedChannelRandomAccessSource;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import es.dmoral.toasty.Toasty;

/*
 * Created by Ahmad Abu Hasan on 28/01/2021
 */

public class AddSuppliersActivity extends AppCompatActivity {

    EditText editText_Name;
    EditText editText_Address;
    EditText editText_Contact;
    EditText editText_Fax;
    EditText editText_Sales;
    EditText editText_Hp;
    EditText editText_Account;
    EditText editText_Information;
    EditText editText_LastUpdate;

    ProgressDialog loading;
    TextView textView_Add;

    Calendar calendar;
    SimpleDateFormat simpleDateFormat;
    private String datetime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_suppliers);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.add_suppliers);

        this.editText_Name = findViewById(R.id.et_supplier_name);
        this.editText_Address = findViewById(R.id.et_supplier_address);
        this.editText_Contact = findViewById(R.id.et_supplier_contact);
        this.editText_Fax = findViewById(R.id.et_supplier_fax);
        this.editText_Sales = findViewById(R.id.et_supplier_sales);
        this.editText_Hp = findViewById(R.id.et_supplier_hp);
        this.editText_Account = findViewById(R.id.et_supplier_account);
        this.editText_Information = findViewById(R.id.et_supplier_information);
        this.editText_LastUpdate = findViewById(R.id.et_supplier_last_update);
        this.editText_LastUpdate.setEnabled(false);
        this.textView_Add = findViewById(R.id.tv_add_supplier);

        this.textView_Add.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SimpleDateFormat")
            @Override
            public void onClick(View v) {
                calendar = Calendar.getInstance();
                simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                datetime = simpleDateFormat.format(calendar.getTime());

                String suppliers_name = AddSuppliersActivity.this.editText_Name.getText().toString().trim();
                String suppliers_address = AddSuppliersActivity.this.editText_Address.getText().toString().trim();
                String suppliers_contact = AddSuppliersActivity.this.editText_Contact.getText().toString().trim();
                String suppliers_fax = AddSuppliersActivity.this.editText_Fax.getText().toString().trim();
                String suppliers_sales = AddSuppliersActivity.this.editText_Sales.getText().toString().trim();
                String suppliers_hp = AddSuppliersActivity.this.editText_Hp.getText().toString().trim();
                String suppliers_account = AddSuppliersActivity.this.editText_Account.getText().toString().trim();
                String suppliers_information = AddSuppliersActivity.this.editText_Information.getText().toString().trim();
                String suppliers_last_update = AddSuppliersActivity.this.datetime;
                //String suppliers_last_update = AddSuppliersActivity.this.editText_LastUpdate.getText().toString().trim();

                if (suppliers_name.isEmpty()) {
                    AddSuppliersActivity.this.editText_Name.setError(AddSuppliersActivity.this.getString(R.string.enter_suppliers_name));
                    AddSuppliersActivity.this.editText_Name.requestFocus();
                } else if (suppliers_address.isEmpty()) {
                    AddSuppliersActivity.this.editText_Address.setError(AddSuppliersActivity.this.getString(R.string.enter_suppliers_address));
                    AddSuppliersActivity.this.editText_Address.requestFocus();
                } else if (suppliers_contact.isEmpty()) {
                    AddSuppliersActivity.this.editText_Contact.setError(AddSuppliersActivity.this.getString(R.string.enter_suppliers_contact));
                    AddSuppliersActivity.this.editText_Contact.requestFocus();
                } else if (suppliers_hp.isEmpty()) {
                    AddSuppliersActivity.this.editText_Hp.setError(AddSuppliersActivity.this.getString(R.string.enter_suppliers_hp));
                    AddSuppliersActivity.this.editText_Hp.requestFocus();
                } else if (suppliers_account.isEmpty()) {
                    AddSuppliersActivity.this.editText_Account.setError(AddSuppliersActivity.this.getString(R.string.enter_suppliers_account));
                    AddSuppliersActivity.this.editText_Account.requestFocus();
                } else {
                    DatabaseAccess databaseAccess = DatabaseAccess.getInstance(AddSuppliersActivity.this);
                    databaseAccess.open();
                    if (databaseAccess.addSuppliers(suppliers_name, suppliers_address, suppliers_contact, suppliers_fax, suppliers_sales, suppliers_hp, suppliers_account, suppliers_information, suppliers_last_update)) {
                        Toasty.success(AddSuppliersActivity.this, (int) R.string.suppliers_successfully_added, Toasty.LENGTH_SHORT).show();
                        Intent intent = new Intent(AddSuppliersActivity.this, SuppliersActivity.class);
                        //intent.addFlags(PagedChannelRandomAccessSource.DEFAULT_TOTAL_BUFSIZE);
                        AddSuppliersActivity.this.startActivity(intent);
                        return;
                    }
                    Toasty.error(AddSuppliersActivity.this, (int) R.string.failed, Toasty.LENGTH_SHORT).show();
                }
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_supplier_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        } else if (item.getItemId() != R.id.menu_import_supplier) {
            return super.onOptionsItemSelected(item);
        } else {
            fileChooser();
            return true;
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, SuppliersActivity.class));
        finish();
        //super.onBackPressed();
    }

    public void fileChooser() {
        new ChooserDialog((Activity) this).displayPath(true).withFilter(false, false, "xls")
                .withChosenListener(new ChooserDialog.Result() {

                    @Override
                    public void onChoosePath(String path, File pathFile) {
                        AddSuppliersActivity.this.onImport(path);
                    }
                }).withOnCancelListener(new DialogInterface.OnCancelListener() {
            public void onCancel(DialogInterface dialog) {
                Log.d("CANCEL", "CANCEL");
                dialog.cancel();
            }
        }).build().show();
    }

    public void onImport(String path) {
        String directory_path = path;
        DatabaseAccess.getInstance(this).open();
        if (!new File(directory_path).exists()) {
            Toast.makeText(this, (int) R.string.no_file_found, Toast.LENGTH_SHORT).show();
        } else {
            new ExcelToSQLite(getApplicationContext(), DatabaseOpenHelper.DATABASE_NAME, false)
                    .importFromFile(directory_path, new ExcelToSQLite.ImportListener() {

                        @Override
                        public void onStart() {
                            AddSuppliersActivity.this.loading = new ProgressDialog(AddSuppliersActivity.this);
                            AddSuppliersActivity.this.loading.setMessage(AddSuppliersActivity.this.getString(R.string.data_importing_please_wait));
                            AddSuppliersActivity.this.loading.setCancelable(false);
                            AddSuppliersActivity.this.loading.show();
                        }

                        @Override
                        public void onCompleted(String dbName) {
                            new Handler().postDelayed(new Runnable() {
                                public void run() {
                                    AddSuppliersActivity.this.loading.dismiss();
                                    Toasty.success(AddSuppliersActivity.this, (int) R.string.data_successfully_imported, Toasty.LENGTH_SHORT).show();
                                    AddSuppliersActivity.this.startActivity(new Intent(AddSuppliersActivity.this, DashboardActivity.class));
                                    AddSuppliersActivity.this.finish();
                                }
                            }, 5000);
                        }

                        @Override
                        public void onError(Exception e) {
                            AddSuppliersActivity.this.loading.dismiss();
                            Log.d("Error : ", "" + e.getMessage());
                            Toasty.error(AddSuppliersActivity.this, (int) R.string.data_import_fail, Toasty.LENGTH_SHORT).show();
                        }
                    });
        }
    }
}