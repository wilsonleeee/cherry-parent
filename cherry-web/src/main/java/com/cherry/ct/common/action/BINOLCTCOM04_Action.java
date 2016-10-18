/*
 * @(#)BINOLCTTPL01_Action.java     1.0 2013/01/11
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
package com.cherry.ct.common.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM05_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM15_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM32_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.ct.common.form.BINOLCTCOM04_Form;
import com.cherry.ct.tpl.interfaces.BINOLCTTPL01_IF;
import com.cherry.ct.tpl.interfaces.BINOLCTTPL02_IF;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 
 * 
 * @author ZhangGS
 * @version 1.0 2013.01.11
 */
public class BINOLCTCOM04_Action extends BaseAction implements ModelDriven<BINOLCTCOM04_Form>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1549713813589387068L;
	private BINOLCTCOM04_Form form = new BINOLCTCOM04_Form();
	
	/**异常日志*/
	private static final Logger logger = LoggerFactory.getLogger(BINOLCTCOM04_Action.class);
	
	@Resource
	private BINOLCM05_BL binolcm05_BL;
	
	@Resource
	private BINOLCTTPL01_IF binolcttpl01_IF;
	
	@Resource
	private BINOLCTTPL02_IF binolcttpl02_IF;
	
	@Resource
	private BINOLCM15_BL binOLCM15_BL;
	
	@Resource
	private BINOLCM32_BL binOLCM32_BL;
	
	/** 品牌List */
	private List<Map<String, Object>> brandList;	
	
	/** 沟通模板List */
	private List<Map<String, Object>> templateList;
	
	/** 沟通模板变量List */
	private List paramList;
	
	/** 沟通内容非法字符List*/
	private List illegalCharList;
	
	public List<Map<String, Object>> getBrandList() {
		return brandList;
	}

	public void setBrandList(List<Map<String, Object>> brandList) {
		this.brandList = brandList;
	}

	public List<Map<String, Object>> getTemplateList() {
		return templateList;
	}

	public void setTemplateList(List<Map<String, Object>> templateList) {
		this.templateList = templateList;
	}
	
	public List getParamList() {
		return paramList;
	}

	public void setParamList(List paramList) {
		this.paramList = paramList;
	}

	@SuppressWarnings("unchecked")
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
	
			// 总部用户登录的时候
			if (CherryConstants.BRAND_INFO_ID_VALUE == brandId) {
				// 取得所管辖的品牌List
				brandList = binolcm05_BL.getBrandInfoList(map);
			}else {
				form.setBrandInfoId(ConvertUtil.getString(brandId));
				//默认信息签名
				form.setSignature(userInfo.getBrandName());
			}
			map.put(CherryConstants.BRANDINFOID, form.getBrandInfoId());
			// 模板用途
			map.put("templateUse", form.getTemplateUse());
			
			//有效区分
			map.put("validFlag", "1");
			
			paramList = binOLCM32_BL.getVariableList(map);
			
			//沟通方式
			map.put("commType", form.getMessageType());
			//非法字符
			setIllegalCharList(binOLCM32_BL.getIllegalCharList(map));
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
	
	@SuppressWarnings("unchecked")
	public String search() throws Exception{
		try{
			Map<String, Object> map = getSearchMap();
			//取得模板数量
			int count = binolcttpl01_IF.getTemplateCount(map);
			if(count > 0){
				List<Map<String, Object>> tplList = binolcttpl01_IF.getTemplateList(map);
				// 取得List
				this.setTemplateList(tplList);
			}
			// form表单设置
			form.setITotalDisplayRecords(count);
			form.setITotalRecords(count);
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
	
	public void save() throws Exception{
		Map<String,Object> resultMap = new HashMap<String, Object>();
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
			// 模板编号
			if("2".equals(form.getMessageType())){
				templateCode = binOLCM15_BL.getSequenceId(userInfo
						.getBIN_OrganizationInfoID(), brandId, "C");
			}else{
				templateCode = binOLCM15_BL.getSequenceId(userInfo
						.getBIN_OrganizationInfoID(), brandId, "B");
			}
			map.put("templateCode", templateCode);
			// 模板名称
			map.put("templateName", form.getTemplateName());
			// 模板用途
			map.put("templateUse", form.getTemplateUse());
			// 模板类型
			map.put("templateType", form.getMessageType());
			// 适用客户类型
			map.put("customerType", form.getCustomerType());
			// 模板内容
			map.put("contents", form.getMsgContents());
			// 是否默认模板
			map.put("isDefault", "0");
			// 保存沟通设置
			binolcttpl02_IF.saveTemplate(map, type);
			//处理成功
			resultMap.put("successMsg", getText("ICM00002"));
		} catch(Exception e){
			logger.error(e.getMessage(), e);
			// 自定义异常的场合
			if(e instanceof CherryException){
				CherryException temp = (CherryException)e;
				resultMap.put("errorMsg", temp.getErrMessage());
			 }else{
				//系统发生异常，请联系管理人员。
				resultMap.put("errorMsg",getText("ECM00036"));
			 }
		}
		ConvertUtil.setResponseByAjax(response, resultMap);
	}
	
	private Map<String, Object> getSearchMap() {
		//参数Map
		Map<String, Object> map = new HashMap<String, Object>();
		// form参数设置到map中
		ConvertUtil.setForm(form, map);
		// 用户信息
		UserInfo userInfo = (UserInfo) session     
				.get(CherryConstants.SESSION_USERINFO);
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo
				.getBIN_OrganizationInfoID());
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
		map.put(CherryConstants.SESSION_LANGUAGE, session
				.get(CherryConstants.SESSION_LANGUAGE));
		// 模板类型
		map.put("templateType", form.getMessageType());
		// 模板状态
		map.put("status", form.getStatus());
		// 模板名称
		map.put("templateName", form.getTemplateName());
		// 模板用途
		map.put("templateUse", form.getTemplateUse());
		// 适用客户类型
		map.put("customerType", form.getCustomerType());
		
		return map;
	}

	@Override
	public BINOLCTCOM04_Form getModel() {
		return form;
	}

	public List getIllegalCharList() {
		return illegalCharList;
	}

	public void setIllegalCharList(List illegalCharList) {
		this.illegalCharList = illegalCharList;
	}
	
}
