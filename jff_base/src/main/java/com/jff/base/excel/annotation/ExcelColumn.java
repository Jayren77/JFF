package com.jff.base.excel.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelColumn {
	/**
	 * 
	 * <p>Title: label </p>
	 * <p>Description: 列名 </p>
	 * @author jren
	 * Date: 2017年12月6日 下午4:19:07
	 * @return
	 */
	String label() default "";
	/**
	 * 
	 * <p>Title: expression </p>
	 * <p>Description: eg:  1:是/0:否    格式：AA:BB,CC:DD </p>
	 * @author jren
	 * Date: 2017年12月6日 下午4:10:43
	 * @return 表达式
	 */
	String expression() default "";
	/**
	 * 
	 * <p>Title: formart </p>
	 * <p>Description: 格式  支持Timestamp</p>
	 * @author jren
	 * Date: 2018年1月4日 上午11:49:18
	 * @return String
	 */
	String formart() default "";
	/**
	 * 
	 * <p>Title: width </p>
	 * <p>Description: 列宽 </p>
	 * @author jren
	 * Date: 2017年12月6日 下午4:19:53
	 * @return int
	 */
	int width() default 3254;
	
}
