package ttyy.com.datasdao.cmds;

/**
 * Author: hujinqi
 * Date  : 2016-08-18
 * Description:
 */
public interface AggregateFunctions<T> {

    /**
     * cout()函数
     * @param column
     * @return
     */
    int count(String column);

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
