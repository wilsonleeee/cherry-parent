/*	
 * @(#)BINBAT124_Action.java     1.0 @2015-12-16		
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
package com.cherry.middledbout.stand.stockTaking.action;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryBatchException;
import com.cherry.middledbout.stand.stockTaking.bl.BINBAT124_BL;

/**
 * 
 * 柜台盘点单据导出(标准接口)Action
 * @author jijw
 *  * @version 1.0 2015.12.16
 *
 */
public class BINBAT124_Action extends BaseAction {

	private static final long serialVersionUID = 5774432446303271610L;
	
	private static Logger logger = LoggerFactory.getLogger(BINBAT124_Action.class.getName());
	
	/** 组织Id */
	private String organizationInfoId;
	
	/** 品牌ID */
	private int brandInfoId;
	
	/** 品牌CODE */
	private String brandCode;

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
	
	public String getBrandCode() {
		return brandCode;
	}

	public void setBrandCode(String brandCode) {
		this.brandCode = brandCode;
	}
	
	/**柜台盘点单据导出(标准接口)BL **/
	@Resource(name = "binBAT124_BL")
	private BINBAT124_BL binBAT124_BL;
	
	/**
	 * 
	 * @param 无
	 * @return String
	 * 
	 */
	public String binbat124Exec() throws Exception {
		logger.info("****************************** 柜台盘点单据导出(标准接口)开始 ***************************");
		// 设置batch处理标志
		int flg = CherryBatchConstants.BATCH_SUCCESS;
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			UserInfo userInfo = (UserInfo) session.get(CherryBatchConstants.SESSION_USERINFO);
			// 所属组织
			map.put(CherryBatchConstants.BRAND_CODE, brandCode);
			map.put(CherryBatchConstants.BRANDINFOID, brandInfoId);
			map.put(CherryBatchConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
			
			// Job运行履历表的运行方式
			map.put("RunType", "MT");
			
			flg = binBAT124_BL.tran_batchExec(map);
			
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
				this.addActionMessage("柜台盘点单据导出(标准接口)处理正常终了");
				logger.info("******************************柜台盘点单据导出(标准接口)处理正常终了***************************");
			} else if (flg == CherryBatchConstants.BATCH_WARNING) {
				this.addActionError("柜台盘点单据导出(标准接口)处理警告终了");
				logger.info("******************************柜台盘点单据导出(标准接口)处理警告终了***************************");
			} else if (flg == CherryBatchConstants.BATCH_ERROR) {
				this.addActionError("柜台盘点单据导出(标准接口)处理异常终了");
				logger.info("******************************柜台盘点单据导出(标准接口)处理异常终了***************************");
			}
		}
		return "DOBATCHRESULT";
	}
	

}
