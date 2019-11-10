package com.example.bledemo.Services;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.util.ArrayList;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class LogEventos extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    public static final String ACTION_FOO = "com.example.bledemo.Services.action.FOO";
    public static final String ACTION_BAZ = "com.example.bledemo.Services.action.BAZ";
    public static final String ACTION_NEWMS = "com.example.bledemo.Services.action.NEWMS";
    public static final String ACTION_CONSULTAR = "com.example.bledemo.Services.action.CONSULTAR";

    // TODO: Rename parameters
    public static final String EXTRA_PARAM1 = "com.example.bledemo.Services.extra.PARAM1";
    public static final String EXTRA_PARAM2 = "com.example.bledemo.Services.extra.PARAM2";
    public static final String EXTRA_PARAMMS = "com.example.bledemo.Services.extra.PARAMMS";
    public static final String EXTRA_PARAMTIME = "com.example.bledemo.Services.extra.PARAMTIME";
    public static final String EXTRA_MESSAGESLOG = "com.example.bledemo.Services.extra.MESSAGESLOG";

    // TODO: Variables Globales
    private static String messagesLog;

    public LogEventos() {
        super("LogEventos");
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionFoo(Context context, String param1, String param2) {
        Intent intent = new Intent(context, LogEventos.class);
        intent.setAction(ACTION_FOO);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    /**
     * Starts this service to perform action Baz with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionBaz(Context context, String param1, String param2) {
        Intent intent = new Intent(context, LogEventos.class);
        intent.setAction(ACTION_BAZ);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    public static void startActionNewms(Context context, String paramMs, String paramTime) {
        Intent intent = new Intent(context, LogEventos.class);
        intent.setAction(ACTION_NEWMS);
        intent.putExtra(EXTRA_PARAMMS, paramMs);
        intent.putExtra(EXTRA_PARAMTIME, paramTime);
        context.startService(intent);
        messagesLog = "";

    }




    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_FOO.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
                handleActionFoo(param1, param2);
            } else if (ACTION_BAZ.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
                handleActionBaz(param1, param2);
            } else if (ACTION_NEWMS.equals(action)){
                final String paramMs = intent.getStringExtra(EXTRA_PARAMMS);
                final String paramTime = intent.getStringExtra(EXTRA_PARAMTIME);
                handleActionNewMs(paramMs,paramTime);
            }else if(ACTION_CONSULTAR.equals(action)) {
                consultar();
            }
        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionFoo(String param1, String param2) {
        // TODO: Handle action Foo
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    private void handleActionBaz(String param1, String param2) {
        // TODO: Handle action Baz
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    private void handleActionNewMs(String paramMs, String paramTime) {
        // TODO: Handle action NewMs
        String message = ""+paramMs+" :: ["+paramTime+"]";
        messagesLog += "\n"+message;
    }

    public void consultar(){
        Intent intentConsultar = new Intent(this, LogEventos.class);
        intentConsultar.setAction(ACTION_CONSULTAR);
        intentConsultar.putExtra("messagelog", messagesLog);
        //String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        sendBroadcast(intentConsultar);
    }
}
