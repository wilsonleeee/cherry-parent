/*
 * @(#)BINBEIFEMP04_Action.java     1.0 2013/04/25
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
package com.cherry.ia.emp.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryBatchException;
import com.cherry.ia.emp.bl.BINBEIFEMP04_BL;

/**
 * CPA账号同步处理Action
 * 
 * @author WangCT
 * @version 1.0 2013/04/25
 */
public class BINBEIFEMP04_Action extends BaseAction {
	
	private static final long serialVersionUID = 4215721992398145538L;

	private static Logger logger = LoggerFactory.getLogger(BINBEIFEMP04_Action.class.getName());
	
	/** CPA账号同步处理BL **/
	@Resource
	private BINBEIFEMP04_BL binBEIFEMP04_BL;
	
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
		
		Map<String, Object> map = new HashMap<String, Object>();
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session.get(CherryBatchConstants.SESSION_USERINFO);
		// 所属组织
		map.put(CherryBatchConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		// 品牌Id
		map.put(CherryBatchConstants.BRANDINFOID, brandInfoId);
		// 取得新后台岗位List
		positionList = binBEIFEMP04_BL.getPositionList(map);
		
		return SUCCESS;
	}
	
	/**
	 * <p>
	 * CPA账号同步处理
	 * </p>
	 * 
	 * 
	 * @param 无
	 * @return String
	 * 
	 */
	public String binbeifemp04Exec() throws Exception {
		
		logger.info("******************************CPA账号同步处理开始***************************");
		// 设置batch处理标志
		int flg = CherryBatchConstants.BATCH_SUCCESS;
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			// 登陆用户信息
			UserInfo userInfo = (UserInfo) session.get(CherryBatchConstants.SESSION_USERINFO);
			// 所属组织
			map.put(CherryBatchConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
			// 品牌Id
			map.put(CherryBatchConstants.BRANDINFOID, brandInfoId);
			// 组织code
			map.put("orgCode", userInfo.getOrgCode());
			// 品牌code
			map.put(CherryBatchConstants.BRAND_CODE, brandCode);
			// CPA数据库名
			map.put("dbName", dbName);
			// CPA中用户权限级别和新后台岗位对应关系
			map.put("levelRelation", levelRelation);
			// CPA账号同步处理
			List<Map<String, Object>> errorList = binBEIFEMP04_BL.tran_importCPAUser(map);
			if(errorList != null && !errorList.isEmpty()) {
				for(Map<String, Object> errorMap : errorList) {
					String errorMes = (String)errorMap.get("errorMes");
					logger.error(errorMes);
					this.addActionError(errorMes);
				}
				flg = CherryBatchConstants.BATCH_WARNING;
			}
		} catch (CherryBatchException cbx) {
			flg = CherryBatchConstants.BATCH_WARNING;
		} catch (Exception e) {
			flg = CherryBatchConstants.BATCH_ERROR;
		} finally {
			if (flg == CherryBatchConstants.BATCH_SUCCESS) {
				this.addActionMessage("CPA账号同步处理正常终了");
				logger.info("******************************CPA账号同步处理正常终了***************************");
			} else if (flg == CherryBatchConstants.BATCH_WARNING) {
				this.addActionError("CPA账号同步处理警告终了");
				logger.info("******************************CPA账号同步处理警告终了***************************");
			} else if (flg == CherryBatchConstants.BATCH_ERROR) {
				this.addActionError("CPA账号同步处理异常终了");
				logger.info("******************************CPA账号同步处理异常终了***************************");
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
	
	/** CPA数据库名 */
	private String dbName;
	
	/** CPA中用户权限级别和新后台岗位对应关系 */
	private String levelRelation;
	
	private List<Map<String, Object>> positionList;

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

	public String getDbName() {
		return dbName;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

	public String getLevelRelation() {
		return levelRelation;
	}

	public void setLevelRelation(String levelRelation) {
		this.levelRelation = levelRelation;
	}

	public List<Map<String, Object>> getPositionList() {
		return positionList;
	}

	public void setPositionList(List<Map<String, Object>> positionList) {
		this.positionList = positionList;
	}

}
