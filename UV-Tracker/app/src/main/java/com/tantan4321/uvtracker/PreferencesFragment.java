package com.tantan4321.uvtracker;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;

public class PreferencesFragment extends PreferenceFragment
        implements Preference.OnPreferenceChangeListener {

    private EditTextPreference mPasscodePreference;
    SharedPreferences mSharedPreferences;

    public static Fragment newInstance() {
        Fragment fragment = new PreferencesFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mSharedPreferences = getActivity().getSharedPreferences(
                getActivity().getPackageName(), Context.MODE_PRIVATE);
        mPasscodePreference = (EditTextPreference)
                findPreference("passcode");
        mPasscodePreference.setOnPreferenceChangeListener(this);
        final String passcode = mSharedPreferences.getString(BluetoothService.PREF_LOCK_PASSCODE,
                BluetoothService.DEFAULT_LOCK_PASSCODE);
        mPasscodePreference.setText(passcode);
        mPasscodePreference.setSummary(passwordString(passcode.length()));
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (preference.equals(mPasscodePreference)) {
            final String passcode = (String) newValue;
            mSharedPreferences.edit().putString(
                    BluetoothService.PREF_LOCK_PASSCODE, passcode).apply();
            mPasscodePreference.setSummary(passwordString(passcode.length()));
        }
        return true;
    }

    private String passwordString(int n) {
        StringBuilder stringBuilder =
                new StringBuilder(BluetoothService.MAX_PASSCODE_LENGTH);
        for (int i=0; i < n; i++) {
            stringBuilder.appendCodePoint(8226);
        }
        return stringBuilder.toString();
    }
}