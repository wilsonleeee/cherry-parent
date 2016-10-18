/*		
 * @(#)BINOLPLUPM01_Action.java     1.0 2010/12/24		
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
package com.cherry.pl.upm.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;

import com.cherry.pl.upm.bl.BINOLPLUPM01_BL;
import com.cherry.pl.upm.form.BINOLPLUPM01_Form;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM05_BL;
import com.cherry.cm.util.ConvertUtil;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 用户信息查询Action
 * 
 *
 */
@SuppressWarnings("unchecked")
public class BINOLPLUPM01_Action extends BaseAction implements
ModelDriven<BINOLPLUPM01_Form>{

	private static final long serialVersionUID = 7052727878284081396L;
	@Resource
	private BINOLPLUPM01_BL binolplupm01_BL;
	
	@Resource
	private BINOLCM05_BL binOLCM05_BL;
	
	//form
	private BINOLPLUPM01_Form form = new BINOLPLUPM01_Form();
	
	//用户信息List
	private List userList;
	
	//品牌List
	private List brandInfoList ;
	
	//品牌名称 
	private String brandName;
	
	@Override
	public BINOLPLUPM01_Form getModel() {
		return form;
	}

	//用户信息List
	public void setUserList(List userList) {
		this.userList = userList;
	}
	public List getUserList() {
		return userList;
	}
	
	//品牌List
	public void setBrandInfoList(List brandInfoList) {
		this.brandInfoList = brandInfoList;
	}

	public List getBrandInfoList() {
		return brandInfoList;
	}

	//品牌名称 
	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}


	public String getBrandName() {
		return brandName;
	}


	/**
	 * <p>
	 * 画面初期显示
	 * </p>
	 * 
	 * 
	 * @param 无
	 * @return String 跳转页面
	 * 
	 */
	public String init() throws Exception {
		// 参数Map
		Map<String, Object> map = new HashMap<String, Object>();
		// 用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 语言类型
		map.put(CherryConstants.SESSION_LANGUAGE, session
				.get(CherryConstants.SESSION_LANGUAGE));
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo
				.getBIN_OrganizationInfoID());
		// 品牌ID
		int brandInfoId = userInfo.getBIN_BrandInfoID();
		map.put(CherryConstants.BRANDINFOID, brandInfoId);
		// 总部用户登录的时候
		if (CherryConstants.BRAND_INFO_ID_VALUE == brandInfoId) {
			// 取得所管辖的品牌List
			brandInfoList = binOLCM05_BL.getBrandInfoList(map);
			Map brandMap = new HashMap();
			// 品牌ID
			brandMap.put("brandInfoId", brandInfoId);
			// 品牌名称
			brandMap.put("brandName", getText("PPL00006"));
			if (null != brandInfoList && !brandInfoList.isEmpty()) {
				brandInfoList.add(0, brandMap);
			} else {
				brandInfoList = new ArrayList();
				brandInfoList.add(brandMap);
			}
		} else {
			// 取得品牌名称
			brandName = binOLCM05_BL.getBrandName(map);
			// 品牌ID
			form.setBrandInfoId(String.valueOf(brandInfoId));
		}
		return SUCCESS;
	}

	public String search()throws Exception{
		// 取得参数MAP
		Map<String, Object> searchMap = getSearchMap();
		// 取得用户总数
		int count = binolplupm01_BL.getUserCount(searchMap);
		//显示记录
		form.setITotalDisplayRecords(count);
		//总记录
		form.setITotalRecords(count);
		if(count != 0){
			// 取得用户信息List 
			userList = binolplupm01_BL.getUserList(searchMap);
		}
		// AJAX返回至dataTable结果页面
		return "BINOLPLUPM01_1";
	}
	
	/**
	 * 查询参数MAP取得
	 * 
	 * @param tableParamsDTO
	 */
	private Map<String, Object> getSearchMap() {
		// 参数MAP
		Map<String, Object> map = new HashMap<String, Object>();
		// 登入用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// form参数设置到paramMap中
		ConvertUtil.setForm(form, map);
		// 语言
		String language = (String)session.get(CherryConstants.SESSION_LANGUAGE);
		if(language != null) {
			map.put(CherryConstants.SESSION_LANGUAGE, language);
		}
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo
				.getBIN_OrganizationInfoID());
		// 所属品牌
		String brandInfoId = form.getBrandInfoId();
		// 非总部用户登录的时候
		if (CherryConstants.BRAND_INFO_ID_VALUE != userInfo
				.getBIN_BrandInfoID()) {
			brandInfoId = String.valueOf(userInfo.getBIN_BrandInfoID());
		}
		// 登陆用户所属品牌ID
		map.put(CherryConstants.BRANDINFOID, brandInfoId);
		// 用户账号
		if(form.getLoginName() != null && !"".equals(form.getLoginName())) {
			map.put("loginName", form.getLoginName());
		}
		// 员工代号
		if(form.getEmployeeCode() != null && !"".equals(form.getEmployeeCode())) {
			map.put("employeeCode", form.getEmployeeCode());
		}
		// 员工姓名
		if(form.getEmployeeName() != null && !"".equals(form.getEmployeeName())) {
			map.put("employeeName", form.getEmployeeName());
		}
		// 有效区分
		map.put(CherryConstants.VALID_FLAG, form.getValidFlag());
		
		return map;
	}
	
	/**
	 * 删除用户信息
	 * 
	 * @return
	 * @throws Exception
	 */
	public String operate()throws Exception{
	
		// 参数MAP
		Map<String, Object> map = new HashMap<String, Object>();
		// 登入用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 更新者
		map.put(CherryConstants.UPDATEDBY, userInfo.getLoginName());
		//用户ID
		map.put("userId", form.getUserId());
		//更新时间
		map.put("modifyTime", form.getModifyTime());
		//更新次数
		map.put("modifyCount", form.getModifyCount());
		// 用户信息有效区分
		String validFlag = null;
		if ("1".equals(form.getOptFlag())) {
			// 用户信息有效
			validFlag = "1";
		} else {
			// 用户信息无效
			validFlag = "0";
		}
		// 有效区分
		map.put("validFlag", validFlag);
		try {
			// 更新数据库
			binolplupm01_BL.tran_inValidUser(map);
		} catch (Exception e) {
			// 删除失败的场合
			if(e instanceof CherryException){
                CherryException temp = (CherryException)e;            
                this.addActionError(temp.getErrMessage());                
            }else{
                throw e;
            }    
		}
		
		return null;
	}
}
