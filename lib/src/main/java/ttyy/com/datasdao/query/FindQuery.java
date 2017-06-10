package ttyy.com.datasdao.query;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import ttyy.com.datasdao.convertor.CursorConvertor;
import ttyy.com.datasdao.modules.SerializeHelper;

/**
 * Author: hjq
 * Date  : 2016/08/18 21:03
 * Name  : FindQuery
 * Intro : 查找查询
 * Modification  History:
 * Date          Author        	 Version          Description
 * ----------------------------------------------------------
 * 2016/08/18    hjq   1.0              1.0
 */
public abstract class FindQuery<T> extends BaseQuery<T> implements AggregateFunctions<T> {

    /**
     * where条件语句
     */
    protected String str_where;

    protected HashMap<String, String> mWhereColumnExpressions;

    public FindQuery(Class<T> tClass, SQLiteDatabase database) {
        super(tClass, database);

        mWhereColumnExpressions = new HashMap<>();
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

    @Override
    public FindQuery setCursorConvertor(CursorConvertor convertor) {
        super.setCursorConvertor(convertor);
        return this;
    }

    public FindQuery<T> addWhereColumn(String column, Object value){

        if(TextUtils.isEmpty(column)){
            throw new UnsupportedOperationException("Column Name Not Support Empty Value!");
        }

        if(value == null){
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

    protected void resetWhereExpression(){
        if(mWhereColumnExpressions == null
                || mWhereColumnExpressions.size() == 0){
            return;
        }
        List<String> tupples = new LinkedList<>(mWhereColumnExpressions.values());
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < tupples.size(); i++){
            String exp = tupples.get(i);
            sb.append(exp);
            if(i < (tupples.size() - 1)){
                sb.append(" AND ");
            }
        }
        where(sb.toString());
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
                    result = mModuleTable.convertFromCursor(cursor);
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
                if(cursor.moveToLast()){
                    result = mModuleTable.convertFromCursor(cursor);
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
                    T tmp = mModuleTable.convertFromCursor(cursor);
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
                    result = mModuleTable.convertFromCursor(cursor);
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
                        T tmp = mModuleTable.convertFromCursor(cursor);
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
