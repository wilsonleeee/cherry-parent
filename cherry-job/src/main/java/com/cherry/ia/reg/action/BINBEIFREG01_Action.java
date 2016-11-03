/*
 * @(#)BINBEIFREG01_Action.java v1.0 2015-2-6
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
package com.cherry.ia.reg.action;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryBatchException;
import com.cherry.ia.reg.bl.BINBEIFREG01_BL;

/**
 * 区域信息下发Action
 * 
 * @author JiJW
 * @version 1.0 2015-2-6
 */
public class BINBEIFREG01_Action extends BaseAction {

	private static final long serialVersionUID = -4396310257031836640L;
	
	@Resource(name="binBEIFREG01_BL")
	private BINBEIFREG01_BL binBEIFREG01_BL;
	
	private static Logger logger = LoggerFactory.getLogger(BINBEIFREG01_Action.class.getName());
	/** 品牌ID */
	private int brandInfoId;
	/** 品牌Code */
	private String brandCode;
	
	public int getBrandInfoId() {
		return brandInfoId;
	}

	public void setBrandInfoId(int brandInfoId) {
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
	public String binbeifreg01Exec() throws Exception {
		logger
				.info("****************************** 区域信息下发(BINBEIFREG01)Batch处理开始 ***************************");
		// 设置batch处理标志
		int flg = CherryBatchConstants.BATCH_SUCCESS;
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			// 登陆用户信息
			UserInfo userInfo = (UserInfo) session.get(CherryBatchConstants.SESSION_USERINFO);
			// 所属组织
			map.put(CherryBatchConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
			map.put(CherryBatchConstants.BRANDINFOID, brandInfoId);
			map.put(CherryBatchConstants.BRAND_CODE, brandCode);
			flg = binBEIFREG01_BL.tran_batchRegion(map);
		} catch (CherryBatchException cbx) {
			flg = CherryBatchConstants.BATCH_WARNING;
			logger.info("=============WARN MSG================");
			logger.info(cbx.getMessage());
			logger.info("=====================================");
		} catch (Exception e) {
			flg = CherryBatchConstants.BATCH_ERROR;
			logger.error("=============ERROR MSG===============");
			logger.error(e.getMessage(),e);
			logger.error("=====================================");
		} finally {
			if (flg == CherryBatchConstants.BATCH_SUCCESS) {
				this.addActionMessage("区域信息下发(BINBEIFREG01)Batch处理正常终了");
				logger.info("******************************区域信息下发(BINBEIFREG01)Batch处理正常终了***************************");
			} else if (flg == CherryBatchConstants.BATCH_WARNING) {
				this.addActionError("区域信息下发(BINBEIFREG01)Batch处理警告终了");
				logger.info("******************************区域信息下发(BINBEIFREG01)Batch处理警告终了***************************");
			} else if (flg == CherryBatchConstants.BATCH_ERROR) {
				this.addActionError("区域信息下发(BINBEIFREG01)Batch处理异常终了");
				logger.info("******************************区域信息下发(BINBEIFREG01)Batch处理异常终了***************************");
			}
		}
		return "DOBATCHRESULT";
	}
}
