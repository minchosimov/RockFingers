package com.mincho.rockfingers.been;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class LogBeen {
    private static final String DATE_FORMAT_SQL = "yyyy-MM-dd HH:mm:ss";
    private static final String DATE_FORMAT_CHART = "dd MM yyyy";
    private static final SimpleDateFormat dateFormatSql = new SimpleDateFormat(DATE_FORMAT_SQL, Locale.getDefault());
    private static final SimpleDateFormat dateFormatChart = new SimpleDateFormat(DATE_FORMAT_CHART, Locale.getDefault());
    private int ID;
    private Calendar date;
    private String routine;
    private String level;
    private Double wTime;

    public LogBeen() {
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public Calendar getDateTime() {
        return date;
    }

    public void setDateTime(Calendar date) {
        this.date = date;
    }

    public String getRoutine() {
        return routine;
    }

    public void setRoutine(String routine) {
        this.routine = routine;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public Double getwTime() {
        return wTime;
    }

    public void setwTime(Double time) {
        this.wTime = time;
    }

    public String getDateAsDateSQL() {
        return dateFormatSql.format(this.date.getTime());
    }

    public String getDateAsDateChart() {
        return dateFormatChart.format(this.date.getTime());
    }

    public void setDateFromSQL(String s) {
        try {
            Calendar c = Calendar.getInstance();
            c.setTime(dateFormatSql.parse(s));
            this.date = c;

        } catch (ParseException e) {
            this.date = null;
        }
    }

}
