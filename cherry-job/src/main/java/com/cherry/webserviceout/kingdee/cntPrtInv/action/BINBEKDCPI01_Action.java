/*	
 * @(#)BINBEKDCPI01_Action.java     1.0 @2015-5-13		
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
package com.cherry.webserviceout.kingdee.cntPrtInv.action;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryBatchException;
import com.cherry.webserviceout.kingdee.cntPrtInv.bl.BINBEKDCPI01_BL;

/**
 *
 * Kingdee接口：柜台产品库存更新Action
 *
 * @author jijw
 *
 * @version  2015-5-13
 */
public class BINBEKDCPI01_Action extends BaseAction {

	private static final long serialVersionUID = -5818624354784540939L;
	
	private static Logger logger = LoggerFactory.getLogger(BINBEKDCPI01_Action.class.getName());
	
	@Resource(name="binbekdcpi01_BL")
	private BINBEKDCPI01_BL binbekdcpi01_BL;
	
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
	 * 
	 * @param 无
	 * @return String
	 * 
	 */
	public String binbekdcpi01Exec() throws Exception {
		logger.info("******************************Kingdee柜台产品库存更新Batch处理开始***************************");
		// 设置batch处理标志
		int flg = CherryBatchConstants.BATCH_SUCCESS;
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			UserInfo userInfo = (UserInfo) session.get(CherryBatchConstants.SESSION_USERINFO);
			// 所属组织
			map.put(CherryBatchConstants.BRANDINFOID, brandInfoId);
			map.put(CherryBatchConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
			
			// Job运行履历表的运行方式
			map.put("RunType", "MT");
			
			flg = binbekdcpi01_BL.tran_batchkdcpi01(map);
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
				this.addActionMessage("Kingdee柜台产品库存更新Batch处理正常终了");
				logger.info("******************************Kingdee柜台产品库存更新Batch处理正常终了***************************");
			} else if (flg == CherryBatchConstants.BATCH_WARNING) {
				this.addActionError("Kingdee柜台产品库存更新处理Batch警告终了");
				logger.info("******************************Kingdee柜台产品库存更新Batch处理警告终了***************************");
			} else if (flg == CherryBatchConstants.BATCH_ERROR) {
				this.addActionError("Kingdee柜台产品库存更新Batch处理异常终了");
				logger.info("******************************Kingdee柜台产品库存更新Batch处理异常终了***************************");
			}
		}
		return "DOBATCHRESULT";
	}
}
