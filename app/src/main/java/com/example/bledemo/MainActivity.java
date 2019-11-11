package com.example.bledemo;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.example.bledemo.adapters.BluetoothDeviceListAdapter;
import com.example.bledemo.ble.BLEManager;
import com.example.bledemo.ble.BLEManagerCallerInterface;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.os.IBinder;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import java.io.Serializable;

public class MainActivity extends AppCompatActivity implements BLEManagerCallerInterface {

    public BLEManager bleManager;
    private MainActivity mainActivity;
    private Main2Activity activity2;
    public Main3Activity main3Activity;
    public Intent intentActivity2;
    public Intent intentActivity3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(bleManager!=null){
                    bleManager.scanDevices();
                }
            }
        });



        CheckIfBLEIsSupported();

        mainActivity=this;
        main3Activity = new Main3Activity();

        intentActivity3 = new Intent(this, Main3Activity.class);
        //intentActivity3.putExtra("Activities", new Activities(mainActivity, bleManager));
        //intentActivity3.putExtra("bleManager", bleManager);

        FloatingActionButton logBtn = findViewById(R.id.btn2);
        logBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openActivity2(intentActivity2);
            }
        });

        FloatingActionButton btn3 = findViewById(R.id.btn3);
        logBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog("BTN2 presionado");
            }
        });


    }

    /*##########################  Demas funciones      #############*/

    public void CheckIfBLEIsSupported(){
        if(!CheckIfBLEIsSupportedOrNot(this)){
            alertDialog("No se puede crear debido a que el dispositivo no soporta BLE");

        }else{


            permissions();

            bleManager=new BLEManager(this,this);
            if(!bleManager.isBluetoothOn()){
                bleManager.enableBluetoothDevice(this, 1001);
            }else{
                bleManager.requestLocationPermissions(this,1002);
            }
            alertDialog("BLE Funciona en este dispositivo");
        }
        Toast.makeText(getApplicationContext(),
                "Toast por defecto", Toast.LENGTH_SHORT);
    }

    public static boolean CheckIfBLEIsSupportedOrNot(Context context){
        try {
            if (!context.getPackageManager().
                    hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
                return false;
            }
            return true;
        }catch (Exception error){

        }
        return false;
    }

    public void alertDialog(String message){

        new AlertDialog.Builder(MainActivity.this)
                .setTitle("Free BLE Tools")
                .setMessage(""+message)
                .setCancelable(false)
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Whatever...
                    }
                }).show();
    }


    /*    ########        #### FIN #####        ######### */

    /*    ########        #### Permisos #####        ######### */
    public void permissions(){
        if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.BLUETOOTH_ADMIN)
                !=PackageManager.PERMISSION_GRANTED
            || ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH)
                !=PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(this,
                    new String[]{
                            Manifest.permission.
                                    BLUETOOTH_ADMIN,
                            Manifest.permission.
                                    BLUETOOTH},1005);

            return;
        }


    }

    /*    ########        #### Fin Permisos #####        ######### */


    public void openActivity2 (Intent intentActivity2){
        if (intentActivity2 == null){
            intentActivity2 = new Intent(this, Main2Activity.class);
        }
        startActivity(intentActivity2);
    }

    public void openActivity3 (Intent intentActivity3){
        if (intentActivity3 == null){
            intentActivity3 = new Intent(this, Main2Activity.class);
        }

        startActivity(intentActivity3);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        boolean allPermissionsGranted=true;
        if (requestCode == 1002) {
            for (int currentResult:grantResults
            ) {
                if(currentResult!= PackageManager.PERMISSION_GRANTED){
                    allPermissionsGranted=false;
                    break;
                }
            }
            if(!allPermissionsGranted){
                AlertDialog.Builder builder=new AlertDialog.Builder(this)
                        .setTitle("Permissions")
                        .setMessage("Camera and Location permissions must be granted in order to execute the app")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        });
                builder.show();
            }
        }
        if (requestCode == 1006 || requestCode == 1003) {
            for (int currentResult:grantResults
            ) {
                if(currentResult!= PackageManager.PERMISSION_GRANTED){
                    allPermissionsGranted=false;
                    break;
                }
            }
            if(!allPermissionsGranted){
                AlertDialog.Builder builder=new AlertDialog.Builder(this)
                        .setTitle("Permissions")
                        .setMessage("Camera and Location permissions must be granted in order to execute the app")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        });
                builder.show();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try{

            if(requestCode==1001){
                if(resultCode!=Activity.RESULT_OK){

                }else{
                    bleManager.requestLocationPermissions(this,1002);

                }
            }


        }catch (Exception error){

        }
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
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try{
                    ListView listView=(ListView)findViewById(R.id.devices_list_id);
                    BluetoothDeviceListAdapter adapter=new BluetoothDeviceListAdapter(getApplicationContext(),bleManager.scanResults,mainActivity);
                    listView.setAdapter(adapter);

                }catch (Exception error){

                }

            }
        });


    }
}
