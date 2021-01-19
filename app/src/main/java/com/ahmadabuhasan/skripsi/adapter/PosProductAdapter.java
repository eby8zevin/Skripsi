package com.ahmadabuhasan.skripsi.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.internal.view.SupportMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.ahmadabuhasan.skripsi.R;
import com.ahmadabuhasan.skripsi.data.EditProductActivity;
import com.ahmadabuhasan.skripsi.database.DatabaseAccess;
import com.ahmadabuhasan.skripsi.database.DatabaseOpenHelper;
import com.ahmadabuhasan.skripsi.kasir.PosActivity;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import es.dmoral.toasty.Toasty;

/*
 * Created by Ahmad Abu Hasan on 19/01/2021
 */

public class PosProductAdapter extends RecyclerView.Adapter<PosProductAdapter.MyViewHolder> {

    public static int count;
    private Context context;
    MediaPlayer player;
    private List<HashMap<String, String>> productData;

    public PosProductAdapter(Context context1, List<HashMap<String, String>> productData1) {
        this.context = context1;
        this.productData = productData1;
        this.player = MediaPlayer.create(context1, (int) R.raw.delete_sound);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.pos_product_item, parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final int getStock;
        final DatabaseAccess databaseAccess = DatabaseAccess.getInstance(this.context);

        databaseAccess.open();
        String currency = databaseAccess.getCurrency();

        final String product_id = this.productData.get(position).get(DatabaseOpenHelper.PRODUCT_ID);
        String name = this.productData.get(position).get(DatabaseOpenHelper.PRODUCT_NAME);
        final String product_weight = this.productData.get(position).get(DatabaseOpenHelper.PRODUCT_WEIGHT);
        final String weight_unit_id = this.productData.get(position).get(DatabaseOpenHelper.PRODUCT_WEIGHT_UNIT_ID);
        final String product_stock = this.productData.get(position).get(DatabaseOpenHelper.PRODUCT_STOCK);
        final String product_price = this.productData.get(position).get(DatabaseOpenHelper.PRODUCT_PRICE);

        databaseAccess.open();
        String weight_unit_name = databaseAccess.getWeightUnitName(weight_unit_id);

        int getStock1 = Integer.parseInt(product_stock);
        if (getStock1 > 5) {
            TextView textView = holder.textView_Stock;
            getStock = getStock1;
            textView.setText(this.context.getString(R.string.stock) + " : " + product_stock);
        } else {
            getStock = getStock1;
            TextView textView1 = holder.textView_Stock;
            textView1.setText(this.context.getString(R.string.stock) + " : " + product_stock);
            holder.textView_Stock.setTextColor(SupportMenu.CATEGORY_MASK);
        }

        Double price = Double.parseDouble(product_price);

        holder.textView_ProductName.setText(name);
        TextView textView2 = holder.textView_Weight;
        textView2.setText(product_weight + " " + weight_unit_name);
        TextView textView3 = holder.textView_Price;
        textView3.setText(currency + " " + NumberFormat.getInstance(Locale.getDefault()).format(price));

        holder.cardView_Product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PosProductAdapter.this.player.start();
                Intent intent = new Intent(PosProductAdapter.this.context, EditProductActivity.class);
                intent.putExtra(DatabaseOpenHelper.PRODUCT_ID, product_id);
                PosProductAdapter.this.context.startActivity(intent);
            }
        });

        holder.button_AddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getStock <= 0) {
                    Toasty.warning(PosProductAdapter.this.context, (int) R.string.stock_is_low_please_update_stock, Toasty.LENGTH_SHORT).show();
                    return;
                }
                Log.d("w_id", weight_unit_id);
                databaseAccess.open();
                int check = databaseAccess.addToCart(product_id, product_weight, weight_unit_id, product_price, 1, product_stock);
                databaseAccess.open();
                int count = databaseAccess.getCartItemCount();
                if (count == 0) {
                    PosActivity.textView_Count.setVisibility(View.INVISIBLE);
                } else {
                    PosActivity.textView_Count.setVisibility(View.VISIBLE);
                    PosActivity.textView_Count.setText(String.valueOf(count));
                }
                if (check == 1) {
                    Toasty.success(PosProductAdapter.this.context, (int) R.string.product_added_to_cart, Toasty.LENGTH_SHORT).show();
                    PosProductAdapter.this.player.start();
                } else if (check == 2) {
                    Toasty.info(PosProductAdapter.this.context, (int) R.string.product_already_added_to_cart, Toasty.LENGTH_SHORT).show();
                } else {
                    Toasty.error(PosProductAdapter.this.context, (int) R.string.product_added_to_cart_failed_try_again, Toasty.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return productData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        CardView cardView_Product;

        TextView textView_ProductName;
        TextView textView_Stock;
        TextView textView_Weight;
        TextView textView_Price;

        Button button_AddToCart;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.cardView_Product = itemView.findViewById(R.id.card_product);
            this.textView_ProductName = itemView.findViewById(R.id.tv_product_name);
            this.textView_Stock = itemView.findViewById(R.id.tv_stock);
            this.textView_Weight = itemView.findViewById(R.id.tv_weight);
            this.textView_Price = itemView.findViewById(R.id.tv_price);
            this.button_AddToCart = itemView.findViewById(R.id.btn_add_to_cart);
        }
    }
}
