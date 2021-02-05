package com.ahmadabuhasan.skripsi.data;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.internal.view.SupportMenu;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
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
//import com.itextpdf.text.io.PagedChannelRandomAccessSource;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import es.dmoral.toasty.Toasty;

/*
 * Created by Ahmad Abu Hasan on 05/02/2021
 */

public class EditProductActivity extends AppCompatActivity {

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

    Calendar calendar;
    SimpleDateFormat simpleDateFormat;
    private String datetime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.product_details);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

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

        this.productID = getIntent().getExtras().getString(DatabaseOpenHelper.PRODUCT_ID);

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

        this.imageView_ScanCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditProductActivity.this.startActivity(new Intent(EditProductActivity.this, EditProductScannerViewActivity.class));
            }
        });

        this.categoryNames = new ArrayList();
        this.supplierNames = new ArrayList();
        this.weightUnitNames = new ArrayList();

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
        //this.editText_Category.setText(product_category_id);
        this.editText_Buy.setText(product_buy);
        this.editText_Stock.setText(product_stock);
        this.editText_Price.setText(product_price);
        this.editText_Total_Qty.setText(product_total_qty);
        this.editText_Disc_Qty.setText(product_disc_qty);
        this.editText_Weight.setText(product_weight);
        databaseAccess.open();
        this.editText_Weight_Unit.setText(databaseAccess.getWeightUnitName(product_weight_unit_id));
        //this.editText_Weight_Unit.setText(product_weight_unit_id);
        this.editText_Last_Update.setText(product_last_update);
        this.editText_Information.setText(product_information);
        databaseAccess.open();
        this.editText_Supplier.setText(databaseAccess.getSupplierName(product_supplier_id));
        //this.editText_Supplier.setText(product_supplier_id);

        this.selectedCategoryID = product_category_id;
        this.selectedWeightUnitID = product_weight_unit_id;
        this.selectedSupplierID = product_supplier_id;

        databaseAccess.open();
        final List<HashMap<String, String>> productCategory = databaseAccess.getProductCategory();
        int i = 0;
        while (i < productCategory.size()) {
            DatabaseAccess databaseAccess1 = databaseAccess;
            this.categoryNames.add(productCategory.get(i).get(DatabaseOpenHelper.CATEGORY_NAME));
            i++;
            databaseAccess = databaseAccess1;
        }

        databaseAccess.open();
        final List<HashMap<String, String>> weightUnit = databaseAccess.getWeightUnit();
        for (int i1 = 0; i1 < weightUnit.size(); i1++) {
            this.weightUnitNames.add(weightUnit.get(i1).get(DatabaseOpenHelper.WEIGHT_UNIT));
        }

        databaseAccess.open();
        final List<HashMap<String, String>> productSupplier = databaseAccess.getProductSupplier();
        for (int i2 = 0; i2 < productSupplier.size(); i2++) {
            this.supplierNames.add(productSupplier.get(i2).get(DatabaseOpenHelper.SUPPLIER_NAME));
        }

        this.editText_Category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditProductActivity.this.categoryAdapter = new ArrayAdapter<>(EditProductActivity.this, android.R.layout.simple_list_item_1);
                EditProductActivity.this.categoryAdapter.addAll(EditProductActivity.this.categoryNames);
                AlertDialog.Builder dialog = new AlertDialog.Builder(EditProductActivity.this);
                View dialogView = EditProductActivity.this.getLayoutInflater().inflate(R.layout.dialog_list_search, (ViewGroup) null);
                dialog.setView(dialogView);
                dialog.setCancelable(false);
                ListView dialog_list = (ListView) dialogView.findViewById(R.id.dialog_list);
                ((TextView) dialogView.findViewById(R.id.dialog_title)).setText(R.string.product_category);
                dialog_list.setVerticalScrollBarEnabled(true);
                dialog_list.setAdapter((ListAdapter) EditProductActivity.this.categoryAdapter);
                ((EditText) dialogView.findViewById(R.id.dialog_input)).addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        EditProductActivity.this.categoryAdapter.getFilter().filter(s);
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
                        String selectedItem = EditProductActivity.this.categoryAdapter.getItem(position);
                        String category_id = "0";
                        EditProductActivity.this.editText_Category.setText(selectedItem);
                        for (int i = 0; i < EditProductActivity.this.categoryNames.size(); i++) {
                            if (EditProductActivity.this.categoryNames.get(i).equalsIgnoreCase(selectedItem)) {
                                category_id = (String) ((HashMap) productCategory.get(i)).get(DatabaseOpenHelper.CATEGORY_ID);
                            }
                        }
                        EditProductActivity.this.selectedCategoryID = category_id;
                        Log.d(DatabaseOpenHelper.CATEGORY_ID, category_id);
                    }
                });
            }
        });

        this.editText_Weight_Unit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditProductActivity.this.weightUnitAdapter = new ArrayAdapter<>(EditProductActivity.this, android.R.layout.simple_list_item_1);
                EditProductActivity.this.weightUnitAdapter.addAll(EditProductActivity.this.weightUnitNames);
                AlertDialog.Builder dialog = new AlertDialog.Builder(EditProductActivity.this);
                View dialogView = EditProductActivity.this.getLayoutInflater().inflate(R.layout.dialog_list_search, (ViewGroup) null);
                dialog.setView(dialogView);
                dialog.setCancelable(false);
                ListView dialog_list = (ListView) dialogView.findViewById(R.id.dialog_list);
                ((TextView) dialogView.findViewById(R.id.dialog_title)).setText(R.string.product_weight_unit);
                dialog_list.setVerticalScrollBarEnabled(true);
                dialog_list.setAdapter((ListAdapter) EditProductActivity.this.weightUnitAdapter);
                ((EditText) dialogView.findViewById(R.id.dialog_input)).addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        EditProductActivity.this.weightUnitAdapter.getFilter().filter(s);

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
                        String selectedItem = EditProductActivity.this.weightUnitAdapter.getItem(position);
                        String weight_unit_id = "0";
                        EditProductActivity.this.editText_Weight_Unit.setText(selectedItem);
                        for (int i = 0; i < EditProductActivity.this.weightUnitNames.size(); i++) {
                            if (EditProductActivity.this.weightUnitNames.get(i).equalsIgnoreCase(selectedItem)) {
                                weight_unit_id = (String) ((HashMap) weightUnit.get(i)).get(DatabaseOpenHelper.WEIGHT_ID);
                            }
                        }
                        EditProductActivity.this.selectedWeightUnitID = weight_unit_id;
                        Log.d(DatabaseOpenHelper.WEIGHT_UNIT, EditProductActivity.this.selectedWeightUnitID);
                    }
                });
            }
        });

        this.editText_Supplier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditProductActivity.this.supplierAdapter = new ArrayAdapter<>(EditProductActivity.this, android.R.layout.simple_list_item_1);
                EditProductActivity.this.supplierAdapter.addAll(EditProductActivity.this.supplierNames);
                AlertDialog.Builder dialog = new AlertDialog.Builder(EditProductActivity.this);
                View dialogView = EditProductActivity.this.getLayoutInflater().inflate(R.layout.dialog_list_search, (ViewGroup) null);
                dialog.setView(dialogView);
                dialog.setCancelable(false);
                ListView dialog_list = (ListView) dialogView.findViewById(R.id.dialog_list);
                ((TextView) dialogView.findViewById(R.id.dialog_title)).setText(R.string.suppliers);
                dialog_list.setVerticalScrollBarEnabled(true);
                dialog_list.setAdapter((ListAdapter) EditProductActivity.this.supplierAdapter);
                ((EditText) dialogView.findViewById(R.id.dialog_input)).addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        EditProductActivity.this.supplierAdapter.getFilter().filter(s);
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
                        String selectedItem = EditProductActivity.this.supplierAdapter.getItem(position);
                        String supplier_id = "0";
                        EditProductActivity.this.editText_Supplier.setText(selectedItem);
                        for (int i = 0; i < EditProductActivity.this.supplierNames.size(); i++) {
                            if (EditProductActivity.this.supplierNames.get(i).equalsIgnoreCase(selectedItem)) {
                                supplier_id = (String) ((HashMap) productSupplier.get(i)).get(DatabaseOpenHelper.SUPPLIER_ID);
                            }
                        }
                        EditProductActivity.this.selectedSupplierID = supplier_id;
                    }
                });
            }
        });

        this.textView_Edit_Product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportActionBar().setTitle(R.string.edit_product);

                EditProductActivity.this.editText_Name.setEnabled(true);
                EditProductActivity.editText_Code.setEnabled(true);
                EditProductActivity.this.imageView_ScanCode.setEnabled(true);
                EditProductActivity.this.editText_Category.setEnabled(true);
                EditProductActivity.this.editText_Buy.setEnabled(true);
                EditProductActivity.this.editText_Stock.setEnabled(true);
                EditProductActivity.this.editText_Price.setEnabled(true);
                EditProductActivity.this.editText_Total_Qty.setEnabled(true);
                EditProductActivity.this.editText_Disc_Qty.setEnabled(true);
                EditProductActivity.this.editText_Weight.setEnabled(true);
                EditProductActivity.this.editText_Weight_Unit.setEnabled(true);
                EditProductActivity.this.editText_Last_Update.setEnabled(false);
                EditProductActivity.this.editText_Information.setEnabled(true);
                EditProductActivity.this.editText_Supplier.setEnabled(true);

                EditProductActivity.this.editText_Name.setTextColor(SupportMenu.CATEGORY_MASK);
                EditProductActivity.editText_Code.setTextColor(SupportMenu.CATEGORY_MASK);
                EditProductActivity.this.editText_Category.setTextColor(SupportMenu.CATEGORY_MASK);
                EditProductActivity.this.editText_Buy.setTextColor(SupportMenu.CATEGORY_MASK);
                EditProductActivity.this.editText_Stock.setTextColor(SupportMenu.CATEGORY_MASK);
                EditProductActivity.this.editText_Price.setTextColor(SupportMenu.CATEGORY_MASK);
                EditProductActivity.this.editText_Total_Qty.setTextColor(SupportMenu.CATEGORY_MASK);
                EditProductActivity.this.editText_Disc_Qty.setTextColor(SupportMenu.CATEGORY_MASK);
                EditProductActivity.this.editText_Weight.setTextColor(SupportMenu.CATEGORY_MASK);
                EditProductActivity.this.editText_Weight_Unit.setTextColor(SupportMenu.CATEGORY_MASK);
                EditProductActivity.this.editText_Last_Update.setTextColor(SupportMenu.CATEGORY_MASK);
                EditProductActivity.this.editText_Information.setTextColor(SupportMenu.CATEGORY_MASK);
                EditProductActivity.this.editText_Supplier.setTextColor(SupportMenu.CATEGORY_MASK);

                EditProductActivity.this.textView_Edit_Product.setVisibility(View.GONE);
                EditProductActivity.this.textView_Update_Product.setVisibility(View.VISIBLE);
            }
        });

        this.textView_Update_Product.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SimpleDateFormat")
            @Override
            public void onClick(View v) {
                Locale locale = new Locale("id", "ID");
                calendar = Calendar.getInstance();
                simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                //simpleDateFormat = new SimpleDateFormat("dd MMMM yyyy     HH:mm", locale);
                datetime = simpleDateFormat.format(calendar.getTime());

                String product_name = EditProductActivity.this.editText_Name.getText().toString();
                String product_code = EditProductActivity.editText_Code.getText().toString();
                String product_category_id = EditProductActivity.this.selectedCategoryID;
                String product_buy = EditProductActivity.this.editText_Buy.getText().toString();
                String product_stock = EditProductActivity.this.editText_Stock.getText().toString();
                String product_price = EditProductActivity.this.editText_Price.getText().toString();
                String product_total_qty = EditProductActivity.this.editText_Total_Qty.getText().toString();
                String product_disc_qty = EditProductActivity.this.editText_Disc_Qty.getText().toString();
                String product_weight = EditProductActivity.this.editText_Weight.getText().toString();
                String product_weight_unit_id = EditProductActivity.this.selectedWeightUnitID;
                //String product_last_update = EditProductActivity.this.editText_Last_Update.getText().toString();
                String product_last_update = EditProductActivity.this.datetime;
                String product_information = EditProductActivity.this.editText_Information.getText().toString();
                String product_supplier_id = EditProductActivity.this.selectedSupplierID;

                if (product_name.isEmpty()) {
                    EditProductActivity.this.editText_Name.setError(EditProductActivity.this.getString(R.string.product_name_cannot_be_empty));
                    EditProductActivity.this.editText_Name.requestFocus();
                } else if (product_category_id.isEmpty()) {
                    EditProductActivity.this.editText_Category.setError(EditProductActivity.this.getString(R.string.product_category_cannot_be_empty));
                    EditProductActivity.this.editText_Category.requestFocus();
                } else if (product_stock.isEmpty()) {
                    EditProductActivity.this.editText_Stock.setError(EditProductActivity.this.getString(R.string.product_stock_cannot_be_empty));
                    EditProductActivity.this.editText_Stock.requestFocus();
                } else if (product_price.isEmpty()) {
                    EditProductActivity.this.editText_Price.setError(EditProductActivity.this.getString(R.string.product_price_cannot_be_empty));
                    EditProductActivity.this.editText_Price.requestFocus();
                } else if (product_weight.isEmpty()) {
                    EditProductActivity.this.editText_Weight.setError(EditProductActivity.this.getString(R.string.product_weight_cannot_be_empty));
                    EditProductActivity.this.editText_Weight.requestFocus();
                } else if (product_supplier_id.isEmpty()) {
                    EditProductActivity.this.editText_Supplier.setError(EditProductActivity.this.getString(R.string.product_supplier_cannot_be_empty));
                    EditProductActivity.this.editText_Supplier.requestFocus();
                } else {
                    DatabaseAccess databaseAccess = DatabaseAccess.getInstance(EditProductActivity.this);
                    databaseAccess.open();
                    if (databaseAccess.updateProduct(product_name, product_code, product_category_id, product_buy, product_stock, product_price, product_total_qty, product_disc_qty, product_weight, product_weight_unit_id, product_last_update, product_information, product_supplier_id, EditProductActivity.this.productID)) {
                        Toasty.success(EditProductActivity.this, (int) R.string.update_successfully, Toasty.LENGTH_SHORT).show();
                        Intent intent = new Intent(EditProductActivity.this, ProductActivity.class);
                        //intent.addFlags(PagedChannelRandomAccessSource.DEFAULT_TOTAL_BUFSIZE);
                        EditProductActivity.this.startActivity(intent);
                        return;
                    }
                    Toasty.error(EditProductActivity.this, (int) R.string.failed, Toasty.LENGTH_SHORT).show();
                }
            }
        });
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}