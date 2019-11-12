package com.example.bledemo.adapters;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.bledemo.MainActivity;
import com.example.bledemo.R;
import com.example.bledemo.ble.BLEManager;

import java.io.Serializable;
import java.util.List;



public class BluetoothDeviceListAdapter extends ArrayAdapter<ScanResult> {
    private final Context context;
    private MainActivity mainActivity;
    private List<ScanResult> scanResultList;

    public BluetoothDeviceListAdapter(@NonNull Context context, List<ScanResult> scanResultList, MainActivity mainActivity) {
        super(context, R.layout.device_list_item,scanResultList);
        this.context = context;
        this.mainActivity=mainActivity;
        this.scanResultList = scanResultList;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = mainActivity.getLayoutInflater();

        View rowView= inflater.inflate(R.layout.device_list_item, null, true);

        ScanResult scanDivice =  scanResultList.get(position);

        TextView txtTitle = (TextView) rowView.findViewById(R.id.device_list_item_text_view);
        txtTitle.setText(scanResultList.get(position).getDevice().getAddress()+"");

        TextView txtPosition = (TextView) rowView.findViewById(R.id.device_list_item_text_pos);
        txtPosition.setText(position+":");

        String deviceRssi = ""+scanDivice.getRssi();
        TextView txtRssi = (TextView) rowView.findViewById(R.id.device_list_item_text_Rssi);
        txtRssi.setText(deviceRssi);

        String deviceName=scanResultList.get(position).getDevice().getName();
        TextView deviceNameTxtView = (TextView) rowView.findViewById(R.id.device_list_item_text_view2);
        deviceNameTxtView.setText(deviceName);

        txtTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String address=((TextView) view.findViewById(R.id.device_list_item_text_view)).getText()+"";
                Toast.makeText(context,"clic corto: selected address: "+address,Toast.LENGTH_LONG).show();
                //mainActivity.bleManager.connectToGATTServer(mainActivity.bleManager.getByAddress(address));

                //mainActivity.bleManager.connectToGATTServer(mainActivity.bleManager.getByAddress(address));
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
                //mainActivity.bleManager.connectToGATTServer(mainActivity.bleManager.getByAddress(address));
                Intent intentActivity3 = mainActivity.intentActivity3;
                intentActivity3.putExtra("addres", address);


                mainActivity.openActivity3(intentActivity3);
                return true;
            }
        });

        return rowView;
    }
}