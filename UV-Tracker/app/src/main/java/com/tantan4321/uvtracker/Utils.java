package com.tantan4321.uvtracker;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

/**
 * Class containing general utility methods used throughout project
 */
public class Utils {

    /**
     * Check if Bluetooth is available and enabled
     * @param adapter Bluetooth adapter object
     * @return
     */
    public static boolean verifyBluetooth(BluetoothAdapter adapter){
        if(adapter == null || !adapter.isEnabled()){
            return false;
        }else{
            return true;
        }
    }

    public static String hexToString(byte[] data) {
        StringBuilder sb = new StringBuilder(data.length);
        for(byte byteChar : data) {
            sb.append(String.format("%02X ", byteChar));
        }

        return sb.toString();
    }

    public static void toast(Context context, String str){
        Toast toast = Toast.makeText(context, str, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER | Gravity.BOTTOM, 0, 0);
        toast.show();
    }

    public static String formatSeconds(int timeInSeconds){
        int secondsLeft = timeInSeconds % 3600 % 60;
        int minutes = (int)Math.floor(timeInSeconds % 3600 / 60);
        int hours = (int)Math.floor(timeInSeconds / 3600);

        String HH = "" +hours;
        String MM = minutes < 10 ? "0" + minutes : "" + minutes;
        String SS = secondsLeft < 10 ? "0" + secondsLeft : "" + secondsLeft;

        return HH + ":" + MM + ":" + SS;
    }

}
