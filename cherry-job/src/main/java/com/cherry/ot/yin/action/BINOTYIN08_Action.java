/*	
 * @(#)BINOTYIN01_Action.java     1.0 @2013-3-18		
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
package com.cherry.ot.yin.action;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryBatchException;
import com.cherry.ot.yin.bl.BINOTYIN08_BL;

/**
 *
 * 颖通接口：发货单退库单导入Action
 *
 * @author jijw
 *
 * @version  2013-3-18
 */
public class BINOTYIN08_Action extends BaseAction {

	private static final long serialVersionUID = 3406216484155880678L;

	private static Logger logger = LoggerFactory.getLogger(BINOTYIN08_Action.class.getName());
	
	@Resource(name="binOTYIN08_BL")
	private BINOTYIN08_BL binOTYIN08_BL;
	
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
	public String binotyin08Exec() throws Exception {
		logger.info("******************************颖通发货单退库单导入处理开始***************************");
		// 设置batch处理标志
		int flg = CherryBatchConstants.BATCH_SUCCESS;
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			UserInfo userInfo = (UserInfo) session
					.get(CherryBatchConstants.SESSION_USERINFO);
			// 所属组织
			map.put(CherryBatchConstants.BRANDINFOID, brandInfoId);
			map.put(CherryBatchConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
			flg = binOTYIN08_BL.tran_batchOTYIN08(map);
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
		} finally {
			if (flg == CherryBatchConstants.BATCH_SUCCESS) {
				this.addActionMessage("颖通发货单退库单导入处理正常终了");
				logger.info("******************************颖通发货单退库单导入处理正常终了***************************");
			} else if (flg == CherryBatchConstants.BATCH_WARNING) {
				this.addActionError("颖通发货单退库单导入处理警告终了");
				logger.info("******************************颖通发货单退库单导入处理警告终了***************************");
			} else if (flg == CherryBatchConstants.BATCH_ERROR) {
				this.addActionError("颖通发货单退库单导入处理异常终了");
				logger.info("******************************颖通发货单退库单导入处理异常终了***************************");
			}
		}
		return "DOBATCHRESULT";
	}

}
