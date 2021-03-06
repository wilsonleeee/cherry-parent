/*	
 * @(#)BINBEMBTIF01_Action.java     1.0 2015/06/24		
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
import com.cherry.cm.core.CherryChecker;
import com.cherry.mb.tif.bl.BINBEMBTIF01_BL;

/**
 * 同步天猫会员处理Action
 * 
 * @author hub
 * @version 1.0 2015/06/24
 */
public class BINBEMBTIF01_Action extends BaseAction {
	
	private static final long serialVersionUID = 6270353895795954686L;

	private static Logger logger = LoggerFactory.getLogger(BINBEMBTIF01_Action.class.getName());
	
	/** 同步天猫会员处理BL */
	@Resource
	private BINBEMBTIF01_BL binBEMBTIF01_BL;
	
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
	 * 同步天猫会员处理
	 * 
	 * @param 无
	 * @return String
	 * */
	public String binbembarcExec() throws Exception {
		
		// 积分清零起始会员ID
		mebIdStart = mebIdStart.trim();
		// 积分清零结束会员ID
		mebIdEnd = mebIdEnd.trim();
		// 数字验证
		if (!CherryChecker.isNullOrEmpty(mebIdStart, true) 
				&& !CherryChecker.isNumeric(mebIdStart)) {
			this.addActionError("起始会员ID不是数字！");
			return "DOBATCHRESULT";
		} else if (!CherryChecker.isNullOrEmpty(mebIdEnd, true) 
				&& !CherryChecker.isNumeric(mebIdEnd)) {
			this.addActionError("结束会员ID不是数字！");
			return "DOBATCHRESULT";
		}
		
		logger.info("******************************同步天猫会员处理开始***************************");
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
			if (!"".equals(mebIdStart)) {
				map.put("mebIdStart", mebIdStart);
			}
			if (!"".equals(mebIdEnd)) {
				map.put("mebIdEnd", mebIdEnd);
			}
			flg = binBEMBTIF01_BL.tran_MemSync(map);
		} catch (CherryBatchException cbx) {
			flg = CherryBatchConstants.BATCH_WARNING;
		} catch (Exception e) {
			flg = CherryBatchConstants.BATCH_ERROR;
		} finally {
			if(flg == CherryBatchConstants.BATCH_SUCCESS) {
				this.addActionMessage("同步天猫会员处理正常终了");
				logger.info("******************************同步天猫会员处理正常终了***************************");
			} else if(flg == CherryBatchConstants.BATCH_WARNING) {
				this.addActionError("同步天猫会员处理警告终了");
				logger.info("******************************同步天猫会员处理警告终了***************************");
			} else if(flg == CherryBatchConstants.BATCH_ERROR) {
				this.addActionError("同步天猫会员处理异常终了");
				logger.info("******************************同步天猫会员处理异常终了***************************");
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
	
	/** 积分清零起始会员ID */
	private String mebIdStart;
	
	/** 积分清零结束会员ID */
	private String mebIdEnd;

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

	public String getMebIdStart() {
		return mebIdStart;
	}

	public void setMebIdStart(String mebIdStart) {
		this.mebIdStart = mebIdStart;
	}

	public String getMebIdEnd() {
		return mebIdEnd;
	}

	public void setMebIdEnd(String mebIdEnd) {
		this.mebIdEnd = mebIdEnd;
	}
}
