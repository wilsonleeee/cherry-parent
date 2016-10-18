/*	
 * @(#)BINOLJNMAN05_Action.java     1.0 2012/10/30		
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
package com.cherry.jn.man.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM05_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.Bean2Map;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.dr.cmbussiness.util.DroolsConstants;
import com.cherry.jn.common.interfaces.BINOLJNCOM03_IF;
import com.cherry.jn.man.form.BINOLJNMAN05_Form;
import com.cherry.jn.man.interfaces.BINOLJNMAN05_IF;
import com.googlecode.jsonplugin.JSONUtil;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 积分规则配置一览Action
 * 
 * @author hub
 * @version 1.0 2012.10.30
 */
public class BINOLJNMAN05_Action extends BaseAction implements
ModelDriven<BINOLJNMAN05_Form>{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2159975689201139643L;

	/** 参数FORM */
	private BINOLJNMAN05_Form form = new BINOLJNMAN05_Form();
	
	@Resource
	private BINOLCM05_BL binOLCM05_BL;
	
	@Resource
	private BINOLJNMAN05_IF binoljnman05_IF;
	
	@Resource
	private BINOLJNCOM03_IF binoljncom03IF;
	
	/** 会员俱乐部List */
	private List<Map<String, Object>> clubList;
	
	@Override
	public BINOLJNMAN05_Form getModel() {
		// TODO Auto-generated method stub
		return form;
	}
	
	public List<Map<String, Object>> getClubList() {
		return clubList;
	}

	public void setClubList(List<Map<String, Object>> clubList) {
		this.clubList = clubList;
	}

	/**
	 * <p>
	 * 积分规则配置一览初期显示
	 * </p>
	 * 
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
		// 语言类型
		map.put(CherryConstants.SESSION_LANGUAGE, session
				.get(CherryConstants.SESSION_LANGUAGE));
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo
				.getBIN_OrganizationInfoID());
		// 品牌ID
		int brandInfoId = userInfo.getBIN_BrandInfoID();
		// 总部用户登录的时候
		if (CherryConstants.BRAND_INFO_ID_VALUE == brandInfoId) {
			map.put("noHeadKbn", "1");
			// 取得所管辖的品牌List
			List<Map<String, Object>> brandList = binOLCM05_BL.getBrandInfoList(map);
			form.setBrandInfoList(brandList);
			if (null != brandList && !brandList.isEmpty()) {
				map.put("brandInfoId", ((Map<String, Object>) 
						brandList.get(0)).get("brandInfoId"));
			}
		} else {
			map.put("brandInfoId", brandInfoId);
			// 取得品牌名称
			form.setBrandName(binOLCM05_BL.getBrandName(map));
			// 品牌ID
			form.setBrandInfoId(ConvertUtil.getString(brandInfoId));
		}
		clubList = binOLCM05_BL.getClubList(map);
		if (null != clubList && !clubList.isEmpty()) {
			String memberClubId = String.valueOf(clubList.get(0).get("memberClubId"));
			form.setMemberClubId(memberClubId);
		}
		return "success";
	}
	
	/**
	 * <p>
	 * 积分规则配置查询
	 * </p>
	 * 
	 * 
	 * @param 无
	 * @return String 跳转页面
	 * 
	 */
	public String search() throws Exception{
		Map<String, Object> baseMap = (Map<String, Object>) Bean2Map.toHashMap(form);
		ConvertUtil.setForm(form, baseMap);
		// 用户信息
		UserInfo userInfo = (UserInfo) session     
				.get(CherryConstants.SESSION_USERINFO);
		// 所属组织
		baseMap.put(CherryConstants.ORGANIZATIONINFOID, userInfo
				.getBIN_OrganizationInfoID());
		// 品牌信息ID
		baseMap.put(CherryConstants.BRANDINFOID, form.getBrandInfoId());
		// 活动类型:积分
		baseMap.put("campaignType", "3");
		// 取得规则配置件数 
		int count = binoljnman05_IF.getCampGrpCount(baseMap);
		if(count > 0){
			// 取得规则配置List
			List<Map<String, Object>> campGrpList = binoljnman05_IF.getCampGrpList(baseMap);
			// 规则配置List
			form.setCampGrpList(campGrpList);
		}
		// form表单设置
		form.setITotalDisplayRecords(count);
		form.setITotalRecords(count);
		return "success";
	}
	
	
    
    /**
	 * <p>
	 * 验证是否存在已启用的配置
	 * </p>
	 * 
	 * 
	 * @param 无
	 * @return String 组合规则信息
	 * @throws Exception 
	 * 
	 */
    public void confCheck() throws Exception{
    	Map<String, Object> map = new HashMap<String, Object>();
		// 用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo
				.getBIN_OrganizationInfoID());
		// 所属品牌
		map.put("brandInfoId", form.getBrandInfoId());
		// 会员活动类型：积分
    	map.put("campaignType", DroolsConstants.CAMPAIGN_TYPE3);
    	// 会员俱乐部ID
    	map.put("memberClubId", form.getMemberClubId());
    	// 取得有效的规则配置信息
    	Map<String, Object> validConfigInfo = binoljncom03IF.getValidConfigInfo(map);
		if (null != validConfigInfo && !validConfigInfo.isEmpty()) {
			// 验证结果：失败
			validConfigInfo.put("RESULT", "NG");
		} else {
			if (null == validConfigInfo) {
				validConfigInfo = new HashMap<String, Object>();
			}
			// 验证结果：成功
			validConfigInfo.put("RESULT", "OK");
		}
		// 响应JSON对象
		ConvertUtil.setResponseByAjax(response, JSONUtil.serialize(validConfigInfo));
    }
    
    /**
	 * <p>
	 * 停用/启用规则配置
	 * </p>
	 * 
	 * 
	 * @param 无
	 * @return String 跳转页面
	 * @throws Exception 
	 * 
	 */
    public String confValid() throws Exception{
    	Map<String, Object> map = new HashMap<String, Object>();
    	// 有效区分
    	map.put("validFlag", form.getValidFlag());
    	// 配置ID
    	map.put("campaignGrpId", form.getCampaignGrpId());
    	try{
    		Map<String, Object> configInfo = binoljncom03IF.getRuleConfigInfo(map);
    		// 品牌ID
    		map.put("brandInfoId", configInfo.get("brandInfoId"));
    		// 组织ID
    		map.put("organizationInfoId", configInfo.get("organizationInfoId"));
    		// 会员俱乐部ID
        	map.put("memberClubId", form.getMemberClubId());
    		// 规则类型
    		map.put("campaignType", "3");
			// 停用或者启用配置
    		binoljnman05_IF.tran_editValid(map);
		}catch (Exception e){
			// 操作失败
			this.addActionError(getText("ECM00005"));
			return CherryConstants.GLOBAL_ACCTION_RESULT;
		}
		// 操作成功
		this.addActionMessage(getText("ICM00002"));
		return CherryConstants.GLOBAL_ACCTION_RESULT;
    }
}
