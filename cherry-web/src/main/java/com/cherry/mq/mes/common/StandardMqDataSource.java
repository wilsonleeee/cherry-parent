/*
 * @(#)MqDataSource.java     1.0 2015-12-29 
 * 
 * Copyright (c) 2010 SHANGHAI BINGKUN DIGITAL TECHNOLOGY CO.,LTD
 * All rights reserved
 * 
 * This software is the confidential and proprietary information of 
 * SHANGHAI BINGKUN.("Confidential Information").  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with SHANGHAI BINGKUN.
 */
package com.cherry.mq.mes.common;

import com.cherry.cm.core.CustomerContextHolder;
import com.cherry.cm.service.BaseService;
import com.cherry.mq.mes.service.MqDataSource_Service;
import com.cherry.mq.mes.service.StandardMq_Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 
 * @ClassName: StandardMqDateSource
 * @Description: TODO(MQ接收设置数据源) 
 * @author menghao
 * @version v1.0.0 2015-12-29 
 *
 */
public class StandardMqDataSource extends BaseService {
	
	/** 数据源信息List **/
	private static List<Map<String,Object>> datasourceList;
	
	@Resource(name="standardMq_Service")
	private StandardMq_Service standardMq_Service;
	
	/**
	 * 
	 * 设定数据源
	 * 
	 * @param brandCode
	 * @return true：设置正常完成；false：未找到相应的数据源
	 * @throws Exception 
	 */
	public boolean setBrandDataSource(String brandCode) throws Exception {
		if (datasourceList == null || datasourceList.isEmpty()) {
			setDatasourceList();
		}
		if(datasourceList != null) {
			for (Map<String, Object> datasourceMap : datasourceList) {
				String datasourceBrandCode = (String)datasourceMap.get("BrandCode");
				if (datasourceBrandCode != null && datasourceBrandCode.equals(brandCode)){
					String dataSourceName = (String)datasourceMap.get("DataSourceName");
					// 将获取的数据源名设定到线程本地变量contextHolder中（新后台品牌数据库）
					CustomerContextHolder.setCustomerDataSourceType(dataSourceName);
					return true;
				}
			}
		}
		
		return false;
	}
	
	private synchronized void setDatasourceList() throws Exception {
		List<Map<String, Object>> resultList = standardMq_Service
				.getBrandDataSourceConfigList();
		if (null == resultList || resultList.isEmpty()) {
			datasourceList = null;
		} else {
			datasourceList = resultList;
		}
	}
}
