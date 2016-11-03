/*	
 * @(#)BINBEKDPRO01_Action.java     1.0 @2015-4-29		
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
package com.cherry.webserviceout.kingdee.product.action;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.BatchLoggerDTO;
import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryBatchException;
import com.cherry.cm.core.CherryBatchLogger;
import com.cherry.ia.pro.bl.BINBEIFPRO04_BL;
import com.cherry.webserviceout.kingdee.product.bl.BINBEKDPRO01_BL;
import com.cherry.webserviceout.kingdee.product.function.BINBEKDPRO01_FN;

/**
 *
 * Kingdee接口：产品导入Action
 *
 * @author jijw
 *
 * @version  2015-4-29
 */
public class BINBEKDPRO01_Action extends BaseAction {

	private static final long serialVersionUID = -5818624354784540939L;
	
	private static Logger logger = LoggerFactory.getLogger(BINBEKDPRO01_Action.class.getName());
	
	private static CherryBatchLogger cblogger = new CherryBatchLogger(BINBEKDPRO01_FN.class);
	
	@Resource(name="binbekdpro01_BL")
	private BINBEKDPRO01_BL binbekdpro01_BL;
	
	/** 产品实时下发 */
	@Resource(name="binbeifpro04_BL")
	private BINBEIFPRO04_BL binbeifpro04_BL;
	
	/** 组织Id */
	private String organizationInfoId;
	
	/** 品牌ID */
	private int brandInfoId;

	public int getBrandInfoId() {
		return brandInfoId;
	}

	public void setBrandInfoId(int brandInfoId) {
		this.brandInfoId = brandInfoId;
	}
	
	public String getOrganizationInfoId() {
		return organizationInfoId;
	}

	public void setOrganizationInfoId(String organizationInfoId) {
		this.organizationInfoId = organizationInfoId;
	}

	/**
	 * 
	 * @param 无
	 * @return String
	 * 
	 */
	public String binbekdpro01Exec() throws Exception {
		logger.info("******************************Kingdee产品导入处理开始***************************");
		// 设置batch处理标志
		int flg = CherryBatchConstants.BATCH_SUCCESS;
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			UserInfo userInfo = (UserInfo) session
					.get(CherryBatchConstants.SESSION_USERINFO);
			// 所属组织
			map.put(CherryBatchConstants.BRANDINFOID, brandInfoId);
			map.put(CherryBatchConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
			
			// Job运行履历表的运行方式
			map.put("RunType", "MT");
			
			flg = binbekdpro01_BL.tran_batchkdpro01(map);
			
			/*
			// 调用产品实时下发
			try{
				// 是否实时发送MQ 1:是、 0或空：否
				map.put("IsSendMQ", "1");
				
				binbeifpro04_BL.tran_batchProducts(map);
			}catch(Exception e){
				BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
				batchLoggerDTO.setCode("EKD00016");
				batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
				cblogger.BatchLogger(batchLoggerDTO, e);
			}
			*/
			
		} catch (CherryBatchException cbx) {
			flg = CherryBatchConstants.BATCH_WARNING;
			logger.info("=============WARN MSG================");
			logger.info(cbx.getMessage(),cbx);
			logger.info("=====================================");
		} catch (Exception e) {
			flg = CherryBatchConstants.BATCH_ERROR;
			logger.error("=============ERROR MSG===============");
			logger.error(e.getMessage(),e);
			logger.error("=====================================");
		} finally {
			if (flg == CherryBatchConstants.BATCH_SUCCESS) {
				this.addActionMessage("Kingdee产品导入处理正常终了");
				logger.info("******************************Kingdee产品导入处理正常终了***************************");
			} else if (flg == CherryBatchConstants.BATCH_WARNING) {
				this.addActionError("Kingdee产品导入处理警告终了");
				logger.info("******************************Kingdee产品导入处理警告终了***************************");
			} else if (flg == CherryBatchConstants.BATCH_ERROR) {
				this.addActionError("Kingdee产品导入处理异常终了");
				logger.info("******************************Kingdee产品导入处理异常终了***************************");
			}
		}
		return "DOBATCHRESULT";
	}
}
