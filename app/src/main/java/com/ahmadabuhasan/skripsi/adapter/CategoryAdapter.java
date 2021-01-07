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
import com.ahmadabuhasan.skripsi.settings.categories.EditCategoryActivity;

import java.util.HashMap;
import java.util.List;

import es.dmoral.toasty.Toasty;

/*
 * Created by Ahmad Abu Hasan on 07/01/2021
 */

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyViewHolder> {

    private List<HashMap<String, String>> categoryData;
    private Context context;

    public CategoryAdapter(Context context1, List<HashMap<String, String>> categoryData1) {
        this.context = context1;
        this.categoryData = categoryData1;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item, parent, false));
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final String category_id = this.categoryData.get(position).get(DatabaseOpenHelper.CATEGORY_ID);
        holder.textView_CategoryName.setText(this.categoryData.get(position).get(DatabaseOpenHelper.CATEGORY_NAME));
        holder.imageView_Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(CategoryAdapter.this.context).setMessage(R.string.want_to_delete_category)
                        .setCancelable(false)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                DatabaseAccess databaseAccess = DatabaseAccess.getInstance(CategoryAdapter.this.context);
                                databaseAccess.open();
                                if (databaseAccess.deleteCategory(category_id)) {
                                    Toasty.success(CategoryAdapter.this.context, (int) R.string.category_deleted, Toasty.LENGTH_SHORT).show();
                                    CategoryAdapter.this.categoryData.remove(holder.getAdapterPosition());
                                    CategoryAdapter.this.notifyItemRemoved(holder.getAdapterPosition());
                                } else {
                                    Toasty.error(CategoryAdapter.this.context, (int) R.string.failed, Toasty.LENGTH_SHORT).show();
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
        return this.categoryData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textView_CategoryName;
        ImageView imageView_Delete;

        public MyViewHolder(View itemView) {
            super(itemView);
            textView_CategoryName = itemView.findViewById(R.id.tv_category_name);
            imageView_Delete = itemView.findViewById(R.id.img_delete);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Intent i = new Intent(CategoryAdapter.this.context, EditCategoryActivity.class);
            i.putExtra(DatabaseOpenHelper.CATEGORY_ID, (String) ((HashMap) CategoryAdapter.this.categoryData.get(getAdapterPosition())).get(DatabaseOpenHelper.CATEGORY_ID));
            i.putExtra(DatabaseOpenHelper.CATEGORY_NAME, (String) ((HashMap) CategoryAdapter.this.categoryData.get(getAdapterPosition())).get(DatabaseOpenHelper.CATEGORY_NAME));
            CategoryAdapter.this.context.startActivity(i);
        }
    }
}