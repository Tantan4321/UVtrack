package com.tantan4321.uvtracker;

import android.content.Context;
import android.content.Intent;

public class BootReceiver extends android.content.BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // Start the service on boot
        context.startService(new Intent(context, BluetoothService.class));
    }
}
