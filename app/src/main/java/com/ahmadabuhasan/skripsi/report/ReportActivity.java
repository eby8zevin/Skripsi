package com.ahmadabuhasan.skripsi.report;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.ahmadabuhasan.skripsi.R;

/*
 * Created by Ahmad Abu Hasan on 27/01/2021
 */

public class ReportActivity extends AppCompatActivity {

    CardView cardView_SalesReport;
    CardView cardView_ExpenseReport;
    CardView cardView_SalesGraph;
    CardView cardView_ExpenseGraph;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle((int) R.string.report);

        this.cardView_SalesReport = findViewById(R.id.card_sales_report);
        this.cardView_ExpenseReport = findViewById(R.id.card_expense_report);
        this.cardView_SalesGraph = findViewById(R.id.card_sales_graph);
        this.cardView_ExpenseGraph = findViewById(R.id.card_expense_graph);

        this.cardView_SalesReport.setOnClickListener(v -> ReportActivity.this.startActivity(new Intent(ReportActivity.this, SalesReportActivity.class)));
        this.cardView_ExpenseReport.setOnClickListener(v -> ReportActivity.this.startActivity(new Intent(ReportActivity.this, ExpenseReportActivity.class)));
        this.cardView_SalesGraph.setOnClickListener(v -> ReportActivity.this.startActivity(new Intent(ReportActivity.this, SalesGraphActivity.class)));
        this.cardView_ExpenseGraph.setOnClickListener(v -> ReportActivity.this.startActivity(new Intent(ReportActivity.this, ExpenseGraphActivity.class)));
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() != android.R.id.home) {
            return super.onOptionsItemSelected(item);
        }
        finish();
        return true;
    }
}