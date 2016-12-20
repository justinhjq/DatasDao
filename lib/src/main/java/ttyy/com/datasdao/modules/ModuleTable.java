package ttyy.com.datasdao.modules;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ttyy.com.datasdao.annos.Table;
import ttyy.com.datasdao.convertor.CursorConvertor;

/**
 * Author: Administrator
 * Date  : 2016/12/20 10:27
 * Name  : ModuleTable
 * Intro : Edit By Administrator
 * Modification  History:
 * Date          Author        	 Version          Description
 * ----------------------------------------------------------
 * 2016/12/20    Administrator   1.0              1.0
 */
public final class ModuleTable<T> {

    Class<T> tClass;
    List<ModuleColumn> mColumns;
    SQLiteDatabase mDatabase;

    String mTableName;
    String mCreateTableSql;

    CursorConvertor mCursorConvertor = CursorConvertor.DEFAULT;

    private ModuleTable(Class<T> clazz, SQLiteDatabase database){
        this.tClass = clazz;
        this.mDatabase = database;
    }

    public static <TB> ModuleTable<TB> from(Class<TB> clazz, SQLiteDatabase database){
        return new ModuleTable<>(clazz, database);
    }

    /**
     * 设置转换工具
     * @param convertor
     */
    public void setCursorConvertor(CursorConvertor convertor){
        this.mCursorConvertor = convertor;
    }

    /**
     * 讲cursor转换为数据模型
     * @param cursor
     * @return
     */
    public T convertFromCursor(Cursor cursor){
        return mCursorConvertor.convertFromCursor(this, cursor);
    }

    public boolean isTableExists(){

        StringBuilder sqlSb = new StringBuilder();
        sqlSb.append("SELECT COUNT(*) FROM sqlite_master WHERE type = 'table' AND name = ?");

        Cursor cursor = null;
        if (mDatabase.isDbLockedByCurrentThread()) {
            cursor = mDatabase.rawQuery(sqlSb.toString(), new String[]{getTableName()});

        } else {
            mDatabase.beginTransaction();
            try {

                cursor = mDatabase.rawQuery(sqlSb.toString(), new String[]{getTableName()});
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

    public String getCreateTableSql(){

        if(mCreateTableSql == null){
            StringBuilder sqlSb = new StringBuilder();
            sqlSb.append("CREATE TABLE IF NOT EXISTS ");
            sqlSb.append(getTableName());

            StringBuilder sb = new StringBuilder();
            for (ModuleColumn tmp : getColumns()) {
                sb.append(tmp.getSQLDescription());
                if (getColumns().indexOf(tmp) < (getColumns().size() - 1)) {
                    sb.append(",");
                }
            }

            if (sb.length() > 0) {
                sqlSb.append("( ");
                sqlSb.append(sb);
                sqlSb.append(" );");
            } else {
                throw new UnsupportedOperationException("Clazz Has No Columns");
            }

            mCreateTableSql = sqlSb.toString();
        }
        return mCreateTableSql;
    }

    public List<ModuleColumn> getColumns(){
        if(mColumns == null){
            mColumns = new ArrayList<>();
            Field[] fields = tClass.getDeclaredFields();
            for (int i = 0; i < fields.length; i++) {
                Field field = fields[i];
                ModuleColumn mDaoColumn = ModuleColumn.from(field);
                if (mDaoColumn.isIgnore())
                    continue;
                mColumns.add(mDaoColumn);
            }
        }

        return mColumns;
    }

    public void createTable(){

        String sql = getCreateTableSql();
        if (mDatabase.isDbLockedByCurrentThread()) {
            mDatabase.execSQL(sql);

        } else {
            // 若数据库被其他线程锁住 那么用事务延迟获取数据库连接
            mDatabase.beginTransaction();
            try {
                mDatabase.execSQL(sql);
                mDatabase.setTransactionSuccessful();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                mDatabase.endTransaction();
            }
        }
    }

    public void dropTable(){
        StringBuilder sqlSb = new StringBuilder();
        sqlSb.append("DROP TABLE IF EXISTS ");
        sqlSb.append(getTableName());
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

    static Pattern mPattern = Pattern.compile(".*\\.(.*)\\.(.*)");
    public String getTableName(){
        if(mTableName == null){
            Table mAnnoTable = tClass.getAnnotation(Table.class);
            if (mAnnoTable != null
                    && !TextUtils.isEmpty(mAnnoTable.value())) {
                mTableName = mAnnoTable.value();
            } else {
                String clazzPath = tClass.getCanonicalName();

                String tableName = null;
                if (clazzPath.split("\\.").length > 2) {
                    Matcher matcher = mPattern.matcher(clazzPath);
                    if (matcher.find()) {
                        tableName = matcher.group(1) + "$" + matcher.group(2);
                    }
                } else {
                    tableName = clazzPath.replaceAll("\\.", "$");
                }

                mTableName = tableName;
            }
        }

        return mTableName;
    }

    public Class<T> getTableClass(){
        return tClass;
    }
}