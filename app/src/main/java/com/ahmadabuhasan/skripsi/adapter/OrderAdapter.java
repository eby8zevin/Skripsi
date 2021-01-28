package com.ahmadabuhasan.skripsi.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ahmadabuhasan.skripsi.R;
import com.ahmadabuhasan.skripsi.database.DatabaseAccess;
import com.ahmadabuhasan.skripsi.database.DatabaseOpenHelper;
import com.ahmadabuhasan.skripsi.print.OrderDetailsActivity;
import com.gitonway.lee.niftymodaldialogeffects.lib.Effectstype;
import com.gitonway.lee.niftymodaldialogeffects.lib.NiftyDialogBuilder;

import java.util.HashMap;
import java.util.List;

import es.dmoral.toasty.Toasty;

/*
 * Created by Ahmad Abu Hasan on 28/01/2021
 */

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.MyViewHolder> {

    Context context;
    private List<HashMap<String, String>> orderData;

    public OrderAdapter(Context context1, List<HashMap<String, String>> orderData1) {
        this.context = context1;
        this.orderData = orderData1;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.order_item, parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final String invoice_id = this.orderData.get(position).get(DatabaseOpenHelper.ORDER_LIST_INVOICE_ID);
        String orderStatus = this.orderData.get(position).get(DatabaseOpenHelper.ORDER_LIST_STATUS);

        holder.textView_CustomerName.setText(this.orderData.get(position).get(DatabaseOpenHelper.ORDER_LIST_CUSTOMER_NAME));
        TextView textView = holder.textView_OrderId;
        textView.setText(this.context.getString(R.string.order_id) + " " + invoice_id);

        TextView textView1 = holder.textView_PaymentMethod;
        textView1.setText(this.context.getString(R.string.payment_method) + " " + this.orderData.get(position).get(DatabaseOpenHelper.ORDER_LIST_PAYMENT_METHOD));

        TextView textView2 = holder.textView_OrderType;
        textView2.setText(this.context.getString(R.string.order_type) + " " + this.orderData.get(position).get(DatabaseOpenHelper.ORDER_LIST_TYPE));

        TextView textView3 = holder.textView_Date;
        textView3.setText(this.orderData.get(position).get(DatabaseOpenHelper.ORDER_LIST_TIME) + " " + this.orderData.get(position).get(DatabaseOpenHelper.ORDER_LIST_DATE));
        holder.textView_OrderStatus.setText(orderStatus);

        if (orderStatus.equals(DatabaseOpenHelper.COMPLETED)) {
            holder.textView_OrderStatus.setBackgroundColor(Color.parseColor("#43A047"));
            holder.textView_OrderStatus.setTextColor(-1);
            holder.imageView_Status.setVisibility(View.GONE);
        } else if (orderStatus.equals(DatabaseOpenHelper.CANCEL)) {
            holder.textView_OrderStatus.setBackgroundColor(Color.parseColor("#E53935"));
            holder.textView_OrderStatus.setTextColor(-1);
            holder.imageView_Status.setVisibility(View.GONE);
        }

        holder.imageView_Status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final NiftyDialogBuilder dialogBuilder = NiftyDialogBuilder.getInstance(OrderAdapter.this.context);
                dialogBuilder.withTitle(OrderAdapter.this.context.getString(R.string.change_order_status))
                        .withMessage(OrderAdapter.this.context.getString(R.string.please_change_order_status_to_complete_or_cancel))
                        .withEffect(Effectstype.Slidetop).withDialogColor("#01BAEF")
                        .withButton1Text(OrderAdapter.this.context.getString(R.string.order_completed))
                        .withButton2Text(OrderAdapter.this.context.getString(R.string.cancel_order))
                        .setButton1Click(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                DatabaseAccess databaseAccess = DatabaseAccess.getInstance(OrderAdapter.this.context);
                                databaseAccess.open();
                                if (databaseAccess.updateOrder(invoice_id, DatabaseOpenHelper.COMPLETED)) {
                                    Toasty.success(OrderAdapter.this.context, (int) R.string.order_updated, Toasty.LENGTH_SHORT).show();
                                    holder.textView_OrderStatus.setText(DatabaseOpenHelper.COMPLETED);
                                    holder.textView_OrderStatus.setBackgroundColor(Color.parseColor("#43A047"));
                                    holder.textView_OrderStatus.setTextColor(Color.WHITE);
                                    holder.imageView_Status.setVisibility(View.GONE);
                                } else {
                                    Toast.makeText(OrderAdapter.this.context, (int) R.string.failed, Toast.LENGTH_SHORT).show();
                                }
                                dialogBuilder.dismiss();
                            }
                        }).setButton2Click(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(OrderAdapter.this.context);
                        databaseAccess.open();
                        if (databaseAccess.updateOrder(invoice_id, DatabaseOpenHelper.CANCEL)) {
                            Toasty.error(OrderAdapter.this.context, (int) R.string.order_updated, Toasty.LENGTH_SHORT).show();
                            holder.textView_OrderStatus.setText(DatabaseOpenHelper.CANCEL);
                            holder.textView_OrderStatus.setBackgroundColor(Color.parseColor("#E53935"));
                            holder.textView_OrderStatus.setTextColor(Color.WHITE);
                            holder.imageView_Status.setVisibility(View.GONE);
                        } else {
                            Toast.makeText(OrderAdapter.this.context, (int) R.string.failed, Toast.LENGTH_SHORT).show();
                        }
                        dialogBuilder.dismiss();
                    }
                }).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.orderData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imageView_Status;
        TextView textView_CustomerName;
        TextView textView_OrderId;
        TextView textView_PaymentMethod;
        TextView textView_OrderType;
        TextView textView_Date;
        TextView textView_OrderStatus;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.imageView_Status = itemView.findViewById(R.id.img_order_status);
            this.textView_CustomerName = itemView.findViewById(R.id.tv_order_customer_name);
            this.textView_OrderId = itemView.findViewById(R.id.tv_order_id);
            this.textView_PaymentMethod = itemView.findViewById(R.id.tv_order_payment_method);
            this.textView_OrderType = itemView.findViewById(R.id.tv_order_type);
            this.textView_Date = itemView.findViewById(R.id.tv_order_date);
            this.textView_OrderStatus = itemView.findViewById(R.id.tv_order_status);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Intent i = new Intent(OrderAdapter.this.context, OrderDetailsActivity.class);
            i.putExtra(DatabaseOpenHelper.ORDER_LIST_ID, (String) ((HashMap) OrderAdapter.this.orderData.get(getAdapterPosition())).get(DatabaseOpenHelper.ORDER_LIST_INVOICE_ID));
            i.putExtra(DatabaseOpenHelper.ORDER_LIST_CUSTOMER_NAME, (String) ((HashMap) OrderAdapter.this.orderData.get(getAdapterPosition())).get(DatabaseOpenHelper.ORDER_LIST_CUSTOMER_NAME));
            i.putExtra(DatabaseOpenHelper.ORDER_LIST_DATE, (String) ((HashMap) OrderAdapter.this.orderData.get(getAdapterPosition())).get(DatabaseOpenHelper.ORDER_LIST_DATE));
            i.putExtra(DatabaseOpenHelper.ORDER_LIST_TIME, (String) ((HashMap) OrderAdapter.this.orderData.get(getAdapterPosition())).get(DatabaseOpenHelper.ORDER_LIST_TIME));
            i.putExtra(DatabaseOpenHelper.ORDER_LIST_TAX, (String) ((HashMap) OrderAdapter.this.orderData.get(getAdapterPosition())).get(DatabaseOpenHelper.ORDER_LIST_TAX));
            i.putExtra(DatabaseOpenHelper.ORDER_LIST_DISCOUNT, (String) ((HashMap) OrderAdapter.this.orderData.get(getAdapterPosition())).get(DatabaseOpenHelper.ORDER_LIST_DISCOUNT));
            OrderAdapter.this.context.startActivity(i);
        }
    }
}