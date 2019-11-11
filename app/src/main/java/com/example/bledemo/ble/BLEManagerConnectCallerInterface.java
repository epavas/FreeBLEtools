package com.example.bledemo.ble;

import android.bluetooth.BluetoothGatt;

public interface BLEManagerConnectCallerInterface {
    void onConnectionStateChange( BluetoothGatt gatt, int status, int newState);
    void onServicesDiscovered(BluetoothGatt gatt, int status);
}
