/*	
 * @(#)BINBEMBARC02_Action.java     1.0 2013/04/11	
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
import com.cherry.mb.arc.bl.BINBEMBARC02_BL;

/**
 * 会员积分初始导入处理Action
 * 
 * @author hub
 * @version 1.0 2013/04/11
 */
public class BINBEMBARC02_Action extends BaseAction{

	private static final long serialVersionUID = 4734939269685248207L;
	
	private static Logger logger = LoggerFactory.getLogger(BINBEMBARC02_Action.class.getName());
	
	/** 会员积分初始导入处理BL */
	@Resource
	private BINBEMBARC02_BL binBEMBARC02_BL;
	
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
	 * 会员积分初始导入处理
	 * 
	 * @param 无
	 * @return String
	 * */
	public String binbembarcExec() throws Exception {
		// 会员积分起始ID
		mptIdStart = mptIdStart.trim();
		// 会员积分结束ID
		mptIdEnd = mptIdEnd.trim();
		// 最近几天
		uptDay = uptDay.trim();
		// 数字验证
		if (!CherryChecker.isNullOrEmpty(mptIdStart, true) 
				&& !CherryChecker.isNumeric(mptIdStart)) {
			this.addActionError("会员积分起始ID不是数字！");
			return "DOBATCHRESULT";
		} else if (!CherryChecker.isNullOrEmpty(mptIdEnd, true) 
				&& !CherryChecker.isNumeric(mptIdEnd)) {
			this.addActionError("会员积分结束ID不是数字！");
			return "DOBATCHRESULT";
		} else if (!CherryChecker.isNullOrEmpty(uptDay, true) 
				&& !CherryChecker.isNumeric(uptDay)) {
			this.addActionError("最近的天数不是数字！");
			return "DOBATCHRESULT";
		}
		logger.info("******************************会员积分初始导入处理开始***************************");
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
			if (!"".equals(mptIdStart)) {
				map.put("mptIdStart", mptIdStart);
			}
			if (!"".equals(mptIdEnd)) {
				map.put("mptIdEnd", mptIdEnd);
			}
			if (!"".equals(uptDay)) {
				map.put("uptDay", uptDay);
			} else {
				map.put("uptDay", "0");
			}
			map.put("cgptFlag", cgptFlag);
			if ("1".equals(zflag)) {
				map.put("zflag", "1");
			}
			// 会员积分初始导入处理
			flg = binBEMBARC02_BL.tran_ImptMemPoint(map);
		} catch (CherryBatchException cbx) {
			flg = CherryBatchConstants.BATCH_WARNING;
			logger.error(cbx.getMessage(),cbx);
		} catch (Exception e) {
			flg = CherryBatchConstants.BATCH_ERROR;
			logger.error(e.getMessage(),e);
		} finally {
			if(flg == CherryBatchConstants.BATCH_SUCCESS) {
				this.addActionMessage("会员积分初始导入处理正常终了");
				logger.info("******************************会员积分初始导入处理正常终了***************************");
			} else if(flg == CherryBatchConstants.BATCH_WARNING) {
				this.addActionError("会员积分初始导入处理警告终了");
				logger.info("******************************会员积分初始导入处理警告终了***************************");
			} else if(flg == CherryBatchConstants.BATCH_ERROR) {
				this.addActionError("会员积分初始导入处理异常终了");
				logger.info("******************************会员积分初始导入处理异常终了***************************");
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
	
	/** 会员积分起始ID */
	private String mptIdStart;
	
	/** 会员积分结束ID */
	private String mptIdEnd;
	
	/** 最近几天 */
	private String uptDay;
	
	/** 总积分和可兑换积分别导入 */
	private String cgptFlag;
	
	/** 是否仅导入标记的记录 */
	private String zflag;

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

	public String getMptIdStart() {
		return mptIdStart;
	}

	public void setMptIdStart(String mptIdStart) {
		this.mptIdStart = mptIdStart;
	}

	public String getMptIdEnd() {
		return mptIdEnd;
	}

	public void setMptIdEnd(String mptIdEnd) {
		this.mptIdEnd = mptIdEnd;
	}

	public String getUptDay() {
		return uptDay;
	}

	public void setUptDay(String uptDay) {
		this.uptDay = uptDay;
	}

	public String getCgptFlag() {
		return cgptFlag;
	}

	public void setCgptFlag(String cgptFlag) {
		this.cgptFlag = cgptFlag;
	}

	public String getZflag() {
		return zflag;
	}

	public void setZflag(String zflag) {
		this.zflag = zflag;
	}
}
