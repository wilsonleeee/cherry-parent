/*  
 * @(#)BINOLMOMAN08_Action.java     1.0 2012/11/15      
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
package com.cherry.mo.man.action;

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
import com.cherry.cm.util.ConvertUtil;
import com.cherry.mo.man.form.BINOLMOMAN08_Form;
import com.cherry.mo.man.interfaces.BINOLMOMAN08_IF;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 
 * POS配置项Action
 * 
 */
@SuppressWarnings("unchecked")
public class BINOLMOMAN08_Action extends BaseAction implements
		ModelDriven<BINOLMOMAN08_Form> {

	/** 参数FORM */
	private BINOLMOMAN08_Form form = new BINOLMOMAN08_Form();

	/** 品牌List */
	private List<Map<String, Object>> brandInfoList;

	/** 共通BL */
	@Resource(name = "binOLCM05_BL")
	private BINOLCM05_BL binOLCM05_BL;

	@Resource(name = "binOLMOMAN08_BL")
	private BINOLMOMAN08_IF binOLMOMAN08_BL;

	/** 配置項List 新数据 */
	private List<Map<String, Object>> posConfigNewInfoList;

	/** 配置項List 筛选后数据 */
	private List posConfigInfoList;

	private Map posConfig;

	/**
	 * <p>
	 * 画面初期显示
	 * </p>
	 * 
	 * @param 无
	 * @return String 跳转页面
	 * 
	 */
	public String init() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		// 用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 总部的场合
		if (userInfo.getBIN_BrandInfoID() == CherryConstants.BRAND_INFO_ID_VALUE) {
			// 语言类型
			String language = (String) session
					.get(CherryConstants.SESSION_LANGUAGE);
			// 所属组织
			map.put(CherryConstants.ORGANIZATIONINFOID,
					userInfo.getBIN_OrganizationInfoID());
			// 用户ID
			map.put(CherryConstants.USERID, userInfo.getBIN_UserID());
			// 语言类型
			map.put(CherryConstants.SESSION_LANGUAGE, language);

			// 取得品牌List
			brandInfoList = binOLCM05_BL.getBrandInfoList(map);
		} else {
			Map<String, Object> brandMap = new HashMap<String, Object>();
			// 品牌ID
			brandMap.put("brandInfoId", userInfo.getBIN_BrandInfoID());
			// 品牌名称
			brandMap.put("brandName", userInfo.getBrandName());
			brandInfoList = new ArrayList<Map<String, Object>>();
			brandInfoList.add(brandMap);
		}
		return SUCCESS;
	}

	/**
	 * <p>
	 * POS配置项查询
	 * </p>
	 * 
	 * @return
	 */
	public String search() throws Exception {

		// 取得参数MAP
		Map<String, Object> searchMap = getSearchMap();

		// 取得配置项总数
		int count = binOLMOMAN08_BL.searchMachineInfoCount(searchMap);

		// 取得配置项List
		if (count > 0) {

			// 配置項List 新数据
			posConfigInfoList = binOLMOMAN08_BL
					.searchPosConfigNewInfoList(searchMap);

		}
		// form表单设置
		form.setITotalDisplayRecords(count);
		form.setITotalRecords(count);
		return "BINOLMOMAN08_1";
	}

	/**
	 * 查询参数MAP取得
	 * 
	 * @param tableParamsDTO
	 */
	private Map<String, Object> getSearchMap() {
		// 参数MAP
		Map<String, Object> map = new HashMap<String, Object>();
		// 用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// form参数设置到paramMap中
		ConvertUtil.setForm(form, map);
		// 用户ID
		map.put(CherryConstants.USERID, userInfo.getBIN_UserID());
		// 语言类型
		map.put(CherryConstants.SESSION_LANGUAGE,
				session.get(CherryConstants.SESSION_LANGUAGE));
		// 所属品牌不存在的场合
		if (form.getBrandInfoId() == null || "".equals(form.getBrandInfoId())) {
			// 不是总部的场合
			if (userInfo.getBIN_BrandInfoID() != CherryConstants.BRAND_INFO_ID_VALUE) {
				// 所属品牌
				map.put(CherryConstants.BRANDINFOID,
						userInfo.getBIN_BrandInfoID());
			}
		} else {
			map.put(CherryConstants.BRANDINFOID, form.getBrandInfoId());
		}
		// 组织ID
		map.put("organizationInfoId", userInfo.getBIN_OrganizationInfoID());

		map.put("configCode", form.getConfigCode());

		map.put("configNote", form.getConfigNote());

		return map;
	}

	/**
	 * <p>
	 * 编辑画面初期显示
	 * </p>
	 * 
	 * @param 无
	 * @return String 跳转页面
	 * 
	 */
	public String editinit() throws Exception {

		// 取得要编辑的配置项ID
		int posConfigID = form.getPosConfigID();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("posConfigID", posConfigID);
		// 根据记录ID查询配置项INFO
		posConfig = binOLMOMAN08_BL.getPosConfig(map);
		return "BINOLMOMAN08_2";
	}
	
    /**
     * <p>
     * 新增画面初期显示
     * </p>
     * 
     * @param 无
     * @return String 跳转页面
     * 
     */
    public String add() throws Exception {
		return "BINOLMOMAN08_4";  	
    }
    
	/**
	 * <p>
	 * 编辑确认保存
	 * </p>
	 * 
	 * @param 无
	 * @return String 跳转页面
	 * 
	 */
	public String save() throws Exception {
		
		String opt="";
		
		int organizationInfoId = 0;
		if(form.getOrganizationInfoId()!=null){			
		
		organizationInfoId =form.getOrganizationInfoId();	    
	    opt="update";
		}else{
		 opt="add";
		} 

		// 用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);

		Map<String, Object> map = new HashMap<String, Object>();

		// 品牌代码
		map.put("BrandCode", userInfo.getBrandCode());
		try {
			if(opt.equals("update")){//编辑
							
				// 判断是否是原始数据
				if (organizationInfoId == -9999) {
	
					// 组织ID
					organizationInfoId = userInfo.getBIN_OrganizationInfoID();
					map.put("organizationInfoId", organizationInfoId);
					// 品牌ID
					int brandInfoId = userInfo.getBIN_BrandInfoID();
					map.put("brandInfoId", brandInfoId);
					// 配置项代码
					String configCode = form.getConfigCode();
					map.put("configCode", configCode);
					// 配置项说明
					String configNote = form.getConfigNote();
					map.put("configNote", configNote);
					// 配置结果
					String configValue = form.getConfigValue();
					map.put("configValue", configValue);
					// 配置类型
					String configType = form.getConfigType();
					map.put("configType", configType);
					// 是否有效区分
					String validFlag = form.getValidFlag();
					map.put("validFlag", validFlag);
					binOLMOMAN08_BL.tran_addPosConfig(map);
	
				} else {
			        //组织ID
			        map.put("organizationInfoId", userInfo.getBIN_OrganizationInfoID());
			        //品牌ID
			        map.put("brandInfoId", userInfo.getBIN_BrandInfoID());
					// 配置结果
					String configValue = form.getConfigValue();
					map.put("configValue", configValue);
	
					// 配置项ID
					int posConfigID = form.getPosConfigID();
					map.put("posConfigID", posConfigID);
					binOLMOMAN08_BL.tran_updatePosConfig(map);
				}
				
			//新增	
			}else{
				// 组织ID
				organizationInfoId = userInfo.getBIN_OrganizationInfoID();
				map.put("organizationInfoId", organizationInfoId);
				// 品牌ID
				int brandInfoId = userInfo.getBIN_BrandInfoID();
				map.put("brandInfoId", brandInfoId);
				// 配置项代码
				String configCode = form.getConfigCode();
				map.put("configCode", configCode);
				// 配置项说明
				String configNote = form.getConfigNote();
				map.put("configNote", configNote);
				// 配置结果
				String configValue = form.getConfigValue();
				map.put("configValue", configValue);
				// 配置类型
				String configType = form.getConfigType();
				map.put("configType", configType);
				// 是否有效区分
				String validFlag = form.getValidFlag();
				map.put("validFlag", 1);
				binOLMOMAN08_BL.tran_addPosConfig(map);
				
				// 组织ID（默认组织-9999）
				map.put("organizationInfoId", "-9999");
				// 品牌ID（默认品牌-9999）
				map.put("brandInfoId", "-9999");
				binOLMOMAN08_BL.tran_addPosConfig(map);
			}
		} catch (Exception e) {
			if (e instanceof CherryException) {
				CherryException temp = (CherryException) e;
				this.addActionError(temp.getErrMessage());
				return CherryConstants.GLOBAL_ACCTION_RESULT;
			} else {
				this.addActionError(e.getMessage());
				return CherryConstants.GLOBAL_ACCTION_RESULT;
			}
		}
		return "BINOLMOMAN08_3";

	}

	public List getPosConfigInfoList() {
		return posConfigInfoList;
	}

	public void setPosConfigInfoList(List posConfigInfoList) {
		this.posConfigInfoList = posConfigInfoList;
	}

	public Map getPosConfig() {
		return posConfig;
	}

	public void setPosConfig(Map posConfig) {
		this.posConfig = posConfig;
	}

	@Override
	public BINOLMOMAN08_Form getModel() {
		return form;
	}

}
