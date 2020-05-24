package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DBMatches {

    private static final String DATABASE_NAME = "students.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "Students";

    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "Name";
    private static final String COLUMN_SURNAME = "SurName";
    private static final String COLUMN_MIDDLENAME = "MiddleName";
    private static final String GROUP = "Grup";

    private static final int NUM_COLUMN_ID = 0;
    private static final int NUM_COLUMN_NAME = 1;
    private static final int NUM_COLUMN_SURNAME = 2;
    private static final int NUM_COLUMN_MIDDLENAME = 3;
    private static final int NUM_GROUP = 4;

    private SQLiteDatabase mDataBase;

    public DBMatches(Context context) {
        OpenHelper mOpenHelper = new OpenHelper(context);
        mDataBase = mOpenHelper.getWritableDatabase();
    }

    public long insert(String teamhouse,String teamguest,String goalshouse,String goalsguest) {
        ContentValues cv=new ContentValues();
        cv.put(COLUMN_NAME, teamhouse);
        cv.put(COLUMN_SURNAME, teamguest);
        cv.put(COLUMN_MIDDLENAME, goalshouse);
        cv.put(GROUP,goalsguest);
        return mDataBase.insert(TABLE_NAME, null, cv);
    }

    public int update(Matches md) {
        ContentValues cv=new ContentValues();
        cv.put(COLUMN_NAME, md.getName());
        cv.put(COLUMN_SURNAME, md.getSurname());
        cv.put(COLUMN_MIDDLENAME, md.getMiddlename());
        cv.put(GROUP,md.getGroup());
        return mDataBase.update(TABLE_NAME, cv, COLUMN_ID + " = ?",new String[] { String.valueOf(md.getId())});
    }

    public void deleteAll() {
        mDataBase.delete(TABLE_NAME, null, null);
    }

    public void delete(long id) {
        mDataBase.delete(TABLE_NAME, COLUMN_ID + " = ?", new String[] { String.valueOf(id) });
    }

    public Matches select(long id) {
        Cursor mCursor = mDataBase.query(TABLE_NAME, null, COLUMN_ID + " = ?", new String[]{String.valueOf(id)}, null, null, null);

        mCursor.moveToFirst();
        String TeamHome = mCursor.getString(NUM_COLUMN_NAME);
        String TeamGuest = mCursor.getString(NUM_COLUMN_SURNAME);
        String GoalsHome = mCursor.getString(NUM_COLUMN_MIDDLENAME);
        String GoalsGuest=mCursor.getString(NUM_GROUP);
        return new Matches(id, TeamHome, TeamGuest, GoalsHome,GoalsGuest);
    }

    public ArrayList<Matches> selectAll() {
        Cursor mCursor = mDataBase.query(TABLE_NAME, null, null, null, null, null, null);

        ArrayList<Matches> arr = new ArrayList<Matches>();
        mCursor.moveToFirst();
        if (!mCursor.isAfterLast()) {
            do {
                long id = mCursor.getLong(NUM_COLUMN_ID);
                String TeamHome = mCursor.getString(NUM_COLUMN_NAME);
                String TeamGuest = mCursor.getString(NUM_COLUMN_SURNAME);
                String GoalsHome = mCursor.getString(NUM_COLUMN_MIDDLENAME);
                String GoalsGuest=mCursor.getString(NUM_GROUP);
                arr.add(new Matches(id, TeamHome, TeamGuest, GoalsHome,GoalsGuest));
            } while (mCursor.moveToNext());
        }
        return arr;
    }

    private class OpenHelper extends SQLiteOpenHelper {

        OpenHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }
        @Override
        public void onCreate(SQLiteDatabase db) {
            String query = "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_NAME + " TEXT, " +
                    COLUMN_SURNAME + " TEXT, " +
                    COLUMN_MIDDLENAME + " TEXT,"+
                    GROUP +" TEXT);";
            db.execSQL(query);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }
    }
}