/*
 * @(#)BINBEIFEMP05_Action.java v1.0 2013-10-29
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
package com.cherry.ia.emp.action;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryBatchException;
import com.cherry.ia.emp.bl.BINBEIFEMP05_BL;

/**
 * 刷新老后台U盘绑定柜台数据Action
 * 
 * @author JiJW
 * @version 1.0 2013-10-29
 */
public class BINBEIFEMP05_Action extends BaseAction {

	private static final long serialVersionUID = -5541871221811024717L;
	
	private static Logger logger = LoggerFactory.getLogger(BINBEIFEMP05_Action.class.getName());
	
	@Resource(name="binBEIFEMP05_BL")
	private BINBEIFEMP05_BL binBEIFEMP05_BL;
	
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
	public String binbeifemp05Exec() throws Exception {
		logger.info("******************************BINBEIFEMP05处理开始***************************");
		// 设置batch处理标志
		int flg = CherryBatchConstants.BATCH_SUCCESS;
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			// 登陆用户信息
			UserInfo userInfo = (UserInfo) session.get(CherryBatchConstants.SESSION_USERINFO);
			// 所属组织
			map.put(CherryBatchConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
			map.put(CherryBatchConstants.BRANDINFOID, brandInfoId);
			flg = binBEIFEMP05_BL.tran_batchUDiskCounter(map);
			
		} catch (CherryBatchException cbx) {
			flg = CherryBatchConstants.BATCH_WARNING;
			logger.info("=============WARN MSG================");
			logger.info(cbx.getMessage(),cbx);
			logger.info("=====================================");
		} catch (Exception e) {
			flg = CherryBatchConstants.BATCH_ERROR;
			logger.error("=============ERROR MSG===============");
			logger.error(e.getMessage(),e);
			logger.error("=====================================");
		} finally {
			if (flg == CherryBatchConstants.BATCH_SUCCESS) {
				this.addActionMessage("刷新老后台U盘绑定柜台数据处理正常终了");
				logger.info("******************************刷新老后台U盘绑定柜台数据处理正常终了***************************");
			} else if (flg == CherryBatchConstants.BATCH_WARNING) {
				this.addActionError("刷新老后台U盘绑定柜台数据处理警告终了");
				logger.info("******************************刷新老后台U盘绑定柜台数据处理警告终了***************************");
			} else if (flg == CherryBatchConstants.BATCH_ERROR) {
				this.addActionError("刷新老后台U盘绑定柜台数据处理异常终了");
				logger.info("******************************刷新老后台U盘绑定柜台数据处理异常终了***************************");
			}
		}
		return "DOBATCHRESULT";
	}

}
