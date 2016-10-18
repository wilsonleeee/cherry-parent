/*
 * @(#)BINOLPLSCF07_Action.java     1.0 2011/3/25
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

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM05_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.Bean2Map;
import com.cherry.pl.scf.bl.BINOLPLSCF06_BL;
import com.cherry.pl.scf.bl.BINOLPLSCF07_BL;
import com.cherry.pl.scf.bl.BINOLPLSCF08_BL;
import com.cherry.pl.scf.form.BINOLPLSCF06_Form;
import com.googlecode.jsonplugin.JSONUtil;
import com.googlecode.jsonplugin.annotations.JSON;
import com.opensymphony.xwork2.ModelDriven;

/**
 * code值管理编辑Action
 * 
 * @author zhangjie
 * @version 1.0 2011.3.25
 */
@SuppressWarnings("unchecked")
public class BINOLPLSCF08_Action extends BaseAction implements
ModelDriven<BINOLPLSCF06_Form>{

	private static final long serialVersionUID = 2880040071704324074L;
	/** code值管理一览BL */
	@Resource
	private BINOLPLSCF08_BL binolplscf08_BL;
	@Resource
	private BINOLPLSCF06_BL binolplscf06_BL;
	@Resource
	private BINOLPLSCF07_BL binolplscf07_BL;
	
	/** CODE管理表详细 */
	private Map codeMDetail;

	private List coderList;

	/** 参数FORM */
	private BINOLPLSCF06_Form form = new BINOLPLSCF06_Form();
	
	
	/** CODE管理表ID */
	private String codeManagerID;
	
	/** CODE管理表信息 */
	private Map codeMInfo;	
	
	/** 组织List */
	private List<Map<String, Object>> orgInfoList;

	/** 品牌List */
	private List<Map<String, Object>> brandInfoList;


	/**
	 * code值管理详细初期表示
	 * 
	 * @return code值管理详细画面
	 */
	public String init() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("codeManagerID", codeManagerID);
		codeMInfo = binolplscf08_BL.getCodeMInfo(map);
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
			orgInfoList = binolplscf06_BL.getOrgInfoList();
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
			if (null != codeMInfo && !codeMInfo.isEmpty()) {
				// 选中的组织Code
				String selOrgCode = (String) codeMInfo.get("orgCode");
				if (!String.valueOf(CherryConstants.ORG_CODE_ALL).equals(selOrgCode)) {
					// 查询品牌CodeList
					brandInfoList = binolplscf06_BL.getBrandCodeList(codeMInfo);
					if (null != brandInfoList && !brandInfoList.isEmpty()) {
						brandInfoList.add(0, defaultMap);				
					} else {
						brandInfoList = new ArrayList<Map<String, Object>>();
						brandInfoList.add(defaultMap);				
					}
				}
			}
			if (null == brandInfoList) {
				brandInfoList = new ArrayList<Map<String, Object>>();
				brandInfoList.add(defaultMap);
			}
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
				brandInfoList = binolplscf06_BL.getBrandCodeList(orgMap);
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
		
		return SUCCESS;
		
	}

	/**
	 * <p>
	 * 更新
	 * </p>
	 * 
	 * 
	 * @param 无
	 * @return String 跳转页面
	 * @throws Exception
	 * 
	 */
	public String update() throws Exception {
		// 参数MAP取得
		Map<String, Object> map = getParamsMap();
		map.put("codeManagerID", codeManagerID);
		// 更新code管理值数据
		binolplscf08_BL.tran_updateCodeM(map);
		// 处理成功
		this.addActionMessage(getText("ICM00002"));
		return CherryConstants.GLOBAL_ACCTION_RESULT_BODY;
	}
	
	/**
	 * 删除code值
	 * 
	 * 
	 * */
	public String deleteCode()throws Exception{
		Map<String,Object> map = this.getSessionMap();
		
		map.put("code_orgCode", form.getOrgCode().trim());
		map.put("code_brandCode", form.getBrandCode().trim());
		map.put("codeType", form.getCodeType());
		map.put("codeManagerID", form.getCodeManagerID().trim());
		
		String codeArr = form.getDeleteCodeArr();
		List<Map<String,Object>> codeList = (List<Map<String,Object>>)JSONUtil.deserialize(codeArr);
		
		binolplscf08_BL.tran_deleteCode(codeList, map);
		
		codeMDetail = binolplscf07_BL.getCodeMDetail(map);
		
		coderList = binolplscf07_BL.getCoderList(codeMDetail);
		
		return SUCCESS;
	}
	
	/**
	 * 保存编辑
	 * 
	 * */
	public String saveEdit() throws Exception{
		try{
			//用户及操作信息
			Map<String,Object> sessionMap = this.getSessionMap();
			//code管理值信息
			Map<String,Object> codeManagerMap = new HashMap<String,Object>();
			//类别
			codeManagerMap.put("codeType", form.getCodeType().trim());
			//类别名称
			codeManagerMap.put("codeName", form.getCodeName().trim());
			//Key说明
			codeManagerMap.put("keyDescription", form.getKeyDescription().trim());
			//Value1说明
			codeManagerMap.put("value1Description", form.getValue1Description().trim());
			//Value2说明
			codeManagerMap.put("value2Description", form.getValue2Description().trim());
			//Value3说明
			codeManagerMap.put("value3Description", form.getValue3Description().trim());
			
			List<String[]> codeList = new ArrayList<String[]>();
			codeList.add(form.getCodeKeyArr());
			codeList.add(form.getValue1Arr());
			codeList.add(form.getValue2Arr());
			codeList.add(form.getValue3Arr());
			codeList.add(form.getGradeArr());
			codeList.add(form.getCodeOrderArr());
			
			Integer codeMId = binolplscf08_BL.tran_savaEdit(codeList, codeManagerMap, sessionMap);
			Map<String,Object> searchMap = new HashMap<String,Object>();
			searchMap.put("codeManagerID", codeMId);
			codeMDetail = binolplscf07_BL.getCodeMDetail(searchMap);
			coderList = binolplscf07_BL.getCoderList(codeMDetail);
		}catch(Exception e){
			CherryException temp = (CherryException) e;
			this.addActionError(temp.getErrMessage());
			return CherryConstants.GLOBAL_ACCTION_RESULT;
		}
		return SUCCESS;
	}
	
	/**
	 * 返回按钮
	 * 
	 * 
	 * */
	public String doBack(){
		
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("codeManagerID", form.getCodeManagerID().trim());
		
		codeMDetail = binolplscf07_BL.getCodeMDetail(paramMap);
		
		coderList = binolplscf07_BL.getCoderList(codeMDetail);
		
		return SUCCESS;
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
		}*/
		// 用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 作成者
		map.put(CherryConstants.CREATEDBY, userInfo.getBIN_UserID());
		// 更新者
		map.put(CherryConstants.UPDATEDBY, userInfo.getBIN_UserID());

		return map;
	}
	
	public Map<String,Object> getSessionMap(){
		Map<String,Object> map = new HashMap<String,Object>();
		UserInfo userInfo = (UserInfo)session.get(CherryConstants.SESSION_USERINFO);
		map.put("orgCode", userInfo.getOrganizationInfoCode());
		map.put("brandCode", userInfo.getBrandCode());
		map.put(CherryConstants.CREATEDBY, userInfo.getBIN_UserID());
		map.put(CherryConstants.UPDATEDBY, userInfo.getBIN_UserID());
		map.put(CherryConstants.CREATEPGM, "BINOLPLSCF08");
		map.put(CherryConstants.UPDATEPGM, "BINOLPLSCF08");
		return map;
	}

	public Map getCodeMInfo() {
		return codeMInfo;
	}



	public void setCodeMInfo(Map codeMInfo) {
		this.codeMInfo = codeMInfo;
	}



	public List<Map<String, Object>> getOrgInfoList() {
		return orgInfoList;
	}



	public void setOrgInfoList(List<Map<String, Object>> orgInfoList) {
		this.orgInfoList = orgInfoList;
	}



	public List<Map<String, Object>> getBrandInfoList() {
		return brandInfoList;
	}



	public void setBrandInfoList(List<Map<String, Object>> brandInfoList) {
		this.brandInfoList = brandInfoList;
	}


	public String getCodeManagerID() {
		return codeManagerID;
	}



	public void setCodeManagerID(String codeManagerID) {
		this.codeManagerID = codeManagerID;
	}



	@Override
	public BINOLPLSCF06_Form getModel() {
		return form;
		}

	public Map getCodeMDetail() {
		return codeMDetail;
	}

	public void setCodeMDetail(Map codeMDetail) {
		this.codeMDetail = codeMDetail;
	}

	public List getCoderList() {
		return coderList;
	}

	public void setCoderList(List coderList) {
		this.coderList = coderList;
	}

	

}
