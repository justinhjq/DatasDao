package ttyy.com.sp.multiprocess;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

/**
 * author: admin
 * date: 2017/12/25
 * version: 0
 * mail: secret
 * desc: AppStores
 */

public class AppStores implements StoreIntf {

    static final Uri MULTI_SP_URI = Uri.parse("content://" + StoresContentProvider.AUTHORITY + "/" + StoresContentProvider.PATH_SP);

    static volatile StoreIntf SINGLETON;

    ContentResolver resolver;

    private AppStores(Context context) {
        resolver = context.getContentResolver();
    }

    public static StoreIntf get(Context context) {

        StoreIntf INST = SINGLETON;
        if (INST == null) {
            synchronized (AppStores.class) {
                INST = SINGLETON;
                if (INST == null) {
                    INST = new AppStores(context);
                    SINGLETON = INST;
                }
            }
        }

        return INST;
    }

    @Override
    public StoreIntf putInteger(String key, int value) {
        ContentValues values = new ContentValues();
        values.put(StoresContentProvider.COLUMN_KEY, key);
        values.put(StoresContentProvider.COLUMN_VALUE, value);
        resolver.insert(MULTI_SP_URI, values);
        return this;
    }

    @Override
    public int getInteger(String key) {
        return getInteger(key, -1);
    }

    @Override
    public int getInteger(String key, int defaultValue) {
        Cursor cursor = resolver.query(MULTI_SP_URI,
                new String[]{StoresContentProvider.COLUMN_VALUE},
                "WHERE " + StoresContentProvider.COLUMN_KEY + " = ?",
                new String[]{key},
                null);
        if (cursor != null) {

            if (cursor.moveToFirst()) {
                defaultValue = cursor.getInt(0);
            }

        }
        return defaultValue;
    }

    @Override
    public StoreIntf putFloat(String key, float value) {
        ContentValues values = new ContentValues();
        values.put(StoresContentProvider.COLUMN_KEY, key);
        values.put(StoresContentProvider.COLUMN_VALUE, value);
        resolver.insert(MULTI_SP_URI, values);
        return this;
    }

    @Override
    public float getFloat(String key) {
        return getFloat(key, -1);
    }

    @Override
    public float getFloat(String key, float defaultValue) {
        return defaultValue;
    }

    @Override
    public StoreIntf putDouble(String key, double value) {
        ContentValues values = new ContentValues();
        values.put(StoresContentProvider.COLUMN_KEY, key);
        values.put(StoresContentProvider.COLUMN_VALUE, value);
        resolver.insert(MULTI_SP_URI, values);
        return this;
    }

    @Override
    public double getDouble(String key) {
        return getDouble(key, -1);
    }

    @Override
    public double getDouble(String key, double defaultValue) {
        return defaultValue;
    }

    @Override
    public StoreIntf putLong(String key, long value) {
        ContentValues values = new ContentValues();
        values.put(StoresContentProvider.COLUMN_KEY, key);
        values.put(StoresContentProvider.COLUMN_VALUE, value);
        resolver.insert(MULTI_SP_URI, values);
        return this;
    }

    @Override
    public long getLong(String key) {
        return getLong(key, -1);
    }

    @Override
    public long getLong(String key, long defaultValue) {
        return defaultValue;
    }

    @Override
    public StoreIntf putString(String key, String value) {
        ContentValues values = new ContentValues();
        values.put(StoresContentProvider.COLUMN_KEY, key);
        values.put(StoresContentProvider.COLUMN_VALUE, value);
        resolver.insert(MULTI_SP_URI, values);
        return this;
    }

    @Override
    public String getString(String key) {
        return getString(key, null);
    }

    @Override
    public String getString(String key, String defaultValue) {
        return defaultValue;
    }

    @Override
    public StoreIntf putBoolean(String key, boolean value) {
        ContentValues values = new ContentValues();
        values.put(StoresContentProvider.COLUMN_KEY, key);
        values.put(StoresContentProvider.COLUMN_VALUE, value);
        resolver.insert(MULTI_SP_URI, values);
        return this;
    }

    @Override
    public boolean getBoolean(String key) {
        return getBoolean(key, false);
    }

    @Override
    public boolean getBoolean(String key, boolean defaultValue) {
        return defaultValue;
    }
}
