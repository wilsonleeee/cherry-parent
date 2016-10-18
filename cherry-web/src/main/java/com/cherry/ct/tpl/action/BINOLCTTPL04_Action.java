/*
 * @(#)BINOLCTTPL04_Action.java     1.0 2013/11/19
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

package com.cherry.ct.tpl.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM05_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.ct.tpl.form.BINOLCTTPL04_Form;
import com.cherry.ct.tpl.interfaces.BINOLCTTPL04_IF;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 沟通模板管理Action
 * 
 * @author ZhangLe
 * @version 1.0 2013.11.19
 */
public class BINOLCTTPL04_Action extends BaseAction implements ModelDriven<BINOLCTTPL04_Form>{

	private static final long serialVersionUID = 775702890507443665L;


	/**异常日志*/
	private static final Logger logger = LoggerFactory.getLogger(BINOLCTTPL04_Action.class);
	private BINOLCTTPL04_Form form = new BINOLCTTPL04_Form();
	
	@Resource(name="binOLCTTPL04_BL")
	private BINOLCTTPL04_IF binOLCTTPL04_BL;
	@Resource(name="binOLCM05_BL")
	private BINOLCM05_BL binOLCM05_BL;
	
	@SuppressWarnings("rawtypes")
	/**品牌List*/
	private List brandList;
	/**非法字符List*/
	@SuppressWarnings("rawtypes")
	private List illegalCharList;
	/**非法字符Map*/
	@SuppressWarnings("rawtypes")
	private Map illegalCharInfo;
	
	/**
	 * 初始化查询画面
	 * @return
	 * @throws Exception
	 */
	public String init() throws Exception{
		UserInfo userInfo = (UserInfo) request.getSession().getAttribute(CherryConstants.SESSION_USERINFO);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		if(userInfo.getBIN_BrandInfoID() != CherryConstants.BRAND_INFO_ID_VALUE){
			form.setBrandInfoId(ConvertUtil.getString(userInfo.getBIN_BrandInfoID()));
			form.setBrandName(userInfo.getBrandName());
		}else{
			brandList = binOLCM05_BL.getBrandInfoList(map);
		}
		return SUCCESS;
	}
	
	/**
	 * 非法字符查询
	 * @return
	 */
	public String search() {
		try {
			Map<String, Object> map = this.getSearchMap();
			int count = binOLCTTPL04_BL.getIllegalCharCount(map);
			if(count > 0){
				illegalCharList = binOLCTTPL04_BL.getIllegalCharList(map);
			}
			form.setITotalDisplayRecords(count);
			form.setITotalRecords(count);
			return SUCCESS;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			// 自定义异常的场合
			if(e instanceof CherryException){
				CherryException temp = (CherryException)e;
				this.addActionError(temp.getErrMessage());
				return CherryConstants.GLOBAL_ACCTION_RESULT;
			}else{
				//系统发生异常，请联系管理人员。
				this.addActionError(getText("ECM00036"));
				return CherryConstants.GLOBAL_ACCTION_RESULT;
			}
		}
	}
	
	/**
	 * 初始化添加画面
	 * @return
	 */
	public String addInit() {
		UserInfo userInfo = (UserInfo) request.getSession().getAttribute(CherryConstants.SESSION_USERINFO);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		if(userInfo.getBIN_BrandInfoID() != CherryConstants.BRAND_INFO_ID_VALUE){
			form.setBrandInfoId(ConvertUtil.getString(userInfo.getBIN_BrandInfoID()));
			form.setBrandName(userInfo.getBrandName());
		}else{
			brandList = binOLCM05_BL.getBrandInfoList(map);
		}
		return SUCCESS;
	}
	
	/**
	 * 添加非法字符
	 * @return
	 */
	public String add() {
		try{
			Map<String, Object> map = this.getSearchMap();
			binOLCTTPL04_BL.tran_addIllegalChar(map);
			this.addActionMessage(getText("ICM00001"));
			return CherryConstants.GLOBAL_ACCTION_RESULT;
		}catch (Exception e) {
			logger.error(e.getMessage(), e);
			// 自定义异常的场合
			if(e instanceof CherryException){
				CherryException temp = (CherryException)e;
				this.addActionError(temp.getErrMessage());
				return CherryConstants.GLOBAL_ACCTION_RESULT;
			}else{
				//系统发生异常，请联系管理人员。
				this.addActionError(getText("ECM00036"));
				return CherryConstants.GLOBAL_ACCTION_RESULT;
			}
		}
	}
	
	/**
	 * 添加验证
	 * @throws Exception
	 */
	public void validateAdd(){
		Map<String, Object> map = this.getSearchMap();
		if(binOLCTTPL04_BL.isRepeat(map)){
			this.addFieldError("charValue", getText("ECM00032", new String[]{getText("CTM00021")}));
		}
	}
	
	/**
	 * 更新非法字符
	 * @return
	 */
	public String update() {
		try{
			Map<String, Object> map = this.getSearchMap();
			binOLCTTPL04_BL.tran_updateIllegalChar(map);
			this.addActionMessage(getText("ICM00001"));
			return CherryConstants.GLOBAL_ACCTION_RESULT;
		}catch (Exception e) {
			logger.error(e.getMessage(), e);
			// 自定义异常的场合
			if(e instanceof CherryException){
				CherryException temp = (CherryException)e;
				this.addActionError(temp.getErrMessage());
				return CherryConstants.GLOBAL_ACCTION_RESULT;
			}else{
				//系统发生异常，请联系管理人员。
				this.addActionError(getText("ECM00036"));
				return CherryConstants.GLOBAL_ACCTION_RESULT;
			}
		}		
	}
	
	/**
	 * 更新验证
	 * @throws Exception
	 */
	public void validateUpdate(){
		if(!CherryChecker.isNullOrEmpty(form.getCharValue(), true)){
			Map<String, Object> map = this.getSearchMap();
			if(binOLCTTPL04_BL.isRepeat(map)){
				this.addFieldError("charValue", getText("ECM00032", new String[]{getText("CTM00021")}));
			}
		}
	}
	
	/**
	 * 添加非法字符
	 * @return
	 */
	public String editInit() {
		try{
			UserInfo userInfo = (UserInfo) request.getSession().getAttribute(CherryConstants.SESSION_USERINFO);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
			if(userInfo.getBIN_BrandInfoID() != CherryConstants.BRAND_INFO_ID_VALUE){
				form.setBrandInfoId(ConvertUtil.getString(userInfo.getBIN_BrandInfoID()));
				form.setBrandName(userInfo.getBrandName());
			}else{
				brandList = binOLCM05_BL.getBrandInfoList(map);
			}
			map = this.getSearchMap();
			illegalCharInfo = binOLCTTPL04_BL.getIllegalCharMap(map);
			return SUCCESS;
		}catch (Exception e) {
			logger.error(e.getMessage(), e);
			// 自定义异常的场合
			if(e instanceof CherryException){
				CherryException temp = (CherryException)e;
				this.addActionError(temp.getErrMessage());
				return CherryConstants.GLOBAL_ACCTION_RESULT;
			}else{
				//系统发生异常，请联系管理人员。
				this.addActionError(getText("ECM00036"));
				return CherryConstants.GLOBAL_ACCTION_RESULT;
			}
		}
	}
	
	/**
	 * 共通map
	 * @return
	 */
	public Map<String, Object> getSearchMap() {
		Map<String, Object> map = new HashMap<String, Object>();
		UserInfo userInfo = (UserInfo) request.getSession().getAttribute(CherryConstants.SESSION_USERINFO);
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		if(CherryChecker.isNullOrEmpty(form.getBrandInfoId(), true)){
			if(userInfo.getBIN_BrandInfoID() != CherryConstants.BRAND_INFO_ID_VALUE){
				map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
			}
		}else{
			map.put(CherryConstants.BRANDINFOID, form.getBrandInfoId());
		}
		map.put("charId", form.getCharId());
		//查询用
		map.put("sCharValue", form.getsCharValue());
		//更新和添加用
		map.put("charValue", form.getCharValue());
		map.put("commType", form.getCommType());
		map.put("validFlag", form.getValidFlag());
		map.put("remark", form.getRemark());
		map.put(CherryConstants.CREATEDBY, userInfo.getBIN_UserID());
		map.put(CherryConstants.CREATEPGM, "BINOLCTTPL04");
		map.put(CherryConstants.UPDATEDBY, userInfo.getBIN_UserID());
		map.put(CherryConstants.UPDATEPGM, "BINOLCTTPL04");
		ConvertUtil.setForm(form, map);
		return CherryUtil.remEmptyVal(map);
	}

	@Override
	public BINOLCTTPL04_Form getModel() {
		return form;
	}

	@SuppressWarnings("rawtypes")
	public List getBrandList() {
		return brandList;
	}

	@SuppressWarnings("rawtypes")
	public void setBrandList(List brandList) {
		this.brandList = brandList;
	}

	@SuppressWarnings("rawtypes")
	public List getIllegalCharList() {
		return illegalCharList;
	}

	@SuppressWarnings("rawtypes")
	public void setIllegalCharList(List illegalCharList) {
		this.illegalCharList = illegalCharList;
	}

	@SuppressWarnings("rawtypes")
	public Map getIllegalCharInfo() {
		return illegalCharInfo;
	}

	@SuppressWarnings("rawtypes")
	public void setIllegalCharInfo(Map illegalCharInfo) {
		this.illegalCharInfo = illegalCharInfo;
	}
	

}
