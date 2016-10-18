/*	
 * @(#)BINOLCM34_Action.java     1.0 2012/01/18		
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
package com.cherry.cm.cmbussiness.action;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM33_BL;
import com.cherry.cm.cmbussiness.form.BINOLCM35_Form;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.Bean2Map;
import com.cherry.cm.util.ConvertUtil;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 查询会员和会员事件弹出画面共通Action
 * 
 * @author WangCT
 * @version 1.0 2012/01/18
 */
public class BINOLCM35_Action extends BaseAction implements ModelDriven<BINOLCM35_Form> {

	private static final long serialVersionUID = 7227472618197702582L;
	
	/** 会员检索画面共通BL **/
	@Resource
	private BINOLCM33_BL binOLCM33_BL;
	
	/**
	 * 
	 * 查询会员画面
	 * 
	 */
	public String searchMemInit() throws Exception {
		
		return SUCCESS;
	}
	
	/**
	 * 
	 * 查询会员列表
	 * 
	 */
	public String searchMemList() throws Exception {
		
		Map<String, Object> map = (Map)Bean2Map.toHashMap(form);
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		map.put(CherryConstants.ORG_CODE, userInfo.getOrganizationInfoCode());
		// 不是总部的场合
		if(userInfo.getBIN_BrandInfoID() != CherryConstants.BRAND_INFO_ID_VALUE) {
			// 所属品牌
			map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
			map.put(CherryConstants.BRAND_CODE, userInfo.getBrandCode());
		}
		
		// dataTable上传的参数设置到map
		ConvertUtil.setForm(form, map);
		
		// 不带权限查询
		map.put("privilegeFlag", "0");
		// 包含无效数据查询
		map.put("validFlag", "1");
		// 包含资料不全信息查询
		map.put("memInfoRegFlg", "1");
		
		// 查询会员信息List
		Map<String, Object> resultMap = binOLCM33_BL.searchMemByLuceneList(map);
		if(resultMap != null) {
			int count = Integer.parseInt(resultMap.get("total").toString());
			form.setITotalDisplayRecords(count);
			form.setITotalRecords(count);
			if(count != 0) {
				memberInfoList = (List)resultMap.get("list");
			}
		}
		return SUCCESS;
	}
	
	/**
	 * 
	 * 查询会员销售画面
	 * 
	 */
	public String searchMemSaleInit() throws Exception {
		
		return SUCCESS;
	}
	
	/**
	 * 
	 * 查询会员销售列表
	 * 
	 */
	public String searchMemSaleList() throws Exception {
		
		Map<String, Object> map = (Map)Bean2Map.toHashMap(form);
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		map.put(CherryConstants.ORG_CODE, userInfo.getOrganizationInfoCode());
		// 不是总部的场合
		if(userInfo.getBIN_BrandInfoID() != CherryConstants.BRAND_INFO_ID_VALUE) {
			// 所属品牌
			map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
			map.put(CherryConstants.BRAND_CODE, userInfo.getBrandCode());
		}
		
		// dataTable上传的参数设置到map
		ConvertUtil.setForm(form, map);
		
		map.put("eventType", "1");
		map.put("eventDateStart", form.getSaleDateStart());
		map.put("eventDateEnd", form.getSaleDateEnd());
		
		// 查询会员销售信息List
		Map<String, Object> resultMap = binOLCM33_BL.searchMemSaleByLuceneList(map);
		if(resultMap != null) {
			int count = Integer.parseInt(resultMap.get("total").toString());
			form.setITotalDisplayRecords(count);
			form.setITotalRecords(count);
			if(count != 0) {
				memSaleInfoList = (List)resultMap.get("list");
			}
		}
		
		return SUCCESS;
	}
	
	/** 会员信息List **/
	private List<Map<String, Object>> memberInfoList;
	
	/** 会员销售信息List **/
	private List<Map<String, Object>> memSaleInfoList;
	
	public List<Map<String, Object>> getMemberInfoList() {
		return memberInfoList;
	}

	public void setMemberInfoList(List<Map<String, Object>> memberInfoList) {
		this.memberInfoList = memberInfoList;
	}

	public List<Map<String, Object>> getMemSaleInfoList() {
		return memSaleInfoList;
	}

	public void setMemSaleInfoList(List<Map<String, Object>> memSaleInfoList) {
		this.memSaleInfoList = memSaleInfoList;
	}

	private BINOLCM35_Form form = new BINOLCM35_Form();

	@Override
	public BINOLCM35_Form getModel() {
		return form;
	}

}
