/*	
 * @(#)BINOLJNMAN02_Action.java     1.0 2011/4/18		
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

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM00_BL;
import com.cherry.cp.common.dto.CampaignDTO;
import com.cherry.cp.common.dto.CampaignRuleDTO;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.DateUtil;
import com.cherry.jn.common.action.BINOLJNCOM01_Action;
import com.cherry.jn.common.interfaces.BINOLJNCOM01_IF;
import com.cherry.jn.man.form.BINOLJNMAN02_Form;
import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 设定规则条件Action
 * 
 * @author hub
 * @version 1.0 2011.4.18
 */
public class BINOLJNMAN02_Action extends BINOLJNCOM01_Action implements
ModelDriven<BINOLJNMAN02_Form>{

	private static final long serialVersionUID = -3840981549276606117L;
	
	/** 参数FORM */
	private BINOLJNMAN02_Form form = new BINOLJNMAN02_Form();
	
	@Resource
    private BINOLJNCOM01_IF binoljncom01IF;
	
	@Resource
	private BINOLCM00_BL binolcm00BL;
	
	/** 活动模板List */
	private List<Map<String, Object>> camTempList;
	
	/** 节日 */
	private String holidays;
	
	public String getHolidays() {
		return holidays;
	}

	public void setHolidays(String holidays) {
		this.holidays = holidays;
	}
	
	@Override
	public BINOLJNMAN02_Form getModel() {
		return form;
	}
	
	public List<Map<String, Object>> getCamTempList() {
		return camTempList;
	}

	public void setCamTempList(List<Map<String, Object>> camTempList) {
		this.camTempList = camTempList;
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
		// 用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		Map<String, Object> map = new HashMap<String, Object>();
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo
				.getBIN_OrganizationInfoID());
		// 品牌ID
		map.put("brandInfoId", form.getBrandInfoId());
		// 页面ID
		map.put("pageId", "P00000000001");
		// 取得页面对应的活动模板List
		camTempList = binoljncom01IF.searchCamTempList(map);
		if (null != camTempList) {
			for (Map<String, Object> camTemp : camTempList) {
				// 模板编号
				String tempCode = (String) camTemp.get("tempCode");
				if ("BUS000001".equals(tempCode)) {
					// 默认选中单次购买
					camTemp.put("isChecked", "1");
				}
			}
		}
		// 创建会员活动DTO
		CampaignDTO campaignDTO = createCampaignDTO(1);
		session.put("campaignInfo", campaignDTO);
		// 查询假日
		holidays = binolcm00BL.getHolidays(map);
		return SUCCESS;
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
	public String edit() throws Exception {
		// 创建会员活动DTO
		CampaignDTO campaignDTO = createCampaignDTO(2);
		// 子活动DTO
		CampaignRuleDTO campaignRuleInfo = campaignDTO.getCampaignRule();
		session.put("campaignInfo", campaignDTO);
		// 规则体详细
		String camTempStr = campaignRuleInfo.getRuleDetail();
		convertCamTempList(camTempStr);
		Map<String, Object> map = new HashMap<String, Object>();
		// 用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo
				.getBIN_OrganizationInfoID());
		// 查询假日
		holidays = binolcm00BL.getHolidays(map);
		return SUCCESS;
	}
	
	/**
	 * <p>
	 * 下一步处理
	 * </p>
	 * 
	 * 
	 * @param 无
	 * @return String 跳转页面
	 * 
	 */
	public String next() throws Exception {
		validateForm(form.getCamTemps());
		if (!isCorrect || !BUS000001_isChecked) {
			if (!BUS000001_isChecked) {
				this.addActionError("必须选择一种购买条件");
			}
			convertCamTempList(form.getCamTemps());
			return INPUT;
		}
		session.put("camTemps", form.getCamTemps());
		session.put("relationInfo", form.getRelationInfo());
		return SUCCESS;
	}
	
	/**
	 * <p>
	 * 后一张页面返回显示
	 * </p>
	 * 
	 * 
	 * @param 无
	 * @return String 跳转页面
	 * 
	 */
	public String backInit() throws Exception {
		convertCamTempList((String) session.get("camTemps"));
		session.remove("camTemps");
		return SUCCESS;
	}
	
	/**
	 * <p>
	 * 后一张页面返回显示
	 * </p>
	 * 
	 * 
	 * @param String 活动模板信息
	 * @return String 跳转页面
	 * 
	 */
	@SuppressWarnings("unchecked")
	private void convertCamTempList(String camTempStr) throws JSONException {
		if (null != camTempStr) {
			Map<String, Object> map = new HashMap<String, Object>();
			// 会员活动DTO
			CampaignDTO campaignDTO = (CampaignDTO) session.get("campaignInfo");
			if (null != campaignDTO) {
				// 所属组织
				map.put("organizationInfoId", campaignDTO.getOrganizationInfoId());
				// 品牌ID
				map.put("brandInfoId", campaignDTO.getBrandInfoId());
				// 页面ID
				map.put("pageId", "P00000000001");
				List<Map<String, Object>> camTemps = (List<Map<String, Object>>) JSONUtil
				.deserialize(camTempStr);
				// 取得页面对应的活动模板List
				camTempList = binoljncom01IF.convertCamTempList(map, camTemps);
			}
		}
	}
	
	/**
	 * <p>
	 * 创建会员活动DTO
	 * </p>
	 * 
	 * 
	 * @param 
	 * @return CampaignDTO 会员活动DTO
	 * 
	 */
	private CampaignDTO createCampaignDTO(int operKbn) {
		// 会员活动DTO
		CampaignDTO campaignInfo = new CampaignDTO();
		// 用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 操作区分
		campaignInfo.setOperKbn(operKbn);
		// 所属组织
		campaignInfo.setOrganizationInfoId(ConvertUtil.getString(userInfo
				.getBIN_OrganizationInfoID()));
		// 所属品牌
		campaignInfo.setBrandInfoId(form.getBrandInfoId());
		// 会员等级组ID
		campaignInfo.setMemberLevelGroupId(form.getGroupId());
		// 活动有效期开始日期
//		Date campaignFromDate = DateUtil.coverString2Date(form.getCampaignFromDate());
//		// 活动有效期结束日期
//		Date campaignToDate = DateUtil.coverString2Date(form.getCampaignToDate());
//		campaignInfo.setCampaignFromDate(campaignFromDate);
//		campaignInfo.setCampaignToDate(campaignToDate);
		// 子活动DTO
		CampaignRuleDTO campaignRuleInfo = new CampaignRuleDTO();
		// 会员活动ID
		String campaignId = form.getCampaignId();
		if (null != campaignId && !"".equals(campaignId)) {
			campaignInfo.setCampaignId(Integer.parseInt(campaignId));
			campaignRuleInfo.setCampaignId(Integer.parseInt(campaignId));
		}
		// 子活动ID
		String campaignRuleId = form.getCampaignRuleId();
		if (null != campaignRuleId && !"".equals(campaignRuleId)) {
			campaignRuleInfo.setCampaignRuleId(Integer.parseInt(campaignRuleId));
			Map<String, Object> map = new HashMap<String, Object>();
			// 子活动ID
			map.put("campaignRuleId", campaignRuleId);
			// 取得会员子活动信息
			Map<String, Object> campaignRuleMap = binoljncom01IF.getCampaignRuleMap(map);
			if (null != campaignRuleMap && !campaignRuleMap.isEmpty()) {
				// 规则体详细
				campaignRuleInfo.setRuleDetail((String) campaignRuleMap.get("ruleDetail"));
				// 更新前更新日时
				campaignRuleInfo.setRuleUpdateTime((String) campaignRuleMap.get("ruleUpdateTime"));
				// 更新前更新次数
			//	campaignRuleInfo.setRuleModifyCount((ConvertUtil.getString(campaignRuleMap.get("ruleModifyCount"))));
			}
		}
		// 会员等级ID
//		campaignRuleInfo.setMemberLevelId(form.getMemberLevelId());
//		// 会员等级级别
//		campaignRuleInfo.setMemberLevelGrade(form.getMemberLevelGrade());
		campaignInfo.setCampaignRule(campaignRuleInfo);
		return campaignInfo;
	}
}
