package ttyy.com.datasdao.query;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.List;

import ttyy.com.datasdao.convertor.CursorConvertor;
import ttyy.com.datasdao.modules.ModuleColumn;
import ttyy.com.datasdao.modules.ModuleTable;
import ttyy.com.datasdao.modules.SerializeHelper;

/**
 * Author: hjq
 * Date  : 2016/12/19 21:14
 * Name  : BaseQuery
 * Intro : Edit By hjq
 * Modification  History:
 * Date          Author        	 Version          Description
 * ----------------------------------------------------------
 * 2016/12/19    hjq   1.0              1.0
 */
public abstract class BaseQuery<T> {

    protected ModuleTable<T> mModuleTable;
    protected SQLiteDatabase mDatabase;

    protected boolean isDebug;

    public BaseQuery(Class<T> tClass, SQLiteDatabase database){
        this.mDatabase = database;
        this.mModuleTable = ModuleTable.from(tClass, database);

        if(!mModuleTable.isTableExists()){
            if(isDebug){
                Log.i("Datas",">>>>>> Command: Delete "+ getTableName() +" Not Exists! <<<<<<");
            }
            mModuleTable.createTable();
        }
    }

    /**
     * 设置Debug模式
     * @param isDebug
     */
    public void setIsDebug(boolean isDebug){
        this.isDebug = isDebug;
    }

    protected <M extends BaseQuery> M setCursorConvertor(CursorConvertor convertor){
        mModuleTable.setCursorConvertor(convertor);
        return (M) this;
    }

    /**
     * 生成sql语句
     * @return
     */
    protected abstract String createSql();

    /**
     * 生成数据库查询对象
     *
     * @return
     */
    protected SQLiteStatement compile() {
        String sql = createSql();
        SQLiteStatement stmt = mDatabase.compileStatement(sql);
        return stmt;
    }

    /**
     * 获取数据库表格列
     * @return
     */
    public final List<ModuleColumn> getColumns(){
        return mModuleTable.getColumns();
    }

    /**
     * 获取数据库表格名称
     * @return
     */
    public final String getTableName(){
        return mModuleTable.getTableName();
    }

    /**
     * 数据预处理绑定
     *
     * @param stmt
     * @param entity
     */
    protected void bindValues(SQLiteStatement stmt,
                                  Object entity) {

        if (entity == null) {
            return;
        }

        for (int i = 0; i < getColumns().size(); i++) {
            int stmtIndex = i + 1;
            ModuleColumn mColumn = getColumns().get(i);
            try {

                Type columnType = mColumn.getPropertyType();
                Field mField = mColumn.getField();

                if (String.class.equals(columnType)) {
                    String value = (String) mField.get(entity);
                    if (value == null) {
                        stmt.bindNull(stmtIndex);
                    } else {
                        stmt.bindString(stmtIndex, value);
                    }

                } else if (columnType.equals(Integer.class)
                        || columnType.equals(int.class)) {
                    int value = mField.getInt(entity);
                    stmt.bindLong(stmtIndex, value);

                } else if (columnType.equals(Float.class)
                        || columnType.equals(float.class)) {
                    float value = mField.getFloat(entity);
                    stmt.bindDouble(stmtIndex, value);

                } else if (columnType.equals(Double.class)
                        || columnType.equals(double.class)) {
                    double value = mField.getDouble(entity);
                    stmt.bindDouble(stmtIndex, value);

                } else if (columnType.equals(Long.class)
                        || columnType.equals(long.class)) {
                    long value = mField.getLong(entity);
                    stmt.bindLong(stmtIndex, value);

                } else if (columnType.equals(Boolean.class)
                        || columnType.equals(boolean.class)) {
                    boolean value = mField.getBoolean(entity);
                    stmt.bindString(stmtIndex, value + "");

                } else {
                    Object obj = mField.get(entity);
                    if (obj == null) {
                        stmt.bindNull(stmtIndex);
                    } else {
                        byte[] value = SerializeHelper.serialize(obj);
                        stmt.bindBlob(stmtIndex, value);
                    }

                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                stmt.bindNull(stmtIndex);
                continue;
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
                stmt.bindNull(stmtIndex);
                continue;
            }

        }
    }

}
