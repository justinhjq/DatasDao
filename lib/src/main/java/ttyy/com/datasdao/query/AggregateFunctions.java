package ttyy.com.datasdao.query;

/**
 * Author: hjq
 * Date  : 2016/12/19 21:03
 * Name  : AggregateFunctions
 * Intro : 聚合函数
 * Modification  History:
 * Date          Author        	 Version          Description
 * ----------------------------------------------------------
 * 2016/08/18    hjq   1.0              1.0
 */
public interface AggregateFunctions<T> {

    /**
     * cout()函数
     * @return
     */
    int count();

    /**
     * 求平均数
     * @param column
     * @return
     */
    double average(String column);

    /**
     * 求最小值
     * @param column
     * @return
     */
    double min(String column);

    /**
     * 求最大值
     * @param column
     * @return
     */
    double max(String column);

    /**
     * 从offset到limitNumber
     * @param offset
     * @param limitNumber
     * @return
     */
    FindQuery<T> limit(int offset, int limitNumber);

    /**
     * 排序
     * @param column
     * @param type
     */
    FindQuery<T> orderBy(String column, String type);

}
