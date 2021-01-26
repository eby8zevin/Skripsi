package com.ahmadabuhasan.skripsi.expense;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.ahmadabuhasan.skripsi.R;

public class EditExpenseActivity extends AppCompatActivity {
    
    EditText editText_ExpenseName;
    EditText editText_ExpenseNote;
    EditText editText_ExpenseAmount;
    EditText editText_Date;
    EditText editText_Time;
    TextView textView_Edit;
    TextView textView_Update;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_expense);
        
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.edit_expense);

        this.editText_ExpenseName = findViewById(R.id.et_expense_name);
        this.editText_ExpenseNote= findViewById(R.id.et_expense_note);
        this.editText_ExpenseAmount= findViewById(R.id.et_expense_amount);
        this.editText_Date= findViewById(R.id.et_expense_date);
        this.editText_Time= findViewById(R.id.et_expense_time);
        this.textView_Edit = findViewById(R.id.tv_edit_expense);
        this.textView_Update = findViewById(R.id.tv_update_expense);
        
        final String get_expense_id = getIntent().getExtras().getString(DatabaseOpenHelper.EXPENSE_ID);
        String get_expense_name = getIntent().getExtras().getString(DatabaseOpenHelper.EXPENSE_NAME);
        String get_expense_note = getIntent().getExtras().getString(DatabaseOpenHelper.EXPENSE_NOTE);
        String get_expense_amount = getIntent().getExtras().getString(DatabaseOpenHelper.EXPENSE_AMOUNT);
        String get_expense_date = getIntent().getExtras().getString(DatabaseOpenHelper.EXPENSE_DATE);
        String get_expense_time = getIntent().getExtras().getString(DatabaseOpenHelper.EXPENSE_TIME);
        
        this.editText_ExpenseName.setText(get_expense_name);
        this.editText_ExpenseNote.setText(get_expense_note);
        this.editText_ExpenseAmount.setText(get_expense_amount);
        this.editText_Date.setText(get_expense_date);
        this.editText_Time.setText(get_expense_time);
        
        this.editText_ExpenseName.setEnabled(false);
        this.editText_ExpenseNote.setEnabled(false);
        this.editText_ExpenseAmount.setEnabled(false);
        this.editText_Date.setEnabled(false);
        this.editText_Time.setEnabled(false);

        this.textView_Update.setVisibility(View.INVISIBLE);
        
        
    }
    
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() != android.R.id.home) {
            return super.onOptionsItemSelected(item);
        }
        finish();
        return true;
    }

}
