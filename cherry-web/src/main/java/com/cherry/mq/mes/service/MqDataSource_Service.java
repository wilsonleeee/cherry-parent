/*  
 * @(#)MqDataSource_Service.java     1.0 2015/12/29      
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
package com.cherry.mq.mes.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 
 * @ClassName: MqDataSource_Service 
 * @Description: TODO(MQ接收数据源相关) 
 * @author menghao
 * @version v1.0.0 2015-12-29 
 *
 */
@SuppressWarnings("unchecked")
public class MqDataSource_Service extends BaseService{
	
	/**
	 * 从配置数据库查询品牌数据库对应表获取所有品牌的数据源
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getBrandDataSourceConfigList (){
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "MqDataSource.getBrandDataSourceConfigList");
		return baseConfServiceImpl.getList(paramMap);
	}
	
}
