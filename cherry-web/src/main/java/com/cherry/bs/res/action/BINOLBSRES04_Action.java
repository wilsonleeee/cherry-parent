/*  
 * @(#)BINOLBSRES04_Action.java     1.0 2011/05/31      
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
package com.cherry.bs.res.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.bs.res.form.BINOLBSRES04_Form;
import com.cherry.bs.res.interfaces.BINOLBSRES04_IF;
import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM00_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM05_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.Bean2Map;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.opensymphony.xwork2.ModelDriven;

public class BINOLBSRES04_Action extends BaseAction implements
		ModelDriven<BINOLBSRES04_Form> {

	private static final long serialVersionUID = -7304040943608147010L;

	private BINOLBSRES04_Form form = new BINOLBSRES04_Form();

	@Resource(name="binOLBSRES04_BL")
	private BINOLBSRES04_IF binOLBSRES04_BL;

	@Resource(name="binOLCM05_BL")
	private BINOLCM05_BL binOLCM05_BL;
	
	/** 共通BL */
	@Resource(name="binOLCM00_BL")
	private BINOLCM00_BL binolcm00BL;

	private List<Map<String, Object>> brandInfoList;
	
	/** 区域List */
	private List<Map<String, Object>> reginList;

	public List<Map<String, Object>> getBrandInfoList() {
		return brandInfoList;
	}

	public void setBrandInfoList(List<Map<String, Object>> brandInfoList) {
		this.brandInfoList = brandInfoList;
	}

	@SuppressWarnings("unchecked")
	public String init() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID,
				userInfo.getBIN_OrganizationInfoID());
		// 所属品牌
		if (userInfo.getBIN_BrandInfoID() == -9999) {
			brandInfoList = binOLCM05_BL.getBrandInfoList(map);
		} else {
			Map<String, Object> brandMap = new HashMap<String, Object>();
			// 品牌ID
			brandMap.put("brandInfoId", userInfo.getBIN_BrandInfoID());
			// 品牌名称
			brandMap.put("brandName", userInfo.getBrandName());
			if (null != brandInfoList && !brandInfoList.isEmpty()) {
				brandInfoList.add(0, brandMap);
			} else {
				brandInfoList = new ArrayList<Map<String, Object>>();
				brandInfoList.add(brandMap);
			}
			map.put("brandInfoId", userInfo.getBIN_BrandInfoID());
			// 取得区域List
			reginList = binolcm00BL.getReginList(map);
		}
		return SUCCESS;
	}

	@SuppressWarnings("unchecked")
	public String save() throws Exception {
		Map<String, Object> map = (Map<String, Object>) Bean2Map
				.toHashMap(form);
		
		
		// 剔除map中的空值
		map = CherryUtil.removeEmptyVal(map);

		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 组织ID
		map.put(CherryConstants.ORGANIZATIONINFOID,
				userInfo.getBIN_OrganizationInfoID());
		// 作成者
		map.put(CherryConstants.CREATEDBY, userInfo.getBIN_UserID());
		// 作成程序名
		map.put(CherryConstants.CREATEPGM, "BINOLBSRES04");
		// 作成日时
		map.put(CherryConstants.CREATE_TIME,
				CherryUtil.getSysDateTime(CherryConstants.DATE_PATTERN));

		binOLBSRES04_BL.tran_insertRes(map);
		// 处理成功
		this.addActionMessage(getText("ICM00002"));
		return CherryConstants.GLOBAL_ACCTION_RESULT_BODY;
	}

	public void validateSave() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("brandInfoId", form.getBrandInfoId());
		map.put("resellerCode", form.getResellerCode().trim());
		String count = binOLBSRES04_BL.getCount(map);
		if(!CherryChecker.isAlphanumeric(form.getResellerCode())){
			this.addFieldError("resellerCode", getText("ECM00031", new String[] { getText("PBS00091")}));
		}
		if (count.equals("1")) {
			this.addFieldError("resellerCode", getText("ECM00032", new String[] { getText("PBS00091") }));
		}
		int levelCode = ConvertUtil.getInt(form.getLevelCode());
		String parentCode = form.getParentResellerCode();
		if(levelCode > 1 && CherryChecker.isNullOrEmpty(parentCode, true)){
			this.addFieldError("parentResellerCode", getText("ECM00054", new String[] { getText("PBS00093")}));
		}
	}

	@Override
	public BINOLBSRES04_Form getModel() {
		return form;
	}

	public List<Map<String, Object>> getReginList() {
		return reginList;
	}

	public void setReginList(List<Map<String, Object>> reginList) {
		this.reginList = reginList;
	}

}