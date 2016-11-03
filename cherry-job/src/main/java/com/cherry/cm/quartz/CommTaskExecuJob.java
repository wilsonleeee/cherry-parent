/*	
 * @(#)CommTaskExecuJob.java     1.0 2013/02/28	
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
package com.cherry.cm.quartz;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.cmbussiness.bl.BINOLCM06_BL;
import com.cherry.cm.core.CustomerContextHolder;
import com.cherry.ct.smg.interfaces.BINBECTSMG01_IF;

/**
 * 沟通任务执行Job
 * 
 * @author WangCT
 * @version 1.0 2013/02/28	
 */
public class CommTaskExecuJob {
	
	private static Logger logger = LoggerFactory.getLogger(CommTaskExecuJob.class.getName());
	
	/** 数据源信息List **/
	private static List<Map<String,Object>> brandDataSourceList;
	
	@Resource
	private BINOLCM06_BL binOLCM06_BL;
	
	@Resource
	private BINBECTSMG01_IF binBECTSMG01_BL;
	
	/**
	 * 
     * 取得所有品牌数据源信息List
     * 
     */
	private synchronized void selBrandDataSourceList() {
		Map<String, Object> map = new HashMap<String, Object>();
		// 取得所有品牌数据源信息List
		brandDataSourceList = binOLCM06_BL.getConfBrandInfoList(map);
	}
	
	/**
	 * 设定数据源
	 * 
	 * @param map 
	 */
	public void setBrandDataSource(String brandCode) {
		if(brandDataSourceList == null || brandDataSourceList.isEmpty()) {
			this.selBrandDataSourceList();
		}
		for (int i = 0; i < brandDataSourceList.size(); i++) {
			Map<String, Object> brandDataSourceMap = brandDataSourceList.get(i);
			String _brandCode = (String)brandDataSourceMap.get("brandCode");
			if (_brandCode != null && _brandCode.equals(brandCode)) {
				String dataSourceName = (String)brandDataSourceMap.get("dataSourceName");
				// 设定数据源
				CustomerContextHolder.setCustomerDataSourceType(dataSourceName);
				break;
			}
		}
	}
	
	/**
	 * 执行沟通任务处理
	 * 
	 * @param brandCode 品牌代码
	 * @param schedulesId 任务ID
	 */
	public void execuCommTask(String brandCode, String schedulesId) {
		
		try {
			// 设定数据源
			this.setBrandDataSource(brandCode);
			// 运行单个调度计划
			binBECTSMG01_BL.runSchedules(schedulesId);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			// 清除新后台品牌数据源
			CustomerContextHolder.clearCustomerDataSourceType();
		}
	}

}
