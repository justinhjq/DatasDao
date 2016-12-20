package ttyy.com.datasdao.modules;

import android.database.Cursor;
import android.text.TextUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Type;

import ttyy.com.datasdao.annos.Column;

/**
 * Author: Administrator
 * Date  : 2016/12/20 09:51
 * Name  : ModuleColumn
 * Intro : Edit By Administrator
 * Modification  History:
 * Date          Author        	 Version          Description
 * ----------------------------------------------------------
 * 2016/12/20    Administrator   1.0              1.0
 */
public final class ModuleColumn {

    private Field mField;
    private Column mColumn;

    private String mPropertyName;
    private Type mPropertyType;

    private String mSQLDescription;

    private ModuleColumn(Field field) {
        this.mField = field;
        this.mField.setAccessible(true);
        this.mColumn = field.getAnnotation(Column.class);

        this.mPropertyName = mField.getName();
        this.mPropertyType = mField.getType();
    }

    protected static ModuleColumn from(Field field) {
        return new ModuleColumn(field);
    }

    /**
     * 是否可忽略
     *
     * @return
     */
    public boolean isIgnore() {
        return mColumn == null || mColumn.ignore();
    }

    /**
     * 获取Column
     *
     * @return
     */
    public Column getColumn() {
        return this.mColumn;
    }

    /**
     * 获取Field
     *
     * @return
     */
    public Field getField() {
        return this.mField;
    }

    /**
     * 获取对应的列名称
     *
     * @return
     */
    public String getColumnName() {
        return TextUtils.isEmpty(mColumn.name()) ? this.mPropertyName : mColumn.name();
    }

    /**
     * 获取Class Field属性名称
     *
     * @return
     */
    public String getPropertyName() {
        return this.mPropertyName;
    }

    /**
     * 获取Field 属性类型
     * @return
     */
    public Type getPropertyType(){
        return this.mPropertyType;
    }

    /**
     * 设置obj的mFiled的属性值
     *
     * @param obj
     * @param value
     * @throws IllegalAccessException
     */
    public void setPropertyValue(Object obj, Object value) throws IllegalAccessException {
        this.mField.set(obj, value);
    }

    /**
     * 设置obj的mFiled的属性值
     *
     * @param obj
     * @param cursor
     * @throws IllegalAccessException
     */
    public void setPropertyType(Object obj, Cursor cursor) throws IllegalAccessException {
        Object value = null;
        int columnIndex = cursor.getColumnIndex(getColumnName());

        if(columnIndex == -1)
            return;

        if (String.class.equals(getPropertyType())) {
            value = cursor.getString(columnIndex);

        } else if (getPropertyType().equals(Integer.class)
                || getPropertyType().equals(int.class)) {
            value = cursor.getInt(columnIndex);

        } else if (getPropertyType().equals(Float.class)
                || getPropertyType().equals(float.class)) {
            value = cursor.getFloat(columnIndex);

        } else if (getPropertyType().equals(Double.class)
                || getPropertyType().equals(double.class)) {
            value = cursor.getDouble(columnIndex);

        } else if (getPropertyType().equals(Long.class)
                || getPropertyType().equals(long.class)) {
            value = cursor.getLong(columnIndex);

        } else if (getPropertyType().equals(Boolean.class)
                || getPropertyType().equals(boolean.class)) {
            String valueStr = cursor.getString(columnIndex);
            value = false;
            if (!TextUtils.isEmpty(valueStr)) {
                value = Boolean.parseBoolean(valueStr);
            }

        } else {
            byte[] bytes = cursor.getBlob(columnIndex);
            value = SerializeHelper.reSerialize(bytes);
        }

        setPropertyValue(obj, value);
    }

    /**
     * 获取SQL语句对该Column的描述
     *
     * @return
     */
    public String getSQLDescription() {
        if (mSQLDescription == null) {
            StringBuilder sb = new StringBuilder();
            sb.append(getColumnName());
            sb.append(" ");
            if (String.class.equals(mPropertyType)) {
                sb.append("CHAR(255)");
            } else if (Integer.class.equals(mPropertyType)
                    || int.class.equals(mPropertyType)) {
                sb.append("INTEGER");
            } else if (Float.class.equals(mPropertyType)
                    || float.class.equals(mPropertyType)) {
                sb.append("FLOAT");
            } else if (Double.class.equals(mPropertyType)
                    || double.class.equals(mPropertyType)) {
                sb.append("DOUBLE");
            } else if (Long.class.equals(mPropertyType)
                    || long.class.equals(mPropertyType)) {
                sb.append("LONG");
            } else if (Boolean.class.equals(mPropertyType)
                    || boolean.class.equals(mPropertyType)) {
                sb.append("CHAR(8)");
            } else {
                sb.append("BLOB");
            }

            if (mColumn != null) {
                if (!mColumn.nullble()) {
                    sb.append(" NOT NULL");
                }

                if (mColumn.unique()) {
                    sb.append(" UNIQUE");
                }
            }

            mSQLDescription = sb.toString();
        }

        return mSQLDescription;
    }
}