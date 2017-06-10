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
 * Name  : DeleteQuery
 * Intro : 删除查询
 * Modification  History:
 * Date          Author        	 Version          Description
 * ----------------------------------------------------------
 * 2016/08/18    hjq   1.0              1.0
 */
public abstract class DeleteQuery<T> extends BaseQuery<T>{

    /**
     * where条件语句
     */
    protected String str_where;

    protected HashMap<String, String> mWhereColumnExpressions;

    public DeleteQuery(Class<T> tClass, SQLiteDatabase database) {
        super(tClass, database);

        mWhereColumnExpressions = new HashMap<>();
    }

    /**
     * where 条件语句
     * @param where
     */
    public DeleteQuery<T> where(String where){
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

    public DeleteQuery<T> addWhereColumn(String column, Object value){

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

    // >>>>>>>>>>>>>>>  具体的删除提供的方法说明  <<<<<<<<<<<<<<<<<<<< //
    /**
     * 删除
     */
    public void delete(){
        deleteOne(null);
    }

    /**
     * 是否从Class中提取Where语句
     * @return
     */
    protected final boolean isWhereClauseFromClass(){
        return "from_class".equals(str_where);
    }

    /**
     * 设置Where语句从class中提取
     */
    protected final void setWhereClauseFromClass(){
        str_where = "from_class";
    }

    /**
     * 删除一个对象
     * @param one
     */
    public void deleteOne(T one){

        if(one != null){
            setWhereClauseFromClass();
        }

        SQLiteStatement stmt = compile();

        if (mDatabase.isDbLockedByCurrentThread()) {

            bindValues(stmt, one);
            stmt.executeUpdateDelete();
        } else {

            mDatabase.beginTransaction();
            try {
                bindValues(stmt, one);
                stmt.executeUpdateDelete();
                mDatabase.setTransactionSuccessful();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                mDatabase.endTransaction();
            }

        }
    }

    /**
     * 删除一个列表
     * @param objs
     */
    public void deleteAll(List<T> objs){
        if(objs == null
                || objs.size() < 1 ){
            delete();
            return;
        }

        setWhereClauseFromClass();
        SQLiteStatement stmt = compile();

        if (mDatabase.isDbLockedByCurrentThread()) {

            for(T tmp : objs){
                bindValues(stmt, tmp);
                stmt.executeUpdateDelete();
            }

        } else {

            mDatabase.beginTransaction();
            try {
                for(T tmp : objs){
                    bindValues(stmt, tmp);
                    stmt.executeUpdateDelete();
                }
                mDatabase.setTransactionSuccessful();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                mDatabase.endTransaction();
            }
        }
    }

    public void dropTable() {
        mModuleTable.dropTable();
    }
}
