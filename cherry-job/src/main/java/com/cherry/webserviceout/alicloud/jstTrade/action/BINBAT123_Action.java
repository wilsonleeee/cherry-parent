/*	
 * @(#)BINBAT123_Action.java     1.0 @2016-09-26		
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

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryBatchException;
import com.cherry.webserviceout.alicloud.jstTrade.bl.BINBAT123_BL;

/**
*
* 聚石塔接口：天猫退款订单导入Action
* 
*
* @author 
*
* @version  2016-09-26
*/
public class BINBAT123_Action extends BaseAction {

	private static final long serialVersionUID = 7070355030398328620L;
	
	private static Logger logger = LoggerFactory.getLogger(BINBAT123_Action.class.getName());
	
	@Resource(name="binbat123_BL")
	private BINBAT123_BL binbat123_BL;
	
	/** 组织Id */
	private String organizationInfoId;
	
	/** 品牌ID */
	private int brandInfoId;
	
	/** 品牌Code **/
	public String brandCode;
	
	/** 退款单号 **/
	public String refundId;

	public String getOrganizationInfoId() {
		return organizationInfoId;
	}

	public void setOrganizationInfoId(String organizationInfoId) {
		this.organizationInfoId = organizationInfoId;
	}

	public int getBrandInfoId() {
		return brandInfoId;
	}

	public void setBrandInfoId(int brandInfoId) {
		this.brandInfoId = brandInfoId;
	}

	public String getBrandCode() {
		return brandCode;
	}

	public void setBrandCode(String brandCode) {
		this.brandCode = brandCode;
	}

	public String getRefundId() {
		return refundId;
	}

	public void setRefundId(String refundId) {
		this.refundId = refundId;
	}
	
	/**
	 * <p>
	 * 画面初期显示
	 * </p>
	 * 
	 * 
	 * @param 无
	 * @return String
	 * 
	 */
	public String init() throws Exception {
		
		return SUCCESS;
	}

	/**
	 * 
	 * @param 无
	 * @return String
	 * 
	 */
	public String exec() throws Exception {
		logger.info("******************************天猫退款订单导入Batch处理开始***************************");
		// 设置batch处理标志
		int flg = CherryBatchConstants.BATCH_SUCCESS;
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			UserInfo userInfo = (UserInfo) session.get(CherryBatchConstants.SESSION_USERINFO);
			// 所属组织
			map.put(CherryBatchConstants.BRANDINFOID, brandInfoId);
			map.put("brandCode", brandCode);
			map.put(CherryBatchConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
			map.put("refundId", refundId);
			
			flg = binbat123_BL.tran_getRefund(map);
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
				this.addActionMessage("天猫退款订单导入Batch处理正常终了");
				logger.info("******************************天猫退款订单导入Batch处理正常终了***************************");
			} else if (flg == CherryBatchConstants.BATCH_WARNING) {
				this.addActionError("天猫退款订单导入Batch处理警告终了");
				logger.info("******************************天猫退款订单导入Batch处理警告终了***************************");
			} else if (flg == CherryBatchConstants.BATCH_ERROR) {
				this.addActionError("天猫退款订单导入Batch处理异常终了");
				logger.info("******************************天猫退款订单导入Batch处理异常终了***************************");
			}
		}
		return "DOBATCHRESULT";
	}
}
