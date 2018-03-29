package com.mincho.rockfingers;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mincho.rockfingers.been.LogBeen;
import com.mincho.rockfingers.database.LogDB;
import com.mincho.rockfingers.util.DateDisplayFormat;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;


public class LogActivity extends AppCompatActivity {

    //bean instance
    ArrayList<LogBeen> logBeens;

    //database instance
    LogDB logDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);

        //add action bar
        final ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        // Enable the Up button
        actionBar.setDisplayHomeAsUpEnabled(true);

        //log db
        logDB = new LogDB(getApplicationContext());
        logDB.open();

        logBeens = logDB.getAllLog();
        LinearLayout mainLL = (LinearLayout) findViewById(R.id.log_main_ll);

        LinearLayout.LayoutParams paramTV = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
        int month = 0;
        if (logBeens.size() > 0) {

            month = logBeens.get(0).getDateTime().getTime().getMonth() + 1;

            LinearLayout ln = new LinearLayout(this);
            ln.setOrientation(LinearLayout.HORIZONTAL);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(0, 5, 0, 0);
            ln.setWeightSum(3);
            ln.setLayoutParams(params);

            ln.setBackgroundColor(getResources().getColor(R.color.green_bg));

            TextView monthTV = new TextView(this);
            monthTV.setText(logBeens.get(0).getDateTime().getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault()));
            monthTV.setTextAppearance(this, R.style.LogActivity_th);
            monthTV.setLayoutParams(paramTV);
            monthTV.setGravity(Gravity.CENTER);
            monthTV.setPadding(5, 0, 0, 0);
            ln.addView(monthTV);

            String[] s = logDB.getSumByMonth(month);

            TextView countTV = new TextView(this);
            countTV.setText(s[1]);
            countTV.setTextAppearance(this, R.style.LogActivity_th);
            countTV.setLayoutParams(paramTV);
            countTV.setGravity(Gravity.CENTER);
            ln.addView(countTV);

            TextView totalTV = new TextView(this);
            totalTV.setText(s[0]);
            totalTV.setTextAppearance(this, R.style.LogActivity_th);
            totalTV.setLayoutParams(paramTV);
            totalTV.setGravity(Gravity.CENTER);
            ln.addView(totalTV);
            mainLL.addView(ln);

        }

        for (int i = 0; i < logBeens.size(); i++) {
            if (month != (logBeens.get(i).getDateTime().getTime().getMonth() + 1)) {
                month = logBeens.get(i).getDateTime().getTime().getMonth() + 1;

                LinearLayout ln = new LinearLayout(this);
                ln.setOrientation(LinearLayout.HORIZONTAL);

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                params.setMargins(0, 5, 0, 0);
                ln.setWeightSum(3);
                ln.setLayoutParams(params);
                ln.setBackgroundColor(getResources().getColor(R.color.green_bg));

                TextView monthTV = new TextView(this);
                monthTV.setText(logBeens.get(i).getDateTime().getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault()));
                monthTV.setLayoutParams(paramTV);
                monthTV.setGravity(Gravity.CENTER);
                monthTV.setPadding(5, 0, 0, 0);
                monthTV.setTextAppearance(this, R.style.LogActivity_th);
                ln.addView(monthTV);

                String[] s = logDB.getSumByMonth(month);

                TextView countTV = new TextView(this);
                countTV.setText(s[1]);
                countTV.setLayoutParams(paramTV);
                countTV.setGravity(Gravity.CENTER);
                countTV.setTextAppearance(this, R.style.LogActivity_th);
                ln.addView(countTV);

                TextView totalTV = new TextView(this);
                totalTV.setText(s[0]);
                countTV.setLayoutParams(paramTV);
                totalTV.setGravity(Gravity.CENTER);
                totalTV.setTextAppearance(this, R.style.LogActivity_th);
                ln.addView(totalTV);
                mainLL.addView(ln);
            }

            LinearLayout lr = new LinearLayout(this);
            lr.setOrientation(LinearLayout.HORIZONTAL);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(0, 5, 0, 0);
            lr.setWeightSum(4);
            lr.setBackgroundColor(getResources().getColor(R.color.green_bg));
            lr.setLayoutParams(params);
            lr.setVerticalGravity(Gravity.CENTER_VERTICAL);
            lr.setMinimumHeight(60);

            TextView dateTV = new TextView(this);
            dateTV.setText(DateDisplayFormat.getFormattedDate(logBeens.get(i).getDateTime()));
            dateTV.setMinWidth(170);
            dateTV.setPadding(5, 0, 0, 0);
            dateTV.setLayoutParams(paramTV);
            dateTV.setTextAppearance(this, R.style.LogActivity_td);
            lr.addView(dateTV);

            String routineText = logBeens.get(i).getRoutine() + "\n " + logBeens.get(i).getLevel();
            TextView routineTV = new TextView(this);
            routineTV.setText(routineText);
            routineTV.setTextAppearance(this, R.style.LogActivity_td);
            routineTV.setLayoutParams(paramTV);
            lr.addView(routineTV);

            TextView timeTV = new TextView(this);
            timeTV.setText(String.format(logBeens.get(i).getwTime().toString() + "''", Locale.getDefault()));
            timeTV.setMinWidth(100);
            timeTV.setLayoutParams(paramTV);
            timeTV.setTextAppearance(this, R.style.LogActivity_td);
            lr.addView(timeTV);

            ImageButton buttonDelete = new ImageButton(this);
            buttonDelete.setId(100 + i);
            buttonDelete.setImageDrawable(getResources().getDrawable(R.drawable.delete_icon));
            buttonDelete.setBackgroundColor(getResources().getColor(R.color.green_bg));
            buttonDelete.setMinimumWidth(80);
            buttonDelete.setLayoutParams(paramTV);
            buttonDelete.setMaxWidth(100);
            final int finalI = i;
            buttonDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setResult(RESULT_OK);
                    logDB.deleteLogRow(Integer.toString(logBeens.get(finalI).getID()));
                    Intent intent = getIntent();
                    finish();
                    startActivity(intent);
                }
            });
            lr.addView(buttonDelete);
            mainLL.addView(lr);
        }

    }



    @Override
    protected void onDestroy() {
        // Log.v("DB", "onDestroy");
        logDB.close();
        super.onDestroy();
    }

}
