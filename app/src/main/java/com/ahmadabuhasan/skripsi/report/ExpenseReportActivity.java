package com.ahmadabuhasan.skripsi.report;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ahmadabuhasan.skripsi.R;
import com.ahmadabuhasan.skripsi.adapter.ExpenseAdapter;
import com.ahmadabuhasan.skripsi.database.DatabaseAccess;
import com.ahmadabuhasan.skripsi.database.DatabaseOpenHelper;
import com.ajts.androidmads.library.SQLiteToExcel;
import com.obsez.android.lib.filechooser.ChooserDialog;

import java.io.File;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import es.dmoral.toasty.Toasty;

/*
 * Created by Ahmad Abu Hasan on 28/01/2021
 */

public class ExpenseReportActivity extends AppCompatActivity {

    TextView textView_TotalPrice;
    TextView textView_NoExpense;
    ImageView imageView_NoExpense;
    ProgressDialog loading;
    private ExpenseAdapter expenseAdapter;
    private RecyclerView recyclerView;
    List<HashMap<String, String>> expenseList;
    double total_price;
    DecimalFormat decimalFormat;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_report);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.all_expense);

        this.textView_TotalPrice = findViewById(R.id.tv_total_price);
        this.textView_NoExpense = findViewById(R.id.tv_no_expense);
        this.imageView_NoExpense = findViewById(R.id.image_no_expense);
        this.recyclerView = findViewById(R.id.expense_report_recyclerview);

        this.recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        this.recyclerView.setHasFixedSize(true);

        this.decimalFormat = new DecimalFormat("#0.00");
        this.imageView_NoExpense.setVisibility(View.GONE);
        this.textView_NoExpense.setVisibility(View.GONE);

        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(this);
        databaseAccess.open();
        ArrayList<HashMap<String, String>> allExpense = databaseAccess.getAllExpense();
        this.expenseList = allExpense;
        if (allExpense.size() <= 0) {
            Toasty.info(this, (int) R.string.no_data_found, Toasty.LENGTH_SHORT).show();
            this.recyclerView.setVisibility(View.GONE);
            this.imageView_NoExpense.setVisibility(View.VISIBLE);
            this.imageView_NoExpense.setImageResource(R.drawable.not_found);
            this.textView_TotalPrice.setVisibility(View.VISIBLE);
            this.textView_TotalPrice.setVisibility(View.GONE);
        } else {
            ExpenseAdapter expenseAdapter1 = new ExpenseAdapter(this, this.expenseList);
            this.expenseAdapter = expenseAdapter1;
            this.recyclerView.setAdapter(expenseAdapter1);
        }

        databaseAccess.open();
        String currency = databaseAccess.getCurrency();

        databaseAccess.open();
        this.total_price = databaseAccess.getTotalExpense("all");
        TextView textView = this.textView_TotalPrice;
        textView.setText(getString(R.string.total_expense) + ": " + currency + " " + NumberFormat.getInstance(Locale.getDefault()).format(this.total_price));
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.all_sales_menu, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.menu_all_sales:
                getReport("all");
                return true;
            case R.id.menu_daily:
                getReport(DatabaseOpenHelper.DAILY);
                return true;
            case R.id.menu_monthly:
                getReport(DatabaseOpenHelper.MONTHLY);
                return true;
            case R.id.menu_yearly:
                getReport(DatabaseOpenHelper.YEARLY);
                return true;
            case R.id.menu_export_data:
                folderChooser();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @SuppressLint("SetTextI18n")
    public void getReport(String type) {
        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(this);
        databaseAccess.open();
        ArrayList<HashMap<String, String>> expenseReport = databaseAccess.getExpenseReport(type);
        this.expenseList = expenseReport;
        if (expenseReport.size() <= 0) {
            Toasty.info(this, "No Data Found", Toasty.LENGTH_SHORT).show();
            this.recyclerView.setVisibility(View.GONE);
            this.imageView_NoExpense.setVisibility(View.VISIBLE);
            this.imageView_NoExpense.setImageResource(R.drawable.not_found);
            this.textView_NoExpense.setVisibility(View.VISIBLE);
            this.textView_TotalPrice.setVisibility(View.GONE);
        } else {
            ExpenseAdapter expenseAdapter2 = new ExpenseAdapter(this, this.expenseList);
            this.expenseAdapter = expenseAdapter2;
            this.recyclerView.setAdapter(expenseAdapter2);
            this.recyclerView.setVisibility(View.VISIBLE);
            this.imageView_NoExpense.setVisibility(View.GONE);
            this.textView_NoExpense.setVisibility(View.GONE);
            this.textView_TotalPrice.setVisibility(View.VISIBLE);
        }

        databaseAccess.open();
        String currency = databaseAccess.getCurrency();

        databaseAccess.open();
        this.total_price = databaseAccess.getTotalExpense(type);
        TextView textView = this.textView_TotalPrice;
        textView.setText(getString(R.string.total_expense) + ": " + currency + " " + NumberFormat.getInstance(Locale.getDefault()).format(this.total_price));
    }

    public void folderChooser() {
        new ChooserDialog((Activity) this).displayPath(true).withFilter(true, false, new String[0])
                .withChosenListener(new ChooserDialog.Result() {

                    @Override
                    public void onChoosePath(String path, File pathFile) {
                        ExpenseReportActivity.this.onExport(path);
                        Log.d("path", path);
                    }
                }).build().show();
    }

    public void onExport(String path) {
        String directory_path = path;
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        new SQLiteToExcel(getApplicationContext(), DatabaseOpenHelper.DATABASE_NAME, directory_path)
                .exportSingleTable("expense", "expense.xls", new SQLiteToExcel.ExportListener() {

                    @Override
                    public void onStart() {
                        ExpenseReportActivity.this.loading = new ProgressDialog(ExpenseReportActivity.this);
                        ExpenseReportActivity.this.loading.setMessage(ExpenseReportActivity.this.getString(R.string.data_exporting_please_wait));
                        ExpenseReportActivity.this.loading.setCancelable(false);
                        ExpenseReportActivity.this.loading.show();
                    }

                    @Override
                    public void onCompleted(String filePath) {
                        new Handler().postDelayed(new Runnable() {
                            public void run() {
                                ExpenseReportActivity.this.loading.dismiss();
                                Toasty.success(ExpenseReportActivity.this, (int) R.string.data_successfully_exported, Toasty.LENGTH_SHORT).show();
                            }
                        }, 5000);
                    }

                    @Override
                    public void onError(Exception e) {
                        ExpenseReportActivity.this.loading.dismiss();
                        Toasty.error(ExpenseReportActivity.this, (int) R.string.data_export_fail, Toasty.LENGTH_SHORT).show();
                    }
                });
    }
}