/*	
 * @(#)BINBECTSMG09_Action.java     1.0 2016/05/02		
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
package com.cherry.ct.smg.action;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryChecker;
import com.cherry.ct.smg.interfaces.BINBECTSMG09_IF;

/**
 * 短信签名管理Action
 * 
 * @author hub
 * @version 1.0 2016/05/02
 */
public class BINBECTSMG09_Action extends BaseAction{

	private static final long serialVersionUID = -398101364306839375L;
	
	private static Logger logger = LoggerFactory.getLogger(BINBECTSMG09_Action.class.getName());
	
	@Resource
	private BINBECTSMG09_IF binBECTSMG09_BL;
	
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
		try {
			// 取得品牌的短信签名
			signName = binBECTSMG09_BL.getBrandSignName(brandCode);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return SUCCESS;
	}
	
	/**
	 * 短信签名管理处理
	 * 
	 * @param 无
	 * @return String
	 * */
	public String exec() throws Exception {
		
		// 必须输入短信签名
		if (CherryChecker.isNullOrEmpty(signName, true) ) {
			this.addActionError("短信签名不能为空！");
			return "DOBATCHRESULT";
		}
		logger.info("******************************短信签名管理处理开始***************************");
		// 设置batch处理标志
		int flg = CherryBatchConstants.BATCH_SUCCESS;
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			// 品牌code
			map.put(CherryBatchConstants.BRAND_CODE, brandCode);
			// 短信签名
			map.put("signName", signName.trim());
			// 更新短信签名
			flg = binBECTSMG09_BL.tran_upSignName(map);
		} catch (Exception e) {
			flg = CherryBatchConstants.BATCH_ERROR;
			logger.error(e.getMessage(),e);
		} finally {
			if(flg == CherryBatchConstants.BATCH_SUCCESS) {
				this.addActionMessage("短信签名管理处理正常终了");
				logger.info("******************************短信签名管理处理正常终了***************************");
			} else if(flg == CherryBatchConstants.BATCH_WARNING) {
				this.addActionError("短信签名管理处理警告终了");
				logger.info("******************************短信签名管理处理警告终了***************************");
			} else if(flg == CherryBatchConstants.BATCH_ERROR) {
				this.addActionError("短信签名管理处理异常终了");
				logger.info("******************************短信签名管理处理异常终了***************************");
			}
		}
		return "DOBATCHRESULT";
	}
	
	/** 品牌Id */
	private String brandInfoId;
	
	/** 组织Id */
	private String organizationInfoId;
	
	/** 品牌code */
	private String brandCode;
	
	/** 短信签名 */
	private String signName;

	public String getBrandInfoId() {
		return brandInfoId;
	}

	public void setBrandInfoId(String brandInfoId) {
		this.brandInfoId = brandInfoId;
	}

	public String getOrganizationInfoId() {
		return organizationInfoId;
	}

	public void setOrganizationInfoId(String organizationInfoId) {
		this.organizationInfoId = organizationInfoId;
	}

	public String getBrandCode() {
		return brandCode;
	}

	public void setBrandCode(String brandCode) {
		this.brandCode = brandCode;
	}

	public String getSignName() {
		return signName;
	}

	public void setSignName(String signName) {
		this.signName = signName;
	}
}
