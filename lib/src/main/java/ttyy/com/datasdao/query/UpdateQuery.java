package ttyy.com.datasdao.query;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.text.TextUtils;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import ttyy.com.datasdao.modules.SerializeHelper;

/**
 * Author: hjq
 * Date  : 2016/08/18 21:03
 * Name  : UpdateQuery
 * Intro : 更新查询
 * Modification  History:
 * Date          Author        	 Version          Description
 * ----------------------------------------------------------
 * 2016/08/18    hjq             1.0              1.0
 * 2017/06/06    hjq             1.0              添加addUpdateColumn方法
 */
public abstract class UpdateQuery<T> extends BaseQuery<T> {

    /**
     * where条件语句
     */
    protected String str_where;

    protected String str_set;

    protected HashMap<String, String> mUpdateColumnExpressions;
    protected HashMap<String, String> mWhereColumnExpressions;

    public UpdateQuery(Class<T> tClass, SQLiteDatabase database) {
        super(tClass, database);

        mUpdateColumnExpressions = new HashMap<>();
        mWhereColumnExpressions = new HashMap<>();
    }

    /**
     * where 条件语句
     *
     * @param where
     */
    public UpdateQuery<T> where(String where) {
        if (TextUtils.isEmpty(where)) {
            return this;
        }

        if (where.trim().toLowerCase().startsWith("where")) {
            str_where = where.trim().substring(5);
        } else {
            str_where = where;
        }

        return this;
    }

    /**
     * SET语句
     *
     * @param set
     * @return
     */
    public UpdateQuery<T> set(String set) {
        str_set = set;
        if (set.trim().toLowerCase().startsWith("set")) {
            str_set = set.trim().substring(3);
        } else {
            str_set = set;
        }
        return this;
    }

    /**
     * Update Set语句
     *
     * @param column
     * @param value
     * @return
     */
    public UpdateQuery<T> addUpdateColumn(String column, Object value) {

        if (TextUtils.isEmpty(column)) {
            throw new UnsupportedOperationException("Column Name Not Support Empty Value!");
        }

        if (value == null) {
            throw new UnsupportedOperationException("Column Value Not Support Null Value!");
        }

        Class<?> valueTypeClass = value.getClass();
        StringBuilder sb = new StringBuilder(column).append("=");
        if (String.class.equals(valueTypeClass)) {

            sb.append("'").append(value.toString()).append("'");
        } else if (valueTypeClass.equals(Integer.class)
                || valueTypeClass.equals(int.class)) {

            sb.append(value.toString());
        } else if (valueTypeClass.equals(Float.class)
                || valueTypeClass.equals(float.class)) {

            sb.append(value.toString());
        } else if (valueTypeClass.equals(Double.class)
                || valueTypeClass.equals(double.class)) {

            sb.append(value.toString());
        } else if (valueTypeClass.equals(Long.class)
                || valueTypeClass.equals(long.class)) {

            sb.append(value.toString());
        } else if (valueTypeClass.equals(Boolean.class)
                || valueTypeClass.equals(boolean.class)) {

            sb.append(value.toString());
        } else {

            byte[] bytes = SerializeHelper.serialize(value);
            sb.append(bytes.toString());
        }

        mUpdateColumnExpressions.put(column, sb.toString());
        return this;
    }

    protected void resetSetExpression() {
        if (mUpdateColumnExpressions == null
                || mUpdateColumnExpressions.size() == 0) {
            return;
        }

        List<String> tupples = new LinkedList<>(mUpdateColumnExpressions.values());
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < tupples.size(); i++) {
            String exp = tupples.get(i);
            sb.append(exp);
            if (i < (tupples.size() - 1)) {
                sb.append(",");
            }
        }
        set(sb.toString());
    }

    /**
     * Where条件
     *
     * @param column
     * @param value
     * @return
     */
    public UpdateQuery<T> addWhereColumn(String column, Object value) {

        if (TextUtils.isEmpty(column)) {
            throw new UnsupportedOperationException("Column Name Not Support Empty Value!");
        }

        if (value == null) {
            throw new UnsupportedOperationException("Column Value Not Support Null Value!");
        }

        Class<?> valueTypeClass = value.getClass();
        StringBuilder sb = new StringBuilder(column).append("=");
        if (String.class.equals(valueTypeClass)) {

            sb.append("'").append(value.toString()).append("'");
        } else if (valueTypeClass.equals(Integer.class)
                || valueTypeClass.equals(int.class)) {

            sb.append(value.toString());
        } else if (valueTypeClass.equals(Float.class)
                || valueTypeClass.equals(float.class)) {

            sb.append(value.toString());
        } else if (valueTypeClass.equals(Double.class)
                || valueTypeClass.equals(double.class)) {

            sb.append(value.toString());
        } else if (valueTypeClass.equals(Long.class)
                || valueTypeClass.equals(long.class)) {

            sb.append(value.toString());
        } else if (valueTypeClass.equals(Boolean.class)
                || valueTypeClass.equals(boolean.class)) {

            sb.append(value.toString());
        } else {

            byte[] bytes = SerializeHelper.serialize(value);
            sb.append(bytes.toString());
        }

        mWhereColumnExpressions.put(column, sb.toString());
        return this;
    }

    protected void resetWhereExpression() {
        if (mWhereColumnExpressions == null
                || mWhereColumnExpressions.size() == 0) {
            return;
        }
        List<String> tupples = new LinkedList<>(mWhereColumnExpressions.values());
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < tupples.size(); i++) {
            String exp = tupples.get(i);
            sb.append(exp);
            if (i < (tupples.size() - 1)) {
                sb.append(" AND ");
            }
        }
        where(sb.toString());
    }

    // >>>>>>>>>>>>>>>  具体的查询提供的方法说明  <<<<<<<<<<<<<<<<<<<< //

    /**
     * 更新
     */
    public void update() {

        // 重设SET EXP
        resetSetExpression();

        // 重设Where语句
        resetWhereExpression();

        SQLiteStatement mStmt = compile();
        if (mDatabase.isDbLockedByCurrentThread()) {

            int num = mStmt.executeUpdateDelete();
        } else {

            mDatabase.beginTransaction();
            try {

                int num = mStmt.executeUpdateDelete();
                mDatabase.setTransactionSuccessful();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                mDatabase.endTransaction();
            }
        }
    }
}
