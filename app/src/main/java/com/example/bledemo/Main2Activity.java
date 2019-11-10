package com.example.bledemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.bledemo.Services.LogEventos;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.Date;

import static com.example.bledemo.Services.LogEventos.startActionNewms;

public class Main2Activity extends AppCompatActivity {

    public EditText logEveEditT;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intentLogServ = new Intent(this, LogEventos.class);
        startService(intentLogServ);
        startActionNewms(this,"Inniciar servicio ActioNewMs", ""+new SimpleDateFormat("yyyy-MM-dd").format(new Date()));

        setContentView(R.layout.activity_main2);
        logEveEditT =  (EditText) findViewById(R.id.editText);
        Intent intentConsultar = new Intent(
                getApplicationContext(), LogEventos.class);

        FloatingActionButton btnlogEv = findViewById(R.id.btnlogEv);
        btnlogEv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerAction();
            }
        });

    }

    public void registerAction(){
        Intent intenRegisterAction =  new Intent(this, LogEventos.class);
        intenRegisterAction.setAction(LogEventos.ACTION_NEWMS);
        intenRegisterAction.putExtra(LogEventos.EXTRA_MESSAGESLOG, "Clic en btnlogEv");
        intenRegisterAction.putExtra(LogEventos.EXTRA_PARAMTIME, ""+new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        sendBroadcast(intenRegisterAction);
    }

    public class ResponseReceiver extends BroadcastReceiver {
        public LogEventos logEventos;
        public String messages;

        // Sin instancias
        private ResponseReceiver() {
        }


        @Override
        public void onReceive(Context context, Intent intent) {
            if (LogEventos.ACTION_CONSULTAR.equals(intent.getAction())) {
                logEveEditT.setText(intent.getStringExtra(LogEventos.ACTION_CONSULTAR));
            }
        }
    }

}
