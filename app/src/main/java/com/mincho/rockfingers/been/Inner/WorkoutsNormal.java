package com.mincho.rockfingers.been.Inner;

import com.mincho.rockfingers.been.HoldBeen;
import com.mincho.rockfingers.been.WorkoutBeen;
import com.mincho.rockfingers.util.WType;

/**
 * Created by simov on 12-Sep-16.
 * Normal workout class
 */
public class WorkoutsNormal extends WorkoutsInner {

    private int idRoutineWorkout;
    private int number;
    private HoldBeen leftH, rightH;

    public WorkoutsNormal(int idRoutineWorkout, int numberRW, int durationRW,
                          WorkoutBeen workout, HoldBeen leftH, HoldBeen rightH,
                          boolean isNext) {
        super(durationRW, workout, isNext);
        this.idRoutineWorkout = idRoutineWorkout;
        this.leftH = leftH;
        this.number = numberRW;
        this.rightH = rightH;
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
        public void setNumber(int number) {
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

    @Override
    public int getCurrDuration() {
        return this.getDuration();
    }

    @Override
    public String getOutText() {
        String text = "";
        if (this.getLeftH().getIdHold() != this.getRightH().getIdHold()) {
            text += this.getWorkout().getNameWorkout() + " : " + this.getDuration() + "s ("
                    + this.getLeftH().getNameHold() + " - " + this.getRightH().getNameHold() + ")";
        } else {
            text += this.getWorkout().getNameWorkout() + " : " + this.getDuration() + "s ("
                    + this.getLeftH().getNameHold() + ")";
        }
        return text;
    }

    @Override
    public WType.WTypeC getWType() {
        return WType.WTypeC.Normal;
    }
}
