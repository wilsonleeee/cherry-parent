/*	
 * @(#)BINBAT111_Action.java     1.0 @2015-9-16		
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
package com.cherry.webserviceout.alicloud.jstTrade.action;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryBatchException;
import com.cherry.cm.core.CherryChecker;
import com.cherry.webserviceout.alicloud.jstTrade.bl.BINBAT125_BL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * 天猫退款转MQ（销售）
 * 
 *
 * @author fxb
 *
 * @version  2016-11-04
 */
public class BINBAT125_Action extends BaseAction {

	private static final long serialVersionUID = -2353591036927907349L;

	private static Logger logger = LoggerFactory.getLogger(BINBAT125_Action.class.getName());
	
	@Resource(name="binbat125_BL")
	private BINBAT125_BL binbat125_BL;

	/** 组织Id */
	private String organizationInfoId;

	/** 品牌ID */
	private int brandInfoId;

	/** 品牌Code **/
	public String brandCode;

	/** 起始订单ID **/
	public String startId;

	/** 结束订单ID **/
	public String endId;

	public String getStartId() {
		return startId;
	}

	public void setStartId(String startId) {
		this.startId = startId;
	}

	public String getEndId() {
		return endId;
	}

	public void setEndId(String endId) {
		this.endId = endId;
	}

	public String getBrandCode() {
		return brandCode;
	}

	public void setBrandCode(String brandCode) {
		this.brandCode = brandCode;
	}

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
	 * <p>
	 * 画面初期显示
	 * </p>
	 *
	 *
	 * @param
	 * @return String
	 *
	 */
	public String init() throws Exception {

		return SUCCESS;
	}

	/**
	 *
	 * @param
	 * @return String
	 *
	 */
	public String binbat125Exec() throws Exception {
		// 起始ID
		startId = startId.trim();
		// 结束ID
		endId = endId.trim();
		// 数字验证
		if (!CherryChecker.isNullOrEmpty(startId, true)
				&& !CherryChecker.isNumeric(startId)) {
			this.addActionError("起始ID不是数字！");
			return "DOBATCHRESULT";
		} else if (!CherryChecker.isNullOrEmpty(endId, true)
				&& !CherryChecker.isNumeric(endId)) {
			this.addActionError("结束ID不是数字！");
			return "DOBATCHRESULT";
		}

		logger.info("******************************天猫退款转销售MQBatch处理开始***************************");
		// 设置batch处理标志
		int flg = CherryBatchConstants.BATCH_SUCCESS;
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			UserInfo userInfo = (UserInfo) session.get(CherryBatchConstants.SESSION_USERINFO);
			// 所属组织
			map.put(CherryBatchConstants.BRANDINFOID, brandInfoId);
			map.put("brandCode", brandCode);
			map.put(CherryBatchConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
			if (!"".equals(startId)) {
				map.put("startId", startId);
			}
			if (!"".equals(endId)) {
				map.put("endId", endId);
			}

			flg = binbat125_BL.tran_batchBat125(map);
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
				this.addActionMessage("天猫退款转MQBatch处理正常终了");
				logger.info("******************************天猫退款转MQBatchBatch处理正常终了***************************");
			} else if (flg == CherryBatchConstants.BATCH_WARNING) {
				this.addActionError("天猫退款转MQBatch处理警告终了");
				logger.info("******************************天猫退款转MQBatch处理警告终了***************************");
			} else if (flg == CherryBatchConstants.BATCH_ERROR) {
				this.addActionError("天猫退款转MQBatch处理异常终了");
				logger.info("******************************天猫退款转MQBatch处理异常终了***************************");
			}
		}
		return "DOBATCHRESULT";
	}


}