/*  
 * @(#)BINOLMOMAN06_Action.java    1.0 2011-7-28     
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

package com.cherry.mo.man.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM05_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.mo.man.form.BINOLMOMAN06_Form;
import com.cherry.mo.man.interfaces.BINOLMOMAN06_IF;
import com.googlecode.jsonplugin.JSONUtil;
import com.opensymphony.xwork2.ModelDriven;

@SuppressWarnings("unchecked")
public class BINOLMOMAN06_Action extends BaseAction implements
		ModelDriven<BINOLMOMAN06_Form> {

	private static final long serialVersionUID = 1L;

	private BINOLMOMAN06_Form form = new BINOLMOMAN06_Form();
	
	private List<Map<String, Object>> brandInfoList;
	
	private List<Map<String,Object>> udiskList;
	
	private int count = 0;
	
	@Resource
	private BINOLCM05_BL binOLCM05_BL;
	@Resource
	private BINOLMOMAN06_IF binOLMOMAN06_BL;
	
	public String init(){
		Map<String, Object> map = new HashMap<String, Object>();
		// 用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 用户ID
		map.put(CherryConstants.USERID, userInfo.getBIN_UserID());
		// 语言类型
        String language = (String) session
                .get(CherryConstants.SESSION_LANGUAGE);
        // 语言类型
        map.put(CherryConstants.SESSION_LANGUAGE, language);
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo
				.getBIN_OrganizationInfoID());
		// 所属品牌
		map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		if (userInfo.getBIN_BrandInfoID() == -9999) {
			brandInfoList = binOLCM05_BL.getBrandInfoList(map);
		} else {
			Map<String, Object> brandMap = new HashMap<String, Object>();
			// 品牌ID
			brandMap.put("brandInfoId", userInfo.getBIN_BrandInfoID());
			// 品牌名称
			brandMap.put("brandName", userInfo.getBrandName());
			brandInfoList = new ArrayList<Map<String, Object>>();
			brandInfoList.add(brandMap);
		}
		return SUCCESS;
	}
	
	public String search() throws Exception{
		Map<String, Object> map = new HashMap<String, Object>();
		// 用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 用户ID
		map.put(CherryConstants.USERID, userInfo.getBIN_UserID());
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo
				.getBIN_OrganizationInfoID());
		// 所属品牌
		map.put(CherryConstants.BRANDINFOID, form.getBrandInfoId());
		map.put("udiskSn", form.getUdiskSn());
		map.put("employeeName", form.getEmployeeName());
		map.put("employeeCode", form.getEmployeeCode());
		map.put("ownerRight", form.getOwnerRight());
		// form参数设置到map中
		ConvertUtil.setForm(form, map);
		udiskList = binOLMOMAN06_BL.getUdiskList(map);
		count = binOLMOMAN06_BL.getUdiskCount(map);
		// form表单设置
		form.setITotalDisplayRecords(count);
		form.setITotalRecords(count);
		return "BINOLMOMAN06_1";
	}
	
	public String employeeBind() throws Exception{
		try{
			Map<String, Object> map = new HashMap<String, Object>();
			// 用户信息
			UserInfo userInfo = (UserInfo) session
					.get(CherryConstants.SESSION_USERINFO);
			// 用户ID
			map.put(CherryConstants.USERID, userInfo.getBIN_UserID());
			// 所属组织
			map.put(CherryConstants.ORGANIZATIONINFOID, userInfo
					.getBIN_OrganizationInfoID());
			// 所属品牌
			map.put(CherryConstants.BRANDINFOID, form.getBrandInfoId());
			map.put("udiskInfoId", form.getUdiskInfoId());
			map.put("employeeCode", form.getEmployeeCode());
			map.put(CherryConstants.UPDATEDBY, userInfo.getBIN_UserID());
			map.put(CherryConstants.UPDATEPGM, "BINOLMOMAN06");
			binOLMOMAN06_BL.tran_employeeBind(map);
			this.addActionMessage(getText("ICM00002"));  
			return CherryConstants.GLOBAL_ACCTION_RESULT;
		}catch(Exception ex){
			if(ex instanceof CherryException){
                CherryException temp = (CherryException)ex;
                this.addActionError(temp.getErrMessage());
                return CherryConstants.GLOBAL_ACCTION_RESULT;
            }else{
            	this.addActionError(ex.getMessage());
                return CherryConstants.GLOBAL_ACCTION_RESULT;
            }
		}
	}
	
	public String employeeUnbind() throws Exception{
		try{
			Map<String, Object> map = new HashMap<String, Object>();
			// 用户信息
			UserInfo userInfo = (UserInfo) session
					.get(CherryConstants.SESSION_USERINFO);
			// 用户ID
			map.put(CherryConstants.USERID, userInfo.getBIN_UserID());
			// 所属组织
			map.put(CherryConstants.ORGANIZATIONINFOID, userInfo
					.getBIN_OrganizationInfoID());
			// 所属品牌
			map.put(CherryConstants.BRANDINFOID, form.getBrandInfoId());
			map.put(CherryConstants.UPDATEDBY, userInfo.getBIN_UserID());
			map.put(CherryConstants.UPDATEPGM, "BINOLMOMAN06");
			String udiskIdStr = form.getUdiskIdStr();
			List<Map<String, Object>> list = (List<Map<String, Object>>) JSONUtil.deserialize(udiskIdStr);
			binOLMOMAN06_BL.tran_employeeUnbind(list, map);
			this.addActionMessage(getText("ICM00002"));  
			return CherryConstants.GLOBAL_ACCTION_RESULT;
		}catch(Exception ex){
			if(ex instanceof CherryException){
                CherryException temp = (CherryException)ex;
                this.addActionError(temp.getErrMessage());
                return CherryConstants.GLOBAL_ACCTION_RESULT;
            }else{
            	this.addActionError(ex.getMessage());
                return CherryConstants.GLOBAL_ACCTION_RESULT;
            }
		}
	}
	
	public String deleteUdisk() throws Exception{
		try{
			Map<String, Object> map = new HashMap<String, Object>();
			// 用户信息
			UserInfo userInfo = (UserInfo) session
					.get(CherryConstants.SESSION_USERINFO);
			// 用户ID
			map.put(CherryConstants.USERID, userInfo.getBIN_UserID());
			// 所属组织
			map.put(CherryConstants.ORGANIZATIONINFOID, userInfo
					.getBIN_OrganizationInfoID());
			// 所属品牌
			map.put(CherryConstants.BRANDINFOID, form.getBrandInfoId());
			map.put(CherryConstants.UPDATEDBY, userInfo.getBIN_UserID());
			map.put(CherryConstants.UPDATEPGM, "BINOLMOMAN06");
			String udiskIdStr = form.getUdiskIdStr();
			List<Map<String, Object>> list = (List<Map<String, Object>>) JSONUtil.deserialize(udiskIdStr);
			binOLMOMAN06_BL.tran_deleteUdisk(list, map);
			this.addActionMessage(getText("ICM00002"));  
			return CherryConstants.GLOBAL_ACCTION_RESULT;
		}catch(Exception ex){
			if(ex instanceof CherryException){
                CherryException temp = (CherryException)ex;
                this.addActionError(temp.getErrMessage());
                return CherryConstants.GLOBAL_ACCTION_RESULT;
            }else{
            	this.addActionError(ex.getMessage());
                return CherryConstants.GLOBAL_ACCTION_RESULT;
            }
		}
	}
	
	@Override
	public BINOLMOMAN06_Form getModel() {
		// TODO Auto-generated method stub
		return form;
	}
	public List<Map<String, Object>> getBrandInfoList() {
		return brandInfoList;
	}
	public void setBrandInfoList(List<Map<String, Object>> brandInfoList) {
		this.brandInfoList = brandInfoList;
	}

	public List<Map<String, Object>> getUdiskList() {
		return udiskList;
	}

	public void setUdiskList(List<Map<String, Object>> udiskList) {
		this.udiskList = udiskList;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
	
}
