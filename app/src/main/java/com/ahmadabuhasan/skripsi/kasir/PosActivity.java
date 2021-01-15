package com.ahmadabuhasan.skripsi.kasir;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.ahmadabuhasan.skripsi.R;

/*
 * Created by Ahmad Abu Hasan on 16/01/2021
 */

public class PosActivity extends AppCompatActivity {

    public static TextView textView_Count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pos);
    }
}