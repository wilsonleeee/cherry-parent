/*
 * @(#)BINOLCTTPL03_Action.java     1.0 2013/10/08
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
import com.cherry.cm.cmbussiness.bl.BINOLCM32_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.Bean2Map;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.ct.tpl.form.BINOLCTTPL03_Form;
import com.cherry.ct.tpl.interfaces.BINOLCTTPL03_IF;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 沟通模板管理Action
 * 
 * @author ZhangLe
 * @version 1.0 2013.10.08
 */
public class BINOLCTTPL03_Action extends BaseAction implements ModelDriven<BINOLCTTPL03_Form>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2440235627017916055L;
	
	/**异常日志*/
	private static final Logger logger = LoggerFactory.getLogger(BINOLCTTPL03_Action.class);
	
	@Resource
	private BINOLCTTPL03_IF binOLCTTPL03_IF ;
	
	@Resource
	private BINOLCM32_BL binOLCM32_BL;
	
	/** 沟通模板变量List */
	@SuppressWarnings("rawtypes")
	private List paramList;
	
	/** 沟通模板变量Info */
	@SuppressWarnings("rawtypes")
	private Map paramInfo;

	@SuppressWarnings("rawtypes")
	public List getParamList() {
		return paramList;
	}

	@SuppressWarnings("rawtypes")
	public void setParamList(List paramList) {
		this.paramList = paramList;
	}

	@SuppressWarnings("rawtypes")
	public Map getParamInfo() {
		return paramInfo;
	}

	@SuppressWarnings("rawtypes")
	public void setParamInfo(Map paramInfo) {
		this.paramInfo = paramInfo;
	}

	/** 沟通模板变量设置Form */
	private BINOLCTTPL03_Form form = new BINOLCTTPL03_Form();
	
	public String init() throws Exception{
		return SUCCESS;
	}
	
	/**
	 * 查询模板变量
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public String search() throws Exception{
		try{
			//参数Map
			Map<String, Object> map = new HashMap<String, Object>();
			map = (Map<String, Object>) Bean2Map.toHashMap(form);
			CherryUtil.removeEmptyVal(map);
			paramList = binOLCM32_BL.getVariableList(map);
			return SUCCESS;
		} catch(Exception e){
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
	 * 停用/启用模板变量
	 * @return
	 */
	public String disOrEnable() {
		try{
			UserInfo userInfo = (UserInfo) session
					.get(CherryConstants.SESSION_USERINFO);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("associateId", form.getAssociateId());
			map.put("validFlag", form.getValidFlag());
			map.put("updatedBy", userInfo.getBIN_UserID());
			map.put("updatePGM", "BINOLCTTPL03");
			//清除空值
			CherryUtil.removeEmptyVal(map);
			binOLCTTPL03_IF.disOrEnable(map);
			this.addActionMessage(getText("ICM00002"));
			return CherryConstants.GLOBAL_ACCTION_RESULT;
		} catch(Exception e){
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
	
	/***
	 * 编辑初始化界面
	 * @return
	 */
	public String edit() {
		try{
			Map<String, Object> map =new HashMap<String, Object>();
			map.put("queryType", "1");
			if("1".equals(ConvertUtil.getString(form.getType()))){
				map.put("isBasicFlag", "1");
			}else if("3".equals(ConvertUtil.getString(form.getType()))){
				map.put("isBasicFlag", "2");
			}else{
				map.put("isBasicFlag", "1");
			}
			paramList = binOLCM32_BL.getVariableList(map);
			map = new HashMap<String, Object>();
			map.put("variableCode", form.getVariableCode());
			map.put("queryType", "1");
			paramInfo = binOLCM32_BL.getVariable(map);
			return SUCCESS;
		}catch(Exception e){
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
	 * 更新模板变量
	 * @return
	 */
	public String update() {
		try{
			UserInfo userInfo = (UserInfo) session
					.get(CherryConstants.SESSION_USERINFO);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("variableCode", form.getVariableCode());
			map.put("comments", form.getComments());
			map.put("basicVariable", form.getBasicVariable());
			map.put("operatorChar", form.getOperatorChar());
			map.put("computedValue", form.getComputedValue());
			map.put("type", form.getType());
			map.put("updatedBy", userInfo.getBIN_UserID());
			map.put("updatePGM", "BINOLCTTPL03");
			//清除空值
			CherryUtil.removeEmptyVal(map);
			binOLCTTPL03_IF.tran_updateVariable(map);
			this.addActionMessage(getText("ICM00002"));
			return CherryConstants.GLOBAL_ACCTION_RESULT;
		} catch(Exception e){
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
	
	@Override
	public BINOLCTTPL03_Form getModel() {
		return form;
	}
}
