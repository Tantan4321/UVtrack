package com.tantan4321.uvtracker;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class ReaderFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "ReaderFragment";

    private TextView tvTime, tvIndex;

    // Fragments need an empty default contructor
    public ReaderFragment() { }

    public static Fragment newInstance() {
        Fragment fragment = new ReaderFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Thread t = new Thread() {

            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(1000);
                        updateUI();
                    }
                } catch (InterruptedException e) {
                }
            }
        };

        t.start();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.reader_fragment, container, false);

        Button data = rootView.findViewById(R.id.btn_data);

        tvTime = (TextView) rootView.findViewById(R.id.tv_hours);
        tvIndex = (TextView) rootView.findViewById(R.id.tv_index);

        data.setOnClickListener(this);
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
        FragmentManager fragmentManager = getFragmentManager();

        switch (v.getId()) {
            case R.id.btn_data:
                ((MainActivity)getActivity()).changeFragment(R.id.btn_data);
                break;
            default:
                break;
        }
    }

    public void updateUI(){
        Log.d(TAG, "updateUI: " + Utils.formatSeconds(DataStore.GetInstance().timer));
        tvTime.setText(Utils.formatSeconds(DataStore.GetInstance().timer));

        tvIndex.setText("" + DataStore.GetInstance().latest[2]);
    }


}