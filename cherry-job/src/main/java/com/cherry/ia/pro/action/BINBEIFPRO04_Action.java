/*
 * @(#)BINBEIFPRO04_Action.java     1.0 2014/08/18
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
package com.cherry.ia.pro.action;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryBatchException;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.ia.pro.bl.BINBEIFPRO04_BL;

/**
 * 
 *产品下发(实时)ACTION
 * 
 * 
 * @author jijw
 * @version 1.0 2011/07/18
 */
public class BINBEIFPRO04_Action extends BaseAction {

	private static final long serialVersionUID = -4396310257031836640L;
	@Resource
	private BINBEIFPRO04_BL binbeifpro04_BL;

	private static Logger logger = LoggerFactory.getLogger(BINBEIFPRO04_Action.class.getName());
	
	/** 组织Id */
	private String organizationInfoId;
	
	/** 品牌ID */
	private int brandInfoId;

	public int getBrandInfoId() {
		return brandInfoId;
	}

	public void setBrandInfoId(int brandInfoId) {
		this.brandInfoId = brandInfoId;
	}
	
	public String getOrganizationInfoId() {
		return organizationInfoId;
	}

	public void setOrganizationInfoId(String organizationInfoId) {
		this.organizationInfoId = organizationInfoId;
	}

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
	public String binbeifpro04Exec() throws Exception {
		logger.info("****************************** 产品下发(实时)处理开始***************************");
		// 设置batch处理标志
		int flg = CherryBatchConstants.BATCH_SUCCESS;
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			// 登陆用户信息
			UserInfo userInfo = (UserInfo) session.get(CherryBatchConstants.SESSION_USERINFO);
			// 所属组织
			map.put(CherryBatchConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());

			map.put(CherryBatchConstants.BRANDINFOID, brandInfoId);
			map.put(CherryBatchConstants.BRAND_CODE, userInfo.getBrandCode());
			
			// Job运行履历表的运行方式
			map.put("RunType", "MT");
			// Job运行履历表的运行方式
			map.put("language", userInfo.getLanguage());
//			map.put(CherryBatchConstants.ORGANIZATIONINFOID, organizationInfoId);
			Map<String,Object> flagMap = binbeifpro04_BL.tran_batchProducts(map);
			flg=ConvertUtil.getInt(flagMap.get("flag"));
		} catch (CherryBatchException cbx) {
			flg = CherryBatchConstants.BATCH_WARNING;
			logger.error("=============WARN MSG================");
			logger.error(cbx.getMessage(),cbx);
			logger.error("=====================================");
		} catch (Exception e) {
			flg = CherryBatchConstants.BATCH_ERROR;
			logger.error("=============ERROR MSG===============");
			logger.error(e.getMessage(),e);
			logger.error("=====================================");
		} catch(Throwable t){
			flg = CherryBatchConstants.BATCH_ERROR;
			logger.error("=============ERROR MSG===============");
			logger.error(t.getMessage(),t);
			logger.error("=====================================");
		}
		finally {
			if (flg == CherryBatchConstants.BATCH_SUCCESS) {
				this.addActionMessage("产品下发(实时)处理正常终了");
				logger.info("******************************产品下发(实时)处理正常终了***************************");
			} else if (flg == CherryBatchConstants.BATCH_WARNING) {
				this.addActionError("产品下发(实时)处理警告终了");
				logger.info("******************************产品下发(实时)处理警告终了***************************");
			} else if (flg == CherryBatchConstants.BATCH_ERROR) {
				this.addActionError("产品下发(实时)处理异常终了");
				logger.info("******************************产品下发(实时)处理异常终了***************************");
			}
		}
		return "DOBATCHRESULT";
	}
}
