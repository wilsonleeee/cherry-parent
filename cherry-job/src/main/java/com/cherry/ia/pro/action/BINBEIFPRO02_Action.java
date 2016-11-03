/*
 * @(#)BINBEIFPRO02_Action.java     1.0 2011/07/18
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

import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryBatchException;
import com.cherry.ia.pro.bl.BINBEIFPRO02_BL;

/**
 * 
 *产品下发ACTION
 * 
 * 
 * @author lipc
 * @version 1.0 2011/07/18
 */
public class BINBEIFPRO02_Action extends BaseAction {

	private static final long serialVersionUID = -4396310257031836640L;
	@Resource
	private BINBEIFPRO02_BL binbeifpro02BL;

	private static Logger logger = LoggerFactory
			.getLogger(BINBEIFPRO02_Action.class.getName());
	/** 品牌ID */
	private int brandInfoId;

	public int getBrandInfoId() {
		return brandInfoId;
	}

	public void setBrandInfoId(int brandInfoId) {
		this.brandInfoId = brandInfoId;
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
	public String binbeifpro02Exec() throws Exception {
		logger.info("******************************BINBEIFPRO02处理开始***************************");
		// 设置batch处理标志
		int flg = CherryBatchConstants.BATCH_SUCCESS;
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put(CherryBatchConstants.BRANDINFOID, brandInfoId);
			flg = binbeifpro02BL.tran_batchProducts(map);
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
				this.addActionMessage("产品下发处理正常终了");
				logger.info("******************************产品下发处理正常终了***************************");
			} else if (flg == CherryBatchConstants.BATCH_WARNING) {
				this.addActionError("产品下发处理警告终了");
				logger.info("******************************产品下发处理警告终了***************************");
			} else if (flg == CherryBatchConstants.BATCH_ERROR) {
				this.addActionError("产品下发处理异常终了");
				logger.info("******************************产品下发处理异常终了***************************");
			}
		}
		return "DOBATCHRESULT";
	}
}
