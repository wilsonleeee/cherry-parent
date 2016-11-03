/*
 * @(#)BINBEPLDPL03_Action.java     1.0 2012.04.12
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
 * 权限表维护共通Action
 * 
 * @author WangCT
 * @version 1.0 2012.04.12
 */
public class BINBEPLDPL03_Action extends BaseAction {
	
	private static final long serialVersionUID = 478481201339353235L;
	
	private static Logger logger = LoggerFactory.getLogger(BINBEPLDPL03_Action.class.getName());
	
	/** 实时刷新数据权限BL */
	@Resource
	private BINBEPLDPL04_BL binBEPLDPL04_BL;
	
	public String binbepldpl03Exec1() {
		
		logger.info("******************************重建部门权限表和索引BATCH处理开始***************************");
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
			// 组织code
			map.put("orgCode", userInfo.getOrgCode());
			// 品牌code
			map.put(CherryBatchConstants.BRAND_CODE, brandCode);
			// 重建部门权限表和索引
			flg = binBEPLDPL04_BL.createDepartPrivilegeTable(map);
		} catch (CherryBatchException cbx) {
			flg = CherryBatchConstants.BATCH_WARNING;
		} catch (Exception e) {
			flg = CherryBatchConstants.BATCH_ERROR;
		} finally {
			if(flg == CherryBatchConstants.BATCH_SUCCESS) {
				this.addActionMessage("重建部门权限表和索引BATCH处理正常终了");
				logger.info("******************************重建部门权限表和索引BATCH处理正常终了***************************");
			} else if(flg == CherryBatchConstants.BATCH_WARNING) {
				this.addActionError("重建部门权限表和索引BATCH处理警告终了");
				logger.info("******************************重建部门权限表和索引BATCH处理警告终了***************************");
			} else if(flg == CherryBatchConstants.BATCH_ERROR) {
				this.addActionError("重建部门权限表和索引BATCH处理异常终了");
				logger.info("******************************重建部门权限表和索引BATCH处理异常终了***************************");
			}
		}
		return "DOBATCHRESULT";
	}
	
	public String binbepldpl03Exec2() {
		
//		logger.info("******************************给部门权限表创建索引BATCH处理开始***************************");
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
//			// 品牌code
//			map.put(CherryBatchConstants.BRAND_CODE, brandCode);
//			// 给部门权限表创建索引
//			flg = binBEPLDPL03_BL.tran_createDepartPrivilegeIndex(map);
//		} catch (CherryBatchException cbx) {
//			flg = CherryBatchConstants.BATCH_WARNING;
//		} catch (Exception e) {
//			flg = CherryBatchConstants.BATCH_ERROR;
//		} finally {
//			if(flg == CherryBatchConstants.BATCH_SUCCESS) {
//				this.addActionMessage("给部门权限表创建索引BATCH处理正常终了");
//				logger.info("******************************给部门权限表创建索引BATCH处理正常终了***************************");
//			} else if(flg == CherryBatchConstants.BATCH_WARNING) {
//				this.addActionError("给部门权限表创建索引BATCH处理警告终了");
//				logger.info("******************************给部门权限表创建索引BATCH处理警告终了***************************");
//			} else if(flg == CherryBatchConstants.BATCH_ERROR) {
//				this.addActionError("给部门权限表创建索引BATCH处理异常终了");
//				logger.info("******************************给部门权限表创建索引BATCH处理异常终了***************************");
//			}
//		}
		return "DOBATCHRESULT";
	}
	
	public String binbepldpl03Exec3() {
		
		logger.info("******************************重建人员权限表和索引BATCH处理开始***************************");
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
			// 重建人员权限表和索引
			flg = binBEPLDPL04_BL.createEmployeePrivilegeTable(map);
		} catch (CherryBatchException cbx) {
			flg = CherryBatchConstants.BATCH_WARNING;
		} catch (Exception e) {
			flg = CherryBatchConstants.BATCH_ERROR;
		} finally {
			if(flg == CherryBatchConstants.BATCH_SUCCESS) {
				this.addActionMessage("重建人员权限表和索引BATCH处理正常终了");
				logger.info("******************************重建人员权限表和索引BATCH处理正常终了***************************");
			} else if(flg == CherryBatchConstants.BATCH_WARNING) {
				this.addActionError("重建人员权限表和索引BATCH处理警告终了");
				logger.info("******************************重建人员权限表和索引BATCH处理警告终了***************************");
			} else if(flg == CherryBatchConstants.BATCH_ERROR) {
				this.addActionError("重建人员权限表和索引BATCH处理异常终了");
				logger.info("******************************重建人员权限表和索引BATCH处理异常终了***************************");
			}
		}
		return "DOBATCHRESULT";
	}
	
	public String binbepldpl03Exec4() {
		
//		logger.info("******************************给人员权限表创建索引BATCH处理开始***************************");
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
//			// 品牌code
//			map.put(CherryBatchConstants.BRAND_CODE, brandCode);
//			// 给人员权限表创建索引
//			flg = binBEPLDPL03_BL.tran_createEmployeePrivilegeIndex(map);
//		} catch (CherryBatchException cbx) {
//			flg = CherryBatchConstants.BATCH_WARNING;
//		} catch (Exception e) {
//			flg = CherryBatchConstants.BATCH_ERROR;
//		} finally {
//			if(flg == CherryBatchConstants.BATCH_SUCCESS) {
//				this.addActionMessage("给人员权限表创建索引BATCH处理正常终了");
//				logger.info("******************************给人员权限表创建索引BATCH处理正常终了***************************");
//			} else if(flg == CherryBatchConstants.BATCH_WARNING) {
//				this.addActionError("给人员权限表创建索引BATCH处理警告终了");
//				logger.info("******************************给人员权限表创建索引BATCH处理警告终了***************************");
//			} else if(flg == CherryBatchConstants.BATCH_ERROR) {
//				this.addActionError("给人员权限表创建索引BATCH处理异常终了");
//				logger.info("******************************给人员权限表创建索引BATCH处理异常终了***************************");
//			}
//		}
		return "DOBATCHRESULT";
	}
	
	public String binbepldpl03Exec5() {
		
		logger.info("******************************重建部门从属关系表和索引BATCH处理开始***************************");
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
			// 重建部门从属关系表和索引
			flg = binBEPLDPL04_BL.createDepartRelationTable(map);
		} catch (CherryBatchException cbx) {
			flg = CherryBatchConstants.BATCH_WARNING;
		} catch (Exception e) {
			flg = CherryBatchConstants.BATCH_ERROR;
		} finally {
			if(flg == CherryBatchConstants.BATCH_SUCCESS) {
				this.addActionMessage("重建部门从属关系表和索引BATCH处理正常终了");
				logger.info("******************************重建部门从属关系表和索引BATCH处理正常终了***************************");
			} else if(flg == CherryBatchConstants.BATCH_WARNING) {
				this.addActionError("重建部门从属关系表和索引BATCH处理警告终了");
				logger.info("******************************重建部门从属关系表和索引BATCH处理警告终了***************************");
			} else if(flg == CherryBatchConstants.BATCH_ERROR) {
				this.addActionError("重建部门从属关系表和索引BATCH处理异常终了");
				logger.info("******************************重建部门从属关系表和索引BATCH处理异常终了***************************");
			}
		}
		return "DOBATCHRESULT";
	}
	
	public String binbepldpl03Exec6() {
		
//		logger.info("******************************给部门从属关系表创建索引BATCH处理开始***************************");
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
//			// 品牌code
//			map.put(CherryBatchConstants.BRAND_CODE, brandCode);
//			// 给部门从属关系表创建索引
//			flg = binBEPLDPL03_BL.tran_createDepartRelationIndex(map);
//		} catch (CherryBatchException cbx) {
//			flg = CherryBatchConstants.BATCH_WARNING;
//		} catch (Exception e) {
//			flg = CherryBatchConstants.BATCH_ERROR;
//		} finally {
//			if(flg == CherryBatchConstants.BATCH_SUCCESS) {
//				this.addActionMessage("给部门从属关系表创建索引BATCH处理正常终了");
//				logger.info("******************************给部门从属关系表创建索引BATCH处理正常终了***************************");
//			} else if(flg == CherryBatchConstants.BATCH_WARNING) {
//				this.addActionError("给部门从属关系表创建索引BATCH处理警告终了");
//				logger.info("******************************给部门从属关系表创建索引BATCH处理警告终了***************************");
//			} else if(flg == CherryBatchConstants.BATCH_ERROR) {
//				this.addActionError("给部门从属关系表创建索引BATCH处理异常终了");
//				logger.info("******************************给部门从属关系表创建索引BATCH处理异常终了***************************");
//			}
//		}
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
