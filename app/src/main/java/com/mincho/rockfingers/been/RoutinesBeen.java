package com.mincho.rockfingers.been;

import android.content.Context;

/**
 * Created by simov on 07-May-17. routines been for buy
 */

public class RoutinesBeen {
    private int idRoutine;
    private String nameRoutine;
    private String authorRoutine;
    private String decrRoutine;
    private String nicNameRoutine;
    private String picRoutine;

    public RoutinesBeen(int idRoutine, String nameRoutine, String authorRoutine, String decrRoutine, String nicNameRoutine, String picRoutine) {
        this.idRoutine = idRoutine;
        this.nameRoutine = nameRoutine;
        this.authorRoutine = authorRoutine;
        this.decrRoutine = decrRoutine;
        this.nicNameRoutine = nicNameRoutine;
        this.picRoutine = picRoutine;
    }

    public int getIdRoutine() {
        return idRoutine;
    }

    public String getNameRoutine() {
        return nameRoutine;
    }

    public String getAuthorRoutine() {
        return authorRoutine;
    }

    public String getDecrRoutine() {
        return decrRoutine;
    }

    public String getPicRoutine() {
        return picRoutine;
    }

    public String getNicNameRoutine() {
        return nicNameRoutine;
    }

    public int getIdPictureRoutine(Context mCtx) {
        return mCtx.getResources().getIdentifier(this.picRoutine, "drawable", mCtx.getPackageName());
    }
}
