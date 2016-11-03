/*	
 * @(#)BINBESSPRM08_Action.java     1.0 2016/06/27	
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
package com.cherry.ss.prm.action;

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
import com.cherry.ss.prm.bl.BINBESSPRM08_BL;

/**
 * 家化优惠券推送Action
 * 
 * @author hub
 * @version 1.0 2015/06/24
 */
public class BINBESSPRM08_Action extends BaseAction{

	private static final long serialVersionUID = -2198629332308322820L;
	
	private static Logger logger = LoggerFactory.getLogger(BINBESSPRM08_Action.class.getName());
	
	/** 家化优惠券推送处理BL */
	@Resource(name="binBESSPRM08_BL")
	private BINBESSPRM08_BL binBESSPRM08_BL;
	
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
	 * 优惠券发放清单传输接口
	 * 
	 * @param 无
	 * @return String
	 * */
	public String sendCoupon() throws Exception {
		
		logger.info("******************************优惠券发放清单传输接口开始***************************");
		// 设置batch处理标志
		int flg = CherryBatchConstants.BATCH_SUCCESS;
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			// 推送选择的活动
			if (!CherryChecker.isNullOrEmpty(ruleCodes, true)) {
				map.put("ruleCodes", ruleCodes.trim());
			}
			// 登陆用户信息
			UserInfo userInfo = (UserInfo) session
					.get(CherryBatchConstants.SESSION_USERINFO);
			// 所属组织
			map.put(CherryBatchConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
			// 品牌Id
			map.put(CherryBatchConstants.BRANDINFOID, brandInfoId);
			// 品牌code
			map.put(CherryBatchConstants.BRAND_CODE, brandCode);
			flg = binBESSPRM08_BL.tran_sendCoupon(map);
		} catch (CherryBatchException cbx) {
			flg = CherryBatchConstants.BATCH_WARNING;
		} catch (Exception e) {
			flg = CherryBatchConstants.BATCH_ERROR;
			logger.error(e.getMessage(),e);
		} finally {
			if(flg == CherryBatchConstants.BATCH_SUCCESS) {
				this.addActionMessage("优惠券发放清单传输接口正常终了");
				logger.info("******************************优惠券发放清单传输接口正常终了***************************");
			} else if(flg == CherryBatchConstants.BATCH_WARNING) {
				this.addActionError("优惠券发放清单传输接口警告终了");
				logger.info("******************************优惠券发放清单传输接口警告终了***************************");
			} else if(flg == CherryBatchConstants.BATCH_ERROR) {
				this.addActionError("优惠券发放清单传输接口异常终了");
				logger.info("******************************优惠券发放清单传输接口异常终了***************************");
			}
		}
		return "DOBATCHRESULT";
	}
	
	/**
	 * 优惠券状态推送接口
	 * 
	 * @param 无
	 * @return String
	 * */
	public String sendUpdateCoupon() throws Exception {
		
		logger.info("******************************优惠券状态推送接口接口开始***************************");
		// 设置batch处理标志
		int flg = CherryBatchConstants.BATCH_SUCCESS;
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			// 推送选择的活动
			if (!CherryChecker.isNullOrEmpty(ruleCodes, true)) {
				map.put("ruleCodes", ruleCodes.trim());
			}
			// 登陆用户信息
			UserInfo userInfo = (UserInfo) session
					.get(CherryBatchConstants.SESSION_USERINFO);
			// 所属组织
			map.put(CherryBatchConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
			// 品牌Id
			map.put(CherryBatchConstants.BRANDINFOID, brandInfoId);
			// 品牌code
			map.put(CherryBatchConstants.BRAND_CODE, brandCode);
			flg = binBESSPRM08_BL.tran_sendUpdateCoupon(map);
		} catch (CherryBatchException cbx) {
			flg = CherryBatchConstants.BATCH_WARNING;
		} catch (Exception e) {
			flg = CherryBatchConstants.BATCH_ERROR;
			logger.error(e.getMessage(),e);
		} finally {
			if(flg == CherryBatchConstants.BATCH_SUCCESS) {
				this.addActionMessage("优惠券状态推送接口正常终了");
				logger.info("******************************优惠券状态推送接口正常终了***************************");
			} else if(flg == CherryBatchConstants.BATCH_WARNING) {
				this.addActionError("优惠券状态推送接口警告终了");
				logger.info("******************************优惠券状态推送接口警告终了***************************");
			} else if(flg == CherryBatchConstants.BATCH_ERROR) {
				this.addActionError("优惠券状态推送接口异常终了");
				logger.info("******************************优惠券状态推送接口异常终了***************************");
			}
		}
		return "DOBATCHRESULT";
	}
	
	
	/**
	 * 优惠券生成接收接口
	 * 
	 * @param 无
	 * @return String
	 * */
	public String sendGenerate() throws Exception {
		
		logger.info("******************************优惠券生成接收接口开始***************************");
		// 设置batch处理标志
		int flg = CherryBatchConstants.BATCH_SUCCESS;
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			// 推送选择的活动
			if (!CherryChecker.isNullOrEmpty(ruleCodes, true)) {
				map.put("ruleCodes", ruleCodes.trim());
			}
			// 登陆用户信息
			UserInfo userInfo = (UserInfo) session
					.get(CherryBatchConstants.SESSION_USERINFO);
			// 所属组织
			map.put(CherryBatchConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
			// 品牌Id
			map.put(CherryBatchConstants.BRANDINFOID, brandInfoId);
			// 品牌code
			map.put(CherryBatchConstants.BRAND_CODE, brandCode);
			flg = binBESSPRM08_BL.sendGenerate(map);
		} catch (CherryBatchException cbx) {
			flg = CherryBatchConstants.BATCH_WARNING;
		} catch (Exception e) {
			flg = CherryBatchConstants.BATCH_ERROR;
			logger.error(e.getMessage(),e);
		} finally {
			if(flg == CherryBatchConstants.BATCH_SUCCESS) {
				this.addActionMessage("优惠券生成接收接口正常终了");
				logger.info("******************************优惠券生成接收接口正常终了***************************");
			} else if(flg == CherryBatchConstants.BATCH_WARNING) {
				this.addActionError("优惠券生成接收接口警告终了");
				logger.info("******************************优惠券生成接收接口警告终了***************************");
			} else if(flg == CherryBatchConstants.BATCH_ERROR) {
				this.addActionError("优惠券生成接收接口异常终了");
				logger.info("******************************优惠券生成接收接口异常终了***************************");
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
	
	/** 需要推送的券活动代码 */
	private String ruleCodes;

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

	public String getRuleCodes() {
		return ruleCodes;
	}

	public void setRuleCodes(String ruleCodes) {
		this.ruleCodes = ruleCodes;
	}
}
