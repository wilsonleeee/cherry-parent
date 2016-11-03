/*
 * @(#)BINBEIFCOU01_BL.java     1.0 2010/11/12
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

package com.cherry.ia.cou.bl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.core.BatchLoggerDTO;
import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryBatchException;
import com.cherry.cm.core.CherryBatchLogger;
import com.cherry.ia.cou.service.BINBEIFCOU02_Service;

/**
 * 
 * 柜台下发BL
 * @author Jijw
 * @version 1.0 2013.12.06
 */
public class BINBEIFCOU02_BL {

	private static Logger log = LoggerFactory.getLogger(BINBEIFCOU02_BL.class.getName());
	private static CherryBatchLogger logger = new CherryBatchLogger(BINBEIFCOU02_BL.class);	
	
	/** 柜台下发Service */
	@Resource
	private BINBEIFCOU02_Service binbeifcou02Service;

	/** BATCH处理标志 */
	private int flag = CherryBatchConstants.BATCH_SUCCESS;
	
	/**
	 * 刷新老后台U盘绑定柜台数据batch处理
	 * 
	 * @param 无
	 * 
	 * @return Map
	 * @throws CherryBatchException
	 */
	public int tran_batchCou02(Map<String, Object> map)
			throws CherryBatchException {
		
		try{
			
			getComMap(map);
			
			String branCode = binbeifcou02Service.getBrandCode(map);
			map.put("branCode", branCode);
			
			// Step1:将到期的柜台无效掉
			List<Map<String, Object>> expiredOrgList = binbeifcou02Service.getExpiredOrganization(map);
			map.put("expiredOrgList", expiredOrgList);
			if(null != expiredOrgList && expiredOrgList.size() > 0){
				binbeifcou02Service.updateExpiredOrg(map);
				binbeifcou02Service.updateExpiredCnt(map);
			}
			
			// Step2:删除Counter_SCS表中当前品牌Code的柜台数据，并将新后台有效的柜台写进去
			binbeifcou02Service.clearCounterSCS(map);
			
			List<Map<String, Object>> cntList = binbeifcou02Service.getCounterInfo(map);
			
			if(null != cntList && cntList.size() > 0){
				binbeifcou02Service.insertCounterSCS(cntList);
			}
			
			// 老后台数据源提交
			binbeifcou02Service.ifManualCommit();
			
		}catch (Exception e) {
			log.error(e.getMessage(),e);
			
			BatchLoggerDTO batchLoggerDTO1 = new BatchLoggerDTO();
			batchLoggerDTO1.setCode("EIF02017");
			batchLoggerDTO1.setLevel(CherryBatchConstants.LOGGER_ERROR);
			logger.BatchLogger(batchLoggerDTO1);
			CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
			cherryBatchLogger.BatchLogger(batchLoggerDTO1, e);
			try{
				// 老后台数据源回滚
				binbeifcou02Service.ifManualRollback();
			}catch (Exception e2) {
				log.error(e.getMessage(),e2);
			}
			
			// 失败件数
			flag = CherryBatchConstants.BATCH_ERROR;
		}
		return flag;
	}
	
	private void getComMap(Map<String, Object> baseMap) {

		// 更新程序名
		baseMap.put(CherryBatchConstants.UPDATEPGM, "BINBEIFCOU02");
		// 作成程序名
		baseMap.put(CherryBatchConstants.CREATEPGM, "BINBEIFCOU02");
		// 作成者
		baseMap.put(CherryBatchConstants.CREATEDBY,
				CherryBatchConstants.UPDATE_NAME);
		// 更新者
		baseMap.put(CherryBatchConstants.UPDATEDBY,
				CherryBatchConstants.UPDATE_NAME);
	}

}
