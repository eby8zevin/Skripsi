package com.ahmadabuhasan.skripsi.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.ahmadabuhasan.skripsi.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;

import es.dmoral.toasty.Toasty;

/*
 * Created by Ahmad Abu Hasan on 30/01/2021
 */

public class DatabaseOpenHelper extends SQLiteOpenHelper {

    private final Context mContext;

    public static final String DAILY = "daily";
    public static final String MONTHLY = "monthly";
    public static final String YEARLY = "yearly";
    public static final String COMPLETED = "Completed";
    public static final String CANCEL = "Cancel";

    // Database Version
    public static final int DATABASE_VERSION = 1;

    // Database Name
    public static final String DATABASE_NAME = "skripsi.db";

    // Table Name
    public static final String TABLE_CUSTOMER = "customers";
    public static final String TABLE_EXPENSE = "expense";
    public static final String TABLE_ORDER_DETAILS = "order_details";
    public static final String TABLE_ORDER_LIST = "order_list";
    public static final String TABLE_ORDER_TYPE = "order_type";
    public static final String TABLE_PAYMENT_METHOD = "payment_method";
    public static final String TABLE_PRODUCT = "products";
    public static final String TABLE_PRODUCT_CART = "product_cart";
    public static final String TABLE_CATEGORY = "product_category";
    public static final String TABLE_WEIGHT = "product_weight";
    public static final String TABLE_SHOP = "shop";
    public static final String TABLE_SUPPLIER = "suppliers";

    // Column customers
    public static final String CUSTOMER_ID = "customer_id";
    public static final String CUSTOMER_NAME = "customer_name";
    public static final String CUSTOMER_ADDRESS = "customer_address";
    public static final String CUSTOMER_HP = "customer_hp";
    public static final String CUSTOMER_WA = "customer_wa";
    public static final String CUSTOMER_ACCOUNT = "customer_account";
    public static final String CUSTOMER_INFORMATION = "customer_information";
    public static final String CUSTOMER_LAST_UPDATE = "customer_last_update";

    // Column expense
    public static final String EXPENSE_ID = "expense_id";
    public static final String EXPENSE_NAME = "expense_name";
    public static final String EXPENSE_NOTE = "expense_note";
    public static final String EXPENSE_AMOUNT = "expense_amount";
    public static final String EXPENSE_DATE = "expense_date";
    public static final String EXPENSE_TIME = "expense_time";

    // Column order_details
    public static final String ORDER_DETAILS_ID = "order_details_id";
    public static final String ORDER_DETAILS_INVOICE_ID = "invoice_id";
    public static final String ORDER_DETAILS_PRODUCT_NAME = "product_name";
    public static final String ORDER_DETAILS_PRODUCT_WEIGHT = "product_weight";
    public static final String ORDER_DETAILS_PRODUCT_QTY = "product_qty";
    public static final String ORDER_DETAILS_PRODUCT_PRICE = "product_price";
    public static final String ORDER_DETAILS_ORDER_DATE = "product_order_date";
    public static final String ORDER_DETAILS_ORDER_STATUS = "order_status";

    // Column order_list
    public static final String ORDER_LIST_ID = "order_id";
    public static final String ORDER_LIST_INVOICE_ID = "invoice_id";
    public static final String ORDER_LIST_DATE = "order_date";
    public static final String ORDER_LIST_TIME = "order_time";
    public static final String ORDER_LIST_TYPE = "order_type";
    public static final String ORDER_LIST_PAYMENT_METHOD = "order_payment_method";
    public static final String ORDER_LIST_CUSTOMER_NAME = "customer_name";
    public static final String ORDER_LIST_TAX = "tax";
    public static final String ORDER_LIST_DISCOUNT = "discount";
    public static final String ORDER_LIST_STATUS = "order_status";

    // Column order_type
    public static final String ORDER_TYPE_ID = "order_type_id";
    public static final String ORDER_TYPE_NAME = "order_type_name";

    // Column payment_method
    public static final String PAYMENT_METHOD_ID = "payment_method_id";
    public static final String PAYMENT_METHOD_NAME = "payment_method_name";

    // Column products
    public static final String PRODUCT_ID = "product_id";
    public static final String PRODUCT_NAME = "product_name";
    public static final String PRODUCT_CODE = "product_code";
    public static final String PRODUCT_CATEGORY = "product_category";
    public static final String PRODUCT_BUY = "product_buy";
    public static final String PRODUCT_STOCK = "product_stock";
    public static final String PRODUCT_PRICE = "product_price";
    public static final String PRODUCT_TOTAL_QTY = "product_total_qty";
    public static final String PRODUCT_DISC_QTY = "product_disc_qty";
    public static final String PRODUCT_WEIGHT = "product_weight";
    public static final String PRODUCT_WEIGHT_UNIT_ID = "product_weight_unit_id";
    public static final String PRODUCT_LAST_UPDATE = "product_last_update";
    public static final String PRODUCT_INFORMATION = "product_information";
    public static final String PRODUCT_SUPPLIER = "product_supplier";

    // Column product_cart
    public static final String PRODUCT_CART_ID = "cart_id";
    public static final String CART_PRODUCT_ID = "product_id";
    public static final String CART_PRODUCT_WEIGHT = "product_weight";
    public static final String CART_PRODUCT_WEIGHT_UNIT = "product_weight_unit";
    public static final String CART_PRODUCT_PRICE = "product_price";
    public static final String CART_PRODUCT_QTY = "product_qty";
    public static final String CART_PRODUCT_STOCK = "product_stock";

    // Column product_category
    public static final String CATEGORY_ID = "category_id";
    public static final String CATEGORY_NAME = "category_name";

    // Column product_weight
    public static final String WEIGHT_ID = "weight_id";
    public static final String WEIGHT_UNIT = "weight_unit";

    // Column shop
    public static final String SHOP_ID = "shop_id";
    public static final String SHOP_NAME = "shop_name";
    public static final String SHOP_CONTACT = "shop_contact";
    public static final String SHOP_EMAIL = "shop_email";
    public static final String SHOP_ADDRESS = "shop_address";
    public static final String SHOP_CURRENCY = "shop_currency";
    public static final String SHOP_TAX = "tax";

    // Column suppliers
    public static final String SUPPLIER_ID = "supplier_id";
    public static final String SUPPLIER_NAME = "supplier_name";
    public static final String SUPPLIER_ADDRESS = "supplier_address";
    public static final String SUPPLIER_CONTACT = "supplier_contact";
    public static final String SUPPLIER_FAX = "supplier_fax";
    public static final String SUPPLIER_SALES = "supplier_sales";
    public static final String SUPPLIER_HP = "supplier_hp";
    public static final String SUPPLIER_ACCOUNT = "supplier_account";
    public static final String SUPPLIER_INFORMATION = "supplier_information";
    public static final String SUPPLIER_LAST_UPDATE = "supplier_last_update";

    public DatabaseOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.mContext = context;
    }

    // Table Create Statement
    // customers
    private static final String CREATE_CUSTOMERS = "CREATE TABLE " + TABLE_CUSTOMER +
            "(" + CUSTOMER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + CUSTOMER_NAME + " TEXT,"
            + CUSTOMER_ADDRESS + " TEXT,"
            + CUSTOMER_HP + " TEXT,"
            + CUSTOMER_WA + " TEXT,"
            + CUSTOMER_ACCOUNT + " TEXT,"
            + CUSTOMER_INFORMATION + " TEXT,"
            + CUSTOMER_LAST_UPDATE + " TEXT"
            + ")";

    // expense
    private static final String CREATE_EXPENSE = "CREATE TABLE " + TABLE_EXPENSE +
            "(" + EXPENSE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + EXPENSE_NAME + " TEXT,"
            + EXPENSE_NOTE + " TEXT,"
            + EXPENSE_AMOUNT + " TEXT,"
            + EXPENSE_DATE + " TEXT,"
            + EXPENSE_TIME + " TEXT"
            + ")";

    // order_details
    private static final String CREATE_ORDER_DETAILS = "CREATE TABLE " + TABLE_ORDER_DETAILS +
            "(" + ORDER_DETAILS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + ORDER_DETAILS_INVOICE_ID + " TEXT,"
            + ORDER_DETAILS_PRODUCT_NAME + " TEXT,"
            + ORDER_DETAILS_PRODUCT_WEIGHT + " TEXT,"
            + ORDER_DETAILS_PRODUCT_QTY + " TEXT,"
            + ORDER_DETAILS_PRODUCT_PRICE + " TEXT,"
            + ORDER_DETAILS_ORDER_DATE + " TEXT,"
            + ORDER_DETAILS_ORDER_STATUS + " TEXT"
            + ")";

    // order_list
    private static final String CREATE_ORDER_LIST = "CREATE TABLE " + TABLE_ORDER_LIST +
            "(" + ORDER_LIST_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + ORDER_LIST_INVOICE_ID + " TEXT,"
            + ORDER_LIST_DATE + " TEXT,"
            + ORDER_LIST_TIME + " TEXT,"
            + ORDER_LIST_TYPE + " TEXT,"
            + ORDER_LIST_PAYMENT_METHOD + " TEXT,"
            + ORDER_LIST_CUSTOMER_NAME + " TEXT,"
            + ORDER_LIST_TAX + " TEXT,"
            + ORDER_LIST_DISCOUNT + " TEXT,"
            + ORDER_LIST_STATUS + " TEXT"
            + ")";

    // order_type
    private static final String CREATE_ORDER_TYPE = "CREATE TABLE " + TABLE_ORDER_TYPE +
            "(" + ORDER_TYPE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + ORDER_TYPE_NAME + " TEXT"
            + ")";

    // payment_method
    private static final String CREATE_PAYMENT_METHOD = "CREATE TABLE " + TABLE_PAYMENT_METHOD +
            "(" + PAYMENT_METHOD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + PAYMENT_METHOD_NAME + " TEXT"
            + ")";

    // products
    private static final String CREATE_PRODUCTS = "CREATE TABLE " + TABLE_PRODUCT +
            "(" + PRODUCT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + PRODUCT_NAME + " TEXT,"
            + PRODUCT_CODE + " TEXT,"
            + PRODUCT_CATEGORY + " TEXT,"
            + PRODUCT_BUY + " TEXT,"
            + PRODUCT_STOCK + " TEXT,"
            + PRODUCT_PRICE + " TEXT,"
            + PRODUCT_TOTAL_QTY + " TEXT,"
            + PRODUCT_DISC_QTY + " TEXT,"
            + PRODUCT_WEIGHT + " TEXT,"
            + PRODUCT_WEIGHT_UNIT_ID + " TEXT,"
            + PRODUCT_LAST_UPDATE + " TEXT,"
            + PRODUCT_INFORMATION + " TEXT,"
            + PRODUCT_SUPPLIER + " TEXT"
            + ")";

    // product_cart
    private static final String CREATE_PRODUCT_CART = "CREATE TABLE " + TABLE_PRODUCT_CART +
            "(" + PRODUCT_CART_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + CART_PRODUCT_ID + " TEXT,"
            + CART_PRODUCT_WEIGHT + " TEXT,"
            + CART_PRODUCT_WEIGHT_UNIT + " TEXT,"
            + CART_PRODUCT_PRICE + " TEXT,"
            + CART_PRODUCT_QTY + " TEXT,"
            + CART_PRODUCT_STOCK + " TEXT"
            + ")";

    // product_category
    private static final String CREATE_CATEGORY = "CREATE TABLE " + TABLE_CATEGORY +
            "(" + CATEGORY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + CATEGORY_NAME + " TEXT"
            + ")";

    // product_weight
    private static final String CREATE_WEIGHT = "CREATE TABLE " + TABLE_WEIGHT +
            "(" + WEIGHT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + WEIGHT_UNIT + " TEXT"
            + ")";

    // shop
    private static final String CREATE_SHOP = "CREATE TABLE " + TABLE_SHOP +
            "(" + SHOP_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + SHOP_NAME + " TEXT,"
            + SHOP_CONTACT + " TEXT,"
            + SHOP_EMAIL + " TEXT,"
            + SHOP_ADDRESS + " TEXT,"
            + SHOP_CURRENCY + " TEXT,"
            + SHOP_TAX + " TEXT"
            + ")";

    // suppliers
    private static final String CREATE_SUPPLIERS = "CREATE TABLE " + TABLE_SUPPLIER +
            "(" + SUPPLIER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + SUPPLIER_NAME + " TEXT,"
            + SUPPLIER_ADDRESS + " TEXT,"
            + SUPPLIER_CONTACT + " TEXT,"
            + SUPPLIER_FAX + " TEXT,"
            + SUPPLIER_SALES + " TEXT,"
            + SUPPLIER_HP + " TEXT,"
            + SUPPLIER_ACCOUNT + " TEXT,"
            + SUPPLIER_INFORMATION + " TEXT,"
            + SUPPLIER_LAST_UPDATE + " TEXT"
            + ")";

    @Override
    public void onCreate(SQLiteDatabase db) {
        // script sql
        db.execSQL(CREATE_CUSTOMERS);
        db.execSQL(CREATE_EXPENSE);
        db.execSQL(CREATE_ORDER_DETAILS);
        db.execSQL(CREATE_ORDER_LIST);
        db.execSQL(CREATE_ORDER_TYPE);
        db.execSQL(CREATE_PAYMENT_METHOD);
        db.execSQL(CREATE_PRODUCTS);
        db.execSQL(CREATE_PRODUCT_CART);
        db.execSQL(CREATE_CATEGORY);
        db.execSQL(CREATE_WEIGHT);
        //db.execSQL(CREATE_SHOP);
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_SHOP + "(shop_id INTEGER PRIMARY KEY, shop_name TEXT, shop_contact TEXT, shop_email TEXT, shop_address TEXT, shop_currency TEXT, tax TEXT)");
        db.execSQL("INSERT INTO " + TABLE_SHOP + "(shop_id, shop_name, shop_contact, shop_email, shop_address, shop_currency, tax) VALUES (1, 'Skripsi', '0123456789', 'skripsi@ahmadabuhasan.com', 'Pasuruan City', 'Rp', '0')");
        db.execSQL(CREATE_SUPPLIERS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CUSTOMER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXPENSE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDER_DETAILS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDER_LIST);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDER_TYPE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PAYMENT_METHOD);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCT_CART);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WEIGHT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SHOP);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SUPPLIER);

        onCreate(db);
    }

    public void backup(String outFileName) {
        try {
            FileInputStream fis = new FileInputStream(new File(this.mContext.getDatabasePath(DATABASE_NAME).toString()));
            OutputStream output = new FileOutputStream(outFileName);
            byte[] buffer = new byte[1024];
            while (true) {
                int length = fis.read(buffer);
                if (length > 0) {
                    output.write(buffer, 0, length);
                } else {
                    output.flush();
                    output.close();
                    fis.close();
                    Toasty.success(this.mContext, this.mContext.getString(R.string.backup_completed_successfully), Toasty.LENGTH_SHORT).show();
                    return;
                }
            }
        } catch (Exception e) {
            Toasty.error(this.mContext, (int) R.string.unable_to_backup_database_retry, Toasty.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public void importDB(String inFileName) {
        String outFileName = this.mContext.getDatabasePath(DATABASE_NAME).toString();
        try {
            FileInputStream fis = new FileInputStream(new File(inFileName));
            OutputStream output = new FileOutputStream(outFileName);
            byte[] buffer = new byte[1024];
            while (true) {
                int length = fis.read(buffer);
                if (length > 0) {
                    output.write(buffer, 0, length);
                } else {
                    output.flush();
                    output.close();
                    fis.close();
                    Toasty.success(this.mContext, (int) R.string.database_Import_completed, Toasty.LENGTH_SHORT).show();
                    return;
                }
            }
        } catch (Exception e) {
            Toasty.error(this.mContext, (int) R.string.unable_to_import_database_retry, Toasty.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
    
    https://github.com/prof18/Database-Backup-Restore
    public void backup(String outFileName) {

        //database path
        final String inFileName = mContext.getDatabasePath(DATABASE_NAME).toString();

        try {

            File dbFile = new File(inFileName);
            FileInputStream fis = new FileInputStream(dbFile);

            // Open the empty db as the output stream
            OutputStream output = new FileOutputStream(outFileName);

            // Transfer bytes from the input file to the output file
            byte[] buffer = new byte[1024];
            int length;
            while ((length = fis.read(buffer)) > 0) {
                output.write(buffer, 0, length);
            }

            // Close the streams
            output.flush();
            output.close();
            fis.close();

            Toast.makeText(mContext, "Backup Completed", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            Toast.makeText(mContext, "Unable to backup database. Retry", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public void importDB(String inFileName) {

        final String outFileName = mContext.getDatabasePath(DATABASE_NAME).toString();

        try {

            File dbFile = new File(inFileName);
            FileInputStream fis = new FileInputStream(dbFile);

            // Open the empty db as the output stream
            OutputStream output = new FileOutputStream(outFileName);

            // Transfer bytes from the input file to the output file
            byte[] buffer = new byte[1024];
            int length;
            while ((length = fis.read(buffer)) > 0) {
                output.write(buffer, 0, length);
            }

            // Close the streams
            output.flush();
            output.close();
            fis.close();

            Toast.makeText(mContext, "Import Completed", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            Toast.makeText(mContext, "Unable to import database. Retry", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

}
