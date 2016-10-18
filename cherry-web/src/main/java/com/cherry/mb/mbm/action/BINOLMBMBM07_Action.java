/*
 * @(#)BINOLMBMBM07_Form.java     1.0 2012.12.07
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
package com.cherry.mb.mbm.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.Bean2Map;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.DateUtil;
import com.cherry.mb.mbm.bl.BINOLMBMBM07_BL;
import com.cherry.mb.mbm.form.BINOLMBMBM07_Form;
import com.googlecode.jsonplugin.JSONUtil;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 会员活动记录画面Action
 * 
 * @author WangCT
 * @version 1.0 2012.12.07
 */
public class BINOLMBMBM07_Action extends BaseAction implements ModelDriven<BINOLMBMBM07_Form> {

	private static final long serialVersionUID = 2039591282162827292L;
	
	/** 会员活动记录画面BL */
	@Resource
	private BINOLMBMBM07_BL binOLMBMBM07_BL;
	
	/** 系统配置项 共通BL */
	@Resource
	private BINOLCM14_BL binOLCM14_BL;
	
	/**
	 * 会员可参加活动画面初期显示
	 * 
	 * @return 会员可参加活动画面
	 */
	public String init() throws Exception {
		
		Map<String, Object> map = (Map)Bean2Map.toHashMap(form);
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		// 不是总部的场合
		if(userInfo.getBIN_BrandInfoID() != CherryConstants.BRAND_INFO_ID_VALUE) {
			// 所属品牌
			map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		}
		// 查询会员可参加活动List
		campaignInfoList = binOLMBMBM07_BL.getCurCampaignList(map);
		
		Map<String, Object> memMap = new HashMap<String, Object>();
		memMap.put("memberInfoId", form.getMemberInfoId());
		campMebJson = JSONUtil.serialize(memMap);
		
		return SUCCESS;
	}
	
	/**
	 * 
	 * 查询会员活动详细信息
	 * 
	 */
	public String searchDetail() throws Exception {
		
		Map<String, Object> paramMap = (Map)Bean2Map.toHashMap(form);
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 所属组织
		paramMap.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		// 所属品牌
		paramMap.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		if("0".equals(form.getActType())) {
			// 查询促销活动详细信息
			campaignDetailInfo = binOLMBMBM07_BL.getProCampaignDetail(paramMap);
			if(campaignDetailInfo != null && !campaignDetailInfo.isEmpty()) {
				// 促销活动套装折扣策略
				String configTZZK = binOLCM14_BL.getConfigValue("1054", 
						ConvertUtil.getString(paramMap.get(CherryConstants.ORGANIZATIONINFOID)), 
						ConvertUtil.getString(paramMap.get(CherryConstants.BRANDINFOID)));
				// 促销活动是否绑产品
				String bindProFlag = binOLCM14_BL.getConfigValue("1060", 
						ConvertUtil.getString(paramMap.get(CherryConstants.ORGANIZATIONINFOID)), 
						ConvertUtil.getString(paramMap.get(CherryConstants.BRANDINFOID)));
				// 虚拟促销品生成方式
				String virtualPrmFlag = binOLCM14_BL.getConfigValue("1068", 
						ConvertUtil.getString(paramMap.get(CherryConstants.ORGANIZATIONINFOID)), 
						ConvertUtil.getString(paramMap.get(CherryConstants.BRANDINFOID)));
				map = new HashMap<String, Object>();
				map.put("configTZZK", configTZZK);
				map.put("bindProFlag", bindProFlag);
				map.put("virtualPrmFlag", virtualPrmFlag);
				prmActiveRelList = (List)campaignDetailInfo.get("subResultInfoList");
			}
			return "PROACTDETAIL";
		} else {
			// 查询活动详细信息
			campaignDetailInfo = binOLMBMBM07_BL.getCampaignDetail(paramMap);
			return "ACTDETAIL";
		}
	}
	
	/**
	 * 会员参与活动履历画面初期显示
	 * 
	 * @return 会员参与活动履历画面
	 */
	public String campaignHistoryInit() throws Exception {
		
		return SUCCESS;
	}
	
	/**
	 * 查询会员参与活动履历
	 * 
	 */
	public String searchCampaignHistory() throws Exception {
		
		Map<String, Object> map = (Map)Bean2Map.toHashMap(form);
		ConvertUtil.setForm(form, map);
		if(form.getParticipateTimeStart() != null && !"".equals(form.getParticipateTimeStart())) {
			map.put("participateTimeStart", DateUtil.suffixDate(form.getParticipateTimeStart(), 0));
		}
		if(form.getParticipateTimeEnd() != null && !"".equals(form.getParticipateTimeEnd())) {
			map.put("participateTimeEnd", DateUtil.suffixDate(form.getParticipateTimeEnd(), 1));
		}
		// 查询会员参与活动履历总数
		int campaignHistoryCount = binOLMBMBM07_BL.getCampaignHistoryCount(map);
		form.setITotalDisplayRecords(campaignHistoryCount);
		form.setITotalRecords(campaignHistoryCount);
		if(campaignHistoryCount > 0) {
			// 查询会员参与活动履历List
			campaignHistoryList = binOLMBMBM07_BL.getCampaignHistoryList(map);
		}
		return SUCCESS;
	}
	
	/**
	 * 查询会员已预约活动
	 * 
	 * @return 会员已预约活动画面
	 */
	public String searchCampaignOrder() throws Exception {
		
		Map<String, Object> map = (Map)Bean2Map.toHashMap(form);
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		// 不是总部的场合
		if(userInfo.getBIN_BrandInfoID() != CherryConstants.BRAND_INFO_ID_VALUE) {
			// 所属品牌
			map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		}
		// 查询会员已预约活动List
		campaignOrderList = binOLMBMBM07_BL.getCampaignOrderList(map);
		
		return SUCCESS;
	}
	
	/**
	 * 查询预约活动明细
	 * 
	 * @return 预约活动明细画面
	 */
	public String searchOrderDetail() throws Exception {
		
		Map<String, Object> map = (Map)Bean2Map.toHashMap(form);
		
		// 查询会员已预约活动明细
		campaignOrderDetail = binOLMBMBM07_BL.getCampaignOrderDetail(map);
		
		return SUCCESS;
	}
	
	/** 会员可参加活动List */
	private List<Map<String, Object>> campaignInfoList;
	
	/** 会员活动详细 */
	private Map campaignDetailInfo;
	
	/** 会员参加活动履历List */
	private List<Map<String, Object>> campaignHistoryList;
	
	/** 促销活动结果信息List */
	private List<Map<String, Object>> prmActiveRelList;
	
	/** 促销活动结果信息 */
	private Map map;
	
	private String campMebJson;
	
	/** 已预约活动List */
	private List<Map<String, Object>> campaignOrderList;
	
	/** 预约活动明细 */
	private Map campaignOrderDetail;
	
	public List<Map<String, Object>> getCampaignInfoList() {
		return campaignInfoList;
	}

	public void setCampaignInfoList(List<Map<String, Object>> campaignInfoList) {
		this.campaignInfoList = campaignInfoList;
	}

	public Map getCampaignDetailInfo() {
		return campaignDetailInfo;
	}

	public void setCampaignDetailInfo(Map campaignDetailInfo) {
		this.campaignDetailInfo = campaignDetailInfo;
	}

	public List<Map<String, Object>> getCampaignHistoryList() {
		return campaignHistoryList;
	}

	public void setCampaignHistoryList(List<Map<String, Object>> campaignHistoryList) {
		this.campaignHistoryList = campaignHistoryList;
	}

	public List<Map<String, Object>> getPrmActiveRelList() {
		return prmActiveRelList;
	}

	public void setPrmActiveRelList(List<Map<String, Object>> prmActiveRelList) {
		this.prmActiveRelList = prmActiveRelList;
	}

	public Map getMap() {
		return map;
	}

	public void setMap(Map map) {
		this.map = map;
	}

	public String getCampMebJson() {
		return campMebJson;
	}

	public void setCampMebJson(String campMebJson) {
		this.campMebJson = campMebJson;
	}

	public List<Map<String, Object>> getCampaignOrderList() {
		return campaignOrderList;
	}

	public void setCampaignOrderList(List<Map<String, Object>> campaignOrderList) {
		this.campaignOrderList = campaignOrderList;
	}

	public Map getCampaignOrderDetail() {
		return campaignOrderDetail;
	}

	public void setCampaignOrderDetail(Map campaignOrderDetail) {
		this.campaignOrderDetail = campaignOrderDetail;
	}

	/** 会员活动记录画面Form */
	private BINOLMBMBM07_Form form = new BINOLMBMBM07_Form();

	@Override
	public BINOLMBMBM07_Form getModel() {
		return form;
	}

}
