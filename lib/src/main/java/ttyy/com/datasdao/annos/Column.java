package ttyy.com.datasdao.annos;

/**
 * Author: hujinqi
 * Date  : 2016-08-18
 * Description:
 */
public @interface Column {

    boolean ignore() default false;

    boolean nullble() default false;

    boolean unique() default false;

}
