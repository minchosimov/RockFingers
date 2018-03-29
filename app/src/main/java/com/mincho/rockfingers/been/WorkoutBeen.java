package com.mincho.rockfingers.been;

import android.content.Context;

public class WorkoutBeen {

    private int idWorkout;
    private String nameWorkout;
    private String pictureWorkout;
    private int pictureIdWorkout;

    public WorkoutBeen(int idWorkout, String nameWorkout, String pictureWorkout) {
        this.idWorkout = idWorkout;
        this.nameWorkout = nameWorkout;
        this.pictureWorkout = pictureWorkout;
    }

    /* public int getIdWorkout() {
         return this.pictureIdWorkout;
     }

     public void setIdWorkout(int idWorkout) {
         this.idWorkout = idWorkout;
     }

     */
    public String getNameWorkout() {
        return nameWorkout;
    }

    /* public void setNameWorkout(String nameWorkout) {
         this.nameWorkout = nameWorkout;
     }

     public String getPictureWorkout() {
         return pictureWorkout;
     }

     public void setPictureWorkout(String pictureWorkout) {
         this.pictureWorkout = pictureWorkout;
     }

     public WorkoutBeen() {

     }*/
    public int getPictureIdWorkout() {
        return pictureIdWorkout;
    }

    public void setPictureIdWorkout(Context mCtx) {
        this.pictureIdWorkout = mCtx.getResources().getIdentifier(this.pictureWorkout, "drawable", mCtx.getPackageName());
    }

}
