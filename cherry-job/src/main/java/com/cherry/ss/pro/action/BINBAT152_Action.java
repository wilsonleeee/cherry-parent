/*	
 * @(#)BINBAT152_Action.java     1.0 @2016-07-09		
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

package com.cherry.ss.pro.action;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryBatchException;
import com.cherry.ss.pro.bl.BINBAT152_BL;

/**
 * 补录产品入出库成本(标准接口)Action
 * 
 * @author chenkuan
 * 
 * @version 2016-07-09
 * 
 */
public class BINBAT152_Action extends BaseAction {
	private static final long serialVersionUID = -8319887739391732152L;
	private static Logger logger = LoggerFactory.getLogger(BINBAT152_Action.class.getName());

	/** 品牌Id */
	private String brandInfoId;

	/** 组织Id */
	private String organizationInfoId;
	
	/** 品牌Code **/
	private String brandCode;
	
	public String getBrandCode() {
		return brandCode;
	}

	public void setBrandCode(String brandCode) {
		this.brandCode = brandCode;
	}

	public String getOrganizationInfoId() {
		return organizationInfoId;
	}

	public void setOrganizationInfoId(String organizationInfoId) {
		this.organizationInfoId = organizationInfoId;
	}

	public String getBrandInfoId() {
		return brandInfoId;
	}

	public void setBrandInfoId(String brandInfoId) {
		this.brandInfoId = brandInfoId;
	}
	
	@Resource(name = "binbat152_BL")
	private BINBAT152_BL binbat152_BL;
	
	public String tran_binbat152() throws Exception {
		logger.info("******************************补录产品入出库成本(标准接口)处理开始***************************");
		// 设置batch处理标志
		int flg = CherryBatchConstants.BATCH_SUCCESS;
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			// 登陆用户信息
			UserInfo userInfo = (UserInfo) session
					.get(CherryBatchConstants.SESSION_USERINFO);
			// 所属组织
			map.put(CherryBatchConstants.ORGANIZATIONINFOID,
					userInfo.getBIN_OrganizationInfoID());
			// 品牌Id
			map.put(CherryBatchConstants.BRANDINFOID, brandInfoId);
			//品牌Code
			map.put(CherryBatchConstants.BRAND_CODE, brandCode);
			// Job运行履历表的运行方式
			map.put("RunType", "MT");

			flg = binbat152_BL.tran_binbat152(map);
		} catch (CherryBatchException cbx) {
			logger.error(cbx.getMessage(),cbx);
			flg = CherryBatchConstants.BATCH_WARNING;
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			flg = CherryBatchConstants.BATCH_ERROR;
		} finally {
			if (flg == CherryBatchConstants.BATCH_SUCCESS) {
				this.addActionMessage("补录产品入出库成本(标准接口)处理正常结束");
				logger.info("******************************补录产品入出库成本(标准接口)处理正常结束***************************");
			} else if (flg == CherryBatchConstants.BATCH_WARNING) {
				this.addActionError("补录产品入出库成本(标准接口)处理警告结束");
				logger.info("******************************补录产品入出库成本(标准接口)处理警告结束***************************");
			} else if (flg == CherryBatchConstants.BATCH_ERROR) {
				this.addActionError("补录产品入出库成本(标准接口)处理异常结束");
				logger.info("******************************补录产品入出库成本(标准接口)处理异常结束***************************");
			}
		}
		return "DOBATCHRESULT";
	}
}
