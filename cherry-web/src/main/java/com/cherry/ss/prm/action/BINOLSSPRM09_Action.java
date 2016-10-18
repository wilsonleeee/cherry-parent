/*		
 * @(#)BINOLSSPRM09_Action.java     1.0 2010/11/25		
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

import com.cherry.ss.prm.bl.BINOLSSPRM09_BL;
import com.cherry.ss.prm.bl.BINOLSSPRM10_BL;
import com.cherry.ss.prm.form.BINOLSSPRM09_Form;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM05_BL;
import com.cherry.cm.util.ConvertUtil;
import com.googlecode.jsonplugin.JSONUtil;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 促销品类别查询Action
 * 
 *
 */
@SuppressWarnings("unchecked")
public class BINOLSSPRM09_Action extends BaseAction implements
ModelDriven<BINOLSSPRM09_Form>{

	private static final long serialVersionUID = 1L;
	
	@Resource
	private BINOLSSPRM09_BL binolssprm09_BL;
	
	/** 共通BL */
	@Resource
	private BINOLCM05_BL binolcm05_BL;
	
	/** 添加类别画面BL */
	@Resource
	private BINOLSSPRM10_BL binolssprm10_BL;
	
	//form
	private BINOLSSPRM09_Form form = new BINOLSSPRM09_Form();
	
	//促销品类别List
	private List prmCategoryList;
	
	//所属品牌List
	private List brandInfoList;
	
	//上级促销品类别List
	private List higherCategoryList;

	@Override
	public BINOLSSPRM09_Form getModel() {
		
		return form;
	}

	//促销品类别List
	public void setPrmCategoryList(List prmCategoryList) {
		this.prmCategoryList = prmCategoryList;
	}

	public List getPrmCategoryList() {
		return prmCategoryList;
	}

	//所属品牌List
	public void setBrandInfoList(List brandInfoList) {
		this.brandInfoList = brandInfoList;
	}

	public List getBrandInfoList() {
		return brandInfoList;
	}

	//上级促销品类别List
	public void setHigherCategoryList(List higherCategoryList) {
		this.higherCategoryList = higherCategoryList;
	}

	public List getHigherCategoryList() {
		return higherCategoryList;
	}

	/**
	 * <p>
	 *促销品类别 一览（列表模式）画面初期处理
	 * </p>
	 * 
	 * 
	 * @param 无
	 * @return String 促销品类别一览画面 
	 * 
	 */
	public String init() throws Exception {
		
		return SUCCESS;
	}

	/**
	 * 促销品类别一览（树模式）画面初期处理
	 * @param 无
	 * @return 促销品类别一览画面 
	 */
	public String treeInit() {
		
		return SUCCESS;
	}
	
	
	/**
	 * AJAX取得某一类别的直属下级类别
	 * 
	 * @return 类别一览画面 
	 */
	public String next() throws Exception {
		
		Map<String, Object> map = new HashMap<String, Object>();
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 语言
		String language = (String)session.get(CherryConstants.SESSION_LANGUAGE);
		if(language != null) {
			map.put(CherryConstants.SESSION_LANGUAGE, language);
		}
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		// 不是总部的场合
		if(userInfo.getBIN_BrandInfoID() != CherryConstants.BRAND_INFO_ID_VALUE) {
			// 所属品牌
			map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		}
		// 类别节点位置
		map.put(CherryConstants.PATH, form.getPath());
		// 取得某一类别的直属下级类别
		String categoryTree = binolssprm09_BL.getNextCategoryList(map);
		
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(categoryTree);
		
		return null;
	}
	
	/**
	 * 类别一览（列表模式）画面初期处理
	 * 
	 * @return 类别一览画面 
	 */
	public String listInit() {
		//参数Map
		Map<String, Object> map = new HashMap<String, Object>();
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
		}else{
			map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		}
		// 语言类型
		map.put(CherryConstants.SESSION_LANGUAGE, session.get(CherryConstants.SESSION_LANGUAGE));
		//取得上级促销品类别List
		higherCategoryList = binolssprm09_BL.getHigherCategoryList(map);
		
		return SUCCESS;
	}
	
	/**
	 * 取得品牌ID下的上级类别List
	 * 
	 */
	public String filterByBrandInfo() throws Exception {
		
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 语言
		String language = (String)session.get(CherryConstants.SESSION_LANGUAGE);
		if(language != null) {
			map.put(CherryConstants.SESSION_LANGUAGE, language);
		}
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		// 所属品牌不存在的场合
		if(form.getBrandInfoId() == null || "".equals(form.getBrandInfoId())) {
			// 不是总部的场合
			if(userInfo.getBIN_BrandInfoID() != CherryConstants.BRAND_INFO_ID_VALUE) {
				// 所属品牌
				map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
			}
		} else {
			map.put(CherryConstants.BRANDINFOID, form.getBrandInfoId());
		}
		
		//  取得上级类别信息List
		resultMap.put("higherCategoryList", binolssprm10_BL.getHigherCategoryList(map));
		
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(JSONUtil.serialize(resultMap));
		return null;
	}
	
	/**
	 * 类别一览（列表模式）
	 * 
	 * @return 类别一览画面 
	 */
	public String categoryList() {
		
		Map<String, Object> map = new HashMap<String, Object>();
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 语言
		String language = (String)session.get(CherryConstants.SESSION_LANGUAGE);
		if(language != null) {
			map.put(CherryConstants.SESSION_LANGUAGE, language);
		}
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		// 所属品牌不存在的场合
		if(form.getBrandInfoId() == null || "".equals(form.getBrandInfoId())) {
			// 不是总部的场合
			if(userInfo.getBIN_BrandInfoID() != CherryConstants.BRAND_INFO_ID_VALUE) {
				// 所属品牌
				map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
			}
		} else {
			map.put(CherryConstants.BRANDINFOID, form.getBrandInfoId());
		}
		// 语言类型
		map.put(CherryConstants.SESSION_LANGUAGE, session.get(CherryConstants.SESSION_LANGUAGE));
		if (form.getItemClassName() != null && !"".equals(form.getItemClassName())){
			// 促销品类别名称
			map.put("itemClassName", form.getItemClassName());
		}
		if (form.getItemClassCode() != null && !"".equals(form.getItemClassCode())){
			//促销品类别码
			map.put("itemClassCode", form.getItemClassCode());
		}
		if (form.getCurClassCode() != null && !"".equals(form.getCurClassCode())){
			// 促销品类别特征码
			map.put("curClassCode", form.getCurClassCode());
		}
		if(form.getPath() != null && !"".equals(form.getPath())) {
			// 上级类别节点位置
			map.put("path", form.getPath());
		}
		if(form.getValidFlag() != null && !"".equals(form.getValidFlag())) {
			// 有效区分
			map.put(CherryConstants.VALID_FLAG, form.getValidFlag()); 
		}
		// dataTable上传的参数设置到map
		ConvertUtil.setForm(form, map);
		int count = binolssprm09_BL.getPrmCateCount(map);
		form.setITotalDisplayRecords(count);
		form.setITotalRecords(count);
		if(count != 0) {
			// 取得促销产品信息List 
			prmCategoryList = binolssprm09_BL.searchPrmCategoryList(map);
		}
		return SUCCESS;
	}
	
	
	/**
	 * 停用促销产品类别
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
		
		// 促销产品数组（有效区分+促销产品类别ID+更新日期+更新次数）
		String[] prmCategoryInfos = form.getPrmCategoryInfo();
		// 促销品有效区分
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
			binolssprm09_BL.tran_operatePrmCategory(prmCategoryInfos,validFlag,userId);
		} catch (CherryException e) {
			this.addActionError(e.getErrMessage());
			return ERROR;
		}
		return null;
	}
	
}
