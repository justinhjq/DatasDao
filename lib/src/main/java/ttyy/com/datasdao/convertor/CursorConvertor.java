package ttyy.com.datasdao.convertor;

import android.database.Cursor;

import ttyy.com.datasdao.modules.ModuleTable;

/**
 * Author: Administrator
 * Date  : 2016/12/20 10:55
 * Name  : CursorConvertor
 * Intro : Edit By Administrator
 * Modification  History:
 * Date          Author        	 Version          Description
 * ----------------------------------------------------------
 * 2016/12/20    Administrator   1.0              1.0
 */
public interface CursorConvertor {

    <T> T convertFromCursor(ModuleTable<T> table, Cursor cursor);

    CursorConvertor DEFAULT = new DefaultCursorConvertor();
}
