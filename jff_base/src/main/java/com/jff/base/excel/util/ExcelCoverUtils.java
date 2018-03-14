package com.jff.base.excel.util;

import java.sql.Timestamp;

/**
 * 
 * <p>Title: ExcelCoverUtils </p>
 * <p>Description: 反射转换工具类 </p>
 */
public class ExcelCoverUtils {
	
	private ExcelCoverUtils(){
		
	}
	
	/**
	 * 
	 * <p>Title: toString </p>
	 * <p>Description: 依类型转换String </p>
	 * @author jren
	 * Date: 2017年12月1日 下午4:04:03
	 * @param object 需转换的对象
	 * @param type 对象类型
	 * @return 转换后的类型
	 */
	public static String toString(Object object, Class<?> type, String formart) {
//		if(null == object){
//			return "";
//		}
//		StringBuffer buffer = new StringBuffer();
//		switch (type.getName()) {
//		case "int":
//			return object.toString();
//		case "java.lang.String":
//			return (String)object;
//		case "java.sql.Timestamp":
//			Timestamp timestamp = (Timestamp)object;
//			return RiDateUtils.dateToString(timestamp.getTime(), formart);
//		case "[Ljava.sql.Timestamp;":
//			Timestamp[] timestamps = (Timestamp[])object;
//			for(int i = 0;i<timestamps.length;i++){
////				buffer.append(RiDateUtils.dateToString(timestamps[i].getTime(), formart));
////				if(i != timestamps.length -1){
////					buffer.append(ExcelConstant.SEPARATOR);
////				}
//			}
//			return handlerSeparator(buffer).toString();
//		case "[Ljava.lang.String;":
//			String[] strs = (String[] )object;
//			for(int i = 0;i<strs.length;i++){
//				buffer.append(strs[i]);
//				if(i != strs.length -1){
//					buffer.append(ExcelConstant.SEPARATOR);
//				}
//			}
//			return handlerSeparator(buffer).toString();
//		case "[I":
//			int[] ints = (int[])object;
//			for(int i = 0;i<ints.length;i++){
//				buffer.append(ints[i]);
//				if(i != ints.length -1){
//					buffer.append(ExcelConstant.SEPARATOR);
//				}
//			}
//			return handlerSeparator(buffer).toString();
//		default:
//
			return null;
//		}
		
	}
	
	/**
	 * 
	 * <p>Title: handlerSeparator </p>
	 * <p>Description: 处理一下字符串结尾多余的符号 </p>
	 * @author jren
	 * Date: 2018年1月9日 上午11:27:47
	 * @param value
	 * @return
	 */
	private static StringBuffer handlerSeparator(StringBuffer value){
//		//以“/”结尾时
//		if(RiCheckUtils.isNotEmpty(value.toString())){
//			value.insert(0, "(");
//			value.append(")");
//		}
		return value;
	}
	
}
