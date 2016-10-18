/*	
 * @(#)BINOLJNMAN15_Action.java     1.0 2013/08/28		
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM05_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.Bean2Map;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.jn.man.form.BINOLJNMAN15_Form;
import com.cherry.jn.man.interfaces.BINOLJNMAN15_IF;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 积分清零 Action
 * 
 * @author hub
 * @version 1.0 2013.08.28
 */
public class BINOLJNMAN15_Action extends BaseAction implements
ModelDriven<BINOLJNMAN15_Form>{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8238017541607391403L;
	/** 参数FORM */
	private BINOLJNMAN15_Form form = new BINOLJNMAN15_Form();
	
	@Resource
	private BINOLCM05_BL binOLCM05_BL;
	
	@Resource
	private BINOLJNMAN15_IF binoljnman15_IF;
	
	/** 会员俱乐部List */
	private List<Map<String, Object>> clubList;
	
	private static final Logger logger = LoggerFactory.getLogger(BINOLJNMAN15_Action.class);
		
	public List<Map<String, Object>> getClubList() {
		return clubList;
	}

	public void setClubList(List<Map<String, Object>> clubList) {
		this.clubList = clubList;
	}

	@Override
	public BINOLJNMAN15_Form getModel() {
		return form;
	}
	
	/**
	 * <p>
	 * 积分规则一览初期显示
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
		// 积分清零
		form.setCampaignType("8888");
		clubList = binOLCM05_BL.getClubList(map);
		if (null != clubList && !clubList.isEmpty()) {
			String memberClubId = String.valueOf(clubList.get(0).get("memberClubId"));
			form.setMemberClubId(memberClubId);
		}
		return SUCCESS;
	}
	
	/**
	 * <p>
	 * 积分规则查询
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
		// 活动类型
		baseMap.put("campaignType", form.getCampaignType());
		int count = binoljnman15_IF.getCampaignRuleCount(baseMap);
		if(count > 0){
			List<Map<String, Object>> camtempList = binoljnman15_IF.getCampaignList(baseMap);
			// 取得积分活动信息List
			form.setCamtempList(camtempList);
		}
		// form表单设置
		form.setITotalDisplayRecords(count);
		form.setITotalRecords(count);
		return SUCCESS;
	}
	
	/**
	 * <p>
	 * 停用/启用规则
	 * </p>
	 * 
	 * 
	 * @param 无
	 * @return String 跳转页面
	 * @throws Exception 
	 * 
	 */
    public String ruleValid() throws Exception{
    	Map<String, Object> map = new HashMap<String, Object>();
    	// 有效区分
    	map.put("validFlag", form.getValidFlag());
    	// 活动ID
    	map.put("campaignId", form.getCampaignId());
    	try{
			// 停用或者启用规则
    		binoljnman15_IF.tran_editValid(map);
		}catch (Exception e){
			logger.error(e.getMessage(),e);
			// 操作失败
			this.addActionError(getText("ECM00005"));
			return CherryConstants.GLOBAL_ACCTION_RESULT;
		}
		// 操作成功
		this.addActionMessage(getText("ICM00002"));
		return CherryConstants.GLOBAL_ACCTION_RESULT;
    }
}
