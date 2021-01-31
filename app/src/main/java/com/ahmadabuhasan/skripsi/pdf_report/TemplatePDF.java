package com.ahmadabuhasan.skripsi.pdf_report;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

import com.google.android.gms.drive.DriveFile;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfObject;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

/*
 * Created by Ahmad Abu Hasan on 31/01/2021
 */

public class TemplatePDF {

    PdfWriter pdfWriter;
    private Document document;
    private Paragraph paragraph;
    private File pdfFile;
    private Context context;

    private Font fTitle = new Font(Font.FontFamily.TIMES_ROMAN, 6.0f, 0, BaseColor.GRAY);
    private Font fSubTitle = new Font(Font.FontFamily.TIMES_ROMAN, 4.0f, 2, BaseColor.GRAY);
    private Font fText = new Font(Font.FontFamily.TIMES_ROMAN, 4.0f, 2, BaseColor.GRAY);
    private Font fHighText = new Font(Font.FontFamily.TIMES_ROMAN, 4.0f, 2, BaseColor.GRAY);
    private Font fRowText = new Font(Font.FontFamily.TIMES_ROMAN, 4.0f, 2, BaseColor.GRAY);

    public TemplatePDF(Context context1) {
        this.context = context1;
    }

    public void openDocument() {
        createFile();
        try {
            Document document1 = new Document(new Rectangle(164.41f, 500.41f));
            this.document = document1;
            this.pdfWriter = PdfWriter.getInstance(document1, new FileOutputStream(this.pdfFile));
            this.document.open();
        } catch (Exception e) {
            Log.e("createFile", e.toString());
        }
    }

    private void createFile() {
        File folder = new File(Environment.getExternalStorageDirectory().toString(), PdfObject.TEXT_PDFDOCENCODING);
        if (!folder.exists()) {
            folder.mkdir();
        }
        this.pdfFile = new File(folder, "order_receipt.pdf");
    }

    public void closeDocument() {
        this.document.close();
    }

    public void addMetaData(String title, String subject, String author) {
        this.document.addTitle(title);
        this.document.addSubject(subject);
        this.document.addAuthor(author);
    }

    public void addTitle(String title, String subTitle, String date) {
        try {
            this.paragraph = new Paragraph();
            addChildP(new Paragraph(title, this.fTitle));
            addChildP(new Paragraph(subTitle, this.fSubTitle));
            addChildP(new Paragraph("Order Date:" + date, this.fHighText));
            this.document.add(this.paragraph);
        } catch (Exception e) {
            Log.e("addTitle", e.toString());
        }
    }

    public void addImage(Bitmap bm) {
        try {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.JPEG, 50, stream);
            Image img = Image.getInstance(stream.toByteArray());
            img.setAlignment(Image.ALIGN_BOTTOM);
            img.setAlignment(Image.ALIGN_CENTER);
            img.scaleAbsolute(80.0f, 20.0f);
            this.document.add(img);
        } catch (Exception e) {
            Log.e("addParagraph", e.toString());
        }
    }

    public void addChildP(Paragraph childParagraph) {
        childParagraph.setAlignment(Element.ALIGN_CENTER);
        this.paragraph.add((Element) childParagraph);
    }

    public void addParagraph(String text) {
        try {
            Paragraph paragraph1 = new Paragraph(text, this.fText);
            this.paragraph = paragraph1;
            paragraph1.setAlignment(Element.ALIGN_CENTER);
            this.document.add(this.paragraph);
        } catch (Exception e) {
            Log.e("addParagraph", e.toString());
        }
    }

    public void addRightParagraph(String text) {
        try {
            Paragraph paragraph2 = new Paragraph(text, this.fText);
            this.paragraph = paragraph2;
            paragraph2.setSpacingAfter(5.0f);
            this.paragraph.setSpacingBefore(5.0f);
            this.paragraph.setAlignment(Element.ALIGN_CENTER);
            this.document.add(this.paragraph);
        } catch (Exception e) {
            Log.e("addParagraph", e.toString());
        }
    }

    public void createTable(String[] header, ArrayList<String[]> clients) {
        try {
            Paragraph paragraph3 = new Paragraph();
            this.paragraph = paragraph3;
            paragraph3.setFont(this.fText);
            PdfPTable pdfPTable = new PdfPTable(header.length);
            pdfPTable.setWidthPercentage(100.0f);
            pdfPTable.setSpacingBefore(5.0f);
            for (int indexC = 0; indexC < header.length; indexC++) {
                PdfPCell pdfPCell = new PdfPCell(new Phrase(header[indexC], this.fSubTitle));
                pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                pdfPCell.setBorderColor(BaseColor.GRAY);
                pdfPTable.addCell(pdfPCell);
            }
            for (int indexR = 0; indexR < clients.size(); indexR++) {
                String[] row = clients.get(indexR);
                for (int indexC1 = 0; indexC1 < header.length; indexC1++) {
                    PdfPCell pdfPCell1 = new PdfPCell(new Phrase(row[indexC1], this.fRowText));
                    pdfPCell1.setHorizontalAlignment(Element.ALIGN_CENTER);
                    pdfPCell1.setBorder(0);
                    pdfPTable.addCell(pdfPCell1);
                }
            }
            this.paragraph.add((Element) pdfPTable);
            this.document.add(this.paragraph);
        } catch (Exception e) {
            Log.e("createTable", e.toString());
        }
    }

    @SuppressLint("WrongConstant")
    public void viewPDF() {
        Intent intent = new Intent(this.context, ViewPDFActivity.class);
        intent.putExtra("path", this.pdfFile.getAbsolutePath());
        //intent.addFlags(DriveFile.MODE_READ_ONLY);
        intent.addFlags(DriveFile.MODE_READ_ONLY);
        this.context.startActivity(intent);
    }
}