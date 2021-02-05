package com.ahmadabuhasan.skripsi.settings.categories;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
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
 * Created by Ahmad Abu Hasan on 05/02/2021
 */

public class AddCategoryActivity extends AppCompatActivity {

    EditText editText_Category;
    TextView textView_Add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.add_category);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        this.editText_Category = findViewById(R.id.et_category_name);
        this.textView_Add = findViewById(R.id.tv_add_category);

        this.textView_Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String category_name = AddCategoryActivity.this.editText_Category.getText().toString().trim();
                if (category_name.isEmpty()) {
                    AddCategoryActivity.this.editText_Category.setError(AddCategoryActivity.this.getString(R.string.enter_category_name));
                    AddCategoryActivity.this.editText_Category.requestFocus();
                    return;
                }
                DatabaseAccess databaseAccess = DatabaseAccess.getInstance(AddCategoryActivity.this);
                databaseAccess.open();
                if (databaseAccess.addCategory(category_name)) {
                    Toasty.success(AddCategoryActivity.this, (int) R.string.category_added_successfully, Toasty.LENGTH_SHORT).show();
                    Intent intent = new Intent(AddCategoryActivity.this, CategoriesActivity.class);
                    //intent.addFlags(PagedChannelRandomAccessSource.DEFAULT_TOTAL_BUFSIZE);
                    AddCategoryActivity.this.startActivity(intent);
                    return;
                }
                Toasty.error(AddCategoryActivity.this, (int) R.string.failed, Toasty.LENGTH_SHORT).show();
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