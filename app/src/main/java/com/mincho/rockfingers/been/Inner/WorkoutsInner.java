package com.mincho.rockfingers.been.Inner;

import com.mincho.rockfingers.been.WorkoutBeen;
import com.mincho.rockfingers.util.WType;

/**
 * Created by simov on 12-Sep-16.
 * main class
 */
public abstract class WorkoutsInner {

    private int duration;
    private WorkoutBeen workout;
    private boolean isChanged;
    private boolean isNext;


    public WorkoutsInner(int duration, WorkoutBeen workout, boolean isNext) {
        this.duration = duration;
        this.workout = workout;
        this.isChanged = false;
        this.isNext = isNext;
    }

    abstract public int getCurrDuration();

    abstract public String getOutText();

    abstract public WType.WTypeC getWType();

    public boolean isNext() {
        return this.isNext;
    }

    public int getDuration() {
        return this.duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public WorkoutBeen getWorkout() {
        return workout;
    }


    public void setWorkout(WorkoutBeen workout) {
        this.workout = workout;
    }

    public boolean isChanged() {
        return this.isChanged;
    }

    public void setChanged(boolean a) {
        this.isChanged = a;
    }
}
