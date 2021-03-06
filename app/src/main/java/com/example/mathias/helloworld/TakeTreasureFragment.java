package com.example.mathias.helloworld;

import android.app.Fragment;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class TakeTreasureFragment extends Fragment {

    private ListView listView;
    private ArrayList<String> item;
    private ArrayAdapter<String> mArrayAdapter;
    private BroadcastReceiver mReceiver;

    public TakeTreasureFragment(){}

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_take_treasure, container, false);
        item = new ArrayList<>();

        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        bluetoothAdapter.startDiscovery();

        // Create a BroadcastReceiver for ACTION_FOUND
        mReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                // When discovery finds a device
                if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                    // Get the BluetoothDevice object from the Intent
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    // Add the name and address to an array adapter to show in a ListView
                    addUserByBluetooth(device.getAddress());
                    Log.d("Device",device.getAddress());

                }
            }
        };
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        getActivity().registerReceiver(mReceiver, filter);

        mArrayAdapter = new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_list_item_1, item);
        listView = (ListView) rootView.findViewById(R.id.listView);
        listView.setAdapter(mArrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView parent, View view, int position, long id) {
                String index = (String) parent.getItemAtPosition(position);
                String MACAdress = "default";
                for (int i = 0; i < index.length(); i++) {
                    if (index.charAt(i) == '(') {
                        MACAdress = index.substring(i + 1, index.length()-1);
                    }
                }
                Log.d("lol", MACAdress);
                takeTreasureByBluetooth(MACAdress);
            }
        });


            return rootView;
    }

    public Boolean addUserByBluetooth(final String address){

        String req_tag = "req_bluetooth";

        StringRequest req = new StringRequest(Request.Method.POST,
                AppConfig.URL_LOGIN,
                new Response.Listener<String>() {
                    @Override  //If succesfull, should move to next screen
                    public void onResponse(String response) {
                        Log.d("Bluetooth", "Bluetooth response: " + response);

                        try {
                            //Create JSONObject, easier to work with
                            JSONObject JResponse = new JSONObject(response);
                            boolean error = JResponse.getBoolean("error");
                            if (!error) {
                                String name = JResponse.getString("name");
                                int treasures = JResponse.getInt("treasure");
                                mArrayAdapter.add(name + "\nNr of treasures " + treasures + "\n(" + address + ")");
                            }
                            //Shows an error if we couldn't login and prints the error message from server
                            //TODO don't show the direct message but write a custom error message
                            else{
                                Log.e("Bluetooth", "Bluetooth error: " + response);

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override //If not succesfull, show user error message
            //only does it, if there's a network error, not login error
            public void onErrorResponse(VolleyError error) {
                Log.e("Bluetooth", "Bluetooth error: " + error.getMessage());
                Toast.makeText(TakeTreasureFragment.this.getActivity().getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG)
                        .show();
            }
        })  {
            @Override // Set all parameters for for server
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("tag", "getBluetoothName");
                params.put("bluetoothMAC", address);
                return params;
            }
        };

        //add tag to request
        req.addMarker(req_tag);
        //Adding request to request queue
        NetworkSingleton.getInstance(TakeTreasureFragment.this.getActivity().getApplicationContext()).addToRequestQueue(req);
        return true;
    }

    public Boolean takeTreasureByBluetooth(final String adress){

        String req_tag = "req_bluetooth";

        StringRequest req = new StringRequest(Request.Method.POST,
                AppConfig.URL_LOGIN,
                new Response.Listener<String>() {
                    @Override  //If succesfull, should move to next screen
                    public void onResponse(String response) {
                        Log.d("Bluetooth", "Bluetooth response: " + response);

                        try {
                            //Create JSONObject, easier to work with
                            JSONObject JResponse = new JSONObject(response);
                            boolean error = JResponse.getBoolean("error");
                            if (!error) {
                                MediaPlayer mediaPlayer = MediaPlayer.create(getActivity(),R.raw.getcoin);
                                mediaPlayer.start();
                                Context context = getActivity().getApplicationContext();
                                CharSequence text = "Congratz Matey! You have taken one of the treasures!";
                                int duration = Toast.LENGTH_SHORT;
                                Toast toast = Toast.makeText(context, text, duration);
                                toast.show();

                            }
                            //Shows an error if we couldn't login and prints the error message from server
                            //TODO don't show the direct message but write a custom error message
                            else{
                                Log.e("Bluetooth", "Bluetooth error: " + response);
                                Toast.makeText(TakeTreasureFragment.this.getActivity().getApplicationContext(), response, Toast.LENGTH_LONG)
                                        .show();
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override //If not succesfull, show user error message
            //only does it, if there's a network error, not login error
            public void onErrorResponse(VolleyError error) {
                Log.e("Bluetooth", "Bluetooth error: " + error.getMessage());
                Toast.makeText(TakeTreasureFragment.this.getActivity().getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG)
                        .show();
            }
        })  {
            @Override // Set all parameters for for server
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("tag", "takeTreasure");
                params.put("hapsedFrom", adress);
                params.put("hapser",UserStatic.getMACAdress());
                return params;
            }
        };

        //add tag to request
        req.addMarker(req_tag);
        //Adding request to request queue
        NetworkSingleton.getInstance(TakeTreasureFragment.this.getActivity().getApplicationContext()).addToRequestQueue(req);
        return true;
    }

    @Override
    public void onDestroyView(){
        super.onDestroyView();
        getActivity().unregisterReceiver(mReceiver);
    }
}
