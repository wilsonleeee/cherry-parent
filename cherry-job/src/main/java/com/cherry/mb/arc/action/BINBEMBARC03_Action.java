/*	
 * @(#)BINBEMBARC03_Action.java     1.0 2013/04/11	
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
import com.cherry.mb.arc.bl.BINBEMBARC03_BL;

/**
 * 积分明细初始导入处理Action
 * 
 * @author hub
 * @version 1.0 2013/04/11
 */
public class BINBEMBARC03_Action extends BaseAction{

	private static final long serialVersionUID = -2711835628013033708L;
	
	private static Logger logger = LoggerFactory.getLogger(BINBEMBARC03_Action.class.getName());
	
	/** 积分明细初始导入处理BL */
	@Resource
	private BINBEMBARC03_BL binBEMBARC03_BL;
	
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
	 * 积分明细初始导入处理
	 * 
	 * @param 无
	 * @return String
	 * */
	public String binbembarcExec() throws Exception {
		// 积分明细起始ID
		dtlIdStart = dtlIdStart.trim();
		// 积分明细结束ID
		dtlIdEnd = dtlIdEnd.trim();
		// 数字验证
		if (!CherryChecker.isNullOrEmpty(dtlIdStart, true) 
				&& !CherryChecker.isNumeric(dtlIdStart)) {
			this.addActionError("积分明细起始ID不是数字！");
			return "DOBATCHRESULT";
		} else if (!CherryChecker.isNullOrEmpty(dtlIdEnd, true) 
				&& !CherryChecker.isNumeric(dtlIdEnd)) {
			this.addActionError("积分明细结束ID不是数字！");
			return "DOBATCHRESULT";
		}
		logger.info("******************************积分明细初始导入处理开始***************************");
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
			if (!"".equals(dtlIdStart)) {
				map.put("dtlIdStart", dtlIdStart);
			}
			if (!"".equals(dtlIdEnd)) {
				map.put("dtlIdEnd", dtlIdEnd);
			}
			if ("1".equals(zflag)) {
				map.put("zflag", "1");
			}
			// 积分明细初始导入处理
			flg = binBEMBARC03_BL.tran_ImptPointDetail(map);
		} catch (CherryBatchException cbx) {
			flg = CherryBatchConstants.BATCH_WARNING;
			logger.error(cbx.getMessage(),cbx);
		} catch (Exception e) {
			flg = CherryBatchConstants.BATCH_ERROR;
			logger.error(e.getMessage(),e);
		} finally {
			if(flg == CherryBatchConstants.BATCH_SUCCESS) {
				this.addActionMessage("积分明细初始导入处理正常终了");
				logger.info("******************************积分明细初始导入处理正常终了***************************");
			} else if(flg == CherryBatchConstants.BATCH_WARNING) {
				this.addActionError("积分明细初始导入处理警告终了");
				logger.info("******************************积分明细初始导入处理警告终了***************************");
			} else if(flg == CherryBatchConstants.BATCH_ERROR) {
				this.addActionError("积分明细初始导入处理异常终了");
				logger.info("******************************积分明细初始导入处理异常终了***************************");
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
	
	/** 积分明细起始ID */
	private String dtlIdStart;
	
	/** 积分明细结束ID */
	private String dtlIdEnd;
	
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

	public String getDtlIdStart() {
		return dtlIdStart;
	}

	public void setDtlIdStart(String dtlIdStart) {
		this.dtlIdStart = dtlIdStart;
	}

	public String getDtlIdEnd() {
		return dtlIdEnd;
	}

	public void setDtlIdEnd(String dtlIdEnd) {
		this.dtlIdEnd = dtlIdEnd;
	}

	public String getZflag() {
		return zflag;
	}

	public void setZflag(String zflag) {
		this.zflag = zflag;
	}
}
