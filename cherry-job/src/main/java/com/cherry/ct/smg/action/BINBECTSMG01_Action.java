/*	
 * @(#)BINBEDRHAN01_Action.java     1.0 2013/02/19	
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
package com.cherry.ct.smg.action;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryBatchException;
import com.cherry.ct.smg.interfaces.BINBECTSMG01_IF;

/**
 * 沟通信息发送Action
 * 
 * @author ZhangGS
 * @version 1.0 2013/02/19	
 */
public class BINBECTSMG01_Action extends BaseAction{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4348251939187987524L;
	
	private static Logger logger = LoggerFactory.getLogger(BINBECTSMG01_Action.class.getName());
	
	@Resource
	private BINBECTSMG01_IF binBECTSMG01_IF;
	
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
	
	/**
	 * 发送沟通信息batch处理
	 * 
	 * @param 无
	 * @return String
	 * 
	 */
	public String binbectsmg01Exec() throws Exception {
		logger.info("******************************发送沟通信息开始***************************");
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
			// 调度任务类型
			map.put(CherryBatchConstants.TASK_TYPE, CherryBatchConstants.TASK_TYPE_VALUE);
			// 发送沟通信息batch主处理
			flg = binBECTSMG01_IF.runAllSchedules(map);
		} catch (CherryBatchException cbx) {
			flg = CherryBatchConstants.BATCH_WARNING;
		} catch (Exception e) {
			flg = CherryBatchConstants.BATCH_ERROR;
		} finally {
			if (flg == CherryBatchConstants.BATCH_SUCCESS) {
				this.addActionMessage("发送沟通信息BATCH处理正常结束");
				logger.info("******************************发送沟通信息正常结束***************************");
			} else if (flg == CherryBatchConstants.BATCH_WARNING) {
				this.addActionError("发送沟通信息BATCH处理警告结束");
				logger.info("******************************发送沟通信息警告结束***************************");
			} else if (flg == CherryBatchConstants.BATCH_ERROR) {
				this.addActionError("发送沟通信息BATCH处理异常结束");
				logger.info("******************************发送沟通信息异常结束***************************");
			}
		}
		return "DOBATCHRESULT";
	}
	
	
}
