package com.mincho.rockfingers.been.Inner;

import com.mincho.rockfingers.been.HoldBeen;
import com.mincho.rockfingers.been.WorkoutBeen;
import com.mincho.rockfingers.util.WType;


public class WorkoutsCounter extends WorkoutsInner {
    private int idRoutineWorkout;
    private int number;
    private int count;
    private int timeForOne;
    private HoldBeen leftH, rightH;

    public WorkoutsCounter(int idRoutineWorkout, int numberRW, int durationRW, int countRW,
                           WorkoutBeen workout, HoldBeen left, HoldBeen right,
                           boolean isNext) {
        super(durationRW, workout, isNext);
        this.idRoutineWorkout = idRoutineWorkout;
        this.number = numberRW;
        this.count = countRW;
        this.timeForOne = this.getDuration() / this.count;
        this.leftH = left;
        this.rightH = right;
    }

    public int getCount() {
        return this.count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getIdRoutineWorkout() {
        return idRoutineWorkout;
    }


    /*
        public void setIdRoutineWorkout(int idRoutineWorkout) {
            this.idRoutineWorkout = idRoutineWorkout;
        }
    */
    public HoldBeen getLeftH() {
        return leftH;
    }

    /*
        public void setLeftH(HoldBeen leftH) {
            this.leftH = leftH;
        }
    */
    public int getNumber() {
        return this.number;
    }

    /*
        public void setNumber(int numberRW) {
            this.number = number;
        }
    */
    public HoldBeen getRightH() {
        return rightH;
    }

    /*
        public void setRightH(HoldBeen rightH) {
            this.rightH = rightH;
        }
    */
    public int getTime() {
        return this.timeForOne;
    }

    public void setTime(int time) {
        this.timeForOne = time;
    }

    @Override
    public void setDuration(int duration) {
        super.setDuration(duration);
        int d = (int) (duration / timeForOne) + 1;
        setCount(d);
        //Log.v("DUR","set count "+d);
    }

    @Override
    public int getCurrDuration() {
        return this.getCount();
    }

    @Override
    public String getOutText() {
        String text = "";
        if (this.getLeftH().getIdHold() != this.getRightH().getIdHold()) {
            text += this.getWorkout().getNameWorkout() + " : " + this.getCount() + " ("
                    + this.getLeftH().getNameHold() + " - " + this.getRightH().getNameHold() + ")";
        } else {
            text += this.getWorkout().getNameWorkout() + " : " + this.getCount() + " ("
                    + this.getLeftH().getNameHold() + ")";
        }
        return text;
    }

    @Override
    public WType.WTypeC getWType() {
        return WType.WTypeC.Count;
    }
}
