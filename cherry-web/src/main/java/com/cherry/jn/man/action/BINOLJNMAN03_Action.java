/*	
 * @(#)BINOLJNMAN03_Action.java     1.0 2011/4/18		
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
import com.cherry.cp.common.dto.CampaignDTO;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.Bean2Map;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.jn.common.interfaces.BINOLJNCOM01_IF;
import com.cherry.jn.man.form.BINOLJNMAN03_Form;
import com.cherry.jn.man.interfaces.BINOLJNMAN03_IF;
import com.googlecode.jsonplugin.JSONUtil;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 确认与创建Action
 * 
 * @author hub
 * @version 1.0 2011.4.18
 */
public class BINOLJNMAN03_Action extends BaseAction implements
ModelDriven<BINOLJNMAN03_Form>{

	private static final long serialVersionUID = -1065996490281409962L;
	
	/** 参数FORM */
	private BINOLJNMAN03_Form form = new BINOLJNMAN03_Form();
	
	@Resource
    private BINOLJNCOM01_IF binoljncom01IF;
	
	@Resource
    private BINOLJNMAN03_IF binoljnman03IF;
	
	/** 活动模板List */
	private List<Map<String, Object>> camTempList;

	@Override
	public BINOLJNMAN03_Form getModel() {
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
	@SuppressWarnings("unchecked")
	public String init() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		// 会员活动DTO
		CampaignDTO campaignDTO = (CampaignDTO) session.get("campaignInfo");
		if (null != campaignDTO) {
			// 所属组织
			map.put("organizationInfoId", campaignDTO.getOrganizationInfoId());
			// 品牌ID
			map.put("brandInfoId", campaignDTO.getBrandInfoId());
		}
		map.put("pageId", "P00000000002");
		if (null != session.get("camTemps")) {
			List<Map<String, Object>> camTemps = (List<Map<String, Object>>) JSONUtil
			.deserialize(ConvertUtil.getString(session.get("camTemps")));
			// 取得页面对应的活动模板List
			camTempList = binoljncom01IF.convertCamTempList(map, camTemps);
		}
		return SUCCESS;
	}
	
	/**
	 * <p>
	 * 保存处理
	 * </p>
	 * 
	 * 
	 * @param 无
	 * @return String 跳转页面
	 * 
	 */
	@SuppressWarnings("unchecked")
	public String save() throws Exception {
		Map<String, Object> map = (Map<String, Object>)Bean2Map.toHashMap(form);
		map.put("camTemps", session.get("camTemps"));
		map.put("campaignInfo", session.get("campaignInfo"));
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
			// 取得更新会员活动相关表时所需要的参数
			binoljnman03IF.getCamTempParams(map);
			Map<String, Object> ruleInfo = new HashMap<String, Object>();
			ruleInfo.put("dtoName", "JoinDTO");
			ruleInfo.put("campaignType", "9");
			map.put("ruleInfo", ruleInfo);
			map.put("camTemps", session.get("camTemps"));
			map.put("relationInfo", session.get("relationInfo"));
			// 会员活动保存处理
			binoljnman03IF.tran_saveCampaign(map);
		} catch (CherryException e) {
			this.addActionError(e.getErrMessage());
			return CherryConstants.GLOBAL_ACCTION_RESULT_BODY;
		}
		// 处理成功
		this.addActionMessage(getText("ICM00002"));
		return CherryConstants.GLOBAL_ACCTION_RESULT_BODY;
	}

}
