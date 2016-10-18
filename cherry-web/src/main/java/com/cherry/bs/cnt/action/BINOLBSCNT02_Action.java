/*	
 * @(#)BINOLBSCNT02_Action.java     1.0 2011/05/09		
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

package com.cherry.bs.cnt.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.bs.cnt.bl.BINOLBSCNT02_BL;
import com.cherry.bs.cnt.form.BINOLBSCNT02_Form;
import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.ConvertUtil;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 
 * 	柜台详细画面Action
 * 
 * @author WangCT
 * @version 1.0 2011.05.009
 */
public class BINOLBSCNT02_Action extends BaseAction implements ModelDriven<BINOLBSCNT02_Form> {
	
	private static final long serialVersionUID = -8087314934778206721L;
	
	/** 柜台详细画面Form */
	private BINOLBSCNT02_Form form = new BINOLBSCNT02_Form();
	
	/** 柜台详细画面BL */
	@Resource
	private BINOLBSCNT02_BL binOLBSCNT02_BL;
	
	@Resource
	private BINOLCM14_BL binOLCM14_BL;
	
	/**
	 * 
	 * 画面初期显示
	 * 
	 * @param 无
	 * @return String 柜台详细画面
	 * 
	 */
	public String init() throws Exception {
		
		Map<String, Object> map = new HashMap<String, Object>();
		// 用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 语言
		String language = (String)session.get(CherryConstants.SESSION_LANGUAGE);
		if(language != null) {
			map.put(CherryConstants.SESSION_LANGUAGE, language);
		}
		// 柜台ID
		map.put("counterInfoId", form.getCounterInfoId());
		// 部门ID
		map.put("organizationId", form.getOrganizationId());
		// 柜台号
		map.put("counterCode", form.getCounterCode());
		// 取得柜台详细信息
		counterInfo = binOLBSCNT02_BL.getCounterInfo(map);
		if(counterInfo == null || counterInfo.isEmpty()) {
			this.addActionError(getText("EBS00020"));
            return SUCCESS;
		}
		
		//是否维护柜台密码
		form.setMaintainPassWord(binOLCM14_BL.isConfigOpen("1049", userInfo.getBIN_OrganizationInfoID(), (Integer)counterInfo.get(CherryConstants.BRANDINFOID)));
		
		//是否支持柜台协同
		form.setMaintainCoutSynergy(binOLCM14_BL.isConfigOpen("1050", userInfo.getBIN_OrganizationInfoID(), (Integer)counterInfo.get(CherryConstants.BRANDINFOID)));
		
		return SUCCESS;
	}
	
	/**
	 * 查询柜台事件信息
	 * 
	 * @return String 柜台事件信息画面
	 */
	public String searchCouEvent() {
		// 取得参数MAP
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("counterInfoId", form.getCounterInfoId());
		// form参数设置到map中
		ConvertUtil.setForm(form, map);
		// 取得柜台事件信息总数
		int count = binOLBSCNT02_BL.getCounterEventCount(map);
		if(count > 0) {
			// 取得柜台事件信息List
			counterEventList = binOLBSCNT02_BL.getCounterEventList(map);
		}
		// form表单设置
		form.setITotalDisplayRecords(count);
		form.setITotalRecords(count);
		return SUCCESS;
	}
	
	/**
	 * 查询柜台方案信息
	 * 
	 * @return String 柜台方案信息画面
	 */
	public String searchCouSolution() {
		// 取得参数MAP
		Map<String, Object> map = new HashMap<String, Object>();

		map.put("counterInfoId", form.getCounterInfoId());
		map.put("counterCode", form.getCounterCode());

		//map.put("counterCode",form.getCounterCode());
		// form参数设置到map中
		ConvertUtil.setForm(form, map);
		// 方案总数取得
		int count = binOLBSCNT02_BL.getCounterSolutionCount(map);
		if(count != 0) {
			// 取得柜台信息List
			counterSolutionList = binOLBSCNT02_BL.getCounterSolutionList(map);
		}
		// form表单设置
		form.setITotalDisplayRecords(count);
		form.setITotalRecords(count);
		return SUCCESS;
	}
	 
	/** 柜台详细信息 */
	private Map counterInfo;
	
	/** 柜台事件信息List */
	private List<Map<String, Object>> counterEventList;
	
	/** 柜台方案信息List */
	private List<Map<String, Object>> counterSolutionList;

	public Map getCounterInfo() {
		return counterInfo;
	}

	public void setCounterInfo(Map counterInfo) {
		this.counterInfo = counterInfo;
	}

	public List<Map<String, Object>> getCounterEventList() {
		return counterEventList;
	}

	public void setCounterEventList(List<Map<String, Object>> counterEventList) {
		this.counterEventList = counterEventList;
	}
	
	public List<Map<String, Object>> getCounterSolutionList() {
		return counterSolutionList;
	}
	
	public void setCounterSolutionList(List<Map<String, Object>> counterSolutionList) {
		this.counterSolutionList = counterSolutionList;
	}

	@Override
	public BINOLBSCNT02_Form getModel() {
		return form;
	}


}
