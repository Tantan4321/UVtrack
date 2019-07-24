package com.tantan4321.uvtracker;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.provider.ContactsContract;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class DataHandler{
    private static final String TAG = "DataHandler";

    private Context context;
    private static final String LOG_FILE = "data.txt";
    private static final String UV_FILE = "UV.txt";
    private static final String DIRECTORY_NAME = "uvLogs";

    private static DataHandler m_pInstance;
    public static DataHandler GetInstance() {
        if (m_pInstance == null) m_pInstance = new DataHandler();
        return m_pInstance;
    }

    public DataHandler(){
        File rootPath = new File(Environment.getExternalStorageDirectory(), DIRECTORY_NAME);
        if (!rootPath.exists()) {
            if(rootPath.mkdirs()) {
                Log.d("Data Handler","Directory create success :"+ rootPath.getAbsolutePath());
            }else {
                Log.d("Data Handler","FAILED TO CREATE DIRECTORY :"+ rootPath.getAbsolutePath());
            }
        }

        File logFile = new File(rootPath, LOG_FILE);
        try {
            logFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        File uvFile = new File(rootPath, UV_FILE);
        try {
            uvFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setContext(Context context){
        this.context = context;
    }

    public void writeToLogFile(String data) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(LOG_FILE, Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    // Read text from file
    public String readFromFile() {

        String ret = "";

        try {
            InputStream inputStream = context.openFileInput(LOG_FILE);

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        return ret;
    }
}
