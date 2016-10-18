/*
 * @(#)BINOLCTTPL02_Action.java     1.0 2012/11/06
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
import com.cherry.cm.cmbussiness.bl.BINOLCM15_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM32_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.ct.tpl.form.BINOLCTTPL02_Form;
import com.cherry.ct.tpl.interfaces.BINOLCTTPL02_IF;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 沟通模板管理Action
 * 
 * @author ZhangGS
 * @version 1.0 2012.11.06
 */
public class BINOLCTTPL02_Action extends BaseAction implements ModelDriven<BINOLCTTPL02_Form>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2440235627017916055L;
	
	/**异常日志*/
	private static final Logger logger = LoggerFactory.getLogger(BINOLCTTPL02_Action.class);
	
	/** 各类编号取号共通BL */
	@Resource
	private BINOLCM15_BL binOLCM15_BL;
	
	@Resource
	private BINOLCTTPL02_IF binolcttpl02_IF;
	
	@Resource
	private BINOLCM32_BL binOLCM32_BL;
	
	/** 沟通模板变量List */
	private List paramList;
	
	/** 沟通模版内容非法字符List*/
	private List illegalCharList;

	public List getParamList() {
		return paramList;
	}

	public void setParamList(List paramList) {
		this.paramList = paramList;
	}

	/** 沟通模板管理Form */
	private BINOLCTTPL02_Form form = new BINOLCTTPL02_Form();
	
	public String init() throws Exception{
		try{
			Map<String, Object> map = new HashMap<String, Object>();
			// 用户信息
			UserInfo userInfo = (UserInfo) session
					.get(CherryConstants.SESSION_USERINFO);
			// 所属组织
			map.put(CherryConstants.ORGANIZATIONINFOID, userInfo
					.getBIN_OrganizationInfoID());
			// 品牌ID
			int brandId = userInfo.getBIN_BrandInfoID();
			
			map.put(CherryConstants.BRANDINFOID, ConvertUtil.getString(brandId));
			
			map.put("templateUse", CherryConstants.DEFAULTVARIABLEGROUP);
			
			map.put("validFlag", "1");
			
			paramList = binOLCM32_BL.getVariableList(map);
			
			//沟通方式，默认为短信
			map.put("commType", 1);
			//非法字符
			illegalCharList = binOLCM32_BL.getIllegalCharList(map);
			
			form.setSignature(userInfo.getBrandName());
			
			form.setTemplateUse(CherryConstants.DEFAULTVARIABLEGROUP);
		} catch(Exception e){
			logger.error(e.getMessage(), e);
			// 自定义异常的场合
			if(e instanceof CherryException){
				CherryException temp = (CherryException)e;
				this.addActionError(temp.getErrMessage());
				return CherryConstants.GLOBAL_ACCTION_RESULT_PAGE;
			 }else{
				//系统发生异常，请联系管理人员。
				this.addActionError(getText("ECM00036"));
				return CherryConstants.GLOBAL_ACCTION_RESULT_PAGE;
			 }
		}
		return SUCCESS;
	}
	
	/** 编辑沟通模板 */
	public String editInit() throws Exception{
		try{
			Map<String, Object> map = new HashMap<String, Object>();
			Map<String, Object> templateMap = new HashMap<String, Object>();
			// 用户信息
			UserInfo userInfo = (UserInfo) session
					.get(CherryConstants.SESSION_USERINFO);
			// 所属组织
			map.put(CherryConstants.ORGANIZATIONINFOID, userInfo
					.getBIN_OrganizationInfoID());
			// 品牌ID
			int brandId = userInfo.getBIN_BrandInfoID();
			
			map.put(CherryConstants.BRANDINFOID, ConvertUtil.getString(brandId));
			
			if(!CherryChecker.isNullOrEmpty(form.getTemplateCode(), true)){
				map.put("templateCode", form.getTemplateCode());
				// 获取沟通模板
				templateMap = binolcttpl02_IF.getTemplateInfo(map);
				// 设置沟通模板属性
				if(null != templateMap && !templateMap.isEmpty()){
					if(CherryChecker.isNullOrEmpty(form.getTemplateCode(), true))
					{
						form.setTemplateCode(ConvertUtil.getString(templateMap.get("templateCode")));
					}
					if(CherryChecker.isNullOrEmpty(form.getTemplateName(), true))
					{
						form.setTemplateName(ConvertUtil.getString(templateMap.get("templateName")));
					}
					if(CherryChecker.isNullOrEmpty(form.getTemplateUse(), true))
					{
						form.setTemplateUse(ConvertUtil.getString(templateMap.get("templateUse")));
					}
					if(CherryChecker.isNullOrEmpty(form.getTemplateType(), true))
					{
						form.setTemplateType(ConvertUtil.getString(templateMap.get("templateType")));
					}
					if(CherryChecker.isNullOrEmpty(form.getCustomerType(), true))
					{
						form.setCustomerType(ConvertUtil.getString(templateMap.get("customerType")));
					}
					if(CherryChecker.isNullOrEmpty(form.getMsgContents(), true))
					{
						form.setMsgContents(ConvertUtil.getString(templateMap.get("msgContents")));
					}
				}
			}
			
			map.put("templateUse", form.getTemplateUse());
			
			map.put("validFlag", "1");
			
			paramList = binOLCM32_BL.getVariableList(map);
			//沟通方式，默认为短信
			map.put("commType", 1);
			//非法字符
			illegalCharList = binOLCM32_BL.getIllegalCharList(map);
		} catch(Exception e){
			logger.error(e.getMessage(), e);
			// 自定义异常的场合
			if(e instanceof CherryException){
				CherryException temp = (CherryException)e;
				this.addActionError(temp.getErrMessage());
				return CherryConstants.GLOBAL_ACCTION_RESULT_PAGE;
			 }else{
				//系统发生异常，请联系管理人员。
				this.addActionError(getText("ECM00036"));
				return CherryConstants.GLOBAL_ACCTION_RESULT_PAGE;
			 }
		}
		return SUCCESS;
	}
	
	public String save() throws Exception{
		try{
			//参数Map
			Map<String, Object> map = new HashMap<String, Object>();
			String type = "INSERT";
			String templateCode = "";
			// 用户信息
			UserInfo userInfo = (UserInfo) session     
					.get(CherryConstants.SESSION_USERINFO);
			// 品牌代码
			String brandCode = userInfo.getBrandCode();
			// 品牌ID
			int brandId = userInfo.getBIN_BrandInfoID();
			// 部门ID
			map.put(CherryConstants.ORGANIZATIONID, userInfo
					.getBIN_OrganizationID());
			// 所属组织
			map.put(CherryConstants.ORGANIZATIONINFOID, userInfo
					.getBIN_OrganizationInfoID());
			// 品牌ID
			map.put("brandInfoId", ConvertUtil.getString(brandId));
			// 品牌代码
			map.put("brandCode", brandCode);
			// 作成者
			map.put(CherryConstants.CREATEDBY, userInfo.getLoginName());
			// 更新者
			map.put(CherryConstants.UPDATEDBY, userInfo.getLoginName());
			
			if("edit".equals(form.getShowType())){
				templateCode = form.getTemplateCode();
				type = "UPDATE";
			}else{
				// 模板编号
				if("2".equals(form.getTemplateType())){
					templateCode = binOLCM15_BL.getSequenceId(userInfo
							.getBIN_OrganizationInfoID(), brandId, "C");
				}else{
					templateCode = binOLCM15_BL.getSequenceId(userInfo
							.getBIN_OrganizationInfoID(), brandId, "B");
				}
				type = "INSERT";
			}
			map.put("templateCode", templateCode);
			// 模板名称
			map.put("templateName", form.getTemplateName());
			// 模板用途
			map.put("templateUse", form.getTemplateUse());
			// 模板类型
			map.put("templateType", "1");
			// 适用客户类型
			map.put("customerType", form.getCustomerType());
			// 模板内容
			map.put("contents", form.getMsgContents());
			// 是否默认模板
			map.put("isDefault", "0");
			
			// 保存沟通设置
			binolcttpl02_IF.saveTemplate(map, type);
			//处理成功
			this.addActionMessage(getText("ICM00002"));
			return CherryConstants.GLOBAL_ACCTION_RESULT_BODY;
		} catch(Exception e){
			logger.error(e.getMessage(), e);
			// 自定义异常的场合
			if(e instanceof CherryException){
				CherryException temp = (CherryException)e;
				this.addActionError(temp.getErrMessage());
				return CherryConstants.GLOBAL_ACCTION_RESULT_PAGE;
			 }else{
				//系统发生异常，请联系管理人员。
				this.addActionError(getText("ECM00036"));
				return CherryConstants.GLOBAL_ACCTION_RESULT_PAGE;
			 }
		}
	}
	
	/**
	 * 更改模板变量
	 * @throws Exception
	 */
	public String change() throws Exception{
		try{
			//参数Map
			Map<String, Object> map = new HashMap<String, Object>();
			// 用户信息
			UserInfo userInfo = (UserInfo) session
					.get(CherryConstants.SESSION_USERINFO);
			// 所属组织
			map.put(CherryConstants.ORGANIZATIONINFOID, userInfo
					.getBIN_OrganizationInfoID());
			// 品牌ID
			int brandId = userInfo.getBIN_BrandInfoID();
			
			map.put(CherryConstants.BRANDINFOID, ConvertUtil.getString(brandId));
			
			map.put("templateUse", form.getTemplateUse());
			
			map.put("validFlag", "1");
			
			paramList = binOLCM32_BL.getVariableList(map);
		} catch(Exception e){
			logger.error(e.getMessage(), e);
			// 自定义异常的场合
			if(e instanceof CherryException){
				CherryException temp = (CherryException)e;
				this.addActionError(temp.getErrMessage());
				return CherryConstants.GLOBAL_ACCTION_RESULT_PAGE;
			 }else{
				//系统发生异常，请联系管理人员。
				this.addActionError(getText("ECM00036"));
				return CherryConstants.GLOBAL_ACCTION_RESULT_PAGE;
			 }
		}
		return SUCCESS;
	}
	
	@Override
	public BINOLCTTPL02_Form getModel() {
		return form;
	}

	public List getIllegalCharList() {
		return illegalCharList;
	}

	public void setIllegalCharList(List illegalCharList) {
		this.illegalCharList = illegalCharList;
	}
}
