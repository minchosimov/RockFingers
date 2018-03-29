package com.mincho.rockfingers;

import android.content.res.Resources;
import android.os.Build;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mincho.rockfingers.been.Inner.WorkoutsInner;

import java.util.ArrayList;

class TaskThread extends Thread {

    //this is curent tast
    private WorkoutsInner curentTask;
    //task in this minute
    private ArrayList<WorkoutsInner> currMinTasks;
    //next minute task
    private ArrayList<WorkoutsInner> nextMinTasks;
    //count down timer
    private TaskCountDownTimer aTCDT;

    //constructor
    TaskThread(WorkoutsInner curentTask) {
        this.curentTask = curentTask;
        this.currMinTasks = new ArrayList<>();
        this.nextMinTasks = new ArrayList<>();
        this.aTCDT = new TaskCountDownTimer(this.curentTask.getDuration() * 1000, 1000, this.curentTask);
        //  Log.v("progress","init - "+(this.curentTask.getDurationRW()*1000));
    }

    TaskThread(WorkoutsInner curentTask, ArrayList<WorkoutsInner> aCurrMinTasks, ArrayList<WorkoutsInner> aNetxMinTasks) {
        this.curentTask = curentTask;
        this.currMinTasks = new ArrayList<>(aCurrMinTasks);
        this.nextMinTasks = new ArrayList<>(aNetxMinTasks);
        this.aTCDT = new TaskCountDownTimer((long) (this.curentTask.getDuration() * 1000), 1000, this.curentTask);

    }

    @Override
    public void run() {

        String currText = "";
        String nextText;

        LinearLayout.LayoutParams paramTV =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        paramTV.setMargins(0, 0, 0, 5);

        DoRoutineActivity.nextMLL.removeAllViews();

        if (this.curentTask.getWorkout().getNameWorkout().equalsIgnoreCase("rest")) {
            DoRoutineActivity.mMP_rest.start();
        } else {
            if (!this.curentTask.isNext())
                DoRoutineActivity.mMP_go.start();
            else
                DoRoutineActivity.mMP_next.start();
        }

        //извеждане на името на упражнението

        DoRoutineActivity.workout.setText(this.curentTask.getOutText());

        //извеждане на името на следващото упражнение от тази минута

        if (!this.currMinTasks.isEmpty()) {
            for (int i = 0; i < this.currMinTasks.size(); i++) {
                currText += (this.currMinTasks.get(i).getOutText() + " \n");
            }
        }
        DoRoutineActivity.thisMinExercises.setSingleLine(false);
        DoRoutineActivity.thisMinExercises.setText(currText);

        //извеждане на името на упражненията от следващата минута
        if (!this.nextMinTasks.isEmpty()) {
            for (int i = 0; i < this.nextMinTasks.size(); i++) {
                nextText = this.nextMinTasks.get(i).getOutText();
                TextView newTV = new TextView(DoRoutineActivity.baseContext);
                newTV.setLayoutParams(paramTV);
                newTV.setGravity(Gravity.START);
                newTV.setGravity(Gravity.START);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    newTV.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.bullet, 0, 0, 0);
                } else {
                    newTV.setCompoundDrawablesWithIntrinsicBounds(R.drawable.bullet, 0, 0, 0);
                }
                newTV.setCompoundDrawablePadding(3);
                newTV.setTextAppearance(DoRoutineActivity.baseContext, R.style.DoRoutine_nextMin);
                newTV.setText(nextText);
                DoRoutineActivity.nextMLL.addView(newTV);
            }
        }
        //DoRoutineActivityNew.nextMinExercises.setText(nextText);

        try {
            DoRoutineActivity.workoutImage.setImageResource(this.curentTask.getWorkout().getPictureIdWorkout());
        } catch (Resources.NotFoundException e) {
            DoRoutineActivity.workoutImage.setImageResource(R.drawable.none);
        }

        DoRoutineActivity.mProgress.setMax(this.curentTask.getCurrDuration());
        this.aTCDT.start();

    }

    WorkoutsInner getCurentTask() {
        return curentTask;
    }

    void addcurrMinTasks(WorkoutsInner aTask) {
        this.currMinTasks.add(aTask);
    }

    ArrayList<WorkoutsInner> getCurrMinTasks() {
        return currMinTasks;
    }

    void addnextMinTasks(WorkoutsInner aTask) {
        this.nextMinTasks.add(aTask);
    }

    ArrayList<WorkoutsInner> getNextMinTasks() {
        return nextMinTasks;
    }

    TaskCountDownTimer getaTCDT() {
        return aTCDT;
    }
}
