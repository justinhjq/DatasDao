package ttyy.com.datasdao.cmds.impls;

import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;

import ttyy.com.datasdao.cmds.DeleteQuery;

/**
 * Author: hujinqi
 * Date  : 2016-08-18
 * Description:
 */
public class DeleteQueryImpl<T> extends DeleteQuery<T> {


    public DeleteQueryImpl(Class<T> tClass, SQLiteDatabase database) {
        super(tClass, database);
    }

    @Override
    protected String createSql() {

        StringBuilder sb = new StringBuilder();
        sb.append("DELETE FROM ");
        sb.append(mTableName);

        if(!TextUtils.isEmpty(str_where)){
            sb.append(" WHERE ")
                   .append(str_where);

            return sb.toString();
        }

        if(isWhereClauseFromClass()){
            sb.append(" WHERE ");
            for(int i = 0 ; i < mColumns.length ; i++){
                sb.append(mColumns[i]);
                sb.append(" = ? ");
                if(i < (mColumns.length - 1)){
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
