package com.ahmadabuhasan.skripsi.utils;

import android.content.Context;

/*
 * Created by Ahmad Abu Hasan on 01/02/2021
 */

//https://github.com/saeed-khalafinejad/Android-Thermal-Bluetooth-Print
public class RongtaPrnMng extends BixolonPrnMng {

    public RongtaPrnMng(Context c, String deviceAddr, IPrintToPrinter prnToWoosim) {
        super(c, deviceAddr, prnToWoosim);
    }
}