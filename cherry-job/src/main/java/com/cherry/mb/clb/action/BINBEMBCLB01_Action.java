/*	
 * @(#)BINBEMBARC01_Action.java     1.0 2014/11/06		
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
package com.cherry.mb.clb.action;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryBatchException;
import com.cherry.mb.clb.bl.BINBEMBCLB01_BL;

/**
 * 会员俱乐部下发(实时)处理Action
 * 
 * @author HUB
 * @version 1.0 2014/11/06
 */
public class BINBEMBCLB01_Action extends BaseAction {
	

	private static final long serialVersionUID = -4214001575461170183L;

	@Resource
	private BINBEMBCLB01_BL binbembclb01_BL;
	
	private static Logger logger = LoggerFactory.getLogger(BINBEMBCLB01_Action.class);
	
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
	public String binbembclb01Exec() throws Exception {
		logger.info("******************************会员俱乐部下发(实时)处理开始***************************");
		// 设置batch处理标志
		int flg = CherryBatchConstants.BATCH_SUCCESS;
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			// 登陆用户信息
			UserInfo userInfo = (UserInfo) session.get(CherryBatchConstants.SESSION_USERINFO);
			// 所属组织
			map.put(CherryBatchConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
			map.put("bin_OrganizationInfoID", userInfo.getBIN_OrganizationInfoID());
			// 品牌信息ID
			map.put("brandInfoId", brandInfoId);
			map.put("brandInfoID", brandInfoId);
			// 品牌Code
			map.put("brandCode", brandCode);
			// 会员俱乐部实时下发
			flg = binbembclb01_BL.tran_batchClub(map);
		} catch (CherryBatchException cbx) {
			flg = CherryBatchConstants.BATCH_WARNING;
			logger.error(cbx.getMessage(),cbx);
		} catch (Exception e) {
			flg = CherryBatchConstants.BATCH_ERROR;
			logger.error(e.getMessage(),e);
		} finally {
			if (flg == CherryBatchConstants.BATCH_SUCCESS) {
				this.addActionMessage("会员俱乐部下发(实时)处理正常终了");
				logger
						.info("******************************会员俱乐部下发(实时)处理正常终了***************************");
			} else if (flg == CherryBatchConstants.BATCH_WARNING) {
				this.addActionError("会员俱乐部下发(实时)处理警告终了");
				logger
						.info("******************************会员俱乐部下发(实时)处理警告终了***************************");
			} else if (flg == CherryBatchConstants.BATCH_ERROR) {
				this.addActionError("会员俱乐部下发(实时)处理异常终了");
				logger
						.info("******************************会员俱乐部下发(实时)处理异常终了***************************");
			}
		}
		return "DOBATCHRESULT";
	}
}
