package com.example.bledemo.adapters;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.bledemo.Main3Activity;
import com.example.bledemo.R;


import java.util.List;

public class BluetoothServiceListAdapter extends ArrayAdapter<BluetoothGattService> {
    private final Context context;
    private Main3Activity main3Activity;
    private List<BluetoothGattService> bluetoothGattServices;

    public BluetoothServiceListAdapter(@NonNull Context context, Main3Activity main3Activity,
                                       List<BluetoothGattService> bluetoothGattServices) {
        super(context, R.layout.service_list_item, bluetoothGattServices);
        this.context = context;
        this.main3Activity = main3Activity;
        this.bluetoothGattServices = bluetoothGattServices;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = main3Activity.getLayoutInflater();

        View rowView = inflater.inflate(R.layout.service_list_item, null, true);

        BluetoothGattService bluetoothGattService = bluetoothGattServices.get(position);

        TextView txtName = (TextView) rowView.findViewById(R.id.service_list_item_name);
        TextView txtUUID = (TextView) rowView.findViewById(R.id.service_list_item_UUID);
        ListView lvChrts = (ListView) rowView.findViewById(R.id.service_list_item_characteristic_list_id);
        if(bluetoothGattService!=null) {

            txtName.setText(bluetoothGattService.getInstanceId() + " : "
                    + bluetoothGattService.describeContents() + " : "
                    + bluetoothGattService.getType() + " : ");

            txtUUID.setText(bluetoothGattService.getUuid() + ":");

            List<BluetoothGattCharacteristic> currentCharacteristics = bluetoothGattService.getCharacteristics();
            BluetoothCharacteristicAdapter adapterChrts = new BluetoothCharacteristicAdapter(context, main3Activity, currentCharacteristics);
            lvChrts.setAdapter(adapterChrts);

            return rowView;
        }
        txtName.setText( " null ");
        txtUUID.setText( "null");
        return rowView;
    }
}
