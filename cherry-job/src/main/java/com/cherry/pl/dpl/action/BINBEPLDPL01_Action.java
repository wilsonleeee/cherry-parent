/*
 * @(#)BINBEPLDPL01_Action.java     1.0 2010/11/04
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

package com.cherry.pl.dpl.action;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryBatchException;
import com.cherry.pl.dpl.bl.BINBEPLDPL04_BL;

/**
 * 部门数据过滤权限共通Action
 * 
 * @author WangCT
 * @version 1.0 2010.11.04
 */
public class BINBEPLDPL01_Action extends BaseAction {
	
	private static final long serialVersionUID = 7196903446519225875L;
	
	private static Logger logger = LoggerFactory.getLogger(BINBEPLDPL01_Action.class.getName());
	
	/** 实时刷新数据权限BL */
	@Resource
	private BINBEPLDPL04_BL binBEPLDPL04_BL;
	
	public String binbepldpl01Exec() {
		
//		logger.info("******************************刷新数据权限BATCH处理开始***************************");
//		// 设置batch处理标志
//		int flg = CherryBatchConstants.BATCH_SUCCESS;
//		try {
//			Map<String, Object> map = new HashMap<String, Object>();
//			// 登陆用户信息
//			UserInfo userInfo = (UserInfo) session
//					.get(CherryBatchConstants.SESSION_USERINFO);
//			// 所属组织
//			map.put(CherryBatchConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
//			// 品牌Id
//			map.put(CherryBatchConstants.BRANDINFOID, brandInfoId);
//			// 组织代码
//			map.put("orgCode", userInfo.getOrgCode());
//			// 品牌code
//			map.put(CherryBatchConstants.BRAND_CODE, brandCode);
//			// 创建部门数据过滤权限
//			flg = binBEPLDPL04_BL.sendRefreshPlMsg(map);
//		} catch (CherryBatchException cbx) {
//			flg = CherryBatchConstants.BATCH_WARNING;
//		} catch (Exception e) {
//			flg = CherryBatchConstants.BATCH_ERROR;
//		} finally {
//			if(flg == CherryBatchConstants.BATCH_SUCCESS) {
//				this.addActionMessage("刷新数据权限BATCH处理正常终了");
//				logger.info("******************************刷新数据权限BATCH处理正常终了***************************");
//			} else if(flg == CherryBatchConstants.BATCH_WARNING) {
//				this.addActionError("刷新数据权限BATCH处理警告终了");
//				logger.info("******************************刷新数据权限BATCH处理警告终了***************************");
//			} else if(flg == CherryBatchConstants.BATCH_ERROR) {
//				this.addActionError("刷新数据权限BATCH处理异常终了");
//				logger.info("******************************刷新数据权限BATCH处理异常终了***************************");
//			}
//		}
		return "DOBATCHRESULT";
	}
	
	public String createDepartRelation() {
		
		logger.info("******************************刷新数据权限BATCH处理开始***************************");
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
			// 组织代码
			map.put("orgCode", userInfo.getOrgCode());
			// 品牌code
			map.put(CherryBatchConstants.BRAND_CODE, brandCode);
			// 发送刷新数据权限MQ消息
			flg = binBEPLDPL04_BL.sendRefreshPlMsg(map);
		} catch (CherryBatchException cbx) {
			flg = CherryBatchConstants.BATCH_WARNING;
		} catch (Exception e) {
			flg = CherryBatchConstants.BATCH_ERROR;
		} finally {
			if(flg == CherryBatchConstants.BATCH_SUCCESS) {
				this.addActionMessage("刷新数据权限BATCH处理正常终了");
				logger.info("******************************刷新数据权限BATCH处理正常终了***************************");
			} else if(flg == CherryBatchConstants.BATCH_WARNING) {
				this.addActionError("刷新数据权限BATCH处理警告终了");
				logger.info("******************************刷新数据权限BATCH处理警告终了***************************");
			} else if(flg == CherryBatchConstants.BATCH_ERROR) {
				this.addActionError("刷新数据权限BATCH处理异常终了");
				logger.info("******************************刷新数据权限BATCH处理异常终了***************************");
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

}
