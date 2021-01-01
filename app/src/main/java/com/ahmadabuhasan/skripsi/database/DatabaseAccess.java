package com.ahmadabuhasan.skripsi.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;

/*
 * Created by Ahmad Abu Hasan on 01/01/2021
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

}
