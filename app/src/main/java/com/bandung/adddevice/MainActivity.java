package com.bandung.adddevice;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

public class MainActivity extends ListActivity implements MainActivitya {
//    private Spinner spinner;
//    String [] type ={"Sensor","Actuator"};

    WifiManager mainWifiObj;
    WifiScanReceiver wifiReciever;
    CheckBox sensor, akuator;
    private WifiManager wifiManager;
    ListView list;
    String wifis[];
    EditText name, tipe;
    String user = "homeauto";
    String Pass = "homeauto12345!";
    String host = "167.205.7.226";
    String vhost = "/homeauto";
    ConnectionFactory factory = new ConnectionFactory();
    //    private IntentIntegrator intentIntegrator;
    String Mac_Addres, DeviceName, Device_Code;
    String IsianSpinner;


    private final int MY_PERMISSIONS_ACCESS_COARSE_LOCATION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        list = getListView();
        mainWifiObj = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        wifiReciever = new WifiScanReceiver();
        mainWifiObj.startScan();
        // listening to single list item on click
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // selected item
                String ssid = ((TextView) view).getText().toString();
                connectToWifi(ssid);
                Toast.makeText(MainActivity.this, "Wifi SSID : " + ssid, Toast.LENGTH_SHORT).show();

            }
        });


//
    //        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION)
    //                != PackageManager.PERMISSION_GRANTED) {
    //            ActivityCompat.requestPermissions(
    //                    MainActivity.this,
    //                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, MY_PERMISSIONS_ACCESS_COARSE_LOCATION
    //            );
    //        } else {
    //            wifiManager.startScan();
    //        }

    }


    protected void onPause() {
        unregisterReceiver(wifiReciever);
        super.onPause();
    }

    protected void onResume() {
        registerReceiver(wifiReciever, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        super.onResume();
    }

    class WifiScanReceiver extends BroadcastReceiver {
        @SuppressLint("UseValueOf")
        public void onReceive(Context c, Intent intent) {
            List<ScanResult> wifiScanList = mainWifiObj.getScanResults();
            wifis = new String[wifiScanList.size()];
            for (int i = 0; i < wifiScanList.size(); i++) {
                wifis[i] = ((wifiScanList.get(i)).toString());
            }
            String filtered[] = new String[wifiScanList.size()];
            int counter = 0;
            for (String eachWifi : wifis) {
                String[] temp = eachWifi.split(",");

                filtered[counter] = temp[0].substring(5).trim();//+"\n" + temp[2].substring(12).trim()+"\n" +temp[3].substring(6).trim();//0->SSID, 2->Key Management 3-> Strength

                counter++;

            }
            list.setAdapter(new ArrayAdapter<String>(getApplicationContext(), R.layout.list_item, R.id.label, filtered));
        }
    }

    private void finallyConnect(String networkPass, String networkSSID) {
        WifiConfiguration wifiConfig = new WifiConfiguration();
        wifiConfig.SSID = String.format("\"%s\"", networkSSID);
        wifiConfig.preSharedKey = String.format("\"%s\"", networkPass);

        // remember id
        int netId = mainWifiObj.addNetwork(wifiConfig);
        mainWifiObj.disconnect();
        mainWifiObj.enableNetwork(netId, true);
        mainWifiObj.reconnect();
        WifiConfiguration conf = new WifiConfiguration();
        conf.SSID = "\"\"" + networkSSID + "\"\"";
        conf.preSharedKey = "\"" + networkPass + "\"";
        mainWifiObj.addNetwork(conf);
    }

    private void connectToWifi(final String wifiSSID) {
        final Dialog dialog = new Dialog(this);

        dialog.setContentView(R.layout.connect);
        dialog.setTitle("Connect to Network");
        final TextView[] textSSID = {(TextView) dialog.findViewById(R.id.mac)};
        Button dialogButton = (Button) dialog.findViewById(R.id.okButton);
        name = (EditText) dialog.findViewById(R.id.name);
        tipe = (EditText) dialog.findViewById(R.id.tipe);
//        Device_Type = (Spinner) dialog.findViewById( R.id.device_type );
//        final EditText Device_Type=(EditText) dialog.findViewById(R.id.device_type);
        textSSID[0].setText(wifiSSID);



        // if button is clicked, connect to the network;
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setupConnectionFactory();
                JSONObject obj = new JSONObject();
                try {
                    obj.put("mac", textSSID[0].getText().toString());
                    obj.put("devicename",name.getText().toString());
                    obj.put("devicetype",tipe.getText().toString());
                    publish(obj.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                } catch (TimeoutException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (KeyManagementException e) {
                    e.printStackTrace();
                }

//                String checkPassword = pass.getText().toString();
//                finallyConnect(checkPassword, wifiSSID);
//                dialog.dismiss();
            }

        });

        dialog.show();
    }

    public void setupConnectionFactory() {
        try {
            factory.setAutomaticRecoveryEnabled(false);
            factory.setUri("amqp://" + user + ":" + Pass + "@" + host);
            factory.setVirtualHost(vhost);
        } catch (KeyManagementException | NoSuchAlgorithmException | URISyntaxException e1) {
            e1.printStackTrace();
        }
    }

    public  void  publish(String message) throws NoSuchAlgorithmException, KeyManagementException, URISyntaxException, IOException, TimeoutException, InterruptedException {
        String queue_name_publish ="registration_device";
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection connection = factory.newConnection();

        Log.d("ConnectionRMQ", "publish: "+connection.isOpen());
        Channel channel = connection.createChannel();
        Log.d("ChannelRMQ", "publish: "+channel.isOpen());
        if (channel.isOpen()== true) {
            Toast.makeText(this, "Berhasil", Toast.LENGTH_SHORT).show();
            Intent intent=new Intent(MainActivity.this,Menu_Utama.class);
            startActivity(intent);
            finish();
        }
        String messageOn = message ;
        channel.basicPublish("", queue_name_publish,null,messageOn.getBytes());
    }

    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}

