package com.ahmadabuhasan.skripsi.expense;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.internal.view.SupportMenu;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.ahmadabuhasan.skripsi.R;
import com.ahmadabuhasan.skripsi.database.DatabaseAccess;
import com.ahmadabuhasan.skripsi.database.DatabaseOpenHelper;
//import com.itextpdf.text.io.PagedChannelRandomAccessSource;

import java.util.Calendar;

import es.dmoral.toasty.Toasty;

/*
 * Created by Ahmad Abu Hasan on 28/01/2021
 */

public class EditExpenseActivity extends AppCompatActivity {

    EditText editText_ExpenseName;
    EditText editText_ExpenseNote;
    EditText editText_ExpenseAmount;
    EditText editText_Date;
    EditText editText_Time;
    TextView textView_Edit;
    TextView textView_Update;

    String date_time = "";
    int mMinute;
    int mHour;
    int mDay;
    int mMonth;
    int mYear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_expense);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.edit_expense);

        this.editText_ExpenseName = findViewById(R.id.et_expense_name);
        this.editText_ExpenseNote = findViewById(R.id.et_expense_note);
        this.editText_ExpenseAmount = findViewById(R.id.et_expense_amount);
        this.editText_Date = findViewById(R.id.et_expense_date);
        this.editText_Time = findViewById(R.id.et_expense_time);
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

        this.editText_Date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditExpenseActivity.this.datePicker();
            }
        });

        this.editText_Time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditExpenseActivity.this.timePicker();
            }
        });

        this.textView_Edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditExpenseActivity.this.editText_ExpenseName.setEnabled(true);
                EditExpenseActivity.this.editText_ExpenseNote.setEnabled(true);
                EditExpenseActivity.this.editText_ExpenseAmount.setEnabled(true);
                EditExpenseActivity.this.editText_Date.setEnabled(true);
                EditExpenseActivity.this.editText_Time.setEnabled(true);
                EditExpenseActivity.this.editText_ExpenseName.setTextColor(SupportMenu.CATEGORY_MASK);
                EditExpenseActivity.this.editText_ExpenseNote.setTextColor(SupportMenu.CATEGORY_MASK);
                EditExpenseActivity.this.editText_ExpenseAmount.setTextColor(SupportMenu.CATEGORY_MASK);
                EditExpenseActivity.this.editText_Date.setTextColor(SupportMenu.CATEGORY_MASK);
                EditExpenseActivity.this.editText_Time.setTextColor(SupportMenu.CATEGORY_MASK);
                EditExpenseActivity.this.textView_Edit.setVisibility(View.GONE);
                EditExpenseActivity.this.textView_Update.setVisibility(View.VISIBLE);
            }
        });

        this.textView_Update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String expense_name = EditExpenseActivity.this.editText_ExpenseName.getText().toString();
                String expense_note = EditExpenseActivity.this.editText_ExpenseNote.getText().toString();
                String expense_amount = EditExpenseActivity.this.editText_ExpenseAmount.getText().toString();
                String expense_date = EditExpenseActivity.this.editText_Date.getText().toString();
                String expense_time = EditExpenseActivity.this.editText_Time.getText().toString();
                if (expense_name.isEmpty()) {
                    EditExpenseActivity.this.editText_ExpenseName.setError(EditExpenseActivity.this.getString(R.string.expense_name_cannot_be_empty));
                    EditExpenseActivity.this.editText_ExpenseName.requestFocus();
                } else if (expense_amount.isEmpty()) {
                    EditExpenseActivity.this.editText_ExpenseAmount.setError(EditExpenseActivity.this.getString(R.string.expense_amount_cannot_be_empty));
                    EditExpenseActivity.this.editText_ExpenseAmount.requestFocus();
                } else {
                    DatabaseAccess databaseAccess = DatabaseAccess.getInstance(EditExpenseActivity.this);
                    databaseAccess.open();
                    if (databaseAccess.updateExpense(get_expense_id, expense_name, expense_amount, expense_note, expense_date, expense_time)) {
                        Toasty.success(EditExpenseActivity.this, (int) R.string.update_successfully, Toasty.LENGTH_SHORT).show();
                        Intent intent = new Intent(EditExpenseActivity.this, ExpenseActivity.class);
                        //intent.addFlags(PagedChannelRandomAccessSource.DEFAULT_TOTAL_BUFSIZE);
                        EditExpenseActivity.this.startActivity(intent);
                        return;
                    }
                    Toasty.error(EditExpenseActivity.this, (int) R.string.failed, Toasty.LENGTH_SHORT).show();
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
                //EditExpenseActivity.this.date_time = year + "-" + fm + "-" + fd;
                EditExpenseActivity.this.date_time = fd + "-" + fm + "-" + year;
                EditExpenseActivity.this.editText_Date.setText(EditExpenseActivity.this.date_time);
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
                EditExpenseActivity.this.mHour = hourOfDay;
                EditExpenseActivity.this.mMinute = minute;
                if (EditExpenseActivity.this.mHour < 12) {
                    am_pm = "AM";
                    EditExpenseActivity.this.mHour = hourOfDay;
                } else {
                    am_pm = "PM";
                    EditExpenseActivity.this.mHour = hourOfDay - 12;
                }
                EditText editText = EditExpenseActivity.this.editText_Time;
                editText.setText(EditExpenseActivity.this.mHour + ":" + minute + " " + am_pm);
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