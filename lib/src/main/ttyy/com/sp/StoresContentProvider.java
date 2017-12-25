package ttyy.com.sp.multiprocess;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * author: admin
 * date: 2017/12/25
 * version: 0
 * mail: secret
 * desc: ContentProviderImpl
 */

public class StoresContentProvider extends ContentProvider {

    static final String AUTHORITY = "com.jin.data.multiprocess.sp";
    static final String PATH_SP = "spvalues";

    static final String TABLE_SQL;
    static final String TABLE_NAME = "MULTI_PROCESS_SP_TABLE";
    static final int TABLE_VERSION = 1;
    static final String COLUMN_KEY = "KEY";
    static final String COLUMN_VALUE = "VALUE";

    SQLiteDatabase database;

    UriMatcher mUriMathcer = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        TABLE_SQL = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME
                + " ("
                + COLUMN_KEY + " CHAR(255) NOT NULL ,"
                + COLUMN_VALUE + " CHAR(255) ,"
                + "PRIMARY KEY (KEY) ON CONFLICT REPLACE"
                + ")";

    }

    {
        mUriMathcer.addURI(AUTHORITY, PATH_SP, 0);
    }

    @Override
    public boolean onCreate() {
        SQLiteOpenHelper helper = new SQLiteOpenHelper(getContext(),
                "MULTI_PROCESS_SP_DB", null, TABLE_VERSION) {
            @Override
            public void onCreate(SQLiteDatabase db) {

            }

            @Override
            public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
                String DROP_SQL = "DROP TABLE IF EXISTS " + TABLE_NAME;
                db.execSQL(DROP_SQL);
            }
        };
        database = helper.getWritableDatabase();
        database.execSQL(TABLE_SQL);

        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        if (mUriMathcer.match(uri) == 0) {
            // sp操作类型
            String SQL = "SELECT " + COLUMN_VALUE + " FROM " + TABLE_NAME + " " + selection;
            Cursor cursor = database.rawQuery(SQL, selectionArgs);

            return cursor;
        }
        return null;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        if (mUriMathcer.match(uri) == 0) {
            // sp操作类型

            database.insert(TABLE_NAME, null, values);
        }
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        if (mUriMathcer.match(uri) == 0) {

            int rowNumber = database.delete(TABLE_NAME, selection, selectionArgs);

            return rowNumber;
        }
        return -1;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        if (mUriMathcer.match(uri) == 0) {

            int rowNumber = database.update(TABLE_NAME, values, selection, selectionArgs);

            return rowNumber;
        }
        return -1;
    }
}
