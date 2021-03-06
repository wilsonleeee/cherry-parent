/*
 * @(#)BINBETLBAT04_Action.java     1.0 2011/07/13
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
import com.cherry.tl.bat.bl.BINBETLBAT04_BL;

/**
 * 
 * 更新日结状态Action
 * 
 * 
 * @author TongLin
 * @version 1.0 2012/04/09
 */
public class BINBETLBAT05_Action extends BaseAction {

	private static final long serialVersionUID = 7417369503622552284L;
	
	/** 更新日结状态BL */
	@Resource
	private BINBETLBAT04_BL binBETLBAT04_BL;

	private static Logger logger = LoggerFactory
			.getLogger(BINBETLBAT05_Action.class.getName());

	/**
	 * 
	 * 更新日结状态
	 * 
	 * @param 无
	 * @return String
	 * 
	 */
	public String binbetlbat05Exec() throws Exception {
		
		logger.info("******************************更新日结状态处理开始***************************");
		// 设置batch处理标志
		int flg = CherryBatchConstants.BATCH_SUCCESS;
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			// 登陆用户信息
			UserInfo userInfo = (UserInfo) session
					.get(CherryBatchConstants.SESSION_USERINFO);
			// 所属组织
			map.put(CherryBatchConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
			// 品牌Id
			map.put(CherryBatchConstants.BRANDINFOID, brandInfoId);
			// 品牌code
			map.put(CherryBatchConstants.BRAND_CODE, brandCode);
			// 更新日结状态
			flg = binBETLBAT04_BL.updateCloseFlag(map);
		} catch (CherryBatchException cbx) {
			flg = CherryBatchConstants.BATCH_WARNING;
		} catch (Exception e) {
			flg = CherryBatchConstants.BATCH_ERROR;
		} finally {
			if (flg == CherryBatchConstants.BATCH_SUCCESS) {
				this.addActionMessage("更新日结状态BATCH处理正常终了");
				logger.info("******************************更新日结状态处理正常终了***************************");
			} else if (flg == CherryBatchConstants.BATCH_WARNING) {
				this.addActionError("更新日结状态BATCH处理警告终了");
				logger.info("******************************更新日结状态处理警告终了***************************");
			} else if (flg == CherryBatchConstants.BATCH_ERROR) {
				this.addActionError("更新日结状态BATCH处理异常终了");
				logger.info("******************************更新日结状态处理异常终了***************************");
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

}
