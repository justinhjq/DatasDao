package ttyy.com.datasdao;

import android.database.sqlite.SQLiteDatabase;

import ttyy.com.datasdao.query.AlterQuery;
import ttyy.com.datasdao.query.DeleteQuery;
import ttyy.com.datasdao.query.FindQuery;
import ttyy.com.datasdao.query.InsertQuery;
import ttyy.com.datasdao.query.UpdateQuery;
import ttyy.com.datasdao.query.impls.DeleteQueryImpl;
import ttyy.com.datasdao.query.impls.FindQueryImpl;
import ttyy.com.datasdao.query.impls.InsertQueryImpl;
import ttyy.com.datasdao.query.impls.UpdateQueryImpl;

/**
 * Author: hjq
 * Date  : 2016/12/19 21:07
 * Name  : Core
 * Intro : Edit By hjq
 * Modification  History:
 * Date          Author        	 Version          Description
 * ----------------------------------------------------------
 * 2016/12/19    hjq   1.0              1.0
 */
public class Core {

    private SimpleSqliteDao mSqliteDao;

    private SQLiteDatabase mSqliteDatabase;

    protected Core(SimpleSqliteDao database){
        this.mSqliteDao = database;
    }

    protected Core(SQLiteDatabase database){
        mSqliteDatabase = database;
    }

    protected SimpleSqliteDao getDatabaseDao(){
        return mSqliteDao;
    }

    private SQLiteDatabase getSqliteDatabase(){
        return mSqliteDatabase == null ? mSqliteDao.getDatabase() : mSqliteDatabase;
    }

    public <T> FindQuery<T> findQuery(Class<T> tableModule){
        FindQuery query = new FindQueryImpl<T>(tableModule, getSqliteDatabase());
        query.setIsDebug(getDatabaseDao().getConfig().isDebug());
        return query;
    }

    public <T> InsertQuery<T> insertQuery(Class<T> tableModule){
        InsertQuery query = new InsertQueryImpl<T>(tableModule, getSqliteDatabase());
        query.setIsDebug(getDatabaseDao().getConfig().isDebug());
        return query;
    }

    public <T> DeleteQuery<T> deleteQuery(Class<T> tableModule){
        DeleteQuery query = new DeleteQueryImpl<T>(tableModule, getSqliteDatabase());
        query.setIsDebug(getDatabaseDao().getConfig().isDebug());
        return query;
    }

    public <T> UpdateQuery<T> updateQuery(Class<T> tableModule){
        UpdateQuery query = new UpdateQueryImpl(tableModule, getSqliteDatabase());
        query.setIsDebug(getDatabaseDao().getConfig().isDebug());
        return query;
    }

    public <T> AlterQuery<T> alterQuery(Class<T> tableModule){
        AlterQuery query = new AlterQuery(tableModule, getSqliteDatabase());
        if(getDatabaseDao() != null)
            query.setIsDebug(getDatabaseDao().getConfig().isDebug());
        else
            query.setIsDebug(true);
        return query;
    }

}
