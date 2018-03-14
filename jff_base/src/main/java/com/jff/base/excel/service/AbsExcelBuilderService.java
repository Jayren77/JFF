package com.jff.base.excel.service;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.jff.base.excel.annotation.ExcelColumn;
import com.jff.base.excel.annotation.ExcelHeader;
import com.jff.base.excel.annotation.ExcelRequestMessage;
import com.jff.base.excel.entity.ExcelReqMessage;
import com.jff.base.excel.util.ExcelBuildException;
import com.jff.base.excel.util.ExcelConstant;
import com.jff.base.excel.util.ExcelCoverUtils;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;


/**
 * AbsExcelBuilderService 报表生成抽象类
 */
public abstract class AbsExcelBuilderService implements IExcelBuilderService {

    private static final Logger LOG = LoggerFactory.getLogger(AbsExcelBuilderService.class);
	/**
	 * 
	 * <p>Title: matchResultExpress </p>
	 * <p>Description: 匹配结果类表达式 </p>
	 * @author jren
	 * Date: 2017年12月6日 下午4:09:57
	 * @param value 变量值
	 * @param express 表达式
	 * @return 表单中应展示的内容   如 变量值： 1，则匹配后 实际展示为：是
	 */
	public abstract String matchResultExpress(String value, String express);
	/**
	 * 
	 * <p>Title: matchRequestExpress </p>
	 * <p>Description: 匹配查询类表达式 </p>
	 * @author jren
	 * Date: 2018年1月8日 上午11:11:31
	 * @param value
	 * @param express
	 * @return
	 */
	public abstract String matchRequestExpress(String value, String express);


	@Override
	public <T> Workbook buildWorkbook(List<T> resultList,
                                      ExcelReqMessage reqMessage, T t) {
		Workbook workbook = new XSSFWorkbook();
		try {
			// step 1:列名
			List<ExcelColumn> columns = getExcelColumns(t);
			// step 2:解析查询条件
			String reqestStr = buildRequestMessage(reqMessage);
			// step 3:生成框架
			generateExcelStructure(workbook, reqestStr, columns, t);
			// step 4:填充数据
			setDataToExcel(resultList, workbook, columns);
		} catch (Exception e) {
			LOG.error("报表生成失败：", e);
		}

		return workbook;
	}
	/**
	 * 
	 * <p>Title: buildRequestMessage </p>
	 * <p>Description: 生成查询条件 </p>
	 * @author jren
	 * Date: 2017年11月29日 下午5:20:04
	 * @param reqMessage reqMessage
	 * @throws Exception 
	 */
	public String buildRequestMessage(ExcelReqMessage reqMessage) throws Exception {
		//step 1: 创建一个StringBuffer
		StringBuffer sBuffer = new StringBuffer();
		//step 2: 遍历reqMessage fields
		Field[] fields = reqMessage.getClass().getDeclaredFields();
		for(final Field field:fields){
			ExcelRequestMessage excelRequset =field.getAnnotation(ExcelRequestMessage.class);
			field.setAccessible(true);
			if (null == excelRequset) {
				continue;
			}
			//step 2.1:获取字段值
			String value = getValue(field,reqMessage,excelRequset.formart());
			//step 2.2:获取label
			String label = excelRequset.label();
			//step 2.2:获取表达式
			String express = excelRequset.expression();
			//step 2.3:根据表达式组装到StringBuffer中
			String result = matchRequestExpress(value,express);
			LOG.debug("value:"+value+",exp:"+express);
			//step 2.4:查看是否有关联字段，有的话替换result中的?占位符 
			try {
				result = buildResultByAsoFields(result,excelRequset, reqMessage);
			} catch (Exception e) {
				LOG.error("报表查询条件拼接阶段出错，出错字段:"+field.getName(),e);
			}
			//step 2.5:null/"" 值不拼接到查询条件中
			if(StringUtils.isEmpty(result)){
				//字段值为空，跳过拼接
				LOG.debug("查询实体中本字段:"+field.getName()+"为空，不必拼接");
				continue;
			}
			sBuffer.append(label).append(":").append(result).append("；");
		}
		if(StringUtils.isEmpty(sBuffer.toString())){
			return ExcelConstant.NO_CONDITION_REQUEST;
		}
		sBuffer.insert(0, ExcelConstant.REQUEST_PRE);
		
		return sBuffer.toString();
	}
	/**
	 * 
	 * <p>Title: generateExcelStructure </p>
	 * <p>Description: 构建报表结构 </p>
	 * @author jren
	 * Date: 2018年1月5日 下午1:38:58
	 * @param workbook 报表类
	 * @param reqMessage 前端请求内容
	 * @param excelColumn 注解信息
	 * @param t 做表类
	 * @throws ExcelBuildException 异常
	 */
	public <T> void generateExcelStructure(Workbook workbook,
			String reqMessage, List<ExcelColumn> excelColumn, T t)
			throws ExcelBuildException {
		ExcelHeader excelHeader = getExcelHeader(t);
		//表头
		String title = excelHeader.excelName();
		Sheet sheet = workbook.createSheet();
		//设置列宽
		for(int i = 0; i < excelColumn.size();i++){
			sheet.setColumnWidth(i, excelColumn.get(i).width());
		}
		
		//第一行:标题
		Row row1 = sheet.createRow(0);
		row1.setHeight((short)520);
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, excelColumn.size()-1));
		for (int i = 0; i < excelColumn.size(); i++) {
			Cell cellOne = row1.createCell(i);
            cellOne.setCellValue(title);
            cellOne.setCellStyle(styleFactory(workbook, HSSFColor.ROSE.index,
                    "微软雅黑", (short) 18,HSSFColor.BLACK.index));
        }
		// 查询条件
		Row row2 = sheet.createRow(1);
		sheet.addMergedRegion(new CellRangeAddress(1, 1, 0,
				excelColumn.size() - 1));
		row2.setHeight((short) 1000);
		Cell cellTitle = row2.createCell(0);
		cellTitle.setCellStyle(styleFactory(workbook,
				HSSFColor.LIGHT_GREEN.index));
		XSSFRichTextString text = new XSSFRichTextString(reqMessage);
		text.applyFont(0, 4, fontStyle(workbook, "微软雅黑"));
		text.applyFont(4, text.length(), fontStyle(workbook, "宋体"));
		cellTitle.setCellValue(text);
		
		//列名
		if(excelHeader.maxMergeRowNum() == 0 || StringUtils.isEmpty(excelHeader.mergeCellDetail())){
			//单元格不合并，使用默认的方式创建表结构
			defaultBuilidColums(workbook, excelColumn);
		}else{
			buildMergeColums(workbook,excelColumn,excelHeader);
		}
	}
	/**
	 * 
	 * <p>Title: setDataToExcel </p>
	 * <p>Description: 填充数据 </p>
	 * @author jren
	 * Date: 2018年1月5日 下午2:24:24
	 * @param resuList 结果集
	 * @param workbook 报表类
	 * @param excelColumns 注解集合
	 * @throws Exception 异常
	 */
	public <T> void setDataToExcel(List<T> resuList,Workbook workbook,
			List<ExcelColumn> excelColumns) throws Exception{
		int index = workbook.getSheetAt(0).getLastRowNum()+1;
		CellStyle contentStyle = workbook.createCellStyle();
		//若数据内容缺失，生成空表 添加无数据栏
		 if(CollectionUtils.isEmpty(resuList)){
			 int colums = excelColumns.size();
			 	Sheet sheet = workbook.getSheetAt(0);
	        	Row row = sheet.createRow(index);
	        	row.setHeight((short) 400);
				Cell cell = null;
				sheet.addMergedRegion(new CellRangeAddress(index, index, 0, colums-1));
				for (int i = 0; i < colums; i++) {
					cell = row.createCell(i);
					cell.setCellStyle(styleFactory(contentStyle,HSSFColor.RED.index));
					cell.setCellValue("无数据");
				} 
				return;
		 }
		 T t = resuList.get(0);
		 Map<String, Field> fields = getEffectFields(t.getClass()
					.getDeclaredFields());
		// 从列名栏下开始填充数据
		Iterator<T> resultIterator = resuList.iterator();
		while (resultIterator.hasNext()) {
			T result = resultIterator.next();
			Row row = workbook.getSheetAt(0).createRow(index);
			Iterator<ExcelColumn> exIterator = excelColumns.iterator();
			int flag = 0;
			while (exIterator.hasNext()) {
				ExcelColumn excelColumn = exIterator.next();
				Field field = fields.get(excelColumn.label());
				field.setAccessible(true);
				Object object = field.get(result);
				Cell cell = row.createCell(flag);
				cell.setCellStyle(styleFactory(contentStyle,
						HSSFColor.LIGHT_YELLOW.index));
				String value = ExcelCoverUtils.toString(object,
						field.getType(), excelColumn.formart());
				cell.setCellValue(matchResultExpress(value,
						excelColumn.expression()));
				flag++;
			}
			index++;
		}
		
	}
	
	/**
	 * 
	 * <p>Title: buildResultByAsoFields </p>
	 * <p>Description: 当associatedField值不为空时，用关联变量替换占位符，完成查询条件的输出 </p>
	 * @author jren
	 * Date: 2018年1月4日 上午9:49:47
	 * @param result
	 * @param excelRequset
	 * @param reqMessage
	 * @throws Exception
	 * @return result
	 */
	private String buildResultByAsoFields(String result,ExcelRequestMessage excelRequset,ExcelReqMessage reqMessage) throws Exception {
		if(!StringUtils.isEmpty(excelRequset.associatedField())){
			String fieldName = excelRequset.associatedField();
			Field field = reqMessage.getClass().getDeclaredField(fieldName);
			field.setAccessible(true);
			String fieldValue = ExcelCoverUtils.toString(field.get(reqMessage),field.getType(),excelRequset.formart());
			result = result.replace("?", fieldValue).trim();
			//判断拼接两端的内容是否为空
			if(result.equals(ExcelConstant.SEPARATOR)||result.equals("-")){
				return null;
			}
		}
		return result;
	}
	/**
	 * 
	 * <p>Title: getValue </p>
	 * <p>Description: 获取变量值</p>
	 * @author jren
	 * Date: 2017年12月1日 下午2:17:45
	 * @param <T>
	 * @param field 成员变量
	 * @return String 统一返回字符，以便后面判断
	 * @throws Exception Exception
	 */
	private <T> String getValue(Field field,T t,String formart) throws Exception {
		field.setAccessible(true);
		//获取成员变量值
		return ExcelCoverUtils.toString(field.get(t),field.getType(),formart);
	}
	/**
	 * 
	 * <p>Title: getExcelHeader </p>
	 * <p>Description: 获取类表头注解信息</p>
	 * @author jren
	 * Date: 2017年11月28日 下午4:48:43
	 * @param t 做表类
	 * @return ExcelHeader
	 */
	protected <T> ExcelHeader getExcelHeader(T t) {
		return t.getClass().getAnnotation(ExcelHeader.class);
	}
	
	/**
	 * <p>Title: getExcelColumns </p>
	 * <p>Description: 获取报表列名注释 </p>
	 * @author jren
	 * Date: 2017年11月28日 下午4:54:31
	 * @param <T>
	 * @param t 做表类
	 * @return List<ExcelColumn>
	 */
	private <T> List<ExcelColumn> getExcelColumns(T t){
		Field[] fields = t.getClass().getDeclaredFields();
		List<ExcelColumn> excelColumns = new ArrayList<>();
		for (final Field field : fields) {
			ExcelColumn excelColumn = field.getAnnotation(ExcelColumn.class);
			if(null != excelColumn){
				excelColumns.add(excelColumn);
			}
		}
		return excelColumns;
	}
	/**
	 * 
	 * <p>Title: styleFactory </p>
	 * <p>Description: 单元格样式 </p>
	 * @author jren
	 * Date: 2018年1月5日 下午1:54:49
	 * @param workbook 报表类
	 * @param backColor 单元格背景颜色
	 * @param fontName 字体名称
	 * @param fontSize 字体大小
	 * @param fontColor 字体颜色
	 * @return CellStyle 单元格样式
	 */
	private CellStyle styleFactory(Workbook workbook, short backColor,
                                   String fontName, int fontSize, short fontColor) {
        // 生成并设置一个样式
        CellStyle style = workbook.createCellStyle();
        style.setFillPattern(CellStyle.SOLID_FOREGROUND);
        style.setFillForegroundColor(backColor);
        style.setBorderBottom(CellStyle.BORDER_THIN);
        style.setBorderLeft(CellStyle.BORDER_THIN);
        style.setBorderRight(CellStyle.BORDER_THIN);
        style.setBorderTop(CellStyle.BORDER_THIN);
        style.setAlignment(CellStyle.ALIGN_CENTER);
        style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        // 生成一个字体
        Font font = workbook.createFont();
        font.setFontHeightInPoints((short) fontSize);
        font.setBoldweight(Font.BOLDWEIGHT_BOLD);
        font.setFontName(fontName); 
        font.setColor(fontColor);
        // 把字体应用到当前的样式
        style.setFont(font);
        return style;
    }
    /**
     * <p>Title: styleFactory </p>
     * <p>Description: 单元格样式 </p>
     * @param workbook 报表类
     * @param backColor 背景颜色
     * @return 单元格样式
     */
    private CellStyle styleFactory(Workbook workbook, short backColor) {
        // 生成并设置一个样式
        CellStyle style = workbook.createCellStyle();
        style.setFillPattern(CellStyle.SOLID_FOREGROUND);
        style.setFillForegroundColor(backColor);
        style.setBorderBottom(CellStyle.BORDER_THIN);
        style.setBorderLeft(CellStyle.BORDER_THIN);
        style.setBorderRight(CellStyle.BORDER_THIN);
        style.setBorderTop(CellStyle.BORDER_THIN);
        style.setAlignment(CellStyle.ALIGN_LEFT);
        style.setWrapText(true);
        style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        return style;
    }
    /**
     * <p>Title: styleFactory </p>
     * <p>Description: styleFactory </p>
     * @param style 单元格样式
     * @param backColor 背景颜色
     * @return 单元格样式
     */
    private CellStyle styleFactory(CellStyle style, short backColor){
        style.setFillPattern(CellStyle.SOLID_FOREGROUND);
        style.setFillForegroundColor(backColor);
        style.setBorderBottom(CellStyle.BORDER_THIN);
        style.setBorderLeft(CellStyle.BORDER_THIN);
        style.setBorderRight(CellStyle.BORDER_THIN);
        style.setBorderTop(CellStyle.BORDER_THIN);
        style.setAlignment(CellStyle.ALIGN_CENTER);
        style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        return style;
    }
    /**
     * <p>Title: fontStyle </p>
     * <p>Description: 设置格式 </p>
     * @param workbook 报表类
     * @param fontName 字体名称
     * @return 字体样式
     */
    private Font fontStyle(Workbook workbook, String fontName){
        Font font = workbook.createFont();  
        font.setFontHeightInPoints((short) 12);  
        font.setFontName(fontName); 
        font.setBoldweight(Font.BOLDWEIGHT_BOLD); 
        return font;
    }
    
    /**
	 * 
	 * <p>Title: getEffectFields </p>
	 * <p>Description: 获取有效字段，即有ExcelColumn注解标注的 </p>
	 * @author jren
	 * Date: 2018年1月2日 下午3:45:56
	 * @param declaredFields 所有成员变量
	 * @return  List<Field>
	 */
	private Map<String, Field> getEffectFields(Field[] declaredFields) {
		Map<String, Field> fields = new HashMap<String, Field>();
		for (int i = 0; i < declaredFields.length; i++) {
			Field field = declaredFields[i];
			ExcelColumn excelColumn = field.getAnnotation(ExcelColumn.class);
			if(excelColumn!=null){
				fields.put(excelColumn.label(), field);
			}
		}
		return fields;
	}
	/**
	 * 
	 * <p>Title: buildMergeColums </p>
	 * <p>Description: 当遇到需合并单元格的情况时，需要特殊处理 </p>
	 * @author jren
	 * Date: 2017年12月27日 下午2:13:15
	 * @param workbook workbook
	 * @param excelColumns excelColumns
	 * @throws ExcelBuildException
	 */
	private <T> void buildMergeColums(Workbook workbook,
			List<ExcelColumn> excelColumns, ExcelHeader excelHeader) throws ExcelBuildException {
		Sheet sheet = workbook.getSheetAt(0);
		int mergeRows = excelHeader.maxMergeRowNum();
		int startRows = sheet.getLastRowNum()+1;
		
		//得到边界 0:列左边界，1:列右边界  2：行下边界
		int[] border = getStartEndColNum(excelHeader.mergeCellDetail());
		//完成单元格初始化
		Row rows[] = new Row[startRows+mergeRows];
		for (int i = startRows; i < rows.length; i++) {
			rows[i] = sheet.createRow(i);
			rows[i].setHeight((short)400);
		}
		Cell cellTemp[][] = new Cell[startRows+mergeRows][excelColumns.size()];
		for (int i = startRows; i < rows.length; i++) {
			for (int j = 0; j < excelColumns.size(); j++) {
				cellTemp[i][j] = rows[i].createCell(j);
				cellTemp[i][j].setCellStyle(styleFactory(workbook, HSSFColor.SKY_BLUE.index,
	                    "Arial", (short) 12,HSSFColor.VIOLET.index));
			}
		}
		//完成默认部分的单元格赋值
		for (int i = 0; i < border[0]; i++) {
			sheet.addMergedRegion(new CellRangeAddress(startRows, startRows+mergeRows-1, i, i));
			cellTemp[startRows][i].setCellValue(excelColumns.get(i).label());
		}
		for (int i = border[1]+1; i < excelColumns.size(); i++) {
			sheet.addMergedRegion(new CellRangeAddress(startRows, startRows+mergeRows-1, i, i));
			cellTemp[startRows][i].setCellValue(excelColumns.get(i).label());
		}
		//自定义不合并部分赋值
		for(int i = border[0];i<=border[1];i++){
			cellTemp[border[2]+1][i].setCellValue(excelColumns.get(i).label());
		}
		//完成自定义合并部分单元格赋值
		for(String detail:excelHeader.mergeCellDetail()){
			//eg:  航班信息:2,2,21,30
			String[] details = detail.split(":");
			String[] locations = details[1].split(",");
			if(StringUtils.isEmpty(locations)||locations.length!=4){
				throw new ExcelBuildException("mergeCellDetail标签设值不合法，请检查");
			}
			sheet.addMergedRegion(new CellRangeAddress(Integer.valueOf(locations[0]), Integer.valueOf(locations[1]),
					Integer.valueOf(locations[2]), Integer.valueOf(locations[3])));
			cellTemp[Integer.valueOf(locations[0])][Integer.valueOf(locations[2])].setCellValue(details[0]);
		}

	}
	/**
	 * 
	 * <p>Title: getStartEndColNum </p>
	 * <p>Description: 得到自定义部分的边界 </p>
	 * @author jren
	 * Date: 2017年12月28日 下午1:55:10
	 * @param mergeCellDetail 用户配置详情
	 */
	private int[] getStartEndColNum(String[] mergeCellDetail) {
		int[] border = new int[3];
		int startCol = 1000;
		int endCol = 0;
		int endRow = 0;
		for(int i = 0;i<mergeCellDetail.length;i++){
			String[] location = mergeCellDetail[i].split(":")[1].split(",");
			startCol = startCol<Integer.valueOf(location[2])?startCol:Integer.valueOf(location[2]);
			endCol=endCol>Integer.valueOf(location[3])?endCol:Integer.valueOf(location[3]);
			endRow = endRow>Integer.valueOf(location[0])?endRow:Integer.valueOf(location[0]);
		}
		border[0] = startCol;
		border[1] = endCol;
		border[2] = endRow;
		return border;
	}
	/**
	 * 
	 * <p>Title: defaultBuilidColums </p>
	 * <p>Description: 默认情况下的列名填充 </p>
	 * @author jren
	 * Date: 2017年12月27日 下午1:42:06
	 * @param workbook workbook
	 * @param excelColumns excelColumns
	 */
	private <T> void defaultBuilidColums(Workbook workbook,List<ExcelColumn> excelColumns){
		Sheet sheet = workbook.getSheetAt(0);
		Row row3 = sheet.createRow(2);
	     row3.setHeight((short)400);
	     for(int i =0;i<excelColumns.size();i++){
	    	 Cell cell = row3.createCell(i);
	    	 ExcelColumn eColumn = excelColumns.get(i);
	    	 String cellLabel = eColumn.label();
	    	 cell.setCellValue(new XSSFRichTextString(cellLabel));
	    	 cell.setCellStyle(styleFactory(workbook, HSSFColor.SKY_BLUE.index,
	                    "Arial", (short) 12,HSSFColor.VIOLET.index));
	     }
	}
}
