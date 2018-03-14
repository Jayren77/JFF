package com.jff.base.excel.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelHeader {
	/**
	 * 
	 * <p>Title: excelName </p>
	 * <p>Description: 写入表名 </p>
	 * @author jren
	 * Date: 2017年12月6日 下午4:17:50
	 * @return excelName
	 */
	String excelName() default "";
	
	/**
	 * 
	 * <p>Title: fileName </p>
	 * <p>Description: Excel文件名 </p>
	 * @author jren
	 * Date: 2017年12月12日 下午6:05:01
	 * @return fileName
	 */
	String fileName() default "";
	
	/**
	 * 
	 * <p>Title: sheetName </p>
	 * <p>Description: sheet名 </p>
	 * @author jren
	 * Date: 2017年12月26日 下午3:42:01
	 * @return
	 */
	String sheetName() default "";
	/**
	 * 
	 * <p>Title: maxMergeColsNumber </p>
	 * <p>Description: 最大合并行数</p>
	 * @author jren
	 * Date: 2017年12月26日 下午4:04:27
	 * @return 本报表最大支持的列名级数
	 */
	int maxMergeRowNum() default 0;
	
	/**
	 * 
	 * <p>Title: mergeCellDetail </p>
	 * <p>Description: 合并部分的单元格信息详情，包括：值，合并的范围 </p>
	 * 格式：{"值:1,1,2,4","值:1,1,2,4"}  eg: {"xxxx:1,1,4,6"}</br>
	 * 包括：(ps:行数列数的起始下标为0)</br>
	 * 1. 值</br>
	 * 2. 合并的范围（起始行数，终止行数，起始列数，终止列数）</br>
	 * @author jren
	 * Date: 2017年12月27日 下午5:22:14
	 * @return String[]
	 */
	String[] mergeCellDetail() default {};

}
