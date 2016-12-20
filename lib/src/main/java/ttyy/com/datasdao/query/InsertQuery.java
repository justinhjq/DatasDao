package ttyy.com.datasdao.query;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import java.util.List;

/**
 * Author: hjq
 * Date  : 2016/08/18 21:03
 * Name  : InsertQuery
 * Intro : 插入查询
 * Modification  History:
 * Date          Author        	 Version          Description
 * ----------------------------------------------------------
 * 2016/08/18    hjq   1.0              1.0
 */
public abstract class InsertQuery<T> extends BaseQuery<T> {

    public InsertQuery(Class<T> tClass, SQLiteDatabase database) {
        super(tClass, database);
    }

    // >>>>>>>>>>>>>>>  具体的插入提供的方法说明  <<<<<<<<<<<<<<<<<<<< //

    /**
     * 插入一个
     *
     * @param one
     */
    public void insert(T one) {

        SQLiteStatement mStmt = compile();

        if (mDatabase.isDbLockedByCurrentThread()) {

            bindValues(mStmt, one);
            mStmt.executeInsert();
        } else {
            mDatabase.beginTransaction();
            try {

                bindValues(mStmt, one);
                mStmt.executeInsert();
                mDatabase.setTransactionSuccessful();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                mDatabase.endTransaction();
            }

        }

    }

    /**
     * 插入多个
     *
     * @param ones
     */
    public void insertAll(List<T> ones) {

        SQLiteStatement mStmt = compile();

        if (mDatabase.isDbLockedByCurrentThread()) {

            for (T entity : ones) {
                bindValues(mStmt, entity);
                mStmt.executeInsert();
            }
        } else {
            mDatabase.beginTransaction();
            try {
                for (T entity : ones) {
                    bindValues(mStmt, entity);
                    mStmt.executeInsert();
                }
                mDatabase.setTransactionSuccessful();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                mDatabase.endTransaction();
            }

        }
    }

}
