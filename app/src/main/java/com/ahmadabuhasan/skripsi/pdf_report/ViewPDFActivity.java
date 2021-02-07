package com.ahmadabuhasan.skripsi.pdf_report;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.print.PageRange;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintDocumentInfo;
import android.print.PrintManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Toast;

import com.ahmadabuhasan.skripsi.R;
import com.github.barteksc.pdfviewer.PDFView;
import com.itextpdf.text.pdf.PdfObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/*
 * Created by Ahmad Abu Hasan on 07/02/2021
 */

public class ViewPDFActivity extends AppCompatActivity {

    private File file;
    private PDFView pdfView;
    private Context primaryAppCompatActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_p_d_f);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.order_receipt);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        this.pdfView = findViewById(R.id.pdfView);

        Bundle bundle = getIntent().getExtras();
        Log.d("location", bundle.toString());
        if (bundle != null) {
            this.file = new File(bundle.getString("path", ""));
        }
        this.pdfView.fromFile(this.file)
                .enableSwipe(true)
                .swipeHorizontal(false)
                .enableDoubletap(true)
                .enableAntialiasing(true)
                .load();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_view_pdf, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_share) {
            Toast.makeText(this, (int) R.string.share, Toast.LENGTH_SHORT).show();
            sharePdfFile();
            return true;
        }
        if (item.getItemId() == R.id.action_print) {
            printPDf();
        } else if (item.getItemId() == R.id.action_open_pdf) {
            Toast.makeText(this, (int) R.string.open_with_external_pdf_reader, Toast.LENGTH_SHORT).show();
            openWithExternalPdfApp();
        } else if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void sharePdfFile() {
        Uri uri = FileProvider.getUriForFile(this, getPackageName() + ".provider", this.file);
        Intent intent = new Intent("android.intent.action.SEND");
        intent.setDataAndType(uri, "application/pdf");
        intent.putExtra("android.intent.extra.STREAM", uri);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(intent);
    }

    public void openWithExternalPdfApp() {
        Uri uri = FileProvider.getUriForFile(this, getPackageName() + ".provider", this.file);
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setDataAndType(uri, "application/pdf");
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(intent);
    }

    public void printPDf() {
        if (Build.VERSION.SDK_INT >= 19) {
            PrintManager printManager = (PrintManager) this.primaryAppCompatActivity.getSystemService(PRINT_SERVICE);
            String jobName = getString(R.string.app_name) + "Order Receipt";
            PrintDocumentAdapter pda = new PrintDocumentAdapter() {
                public void onWrite(PageRange[] pages, ParcelFileDescriptor destination, CancellationSignal cancellationSignal, PrintDocumentAdapter.WriteResultCallback callback) {
                    InputStream input = null;
                    OutputStream output = null;
                    try {
                        File folder = new File(Environment.getExternalStorageDirectory().toString(), PdfObject.TEXT_PDFDOCENCODING);
                        if (!folder.exists()) {
                            folder.mkdir();
                        }
                        InputStream input1 = new FileInputStream(new File(folder, "order_receipt.pdf"));
                        OutputStream output1 = new FileOutputStream(destination.getFileDescriptor());
                        byte[] buf = new byte[1024];
                        while (true) {
                            int bytesRead = input1.read(buf);
                            if (bytesRead > 0) {
                                output1.write(buf, 0, bytesRead);
                            } else {
                                callback.onWriteFinished(new PageRange[]{PageRange.ALL_PAGES});
                                try {
                                    input1.close();
                                    output1.close();
                                    return;
                                } catch (IOException e) {
                                    e.printStackTrace();
                                    return;
                                }
                            }
                        }
                    } catch (Exception e2) {
                        if (0 != 0) {
                            try {
                                input.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        if (0 != 0) {
                            try {
                                output.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    } catch (Throwable th) {
                        if (0 != 0) {
                            try {
                                input.close();
                            } catch (IOException e3) {
                                e3.printStackTrace();
                                throw th;
                            }
                        }
                        if (0 != 0) {
                            try {
                                output.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        throw th;
                    }
                }

                public void onLayout(PrintAttributes oldAttributes, PrintAttributes newAttributes, CancellationSignal cancellationSignal, PrintDocumentAdapter.LayoutResultCallback callback, Bundle extras) {
                    if (cancellationSignal.isCanceled()) {
                        callback.onLayoutCancelled();
                    } else {
                        callback.onLayoutFinished(new PrintDocumentInfo.Builder("Name of file").setContentType(PrintDocumentInfo.CONTENT_TYPE_DOCUMENT).build(), true);
                    }
                }
            };
            if (printManager != null) {
                PrintAttributes.Builder builder = new PrintAttributes.Builder();
                builder.setMediaSize(PrintAttributes.MediaSize.PRC_6);
                printManager.print(jobName, pda, builder.build());
            }
        }
    }

    public void attachBaseContext(Context base) {
        this.primaryAppCompatActivity = base;
        super.attachBaseContext(base);
    }
}