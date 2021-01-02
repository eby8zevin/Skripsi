package com.ahmadabuhasan.skripsi.data;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.ahmadabuhasan.skripsi.R;
import com.ahmadabuhasan.skripsi.database.DatabaseAccess;
import com.ahmadabuhasan.skripsi.database.DatabaseOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/*
 * Created by Ahmad Abu Hasan on 02/01/2021
 */

public class AddProductActivity extends AppCompatActivity {

    @SuppressLint("StaticFieldLeak")
    public static EditText editText_Code;

    ArrayAdapter<String> categoryAdapter;
    List<String> categoryNames;
    String selectedCategoryID;
    ArrayAdapter<String> weightUnitAdapter;
    List<String> weightUnitNames;
    String selectedWeightUnitID;
    ArrayAdapter<String> supplierAdapter;
    List<String> supplierNames;
    String selectedSupplierID;

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

    ProgressDialog loading;

    TextView textView_Add_Product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

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
        this.textView_Add_Product = findViewById(R.id.tv_add_product);

        this.imageView_ScanCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddProductActivity.this.startActivity(new Intent(AddProductActivity.this, ScannerViewActivity.class));
            }
        });

        this.categoryNames = new ArrayList();
        this.weightUnitNames = new ArrayList();
        this.supplierNames = new ArrayList();

        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(this);
        databaseAccess.open();

        final List<HashMap<String, String>> productCategory = databaseAccess.getProductCategory();
        databaseAccess.open();
        for (int i = 0; i < productCategory.size(); i++) {
            this.categoryNames.add(productCategory.get(i).get(DatabaseOpenHelper.CATEGORY_NAME));
        }

        final List<HashMap<String, String>> weightUnit = databaseAccess.getWeightUnit();
        databaseAccess.open();
        for (int i1 = 0; i1 < weightUnit.size(); i1++) {
            this.weightUnitNames.add(weightUnit.get(i1).get(DatabaseOpenHelper.WEIGHT_UNIT));
        }

        final List<HashMap<String, String>> productSupplier = databaseAccess.getProductSupplier();
        databaseAccess.open();
        for (int i2 = 0; i2 < productSupplier.size(); i2++) {
            this.supplierNames.add(productSupplier.get(i2).get(DatabaseOpenHelper.SUPPLIER_NAME));
        }

        this.editText_Category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddProductActivity.this.categoryAdapter = new ArrayAdapter<>(AddProductActivity.this, android.R.layout.simple_list_item_1);
                AddProductActivity.this.categoryAdapter.addAll(AddProductActivity.this.categoryNames);
                AlertDialog.Builder dialog = new AlertDialog.Builder(AddProductActivity.this);
                View dialogView = AddProductActivity.this.getLayoutInflater().inflate(R.layout.dialog_list_search, (ViewGroup) null);
                dialog.setView(dialogView);
                dialog.setCancelable(false);
                ListView dialog_list = (ListView) dialogView.findViewById(R.id.dialog_list);
                ((TextView) dialogView.findViewById(R.id.dialog_title)).setText(R.string.product_category);
                dialog_list.setVerticalScrollBarEnabled(true);
                dialog_list.setAdapter((ListAdapter) AddProductActivity.this.categoryAdapter);
                ((EditText) dialogView.findViewById(R.id.dialog_input)).addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        AddProductActivity.this.categoryAdapter.getFilter().filter(s);
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
                final AlertDialog alertDialog = dialog.create();
                ((Button) dialogView.findViewById(R.id.dialog_button)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
                alertDialog.show();
                dialog_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        alertDialog.dismiss();
                        String selectedItem = AddProductActivity.this.categoryAdapter.getItem(position);
                        String category_id = "0";
                        AddProductActivity.this.editText_Category.setText(selectedItem);
                        for (int i = 0; i < AddProductActivity.this.categoryNames.size(); i++) {
                            if (AddProductActivity.this.categoryNames.get(i).equalsIgnoreCase(selectedItem)) {
                                category_id = (String) ((HashMap) productCategory.get(i)).get(DatabaseOpenHelper.CATEGORY_ID);
                            }
                        }
                        AddProductActivity.this.selectedCategoryID = category_id;
                        Log.d(DatabaseOpenHelper.CATEGORY_ID, category_id);
                    }
                });
            }
        });

    }
}