package ttyy.com.datasdao.util;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.text.TextUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ttyy.com.datasdao.annos.Column;
import ttyy.com.datasdao.annos.Table;

/**
 * Author: hjq
 * Date  : 2016/12/19 21:12
 * Name  : InnerUtil
 * Intro : Edit By hjq
 * Modification  History:
 * Date          Author        	 Version          Description
 * ----------------------------------------------------------
 * 2016/12/19    hjq   1.0              1.0
 */
public class InnerUtil {
    /**
     * Author: hujinqi
     * Date  : 2016-08-18
     * Description: 转换工具
     */
    public static class DaoParser {

        /**
         * 数据库表 所有字段
         */
        public static String tableColumns(Class<?> clazz) {

            StringBuilder sb = new StringBuilder();

            Field[] fields = clazz.getDeclaredFields();
            for (int i = 0; i < fields.length; i++) {

                Field field = fields[i];
                field.setAccessible(true);
                String columnName = field.getName();
                Type columnType = field.getType();

                Column column = field.getAnnotation(Column.class);
                if (column != null) {
                    if (column.ignore()) {
                        continue;
                    }
                }

                sb.append(columnName);
                sb.append(" ");
                if (String.class.equals(columnType)) {
                    sb.append("CHAR(255)");
                } else if (columnType.equals(Integer.class)
                        || columnType.equals(int.class)) {
                    sb.append("INTEGER");
                } else if (columnType.equals(Float.class)
                        || columnType.equals(float.class)) {
                    sb.append("FLOAT");
                } else if (columnType.equals(Double.class)
                        || columnType.equals(double.class)) {
                    sb.append("DOUBLE");
                } else if (columnType.equals(Long.class)
                        || columnType.equals(long.class)) {
                    sb.append("LONG");
                } else if (columnType.equals(Boolean.class)
                        || columnType.equals(boolean.class)) {
                    sb.append("CHAR(8)");
                } else {
                    sb.append("BLOB");
                }

                if (column != null) {
                    if (!column.nullble()) {
                        sb.append(" NOT NULL");
                    }

                    if (column.unique()) {
                        sb.append(" UNIQUE");
                    }
                }

                if (i < (fields.length - 1)) {
                    sb.append(",");
                }

            }

            return sb.toString();
        }

        /**
         * 获取实体的属性列表
         */
        public static ArrayList<String> entityColumns(Class<?> clazz) {

            ArrayList<String> columns = new ArrayList<String>();
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                Column column = field.getAnnotation(Column.class);
                if (column != null && column.ignore())
                    continue;
                columns.add(field.getName());
            }

            return columns;
        }

        /**
         * cursor 转换为 entity 实体
         */
        public static <T> T cursorToEntity(Class<T> clazz, Cursor cursor, String[] mColumns) {
            Object object = null;

            if (mColumns == null) {
                mColumns = entityColumns(clazz).toArray(new String[]{});
            }

            object = cursorToEntityWithColumns(clazz, cursor, mColumns);
            return (T) object;
        }

        protected static <T> T cursorToEntityWithColumns(Class<T> clazz, Cursor cursor, String[] mColumns) {
            try {
                Object object = clazz.newInstance();
                for (String temp : mColumns) {

                    Field field = clazz.getDeclaredField(temp);
                    field.setAccessible(true);

                    String columnName = field.getName();
                    Type columnType = field.getType();
                    int columnIndex = cursor.getColumnIndex(columnName);

                    // 不识别的column
                    if (columnIndex == -1)
                        continue;

                    if (String.class.equals(columnType)) {
                        String value = cursor.getString(columnIndex);
                        field.set(object, value);

                    } else if (columnType.equals(Integer.class)
                            || columnType.equals(int.class)) {
                        int value = cursor.getInt(columnIndex);
                        field.set(object, value);

                    } else if (columnType.equals(Float.class)
                            || columnType.equals(float.class)) {
                        float value = cursor.getFloat(columnIndex);
                        field.set(object, value);

                    } else if (columnType.equals(Double.class)
                            || columnType.equals(double.class)) {
                        double value = cursor.getDouble(columnIndex);
                        field.set(object, value);

                    } else if (columnType.equals(Long.class)
                            || columnType.equals(long.class)) {
                        long value = cursor.getLong(columnIndex);
                        field.set(object, value);

                    } else if (columnType.equals(Boolean.class)
                            || columnType.equals(boolean.class)) {
                        String valueStr = cursor.getString(columnIndex);
                        boolean value = false;
                        if (!TextUtils.isEmpty(valueStr)) {
                            value = Boolean.parseBoolean(valueStr);
                        }
                        field.set(object, value);

                    } else {
                        byte[] bytes = cursor.getBlob(columnIndex);
                        Object value = SerializeHelper.reSerialize(bytes);
                        field.set(object, value);
                    }
                }

                return (T) object;
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }

            return null;
        }

    }

    /**
     * Author: hujinqi
     * Date  : 2016-08-18
     * Description:
     */
    public static class TableUtil {

        /**
         * 删除该数据库下所有自定义的表
         */
        public static void dropAllTables(SQLiteDatabase mDatabase) {
            if (mDatabase.isDbLockedByCurrentThread()) {
                executeDropAllCommands(mDatabase);
            } else {
                try {
                    // 开启事务
                    mDatabase.beginTransaction();
                    executeDropAllCommands(mDatabase);
                    mDatabase.setTransactionSuccessful();
                } finally {
                    // 提交事务 否做之前的操作无效数据库会回滚到开启事务之前的状态
                    mDatabase.endTransaction();
                }
            }
        }

        private static void executeDropAllCommands(SQLiteDatabase mDatabase) {
            // 查询所有表名
            List<String> names = new LinkedList<String>();
            String sql = "select name from sqlite_master where type = 'table'";
            Cursor cursor = mDatabase.rawQuery(sql, null);
            if (cursor != null) {
                // 遍历游标获取所有的表名
                while (cursor.moveToNext()) {
                    String name = cursor.getString(0);
                    names.add(name);
                }
                cursor.close();// 关闭游标防止内存泄露
                // 删除表
                for (String name : names) {
                    String d_sql = "DROP TABLE " + name;
                    mDatabase.execSQL(d_sql);
                }
            }
        }


        static Pattern mPattern = Pattern.compile(".*\\.(.*)\\.(.*)");

        /**
         * 获取表名
         *
         * @param clazz
         * @return
         */
        public static String getTableName(Class<?> clazz) {

            Table mAnnoTable = clazz.getAnnotation(Table.class);
            if (mAnnoTable != null
                    && !TextUtils.isEmpty(mAnnoTable.value())) {
                return mAnnoTable.value();
            } else {
                String clazzPath = clazz.getCanonicalName();

                String tableName = null;
                if (clazzPath.split("\\.").length > 2) {
                    Matcher matcher = mPattern.matcher(clazzPath);
                    if (matcher.find()) {
                        tableName = matcher.group(1) + "$" + matcher.group(2);
                    }
                } else {
                    tableName = clazzPath.replaceAll("\\.", "$");
                }

                return tableName;
            }
        }
    }
}
