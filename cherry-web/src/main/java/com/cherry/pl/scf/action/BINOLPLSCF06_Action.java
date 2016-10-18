/*
 * @(#)BINOLPLSCF06_Action.java     1.0 2011/3/25
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

package com.cherry.pl.scf.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CodeTable;
import com.cherry.cm.util.Bean2Map;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.pl.scf.bl.BINOLPLSCF06_BL;
import com.cherry.pl.scf.form.BINOLPLSCF06_Form;
import com.opensymphony.xwork2.ModelDriven;

/**
 * code值管理一览Action
 * 
 * @author zhangjie
 * @version 1.0 2011.3.25
 */
@SuppressWarnings("unchecked")
public class BINOLPLSCF06_Action extends BaseAction implements
		ModelDriven<BINOLPLSCF06_Form> {

	private static final long serialVersionUID = -9089605010044324018L;
	
	protected static final Logger logger = LoggerFactory.getLogger(BINOLPLSCF06_Action.class);

	/** code值管理一览BL */
	@Resource
	private BINOLPLSCF06_BL binOLPLSCF06_BL;

	/** code值管理一览Form */
	private BINOLPLSCF06_Form form = new BINOLPLSCF06_Form();
	
	@Resource
	private CodeTable codeTable;

	/**
	 * code值管理一览初期表示
	 * 
	 * @return code值管理一览画面
	 */
	public String init() {
		// 初期化组织品牌下拉框
//		OrgBrandInit();
		return SUCCESS;
	}
	
	/**
	 * 初期化组织品牌下拉框
	 * 
	 * @return null
	 */
	public void OrgBrandInit() {
		// 用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 组织Code
		String orgCode = userInfo.getOrganizationInfoCode();
		Map<String, Object> defaultMap = new HashMap<String, Object>();
		 // 默认品牌Code
		defaultMap.put("brandCode", String.valueOf(CherryConstants.Brand_CODE_ALL));
		 // 默认品牌名称
		defaultMap.put("brandName", getText("PPL00030"));
		Map<String, Object> orgMap = new HashMap<String, Object>();
		// 超级管理员登录的时候
		if (String.valueOf(CherryConstants.ORG_CODE_ALL).equals(orgCode)) {
			// 取得所有组织List
			orgInfoList = binOLPLSCF06_BL.getOrgInfoList();
			// 默认组织Code
			orgMap.put("orgCode", String.valueOf(CherryConstants.ORG_CODE_ALL));
			// 默认组织名称
			orgMap.put("orgName", getText("PPL00029"));
			if (null != orgInfoList && !orgInfoList.isEmpty()) {
				orgInfoList.add(0, orgMap);
			} else {
				orgInfoList = new ArrayList<Map<String, Object>>();
				orgInfoList.add(orgMap);
			}
			brandInfoList = new ArrayList<Map<String, Object>>();
			brandInfoList.add(defaultMap);
		} else {
			// 组织Code
			orgMap.put("orgCode", orgCode);
			// 组织名称
			orgMap.put("orgName", userInfo.getOrgName());
			orgInfoList = new ArrayList<Map<String, Object>>();
			orgInfoList.add(orgMap);
			// 品牌Code
			String brandCode = userInfo.getBrandCode();
			// 总部用户登录的时候
			if (String.valueOf(CherryConstants.Brand_CODE_ALL).equals(brandCode)) {
				// 查询品牌CodeList
				brandInfoList = binOLPLSCF06_BL.getBrandCodeList(orgMap);
				if (null != brandInfoList && !brandInfoList.isEmpty()) {
					brandInfoList.add(0, defaultMap);				
				} else {
					brandInfoList = new ArrayList<Map<String, Object>>();
					brandInfoList.add(defaultMap);				
				}
			} else {
				// 品牌Code
				defaultMap.put("brandCode", userInfo.getBrandCode());
				 // 品牌名称
				defaultMap.put("brandName", userInfo.getBrandName());
				brandInfoList = new ArrayList<Map<String, Object>>();
				brandInfoList.add(defaultMap);
			}
		}
	}

	/**
	 * 根据组织查询品牌List
	 * 
	 * @return null
	 */
	public String searchBrand() throws Exception {
		
		Map<String, Object> map = new HashMap<String, Object>();
		// 所属组织
		map.put("orgCode", form.getOrgCode());
		// 查询品牌CodeList
		List<Map<String, Object>>list = binOLPLSCF06_BL.getBrandCodeList(map);
		Map<String, Object> defaultMap = new HashMap<String, Object>();
		 // 默认品牌ID
		defaultMap.put("brandCode", String.valueOf(CherryConstants.Brand_CODE_ALL));
		 // 默认品牌名称
		defaultMap.put("brandName", getText("PPL00030"));
		if (null != list && !list.isEmpty()) {
			list.add(0, defaultMap);				
		} else {
			list = new ArrayList<Map<String, Object>>();
			list.add(defaultMap);				
		}
		ConvertUtil.setResponseByAjax(response, list);
		return null;
	}
	
	/**
	 * 取得code值管理表一览
	 * 
	 * @return code值管理表一览画面
	 */
	public String codeList() {

		Map<String, Object> map = new HashMap<String, Object>();
		// 语言
		String language = (String) session
				.get(CherryConstants.SESSION_LANGUAGE);
		if (language != null) {
			map.put(CherryConstants.SESSION_LANGUAGE, language);
		}
		UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
		
		// 组织Code
		map.put("orgCode", userInfo.getOrganizationInfoCode());
		// 品牌Code
		map.put("brandCode", userInfo.getBrandCode());
		// Code类别
		if (form.getCodeType() != null && !"".equals(form.getCodeType())) {
			map.put("codeType", form.getCodeType());
		}
		// CodeName
		if (form.getCodeName() != null && !"".equals(form.getCodeName())) {
			map.put("codeName", form.getCodeName());
		}

		// dataTable上传的参数设置到map
		ConvertUtil.setForm(form, map);

		// 查询审核审批配置信息总数
		int count = binOLPLSCF06_BL.getCodeMCount(map);
//		
//		//该账号对应的组织和品牌未设定code值,则取默认的
//		if(count == 0){
//			map.put("orgCode", CherryConstants.ORG_CODE_ALL);
//			map.put("brandCode", CherryConstants.Brand_CODE_ALL);
//			count = binOLPLSCF06_BL.getCodeMCount(map);
//			codeList = binOLPLSCF06_BL.getCodeMList(map);
//		}
//		// 查询审核审批配置信息List
		codeList = binOLPLSCF06_BL.getCodeMList(map);
		
		form.setITotalDisplayRecords(count);
		form.setITotalRecords(count);
		return SUCCESS;
	}

	/**
	 * code值管理添加一览初期表示
	 * 
	 * @return code值管理添加一览画面
	 */
	public String addInit() {
		// 初期化组织品牌下拉框
		OrgBrandInit();
		return SUCCESS;
	}

	/**
	 * <p>
	 * 保存
	 * </p>
	 * 
	 * 
	 * @param 无
	 * @return String 跳转页面
	 * @throws Exception
	 * 
	 */
	public String saveCodeM() throws Exception {
		// 参数MAP取得
		Map<String, Object> map = getParamsMap();
		// 验证失败
		// if (!validateSave(map)) {
		// return CherryConstants.GLOBAL_ACCTION_RESULT;
		// }
		// 添加code管理值到数据库
		binOLPLSCF06_BL.tran_insertCodeM(map);
		// 处理成功
		this.addActionMessage(getText("ICM00002"));
		return CherryConstants.GLOBAL_ACCTION_RESULT_BODY;
	}

	/**
	 * 参数MAP取得
	 * 
	 * @throws Exception
	 * 
	 */
	private Map<String, Object> getParamsMap() throws Exception {
		// form参数放入Map中
		Map<String, Object> map = (Map<String, Object>) Bean2Map
				.toHashMap(form);
		// 循环map剔除value值前后空格
		 for(Map.Entry<String, Object> en:map.entrySet()){
			 	Object value = en.getValue();
			 	if (value instanceof String) {
			 			value = ((String) value).trim();
			 			en.setValue(value);
			 			//map.put(key, value);
			 	}
			}
		 /*
		Set<String> set = map.keySet();
		Iterator<String> it = set.iterator();
		while (it.hasNext()) {
			String key = it.next();
			Object value = map.get(key);
			if (value instanceof String) {
				value = ((String) value).trim();
				map.put(key, value);
			}
		}
		*/
		// 用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 作成者
		map.put(CherryConstants.CREATEDBY, userInfo.getBIN_UserID());
		// 更新者
		map.put(CherryConstants.UPDATEDBY, userInfo.getBIN_UserID());

		return map;
	}
	
	/**
	 * AJAX 取得小分类并返回JSON对象
	 * 
	 * @throws Exception
	 */
	public void refreshCodes() throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>(); 
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			// 取得小分类List
			List codesList = binOLPLSCF06_BL.getCodeList(map);
			codeTable.setCodesMap(codesList);
			// 刷新系统Code值正常结束
			resultMap.put("result", getText("EPL00018"));
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			// 刷新系统Code值异常结束
			resultMap.put("result", getText("EPL00019"));
		}
		// 响应JSON对象
		ConvertUtil.setResponseByAjax(response, resultMap);
	}

	/** 组织List */
	private List<Map<String, Object>> orgInfoList;

	/** 品牌List */
	private List<Map<String, Object>> brandInfoList;

	public List<Map<String, Object>> getOrgInfoList() {
		return orgInfoList;
	}

	public void setOrgInfoList(List<Map<String, Object>> orgInfoList) {
		this.orgInfoList = orgInfoList;
	}

	/** codeMList */
	private List<Map<String, Object>> codeList;

	public List<Map<String, Object>> getCodeList() {
		return codeList;
	}

	public void setCodeList(List<Map<String, Object>> codeList) {
		this.codeList = codeList;
	}

	public List<Map<String, Object>> getBrandInfoList() {
		return brandInfoList;
	}

	public void setBrandInfoList(List<Map<String, Object>> brandInfoList) {
		this.brandInfoList = brandInfoList;
	}

	@Override
	public BINOLPLSCF06_Form getModel() {
		return form;
	}

}
