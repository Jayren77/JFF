package com.jff.base.excel.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * 
 * <p>Title: DefaultExcelBuilderService </p>
 * <p>Description: 默认报表构建服务类</p>
 */
@Service("defaultExcelBuilderService")
public class DefaultExcelBuilderService extends AbsExcelBuilderService {

	@Override
	public String matchRequestExpress(String value, String express) {
		if(StringUtils.isEmpty(express)){
			return value;
		}
		String[] expresses  = express.split(",");
		for (final String exp:expresses) {
			String[] exps = exp.split(":");
			
			if (exps[0].equals(value)
					||( (StringUtils.isNotEmpty(value) && exps[0].contains(value)))) {
				return exps[1];
			}
			// “@”标识注解字段需与相关字段拼接，这里直接返回原值即可
			if(exps[1].contains("@")){
				return exps[1].replaceAll("@", value);
			}
			//增加default逻辑
			if(StringUtils.isNotEmpty(exps[0])
					&&exps[0].equals("default")){
				return exps[1];
			}
		}
		return "";
	}

	@Override
	public String matchResultExpress(String value, String express) {
		return matchRequestExpress(value, express);
	}

}
