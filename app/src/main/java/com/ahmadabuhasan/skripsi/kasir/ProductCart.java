package com.ahmadabuhasan.skripsi.kasir;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ahmadabuhasan.skripsi.R;
import com.ahmadabuhasan.skripsi.adapter.CartAdapter;
import com.ahmadabuhasan.skripsi.database.DatabaseAccess;
import com.ahmadabuhasan.skripsi.database.DatabaseOpenHelper;
import com.github.mikephil.charting.utils.Utils;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;

/*
 * Created by Ahmad Abu Hasan on 19/01/2021
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

        this.recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        this.recyclerView.setHasFixedSize(true);

        this.textView_no_product.setVisibility(View.GONE);

        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(this);
        databaseAccess.open();
        List<HashMap<String, String>> cartProductList = databaseAccess.getCartProduct();
        if (cartProductList.isEmpty()) {
            this.imgNoProduct.setImageResource(R.drawable.empty_cart);
            this.imgNoProduct.setVisibility(View.VISIBLE);
            this.textView_no_product.setVisibility(View.VISIBLE);
            this.button_SubmitOrder.setVisibility(View.GONE);
            this.recyclerView.setVisibility(View.GONE);
            this.linearLayout.setVisibility(View.GONE);
            this.textView_total_price.setVisibility(View.GONE);
        } else {
            this.imgNoProduct.setVisibility(View.GONE);
            CartAdapter cartAdapter = new CartAdapter(this, cartProductList, this.textView_total_price, this.button_SubmitOrder, this.imgNoProduct, this.textView_no_product);
            this.productCartAdapter = cartAdapter;
            this.recyclerView.setAdapter(cartAdapter);
        }

        this.button_SubmitOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProductCart.this.dialog();
            }
        });
    }

    public void dialog() {
        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(this);
        databaseAccess.open();
        List<HashMap<String, String>> shopData = databaseAccess.getShopInformation();
        final String shop_currency = shopData.get(0).get(DatabaseOpenHelper.SHOP_CURRENCY);
        String tax = shopData.get(0).get(DatabaseOpenHelper.SHOP_TAX);
        double getTax = Double.parseDouble(tax);

        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_payment, (ViewGroup) null);
        dialog.setView(dialogView);
        dialog.setCancelable(false);

        ImageButton dialog_btn_close = (ImageButton) dialogView.findViewById(R.id.btn_close);
        final Button dialog_btn_submit = (Button) dialogView.findViewById(R.id.btn_submit);

        final TextView dialog_customer = (TextView) dialogView.findViewById(R.id.dialog_customer);
        ImageButton dialog_img_customer = (ImageButton) dialogView.findViewById(R.id.img_select_customer);
        final TextView dialog_order_type = (TextView) dialogView.findViewById(R.id.dialog_order_type);
        ImageButton dialog_img_order_type = (ImageButton) dialogView.findViewById(R.id.img_order_type);
        final TextView dialog_order_payment_method = (TextView) dialogView.findViewById(R.id.dialog_order_status);
        ImageButton dialog_img_order_payment_method = (ImageButton) dialogView.findViewById(R.id.img_order_payment_method);
        TextView dialog_text_sub_total = (TextView) dialogView.findViewById(R.id.dialog_text_sub_total);
        TextView dialog_text_total_tax = (TextView) dialogView.findViewById(R.id.dialog_text_total_tax);
        final EditText dialog_et_discount = (EditText) dialogView.findViewById(R.id.et_dialog_discount);
        final TextView dialog_txt_total_cost = (TextView) dialogView.findViewById(R.id.dialog_text_total_cost);

        ((TextView) dialogView.findViewById(R.id.dialog_level_tax)).setText(getString(R.string.total_tax) + "( " + tax + "%) : ");
        final double total_cost = CartAdapter.total_price.doubleValue();
        StringBuilder sb = new StringBuilder();
        sb.append(shop_currency);
        sb.append(this.decimalFormat.format(total_cost));
        dialog_text_sub_total.setText(sb.toString());
        final double calculated_tax = (total_cost * getTax) / 100.0d;
        dialog_text_total_tax.setText(shop_currency + this.decimalFormat.format(calculated_tax));
        double calculated_total_cost = (total_cost + calculated_tax) - Utils.DOUBLE_EPSILON;
        dialog_txt_total_cost.setText(shop_currency + this.decimalFormat.format(calculated_total_cost));
    }
}