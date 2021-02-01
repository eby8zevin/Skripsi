package com.ahmadabuhasan.skripsi.utils;

import android.content.Context;

/*
 * Created by Ahmad Abu Hasan on 01/02/2021
 */

//https://github.com/saeed-khalafinejad/Android-Thermal-Bluetooth-Print
public class printerFactory {

    public static WCharMapperCT42 getActiveCharMapper(Context context) {
        /*Pay attention that based on the Farsi/Arabic font that is installed on the device you may need to
          pick another mapper to print Farsi/Arabic correctly. For printing English, most of the fonts
          are consistent with mappers.*/

        if (PrefMng.getActivePrinter(context) == PrefMng.PRN_WOOSIM_SELECTED)
            return new WCharMapperCT42();

        if (PrefMng.getActivePrinter(context) == PrefMng.PRN_RONGTA_SELECTED)
            return new WCharMapperRongtaDef();

        //Bixolon printer mapper. The mapper also used for all other printers not listed above.
        return new WCharMapperBixolonDef();
    }

    public static WoosimPrnMng createPrnMng(Context c, String deviceAddr, IPrintToPrinter prnToWoosim) {
        if (PrefMng.getActivePrinter(c) == PrefMng.PRN_WOOSIM_SELECTED)
            return new WoosimPrnMng(c, deviceAddr, prnToWoosim);

        if (PrefMng.getActivePrinter(c) == PrefMng.PRN_RONGTA_SELECTED)
            return new RongtaPrnMng(c, deviceAddr, prnToWoosim);

        /*Bixolon printing methods can be used both for Bixolon printers
          and also for all other printers that are not listed above.*/
        return new BixolonPrnMng(c, deviceAddr, prnToWoosim);
    }

    public static printerWordMng createPaperMng(Context c) {
        if (PrefMng.getActivePrinter(c) == PrefMng.PRN_WOOSIM_SELECTED) {
            return new printerWordMng(IWoosimCanvasConst.CMaxChars_2Inch);
            //Base on your need you may change the paper size from 2 to 4 inches.
            //return new printerWordMng(IWoosimCanvasConst.CMaxChars_4Inch);
        }

        /*Bixolon printing methods can be used both for Bixolon printers
          and also for all other printers that are not listed above.*/
        return new printerWordMng(IBixolonCanvasConst.CMaxChars_2Inch);
    }
}