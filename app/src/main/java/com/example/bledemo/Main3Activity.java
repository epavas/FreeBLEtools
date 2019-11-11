package com.example.bledemo;

import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;

import com.example.bledemo.ble.BLEManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.bledemo.R;

public class Main3Activity extends AppCompatActivity {

    public MainActivity mainActivity;
    public BLEManager bleManager;
    public BluetoothDevice bluetoothDevice;
    public String addres;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        Intent intent = getIntent();
        addres = intent.getStringExtra("addres");
        //mainActivity = (MainActivity) intent.getSerializableExtra("mainActivity");
        //bleManager = (BLEManager) intent.getSerializableExtra("mainActivity");

        TextView txtAddres = (TextView)findViewById(R.id.textAddres);
        txtAddres.setText(addres);

        //bluetoothDevice = mainActivity.bleManager.getByAddress(addres);


    }

}
