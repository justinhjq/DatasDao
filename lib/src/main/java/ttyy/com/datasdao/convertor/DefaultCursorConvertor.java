package ttyy.com.datasdao.convertor;

import android.database.Cursor;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import ttyy.com.datasdao.modules.ModuleColumn;
import ttyy.com.datasdao.modules.ModuleTable;

/**
 * Author: Administrator
 * Date  : 2016/12/20 10:57
 * Name  : DefaultCursorConvertor
 * Intro : Edit By Administrator
 * Modification  History:
 * Date          Author        	 Version          Description
 * ----------------------------------------------------------
 * 2016/12/20    Administrator   1.0              1.0
 */
public class DefaultCursorConvertor implements CursorConvertor {

    @Override
    public <T> T convertFromCursor(ModuleTable<T> table, Cursor cursor) {
        try {
            Constructor<T> constructor = table.getModuleClassEmptyConstructor();
            Object object = constructor.newInstance();
            for (ModuleColumn tmp : table.getColumns()) {
                tmp.setPropertyType(object, cursor);
            }
            return (T) object;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }
}
