package com.jff.base.excel.service;

import java.util.List;

import com.jff.base.excel.entity.ExcelReqMessage;

import org.apache.poi.ss.usermodel.Workbook;


/**
 * 
 * <p>Title: IExcelBuilderService </p>
 * <p>Description: 报表构建接口 </p>
 */
public interface IExcelBuilderService {

	/**
	 * 
	 * <p>Title: buildWorkbook </p>
	 * <p>Description: 构建报表 </p>
	 * <p>step 1:获取列名</p>
	 * <p>step 2:解析查询条件</p>
	 * <p>step 3:生成报表结构（支持自定义，结构有变无法预测，默认情况下从上到下 表头->查询条件->列名->具体内容）</p>
	 * <p>step 4:填充数据</p>
	 * @author jren
	 * Date: 2018年1月4日 下午5:46:23
	 * @param resultList 做表类结果集
	 * @param reqMessage 前端请求类
	 * @param t 做表类
	 * @return Workbook
	 */
	public <T> Workbook buildWorkbook(List<T> resultList, ExcelReqMessage reqMessage, T t);
}
