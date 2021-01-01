package com.ahmadabuhasan.skripsi.data;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.ahmadabuhasan.skripsi.R;

/*
 * Created by Ahmad Abu Hasan on 02/01/2021
 */

public class AddProductActivity extends AppCompatActivity {

    @SuppressLint("StaticFieldLeak")
    public static EditText editText_Code;
    ImageView imageView_ScanCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        editText_Code = findViewById(R.id.et_product_code);
        this.imageView_ScanCode = findViewById(R.id.img_scan_code);
        this.imageView_ScanCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddProductActivity.this.startActivity(new Intent(AddProductActivity.this, ScannerViewActivity.class));
            }
        });
    }
}