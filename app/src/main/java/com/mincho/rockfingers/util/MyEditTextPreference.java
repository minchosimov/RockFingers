package com.mincho.rockfingers.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.mincho.rockfingers.R;

/**
 * Created by simov on 20-May-17. mode edittextpreferences
 */

public class MyEditTextPreference extends EditTextPreference {

    public MyEditTextPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public MyEditTextPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyEditTextPreference(Context context) {
        super(context);
    }


    @Override
    protected void onSetInitialValue(boolean restoreValue, Object defaultValue) {

        super.onSetInitialValue(restoreValue, defaultValue);
        //get previous or default value
        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(getContext());
        String precoutn = SP.getString("pre_count", "3");
        setText(precoutn);
    }


    @Override
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);
        //change edit text color
        EditText et = getEditText();
        et.getBackground().setColorFilter(getContext().getResources().getColor(R.color.black_bg), PorterDuff.Mode.SRC_ATOP);
    }

    @Override
    protected void showDialog(Bundle state) {
        super.showDialog(state);

        //change dialog properties buttons positive and negative
        AlertDialog dialog = (AlertDialog) getDialog();

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.setMargins(15, 0, 15, 0);

        Button positiveButton = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
        positiveButton.setTextAppearance(getContext(), R.style.Setting_button);
        positiveButton.setBackgroundColor(getContext().getResources().getColor(R.color.blue_bg));
        positiveButton.setLayoutParams(lp);

        Button negativeButton = dialog.getButton(DialogInterface.BUTTON_NEGATIVE);
        negativeButton.setTextAppearance(getContext(), R.style.Setting_button);
        negativeButton.setBackgroundColor(getContext().getResources().getColor(R.color.blue_bg));
        negativeButton.setLayoutParams(lp);

        dialog.show();

    }
}
