package ttyy.com.sp.multiprocess;

/**
 * author: admin
 * date: 2017/12/25
 * version: 0
 * mail: secret
 * desc: ProcessSP
 */

public interface StoreIntf {

    StoreIntf putInteger(String key, int value);

    int getInteger(String key);

    int getInteger(String key, int defaultValue);

    StoreIntf putFloat(String key, float value);

    float getFloat(String key);

    float getFloat(String key, float defaultValue);

    StoreIntf putDouble(String key, double value);

    double getDouble(String key);

    double getDouble(String key, double defaultValue);

    StoreIntf putLong(String key, long value);

    long getLong(String key);

    long getLong(String key, long defaultValue);

    StoreIntf putString(String key, String value);

    String getString(String key);

    String getString(String key, String defaultValue);

    StoreIntf putBoolean(String key, boolean value);

    boolean getBoolean(String key);

    boolean getBoolean(String key, boolean defaultValue);
}
