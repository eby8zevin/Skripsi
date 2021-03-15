package com.ahmadabuhasan.skripsi.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.github.mikephil.charting.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

/*
 * Created by Ahmad Abu Hasan on 15/03/2021
 */

public class DatabaseAccess {

    private static DatabaseAccess instance;
    private SQLiteDatabase database;
    private final SQLiteOpenHelper openHelper;

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

    /*KASIR*/
    // PosProductAdapter
    public int addToCart(String product_id, String weight, String weight_unit, String price, int qty, String stock) {
        SQLiteDatabase sqLiteDatabase = this.database;
        if (sqLiteDatabase.rawQuery("SELECT * FROM product_cart WHERE product_id='" + product_id + "'", null).getCount() >= 1) {
            return 2;
        }
        ContentValues values = new ContentValues();
        values.put(DatabaseOpenHelper.CART_PRODUCT_ID, product_id);
        values.put(DatabaseOpenHelper.CART_PRODUCT_WEIGHT, weight);
        values.put(DatabaseOpenHelper.CART_PRODUCT_WEIGHT_UNIT, weight_unit);
        values.put(DatabaseOpenHelper.CART_PRODUCT_PRICE, price);
        values.put(DatabaseOpenHelper.CART_PRODUCT_QTY, qty);
        values.put(DatabaseOpenHelper.CART_PRODUCT_STOCK, stock);
        long check = this.database.insert("product_cart", null, values);
        this.database.close();
        if (check == -1) {
            return -1;
        }
        return 1;
    }

    // PosProductAdapter + PosActivity
    public int getCartItemCount() {
        Cursor cursor = this.database.rawQuery("SELECT * FROM product_cart", null);
        int itemCount = cursor.getCount();
        cursor.close();
        this.database.close();
        return itemCount;
    }

    // ProductCategoryAdapter
    public ArrayList<HashMap<String, String>> getTabProducts(String category_id) {
        ArrayList<HashMap<String, String>> product = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = this.database;
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM products WHERE product_category = '" + category_id + "' ORDER BY product_id DESC", null);
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

    // CartAdapter
    public String getProductName(String product_id) {
        String product_name = "n/a";
        SQLiteDatabase sqLiteDatabase = this.database;
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM products WHERE product_id='" + product_id + "'", (String[]) null);
        if (cursor.moveToFirst()) {
            do {
                product_name = cursor.getString(1);
            } while (cursor.moveToNext());
        }
        cursor.close();
        this.database.close();
        return product_name;
    }

    // CartAdapter
    public double getTotalPrice() {
        double total_price = Utils.DOUBLE_EPSILON;
        Cursor cursor = this.database.rawQuery("SELECT * FROM product_cart", null);
        if (cursor.moveToFirst()) {
            do {
                double price = Double.parseDouble(cursor.getString(4));
                double parseInt = Integer.parseInt(cursor.getString(5));
                Double.isNaN(parseInt);
                total_price += parseInt * price;
            } while (cursor.moveToNext());
        } else {
            total_price = Utils.DOUBLE_EPSILON;
        }
        cursor.close();
        this.database.close();
        return total_price;
    }

    // CartAdapter
    public boolean deleteProductFromCart(String id) {
        long check = (long) this.database.delete("product_cart", "cart_id=?", new String[]{id});
        this.database.close();
        return check == 1;
    }

    // CartAdapter
    public void updateProductQty(String id, String qty) {
        ContentValues values = new ContentValues();
        values.put(DatabaseOpenHelper.CART_PRODUCT_QTY, qty);
        long update = (long) this.database.update("product_cart", values, "cart_id=?", new String[]{id});
    }
    
    public String getTotalQty(String product_id) {
        String total_qty = "n/a";
        SQLiteDatabase sqLiteDatabase = this.database;
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM products WHERE product_id='" + product_id + "'", null);
        if (cursor.moveToFirst()) {
            do {
                total_qty = cursor.getString(7);
            } while (cursor.moveToNext());
        }
        cursor.close();
        this.database.close();
        return total_qty;
    }

    public String getDiscQty(String product_id) {
        String disc_qty = "n/a";
        SQLiteDatabase sqLiteDatabase = this.database;
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM products WHERE product_id='" + product_id + "'", null);
        if (cursor.moveToFirst()) {
            do {
                disc_qty = cursor.getString(8);
            } while (cursor.moveToNext());
        }
        cursor.close();
        this.database.close();
        return disc_qty;
    }

    // ProductCart
    public ArrayList<HashMap<String, String>> getCartProduct() {
        ArrayList<HashMap<String, String>> product = new ArrayList<>();
        Cursor cursor = this.database.rawQuery("SELECT * FROM product_cart", null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<>();
                map.put(DatabaseOpenHelper.PRODUCT_CART_ID, cursor.getString(0));
                map.put(DatabaseOpenHelper.CART_PRODUCT_ID, cursor.getString(1));
                map.put(DatabaseOpenHelper.CART_PRODUCT_WEIGHT, cursor.getString(2));
                map.put(DatabaseOpenHelper.CART_PRODUCT_WEIGHT_UNIT, cursor.getString(3));
                map.put(DatabaseOpenHelper.CART_PRODUCT_PRICE, cursor.getString(4));
                map.put(DatabaseOpenHelper.CART_PRODUCT_QTY, cursor.getString(5));
                map.put(DatabaseOpenHelper.CART_PRODUCT_STOCK, cursor.getString(6));
                product.add(map);
            } while (cursor.moveToNext());
        }
        cursor.close();
        this.database.close();
        return product;
    }

    // ProductCart
    public ArrayList<HashMap<String, String>> getOrderType() {
        ArrayList<HashMap<String, String>> order_type = new ArrayList<>();
        Cursor cursor = this.database.rawQuery("SELECT * FROM order_type ORDER BY order_type_id DESC", null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<>();
                map.put(DatabaseOpenHelper.ORDER_TYPE_ID, cursor.getString(0));
                map.put(DatabaseOpenHelper.ORDER_TYPE_NAME, cursor.getString(1));
                order_type.add(map);
            } while (cursor.moveToNext());
        }
        cursor.close();
        this.database.close();
        return order_type;
    }

    // ProductCart
    public ArrayList<HashMap<String, String>> getPaymentMethod() {
        ArrayList<HashMap<String, String>> payment_method = new ArrayList<>();
        Cursor cursor = this.database.rawQuery("SELECT * FROM payment_method ORDER BY payment_method_id DESC", null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<>();
                map.put(DatabaseOpenHelper.PAYMENT_METHOD_ID, cursor.getString(0));
                map.put(DatabaseOpenHelper.PAYMENT_METHOD_NAME, cursor.getString(1));
                payment_method.add(map);
            } while (cursor.moveToNext());
        }
        cursor.close();
        this.database.close();
        return payment_method;
    }

    // ProductCart
    public void insertOrder(String order_id, JSONObject jsonObject) {
        JSONException jsonException;
        JSONException jsonException1;
        JSONArray jsonArray;

        String product_id;
        String pending = "Pending";
        int i;
        int updated_stock;
        //double updated_stock;

        ContentValues contentValues;
        ContentValues values = new ContentValues();
        ContentValues values1 = new ContentValues();
        ContentValues values2 = new ContentValues();

        try {
            String order_date = jsonObject.getString(DatabaseOpenHelper.ORDER_LIST_DATE);
            String order_time = jsonObject.getString(DatabaseOpenHelper.ORDER_LIST_TIME);
            String order_type = jsonObject.getString(DatabaseOpenHelper.ORDER_LIST_TYPE);
            String order_payment_method = jsonObject.getString(DatabaseOpenHelper.ORDER_LIST_PAYMENT_METHOD);
            String customer_name = jsonObject.getString(DatabaseOpenHelper.ORDER_LIST_CUSTOMER_NAME);
            String tax = jsonObject.getString(DatabaseOpenHelper.ORDER_LIST_TAX);
            String discount = jsonObject.getString(DatabaseOpenHelper.ORDER_LIST_DISCOUNT);

            contentValues = values;
            contentValues.put(DatabaseOpenHelper.ORDER_LIST_INVOICE_ID, order_id);
            contentValues.put(DatabaseOpenHelper.ORDER_LIST_DATE, order_date);
            contentValues.put(DatabaseOpenHelper.ORDER_LIST_TIME, order_time);
            contentValues.put(DatabaseOpenHelper.ORDER_LIST_TYPE, order_type);
            contentValues.put(DatabaseOpenHelper.ORDER_LIST_PAYMENT_METHOD, order_payment_method);
            contentValues.put(DatabaseOpenHelper.ORDER_LIST_CUSTOMER_NAME, customer_name);
            contentValues.put(DatabaseOpenHelper.ORDER_LIST_TAX, tax);
            contentValues.put(DatabaseOpenHelper.ORDER_LIST_DISCOUNT, discount);
            contentValues.put(DatabaseOpenHelper.ORDER_LIST_STATUS, pending);

            this.database.insert("order_list", null, contentValues);
            this.database.delete("product_cart", null, null);
        } catch (JSONException e) {
            jsonException1 = e;
            jsonException1.printStackTrace();
            jsonArray = new JSONArray();
            try {
                jsonArray = jsonObject.getJSONArray("lines");
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
            i = 0;
            while (i < jsonArray.length()) {
                Log.d("insertOrder: ", order_id);
            }
            this.database.close();
        }
        try {
            jsonArray = jsonObject.getJSONArray("lines");
            product_id = "";
            updated_stock = 0;
            i = 0;
            while (i < jsonArray.length()) {
                JSONObject jo = jsonArray.getJSONObject(i);
                String product_name = jo.getString(DatabaseOpenHelper.ORDER_DETAILS_PRODUCT_NAME);
                String product_weight = jo.getString(DatabaseOpenHelper.ORDER_DETAILS_PRODUCT_WEIGHT);
                String product_qty = jo.getString(DatabaseOpenHelper.ORDER_DETAILS_PRODUCT_QTY);
                String product_price = jo.getString(DatabaseOpenHelper.ORDER_DETAILS_PRODUCT_PRICE);
                String product_order_date = jo.getString(DatabaseOpenHelper.ORDER_DETAILS_ORDER_DATE);
                try {
                    product_id = jo.getString(DatabaseOpenHelper.PRODUCT_ID);
                    updated_stock = Integer.parseInt(jo.getString(DatabaseOpenHelper.CART_PRODUCT_STOCK)) - Integer.parseInt(product_qty);
                } catch (JSONException e2) {
                    jsonException = e2;
                    jsonException.printStackTrace();
                    this.database.close();
                }
                values1.put(DatabaseOpenHelper.ORDER_DETAILS_INVOICE_ID, order_id);
                values1.put(DatabaseOpenHelper.ORDER_DETAILS_PRODUCT_NAME, product_name);
                values1.put(DatabaseOpenHelper.ORDER_DETAILS_PRODUCT_WEIGHT, product_weight);
                values1.put(DatabaseOpenHelper.ORDER_DETAILS_PRODUCT_QTY, product_qty);
                values1.put(DatabaseOpenHelper.ORDER_DETAILS_PRODUCT_PRICE, product_price);
                values1.put(DatabaseOpenHelper.ORDER_DETAILS_ORDER_DATE, product_order_date);
                values1.put(DatabaseOpenHelper.ORDER_DETAILS_ORDER_STATUS, pending);

                values2.put(DatabaseOpenHelper.PRODUCT_STOCK, updated_stock);
                //values2.put(DatabaseOpenHelper.PRODUCT_STOCK, Double.valueOf(updated_stock));

                this.database.insert("order_details", null, values1);
                this.database.update("products", values2, "product_id=?", new String[]{product_id});
                i++;
            }
        } catch (JSONException e3) {
            jsonException = e3;
            jsonException.printStackTrace();
            this.database.close();
        }
        this.database.close();
    }
    /*/KASIR*/

    /*DATA*/
    // ProductActivity + PosActivity
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

    // ProductActivity + PosActivity
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

    // ProductAdapter + PosProductAdapter + CartAdapter + OrderDetailsAdapter + ExpenseAdapter
    // + SalesReportActivity + SalesReportAdapter + ExpenseReportActivity + SalesGraphActivity + ExpenseGraphActivity
    public String getCurrency() {
        String currency = "n/a";
        Cursor cursor = this.database.rawQuery("SELECT * FROM shop", null);
        if (cursor.moveToFirst()) {
            do {
                currency = cursor.getString(5);
            } while (cursor.moveToNext());
        }
        cursor.close();
        this.database.close();
        return currency;
    }

    // ProductAdapter
    public boolean deleteProduct(String product_id) {
        long check = (long) this.database.delete("products", "product_id=?", new String[]{product_id});
        //long delete = (long) this.database.delete("product_cart", "product_id=?", new String[]{product_id});
        this.database.close();
        return check == 1;
    }

    // AddProductActivity + CategoriesActivity + PosActivity
    public ArrayList<HashMap<String, String>> getProductCategory() {
        ArrayList<HashMap<String, String>> product_category = new ArrayList<>();
        Cursor cursor = this.database.rawQuery("SELECT * FROM product_category ORDER BY category_id DESC", (String[]) null);
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

    // AddProductActivity + WeightActivity
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

    // AddProductActivity
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

    // EditProductActivity
    public ArrayList<HashMap<String, String>> getProductsInfo(String product_id) {
        ArrayList<HashMap<String, String>> product = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = this.database;
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM products WHERE product_id='" + product_id + "'", null);
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

    // EditProductActivity
    public String getCategoryName(String category_id) {
        String product_category = "n/a";
        SQLiteDatabase sqLiteDatabase = this.database;
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM product_category WHERE category_id=" + category_id + "", null);
        if (cursor.moveToFirst()) {
            do {
                product_category = cursor.getString(1);
            } while (cursor.moveToNext());
        }
        cursor.close();
        this.database.close();
        return product_category;
    }

    // EditProductActivity
    public String getWeightUnitName(String weight_unit_id) {
        String weight_unit_name = "n/a";
        SQLiteDatabase sqLiteDatabase = this.database;
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM product_weight WHERE weight_id=" + weight_unit_id + "", null);
        if (cursor.moveToFirst()) {
            do {
                weight_unit_name = cursor.getString(1);
            } while (cursor.moveToNext());
        }
        cursor.close();
        this.database.close();
        return weight_unit_name;
    }

    // EditProductActivity
    public String getSupplierName(String suppliers_id) {
        String supplier_name = "n/a";
        SQLiteDatabase sqLiteDatabase = this.database;
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM suppliers WHERE supplier_id=" + suppliers_id + "", null);
        if (cursor.moveToFirst()) {
            do {
                supplier_name = cursor.getString(1);
            } while (cursor.moveToNext());
        }
        cursor.close();
        this.database.close();
        return supplier_name;
    }

    // EditProductActivity
    public boolean updateProduct(String product_name, String product_code, String product_category, String product_buy, String product_stock, String product_price, String product_total_qty, String product_disc_qty, String product_weight, String weight_unit_id, String product_last_update, String product_information, String product_supplier, String product_id) {
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
        SQLiteDatabase sQLiteDatabase = this.database;
        String[] strArr = {product_id};
        this.database.isOpen();
        if (((long) sQLiteDatabase.update("products", values, "product_id=?", strArr)) == -1) {
            return false;
        }
        return true;
    }
    /*/DATA*/

    /*PRINT*/
    // OrderAdapter
    public boolean updateOrder(String invoiceId, String orderStatus) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseOpenHelper.ORDER_DETAILS_ORDER_STATUS, orderStatus);
        contentValues.put(DatabaseOpenHelper.ORDER_LIST_STATUS, orderStatus);
        this.database.update("order_details", contentValues, "invoice_id=?", new String[]{invoiceId});
        long l = this.database.update("order_list", contentValues, "invoice_id=?", new String[]{invoiceId});
        this.database.close();
        return (l == 1L);
    }

    // OrdersActivity
    public ArrayList<HashMap<String, String>> getOrderList() {
        ArrayList<HashMap<String, String>> orderList = new ArrayList<>();
        Cursor cursor = this.database.rawQuery("SELECT * FROM order_list ORDER BY order_id DESC", null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<>();
                map.put(DatabaseOpenHelper.ORDER_LIST_INVOICE_ID, cursor.getString(1));
                map.put(DatabaseOpenHelper.ORDER_LIST_DATE, cursor.getString(2));
                map.put(DatabaseOpenHelper.ORDER_LIST_TIME, cursor.getString(3));
                map.put(DatabaseOpenHelper.ORDER_LIST_TYPE, cursor.getString(4));
                map.put(DatabaseOpenHelper.ORDER_LIST_PAYMENT_METHOD, cursor.getString(5));
                map.put(DatabaseOpenHelper.ORDER_LIST_CUSTOMER_NAME, cursor.getString(6));
                map.put(DatabaseOpenHelper.ORDER_LIST_TAX, cursor.getString(7));
                map.put(DatabaseOpenHelper.ORDER_LIST_DISCOUNT, cursor.getString(8));
                map.put(DatabaseOpenHelper.ORDER_LIST_STATUS, cursor.getString(cursor.getColumnIndex(DatabaseOpenHelper.ORDER_LIST_STATUS)));
                orderList.add(map);
            } while (cursor.moveToNext());
        }
        cursor.close();
        this.database.close();
        return orderList;
    }

    // OrdersActivity
    public ArrayList<HashMap<String, String>> searchOrderList(String s) {
        ArrayList<HashMap<String, String>> orderList = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = this.database;
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM order_list WHERE customer_name LIKE '%" + s + "%' OR invoice_id LIKE '%" + s + "%' OR order_date LIKE '%" + s + "%'  OR order_status LIKE '%" + s + "%' ORDER BY order_id DESC", null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<>();
                map.put(DatabaseOpenHelper.ORDER_LIST_INVOICE_ID, cursor.getString(1));
                map.put(DatabaseOpenHelper.ORDER_LIST_DATE, cursor.getString(2));
                map.put(DatabaseOpenHelper.ORDER_LIST_TIME, cursor.getString(3));
                map.put(DatabaseOpenHelper.ORDER_LIST_TYPE, cursor.getString(4));
                map.put(DatabaseOpenHelper.ORDER_LIST_PAYMENT_METHOD, cursor.getString(5));
                map.put(DatabaseOpenHelper.ORDER_LIST_CUSTOMER_NAME, cursor.getString(6));
                map.put(DatabaseOpenHelper.ORDER_LIST_TAX, cursor.getString(7));
                map.put(DatabaseOpenHelper.ORDER_LIST_DISCOUNT, cursor.getString(8));
                map.put(DatabaseOpenHelper.ORDER_LIST_STATUS, cursor.getString(cursor.getColumnIndex(DatabaseOpenHelper.ORDER_LIST_STATUS)));
                orderList.add(map);
            } while (cursor.moveToNext());
        }
        cursor.close();
        this.database.close();
        return orderList;
    }

    // OrderDetailsActivity
    public ArrayList<HashMap<String, String>> getOrderDetailsList(String order_id) {
        ArrayList<HashMap<String, String>> orderDetailsList = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = this.database;
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM order_details WHERE invoice_id='" + order_id + "' ORDER BY order_details_id DESC", null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<>();
                map.put(DatabaseOpenHelper.ORDER_DETAILS_PRODUCT_NAME, cursor.getString(2));
                map.put(DatabaseOpenHelper.ORDER_DETAILS_PRODUCT_WEIGHT, cursor.getString(3));
                map.put(DatabaseOpenHelper.ORDER_DETAILS_PRODUCT_QTY, cursor.getString(4));
                map.put(DatabaseOpenHelper.ORDER_DETAILS_PRODUCT_PRICE, cursor.getString(5));
                orderDetailsList.add(map);
            } while (cursor.moveToNext());
        }
        cursor.close();
        this.database.close();
        return orderDetailsList;
    }

    // OrderDetailsActivity
    public double totalOrderPrice(String invoice_id) {
        double total_price = Utils.DOUBLE_EPSILON;
        SQLiteDatabase sqLiteDatabase = this.database;
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM order_details WHERE invoice_id='" + invoice_id + "'", null);
        if (cursor.moveToFirst()) {
            do {
                double parseInt = (double) Integer.parseInt(cursor.getString(4));
                double price = Double.parseDouble(cursor.getString(5));
                Double.isNaN(parseInt);
                total_price += parseInt * price;
            } while (cursor.moveToNext());
        } else {
            total_price = Utils.DOUBLE_EPSILON;
        }
        cursor.close();
        this.database.close();
        return total_price;
    }
    /*/PRINT*/

    /*SHOP*/
    // ShopInformationActivity
    public ArrayList<HashMap<String, String>> getShopInformation() {
        ArrayList<HashMap<String, String>> shop_info = new ArrayList<>();
        Cursor cursor = this.database.rawQuery("SELECT * FROM shop", null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<>();
                map.put(DatabaseOpenHelper.SHOP_NAME, cursor.getString(1));
                map.put(DatabaseOpenHelper.SHOP_CONTACT, cursor.getString(2));
                map.put(DatabaseOpenHelper.SHOP_EMAIL, cursor.getString(3));
                map.put(DatabaseOpenHelper.SHOP_ADDRESS, cursor.getString(4));
                map.put(DatabaseOpenHelper.SHOP_CURRENCY, cursor.getString(5));
                map.put(DatabaseOpenHelper.SHOP_TAX, cursor.getString(6));
                shop_info.add(map);
            } while (cursor.moveToNext());
        }
        cursor.close();
        this.database.close();
        return shop_info;
    }

    // ShopInformationActivity
    public boolean updateShopInformation(String shop_name, String shop_contact, String shop_email, String shop_address, String shop_currency, String shop_tax) {
        ContentValues values = new ContentValues();
        values.put(DatabaseOpenHelper.SHOP_NAME, shop_name);
        values.put(DatabaseOpenHelper.SHOP_CONTACT, shop_contact);
        values.put(DatabaseOpenHelper.SHOP_EMAIL, shop_email);
        values.put(DatabaseOpenHelper.SHOP_ADDRESS, shop_address);
        values.put(DatabaseOpenHelper.SHOP_CURRENCY, shop_currency);
        values.put(DatabaseOpenHelper.SHOP_TAX, shop_tax);
        long check = (long) this.database.update("shop", values, "shop_id=? ", new String[]{"1"});
        this.database.close();
        return check != -1;
    }
    /*/SHOP*/

    /*CATEGORIES*/
    // CategoriesActivity
    public ArrayList<HashMap<String, String>> searchProductCategory(String s) {
        ArrayList<HashMap<String, String>> product_category = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = this.database;
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM product_category WHERE category_name LIKE '%" + s + "%' ORDER BY category_id DESC ", null);
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

    // CategoryAdapter
    public boolean deleteCategory(String category_id) {
        long check = (long) this.database.delete(DatabaseOpenHelper.TABLE_CATEGORY, "category_id=?", new String[]{category_id});
        this.database.close();
        return check == 1;
    }

    // EditCategoryActivity
    public boolean updateCategory(String category_id, String category_name) {
        ContentValues values = new ContentValues();
        values.put(DatabaseOpenHelper.CATEGORY_NAME, category_name);
        long check = (long) this.database.update(DatabaseOpenHelper.TABLE_CATEGORY, values, "category_id=? ", new String[]{category_id});
        this.database.close();
        return check != -1;
    }

    // AddCategoryActivity
    public boolean addCategory(String category_name) {
        ContentValues values = new ContentValues();
        values.put(DatabaseOpenHelper.CATEGORY_NAME, category_name);
        long check = this.database.insert(DatabaseOpenHelper.TABLE_CATEGORY, null, values);
        this.database.close();
        if (check == -1) {
            return false;
        }
        return true;
    }
    /*/CATEGORIES*/

    /*WEIGHT*/
    // WeightActivity
    public ArrayList<HashMap<String, String>> searchProductWeight(String s) {
        ArrayList<HashMap<String, String>> product_weight_unit = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = this.database;
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM product_weight WHERE weight_unit LIKE '%" + s + "%' ORDER BY weight_id DESC ", null);
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

    // WeightAdapter
    public boolean deleteWeight(String weight_id) {
        long check = (long) this.database.delete(DatabaseOpenHelper.TABLE_WEIGHT, "weight_id=?", new String[]{weight_id});
        this.database.close();
        return check == 1;
    }

    // AddWeightActivity
    public boolean addWeightUnit(String unit_name) {
        ContentValues values = new ContentValues();
        values.put(DatabaseOpenHelper.WEIGHT_UNIT, unit_name);
        long check = this.database.insert(DatabaseOpenHelper.TABLE_WEIGHT, null, values);
        this.database.close();
        if (check == -1) {
            return false;
        }
        return true;
    }

    // EditWeightActivity
    public boolean updateWeight(String unit_id, String unit_name) {
        ContentValues values = new ContentValues();
        values.put(DatabaseOpenHelper.WEIGHT_UNIT, unit_name);
        long check = (long) this.database.update(DatabaseOpenHelper.TABLE_WEIGHT, values, "weight_id=? ", new String[]{unit_id});
        this.database.close();
        return check != -1;
    }
    /*/WEIGHT*/

    /*PAYMENT_METHOD*/
    // PaymentMethodAdapter
    public boolean deletePaymentMethod(String payment_method_id) {
        long check = (long) this.database.delete("payment_method", "payment_method_id=?", new String[]{payment_method_id});
        this.database.close();
        return check == 1;
    }

    // PaymentMethodActivity
    public ArrayList<HashMap<String, String>> searchPaymentMethod(String s) {
        ArrayList<HashMap<String, String>> payment_method = new ArrayList<>();
        SQLiteDatabase sQLiteDatabase = this.database;
        Cursor cursor = sQLiteDatabase.rawQuery("SELECT * FROM payment_method WHERE payment_method_name LIKE '%" + s + "%' ORDER BY payment_method_id DESC ", null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<>();
                map.put(DatabaseOpenHelper.PAYMENT_METHOD_ID, cursor.getString(0));
                map.put(DatabaseOpenHelper.PAYMENT_METHOD_NAME, cursor.getString(1));
                payment_method.add(map);
            } while (cursor.moveToNext());
        }
        cursor.close();
        this.database.close();
        return payment_method;
    }

    // AddPaymentMethodActivity
    public boolean addPaymentMethod(String payment_method_name) {
        ContentValues values = new ContentValues();
        values.put(DatabaseOpenHelper.PAYMENT_METHOD_NAME, payment_method_name);
        long check = this.database.insert("payment_method", null, values);
        this.database.close();
        if (check == -1) {
            return false;
        }
        return true;
    }

    // EditPaymentMethodActivity
    public boolean updatePaymentMethod(String payment_method_id, String payment_method_name) {
        ContentValues values = new ContentValues();
        values.put(DatabaseOpenHelper.PAYMENT_METHOD_NAME, payment_method_name);
        long check = (long) this.database.update("payment_method", values, "payment_method_id=? ", new String[]{payment_method_id});
        this.database.close();
        return check != -1;
    }
    /*/PAYMENT_METHOD*/

    /*EXPENSE*/
    // ExpenseAdapter
    public boolean deleteExpense(String expense_id) {
        long check = (long) this.database.delete("expense", "expense_id=?", new String[]{expense_id});
        this.database.close();
        return check == 1;
    }

    // ExpenseActivity + ExpenseReportActivity
    public ArrayList<HashMap<String, String>> getAllExpense() {
        ArrayList<HashMap<String, String>> expense = new ArrayList<>();
        Cursor cursor = this.database.rawQuery("SELECT * FROM expense ORDER BY expense_id DESC", null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<>();
                map.put(DatabaseOpenHelper.EXPENSE_ID, cursor.getString(cursor.getColumnIndex(DatabaseOpenHelper.EXPENSE_ID)));
                map.put(DatabaseOpenHelper.EXPENSE_NAME, cursor.getString(cursor.getColumnIndex(DatabaseOpenHelper.EXPENSE_NAME)));
                map.put(DatabaseOpenHelper.EXPENSE_NOTE, cursor.getString(cursor.getColumnIndex(DatabaseOpenHelper.EXPENSE_NOTE)));
                map.put(DatabaseOpenHelper.EXPENSE_AMOUNT, cursor.getString(cursor.getColumnIndex(DatabaseOpenHelper.EXPENSE_AMOUNT)));
                map.put(DatabaseOpenHelper.EXPENSE_DATE, cursor.getString(cursor.getColumnIndex(DatabaseOpenHelper.EXPENSE_DATE)));
                map.put(DatabaseOpenHelper.EXPENSE_TIME, cursor.getString(cursor.getColumnIndex(DatabaseOpenHelper.EXPENSE_TIME)));
                expense.add(map);
            } while (cursor.moveToNext());
        }
        cursor.close();
        this.database.close();
        return expense;
    }

    // ExpenseActivity
    public ArrayList<HashMap<String, String>> searchExpense(String s) {
        ArrayList<HashMap<String, String>> expense = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = this.database;
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM expense WHERE expense_name LIKE '%" + s + "%' OR expense_note LIKE '%" + s + "%' ORDER BY expense_id DESC", null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<>();
                map.put(DatabaseOpenHelper.EXPENSE_ID, cursor.getString(cursor.getColumnIndex(DatabaseOpenHelper.EXPENSE_ID)));
                map.put(DatabaseOpenHelper.EXPENSE_NAME, cursor.getString(cursor.getColumnIndex(DatabaseOpenHelper.EXPENSE_NAME)));
                map.put(DatabaseOpenHelper.EXPENSE_NOTE, cursor.getString(cursor.getColumnIndex(DatabaseOpenHelper.EXPENSE_NOTE)));
                map.put(DatabaseOpenHelper.EXPENSE_AMOUNT, cursor.getString(cursor.getColumnIndex(DatabaseOpenHelper.EXPENSE_AMOUNT)));
                map.put(DatabaseOpenHelper.EXPENSE_DATE, cursor.getString(cursor.getColumnIndex(DatabaseOpenHelper.EXPENSE_DATE)));
                map.put(DatabaseOpenHelper.EXPENSE_TIME, cursor.getString(cursor.getColumnIndex(DatabaseOpenHelper.EXPENSE_TIME)));
                expense.add(map);
            } while (cursor.moveToNext());
        }
        cursor.close();
        this.database.close();
        return expense;
    }

    // AddExpenseActivity
    public boolean addExpense(String expense_name, String expense_amount, String expense_note, String date, String time) {
        ContentValues values = new ContentValues();
        values.put(DatabaseOpenHelper.EXPENSE_NAME, expense_name);
        values.put(DatabaseOpenHelper.EXPENSE_AMOUNT, expense_amount);
        values.put(DatabaseOpenHelper.EXPENSE_NOTE, expense_note);
        values.put(DatabaseOpenHelper.EXPENSE_DATE, date);
        values.put(DatabaseOpenHelper.EXPENSE_TIME, time);
        long check = this.database.insert("expense", null, values);
        this.database.close();
        if (check == -1) {
            return false;
        }
        return true;
    }

    // EditExpenseActivity
    public boolean updateExpense(String expense_id, String expense_name, String expense_amount, String expense_note, String date, String time) {
        ContentValues values = new ContentValues();
        values.put(DatabaseOpenHelper.EXPENSE_NAME, expense_name);
        values.put(DatabaseOpenHelper.EXPENSE_AMOUNT, expense_amount);
        values.put(DatabaseOpenHelper.EXPENSE_NOTE, expense_note);
        values.put(DatabaseOpenHelper.EXPENSE_DATE, date);
        values.put(DatabaseOpenHelper.EXPENSE_TIME, time);
        long check = (long) this.database.update("expense", values, "expense_id=?", new String[]{expense_id});
        this.database.close();
        return check != -1;
    }
    /*/EXPENSE*/

    /*REPORT*/
    // SalesReportActivity
    public ArrayList<HashMap<String, String>> getAllSalesItems() {
        ArrayList<HashMap<String, String>> orderDetailsList = new ArrayList<>();
        Cursor cursor = this.database.rawQuery("SELECT * FROM order_details WHERE order_status='Completed' ORDER BY order_details_id DESC", null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<>();
                map.put(DatabaseOpenHelper.ORDER_DETAILS_PRODUCT_NAME, cursor.getString(2));
                map.put(DatabaseOpenHelper.ORDER_DETAILS_PRODUCT_WEIGHT, cursor.getString(3));
                map.put(DatabaseOpenHelper.ORDER_DETAILS_PRODUCT_QTY, cursor.getString(4));
                map.put(DatabaseOpenHelper.ORDER_DETAILS_PRODUCT_PRICE, cursor.getString(5));
                map.put(DatabaseOpenHelper.ORDER_DETAILS_ORDER_DATE, cursor.getString(6));
                orderDetailsList.add(map);
            } while (cursor.moveToNext());
        }
        cursor.close();
        this.database.close();
        return orderDetailsList;
    }

    // SalesReportActivity + SalesGraphActivity
    public double getTotalOrderPrice(String type) {
        Cursor cursor;
        double total_price = Utils.DOUBLE_EPSILON;
        if (type.equals(DatabaseOpenHelper.DAILY)) {
            String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(new Date());
            //String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH).format(new Date());
            SQLiteDatabase sqLiteDatabase = this.database;
            cursor = sqLiteDatabase.rawQuery("SELECT * FROM order_details WHERE order_status='Completed' AND product_order_date='" + currentDate + "' ORDER BY order_details_id DESC", null);
        } else if (type.equals(DatabaseOpenHelper.MONTHLY)) {
            String currentMonth = new SimpleDateFormat("MM", Locale.ENGLISH).format(new Date());
            cursor = this.database.rawQuery("SELECT * FROM order_details WHERE order_status='Completed' AND strftime('%m', product_order_date) = '" + currentMonth + "' ", null);
        } else if (type.equals(DatabaseOpenHelper.YEARLY)) {
            String currentYear = new SimpleDateFormat("yyyy", Locale.ENGLISH).format(new Date());
            cursor = this.database.rawQuery("SELECT * FROM order_details WHERE order_status='Completed' AND strftime('%Y', product_order_date) = '" + currentYear + "' ", null);
        } else {
            cursor = this.database.rawQuery("SELECT * FROM order_details WHERE order_status='Completed' ", null);
        }
        if (cursor.moveToFirst()) {
            do {
                double parseInt = (double) Integer.parseInt(cursor.getString(4));
                double price = Double.parseDouble(cursor.getString(5));
                Double.isNaN(parseInt);
                total_price += parseInt * price;
            } while (cursor.moveToNext());
        } else {
            total_price = Utils.DOUBLE_EPSILON;
        }
        cursor.close();
        this.database.close();
        return total_price;
    }

    // SalesReportActivity + SalesGraphActivity
    public double getTotalTax(String type) {
        Cursor cursor;
        double total_tax = Utils.DOUBLE_EPSILON;
        if (type.equals(DatabaseOpenHelper.DAILY)) {
            String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(new Date());
            //String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH).format(new Date());
            SQLiteDatabase sqLiteDatabase = this.database;
            cursor = sqLiteDatabase.rawQuery("SELECT * FROM order_list WHERE order_status='Completed' AND order_date='" + currentDate + "' ORDER BY order_id DESC", null);
        } else if (type.equals(DatabaseOpenHelper.MONTHLY)) {
            String currentMonth = new SimpleDateFormat("MM", Locale.ENGLISH).format(new Date());
            cursor = this.database.rawQuery("SELECT * FROM order_list WHERE order_status='Completed' AND strftime('%m', order_date) = '" + currentMonth + "' ", null);
        } else if (type.equals(DatabaseOpenHelper.YEARLY)) {
            String currentYear = new SimpleDateFormat("yyyy", Locale.ENGLISH).format(new Date());
            cursor = this.database.rawQuery("SELECT * FROM order_list WHERE order_status='Completed' AND strftime('%Y', order_date) = '" + currentYear + "' ", null);
        } else {
            cursor = this.database.rawQuery("SELECT * FROM order_list WHERE order_status='Completed' ", null);
        }
        if (cursor.moveToFirst()) {
            do {
                total_tax += Double.parseDouble(cursor.getString(cursor.getColumnIndex(DatabaseOpenHelper.ORDER_LIST_TAX)));
            } while (cursor.moveToNext());
        } else {
            total_tax = Utils.DOUBLE_EPSILON;
        }
        cursor.close();
        this.database.close();
        return total_tax;
    }

    // SalesReportActivity + SalesGraphActivity
    public double getTotalDiscount(String type) {
        Cursor cursor;
        double total_discount = Utils.DOUBLE_EPSILON;
        if (type.equals(DatabaseOpenHelper.DAILY)) {
            String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(new Date());
            //String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH).format(new Date());
            SQLiteDatabase sqLiteDatabase = this.database;
            cursor = sqLiteDatabase.rawQuery("SELECT * FROM order_list WHERE order_status='Completed' AND order_date='" + currentDate + "' ORDER BY order_id DESC", null);
        } else if (type.equals(DatabaseOpenHelper.MONTHLY)) {
            String currentMonth = new SimpleDateFormat("MM", Locale.ENGLISH).format(new Date());
            cursor = this.database.rawQuery("SELECT * FROM order_list WHERE order_status='Completed' AND strftime('%m', order_date) = '" + currentMonth + "' ", null);
        } else if (type.equals(DatabaseOpenHelper.YEARLY)) {
            String currentYear = new SimpleDateFormat("yyyy", Locale.ENGLISH).format(new Date());
            cursor = this.database.rawQuery("SELECT * FROM order_list WHERE order_status='Completed' AND strftime('%Y', order_date) = '" + currentYear + "' ", null);
        } else {
            cursor = this.database.rawQuery("SELECT * FROM order_list WHERE order_status='Completed'", null);
        }
        if (cursor.moveToFirst()) {
            do {
                total_discount += Double.parseDouble(cursor.getString(cursor.getColumnIndex(DatabaseOpenHelper.ORDER_LIST_DISCOUNT)));
            } while (cursor.moveToNext());
        } else {
            total_discount = Utils.DOUBLE_EPSILON;
        }
        cursor.close();
        this.database.close();
        return total_discount;
    }

    // SalesReportActivity
    public ArrayList<HashMap<String, String>> getSalesReport(String type) {
        ArrayList<HashMap<String, String>> orderDetailsList = new ArrayList<>();
        Cursor cursor = null;
        if (type.equals("all")) {
            cursor = this.database.rawQuery("SELECT * FROM order_details WHERE order_status='Completed' ORDER BY order_details_id DESC", null);
        } else if (type.equals(DatabaseOpenHelper.DAILY)) {
            String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(new Date());
            //String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH).format(new Date());
            SQLiteDatabase sqLiteDatabase = this.database;
            Log.d("DAILY", currentDate);
            cursor = sqLiteDatabase.rawQuery("SELECT * FROM order_details WHERE order_status='Completed' AND product_order_date='" + currentDate + "' ORDER BY order_Details_id DESC", null);
        } else if (type.equals(DatabaseOpenHelper.MONTHLY)) {
            String currentMonth = new SimpleDateFormat("MM", Locale.ENGLISH).format(new Date());
            Log.d("MONTHLY", currentMonth);
            cursor = this.database.rawQuery("SELECT * FROM order_details WHERE order_status='Completed' AND strftime('%m', product_order_date) = '" + currentMonth + "' ", null);
        } else if (type.equals(DatabaseOpenHelper.YEARLY)) {
            String currentYear = new SimpleDateFormat("yyyy", Locale.ENGLISH).format(new Date());
            Log.d("YEAR", currentYear);
            cursor = this.database.rawQuery("SELECT * FROM order_details WHERE order_status='Completed' AND strftime('%Y', product_order_date) = '" + currentYear + "' ", null);
        }
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<>();
                map.put(DatabaseOpenHelper.ORDER_DETAILS_PRODUCT_NAME, cursor.getString(2));
                map.put(DatabaseOpenHelper.ORDER_DETAILS_PRODUCT_WEIGHT, cursor.getString(3));
                map.put(DatabaseOpenHelper.ORDER_DETAILS_PRODUCT_QTY, cursor.getString(4));
                map.put(DatabaseOpenHelper.ORDER_DETAILS_PRODUCT_PRICE, cursor.getString(5));
                map.put(DatabaseOpenHelper.ORDER_DETAILS_ORDER_DATE, cursor.getString(6));
                orderDetailsList.add(map);
            } while (cursor.moveToNext());
        }
        cursor.close();
        this.database.close();
        return orderDetailsList;
    }

    // ExpenseReportActivity + ExpenseGraphActivity
    public double getTotalExpense(String type) {
        Cursor cursor;
        double total_cost = Utils.DOUBLE_EPSILON;
        if (type.equals(DatabaseOpenHelper.DAILY)) {
            String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(new Date());
            //String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH).format(new Date());
            SQLiteDatabase sqLiteDatabase = this.database;
            cursor = sqLiteDatabase.rawQuery("SELECT * FROM expense WHERE expense_date='" + currentDate + "' ORDER BY expense_id DESC", null);
        } else if (type.equals(DatabaseOpenHelper.MONTHLY)) {
            String currentMonth = new SimpleDateFormat("MM", Locale.ENGLISH).format(new Date());
            cursor = this.database.rawQuery("SELECT * FROM expense WHERE strftime('%m', expense_date) = '" + currentMonth + "' ", null);
        } else if (type.equals(DatabaseOpenHelper.YEARLY)) {
            String currentYear = new SimpleDateFormat("yyyy", Locale.ENGLISH).format(new Date());
            cursor = this.database.rawQuery("SELECT * FROM expense WHERE strftime('%Y', expense_date) = '" + currentYear + "' ", null);
        } else {
            cursor = this.database.rawQuery("SELECT * FROM expense", null);
        }
        if (cursor.moveToFirst()) {
            do {
                total_cost += Double.parseDouble(cursor.getString(3));
            } while (cursor.moveToNext());
        } else {
            total_cost = Utils.DOUBLE_EPSILON;
        }
        cursor.close();
        this.database.close();
        return total_cost;
    }

    // ExpenseReportActivity
    public ArrayList<HashMap<String, String>> getExpenseReport(String type) {
        ArrayList<HashMap<String, String>> orderDetailsList = new ArrayList<>();
        Cursor cursor = null;
        if (type.equals("all")) {
            cursor = this.database.rawQuery("SELECT * FROM expense ORDER BY expense_id DESC", null);
        } else if (type.equals(DatabaseOpenHelper.DAILY)) {
            String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(new Date());
            //String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH).format(new Date());
            SQLiteDatabase sqLiteDatabase = this.database;
            Log.d("DAILY", currentDate);
            cursor = sqLiteDatabase.rawQuery("SELECT * FROM expense WHERE expense_date='" + currentDate + "' ORDER BY expense_id DESC", null);
        } else if (type.equals(DatabaseOpenHelper.MONTHLY)) {
            String currentMonth = new SimpleDateFormat("MM", Locale.ENGLISH).format(new Date());
            Log.d("MONTHLY", currentMonth);
            cursor = this.database.rawQuery("SELECT * FROM expense WHERE strftime('%m', expense_date) = '" + currentMonth + "' ", null);
        } else if (type.equals(DatabaseOpenHelper.YEARLY)) {
            String currentYear = new SimpleDateFormat("yyyy", Locale.ENGLISH).format(new Date());
            Log.d("YEARLY", currentYear);
            cursor = this.database.rawQuery("SELECT * FROM expense WHERE strftime('%Y', expense_date) = '" + currentYear + "' ", null);
        }
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<>();
                map.put(DatabaseOpenHelper.EXPENSE_ID, cursor.getString(cursor.getColumnIndex(DatabaseOpenHelper.EXPENSE_ID)));
                map.put(DatabaseOpenHelper.EXPENSE_NAME, cursor.getString(cursor.getColumnIndex(DatabaseOpenHelper.EXPENSE_NAME)));
                map.put(DatabaseOpenHelper.EXPENSE_NOTE, cursor.getString(cursor.getColumnIndex(DatabaseOpenHelper.EXPENSE_NOTE)));
                map.put(DatabaseOpenHelper.EXPENSE_AMOUNT, cursor.getString(cursor.getColumnIndex(DatabaseOpenHelper.EXPENSE_AMOUNT)));
                map.put(DatabaseOpenHelper.EXPENSE_DATE, cursor.getString(cursor.getColumnIndex(DatabaseOpenHelper.EXPENSE_DATE)));
                map.put(DatabaseOpenHelper.EXPENSE_TIME, cursor.getString(cursor.getColumnIndex(DatabaseOpenHelper.EXPENSE_TIME)));
                orderDetailsList.add(map);
            } while (cursor.moveToNext());
        }
        cursor.close();
        this.database.close();
        return orderDetailsList;
    }

    // SalesGraphActivity
    public float getMonthlySalesAmount(String month, String getYear) {
        float total_price = 0.0f;
        Cursor cursor = this.database.rawQuery("SELECT * FROM order_details WHERE order_status='Completed' AND strftime('%m', product_order_date) = '" + month + "' AND strftime('%Y', product_order_date) = '" + getYear + "'  ", null);
        if (cursor.moveToFirst()) {
            do {
                total_price += ((float) Integer.parseInt(cursor.getString(4))) * Float.parseFloat(cursor.getString(5));
            } while (cursor.moveToNext());
        } else {
            total_price = 0.0f;
        }
        cursor.close();
        this.database.close();
        Log.d("total_price", "" + total_price);
        return total_price;
    }

    // ExpenseGraphActivity
    public float getMonthlyExpenseAmount(String month, String getYear) {
        float total_cost = 0.0f;
        Cursor cursor = this.database.rawQuery("SELECT * FROM expense WHERE strftime('%m', expense_date) = '" + month + "' AND strftime('%Y', expense_date) = '" + getYear + "'  ", null);
        if (cursor.moveToFirst()) {
            do {
                total_cost += Float.parseFloat(cursor.getString(3));
            } while (cursor.moveToNext());
        } else {
            total_cost = 0.0f;
        }
        cursor.close();
        this.database.close();
        Log.d("total_price", "" + total_cost);
        return total_cost;
    }
    /*/REPORT*/

    /*CUSTOMERS*/
    // CustomersActivity + ProductCart
    public ArrayList<HashMap<String, String>> getCustomers() {
        ArrayList<HashMap<String, String>> customer = new ArrayList<>();
        Cursor cursor = this.database.rawQuery("SELECT * FROM customers ORDER BY customer_id DESC", null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<>();
                map.put(DatabaseOpenHelper.CUSTOMER_ID, cursor.getString(0));
                map.put(DatabaseOpenHelper.CUSTOMER_NAME, cursor.getString(1));
                map.put(DatabaseOpenHelper.CUSTOMER_ADDRESS, cursor.getString(2));
                map.put(DatabaseOpenHelper.CUSTOMER_HP, cursor.getString(3));
                map.put(DatabaseOpenHelper.CUSTOMER_WA, cursor.getString(4));
                map.put(DatabaseOpenHelper.CUSTOMER_ACCOUNT, cursor.getString(5));
                map.put(DatabaseOpenHelper.CUSTOMER_INFORMATION, cursor.getString(6));
                map.put(DatabaseOpenHelper.CUSTOMER_LAST_UPDATE, cursor.getString(7));
                customer.add(map);
            } while (cursor.moveToNext());
        }
        cursor.close();
        this.database.close();
        return customer;
    }

    // CustomersActivity
    public ArrayList<HashMap<String, String>> searchCustomers(String s) {
        ArrayList<HashMap<String, String>> customer = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = this.database;
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM customers WHERE customer_name LIKE '%" + s + "%' ORDER BY customer_id DESC", null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<>();
                map.put(DatabaseOpenHelper.CUSTOMER_ID, cursor.getString(0));
                map.put(DatabaseOpenHelper.CUSTOMER_NAME, cursor.getString(1));
                map.put(DatabaseOpenHelper.CUSTOMER_ADDRESS, cursor.getString(2));
                map.put(DatabaseOpenHelper.CUSTOMER_HP, cursor.getString(3));
                map.put(DatabaseOpenHelper.CUSTOMER_WA, cursor.getString(4));
                map.put(DatabaseOpenHelper.CUSTOMER_ACCOUNT, cursor.getString(5));
                map.put(DatabaseOpenHelper.CUSTOMER_INFORMATION, cursor.getString(6));
                map.put(DatabaseOpenHelper.CUSTOMER_LAST_UPDATE, cursor.getString(7));
                customer.add(map);
            } while (cursor.moveToNext());
        }
        cursor.close();
        this.database.close();
        return customer;
    }

    // CustomerAdapter
    public boolean deleteCustomer(String customer_id) {
        long check = (long) this.database.delete("customers", "customer_id=?", new String[]{customer_id});
        this.database.close();
        return check == 1;
    }

    // AddCustomersActivity
    public boolean addCustomer(String customer_name, String customer_address, String customer_hp, String customer_wa, String customer_account, String customer_information, String customer_last_update) {
        ContentValues values = new ContentValues();
        values.put(DatabaseOpenHelper.CUSTOMER_NAME, customer_name);
        values.put(DatabaseOpenHelper.CUSTOMER_ADDRESS, customer_address);
        values.put(DatabaseOpenHelper.CUSTOMER_HP, customer_hp);
        values.put(DatabaseOpenHelper.CUSTOMER_WA, customer_wa);
        values.put(DatabaseOpenHelper.CUSTOMER_ACCOUNT, customer_account);
        values.put(DatabaseOpenHelper.CUSTOMER_INFORMATION, customer_information);
        values.put(DatabaseOpenHelper.CUSTOMER_LAST_UPDATE, customer_last_update);
        long check = this.database.insert("customers", null, values);
        this.database.close();
        if (check == -1) {
            return false;
        }
        return true;
    }

    // EditCustomersActivity
    public boolean updateCustomer(String customer_id, String customer_name, String customer_address, String customer_hp, String customer_wa, String customer_account, String customer_information, String customer_last_update) {
        ContentValues values = new ContentValues();
        values.put(DatabaseOpenHelper.CUSTOMER_NAME, customer_name);
        values.put(DatabaseOpenHelper.CUSTOMER_ADDRESS, customer_address);
        values.put(DatabaseOpenHelper.CUSTOMER_HP, customer_hp);
        values.put(DatabaseOpenHelper.CUSTOMER_WA, customer_wa);
        values.put(DatabaseOpenHelper.CUSTOMER_ACCOUNT, customer_account);
        values.put(DatabaseOpenHelper.CUSTOMER_INFORMATION, customer_information);
        values.put(DatabaseOpenHelper.CUSTOMER_LAST_UPDATE, customer_last_update);
        long check = (long) this.database.update("customers", values, " customer_id=? ", new String[]{customer_id});
        this.database.close();
        return check != -1;
    }
    /*/CUSTOMERS*/

    /*SUPPLIERS*/
    // SuppliersActivity
    public ArrayList<HashMap<String, String>> getSuppliers() {
        ArrayList<HashMap<String, String>> supplier = new ArrayList<>();
        Cursor cursor = this.database.rawQuery("SELECT * FROM suppliers ORDER BY supplier_id DESC", null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<>();
                map.put(DatabaseOpenHelper.SUPPLIER_ID, cursor.getString(0));
                map.put(DatabaseOpenHelper.SUPPLIER_NAME, cursor.getString(1));
                map.put(DatabaseOpenHelper.SUPPLIER_ADDRESS, cursor.getString(2));
                map.put(DatabaseOpenHelper.SUPPLIER_CONTACT, cursor.getString(3));
                map.put(DatabaseOpenHelper.SUPPLIER_FAX, cursor.getString(4));
                map.put(DatabaseOpenHelper.SUPPLIER_SALES, cursor.getString(5));
                map.put(DatabaseOpenHelper.SUPPLIER_HP, cursor.getString(6));
                map.put(DatabaseOpenHelper.SUPPLIER_ACCOUNT, cursor.getString(7));
                map.put(DatabaseOpenHelper.SUPPLIER_INFORMATION, cursor.getString(8));
                map.put(DatabaseOpenHelper.SUPPLIER_LAST_UPDATE, cursor.getString(9));
                supplier.add(map);
            } while (cursor.moveToNext());
        }
        this.database.close();
        return supplier;
    }

    // SuppliersActivity
    public ArrayList<HashMap<String, String>> searchSuppliers(String s) {
        ArrayList<HashMap<String, String>> customer = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = this.database;
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM suppliers WHERE supplier_name LIKE '%" + s + "%' ORDER BY supplier_id DESC", null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<>();
                map.put(DatabaseOpenHelper.SUPPLIER_ID, cursor.getString(0));
                map.put(DatabaseOpenHelper.SUPPLIER_NAME, cursor.getString(1));
                map.put(DatabaseOpenHelper.SUPPLIER_ADDRESS, cursor.getString(2));
                map.put(DatabaseOpenHelper.SUPPLIER_CONTACT, cursor.getString(3));
                map.put(DatabaseOpenHelper.SUPPLIER_FAX, cursor.getString(4));
                map.put(DatabaseOpenHelper.SUPPLIER_SALES, cursor.getString(5));
                map.put(DatabaseOpenHelper.SUPPLIER_HP, cursor.getString(6));
                map.put(DatabaseOpenHelper.SUPPLIER_ACCOUNT, cursor.getString(7));
                map.put(DatabaseOpenHelper.SUPPLIER_INFORMATION, cursor.getString(8));
                map.put(DatabaseOpenHelper.SUPPLIER_LAST_UPDATE, cursor.getString(9));
                customer.add(map);
            } while (cursor.moveToNext());
        }
        cursor.close();
        this.database.close();
        return customer;
    }

    // SupplierAdapter
    public boolean deleteSupplier(String customer_id) {
        long check = (long) this.database.delete("suppliers", "supplier_id=?", new String[]{customer_id});
        this.database.close();
        return check == 1;
    }

    // AddSuppliersActivity
    public boolean addSuppliers(String suppliers_name, String suppliers_address, String suppliers_contact, String suppliers_fax, String suppliers_sales, String suppliers_hp, String suppliers_account, String suppliers_information, String suppliers_last_update) {
        ContentValues values = new ContentValues();
        values.put(DatabaseOpenHelper.SUPPLIER_NAME, suppliers_name);
        values.put(DatabaseOpenHelper.SUPPLIER_ADDRESS, suppliers_address);
        values.put(DatabaseOpenHelper.SUPPLIER_CONTACT, suppliers_contact);
        values.put(DatabaseOpenHelper.SUPPLIER_FAX, suppliers_fax);
        values.put(DatabaseOpenHelper.SUPPLIER_SALES, suppliers_sales);
        values.put(DatabaseOpenHelper.SUPPLIER_HP, suppliers_hp);
        values.put(DatabaseOpenHelper.SUPPLIER_ACCOUNT, suppliers_account);
        values.put(DatabaseOpenHelper.SUPPLIER_INFORMATION, suppliers_information);
        values.put(DatabaseOpenHelper.SUPPLIER_LAST_UPDATE, suppliers_last_update);
        long check = this.database.insert("suppliers", null, values);
        this.database.close();
        if (check == -1) {
            return false;
        }
        return true;
    }

    // EditSuppliersActivity
    public boolean updateSuppliers(String suppliers_id, String suppliers_name, String suppliers_address, String suppliers_contact, String suppliers_fax, String suppliers_sales, String suppliers_hp, String suppliers_account, String suppliers_information, String suppliers_last_update) {
        ContentValues values = new ContentValues();
        values.put(DatabaseOpenHelper.SUPPLIER_NAME, suppliers_name);
        values.put(DatabaseOpenHelper.SUPPLIER_ADDRESS, suppliers_address);
        values.put(DatabaseOpenHelper.SUPPLIER_CONTACT, suppliers_contact);
        values.put(DatabaseOpenHelper.SUPPLIER_FAX, suppliers_fax);
        values.put(DatabaseOpenHelper.SUPPLIER_SALES, suppliers_sales);
        values.put(DatabaseOpenHelper.SUPPLIER_HP, suppliers_hp);
        values.put(DatabaseOpenHelper.SUPPLIER_ACCOUNT, suppliers_account);
        values.put(DatabaseOpenHelper.SUPPLIER_INFORMATION, suppliers_information);
        values.put(DatabaseOpenHelper.SUPPLIER_LAST_UPDATE, suppliers_last_update);
        long check = (long) this.database.update("suppliers", values, "supplier_id=?", new String[]{suppliers_id});
        this.database.close();
        return check != -1;
    }
    /*/SUPPLIERS*/
}
