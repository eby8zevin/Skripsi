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
import com.ahmadabuhasan.skripsi.database.DatabaseAccess;
import com.ahmadabuhasan.skripsi.database.DatabaseOpenHelper;
import com.ahmadabuhasan.skripsi.suppliers.EditSuppliersActivity;

import java.util.HashMap;
import java.util.List;

import es.dmoral.toasty.Toasty;

/*
 * Created by Ahmad Abu Hasan on 13/01/2021
 */

public class SupplierAdapter extends RecyclerView.Adapter<SupplierAdapter.MyViewHolder> {

    private Context context;
    private List<HashMap<String, String>> supplierData;

    public SupplierAdapter(Context context1, List<HashMap<String, String>> supplierData1) {
        this.context = context1;
        this.supplierData = supplierData1;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.supplier_item, parent, false));
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final String supplier_id = this.supplierData.get(position).get(DatabaseOpenHelper.SUPPLIER_ID);
        final String hp = this.supplierData.get(position).get(DatabaseOpenHelper.SUPPLIER_HP);

        holder.textView_Name.setText(this.supplierData.get(position).get(DatabaseOpenHelper.SUPPLIER_NAME));
        holder.textView_Address.setText(this.supplierData.get(position).get(DatabaseOpenHelper.SUPPLIER_ADDRESS));
        holder.textView_Contact.setText(this.supplierData.get(position).get(DatabaseOpenHelper.SUPPLIER_CONTACT));
        holder.textView_Hp.setText(hp);
        holder.textView_Account.setText(this.supplierData.get(position).get(DatabaseOpenHelper.SUPPLIER_ACCOUNT));

        holder.imageView_Call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:" + hp));
                SupplierAdapter.this.context.startActivity(callIntent);
            }
        });

        holder.imageView_Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(SupplierAdapter.this.context).setMessage(R.string.want_to_delete_supplier).setCancelable(false).setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(SupplierAdapter.this.context);
                        databaseAccess.open();
                        if (databaseAccess.deleteSupplier(supplier_id)) {
                            Toasty.error(SupplierAdapter.this.context, (int) R.string.supplier_deleted, Toasty.LENGTH_SHORT).show();
                            SupplierAdapter.this.supplierData.remove(holder.getAdapterPosition());
                            SupplierAdapter.this.notifyItemRemoved(holder.getAdapterPosition());
                        } else {
                            Toast.makeText(SupplierAdapter.this.context, (int) R.string.failed, Toast.LENGTH_SHORT).show();
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
        return this.supplierData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textView_Name;
        TextView textView_Address;
        TextView textView_Contact;
        TextView textView_Hp;
        TextView textView_Account;

        ImageView imageView_Call;
        ImageView imageView_Delete;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.textView_Name = itemView.findViewById(R.id.tv_supplier_name);
            this.textView_Address = itemView.findViewById(R.id.tv_supplier_address);
            this.textView_Contact = itemView.findViewById(R.id.tv_supplier_contact);
            this.textView_Hp = itemView.findViewById(R.id.tv_supplier_hp);
            this.textView_Account = itemView.findViewById(R.id.tv_supplier_account);
            this.imageView_Call = itemView.findViewById(R.id.img_call);
            this.imageView_Delete = itemView.findViewById(R.id.img_delete);
            itemView.setOnClickListener(this);
        }

        public void onClick(View view) {
            Intent i = new Intent(SupplierAdapter.this.context, EditSuppliersActivity.class);
            i.putExtra(DatabaseOpenHelper.SUPPLIER_ID, (String) ((HashMap) SupplierAdapter.this.supplierData.get(getAdapterPosition())).get(DatabaseOpenHelper.SUPPLIER_ID));
            i.putExtra(DatabaseOpenHelper.SUPPLIER_NAME, (String) ((HashMap) SupplierAdapter.this.supplierData.get(getAdapterPosition())).get(DatabaseOpenHelper.SUPPLIER_NAME));
            i.putExtra(DatabaseOpenHelper.SUPPLIER_ADDRESS, (String) ((HashMap) SupplierAdapter.this.supplierData.get(getAdapterPosition())).get(DatabaseOpenHelper.SUPPLIER_ADDRESS));
            i.putExtra(DatabaseOpenHelper.SUPPLIER_CONTACT, (String) ((HashMap) SupplierAdapter.this.supplierData.get(getAdapterPosition())).get(DatabaseOpenHelper.SUPPLIER_CONTACT));
            i.putExtra(DatabaseOpenHelper.SUPPLIER_FAX, (String) ((HashMap) SupplierAdapter.this.supplierData.get(getAdapterPosition())).get(DatabaseOpenHelper.SUPPLIER_FAX));
            i.putExtra(DatabaseOpenHelper.SUPPLIER_SALES, (String) ((HashMap) SupplierAdapter.this.supplierData.get(getAdapterPosition())).get(DatabaseOpenHelper.SUPPLIER_SALES));
            i.putExtra(DatabaseOpenHelper.SUPPLIER_HP, (String) ((HashMap) SupplierAdapter.this.supplierData.get(getAdapterPosition())).get(DatabaseOpenHelper.SUPPLIER_HP));
            i.putExtra(DatabaseOpenHelper.SUPPLIER_ACCOUNT, (String) ((HashMap) SupplierAdapter.this.supplierData.get(getAdapterPosition())).get(DatabaseOpenHelper.SUPPLIER_ACCOUNT));
            i.putExtra(DatabaseOpenHelper.SUPPLIER_INFORMATION, (String) ((HashMap) SupplierAdapter.this.supplierData.get(getAdapterPosition())).get(DatabaseOpenHelper.SUPPLIER_INFORMATION));
            i.putExtra(DatabaseOpenHelper.SUPPLIER_LAST_UPDATE, (String) ((HashMap) SupplierAdapter.this.supplierData.get(getAdapterPosition())).get(DatabaseOpenHelper.SUPPLIER_LAST_UPDATE));
            SupplierAdapter.this.context.startActivity(i);
        }
    }
}