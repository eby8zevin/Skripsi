package com.ahmadabuhasan.skripsi.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.MediaPlayer;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
 * Created by Ahmad Abu Hasan on 27/02/2021
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
        final double getStock = Double.parseDouble(Objects.requireNonNull(this.cart_product.get(position).get(DatabaseOpenHelper.CART_PRODUCT_STOCK)));

        databaseAccess.open();
        String totalQty = databaseAccess.getTotalQty(product_id);
        int a = Integer.parseInt(totalQty);
        databaseAccess.open();
        String discQty = databaseAccess.getDiscQty(product_id);
        int b = Integer.parseInt(discQty);

        databaseAccess.open();
        String weight_unit_name = databaseAccess.getWeightUnitName(weight_unit_id);

        databaseAccess.open();
        final String currency = databaseAccess.getCurrency();

        databaseAccess.open();
        total_price = databaseAccess.getTotalPrice();

        TextView textView = this.textView_total_price;
        textView.setText(this.context.getString(R.string.total_price) + " " + currency + " " + NumberFormat.getInstance(Locale.getDefault()).format(total_price));

        double parseDouble = Double.parseDouble(Objects.requireNonNull(price));
        double parseInt = Double.parseDouble(Objects.requireNonNull(qty));
        Double.isNaN(parseInt);
        double getPrice = parseInt * parseDouble;

        holder.textView_ItemName.setText(product_name);
        TextView textView1 = holder.textView_Weight;
        textView1.setText(weight + " " + weight_unit_name);
        TextView textView2 = holder.textView_Price;
        textView2.setText(currency + " " + NumberFormat.getInstance(Locale.getDefault()).format(getPrice));

        EditText qtyNumber = holder.QtyNumber;
        qtyNumber.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);
        qtyNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String sQty = s.toString();
                if (!sQty.isEmpty()) {
                    final double get_qty = Double.parseDouble(sQty);
                    if (get_qty > getStock) {
                        Context context = CartAdapter.this.context;
                        Toasty.error(context, CartAdapter.this.context.getString(R.string.available_stock) + " " + getStock, Toasty.LENGTH_SHORT).show();
                    } else if (get_qty >= a) {
                        double c = parseDouble - b;
                        final double cc = c * get_qty;
                        TextView tvPrice = holder.textView_Price;
                        tvPrice.setText(currency + " " + NumberFormat.getInstance(Locale.getDefault()).format(cc));

                        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(CartAdapter.this.context);
                        databaseAccess.open();
                        databaseAccess.updateProductQty(cart_id, "" + get_qty);

                        databaseAccess.open();
                        CartAdapter.total_price = cc;

                        TextView tvTotalPrice = CartAdapter.this.textView_total_price;
                        tvTotalPrice.setText(CartAdapter.this.context.getString(R.string.total_price) + " " + currency + " " + NumberFormat.getInstance(Locale.getDefault()).format(CartAdapter.total_price));
                    } else {
                        final double dd = parseDouble * get_qty;
                        TextView tvPrice1 = holder.textView_Price;
                        tvPrice1.setText(currency + " " + NumberFormat.getInstance(Locale.getDefault()).format(dd));

                        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(CartAdapter.this.context);
                        databaseAccess.open();
                        databaseAccess.updateProductQty(cart_id, "" + get_qty);

                        databaseAccess.open();
                        CartAdapter.total_price = dd;

                        TextView tvTotalPrice1 = CartAdapter.this.textView_total_price;
                        tvTotalPrice1.setText(CartAdapter.this.context.getString(R.string.total_price) + " " + currency + " " + NumberFormat.getInstance(Locale.getDefault()).format(CartAdapter.total_price));

                    }
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        holder.QtyNumber.setText(qty);

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
    }

    @Override
    public int getItemCount() {
        return this.cart_product.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imgProduct;
        TextView textView_ItemName;
        TextView textView_Weight;
        TextView textView_Price;
        ImageView imgDelete;
        EditText QtyNumber;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.imgProduct = itemView.findViewById(R.id.cart_product_image);
            this.textView_ItemName = itemView.findViewById(R.id.tv_item_name);
            this.textView_Weight = itemView.findViewById(R.id.tv_weight);
            this.textView_Price = itemView.findViewById(R.id.tv_price);
            this.imgDelete = itemView.findViewById(R.id.img_delete);
            this.QtyNumber = itemView.findViewById(R.id.tv_number);
        }
    }
}