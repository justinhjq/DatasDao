package ttyy.com.datasdao.cmds.impls;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import ttyy.com.datasdao.cmds.InsertQuery;

/**
 * Author: hujinqi
 * Date  : 2016-08-18
 * Description:
 */
public class InsertQueryImpl<T> extends InsertQuery<T> {

    public InsertQueryImpl(Class<T> tClass, SQLiteDatabase database) {
        super(tClass, database);
    }

    @Override
    protected String createSql() {

        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO ").append(mTableName)
                .append(" VALUES ( ");
        for(int i = 0 ; i < mColumns.length ; i++){
            sb.append(" ? ");
            if(i < ( mColumns.length - 1 )){
                sb.append(",");
            }
        }
        sb.append(" )");

        if(isDebug){
            Log.i("Datas",">>>>>> "+sb.toString()+" <<<<<<<<");
        }

        return sb.toString();
    }

}
