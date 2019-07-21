package com.tantan4321.uvtracker;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ReaderFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "ReaderFragment";

    private int mConnectionState = BluetoothService.STATE_DISCONNECTED;
    private int mDoorState = BluetoothService.TRACKING_STATE_UNKNOWN;



    private static final String ARG_CONNECTION_STATE = "ARG_DEFAULT_DEVICE_ADDRESS";
    private static final String ARG_DOOR_STATE = "ARG_DEFAULT_DEVICE_ADDRESS";

    // Fragments need an empty default contructor
    public ReaderFragment() { }

    public static Fragment newInstance() {
        Fragment fragment = new ReaderFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.reader_fragment, container, false);


        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View v) {

    }


}