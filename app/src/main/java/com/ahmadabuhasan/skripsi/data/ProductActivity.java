package com.ahmadabuhasan.skripsi.data;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import com.ahmadabuhasan.skripsi.DashboardActivity;
import com.ahmadabuhasan.skripsi.R;
import com.ahmadabuhasan.skripsi.adapter.ProductAdapter;
import com.ahmadabuhasan.skripsi.database.DatabaseAccess;
import com.ahmadabuhasan.skripsi.database.DatabaseOpenHelper;
import com.ajts.androidmads.library.SQLiteToExcel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.obsez.android.lib.filechooser.ChooserDialog;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import es.dmoral.toasty.Toasty;

/*
 * Created by Ahmad Abu Hasan on 05/01/2021
 */

public class ProductActivity extends AppCompatActivity {

    EditText editText_Search;
    FloatingActionButton floatingActionButton_fabAdd;
    ImageView imgNoProduct;
    ProgressDialog loading;
    ProductAdapter productAdapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.all_product);

        this.floatingActionButton_fabAdd = findViewById(R.id.fab_add);
        this.editText_Search = findViewById(R.id.et_search);
        this.recyclerView = findViewById(R.id.product_recyclerview);
        this.imgNoProduct = findViewById(R.id.image_no_product);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        this.recyclerView.setHasFixedSize(true);

        this.floatingActionButton_fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProductActivity.this.startActivity(new Intent(ProductActivity.this, AddProductActivity.class));
            }
        });

        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(this);
        databaseAccess.open();
        List<HashMap<String, String>> productData = databaseAccess.getProducts();
        Log.d("data", "" + productData.size());
        if (productData.size() <= 0) {
            Toasty.info(this, (int) R.string.no_product_found, Toasty.LENGTH_SHORT).show();
            this.imgNoProduct.setImageResource(R.drawable.no_data);
        } else {
            this.imgNoProduct.setVisibility(View.GONE);
            ProductAdapter productAdapter1 = new ProductAdapter(this, productData);
            this.productAdapter = productAdapter1;
            this.recyclerView.setAdapter(productAdapter1);
        }

        this.editText_Search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                DatabaseAccess databaseAccess = DatabaseAccess.getInstance(ProductActivity.this);
                databaseAccess.open();
                List<HashMap<String, String>> searchProductList = databaseAccess.getSearchProducts(s.toString());
                if (searchProductList.size() <= 0) {
                    ProductActivity.this.recyclerView.setVisibility(View.GONE);
                    ProductActivity.this.imgNoProduct.setVisibility(View.VISIBLE);
                    ProductActivity.this.imgNoProduct.setImageResource(R.drawable.no_data);
                    return;
                }
                ProductActivity.this.recyclerView.setVisibility(View.VISIBLE);
                ProductActivity.this.imgNoProduct.setVisibility(View.GONE);
                ProductActivity productActivity = ProductActivity.this;
                productActivity.productAdapter = new ProductAdapter(productActivity, searchProductList);
                ProductActivity.this.recyclerView.setAdapter(ProductActivity.this.productAdapter);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.all_product_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            Intent intent = new Intent(this, DashboardActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            return true;
        } else if (itemId != R.id.menu_export) {
            return super.onOptionsItemSelected(item);
        } else {
            folderChooser();
            return true;
        }
    }

    public void folderChooser() {
        new ChooserDialog((Activity) this).displayPath(true).withFilter(true, false, new String[0]).withChosenListener(new ChooserDialog.Result() {

            @Override
            public void onChoosePath(String path, File pathFile) {
                ProductActivity.this.onExport(path);
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
        new SQLiteToExcel(getApplicationContext(), DatabaseOpenHelper.DATABASE_NAME, directory_path).exportSingleTable("products", "products.xls", new SQLiteToExcel.ExportListener() {

            @Override
            public void onStart() {
                ProductActivity.this.loading = new ProgressDialog(ProductActivity.this);
                ProductActivity.this.loading.setMessage(ProductActivity.this.getString(R.string.data_exporting_please_wait));
                ProductActivity.this.loading.setCancelable(false);
                ProductActivity.this.loading.show();
            }

            @Override
            public void onCompleted(String filePath) {
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        ProductActivity.this.loading.dismiss();
                        Toasty.success(ProductActivity.this, (int) R.string.data_successfully_exported, Toasty.LENGTH_SHORT).show();
                    }
                }, 5000);
            }

            @Override
            public void onError(Exception e) {
                ProductActivity.this.loading.dismiss();
                Toasty.error(ProductActivity.this, (int) R.string.data_export_fail, Toasty.LENGTH_SHORT).show();
            }
        });
    }
}