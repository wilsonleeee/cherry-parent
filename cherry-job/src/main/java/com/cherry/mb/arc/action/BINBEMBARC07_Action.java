/*	
 * @(#)BINBEMBARC07_Action.java     1.0 2013/12/19	
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
import com.cherry.cm.core.CherryChecker;
import com.cherry.mb.arc.bl.BINBEMBARC07_BL;

/**
 * 珀莱雅会员等级调整Action
 * @author hub
 * @version 1.0 2013/12/19
 *
 */
public class BINBEMBARC07_Action extends BaseAction{

	private static final long serialVersionUID = -8203746841713531666L;
	
	private static Logger logger = LoggerFactory.getLogger(BINBEMBARC07_Action.class.getName());
	
	@Resource(name="binBEMBARC07_BL")
	private BINBEMBARC07_BL binBEMBARC07_BL;
	
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
	
	public String binbembarc07Exec() throws Exception {
		// 积分清零起始会员ID
		mebIdStart = mebIdStart.trim();
		// 积分清零结束会员ID
		mebIdEnd = mebIdEnd.trim();
		// 数字验证
		if (!CherryChecker.isNullOrEmpty(mebIdStart, true) 
				&& !CherryChecker.isNumeric(mebIdStart)) {
			this.addActionError("会员ID不是数字！");
			return "DOBATCHRESULT";
		} else if (!CherryChecker.isNullOrEmpty(mebIdEnd, true) 
				&& !CherryChecker.isNumeric(mebIdEnd)) {
			this.addActionError("会员ID不是数字！");
			return "DOBATCHRESULT";
		}
		logger.info("******************************珀莱雅会员等级调整开始***************************");
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
			map.put("orgCode", userInfo.getOrgCode());
			// 品牌code
			map.put(CherryBatchConstants.BRAND_CODE, brandCode);
			if (!"".equals(mebIdStart)) {
				map.put("mebIdStart", mebIdStart);
			}
			if (!"".equals(mebIdEnd)) {
				map.put("mebIdEnd", mebIdEnd);
			}
			map.put("levelCalcFlag", levelCalcFlag);
			// 珀莱雅会员等级调整
			flg = binBEMBARC07_BL.tran_levelCalc(map);
		} catch (CherryBatchException cbx) {
			flg = CherryBatchConstants.BATCH_WARNING;
			logger.error(cbx.getMessage(), cbx);
		} catch (Exception e) {
			flg = CherryBatchConstants.BATCH_ERROR;
			logger.error(e.getMessage(), e);
		} finally {
			if(flg == CherryBatchConstants.BATCH_SUCCESS) {
				this.addActionMessage("珀莱雅会员等级调整正常终了");
				logger.info("******************************珀莱雅会员等级调整正常终了***************************");
			} else if(flg == CherryBatchConstants.BATCH_WARNING) {
				this.addActionError("珀莱雅会员等级调整警告终了");
				logger.info("******************************珀莱雅会员等级调整警告终了***************************");
			} else if(flg == CherryBatchConstants.BATCH_ERROR) {
				this.addActionError("珀莱雅会员等级调整异常终了");
				logger.info("******************************珀莱雅会员等级调整异常终了***************************");
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
	
	/** 是否需要进行履历已存在判断 */
	private String levelCalcFlag;

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

	public String getLevelCalcFlag() {
		return levelCalcFlag;
	}

	public void setLevelCalcFlag(String levelCalcFlag) {
		this.levelCalcFlag = levelCalcFlag;
	}
}
