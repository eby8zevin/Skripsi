package com.ahmadabuhasan.skripsi.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;

/*
 * Created by Ahmad Abu Hasan on 02/01/2021
 */

public class DatabaseAccess {

    private static DatabaseAccess instance;
    private SQLiteDatabase database;
    private SQLiteOpenHelper openHelper;

    private DatabaseAccess(Context context) {
        this.openHelper = new DatabaseOpenHelper(context);
    }

    public static DatabaseAccess getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseAccess(context);
        }
        return instance;
    }

    public void open() {
        this.database = this.openHelper.getWritableDatabase();
    }

    public void close() {
        SQLiteDatabase sQLiteDatabase = this.database;
        if (sQLiteDatabase != null) {
            sQLiteDatabase.close();
        }
    }

    // ProductActivity
    public ArrayList<HashMap<String, String>> getProducts() {
        ArrayList<HashMap<String, String>> product = new ArrayList<>();
        Cursor cursor = this.database.rawQuery("SELECT * FROM products ORDER BY product_id DESC", null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<>();
                map.put(DatabaseOpenHelper.PRODUCT_ID, cursor.getString(0));
                map.put(DatabaseOpenHelper.PRODUCT_NAME, cursor.getString(1));
                map.put(DatabaseOpenHelper.PRODUCT_CODE, cursor.getString(2));
                map.put(DatabaseOpenHelper.PRODUCT_CATEGORY, cursor.getString(3));
                map.put(DatabaseOpenHelper.PRODUCT_BUY, cursor.getString(4));
                map.put(DatabaseOpenHelper.PRODUCT_STOCK, cursor.getString(5));
                map.put(DatabaseOpenHelper.PRODUCT_PRICE, cursor.getString(6));
                map.put(DatabaseOpenHelper.PRODUCT_TOTAL_QTY, cursor.getString(7));
                map.put(DatabaseOpenHelper.PRODUCT_DISC_QTY, cursor.getString(8));
                map.put(DatabaseOpenHelper.PRODUCT_WEIGHT, cursor.getString(9));
                map.put(DatabaseOpenHelper.PRODUCT_WEIGHT_UNIT_ID, cursor.getString(10));
                map.put(DatabaseOpenHelper.PRODUCT_LAST_UPDATE, cursor.getString(11));
                map.put(DatabaseOpenHelper.PRODUCT_INFORMATION, cursor.getString(12));
                map.put(DatabaseOpenHelper.PRODUCT_SUPPLIER, cursor.getString(13));
                product.add(map);
            } while (cursor.moveToNext());
        }
        cursor.close();
        this.database.close();
        return product;
    }

    // ProductActivity
    public ArrayList<HashMap<String, String>> getSearchProducts(String string) {
        ArrayList<HashMap<String, String>> product = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = this.database;
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM products WHERE product_name LIKE '%" + string + "%' OR product_code LIKE '%" + string + "%' ORDER BY product_id DESC", null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<>();
                map.put(DatabaseOpenHelper.PRODUCT_ID, cursor.getString(0));
                map.put(DatabaseOpenHelper.PRODUCT_NAME, cursor.getString(1));
                map.put(DatabaseOpenHelper.PRODUCT_CODE, cursor.getString(2));
                map.put(DatabaseOpenHelper.PRODUCT_CATEGORY, cursor.getString(3));
                map.put(DatabaseOpenHelper.PRODUCT_BUY, cursor.getString(4));
                map.put(DatabaseOpenHelper.PRODUCT_STOCK, cursor.getString(5));
                map.put(DatabaseOpenHelper.PRODUCT_PRICE, cursor.getString(6));
                map.put(DatabaseOpenHelper.PRODUCT_TOTAL_QTY, cursor.getString(7));
                map.put(DatabaseOpenHelper.PRODUCT_DISC_QTY, cursor.getString(8));
                map.put(DatabaseOpenHelper.PRODUCT_WEIGHT, cursor.getString(9));
                map.put(DatabaseOpenHelper.PRODUCT_WEIGHT_UNIT_ID, cursor.getString(10));
                map.put(DatabaseOpenHelper.PRODUCT_LAST_UPDATE, cursor.getString(11));
                map.put(DatabaseOpenHelper.PRODUCT_INFORMATION, cursor.getString(12));
                map.put(DatabaseOpenHelper.PRODUCT_SUPPLIER, cursor.getString(13));
                product.add(map);
            } while (cursor.moveToNext());
        }
        cursor.close();
        this.database.close();
        return product;
    }

    // ProductAdapter
    public String getCurrency() {
        String currency = "n/a";
        Cursor cursor = this.database.rawQuery("SELECT * FROM shop", null);
        if (cursor.moveToFirst()) {
            do {
                currency = cursor.getString(6);
            } while (cursor.moveToNext());
        }
        cursor.close();
        this.database.close();
        return currency;
    }

    // ProductAdapter
    public boolean deleteProduct(String product_id) {
        long check = (long) this.database.delete("products", "product_id=?", new String[]{product_id});
        long delete = (long) this.database.delete("product_cart", "product_id=?", new String[]{product_id});
        this.database.close();
        return check == 1;
    }

    // AddProductActivity
    public ArrayList<HashMap<String, String>> getProductCategory() {
        ArrayList<HashMap<String, String>> product_category = new ArrayList<>();
        Cursor cursor = this.database.rawQuery("SELECT * FROM product_category ORDER BY category_id DESC", null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<>();
                map.put(DatabaseOpenHelper.CATEGORY_ID, cursor.getString(0));
                map.put(DatabaseOpenHelper.CATEGORY_NAME, cursor.getString(1));
                product_category.add(map);
            } while (cursor.moveToNext());
        }
        cursor.close();
        this.database.close();
        return product_category;
    }

    // AddProductActivity
    public ArrayList<HashMap<String, String>> getWeightUnit() {
        ArrayList<HashMap<String, String>> product_weight_unit = new ArrayList<>();
        Cursor cursor = this.database.rawQuery("SELECT * FROM product_weight", null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<>();
                map.put(DatabaseOpenHelper.WEIGHT_ID, cursor.getString(0));
                map.put(DatabaseOpenHelper.WEIGHT_UNIT, cursor.getString(1));
                product_weight_unit.add(map);
            } while (cursor.moveToNext());
        }
        cursor.close();
        this.database.close();
        return product_weight_unit;
    }

    // AddProductActivity
    public ArrayList<HashMap<String, String>> getProductSupplier() {
        ArrayList<HashMap<String, String>> product_suppliers = new ArrayList<>();
        Cursor cursor = this.database.rawQuery("SELECT * FROM suppliers", null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<>();
                map.put(DatabaseOpenHelper.SUPPLIER_ID, cursor.getString(0));
                map.put(DatabaseOpenHelper.SUPPLIER_NAME, cursor.getString(1));
                product_suppliers.add(map);
            } while (cursor.moveToNext());
        }
        cursor.close();
        this.database.close();
        return product_suppliers;
    }

    public boolean addProduct(String product_name, String product_code, String product_category, String product_buy, String product_stock, String product_price, String product_total_qty, String product_disc_qty, String product_weight, String weight_unit_id, String product_last_update, String product_information, String product_supplier) {
        ContentValues values = new ContentValues();
        values.put(DatabaseOpenHelper.PRODUCT_NAME, product_name);
        values.put(DatabaseOpenHelper.PRODUCT_CODE, product_code);
        values.put(DatabaseOpenHelper.PRODUCT_CATEGORY, product_category);
        values.put(DatabaseOpenHelper.PRODUCT_BUY, product_buy);
        values.put(DatabaseOpenHelper.PRODUCT_STOCK, product_stock);
        values.put(DatabaseOpenHelper.PRODUCT_PRICE, product_price);
        values.put(DatabaseOpenHelper.PRODUCT_TOTAL_QTY, product_total_qty);
        values.put(DatabaseOpenHelper.PRODUCT_DISC_QTY, product_disc_qty);
        values.put(DatabaseOpenHelper.PRODUCT_WEIGHT, product_weight);
        values.put(DatabaseOpenHelper.PRODUCT_WEIGHT_UNIT_ID, weight_unit_id);
        values.put(DatabaseOpenHelper.PRODUCT_LAST_UPDATE, product_last_update);
        values.put(DatabaseOpenHelper.PRODUCT_INFORMATION, product_information);
        values.put(DatabaseOpenHelper.PRODUCT_SUPPLIER, product_supplier);
        long check = this.database.insert("products", null, values);
        this.database.close();
        if (check == -1) {
            return false;
        }
        return true;
    }

}
