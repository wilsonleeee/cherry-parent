/*
 * @(#)BINBETLBAT06_BL.java     1.0 2013/08/20
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
package com.cherry.tl.bat.bl;

import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.core.BatchLoggerDTO;
import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryBatchLogger;
import com.cherry.cm.mongo.MongoDB;
import com.cherry.dr.cmbussiness.util.DateUtil;
import com.cherry.tl.bat.service.BINBETLBAT06_Service;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

/**
 * 
 * 清理MongoDB相关表处理BL
 * 
 * @author WangCT
 * @version 1.0 2013/08/20
 */
public class BINBETLBAT06_BL {
	
	/** 系统配置项 共通 **/
	@Resource
	private BINOLCM14_BL binOLCM14_BL;
	
	/** 清理MongoDB相关表处理Service */
	@Resource
	private BINBETLBAT06_Service binBETLBAT06_Service;
	
	/**
	 * 清理MongoDB相关表处理
	 * 
	 * @param map 传入参数包括组织ID、品牌ID等
	 * @return BATCH处理标志
	 * @throws Exception 
	 */
	public int clearMongoDB(Map<String, Object> map) throws Exception {
		
		CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
		// BATCH处理标志
		int flag = CherryBatchConstants.BATCH_SUCCESS;
		try {
			String organizationInfoID = map.get(CherryBatchConstants.ORGANIZATIONINFOID).toString();
			String brandInfoID = map.get(CherryBatchConstants.BRANDINFOID).toString();
			String months = binOLCM14_BL.getConfigValue("1091", organizationInfoID, brandInfoID);
			if(months != null && !"".equals(months)) {
				// 清理MongoDB相关表的时间点（月数）
				int monthCount = Integer.parseInt(months);
				// 取得业务日期
				String bussinessDate = binBETLBAT06_Service.getBussinessDate(map);
				// 设置清理MongoDB相关表的时间点
				String removeDate = DateUtil.addDateByMonth("yyyy-MM-dd", bussinessDate, -monthCount);
				removeDate = removeDate + " 23:59:59";
				
				DBObject dbObject = new BasicDBObject();
				dbObject.put("BrandCode", map.get(CherryBatchConstants.BRAND_CODE));
				dbObject.put("OccurTime", new BasicDBObject("$lte", removeDate));
				MongoDB.removeAll("MGO_BusinessLog", dbObject);
				MongoDB.removeAll("MGO_MQSendLog", dbObject);
			} else {
				flag = CherryBatchConstants.BATCH_WARNING;
				BatchLoggerDTO batchLoggerDTO1 = new BatchLoggerDTO();
				batchLoggerDTO1.setCode("ETL00011");
				batchLoggerDTO1.setLevel(CherryBatchConstants.LOGGER_ERROR);
				cherryBatchLogger.BatchLogger(batchLoggerDTO1);
				return flag;
			}
		} catch (Exception e) {
			flag = CherryBatchConstants.BATCH_WARNING;
			BatchLoggerDTO batchLoggerDTO1 = new BatchLoggerDTO();
			batchLoggerDTO1.setCode("ETL00009");
			batchLoggerDTO1.setLevel(CherryBatchConstants.LOGGER_ERROR);
			cherryBatchLogger.BatchLogger(batchLoggerDTO1, e);
			return flag;
		}
		
		BatchLoggerDTO batchLoggerDTO1 = new BatchLoggerDTO();
		batchLoggerDTO1.setCode("ETL00010");
		batchLoggerDTO1.setLevel(CherryBatchConstants.LOGGER_INFO);
		cherryBatchLogger.BatchLogger(batchLoggerDTO1);
		
		return flag;
	}

}
