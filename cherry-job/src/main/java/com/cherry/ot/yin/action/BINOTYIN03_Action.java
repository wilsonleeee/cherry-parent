/*	
 * @(#)BINOTYIN03_Action.java     1.0 @2013-03-11		
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

package com.cherry.ot.yin.action;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryBatchException;
import com.cherry.ot.yin.bl.BINOTYIN03_BL;

/**
 * 颖通接口：产品调拨确认单导出Action
 * 
 * @author menghao
 * 
 * @version 2013-03-11
 *
 */
public class BINOTYIN03_Action extends BaseAction {

	private static final long serialVersionUID = -615159224395927704L;

	@Resource(name = "binOTYIN03_BL")
	private BINOTYIN03_BL binOTYIN03_BL;

	private static Logger logger = LoggerFactory
			.getLogger(BINOTYIN03_Action.class.getName());

	public String init() throws Exception {

		logger.info("******************************颖通产品调拨确认单导出处理开始***************************");
		// 设置batch处理标志
		int flg = CherryBatchConstants.BATCH_SUCCESS;
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			// 登陆用户信息
			UserInfo userInfo = (UserInfo) session
					.get(CherryBatchConstants.SESSION_USERINFO);
			// 所属组织
			map.put(CherryBatchConstants.ORGANIZATIONINFOID,
					userInfo.getBIN_OrganizationInfoID());
			// 品牌Id
			map.put(CherryBatchConstants.BRANDINFOID, brandInfoId);

			flg = binOTYIN03_BL.tran_batchExportPrtAllocatOut(map);
		} catch (CherryBatchException cbx) {
			logger.error(cbx.getMessage(),cbx);
			flg = CherryBatchConstants.BATCH_WARNING;
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			flg = CherryBatchConstants.BATCH_ERROR;
		} finally {
			if (flg == CherryBatchConstants.BATCH_SUCCESS) {
				this.addActionMessage("颖通产品调拨确认单导出处理正常终了");
				logger.info("******************************颖通产品调拨确认单导出处理正常终了***************************");
			} else if (flg == CherryBatchConstants.BATCH_WARNING) {
				this.addActionError("颖通产品调拨确认单导出处理警告终了");
				logger.info("******************************颖通产品调拨确认单导出处理警告终了***************************");
			} else if (flg == CherryBatchConstants.BATCH_ERROR) {
				this.addActionError("颖通产品调拨确认单导出处理异常终了");
				logger.info("******************************颖通产品调拨确认单导出处理异常终了***************************");
			}
		}
		return "DOBATCHRESULT";
	}

	/** 品牌Id */
	private String brandInfoId;

	/** 组织Id */
	private String organizationInfoId;
	
	public String getOrganizationInfoId() {
		return organizationInfoId;
	}

	public void setOrganizationInfoId(String organizationInfoId) {
		this.organizationInfoId = organizationInfoId;
	}

	public String getBrandInfoId() {
		return brandInfoId;
	}

	public void setBrandInfoId(String brandInfoId) {
		this.brandInfoId = brandInfoId;
	}

}
