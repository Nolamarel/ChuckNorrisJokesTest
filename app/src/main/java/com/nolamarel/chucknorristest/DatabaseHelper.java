package com.nolamarel.chucknorristest;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.nolamarel.chucknorristest.models.JokeDb;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TABLE_NAME = "jokes";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_JOKEID = "jokeId";

    private static final String COLUMN_ICON = "iconUrl";
    private static final String COLUMN_URL = "url";
    private static final String COLUMN_VALUE = "value";


    public DatabaseHelper(Context context) {
        super(context, "jokes.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_JOKEID + " TEXT, " +
                COLUMN_ICON + " TEXT, " +
                COLUMN_URL + " TEXT, " +
                COLUMN_VALUE + " TEXT) ";
        try {
            db.execSQL(createTable);
        } catch (Exception e) {
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean addJoke(JokeDb joke){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_JOKEID, joke.getJokeId());
        cv.put(COLUMN_ICON, joke.getIcon_url());
        cv.put(COLUMN_URL, joke.getUrl());
        cv.put(COLUMN_VALUE, joke.getValue());

        long newId = db.insert(TABLE_NAME, null, cv);
        db.close();
        if (newId != -1) {
            joke.setId((int) newId); // Установка ID в объекте JokeDb
            return true;
        } else {
            return false;
        }
    }

    public JokeDb getJokeById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME,
                new String[]{COLUMN_ID, COLUMN_JOKEID, COLUMN_ICON, COLUMN_URL, COLUMN_VALUE},
                COLUMN_ID + " = ?",
                new String[]{String.valueOf(id)},
                null, null, null);

        JokeDb joke = null;
        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(COLUMN_ID);
            int jokeIdIndex = cursor.getColumnIndex(COLUMN_JOKEID);
            int iconIndex = cursor.getColumnIndex(COLUMN_ICON);
            int urlIndex = cursor.getColumnIndex(COLUMN_URL);
            int valueIndex = cursor.getColumnIndex(COLUMN_VALUE);

            if (idIndex != -1 && iconIndex != -1 && urlIndex != -1 && valueIndex != -1) {
                joke = new JokeDb(
                        cursor.getString(iconIndex),
                        cursor.getInt(idIndex),
                        cursor.getString(urlIndex),
                        cursor.getString(valueIndex),
                        cursor.getString(jokeIdIndex)
                );
            }
        }
        cursor.close();
        db.close();
        return joke;
    }

}
