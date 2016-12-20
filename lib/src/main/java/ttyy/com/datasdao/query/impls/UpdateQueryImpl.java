package ttyy.com.datasdao.query.impls;

import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;

import ttyy.com.datasdao.query.UpdateQuery;

/**
 * Author: hujinqi
 * Date  : 2016-08-18
 * Description:
 */
public class UpdateQueryImpl<T> extends UpdateQuery<T> {

    public UpdateQueryImpl(Class<T> tClass, SQLiteDatabase database) {
        super(tClass, database);
    }

    @Override
    protected String createSql() {

        StringBuilder sb = new StringBuilder();
        sb.append("UPDATE ").append(getTableName());

        if(!TextUtils.isEmpty(str_set)){
            sb.append(" SET ").append(str_set);
        }

        if(!TextUtils.isEmpty(str_where)){
            sb.append(" WHERE ").append(str_where);
        }

        if(isDebug){
            Log.i("Datas",">>>>>> "+sb.toString()+" <<<<<<<<");
        }

        return sb.toString();
    }

}
