package com.mincho.rockfingers;


import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.preference.DialogPreference;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;


public class TimeSettingsActivity extends DialogPreference {

    private static Context mContext;
    private final int DEFAULT_VALUE = 2;
    //private final String ACT_TAG = "TimerSetup";
    private final String TEXT_PULL = "pullValue";
    private final String TEXT_SHRUG = "shrugValue";
    private final String TEXT_KNEE = "kneeValue";
    private SharedPreferences mSettings;
    private Integer mValuePull, mValueShrug, mValueKnee;
    private NumberPicker mNumberPickerPull, mNumberPickerShrug, mNumberPickerKnee;
    private boolean positiveClose = false;


    public TimeSettingsActivity(Context context, AttributeSet attrs) {
        super(context, attrs);

        setDialogLayoutResource(R.layout.activity_time_settings);


        // setNegativeButtonText(R.string.negative_button);
        // setPositiveButtonText(R.string.positive_button);

        mContext = context;
    }

    @Override
    protected void onPrepareDialogBuilder(AlertDialog.Builder builder) {
        super.onPrepareDialogBuilder(builder);
        builder.setTitle(null);
        builder.setPositiveButton(null, null);
        builder.setNegativeButton(null, null);
    }

    @Override
    protected void onBindDialogView(@NonNull View view) {
        super.onBindDialogView(view);

        // Set min and max values to our NumberPicker

        //Pull up
        mNumberPickerPull = (NumberPicker) view.findViewById(R.id.pullupSeconds);
        mNumberPickerPull.setMinValue(1);
        mNumberPickerPull.setMaxValue(4);
        mNumberPickerPull.setBackgroundColor(mContext.getResources().getColor(R.color.blue_bg));
        //Shrugs
        mNumberPickerShrug = (NumberPicker) view.findViewById(R.id.shrugSeconds);
        mNumberPickerShrug.setMinValue(1);
        mNumberPickerShrug.setMaxValue(4);
        //Knee rises
        mNumberPickerKnee = (NumberPicker) view.findViewById(R.id.kneeSeconds);
        mNumberPickerKnee.setMinValue(1);
        mNumberPickerKnee.setMaxValue(4);

        // Set default/current/selected value if set
        initValue();
        if (mValuePull != null) mNumberPickerPull.setValue(mValuePull);
        if (mValueShrug != null) mNumberPickerShrug.setValue(mValueShrug);
        if (mValueKnee != null) mNumberPickerKnee.setValue(mValueKnee);
        //  Log.d(ACT_TAG,"pull - "+mValuePull);

        final Button positiveButton = (Button) view.findViewById(R.id.button_set);
        positiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                positiveClose = true;
                getDialog().dismiss();

            }
        });
        Button negativeButton = (Button) view.findViewById(R.id.button_cancel);
        negativeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                positiveClose = false;
                getDialog().dismiss();
            }
        });


    }

    /*
    * Called when the dialog is closed.
    * If the positive button was clicked then persist
    * the data (save in SharedPreferences by calling `persistInt()`)
    * */
    private void initValue() {
        mSettings = PreferenceManager.getDefaultSharedPreferences(mContext);
        // SharedPreferences.Editor editor = mSettings.edit();
        mValuePull = mSettings.getInt(TEXT_PULL, DEFAULT_VALUE);
        mValueShrug = mSettings.getInt(TEXT_SHRUG, DEFAULT_VALUE);
        mValueKnee = mSettings.getInt(TEXT_KNEE, DEFAULT_VALUE);

    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        mSettings = PreferenceManager.getDefaultSharedPreferences(mContext);
        SharedPreferences.Editor editor = mSettings.edit();
        if (positiveClose) {
            mValuePull = mNumberPickerPull.getValue();
            editor.putInt(TEXT_PULL, mValuePull);
            //persistInt(mValuePull);
            mValueShrug = mNumberPickerShrug.getValue();
            editor.putInt(TEXT_SHRUG, mValueShrug);
            //persistInt(mValueShrug);
            mValueKnee = mNumberPickerKnee.getValue();
            editor.putInt(TEXT_KNEE, mValueKnee);
            //persistInt(mValueKnee);
            editor.apply();
            Log.e("set", "Close positive");
            // editor.commit();
        }
        Log.e("set", "Close");
    }

    /*
    * Set initial value of the preference. Called when
    * the preference object is added to the screen.
    *
    * If `restorePersistedValue` is true, the Preference
    * value should be restored from the SharedPreferences
    * else the Preference value should be set to defaultValue
    * passed and it should also be persisted (saved).
    *
    * `restorePersistedValue` will generally be false when
    * you've specified `android:defaultValue` that calls
    * `onGetDefaultValue()` (check below) and that in turn
    * returns a value which is passed as the `defaultValue`
    * to `onSetInitialValue()`.
    * */
    @Override
    protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) {
        super.onSetInitialValue(restorePersistedValue, defaultValue);
        // Log.d(ACT_TAG, "boolean: " + restorePersistedValue + " object: " + defaultValue);

        mSettings = PreferenceManager.getDefaultSharedPreferences(mContext);
        SharedPreferences.Editor editor = mSettings.edit();
        if (restorePersistedValue) {
            mValuePull = mSettings.getInt(TEXT_PULL, DEFAULT_VALUE);
            mValueShrug = mSettings.getInt(TEXT_SHRUG, DEFAULT_VALUE);
            mValueKnee = mSettings.getInt(TEXT_KNEE, DEFAULT_VALUE);
        } else {
            mValuePull = (int) defaultValue;
            editor.putInt(TEXT_PULL, mValuePull);
            //persistInt(mValuePull);
            mValueShrug = (int) defaultValue;
            editor.putInt(TEXT_SHRUG, mValueShrug);
            //persistInt(mValueShrug);
            mValueKnee = (int) defaultValue;
            editor.putInt(TEXT_KNEE, mValueKnee);
            //persistInt(mValueKnee);
            editor.apply();
            //editor.commit();
        }
    }

    /*
    * Called when you set `android:defaultValue`
    *
    * Just incase the value is undefined, you can return
    * DEFAULT_VALUE so that it gets passed to `onSetInitialValue()`
    * that gets saved in SharedPreferences.
    * */

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        // Log.d(ACT_TAG, "Index: " + index + " Value: " + a.getInteger(index, DEFAULT_VALUE));
        //return super.onGetDefaultValue(a, index);
        return a.getInteger(index, DEFAULT_VALUE);
    }
}
