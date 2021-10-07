package com.ahmadabuhasan.skripsi.settings.categories;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ahmadabuhasan.skripsi.R;
import com.ahmadabuhasan.skripsi.adapter.CategoryAdapter;
import com.ahmadabuhasan.skripsi.database.DatabaseAccess;
import com.ahmadabuhasan.skripsi.settings.SettingsActivity;
import com.ahmadabuhasan.skripsi.settings.SettingsWarehouse;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.HashMap;
import java.util.List;

import es.dmoral.toasty.Toasty;

import static com.ahmadabuhasan.skripsi.LoginActivity.item;

/*
 * Created by Ahmad Abu Hasan on 07/10/2021
 */

public class CategoriesActivity extends AppCompatActivity {

    EditText editText_Search;
    FloatingActionButton floatingActionButton_fabAdd;
    ImageView imgNoProduct;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.categories);

        this.editText_Search = findViewById(R.id.et_categories_search);
        this.floatingActionButton_fabAdd = findViewById(R.id.fab_add);
        this.imgNoProduct = findViewById(R.id.image_no_product);
        this.recyclerView = findViewById(R.id.categories_recyclerview);

        this.recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        this.recyclerView.setHasFixedSize(true);

        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(this);
        databaseAccess.open();
        List<HashMap<String, String>> categoryData = databaseAccess.getProductCategory();
        Log.d("data", "" + categoryData.size());
        if (categoryData.size() <= 0) {
            Toasty.info(this, (int) R.string.no_data_found, Toasty.LENGTH_SHORT).show();
            this.imgNoProduct.setImageResource(R.drawable.no_data);
        } else {
            this.imgNoProduct.setVisibility(View.GONE);
            this.recyclerView.setAdapter(new CategoryAdapter(this, categoryData));
        }

        this.floatingActionButton_fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CategoriesActivity.this.startActivity(new Intent(CategoriesActivity.this, AddCategoryActivity.class));
            }
        });

        this.editText_Search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                DatabaseAccess databaseAccess = DatabaseAccess.getInstance(CategoriesActivity.this);
                databaseAccess.open();
                List<HashMap<String, String>> searchCategoryList = databaseAccess.searchProductCategory(s.toString());
                if (searchCategoryList.size() <= 0) {
                    CategoriesActivity.this.recyclerView.setVisibility(View.GONE);
                    CategoriesActivity.this.imgNoProduct.setVisibility(View.VISIBLE);
                    CategoriesActivity.this.imgNoProduct.setImageResource(R.drawable.no_data);
                    return;
                }
                CategoriesActivity.this.recyclerView.setVisibility(View.VISIBLE);
                CategoriesActivity.this.imgNoProduct.setVisibility(View.GONE);
                CategoriesActivity.this.recyclerView.setAdapter(new CategoryAdapter(CategoriesActivity.this, searchCategoryList));
            }

            @Override
            public void afterTextChanged(Editable s) {

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
        if (item.equals("Warehouse")) {
            startActivity(new Intent(this, SettingsWarehouse.class));
        } else {
            startActivity(new Intent(this, SettingsActivity.class));
        }
        finish();

        //super.onBackPressed();
    }
}