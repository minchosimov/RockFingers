package com.mincho.rockfingers;

import android.os.CountDownTimer;

import com.mincho.rockfingers.been.Inner.WorkoutsInner;

class TaskCountDownTimer extends CountDownTimer {
    boolean hasFinished; //is it finished
    private long remainMilli; //remain time
    private WorkoutsInner aTask; //currenty task

    TaskCountDownTimer(long millisInFuture, long countDownInterval, WorkoutsInner aTask) {
        super(millisInFuture, countDownInterval);
        this.hasFinished = false;
        this.remainMilli = millisInFuture;
        this.aTask = aTask;
    }

    synchronized public void onTick(long millisUntilFinished) {

        remainMilli = millisUntilFinished;
        DoRoutineActivity.mProgress.setProgress((int) millisUntilFinished / 1000);
        int d = (int) remainMilli / 1000;
        aTask.setDuration(d);
        //   Log.v("DUR","duratio - "+d);
        DoRoutineActivity.workout.setText(this.aTask.getOutText());
    }

    public void onFinish() {
        hasFinished = true;
        remainMilli = 0;
    }

}
