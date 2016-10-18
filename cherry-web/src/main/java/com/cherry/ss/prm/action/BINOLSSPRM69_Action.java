/*		
 * @(#)BINOLSSPRM69_Action.java     1.0 2014/02/10		
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM05_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.ss.prm.bl.BINOLSSPRM69_BL;
import com.cherry.ss.prm.form.BINOLSSPRM69_Form;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 智能促销配置
 * @author lipc
 * @version 1.0 2014.02.10
 */
public class BINOLSSPRM69_Action extends BaseAction implements ModelDriven<BINOLSSPRM69_Form>{
	
	private static final Logger logger = LoggerFactory.getLogger(BINOLSSPRM69_Action.class);
	
	private static final long serialVersionUID = -4155552754712330635L;
	
	private BINOLSSPRM69_Form form = new BINOLSSPRM69_Form();
	
	private String brandName;
	
	private String brandId;
	
	/** 促销规则List **/
	private List<Map<String,Object>> ruleList;
	
	/** 排他关系List **/
	private List<Map<String, Object>> ruleRelationList;
	
	/** 排他关系分组List **/
	private List<Map<String, Object>> ruleRelationGroupList;
	
	/** 排他关系关联值的选项值 **/
	private List<Map<String, Object>> optionList;
	
	/** 单条排他关系 **/
	@SuppressWarnings("rawtypes")
	private Map ruleRelationGroupMap;
	
	/** 画面传入参数 **/
	private Map<String, String> params;
	
	@Resource(name="binOLSSPRM69_BL")
	private BINOLSSPRM69_BL prm69_BL;
	
	@Resource(name="binOLCM05_BL")
	private BINOLCM05_BL binOLCM05_BL;
	
	/**
	 * 初始化主画面
	 * @return
	 */
	public String init(){
		return SUCCESS;
	}
	
	/**
	 * 设置规则等级
	 * @return
	 */
	public String saveLevel() {
		try {
			Map<String,Object> map = getCommMap();
			map.putAll(params);
			prm69_BL.tran_updatePrmRule(map);
			this.addActionMessage(getText("ICM00002"));
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			this.addActionError(getText("ECM00089"));
		}
		return CherryConstants.GLOBAL_ACCTION_RESULT;
	}
	
	/**
	 * 保存排他关系
	 * @return
	 */
	public String saveRelation() {
		try {
			Map<String,Object> map = getCommMap();
			map.putAll(params);
			prm69_BL.tran_savePrmRuleRelation(map);
			this.addActionMessage(getText("ICM00002"));
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			this.addActionError(getText("ECM00089"));
		}
		return CherryConstants.GLOBAL_ACCTION_RESULT_PAGE;
	}
	
	/**
	 * 停用或启用排他关系分组
	 * @return
	 */
	public String disOrEnableRelation() {
		try {
			Map<String, Object> map = this.getCommMap();
			map.putAll(params);
			prm69_BL.disOrEnableRelation(map);
			this.addActionMessage(getText("ICM00002"));
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			this.addActionError(getText("ECM00089"));
		}
		return CherryConstants.GLOBAL_ACCTION_RESULT;
	}
	
	/**
	 * 初始化画面
	 * @return
	 */
	public String initPage() {
		try {
			Map<String,Object> map = getCommMap();
			brandName = ConvertUtil.getString(map.get(CherryConstants.BRAND_NAME));
			int pageNo = ConvertUtil.getInt(params.get("pageNo"));
			if(pageNo == 0){
				//等级调整初始化
				ruleList = prm69_BL.getPrmRuleList(map);
			}else if(pageNo == 1){
				//排他关系分组画面初始化
				ruleRelationGroupList = prm69_BL.getPrmRuleRelationGroupList(map);
			}else if(pageNo == 2){
				//配置排他关系画面初始化
				String groupNo = params.get("groupNo");
				if(!CherryChecker.isNullOrEmpty(groupNo, true)){
					map.put("groupNo", groupNo);
					ruleRelationList = prm69_BL.getPrmRuleRelationList(map);
					ruleRelationGroupMap = prm69_BL.getPrmRuleRelationGroup(map);
				}
			}else if(pageNo == 3){
				//选择排他关系画面初始化
				ConvertUtil.setForm(form, map);
				map.putAll(params);
				optionList = prm69_BL.getRelationOptions(map);
				int count = prm69_BL.getRelationOptionCount(map);
				if(count > 0){
					optionList = prm69_BL.getRelationOptions(map);
				}
				form.setITotalDisplayRecords(count);
				form.setITotalRecords(count);
			}else if(pageNo == 4){
				//排他关系初始化
			}else if(pageNo == 5){
				String groupNo = params.get("groupNo");
				if(!CherryChecker.isNullOrEmpty(groupNo, true)){
					map.put("groupNo", groupNo);
					ruleRelationList = prm69_BL.getPrmRuleRelationList(map);
					ruleRelationGroupMap = prm69_BL.getPrmRuleRelationGroup(map);
				}
			}
			return "page_"+pageNo;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			this.addActionError(getText("ECM00036"));
			return CherryConstants.GLOBAL_ACCTION_RESULT_PAGE;
		}
	}
	
	/**
	 * 取得共通参数Map
	 * 
	 * @return
	 */
	private Map<String, Object> getCommMap() {
		Map<String, Object> map = new HashMap<String, Object>();
		// 用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 语言
			map.put(CherryConstants.USERID, userInfo.getBIN_UserID());
		// 语言
		map.put(CherryConstants.SESSION_LANGUAGE, session
				.get(CherryConstants.SESSION_LANGUAGE));
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo
				.getBIN_OrganizationInfoID());
		// 所属品牌
		map.put(CherryConstants.BRANDINFOID, brandId);
		// 所属品牌
		map.put(CherryConstants.BRAND_NAME, binOLCM05_BL.getBrandName(map));
		// 系统时间
		map.put("sysTime", prm69_BL.getSYSDateTime());
		return map;
	}


	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public List<Map<String, Object>> getRuleList() {
		return ruleList;
	}

	public void setRuleList(List<Map<String, Object>> ruleList) {
		this.ruleList = ruleList;
	}

	public Map<String, String> getParams() {
		return params;
	}

	public void setParams(Map<String, String> params) {
		this.params = params;
	}

	public List<Map<String, Object>> getRuleRelationList() {
		return ruleRelationList;
	}

	public void setRuleRelationList(List<Map<String, Object>> ruleRelationList) {
		this.ruleRelationList = ruleRelationList;
	}

	public List<Map<String, Object>> getRuleRelationGroupList() {
		return ruleRelationGroupList;
	}

	public void setRuleRelationGroupList(List<Map<String, Object>> ruleRelationGroupList) {
		this.ruleRelationGroupList = ruleRelationGroupList;
	}

	@SuppressWarnings("rawtypes")
	public Map getRuleRelationGroupMap() {
		return ruleRelationGroupMap;
	}

	@SuppressWarnings("rawtypes")
	public void setRuleRelationGroupMap(Map ruleRelationGroupMap) {
		this.ruleRelationGroupMap = ruleRelationGroupMap;
	}

	public String getBrandId() {
		return brandId;
	}

	public void setBrandId(String brandId) {
		this.brandId = brandId;
	}

	public List<Map<String, Object>> getOptionList() {
		return optionList;
	}

	public void setOptionList(List<Map<String, Object>> optionList) {
		this.optionList = optionList;
	}

	@Override
	public BINOLSSPRM69_Form getModel() {
		return form;
	}
}
