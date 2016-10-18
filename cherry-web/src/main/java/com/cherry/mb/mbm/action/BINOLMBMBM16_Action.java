/*
 * @(#)BINOLMBMBM16_Action.java     1.0 2013/05/20
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
import com.cherry.cm.cmbussiness.bl.BINOLCM05_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.cherry.mb.mbm.bl.BINOLMBMBM16_BL;
import com.cherry.mb.mbm.form.BINOLMBMBM16_Form;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 会员升降级履历
 * 
 * @author Luohong
 * @version 1.0 2013/05/20
 */
public class BINOLMBMBM16_Action extends BaseAction implements ModelDriven<BINOLMBMBM16_Form>{

	private static final long serialVersionUID = 2556198732103042285L;
	
	/** 会员升降级Form*/
	private  BINOLMBMBM16_Form  form = new BINOLMBMBM16_Form();
	
	/** 会员升降级BL*/
	@Resource
	private  BINOLMBMBM16_BL  binOLMBMBM16_BL;
	
	/** 系统配置项 共通BL */
	@Resource(name="binOLCM14_BL")
	private BINOLCM14_BL binOLCM14_BL;
	
	@Resource
	private BINOLCM05_BL binOLCM05_BL;
	
	/**会员升降级List*/
	private List<Map<String,Object>> memLevelList;
	
	public List<Map<String, Object>> getMemLevelList() {
		return memLevelList;
	}
	public void setMemLevelList(List<Map<String, Object>> memLevelList) {
		this.memLevelList = memLevelList;
	}
	
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
	 * 会员升降级Init
	 * @return
	 * @throws Exception
	 */
	public String relegationInit()throws Exception{
		Map<String,Object> map= getCommMap();
		//会员Id
		map.put("memberInfoId",form.getMemberInfoId());
		// 俱乐部模式
		clubMod = binOLCM14_BL.getConfigValue("1299", String.valueOf(map.get(CherryConstants.ORGANIZATIONINFOID)),
				String.valueOf(map.get(CherryConstants.BRANDINFOID)));
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
			}
		}
		return SUCCESS;
	}
	
	/**
	 * 查询会员等级变化信息
	 * @return
	 * @throws Exception
	 */
	public String searchLevel()throws Exception{
		Map<String,Object> map= getCommMap();
		//会员Id
		map.put("memberInfoId",form.getMemberInfoId());
		// 会员俱乐部ID
		map.put("memberClubId", form.getMemberClubId());
		//会员等级升降List
		memLevelList = binOLMBMBM16_BL.getMemLevelList(map);
		return SUCCESS;
	}
	
	/**
	 * 共通参数Map
	 */
	private Map<String,Object> getCommMap(){
		Map<String,Object> map = new HashMap<String, Object>();
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 用户信息
		map.put(CherryConstants.SESSION_USERINFO, userInfo);
		// 语言
		map.put(CherryConstants.SESSION_LANGUAGE, session
				.get(CherryConstants.SESSION_LANGUAGE));
		// 组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo
				.getBIN_OrganizationInfoID());
		// 用户Id
		map.put(CherryConstants.USERID, userInfo.getBIN_UserID());
		// 品牌
		map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		return map;
	}
	
	@Override
	public BINOLMBMBM16_Form getModel() {
		return form;
	}
}
