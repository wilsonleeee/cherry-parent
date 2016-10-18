/*
 * @(#)RPIndexAction.java     1.0 2010/10/27
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

package com.cherry.rp.common;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM00_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryMenu;
import com.cherry.cm.core.CodeTable;
import com.cherry.cm.core.DESPlus;
import com.cherry.cm.core.PropertiesUtil;

/**
 * 报表管理首页Action
 * 
 * @author WangCT
 * @version 1.0 2010.10.27
 */
public class RPIndexAction extends BaseAction {
	
	private static final long serialVersionUID = 2128176181827474982L;
	
	@Resource(name="binOLCM00_BL")
	private BINOLCM00_BL binOLCM00_BL;
	
	@Resource(name="binOLCM14_BL")
	private BINOLCM14_BL binOLCM14_BL;
	
	@Resource
	private CodeTable code;
	
	/**
	 * action名称
	 */
	private String actionName;
	
	/**
	 * 是否启用了BI报表系统
	 */
	private String biFlag;
	
	/**
	 * 访问BI的URL
	 */
	private String biUrl;
	
	/**
	 * 访问BI的用户名
	 */
	private String biUserName;
	
	/**
	 * 访问BI的用户密码
	 */
	private String biPassWord;
	
	/**
	 * 模拟登陆时是否对密码加密
	 */
	private String encryptFlag;
	
	
	
	/**
	 * 报表管理首页初期化
	 * 
	 * @return String 
	 * @throws Exception 
	 */
	public String init() throws Exception{
		request.getSession().setAttribute(CherryConstants.SESSION_TOPMENU_CURRENT, request.getParameter("MENU_ID"));
		Map map = (Map)session.get(CherryConstants.SESSION_LEFTMENU_XMLDOCMAP);
		request.getSession().setAttribute(CherryConstants.SESSION_LEFTMENUALL_CURRENT, map.get(request.getParameter("MENU_ID")));
		
		Map<String, Object> xmldocumentmap = (Map<String, Object>)session.get(CherryConstants.SESSION_LEFTMENU_XMLDOCMAP);
		CherryMenu doc = (CherryMenu)xmldocumentmap.get(request.getParameter("MENU_ID"));
		String url = "";
		if(doc.getChildList()!=null && doc.getChildList().size()>0){
			url=doc.getChildList().get(0).getMenuURL();
		}
		this.setActionName(url);
		
//		 String biurl = PropertiesUtil.pps.getProperty("BIService.WebURL", "");
		// 从配置文件中读取BI配置信息的WebURL属性
		UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO); 
		String biurl = PropertiesUtil.getBIConfigValue(userInfo.getBrandCode(), CherryConstants.BIConfig_WebURL);
		
		if(biurl==null||"".equals(biurl)){
			this.setBiFlag("FALSE");
		}else{
			this.setBiFlag("TRUE");
		}
		
		return SUCCESS;
	}
	
	public String openBIWindow() throws Exception{
		UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO); 
		// 从配置文件中读取BI配置信息的WebURL属性
//		 this.setBiUrl(PropertiesUtil.pps.getProperty("BIService.WebURL", ""));
		String biurl = PropertiesUtil.getBIConfigValue(userInfo.getBrandCode(), CherryConstants.BIConfig_WebURL);
		this.setBiUrl(biurl);
		//取得用户信息
    	Map<String, Object> map = new HashMap<String, Object>();
    	// 登陆名
		map.put("loginName", userInfo.getLoginName());
    	Map<String, Object> userDetail = binOLCM00_BL.UserDetail(map);
    	// 密码
    	String password = String.valueOf(userDetail.get("PassWord"));
    	// 密码解密
    	DESPlus des = new DESPlus(CherryConstants.CUSTOMKEY);
		String psd=  des.decrypt(password);
		
		// BI用户名
		String biLoginName = userInfo.getLoginName();
		// 从CodeTable中取得用户名的前缀
		Map<String, Object> codeMap = new HashMap<String, Object>();
		int brandInfoId = userInfo.getBIN_BrandInfoID();
		if (CherryConstants.BRAND_INFO_ID_VALUE == brandInfoId) {
			codeMap = code.getCode("1179", brandInfoId);
		} else {
			String brandCode = userInfo.getBrandCode();
			codeMap = code.getCode("1179", brandCode);
		}
		if(codeMap != null && !codeMap.isEmpty()) {
			String pre = (String)codeMap.get("value1");
			if(pre != null && !"".equals(pre)) {
				biLoginName = pre + biLoginName;
			}
		}
		
		boolean flag = binOLCM14_BL.isConfigOpen("1121", userInfo.getBIN_OrganizationInfoID(), userInfo.getBIN_BrandInfoID());
		if(flag){
			this.setEncryptFlag("Y");
		}else{
			this.setEncryptFlag("N");
		}
		
		this.setBiUserName(biLoginName);
		this.setBiPassWord(psd);
		return SUCCESS;
	}
	/**
	 * @return the actionName
	 */
	public String getActionName() {
		return actionName;
	}
	/**
	 * @param actionName the actionName to set
	 */
	public void setActionName(String actionName) {
		this.actionName = actionName;
	}
	/**
	 * @return the biUrl
	 */
	public String getBiUrl() {
		return biUrl;
	}
	/**
	 * @param biUrl the biUrl to set
	 */
	public void setBiUrl(String biUrl) {
		this.biUrl = biUrl;
	}

	/**
	 * @return the biUserName
	 */
	public String getBiUserName() {
		return biUserName;
	}

	/**
	 * @param biUserName the biUserName to set
	 */
	public void setBiUserName(String biUserName) {
		this.biUserName = biUserName;
	}

	/**
	 * @return the biPassWord
	 */
	public String getBiPassWord() {
		return biPassWord;
	}

	/**
	 * @param biPassWord the biPassWord to set
	 */
	public void setBiPassWord(String biPassWord) {
		this.biPassWord = biPassWord;
	}

	/**
	 * @return the biFlag
	 */
	public String getBiFlag() {
		return biFlag;
	}

	/**
	 * @param biFlag the biFlag to set
	 */
	public void setBiFlag(String biFlag) {
		this.biFlag = biFlag;
	}

	public String getEncryptFlag() {
		return encryptFlag;
	}

	public void setEncryptFlag(String encryptFlag) {
		this.encryptFlag = encryptFlag;
	}
}
