/*	
 * @(#)BINOLCPCOM04_Action.java     1.0 2011/7/18		
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
package com.cherry.cp.common.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cp.common.dto.CampaignDTO;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.Bean2Map;
import com.cherry.cp.common.dto.RuleTestDTO;
import com.cherry.cp.common.form.BINOLCPCOM04_Form;
import com.cherry.cp.common.interfaces.BINOLCPCOM04_IF;
import com.cherry.cp.common.service.BINOLCPCOM02_Service;
import com.cherry.cp.common.util.CampUtil;
import com.googlecode.jsonplugin.JSONUtil;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 规则测试共通 Action
 * 
 * @author hub
 * @version 1.0 2011.7.18
 */
public class BINOLCPCOM04_Action extends BaseAction implements
ModelDriven<BINOLCPCOM04_Form>{
	
	private static final long serialVersionUID = -6112507765567999879L;
	
	/** 参数FORM */
	private BINOLCPCOM04_Form form = new BINOLCPCOM04_Form();
	
	@Resource
    private BINOLCPCOM04_IF binolcpcom04IF;
	
	@Resource
	private BINOLCPCOM02_Service binolcpcom02_Service;
	
	/** 测试模板List */
	private List<Map<String, Object>> camTempList;
	
	/** 等级List */
	private List<Map<String, Object>> memberList;
	
	/** 规则测试 DTO */
	private RuleTestDTO ruleTestDTO;
	
	@Override
	public BINOLCPCOM04_Form getModel() {
		// TODO Auto-generated method stub
		return form;
	}
	
	public List<Map<String, Object>> getCamTempList() {
		return camTempList;
	}

	public void setCamTempList(List<Map<String, Object>> camTempList) {
		this.camTempList = camTempList;
	}
	
	public RuleTestDTO getRuleTestDTO() {
		return ruleTestDTO;
	}

	public void setRuleTestDTO(RuleTestDTO ruleTestDTO) {
		this.ruleTestDTO = ruleTestDTO;
	}

	public List<Map<String, Object>> getMemberList() {
		return memberList;
	}

	public void setMemberList(List<Map<String, Object>> memberList) {
		this.memberList = memberList;
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
		// 测试模板List
		camTempList = binolcpcom04IF.getTemplateList(map);
		// 模板名称
		String templateName = CampUtil.TEMPLATE_BYTYPE.getTemplateName(form.getCampaignType());
		form.setTemplateName(templateName);
		// 取得等级信息
		memberList = binolcpcom02_Service.getLevelList(map);
		return SUCCESS;
	}
	
	/**
	 * <p>
	 * 开始测试
	 * </p>
	 * 
	 * 
	 * @param 无
	 * @return String 跳转页面
	 * 
	 */
	
	public String startRule() throws Exception {
		Map<String, Object> map = (Map<String, Object>)Bean2Map.toHashMap(form);
		map.put("campaignRule", form.getCampaignRule());
		// 用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 语言类型
		map.put(CherryConstants.SESSION_LANGUAGE, session
				.get(CherryConstants.SESSION_LANGUAGE));
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo
				.getBIN_OrganizationInfoID());
		try {
			// 加载区分
			String loadKbn = (String) map.get("loadKbn");
			if ("1".equals(loadKbn)) {
				CampaignDTO campaignDTO = (CampaignDTO) session.get("campInfo");
				if (null != campaignDTO) {
					// 规则名称
					map.put("campInfo", campaignDTO);
					// 规则名称
					map.put("campaignName", campaignDTO.getCampaignName());
					// 会员活动组ID
					map.put("campaignGrpId", campaignDTO.getCampaignGrpId());
					// 所有步骤模板信息List
	    			List<Map<String, Object>> allCamTempList = new ArrayList<Map<String, Object>>();
					for (int i = 0; ; i++) {
						// 模板Key
			    		String key = "camTemps" + i;
			    		String camTempsInfo = (String) session.get(key);
						if (null != camTempsInfo && !"".equals(camTempsInfo)) {
							List<Map<String, Object>> camTemps = (List<Map<String, Object>>) 
	            			JSONUtil.deserialize(camTempsInfo);
	            			if (null != camTemps && !camTemps.isEmpty()) {
	            				allCamTempList.addAll(camTemps);
	            			}
			    		} else {
			    			break;
			    		}
					}
					// 规则体详细
					map.put("ruleDetailList", allCamTempList);
				}
			}
			// 测试规则
			ruleTestDTO = binolcpcom04IF.executeRule(map);
			if (null == ruleTestDTO) {
				throw new CherryException("ECP00009");
			}
		} catch (CherryException e) {
			this.addActionError(e.getErrMessage());
			return CherryConstants.GLOBAL_ACCTION_RESULT_BODY;
		}
		return SUCCESS;
	}
}
