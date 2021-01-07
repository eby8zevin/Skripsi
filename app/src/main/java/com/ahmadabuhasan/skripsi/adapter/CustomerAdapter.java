package com.ahmadabuhasan.skripsi.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ahmadabuhasan.skripsi.R;
import com.ahmadabuhasan.skripsi.customers.EditCustomersActivity;
import com.ahmadabuhasan.skripsi.database.DatabaseAccess;
import com.ahmadabuhasan.skripsi.database.DatabaseOpenHelper;

import java.util.HashMap;
import java.util.List;

import es.dmoral.toasty.Toasty;

/*
 * Created by Ahmad Abu Hasan on 07/01/2021
 */

public class CustomerAdapter extends RecyclerView.Adapter<CustomerAdapter.MyViewHolder> {

    private Context context;
    private List<HashMap<String, String>> customerData;

    public CustomerAdapter(Context context1, List<HashMap<String, String>> customerData1) {
        this.context = context1;
        this.customerData = customerData1;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.customer_item, parent, false));
    }

    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final String customer_id = this.customerData.get(position).get(DatabaseOpenHelper.CUSTOMER_ID);
        final String cell = this.customerData.get(position).get(DatabaseOpenHelper.CUSTOMER_CALL);
        holder.txtCustomerName.setText(this.customerData.get(position).get(DatabaseOpenHelper.CUSTOMER_NAME));
        holder.txtCell.setText(cell);
        holder.txtEmail.setText(this.customerData.get(position).get(DatabaseOpenHelper.CUSTOMER_EMAIL));
        holder.txtAddress.setText(this.customerData.get(position).get(DatabaseOpenHelper.CUSTOMER_ADDRESS));
        holder.imgCall.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent callIntent = new Intent("android.intent.action.DIAL");
                callIntent.setData(Uri.parse("tel:" + cell));
                CustomerAdapter.this.context.startActivity(callIntent);
            }
        });
        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                new AlertDialog.Builder(CustomerAdapter.this.context).setMessage(R.string.want_to_delete_customer).setCancelable(false).setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(CustomerAdapter.this.context);
                        databaseAccess.open();
                        if (databaseAccess.deleteCustomer(customer_id)) {
                            Toasty.error(CustomerAdapter.this.context, (int) R.string.customer_deleted, Toasty.LENGTH_SHORT).show();
                            CustomerAdapter.this.customerData.remove(holder.getAdapterPosition());
                            CustomerAdapter.this.notifyItemRemoved(holder.getAdapterPosition());
                        } else {
                            Toast.makeText(CustomerAdapter.this.context, (int) R.string.failed, Toast.LENGTH_SHORT).show();
                        }
                        dialog.cancel();
                    }
                }).setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.customerData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imgCall;
        ImageView imgDelete;
        TextView txtAddress;
        TextView txtCell;
        TextView txtCustomerName;
        TextView txtEmail;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.txtCustomerName = (TextView) itemView.findViewById(R.id.txt_customer_name);
            this.txtCell = (TextView) itemView.findViewById(R.id.txt_cell);
            this.txtEmail = (TextView) itemView.findViewById(R.id.txt_email);
            this.txtAddress = (TextView) itemView.findViewById(R.id.txt_address);
            this.imgDelete = (ImageView) itemView.findViewById(R.id.img_delete);
            this.imgCall = (ImageView) itemView.findViewById(R.id.img_call);
            itemView.setOnClickListener(this);
        }

        public void onClick(View view) {
            Intent i = new Intent(CustomerAdapter.this.context, EditCustomersActivity.class);
            i.putExtra(DatabaseOpenHelper.CUSTOMER_ID, (String) ((HashMap) CustomerAdapter.this.customerData.get(getAdapterPosition())).get(DatabaseOpenHelper.CUSTOMER_ID));
            i.putExtra(DatabaseOpenHelper.CUSTOMER_NAME, (String) ((HashMap) CustomerAdapter.this.customerData.get(getAdapterPosition())).get(DatabaseOpenHelper.CUSTOMER_NAME));
            i.putExtra(DatabaseOpenHelper.CUSTOMER_CALL, (String) ((HashMap) CustomerAdapter.this.customerData.get(getAdapterPosition())).get(DatabaseOpenHelper.CUSTOMER_CALL));
            i.putExtra(DatabaseOpenHelper.CUSTOMER_EMAIL, (String) ((HashMap) CustomerAdapter.this.customerData.get(getAdapterPosition())).get(DatabaseOpenHelper.CUSTOMER_EMAIL));
            i.putExtra(DatabaseOpenHelper.CUSTOMER_ADDRESS, (String) ((HashMap) CustomerAdapter.this.customerData.get(getAdapterPosition())).get(DatabaseOpenHelper.CUSTOMER_ADDRESS));
            CustomerAdapter.this.context.startActivity(i);
        }
    }
}
