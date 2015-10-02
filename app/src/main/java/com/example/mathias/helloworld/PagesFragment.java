package com.example.mathias.helloworld;

import android.app.Fragment;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;

import java.util.Set;


public class PagesFragment extends Fragment {
    private ArrayAdapter<String> mAdapter;
    private AbsListView mListView;
    private static BluetoothAdapter mBtAdapter;
    private final BroadcastReceiver bReciever = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                mAdapter.add(device.getName() + "\n" + device.getAddress());
            }
        }
    };

    public PagesFragment(){
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBtAdapter = BluetoothAdapter.getDefaultAdapter();

        mAdapter = new ArrayAdapter<String>(getActivity(), R.layout.fragment_pages);
        Set<BluetoothDevice> pairedDevices = mBtAdapter.getBondedDevices();
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                mAdapter.add(device.getName() + "\n" + device.getAddress());
            }
        }
    }

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_pages, container, false);


        return rootView;
    }


}