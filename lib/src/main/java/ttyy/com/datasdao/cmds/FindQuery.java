package ttyy.com.datasdao.cmds;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

import ttyy.com.datasdao.util.InnerUtil;

/**
 * Author: hujinqi
 * Date  : 2016-08-18
 * Description: 查找查询
 */
public abstract class FindQuery<T> extends BaseQuery<T> implements AggregateFunctions<T> {

    /**
     * where条件语句
     */
    protected String str_where;

    public FindQuery(Class<T> tClass, SQLiteDatabase database) {
        super(tClass, database);
    }

    /**
     * where 条件语句
     * @param where
     */
    public FindQuery<T> where(String where){
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
     * 执行Sql
     * @return
     */
    protected abstract Cursor go();

    // >>>>>>>>>>>>>>>  具体的查询提供的方法说明  <<<<<<<<<<<<<<<<<<<< //

    /**
     * 选择第一个
     * @return
     */
    public T selectFirst(){
        T result = null;
        Cursor cursor = go();
        if(cursor != null){
            try {
                if(cursor.moveToNext()){
                    result = InnerUtil.DaoParser.cursorToEntity(tClass, cursor, mColumns);
                }
            }finally {
                cursor.close();
            }
        }

        return result;
    }

    /**
     * 选择最后一个
     * @return
     */
    public T selectLast(){
        T result = null;
        Cursor cursor = go();
        if(cursor != null){
            try{
                cursor.moveToFirst();
                if(cursor.moveToLast()){
                    result = InnerUtil.DaoParser.cursorToEntity(tClass, cursor, mColumns);
                }
            }finally {
                cursor.close();
            }
        }

        return result;
    }

    /**
     * 选择所有
     * @return
     */
    public List<T> selectAll(){
        List<T> result = new ArrayList<>();
        Cursor cursor = go();
        if(cursor != null){
            try{
                while (cursor.moveToNext()){
                    T tmp = InnerUtil.DaoParser.cursorToEntity(tClass, cursor, mColumns);
                    result.add(tmp);
                }
            }finally {
                cursor.close();
            }
        }

        return result;
    }

    /**
     * 选择index
     * @param index
     * @return
     */
    public T selectAt(int index){
        T result = null;
        Cursor cursor = go();
        if(cursor != null){
            try{
                if(cursor.moveToPosition(index)){
                    result = InnerUtil.DaoParser.cursorToEntity(tClass, cursor, mColumns);
                }
            }finally {
                cursor.close();
            }
        }

        return result;
    }

    /**
     * 选择从start-end的数据
     * @param start
     * @param end
     * @return
     */
    public List<T> selectAllAt(int start, int end){
        List<T> result = new ArrayList<>();
        Cursor cursor = go();
        if(cursor != null){
            try{
                if(cursor.moveToPosition(start)){
                    do {
                        T tmp = InnerUtil.DaoParser.cursorToEntity(tClass, cursor, mColumns);
                        result.add(tmp);
                        start++;
                    }while (cursor.moveToNext() && start < end);
                }
            }finally {
                cursor.close();
            }
        }

        return result;
    }

}
