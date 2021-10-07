package com.ahmadabuhasan.skripsi;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import es.dmoral.toasty.Toasty;

/*
 * Created by Ahmad Abu Hasan on 07/10/2021
 */

public class LoginActivity extends AppCompatActivity {

    EditText etUsername, etPassword;
    Button btnLogin;
    Spinner spinner;

    private static long backPressed;
    public static String item;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle("Login POS");

        etUsername = findViewById(R.id.username);
        etUsername.setText("Admin");
        etPassword = findViewById(R.id.password);
        etPassword.setText("123456");
        btnLogin = findViewById(R.id.login);
        spinner = findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.userType, R.layout.support_simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                item = spinner.getSelectedItem().toString();
                if (etUsername.getText().toString().equals("Admin") && etPassword.getText().toString().equals("123456") && item.equals("Admin")) {
                    Intent i = new Intent(LoginActivity.this, DashboardActivity.class);
                    startActivity(i);
                } else if (etUsername.getText().toString().equals("Kasir") && etPassword.getText().toString().equals("123456") && item.equals("Cashier")) {
                    Intent i = new Intent(LoginActivity.this, CashierDashboard.class);
                    startActivity(i);
                } else if (etUsername.getText().toString().equals("Gudang") && etPassword.getText().toString().equals("123456") && item.equals("Warehouse")) {
                    Intent i = new Intent(LoginActivity.this, WarehouseDashboard.class);
                    startActivity(i);
                } else {
                    Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void onBackPressed() {
        if (backPressed + 2000 > System.currentTimeMillis()) {
            finishAffinity();
        } else {
            Toasty.info((Context) this, (int) R.string.press_once_again_to_exit, Toasty.LENGTH_SHORT).show();
        }
        backPressed = System.currentTimeMillis();
    }
}
