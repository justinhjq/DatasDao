package ttyy.com.datasdao;

import ttyy.com.datasdao.cmds.DeleteQuery;
import ttyy.com.datasdao.cmds.FindQuery;
import ttyy.com.datasdao.cmds.InsertQuery;
import ttyy.com.datasdao.cmds.UpdateQuery;
import ttyy.com.datasdao.cmds.impls.DeleteQueryImpl;
import ttyy.com.datasdao.cmds.impls.FindQueryImpl;
import ttyy.com.datasdao.cmds.impls.InsertQueryImpl;
import ttyy.com.datasdao.cmds.impls.UpdateQueryImpl;

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

    private SimpleSqliteDataBase mDatabase;

    protected Core(SimpleSqliteDataBase database){
        this.mDatabase = database;
    }

    protected SimpleSqliteDataBase getDatabase(){
        return mDatabase;
    }

    public <T> FindQuery<T> findQuery(Class<T> tableModule){
        FindQuery query = new FindQueryImpl<T>(tableModule, mDatabase.getDatabase());
        query.setIsDebug(mDatabase.getConfig().isDebug());
        return query;
    }

    public <T> InsertQuery<T> insertQuery(Class<T> tableModule){
        InsertQuery query = new InsertQueryImpl<T>(tableModule, mDatabase.getDatabase());
        query.setIsDebug(mDatabase.getConfig().isDebug());
        return query;
    }

    public <T> DeleteQuery<T> deleteQuery(Class<T> tableModule){
        DeleteQuery query = new DeleteQueryImpl<T>(tableModule, mDatabase.getDatabase());
        query.setIsDebug(mDatabase.getConfig().isDebug());
        return query;
    }

    public <T> UpdateQuery<T> updateQuery(Class<T> tableModule){
        UpdateQuery query = new UpdateQueryImpl(tableModule, mDatabase.getDatabase());
        query.setIsDebug(mDatabase.getConfig().isDebug());
        return query;
    }

}
