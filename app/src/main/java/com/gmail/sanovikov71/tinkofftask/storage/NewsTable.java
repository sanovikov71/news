package com.gmail.sanovikov71.tinkofftask.storage;

import android.database.sqlite.SQLiteDatabase;

public class NewsTable {

    static final String TABLE_NEWS = "news";
    static final String COLUMN_ID = "_id";
    public static final String COLUMN_BACKEND_ID = "backend_id";
    public static final String COLUMN_TEXT = "text";
    public static final String COLUMN_CONTENT = "content";
    public static final String COLUMN_PUBLICATION_DATE = "publication_date";

    private static final String DATABASE_CREATE = "create table "
            + TABLE_NEWS
            + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_BACKEND_ID + " integer unique, "
            + COLUMN_TEXT + " text, "
            + COLUMN_CONTENT + " text, "
            + COLUMN_PUBLICATION_DATE + " integer"
            + ");";

    static void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    static void onUpgrade(SQLiteDatabase database, int oldVersion,
                          int newVersion) {
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_NEWS);
        onCreate(database);
    }

}
