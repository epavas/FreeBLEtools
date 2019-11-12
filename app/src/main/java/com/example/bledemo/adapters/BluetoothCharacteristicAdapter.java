package com.example.bledemo.adapters;

import android.bluetooth.BluetoothGattCharacteristic;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.bledemo.Main3Activity;
import com.example.bledemo.R;

import java.util.List;

public class BluetoothCharacteristicAdapter extends ArrayAdapter<BluetoothGattCharacteristic> {
    private final Context context;
    private Main3Activity main3Activity;
    private List<BluetoothGattCharacteristic> currentCharacteristics;

    public BluetoothCharacteristicAdapter(@NonNull Context context, Main3Activity main3Activity,
                                          List<BluetoothGattCharacteristic> currentCharacteristics) {
        super(context, R.layout.characteristic_list_item, currentCharacteristics);
        this.context = context;
        this.main3Activity = main3Activity;
        this.currentCharacteristics = currentCharacteristics;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = main3Activity.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.characteristic_list_item, null, true);

        BluetoothGattCharacteristic currentCharacteristic = currentCharacteristics.get(position);

        TextView txtCharac = (TextView)rowView.findViewById(R.id.characteristic_list_item_text_active);


        if (currentCharacteristic != null){
            txtCharac.setText("OkCharacteristic");
            String typeChar = "(";
            if(main3Activity.isCharacteristicReadable(currentCharacteristic)){
                typeChar += " R ";
            }
            if(main3Activity.isCharacteristicWriteable(currentCharacteristic)){
                typeChar += "/ W ";
            }
            if(main3Activity.isCharacteristicNotifiable(currentCharacteristic)){
                typeChar += "/ N ";
            }

            TextView txtChName = (TextView)rowView.findViewById(R.id.characteristic_list_item_text_name);
            txtChName.setText(typeChar+")");


        }
        return rowView;
    }
}
