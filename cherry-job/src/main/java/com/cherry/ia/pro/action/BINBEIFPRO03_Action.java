/*
 * @(#)BINBEIFPRO03_Action.java     1.0 2014/05/26
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

import com.cherry.cm.util.CherryBatchUtil;
import com.cherry.cm.util.ConvertUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryBatchException;
import com.cherry.ia.pro.bl.BINBEIFPRO03_BL;

/**
 * 
 * 柜台柜台产品下发ACTION
 * 
 * 
 * @author jijw
 * @version 1.0 2014/05/26
 */
public class BINBEIFPRO03_Action extends BaseAction {

	private static final long serialVersionUID = -4396310257031836640L;
	@Resource
	private BINBEIFPRO03_BL binbeifpro03BL;

	private static Logger logger = LoggerFactory
			.getLogger(BINBEIFPRO03_Action.class.getName());
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
	public String binbeifpro03Exec() throws Exception {
		logger.info("******************************BINBEIFPRO03处理开始***************************");
		// 设置batch处理标志
		int flg = CherryBatchConstants.BATCH_SUCCESS;
		Map<String, Object> resMap = new HashMap<String, Object>();
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put(CherryBatchConstants.BRANDINFOID, brandInfoId);
			// 所属组织
			// 登陆用户信息
			UserInfo userInfo = (UserInfo) session.get(CherryBatchConstants.SESSION_USERINFO);
			map.put(CherryBatchConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
			map.put("userID", userInfo.getBIN_UserID());
			flg = binbeifpro03BL.tran_batchCouProducts(map);
			if(flg == CherryBatchConstants.BATCH_SUCCESS && Boolean.parseBoolean(map.get("IsSendMQ").toString())) {
				// 备份产品下发数据/MQ下发
				Map<String, Object> flagMapMQ = binbeifpro03BL.tran_batchCntProductsMQSend(map);
				resMap.putAll(flagMapMQ);
				flg= ConvertUtil.getInt(resMap.get("flag"));
			}
			binbeifpro03BL.outMessage();
			binbeifpro03BL.tran_programEnd(map);
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
				this.addActionMessage("柜台产品下发处理正常终了");
				logger.info("******************************柜台产品下发处理正常终了***************************");
			} else if (flg == CherryBatchConstants.BATCH_WARNING) {
				this.addActionError("柜台产品下发处理警告终了");
				logger.info("******************************柜台产品下发处理警告终了***************************");
			} else if (flg == CherryBatchConstants.BATCH_ERROR) {
				this.addActionError("柜台产品下发处理异常终了");
				logger.info("******************************柜台产品下发处理异常终了***************************");
			}
		}
		return "DOBATCHRESULT";
	}
}
