package com.tantan4321.uvtracker;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;

public class ReaderFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "ReaderFragment";

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
        FragmentManager fragmentManager = getFragmentManager();

        switch (v.getId()) {
            case R.id.btn_data:
                Log.e(TAG, "onClick: BRUh");
                getActivity().setTitle(R.string.nav_label_uv_reader);
                Fragment fragment = ReaderFragment.newInstance();
                FragmentTransaction ft = fragmentManager.beginTransaction();
                ft.replace(R.id.content_main, fragment, MainActivity.TAG_FRAGMENT_DATA);
                ft.commit();
                break;
            default:
                break;
        }
    }


}