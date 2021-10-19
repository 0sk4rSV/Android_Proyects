package com.example.actionbar_navigationdrawer_osv.oovv;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        //Iniciar el servicio
        Intent background = new Intent(context, AlarmService.class);
        context.startService(background);
    }
}

