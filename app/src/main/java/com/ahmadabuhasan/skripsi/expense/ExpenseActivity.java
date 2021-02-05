package com.ahmadabuhasan.skripsi.expense;

import android.content.Intent;
import android.content.pm.ActivityInfo;
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

import com.ahmadabuhasan.skripsi.DashboardActivity;
import com.ahmadabuhasan.skripsi.R;
import com.ahmadabuhasan.skripsi.adapter.ExpenseAdapter;
import com.ahmadabuhasan.skripsi.database.DatabaseAccess;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.HashMap;
import java.util.List;

import es.dmoral.toasty.Toasty;

/*
 * Created by Ahmad Abu Hasan on 05/02/2021
 */

public class ExpenseActivity extends AppCompatActivity {

    EditText editText_Search;
    FloatingActionButton floatingActionButton_fabAdd;
    ImageView imgNoExpense;
    private RecyclerView recyclerView;
    ExpenseAdapter productAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.all_expense);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        this.editText_Search = findViewById(R.id.et_expense_search);
        this.floatingActionButton_fabAdd = findViewById(R.id.fab_add);
        this.imgNoExpense = findViewById(R.id.image_no_expense);
        this.recyclerView = findViewById(R.id.expense_recyclerview);

        this.recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        this.recyclerView.setHasFixedSize(true);

        this.floatingActionButton_fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExpenseActivity.this.startActivity(new Intent(ExpenseActivity.this, AddExpenseActivity.class));
            }
        });

        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(this);
        databaseAccess.open();
        List<HashMap<String, String>> productData = databaseAccess.getAllExpense();
        Log.d("data", "" + productData.size());
        if (productData.size() <= 0) {
            Toasty.info(this, (int) R.string.no_data_found, Toasty.LENGTH_SHORT).show();
            this.imgNoExpense.setImageResource(R.drawable.no_data);
        } else {
            this.imgNoExpense.setVisibility(View.GONE);
            ExpenseAdapter expenseAdapter = new ExpenseAdapter(this, productData);
            this.productAdapter = expenseAdapter;
            this.recyclerView.setAdapter(expenseAdapter);
        }

        this.editText_Search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                DatabaseAccess databaseAccess = DatabaseAccess.getInstance(ExpenseActivity.this);
                databaseAccess.open();
                List<HashMap<String, String>> searchExpenseList = databaseAccess.searchExpense(s.toString());
                if (searchExpenseList.size() <= 0) {
                    ExpenseActivity.this.recyclerView.setVisibility(View.GONE);
                    ExpenseActivity.this.imgNoExpense.setVisibility(View.VISIBLE);
                    ExpenseActivity.this.imgNoExpense.setImageResource(R.drawable.no_data);
                    return;
                }
                ExpenseActivity.this.recyclerView.setVisibility(View.VISIBLE);
                ExpenseActivity.this.imgNoExpense.setVisibility(View.GONE);
                ExpenseActivity expenseActivity = ExpenseActivity.this;
                expenseActivity.productAdapter = new ExpenseAdapter(expenseActivity, searchExpenseList);
                ExpenseActivity.this.recyclerView.setAdapter(ExpenseActivity.this.productAdapter);
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
        startActivity(new Intent(this, DashboardActivity.class));
        finish();
        //super.onBackPressed();
    }
}