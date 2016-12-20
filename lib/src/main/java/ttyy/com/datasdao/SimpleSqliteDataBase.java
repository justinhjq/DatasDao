package ttyy.com.datasdao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import java.io.File;

/**
 * Author: hujinqi
 * Date  : 2016-08-18
 * Description:
 */
public class SimpleSqliteDataBase {

    private SQLiteDatabase database;
    private DaoBuilder builder;

    SimpleSqliteDataBase(DaoBuilder builder) {
        database = createDataBase(builder);
    }

    private SQLiteDatabase createDataBase(DaoBuilder builder) {
        if (!TextUtils.isEmpty(builder.getDbDir())) {
            // 自定义dbDir为空，那么就取默认的db文件存储位置
            File file = new File(builder.getDbDir(), builder.getDbName());
            database = SQLiteDatabase.openOrCreateDatabase(file, null);
        } else {
            // 没有自定义dbDir，那么默认存储在手机内存中，需要root权限访问
            database = builder.getContext().openOrCreateDatabase(builder.getDbName(), Context.MODE_PRIVATE, null);
        }

        // 检查版本更新
        int oldVersion = database.getVersion();
        int newVersion = builder.getVersion();
        if (oldVersion != newVersion) {
            if (builder.getCallback() != null) {
                if (oldVersion < newVersion) {
                    builder.getCallback().onUpgrade(database, oldVersion, newVersion);
                } else {
                    builder.getCallback().onDowngrade(database, oldVersion, newVersion);
                }
            }
            database.setVersion(newVersion);
        }

        return database;
    }

    public SQLiteDatabase getDatabase() {
        return this.database;
    }

    public DaoBuilder getConfig(){
        return builder;
    }

    /**
     * 删除数据库
     * 从本地删除
     * @return
     */
    public boolean destroy(){
        return builder.getContext().deleteDatabase(builder.getDbName());
    }

}
