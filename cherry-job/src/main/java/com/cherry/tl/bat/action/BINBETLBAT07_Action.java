/*
 * @(#)BINBETLBAT07_Action.java     1.0 2013/10/16
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
package com.cherry.tl.bat.action;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryBatchException;
import com.cherry.tl.bat.bl.BINBETLBAT07_BL;

/**
 * 
 * 清除临时文件处理Action
 * 
 * @author WangCT
 * @version 1.0 2013/10/16
 */
public class BINBETLBAT07_Action extends BaseAction {

	private static final long serialVersionUID = -7000025697414226986L;
	
	private static Logger logger = LoggerFactory.getLogger(BINBETLBAT07_Action.class.getName());
	
	/** 清除临时文件处理BL */
	@Resource
	private BINBETLBAT07_BL binBETLBAT07_BL;
	
	/**
	 * 清除临时文件处理
	 * 
	 * @param 无
	 * @return String
	 */
	public String binbetlbat07Exec() throws Exception {
		
		logger.info("******************************清除临时文件处理开始***************************");
		// 设置batch处理标志
		int flg = CherryBatchConstants.BATCH_SUCCESS;
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			// 登陆用户信息
			UserInfo userInfo = (UserInfo) session.get(CherryBatchConstants.SESSION_USERINFO);
			// 组织Id
			map.put(CherryBatchConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
			// 品牌Id
			map.put(CherryBatchConstants.BRANDINFOID, brandInfoId);
			// 组织code
			map.put("orgCode", userInfo.getOrgCode());
			// 品牌code
			map.put(CherryBatchConstants.BRAND_CODE, brandCode);
			// 清除文件导出时存放的临时文件处理
			flg = binBETLBAT07_BL.clearTempFiles(map);
		} catch (CherryBatchException cbx) {
			flg = CherryBatchConstants.BATCH_WARNING;
		} catch (Exception e) {
			flg = CherryBatchConstants.BATCH_ERROR;
		} finally {
			if (flg == CherryBatchConstants.BATCH_SUCCESS) {
				this.addActionMessage("清除临时文件处理正常终了");
				logger.info("******************************清除临时文件处理正常终了***************************");
			} else if (flg == CherryBatchConstants.BATCH_WARNING) {
				this.addActionError("清除临时文件处理警告终了");
				logger.info("******************************清除临时文件处理警告终了***************************");
			} else if (flg == CherryBatchConstants.BATCH_ERROR) {
				this.addActionError("清除临时文件处理异常终了");
				logger.info("******************************清除临时文件处理异常终了***************************");
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
