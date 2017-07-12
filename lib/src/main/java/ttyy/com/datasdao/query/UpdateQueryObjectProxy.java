package ttyy.com.datasdao.query;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import ttyy.com.datasdao.modules.ModuleColumn;

/**
 * author: admin
 * date: 2017/07/12
 * version: 0
 * mail: secret
 * desc: UpdateQueryObjectProxy
 */

public class UpdateQueryObjectProxy<T> {

    /**
     * 被代理查询对象
     */
    UpdateQuery<T> mRealQuery;

    /**
     * 数据源
     */
    T[] objects;

    List<ModuleColumn> mUpdateColumns;

    protected UpdateQueryObjectProxy(UpdateQuery<T> realQuery) {
        mRealQuery = realQuery;
        mUpdateColumns = new ArrayList<>();
    }

    /**
     * 设置代理对象
     * @param objects
     * @return
     */
    protected UpdateQueryObjectProxy obj(T... objects) {
        this.objects = objects;
        return this;
    }

    /**
     * 筛选属性名
     * @param columns
     * @return
     */
    public UpdateQueryObjectProxy withProperty(String... columns) {
        if (columns == null
                || columns.length < 1) {
            return this;
        }

        mUpdateColumns.clear();
        for (String tmp : columns) {
            for (ModuleColumn column : mRealQuery.getColumns()) {
                if (column.getPropertyName().equals(tmp)) {
                    mUpdateColumns.add(column);
                    break;
                }
            }
        }

        return this;
    }

    /**
     * 筛选数据库表字段名
     * @param columns
     * @return
     */
    public UpdateQueryObjectProxy withColumn(String... columns) {
        if (columns == null
                || columns.length < 1) {
            return this;
        }

        mUpdateColumns.clear();
        for (String tmp : columns) {
            for (ModuleColumn column : mRealQuery.getColumns()) {
                if (column.getColumnName().equals(tmp)) {
                    mUpdateColumns.add(column);
                    break;
                }
            }
        }

        return this;
    }

    /**
     * 筛选数据库表字段名
     * @param columns
     * @return
     */
    public UpdateQueryObjectProxy withOutColumn(String... columns) {
        if (columns == null
                || columns.length < 1) {
            return this;
        }
        List<ModuleColumn> mUpdateColumnsCopy = new ArrayList<>(mRealQuery.getColumns());
        mUpdateColumns.clear();
        for (String tmp : columns) {
            for (ModuleColumn column : mUpdateColumnsCopy) {
                if (!column.getColumnName().equals(tmp)) {
                    mUpdateColumns.add(column);
                }
            }
        }

        return this;
    }

    /**
     * 筛选属性名
     * @param columns
     * @return
     */
    public UpdateQueryObjectProxy withOutProperty(String... columns) {
        if (columns == null
                || columns.length < 1) {
            return this;
        }
        List<ModuleColumn> mUpdateColumnsCopy = new ArrayList<>(mRealQuery.getColumns());
        mUpdateColumns.clear();
        for (String tmp : columns) {
            for (ModuleColumn column : mUpdateColumnsCopy) {
                if (!column.getPropertyName().equals(tmp)) {
                    mUpdateColumns.add(column);
                }
            }
        }

        return this;
    }

    /**
     * addWhereColumn代理
     * @param key
     * @param value
     * @return
     */
    public UpdateQueryObjectProxy addWhereColumn(String key, Object value) {
        mRealQuery.addWhereColumn(key, value);
        return this;
    }

    /**
     * where代理
     * @param sql
     * @return
     */
    public UpdateQueryObjectProxy where(String sql) {
        mRealQuery.where(sql);
        return this;
    }

    /**
     * update代理
     */
    public void update() {

        if (objects == null
                || objects.length < 1) {
            return;
        }

        if(mUpdateColumns == null
                || mUpdateColumns.size() < 1){
            Log.w("Datas", "Hasn't set detail updateColumns");
            mUpdateColumns = new ArrayList<>(mRealQuery.getColumns());
        }

        // 重设Where语句
        mRealQuery.resetWhereExpression();

        ArrayList<String> sqls = new ArrayList<>(objects.length);
        try {
            for (T t : objects) {
                for (ModuleColumn column : mUpdateColumns) {
                    mRealQuery.addUpdateColumn(column.getColumnName(), column.getPropertyValue(t));
                }
                // 重设SET EXP
                mRealQuery.resetSetExpression();
                sqls.add(mRealQuery.createSql());
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        if(sqls.size() < 1){
            return;
        }

        if (mRealQuery.mDatabase.isDbLockedByCurrentThread()) {

            for(String sql : sqls){
                mRealQuery.mDatabase.execSQL(sql);
            }

        } else {

            mRealQuery.mDatabase.beginTransaction();
            try {

                for(String sql : sqls){
                    mRealQuery.mDatabase.execSQL(sql);
                }

                mRealQuery.mDatabase.setTransactionSuccessful();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                mRealQuery.mDatabase.endTransaction();
            }
        }
    }

}
