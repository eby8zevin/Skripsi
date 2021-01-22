package com.ahmadabuhasan.skripsi.customers;

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
import android.widget.ImageView;
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
 * Created by Ahmad Abu Hasan on 22/01/2021
 */

public class AddCustomersActivity extends AppCompatActivity {

    EditText editText_CustomerName;
    EditText editText_Address;
    EditText editText_Hp;
    EditText editText_Wa;
    EditText editText_Account;
    EditText editText_Information;
    EditText editText_LastUpdate;

    ProgressDialog loading;
    ImageView imageView_Copy;
    TextView textView_AddCustomer;

    Calendar calendar;
    SimpleDateFormat simpleDateFormat;
    private String datetime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_customers);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.add_customer);

        this.editText_CustomerName = findViewById(R.id.et_customer_name);
        this.editText_Address = findViewById(R.id.et_customer_address);
        this.editText_Hp = findViewById(R.id.et_customer_hp);
        this.editText_Wa = findViewById(R.id.et_customer_wa);
        this.editText_Account = findViewById(R.id.et_customer_account);
        this.editText_Information = findViewById(R.id.et_customer_information);
        this.editText_LastUpdate = findViewById(R.id.et_customer_last_update);
        this.editText_LastUpdate.setEnabled(false);
        this.textView_AddCustomer = findViewById(R.id.tv_add_customer);
        this.imageView_Copy = findViewById(R.id.copy_hp);

        this.imageView_Copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText_Wa.setText(editText_Hp.getText().toString());
            }
        });

        this.textView_AddCustomer.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SimpleDateFormat")
            @Override
            public void onClick(View v) {
                calendar = Calendar.getInstance();
                simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                datetime = simpleDateFormat.format(calendar.getTime());

                String customer_name = AddCustomersActivity.this.editText_CustomerName.getText().toString().trim();
                String customer_address = AddCustomersActivity.this.editText_Address.getText().toString().trim();
                String customer_hp = AddCustomersActivity.this.editText_Hp.getText().toString().trim();
                String customer_wa = AddCustomersActivity.this.editText_Wa.getText().toString().trim();
                String customer_account = AddCustomersActivity.this.editText_Account.getText().toString().trim();
                String customer_information = AddCustomersActivity.this.editText_Information.getText().toString().trim();
                String customer_last_update = AddCustomersActivity.this.datetime;
                //String customer_last_update = AddCustomersActivity.this.editText_LastUpdate.getText().toString().trim();

                if (customer_name.isEmpty()) {
                    AddCustomersActivity.this.editText_CustomerName.setError(AddCustomersActivity.this.getString(R.string.enter_customer_name));
                    AddCustomersActivity.this.editText_CustomerName.requestFocus();
                } else if (customer_address.isEmpty()) {
                    AddCustomersActivity.this.editText_Address.setError(AddCustomersActivity.this.getString(R.string.enter_customer_address));
                    AddCustomersActivity.this.editText_Address.requestFocus();
                } else if (customer_hp.isEmpty()) {
                    AddCustomersActivity.this.editText_Hp.setError(AddCustomersActivity.this.getString(R.string.enter_customer_hp));
                    AddCustomersActivity.this.editText_Hp.requestFocus();
                } else if (customer_wa.isEmpty()) {
                    AddCustomersActivity.this.editText_Wa.setError(AddCustomersActivity.this.getString(R.string.enter_customer_wa));
                    AddCustomersActivity.this.editText_Wa.requestFocus();
                } else {
                    DatabaseAccess databaseAccess = DatabaseAccess.getInstance(AddCustomersActivity.this);
                    databaseAccess.open();
                    if (databaseAccess.addCustomer(customer_name, customer_address, customer_hp, customer_wa, customer_account, customer_information, customer_last_update)) {
                        Toasty.success(AddCustomersActivity.this, (int) R.string.customer_successfully_added, Toasty.LENGTH_SHORT).show();
                        Intent intent = new Intent(AddCustomersActivity.this, CustomersActivity.class);
                        //intent.addFlags(PagedChannelRandomAccessSource.DEFAULT_TOTAL_BUFSIZE);
                        AddCustomersActivity.this.startActivity(intent);
                        return;
                    }
                    Toasty.error(AddCustomersActivity.this, (int) R.string.failed, Toasty.LENGTH_SHORT).show();
                }
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_product_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            Intent intent = new Intent(this, CustomersActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
            return true;
        } else if (itemId != R.id.menu_import) {
            return super.onOptionsItemSelected(item);
        } else {
            fileChooser();
            return true;
        }
    }

    public void fileChooser() {
        new ChooserDialog((Activity) this).displayPath(true).withFilter(false, false, "xls").withChosenListener(new ChooserDialog.Result() {
            @Override
            public void onChoosePath(String path, File pathFile) {
                AddCustomersActivity.this.onImport(path);
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
            new ExcelToSQLite(getApplicationContext(), DatabaseOpenHelper.DATABASE_NAME, false).importFromFile(directory_path, new ExcelToSQLite.ImportListener() {
                @Override
                public void onStart() {
                    AddCustomersActivity.this.loading = new ProgressDialog(AddCustomersActivity.this);
                    AddCustomersActivity.this.loading.setMessage(AddCustomersActivity.this.getString(R.string.data_importing_please_wait));
                    AddCustomersActivity.this.loading.setCancelable(false);
                    AddCustomersActivity.this.loading.show();
                }

                @Override
                public void onCompleted(String dbName) {
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            AddCustomersActivity.this.loading.dismiss();
                            Toasty.success(AddCustomersActivity.this, (int) R.string.data_successfully_imported, Toasty.LENGTH_SHORT).show();
                            AddCustomersActivity.this.startActivity(new Intent(AddCustomersActivity.this, DashboardActivity.class));
                            AddCustomersActivity.this.finish();
                        }
                    }, 5000);
                }

                @Override
                public void onError(Exception e) {
                    AddCustomersActivity.this.loading.dismiss();
                    Log.d("Error : ", "" + e.getMessage());
                    Toasty.error(AddCustomersActivity.this, (int) R.string.data_import_fail, Toasty.LENGTH_SHORT).show();
                }
            });
        }
    }
}