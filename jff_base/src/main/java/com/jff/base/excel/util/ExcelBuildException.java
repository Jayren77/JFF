package com.jff.base.excel.util;



/**
 * 
 * <p>Title: ExcelBuildException </p>
 * <p>Description: 报表构建 </p>
 */
public class ExcelBuildException extends Exception {

	/**
	 * @Fields serialVersionUID: serialVersionUID
	 */
	private static final long serialVersionUID = 3565652390301236476L;

	/**
	 * Description: Constructor
	 * @param message 异常消息
	 */
	public ExcelBuildException(String message) {
		super(message);
	}

	/**
	 * Description: Constructor
	 * @param code 异常编码
	 * @param message 异常消息
	 */
	public ExcelBuildException(String code, String message) {
		super(code + ": " + message);
	}
	
	/**
	 * Description: Constructor
	 * @param message 异常消息
	 * @param cause 异常堆栈
	 */
	public ExcelBuildException(String message, Throwable cause) {
		super(message, cause);
	}
	
	/**
	 * Description: Constructor
	 * @param code 异常编码
	 * @param message 异常消息
	 * @param cause 异常堆栈
	 */
	public ExcelBuildException(String code, String message, Throwable cause) {
		super(code + ": " + message, cause);
	}

	
	
}
