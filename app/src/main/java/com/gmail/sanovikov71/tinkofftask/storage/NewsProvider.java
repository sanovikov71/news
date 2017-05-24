package com.gmail.sanovikov71.tinkofftask.storage;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class NewsProvider extends ContentProvider {

    static final String AUTHORITY = "com.gmail.sanovikov71.tinkofftask";

    static final String NEWS_PATH = "news";

    public static final Uri NEWS_CONTENT_URI = Uri.parse("content://"
            + AUTHORITY + "/" + NEWS_PATH);

    static final String NEWS_CONTENT_TYPE = "vnd.android.cursor.dir/vnd."
            + AUTHORITY + "." + NEWS_PATH;

    static final String NEWS_CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd."
            + AUTHORITY + "." + NEWS_PATH;

    static final int URI_ALL_NEWS = 1;
    static final int URI_NEWS_ITEM = 2;

    private static final UriMatcher URI_MATCHER;

    static {
        URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
        URI_MATCHER.addURI(AUTHORITY, NEWS_PATH, URI_ALL_NEWS);
        URI_MATCHER.addURI(AUTHORITY, NEWS_PATH + "/#", URI_NEWS_ITEM);
    }

    private DatabaseHelper databaseHelper;

    @Override
    public boolean onCreate() {
        databaseHelper = new DatabaseHelper(getContext());
        return false;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (URI_MATCHER.match(uri)) {
            case URI_ALL_NEWS:
                return NEWS_CONTENT_TYPE;
            case URI_NEWS_ITEM:
                return NEWS_CONTENT_ITEM_TYPE;
        }
        return null;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri,
                        @Nullable String[] projection,
                        @Nullable String selection,
                        @Nullable String[] selectionArgs,
                        @Nullable String sortOrder) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();

        switch (URI_MATCHER.match(uri)) {
            case URI_ALL_NEWS:
                builder.setTables(NewsTable.TABLE_NEWS);
                break;
            case URI_NEWS_ITEM:
                builder.setTables(NewsTable.TABLE_NEWS);
                builder.appendWhere(NewsTable.COLUMN_ID + " = " +
                        uri.getLastPathSegment());
                break;
            default:
                throw new IllegalArgumentException(
                        "Unsupported URI: " + uri);
        }
        Cursor cursor = builder.query(
                db,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                NewsTable.COLUMN_PUBLICATION_DATE + " DESC");

        cursor.setNotificationUri(getContext().getContentResolver(), NEWS_CONTENT_URI);

        return cursor;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        throw new UnsupportedOperationException("Insert is not implemented for news provider. "
                + "Bulk insert is used.");
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        throw new UnsupportedOperationException("Delete is not implemented for news provider.");
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        int cnt = db.updateWithOnConflict(
                NewsTable.TABLE_NEWS,
                values,
                selection,
                selectionArgs,
                SQLiteDatabase.CONFLICT_REPLACE
        );

        getContext().getContentResolver().notifyChange(uri, null);

        return cnt;
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
        int numInserted = 0;

        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            for (ContentValues cv : values) {
                db.insertWithOnConflict(
                        NewsTable.TABLE_NEWS,
                        null,
                        cv,
                        SQLiteDatabase.CONFLICT_IGNORE
                );
            }
            db.setTransactionSuccessful();
            getContext().getContentResolver().notifyChange(uri, null);
            numInserted = values.length;
        } finally {
            db.endTransaction();
        }

        return numInserted;
    }
}
