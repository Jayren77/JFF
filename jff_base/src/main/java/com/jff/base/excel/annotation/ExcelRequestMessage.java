package com.jff.base.excel.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelRequestMessage {
	/**
	 * 
	 * <p>Title: labal </p>
	 * <p>Description: 固定标签 </p>
	 * @author jren
	 * Date: 2017年12月6日 下午4:18:18
	 * @return 标签名
	 */
	String label() default "";
	
	/**
	 * 
	 * <p>Title: expression </p>
	 * <p>Description: eg:  1:是/0:否    格式：AA:BB,CC:DD </p>
	 * <p>增加default逻辑 ：default请放置在表达式最后一位  eg: default:xxx</p>
	 * <p>增加拼接逻辑：@占位注解字段本身值，?占位本类中的关联字段值,格式举例:  -:@-?，"-"拼接标识</p>
	 * @author jren
	 * Date: 2017年12月6日 下午4:10:43
	 * @return 表达式
	 */
	String expression() default "";
	
	/**
	 * 
	 * <p>Title: associatedField </p>
	 * <p>Description: 关联的变量名  ps: 仅支持基本类型+Timestamp</p>
	 * @author jren
	 * Date: 2018年1月3日 下午1:46:13
	 * @return 对应的变量值
	 */
	String associatedField() default "";
	/**
	 * 
	 * <p>Title: formart </p>
	 * <p>Description: 格式  支持Timestamp</p>
	 * @author jren
	 * Date: 2018年1月4日 上午11:49:18
	 * @return String
	 */
	String formart() default "";
}
