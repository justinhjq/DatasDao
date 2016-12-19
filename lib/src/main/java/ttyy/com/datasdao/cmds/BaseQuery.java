package ttyy.com.datasdao.cmds;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import java.lang.reflect.Field;
import java.lang.reflect.Type;

import ttyy.com.datasdao.util.InnerUtil;
import ttyy.com.datasdao.util.SerializeHelper;

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

    protected Class<T> tClass;
    protected String[] mColumns;
    protected String mTableName;
    protected SQLiteDatabase mDatabase;

    protected boolean isDebug;

    public BaseQuery(Class<T> tClass, SQLiteDatabase database){
        this.tClass = tClass;
        this.mDatabase = database;
        this.mTableName = InnerUtil.TableUtil.getTableName(tClass);

        if(!isTableExists()){
            if(isDebug){
                Log.i("Datas",">>>>>> Command: Delete "+ mTableName +" Not Exists! <<<<<<");
            }
            createTable();
        }
    }

    /**
     * 设置Debug模式
     * @param isDebug
     */
    public void setIsDebug(boolean isDebug){
        this.isDebug = isDebug;
    }

    /**
     * 表格是否存在
     */
    protected boolean isTableExists() {

        StringBuilder sqlSb = new StringBuilder();
        sqlSb.append("SELECT COUNT(*) FROM sqlite_master WHERE type = 'table' AND name = ?");

        Cursor cursor = null;
        if (mDatabase.isDbLockedByCurrentThread()) {
            cursor = mDatabase.rawQuery(sqlSb.toString(), new String[]{mTableName});

        } else {
            mDatabase.beginTransaction();
            try {

                cursor = mDatabase.rawQuery(sqlSb.toString(), new String[]{mTableName});
                mDatabase.setTransactionSuccessful();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                mDatabase.endTransaction();
            }
        }

        if (cursor != null) {
            try {
                if (cursor.moveToNext()) {
                    int count = cursor.getInt(0);
                    return count > 0;
                }
            } finally {
                cursor.close();
            }
        }

        return false;
    }

    /**
     * 创建表格
     */
    protected void createTable() {

        StringBuilder sqlSb = new StringBuilder();
        sqlSb.append("CREATE TABLE IF NOT EXISTS ");
        sqlSb.append(mTableName);
        String columns = InnerUtil.DaoParser.tableColumns(tClass);
        if (columns.length() > 0) {
            sqlSb.append("( ");
            sqlSb.append(columns);
            sqlSb.append(" );");
        } else {
            throw new UnsupportedOperationException("Clazz Has No Columns");
        }

        if (mDatabase.isDbLockedByCurrentThread()) {
            mDatabase.execSQL(sqlSb.toString());

        } else {
            // 若数据库被其他线程锁住 那么用事务延迟获取数据库连接
            mDatabase.beginTransaction();
            try {
                mDatabase.execSQL(sqlSb.toString());
                mDatabase.setTransactionSuccessful();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                mDatabase.endTransaction();
            }
        }
    }

    /**
     * 删除表格
     * 只有DeleteQuery才有权限删除
     */
    protected void dropTable() {

        StringBuilder sqlSb = new StringBuilder();
        sqlSb.append("DROP TABLE IF EXISTS ");
        sqlSb.append(mTableName);
        if (mDatabase.isDbLockedByCurrentThread()) {
            mDatabase.execSQL(sqlSb.toString());
        } else {
            mDatabase.beginTransaction();
            try {
                mDatabase.execSQL(sqlSb.toString());
                mDatabase.setTransactionSuccessful();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                mDatabase.endTransaction();
            }

        }
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
     * 数据预处理绑定
     *
     * @param stmt
     * @param entity
     * @param mColumns
     */
    protected void bindValues(SQLiteStatement stmt,
                                  Object entity,
                                  String[] mColumns) {

        if (entity == null) {
            return;
        }

        Class<?> tClass = entity.getClass();
        for (int i = 0; i < mColumns.length; i++) {
            int stmtIndex = i + 1;
            try {
                Field field = tClass.getDeclaredField(mColumns[i]);
                field.setAccessible(true);

                Type columnType = field.getType();

                if (String.class.equals(columnType)) {
                    String value = (String) field.get(entity);
                    if (value == null) {
                        stmt.bindNull(stmtIndex);
                    } else {
                        stmt.bindString(stmtIndex, value);
                    }

                } else if (columnType.equals(Integer.class)
                        || columnType.equals(int.class)) {
                    int value = field.getInt(entity);
                    stmt.bindLong(stmtIndex, value);

                } else if (columnType.equals(Float.class)
                        || columnType.equals(float.class)) {
                    float value = field.getFloat(entity);
                    stmt.bindDouble(stmtIndex, value);

                } else if (columnType.equals(Double.class)
                        || columnType.equals(double.class)) {
                    double value = field.getDouble(entity);
                    stmt.bindDouble(stmtIndex, value);

                } else if (columnType.equals(Long.class)
                        || columnType.equals(long.class)) {
                    long value = field.getLong(entity);
                    stmt.bindLong(stmtIndex, value);

                } else if (columnType.equals(Boolean.class)
                        || columnType.equals(boolean.class)) {
                    boolean value = field.getBoolean(entity);
                    stmt.bindString(stmtIndex, value + "");

                } else {
                    Object obj = field.get(entity);
                    if (obj == null) {
                        stmt.bindNull(stmtIndex);
                    } else {
                        byte[] value = SerializeHelper.serialize(obj);
                        stmt.bindBlob(stmtIndex, value);
                    }

                }

            } catch (NoSuchFieldException e) {
                e.printStackTrace();
                stmt.bindNull(stmtIndex);
                continue;
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
