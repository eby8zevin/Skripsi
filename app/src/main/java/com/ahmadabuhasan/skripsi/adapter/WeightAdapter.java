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
import com.ahmadabuhasan.skripsi.settings.weight_unit.EditWeightActivity;

import java.util.HashMap;
import java.util.List;

import es.dmoral.toasty.Toasty;

/*
 * Created by Ahmad Abu Hasan on 05/01/2021
 */

public class WeightAdapter extends RecyclerView.Adapter<WeightAdapter.MyViewHolder> {

    private List<HashMap<String, String>> weightData;
    private Context context;

    public WeightAdapter(Context context1, List<HashMap<String, String>> weightData1) {
        this.context = context1;
        this.weightData = weightData1;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.weight_item, parent, false));
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final String weight_id = this.weightData.get(position).get(DatabaseOpenHelper.WEIGHT_ID);
        holder.textView_WeightName.setText(this.weightData.get(position).get(DatabaseOpenHelper.WEIGHT_UNIT));
        holder.imageView_Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(WeightAdapter.this.context).setMessage(R.string.want_to_delete_weight)
                        .setCancelable(false)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                DatabaseAccess databaseAccess = DatabaseAccess.getInstance(WeightAdapter.this.context);
                                databaseAccess.open();
                                if (databaseAccess.deleteWeight(weight_id)) {
                                    Toasty.success(WeightAdapter.this.context, (int) R.string.weight_deleted, Toasty.LENGTH_SHORT).show();
                                    WeightAdapter.this.weightData.remove(holder.getAdapterPosition());
                                    WeightAdapter.this.notifyItemRemoved(holder.getAdapterPosition());
                                } else {
                                    Toasty.error(WeightAdapter.this.context, (int) R.string.failed, Toasty.LENGTH_SHORT).show();
                                }
                                dialog.cancel();
                            }
                        }).setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
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
        return this.weightData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textView_WeightName;
        ImageView imageView_Delete;

        public MyViewHolder(View itemView) {
            super(itemView);
            textView_WeightName = itemView.findViewById(R.id.tv_weight_name);
            imageView_Delete = itemView.findViewById(R.id.img_delete);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent i = new Intent(WeightAdapter.this.context, EditWeightActivity.class);
            i.putExtra(DatabaseOpenHelper.WEIGHT_ID, (String) ((HashMap) WeightAdapter.this.weightData.get(getAdapterPosition())).get(DatabaseOpenHelper.WEIGHT_ID));
            i.putExtra(DatabaseOpenHelper.WEIGHT_UNIT, (String) ((HashMap) WeightAdapter.this.weightData.get(getAdapterPosition())).get(DatabaseOpenHelper.WEIGHT_UNIT));
            WeightAdapter.this.context.startActivity(i);
        }
    }
}
