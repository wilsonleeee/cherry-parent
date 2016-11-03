/*	
 * @(#)BINBEMBTIF07_Action.java     1.0 2015/06/24		
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
package com.cherry.mb.tif.action;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryBatchException;
import com.cherry.mb.tif.bl.BINBEMBTIF07_BL;

/**
 * 重新回调天猫积分处理Action
 * 
 * @author hub
 * @version 1.0 2015/06/24
 */
public class BINBEMBTIF07_Action extends BaseAction{

	private static final long serialVersionUID = -9134836120753704746L;
	
	private static Logger logger = LoggerFactory.getLogger(BINBEMBTIF07_Action.class.getName());
	
	/** 重新回调天猫积分处理BL */
	@Resource
	private BINBEMBTIF07_BL binBEMBTIF07_BL;
	
	/**
	 * <p>
	 * 重新回调天猫积分处理
	 * </p>
	 * 
	 * 
	 * @param 无
	 * @return String
	 * 
	 */
	public String binbembarcExec() throws Exception {

		logger.info("******************************重新回调天猫积分处理开始***************************");
		// 设置batch处理标志
		int flg = CherryBatchConstants.BATCH_SUCCESS;
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			// 登陆用户信息
			UserInfo userInfo = (UserInfo) session
					.get(CherryBatchConstants.SESSION_USERINFO);
			// 所属组织
			map.put(CherryBatchConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
			// 组织代码
			map.put("orgCode", userInfo.getOrgCode());
			// 品牌Id
			map.put(CherryBatchConstants.BRANDINFOID, brandInfoId);
			// 品牌code
			map.put(CherryBatchConstants.BRAND_CODE, brandCode);
			// 重新回调天猫积分处理
			flg = binBEMBTIF07_BL.tran_recallTmall(map);
		} catch (CherryBatchException cbx) {
			flg = CherryBatchConstants.BATCH_WARNING;
			logger.error(cbx.getMessage(),cbx);
		} catch (Exception e) {
			flg = CherryBatchConstants.BATCH_ERROR;
			logger.error(e.getMessage(),e);
		} finally {
			if (flg == CherryBatchConstants.BATCH_SUCCESS) {
				this.addActionMessage("重新回调天猫积分处理正常终了");
				logger.info("******************************重新回调天猫积分处理正常终了***************************");
			} else if (flg == CherryBatchConstants.BATCH_WARNING) {
				this.addActionError("重新回调天猫积分处理警告终了");
				logger.info("******************************重新回调天猫积分处理警告终了***************************");
			} else if (flg == CherryBatchConstants.BATCH_ERROR) {
				this.addActionError("重新回调天猫积分处理异常终了");
				logger.info("******************************重新回调天猫积分处理异常终了***************************");
			}
		}
		return "DOBATCHRESULT";
	}
	
	/** 品牌Id */
	private String brandInfoId;
	
	/** 品牌code */
	private String brandCode;

	public String getBrandInfoId() {
		return brandInfoId;
	}

	public void setBrandInfoId(String brandInfoId) {
		this.brandInfoId = brandInfoId;
	}

	public String getBrandCode() {
		return brandCode;
	}

	public void setBrandCode(String brandCode) {
		this.brandCode = brandCode;
	}

}
