package ttyy.com.datasdao;

import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Author: hjq
 * Date  : 2016/12/19 21:03
 * Name  : Datas
 * Intro : Edit By hjq
 * Modification  History:
 * Date          Author        	 Version          Description
 * ----------------------------------------------------------
 * 2016/12/19    hjq   1.0              1.0
 */
public class Datas {

    /**
     * 创建一个数据库
     * @param builder
     * @return
     */
    public static Core createSqliteDatabase(DaoBuilder builder){
        SimpleSqliteDao db = builder.build();
        Core core = new Core(db);
        Cache.cache(builder.getDbName(), core);
        return core;
    }

    /**
     * 删除一个数据库
     * @param databaseName
     * @return
     */
    public static boolean destroySqliteDatabase(String databaseName){
        Core core = Cache.fromCache(databaseName);
        if(core != null){
            return core.getDatabaseDao().destroy();
        }
        return false;
    }

    /**
     * 获取一个db
     * @param dbName
     * @return
     */
    public static Core from(String dbName){
        try{
            return Cache.fromCache(dbName);
        }finally {
            Cache.setLastDBName(dbName);
        }
    }

    /**
     * 默认取上一个
     * @return
     */
    public static Core core(){
        return Cache.last();
    }

    /**
     * 生成一个新的 但是不入缓存
     * @param database
     * @return
     */
    public static Core from(SQLiteDatabase database){
        return new Core(database);
    }

    /**
     * 缓存
     */
    static class Cache{
        static Map<String, Core> caches = new HashMap<>();
        static String mLatestDBName = null;

        static void cache(String dbName, Core core){
            caches.put(dbName, core);

            setLastDBName(dbName);
        }

        static Core fromCache(String dbName){
            if(TextUtils.isEmpty(dbName))
                return null;

            return caches.get(dbName);
        }

        /**
         * 获取最近一次的数据库对象
         * @return
         */
        static Core last(){
            return fromCache(mLatestDBName);
        }

        /**
         * 记下最近一次的数据库名称
         * @param name
         */
        static void setLastDBName(String name){
            mLatestDBName = name;
        }
    }

}
