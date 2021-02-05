package com.ahmadabuhasan.skripsi.report;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ahmadabuhasan.skripsi.R;
import com.ahmadabuhasan.skripsi.adapter.SalesReportAdapter;
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
 * Created by Ahmad Abu Hasan on 05/02/2021
 */

public class SalesReportActivity extends AppCompatActivity {

    TextView textView_TotalPrice;
    TextView textView_TotalTax;
    TextView textView_Discount;
    TextView textView_NetSales;
    TextView textView_NoSales;
    ImageView imageView_NoSales;
    ProgressDialog loading;
    private SalesReportAdapter orderDetailsAdapter;
    private RecyclerView recyclerView;
    List<HashMap<String, String>> orderDetailsList;
    DecimalFormat decimalFormat;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_report);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.all_sales);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        this.textView_TotalPrice = findViewById(R.id.tv_total_price);
        this.textView_TotalTax = findViewById(R.id.tv_total_tax);
        this.textView_Discount = findViewById(R.id.tv_total_discount);
        this.textView_NetSales = findViewById(R.id.tv_net_sales);
        this.textView_NoSales = findViewById(R.id.tv_no_sales);
        this.imageView_NoSales = findViewById(R.id.image_no_sales);
        this.recyclerView = findViewById(R.id.sales_report_recyclerview);

        this.recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        this.recyclerView.setHasFixedSize(true);

        this.decimalFormat = new DecimalFormat("#0.00");
        this.imageView_NoSales.setVisibility(View.GONE);
        this.textView_NoSales.setVisibility(View.GONE);

        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(this);
        databaseAccess.open();
        ArrayList<HashMap<String, String>> allSalesItems = databaseAccess.getAllSalesItems();
        this.orderDetailsList = allSalesItems;
        if (allSalesItems.size() <= 0) {
            Toasty.info(this, (int) R.string.no_data_found, Toasty.LENGTH_SHORT).show();
            this.recyclerView.setVisibility(View.GONE);
            this.imageView_NoSales.setVisibility(View.VISIBLE);
            this.imageView_NoSales.setImageResource(R.drawable.not_found);
            this.textView_NoSales.setVisibility(View.VISIBLE);
            this.textView_TotalPrice.setVisibility(View.GONE);
        } else {
            SalesReportAdapter salesReportAdapter1 = new SalesReportAdapter(this, this.orderDetailsList);
            this.orderDetailsAdapter = salesReportAdapter1;
            this.recyclerView.setAdapter(salesReportAdapter1);
        }

        databaseAccess.open();
        String currency = databaseAccess.getCurrency();

        databaseAccess.open();
        double sub_total = databaseAccess.getTotalOrderPrice("all");
        TextView textView = this.textView_TotalPrice;
        textView.setText(getString(R.string.total_sales) + " " + currency + " " + NumberFormat.getInstance(Locale.getDefault()).format(sub_total));

        databaseAccess.open();
        double get_tax = databaseAccess.getTotalTax("all");
        TextView textView2 = this.textView_TotalTax;
        textView2.setText(getString(R.string.total_tax) + " (+) : " + currency + " " + NumberFormat.getInstance(Locale.getDefault()).format(get_tax));

        databaseAccess.open();
        double get_discount = databaseAccess.getTotalDiscount("all");
        TextView textView3 = this.textView_Discount;
        textView3.setText(getString(R.string.total_discount) + " (-) : " + currency + " " + NumberFormat.getInstance(Locale.getDefault()).format(get_discount));

        TextView textView4 = this.textView_NetSales;
        textView4.setText(getString(R.string.net_sales) + ": " + currency + " " + NumberFormat.getInstance(Locale.getDefault()).format((sub_total + get_tax) - get_discount));
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
        Log.d("TYPE", type);
        ArrayList<HashMap<String, String>> salesReport = databaseAccess.getSalesReport(type);
        this.orderDetailsList = salesReport;
        if (salesReport.size() <= 0) {
            Toasty.info(this, (int) R.string.no_data_found, Toasty.LENGTH_SHORT).show();
            this.recyclerView.setVisibility(View.GONE);
            this.imageView_NoSales.setVisibility(View.VISIBLE);
            this.imageView_NoSales.setImageResource(R.drawable.not_found);
            this.textView_NoSales.setVisibility(View.VISIBLE);
            this.textView_TotalPrice.setVisibility(View.GONE);
        } else {
            SalesReportAdapter salesReportAdapter2 = new SalesReportAdapter(this, this.orderDetailsList);
            this.orderDetailsAdapter = salesReportAdapter2;
            this.recyclerView.setAdapter(salesReportAdapter2);
            this.recyclerView.setVisibility(View.VISIBLE);
            this.imageView_NoSales.setVisibility(View.GONE);
            this.textView_NoSales.setVisibility(View.GONE);
            this.textView_TotalPrice.setVisibility(View.VISIBLE);
        }

        databaseAccess.open();
        String currency = databaseAccess.getCurrency();

        databaseAccess.open();
        double sub_total = databaseAccess.getTotalOrderPrice(type);
        TextView textView = this.textView_TotalPrice;
        textView.setText(getString(R.string.total_sales) + " " + currency + " " + NumberFormat.getInstance(Locale.getDefault()).format(sub_total));

        databaseAccess.open();
        double get_tax = databaseAccess.getTotalTax(type);
        TextView textView1 = this.textView_TotalTax;
        textView1.setText(getString(R.string.total_tax) + " (+) : " + currency + " " + NumberFormat.getInstance(Locale.getDefault()).format(get_tax));

        databaseAccess.open();
        double get_discount = databaseAccess.getTotalDiscount(type);
        TextView textView2 = this.textView_Discount;
        textView2.setText(getString(R.string.total_discount) + " (-) : " + currency + " " + NumberFormat.getInstance(Locale.getDefault()).format(get_discount));

        TextView textView3 = this.textView_NetSales;
        textView3.setText(getString(R.string.net_sales) + ": " + currency + " " + NumberFormat.getInstance(Locale.getDefault()).format((sub_total + get_tax) - get_discount));
    }

    public void folderChooser() {
        new ChooserDialog((Activity) this).displayPath(true).withFilter(true, false, new String[0])
                .withChosenListener(new ChooserDialog.Result() {

                    @Override
                    public void onChoosePath(String path, File pathFile) {
                        SalesReportActivity.this.onExport(path);
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
                .exportSingleTable("order_details", "order_details.xls", new SQLiteToExcel.ExportListener() {

                    @Override
                    public void onStart() {
                        SalesReportActivity.this.loading = new ProgressDialog(SalesReportActivity.this);
                        SalesReportActivity.this.loading.setMessage(SalesReportActivity.this.getString(R.string.data_exporting_please_wait));
                        SalesReportActivity.this.loading.setCancelable(false);
                        SalesReportActivity.this.loading.show();
                    }

                    @Override
                    public void onCompleted(String filePath) {
                        new Handler().postDelayed(new Runnable() {
                            public void run() {
                                SalesReportActivity.this.loading.dismiss();
                                Toasty.success(SalesReportActivity.this, (int) R.string.data_successfully_exported, Toasty.LENGTH_SHORT).show();
                            }
                        }, 5000);
                    }

                    @Override
                    public void onError(Exception e) {
                        SalesReportActivity.this.loading.dismiss();
                        Toasty.error(SalesReportActivity.this, (int) R.string.data_export_fail, Toasty.LENGTH_SHORT).show();
                    }
                });
    }
}