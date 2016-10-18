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
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.pl.scf.bl.BINOLPLSCF07_BL;
import com.cherry.pl.scf.form.BINOLPLSCF06_Form;
import com.opensymphony.xwork2.ModelDriven;

/**
 * code值管理详细Action
 * 
 * @author zhangjie
 * @version 1.0 2011.3.25
 */
@SuppressWarnings("unchecked")
public class BINOLPLSCF07_Action extends BaseAction implements
ModelDriven<BINOLPLSCF06_Form>{

	private static final long serialVersionUID = 2880040071704324074L;
	/** code值管理一览BL */
	@Resource
	private BINOLPLSCF07_BL binolplscf07_BL;
	
	/** CODE管理表ID */
	private String codeManagerID;
	
	/** CODE管理表详细 */
	private Map codeMDetail;

	private List coderList;
	
	/** 参数FORM */
	private BINOLPLSCF06_Form form = new BINOLPLSCF06_Form();

	/**
	 * code值管理详细初期表示
	 * 
	 * @return code值管理详细画面
	 */
	public String init() {
		Map<String, Object> map = new HashMap<String, Object>();
		// 接收CODE管理表ID
		map.put("codeManagerID",form.getCodeManagerID());
		// 默认组织
		map.put("DefOrgName",getText("PPL00029"));
		// 默认品牌
		map.put("DefBrandName",getText("PPL00030"));
		// 取得CODE管理表详细信息
		codeMDetail = binolplscf07_BL.getCodeMDetail(map);

		coderList=binolplscf07_BL.getCoderList(codeMDetail);
		
		return SUCCESS;
		
	}
	
	public String save() throws Exception{
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
			//组织code
			codeManagerMap.put("orgCode", form.getOrgCode());
			//品牌code
			codeManagerMap.put("brandCode", form.getBrandCode());
			
			List<String[]> codeList = new ArrayList<String[]>();
			codeList.add(form.getCodeKeyArr());
			codeList.add(form.getValue1Arr());
			codeList.add(form.getValue2Arr());
			codeList.add(form.getValue3Arr());
			codeList.add(form.getGradeArr());
			codeList.add(form.getCodeOrderArr());
			binolplscf07_BL.tran_sava(codeList, codeManagerMap, sessionMap);
			this.addActionMessage(getText("ICM00002"));
			return CherryConstants.GLOBAL_ACCTION_RESULT_BODY;
		}catch(Exception e){
			if(e instanceof CherryException){
				CherryException temp = (CherryException) e;
				this.addActionError(temp.getErrMessage());
				return CherryConstants.GLOBAL_ACCTION_RESULT;
			}
			throw e;
		}
	}

	public Map<String,Object> getSessionMap(){
		Map<String,Object> map = new HashMap<String,Object>();
		UserInfo userInfo = (UserInfo)session.get(CherryConstants.SESSION_USERINFO);
		map.put(CherryConstants.CREATEDBY, userInfo.getBIN_UserID());
		map.put(CherryConstants.UPDATEDBY, userInfo.getBIN_UserID());
		map.put(CherryConstants.CREATEPGM, "BINOLPLSCF07");
		map.put(CherryConstants.UPDATEPGM, "BINOLPLSCF07");
		return map;
	}
	
	public Map getCodeMDetail() {
		return codeMDetail;
	}

	public void setCodeMDetail(Map codeMDetail) {
		this.codeMDetail = codeMDetail;
	}

	public String getCodeManagerID() {
		return codeManagerID;
	}

	public void setCodeManagerID(String codeManagerID) {
		this.codeManagerID = codeManagerID;
	}

	public List getCoderList() {
		return coderList;
	}


	public void setCoderList(List coderList) {
		this.coderList = coderList;
	}

	@Override
	public BINOLPLSCF06_Form getModel() {
		// TODO Auto-generated method stub
		return form;
	}

}
