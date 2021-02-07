package com.ahmadabuhasan.skripsi.expense;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.ahmadabuhasan.skripsi.R;
import com.ahmadabuhasan.skripsi.database.DatabaseAccess;
//import com.itextpdf.text.io.PagedChannelRandomAccessSource;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import es.dmoral.toasty.Toasty;

/*
 * Created by Ahmad Abu Hasan on 07/02/2021
 */

public class AddExpenseActivity extends AppCompatActivity {

    EditText editText_ExpenseName;
    EditText editText_ExpenseNote;
    EditText editText_ExpenseAmount;
    EditText editText_Date;
    EditText editText_Time;
    TextView textView_Add;

    String date_time = "";
    int mMinute;
    int mHour;
    int mDay;
    int mMonth;
    int mYear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.add_expense);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        this.editText_ExpenseName = findViewById(R.id.et_expense_name);
        this.editText_ExpenseNote = findViewById(R.id.et_expense_note);
        this.editText_ExpenseAmount = findViewById(R.id.et_expense_amount);
        this.editText_Date = findViewById(R.id.et_expense_date);
        this.editText_Time = findViewById(R.id.et_expense_time);
        this.textView_Add = findViewById(R.id.tv_add_expense);

        String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(new Date());
        //String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH).format(new Date());
        String currentTime = new SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(new Date());
        this.editText_Date.setText(currentDate);
        this.editText_Time.setText(currentTime);

        this.editText_Date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddExpenseActivity.this.datePicker();
            }
        });

        this.editText_Time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddExpenseActivity.this.timePicker();
            }
        });

        this.textView_Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String expense_name = AddExpenseActivity.this.editText_ExpenseName.getText().toString();
                String expense_note = AddExpenseActivity.this.editText_ExpenseNote.getText().toString();
                String expense_amount = AddExpenseActivity.this.editText_ExpenseAmount.getText().toString();
                String expense_date = AddExpenseActivity.this.editText_Date.getText().toString();
                String expense_time = AddExpenseActivity.this.editText_Time.getText().toString();
                if (expense_name.isEmpty()) {
                    AddExpenseActivity.this.editText_ExpenseName.setError(AddExpenseActivity.this.getString(R.string.expense_name_cannot_be_empty));
                    AddExpenseActivity.this.editText_ExpenseName.requestFocus();
                } else if (expense_amount.isEmpty()) {
                    AddExpenseActivity.this.editText_ExpenseAmount.setError(AddExpenseActivity.this.getString(R.string.expense_amount_cannot_be_empty));
                    AddExpenseActivity.this.editText_ExpenseAmount.requestFocus();
                } else {
                    DatabaseAccess databaseAccess = DatabaseAccess.getInstance(AddExpenseActivity.this);
                    databaseAccess.open();
                    if (databaseAccess.addExpense(expense_name, expense_amount, expense_note, expense_date, expense_time)) {
                        Toasty.success(AddExpenseActivity.this, (int) R.string.expense_successfully_added, Toasty.LENGTH_SHORT).show();
                        Intent intent = new Intent(AddExpenseActivity.this, ExpenseActivity.class);
                        //intent.addFlags(PagedChannelRandomAccessSource.DEFAULT_TOTAL_BUFSIZE);
                        AddExpenseActivity.this.startActivity(intent);
                        return;
                    }
                    Toasty.error(AddExpenseActivity.this, (int) R.string.failed, Toasty.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void datePicker() {
        Calendar calendar = Calendar.getInstance();
        this.mYear = calendar.get(Calendar.YEAR);
        this.mMonth = calendar.get(Calendar.MONTH);
        this.mDay = calendar.get(Calendar.DAY_OF_MONTH);
        new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                int month = monthOfYear + 1;
                String fm = "" + month;
                String fd = "" + dayOfMonth;
                if (monthOfYear < 10) {
                    fm = "0" + month;
                }
                if (dayOfMonth < 10) {
                    fd = "0" + dayOfMonth;
                }
                AddExpenseActivity.this.date_time = year + "-" + fm + "-" + fd;
                //AddExpenseActivity.this.date_time = fd + "-" + fm + "-" + year;
                AddExpenseActivity.this.editText_Date.setText(AddExpenseActivity.this.date_time);
            }
        }, this.mYear, this.mMonth, this.mDay).show();
    }

    private void timePicker() {
        Calendar calendar = Calendar.getInstance();
        this.mHour = calendar.get(Calendar.HOUR_OF_DAY);
        this.mMinute = calendar.get(Calendar.MINUTE);
        new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @SuppressLint("SetTextI18n")
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String am_pm;
                AddExpenseActivity.this.mHour = hourOfDay;
                AddExpenseActivity.this.mMinute = minute;
                if (AddExpenseActivity.this.mHour < 12) {
                    am_pm = "AM";
                    AddExpenseActivity.this.mHour = hourOfDay;
                } else {
                    am_pm = "PM";
                    AddExpenseActivity.this.mHour = hourOfDay - 12;
                }
                EditText editText = AddExpenseActivity.this.editText_Time;
                editText.setText(AddExpenseActivity.this.mHour + ":" + minute + " " + am_pm);
            }
        }, this.mHour, this.mMinute, false).show();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() != android.R.id.home) {
            return super.onOptionsItemSelected(item);
        }
        finish();
        return true;
    }
}