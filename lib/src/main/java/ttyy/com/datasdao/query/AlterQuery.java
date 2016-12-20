package ttyy.com.datasdao.query;

import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;

import java.lang.reflect.Type;

import ttyy.com.datasdao.modules.ModuleColumn;

/**
 * Author: Administrator
 * Date  : 2016/12/20 10:32
 * Name  : AlterQuery
 * Intro : SQLite Alter操作 只支持RENAME, ADD两种
 * Modification  History:
 * Date          Author        	 Version          Description
 * ----------------------------------------------------------
 * 2016/12/20    Administrator   1.0              1.0
 */
public class AlterQuery<T> extends BaseQuery<T> {

    String mNewColumnDescription;

    String mTableOldName;
    String mTableNewName;

    public AlterQuery(Class<T> tClass, SQLiteDatabase database) {
        super(tClass, database);
    }

    public AlterQuery addColumn(ModuleColumn mColumn) {
        mNewColumnDescription = mColumn.getSQLDescription();
        String sql = createSql();

        if(isDebug){
            Log.i("Datas",">>>>>> "+sql+" <<<<<<<<");
        }


        if (mDatabase.isDbLockedByCurrentThread()) {
            mDatabase.execSQL(sql);
        } else {
            try {
                mDatabase.beginTransaction();
                mDatabase.execSQL(sql);
                mDatabase.setTransactionSuccessful();
            } finally {
                mDatabase.endTransaction();
            }
        }
        return this;
    }

    public AlterQuery addColumn(String columnName, Type type) {
        StringBuilder sb = new StringBuilder();
        sb.append(columnName);
        sb.append(" ");
        if (String.class.equals(type)) {
            sb.append("CHAR(255)");
        } else if (Integer.class.equals(type)
                || int.class.equals(type)) {
            sb.append("INTEGER");
        } else if (Float.class.equals(type)
                || float.class.equals(type)) {
            sb.append("FLOAT");
        } else if (Double.class.equals(type)
                || double.class.equals(type)) {
            sb.append("DOUBLE");
        } else if (Long.class.equals(type)
                || long.class.equals(type)) {
            sb.append("LONG");
        } else if (Boolean.class.equals(type)
                || boolean.class.equals(type)) {
            sb.append("CHAR(8)");
        } else {
            sb.append("BLOB");
        }
        mNewColumnDescription = sb.toString();

        String sql = createSql();

        if(isDebug){
            Log.i("Datas",">>>>>> "+sql+" <<<<<<<<");
        }


        if (mDatabase.isDbLockedByCurrentThread()) {
            mDatabase.execSQL(sql);
        } else {
            try {
                mDatabase.beginTransaction();
                mDatabase.execSQL(sql);
                mDatabase.setTransactionSuccessful();
            } finally {
                mDatabase.endTransaction();
            }
        }

        return this;
    }

    public AlterQuery renameTable(String oldName, String newName) {
        mTableNewName = newName;
        mTableOldName = oldName;
        String sql = createSql();

        if(isDebug){
            Log.i("Datas",">>>>>> "+sql+" <<<<<<<<");
        }

        if (mDatabase.isDbLockedByCurrentThread()) {
            mDatabase.execSQL(sql);
        } else {
            try {
                mDatabase.beginTransaction();
                mDatabase.execSQL(sql);
                mDatabase.setTransactionSuccessful();
            } finally {
                mDatabase.endTransaction();
            }
        }
        return this;
    }

    @Override
    protected String createSql() {
        try {
            StringBuilder sb = new StringBuilder();
            if(!TextUtils.isEmpty(mTableNewName)){
                sb.append("ALTER TABLE ")
                        .append(mTableOldName)
                        .append(" RENAME TO ")
                        .append(mTableNewName);
            }else {
                sb.append("ALTER TABLE ")
                        .append(getTableName())
                        .append(" ADD COLUMN ")
                        .append(mNewColumnDescription);
            }
            return sb.toString();
        } finally {
            mNewColumnDescription = null;
            mTableOldName = null;
            mTableNewName = null;
        }
    }
}
