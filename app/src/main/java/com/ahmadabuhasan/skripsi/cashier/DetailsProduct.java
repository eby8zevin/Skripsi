package com.ahmadabuhasan.skripsi.cashier;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.ahmadabuhasan.skripsi.R;
import com.ahmadabuhasan.skripsi.database.DatabaseAccess;
import com.ahmadabuhasan.skripsi.database.DatabaseOpenHelper;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/*
 * Created by Ahmad Abu Hasan on 07/10/2021
 */

public class DetailsProduct extends AppCompatActivity {

    EditText editText_Code;

    String productID;

    EditText editText_Name;
    EditText editText_Category;
    EditText editText_Buy;
    EditText editText_Stock;
    EditText editText_Price;
    EditText editText_Total_Qty;
    EditText editText_Disc_Qty;
    EditText editText_Weight;
    EditText editText_Weight_Unit;
    EditText editText_Last_Update;
    EditText editText_Information;
    EditText editText_Supplier;

    ImageView imageView_ScanCode;
    TextView textView_Edit_Product;
    TextView textView_Update_Product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);

        Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.product_details);

        this.editText_Name = findViewById(R.id.et_product_name);
        editText_Code = findViewById(R.id.et_product_code);
        this.imageView_ScanCode = findViewById(R.id.img_scan_code);
        this.editText_Category = findViewById(R.id.et_product_category);
        this.editText_Buy = findViewById(R.id.et_buy);
        this.editText_Stock = findViewById(R.id.et_product_stock);
        this.editText_Price = findViewById(R.id.et_product_price);
        this.editText_Total_Qty = findViewById(R.id.et_product_total_qty);
        this.editText_Disc_Qty = findViewById(R.id.et_product_disc_qty);
        this.editText_Weight = findViewById(R.id.et_product_weight);
        this.editText_Weight_Unit = findViewById(R.id.et_product_weight_unit);
        this.editText_Last_Update = findViewById(R.id.et_product_last_update);
        this.editText_Information = findViewById(R.id.et_product_information);
        this.editText_Supplier = findViewById(R.id.et_product_supplier);
        this.textView_Edit_Product = findViewById(R.id.tv_edit_product);
        this.textView_Update_Product = findViewById(R.id.tv_update_product);

        this.textView_Edit_Product.setVisibility(View.GONE);
        this.textView_Update_Product.setVisibility(View.GONE);

        this.editText_Name.setEnabled(false);
        editText_Code.setEnabled(false);
        this.imageView_ScanCode.setEnabled(false);
        this.editText_Category.setEnabled(false);
        this.editText_Buy.setEnabled(false);
        this.editText_Stock.setEnabled(false);
        this.editText_Price.setEnabled(false);
        this.editText_Total_Qty.setEnabled(false);
        this.editText_Disc_Qty.setEnabled(false);
        this.editText_Weight.setEnabled(false);
        this.editText_Weight_Unit.setEnabled(false);
        this.editText_Last_Update.setEnabled(false);
        this.editText_Information.setEnabled(false);
        this.editText_Supplier.setEnabled(false);

        this.productID = getIntent().getExtras().getString(DatabaseOpenHelper.PRODUCT_ID);

        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(this);
        databaseAccess.open();
        List<HashMap<String, String>> productData = databaseAccess.getProductsInfo(this.productID);

        String product_name = productData.get(0).get(DatabaseOpenHelper.PRODUCT_NAME);
        String product_code = productData.get(0).get(DatabaseOpenHelper.PRODUCT_CODE);
        String product_category_id = productData.get(0).get(DatabaseOpenHelper.PRODUCT_CATEGORY);
        String product_buy = productData.get(0).get(DatabaseOpenHelper.PRODUCT_BUY);
        String product_stock = productData.get(0).get(DatabaseOpenHelper.PRODUCT_STOCK);
        String product_price = productData.get(0).get(DatabaseOpenHelper.PRODUCT_PRICE);
        String product_total_qty = productData.get(0).get(DatabaseOpenHelper.PRODUCT_TOTAL_QTY);
        String product_disc_qty = productData.get(0).get(DatabaseOpenHelper.PRODUCT_DISC_QTY);
        String product_weight = productData.get(0).get(DatabaseOpenHelper.PRODUCT_WEIGHT);
        String product_weight_unit_id = productData.get(0).get(DatabaseOpenHelper.PRODUCT_WEIGHT_UNIT_ID);
        String product_last_update = productData.get(0).get(DatabaseOpenHelper.PRODUCT_LAST_UPDATE);
        String product_information = productData.get(0).get(DatabaseOpenHelper.PRODUCT_INFORMATION);
        String product_supplier_id = productData.get(0).get(DatabaseOpenHelper.PRODUCT_SUPPLIER);

        this.editText_Name.setText(product_name);
        editText_Code.setText(product_code);
        databaseAccess.open();
        this.editText_Category.setText(databaseAccess.getCategoryName(product_category_id));
        this.editText_Buy.setText(product_buy);
        this.editText_Stock.setText(product_stock);
        this.editText_Price.setText(product_price);
        this.editText_Total_Qty.setText(product_total_qty);
        this.editText_Disc_Qty.setText(product_disc_qty);
        this.editText_Weight.setText(product_weight);
        databaseAccess.open();
        this.editText_Weight_Unit.setText(databaseAccess.getWeightUnitName(product_weight_unit_id));
        this.editText_Last_Update.setText(product_last_update);
        this.editText_Information.setText(product_information);
        databaseAccess.open();
        this.editText_Supplier.setText(databaseAccess.getSupplierName(product_supplier_id));
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, PosActivity.class));
        finish();
    }
}