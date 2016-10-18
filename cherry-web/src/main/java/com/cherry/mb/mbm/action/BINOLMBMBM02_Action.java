/*
 * @(#)BINOLMBMBM02_Action.java     1.0 2011.10.25
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

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM05_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.Bean2Map;
import com.cherry.mb.mbm.bl.BINOLMBMBM02_BL;
import com.cherry.mb.mbm.form.BINOLMBMBM02_Form;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 会员详细画面Action
 * 
 * @author WangCT
 * @version 1.0 2011.10.25
 */
@SuppressWarnings("unchecked")
public class BINOLMBMBM02_Action extends BaseAction implements ModelDriven<BINOLMBMBM02_Form> {
	
	private static final long serialVersionUID = 3474595749439691888L;
	
	/** 会员详细画面BL */
	@Resource
	private BINOLMBMBM02_BL binOLMBMBM02_BL;
	
	@Resource
	private BINOLCM14_BL binOLCM14_BL;
	
	@Resource
	private BINOLCM05_BL binOLCM05_BL;
	
	private String clubMod;
	
	/** 会员俱乐部List */
	private List<Map<String, Object>> clubList;
	
	public String getClubMod() {
		return clubMod;
	}

	public void setClubMod(String clubMod) {
		this.clubMod = clubMod;
	}

	public List<Map<String, Object>> getClubList() {
		return clubList;
	}

	public void setClubList(List<Map<String, Object>> clubList) {
		this.clubList = clubList;
	}
	
	/**
	 * 会员详细画面初期处理
	 * 
	 * @return 会员详细画面
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
			// 所属品牌Code
			map.put(CherryConstants.BRAND_CODE, userInfo.getBrandCode());
		}
		// 取得会员信息
		memberInfoMap = binOLMBMBM02_BL.getMemberInfo(map);
		// 俱乐部模式
		clubMod = binOLCM14_BL.getConfigValue("1299", String.valueOf(userInfo
				.getBIN_OrganizationInfoID()), String.valueOf(userInfo.getBIN_BrandInfoID()));
		if (!"3".equals(clubMod)) {
			// 语言类型
			map.put(CherryConstants.SESSION_LANGUAGE, session
					.get(CherryConstants.SESSION_LANGUAGE));
			// 取得会员俱乐部列表
			List<Map<String, Object>> clubInfoList = binOLCM05_BL.getClubList(map);
			if (null != clubInfoList && !clubInfoList.isEmpty()) {
				// 取得会员已经拥有的俱乐部列表
				clubList = binOLCM05_BL.getMemClubList(map);
				if (null == clubList || clubList.isEmpty()) {
					clubList = clubInfoList;
				} else {
					for (Map<String, Object> clubMap : clubList) {
						// 会员俱乐部ID
						String memberClubId = String.valueOf(clubMap.get("memberClubId"));
						for (int i = 0; i < clubInfoList.size(); i++) {
							Map<String, Object> clubInfo = clubInfoList.get(i);
							if (memberClubId.equals(String.valueOf(clubInfo.get("memberClubId")))) {
								clubMap.putAll(clubInfo);
								clubInfoList.remove(i);
								break;
							}
						}
					}
					clubList.addAll(clubInfoList);
				}
				map.put("memberClubId", clubList.get(0).get("memberClubId"));
				// 查询会员俱乐部信息
				memClubMap = binOLMBMBM02_BL.getMemClubInfo(map);
			}
		}
		return SUCCESS;
	}
	
	/**
	 * 切换俱乐部
	 * 
	 * @return 俱乐部信息
	 */
	public String changeClub() throws Exception {
		Map<String, Object> map = (Map)Bean2Map.toHashMap(form);
		// 查询会员俱乐部信息
		memClubMap = binOLMBMBM02_BL.getMemClubInfo(map);
		return SUCCESS;
	}
	
	/** 会员详细信息 */
	private Map memberInfoMap;
	
	/** 会员俱乐部信息 */
	private Map memClubMap;
	
	public Map getMemberInfoMap() {
		return memberInfoMap;
	}

	public void setMemberInfoMap(Map memberInfoMap) {
		this.memberInfoMap = memberInfoMap;
	}
	
	public Map getMemClubMap() {
		return memClubMap;
	}

	public void setMemClubMap(Map memClubMap) {
		this.memClubMap = memClubMap;
	}

	/** 会员详细画面Form */
	private BINOLMBMBM02_Form form = new BINOLMBMBM02_Form();

	@Override
	public BINOLMBMBM02_Form getModel() {
		return form;
	}

}
