/*
 * @(#)BINBEIFDEP01_Action.java     1.0 2010/11/12
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

package com.cherry.ia.dep.action;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryBatchException;
import com.cherry.ia.dep.bl.BINBEIFDEP01_BL;

/**
 * 
 * 部门列表导入Action
 * 
 * 
 * @author WangCT
 * @version 1.0 2011.05.26
 */
public class BINBEIFDEP01_Action extends BaseAction {

	private static final long serialVersionUID = 7745392582790890491L;
	
	private static Logger logger = LoggerFactory.getLogger(BINBEIFDEP01_Action.class.getName());
	
	/** 部门列表导入BL */
	@Resource
	private BINBEIFDEP01_BL binBEIFDEP01_BL;
	
	/**
	 * <p>
	 * 部门列表导入batch处理
	 * </p>
	 * 
	 * 
	 * @param 无
	 * @return String
	 * 
	 */
	public String binbeifdep01Exec() throws Exception {

		logger.info("******************************部门列表导入处理开始***************************");
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
			// 部门列表导入batch主处理
			flg = binBEIFDEP01_BL.tran_batchDepart(map);
		} catch (CherryBatchException cbx) {
			flg = CherryBatchConstants.BATCH_WARNING;
		} catch (Exception e) {
			flg = CherryBatchConstants.BATCH_ERROR;
		} finally {
			if (flg == CherryBatchConstants.BATCH_SUCCESS) {
				this.addActionMessage("部门导入BATCH处理正常终了");
				logger.info("******************************部门列表导入处理正常终了***************************");
			} else if (flg == CherryBatchConstants.BATCH_WARNING) {
				this.addActionError("部门导入BATCH处理警告终了");
				logger.info("******************************部门列表导入处理警告终了***************************");
			} else if (flg == CherryBatchConstants.BATCH_ERROR) {
				this.addActionError("部门导入BATCH处理异常终了");
				logger.info("******************************部门列表导入处理异常终了***************************");
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
