package com.ahmadabuhasan.skripsi.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.ahmadabuhasan.skripsi.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;

import es.dmoral.toasty.Toasty;

/*
 * Created by Ahmad Abu Hasan on 01/01/2021
 */

public class DatabaseOpenHelper extends SQLiteOpenHelper {

    private final Context mContext;

    //Database Version
    public static final int DATABASE_VERSION = 1;

    //Database Name
    public static final String DATABASE_NAME = "skripsi.db";

    public DatabaseOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void backup(String outFileName) {
        try {
            FileInputStream fis = new FileInputStream(new File(this.mContext.getDatabasePath(DATABASE_NAME).toString()));
            OutputStream output = new FileOutputStream(outFileName);
            byte[] buffer = new byte[1024];
            while (true) {
                int read = fis.read(buffer);
                int length = read;
                if (read > 0) {
                    output.write(buffer, 0, length);
                } else {
                    output.flush();
                    output.close();
                    fis.close();
                    Toasty.success(this.mContext, (CharSequence) this.mContext.getString(R.string.backup_completed_successfully), Toasty.LENGTH_SHORT).show();
                    return;
                }
            }
        } catch (Exception e) {
            Toasty.error(this.mContext, (int) R.string.unable_to_backup_database_retry, Toasty.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public void importDB(String inFileName) {
        String outFileName = this.mContext.getDatabasePath(DATABASE_NAME).toString();
        try {
            FileInputStream fis = new FileInputStream(new File(inFileName));
            OutputStream output = new FileOutputStream(outFileName);
            byte[] buffer = new byte[1024];
            while (true) {
                int read = fis.read(buffer);
                int length = read;
                if (read > 0) {
                    output.write(buffer, 0, length);
                } else {
                    output.flush();
                    output.close();
                    fis.close();
                    Toasty.success(this.mContext, (int) R.string.database_Import_completed, Toasty.LENGTH_SHORT).show();
                    return;
                }
            }
        } catch (Exception e) {
            Toasty.error(this.mContext, (int) R.string.unable_to_import_database_retry, Toasty.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
}
