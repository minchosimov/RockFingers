package com.mincho.rockfingers;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.mincho.rockfingers.been.BoardBeen;
import com.mincho.rockfingers.been.Inner.WorkoutsInner;
import com.mincho.rockfingers.been.LogBeen;
import com.mincho.rockfingers.been.RoutineWorkoutBeenNew;
import com.mincho.rockfingers.database.LogDB;
import com.mincho.rockfingers.database.MainDBNew;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DoRoutineActivity extends AppCompatActivity {

    //гласови команди
    public static MediaPlayer mMP_rest, mMP_go, mMP_ready, mMP_three, mMP_two, mMP_one, mMP_done, mMP_next;
    //base context
    public static Context baseContext;
    //полета
    protected static TextView workout;
    protected static TextView thisMinExercises;
    protected static LinearLayout nextMLL;
    protected static ProgressBar mProgress;
    protected static ImageView workoutImage; //image
    //
    private final Handler myHandler = new Handler();
    //база данни
    private MainDBNew mainDB;
    //хронометър
    private Chronometer chrono;

    private boolean isChronoStart = false;
    private boolean isTen = false;
    private boolean isDone = false;
    //инициализация на хронометъра
    //private TextView workoutText;
    private Button buttonStart, buttonStop;
    private ToggleButton buttonPause; // pause - resume button

    private Timer myTimer;    //main timer
    private ArrayList<MyTimerTask> myTimerTasks; //timer tasks list
    //pause listener
    View.OnClickListener mPauseListener = new View.OnClickListener() {
        public synchronized void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_pause:
                    if (buttonPause.isChecked()) {
                        startPause();
                    } else {
                        startResume();
                    }
                    break;
            }
        }
    };
    //Firebase variable
   // private FirebaseAnalytics mFirebaseAnalytics;
    //входни параметри
    private int routine, level;
    //save dialog
    private AlertDialog alert;
    Chronometer.OnChronometerTickListener mTickListener = new Chronometer.OnChronometerTickListener() {
        public void onChronometerTick(Chronometer chronometer) {
            choiceTick();
        }
    };
    //stop listener
    View.OnClickListener mStopListner = new View.OnClickListener() {
        public synchronized void onClick(View v) {
            startPause();
            alertLog();
        }

    };
    //handler task start
    private Runnable mUpdateTimeTask = new Runnable() {
        public void run() {
            int stoppedMilliseconds = 0;
            String chronoText = chrono.getText().toString();
            String array[] = chronoText.split(":");
            if (array.length == 2) {
                stoppedMilliseconds = Integer.parseInt(array[0]) * 60 * 1000
                        + Integer.parseInt(array[1]) * 1000;
            } else if (array.length == 3) {
                stoppedMilliseconds = Integer.parseInt(array[0]) * 60 * 60
                        * 1000 + Integer.parseInt(array[1]) * 60 * 1000
                        + Integer.parseInt(array[2]) * 1000;
            }
            chrono.setBase(SystemClock.elapsedRealtime() - stoppedMilliseconds);
            chrono.start();
        }
    };
    //start listener
    View.OnClickListener mStartListener = new View.OnClickListener() {
        public synchronized void onClick(View v) {
            isChronoStart = true;
            // Log.e("screen","is - "+isChronoStart);
            buttonPause.setVisibility(View.VISIBLE);
            buttonPause.setEnabled(true);
            buttonStart.setVisibility(View.GONE);
            buttonStop.setEnabled(true);
            //get previous or default value
            SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(baseContext);
            String precountStr = SP.getString("pre_count", "3");
            int nv = 0;
            try {
                nv = Integer.parseInt(precountStr);
            } catch (NumberFormatException nfe) {
                System.out.println("Could not parse " + nfe);
            }
            //nv = nv*1000;

            AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext(), R.style.AlertDialog);
            builder.setTitle("Get Ready!");
            builder.setMessage(" " + nv);
            final AlertDialog diag = builder.create();
            diag.show();

            TextView textView = (TextView) diag.findViewById(android.R.id.message);
            textView.setTextSize(18);

            final int s = nv;
            new CountDownTimer(s * 1000, 1000) {
                int i = s;

                @Override
                public void onTick(long millisUntilFinished) {
                    // TODO Auto-generated method stub
                    Log.e("doitt", "" + millisUntilFinished);
                    diag.setMessage("" + (i - 1));
                    switch (i) {
                        case 5:
                            mMP_ready.start();
                            i--;
                            break;
                        case 4:
                            mMP_three.start();
                            i--;
                            break;
                        case 3:
                            mMP_two.start();
                            i--;
                            break;
                        case 2:
                            mMP_one.start();
                            i--;
                            break;
                        default:
                            i--;
                            break;
                    }
                }

                @Override
                public void onFinish() {
                    // TODO Auto-generated method stub
                    diag.dismiss();
                    startProcess();
                    mUpdateTimeTask.run();
                }
            }.start();

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Obtain the FirebaseAnalytics instance.
       // mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        baseContext = getBaseContext();

        setContentView(R.layout.activity_do_routine);
        //keep screen on
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        //  int orientation = this.getResources().getConfiguration().orientation;
        MobileAds.initialize(this, getString(R.string.banner_ad_unit_id));
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


        //add action bar
        final ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        // Enable the Up button
        actionBar.setDisplayHomeAsUpEnabled(true);

        ImageView campImage;

        // инициализиране на база данни
        mainDB = new MainDBNew(getApplicationContext());
        mainDB.open();

        //гласови команди инициализация
        AssetFileDescriptor afd;

        mMP_go = new MediaPlayer();
        afd = getResources().openRawResourceFd(R.raw.go);
        try {
            mMP_go.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getDeclaredLength());
            mMP_go.prepareAsync();
            afd.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mMP_rest = new MediaPlayer();
        afd = getResources().openRawResourceFd(R.raw.rest);
        try {
            mMP_rest.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getDeclaredLength());
            mMP_rest.prepareAsync();
            afd.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        mMP_ready = new MediaPlayer();
        afd = getResources().openRawResourceFd(R.raw.ready);
        try {
            mMP_ready.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getDeclaredLength());
            mMP_ready.prepare();
            afd.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        mMP_three = new MediaPlayer();
        afd = getResources().openRawResourceFd(R.raw.three);
        try {
            mMP_three.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getDeclaredLength());
            mMP_three.prepare();
            afd.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        mMP_two = new MediaPlayer();
        afd = getResources().openRawResourceFd(R.raw.two);
        try {
            mMP_two.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getDeclaredLength());
            mMP_two.prepare();
            afd.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        mMP_one = new MediaPlayer();
        afd = getResources().openRawResourceFd(R.raw.one);
        try {
            mMP_one.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getDeclaredLength());
            mMP_one.prepare();
            afd.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        mMP_done = new MediaPlayer();
        afd = getResources().openRawResourceFd(R.raw.done);
        try {
            mMP_done.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getDeclaredLength());
            mMP_done.prepare();
            afd.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        mMP_next = new MediaPlayer();
        afd = getResources().openRawResourceFd(R.raw.next);
        try {
            mMP_next.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getDeclaredLength());
            mMP_next.prepare();
            afd.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //chronometer
        chrono = (Chronometer) findViewById(R.id.chronometer);
        chrono.setOnChronometerTickListener(mTickListener);
        //countdown textview
        mProgress = (ProgressBar) findViewById(R.id.progressBar);

        buttonStart = (Button) findViewById(R.id.btn_start); // button  start
        buttonStart.setOnClickListener(mStartListener);

        buttonPause = (ToggleButton) findViewById(R.id.btn_pause); // button  pause-resume
        buttonPause.setOnClickListener(mPauseListener);
        buttonPause.setEnabled(false);

        buttonStop = (Button) findViewById(R.id.btn_stop);
        buttonStop.setOnClickListener(mStopListner);
        buttonStop.setEnabled(false);

        //Найменование на уражнението
        workout = (TextView) findViewById(R.id.workout); // text workout

        //следващото упръжнение от тази минута
        thisMinExercises = (TextView) findViewById(R.id.thismin);

        //упръжненията от следващата минута
        // nextMinExercises = (TextView) findViewById(R.id.next);
        nextMLL = (LinearLayout) findViewById(R.id.next_m_ll);
        //картинка на упражнението
        workoutImage = (ImageView) findViewById(R.id.eximage);
        //картинка на борда
        campImage = (ImageView) findViewById(R.id.campimage);
        //извличане на променливите
        Bundle b = getIntent().getExtras();
        level = b.getInt("level");
        routine = b.getInt("routine");
        // Log.v("PARAM","Level ID - "+level);
        // Log.v("PARAM","Routine ID - "+routine);

        //зареждане от базата на борда
        BoardBeen bb = mainDB.selectBoardByID(mainDB.selectRoutineByID(routine).getBoard());
        // Log.e("bb",bb.getPictureBoard());
        if (bb.getPictureBoard().equalsIgnoreCase("none")) {
            //  Log.e("bb","gone");
            campImage.setVisibility(View.GONE);
        } else {
            //  Log.e("bb","else");
            bb.setIdPictureBoard(getApplicationContext());
            //инициализация на снимка на борда
            campImage.setImageResource(bb.getIdPictureBoard());
        }
        //активиране на нивото
        activateLevel();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if ((id == android.R.id.home) && isChronoStart) {
            //   Log.d("BACK_BUTTON_DOESNT_WORK", "Menu back");
            startPause();
            alertLog();
            return true;
        } else {
            setResult(RESULT_CANCELED);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        resetTimerTasks();
        finish();
        mainDB.close();
    }

    //обработка на хронометъра
    private void choiceTick() {
        Pattern p, pp;
        Matcher m, mm;
        p = Pattern.compile("10:[0-9][0-9]");
        pp = Pattern.compile("14:[0-9][0-9]");
        m = p.matcher(chrono.getText());
        if (m.matches() && (!isTen)) {
            isTen = true;
            alertLog();
            mMP_done.start();
        }
        mm = pp.matcher(chrono.getText());
        if (isTen && !isDone && mm.matches()) {
            if (alert.isShowing()) {
                alert.dismiss();
            }
            Log.e("efr", "done");
            isDone = true;
            startPause();
            saveLog();
            resetTimerTasks();
            setResult(RESULT_OK);
            finish();
        }

    }

    //запис в базата данни LogActivity
    private void saveLog() {
        LogDB lDB = new LogDB(getApplicationContext());
        lDB.open();
        LogBeen lB = new LogBeen();
        lB.setDateTime(Calendar.getInstance());
        lB.setRoutine(mainDB.selectRoutineByID(routine).getNameRoutine());
        lB.setLevel(mainDB.selectLevelByID(level).getNameLevel());
        String time = chrono.getText().toString();
        time = time.replace(":", ".");
        Double t = Double.parseDouble(time);
        lB.setwTime(t);
        lDB.insertLog(lB);
        lDB.close();
        //Here Add Firebase LogEvent
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "save_log");
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, time);
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "Button");
        RockFingerApplication.getFirebaseAnalytics().logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
    }

    private void alertLog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialog);

        builder.setTitle("Save");
        builder.setMessage("Save workout?");
        builder.setCancelable(false);
        //startPause();

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                // Code that is executed when clicking YES
                saveLog();
                dialog.dismiss();
                resetTimerTasks();
                setResult(RESULT_OK);
                finish();
            }

        });

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                // Code that is executed when clicking NO
                dialog.dismiss();
                resetTimerTasks();
                setResult(RESULT_CANCELED);
                finish();
            }

        });

        alert = builder.create();
        alert.show();
    }

    public void startPause() {
        chrono.stop();
        myTimer.purge();
        myTimer.cancel();
        myTimerTasks = resetDoneTask();
    }

    public void startResume() {
        int stoppedMilliseconds = 0;
        String chronoText = chrono.getText().toString();
        String array[] = chronoText.split(":");
        if (array.length == 2) {
            stoppedMilliseconds = Integer.parseInt(array[0]) * 60
                    * 1000 + Integer.parseInt(array[1]) * 1000;
        } else if (array.length == 3) {
            stoppedMilliseconds = Integer.parseInt(array[0]) * 60
                    * 60 * 1000 + Integer.parseInt(array[1]) * 60
                    * 1000 + Integer.parseInt(array[2]) * 1000;
        }
        chrono.setBase(SystemClock.elapsedRealtime()
                - stoppedMilliseconds);
        chrono.start();
        startProcess();
    }

    private ArrayList<MyTimerTask> resetDoneTask() {
        ArrayList<MyTimerTask> aTimerTasks = new ArrayList<>();
        Log.d("stop", "ResetDoneTask size TimerTasks - " + myTimerTasks.size());
        try {
            for (int i = 0; i < myTimerTasks.size(); i++) {
                if (myTimerTasks.get(i) != null) {
                    if (!myTimerTasks.get(i).getaTaskThread().getaTCDT().hasFinished) {
                        //Log.d("stop","remain mill  - "+myTimerTasks.get(i).getaTaskThread().getaTCDT().remainMilli);
                        aTimerTasks.add(new MyTimerTask(myTimerTasks.get(i).getaTaskThread().getCurentTask(),
                                myTimerTasks.get(i).getaTaskThread().getCurrMinTasks(), myTimerTasks.get(i).getaTaskThread().getNextMinTasks()));
                        myTimerTasks.get(i).getaTaskThread().getaTCDT().cancel();
                        myTimerTasks.get(i).cancel();
                    } else {
                        myTimerTasks.get(i).cancel();
                    }
                }

            }
        } catch (NullPointerException ignored) {

        }
        return aTimerTasks;
    }


    private void resetTimerTasks() {
        if (isChronoStart)
            chrono.stop();
        chrono.setBase(SystemClock.elapsedRealtime());
        try {
            for (int i = 0; i < myTimerTasks.size(); i++) {
                if (myTimerTasks.get(i) != null) {
                    if (!myTimerTasks.get(i).getaTaskThread().getaTCDT().hasFinished) {
                        myTimerTasks.get(i).getaTaskThread().getaTCDT().cancel();
                        myTimerTasks.get(i).cancel();
                    } else {
                        myTimerTasks.get(i).cancel();
                    }
                }
            }
            myTimer.purge();
            myTimer.cancel();
            myTimer = null;
        } catch (NullPointerException ignored) {

        }
        isChronoStart = false;
    }

    //start process
    public void startProcess() {
        int h = 0;
        myTimer = new Timer();
        for (int i = 0; i < myTimerTasks.size(); i++) {
            myTimer.schedule(myTimerTasks.get(i), h);
            h += (myTimerTasks.get(i).getaTaskThread().getCurentTask().getDuration() * 1000);
        }
    }

    //activate level
    public void activateLevel() {
        isTen = false;
        loadProcess();

        String currText = "";
        String nextText;

        //извеждане на името на упражнението
        workout.setText(myTimerTasks.get(0).getaTaskThread().getCurentTask().getOutText());

        //извеждане на името на следващото упражнение от тази минута

        if (!myTimerTasks.get(0).getaTaskThread().getCurrMinTasks().isEmpty()) {
            for (int i = 0; i < myTimerTasks.get(0).getaTaskThread().getCurrMinTasks().size(); i++) {
                currText += myTimerTasks.get(0).getaTaskThread().getCurrMinTasks().get(i).getOutText() + " \n";
            }
        }
        thisMinExercises.setText(currText);

        //извеждане на името на упражненията от следващата минута
        LinearLayout.LayoutParams paramTV =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        paramTV.setMargins(0, 0, 0, 5);
        if (!myTimerTasks.get(0).getaTaskThread().getNextMinTasks().isEmpty()) {
            for (int i = 0; i < myTimerTasks.get(0).getaTaskThread().getNextMinTasks().size(); i++) {
                nextText = myTimerTasks.get(0).getaTaskThread().getNextMinTasks().get(i).getOutText();
                TextView newTV = new TextView(this);
                newTV.setLayoutParams(paramTV);
                newTV.setGravity(Gravity.START | Gravity.TOP);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    newTV.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.bullet, 0, 0, 0);
                } else {
                    newTV.setCompoundDrawablesWithIntrinsicBounds(R.drawable.bullet, 0, 0, 0);
                }
                newTV.setCompoundDrawablePadding(3);
                newTV.setTextAppearance(this, R.style.DoRoutine_nextMin);
                newTV.setText(nextText);
                nextMLL.addView(newTV);
            }
        }
        //nextMinExercises.setText(nextText);
        // картика на упр
        try {
            workoutImage.setImageResource(myTimerTasks.get(0).getaTaskThread().getCurentTask().getWorkout().getPictureIdWorkout());
        } catch (Resources.NotFoundException e) {
            workoutImage.setImageResource(R.drawable.none);
        }
    }

    private void loadProcess() {

        myTimerTasks = new ArrayList<>();
        //зареждане на нивото от базата данни
        ArrayList<RoutineWorkoutBeenNew> tasks = mainDB.selectAllRW(routine, level);

        //Log.v("PARAM","tasks - "+tasks.size());
        //инициализация на картинките
        for (int i = 0; i < tasks.size(); i++) {
            for (int j = 0; j < tasks.get(i).getWorkouts().size(); j++) {
                tasks.get(i).getWorkouts().get(j).getWorkout().setPictureIdWorkout(getApplicationContext());
            }
        }

        int wSize = tasks.size();

        for (int i = 0; i < wSize; i++) {
            int vSize = tasks.get(i).getWorkouts().size();
            for (int j = 0; j < vSize; j++) {
                MyTimerTask aTimerTaskThread = new MyTimerTask(tasks.get(i).getWorkouts().get(j));
                //Log.v("task", "root - " + tasks.get(i).getWorkout().getNameWorkout());
                myTimerTasks.add(aTimerTaskThread);
                for (int l = j + 1; l < vSize; l++) {
                    aTimerTaskThread.getaTaskThread().addcurrMinTasks(tasks.get(i).getWorkouts().get(l));
                }
                if (i < wSize - 1) {
                    for (int h = 0; h < tasks.get(i + 1).getWorkouts().size(); h++) {
                        aTimerTaskThread.getaTaskThread().addnextMinTasks(tasks.get(i + 1).getWorkouts().get(h));
                    }
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        //Close the Text to Speech Library
        mMP_next.release();
        mMP_done.release();
        mMP_one.release();
        mMP_two.release();
        mMP_three.release();
        mMP_go.release();
        mMP_ready.release();
        mMP_rest.release();
        resetTimerTasks();
        mainDB.close();
        super.onDestroy();
    }

    //class
    // timer task class
    private class MyTimerTask extends TimerTask {

        private TaskThread aTaskThread;

        MyTimerTask(WorkoutsInner currentTask) {
            this.aTaskThread = new TaskThread(currentTask);
        }

        MyTimerTask(WorkoutsInner currentTask, ArrayList<WorkoutsInner> aCurrMinTasks, ArrayList<WorkoutsInner> aNetxMinTasks) {
            this.aTaskThread = new TaskThread(currentTask, aCurrMinTasks, aNetxMinTasks);
        }

        TaskThread getaTaskThread() {
            return this.aTaskThread;
        }

        @Override
        synchronized public void run() {
            myHandler.post(aTaskThread);
        }
    }
}
