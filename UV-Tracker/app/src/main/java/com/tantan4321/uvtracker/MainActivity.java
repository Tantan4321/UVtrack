package com.tantan4321.uvtracker;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.ParcelUuid;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.android.material.navigation.NavigationView;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        DeviceFragment.DeviceFragmentListener{
    private static final String TAG = "MainActivity";

    static MainActivity instance;

    private static final int REQUEST_ENABLE_BT = 1;

    private static final String TAG_FRAGMENT_READER = "reader";
    private static final String TAG_FRAGMENT_DEVICE = "device";
    private static final String TAG_FRAGMENT_PREFERENCES = "preferences";
    private static final String TAG_FRAGMENT_DATA = "data";

    private BluetoothAdapter mBtAdapter;
    private BluetoothService mBluetoothService;

    private int mConnectionState = BluetoothService.STATE_DISCONNECTED;

    //TODO: Transfer below to foreground service
    // Code to manage Service lifecycle.
    private final ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            mBluetoothService = ((BluetoothService.LocalBinder) service).getService();
            mBluetoothService.broadcastConnectionUpdate();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBluetoothService = null;
        }
    };

    // Handles various events fired by the Service.
    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (BluetoothService.ACTION_CONNECTION_STATE_CHANGED.equals(action)) {
                mConnectionState =
                        intent.getIntExtra(BluetoothService.EXTRA_CONNECTION_STATE, 0);

                onUpdateView();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Use this check to determine whether BLE is supported on the device.  Then you can
        // selectively disable BLE-related features.
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Utils.toast(getApplicationContext(), "BLE is not supported");
            finish();
        }

        // Using bluetooth manager, initialize the bluetooth adapter
        final BluetoothManager bluetoothManager =
                (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBtAdapter = bluetoothManager.getAdapter();

        // Checks if Bluetooth is supported on the device.
        if (mBtAdapter == null) {
            Toast.makeText(this, "Bluetooth not supported.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        setContentView(R.layout.activity_main);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        View refreshButton = findViewById(R.id.refresh_button);
        refreshButton.setOnClickListener(v -> onRefreshClicked(v));

        NavigationView mNavView = findViewById(R.id.nav_view);
        mNavView.setNavigationItemSelectedListener(this);

        //registerServiceReceiver();

        Intent gattServiceIntent = new Intent(this, BluetoothService.class);
        startService(gattServiceIntent);
        bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);

        if (savedInstanceState == null) {
            onNavigationItemSelected(mNavView.getMenu().getItem(0));
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (this.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("File write permission required");
                builder.setMessage("This app requires write permissions");
                builder.setPositiveButton(android.R.string.ok,
                        (dialog, which) -> requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0));
                builder.show();
                return;
            }
        }
        DataHandler.GetInstance();

        DataHandler.GetInstance().setContext(this);



    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!mBtAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
    }
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        changeFragment(id);

        item.setChecked(true);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onScanningStatusChange(boolean scanning) {
        View progress = findViewById(R.id.toolbar_progress_bar);
        View refresh = findViewById(R.id.refresh_button);
        if (scanning) {
            progress.setVisibility(View.VISIBLE);
            refresh.setVisibility(View.GONE);
        } else {
            progress.setVisibility(View.GONE);
            refresh.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onShowScanningStatus (boolean show) {
        View scanningStatus = findViewById(R.id.scanning_status);
        if (show) {
            scanningStatus.setVisibility(View.VISIBLE);
        } else {
            scanningStatus.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onDeviceSelected(BluetoothDevice device) {
        setDefaultDeviceAddress(device.getAddress());
        setDefaultDeviceName(device.getName());
    }

    @Override
    public void onUpdateView() {
        /*ReaderFragment doorControl = (ReaderFragment)
                getFragmentManager().findFragmentByTag(TAG_FRAGMENT_DOOR_CONTROL);
        if (doorControl != null) {
            doorControl.updateState(mConnectionState, mDoorState);
        }
        KeypadFragment keypad = (KeypadFragment)
                getFragmentManager().findFragmentByTag(TAG_FRAGMENT_KEYPAD);
        if (keypad != null) {
            keypad.updateState(mConnectionState, mDoorState);
        }
        */
        DeviceFragment deviceUI = (DeviceFragment)
                getFragmentManager().findFragmentByTag(TAG_FRAGMENT_DEVICE);
        if (deviceUI != null) {
            deviceUI.updateState(mConnectionState);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //TODO: test without killing service
        unbindService(mServiceConnection);
        mBluetoothService = null;
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mGattUpdateReceiver);
    }

    public void onRefreshClicked(View view) {
        DeviceFragment deviceFragment = (DeviceFragment)
                getFragmentManager().findFragmentByTag(TAG_FRAGMENT_DEVICE);
        if (deviceFragment != null) {
            deviceFragment.scanLeDevice(true);
        }
    }

    private String getDefaultDeviceAddress() {
        SharedPreferences prefs = getSharedPreferences(
                getPackageName(), Context.MODE_PRIVATE);
        return prefs.getString(BluetoothService.PREF_DEFAULT_DEVICE_ADDRESS, null);
    }

    private String getDefaultDeviceName() {
        SharedPreferences prefs = getSharedPreferences(
                getPackageName(), Context.MODE_PRIVATE);
        return prefs.getString(BluetoothService.PREF_DEFAULT_DEVICE_NAME, null);
    }

    private void setDefaultDeviceAddress(String address) {
        SharedPreferences prefs = getSharedPreferences(
                getPackageName(), Context.MODE_PRIVATE);
        prefs.edit().putString(BluetoothService.PREF_DEFAULT_DEVICE_ADDRESS, address).apply();
    }

    private void setDefaultDeviceName(String name) {
        SharedPreferences prefs = getSharedPreferences(
                getPackageName(), Context.MODE_PRIVATE);
        prefs.edit().putString(BluetoothService.PREF_DEFAULT_DEVICE_NAME, name).apply();
    }

    public void changeFragment(int id){
        FragmentManager fragmentManager = getFragmentManager();

        if (id == R.id.nav_reader) {
            setTitle(R.string.nav_label_uv_reader);
            Fragment fragment = ReaderFragment.newInstance();
            FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.replace(R.id.content_main, fragment, TAG_FRAGMENT_READER);
            ft.commit();
        }else if(id == R.id.nav_device) {
            setTitle(R.string.nav_label_bt_device);
            Fragment fragment = DeviceFragment.newInstance(
                    getDefaultDeviceAddress(),
                    getDefaultDeviceName(),
                    new ParcelUuid(BluetoothService.BLUNO_SERVICE_UUID));
            FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.replace(R.id.content_main, fragment, TAG_FRAGMENT_DEVICE);
            ft.commit();
        }else if(id == R.id.nav_preferences) {
            setTitle(R.string.nav_label_preferences);
            Fragment fragment = PreferencesFragment.newInstance();
            FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.replace(R.id.content_main, fragment, TAG_FRAGMENT_PREFERENCES);
            ft.commit();
        }
        else if(id == R.id.btn_data){
            setTitle(R.string.nav_label_uv_reader);
            Fragment fragment = DataFragment.newInstance();
            FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.replace(R.id.content_main, fragment, TAG_FRAGMENT_DATA);
            ft.commit();
        }
    }
}
