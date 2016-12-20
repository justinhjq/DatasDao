package ttyy.com.datasdao.demo;

import ttyy.com.datasdao.annos.Column;

/**
 * Author: Administrator
 * Date  : 2016/12/20 14:17
 * Name  : DB_A
 * Intro : Edit By Administrator
 * Modification  History:
 * Date          Author        	 Version          Description
 * ----------------------------------------------------------
 * 2016/12/20    Administrator   1.0              1.0
 */
public class DB_A {

    @Column(name = "a_tag")
    String tag = "DB_A";

    @Column
    int abc = -1;

}
