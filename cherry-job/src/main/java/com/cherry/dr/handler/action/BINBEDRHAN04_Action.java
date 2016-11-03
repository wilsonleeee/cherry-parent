/*	
 * @(#)BINBEDRHAN04_Action.java     1.0 2013/05/13
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
package com.cherry.dr.handler.action;

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
import com.cherry.dr.handler.bl.BINBEDRHAN04_BL;

/**
 * 积分清零处理Action
 * 
 * @author hub
 * @version 1.0 2013.05.13
 */
public class BINBEDRHAN04_Action extends BaseAction{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1713740569654410890L;
	
	private static Logger logger = LoggerFactory.getLogger(BINBEDRHAN04_Action.class.getName());
	
	/** 积分清零处理BL */
	@Resource
	private BINBEDRHAN04_BL binBEDRHAN04_BL;
	
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
	 * <p>
	 * 积分清零处理batch处理
	 * </p>
	 * 
	 * 
	 * @param 无
	 * @return String
	 * 
	 */
	public String binbedrhan04Exec() throws Exception {
		// 积分清零起始会员ID
		mebIdStart = mebIdStart.trim();
		// 积分清零结束会员ID
		mebIdEnd = mebIdEnd.trim();
		// 数字验证
		if (!CherryChecker.isNullOrEmpty(mebIdStart, true) 
				&& !CherryChecker.isNumeric(mebIdStart)) {
			this.addActionError("积分清零起始会员ID不是数字！");
			return "DOBATCHRESULT";
		} else if (!CherryChecker.isNullOrEmpty(mebIdEnd, true) 
				&& !CherryChecker.isNumeric(mebIdEnd)) {
			this.addActionError("积分清零结束会员ID不是数字！");
			return "DOBATCHRESULT";
		}
		logger.info("******************************积分清零处理开始***************************");
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
			// 积分清零处理batch主处理
			flg = binBEDRHAN04_BL.tran_pointClear(map);
		
		} catch (CherryBatchException cbx) {
			flg = CherryBatchConstants.BATCH_WARNING;
		} catch (Exception e) {
			flg = CherryBatchConstants.BATCH_ERROR;
			e.printStackTrace();
		} finally {
			if (flg == CherryBatchConstants.BATCH_SUCCESS) {
				this.addActionMessage("积分清零处理BATCH处理正常终了");
				logger.info("******************************积分清零处理正常终了***************************");
			} else if (flg == CherryBatchConstants.BATCH_WARNING) {
				this.addActionError("积分清零处理BATCH处理警告终了");
				logger.info("******************************积分清零处理警告终了***************************");
			} else if (flg == CherryBatchConstants.BATCH_ERROR) {
				this.addActionError("积分清零处理BATCH处理异常终了");
				logger.info("******************************积分清零处理异常终了***************************");
			}
		}
		return "DOBATCHRESULT";
	}
	
	/** 品牌Id */
	private String brandInfoId;
	
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
