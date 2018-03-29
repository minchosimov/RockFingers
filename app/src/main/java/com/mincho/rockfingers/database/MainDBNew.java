package com.mincho.rockfingers.database;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.preference.PreferenceManager;

import com.mincho.rockfingers.been.BoardBeen;
import com.mincho.rockfingers.been.HoldBeen;
import com.mincho.rockfingers.been.Inner.WorkoutsCounter;
import com.mincho.rockfingers.been.Inner.WorkoutsInner;
import com.mincho.rockfingers.been.Inner.WorkoutsNormal;
import com.mincho.rockfingers.been.Inner.WorkoutsRest;
import com.mincho.rockfingers.been.LevelBeen;
import com.mincho.rockfingers.been.RoutineBeen;
import com.mincho.rockfingers.been.RoutineWorkoutBeenNew;
import com.mincho.rockfingers.been.WorkoutBeen;

import java.util.ArrayList;

public class MainDBNew {

    private static final String DATABASE_NAME = "rockfingersmaindb.db";
    private static final int DATABASE_VERSION = 2;
    //създаване на таблица Board
    private static final String TABLE_BOARD = "Board";
    private static final String CREATE_TABLE_BOARD =
            "CREATE TABLE  Board (" +
                    "idBoard INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "nameBoard VARCHAR(45) NOT NULL UNIQUE ON CONFLICT IGNORE, " +
                    "manyfactureBoard VARCHAR(45) NOT NULL, " +
                    "pictureBoard VARCHAR(45) NOT NULL DEFAULT 'none')";
    private static final String SQL_DELETE_BOARD = "DROP TABLE IF EXISTS " + TABLE_BOARD;
    //създаване на таблица Routine
    private static final String TABLE_ROUTINE = "Routine";
    private static final String CREATE_TABLE_ROUTINE =
            "CREATE TABLE Routine (" +
                    "idRoutine INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "nameRoutine VARCHAR(50) NOT NULL UNIQUE ON CONFLICT IGNORE, " +
                    "authorRoutine VARCHAR(50) NOT NULL, " +
                    "Board_idBoard INTEGER NOT NULL, " +
                    "FOREIGN KEY (Board_idBoard) REFERENCES Board (idBoard) ON DELETE CASCADE ON UPDATE CASCADE )";
    private static final String SQL_DELETE_ROUTINE = "DROP TABLE IF EXISTS " + TABLE_ROUTINE;
    private static final String TABLE_WORKOUT = "Workout";
    //създаване на таблица Workout
    private static final String CREATE_TABLE_WORKOUT =
            "CREATE TABLE Workout (" +
                    "idWorkout INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "nameWorkout VARCHAR(50) NOT NULL UNIQUE ON CONFLICT IGNORE, " +
                    "pictureWorkout VARCHAR(45) NOT NULL DEFAULT 'none')";
    private static final String SQL_DELETE_WORKOUT = "DROP TABLE IF EXISTS " + TABLE_WORKOUT;
    private static final String TABLE_LEVEL = "Level";
    private static final String CREATE_TABLE_LEVEL =
            "CREATE TABLE Level (" +
                    "idLevel INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "nameLevel VARCHAR(45) NOT NULL UNIQUE ON CONFLICT IGNORE, " +
                    "pictureLevel VARCHAR(25)NOT NULL DEFAULT 'none' )";
    private static final String SQL_DELETE_LEVEL = "DROP TABLE IF EXISTS " + TABLE_LEVEL;
    private static final String TABLE_ROUTINE_LEVEL = "Routine_Level";
    private static final String CREATE_TABLE_ROUTINE_LEVEL =
            "CREATE TABLE Routine_Level (" +
                    "Routine_idRoutine INTEGER NOT NULL, " +
                    "Level_idLevel INTEGER NOT NULL, " +
                    "FOREIGN KEY(Routine_idRoutine) REFERENCES Routine(idRoutine) ON DELETE CASCADE ON UPDATE CASCADE, " +
                    "FOREIGN KEY(Level_idLevel) REFERENCES Level(idLevel) ON DELETE CASCADE ON UPDATE CASCADE, " +
                    "UNIQUE (Routine_idRoutine, Level_idLevel) ON CONFLICT IGNORE)";
    private static final String SQL_DELETE_ROUTINE_LEVEL = "DROP TABLE IF EXISTS " + TABLE_ROUTINE_LEVEL;
    //hold
    private static final String TABLE_HOLD = "Hold";
    private static final String CREATE_TABLE_HOLD =
            "CREATE TABLE Hold (" +
                    "idHold INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "nameHold VARCHAR(30) NOT NULL UNIQUE)";
    private static final String SQL_DELETE_HOLD = "DROP TABLE IF EXISTS " + TABLE_HOLD;
    //създаване на таблица Routine_Workouts
    private static final String TABLE_R_W = "Routine_Workout";
    private static final String CREATE_TABLE_ROUTINEWORKOUT =
            "CREATE TABLE Routine_Workout (" +
                    "idRoutineWorkout INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "Routine_idRoutine INTEGER NOT NULL, " +
                    "Level_idLevel INTEGER NOT NULL, " +
                    "Workout_idWorkout INTEGER NOT NULL, " +
                    "Hold_idHoldLeft INTEGER NOT NULL, " +
                    "Hold_idHoldRight INTEGER NOT NULL, " +
                    "numberRW INTEGER NOT NULL, " +
                    "minuteRW INTEGER NOT NULL, " +
                    "durationRW INTEGER NOT NULL, " +
                    "FOREIGN KEY(Workout_idWorkout) REFERENCES Workout(idWorkout) ON DELETE CASCADE ON UPDATE CASCADE, " +
                    "FOREIGN KEY(Routine_idRoutine) REFERENCES Routine(idRoutine) ON DELETE CASCADE ON UPDATE CASCADE, " +
                    "FOREIGN KEY(Hold_idHoldLeft) REFERENCES Hold(idHold) ON DELETE CASCADE ON UPDATE CASCADE, " +
                    "FOREIGN KEY(Hold_idHoldRight) REFERENCES Hold(idHold) ON DELETE CASCADE ON UPDATE CASCADE, " +
                    "FOREIGN KEY(Level_idLevel) REFERENCES Level(idLevel) ON DELETE CASCADE ON UPDATE CASCADE, " +
                    "UNIQUE(Level_idLevel, Routine_idRoutine,numberRW) ON CONFLICT IGNORE)";
    private static final String SQL_DELETE_R_W = "DROP TABLE IF EXISTS " + TABLE_R_W;
    private final Context mCtx;
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    //конструктор
    public MainDBNew(Context mCtx) {
        this.mCtx = mCtx;
    }

    public MainDBNew open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        mDbHelper.close();
    }

    //10 minute sequence
    public void addmin10() {

        String RoutineName = "'10 Minute Training Sequence'";
        String TEN = "(SELECT idRoutine FROM Routine WHERE nameRoutine = " + RoutineName + ")";
        String EASY = "(SELECT idLevel FROM Level WHERE nameLevel = 'easy')";
        String MEDIUM = "(SELECT idLevel FROM Level WHERE nameLevel = 'medium')";
        String HARD = "(SELECT idLevel FROM Level WHERE nameLevel = 'hard')";

        //insert board
        String INSERT_BOARD = "INSERT INTO " + TABLE_BOARD + " (nameBoard,manyfactureBoard,pictureBoard)" +
                "VALUES ('Fondry','METOLIUS','none')";
        mDb.execSQL(INSERT_BOARD);

        //insert routine
        String INSERT_ROUTINE = "INSERT INTO " + TABLE_ROUTINE +
                " (nameRoutine, authorRoutine, Board_idBoard) VALUES (" + RoutineName + ",'METOLIUS', (SELECT idBoard FROM " + TABLE_BOARD + " WHERE nameBoard = 'Fondry'))";
        mDb.execSQL(INSERT_ROUTINE);

        //insert level
        String[] INSERT_LEVEL = {"INSERT INTO " + TABLE_LEVEL + " (nameLevel, pictureLevel) VALUES ('easy','buttonstyle_easy_logo')",
                "INSERT INTO " + TABLE_LEVEL + " (nameLevel,pictureLevel) VALUES ('medium','buttonstyle_medium_logo')",
                "INSERT INTO " + TABLE_LEVEL + " (nameLevel,pictureLevel) VALUES ('hard','buttonstyle_hard_logo')"};
        for (String aINSERT : INSERT_LEVEL) {
            mDb.execSQL(aINSERT);
        }

        //insert routine_level
        String[] INSERT_ROUTINE_LEVEL = {
                "INSERT INTO " + TABLE_ROUTINE_LEVEL + " (Routine_idRoutine,Level_idLevel) VALUES (" + TEN + "," + EASY + ")",
                "INSERT INTO " + TABLE_ROUTINE_LEVEL + " (Routine_idRoutine,Level_idLevel) VALUES (" + TEN + "," + MEDIUM + ")",
                "INSERT INTO " + TABLE_ROUTINE_LEVEL + " (Routine_idRoutine,Level_idLevel) VALUES (" + TEN + "," + HARD + ")",};
        for (String aINSERT : INSERT_ROUTINE_LEVEL) {
            mDb.execSQL(aINSERT);
        }

        //insert holds
        String[] holds = {"Small Edge", "Medium Edge", "Large Edge", "Jug", "Pocket", "Round Sloper", "4-Finger Flat Edge", "3-Finger Pocket", "Large Slope"};
        ContentValues values = new ContentValues();
        for (String hold : holds) {
            values.put("nameHold", hold);
            mDb.insert(TABLE_HOLD, null, values);
        }
        values.clear();

        //insert workout
        String[] workoutName = {"Hang", "Pull-up", "Knee raises", "Shrugs", "90 bent arm hang", "Offset pull-up", "Straight arm hang", "Slightly bent arm hang",
                "L-sit pull-up", "Power pull-up", "L-sit", "Single arm hang"};
        String[] workoutPic = {"deadhang", "pullup", "kneeraises", "deadhang", "bent", "offsetpullup", "deadhang", "bent", "pullup", "pullup", "lsit", "onearm"};
        for (int i = 0; i < workoutName.length; i++) {
            values.put("nameWorkout", workoutName[i]);
            values.put("pictureWorkout", workoutPic[i]);
            mDb.insert(TABLE_WORKOUT, null, values);
        }

        //insert workout routine
        //easy
        String INSERT = "INSERT INTO " + TABLE_R_W + " (Routine_idRoutine,Level_idLevel,Workout_idWorkout,Hold_idHoldLeft,Hold_idHoldRight,numberRW,minuteRW,durationRW) VALUES ";


        String[] INSERT_R_W_Easy = {
                INSERT + "(" + TEN + "," + EASY + ",(SELECT idWorkout FROM Workout WHERE nameWorkout = 'Hang')," +
                        "(SELECT idHold FROM Hold WHERE nameHold = 'Jug'),(SELECT idHold FROM Hold WHERE nameHold = 'Jug'),1,0,15)",
                INSERT + "(" + TEN + "," + EASY + ",(SELECT idWorkout FROM Workout WHERE nameWorkout = 'Pull-up')," +
                        "(SELECT idHold FROM Hold WHERE nameHold = 'Round Sloper'),(SELECT idHold FROM Hold WHERE nameHold = 'Round Sloper'),2,1,71)",
                INSERT + "(" + TEN + "," + EASY + ",(SELECT idWorkout FROM Workout WHERE nameWorkout = 'Hang')," +
                        "(SELECT idHold FROM Hold WHERE nameHold = 'Medium Edge'),(SELECT idHold FROM Hold WHERE nameHold = 'Medium Edge'),3,2,10)",
                INSERT + "(" + TEN + "," + EASY + ",(SELECT idWorkout FROM Workout WHERE nameWorkout = 'Hang')," +
                        "(SELECT idHold FROM Hold WHERE nameHold = 'Pocket'),(SELECT idHold FROM Hold WHERE nameHold = 'Pocket'),4,3,15)",
                INSERT + "(" + TEN + "," + EASY + ",(SELECT idWorkout FROM Workout WHERE nameWorkout = 'Shrugs')," +
                        "(SELECT idHold FROM Hold WHERE nameHold = 'Pocket'),(SELECT idHold FROM Hold WHERE nameHold = 'Pocket'),5,3,83)",
                INSERT + "(" + TEN + "," + EASY + ",(SELECT idWorkout FROM Workout WHERE nameWorkout = 'Hang')," +
                        "(SELECT idHold FROM Hold WHERE nameHold = 'Large Edge'),(SELECT idHold FROM Hold WHERE nameHold = 'Large Edge'),6,4,20)",
                INSERT + "(" + TEN + "," + EASY + ",(SELECT idWorkout FROM Workout WHERE nameWorkout = 'Pull-up')," +
                        "(SELECT idHold FROM Hold WHERE nameHold = 'Large Edge'),(SELECT idHold FROM Hold WHERE nameHold = 'Large Edge'),7,4,72)",
                INSERT + "(" + TEN + "," + EASY + ",(SELECT idWorkout FROM Workout WHERE nameWorkout = 'Hang')," +
                        "(SELECT idHold FROM Hold WHERE nameHold = 'Round Sloper'),(SELECT idHold FROM Hold WHERE nameHold = 'Round Sloper'),8,5,10)",
                INSERT + "(" + TEN + "," + EASY + ",(SELECT idWorkout FROM Workout WHERE nameWorkout = 'Knee raises')," +
                        "(SELECT idHold FROM Hold WHERE nameHold = 'Pocket'),(SELECT idHold FROM Hold WHERE nameHold = 'Pocket'),9,5,95)",
                INSERT + "(" + TEN + "," + EASY + ",(SELECT idWorkout FROM Workout WHERE nameWorkout = 'Pull-up')," +
                        "(SELECT idHold FROM Hold WHERE nameHold = 'Large Edge'),(SELECT idHold FROM Hold WHERE nameHold = 'Large Edge'),10,6,74)",
                INSERT + "(" + TEN + "," + EASY + ",(SELECT idWorkout FROM Workout WHERE nameWorkout = 'Hang')," +
                        "(SELECT idHold FROM Hold WHERE nameHold = 'Medium Edge'),(SELECT idHold FROM Hold WHERE nameHold = 'Medium Edge'),11,7,10)",
                INSERT + "(" + TEN + "," + EASY + ",(SELECT idWorkout FROM Workout WHERE nameWorkout = 'Pull-up')," +
                        "(SELECT idHold FROM Hold WHERE nameHold = 'Jug'),(SELECT idHold FROM Hold WHERE nameHold = 'Jug'),12,8,73)",
                INSERT + "(" + TEN + "," + EASY + ",(SELECT idWorkout FROM Workout WHERE nameWorkout = 'Hang')," +
                        "(SELECT idHold FROM Hold WHERE nameHold = 'Round Sloper'),(SELECT idHold FROM Hold WHERE nameHold = 'Round Sloper'),13,9,60)"};
        for (String aINSERT_R_W : INSERT_R_W_Easy) {
            mDb.execSQL(aINSERT_R_W);
        }

        //medium
        String[] INSERT_R_W_Medium = {
                INSERT + "(" + TEN + "," + MEDIUM + ",(SELECT idWorkout FROM Workout WHERE nameWorkout = 'Hang')," +
                        "(SELECT idHold FROM Hold WHERE nameHold = 'Large Edge'),(SELECT idHold FROM Hold WHERE nameHold = 'Large Edge'),1,0,15)",
                INSERT + "(" + TEN + "," + MEDIUM + ",(SELECT idWorkout FROM Workout WHERE nameWorkout = 'Pull-up')," +
                        "(SELECT idHold FROM Hold WHERE nameHold = 'Large Edge'),(SELECT idHold FROM Hold WHERE nameHold = 'Large Edge'),2,0,73)",
                INSERT + "(" + TEN + "," + MEDIUM + ",(SELECT idWorkout FROM Workout WHERE nameWorkout = 'Pull-up')," +
                        "(SELECT idHold FROM Hold WHERE nameHold = 'Round Sloper'),(SELECT idHold FROM Hold WHERE nameHold = 'Round Sloper'),3,1,72)",
                INSERT + "(" + TEN + "," + MEDIUM + ",(SELECT idWorkout FROM Workout WHERE nameWorkout = 'Hang')," +
                        "(SELECT idHold FROM Hold WHERE nameHold = 'Medium Edge'),(SELECT idHold FROM Hold WHERE nameHold = 'Medium Edge'),4,1,20)",
                INSERT + "(" + TEN + "," + MEDIUM + ",(SELECT idWorkout FROM Workout WHERE nameWorkout = 'Hang')," +
                        "(SELECT idHold FROM Hold WHERE nameHold = 'Small Edge'),(SELECT idHold FROM Hold WHERE nameHold = 'Small Edge'),5,2,20)",
                INSERT + "(" + TEN + "," + MEDIUM + ",(SELECT idWorkout FROM Workout WHERE nameWorkout = '90 bent arm hang')," +
                        "(SELECT idHold FROM Hold WHERE nameHold = 'Pocket'),(SELECT idHold FROM Hold WHERE nameHold = 'Pocket'),6,2,15)",
                INSERT + "(" + TEN + "," + MEDIUM + ",(SELECT idWorkout FROM Workout WHERE nameWorkout = 'Hang')," +
                        "(SELECT idHold FROM Hold WHERE nameHold = 'Round Sloper'),(SELECT idHold FROM Hold WHERE nameHold = 'Round Sloper'),7,3,30)",
                INSERT + "(" + TEN + "," + MEDIUM + ",(SELECT idWorkout FROM Workout WHERE nameWorkout = 'Hang')," +
                        "(SELECT idHold FROM Hold WHERE nameHold = 'Large Edge'),(SELECT idHold FROM Hold WHERE nameHold = 'Large Edge'),8,4,20)",
                INSERT + "(" + TEN + "," + MEDIUM + ",(SELECT idWorkout FROM Workout WHERE nameWorkout = 'Pull-up')," +
                        "(SELECT idHold FROM Hold WHERE nameHold = 'Pocket'),(SELECT idHold FROM Hold WHERE nameHold = 'Pocket'),9,4,74)",
                INSERT + "(" + TEN + "," + MEDIUM + ",(SELECT idWorkout FROM Workout WHERE nameWorkout = 'Offset pull-up')," +
                        "(SELECT idHold FROM Hold WHERE nameHold = 'Jug'),(SELECT idHold FROM Hold WHERE nameHold = 'Small Edge'),10,5,73)",
                INSERT + "(" + TEN + "," + MEDIUM + ",(SELECT idWorkout FROM Workout WHERE nameWorkout = 'Offset pull-up')," +
                        "(SELECT idHold FROM Hold WHERE nameHold = 'Small Edge'),(SELECT idHold FROM Hold WHERE nameHold = 'Jug'),11,5,73)",
                INSERT + "(" + TEN + "," + MEDIUM + ",(SELECT idWorkout FROM Workout WHERE nameWorkout = 'Knee raises')," +
                        "(SELECT idHold FROM Hold WHERE nameHold = 'Jug'),(SELECT idHold FROM Hold WHERE nameHold = 'Jug'),12,6,815)",
                INSERT + "(" + TEN + "," + MEDIUM + ",(SELECT idWorkout FROM Workout WHERE nameWorkout = 'Hang')," +
                        "(SELECT idHold FROM Hold WHERE nameHold = 'Medium Edge'),(SELECT idHold FROM Hold WHERE nameHold = 'Medium Edge'),13,6,15)",
                INSERT + "(" + TEN + "," + MEDIUM + ",(SELECT idWorkout FROM Workout WHERE nameWorkout = 'Hang')," +
                        "(SELECT idHold FROM Hold WHERE nameHold = 'Medium Edge'),(SELECT idHold FROM Hold WHERE nameHold = 'Medium Edge'),14,7,25)",
                INSERT + "(" + TEN + "," + MEDIUM + ",(SELECT idWorkout FROM Workout WHERE nameWorkout = 'Hang')," +
                        "(SELECT idHold FROM Hold WHERE nameHold = 'Round Sloper'),(SELECT idHold FROM Hold WHERE nameHold = 'Round Sloper'),15,8,15)",
                INSERT + "(" + TEN + "," + MEDIUM + ",(SELECT idWorkout FROM Workout WHERE nameWorkout = 'Pull-up')," +
                        "(SELECT idHold FROM Hold WHERE nameHold = 'Jug'),(SELECT idHold FROM Hold WHERE nameHold = 'Jug'),16,8,73)",
                INSERT + "(" + TEN + "," + MEDIUM + ",(SELECT idWorkout FROM Workout WHERE nameWorkout = 'Hang')," +
                        "(SELECT idHold FROM Hold WHERE nameHold = 'Round Sloper'),(SELECT idHold FROM Hold WHERE nameHold = 'Round Sloper'),17,9,60)"};
        // int i =0;
        for (String aINSERT_R_W : INSERT_R_W_Medium) {
            mDb.execSQL(aINSERT_R_W);
            //    i++;
        }

        // Log.v("FRAGMENT","i - "+i);

        String[] INSERT_R_W_Hard = {
                INSERT + "(" + TEN + "," + HARD + ",(SELECT idWorkout FROM Workout WHERE nameWorkout = 'Straight arm hang')," +
                        "(SELECT idHold FROM Hold WHERE nameHold = 'Large Slope'),(SELECT idHold FROM Hold WHERE nameHold = 'Large Slope'),1,0,20)",
                INSERT + "(" + TEN + "," + HARD + ",(SELECT idWorkout FROM Workout WHERE nameWorkout = 'Pull-up')," +
                        "(SELECT idHold FROM Hold WHERE nameHold = '4-Finger Flat Edge'),(SELECT idHold FROM Hold WHERE nameHold = '4-Finger Flat Edge'),2,0,73)",
                INSERT + "(" + TEN + "," + HARD + ",(SELECT idWorkout FROM Workout WHERE nameWorkout = 'Slightly bent arm hang')," +
                        "(SELECT idHold FROM Hold WHERE nameHold = 'Large Slope'),(SELECT idHold FROM Hold WHERE nameHold = 'Large Slope'),3,1,20)",
                INSERT + "(" + TEN + "," + HARD + ",(SELECT idWorkout FROM Workout WHERE nameWorkout = 'L-sit')," +
                        "(SELECT idHold FROM Hold WHERE nameHold = 'Large Slope'),(SELECT idHold FROM Hold WHERE nameHold = 'Large Slope'),4,1,20)",
                INSERT + "(" + TEN + "," + HARD + ",(SELECT idWorkout FROM Workout WHERE nameWorkout = 'Pull-up')," +
                        "(SELECT idHold FROM Hold WHERE nameHold = '3-Finger Pocket'),(SELECT idHold FROM Hold WHERE nameHold = '3-Finger Pocket'),5,2,75)",
                INSERT + "(" + TEN + "," + HARD + ",(SELECT idWorkout FROM Workout WHERE nameWorkout = 'Straight arm hang')," +
                        "(SELECT idHold FROM Hold WHERE nameHold = '3-Finger Pocket'),(SELECT idHold FROM Hold WHERE nameHold = '3-Finger Pocket'),6,2,25)",
                INSERT + "(" + TEN + "," + HARD + ",(SELECT idWorkout FROM Workout WHERE nameWorkout = 'Hang')," +
                        "(SELECT idHold FROM Hold WHERE nameHold = '3-Finger Pocket'),(SELECT idHold FROM Hold WHERE nameHold = '3-Finger Pocket'),7,3,5)",
                INSERT + "(" + TEN + "," + HARD + ",(SELECT idWorkout FROM Workout WHERE nameWorkout = 'Hang')," +
                        "(SELECT idHold FROM Hold WHERE nameHold = 'Small Edge'),(SELECT idHold FROM Hold WHERE nameHold = 'Small Edge'),8,3,5)",
                INSERT + "(" + TEN + "," + HARD + ",(SELECT idWorkout FROM Workout WHERE nameWorkout = 'Hang')," +
                        "(SELECT idHold FROM Hold WHERE nameHold = 'Medium Edge'),(SELECT idHold FROM Hold WHERE nameHold = 'Medium Edge'),9,3,5)",
                INSERT + "(" + TEN + "," + HARD + ",(SELECT idWorkout FROM Workout WHERE nameWorkout = 'Hang')," +
                        "(SELECT idHold FROM Hold WHERE nameHold = 'Large Edge'),(SELECT idHold FROM Hold WHERE nameHold = 'Large Edge'),10,3,5)",
                INSERT + "(" + TEN + "," + HARD + ",(SELECT idWorkout FROM Workout WHERE nameWorkout = 'Hang')," +
                        "(SELECT idHold FROM Hold WHERE nameHold = 'Large Slope'),(SELECT idHold FROM Hold WHERE nameHold = 'Large Slope'),11,3,20)",
                INSERT + "(" + TEN + "," + HARD + ",(SELECT idWorkout FROM Workout WHERE nameWorkout = 'Single arm hang')," +
                        "(SELECT idHold FROM Hold WHERE nameHold = '4-Finger Flat Edge'),(SELECT idHold FROM Hold WHERE nameHold = '4-Finger Flat Edge'),12,4,20)",
                INSERT + "(" + TEN + "," + HARD + ",(SELECT idWorkout FROM Workout WHERE nameWorkout = 'Single arm hang')," +
                        "(SELECT idHold FROM Hold WHERE nameHold = '4-Finger Flat Edge'),(SELECT idHold FROM Hold WHERE nameHold = '4-Finger Flat Edge'),13,4,20)",
                INSERT + "(" + TEN + "," + HARD + ",(SELECT idWorkout FROM Workout WHERE nameWorkout = 'Offset pull-up')," +
                        "(SELECT idHold FROM Hold WHERE nameHold = 'Large Slope'),(SELECT idHold FROM Hold WHERE nameHold = '3-Finger Pocket'),14,5,75)",
                INSERT + "(" + TEN + "," + HARD + ",(SELECT idWorkout FROM Workout WHERE nameWorkout = 'Offset pull-up')," +
                        "(SELECT idHold FROM Hold WHERE nameHold = '3-Finger Pocket'),(SELECT idHold FROM Hold WHERE nameHold = 'Large Slope'),15,5,75)",
                INSERT + "(" + TEN + "," + HARD + ",(SELECT idWorkout FROM Workout WHERE nameWorkout = '90 bent arm hang')," +
                        "(SELECT idHold FROM Hold WHERE nameHold = '4-Finger Flat Edge'),(SELECT idHold FROM Hold WHERE nameHold = '4-Finger Flat Edge'),16,6,30)",
                INSERT + "(" + TEN + "," + HARD + ",(SELECT idWorkout FROM Workout WHERE nameWorkout = 'Straight arm hang')," +
                        "(SELECT idHold FROM Hold WHERE nameHold = '3-Finger Pocket'),(SELECT idHold FROM Hold WHERE nameHold = '3-Finger Pocket'),17,6,15)",
                INSERT + "(" + TEN + "," + HARD + ",(SELECT idWorkout FROM Workout WHERE nameWorkout = 'L-sit pull-up')," +
                        "(SELECT idHold FROM Hold WHERE nameHold = 'Large Slope'),(SELECT idHold FROM Hold WHERE nameHold = 'Large Slope'),18,7,73)",
                INSERT + "(" + TEN + "," + HARD + ",(SELECT idWorkout FROM Workout WHERE nameWorkout = 'Straight arm hang')," +
                        "(SELECT idHold FROM Hold WHERE nameHold = 'Large Slope'),(SELECT idHold FROM Hold WHERE nameHold = 'Large Slope'),19,7,15)",
                INSERT + "(" + TEN + "," + HARD + ",(SELECT idWorkout FROM Workout WHERE nameWorkout = 'Straight arm hang')," +
                        "(SELECT idHold FROM Hold WHERE nameHold = '3-Finger Pocket'),(SELECT idHold FROM Hold WHERE nameHold = '3-Finger Pocket'),20,8,20)",
                INSERT + "(" + TEN + "," + HARD + ",(SELECT idWorkout FROM Workout WHERE nameWorkout = 'Power pull-up')," +
                        "(SELECT idHold FROM Hold WHERE nameHold = '3-Finger Pocket'),(SELECT idHold FROM Hold WHERE nameHold = '3-Finger Pocket'),21,8,73)",
                INSERT + "(" + TEN + "," + HARD + ",(SELECT idWorkout FROM Workout WHERE nameWorkout = 'Slightly bent arm hang')," +
                        "(SELECT idHold FROM Hold WHERE nameHold = 'Large Slope'),(SELECT idHold FROM Hold WHERE nameHold = 'Large Slope'),22,9,59)"
        };
        // int j =0;

        for (String aINSERT_R_W : INSERT_R_W_Hard) {
            mDb.execSQL(aINSERT_R_W);
            // Log.v("FRG","j - "+j);
            // j++;
        }


        //hard
    }

    //3D simulator
    public void addmin3d() {

        String RoutineName = "'10 minutes with 3D Simulator'";
        String BoardName = "3D Simulator";
        String TEN = "(SELECT idRoutine FROM Routine WHERE nameRoutine = " + RoutineName + ")";
        String EASY = "(SELECT idLevel FROM Level WHERE nameLevel = 'easy')";
        String MEDIUM = "(SELECT idLevel FROM Level WHERE nameLevel = 'medium')";
        String HARD = "(SELECT idLevel FROM Level WHERE nameLevel = 'hard')";

        //insert board
        String INSERT_BOARD = "INSERT INTO " + TABLE_BOARD + " (nameBoard,manyfactureBoard,pictureBoard)" +
                "VALUES ('" + BoardName + "','METOLIUS','simulator3d_pic')";
        mDb.execSQL(INSERT_BOARD);

        //insert routine
        String INSERT_ROUTINE = "INSERT INTO " + TABLE_ROUTINE +
                " (nameRoutine, authorRoutine, Board_idBoard) VALUES (" + RoutineName + ",'METOLIUS', (SELECT idBoard FROM " + TABLE_BOARD + " WHERE nameBoard = '" + BoardName + "'))";
        mDb.execSQL(INSERT_ROUTINE);


        //insert level
        String[] INSERT_LEVEL = {"INSERT INTO " + TABLE_LEVEL + " (nameLevel, pictureLevel) VALUES ('easy','buttonstyle_easy_logo')",
                "INSERT INTO " + TABLE_LEVEL + " (nameLevel,pictureLevel) VALUES ('medium','buttonstyle_medium_logo')",
                "INSERT INTO " + TABLE_LEVEL + " (nameLevel,pictureLevel) VALUES ('hard','buttonstyle_hard_logo')"};
        for (String aINSERT : INSERT_LEVEL) {
            mDb.execSQL(aINSERT);
        }

        //insert routine_level
        String[] INSERT_ROUTINE_LEVEL = {
                "INSERT INTO " + TABLE_ROUTINE_LEVEL + " (Routine_idRoutine,Level_idLevel) VALUES (" + TEN + "," + EASY + ")",
                "INSERT INTO " + TABLE_ROUTINE_LEVEL + " (Routine_idRoutine,Level_idLevel) VALUES (" + TEN + "," + MEDIUM + ")",
                "INSERT INTO " + TABLE_ROUTINE_LEVEL + " (Routine_idRoutine,Level_idLevel) VALUES (" + TEN + "," + HARD + ")",};
        for (String aINSERT : INSERT_ROUTINE_LEVEL) {
            mDb.execSQL(aINSERT);
        }

        //insert holds
        String[] holds = {"Jug (1)", "Flat slope (2)", "Round sloper (3)", "Deep 3 fingers pocket (4)", "Medium edge (5)",
                "Shallow edge (6)", "Deep flat edge (7)", "Shallow 3 finger pocket (8)", "Extra deep 3 fingers pocket (9)",
                "Shallow 3 finger edge (10)", "Extra shallow edge (11)", "Deep 2 fingers pocket (12)", "Shallow 3 finger edge (13)",
                "Center Jug (14)", "Deep pocket (15)", "Pocket (16)", "Shallow pocket (17)", "2 fingers pocket(18)", "any hold"};
        ContentValues values = new ContentValues();
        for (String hold : holds) {
            values.put("nameHold", hold);
            mDb.insert(TABLE_HOLD, null, values);
        }
        values.clear();

        //insert workout
        String[] workoutName = {"Hang", "Pull-up", "Knee raises", "Shrugs", "90 bent arm hang", "Offset pull-up", "L-sit pull-up", "Power pull-up", "L-sit", "Offset hang", "One arm hang", "rest hang"};
        String[] workoutPic = {"deadhang", "pullup", "kneeraises", "deadhang", "bent", "offsetpullup", "pullup", "pullup", "lsit", "offsethang", "onearm", "deadhang"};
        for (int i = 0; i < workoutName.length; i++) {
            values.put("nameWorkout", workoutName[i]);
            values.put("pictureWorkout", workoutPic[i]);
            mDb.insert(TABLE_WORKOUT, null, values);
        }

        //insert workout routine
        //easy
        String INSERT = "INSERT INTO " + TABLE_R_W + " (Routine_idRoutine,Level_idLevel,Workout_idWorkout,Hold_idHoldLeft,Hold_idHoldRight,numberRW,minuteRW,durationRW) VALUES ";
        String[] INSERT_R_W_Easy = {
                INSERT + "(" + TEN + "," + EASY + ",(SELECT idWorkout FROM Workout WHERE nameWorkout = 'Hang')," +
                        "(SELECT idHold FROM Hold WHERE nameHold = 'Deep flat edge (7)'),(SELECT idHold FROM Hold WHERE nameHold = 'Deep flat edge (7)'),1,0,10)",
                INSERT + "(" + TEN + "," + EASY + ",(SELECT idWorkout FROM Workout WHERE nameWorkout = 'Hang')," +
                        "(SELECT idHold FROM Hold WHERE nameHold = 'Jug (1)'),(SELECT idHold FROM Hold WHERE nameHold = 'Jug (1)'),2,1,15)",
                INSERT + "(" + TEN + "," + EASY + ",(SELECT idWorkout FROM Workout WHERE nameWorkout = 'Pull-up')," +
                        "(SELECT idHold FROM Hold WHERE nameHold = 'Jug (1)'),(SELECT idHold FROM Hold WHERE nameHold = 'Jug (1)'),3,1,71)",
                INSERT + "(" + TEN + "," + EASY + ",(SELECT idWorkout FROM Workout WHERE nameWorkout = 'Offset pull-up')," +
                        "(SELECT idHold FROM Hold WHERE nameHold = 'Center Jug (14)'),(SELECT idHold FROM Hold WHERE nameHold = 'Deep 3 fingers pocket (4)'),4,2,71)",
                INSERT + "(" + TEN + "," + EASY + ",(SELECT idWorkout FROM Workout WHERE nameWorkout = 'Offset pull-up')," +
                        "(SELECT idHold FROM Hold WHERE nameHold = 'Deep 3 fingers pocket (4)'),(SELECT idHold FROM Hold WHERE nameHold = 'Center Jug (14)'),5,2,71)",
                INSERT + "(" + TEN + "," + EASY + ",(SELECT idWorkout FROM Workout WHERE nameWorkout = 'Hang')," +
                        "(SELECT idHold FROM Hold WHERE nameHold = 'Extra deep 3 fingers pocket (9)'),(SELECT idHold FROM Hold WHERE nameHold = 'Extra deep 3 fingers pocket (9)'),6,3,15)",
                INSERT + "(" + TEN + "," + EASY + ",(SELECT idWorkout FROM Workout WHERE nameWorkout = 'Hang')," +
                        "(SELECT idHold FROM Hold WHERE nameHold = 'Flat slope (2)'),(SELECT idHold FROM Hold WHERE nameHold = 'Flat slope (2)'),7,4,12)",
                INSERT + "(" + TEN + "," + EASY + ",(SELECT idWorkout FROM Workout WHERE nameWorkout = 'Knee raises')," +
                        "(SELECT idHold FROM Hold WHERE nameHold = 'Jug (1)'),(SELECT idHold FROM Hold WHERE nameHold = 'Jug (1)'),8,4,95)",
                INSERT + "(" + TEN + "," + EASY + ",(SELECT idWorkout FROM Workout WHERE nameWorkout = 'Offset hang')," +
                        "(SELECT idHold FROM Hold WHERE nameHold = 'Deep pocket (15)'),(SELECT idHold FROM Hold WHERE nameHold = 'Shallow edge (6)'),9,5,8)",
                INSERT + "(" + TEN + "," + EASY + ",(SELECT idWorkout FROM Workout WHERE nameWorkout = 'Offset hang')," +
                        "(SELECT idHold FROM Hold WHERE nameHold = 'Shallow edge (6)'),(SELECT idHold FROM Hold WHERE nameHold = 'Deep pocket (15)'),10,5,8)",
                INSERT + "(" + TEN + "," + EASY + ",(SELECT idWorkout FROM Workout WHERE nameWorkout = 'Pull-up')," +
                        "(SELECT idHold FROM Hold WHERE nameHold = 'Jug (1)'),(SELECT idHold FROM Hold WHERE nameHold = 'Jug (1)'),11,6,73)",
                INSERT + "(" + TEN + "," + EASY + ",(SELECT idWorkout FROM Workout WHERE nameWorkout = '90 bent arm hang')," +
                        "(SELECT idHold FROM Hold WHERE nameHold = 'Round sloper (3)'),(SELECT idHold FROM Hold WHERE nameHold = 'Round sloper (3)'),12,7,8)",
                INSERT + "(" + TEN + "," + EASY + ",(SELECT idWorkout FROM Workout WHERE nameWorkout = 'Pull-up')," +
                        "(SELECT idHold FROM Hold WHERE nameHold = 'Extra deep 3 fingers pocket (9)'),(SELECT idHold FROM Hold WHERE nameHold = 'Extra deep 3 fingers pocket (9)'),13,8,71)",
                INSERT + "(" + TEN + "," + EASY + ",(SELECT idWorkout FROM Workout WHERE nameWorkout = 'Hang')," +
                        "(SELECT idHold FROM Hold WHERE nameHold = 'Extra deep 3 fingers pocket (9)'),(SELECT idHold FROM Hold WHERE nameHold = 'Extra deep 3 fingers pocket (9)'),14,8,10)",
                INSERT + "(" + TEN + "," + EASY + ",(SELECT idWorkout FROM Workout WHERE nameWorkout = 'Hang')," +
                        "(SELECT idHold FROM Hold WHERE nameHold = 'any hold'),(SELECT idHold FROM Hold WHERE nameHold = 'any hold'),15,9,60)"};
        //int i=0;
        for (String aINSERT_R_W : INSERT_R_W_Easy) {
            mDb.execSQL(aINSERT_R_W);
            //   i++;
            //   Log.v("SEQ","i - "+i);
        }

        //medium
        String[] INSERT_R_W_Medium = {
                INSERT + "(" + TEN + "," + MEDIUM + ",(SELECT idWorkout FROM Workout WHERE nameWorkout = 'Hang')," +
                        "(SELECT idHold FROM Hold WHERE nameHold = 'Medium edge (5)'),(SELECT idHold FROM Hold WHERE nameHold = 'Medium edge (5)'),1,0,25)",
                INSERT + "(" + TEN + "," + MEDIUM + ",(SELECT idWorkout FROM Workout WHERE nameWorkout = 'Hang')," +
                        "(SELECT idHold FROM Hold WHERE nameHold = 'Flat slope (2)'),(SELECT idHold FROM Hold WHERE nameHold = 'Flat slope (2)'),2,1,20)",
                INSERT + "(" + TEN + "," + MEDIUM + ",(SELECT idWorkout FROM Workout WHERE nameWorkout = 'Pull-up')," +
                        "(SELECT idHold FROM Hold WHERE nameHold = 'Flat slope (2)'),(SELECT idHold FROM Hold WHERE nameHold = 'Flat slope (2)'),3,1,73)",
                INSERT + "(" + TEN + "," + MEDIUM + ",(SELECT idWorkout FROM Workout WHERE nameWorkout = '90 bent arm hang')," +
                        "(SELECT idHold FROM Hold WHERE nameHold = 'Shallow edge (6)'),(SELECT idHold FROM Hold WHERE nameHold = 'Shallow edge (6)'),4,2,15)",
                INSERT + "(" + TEN + "," + MEDIUM + ",(SELECT idWorkout FROM Workout WHERE nameWorkout = 'Knee raises')," +
                        "(SELECT idHold FROM Hold WHERE nameHold = 'Jug (1)'),(SELECT idHold FROM Hold WHERE nameHold = 'Jug (1)'),5,2,810)",
                INSERT + "(" + TEN + "," + MEDIUM + ",(SELECT idWorkout FROM Workout WHERE nameWorkout = 'Hang')," +
                        "(SELECT idHold FROM Hold WHERE nameHold = 'Flat slope (2)'),(SELECT idHold FROM Hold WHERE nameHold = 'Flat slope (2)'),6,3,15)",
                INSERT + "(" + TEN + "," + MEDIUM + ",(SELECT idWorkout FROM Workout WHERE nameWorkout = 'Hang')," +
                        "(SELECT idHold FROM Hold WHERE nameHold = 'Round sloper (3)'),(SELECT idHold FROM Hold WHERE nameHold = 'Round sloper (3)'),7,3,15)",
                INSERT + "(" + TEN + "," + MEDIUM + ",(SELECT idWorkout FROM Workout WHERE nameWorkout = 'Offset hang')," +
                        "(SELECT idHold FROM Hold WHERE nameHold = 'Jug (1)'),(SELECT idHold FROM Hold WHERE nameHold = 'Shallow pocket (17)'),8,4,20)",
                INSERT + "(" + TEN + "," + MEDIUM + ",(SELECT idWorkout FROM Workout WHERE nameWorkout = 'Offset hang')," +
                        "(SELECT idHold FROM Hold WHERE nameHold = 'Shallow pocket (17)'),(SELECT idHold FROM Hold WHERE nameHold = 'Jug (1)'),9,4,20)",
                INSERT + "(" + TEN + "," + MEDIUM + ",(SELECT idWorkout FROM Workout WHERE nameWorkout = 'Offset hang')," +
                        "(SELECT idHold FROM Hold WHERE nameHold = 'Deep 3 fingers pocket (4)'),(SELECT idHold FROM Hold WHERE nameHold = 'Extra deep 3 fingers pocket (9)'),10,5,15)",
                INSERT + "(" + TEN + "," + MEDIUM + ",(SELECT idWorkout FROM Workout WHERE nameWorkout = 'Offset hang')," +
                        "(SELECT idHold FROM Hold WHERE nameHold = 'Extra deep 3 fingers pocket (9)'),(SELECT idHold FROM Hold WHERE nameHold = 'Deep 3 fingers pocket (4)'),11,5,15)",
                INSERT + "(" + TEN + "," + MEDIUM + ",(SELECT idWorkout FROM Workout WHERE nameWorkout = 'Pull-up')," +
                        "(SELECT idHold FROM Hold WHERE nameHold = 'Medium edge (5)'),(SELECT idHold FROM Hold WHERE nameHold = 'Medium edge (5)'),12,6,74)",
                INSERT + "(" + TEN + "," + MEDIUM + ",(SELECT idWorkout FROM Workout WHERE nameWorkout = 'Knee raises')," +
                        "(SELECT idHold FROM Hold WHERE nameHold = 'any hold'),(SELECT idHold FROM Hold WHERE nameHold = 'any hold'),13,6,810)",
                INSERT + "(" + TEN + "," + MEDIUM + ",(SELECT idWorkout FROM Workout WHERE nameWorkout = 'Hang')," +
                        "(SELECT idHold FROM Hold WHERE nameHold = 'Deep flat edge (7)'),(SELECT idHold FROM Hold WHERE nameHold = 'Deep flat edge (7)'),14,7,30)",
                INSERT + "(" + TEN + "," + MEDIUM + ",(SELECT idWorkout FROM Workout WHERE nameWorkout = 'One arm hang')," +
                        "(SELECT idHold FROM Hold WHERE nameHold = 'Jug (1)'),(SELECT idHold FROM Hold WHERE nameHold = 'Jug (1)'),15,8,10)",
                INSERT + "(" + TEN + "," + MEDIUM + ",(SELECT idWorkout FROM Workout WHERE nameWorkout = 'One arm hang')," +
                        "(SELECT idHold FROM Hold WHERE nameHold = 'Jug (1)'),(SELECT idHold FROM Hold WHERE nameHold = 'Jug (1)'),16,8,10)",
                INSERT + "(" + TEN + "," + MEDIUM + ",(SELECT idWorkout FROM Workout WHERE nameWorkout = 'Pull-up')," +
                        "(SELECT idHold FROM Hold WHERE nameHold = 'Deep flat edge (7)'),(SELECT idHold FROM Hold WHERE nameHold = 'Deep flat edge (7)'),17,9,74)",
                INSERT + "(" + TEN + "," + MEDIUM + ",(SELECT idWorkout FROM Workout WHERE nameWorkout = 'Hang')," +
                        "(SELECT idHold FROM Hold WHERE nameHold = 'Round sloper (3)'),(SELECT idHold FROM Hold WHERE nameHold = 'Round sloper (3)'),18,9,40)"};
        // int i =0;
        for (String aINSERT_R_W : INSERT_R_W_Medium) {
            mDb.execSQL(aINSERT_R_W);
            //  i++;
            //  Log.v("SEE","i - "+i);
        }

        //hard
        String[] INSERT_R_W_Hard = {
                INSERT + "(" + TEN + "," + HARD + ",(SELECT idWorkout FROM Workout WHERE nameWorkout = 'Hang')," +
                        "(SELECT idHold FROM Hold WHERE nameHold = 'Shallow edge (6)'),(SELECT idHold FROM Hold WHERE nameHold = 'Shallow edge (6)'),1,0,25)",
                INSERT + "(" + TEN + "," + HARD + ",(SELECT idWorkout FROM Workout WHERE nameWorkout = 'Pull-up')," +
                        "(SELECT idHold FROM Hold WHERE nameHold = 'Extra deep 3 fingers pocket (9)'),(SELECT idHold FROM Hold WHERE nameHold = 'Extra deep 3 fingers pocket (9)'),2,0,75)",
                INSERT + "(" + TEN + "," + HARD + ",(SELECT idWorkout FROM Workout WHERE nameWorkout = 'Offset pull-up')," +
                        "(SELECT idHold FROM Hold WHERE nameHold = 'Deep pocket (15)'),(SELECT idHold FROM Hold WHERE nameHold = 'Deep 2 fingers pocket (12)'),3,1,85)",
                INSERT + "(" + TEN + "," + HARD + ",(SELECT idWorkout FROM Workout WHERE nameWorkout = 'Offset pull-up')," +
                        "(SELECT idHold FROM Hold WHERE nameHold = 'Deep 2 fingers pocket (12)'),(SELECT idHold FROM Hold WHERE nameHold = 'Deep pocket (15)'),4,1,85)",
                INSERT + "(" + TEN + "," + HARD + ",(SELECT idWorkout FROM Workout WHERE nameWorkout = 'Hang')," +
                        "(SELECT idHold FROM Hold WHERE nameHold = 'Extra shallow edge (11)'),(SELECT idHold FROM Hold WHERE nameHold = 'Extra shallow edge (11)'),5,2,45)",
                INSERT + "(" + TEN + "," + HARD + ",(SELECT idWorkout FROM Workout WHERE nameWorkout = 'Offset pull-up')," +
                        "(SELECT idHold FROM Hold WHERE nameHold = 'Round sloper (3)'),(SELECT idHold FROM Hold WHERE nameHold = 'Deep 3 fingers pocket (4)'),6,3,85)",
                INSERT + "(" + TEN + "," + HARD + ",(SELECT idWorkout FROM Workout WHERE nameWorkout = 'Offset pull-up')," +
                        "(SELECT idHold FROM Hold WHERE nameHold = 'Deep 3 fingers pocket (4)'),(SELECT idHold FROM Hold WHERE nameHold = 'Round sloper (3)'),7,3,85)",
                INSERT + "(" + TEN + "," + HARD + ",(SELECT idWorkout FROM Workout WHERE nameWorkout = 'Hang')," +
                        "(SELECT idHold FROM Hold WHERE nameHold = 'Extra shallow edge (11)'),(SELECT idHold FROM Hold WHERE nameHold = 'Extra shallow edge (11)'),8,4,10)",
                INSERT + "(" + TEN + "," + HARD + ",(SELECT idWorkout FROM Workout WHERE nameWorkout = 'Hang')," +
                        "(SELECT idHold FROM Hold WHERE nameHold = 'Extra deep 3 fingers pocket (9)'),(SELECT idHold FROM Hold WHERE nameHold = 'Extra deep 3 fingers pocket (9)'),9,4,10)",
                INSERT + "(" + TEN + "," + HARD + ",(SELECT idWorkout FROM Workout WHERE nameWorkout = 'Hang')," +
                        "(SELECT idHold FROM Hold WHERE nameHold = 'Shallow edge (6)'),(SELECT idHold FROM Hold WHERE nameHold = 'Shallow edge (6)'),10,4,10)",
                INSERT + "(" + TEN + "," + HARD + ",(SELECT idWorkout FROM Workout WHERE nameWorkout = 'Hang')," +
                        "(SELECT idHold FROM Hold WHERE nameHold = 'Flat slope (2)'),(SELECT idHold FROM Hold WHERE nameHold = 'Flat slope (2)'),11,4,15)",
                INSERT + "(" + TEN + "," + HARD + ",(SELECT idWorkout FROM Workout WHERE nameWorkout = 'One arm hang')," +
                        "(SELECT idHold FROM Hold WHERE nameHold = 'Round sloper (3)'),(SELECT idHold FROM Hold WHERE nameHold = 'Round sloper (3)'),12,5,15)",
                INSERT + "(" + TEN + "," + HARD + ",(SELECT idWorkout FROM Workout WHERE nameWorkout = 'rest hang')," +
                        "(SELECT idHold FROM Hold WHERE nameHold = 'any hold'),(SELECT idHold FROM Hold WHERE nameHold = 'any hold'),13,5,10)",
                INSERT + "(" + TEN + "," + HARD + ",(SELECT idWorkout FROM Workout WHERE nameWorkout = 'One arm hang')," +
                        "(SELECT idHold FROM Hold WHERE nameHold = 'Round sloper (3)'),(SELECT idHold FROM Hold WHERE nameHold = 'Round sloper (3)'),14,5,15)",
                INSERT + "(" + TEN + "," + HARD + ",(SELECT idWorkout FROM Workout WHERE nameWorkout = 'L-sit pull-up')," +
                        "(SELECT idHold FROM Hold WHERE nameHold = 'Jug (1)'),(SELECT idHold FROM Hold WHERE nameHold = 'Jug (1)'),15,6,75)",
                INSERT + "(" + TEN + "," + HARD + ",(SELECT idWorkout FROM Workout WHERE nameWorkout = '90 bent arm hang')," +
                        "(SELECT idHold FROM Hold WHERE nameHold = 'Deep 2 fingers pocket (12)'),(SELECT idHold FROM Hold WHERE nameHold = 'Deep 2 fingers pocket (12)'),16,6,20)",
                INSERT + "(" + TEN + "," + HARD + ",(SELECT idWorkout FROM Workout WHERE nameWorkout = '90 bent arm hang')," +
                        "(SELECT idHold FROM Hold WHERE nameHold = 'Shallow 3 finger pocket (8)'),(SELECT idHold FROM Hold WHERE nameHold = 'Shallow 3 finger pocket (8)'),17,7,20)",
                INSERT + "(" + TEN + "," + HARD + ",(SELECT idWorkout FROM Workout WHERE nameWorkout = 'Hang')," +
                        "(SELECT idHold FROM Hold WHERE nameHold = 'Extra deep 3 fingers pocket (9)'),(SELECT idHold FROM Hold WHERE nameHold = 'Extra deep 3 fingers pocket (9)'),18,7,25)",
                INSERT + "(" + TEN + "," + HARD + ",(SELECT idWorkout FROM Workout WHERE nameWorkout = 'Hang')," +
                        "(SELECT idHold FROM Hold WHERE nameHold = '2 fingers pocket(18)'),(SELECT idHold FROM Hold WHERE nameHold = 'Shallow pocket (17)'),19,8,10)",
                INSERT + "(" + TEN + "," + HARD + ",(SELECT idWorkout FROM Workout WHERE nameWorkout = 'Hang')," +
                        "(SELECT idHold FROM Hold WHERE nameHold = 'Shallow pocket (17)'),(SELECT idHold FROM Hold WHERE nameHold = '2 fingers pocket(18)'),20,8,10)",
                INSERT + "(" + TEN + "," + HARD + ",(SELECT idWorkout FROM Workout WHERE nameWorkout = 'Power pull-up')," +
                        "(SELECT idHold FROM Hold WHERE nameHold = 'Jug (1)'),(SELECT idHold FROM Hold WHERE nameHold = 'Jug (1)'),21,8,73)",
                INSERT + "(" + TEN + "," + HARD + ",(SELECT idWorkout FROM Workout WHERE nameWorkout = 'Pull-up')," +
                        "(SELECT idHold FROM Hold WHERE nameHold = 'Jug (1)'),(SELECT idHold FROM Hold WHERE nameHold = 'Jug (1)'),22,9,78)",
                INSERT + "(" + TEN + "," + HARD + ",(SELECT idWorkout FROM Workout WHERE nameWorkout = 'Hang')," +
                        "(SELECT idHold FROM Hold WHERE nameHold = 'Round sloper (3)'),(SELECT idHold FROM Hold WHERE nameHold = 'Round sloper (3)'),23,9,30)"
        };
        // int j =0;

        for (String aINSERT_R_W : INSERT_R_W_Hard) {
            mDb.execSQL(aINSERT_R_W);
            // j++;
            // Log.v("SEE","j - "+j);
        }


    }

    public ArrayList<RoutineBeen> selectAllRoutine() {
        ArrayList<RoutineBeen> allR = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_ROUTINE;
        Cursor c = mDb.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
            do {
                String selectRLQuery = "SELECT l.idLevel, l.nameLevel, l.pictureLevel FROM " + TABLE_ROUTINE_LEVEL + " AS rl" +
                        " LEFT OUTER JOIN " + TABLE_LEVEL + " AS l ON rl.Level_idLevel = l.idLevel" +
                        " WHERE rl.Routine_idRoutine = " + c.getInt(0);
                Cursor cc = mDb.rawQuery(selectRLQuery, null);
                ArrayList<LevelBeen> ll = new ArrayList<>();
                if (cc.moveToFirst()) {

                    do {
                        ll.add(new LevelBeen(cc.getInt(0), cc.getString(1)));
                    } while (cc.moveToNext());
                }
                cc.close();
                allR.add(new RoutineBeen(c.getInt(0), c.getString(1), c.getString(2), c.getInt(3), ll));

            } while (c.moveToNext());
        }
        c.close();

        return allR;
    }

    public RoutineBeen selectRoutineByID(int id) {
        RoutineBeen aR = null;
        String selectQuery = "SELECT * FROM " + TABLE_ROUTINE +
                " WHERE idRoutine = " + id;
        Cursor c = mDb.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
            String selectRLQuery = "SELECT l.idLevel, l.nameLevel, l.pictureLevel FROM " + TABLE_ROUTINE_LEVEL + " AS rl" +
                    " LEFT OUTER JOIN " + TABLE_LEVEL + " AS l ON rl.Level_idLevel = l.idLevel" +
                    " WHERE rl.Routine_idRoutine = " + c.getInt(0);
            Cursor cc = mDb.rawQuery(selectRLQuery, null);
            ArrayList<LevelBeen> ll = new ArrayList<>();
            if (cc.moveToFirst()) {

                do {
                    ll.add(new LevelBeen(cc.getInt(0), cc.getString(1)));
                } while (cc.moveToNext());
            }
            cc.close();
            aR = new RoutineBeen(c.getInt(0), c.getString(1), c.getString(2), c.getInt(3), ll);
        }
        c.close();
        return aR;
    }

    public LevelBeen selectLevelByID(int id) {
        LevelBeen aR = null;
        String selectQuery = "SELECT * FROM " + TABLE_LEVEL +
                " WHERE idLevel = " + id;
        Cursor c = mDb.rawQuery(selectQuery, null);
        // Cursor c = mDb.rawQuery("SELECT * FROM ? WHERE idLevel = ?", new String[]{TABLE_LEVEL, Integer.toString(id)});
        if (c.moveToFirst()) {

            aR = new LevelBeen(c.getInt(0), c.getString(1));
        }
        c.close();
        return aR;
    }

    public BoardBeen selectBoardByID(int id) {
        BoardBeen bR = null;
        String selectQuery = "SELECT * FROM " + TABLE_BOARD +
                " WHERE idBoard = " + id;
        Cursor c = mDb.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
            bR = new BoardBeen(c.getInt(0), c.getString(1), c.getString(2), c.getString(3));
        }
        c.close();
        return bR;
    }

  /*  public RoutineBeen selectRoutineByName(String name){
        RoutineBeen aR = null;
        String selectQuery = "SELECT * FROM "+TABLE_ROUTINE +
                " WHERE nameRoutine = '"+name+"'";
        Cursor c = mDb.rawQuery(selectQuery, null);
        if (c.moveToFirst()){
            String selectRLQuery = "SELECT l.idLevel, l.nameLevel, l.pictureLevel FROM "+TABLE_ROUTINE_LEVEL+" AS rl"+
                    " LEFT OUTER JOIN "+TABLE_LEVEL+" AS l ON rl.Level_idLevel = l.idLevel"+
                    " WHERE rl.Routine_idRoutine = "+c.getInt(0);
            Cursor cc = mDb.rawQuery(selectRLQuery,null);
            ArrayList<LevelBeen> ll = new ArrayList<>();
            if (cc.moveToFirst()){

                do{
                    ll.add(new LevelBeen(cc.getInt(0),cc.getString(1),cc.getString(2)));
                }while (cc.moveToNext());
            }
            //Log.v("FREAGMENT",c.getString(1));
            cc.close();
            aR = new RoutineBeen(c.getInt(0),c.getString(1),c.getString(2),c.getInt(3),ll);
        }
        c.close();
        return aR;
    }&*/

    public void updateRoutineWorkout(ArrayList<RoutineWorkoutBeenNew> a) {
        ContentValues data = new ContentValues();
        for (int i = 0; i < a.size(); i++) {
            for (int j = 0; j < a.get(i).getWorkouts().size(); j++) {
                if (a.get(i).getWorkouts().get(j).isChanged()) {
                    switch (a.get(i).getWorkouts().get(j).getWType()) {
                        case Normal:
                            WorkoutsNormal currWCN = (WorkoutsNormal) a.get(i).getWorkouts().get(j);
                            //ContentValues data=new ContentValues();
                            data.put("durationRW", currWCN.getCurrDuration());
                            mDb.update(TABLE_R_W, data, "idRoutineWorkout=" + currWCN.getIdRoutineWorkout(), null);
                            break;
                        case Count:
                            WorkoutsCounter currWCC = (WorkoutsCounter) a.get(i).getWorkouts().get(j);
                            if (currWCC.getWorkout().getNameWorkout().toLowerCase().contains("pull")) {
                                if (currWCC.getCount() < 10) {
                                    data.put("durationRW", 70 + currWCC.getCount());
                                } else
                                    data.put("durationRW", 700 + currWCC.getCount());
                                mDb.update(TABLE_R_W, data, "idRoutineWorkout=" + currWCC.getIdRoutineWorkout(), null);
                            } else if (currWCC.getWorkout().getNameWorkout().toLowerCase().contains("shrug")) {
                                if (currWCC.getCount() < 10) {
                                    data.put("durationRW", 80 + currWCC.getCount());
                                } else
                                    data.put("durationRW", 800 + currWCC.getCount());
                                mDb.update(TABLE_R_W, data, "idRoutineWorkout=" + currWCC.getIdRoutineWorkout(), null);
                            } else if (currWCC.getWorkout().getNameWorkout().toLowerCase().contains("knee")) {
                                if (currWCC.getCount() < 10) {
                                    data.put("durationRW", 90 + currWCC.getCount());
                                } else
                                    data.put("durationRW", 900 + currWCC.getCount());
                                mDb.update(TABLE_R_W, data, "idRoutineWorkout=" + currWCC.getIdRoutineWorkout(), null);
                            }

                            //ContentValues datac = new ContentValues();
                            break;
                    }
                }
            }
        }

    }

    public ArrayList<RoutineWorkoutBeenNew> selectAllRW(int routine, int level) {
        int m = 0;
        int h = 0;
        boolean isFirst = true;
        WorkoutsInner aWorkout;
        ArrayList<RoutineWorkoutBeenNew> allRW = new ArrayList<>();
        allRW.add(new RoutineWorkoutBeenNew(0));
        String selectQuery = "SELECT rw.*, w.nameWorkout, w.pictureWorkout, hl.nameHold, hr.nameHold FROM " + TABLE_R_W + " AS rw" +
                " LEFT OUTER JOIN " + TABLE_WORKOUT + " AS w ON rw.Workout_idWorkout = w.idWorkout" +
                " LEFT OUTER JOIN " + TABLE_HOLD + " AS hl ON rw.Hold_idHoldLeft = hl.idHold" +
                " LEFT OUTER JOIN " + TABLE_HOLD + " AS hr ON rw.Hold_idHoldRight = hr.idHold" +
                " WHERE rw.Level_idLevel = " + level + " AND rw.Routine_idRoutine = " + routine;
        Cursor c = mDb.rawQuery(selectQuery, null);
        //Log.v("DB", "Selected row -" + c.getCount());
        if (c.moveToFirst()) {
            do {
                if (c.getInt(c.getColumnIndex("minuteRW")) > m) {
                    //Log.v("DB", " h = " + h);
                    WorkoutsInner rest = new WorkoutsRest(60 - h);
                    allRW.get(m).addWorkout(rest);
                    m = c.getInt(c.getColumnIndex("minuteRW"));
                    h = 0;
                    Struct st = timerCalc(c.getInt(c.getColumnIndex("durationRW")));
                    allRW.add(new RoutineWorkoutBeenNew(m));

                    if (st.isCount) {
                        aWorkout = new WorkoutsCounter(c.getInt(c.getColumnIndex("idRoutineWorkout")), c.getInt(c.getColumnIndex("numberRW")),
                                st.timer, st.count,
                                new WorkoutBeen(c.getInt(c.getColumnIndex("Workout_idWorkout")), c.getString(c.getColumnIndex("nameWorkout")), c.getString(c.getColumnIndex("pictureWorkout"))),
                                new HoldBeen(c.getInt(c.getColumnIndex("Hold_idHoldLeft")), c.getString(11)), new HoldBeen(c.getInt(c.getColumnIndex("Hold_idHoldRight")), c.getString(12)), false);

                    } else {
                        aWorkout = new WorkoutsNormal(c.getInt(c.getColumnIndex("idRoutineWorkout")), c.getInt(c.getColumnIndex("numberRW")),
                                st.timer,
                                new WorkoutBeen(c.getInt(c.getColumnIndex("Workout_idWorkout")), c.getString(c.getColumnIndex("nameWorkout")), c.getString(c.getColumnIndex("pictureWorkout"))),
                                new HoldBeen(c.getInt(c.getColumnIndex("Hold_idHoldLeft")), c.getString(11)), new HoldBeen(c.getInt(c.getColumnIndex("Hold_idHoldRight")), c.getString(12)), false);


                    }
                    allRW.get(m).addWorkout(aWorkout);
                    h += st.timer;
                } else {
                    boolean isNext = true;
                    if (isFirst) isNext = false;
                    Struct st = timerCalc(c.getInt(c.getColumnIndex("durationRW")));
                    if (st.isCount) {
                        aWorkout = new WorkoutsCounter(c.getInt(c.getColumnIndex("idRoutineWorkout")), c.getInt(c.getColumnIndex("numberRW")),
                                st.timer, st.count,
                                new WorkoutBeen(c.getInt(c.getColumnIndex("Workout_idWorkout")), c.getString(c.getColumnIndex("nameWorkout")), c.getString(c.getColumnIndex("pictureWorkout"))),
                                new HoldBeen(c.getInt(c.getColumnIndex("Hold_idHoldLeft")), c.getString(11)), new HoldBeen(c.getInt(c.getColumnIndex("Hold_idHoldRight")), c.getString(12)), isNext);

                    } else {
                        aWorkout = new WorkoutsNormal(c.getInt(c.getColumnIndex("idRoutineWorkout")), c.getInt(c.getColumnIndex("numberRW")),
                                st.timer,
                                new WorkoutBeen(c.getInt(c.getColumnIndex("Workout_idWorkout")), c.getString(c.getColumnIndex("nameWorkout")), c.getString(c.getColumnIndex("pictureWorkout"))),
                                new HoldBeen(c.getInt(c.getColumnIndex("Hold_idHoldLeft")), c.getString(11)), new HoldBeen(c.getInt(c.getColumnIndex("Hold_idHoldRight")), c.getString(12)), isNext);


                    }
                    //Log.v("indexM","m - "+m);
                    allRW.get(m).addWorkout(aWorkout);
                    h += st.timer;
                }
                isFirst = false;
            } while (c.moveToNext());
        }
        c.close();
        //Log.v("DB", " size array " + allRW.size());
        return allRW;
    }

    private Struct timerCalc(int aTimer) {
        int a, d = 0;
        boolean isC = false;
        int k;
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this.mCtx);
        if (aTimer > 60) {
            isC = true;
            a = (int) (Math.abs(aTimer) / Math.pow(10, Math.floor(Math.log10(Math.abs(aTimer)))));
            if (a == 7) {
                k = prefs.getInt("pullValue", 2);
            } else {
                if (a == 8) {
                    k = prefs.getInt("shrugsValue", 1);

                } else {
                    k = prefs.getInt("kneeValue", 2);
                }
            }
            if (aTimer > 100) {
                d = aTimer - a * 100;
            } else {
                d = aTimer - a * 10;
            }
            aTimer = d * k;
        }
        return new Struct(d, isC, aTimer);
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_TABLE_WORKOUT);
            db.execSQL(CREATE_TABLE_HOLD);
            db.execSQL(CREATE_TABLE_ROUTINE);
            db.execSQL(CREATE_TABLE_LEVEL);
            db.execSQL(CREATE_TABLE_ROUTINE_LEVEL);
            db.execSQL(CREATE_TABLE_BOARD);
            db.execSQL(CREATE_TABLE_ROUTINEWORKOUT);

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            if (newVersion != oldVersion) {
                db.execSQL(SQL_DELETE_WORKOUT);
                db.execSQL(SQL_DELETE_BOARD);
                db.execSQL(SQL_DELETE_HOLD);
                db.execSQL(SQL_DELETE_ROUTINE);
                db.execSQL(SQL_DELETE_LEVEL);
                db.execSQL(SQL_DELETE_ROUTINE_LEVEL);
                db.execSQL(SQL_DELETE_R_W);
                onCreate(db);
            } else {
                // otherwise, create the database
                onCreate(db);
            }

        }

    }

    private class Struct {
        boolean isCount;
        int timer;
        int count;

        Struct(int count, boolean isCount, int timer) {
            this.count = count;
            this.isCount = isCount;
            this.timer = timer;
        }
    }


}
