package ttyy.com.datasdao.query.tool;

import android.text.TextUtils;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import ttyy.com.datasdao.modules.SerializeHelper;

/**
 * author: admin
 * date: 2017/07/12
 * version: 0
 * mail: secret
 * desc: WhereGenerator
 */

public class WhereGenerator {

    HashMap<String, String> mWhereColumnExpressions;

    String mSettedWhereSql;

    public WhereGenerator(){
        mWhereColumnExpressions = new HashMap<>();
        mSettedWhereSql = null;
    }

    /**
     * Where条件
     *
     * @param column
     * @param value
     * @return
     */
    public void addWhereColumn(String column, Object value) {

        if (TextUtils.isEmpty(column)) {
            throw new UnsupportedOperationException("Column Name Not Support Empty Value!");
        }

        if (value == null) {
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
    }

    /**
     * 主动设置sql语句
     * @param sql
     */
    public void setWhereSql(String sql){
        mSettedWhereSql = sql;
    }

    /**
     * 生成对应的where条件语句
     * @return
     */
    public String generate() {

        if(mSettedWhereSql != null){
            return mSettedWhereSql;
        }

        if (mWhereColumnExpressions == null
                || mWhereColumnExpressions.size() == 0) {
            return null;
        }
        List<String> tupples = new LinkedList<>(mWhereColumnExpressions.values());
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < tupples.size(); i++) {
            String exp = tupples.get(i);
            sb.append(exp);
            if (i < (tupples.size() - 1)) {
                sb.append(" AND ");
            }
        }

        return sb.toString();
    }

}
