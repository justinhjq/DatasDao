package ttyy.com.datasdao.annos;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Author: hjq
 * Date  : 2016/12/19 20:59
 * Name  : Column
 * Intro : Edit By hjq
 * Modification  History:
 * Date          Author        	 Version          Description
 * ----------------------------------------------------------
 * 2016/12/19    hjq   1.0              1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Column {

    String name() default "";

    boolean ignore() default false;

    boolean nullble() default true;

    boolean unique() default false;

}
