package com.ahmadabuhasan.skripsi.adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.ahmadabuhasan.skripsi.expense.EditExpenseActivity;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import es.dmoral.toasty.Toasty;

/*
 * Created by Ahmad Abu Hasan on 30/01/2021
 */

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.MyViewHolder> {

    private final Context context;
    private final List<HashMap<String, String>> expenseData;

    public ExpenseAdapter(Context context1, List<HashMap<String, String>> expenseData1) {
        this.context = context1;
        this.expenseData = expenseData1;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.expense_item, parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final DatabaseAccess databaseAccess = DatabaseAccess.getInstance(this.context);
        final String expense_id = this.expenseData.get(position).get(DatabaseOpenHelper.EXPENSE_ID);

        databaseAccess.open();
        String currency = databaseAccess.getCurrency();

        holder.textView_Name.setText(this.expenseData.get(position).get(DatabaseOpenHelper.EXPENSE_NAME));
        Double amount = Double.parseDouble(this.expenseData.get(position).get(DatabaseOpenHelper.EXPENSE_AMOUNT));
        TextView textView = holder.textView_Amount;
        textView.setText(currency + " " + NumberFormat.getInstance(Locale.getDefault()).format(amount));

        TextView textView1 = holder.textView_DateTime;
        textView1.setText(this.expenseData.get(position).get(DatabaseOpenHelper.EXPENSE_DATE) + " " + this.expenseData.get(position).get(DatabaseOpenHelper.EXPENSE_TIME));

        TextView textView2 = holder.textView_Note;
        textView2.setText(this.context.getString(R.string.note) + " " + this.expenseData.get(position).get(DatabaseOpenHelper.EXPENSE_NOTE));

        holder.imageView_Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(ExpenseAdapter.this.context)
                        .setMessage(R.string.want_to_delete_expense)
                        .setCancelable(false)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                databaseAccess.open();
                                if (databaseAccess.deleteExpense(expense_id)) {
                                    Toasty.error(ExpenseAdapter.this.context, (int) R.string.expense_deleted, Toasty.LENGTH_SHORT).show();
                                    ExpenseAdapter.this.expenseData.remove(holder.getAdapterPosition());
                                    ExpenseAdapter.this.notifyItemRemoved(holder.getAdapterPosition());
                                } else {
                                    Toast.makeText(ExpenseAdapter.this.context, (int) R.string.failed, Toast.LENGTH_SHORT).show();
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
        return this.expenseData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textView_Name;
        TextView textView_Amount;
        TextView textView_DateTime;
        TextView textView_Note;

        ImageView imageView_Expense;
        ImageView imageView_Delete;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.textView_Name = itemView.findViewById(R.id.tv_expense_name);
            this.textView_Amount = itemView.findViewById(R.id.tv_expense_amount);
            this.textView_DateTime = itemView.findViewById(R.id.tv_expense_date_time);
            this.textView_Note = itemView.findViewById(R.id.tv_expense_note);
            this.imageView_Expense = itemView.findViewById(R.id.expense_image);
            this.imageView_Delete = itemView.findViewById(R.id.img_delete);
            itemView.setOnClickListener(this);
        }

        public void onClick(View view) {
            Intent i = new Intent(ExpenseAdapter.this.context, EditExpenseActivity.class);
            i.putExtra(DatabaseOpenHelper.EXPENSE_ID, (String) ((HashMap) ExpenseAdapter.this.expenseData.get(getAdapterPosition())).get(DatabaseOpenHelper.EXPENSE_ID));
            i.putExtra(DatabaseOpenHelper.EXPENSE_NAME, (String) ((HashMap) ExpenseAdapter.this.expenseData.get(getAdapterPosition())).get(DatabaseOpenHelper.EXPENSE_NAME));
            i.putExtra(DatabaseOpenHelper.EXPENSE_NOTE, (String) ((HashMap) ExpenseAdapter.this.expenseData.get(getAdapterPosition())).get(DatabaseOpenHelper.EXPENSE_NOTE));
            i.putExtra(DatabaseOpenHelper.EXPENSE_AMOUNT, (String) ((HashMap) ExpenseAdapter.this.expenseData.get(getAdapterPosition())).get(DatabaseOpenHelper.EXPENSE_AMOUNT));
            i.putExtra(DatabaseOpenHelper.EXPENSE_DATE, (String) ((HashMap) ExpenseAdapter.this.expenseData.get(getAdapterPosition())).get(DatabaseOpenHelper.EXPENSE_DATE));
            i.putExtra(DatabaseOpenHelper.EXPENSE_TIME, (String) ((HashMap) ExpenseAdapter.this.expenseData.get(getAdapterPosition())).get(DatabaseOpenHelper.EXPENSE_TIME));
            ExpenseAdapter.this.context.startActivity(i);
        }
    }
}