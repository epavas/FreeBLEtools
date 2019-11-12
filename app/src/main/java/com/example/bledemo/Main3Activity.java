package com.example.bledemo;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.example.bledemo.adapters.BluetoothDeviceListAdapter;
import com.example.bledemo.adapters.BluetoothServiceListAdapter;
import com.example.bledemo.ble.BLEManager;
import com.example.bledemo.ble.BLEManagerCallerInterface;
import com.example.bledemo.ble.BLEManagerConnect;
import com.example.bledemo.ble.BLEManagerConnectCallerInterface;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class Main3Activity extends AppCompatActivity implements BLEManagerCallerInterface, BLEManagerConnectCallerInterface {

    public MainActivity mainActivity;
    public BLEManager bleManager;
    public BLEManagerConnect bleManagerConnect;
    public BluetoothDevice bluetoothDevice;
    public String addres;
    public TextView txtAddres;
    public TextView txtNameDevice;
    public TextView txtConnect;
    public boolean connected;
    public Main3Activity main3Activity;
    public List<BluetoothGattService> lastBluetoothGattServices= new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab_activity3);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BluetoothDevice bleDevice = bleManager.getByAddress(addres);
                if(bleDevice != null){
                    if(!connected) {
                        bluetoothDevice = bleDevice;
                        txtNameDevice.setText(bluetoothDevice.getName());
                        bleManagerConnect.connectToGATTServer(bluetoothDevice);

                        Snackbar.make(view, "Conectando...", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }else{
                        Snackbar.make(view, "Ya se encuentra conectando", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
                }else{
                    Snackbar.make(view, "El dispositivo al que intenta conectarse no se encuentra", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
                Toast.makeText(getApplicationContext(),
                        "Toast por defecto", Toast.LENGTH_SHORT);


            }
        });

        Intent intent = getIntent();
        addres = intent.getStringExtra("addres");
        main3Activity = this;
        

        txtConnect = (TextView)findViewById(R.id.textConect);
        txtNameDevice = (TextView)findViewById(R.id.textNameDevice);
        txtAddres = (TextView)findViewById(R.id.textAddres);
        txtAddres.setText(addres);

        bleManager = new BLEManager(this, this);
        bleManager.scanDevices();

        bleManagerConnect = new BLEManagerConnect(this, this);





    }

    @Override
    public void scanStartedSuccessfully() {

    }

    @Override
    public void scanStoped() {

    }

    @Override
    public void scanFailed(int error) {

    }

    @Override
    public void newDeviceDetected() {
        BluetoothDevice bleDevice = bleManager.getByAddress(addres);
        if(bleDevice != null && !connected) {

            bluetoothDevice = bleDevice;
            txtNameDevice.setText(bluetoothDevice.getName());
            bleManagerConnect.connectToGATTServer(bluetoothDevice);

        }
    }

    //############ Connect methods

    @Override
    public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {

        if(newState==BluetoothGatt.STATE_CONNECTED){
            gatt.discoverServices();
            txtConnect.setText("(Conectado)");
            connected = true;

        }else{
            txtConnect.setText("(Desconectado)");
            connected = false;
        }

    }

    @Override
    public void onServicesDiscovered(BluetoothGatt lastBluetoothGatt, int status) {



        try {

            if(lastBluetoothGatt!=null){


                lastBluetoothGattServices.clear();
                lastBluetoothGattServices = lastBluetoothGatt.getServices();
                ArrayList<String> uIDServices  = new ArrayList<>();


                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            ListView listView=(ListView)findViewById(R.id.service_list_id);
                            BluetoothServiceListAdapter adapter=new BluetoothServiceListAdapter(getApplicationContext(), main3Activity, lastBluetoothGattServices);
                            listView.setAdapter(adapter);

                            TextView txtConection = (TextView)findViewById(R.id.conecction_item_txtServicios);
                            txtConection.setText("Servicios:");

                        }catch (Exception error){
                            Toast.makeText(getApplicationContext(),"Error "+error, Toast.LENGTH_LONG).show();
                        }

                    }
                });

                for(BluetoothGattService currentService: lastBluetoothGattServices){
                    if(currentService!=null){
                        uIDServices.add( currentService.getUuid()+"");
                        for(BluetoothGattCharacteristic currentCharacteristic:currentService.getCharacteristics()){
                            if(currentCharacteristic!=null){
                                if(isCharacteristicNotifiable(currentCharacteristic)){
                                    lastBluetoothGatt.setCharacteristicNotification(currentCharacteristic, true);
                                    for(BluetoothGattDescriptor currentDescriptor:currentCharacteristic.getDescriptors()){
                                        if(currentDescriptor!=null){
                                            try {
                                                currentDescriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                                                lastBluetoothGatt.writeDescriptor(currentDescriptor);
                                            }catch (Exception internalError){
                                                AlertDialog.Builder builder=new AlertDialog.Builder(this)
                                                        .setTitle("Bluetooth")
                                                        .setMessage("Internal Error")
                                                        //.setIcon()
                                                        .setCancelable(false)
                                                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int which) {
                                                                // Whatever...
                                                            }
                                                        });
                                                builder.show();
                                            }
                                        }
                                    }

                                }
                            }
                        }
                    }
                }

                /*String uIDS [] = new String[uIDServices.size()];
                for (int i =0; i < uIDServices.size(); i++){
                    uIDS[i] = uIDServices.get(i);
                }
                ArrayAdapter <String> adapterServices = new ArrayAdapter<String>(this, R.layout.list_item_uids, uIDS);
                ListView lvUIDS = (ListView) findViewById(R.id.service_list_id);
                lvUIDS.setAdapter(adapterServices);*/
            }
        } catch (Exception error){
            AlertDialog.Builder builder=new AlertDialog.Builder(this)
                    .setTitle("Bluetooth")
                    .setMessage("Internal Error")
                    //.setIcon()
                    .setCancelable(false)
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Whatever...
                        }
                    });
            builder.show();
        }
    }


    public boolean isCharacteristicWriteable(BluetoothGattCharacteristic characteristic) {
        return (characteristic.getProperties() &
                (BluetoothGattCharacteristic.PROPERTY_WRITE
                        | BluetoothGattCharacteristic.PROPERTY_WRITE_NO_RESPONSE)) != 0;
    }

    public boolean isCharacteristicReadable(BluetoothGattCharacteristic characteristic) {
        return ((characteristic.getProperties() & BluetoothGattCharacteristic.PROPERTY_READ) != 0);
    }

    public boolean isCharacteristicNotifiable(BluetoothGattCharacteristic characteristic) {
        return ((characteristic.getProperties() & BluetoothGattCharacteristic.PROPERTY_NOTIFY) != 0);
    }

/*
    public View getView(int position) {
        LayoutInflater inflater = this.getLayoutInflater();

        View rowView= inflater.inflate(R.layout.service_list_item, null, true);

        ScanResult scanDivice =  scanResultList.get(position);

        TextView txtTitle = (TextView) rowView.findViewById(R.id.device_list_item_text_view);
        txtTitle.setText(scanResultList.get(position).getDevice().getAddress()+"");

        TextView txtPosition = (TextView) rowView.findViewById(R.id.device_list_item_text_pos);
        txtPosition.setText(position+":");

        String deviceRssi = ""+scanDivice.getRssi();
        TextView txtRssi = (TextView) rowView.findViewById(R.id.device_list_item_text_Rssi);
        txtRssi.setText(deviceRssi);

        String deviceName=scanResultList.get(position).getDevice().getName()+" .:. "+scanDivice.getRssi();
        TextView deviceNameTxtView = (TextView) rowView.findViewById(R.id.device_list_item_text_view2);
        deviceNameTxtView.setText(deviceName);

        txtTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String address=((TextView) view.findViewById(R.id.device_list_item_text_view)).getText()+"";
                Toast.makeText(context,"clic corto: selected address: "+address,Toast.LENGTH_LONG).show();
                //mainActivity.bleManager.connectToGATTServer(mainActivity.bleManager.getByAddress(address));

                mainActivity.bleManager.connectToGATTServer(mainActivity.bleManager.getByAddress(address));
                Intent intentActivity3 = mainActivity.intentActivity3;
                intentActivity3.putExtra("addres", address);


                mainActivity.openActivity3(intentActivity3);

            }
        });
        txtTitle.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                String address=((TextView) view.findViewById(R.id.device_list_item_text_view)).getText()+"";
                Toast.makeText(context,"selected address: "+address,Toast.LENGTH_LONG).show();
                mainActivity.bleManager.connectToGATTServer(mainActivity.bleManager.getByAddress(address));
                Intent intentActivity3 = mainActivity.intentActivity3;
                intentActivity3.putExtra("addres", address);


                mainActivity.openActivity3(intentActivity3);
                return true;
            }
        });

        return rowView;
    }*/


}
