/*
 * @(#)BINOLMBMBM10_Action.java     1.0 2012.11.27
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
import com.cherry.cm.cmbussiness.bl.BINOLCM33_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryMenu;
import com.cherry.cm.gadget.interfaces.GadgetIf;
import com.cherry.mb.mbm.bl.BINOLMBMBM20_BL;
import com.cherry.mb.mbm.service.BINOLMBMBM02_Service;
import com.googlecode.jsonplugin.JSONUtil;

/**
 * 会员详细画面Action
 * 
 * @author WangCT
 * @version 1.0 2012.11.27
 */
public class BINOLMBMBM10_Action extends BaseAction {
	
	private static final long serialVersionUID = 3488365097213924162L;
	
	/** 取得小工具共通BL */
	@Resource
	GadgetIf gadgetBL;
	
	/** 会员详细画面Service */
	@Resource
	private BINOLMBMBM02_Service binOLMBMBM02_Service;
	
	/** 会员提示信息画面BL */
	@Resource
	private BINOLMBMBM20_BL binOLMBMBM20_BL;
	
	/** 会员检索画面共通BL **/
	@Resource
	private BINOLCM33_BL binOLCM33_BL;

	/**
	 * 会员详细画面初期化
	 * 
	 * @return String 
	 * @throws Exception 
	 */
	public String init() throws Exception {
		
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		
		if(userInfo.getCategoryCode() == null || !"50".equals(userInfo.getCategoryCode())) {
			// 取得所有权限
			Map<String, Object> xmldocumentmap = (Map)session.get(CherryConstants.SESSION_LEFTMENU_XMLDOCMAP);
			// 取得对应菜单下的权限
			CherryMenu doc = (CherryMenu)xmldocumentmap.get("MB");
			if(doc == null || doc.getChildMenuByID("BINOLMBMBM01") == null) {
				this.addActionError(getText("EMB00022"));
				return SUCCESS;
			}
		}
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		// 组织代码
        map.put("orgCode", userInfo.getOrganizationInfoCode());
		// 不是总部的场合
		if(userInfo.getBIN_BrandInfoID() != CherryConstants.BRAND_INFO_ID_VALUE) {
			// 所属品牌
			map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
			// 品牌代码
	    	map.put("brandCode", userInfo.getBrandCode());
		}
		if(memberInfoId == null) {
			map.put("memCode", memCode);
			// 根据会员卡号查询会员ID
			memberInfoId = binOLMBMBM02_Service.getMemberInfoId(map);
		}
		map.put("memberInfoId", memberInfoId);
		
		if(userInfo.getCategoryCode() == null || !"50".equals(userInfo.getCategoryCode())) {
			if(weChatLoginFlag == null || !"1".equals(weChatLoginFlag)) {
				if(!"MB".equals(session.get(CherryConstants.SESSION_TOPMENU_CURRENT)) 
						&& "1".equals(session.get(CherryConstants.SESSION_PRIVILEGE_FLAG))) {
					// 用户ID
					map.put("userId", userInfo.getBIN_UserID());
					// 业务类型
					map.put("businessType", "2");
					// 操作类型
					map.put("operationType", "1");
					// 是否带权限查询
					map.put("privilegeFlag", session.get(CherryConstants.SESSION_PRIVILEGE_FLAG));
					// 判断是否有权限查询该会员信息
					Map<String, Object> resultMap = binOLCM33_BL.searchMemList(map);
					if(resultMap != null && !resultMap.isEmpty()) {
						int count = Integer.parseInt(resultMap.get("total").toString());
						if(count == 0) {
							this.addActionError(getText("EMB00022"));
							return SUCCESS;
						}
					}
				}
			}
		}
		
		// 查询会员基本信息
		memberInfoMap = binOLMBMBM02_Service.getMemberInfo(map);
		
		if(memberInfoMap == null || memberInfoMap.isEmpty()) {
			this.addActionError(getText("EMB00021"));
			return SUCCESS;
		}
		
		// 判断会员提示信息是否存在
		hasMemWarnInfo = binOLMBMBM20_BL.hasMemWarnInfo(map);
		return SUCCESS;
	}
	
	/**
	 * 会员个人首页画面初期化
	 * 
	 * @return String 
	 */
	public String topInit() throws Exception {
		
		UserInfo userinfo = (UserInfo)session.get(CherryConstants.SESSION_USERINFO);
		// 取得所有权限
		Map<String, Object> xmldocumentmap = (Map)session.get(CherryConstants.SESSION_LEFTMENU_XMLDOCMAP);
		// 取得对应菜单下的权限
		CherryMenu doc = (CherryMenu)xmldocumentmap.get("MB");
    	Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", userinfo.getBIN_UserID());
		map.put("pageId", "2");
		map.put("doc", doc);
		if(userinfo.getCategoryCode() != null && "50".equals(userinfo.getCategoryCode())) {
			map.put("plFlag", "0");
		}
		// 取得小工具信息
		Map<String, Object> gadgetInfoMap = gadgetBL.getGadgetInfoList(map);
		List<Map<String, Object>> gadgetInfoPLList = (List)gadgetInfoMap.get("gadgetInfoList");
		if(gadgetInfoPLList != null && !gadgetInfoPLList.isEmpty()) {
			for(Map<String, Object> gadgetInfo : gadgetInfoPLList) {
				String gadgetCode = (String)gadgetInfo.get("gadgetCode");
				if("BINOLMBMBM10_01".equals(gadgetCode)) {
					gadgetInfo.put("gadgetName", getText("member.gadget.memInfo"));
				} else if("BINOLMBMBM10_02".equals(gadgetCode)) {
					gadgetInfo.put("gadgetName", getText("member.gadget.memSaleInfo"));
				} else if("BINOLMBMBM10_03".equals(gadgetCode)) {
					gadgetInfo.put("gadgetName", getText("member.gadget.memPointInfo"));
				} else if("BINOLMBMBM10_04".equals(gadgetCode)) {
					gadgetInfo.put("gadgetName", getText("member.gadget.memSaleCountInfo"));
				} else if("BINOLMBMBM10_05".equals(gadgetCode)) {
					gadgetInfo.put("gadgetName", getText("member.gadget.memSaleCountByPro"));
				}
			}
		}
		gadgetInfoMap.put("userInfo", userinfo);
		Map<String, Object> gadgetParam = new HashMap<String, Object>();
		gadgetParam.put("memberInfoId", memberInfoId);
		gadgetInfoMap.put("gadgetParam", gadgetParam);
		gadgetInfoMap.put(CherryConstants.SESSION_LANGUAGE, session.get(CherryConstants.SESSION_CHERRY_LANGUAGE));
		gadgetInfo = JSONUtil.serialize(gadgetInfoMap);
    	
        return SUCCESS;
	}
	
	/** 会员ID */
	private String memberInfoId;
	
	/** 会员卡号 */
	private String memCode;
	
	/** 会员基本信息 */
	private Map memberInfoMap;
	
	/** 会员首页小工具信息 */
	private String gadgetInfo;
	
	/** 会员提示信息是否存在 */
	private boolean hasMemWarnInfo;
	
	/** csrftoken  */
    private String code;
    
    /** 微信登录判断标志  */
    private String weChatLoginFlag;

	public String getMemberInfoId() {
		return memberInfoId;
	}

	public void setMemberInfoId(String memberInfoId) {
		this.memberInfoId = memberInfoId;
	}
	
	public String getMemCode() {
		return memCode;
	}

	public void setMemCode(String memCode) {
		this.memCode = memCode;
	}

	public Map getMemberInfoMap() {
		return memberInfoMap;
	}

	public void setMemberInfoMap(Map memberInfoMap) {
		this.memberInfoMap = memberInfoMap;
	}

	public String getGadgetInfo() {
		return gadgetInfo;
	}

	public void setGadgetInfo(String gadgetInfo) {
		this.gadgetInfo = gadgetInfo;
	}

	public boolean isHasMemWarnInfo() {
		return hasMemWarnInfo;
	}

	public void setHasMemWarnInfo(boolean hasMemWarnInfo) {
		this.hasMemWarnInfo = hasMemWarnInfo;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getWeChatLoginFlag() {
		return weChatLoginFlag;
	}

	public void setWeChatLoginFlag(String weChatLoginFlag) {
		this.weChatLoginFlag = weChatLoginFlag;
	}

}
