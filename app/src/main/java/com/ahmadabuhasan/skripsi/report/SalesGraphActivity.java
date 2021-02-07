package com.ahmadabuhasan.skripsi.report;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.TextView;

import com.ahmadabuhasan.skripsi.R;
import com.ahmadabuhasan.skripsi.database.DatabaseAccess;
import com.ahmadabuhasan.skripsi.database.DatabaseOpenHelper;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/*
 * Created by Ahmad Abu Hasan on 07/02/2021
 */

public class SalesGraphActivity extends AppCompatActivity {

    TextView textView_SelectYear;
    BarChart barChart;
    TextView textView_TotalSales;
    TextView textView_TotalTax;
    TextView textView_TotalDiscount;
    TextView textView_NetSales;
    int mYear = 2021;
    DecimalFormat decimalFormat;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_graph);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.monthly_sales_graph);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        this.textView_SelectYear = findViewById(R.id.tv_select_year);
        this.barChart = findViewById(R.id.barChart);
        this.textView_TotalSales = findViewById(R.id.tv_total_sales);
        this.textView_TotalTax = findViewById(R.id.tv_total_tax);
        this.textView_TotalDiscount = findViewById(R.id.tv_total_discount);
        this.textView_NetSales = findViewById(R.id.tv_net_sales);

        this.barChart.setDrawBarShadow(false);
        this.barChart.setDrawValueAboveBar(true);
        this.barChart.setMaxVisibleValueCount(50);
        this.barChart.setPinchZoom(false);
        this.barChart.setDrawGridBackground(true);

        decimalFormat = new DecimalFormat("#0.00");
        String currentYear = new SimpleDateFormat("yyyy", Locale.ENGLISH).format(new Date());
        TextView textView = this.textView_SelectYear;
        textView.setText(getString(R.string.year) + " " + currentYear);
        getGraphData();
    }

    @SuppressLint("SetTextI18n")
    public void getGraphData() {
        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(this);
        String[] monthNumber = {"00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"};
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            databaseAccess.open();
            String MonthNum = monthNumber[i];
            barEntries.add(new BarEntry((float) i, databaseAccess.getMonthlySalesAmount(MonthNum, "" + this.mYear)));
        }
        XAxis xAxis = this.barChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(new String[]{"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"}));
        xAxis.setCenterAxisLabels(true);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1.0f);
        xAxis.setGranularityEnabled(true);
        xAxis.setLabelCount(12);
        BarDataSet barDataSet = new BarDataSet(barEntries, getString(R.string.monthly_sales_report));
        barDataSet.setColors(ColorTemplate.LIBERTY_COLORS);
        BarData barData = new BarData(barDataSet);
        barData.setBarWidth(0.9f);
        this.barChart.setData(barData);
        this.barChart.setScaleEnabled(false);

        databaseAccess.open();
        String currency = databaseAccess.getCurrency();

        databaseAccess.open();
        double sub_total = databaseAccess.getTotalOrderPrice(DatabaseOpenHelper.YEARLY);
        TextView textView = this.textView_TotalSales;
        textView.setText(getString(R.string.total_sales) + " " + currency + " " + NumberFormat.getInstance(Locale.getDefault()).format(sub_total));

        databaseAccess.open();
        double get_tax = databaseAccess.getTotalTax(DatabaseOpenHelper.YEARLY);
        TextView textView1 = this.textView_TotalTax;
        textView1.setText(getString(R.string.total_tax) + " (+) : " + currency + " " + NumberFormat.getInstance(Locale.getDefault()).format(get_tax));

        databaseAccess.open();
        double get_discount = databaseAccess.getTotalDiscount(DatabaseOpenHelper.YEARLY);
        TextView textView2 = this.textView_TotalDiscount;
        textView2.setText(getString(R.string.total_discount) + " (-) : " + currency + " " + NumberFormat.getInstance(Locale.getDefault()).format(get_discount));

        TextView textView3 = this.textView_NetSales;
        textView3.setText(getString(R.string.net_sales) + ": " + currency + " " + NumberFormat.getInstance(Locale.getDefault()).format((sub_total + get_tax) - get_discount));
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() != android.R.id.home) {
            return super.onOptionsItemSelected(item);
        }
        finish();
        return true;
    }
}