package com.ahmadabuhasan.skripsi.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/*
 * Created by Ahmad Abu Hasan on 01/02/2021
 */

//https://github.com/saeed-khalafinejad/Android-Thermal-Bluetooth-Print
public class ImageGallaryMng {

    public static Bitmap getBitmap(String filePath) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;
        return BitmapFactory.decodeFile(filePath, options);
    }
}