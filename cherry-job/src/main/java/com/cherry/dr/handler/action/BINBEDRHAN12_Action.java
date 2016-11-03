/*
 * @(#)BINBEDRHAN12_Action.java     1.0 2012/08/22
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
import com.cherry.dr.handler.bl.BINBEDRHAN12_BL;

/**
 * 会员规则履历处理Action
 * 
 * @author WangCT
 * @version 1.0 2012/08/22
 */
public class BINBEDRHAN12_Action extends BaseAction {
	
	private static final long serialVersionUID = 4862007890324524780L;

	private static Logger logger = LoggerFactory.getLogger(BINBEDRHAN12_Action.class.getName());
	
	/** 会员规则履历处理BL */
	@Resource
	private BINBEDRHAN12_BL binBEDRHAN12_BL;
	
	/**
	 * <p>
	 * 会员规则履历处理
	 * </p>
	 * 
	 * 
	 * @param 无
	 * @return String
	 * 
	 */
	public String memRuleRecordHandle() throws Exception {

		logger.info("******************************把旧的规则履历迁移到规则履历历史表处理开始***************************");
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
			// 把旧的规则履历迁移到规则履历历史表处理
			flg = binBEDRHAN12_BL.tran_memRuleRecordMoveHandle(map);
		} catch (CherryBatchException cbx) {
			flg = CherryBatchConstants.BATCH_WARNING;
		} catch (Exception e) {
			flg = CherryBatchConstants.BATCH_ERROR;
			e.printStackTrace();
		} finally {
			if (flg == CherryBatchConstants.BATCH_SUCCESS) {
				this.addActionMessage("把旧的规则履历迁移到规则履历历史表处理正常终了");
				logger.info("******************************把旧的规则履历迁移到规则履历历史表处理正常终了***************************");
			} else if (flg == CherryBatchConstants.BATCH_WARNING) {
				this.addActionError("把旧的规则履历迁移到规则履历历史表处理警告终了");
				logger.info("******************************把旧的规则履历迁移到规则履历历史表处理警告终了***************************");
			} else if (flg == CherryBatchConstants.BATCH_ERROR) {
				this.addActionError("把旧的规则履历迁移到规则履历历史表处理异常终了");
				logger.info("******************************把旧的规则履历迁移到规则履历历史表处理异常终了***************************");
			}
		}
		return "DOBATCHRESULT";
	}
	
	/**
	 * <p>
	 * 从MongoDB把规则履历迁移到数据库
	 * </p>
	 * 
	 * 
	 * @param 无
	 * @return String
	 * 
	 */
	public String memRuleRecordHandle1() throws Exception {

		logger.info("******************************从MongoDB把规则履历迁移到数据库处理开始***************************");
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
			// 从MongoDB把规则履历迁移到数据库
			flg = binBEDRHAN12_BL.tran_moveMongoDBToDataBase(map);
		} catch (CherryBatchException cbx) {
			flg = CherryBatchConstants.BATCH_WARNING;
		} catch (Exception e) {
			flg = CherryBatchConstants.BATCH_ERROR;
			e.printStackTrace();
		} finally {
			if (flg == CherryBatchConstants.BATCH_SUCCESS) {
				this.addActionMessage("从MongoDB把规则履历迁移到数据库处理正常终了");
				logger.info("******************************从MongoDB把规则履历迁移到数据库处理正常终了***************************");
			} else if (flg == CherryBatchConstants.BATCH_WARNING) {
				this.addActionError("从MongoDB把规则履历迁移到数据库处理警告终了");
				logger.info("******************************从MongoDB把规则履历迁移到数据库处理警告终了***************************");
			} else if (flg == CherryBatchConstants.BATCH_ERROR) {
				this.addActionError("从MongoDB把规则履历迁移到数据库处理异常终了");
				logger.info("******************************从MongoDB把规则履历迁移到数据库处理异常终了***************************");
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
