package com.ahmadabuhasan.skripsi.expense;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.ahmadabuhasan.skripsi.R;

public class AddExpenseActivity extends AppCompatActivity {
    
    EditText editText_ExpenseName;
    EditText editText_ExpenseNote;
    EditText editText_ExpenseAmount;
    EditText editText_Date;
    EditText editText_Time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);
        
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.add_expense);
        
        this.editText_ExpenseName = findViewById(R.id.et_expense_name);
        this.editText_ExpenseNote= findViewById(R.id.et_expense_note);
        this.editText_ExpenseAmount= findViewById(R.id.et_expense_amount);
        this.editText_Date= findViewById(R.id.et_expense_date);
        this.editText_Time= findViewById(R.id.et_expense_time);
        
        String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(new Date());
        String currentTime = new SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(new Date());
        this.editText_Date.setText(currentDate);
        this.editText_Time.setText(currentTime);
        
    }
}
