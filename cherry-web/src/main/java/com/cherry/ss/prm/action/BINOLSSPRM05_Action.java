/*	
 * @(#)BINOLSSPRM05_Action.java     1.0 2010/11/25		
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
package com.cherry.ss.prm.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;

import com.cherry.ss.prm.bl.BINOLSSPRM05_BL;
import com.cherry.ss.prm.form.BINOLSSPRM05_Form;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM05_BL;
import com.cherry.cm.util.ConvertUtil;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 促销品分类查询Action
 * 
 *
 */
@SuppressWarnings("unchecked")
public class BINOLSSPRM05_Action extends BaseAction implements
ModelDriven<BINOLSSPRM05_Form>{
	
	private static final long serialVersionUID = 7424734062884568354L;

	@Resource
	private BINOLSSPRM05_BL binolssprm05_BL;
	
	/** 共通BL */
	@Resource
	private BINOLCM05_BL binolcm05_BL;
	
	//form
	private BINOLSSPRM05_Form form = new BINOLSSPRM05_Form();
	
	//促销品分类List
	
	private List prmTypeList;
	
	//所属品牌List
	private List brandInfoList;
	
	@Override
	public BINOLSSPRM05_Form getModel() {
		
		return form;
	}

	//促销品分类List
	public void setPrmTypeList(List prmTypeList) {
		this.prmTypeList = prmTypeList;
	}

	public List getPrmTypeList() {
		return prmTypeList;
	}
	
	//所属品牌List
	public void setBrandInfoList(List brandInfoList) {
		this.brandInfoList = brandInfoList;
	}

	public List getBrandInfoList() {
		return brandInfoList;
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
		//参数Map
		Map<String, Object> map = new HashMap<String, Object>();
		// 语言类型
		map.put(CherryConstants.SESSION_LANGUAGE, session
				.get(CherryConstants.SESSION_LANGUAGE));
		// 用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo
				.getBIN_OrganizationInfoID());
		// 总部用户
		if (CherryConstants.BRAND_INFO_ID_VALUE == userInfo.getBIN_BrandInfoID()) {
			// 取得所属品牌List
			brandInfoList = binolcm05_BL.getBrandInfoList(map);
		}
		return SUCCESS;
	}

	public String search()throws Exception{
		int count = 0;
		
		// 取得参数MAP
		Map<String, Object> searchMap = getSearchMap();
		// 取得促销产品分类总数
		count = binolssprm05_BL.getPrmTypeCount(searchMap);
		// 取得促销产品分类信息List 
		prmTypeList = binolssprm05_BL.searchPrmTypeList(searchMap);
		// form表单设置
		//显示记录
		form.setITotalDisplayRecords(count);
		//总记录
		form.setITotalRecords(count);
		
		// AJAX返回至dataTable结果页面
		return "BINOLSSPRM05_1";
	}

	private Map<String, Object> getSearchMap() {

		// 参数MAP
		Map<String, Object> map = new HashMap<String, Object>();
		// 用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// form参数设置到paramMap中
		ConvertUtil.setForm(form, map);
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo
				.getBIN_OrganizationInfoID());
		// 品牌ID
		String brandInfoId = form.getBrandInfoId();
		// 非总部用户登录
		if (CherryConstants.BRAND_INFO_ID_VALUE != userInfo
				.getBIN_BrandInfoID()) {
			brandInfoId = ConvertUtil.getString(userInfo.getBIN_BrandInfoID());
		}
		// 登陆用户所属品牌ID
		map.put(CherryConstants.BRANDINFOID, brandInfoId);
		// 用户ID
		map.put(CherryConstants.USERID, userInfo.getBIN_UserID());
		// 语言类型
		map.put(CherryConstants.SESSION_LANGUAGE, session.get(CherryConstants.SESSION_LANGUAGE));
		// 业务类型
		map.put(CherryConstants.BUSINESS_TYPE, "0");
		// 分类名称
		map.put("categoryName", form.getCategoryName());
		//分类代码
		map.put("categoryCode", form.getCategoryCode());
		// 有效区分
		map.put(CherryConstants.VALID_FLAG, form.getValidFlag());  
		
		return map;
	}
	
	/**
	 * 停用促销产品分类
	 * 
	 * @return
	 * @throws Exception
	 */
	public String operate()throws Exception{
		
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 登陆用户ID
		int userId = userInfo.getBIN_UserID();
		
		// 促销产品分类数组（有效区分+促销产品分类ID+更新日期+更新次数）
		String[] prmTypeInfos = form.getPrmTypeInfo();
		// 促销品分类有效区分
		String validFlag = null;
		if("1".equals(form.getOptFlag())){
			// 促销品有效
			validFlag = "1";
		}else{
			// 促销品停用
			validFlag = "0";
		}
		try {
			// 更新数据库
			binolssprm05_BL.tran_operatePrmType(prmTypeInfos,validFlag,userId);
		} catch (CherryException e) {
			this.addActionError(e.getErrMessage());
			return ERROR;
		}
		return null;
	}
	
}
