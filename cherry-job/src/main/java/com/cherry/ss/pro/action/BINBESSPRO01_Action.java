/*
 * @(#)BINBESSPRO01_Action.java     1.0 2011/3/11
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
package com.cherry.ss.pro.action;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryBatchException;
import com.cherry.ss.pro.bl.BINBESSPRO01_BL;

/**
 * 
 * 产品月度库存统计ACTION
 * 
 * 
 * @author ZhangJie
 * @version 1.0 2011.3.11
 */
public class BINBESSPRO01_Action extends BaseAction {

	private static final long serialVersionUID = 2236200225298165811L;
	@Resource
	private BINBESSPRO01_BL binbesspro01bl;

	private static Logger logger = LoggerFactory
			.getLogger(BINBESSPRO01_Action.class.getName());
	
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
	 * 产品月度库存统计处理
	 * </p>
	 * 
	 * 
	 * @param 无
	 * @return String
	 * 
	 */
	public String binbesspro01Exec() throws Exception {
		
		logger.info("******************************产品月度库存统计处理开始***************************");
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
			// 产品月度库存统计
			flg = binbesspro01bl.tran_batchStockHistory(map);
		} catch (CherryBatchException cbx) {
			flg = CherryBatchConstants.BATCH_WARNING;
		} catch (Exception e) {
			flg = CherryBatchConstants.BATCH_ERROR;
		} finally {
			if (flg == CherryBatchConstants.BATCH_SUCCESS) {
				this.addActionMessage("产品月度库存统计BATCH处理正常终了");
				logger.info("******************************产品月度库存统计处理正常终了***************************");
			} else if (flg == CherryBatchConstants.BATCH_WARNING) {
				this.addActionError("产品月度库存统计BATCH处理警告终了");
				logger.info("******************************产品月度库存统计处理警告终了***************************");
			} else if (flg == CherryBatchConstants.BATCH_ERROR) {
				this.addActionError("产品月度库存统计BATCH处理异常终了");
				logger.info("******************************产品月度库存统计处理异常终了***************************");
			}
		}
		return "DOBATCHRESULT";
	}
	
	/**
	 * <p>
	 * 重算产品月度库存统计处理
	 * </p>
	 * 
	 * 
	 * @param 无
	 * @return String
	 * 
	 */
	public String binbesspro01RecalExec() throws Exception {
		
		logger.info("******************************重算产品月度库存统计处理开始***************************");
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
			// 月度库存重算开始时间点
			map.put("controlDateStart", controlDateStart);
			// 月度库存重算结束时间点
			map.put("controlDateEnd", controlDateEnd);
			// 产品月度库存统计
			flg = binbesspro01bl.tran_batchStockHistoryRecal(map);
		} catch (CherryBatchException cbx) {
			flg = CherryBatchConstants.BATCH_WARNING;
		} catch (Exception e) {
			flg = CherryBatchConstants.BATCH_ERROR;
		} finally {
			if (flg == CherryBatchConstants.BATCH_SUCCESS) {
				this.addActionMessage("重算产品月度库存统计BATCH处理正常终了");
				logger.info("******************************重算产品月度库存统计处理正常终了***************************");
			} else if (flg == CherryBatchConstants.BATCH_WARNING) {
				this.addActionError("重算产品月度库存统计BATCH处理警告终了");
				logger.info("******************************重算产品月度库存统计处理警告终了***************************");
			} else if (flg == CherryBatchConstants.BATCH_ERROR) {
				this.addActionError("重算产品月度库存统计BATCH处理异常终了");
				logger.info("******************************重算产品月度库存统计处理异常终了***************************");
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
	
	/** 月度库存重算开始时间点 */
	private String controlDateStart;
	
	/** 月度库存重算结束时间点 */
	private String controlDateEnd;

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

	public String getControlDateStart() {
		return controlDateStart;
	}

	public void setControlDateStart(String controlDateStart) {
		this.controlDateStart = controlDateStart;
	}

	public String getControlDateEnd() {
		return controlDateEnd;
	}

	public void setControlDateEnd(String controlDateEnd) {
		this.controlDateEnd = controlDateEnd;
	}
}
