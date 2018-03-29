package com.mincho.rockfingers.been.Inner;

import com.mincho.rockfingers.been.WorkoutBeen;
import com.mincho.rockfingers.util.WType;

/**
 * Created by simov on 13-Sep-16.
 * Rest class
 */
public class WorkoutsRest extends WorkoutsInner {
    public WorkoutsRest(int duration) {
        super(duration, new WorkoutBeen(-1, "Rest", "rest"), false);
    }


    @Override
    public int getCurrDuration() {
        return this.getDuration();
    }

    @Override
    public String getOutText() {
        return this.getWorkout().getNameWorkout() + " : " + getCurrDuration() + "s";
    }

    @Override
    public WType.WTypeC getWType() {
        return WType.WTypeC.Rest;
    }
}
