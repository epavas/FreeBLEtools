package com.example.bledemo.ble;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.bledemo.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class BLEManager extends ScanCallback  {
    BLEManagerCallerInterface caller;
    Context context;

    BluetoothManager bluetoothManager;
    private BluetoothAdapter bluetoothAdapter;
    private BluetoothLeScanner bluetoothLeScanner;
    public List<ScanResult> scanResults=new ArrayList<>();

    public BLEManager(BLEManagerCallerInterface caller, Context context) {
        this.caller = caller;
        this.context = context;
        initializeBluetoothManager();
    }



    public void initializeBluetoothManager(){
        try{
            bluetoothManager=(BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
            this.bluetoothAdapter=bluetoothManager.getAdapter();
        }catch (Exception error){

        }
    }

    public boolean isBluetoothOn(){
        try{
            return bluetoothManager.getAdapter().isEnabled();
        }catch (Exception error){

        }
        return false;
    }

    public void requestLocationPermissions(final Activity activity,int REQUEST_CODE){
        try{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                boolean gps_enabled = false;
                boolean network_enabled = false;

                LocationManager locationManager=(LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
                try {
                    gps_enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
                } catch(Exception ex) {}

                try {
                    network_enabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
                } catch(Exception ex) {}

                if(!((gps_enabled)||(network_enabled))){

                    AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                    builder.setMessage("In order to BLE connection be successful please proceed to enable the GPS")
                            .setTitle("Settings");

                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            Intent intent=new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            context.startActivity(intent);

                        }
                    });

                    builder.create().show();
                }
            }
            if (ContextCompat.checkSelfPermission(this.context.getApplicationContext(),
                    android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            } else {
                activity.requestPermissions( new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,
                                                            Manifest.permission.BLUETOOTH},
                        REQUEST_CODE);

            }
            if (ContextCompat.checkSelfPermission(this.context.getApplicationContext(),
                    Manifest.permission.BLUETOOTH) == PackageManager.PERMISSION_GRANTED){
            }else{
                activity.requestPermissions( new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.BLUETOOTH},
                        REQUEST_CODE+1);
            }
        }catch (Exception error){

        }

    }

    public void enableBluetoothDevice(Activity activity,int REQUEST_ENABLE_BT){
        try{
            if (ContextCompat.checkSelfPermission(this.context.getApplicationContext(),
                    Manifest.permission.BLUETOOTH) == PackageManager.PERMISSION_GRANTED){
            }else{
                activity.requestPermissions( new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.BLUETOOTH},
                        REQUEST_ENABLE_BT+5);
            }

            if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                activity.startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }
        }catch (Exception error){

        }
    }



    public void scanDevices(){
        try{
            scanResults.clear();
            bluetoothLeScanner=bluetoothAdapter.getBluetoothLeScanner();
            bluetoothLeScanner.startScan(this);
            caller.scanStartedSuccessfully();
        }catch (Exception error){

        }
    }

    @Override
    public void onScanResult(int callbackType, ScanResult result) {
        if(!isResultAlreadyAtList(result)) {
            result.getRssi();//Regresa la intensidad de la señal
            result.getDevice().getName();//Nombre del dispositivo
            scanResults.add(result);
        }
        caller.newDeviceDetected();

    }

    @Override
    public void onBatchScanResults(List<ScanResult> results) {

    }

    @Override
    public void onScanFailed(int errorCode) {
        caller.scanFailed(errorCode);
    }

    public boolean isResultAlreadyAtList(ScanResult newResult){
        for (ScanResult current : scanResults){
            if(current.getDevice().getAddress().equals(newResult.getDevice().getAddress())){
                return true;
            }
        }
        return false;
    }

    public BluetoothDevice getByAddress(String targetAddress){
        for(ScanResult current : scanResults){
            if(current!=null){
                if(current.getDevice().getAddress().equals(targetAddress)){
                    return current.getDevice();
                }
            }
        }
        return null;
    }



}
