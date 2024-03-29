package com.ahmadabuhasan.skripsi.customers;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ahmadabuhasan.skripsi.CashierDashboard;
import com.ahmadabuhasan.skripsi.DashboardActivity;
import com.ahmadabuhasan.skripsi.R;
import com.ahmadabuhasan.skripsi.adapter.CustomerAdapter;
import com.ahmadabuhasan.skripsi.database.DatabaseAccess;
import com.ahmadabuhasan.skripsi.database.DatabaseOpenHelper;
import com.ajts.androidmads.library.SQLiteToExcel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.obsez.android.lib.filechooser.ChooserDialog;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import es.dmoral.toasty.Toasty;

import static com.ahmadabuhasan.skripsi.LoginActivity.item;

/*
 * Created by Ahmad Abu Hasan on 07/10/2021
 */

public class CustomersActivity extends AppCompatActivity {

    EditText editText_Search;
    FloatingActionButton floatingActionButton_fabAdd;
    ImageView imgNoProduct;
    ProgressDialog loading;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customers);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.all_customer);

        this.editText_Search = findViewById(R.id.et_customer_search);
        this.floatingActionButton_fabAdd = findViewById(R.id.fab_add);
        this.imgNoProduct = findViewById(R.id.image_no_product);
        this.recyclerView = findViewById(R.id.customer_recyclerview);

        this.recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        this.recyclerView.setHasFixedSize(true);

        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(this);
        databaseAccess.open();
        List<HashMap<String, String>> customerData = databaseAccess.getCustomers();
        Log.d("data", "" + customerData.size());
        if (customerData.size() <= 0) {
            Toasty.info(this, (int) R.string.no_customer_found, Toasty.LENGTH_SHORT).show();
            this.imgNoProduct.setImageResource(R.drawable.no_data);
        } else {
            this.imgNoProduct.setVisibility(View.GONE);
            this.recyclerView.setAdapter(new CustomerAdapter(this, customerData));
        }

        this.floatingActionButton_fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomersActivity.this.startActivity(new Intent(CustomersActivity.this, AddCustomersActivity.class));
            }
        });

        this.editText_Search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                DatabaseAccess databaseAccess = DatabaseAccess.getInstance(CustomersActivity.this);
                databaseAccess.open();
                List<HashMap<String, String>> searchCustomerList = databaseAccess.searchCustomers(s.toString());
                if (searchCustomerList.size() <= 0) {
                    CustomersActivity.this.recyclerView.setVisibility(View.GONE);
                    CustomersActivity.this.imgNoProduct.setVisibility(View.VISIBLE);
                    CustomersActivity.this.imgNoProduct.setImageResource(R.drawable.no_data);
                    return;
                }
                CustomersActivity.this.recyclerView.setVisibility(View.VISIBLE);
                CustomersActivity.this.imgNoProduct.setVisibility(View.GONE);
                CustomersActivity.this.recyclerView.setAdapter(new CustomerAdapter(CustomersActivity.this, searchCustomerList));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_customer_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        } else if (item.getItemId() != R.id.menu_export_customer) {
            return super.onOptionsItemSelected(item);
        } else {
            folderChooser();
            return true;
        }
    }

    @Override
    public void onBackPressed() {
        if (item.equals("Cashier")) {
            startActivity(new Intent(this, CashierDashboard.class));
        } else {
            startActivity(new Intent(this, DashboardActivity.class));
        }
        finish();

        //super.onBackPressed();
    }

    public void folderChooser() {
        new ChooserDialog((Activity) this).displayPath(true).withFilter(true, false, new String[0])
                .withChosenListener(new ChooserDialog.Result() {

                    @Override
                    public void onChoosePath(String path, File pathFile) {
                        CustomersActivity.this.onExport(path);
                        Log.d("path", path);
                    }
                }).build().show();
    }

    public void onExport(String path) {
        String directory_path = path;
        File file = new File(directory_path);
        if (!file.exists()) {
            file.mkdirs();
        }
        new SQLiteToExcel(getApplicationContext(), DatabaseOpenHelper.DATABASE_NAME, directory_path)
                .exportSingleTable("customers", "customers.xls", new SQLiteToExcel.ExportListener() {

                    @Override
                    public void onStart() {
                        CustomersActivity.this.loading = new ProgressDialog(CustomersActivity.this);
                        CustomersActivity.this.loading.setMessage(CustomersActivity.this.getString(R.string.data_exporting_please_wait));
                        CustomersActivity.this.loading.setCancelable(false);
                        CustomersActivity.this.loading.show();
                    }

                    @Override
                    public void onCompleted(String filePath) {
                        new Handler().postDelayed(new Runnable() {
                            public void run() {
                                CustomersActivity.this.loading.dismiss();
                                Toasty.success(CustomersActivity.this, (int) R.string.data_successfully_exported, Toasty.LENGTH_SHORT).show();
                            }
                        }, 5000);
                    }

                    @Override
                    public void onError(Exception e) {
                        CustomersActivity.this.loading.dismiss();
                        Toasty.error(CustomersActivity.this, (int) R.string.data_export_fail, Toasty.LENGTH_SHORT).show();
                        Log.d("Error", e.toString());
                    }
                });
    }
}