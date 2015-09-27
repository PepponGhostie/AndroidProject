package com.example.mathias.helloworld;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Adapter;

/**
 * Created by martin Hansen on 27-09-2015.
 */
public class BluetoothActivity extends Activity {
    private Activity thisActivity = this;
    private BluetoothAdapter mBluetoothAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        onCreateBluetoothAdapter(mBluetoothAdapter);
    }

    private void onCreateBluetoothAdapter(BluetoothAdapter adapter) {
        if(adapter == null) {
            //Device does not support bluetooth activity
            return;
        }
        else if(adapter.isEnabled()){
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, 120);
            // Request users to enable bluetooth and bluetooth discoverability if not already enabled
        }
        else {
            Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 120);
            startActivity(discoverableIntent);
        }
    }
}
