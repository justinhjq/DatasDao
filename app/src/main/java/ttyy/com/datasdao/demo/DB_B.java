package ttyy.com.datasdao.demo;

import ttyy.com.datasdao.annos.Column;
import ttyy.com.datasdao.annos.Table;

/**
 * Author: Administrator
 * Date  : 2016/12/20 14:17
 * Name  : DB_B
 * Intro : Edit By Administrator
 * Modification  History:
 * Date          Author        	 Version          Description
 * ----------------------------------------------------------
 * 2016/12/20    Administrator   1.0              1.0
 */
@Table("DB_B_TABLE")
public class DB_B {

    @Column(name = "B_TAG")
    String tag = "DB_B";

}
