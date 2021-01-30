package com.ahmadabuhasan.skripsi.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ahmadabuhasan.skripsi.R;
import com.ahmadabuhasan.skripsi.database.DatabaseAccess;
import com.ahmadabuhasan.skripsi.database.DatabaseOpenHelper;
import com.ahmadabuhasan.skripsi.settings.payment_method.EditPaymentMethodActivity;

import java.util.HashMap;
import java.util.List;

import es.dmoral.toasty.Toasty;

/*
 * Created by Ahmad Abu Hasan on 30/01/2021
 */

public class PaymentMethodAdapter extends RecyclerView.Adapter<PaymentMethodAdapter.MyViewHolder> {

    private Context context;
    private List<HashMap<String, String>> paymentMethodData;

    public PaymentMethodAdapter(Context context1, List<HashMap<String, String>> paymentMethodData1) {
        this.context = context1;
        this.paymentMethodData = paymentMethodData1;
    }

    @NonNull
    @Override
    public PaymentMethodAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.payment_method_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final String payment_method_id = this.paymentMethodData.get(position).get(DatabaseOpenHelper.PAYMENT_METHOD_ID);
        holder.textView_PaymentMethod.setText(this.paymentMethodData.get(position).get(DatabaseOpenHelper.PAYMENT_METHOD_NAME));

        holder.imageView_Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(PaymentMethodAdapter.this.context)
                        .setMessage(R.string.want_to_delete)
                        .setCancelable(false)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                DatabaseAccess databaseAccess = DatabaseAccess.getInstance(PaymentMethodAdapter.this.context);
                                databaseAccess.open();
                                if (databaseAccess.deletePaymentMethod(payment_method_id)) {
                                    Toasty.success(PaymentMethodAdapter.this.context, (int) R.string.payment_method_deleted, Toasty.LENGTH_SHORT).show();
                                    PaymentMethodAdapter.this.paymentMethodData.remove(holder.getAdapterPosition());
                                    PaymentMethodAdapter.this.notifyItemRemoved(holder.getAdapterPosition());
                                } else {
                                    Toasty.error(PaymentMethodAdapter.this.context, (int) R.string.failed, Toasty.LENGTH_SHORT).show();
                                }
                                dialog.cancel();
                            }
                        })
                        .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        }).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.paymentMethodData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textView_PaymentMethod;
        ImageView imageView_Delete;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.textView_PaymentMethod = itemView.findViewById(R.id.tv_payment_method_name);
            this.imageView_Delete = itemView.findViewById(R.id.img_delete);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent i = new Intent(PaymentMethodAdapter.this.context, EditPaymentMethodActivity.class);
            i.putExtra(DatabaseOpenHelper.PAYMENT_METHOD_ID, (String) ((HashMap) PaymentMethodAdapter.this.paymentMethodData.get(getAdapterPosition())).get(DatabaseOpenHelper.PAYMENT_METHOD_ID));
            i.putExtra(DatabaseOpenHelper.PAYMENT_METHOD_NAME, (String) ((HashMap) PaymentMethodAdapter.this.paymentMethodData.get(getAdapterPosition())).get(DatabaseOpenHelper.PAYMENT_METHOD_NAME));
            PaymentMethodAdapter.this.context.startActivity(i);
        }
    }
}