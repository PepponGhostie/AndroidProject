package com.example.mathias.helloworld;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by martin Hansen on 27-09-2015.
 */
public class BluetoothActivity extends Activity implements DeviceList{
    private Activity thisActivity = this;
    private BluetoothAdapter mBthAdapter;
    public static int REQUEST_BLUETOOTH = 1;
    protected ArrayList<DeviceList> deviceItemList;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        BluetoothAdapter mBthAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBthAdapter == null) {
            new AlertDialog.Builder(this)
                    .setTitle("Not compatible")
                    .setMessage("Your phone does not support bluetooth")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which){
                    }})
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
        if (!mBthAdapter.isEnabled()) {
            Intent enableBT = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBT, REQUEST_BLUETOOTH);
        }
    }

    public void onCreateDeviceList(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d("DEVICELIST", "Super called for DeviceListFragment onCreate\n");
        deviceItemList = new ArrayList<DeviceItem>();

    }
}
