package com.gmail.sanovikov71.tinkofftask.storage;

import android.database.sqlite.SQLiteDatabase;

public class NewsTable {

    // Database table
    public static final String TABLE_NEWS = "news";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TEXT = "text";
    public static final String COLUMN_CONTENT = "content";

    // Database creation SQL statement
    private static final String DATABASE_CREATE = "create table "
            + TABLE_NEWS
            + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_TEXT + " text, "
            + COLUMN_CONTENT + " text"
            + ");";

    public static void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion,
                                 int newVersion) {
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_NEWS);
        onCreate(database);
    }

}
