package com.ahmadabuhasan.skripsi.settings.backup;

import android.app.Activity;
import android.content.IntentSender;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.widget.Toast;

import com.ahmadabuhasan.skripsi.R;
import com.ahmadabuhasan.skripsi.database.DatabaseOpenHelper;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.drive.CreateFileActivityOptions;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveClient;
import com.google.android.gms.drive.DriveContents;
import com.google.android.gms.drive.DriveFile;
import com.google.android.gms.drive.DriveId;
import com.google.android.gms.drive.DriveResourceClient;
import com.google.android.gms.drive.MetadataChangeSet;
import com.google.android.gms.drive.OpenFileActivityOptions;
import com.google.android.gms.drive.query.Filters;
import com.google.android.gms.drive.query.SearchableField;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import es.dmoral.toasty.Toasty;

import static com.ahmadabuhasan.skripsi.settings.backup.BackupActivity.REQUEST_CODE_CREATION;
import static com.ahmadabuhasan.skripsi.settings.backup.BackupActivity.REQUEST_CODE_OPENING;
import static com.ahmadabuhasan.skripsi.settings.backup.BackupActivity.REQUEST_CODE_SIGN_IN;

/*
 * Created by Ahmad Abu Hasan on 31/01/2021
 */

public class RemoteBackup {

    /*private static final String TAG = "Google Drive Activity";
    private BackupActivity activity;
    private DriveClient mDriveClient;
    private DriveResourceClient mDriveResourceClient;
    public TaskCompletionSource<DriveId> mOpenItemTaskSource;

    public RemoteBackup(BackupActivity activity1) {
        this.activity = activity1;
    }

    public void connectToDrive(boolean backup) {
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this.activity);
        if (account == null) {
            signIn();
            return;
        }
        this.mDriveClient = Drive.getDriveClient((Activity) this.activity, account);
        this.mDriveResourceClient = Drive.getDriveResourceClient((Activity) this.activity, account);
        if (backup) {
            startDriveBackup();
        } else {
            startDriveRestore();
        }
    }

    private void signIn() {
        Log.i(TAG, "Start sign in");
        this.activity.startActivityForResult(buildGoogleSignInClient().getSignInIntent(), REQUEST_CODE_SIGN_IN);
    }

    private GoogleSignInClient buildGoogleSignInClient() {
        return GoogleSignIn.getClient((Activity) this.activity, new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestScopes(Drive.SCOPE_FILE, new Scope[0]).build());
    }

    private void startDriveBackup() {
        this.mDriveResourceClient.createContents().continueWithTask(new Continuation() {
            @Override
            public final Object then(Task task) {
                return RemoteBackup.this.startDriveBackup0RemoteBackup(task);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public final void onFailure(Exception exc) {
                RemoteBackup.this.startDriveBackup1RemoteBackup(exc);
            }
        });
    }

    public Task startDriveBackup0RemoteBackup(Task task) {
        return createFileIntentSender((DriveContents) task.getResult());
    }

    public void startDriveBackup1RemoteBackup(Exception e) {
        Log.w(TAG, this.activity.getString(R.string.failed_to_create_new_contents), e);
    }

    private Task<Void> createFileIntentSender(DriveContents driveContents) {
        try {
            FileInputStream fis = new FileInputStream(new File(this.activity.getDatabasePath(DatabaseOpenHelper.DATABASE_NAME).toString()));
            OutputStream outputStream = driveContents.getOutputStream();
            byte[] buffer = new byte[1024];
            while (true) {
                int length = fis.read(buffer);
                if (length <= 0) {
                    break;
                }
                outputStream.write(buffer, 0, length);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return this.mDriveClient.newCreateFileActivityIntentSender(new CreateFileActivityOptions.Builder()
                .setInitialMetadata(new MetadataChangeSet.Builder()
                        .setTitle("skripsi_backup.db")
                        .setMimeType("application/db").build())
                .setInitialDriveContents(driveContents).build()).continueWith(new Continuation() {
            @Override
            public final Object then(Task task) throws IntentSender.SendIntentException {
                activity.startIntentSenderForResult((IntentSender) task.getResult(), REQUEST_CODE_CREATION, null, 0, 0, 0);
                return null;
            }
        });
    }

    private void startDriveRestore() {
        pickFile().addOnSuccessListener(this.activity, new OnSuccessListener() {
            @Override
            public final void onSuccess(Object obj) {
                RemoteBackup.this.startDriveRestore3RemoteBackup((DriveId) obj);
            }
        }).addOnFailureListener(this.activity, new OnFailureListener() {
            @Override
            public final void onFailure(Exception exc) {
                RemoteBackup.this.startDriveRestore4RemoteBackup(exc);
            }
        });
    }

    public void startDriveRestore3RemoteBackup(DriveId driveId) {
        retrieveContents(driveId.asDriveFile());
    }

    public void startDriveRestore4RemoteBackup(Exception e) {
        Log.e(TAG, this.activity.getString(R.string.no_file_selected), e);
    }

    private void retrieveContents(DriveFile file) {
        final String FileName = activity.getDatabasePath(DatabaseOpenHelper.DATABASE_NAME).toString();
        this.mDriveResourceClient.openFile(file, DriveFile.MODE_READ_ONLY).continueWithTask(new Continuation() {
            @Override
            public final Object then(Task task) {
                return RemoteBackup.this.retrieveContents5RemoteBackup(FileName, task);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public final void onFailure(Exception exc) {
                RemoteBackup.this.retrieveContents6RemoteBackup(exc);
            }
        });
    }

    public Task retrieveContents5RemoteBackup(String inFileName, Task task) {
        DriveContents contents = (DriveContents) task.getResult();
        try {
            FileInputStream fileInputStream = new FileInputStream(contents.getParcelFileDescriptor().getFileDescriptor());
            OutputStream output = new FileOutputStream(inFileName);
            byte[] buffer = new byte[1024];
            while (true) {
                int length = fileInputStream.read(buffer);
                if (length <= 0) {
                    break;
                }
                output.write(buffer, 0, length);
            }
            output.flush();
            output.close();
            fileInputStream.close();
            Toasty.success(this.activity, (int) R.string.database_Import_completed, Toasty.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this.activity, (int) R.string.failed, Toast.LENGTH_SHORT).show();
        }
        return this.mDriveResourceClient.discardContents(contents);
    }

    public void retrieveContents6RemoteBackup(Exception e) {
        Log.e(TAG, this.activity.getString(R.string.unable_to_read_contents), e);
        Toast.makeText(this.activity, (int) R.string.error_on_import, Toast.LENGTH_SHORT).show();
    }

    private Task<DriveId> pickItem(OpenFileActivityOptions openOptions) {
        this.mOpenItemTaskSource = new TaskCompletionSource<>();
        this.mDriveClient.newOpenFileActivityIntentSender(openOptions).continueWith(new Continuation() {
            @Override
            public final Object then(Task task) throws IntentSender.SendIntentException {
                activity.startIntentSenderForResult((IntentSender) task.getResult(), REQUEST_CODE_OPENING, null, 0, 0, 0);
                return null;
            }
        });
        return this.mOpenItemTaskSource.getTask();
    }

    private Task<DriveId> pickFile() {
        return pickItem(new OpenFileActivityOptions.Builder()
                .setSelectionFilter(Filters.eq(SearchableField.MIME_TYPE, "application/db"))
                .setActivityTitle(this.activity.getString(R.string.select_database_file)).build());
    }*/


    //https://github.com/prof18/Database-Backup-Restore
    private static final String TAG = "Google Drive Activity";

    private DriveClient mDriveClient;
    private DriveResourceClient mDriveResourceClient;

    public TaskCompletionSource<DriveId> mOpenItemTaskSource;

    private BackupActivity activity;

    public RemoteBackup(BackupActivity activity1) {
        this.activity = activity1;
    }

    public void connectToDrive(boolean backup) {
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(activity);
        if (account == null) {
            signIn();
        } else {
            //Initialize the drive api
            mDriveClient = Drive.getDriveClient(activity, account);
            // Build a drive resource client.
            mDriveResourceClient = Drive.getDriveResourceClient(activity, account);
            if (backup)
                startDriveBackup();
            else
                startDriveRestore();
        }
    }

    private void signIn() {
        Log.i(TAG, "Start sign in");
        GoogleSignInClient GoogleSignInClient = buildGoogleSignInClient();
        activity.startActivityForResult(GoogleSignInClient.getSignInIntent(), REQUEST_CODE_SIGN_IN);
    }

    private GoogleSignInClient buildGoogleSignInClient() {
        GoogleSignInOptions signInOptions =
                new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestScopes(Drive.SCOPE_FILE)
                        .build();
        return GoogleSignIn.getClient(activity, signInOptions);
    }

    private void startDriveBackup() {
        mDriveResourceClient
                .createContents()
                .continueWithTask(
                        task -> createFileIntentSender(task.getResult()))
                .addOnFailureListener(
                        e -> Log.w(TAG, "Failed to create new contents.", e));
    }

    private Task<Void> createFileIntentSender(DriveContents driveContents) {

        final String inFileName = activity.getDatabasePath(DatabaseOpenHelper.DATABASE_NAME).toString();

        try {
            File dbFile = new File(inFileName);
            FileInputStream fis = new FileInputStream(dbFile);
            OutputStream outputStream = driveContents.getOutputStream();

            // Transfer bytes from the inputfile to the outputfile
            byte[] buffer = new byte[1024];
            int length;
            while ((length = fis.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        MetadataChangeSet metadataChangeSet = new MetadataChangeSet.Builder()
                .setTitle("database_backup.db")
                .setMimeType("application/db")
                .build();

        CreateFileActivityOptions createFileActivityOptions =
                new CreateFileActivityOptions.Builder()
                        .setInitialMetadata(metadataChangeSet)
                        .setInitialDriveContents(driveContents)
                        .build();

        return mDriveClient
                .newCreateFileActivityIntentSender(createFileActivityOptions)
                .continueWith(
                        task -> {
                            activity.startIntentSenderForResult(task.getResult(), REQUEST_CODE_CREATION, null, 0, 0, 0);
                            return null;
                        });
    }

    private void startDriveRestore() {
        pickFile()
                .addOnSuccessListener(activity,
                        driveId -> retrieveContents(driveId.asDriveFile()))
                .addOnFailureListener(activity, e -> {
                    Log.e(TAG, "No file selected", e);
                });
    }

    private void retrieveContents(DriveFile file) {

        //DB Path
        final String inFileName = activity.getDatabasePath(DatabaseOpenHelper.DATABASE_NAME).toString();

        Task<DriveContents> openFileTask = mDriveResourceClient.openFile(file, DriveFile.MODE_READ_ONLY);

        openFileTask
                .continueWithTask(task -> {
                    DriveContents contents = task.getResult();
                    try {
                        ParcelFileDescriptor parcelFileDescriptor = contents.getParcelFileDescriptor();
                        FileInputStream fileInputStream = new FileInputStream(parcelFileDescriptor.getFileDescriptor());

                        // Open the empty db as the output stream
                        OutputStream output = new FileOutputStream(inFileName);

                        // Transfer bytes from the inputfile to the outputfile
                        byte[] buffer = new byte[1024];
                        int length;
                        while ((length = fileInputStream.read(buffer)) > 0) {
                            output.write(buffer, 0, length);
                        }

                        // Close the streams
                        output.flush();
                        output.close();
                        fileInputStream.close();
                        Toast.makeText(activity, "Import completed", Toast.LENGTH_SHORT).show();

                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(activity, "Error on import", Toast.LENGTH_SHORT).show();
                    }
                    return mDriveResourceClient.discardContents(contents);

                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Unable to read contents", e);
                    Toast.makeText(activity, "Error on import", Toast.LENGTH_SHORT).show();
                });
    }

    private Task<DriveId> pickItem(OpenFileActivityOptions openOptions) {
        mOpenItemTaskSource = new TaskCompletionSource<>();
        mDriveClient
                .newOpenFileActivityIntentSender(openOptions)
                .continueWith((Continuation<IntentSender, Void>) task -> {
                    activity.startIntentSenderForResult(
                            task.getResult(), REQUEST_CODE_OPENING, null, 0, 0, 0);
                    return null;
                });
        return mOpenItemTaskSource.getTask();
    }

    private Task<DriveId> pickFile() {
        OpenFileActivityOptions openOptions =
                new OpenFileActivityOptions.Builder()
                        .setSelectionFilter(Filters.eq(SearchableField.MIME_TYPE, "application/db"))
                        .setActivityTitle("Select DB File")
                        .build();
        return pickItem(openOptions);
    }
}