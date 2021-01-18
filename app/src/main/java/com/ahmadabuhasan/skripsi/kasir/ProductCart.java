package com.ahmadabuhasan.skripsi.kasir;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ahmadabuhasan.skripsi.R;
import com.ahmadabuhasan.skripsi.adapter.CartAdapter;

import java.text.DecimalFormat;
import java.util.List;

/*
 * Created by Ahmad Abu Hasan on 18/01/2021
 */

public class ProductCart extends AppCompatActivity {

    private RecyclerView recyclerView;
    CartAdapter productCartAdapter;
    LinearLayout linearLayout;

    ArrayAdapter<String> customerAdapter;
    List<String> customerNames;
    ArrayAdapter<String> orderTypeAdapter;
    List<String> orderTypeNames;
    ArrayAdapter<String> paymentMethodAdapter;
    List<String> paymentMethodNames;

    DecimalFormat decimalFormat;
    ImageView imgNoProduct;
    TextView textView_no_product;
    TextView textView_total_price;
    Button button_SubmitOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_cart);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.product_cart);

        this.decimalFormat = new DecimalFormat("#0.00");
        this.recyclerView = findViewById(R.id.cart_recyclerview);
        this.imgNoProduct = findViewById(R.id.image_no_product);
        this.textView_no_product = findViewById(R.id.tv_no_product);
        this.textView_total_price = findViewById(R.id.tv_total_price);
        this.button_SubmitOrder = findViewById(R.id.btn_submit_order);
    }
}