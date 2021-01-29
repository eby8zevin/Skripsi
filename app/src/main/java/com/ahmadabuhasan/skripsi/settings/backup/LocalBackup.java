package com.ahmadabuhasan.skripsi.settings.backup;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Environment;
import android.text.InputType;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

import com.ahmadabuhasan.skripsi.R;
import com.ahmadabuhasan.skripsi.database.DatabaseOpenHelper;

import java.io.File;

/*
 * Created by Ahmad Abu Hasan on 30/01/2021
 */

public class LocalBackup {

    private BackupActivity activity;

    public LocalBackup(BackupActivity activity1) {
        this.activity = activity1;
    }

    //ask to the user a name for the backup and perform it. The backup will be saved to a custom folder.
    public void performBackup(final DatabaseOpenHelper db, final String outFileName) {

        Permissions.verifyStoragePermissions(activity);

        File folder = new File(Environment.getExternalStorageDirectory() + File.separator + "Skripsi/");

        boolean success = true;
        if (!folder.exists())
            success = folder.mkdirs();
        if (success) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this.activity);
            builder.setTitle("Backup");
            final EditText input = new EditText(this.activity);
            input.setInputType(InputType.TYPE_CLASS_TEXT);
            builder.setView(input);
            builder.setMessage(R.string.enter_local_database_backup_name)
                    .setCancelable(false)
                    .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String m_Text = input.getText().toString();
                            String out = outFileName + m_Text;
                            db.backup(out);
                            dialog.cancel();
                        }
                    })
                    .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    }).show();
        } else
            Toast.makeText(this.activity, (int) R.string.unable_to_create_directory_retry, Toast.LENGTH_SHORT).show();
    }

    //ask to the user what backup to restore
    public void performRestore(final DatabaseOpenHelper db) {

        Permissions.verifyStoragePermissions(activity);

        File folder = new File(Environment.getExternalStorageDirectory() + File.separator + "Skripsi/");
        if (folder.exists()) {

            final File[] files = folder.listFiles();

            final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this.activity, android.R.layout.select_dialog_item);
            for (File file : files)
                arrayAdapter.add(file.getName());

            AlertDialog.Builder builderSingle = new AlertDialog.Builder(this.activity);
            builderSingle.setTitle(R.string.database_restore);
            builderSingle.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    try {
                        db.importDB(files[which].getPath());
                    } catch (Exception e) {
                        Toast.makeText(activity, (int) R.string.unable_to_restore_retry, Toast.LENGTH_SHORT).show();
                    }
                }
            });
            builderSingle.show();
        } else
            Toast.makeText(this.activity, (int) R.string.backup_folder_not_present, Toast.LENGTH_SHORT).show();
    }

}
