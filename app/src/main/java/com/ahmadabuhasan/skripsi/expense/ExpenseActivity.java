package com.ahmadabuhasan.skripsi.expense;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.ahmadabuhasan.skripsi.R;

/*
 * Created by Ahmad Abu Hasan on 26/01/2021
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
        
        this.editText_Search = findViewById(R.id.et_expense_search);
        this.floatingActionButton_fabAdd = findViewById(R.id.fab_add);
        this.imgNoExpense = findViewById(R.id.image_no_expense);
        this.recyclerView = findViewById(R.id.expense_recyclerview);
        
        this.recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        this.recyclerView.setHasFixedSize(true);
        
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
        
    }
    
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() != android.R.id.home) {
            return super.onOptionsItemSelected(item);
        }
        finish();
        return true;
    }

}
