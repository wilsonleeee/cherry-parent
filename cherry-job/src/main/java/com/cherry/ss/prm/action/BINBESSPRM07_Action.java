/*
 * @(#)BINBESSPRM07_Action.java     1.0 2014/09/20
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
package com.cherry.ss.prm.action;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryBatchException;
import com.cherry.ss.prm.bl.BINBESSPRM07_BL;

/**
 * 
 *促销品下发(实时)ACTION
 * 
 * 
 * @author jijw
 * @version 1.0 2014.09.20
 */
public class BINBESSPRM07_Action extends BaseAction {

	private static final long serialVersionUID = 7320868320219146872L;
	
	@Resource
	private BINBESSPRM07_BL binbessprm07BL;
	
	private static Logger logger = LoggerFactory.getLogger(BINBESSPRM07_Action.class);
	
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
	 * <p>
	 * 画面初期显示
	 * </p>
	 * 
	 * 
	 * @param 无
	 * @return String
	 * 
	 */
	public String binbessprm07Exec() throws Exception {
		logger.info("******************************促销品下发处理开始***************************");
		// 设置batch处理标志
		int flg = CherryBatchConstants.BATCH_SUCCESS;
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			// 登陆用户信息
			UserInfo userInfo = (UserInfo) session.get(CherryBatchConstants.SESSION_USERINFO);
			// 所属组织
			map.put(CherryBatchConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
			// 品牌信息ID
			map.put("brandInfoId", brandInfoId);
			// 品牌Code
			map.put("brandCode", brandCode);
			// 促销品实时下发
			Map<String, Object> resMap = binbessprm07BL.tran_batchPromPrt(map);
			flg = (Integer) resMap.get("flag");
		} catch (CherryBatchException cbx) {
			flg = CherryBatchConstants.BATCH_WARNING;
			logger.error(cbx.getMessage(),cbx);
		} catch (Exception e) {
			flg = CherryBatchConstants.BATCH_ERROR;
			logger.error(e.getMessage(),e);
		} finally {
			if (flg == CherryBatchConstants.BATCH_SUCCESS) {
				this.addActionMessage("促销品下发(实时)处理正常终了");
				logger
						.info("******************************促销品下发(实时)处理正常终了***************************");
			} else if (flg == CherryBatchConstants.BATCH_WARNING) {
				this.addActionError("促销品下发(实时)处理警告终了");
				logger
						.info("******************************促销品下发(实时)处理警告终了***************************");
			} else if (flg == CherryBatchConstants.BATCH_ERROR) {
				this.addActionError("促销品下发(实时)处理异常终了");
				logger
						.info("******************************促销品下发(实时)处理异常终了***************************");
			}
		}
		return "DOBATCHRESULT";
	}
}
