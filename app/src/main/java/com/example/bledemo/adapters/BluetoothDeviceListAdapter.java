package com.example.bledemo.adapters;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.bledemo.R;

import java.util.List;



public class BluetoothDeviceListAdapter extends ArrayAdapter<ScanResult> {
    private final Activity context;
    private List<ScanResult> scanResultList;

    public BluetoothDeviceListAdapter(@NonNull Context context, List<ScanResult> scanResultList, Activity context1) {
        super(context, R.layout.device_list_item,scanResultList);
        this.context = context1;
        this.scanResultList = scanResultList;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context. getLayoutInflater();

        View rowView= inflater.inflate(R.layout.device_list_item, null, true);

        TextView txtTitle = (TextView) rowView.findViewById(R.id.device_list_item_text_view);
        txtTitle.setText(scanResultList.get(position).getDevice().getAddress()+"");

        return rowView;
    }
}