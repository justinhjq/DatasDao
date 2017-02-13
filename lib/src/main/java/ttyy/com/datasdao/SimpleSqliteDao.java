package ttyy.com.datasdao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;

/**
 * Author: hjq
 * Date  : 2016/08/18 21:03
 * Name  : SimpleSqliteDao
 * Intro : Edit By hjq
 * Modification  History:
 * Date          Author        	 Version          Description
 * ----------------------------------------------------------
 * 2016/08/18    hjq   1.0              1.0
 */
public class SimpleSqliteDao {

    private SQLiteDatabase mDataBase;
    private DaoBuilder mBuilder;

    SimpleSqliteDao(DaoBuilder builder) {
        mDataBase = createDataBase(builder);
        mBuilder = builder;
    }

    SimpleSqliteDao(SQLiteDatabase database){
        mDataBase = database;
    }

    private SQLiteDatabase createDataBase(DaoBuilder builder) {
        if (!TextUtils.isEmpty(builder.getDbDir())) {
            // 自定义dbDir为空，那么就取默认的db文件存储位置
            File file = new File(builder.getDbDir(), builder.getDbName());
            if(!file.exists()){
                file.getParentFile().mkdirs();
            }
            mDataBase = SQLiteDatabase.openOrCreateDatabase(file, null);
        } else {
            // 没有自定义dbDir，那么默认存储在手机内存中，需要root权限访问
            mDataBase = builder.getContext().openOrCreateDatabase(builder.getDbName(), Context.MODE_PRIVATE, null);
        }

        // 检查版本更新
        int oldVersion = mDataBase.getVersion();
        int newVersion = builder.getVersion();
        if (oldVersion != newVersion) {
            if (builder.getCallback() != null) {
                if (oldVersion < newVersion) {
                    builder.getCallback().onUpgrade(mDataBase, oldVersion, newVersion);
                } else {
                    builder.getCallback().onDowngrade(mDataBase, oldVersion, newVersion);
                }
            }
            mDataBase.setVersion(newVersion);
        }

        return mDataBase;
    }

    public SQLiteDatabase getDatabase() {
        return this.mDataBase;
    }

    public DaoBuilder getConfig(){
        return mBuilder;
    }

    /**
     * 删除数据库
     * 从本地删除
     * @return
     */
    public boolean destroy(){
//        // 数据库路径
//        String database_name = null;
//        if(getConfig().getDbDir() != null){
//            // 数据库不在默认位置，所以要给具体的路径
//            database_name = mBuilder.getDbDir()+"/"+mBuilder.getDbName();
//        }else {
//            // 默认定位在包目录下
//            database_name = mBuilder.getDbName();
//        }
//
//        if(getConfig().isDebug()){
//            Log.i("Datas",">>>>>> delete database "+database_name+" <<<<<<<<");
//        }
//
//        return mBuilder.getContext().deleteDatabase(database_name);

        if(mBuilder == null || mBuilder.getContext() == null){
            // 缺少Context上下文 无法完成此操作
            return false;
        }

        // 数据库路径
        String database_name = mDataBase.getPath();
        if(getConfig().isDebug()){
            Log.i("Datas",">>>>>> delete database "+database_name+" <<<<<<<<");
        }

        return mBuilder.getContext().deleteDatabase(database_name);
    }

    /**
     * 清空数据库表格
     */
    public void dropAllTables() {
        if(getDatabase().isDbLockedByCurrentThread()) {
            this.executeDropAllCommands(getDatabase());
        } else {
            try {
                getDatabase().beginTransaction();
                this.executeDropAllCommands(getDatabase());
                getDatabase().setTransactionSuccessful();
            } finally {
                getDatabase().endTransaction();
            }
        }

    }

    private void executeDropAllCommands(SQLiteDatabase mDatabase) {
        ArrayList<String> mAllTableNames = new ArrayList();
        String sql = "select name from sqlite_master where type = 'table'";
        Cursor cursor = mDatabase.rawQuery(sql, null);
        if(cursor != null) {
            while(cursor.moveToNext()) {
                String tableName = cursor.getString(0);
                mAllTableNames.add(tableName);
            }

            cursor.close();

            for(String tmpName : mAllTableNames){
                String d_sql = "DROP TABLE " + tmpName;
                mDatabase.execSQL(d_sql);
            }

        }
    }

}
