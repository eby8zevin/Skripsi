package com.ahmadabuhasan.skripsi.print;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.Toast;

import com.ahmadabuhasan.skripsi.R;
import com.ahmadabuhasan.skripsi.database.DatabaseAccess;
import com.ahmadabuhasan.skripsi.database.DatabaseOpenHelper;
import com.ahmadabuhasan.skripsi.pdf_report.BarCodeEncoder;
import com.ahmadabuhasan.skripsi.utils.IPrintToPrinter;
import com.ahmadabuhasan.skripsi.utils.WoosimPrnMng;
import com.ahmadabuhasan.skripsi.utils.printerFactory;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.woosim.printer.WoosimCmd;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/*
 * Created by Ahmad Abu Hasan on 02/02/2021
 */

public class TestPrinter implements IPrintToPrinter {

    private Context context;

    //DecimalFormat decimalFormat = new DecimalFormat("#0.00");
    List<HashMap<String, String>> orderDetailsList;
    Bitmap bm;

    String shopName;
    String shopAddress;
    String shopEmail;
    String shopContact;
    String invoiceId;
    String orderDate;
    String orderTime;
    String customerName;
    String footer;
    double subTotal;
    double totalPrice;
    String tax;
    String discount;
    String currency;

    String name;
    String weight;
    String qty;
    String price;
    double cost_total;

    public TestPrinter(Context context1, String shopName1, String shopAddress1, String shopEmail1, String shopContact1,
                       String invoiceId1, String orderDate1, String orderTime1,
                       String customerName1, String footer1,
                       double subTotal1, double totalPrice1,
                       String tax1, String discount1, String currency1) {
        this.context = context1;
        this.shopName = shopName1;
        this.shopAddress = shopAddress1;
        this.shopEmail = shopEmail1;
        this.shopContact = shopContact1;
        this.invoiceId = invoiceId1;
        this.orderDate = orderDate1;
        this.orderTime = orderTime1;
        this.customerName = customerName1;
        this.footer = footer1;
        this.subTotal = subTotal1;
        this.totalPrice = totalPrice1;
        this.tax = tax1;
        this.discount = discount1;
        this.currency = currency1;

        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(context1);
        databaseAccess.open();
        this.orderDetailsList = databaseAccess.getOrderDetailsList(invoiceId1);
    }

    @Override
    public void printContent(WoosimPrnMng prnMng) {
        double getDiscount = Double.parseDouble(this.discount);
        double getTax = Double.parseDouble(this.tax);
        BarCodeEncoder qrCodeEncoder = new BarCodeEncoder();
        this.bm = null;
        try {
            this.bm = qrCodeEncoder.encodeAsBitmap(this.invoiceId, BarcodeFormat.CODE_128, 400, 48);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        printerFactory.createPaperMng(this.context);
        prnMng.printStr(this.shopName, 2, WoosimCmd.ALIGN_CENTER);
        prnMng.printStr(this.shopAddress, 1, WoosimCmd.ALIGN_CENTER);
        prnMng.printStr("Email: " + this.shopEmail, 1, WoosimCmd.ALIGN_CENTER);
        prnMng.printStr("Contact: " + this.shopContact, 1, WoosimCmd.ALIGN_CENTER);
        prnMng.printStr("Invoice ID: " + this.invoiceId, 1, WoosimCmd.ALIGN_CENTER);
        prnMng.printStr("Order Time: " + this.orderTime + " " + this.orderDate, 1, WoosimCmd.ALIGN_CENTER);
        prnMng.printStr(this.customerName, 1, WoosimCmd.ALIGN_CENTER);
        StringBuilder sb = new StringBuilder();
        sb.append("Email: ");
        sb.append(this.shopEmail);
        prnMng.printStr(sb.toString(), 1, WoosimCmd.ALIGN_CENTER);
        prnMng.printStr("--------------------------------");
        prnMng.printStr("  Items        Price  Qty  Total", 1, WoosimCmd.ALIGN_CENTER);
        prnMng.printStr("--------------------------------");
        for (int i = 0; i < this.orderDetailsList.size(); i++) {
            this.name = this.orderDetailsList.get(i).get(DatabaseOpenHelper.ORDER_DETAILS_PRODUCT_NAME);
            this.weight = this.orderDetailsList.get(i).get(DatabaseOpenHelper.ORDER_DETAILS_PRODUCT_WEIGHT);
            this.qty = this.orderDetailsList.get(i).get(DatabaseOpenHelper.ORDER_DETAILS_PRODUCT_QTY);
            this.price = this.orderDetailsList.get(i).get(DatabaseOpenHelper.ORDER_DETAILS_PRODUCT_PRICE);
            double parseInt = (double) Integer.parseInt(this.qty);
            double parseDouble = Double.parseDouble(this.price);
            Double.isNaN(parseInt);
            this.cost_total = parseInt * parseDouble;
            String trim = this.name.trim();
            prnMng.leftRightAlign(trim, " " + this.price + " x " + this.qty + " " + NumberFormat.getInstance(Locale.getDefault()).format(this.cost_total));
        }
        prnMng.printStr("--------------------------------");
        prnMng.printStr("Sub Total: " + this.currency + " " + NumberFormat.getInstance(Locale.getDefault()).format(this.subTotal), 1, WoosimCmd.ALIGN_RIGHT);
        prnMng.printStr("Total Tax (+): " + this.currency + " " + NumberFormat.getInstance(Locale.getDefault()).format(getTax), 1, WoosimCmd.ALIGN_RIGHT);
        prnMng.printStr("Discount (-): " + this.currency + " " + NumberFormat.getInstance(Locale.getDefault()).format(getDiscount), 1, WoosimCmd.ALIGN_RIGHT);
        prnMng.printStr("--------------------------------");
        prnMng.printStr("Total Price: " + this.currency + " " + NumberFormat.getInstance(Locale.getDefault()).format(this.totalPrice), 1, WoosimCmd.ALIGN_RIGHT);
        prnMng.printStr(this.footer, 1, WoosimCmd.ALIGN_CENTER);
        prnMng.printNewLine();
        prnMng.printPhoto(this.bm);
        prnMng.printNewLine();
        prnMng.printNewLine();
        printEnded(prnMng);

        /*printerWordMng wordMng = printerFactory.createPaperMng(context);
        prnMng.printStr("Header", 2, WoosimCmd.ALIGN_CENTER);
        prnMng.printStr("1-First line", 1, WoosimCmd.ALIGN_LEFT);
        prnMng.printStr(wordMng.getHorizontalUnderline());
        prnMng.printStr(wordMng.autoWordWrap("2-Second line that is very very long line to check word wrap"),
                1, WoosimCmd.ALIGN_LEFT);
        prnMng.printStr(wordMng.getHorizontalUnderline());
        prnMng.printStr("3-Third line", 1, WoosimCmd.ALIGN_LEFT);
        prnMng.printNewLine();
        prnMng.printStr("Footer", 1, WoosimCmd.ALIGN_CENTER);

        //You can also print a logo
        //prnMng.printBitmap("/sdcard/test/001.png", IBixolonCanvasConst.CMaxChars_2Inch);

        //Any finalization, you can call it here or any where in your running activity.
        printEnded(prnMng);*/
    }

    @Override
    public void printEnded(WoosimPrnMng prnMng) {
        //Do any finalization you like after print ended.
        if (prnMng.printSucc()) {
            Toast.makeText(context, R.string.print_succ, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(context, R.string.print_error, Toast.LENGTH_LONG).show();
        }
    }
}