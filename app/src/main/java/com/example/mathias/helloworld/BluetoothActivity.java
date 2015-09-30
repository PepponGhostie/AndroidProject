package com.example.mathias.helloworld;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by martin Hansen on 27-09-2015.
 */
public class BluetoothActivity extends Activity {
    public static int REQUEST_BLUETOOTH_ENABLE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Create a bluetooth adapter to interface with the device's bluetooth
        BluetoothAdapter mBtAdapter = BluetoothAdapter.getDefaultAdapter();

        //Bluetooth is not supported if mBtAdapter is null after calling .getDefaultAdapter()
        if(mBtAdapter == null) {
            new AlertDialog.Builder(this)
                    .setTitle("Action is not supported")
                    .setMessage("Your phone does not support bluetooth")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .show();
        }
        else if(!mBtAdapter.isEnabled()) {
            Intent enableBt = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBt, REQUEST_BLUETOOTH_ENABLE);
        }
    }
}
