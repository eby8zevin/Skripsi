package com.ahmadabuhasan.skripsi.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ahmadabuhasan.skripsi.R;
import com.ahmadabuhasan.skripsi.database.DatabaseAccess;
import com.ahmadabuhasan.skripsi.database.DatabaseOpenHelper;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import es.dmoral.toasty.Toasty;

/*
 * Created by Ahmad Abu Hasan on 21/03/2021
 */

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.MyViewHolder> {

    public static Double total_price;
    private final Context context;
    private final List<HashMap<String, String>> cart_product;
    TextView textView_total_price;
    Button button_SubmitOrder;
    ImageView imgNoProduct;
    TextView textView_no_product;
    MediaPlayer mediaPlayer;

    public CartAdapter(Context context1, List<HashMap<String, String>> cart_product1, TextView textView_total_price1, Button button_SubmitOrder1, ImageView imgNoProduct1, TextView textView_no_product1) {
        this.context = context1;
        this.cart_product = cart_product1;
        this.textView_total_price = textView_total_price1;
        this.button_SubmitOrder = button_SubmitOrder1;
        this.imgNoProduct = imgNoProduct1;
        this.textView_no_product = textView_no_product1;
        this.mediaPlayer = MediaPlayer.create(context1, R.raw.delete_sound);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_product_items, parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(this.context);
        databaseAccess.open();
        final String cart_id = this.cart_product.get(position).get(DatabaseOpenHelper.PRODUCT_CART_ID);
        String product_id = this.cart_product.get(position).get(DatabaseOpenHelper.PRODUCT_ID);
        String product_name = databaseAccess.getProductName(product_id);
        String weight = this.cart_product.get(position).get(DatabaseOpenHelper.CART_PRODUCT_WEIGHT);
        String weight_unit_id = this.cart_product.get(position).get(DatabaseOpenHelper.CART_PRODUCT_WEIGHT_UNIT);
        final String price = this.cart_product.get(position).get(DatabaseOpenHelper.PRODUCT_PRICE);
        String qty = this.cart_product.get(position).get(DatabaseOpenHelper.CART_PRODUCT_QTY);
        final int getStock = Integer.parseInt(Objects.requireNonNull(this.cart_product.get(position).get(DatabaseOpenHelper.CART_PRODUCT_STOCK)));

        databaseAccess.open();
        String getTotalQty = databaseAccess.getTotalQty(product_id);
        int parseTotalQty = Integer.parseInt(getTotalQty);

        databaseAccess.open();
        String getDiscQty = databaseAccess.getDiscQty(product_id);
        int parseDiscQty = Integer.parseInt(getDiscQty);

        databaseAccess.open();
        String weight_unit_name = databaseAccess.getWeightUnitName(weight_unit_id);

        databaseAccess.open();
        final String currency = databaseAccess.getCurrency();

        databaseAccess.open();
        total_price = databaseAccess.getTotalPrice();

        double parsePrice = Double.parseDouble(price);
        double parseQty = Integer.parseInt(qty);
        Double.isNaN(parseQty);

        if (parseQty >= parseTotalQty) {
            double priceDisc = parsePrice - parseDiscQty;
            double getPrice = priceDisc * parseQty;

            TextView textView1 = holder.textView_Price;
            textView1.setText(currency + " " + NumberFormat.getInstance(Locale.getDefault()).format(getPrice));
        } else {
            double getPrice = parseQty * parsePrice;

            TextView textView1 = holder.textView_Price;
            textView1.setText(currency + " " + NumberFormat.getInstance(Locale.getDefault()).format(getPrice));
        }

        holder.textView_Name.setText(product_name);

        TextView textView = holder.textView_Weight;
        textView.setText(weight + " " + weight_unit_name);

        holder.textView_QtyNumber.setText(qty);

        TextView textView2 = this.textView_total_price;
        textView2.setText(this.context.getString(R.string.total_price) + " " + currency + " " + NumberFormat.getInstance(Locale.getDefault()).format(total_price));

        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseAccess databaseAccess = DatabaseAccess.getInstance(CartAdapter.this.context);
                databaseAccess.open();
                if (databaseAccess.deleteProductFromCart(cart_id)) {
                    Toasty.success(CartAdapter.this.context, CartAdapter.this.context.getString(R.string.product_removed_from_cart), Toasty.LENGTH_SHORT).show();

                    CartAdapter.this.mediaPlayer.start();
                    CartAdapter.this.cart_product.remove(holder.getAdapterPosition());
                    CartAdapter.this.notifyItemRemoved(holder.getAdapterPosition());

                    databaseAccess.open();
                    CartAdapter.total_price = databaseAccess.getTotalPrice();

                    TextView textView = CartAdapter.this.textView_total_price;
                    textView.setText(CartAdapter.this.context.getString(R.string.total_price) + " " + currency + " " + NumberFormat.getInstance(Locale.getDefault()).format(CartAdapter.total_price));
                } else {
                    Toasty.error(CartAdapter.this.context, CartAdapter.this.context.getString(R.string.failed), Toasty.LENGTH_SHORT).show();
                }

                databaseAccess.open();
                int itemCount = databaseAccess.getCartItemCount();
                Log.d("itemCount", "" + itemCount);
                if (itemCount <= 0) {
                    CartAdapter.this.textView_total_price.setVisibility(View.GONE);
                    CartAdapter.this.button_SubmitOrder.setVisibility(View.GONE);
                    CartAdapter.this.imgNoProduct.setVisibility(View.VISIBLE);
                    CartAdapter.this.textView_no_product.setVisibility(View.VISIBLE);
                }
            }
        });

        holder.textView_Minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int get_qty = Integer.parseInt(holder.textView_QtyNumber.getText().toString());
                int total_qty = parseTotalQty;
                if (get_qty >= 2) {
                    int get_qty1 = get_qty - 1;

                    if (get_qty >= total_qty) {
                        double parsePrice = Double.parseDouble(price);
                        Double.isNaN(get_qty1);

                        double priceDisc = parsePrice - parseDiscQty;
                        double cost1 = priceDisc * (double) get_qty1;

                        TextView textView = holder.textView_Price;
                        textView.setText(currency + " " + NumberFormat.getInstance(Locale.getDefault()).format(cost1));

                        TextView textView1 = holder.textView_QtyNumber;
                        textView1.setText("" + get_qty1);

                        DatabaseAccess databaseAccess1 = DatabaseAccess.getInstance(CartAdapter.this.context);
                        databaseAccess1.open();
                        databaseAccess1.updateProductQty(cart_id, "" + get_qty1);

                        CartAdapter.total_price = CartAdapter.total_price - priceDisc;
                        TextView textView2 = CartAdapter.this.textView_total_price;
                        textView2.setText(CartAdapter.this.context.getString(R.string.total_price) + " " + currency + " " + NumberFormat.getInstance(Locale.getDefault()).format(CartAdapter.total_price));
                    } else {
                        double cost1 = parsePrice * (double) get_qty1;

                        TextView textView = holder.textView_Price;
                        textView.setText(currency + " " + NumberFormat.getInstance(Locale.getDefault()).format(cost1));

                        TextView textView1 = holder.textView_QtyNumber;
                        textView1.setText("" + get_qty1);

                        DatabaseAccess databaseAccess2 = DatabaseAccess.getInstance(CartAdapter.this.context);
                        databaseAccess2.open();
                        databaseAccess2.updateProductQty(cart_id, "" + get_qty1);

                        double cost2 = cost1 - parsePrice;

                        CartAdapter.total_price = cost2 - Double.parseDouble(price);
                        TextView textView2 = CartAdapter.this.textView_total_price;
                        textView2.setText(CartAdapter.this.context.getString(R.string.total_price) + " " + currency + " " + NumberFormat.getInstance(Locale.getDefault()).format(CartAdapter.total_price));
                    }
                }
            }
        });

        holder.textView_Plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int get_qty = Integer.parseInt(holder.textView_QtyNumber.getText().toString());
                if (get_qty >= getStock) {
                    Context context = CartAdapter.this.context;
                    Toasty.error(context, CartAdapter.this.context.getString(R.string.available_stock) + " " + getStock, Toasty.LENGTH_SHORT).show();
                    return;
                }

                int total_qty = parseTotalQty - 1;

                if (get_qty >= total_qty) {
                    int get_qty1 = get_qty + 1;
                    double parsePrice = Double.parseDouble(price);
                    Double.isNaN(get_qty1);

                    double priceDisc = parsePrice - parseDiscQty;
                    double cost = priceDisc * (double) get_qty1;

                    TextView textView = holder.textView_Price;
                    textView.setText(currency + " " + NumberFormat.getInstance(Locale.getDefault()).format(cost));

                    TextView textView1 = holder.textView_QtyNumber;
                    textView1.setText("" + get_qty1);

                    DatabaseAccess databaseAccess = DatabaseAccess.getInstance(CartAdapter.this.context);
                    databaseAccess.open();
                    databaseAccess.updateProductQty(cart_id, "" + get_qty1);

                    double cost1 = cost - priceDisc;

                    CartAdapter.total_price = CartAdapter.total_price + priceDisc;
                    TextView textView2 = CartAdapter.this.textView_total_price;
                    textView2.setText(CartAdapter.this.context.getString(R.string.total_price) + " " + currency + " " + NumberFormat.getInstance(Locale.getDefault()).format(CartAdapter.total_price));
                } else {
                    int get_qty1 = get_qty + 1;
                    double parseDouble = Double.parseDouble(price);
                    Double.isNaN(get_qty1);

                    double cost = parseDouble * (double) get_qty1;

                    TextView textView = holder.textView_Price;
                    textView.setText(currency + " " + NumberFormat.getInstance(Locale.getDefault()).format(cost));

                    TextView textView1 = holder.textView_QtyNumber;
                    textView1.setText("" + get_qty1);

                    DatabaseAccess databaseAccess = DatabaseAccess.getInstance(CartAdapter.this.context);
                    databaseAccess.open();
                    databaseAccess.updateProductQty(cart_id, "" + get_qty1);

                    CartAdapter.total_price = CartAdapter.total_price + Double.parseDouble(price);
                    TextView textView2 = CartAdapter.this.textView_total_price;
                    textView2.setText(CartAdapter.this.context.getString(R.string.total_price) + " " + currency + " " + NumberFormat.getInstance(Locale.getDefault()).format(CartAdapter.total_price));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.cart_product.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imgProduct;
        TextView textView_Name;
        TextView textView_Weight;
        TextView textView_Price;
        ImageView imgDelete;
        TextView textView_Minus;
        TextView textView_QtyNumber;
        TextView textView_Plus;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.imgProduct = itemView.findViewById(R.id.cart_product_image);
            this.textView_Name = itemView.findViewById(R.id.tv_item_name);
            this.textView_Weight = itemView.findViewById(R.id.tv_weight);
            this.textView_Price = itemView.findViewById(R.id.tv_price);
            this.imgDelete = itemView.findViewById(R.id.img_delete);
            this.textView_Minus = itemView.findViewById(R.id.tv_minus);
            this.textView_QtyNumber = itemView.findViewById(R.id.tv_number);
            this.textView_Plus = itemView.findViewById(R.id.tv_plus);
        }
    }
}