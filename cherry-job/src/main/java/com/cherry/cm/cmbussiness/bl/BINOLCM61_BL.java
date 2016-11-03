/*		
 * @(#)BINOLCM61_BL.java     1.0 2016/01/30
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
package com.cherry.cm.cmbussiness.bl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.cmbussiness.service.BINOLCM59_Service;
import com.cherry.cm.core.BatchLoggerDTO;
import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryBatchLogger;
import com.cherry.cm.util.CherryBatchUtil;

/**
 * 促销规则下发BL
 * @author hub
 * @version 1.0 2016.01.30
 */
public class BINOLCM61_BL {
	
	private static Logger logger = LoggerFactory.getLogger(BINOLCM61_BL.class.getName());
	
	@Resource(name="binOLCM59_Service")
	private BINOLCM59_Service binOLCM59_Service;

	
	/**
	 * 将促销活动规则下发给终端接口表
	 * 
	 * @param map
	 * @throws Exception
	 */
	public int tran_PromotionRule(Map<String, Object> map) throws Exception {
		map.put(CherryBatchConstants.ORGANIZATIONINFOID, map.get("bin_OrganizationInfoID"));
		map.put(CherryBatchConstants.BRANDINFOID, map.get("brandInfoID"));
		/** BATCH处理标志 */
		int flag = CherryBatchConstants.BATCH_SUCCESS;
		logger.info("***********************促销分类属性下发开始**************************");
		try {
			//  清除促销分类属性表（接口表）
			binOLCM59_Service.delProRuleCateBaseSCS(map);
			int count = 0;
			// 获取促销分类属性信息
			List<Map<String, Object>> cateBaseList = binOLCM59_Service.getProRuleCateBase(map);
			if (null != cateBaseList && !cateBaseList.isEmpty()) {
				// 添加促销规则分类记录（接口表）
				binOLCM59_Service.insertProRuleCateBaseSCS(cateBaseList);
				count = cateBaseList.size();
			}
			logger.info("**************************促销分类属性总件数:" + count);
			binOLCM59_Service.ifManualCommit();
		} catch (Exception e) {
			try {
				// 事务回滚
				binOLCM59_Service.ifManualRollback();;
			} catch (Exception ex) {	
				
			}
			logger.error("下发促销分类属性表发生异常！");
			logger.error(e.getMessage(),e);
		}
		logger.info("***********************促销分类属性下发结束**************************");
		// 失败条数
		int failCount = 0;
		// 总件数
		int count = 0;
		try{
			//清除接口表数据
			//清除规则表
			binOLCM59_Service.delProRuleSCS(map);
			//清除规则类型表
			binOLCM59_Service.delProRuleCateSCS(map);
			//规则List
			List<Map<String, Object>> allList = binOLCM59_Service.getProRule2(map);
			if(null != allList && !allList.isEmpty()){
				count = allList.size();
				// 添加促销规则记录（接口表）
				binOLCM59_Service.insertProRuleSCS2(allList);
			}
			int cateCount = 0;
			// 分类List
			List<Map<String, Object>> cateList = binOLCM59_Service.getProRuleCate(map);
			if (null != cateList && !cateList.isEmpty()) {
				binOLCM59_Service.insertProRuleCateSCS2(cateList);
				cateCount = cateList.size();
			}
			logger.info("**************************规则分类总件数:" + cateCount);
			binOLCM59_Service.ifManualCommit();
		} catch (Exception e) {
			try {
				// 事务回滚
				binOLCM59_Service.ifManualRollback();
			} catch (Exception ex) {	
				
			}
			flag = CherryBatchConstants.BATCH_WARNING;
			failCount = count;
			//记录错误日志
			BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
			batchLoggerDTO.setCode("ESS00035");
			batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
			CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
			cherryBatchLogger.BatchLogger(batchLoggerDTO, e);
		}
		//记录完成日志
		CherryBatchUtil.setBatchResultLog(count - failCount, failCount, this);
		return flag;
	}

}
