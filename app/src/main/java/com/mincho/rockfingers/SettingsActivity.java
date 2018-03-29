package com.mincho.rockfingers;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.MenuItem;


public class SettingsActivity extends AppCompatPreferenceActivity {
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //add action bar
        final ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        // add action bar up button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getListView().setBackgroundColor(getResources().getColor(R.color.gray_bg));
        getListView().setCacheColorHint(getResources().getColor(R.color.black_bg));

        getFragmentManager().beginTransaction().replace(android.R.id.content, new MyPreferenceFragment()).commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public static class MyPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(final Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);

            findPreference("pre_count").setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    boolean rtnVal = true;
                    String newVal = (String) newValue;
                    int nv = 0;
                    try {
                        nv = Integer.parseInt(newVal);
                    } catch (NumberFormatException nfe) {
                        System.out.println("Could not parse " + nfe);
                    }
                    if (!((nv >= 3) && (nv <= 10))) {
                        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AlertDialog);
                        builder.setTitle(R.string.alert_title);
                        builder.setMessage(R.string.alert_descr);
                        builder.setPositiveButton(R.string.alert_button, null);
                        builder.show();
                        rtnVal = false;
                    }
                    Log.e("msg", "return " + rtnVal);
                    return rtnVal;
                }
            });
        }
    }

}