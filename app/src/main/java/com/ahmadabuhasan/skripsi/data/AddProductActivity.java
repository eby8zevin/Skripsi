package com.ahmadabuhasan.skripsi.data;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
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
import android.widget.Toast;

import com.ahmadabuhasan.skripsi.DashboardActivity;
import com.ahmadabuhasan.skripsi.R;
import com.ahmadabuhasan.skripsi.database.DatabaseAccess;
import com.ahmadabuhasan.skripsi.database.DatabaseOpenHelper;
import com.ajts.androidmads.library.ExcelToSQLite;
//import com.itextpdf.text.io.PagedChannelRandomAccessSource;
import com.obsez.android.lib.filechooser.ChooserDialog;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import es.dmoral.toasty.Toasty;

/*
 * Created by Ahmad Abu Hasan on 03/01/2021
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

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.add_product);

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
        this.editText_Last_Update.setEnabled(false);
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
        for (int i = 0; i < productCategory.size(); i++) {
            this.categoryNames.add(productCategory.get(i).get(DatabaseOpenHelper.CATEGORY_NAME));
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

        this.editText_Weight_Unit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddProductActivity.this.weightUnitAdapter = new ArrayAdapter<>(AddProductActivity.this, android.R.layout.simple_list_item_1);
                AddProductActivity.this.weightUnitAdapter.addAll(AddProductActivity.this.weightUnitNames);
                AlertDialog.Builder dialog = new AlertDialog.Builder(AddProductActivity.this);
                View dialogView = AddProductActivity.this.getLayoutInflater().inflate(R.layout.dialog_list_search, (ViewGroup) null);
                dialog.setView(dialogView);
                dialog.setCancelable(false);
                ListView dialog_list = (ListView) dialogView.findViewById(R.id.dialog_list);
                ((TextView) dialogView.findViewById(R.id.dialog_title)).setText(R.string.product_weight_unit);
                dialog_list.setVerticalScrollBarEnabled(true);
                dialog_list.setAdapter((ListAdapter) AddProductActivity.this.weightUnitAdapter);
                ((EditText) dialogView.findViewById(R.id.dialog_input)).addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        AddProductActivity.this.weightUnitAdapter.getFilter().filter(s);

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
                        String selectedItem = AddProductActivity.this.weightUnitAdapter.getItem(position);
                        String weight_unit_id = "0";
                        AddProductActivity.this.editText_Weight_Unit.setText(selectedItem);
                        for (int i = 0; i < AddProductActivity.this.weightUnitNames.size(); i++) {
                            if (AddProductActivity.this.weightUnitNames.get(i).equalsIgnoreCase(selectedItem)) {
                                weight_unit_id = (String) ((HashMap) weightUnit.get(i)).get(DatabaseOpenHelper.WEIGHT_ID);
                            }
                        }
                        AddProductActivity.this.selectedWeightUnitID = weight_unit_id;
                        Log.d(DatabaseOpenHelper.WEIGHT_UNIT, AddProductActivity.this.selectedWeightUnitID);

                    }
                });
            }
        });

        this.editText_Supplier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddProductActivity.this.supplierAdapter = new ArrayAdapter<>(AddProductActivity.this, android.R.layout.simple_list_item_1);
                AddProductActivity.this.supplierAdapter.addAll(AddProductActivity.this.supplierNames);
                AlertDialog.Builder dialog = new AlertDialog.Builder(AddProductActivity.this);
                View dialogView = AddProductActivity.this.getLayoutInflater().inflate(R.layout.dialog_list_search, (ViewGroup) null);
                dialog.setView(dialogView);
                dialog.setCancelable(false);
                ListView dialog_list = (ListView) dialogView.findViewById(R.id.dialog_list);
                ((TextView) dialogView.findViewById(R.id.dialog_title)).setText(R.string.suppliers);
                dialog_list.setVerticalScrollBarEnabled(true);
                dialog_list.setAdapter((ListAdapter) AddProductActivity.this.supplierAdapter);
                ((EditText) dialogView.findViewById(R.id.dialog_input)).addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        AddProductActivity.this.supplierAdapter.getFilter().filter(s);
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
                        String selectedItem = AddProductActivity.this.supplierAdapter.getItem(position);
                        String supplier_id = "0";
                        AddProductActivity.this.editText_Supplier.setText(selectedItem);
                        for (int i = 0; i < AddProductActivity.this.supplierNames.size(); i++) {
                            if (AddProductActivity.this.supplierNames.get(i).equalsIgnoreCase(selectedItem)) {
                                supplier_id = (String) ((HashMap) productSupplier.get(i)).get(DatabaseOpenHelper.SUPPLIER_ID);
                            }
                        }
                        AddProductActivity.this.selectedSupplierID = supplier_id;
                    }
                });
            }
        });

        this.textView_Add_Product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String product_name = AddProductActivity.this.editText_Name.getText().toString();
                String product_code = AddProductActivity.editText_Code.getText().toString();
                String product_category_name = AddProductActivity.this.editText_Category.getText().toString();
                String product_category_id = AddProductActivity.this.selectedCategoryID;
                String product_buy = AddProductActivity.this.editText_Buy.getText().toString();
                String product_stock = AddProductActivity.this.editText_Stock.getText().toString();
                String product_price = AddProductActivity.this.editText_Price.getText().toString();
                String product_total_qty = AddProductActivity.this.editText_Total_Qty.getText().toString();
                String product_disc_qty = AddProductActivity.this.editText_Disc_Qty.getText().toString();
                String product_weight = AddProductActivity.this.editText_Weight.getText().toString();
                String product_weight_unit_name = AddProductActivity.this.editText_Weight_Unit.getText().toString();
                String product_weight_unit_id = AddProductActivity.this.selectedWeightUnitID;
                String product_last_update = AddProductActivity.this.editText_Last_Update.getText().toString();
                String product_information = AddProductActivity.this.editText_Information.getText().toString();
                String product_supplier_name = AddProductActivity.this.editText_Supplier.getText().toString();
                String product_supplier_id = AddProductActivity.this.selectedSupplierID;

                if (product_name.isEmpty()) {
                    AddProductActivity.this.editText_Name.setError(AddProductActivity.this.getString(R.string.product_name_cannot_be_empty));
                    AddProductActivity.this.editText_Name.requestFocus();
                    return;
                }
                if (!product_category_name.isEmpty()) {
                    if (!product_category_id.isEmpty()) {
                        if (product_price.isEmpty()) {
                            AddProductActivity.this.editText_Price.setError(AddProductActivity.this.getString(R.string.product_sell_price_cannot_be_empty));
                            AddProductActivity.this.editText_Price.requestFocus();
                            return;
                        }
                        if (!product_weight_unit_name.isEmpty()) {
                            if (!product_weight.isEmpty()) {
                                if (product_stock.isEmpty()) {
                                    AddProductActivity.this.editText_Stock.setError(AddProductActivity.this.getString(R.string.product_stock_cannot_be_empty));
                                    AddProductActivity.this.editText_Stock.requestFocus();
                                    return;
                                }
                                if (!product_supplier_name.isEmpty()) {
                                    if (!product_supplier_id.isEmpty()) {
                                        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(AddProductActivity.this);
                                        databaseAccess.open();
                                        if (databaseAccess.addProduct(product_name, product_code, product_category_id, product_buy, product_stock, product_price, product_total_qty, product_disc_qty, product_weight, product_weight_unit_id, product_last_update, product_information, product_supplier_id)) {
                                            Toasty.success(AddProductActivity.this, (int) R.string.product_successfully_added, Toasty.LENGTH_SHORT).show();
                                            Intent intent = new Intent(AddProductActivity.this, ProductActivity.class);
                                            //intent.addFlags(PagedChannelRandomAccessSource.DEFAULT_TOTAL_BUFSIZE);
                                            AddProductActivity.this.startActivity(intent);
                                            return;
                                        }
                                        Toasty.error(AddProductActivity.this, (int) R.string.failed, Toasty.LENGTH_SHORT).show();
                                        return;
                                    }
                                }
                                AddProductActivity.this.editText_Category.setError(AddProductActivity.this.getString(R.string.product_category_cannot_be_empty));
                                AddProductActivity.this.editText_Category.requestFocus();
                                return;
                            }
                        }
                        AddProductActivity.this.editText_Weight.setError(AddProductActivity.this.getString(R.string.product_weight_cannot_be_empty));
                        AddProductActivity.this.editText_Weight.requestFocus();
                        return;
                    }
                }
                AddProductActivity.this.editText_Supplier.setError(AddProductActivity.this.getString(R.string.product_supplier_cannot_be_empty));
                AddProductActivity.this.editText_Supplier.requestFocus();
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_product_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            finish();
            return true;
        } else if (itemId != R.id.menu_import) {
            return super.onOptionsItemSelected(item);
        } else {
            fileChooser();
            return true;
        }
    }

    public void onImport(String path) {
        DatabaseAccess.getInstance(this).open();
        if (!new File(path).exists()) {
            Toast.makeText(this, (int) R.string.no_file_found, Toast.LENGTH_SHORT).show();
        } else {
            new ExcelToSQLite(getApplicationContext(), DatabaseOpenHelper.DATABASE_NAME, false).importFromFile(path, new ExcelToSQLite.ImportListener() {
                @Override
                public void onStart() {
                    AddProductActivity.this.loading = new ProgressDialog(AddProductActivity.this);
                    AddProductActivity.this.loading.setMessage(AddProductActivity.this.getString(R.string.data_importing_please_wait));
                    AddProductActivity.this.loading.setCancelable(false);
                    AddProductActivity.this.loading.show();
                }

                @Override
                public void onCompleted(String dbName) {
                    new Handler().postDelayed(new Runnable() {

                        public void run() {
                            AddProductActivity.this.loading.dismiss();
                            Toasty.success(AddProductActivity.this, (int) R.string.data_successfully_imported, Toasty.LENGTH_SHORT).show();
                            AddProductActivity.this.startActivity(new Intent(AddProductActivity.this, DashboardActivity.class));
                            AddProductActivity.this.finish();
                        }
                    }, 5000);
                }

                @Override
                public void onError(Exception e) {
                    AddProductActivity.this.loading.dismiss();
                    Log.d("Error : ", "" + e.getMessage());
                    Toasty.error(AddProductActivity.this, (int) R.string.data_import_fail, Toasty.LENGTH_SHORT).show();
                }
            });
        }
    }

    public void fileChooser() {
        new ChooserDialog((Activity) this).displayPath(true).withFilter(false, false, "xls").withChosenListener(new ChooserDialog.Result() {
            @Override
            public void onChoosePath(String path, File pathFile) {
                AddProductActivity.this.onImport(path);
            }
        }).withOnCancelListener(new DialogInterface.OnCancelListener() {
            public void onCancel(DialogInterface dialog) {
                Log.d("CANCEL", "CANCEL");
                dialog.cancel();
            }
        }).build().show();
    }
}