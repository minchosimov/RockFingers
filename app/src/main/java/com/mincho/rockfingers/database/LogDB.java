package com.mincho.rockfingers.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.mincho.rockfingers.been.LogBeen;

import java.util.ArrayList;

public class LogDB {

    private static final String DATABASE_NAME = "rockfingersdb.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_LOG = "log";
    private static final String CName1 = "id";
    private static final String CName2 = "logdatetime";
    private static final String CName3 = "logroutine";
    private static final String CName4 = "loglevel";
    private static final String CName5 = "time";
    private static final String SQL_CREATE_ENTRIES = "CREATE TABLE " + TABLE_LOG + "(" + CName1 + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            CName2 + " DATETIME NOT NULL, " + CName3 + " VARCHAR(20), " + CName4 + " VARCHAR(20)," + CName5 + " NUMBER(4,2) DEFAULT 10.00)";
    private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + TABLE_LOG;


    private final Context mCtx;
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    public LogDB(Context context) {
        this.mCtx = context;
    }

    public LogDB open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        mDbHelper.close();
    }

    public ArrayList<LogBeen> getAllLog() {
        ArrayList<LogBeen> logs = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_LOG + " ORDER BY " + CName1 + " DESC";

        Cursor c = mDb.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    LogBeen td = new LogBeen();
                    td.setID(c.getInt((c.getColumnIndex(CName1))));
                    //String l = c.getString(c.getColumnIndex(CName2));
                    td.setDateFromSQL(c.getString(c.getColumnIndex(CName2))); //setDateFromLong(c.getLong(c.getColumnIndex(CName2)));
                    //Log.v("DB", "get " + l);
                    td.setRoutine(c.getString(c.getColumnIndex(CName3)));
                    td.setLevel(c.getString(c.getColumnIndex(CName4)));
                    td.setwTime(c.getDouble(c.getColumnIndex(CName5)));
                    logs.add(td);
                } while (c.moveToNext());
            }
            //Log.v("DB", logs.size() + " selected element");
            c.close();
        }
        return logs;
    }

    public ArrayList<LogBeen> getLastMonthLog() {
        ArrayList<LogBeen> logs = new ArrayList<>();
        String lastMonth = null;
        String mQuery = "SELECT strftime('%m'," + CName2 + ") FROM " + TABLE_LOG + " ORDER BY id DESC LIMIT 1";
        // Log.v("DB",mQuery);
        Cursor s = mDb.rawQuery(mQuery, null);
        if (s != null) {
            if (s.moveToFirst()) {
                do {
                    lastMonth = s.getString(0);
                } while (s.moveToNext());
            }

            s.close();
        }


        String selectQuery = "SELECT  * FROM " + TABLE_LOG + " WHERE strftime('%m'," + CName2 + ") = '" + lastMonth + "'" + " ORDER BY " + CName1 + " DESC";

        Cursor c = mDb.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    LogBeen td = new LogBeen();
                    td.setID(c.getInt((c.getColumnIndex(CName1))));
                    String l = c.getString(c.getColumnIndex(CName2));
                    td.setDateFromSQL(c.getString(c.getColumnIndex(CName2))); //setDateFromLong(c.getLong(c.getColumnIndex(CName2)));
                    Log.v("DB", "get " + l);
                    td.setRoutine(c.getString(c.getColumnIndex(CName3)));
                    td.setLevel(c.getString(c.getColumnIndex(CName4)));
                    td.setwTime(c.getDouble(c.getColumnIndex(CName5)));
                    logs.add(td);
                } while (c.moveToNext());
            }
            Log.v("DB", logs.size() + " selected element");
            c.close();
        }
        return logs;
    }

    public String[] getSumByMonth(int month) {
        String monthS;
        String[] s = {"", ""};
        if (month < 10) {
            monthS = "0" + month;
        } else {
            monthS = month + "";
        }
        String selectQuery = "SELECT SUM(" + CName5 + "), COUNT(" + CName1 + ") FROM " + TABLE_LOG + " WHERE strftime('%m'," + CName2 + ") = '" + monthS + "'";
        Log.v("DB", selectQuery);


        Cursor c = mDb.rawQuery(selectQuery, null);
        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    s[0] = c.getString(0);
                    s[1] = c.getString(1);
                } while (c.moveToNext());
            }
            c.close();
        }
        return s;
    }

    public String[] getForMainActivity() {

        String[] s = {"", "", ""};

        String selectQuery = "SELECT ROUND(AVG(" + CName5 + "),2), COUNT(" + CName1 + ") FROM " + TABLE_LOG;

        Cursor c = mDb.rawQuery(selectQuery, null);
        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    s[0] = c.getString(0);
                    s[1] = c.getString(1);
                } while (c.moveToNext());
            }
            c.close();
        }

        String mQuery = "SELECT date(" + CName2 + ") FROM " + TABLE_LOG + " ORDER BY id DESC LIMIT 1";
        // Log.v("DB",mQuery);
        c = mDb.rawQuery(mQuery, null);
        if (s != null) {
            if (c.moveToFirst()) {
                do {
                    s[2] = c.getString(0);
                } while (c.moveToNext());
            }

            c.close();
        }


        return s;
    }

    // public double getMontSum (int month)
    public void insertLog(LogBeen logB) {

        ContentValues values = new ContentValues();
        values.put(CName2, logB.getDateAsDateSQL()); // .getDateAsLong());
        values.put(CName3, logB.getRoutine());
        values.put(CName4, logB.getLevel());
        values.put(CName5, logB.getwTime());
        // insert row
        long Row = mDb.insert(TABLE_LOG, null, values);
        Log.v("DB", Row + " row inserted");
    }

    public void deleteLogRow(String id) {

        mDb.delete(TABLE_LOG, "id = ?", new String[]{id});
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {


        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(SQL_CREATE_ENTRIES);

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            if (newVersion > oldVersion) {
                db.execSQL(SQL_DELETE_ENTRIES);
                onCreate(db);
            } else {
                // otherwise, create the database
                onCreate(db);
            }

        }

    }
}
