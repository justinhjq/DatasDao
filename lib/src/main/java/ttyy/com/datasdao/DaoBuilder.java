package ttyy.com.datasdao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Author: hjq
 * Date  : 2016/11/22
 * Class : DaoBuilder
 * Desc  : Edit By Hujinqi
 */
public class DaoBuilder {

    /**
     * 版本号
     */
    private int version;
    /**
     * 数据库回调
     */
    private DaoBuilder.Callback callback;
    /**
     * 数据库存储的目录
     */
    private String dbDir;
    /**
     * 数据库名称
     */
    private String dbName = "sample";
    /**
     * 上下文
     */
    private Context context;
    /**
     * 是否是Debug
     */
    private boolean isDebug;

    private DaoBuilder(Context context) {
        this.context = context.getApplicationContext();
        this.dbName = context.getPackageName();
        this.version = 1;
        this.dbDir = null;
        this.callback = null;
    }

    /**
     * 工厂模式
     *
     * @param context
     * @return
     */
    public static DaoBuilder from(Context context) {
        DaoBuilder builder = new DaoBuilder(context);
        return builder;
    }

    /**
     * 获取上下文
     *
     * @return
     */
    public Context getContext(){
        return context;
    }

    /**
     * 设置版本号
     *
     * @param version
     * @return
     */
    public DaoBuilder setVersion(int version) {
        this.version = version;
        return this;
    }

    /**
     * 获取版本号
     *
     * @return
     */
    public int getVersion() {
        return this.version;
    }

    /**
     * 设置回调
     *
     * @param callback
     * @return
     */
    public DaoBuilder setCallback(DaoBuilder.Callback callback) {
        this.callback = callback;
        return this;
    }

    public DaoBuilder.Callback getCallback() {
        return this.callback;
    }

    /**
     * 设置数据库存储目录
     *
     * @param dbDir
     * @return
     */
    public DaoBuilder setDbDir(String dbDir) {
        this.dbDir = dbDir;
        return this;
    }

    /**
     * 获取数据库存储目录路径
     * @return
     */
    public String getDbDir() {
        return this.dbDir;
    }

    /**
     * 是否是Debug
     * @return
     */
    public boolean isDebug(){
        return isDebug;
    }

    /**
     * 设置Debug模式
     * @param isDebug
     * @return
     */
    public DaoBuilder setDebug(boolean isDebug){
        this.isDebug = isDebug;
        return this;
    }

    /**
     * 设置数据库名称
     *
     * @param name
     * @return
     */
    public DaoBuilder setDbName(String name) {
        this.dbName = name;
        return this;
    }

    /**
     * 获取数据库名称
     * @return
     */
    public String getDbName() {
        return this.dbName;
    }

    /**
     * 内部调用
     *
     * @return
     */
    protected SimpleSqliteDataBase build() {
        SimpleSqliteDataBase db = new SimpleSqliteDataBase(this);
        return db;
    }

    /**
     * 数据库版本信息回调
     */
    public interface Callback {

        /**
         * 版本升级触发
         *
         * @param db
         * @param oldVersion
         * @param newVersion
         */
        void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion);

        /**
         * 版本降低触发
         *
         * @param db
         * @param oldVersion
         * @param newVersion
         */
        void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion);

    }

}
