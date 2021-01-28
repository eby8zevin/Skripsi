package com.ahmadabuhasan.skripsi.settings.weight_unit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.ahmadabuhasan.skripsi.R;
import com.ahmadabuhasan.skripsi.database.DatabaseAccess;
//import com.itextpdf.text.io.PagedChannelRandomAccessSource;

import es.dmoral.toasty.Toasty;

/*
 * Created by Ahmad Abu Hasan on 28/01/2021
 */

public class AddWeightActivity extends AppCompatActivity {

    EditText editText_WeightUnit;
    TextView textView_Add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_weight);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.add_weight_unit);

        this.editText_WeightUnit = findViewById(R.id.et_weight_name);
        this.textView_Add = findViewById(R.id.tv_add_weight);

        this.textView_Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String unit_name = AddWeightActivity.this.editText_WeightUnit.getText().toString().trim();
                if (unit_name.isEmpty()) {
                    AddWeightActivity.this.editText_WeightUnit.setError(AddWeightActivity.this.getString(R.string.enter_weight_unit_name));
                    AddWeightActivity.this.editText_WeightUnit.requestFocus();
                    return;
                }
                DatabaseAccess databaseAccess = DatabaseAccess.getInstance(AddWeightActivity.this);
                databaseAccess.open();
                if (databaseAccess.addWeightUnit(unit_name)) {
                    Toasty.success(AddWeightActivity.this, (int) R.string.weight_unit_added_successfully, Toasty.LENGTH_SHORT).show();
                    Intent intent = new Intent(AddWeightActivity.this, WeightActivity.class);
                    //intent.addFlags(PagedChannelRandomAccessSource.DEFAULT_TOTAL_BUFSIZE);
                    AddWeightActivity.this.startActivity(intent);
                    return;
                }
                Toasty.error(AddWeightActivity.this, (int) R.string.failed, Toasty.LENGTH_SHORT).show();
            }
        });
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() != android.R.id.home) {
            return super.onOptionsItemSelected(item);
        }
        finish();
        return true;
    }
}