package ttyy.com.datasdao.cmds;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.text.TextUtils;

/**
 * Author: hujinqi
 * Date  : 2016-08-18
 * Description: 更新查询
 */
public abstract class UpdateQuery<T> extends BaseQuery<T> {

    /**
     * where条件语句
     */
    protected String str_where;

    protected String str_set;

    public UpdateQuery(Class<T> tClass, SQLiteDatabase database) {
        super(tClass, database);
    }

    /**
     * where 条件语句
     * @param where
     */
    public UpdateQuery<T> where(String where){
        if(TextUtils.isEmpty(where)){
            return this;
        }

        if(where.trim().toLowerCase().startsWith("where")){
            str_where = where.trim().substring(5);
        }else {
            str_where = where;
        }

        return this;
    }

    /**
     * SET语句
     * @param set
     * @return
     */
    public UpdateQuery<T> set(String set){
        str_set = set;
        if(set.trim().toLowerCase().startsWith("set")){
            str_set = set.trim().substring(3);
        }else {
            str_set = set;
        }
        return this;
    }

    // >>>>>>>>>>>>>>>  具体的查询提供的方法说明  <<<<<<<<<<<<<<<<<<<< //

    /**
     * 更新
     */
    public void update(){
        SQLiteStatement mStmt = compile();
        if(mDatabase.isDbLockedByCurrentThread()){

            mStmt.executeUpdateDelete();
        }else{

            mDatabase.beginTransaction();
            try {

                mStmt.executeUpdateDelete();
                mDatabase.setTransactionSuccessful();
            } catch (Exception e) {
                e.printStackTrace();
            } finally{
                mDatabase.endTransaction();
            }
        }
    }
}
