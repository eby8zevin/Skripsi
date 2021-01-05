package com.ahmadabuhasan.skripsi.settings.weight_unit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.internal.view.SupportMenu;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.ahmadabuhasan.skripsi.R;
import com.ahmadabuhasan.skripsi.database.DatabaseAccess;
import com.ahmadabuhasan.skripsi.database.DatabaseOpenHelper;
//import com.itextpdf.text.io.PagedChannelRandomAccessSource;

import es.dmoral.toasty.Toasty;

/*
 * Created by Ahmad Abu Hasan on 05/01/2021
 */

public class EditWeightActivity extends AppCompatActivity {

    EditText editText_WeightUnit;
    TextView textView_Edit;
    TextView textView_Update;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_weight);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.edit_weight_unit);

        this.editText_WeightUnit = findViewById(R.id.et_weight_name);
        this.textView_Edit = findViewById(R.id.tv_edit_weight);
        this.textView_Update = findViewById(R.id.tv_update_weight);

        final String unit_id = getIntent().getExtras().getString(DatabaseOpenHelper.WEIGHT_ID);
        this.editText_WeightUnit.setText(getIntent().getExtras().getString(DatabaseOpenHelper.WEIGHT_UNIT));
        this.editText_WeightUnit.setEnabled(false);
        this.textView_Update.setVisibility(View.INVISIBLE);
        this.textView_Edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditWeightActivity.this.editText_WeightUnit.setEnabled(true);
                EditWeightActivity.this.textView_Update.setVisibility(View.VISIBLE);
                EditWeightActivity.this.editText_WeightUnit.setTextColor(SupportMenu.CATEGORY_MASK);
            }
        });

        this.textView_Update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String unit_name = EditWeightActivity.this.editText_WeightUnit.getText().toString().trim();
                if (unit_name.isEmpty()) {
                    EditWeightActivity.this.editText_WeightUnit.setError(EditWeightActivity.this.getString(R.string.enter_weight_unit_name));
                    EditWeightActivity.this.editText_WeightUnit.requestFocus();
                    return;
                }
                DatabaseAccess databaseAccess = DatabaseAccess.getInstance(EditWeightActivity.this);
                databaseAccess.open();
                if (databaseAccess.updateWeight(unit_id, unit_name)) {
                    Toasty.success(EditWeightActivity.this, (int) R.string.weight_unit_updated, Toasty.LENGTH_SHORT).show();
                    Intent intent = new Intent(EditWeightActivity.this, WeightActivity.class);
                    //intent.addFlags(PagedChannelRandomAccessSource.DEFAULT_TOTAL_BUFSIZE);
                    EditWeightActivity.this.startActivity(intent);
                    return;
                }
                Toasty.error(EditWeightActivity.this, (int) R.string.failed, Toasty.LENGTH_SHORT).show();
            }
        });
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(this, WeightActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}