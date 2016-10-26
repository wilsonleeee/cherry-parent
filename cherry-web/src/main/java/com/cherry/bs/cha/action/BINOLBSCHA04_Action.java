/*  
 * @(#)BINOLBSCHA04_Action.java     1.0 2011/05/31      
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
package com.cherry.bs.cha.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.bs.cha.form.BINOLBSCHA04_Form;
import com.cherry.bs.cha.interfaces.BINOLBSCHA04_IF;
import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM05_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.opensymphony.xwork2.ModelDriven;



public class BINOLBSCHA04_Action extends BaseAction implements ModelDriven<BINOLBSCHA04_Form> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7304040943608147010L;
	/** 组织添加画面Form */
	private BINOLBSCHA04_Form form = new BINOLBSCHA04_Form();
	
	/** 组织添加画面BL */
	@Resource
	private BINOLBSCHA04_IF binOLBSCHA04_IF;
	
	@Resource
	private BINOLCM05_BL binOLCM05_BL;
	
	private List<Map<String, Object>> brandInfoList;
	
	public List<Map<String, Object>> getBrandInfoList() {
		return brandInfoList;
	}

	public void setBrandInfoList(List<Map<String, Object>> brandInfoList) {
		this.brandInfoList = brandInfoList;
	}

	@SuppressWarnings("unchecked")
	public String init() throws Exception{
		Map<String, Object> map = new HashMap<String, Object>();
		UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID,userInfo.getBIN_OrganizationInfoID());
		// 所属品牌
		if (userInfo.getBIN_BrandInfoID() == -9999) {
			brandInfoList = binOLCM05_BL.getBrandInfoList(map);
//			Map<String, Object> brandMap = new HashMap<String, Object>();
//			// 品牌ID
//			brandMap.put("brandInfoId", CherryConstants.BRAND_INFO_ID_VALUE);
//			// 品牌名称
//			brandMap.put("brandName", getText("PPL00006"));
//			if (null != brandInfoList && !brandInfoList.isEmpty()) {
//				brandInfoList.add(0, brandMap);
//			} else {
//				brandInfoList = new ArrayList<Map<String, Object>>();
//				brandInfoList.add(brandMap);
//			}
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
		}		
		form.setEndDate(CherryUtil.getSysDateTime(CherryConstants.DATE_PATTERN));
		return SUCCESS;
	}
	
	public String save() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
		// 组织ID
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo
				.getBIN_OrganizationInfoID());
		// 作成者
		map.put(CherryConstants.CREATEDBY, userInfo.getBIN_UserID());
		// 作成程序名
		map.put(CherryConstants.CREATEPGM, "BINOLBSCHA04");	
		// 作成日时
		map.put(CherryConstants.CREATE_TIME,CherryUtil
				.getSysDateTime(CherryConstants.DATE_PATTERN));
		map.put("brandInfoId", form.getBrandInfoId());
		
		map.put("channelCode", form.getChannelCode().trim());

		map.put("channelName", form.getChannelName().trim());

		map.put("channelNameForeign", form.getChannelNameForeign());

		map.put("joinDate", form.getJoinDate());

		map.put("status", form.getStatus());
		
		binOLBSCHA04_IF.tran_insertChannel(map);
		//处理成功
		this.addActionMessage(getText("ICM00002"));
		return CherryConstants.GLOBAL_ACCTION_RESULT_BODY;
	}
	
	public void validateSave() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("brandInfoId", form.getBrandInfoId());
		if(!ConvertUtil.isBlank(form.getChannelCode())){
			map.put("channelCode", form.getChannelCode().trim());
			String count=binOLBSCHA04_IF.getCount(map);
			if(count.equals("1")) {
				this.addFieldError("channelCode",getText("ECM00032",new String[]{getText("PBS00102"),"20"}));
			}
			map.remove("channelCode");
		}
		
		if(!ConvertUtil.isBlank(form.getChannelName())){
			map.put("channelName", form.getChannelName().trim());
			String count=binOLBSCHA04_IF.getCount(map);
			if(count.equals("1")) {
				this.addFieldError("channelName",getText("ECM00032",new String[]{getText("PBS00050"),"20"}));
			}
			map.remove("channelName");
		}
	} 
	
	@Override
	public BINOLBSCHA04_Form getModel() {
		// TODO Auto-generated method stub
		return form;
	}
	
}