/*	
 * @(#)BINBEDRHAN01_Action.java     1.0 2011/08/26	
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
package com.cherry.dr.handler.action;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryBatchException;
import com.cherry.dr.handler.bl.BINBEDRHAN01_BL;

/**
 * 会员等级和化妆次数重算Action
 * 
 * @author WangCT
 * @version 1.0 2011/08/26	
 */
public class BINBEDRHAN01_Action extends BaseAction {

	private static final long serialVersionUID = -4525314871062308415L;
	
	private static Logger logger = LoggerFactory.getLogger(BINBEDRHAN01_Action.class.getName());
	
	/** 会员等级和化妆次数重算BL */
	@Resource
	private BINBEDRHAN01_BL binBEDRHAN01_BL;
	
	/**
	 * <p>
	 * 会员等级和化妆次数重算batch处理
	 * </p>
	 * 
	 * 
	 * @param 无
	 * @return String
	 * 
	 */
	public String binbedrhan01Exec() throws Exception {

		logger.info("******************************会员等级和化妆次数重算处理开始***************************");
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
			// 会员等级和化妆次数重算batch主处理
			flg = binBEDRHAN01_BL.tran_reCalcMemLevel(map);
		} catch (CherryBatchException cbx) {
			flg = CherryBatchConstants.BATCH_WARNING;
		} catch (Exception e) {
			flg = CherryBatchConstants.BATCH_ERROR;
		} finally {
			if (flg == CherryBatchConstants.BATCH_SUCCESS) {
				this.addActionMessage("会员等级和化妆次数重算BATCH处理正常终了");
				logger.info("******************************会员等级和化妆次数重算处理正常终了***************************");
			} else if (flg == CherryBatchConstants.BATCH_WARNING) {
				this.addActionError("会员等级和化妆次数重算BATCH处理警告终了");
				logger.info("******************************会员等级和化妆次数重算处理警告终了***************************");
			} else if (flg == CherryBatchConstants.BATCH_ERROR) {
				this.addActionError("会员等级和化妆次数重算BATCH处理异常终了");
				logger.info("******************************会员等级和化妆次数重算处理异常终了***************************");
			}
		}
		return "DOBATCHRESULT";
	}
	
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

}
