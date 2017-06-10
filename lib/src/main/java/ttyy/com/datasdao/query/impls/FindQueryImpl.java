package ttyy.com.datasdao.query.impls;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;

import ttyy.com.datasdao.query.FindQuery;

/**
 * Author: hujinqi
 * Date  : 2016-08-18
 * Description: 查询命令实现
 */
public class FindQueryImpl<T> extends FindQuery<T> {

    protected String mFunctionColumn;
    protected String mFunctionName;

    protected String mLimit;
    protected String mOrderBy;

    public FindQueryImpl(Class<T> tClass, SQLiteDatabase database) {
        super(tClass, database);
    }

    @Override
    protected String createSql() {

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        if (!TextUtils.isEmpty(mFunctionName)) {
            sb.append(mFunctionName)
                    .append("(")
                    .append(mFunctionColumn).append(")");
        } else {
            sb.append(" * ");
        }
        sb.append(" FROM ").append(getTableName());

        // 重设Where语句
        resetWhereExpression();

        if (!TextUtils.isEmpty(str_where)) {
            sb.append(" WHERE " + str_where);
        }

        if(!TextUtils.isEmpty(mOrderBy)){
            sb.append(" ").append(mOrderBy);
        }

        if (!TextUtils.isEmpty(mLimit)) {
            sb.append(" ").append(mLimit);
        }

        if(isDebug){
            Log.i("Datas",">>>>>> "+sb.toString()+" <<<<<<<<");
        }

        return sb.toString();
    }

    @Override
    protected Cursor go() {

        String sql = createSql();
        Cursor cursor = null;
        if(mDatabase.isDbLockedByCurrentThread()){
            cursor = mDatabase.rawQuery(sql, null);

        }else{
            mDatabase.beginTransaction();
            try {

                cursor = mDatabase.rawQuery(sql, null);
                mDatabase.setTransactionSuccessful();
            } catch (Exception e) {
                e.printStackTrace();
            } finally{
                mDatabase.endTransaction();
            }

        }

        return cursor;
    }

    @Override
    public int count() {
        mFunctionName = "COUNT";
        mFunctionColumn = "*";

        Cursor cursor = go();
        if(cursor != null){
            try{
                if(cursor.moveToNext()){
                    return cursor.getInt(0);
                }
            }finally {
                cursor.close();
            }
        }

        return -1;
    }

    @Override
    public double average(String column) {
        mFunctionName = "AVERAGE";
        mFunctionColumn = column == null ? "*" : column;

        Cursor cursor = go();
        if(cursor != null){
            try{
                if(cursor.moveToNext()){
                    return cursor.getDouble(0);
                }
            }finally {
                cursor.close();
            }
        }

        return -1;
    }

    @Override
    public double min(String column) {
        mFunctionName = "MIN";
        mFunctionColumn = column == null ? "*" : column;

        Cursor cursor = go();
        if(cursor != null){
            try{
                if(cursor.moveToNext()){
                    return cursor.getDouble(0);
                }
            }finally {
                cursor.close();
            }
        }

        return -1;
    }

    @Override
    public double max(String column) {
        mFunctionName = "MAX";
        mFunctionColumn = column == null ? "*" : column;

        Cursor cursor = go();
        if(cursor != null){
            try{
                if(cursor.moveToNext()){
                    return cursor.getDouble(0);
                }
            }finally {
                cursor.close();
            }
        }

        return -1;
    }

    /**
     * 筛选跨越的offset行 以及筛选一次性最多返回的数据量limitNumber
     */
    @Override
    public FindQuery<T> limit(int offset, int limitNumber) {
        mLimit = "LIMIT " + limitNumber + " OFFSET " + offset;
        return this;
    }

    @Override
    public FindQuery<T> orderBy(String column, String type) {
        mOrderBy = "ORDER BY "+column+" "+type;
        return this;
    }
}
