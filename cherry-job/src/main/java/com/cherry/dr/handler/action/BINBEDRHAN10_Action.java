/*
 * @(#)BINBEDRHAN10_Action.java     1.0 2012/05/28
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
import com.cherry.dr.handler.bl.BINBEDRHAN10_BL;

/**
 * 
 * 处理会员的规则履历记录Action
 * 
 * 
 * @author WangCT
 * @version 1.0 2012/05/28
 */
public class BINBEDRHAN10_Action extends BaseAction {

	private static final long serialVersionUID = 5068660890073560509L;

	private static Logger logger = LoggerFactory.getLogger(BINBEDRHAN10_Action.class.getName());
	
	/** 处理会员的规则履历记录BL */
	@Resource
	private BINBEDRHAN10_BL binBEDRHAN10_BL;
	
	/**
	 * <p>
	 * 处理会员的规则履历记录
	 * </p>
	 * 
	 * 
	 * @param 无
	 * @return String
	 * 
	 */
	public String memRuleRecordHandle() throws Exception {

		logger.info("******************************会员规则履历记录处理开始***************************");
		// 设置batch处理标志
		int flg = CherryBatchConstants.BATCH_SUCCESS;
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			// 登陆用户信息
			UserInfo userInfo = (UserInfo) session
					.get(CherryBatchConstants.SESSION_USERINFO);
			// 所属组织
			map.put(CherryBatchConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
			// 组织代码
			map.put("orgCode", userInfo.getOrgCode());
			// 品牌Id
			map.put(CherryBatchConstants.BRANDINFOID, brandInfoId);
			// 品牌code
			map.put(CherryBatchConstants.BRAND_CODE, brandCode);
			// 处理会员的规则履历记录，把规则履历从数据库迁移到MongoDB，同时把等级变化的履历发送到老后台
			flg = binBEDRHAN10_BL.tran_memRuleRecordHandle(map);
		} catch (CherryBatchException cbx) {
			flg = CherryBatchConstants.BATCH_WARNING;
		} catch (Exception e) {
			flg = CherryBatchConstants.BATCH_ERROR;
			e.printStackTrace();
		} finally {
			if (flg == CherryBatchConstants.BATCH_SUCCESS) {
				this.addActionMessage("会员规则履历记录处理正常终了");
				logger.info("******************************会员规则履历记录处理正常终了***************************");
			} else if (flg == CherryBatchConstants.BATCH_WARNING) {
				this.addActionError("会员规则履历记录处理警告终了");
				logger.info("******************************会员规则履历记录处理警告终了***************************");
			} else if (flg == CherryBatchConstants.BATCH_ERROR) {
				this.addActionError("会员规则履历记录处理异常终了");
				logger.info("******************************会员规则履历记录处理异常终了***************************");
			}
		}
		return "DOBATCHRESULT";
	}
	
	/**
	 * <p>
	 * 处理会员的规则履历记录(把规则履历从MongoDB迁移到数据库)
	 * </p>
	 * 
	 * 
	 * @param 无
	 * @return String
	 * 
	 */
	public String memRuleRecordHandleMon() throws Exception {

		logger.info("******************************会员规则履历记录处理开始(把规则履历从MongoDB迁移到数据库)***************************");
		// 设置batch处理标志
		int flg = CherryBatchConstants.BATCH_SUCCESS;
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			// 登陆用户信息
			UserInfo userInfo = (UserInfo) session
					.get(CherryBatchConstants.SESSION_USERINFO);
			// 所属组织
			map.put(CherryBatchConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
			// 组织代码
			map.put("orgCode", userInfo.getOrgCode());
			// 品牌Id
			map.put(CherryBatchConstants.BRANDINFOID, brandInfoId);
			// 品牌code
			map.put(CherryBatchConstants.BRAND_CODE, brandCode);
			// 处理会员的规则履历记录，把规则履历从MongoDB迁移到数据库，同时更新会员扩展属性表
			flg = binBEDRHAN10_BL.tran_memRuleRecordHandle2(map);
		} catch (CherryBatchException cbx) {
			flg = CherryBatchConstants.BATCH_WARNING;
		} catch (Exception e) {
			flg = CherryBatchConstants.BATCH_ERROR;
			e.printStackTrace();
		} finally {
			if (flg == CherryBatchConstants.BATCH_SUCCESS) {
				this.addActionMessage("会员规则履历记录处理正常终了");
				logger.info("******************************会员规则履历记录处理正常终了***************************");
			} else if (flg == CherryBatchConstants.BATCH_WARNING) {
				this.addActionError("会员规则履历记录处理警告终了");
				logger.info("******************************会员规则履历记录处理警告终了***************************");
			} else if (flg == CherryBatchConstants.BATCH_ERROR) {
				this.addActionError("会员规则履历记录处理异常终了");
				logger.info("******************************会员规则履历记录处理异常终了***************************");
			}
		}
		return "DOBATCHRESULT";
	}
	
	/**
	 * <p>
	 * 把所有会员的化妆次数履历全部发送到老后台
	 * </p>
	 * 
	 * 
	 * @param 无
	 * @return String
	 * 
	 */
	public String memCurBtimesHandle() throws Exception {

		logger.info("******************************把所有会员的化妆次数履历全部发送到老后台开始***************************");
		// 设置batch处理标志
		int flg = CherryBatchConstants.BATCH_SUCCESS;
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			// 登陆用户信息
			UserInfo userInfo = (UserInfo) session
					.get(CherryBatchConstants.SESSION_USERINFO);
			// 所属组织
			map.put(CherryBatchConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
			// 组织代码
			map.put("orgCode", userInfo.getOrgCode());
			// 品牌Id
			map.put(CherryBatchConstants.BRANDINFOID, brandInfoId);
			// 品牌code
			map.put(CherryBatchConstants.BRAND_CODE, brandCode);
			// 把所有会员的化妆次数履历全部发送到老后台
			flg = binBEDRHAN10_BL.tran_memCurBtimesHandle(map);
		} catch (CherryBatchException cbx) {
			flg = CherryBatchConstants.BATCH_WARNING;
		} catch (Exception e) {
			flg = CherryBatchConstants.BATCH_ERROR;
			e.printStackTrace();
		} finally {
			if (flg == CherryBatchConstants.BATCH_SUCCESS) {
				this.addActionMessage("把所有会员的化妆次数履历全部发送到老后台正常终了");
				logger.info("******************************把所有会员的化妆次数履历全部发送到老后台正常终了***************************");
			} else if (flg == CherryBatchConstants.BATCH_WARNING) {
				this.addActionError("把所有会员的化妆次数履历全部发送到老后台警告终了");
				logger.info("******************************把所有会员的化妆次数履历全部发送到老后台警告终了***************************");
			} else if (flg == CherryBatchConstants.BATCH_ERROR) {
				this.addActionError("把所有会员的化妆次数履历全部发送到老后台异常终了");
				logger.info("******************************把所有会员的化妆次数履历全部发送到老后台异常终了***************************");
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
