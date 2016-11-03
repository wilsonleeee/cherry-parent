/*
 * @(#)PromotionLogic.java v1.0 2014-9-9
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
package com.cherry.webservice.promotion.bl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryBatchException;
import com.cherry.ss.prm.bl.BINBESSPRM03_BL;
import com.cherry.ss.prm.bl.BINBESSPRM07_BL;

/**
 * 促销品实时下发
 * 
 * @author JiJW
 * @version 1.0 2014-9-9
 */
public class PromotionLogic {
	private static final Logger logger = LoggerFactory.getLogger(PromotionLogic.class);

	@Resource(name = "binbessprm07_BL")
	private BINBESSPRM07_BL binbessprm07_BL;

	public Map<String, Object> publishPromotion(Map<String, Object> params) {
		Map<String, Object> resMap = new HashMap<String, Object>();
		Map<String, Object> map = new HashMap<String, Object>();
		// 设置batch处理标志
		int flg = CherryBatchConstants.BATCH_SUCCESS;
		// 所属组织
		map.put("organizationInfoId", params.get("BIN_OrganizationInfoID"));
		// 品牌信息ID
		map.put("brandInfoId", params.get("BIN_BrandInfoID"));
		// 品牌Code
		map.put("brandCode", params.get("brandCode"));
		// 操作者
		map.put("EmployeeId", params.get("EmployeeId"));
		// 是否发送MQ
		map.put("sendMQFlag", true);
		
		try {
			logger.info("*********促销品实时Batch下发处理开始*********");
			resMap = binbessprm07_BL.tran_batchPromPrt(map);
			int flag = (Integer) resMap.get("flag");
			if(flag == CherryBatchConstants.BATCH_WARNING || flag == CherryBatchConstants.BATCH_ERROR){
				resMap.put("ERRORCODE", -1);
				resMap.put("ERRORMSG", resMap.get("errorMsg"));
			}

		} catch (CherryBatchException cbx) {
//			flg = CherryBatchConstants.BATCH_WARNING;
			resMap.put("ERRORCODE", -1);
			resMap.put("ERRORMSG", cbx.getMessage());
		} catch (Exception e) {
//			flg = CherryBatchConstants.BATCH_ERROR;
			resMap.put("ERRORCODE", -1);
			resMap.put("ERRORMSG", e.getMessage());
		}
		logger.info("*********促销品实时Batch下发处理结束*********");
		return resMap;
	}
}
