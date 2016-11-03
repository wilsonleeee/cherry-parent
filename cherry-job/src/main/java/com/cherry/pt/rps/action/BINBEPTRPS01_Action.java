/*	
 * @(#)BINBEPTRPS01_Action.java     1.0.0 2013/08/15		
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
package com.cherry.pt.rps.action;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryBatchException;
import com.cherry.pt.rps.bl.BINBEPTRPS01_BL;

/**
 * @author zhangle
 * @version 1.0.1 2013.08.15
 * 
 */
public class BINBEPTRPS01_Action extends BaseAction  {

	private static final long serialVersionUID = 7891082436429598976L;
	private static final Logger logger = LoggerFactory.getLogger(BINBEPTRPS01_Action.class);
	@Resource
	private BINBEPTRPS01_BL binBEPTRPS01_BL;

	public String binBEPTRPS01Exec() {
		logger.info("*************************进销存操作统计处理开始*********************************");
		int flag = CherryBatchConstants.BATCH_SUCCESS;
		try {
			flag = binBEPTRPS01_BL.tran_ScheduleTask(getCommonMap());
		} catch (CherryBatchException cbx) {
			logger.error(cbx.getMessage(),cbx);
			flag = CherryBatchConstants.BATCH_WARNING;
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			flag = CherryBatchConstants.BATCH_ERROR;
		} finally {
			if (flag == CherryBatchConstants.BATCH_SUCCESS) {
				this.addActionMessage("进销存操作统计处理正常终了");
				logger.info("*************************进销存操作统计处理成功结束*********************************");
			} else if (flag == CherryBatchConstants.BATCH_WARNING) {
				this.addActionError("进销存操作统计处理警告终了");
				logger.info("*************************进销存操作统计处理警告结束*********************************");
			} else if (flag == CherryBatchConstants.BATCH_ERROR) {
				this.addActionError("进销存操作统计处理异常终了");
				logger.info("*************************进销存操作统计处理失败结束*********************************");
			}
		} 
		return "DOBATCHRESULT";
	}

	/**
	 * 初始画面
	 * @return
	 */
	public String init() {
		setBussinessDate(binBEPTRPS01_BL.getBussinessDate(getCommonMap()));
		return SUCCESS;
	}
	

	public Map<String, Object> getCommonMap() {
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
		map.put("date", bussinessDate);
		return map;
	}

	
	/** 品牌Id */
	private String brandInfoId;
	
	/** 组织Id */
	private String organizationInfoId;
	
	/** 品牌code */
	private String brandCode;
	
	/** 业务日期*/
	private String bussinessDate;

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

	public String getBussinessDate() {
		return bussinessDate;
	}

	public void setBussinessDate(String bussinessDate) {
		this.bussinessDate = bussinessDate;
	}

}
