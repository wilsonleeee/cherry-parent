/*	
 * @(#)BINOLJNCOM03_Action.java     1.0 2011/4/18		
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
package com.cherry.jn.common.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM05_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.Bean2Map;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cp.common.interfaces.BINOLCPCOM02_IF;
import com.cherry.cp.common.interfaces.TemplateInit_IF;
import com.cherry.dr.cmbussiness.rule.KnowledgeEngine;
import com.cherry.jn.common.form.BINOLJNCOM03_Form;
import com.cherry.jn.common.interfaces.BINOLJNCOM03_IF;
import com.googlecode.jsonplugin.JSONUtil;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 会员活动组查看 Action
 * 
 * @author hub
 * @version 1.0 2011.4.18
 */
public class BINOLJNCOM03_Action extends BaseAction implements
	ModelDriven<BINOLJNCOM03_Form>, ApplicationContextAware{

	private static final long serialVersionUID = -4414656190112802551L;
	
	/** 参数FORM */
	private BINOLJNCOM03_Form form = new BINOLJNCOM03_Form();
	
	@Resource
    private BINOLCPCOM02_IF binolcpcom02IF;
	
	@Resource
	private BINOLCM05_BL binOLCM05_BL;
	
	@Resource
	private BINOLJNCOM03_IF binoljncom03IF;
	
	private static ApplicationContext applicationContext;
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext = applicationContext;
		
	}
	
	/** 所管辖的品牌List */
	private List<Map<String, Object>> brandInfoList;
	
	/** 规则组模板List */
	private List<Map<String, Object>> grpTemplateList;
	
	/** 会员活动List */
	private List<Map<String, Object>> campaignList;
	
	private List<Map<String, Object>> popRuleInfoList;
	
	public List<Map<String, Object>> getPopRuleInfoList() {
		return popRuleInfoList;
	}

	public void setPopRuleInfoList(List<Map<String, Object>> popRuleInfoList) {
		this.popRuleInfoList = popRuleInfoList;
	}

	/** 会员活动组信息 */
	private Map campaignGrpInfo;
	
	/** 会员活动组编辑标志*/
	private  int editFlag;

	/** 活动模板List */
	private List<Map<String, Object>> camTempList;
	
	/** 模板页面名称 */
	private String templateName;
	
	/** 传递的参数 */
	private Map campParams;
	
	public int getEditFlag() {
		return editFlag;
	}

	public void setEditFlag(int editFlag) {
		this.editFlag = editFlag;
	}

	@Override
	public BINOLJNCOM03_Form getModel() {
		return form;
	}
	
	public List<Map<String, Object>> getBrandInfoList() {
		return brandInfoList;
	}

	public void setBrandInfoList(List<Map<String, Object>> brandInfoList) {
		this.brandInfoList = brandInfoList;
	}
	
	public List<Map<String, Object>> getGrpTemplateList() {
		return grpTemplateList;
	}

	public void setGrpTemplateList(List<Map<String, Object>> grpTemplateList) {
		this.grpTemplateList = grpTemplateList;
	}
	
	public Map getCampaignGrpInfo() {
		return campaignGrpInfo;
	}

	public void setCampaignGrpInfo(Map campaignGrpInfo) {
		this.campaignGrpInfo = campaignGrpInfo;
	}
	
	public List<Map<String, Object>> getCampaignList() {
		return campaignList;
	}

	public void setCampaignList(List<Map<String, Object>> campaignList) {
		this.campaignList = campaignList;
	}

	public List<Map<String, Object>> getCamTempList() {
		return camTempList;
	}

	public void setCamTempList(List<Map<String, Object>> camTempList) {
		this.camTempList = camTempList;
	}

	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	public Map getCampParams() {
		return campParams;
	}

	public void setCampParams(Map campParams) {
		this.campParams = campParams;
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
		Map<String, Object> map = new HashMap<String, Object>();
		// 品牌下拉框初期显示
		brandInit(map);
		return SUCCESS;
	}
	
	/**
	 * <p>
	 * 编辑画面初期显示
	 * </p>
	 * 
	 * 
	 * @param 无
	 * @return String 跳转页面
	 * 
	 */
	
	public String edit() throws Exception {
		Map<String, Object> map = (Map<String, Object>)Bean2Map.toHashMap(form);
		// 品牌下拉框初期显示
		brandInit(map);
		// 取得会员活动组信息
		campaignGrpInfo = binoljncom03IF.getCampaignGrpInfo(map);
		if (null == campaignGrpInfo || campaignGrpInfo.isEmpty()) {
			this.addActionError(getText("ECM00038"));
			return CherryConstants.GLOBAL_ACCTION_RESULT_BODY;
		}
		// 所属品牌
		form.setBrandInfoId(String.valueOf(campaignGrpInfo.get("brandInfoId")));
		// 编辑标志
		editFlag = 1;
		return SUCCESS;
	}
	
	/**
	 * <p>
	 * 品牌下拉框初期显示
	 * </p>
	 * 
	 * @param
	 * 		map 查询参数集合
	 * @return
	 * 
	 */
	
	private void brandInit(Map<String, Object> map) throws Exception {
		// 用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 语言类型
		map.put(CherryConstants.SESSION_LANGUAGE,  session
				.get(CherryConstants.SESSION_LANGUAGE));
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo
				.getBIN_OrganizationInfoID());
		// 总部用户登录的时候
		if (CherryConstants.BRAND_INFO_ID_VALUE == userInfo.getBIN_BrandInfoID()) {
			// 取得所管辖的品牌List
			brandInfoList = binOLCM05_BL.getBrandInfoList(map);
		} else {
			// 品牌信息
			Map<String, Object> brandInfo = new HashMap<String, Object>();
			// 品牌ID
			brandInfo.put("brandInfoId", userInfo.getBIN_BrandInfoID());
			// 品牌名称
			brandInfo.put("brandName", userInfo.getBrandName());
			brandInfoList = new ArrayList<Map<String, Object>>();
			brandInfoList.add(brandInfo);
		}
	}
	
	/**
	 * <p>
	 * 改变模板
	 * </p>
	 * 
	 * 
	 * @param 无
	 * @return String 跳转页面
	 * 
	 */
	
	public String change() throws Exception {
		Map<String, Object> map = (Map<String, Object>)Bean2Map.toHashMap(form);
		// 用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 语言类型
		map.put(CherryConstants.SESSION_LANGUAGE,  session
				.get(CherryConstants.SESSION_LANGUAGE));
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo
				.getBIN_OrganizationInfoID());
		// 添加时
		if (CherryChecker.isNullOrEmpty(map.get("campaignGrpId"))) {
			// 取得活动组对应的模板List
			grpTemplateList = binoljncom03IF.getGrpTemplateList(map);
		// 编辑时
		} else {
			if (null != map.get("campaignType") && 
					map.get("campaignType").equals(map.get("campaignTypeOld"))) {
				// 规则体详细
				String ruleDetail = (String) map.get("ruleDetail");
				if (!CherryChecker.isNullOrEmpty(ruleDetail)) {
					// 取得页面显示的规则组模板List
					grpTemplateList = binoljncom03IF.convertGrpTempList(map);
				}
			}
		}
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
    public String save() throws Exception{
    	Map<String, Object> map = (Map<String, Object>)Bean2Map.toHashMap(form);
    	// 用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo
				.getBIN_OrganizationInfoID());
		// 作成者
		map.put(CherryConstants.CREATEDBY, userInfo.getLoginName());
		// 更新者
		map.put(CherryConstants.UPDATEDBY, userInfo.getLoginName());
		try {
			// 会员活动组保存处理
			binoljncom03IF.tran_saveGrp(map);
		} catch (CherryException e) {
			this.addActionError(e.getErrMessage());
			return CherryConstants.GLOBAL_ACCTION_RESULT_BODY;
		}
		// 处理成功
		this.addActionMessage(getText("ICM00002"));
    	return CherryConstants.GLOBAL_ACCTION_RESULT_BODY;
    }
    
    /**
	 * <p>
	 * AJAX会员活动查询
	 * </p>
	 * 
	 * @return
	 */
	public String searchCamp() throws Exception {
		// 取得参数MAP
		Map<String, Object> searchMap = getSearchMap();
		// 取得会员活动总数
		int count = binoljncom03IF.getCampaignCount(searchMap);
		if (count > 0) {
			// 取得会员活动信息List
			campaignList = binoljncom03IF.getCampaignList(searchMap);
		}
		// form表单设置
		// 显示记录
		form.setITotalDisplayRecords(count);
		// 总记录
		form.setITotalRecords(count);
		// AJAX返回至dataTable结果页面
		return SUCCESS;
	}
	
	/**
	 * <p>
	 * AJAX会员活动查询
	 * </p>
	 * 
	 * @return
	 */
	public String popRuleInfo() throws Exception {
		// 取得参数MAP
		Map<String, Object> searchMap = getSearchMap();
		searchMap.put("pointRuleType", form.getPointRuleType());
		searchMap.put("campaignType", "3");
		// 取得会员活动总数
		int count = binoljncom03IF.getPopCampaignCount(searchMap);
		if (count > 0) {
			// 取得会员活动信息List
			popRuleInfoList = binoljncom03IF.getPopCampaignList(searchMap);
		}
		// form表单设置
		// 显示记录
		form.setITotalDisplayRecords(count);
		// 总记录
		form.setITotalRecords(count);
		// AJAX返回至dataTable结果页面
		return SUCCESS;
	}
	
	/**
	 * <p>
	 * 规则查询
	 * </p>
	 * 
	 * @return
	 */
	public String popCampRule() throws Exception {
		// 取得参数MAP
		Map<String, Object> searchMap = getSearchMap();
//		searchMap.put("pointRuleType", form.getPointRuleType());
//		searchMap.put("campaignType", "3");
		// 画面查询条件
		if(!CherryChecker.isNullOrEmpty(form.getSSearch())) {
			searchMap.put("campRuleKw", form.getSSearch());
		}
		// 取得会员活动总数
		int count = binoljncom03IF.getComCampRuleCount(searchMap);
		if (count > 0) {
			// 取得会员活动信息List
			popRuleInfoList = binoljncom03IF.getComCampaignRuleList(searchMap);
		}
		// form表单设置
		// 显示记录
		form.setITotalDisplayRecords(count);
		// 总记录
		form.setITotalRecords(count);
		// AJAX返回至dataTable结果页面
		return SUCCESS;
	}
	
	/**
	 * 查询参数MAP取得
	 * 
	 * @param tableParamsDTO
	 * @throws Exception 
	 */
	private Map<String, Object> getSearchMap() throws Exception {
		// 参数MAP
		Map<String, Object> map = (Map<String, Object>)Bean2Map.toHashMap(form);
		// 用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 语言类型
		map.put(CherryConstants.SESSION_LANGUAGE, session
				.get(CherryConstants.SESSION_LANGUAGE));
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo
				.getBIN_OrganizationInfoID());
		// form参数设置到paramMap中
		ConvertUtil.setForm(form, map);
		return map;
	}
	
	 /**
	 * <p>
	 * 画面初期显示(详细画面)
	 * </p>
	 * 
	 * 
	 * @param 无
	 * @return String 跳转页面
	 * 
	 */
	
	public String detailInit() throws Exception {
    	Map<String, Object> stepMap = new HashMap<String, Object>();
    	// 活动类型
    	stepMap.put("campaignType", form.getCampaignType());
    	// 品牌ID
    	stepMap.put("brandInfoId", form.getBrandInfoId());
    	// 会员俱乐部ID
    	stepMap.put("memberClubId", form.getMemberClubId());
    	// 用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 所属组织
		stepMap.put(CherryConstants.ORGANIZATIONINFOID, userInfo
				.getBIN_OrganizationInfoID());
		stepMap.put(CherryConstants.ORGANIZATIONINFOID, userInfo
				.getBIN_OrganizationInfoID());
		// 取得规则体详细信息
		Map<String, Object> ruleDetailMap = binoljncom03IF.getRuleDetail(stepMap);
		// 所有步骤模板信息List
		List<Map<String, Object>> allCamTempList = new ArrayList<Map<String, Object>>();
		if(null != ruleDetailMap){
			if(null != ruleDetailMap.get("ruleDetail")){
				Map<String, Object> camTemps = (Map<String, Object>) JSONUtil.deserialize((String) ruleDetailMap.get("ruleDetail"));
				if (null != camTemps && !camTemps.isEmpty()) {
					allCamTempList = new ArrayList<Map<String, Object>>();
					for(Map.Entry<String, Object> en : camTemps.entrySet()){
						if ("baseParams".equals(en.getKey())) {
							Map<String, Object> baseParams = (Map<String, Object>) en.getValue();
							stepMap.putAll(baseParams);
							continue;
						}
						List<Map<String, Object>> camTempsList = (List<Map<String, Object>>) en.getValue();
						allCamTempList.addAll(camTempsList);
					}
				}
			}
		}
		stepMap.put("allCamTempList", allCamTempList);
        templateName = (String) stepMap.get("templateName");
		String templateInitBeanName = (String) stepMap.get("templateInitBean");
		TemplateInit_IF templateInitIF = getBeanByName(templateInitBeanName);
		stepMap.put("templateInitIF", templateInitIF);
		// 模板初期显示区分
		stepMap.put("tempInitKbn", "1");
    	camTempList = (List<Map<String, Object>>) binolcpcom02IF.convertCamTempList(stepMap);
    	// 配置页面标志
		form.setConfigKbn("1");
		return SUCCESS;
	}
	
	private <T> T getBeanByName(String name) {
		if (!CherryChecker.isNullOrEmpty(name)) {
			T t = (T) this.applicationContext.getBean(name);
			return t;
		}
		return null;
	}
}
