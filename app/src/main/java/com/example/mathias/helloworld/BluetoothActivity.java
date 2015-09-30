package com.example.mathias.helloworld;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;

import java.util.Set;

/**
 * Created by martin Hansen on 27-09-2015.
 * Handles the initial connection to the bluetooth
 */
public class BluetoothActivity extends Activity {
    public static int REQUEST_BLUETOOTH_ENABLE = 1;
    private BluetoothAdapter mBtAdapter;
    private ArrayAdapter<String> mArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);

        //Create a bluetooth adapter to interface with the device's bluetooth
        mBtAdapter = BluetoothAdapter.getDefaultAdapter();
        //Creates a set to hold (if any) paired devices
        Set<BluetoothDevice> pairedDevices = mBtAdapter.getBondedDevices();
        //Creates a ArrayAdapter for holding and showing the paired devices (if any)
        mArrayAdapter = new ArrayAdapter<String>(this, R.layout.activity_bluetooth);

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
        } //Bluetooth is supported. Now the check to see if it is enabled
        else if(!mBtAdapter.isEnabled()) {
            Intent enableBt = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBt, REQUEST_BLUETOOTH_ENABLE);
        }

        //If there are any paired devices
        if(pairedDevices.size() > 0) {
            //Loop through the paired devices
            for(BluetoothDevice device : pairedDevices) {
                //Add the name and address to an array adapter to show in a ListView
                mArrayAdapter.add(device.getName() + "\n" + device.getAddress());
            }
            //Notify mArrayAdapter to update itself since it's content has changed
            mArrayAdapter.notifyDataSetChanged();
        }
    }

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            // When discovery finds a device
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Get the BluetoothDevice object from the Intent
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // Add the name and address to an array adapter to show in a ListView
                mArrayAdapter.add(device.getName() + "\n" + device.getAddress());
            }
        }
    };
}
