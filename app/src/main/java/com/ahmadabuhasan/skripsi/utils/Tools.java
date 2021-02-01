package com.ahmadabuhasan.skripsi.utils;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.ahmadabuhasan.skripsi.R;

/*
 * Created by Ahmad Abu Hasan on 01/02/2021
 */

//https://github.com/saeed-khalafinejad/Android-Thermal-Bluetooth-Print
public class Tools {

    public static boolean isBlueToothOn(Context c) {
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            Toast.makeText(c, c.getString(R.string.bluetooth_not_available), Toast.LENGTH_LONG).show();
            return false;
        }
        if (!mBluetoothAdapter.isEnabled()) {
            Toast.makeText(c, c.getString(R.string.turnon_bluetooth), Toast.LENGTH_LONG).show();
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            c.startActivity(enableIntent);
            return false;
        }
        return true;
    }
}