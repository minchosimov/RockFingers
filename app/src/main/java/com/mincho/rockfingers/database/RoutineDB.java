package com.mincho.rockfingers.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.mincho.rockfingers.been.RoutinesBeen;

import java.util.ArrayList;

/**
 * Created by simov on 07-May-17.Routine db
 */

public class RoutineDB {

    private static final String DATABASE_NAME = "rockfingersroutine.db";
    private static final int DATABASE_VERSION = 3;

    //създаване на таблица Routine
    private static final String TABLE_ROUTINE = "Routines";
    private static final String CREATE_TABLE_ROUTINE =
            "CREATE TABLE Routines (" +
                    "idRoutine INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "nameRoutine VARCHAR(50) NOT NULL UNIQUE ON CONFLICT IGNORE, " +
                    "authorRoutine VARCHAR(50) NOT NULL, " +
                    "descrRoutine VARCHAR(150), " +
                    "nicNameRoutine VARCHAR(15), " +
                    "pictureRoutine VARCHAR(45) NOT NULL DEFAULT 'none')";
    private static final String SQL_DELETE_ROUTINE = "DROP TABLE IF EXISTS " + TABLE_ROUTINE;

    private final Context mCtx;
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    public RoutineDB(Context mCtx) {
        this.mCtx = mCtx;
    }

    public RoutineDB open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        mDbHelper.close();
    }

    public void installDB() {
        String[] nameRotine = {"10 Minute Training Sequence", "10 minutes with 3D Simulator"};
        String[] authorRotine = {"METOLIUS", "METOLIUS"};
        String[] descrRoutine = {"This is a standard training plan created by METOLUS can be used with different boards. Three levels of difficulty.", "This is a standard training plan created by METOLIUS for the board 'Simulator 3D'. Three levels of difficulty."};
        String[] nicNameRoutine = {"min10", "min3d"};
        String[] picRoutine = {"none", "pic_3d_shadow"};

        ContentValues values = new ContentValues();
        for (int i = 0; i < nameRotine.length; i++) {
            values.put("nameRoutine", nameRotine[i]);
            values.put("authorRoutine", authorRotine[i]);
            values.put("descrRoutine", descrRoutine[i]);
            values.put("nicNameRoutine", nicNameRoutine[i]);
            values.put("pictureRoutine", picRoutine[i]);
            mDb.insert(TABLE_ROUTINE, null, values);
        }
    }

    public ArrayList<RoutinesBeen> getAllRoutines() {
        ArrayList<RoutinesBeen> all = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + TABLE_ROUTINE + " ORDER BY idRoutine DESC";
        Cursor c = mDb.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
            do {
                all.add(new RoutinesBeen(c.getInt(0), c.getString(1), c.getString(2), c.getString(3), c.getString(4), c.getString(5)));
            } while (c.moveToNext());
        }
        c.close();

        return all;
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {


        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_TABLE_ROUTINE);

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            if (newVersion > oldVersion) {
                db.execSQL(SQL_DELETE_ROUTINE);
                onCreate(db);
            } else {
                // otherwise, create the database
                onCreate(db);
            }

        }

    }
}
