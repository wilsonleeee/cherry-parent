/*	
 * @(#)BINBEMBARC04_Action.java     1.0 2013/11/07		
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
package com.cherry.mb.arc.action;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryBatchException;
import com.cherry.mb.arc.bl.BINBEMBARC04_BL;

/**
 * 老后台会员资料导入新后台处理Action
 * 
 * @author WangCT
 * @version 1.0 2013/11/07
 */
public class BINBEMBARC04_Action extends BaseAction {
	
	private static final long serialVersionUID = 2866162090247930064L;

	private static Logger logger = LoggerFactory.getLogger(BINBEMBARC04_Action.class.getName());
	
	/** 老后台会员资料导入新后台处理BL */
	@Resource
	private BINBEMBARC04_BL binBEMBARC04_BL;
	
	/**
	 * 老后台会员资料导入新后台处理
	 * 
	 * @param 无
	 * @return String
	 * */
	public String binbembarc04Exec() throws Exception {
		
		logger.info("******************************老后台会员资料导入新后台处理开始***************************");
		// 设置batch处理标志
		int flg = CherryBatchConstants.BATCH_SUCCESS;
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			// 登陆用户信息
			UserInfo userInfo = (UserInfo) session
					.get(CherryBatchConstants.SESSION_USERINFO);
			// 组织ID
			map.put(CherryBatchConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
			// 品牌ID
			map.put(CherryBatchConstants.BRANDINFOID, brandInfoId);
			// 组织code
			map.put(CherryBatchConstants.BRAND_CODE, userInfo.getOrganizationInfoCode());
			// 品牌code
			map.put(CherryBatchConstants.BRAND_CODE, brandCode);
			// 老后台会员资料导入新后台处理
			flg = binBEMBARC04_BL.memberInfoHandle(map);
		} catch (CherryBatchException cbx) {
			flg = CherryBatchConstants.BATCH_WARNING;
			logger.error(cbx.getMessage(), cbx);
		} catch (Exception e) {
			flg = CherryBatchConstants.BATCH_ERROR;
			logger.error(e.getMessage(), e);
		} finally {
			if(flg == CherryBatchConstants.BATCH_SUCCESS) {
				this.addActionMessage("老后台会员资料导入新后台处理正常终了");
				logger.info("******************************老后台会员资料导入新后台处理正常终了***************************");
			} else if(flg == CherryBatchConstants.BATCH_WARNING) {
				this.addActionError("老后台会员资料导入新后台处理警告终了");
				logger.info("******************************老后台会员资料导入新后台处理警告终了***************************");
			} else if(flg == CherryBatchConstants.BATCH_ERROR) {
				this.addActionError("老后台会员资料导入新后台处理异常终了");
				logger.info("******************************老后台会员资料导入新后台处理异常终了***************************");
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
