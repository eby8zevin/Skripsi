package com.ahmadabuhasan.skripsi.settings.backup;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.ahmadabuhasan.skripsi.R;
import com.ahmadabuhasan.skripsi.database.DatabaseOpenHelper;
import com.ajts.androidmads.library.SQLiteToExcel;
import com.google.android.gms.drive.DriveId;
import com.google.android.gms.drive.OpenFileActivityOptions;
import com.obsez.android.lib.filechooser.ChooserDialog;

import java.io.File;

import es.dmoral.toasty.Toasty;

/*
 * Created by Ahmad Abu Hasan on 30/01/2021
 */

public class BackupActivity extends AppCompatActivity {
    
    private static final String TAG = "Google Drive Activity";

    public static final int REQUEST_CODE_SIGN_IN = 0;
    public static final int REQUEST_CODE_OPENING = 1;
    public static final int REQUEST_CODE_CREATION = 2;
    public static final int REQUEST_CODE_PERMISSIONS = 2;

    //variable for decide if i need to do a backup or a restore.
    //True stands for backup, False for restore
    private boolean isBackup = true;

    private MainActivity activity;

    private LocalBackup localBackup;
    private RemoteBackup remoteBackup;

    CardView cardView_LocalBackUp;
    CardView cardView_LocalImport;
    CardView cardView_ExportToExcel;
    CardView cardView_BackupToDrive;
    CardView cardView_ImportFromDrive;
    ProgressDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backup);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.data_backup);

        this.cardView_LocalBackUp = findViewById(R.id.card_local_backup);
        this.cardView_LocalImport = findViewById(R.id.card_local_db_import);
        this.cardView_ExportToExcel = findViewById(R.id.card_export_to_excel);
        this.cardView_BackupToDrive = findViewById(R.id.card_backup_to_drive);
        this.cardView_ImportFromDrive = findViewById(R.id.card_import_from_drive);

        this.localBackup = new LocalBackup(this);
        this.remoteBackup = new RemoteBackup(this);
        final DatabaseOpenHelper db = new DatabaseOpenHelper(getApplicationContext());

        if (Build.VERSION.SDK_INT >= 23) {
            Permissions.verifyStoragePermissions(this);
        }

        this.cardView_LocalBackUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BackupActivity.this.localBackup.performBackup(db, Environment.getExternalStorageDirectory() + File.separator + "Skripsi/");
                //https://github.com/prof18/Database-Backup-Restore
                String outFileName = Environment.getExternalStorageDirectory() + File.separator + getResources().getString(R.string.app_name) + File.separator;
                localBackup.performBackup(db, outFileName);
            }
        });

        this.cardView_LocalImport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BackupActivity.this.localBackup.performRestore(new DatabaseOpenHelper(BackupActivity.this.getApplicationContext()));
                //https://github.com/prof18/Database-Backup-Restore
                localBackup.performRestore(db);
            }
        });

        this.cardView_ExportToExcel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BackupActivity.this.folderChooser();
            }
        });

        this.cardView_BackupToDrive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BackupActivity.this.isBackup = true;
                BackupActivity.this.remoteBackup.connectToDrive(BackupActivity.this.isBackup);
                //https://github.com/prof18/Database-Backup-Restore
                isBackup = true;
                remoteBackup.connectToDrive(isBackup);
            }
        });

        this.cardView_ImportFromDrive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BackupActivity.this.isBackup = false;
                BackupActivity.this.remoteBackup.connectToDrive(BackupActivity.this.isBackup);
                //https://github.com/prof18/Database-Backup-Restore
                isBackup = false;
                remoteBackup.connectToDrive(isBackup);
            }
        });
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() != android.R.id.home) {
            return super.onOptionsItemSelected(item);
        }
        finish();
        return true;
    }

    public void folderChooser() {
        new ChooserDialog((Activity) this).displayPath(true).withFilter(true, false, new String[0])
                .withChosenListener(new ChooserDialog.Result() {

                    @Override
                    public void onChoosePath(String path, File pathFile) {
                        BackupActivity.this.onExport(path);
                        Log.d("path", path);
                    }
                }).build().show();
    }

    public void onExport(String path) {
        String directory_path = path;
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        new SQLiteToExcel(getApplicationContext(), DatabaseOpenHelper.DATABASE_NAME, directory_path)
                .exportAllTables("Skripsi_AllData.xls", new SQLiteToExcel.ExportListener() {

                    @Override
                    public void onStart() {
                        BackupActivity.this.loading = new ProgressDialog(BackupActivity.this);
                        BackupActivity.this.loading.setMessage(BackupActivity.this.getString(R.string.data_exporting_please_wait));
                        BackupActivity.this.loading.setCancelable(false);
                        BackupActivity.this.loading.show();
                    }

                    @Override
                    public void onCompleted(String filePath) {
                        new Handler().postDelayed(new Runnable() {
                            public void run() {
                                BackupActivity.this.loading.dismiss();
                                Toasty.success(BackupActivity.this, (int) R.string.data_successfully_exported, Toasty.LENGTH_SHORT).show();
                            }
                        }, 5000);
                    }

                    @Override
                    public void onError(Exception e) {
                        BackupActivity.this.loading.dismiss();
                        Toasty.error(BackupActivity.this, (int) R.string.data_export_fail, Toasty.LENGTH_SHORT).show();
                    }
                });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            Log.i(TAG, "Sign in request code");
            if (resultCode == -1) {
                this.remoteBackup.connectToDrive(this.isBackup);
            }
        } else if (requestCode != 1) {
            if (requestCode == 2 && resultCode == -1) {
                Log.i(TAG, "Backup successfully saved.");
                Toasty.success(this, (int) R.string.backup_successfully_loaded, 0).show();
            }
        } else if (resultCode == -1) {
            this.remoteBackup.mOpenItemTaskSource.setResult((DriveId) data.getParcelableExtra("response_drive_id"));
        } else {
            this.remoteBackup.mOpenItemTaskSource.setException(new RuntimeException("Unable to open file"));
        }
    }
    
    https://github.com/prof18/Database-Backup-Restore
    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        switch (requestCode) {

            case REQUEST_CODE_SIGN_IN:
                Log.i(TAG, "Sign in request code");
                // Called after user is signed in.
                if (resultCode == RESULT_OK) {
                    remoteBackup.connectToDrive(isBackup);
                }
                break;

            case REQUEST_CODE_CREATION:
                // Called after a file is saved to Drive.
                if (resultCode == RESULT_OK) {
                    Log.i(TAG, "Backup successfully saved.");
                    Toast.makeText(this, "Backup successufly loaded!", Toast.LENGTH_SHORT).show();
                }
                break;

            case REQUEST_CODE_OPENING:
                if (resultCode == RESULT_OK) {
                    DriveId driveId = data.getParcelableExtra(
                            OpenFileActivityOptions.EXTRA_RESPONSE_DRIVE_ID);
                    remoteBackup.mOpenItemTaskSource.setResult(driveId);
                } else {
                    remoteBackup.mOpenItemTaskSource.setException(new RuntimeException("Unable to open file"));
                }
                
        }
    }


}
