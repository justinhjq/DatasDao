package ttyy.com.datasdao.annos;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import ttyy.com.datasdao.constants.ConflictAction;

/**
 * author: admin
 * date: 2017/12/22
 * version: 0
 * mail: secret
 * desc: Constraint
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Constraint {

    ConflictAction conflict() default ConflictAction.ABORT;

    String[] value();

}
