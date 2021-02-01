package com.ahmadabuhasan.skripsi.print;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ahmadabuhasan.skripsi.R;
import com.ahmadabuhasan.skripsi.adapter.OrderDetailsAdapter;
import com.ahmadabuhasan.skripsi.database.DatabaseAccess;
import com.ahmadabuhasan.skripsi.database.DatabaseOpenHelper;
import com.ahmadabuhasan.skripsi.pdf_report.BarCodeEncoder;
import com.ahmadabuhasan.skripsi.pdf_report.TemplatePDF;
import com.ahmadabuhasan.skripsi.utils.PrefMng;
import com.ahmadabuhasan.skripsi.utils.Tools;
import com.ahmadabuhasan.skripsi.utils.WoosimPrnMng;
import com.ahmadabuhasan.skripsi.utils.printerFactory;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import es.dmoral.toasty.Toasty;

/*
 * Created by Ahmad Abu Hasan on 01/02/2021
 */

public class OrderDetailsActivity extends AppCompatActivity {

    private OrderDetailsAdapter orderDetailsAdapter;
    private RecyclerView recyclerView;
    ImageView imgNoProduct;
    TextView textView_NoProducts;
    TextView textView_TotalPrice;
    TextView textView_Tax;
    TextView textView_Discount;
    TextView textView_TotalCost;
    Button button_PDF;
    Button button_Print;

    String order_id, order_date, order_time, customer_name, tax, discount;
    String shop_name, shop_contact, shop_email, shop_address, currency;
    Double total_price, getTax, getDiscount, calculated_total_price;
    String shortText, longText;

    private static final int REQUEST_CONNECT = 100;
    private String[] header = {"Description", "Price"};
    private TemplatePDF templatePDF;
    private WoosimPrnMng mPrnMng = null;
    Bitmap bm = null;
    DecimalFormat decimalFormat;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.order_details);

        this.imgNoProduct = findViewById(R.id.image_no_product);
        this.textView_NoProducts = findViewById(R.id.tv_no_products);
        this.textView_TotalPrice = findViewById(R.id.tv_total_price);
        this.textView_Tax = findViewById(R.id.tv_tax);
        this.textView_Discount = findViewById(R.id.tv_discount);
        this.textView_TotalCost = findViewById(R.id.tv_total_cost);
        this.button_PDF = findViewById(R.id.btn_pdf_receipt);
        this.button_Print = findViewById(R.id.btn_thermal_printer);
        this.recyclerView = findViewById(R.id.order_details_recyclerview);

        this.order_id = getIntent().getExtras().getString(DatabaseOpenHelper.ORDER_LIST_ID);
        this.order_date = getIntent().getExtras().getString(DatabaseOpenHelper.ORDER_LIST_DATE);
        this.order_time = getIntent().getExtras().getString(DatabaseOpenHelper.ORDER_LIST_TIME);
        this.customer_name = getIntent().getExtras().getString(DatabaseOpenHelper.ORDER_LIST_CUSTOMER_NAME);
        this.tax = getIntent().getExtras().getString(DatabaseOpenHelper.ORDER_LIST_TAX);
        this.discount = getIntent().getExtras().getString(DatabaseOpenHelper.ORDER_LIST_DISCOUNT);

        this.imgNoProduct.setVisibility(View.GONE);
        this.textView_NoProducts.setVisibility(View.GONE);

        this.recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        this.recyclerView.setHasFixedSize(true);

        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(this);
        databaseAccess.open();
        List<HashMap<String, String>> orderDetailsList = databaseAccess.getOrderDetailsList(this.order_id);
        if (orderDetailsList.isEmpty()) {
            Toasty.info(this, (int) R.string.no_data_found, Toasty.LENGTH_SHORT).show();
        } else {
            OrderDetailsAdapter orderDetailsAdapter1 = new OrderDetailsAdapter(this, orderDetailsList);
            this.orderDetailsAdapter = orderDetailsAdapter1;
            this.recyclerView.setAdapter(orderDetailsAdapter1);
        }

        databaseAccess.open();
        List<HashMap<String, String>> shopData = databaseAccess.getShopInformation();
        this.shop_name = shopData.get(0).get(DatabaseOpenHelper.SHOP_NAME);
        this.shop_contact = shopData.get(0).get(DatabaseOpenHelper.SHOP_CONTACT);
        this.shop_email = shopData.get(0).get(DatabaseOpenHelper.SHOP_EMAIL);
        this.shop_address = shopData.get(0).get(DatabaseOpenHelper.SHOP_ADDRESS);
        this.currency = shopData.get(0).get(DatabaseOpenHelper.SHOP_CURRENCY);

        databaseAccess.open();
        this.total_price = databaseAccess.totalOrderPrice(this.order_id);
        this.getTax = Double.parseDouble(this.tax);
        this.getDiscount = Double.parseDouble(this.discount);
        this.textView_Tax.setText(getString(R.string.total_tax) + " : " + this.currency + " " + NumberFormat.getInstance(Locale.getDefault()).format(this.getTax));
        this.textView_Discount.setText(getString(R.string.discount) + " : " + this.currency + " " + NumberFormat.getInstance(Locale.getDefault()).format(this.getDiscount));
        this.calculated_total_price = (this.total_price + this.getTax) - this.getDiscount;
        this.textView_TotalPrice.setText(getString(R.string.sub_total) + ": " + this.currency + " " + NumberFormat.getInstance(Locale.getDefault()).format(this.total_price));
        this.textView_TotalCost.setText(getString(R.string.total_price) + " " + this.currency + " " + NumberFormat.getInstance(Locale.getDefault()).format(this.calculated_total_price));

        this.shortText = "Customer Name: Mr/Mrs. " + this.customer_name;
        this.longText = "Thanks for purchase. Visit again";

        decimalFormat = new DecimalFormat("#0.00");
        TemplatePDF templatePDF1 = new TemplatePDF(getApplicationContext());
        this.templatePDF = templatePDF1;
        templatePDF1.openDocument();
        this.templatePDF.addMetaData("Skripsi", "Order Receipt", "Ahmad Abu Hasan");
        this.templatePDF.addTitle(this.shop_name,
                this.shop_address +
                        "\n Email: " + this.shop_email +
                        "\nContact: " + this.shop_contact +
                        "\nInvoice ID: " + this.order_id,
                " " + this.order_time + " " + this.order_date);
        this.templatePDF.addParagraph(this.shortText);

        try {
            this.bm = new BarCodeEncoder().encodeAsBitmap(this.order_id, BarcodeFormat.CODE_128, 600, 300);
        } catch (WriterException e) {
            Log.d("Data", e.toString());
        }

        this.button_PDF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OrderDetailsActivity.this.templatePDF.createTable(OrderDetailsActivity.this.header, OrderDetailsActivity.this.getOrdersData());
                OrderDetailsActivity.this.templatePDF.addRightParagraph(OrderDetailsActivity.this.longText);
                OrderDetailsActivity.this.templatePDF.addImage(OrderDetailsActivity.this.bm);
                OrderDetailsActivity.this.templatePDF.closeDocument();
                OrderDetailsActivity.this.templatePDF.viewPDF();

            }
        });

        this.button_Print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Tools.isBlueToothOn(OrderDetailsActivity.this)) {
                    PrefMng.saveActivePrinter(OrderDetailsActivity.this, PrefMng.PRN_WOOSIM_SELECTED);
                    OrderDetailsActivity.this.startActivityForResult(new Intent(OrderDetailsActivity.this, DeviceListActivity.class), 100);
                }
            }
        });
    }

    private ArrayList<String[]> getOrdersData() {
        ArrayList<String[]> rows = new ArrayList<>();
        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(this);
        databaseAccess.open();
        List<HashMap<String, String>> orderDetailsList = databaseAccess.getOrderDetailsList(this.order_id);
        for (int i = 0; i < orderDetailsList.size(); i++) {
            String price = orderDetailsList.get(i).get(DatabaseOpenHelper.ORDER_DETAILS_PRODUCT_PRICE);
            String qty = orderDetailsList.get(i).get(DatabaseOpenHelper.ORDER_DETAILS_PRODUCT_QTY);
            double parseInt = (double) Integer.parseInt(qty);
            double parseDouble = Double.parseDouble(price);
            Double.isNaN(parseInt);
            double cost_total = parseInt * parseDouble;

            rows.add(new String[]{orderDetailsList.get(i).get(DatabaseOpenHelper.ORDER_DETAILS_PRODUCT_NAME) +
                    "\n" + orderDetailsList.get(i).get(DatabaseOpenHelper.ORDER_DETAILS_PRODUCT_WEIGHT) +
                    "\n(" + qty + " x " + this.currency + "  " + NumberFormat.getInstance(Locale.getDefault()).format(parseDouble) + ")",
                    this.currency + " " + NumberFormat.getInstance(Locale.getDefault()).format(cost_total)});
        }
        rows.add(new String[]{"..........................................", ".................................."});
        rows.add(new String[]{"Sub Total: ", this.currency + " " + NumberFormat.getInstance(Locale.getDefault()).format(this.total_price)});
        rows.add(new String[]{"Total Tax: ", this.currency + " " + NumberFormat.getInstance(Locale.getDefault()).format(this.getTax)});
        rows.add(new String[]{"Discount: ", this.currency + " " + NumberFormat.getInstance(Locale.getDefault()).format(this.getDiscount)});
        rows.add(new String[]{"..........................................", ".................................."});
        rows.add(new String[]{"Total Price: ", this.currency + " " + NumberFormat.getInstance(Locale.getDefault()).format(this.calculated_total_price)});
        return rows;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() != android.R.id.home) {
            return super.onOptionsItemSelected(item);
        }
        finish();
        return true;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        OrderDetailsActivity orderDetailsActivity;
        Exception e;
        if (requestCode == REQUEST_CONNECT && resultCode == RESULT_OK) {
            try {
                try {
                    orderDetailsActivity = this;
                    try {
                        orderDetailsActivity.mPrnMng = printerFactory.createPrnMng(orderDetailsActivity, data.getExtras().getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS),
                                new TestPrinter(this, this.shop_name,
                                        this.shop_address,
                                        this.shop_email,
                                        this.shop_contact,
                                        this.order_id,
                                        this.order_date,
                                        this.order_time,
                                        this.shortText,
                                        this.longText,
                                        this.total_price,
                                        this.calculated_total_price,
                                        this.tax,
                                        this.discount,
                                        this.currency));
                    } catch (Exception e2) {
                        e = e2;
                    }
                } catch (Exception e3) {
                    e = e3;
                    orderDetailsActivity = this;
                    Toast.makeText(orderDetailsActivity, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            } catch (Exception e4) {
                e = e4;
                orderDetailsActivity = this;
                Toast.makeText(orderDetailsActivity, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    /*public void onDestroy() {
        WoosimPrnMng woosimPrnMng = this.mPrnMng;
        if (woosimPrnMng != null) {
            woosimPrnMng.releaseAllocatoins();
        }
        super.onDestroy();
    }*/

    @Override
    protected void onDestroy() {
        if (mPrnMng != null) mPrnMng.releaseAllocatoins();
        super.onDestroy();
    }
}