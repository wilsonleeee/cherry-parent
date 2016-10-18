/*
 * @(#)BINOLCM15_Action.java     1.0 2011/09/14
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
package com.cherry.cm.cmbussiness.action;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM15_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.ConvertUtil;

/**
 * 编号获取共通Action
 * 
 * @author lipc
 * 
 */
public class BINOLCM15_Action extends BaseAction {

	private static final long serialVersionUID = 395966372586167187L;

	@Resource
	private BINOLCM15_BL binOLCM15_BL;

	/** 类型 */
	private String type;

	/** 品牌ID */
	private int brandInfoId;
	
	private int index;
	
	private String barCode;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	public String getBarCode() {
		return barCode;
	}

	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}
	
	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public int getBrandInfoId() {
		return brandInfoId;
	}

	public void setBrandInfoId(int brandInfoId) {
		this.brandInfoId = brandInfoId;
	}

	/**
	 * AJAX 取得系统自增Code
	 * 
	 * @throws Exception
	 */
	public void querySeqCode() throws Exception {
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		if(brandInfoId == 0){
			brandInfoId = userInfo.getBIN_BrandInfoID();
		}
		String code = binOLCM15_BL.getSequenceId(userInfo
				.getBIN_OrganizationInfoID(), brandInfoId, type);
		// 响应JSON对象
		ConvertUtil.setResponseByAjax(response, code);
	}
	
	/**
	 * 验证虚拟促销品barCode
	 * @return
	 * @throws Exception
	 */
	public String validBarCode() throws Exception{
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		if(brandInfoId == 0){
			brandInfoId = userInfo.getBIN_BrandInfoID();
		}
		if(!CherryChecker.isNullOrEmpty(barCode, true)){
			String field = CherryConstants.BARCODE + "_" + index;
			if(barCode.length() > 13){
				// 促销产品条码长度验证
				this.addFieldError(field,getText("ECM00020", new String[] {getText("PSS00020"), "13"}));
				return CherryConstants.GLOBAL_ACCTION_RESULT;
			}else if (!CherryChecker.isPrmCode(barCode)) {
				// 促销产品条码英数验证
				this.addFieldError(field,getText("PSS00053",new String[] { getText("PSS00020") }));
				return CherryConstants.GLOBAL_ACCTION_RESULT;
			}else{
				// 促销产品条码重复验证
				Map<String, Object> map = new HashMap<String, Object>();
				map.put(CherryConstants.BRANDINFOID, brandInfoId);
				map.put(CherryConstants.BARCODE, barCode);
				int count = binOLCM15_BL.getBarCodeCount(map);
				if(count > 0){
					this.addFieldError(field, getText("ECM00067"));
					return CherryConstants.GLOBAL_ACCTION_RESULT;
				}
				return null;
			}
		}
		return null;
	}
}
