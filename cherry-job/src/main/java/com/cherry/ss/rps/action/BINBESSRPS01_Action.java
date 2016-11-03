/*
 * @(#)BINBESSRPS01_Action.java     1.0 2012/11/08
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
package com.cherry.ss.rps.action;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryBatchException;
import com.cherry.ss.rps.bl.BINBESSRPS01_BL;

/**
 * 销售月度统计Action
 * 
 * @author WangCT
 * @version 1.0 2012/11/08
 */
public class BINBESSRPS01_Action extends BaseAction {

	private static final long serialVersionUID = -7882577478206981247L;
	
	private static Logger logger = LoggerFactory.getLogger(BINBESSRPS01_Action.class.getName());
	
	/** 销售月度统计BL **/
	@Resource
	private BINBESSRPS01_BL binBESSRPS01_BL;
	
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
	 * 销售月度统计处理
	 * </p>
	 * 
	 * 
	 * @param 无
	 * @return String
	 * 
	 */
	public String saleCountBatch() throws Exception {
		
		logger.info("******************************销售月度统计处理开始***************************");
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
			// 销售月度统计处理
			flg = binBESSRPS01_BL.tran_saleCountBatch(map);
		} catch (CherryBatchException cbx) {
			flg = CherryBatchConstants.BATCH_WARNING;
		} catch (Exception e) {
			flg = CherryBatchConstants.BATCH_ERROR;
		} finally {
			if (flg == CherryBatchConstants.BATCH_SUCCESS) {
				this.addActionMessage("销售月度统计处理正常终了");
				logger.info("******************************销售月度统计处理正常终了***************************");
			} else if (flg == CherryBatchConstants.BATCH_WARNING) {
				this.addActionError("销售月度统计处理警告终了");
				logger.info("******************************销售月度统计处理警告终了***************************");
			} else if (flg == CherryBatchConstants.BATCH_ERROR) {
				this.addActionError("销售月度统计处理异常终了");
				logger.info("******************************销售月度统计处理异常终了***************************");
			}
		}
		return "DOBATCHRESULT";
	}
	
	/**
	 * <p>
	 * 重算销售月度统计处理
	 * </p>
	 * 
	 * 
	 * @param 无
	 * @return String
	 * 
	 */
	public String relSaleCountBatch() throws Exception {
		
		logger.info("******************************重算销售月度统计处理开始***************************");
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
			// 销售月度统计重算开始时间点
			map.put("saleCountStart", saleCountStart);
			// 销售月度统计重算结束时间点
			map.put("saleCountEnd", saleCountEnd);
			// 重算销售月度统计处理
			flg = binBESSRPS01_BL.tran_relSaleCountBatch(map);
		} catch (CherryBatchException cbx) {
			flg = CherryBatchConstants.BATCH_WARNING;
		} catch (Exception e) {
			flg = CherryBatchConstants.BATCH_ERROR;
		} finally {
			if (flg == CherryBatchConstants.BATCH_SUCCESS) {
				this.addActionMessage("重算销售月度统计处理正常终了");
				logger.info("******************************重算销售月度统计处理正常终了***************************");
			} else if (flg == CherryBatchConstants.BATCH_WARNING) {
				this.addActionError("重算销售月度统计处理警告终了");
				logger.info("******************************重算销售月度统计处理警告终了***************************");
			} else if (flg == CherryBatchConstants.BATCH_ERROR) {
				this.addActionError("重算销售月度统计处理异常终了");
				logger.info("******************************重算销售月度统计处理异常终了***************************");
			}
		}
		return "DOBATCHRESULT";
	}
	
	/**
	 * <p>
	 * 补录销售月度统计处理
	 * </p>
	 * 
	 * 
	 * @param 无
	 * @return String
	 * 
	 */
	public String saleCountPatchBatch() throws Exception {
		
		logger.info("******************************补录销售月度统计处理开始***************************");
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
			// 补录销售月度统计处理
			flg = binBESSRPS01_BL.tran_saleCountPatchBatch(map);
		} catch (CherryBatchException cbx) {
			flg = CherryBatchConstants.BATCH_WARNING;
		} catch (Exception e) {
			flg = CherryBatchConstants.BATCH_ERROR;
		} finally {
			if (flg == CherryBatchConstants.BATCH_SUCCESS) {
				this.addActionMessage("补录销售月度统计处理正常终了");
				logger.info("******************************补录销售月度统计处理正常终了***************************");
			} else if (flg == CherryBatchConstants.BATCH_WARNING) {
				this.addActionError("补录销售月度统计处理警告终了");
				logger.info("******************************补录销售月度统计处理警告终了***************************");
			} else if (flg == CherryBatchConstants.BATCH_ERROR) {
				this.addActionError("补录销售月度统计处理异常终了");
				logger.info("******************************补录销售月度统计处理异常终了***************************");
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
	
	/** 销售月度统计重算开始时间点 */
	private String saleCountStart;
	
	/** 销售月度统计重算结束时间点 */
	private String saleCountEnd;

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

	public String getSaleCountStart() {
		return saleCountStart;
	}

	public void setSaleCountStart(String saleCountStart) {
		this.saleCountStart = saleCountStart;
	}

	public String getSaleCountEnd() {
		return saleCountEnd;
	}

	public void setSaleCountEnd(String saleCountEnd) {
		this.saleCountEnd = saleCountEnd;
	}

}
