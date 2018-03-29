package com.mincho.rockfingers.been;

import com.mincho.rockfingers.been.Inner.WorkoutsInner;

import java.util.ArrayList;

public class RoutineWorkoutBeenNew {
    private int minuteRW;
    private ArrayList<WorkoutsInner> workouts;

    public RoutineWorkoutBeenNew(int minuteRW) {
        this.minuteRW = minuteRW;
        this.workouts = new ArrayList<>();
    }

    public int getMinuteRW() {
        return minuteRW;
    }

    /*public void setMinuteRW(int minuteRW) {
        this.minuteRW = minuteRW;
    }
*/
    public void addWorkout(WorkoutsInner a) {
        this.workouts.add(a);
    }

    public ArrayList<WorkoutsInner> getWorkouts() {
        return workouts;
    }
}
