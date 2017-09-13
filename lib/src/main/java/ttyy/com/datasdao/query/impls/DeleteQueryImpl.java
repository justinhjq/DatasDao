package ttyy.com.datasdao.query.impls;

import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;

import ttyy.com.datasdao.query.DeleteQuery;

/**
 * Author: Administrator
 * Date  : 2016/08/18 00:32
 * Name  : DeleteQueryImpl
 * Intro :
 * Modification  History:
 * Date          Author        	 Version          Description
 * ----------------------------------------------------------
 * 2016/08/18    Administrator   1.0              1.0
 */
public class DeleteQueryImpl<T> extends DeleteQuery<T> {


    public DeleteQueryImpl(Class<T> tClass, SQLiteDatabase database) {
        super(tClass, database);
    }

    @Override
    protected String createSql() {

        StringBuilder sb = new StringBuilder();
        sb.append("DELETE FROM ");
        sb.append(getTableName());

        // 重设Where语句
        resetWhereExpression();

        if(!TextUtils.isEmpty(str_where)
                && !isWhereClauseFromClass()){
            sb.append(" WHERE ")
                   .append(str_where);

            if(isDebug){
                Log.i("Datas",">>>>>> "+sb.toString()+" <<<<<<<<");
            }

            return sb.toString();
        }else if(isWhereClauseFromClass()){
            sb.append(" WHERE ");
            for(int i = 0 ; i < getColumns().size() ; i++){
                sb.append(getColumns().get(i).getColumnName());
                sb.append(" = ? ");
                if(i < (getColumns().size() - 1)){
                    sb.append(" AND ");
                }
            }
        }



        if(isDebug){
            Log.i("Datas",">>>>>> "+sb.toString()+" <<<<<<<<");
        }

        return sb.toString();
    }

}
