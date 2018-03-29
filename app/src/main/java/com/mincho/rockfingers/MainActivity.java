package com.mincho.rockfingers;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.mincho.rockfingers.been.RoutineBeen;
import com.mincho.rockfingers.been.RoutinesBeen;
import com.mincho.rockfingers.database.LogDB;
import com.mincho.rockfingers.database.MainDBNew;
import com.mincho.rockfingers.database.RoutineDB;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

   // private FirebaseAnalytics mFirebaseAnalytics; //firebase variable

    MainDBNew mainDB;
    LogDB logDB;
    RoutineDB rDB;
    ArrayList<RoutineBeen> allRoutine;
    RoutineBeen selectRoutine;

    Spinner levelSpinner, routineSpinner;

    int RESULT_CODE;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        switch (id) {
            case R.id.action_settings:
                Intent i = new Intent(this, SettingsActivity.class);
                startActivityForResult(i, RESULT_OK);
                return true;
            case R.id.action_log:
                Intent l = new Intent(this, LogActivity.class);
                startActivityForResult(l, RESULT_OK);
                return true;
            case R.id.action_routines:
                Intent t = new Intent(this, RoutinesActivity.class);
                startActivityForResult(t, RESULT_OK);
                return true;
            case R.id.action_about:
                Intent a = new Intent(this, AboutActivity.class);
                startActivity(a);
                return true;
            default:
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setTheme(R.style.AppTheme);
        // super.onCreate(savedInstanceState);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Obtain the FirebaseAnalytics instance.
       // mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);


        //adsense

        MobileAds.initialize(this, getString(R.string.banner_ad_unit_id));

        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        ImageButton bCloseAdv = (ImageButton) findViewById(R.id.close_adv);
        bCloseAdv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertLog();
            }
        });

        // инициализиране на база данни
        logDB = new LogDB(getApplicationContext());
        logDB.open();
        String[] text = logDB.getForMainActivity();

        TextView totalTV = (TextView) findViewById(R.id.main_total_workouts);
        totalTV.setText(text[1]);
        TextView avgTV = (TextView) findViewById(R.id.main_avg_workouts);
        avgTV.setText(text[0]);
        TextView lastTV = (TextView) findViewById(R.id.main_last_workout);
        lastTV.setText(text[2]);

        // init routines db
        rDB = new RoutineDB(getApplicationContext());
        rDB.open();
        ArrayList<RoutinesBeen> allRoutines = rDB.getAllRoutines();

        if (allRoutines.size() == 0) {
            rDB.installDB();
        }

        // init main database
        mainDB = new MainDBNew(getApplicationContext());
        mainDB.open();
        allRoutine = mainDB.selectAllRoutine();

        List<String> routinesName = new ArrayList<>();

        if (allRoutine.size() == 0) {

            Intent myIntent = new Intent(this, RoutinesActivity.class);
            startActivityForResult(myIntent, 1);
        } else {
            for (int i = 0; i < allRoutine.size(); i++) {
                routinesName.add(allRoutine.get(i).getNameRoutine());
            }
        }

        routineSpinner = (Spinner) findViewById(R.id.main_routine_spinner);

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, routinesName);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        routineSpinner.setAdapter(dataAdapter);

        routineSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                loadActivity();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        levelSpinner = (Spinner) findViewById(R.id.main_level_spinner);


        Button beginButton = (Button) findViewById(R.id.main_begin_button);
        beginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Here Add Firebase LogEvent
                Bundle bundle = new Bundle();
                bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "Start routine");
                bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, selectRoutine.getNameRoutine());
                bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "Button");
                RockFingerApplication.getFirebaseAnalytics().logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

                Intent myIntent = new Intent(getApplicationContext(), DoRoutineActivity.class);
                myIntent.putExtra("routine", selectRoutine.getIdRoutine());
                myIntent.putExtra("level", selectRoutine.getLevelsRoutine().get((int) levelSpinner.getSelectedItemId()).getIdLevel());
                startActivityForResult(myIntent, RESULT_CODE);
            }
        });
    }

    private void alertLog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialog);

        builder.setTitle(this.getString(R.string.title_alert_buy));
        builder.setMessage(this.getString(R.string.message_alert_buy));
        builder.setCancelable(false);
        //startPause();

        builder.setPositiveButton(this.getString(R.string.buy_button), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                // Code that is executed when clicking YES
                dialog.dismiss();
                //Here Add Firebase LogEvent
                Bundle bundle = new Bundle();
                bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "buy_pro");
                bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "new user");
                bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "Button");
                RockFingerApplication.getFirebaseAnalytics().logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("market://details?id=com.mincho.rockfingerspro"));
                startActivity(intent);

            }

        });

        builder.setNegativeButton(this.getString(R.string.cancel_button), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                // Code that is executed when clicking NO
                dialog.dismiss();
            }

        });

        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        routineSpinner.measure(0, 0);
        int routineSpinnerWidth = routineSpinner.getMeasuredWidth();// .getWidth();
        int routineSpinnerHeight = routineSpinner.getMeasuredHeight();
        Log.v("Width", " - " + routineSpinnerWidth + " - " + routineSpinnerHeight);

        LinearLayout.LayoutParams lParams = (LinearLayout.LayoutParams) levelSpinner.getLayoutParams();
        lParams.width = routineSpinnerWidth;
        lParams.height = routineSpinnerHeight;
        levelSpinner.setLayoutParams(lParams);

    }

    void loadActivity() {
        selectRoutine = allRoutine.get((int) routineSpinner.getSelectedItemId());
        List<String> levelName = new ArrayList<>();
        for (int i = 0; i < selectRoutine.getLevelsRoutine().size(); i++) {
            levelName.add(selectRoutine.getLevelsRoutine().get(i).getNameLevel());
        }

        ArrayAdapter<String> dataAdapterLevel = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, levelName);
        dataAdapterLevel.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        levelSpinner.setAdapter(dataAdapterLevel);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case RESULT_OK:
                finish();
                startActivity(getIntent());
                break;
            case RESULT_CANCELED:
                //Do nothing
                //return from childActivity by other finishes
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        logDB.close();
        mainDB.close();
        rDB.close();
       // FirebaseCrash.report(new Exception("RockFingers Free error"));
    }
}
