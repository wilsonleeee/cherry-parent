/*	
 * @(#)BINOLRPQUERY_Action.java     1.0 2010/11/08		
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

package com.cherry.rp.query.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.rp.query.bl.BINOLRPQUERY_BL;
import com.cherry.rp.query.form.BINOLRPQUERY_Form;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 查询BI报表共通Action
 * @author WangCT
 *
 */
@SuppressWarnings("unchecked")
public class BINOLRPQUERY_Action extends BaseAction implements ModelDriven<BINOLRPQUERY_Form> {

	private static final long serialVersionUID = 3490114714997791180L;
	
	/** 查询BI报表共通Form */
	private BINOLRPQUERY_Form form = new BINOLRPQUERY_Form();
	
	/** 查询BI报表共通BL */
	@Resource
	private BINOLRPQUERY_BL binOLRPQUERY_BL;
	
	/**
	 * 
	 * 查询BI报表画面初期表示
	 * 
	 * @param 无
	 * @return 查询BI报表画面
	 * 
	 */
	public String init() throws Exception {
		
		Map<String, Object> map = new HashMap<String, Object>();
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 语言
		String language = (String)session.get(CherryConstants.SESSION_LANGUAGE);
		// 用户ID
		map.put(CherryConstants.USERID, userInfo.getBIN_UserID());
		if(language != null) {
			map.put(CherryConstants.SESSION_LANGUAGE, language);
		}
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		// 不是总部的场合
		if(userInfo.getBIN_BrandInfoID() != CherryConstants.BRAND_INFO_ID_VALUE) {
			// 所属品牌
			map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		}
		// BI报表ID
		map.put("biRptCode", form.getBiRptCode());
		// 取得BI报表信息
		biReportInfo = binOLRPQUERY_BL.getBIReportInfo(map);
		if(biReportInfo != null && !biReportInfo.isEmpty()) {
			// BI报表业务类型
			map.put("businessType", biReportInfo.get("businessType"));
			// 操作类型为1：查询
			map.put("operationType", 1);
			// 取得BI报表模式List
			defModeList = binOLRPQUERY_BL.getBIRptDefList(map);
			if(defModeList != null && !defModeList.isEmpty()) {
				// 取得BI报表定义信息
				biRptDefMapList = (List)defModeList.get(0).get("list");
			}
			// 取得BI报表查询条件
			biRptQryDefMapList = binOLRPQUERY_BL.getBIRptQryDefList(map);
		}

		return SUCCESS;
	}
	
	/**
	 * 
	 * 查询BI报表数据
	 * 
	 * @param 无
	 * @return BI报表数据
	 * 
	 */
	public String searchBITable() throws Exception {
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
//		// 从session中取得连接BI服务器的对象
//		DWMdiConnection oCon = (DWMdiConnection)session.get(CherryConstants.SESSION_DWMDICONNECTION);
//		// session中不存在连接BI服务器的对象时
//		if(oCon == null) {
//			// 生成连接BI服务器对象
////			oCon = new DWMdiConnection(PropertiesUtil.pps.getProperty("BIService.IP"), Integer.parseInt(PropertiesUtil.pps.getProperty("BIService.Port")), userInfo.getLoginName(), userInfo.getPassword());
//			oCon = new DWMdiConnection(PropertiesUtil.pps.getProperty("BIService.IP"), Integer.parseInt(PropertiesUtil.pps.getProperty("BIService.Port")), "Administrator", "");
//			// 把生成的BI服务器对象设到session中
//			session.put(CherryConstants.SESSION_DWMDICONNECTION, oCon);
//		}
//		map.put(CherryConstants.SESSION_DWMDICONNECTION, oCon);
		// 登录帐号
		map.put("loginName", userInfo.getLoginName());
		// 登录密码
		map.put("password", userInfo.getPassword());
		// 语言
		String language = (String)session.get(CherryConstants.SESSION_LANGUAGE);
		if(language != null) {
			map.put(CherryConstants.SESSION_LANGUAGE, language);
		}
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		// 不是总部的场合
		if(userInfo.getBIN_BrandInfoID() != CherryConstants.BRAND_INFO_ID_VALUE) {
			// 所属品牌
			map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		}
		// BI报表ID
		map.put("biRptCode", form.getBiRptCode());
		// BI报表定义信息List
		map.put("biRptDefList", form.getBiRptDefList());
		// BI报表查询条件List
		map.put("biRptQryDefList", form.getBiRptQryDefList());
		// BI报表钻透URL
		map.put("drillUrl", form.getDrillUrl());
		
		// 取得BI报表数据
		ConvertUtil.setResponseByAjax(response, binOLRPQUERY_BL.getReportTable(map));
		return null;
	}
	
	/**
	 * 
	 * BI报表钻透画面初期显示
	 * 
	 * @param 无
	 * @return BI报表钻透画面
	 * 
	 */
	public String searchBIRptDetailInit() throws Exception {
		
		Map<String, Object> map = new HashMap<String, Object>();
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 语言
		String language = (String)session.get(CherryConstants.SESSION_LANGUAGE);
		if(language != null) {
			map.put(CherryConstants.SESSION_LANGUAGE, language);
		}
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		// 不是总部的场合
		if(userInfo.getBIN_BrandInfoID() != CherryConstants.BRAND_INFO_ID_VALUE) {
			// 所属品牌
			map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		}
		// BI报表ID
		map.put("biRptCode", form.getBiRptCode());
		// BI报表钻透查询条件
		map.put("drillQuery", form.getDrillQuery());
		// BI报表查询条件(画面显示用)
		map.put("biQueryDisPlay", form.getBiQueryDisPlay());
		// 取得BI报表钻透信息
		biDrlThrough = binOLRPQUERY_BL.getBIDrlThrough(map);
		if(biDrlThrough != null) {
			// BI报表钻透标题
			biDrlThrough.put("title", form.getTitle());
		}
		if(biDrlThrough != null && biDrlThrough.get("drillStrName") != null) {
			String drillStrName = (String)biDrlThrough.get("drillStrName");
			drillStrNames = drillStrName.split(",");
		}
		if(biDrlThrough != null && biDrlThrough.get("drillString") != null) {
			drillString = (String)biDrlThrough.get("drillString");
		}
		
		return SUCCESS;
	}
	
	/**
	 * 
	 * 查询BI报表钻透数据
	 * 
	 * @param 无
	 * @return BI报表钻透数据
	 * 
	 */
	public String searchBIRptDetail() throws Exception {
		
		Map<String, Object> map = new HashMap<String, Object>();
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
//		// 从session中取得连接BI服务器的对象(钻透用)
//		DWDbiConnection oCon = (DWDbiConnection)session.get(CherryConstants.SESSION_DWDBICONNECTION);
//		// session中不存在连接BI服务器的对象时
//		if(oCon == null) {
//			// 生成连接BI服务器对象(钻透用)
////			oCon = new DWDbiConnection(PropertiesUtil.pps.getProperty("BIService.IP"), Integer.parseInt(PropertiesUtil.pps.getProperty("BIService.Port")), userInfo.getLoginName(), userInfo.getPassword());
//			oCon = new DWDbiConnection(PropertiesUtil.pps.getProperty("BIService.IP"), Integer.parseInt(PropertiesUtil.pps.getProperty("BIService.Port")), "Administrator", "");
//			// 把生成的BI服务器对象设到session中
//			session.put(CherryConstants.SESSION_DWDBICONNECTION, oCon);
//		}
//		map.put(CherryConstants.SESSION_DWDBICONNECTION, oCon);
		// 登录帐号
		map.put("loginName", userInfo.getLoginName());
		// 登录密码
		map.put("password", userInfo.getPassword());
		// 语言
		String language = (String)session.get(CherryConstants.SESSION_LANGUAGE);
		if(language != null) {
			map.put(CherryConstants.SESSION_LANGUAGE, language);
		}
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		// 不是总部的场合
		if(userInfo.getBIN_BrandInfoID() != CherryConstants.BRAND_INFO_ID_VALUE) {
			// 所属品牌
			map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		}
		// dataTable上传的参数设置到map
		ConvertUtil.setForm(form, map);
		// BI报表ID
		map.put("biRptCode", form.getBiRptCode());
		// BI报表查询条件
		map.put("query", form.getBiQuery());
		// BI报表钻透查询条件
		map.put("drillQuery", form.getDrillQuery());
		map.put("sEcho", form.getSEcho());
		
		// 取得BI报表钻透数据
		ConvertUtil.setResponseByAjax(response, binOLRPQUERY_BL.getReportDetail(map));
		return null;
	}
	
	/** BI报表信息 */
	private Map biReportInfo;
	
	/** BI报表模式List */
	private List<Map<String, Object>> defModeList;
	
	/** BI报表定义信息 */
	private List<Map<String, Object>> biRptDefMapList;
	
	/** BI报表查询条件 */
	private List<Map<String, Object>> biRptQryDefMapList;
	
	/** BI报表钻透信息 */
	private Map biDrlThrough;
	
	/** 钻透表的显示用选取字段集 */
	private String[] drillStrNames;
	
	/** 钻透表的选取字段集 */
	private String drillString;

	public Map getBiReportInfo() {
		return biReportInfo;
	}

	public void setBiReportInfo(Map biReportInfo) {
		this.biReportInfo = biReportInfo;
	}

	public List<Map<String, Object>> getDefModeList() {
		return defModeList;
	}

	public void setDefModeList(List<Map<String, Object>> defModeList) {
		this.defModeList = defModeList;
	}

	public List<Map<String, Object>> getBiRptDefMapList() {
		return biRptDefMapList;
	}

	public void setBiRptDefMapList(List<Map<String, Object>> biRptDefMapList) {
		this.biRptDefMapList = biRptDefMapList;
	}

	public List<Map<String, Object>> getBiRptQryDefMapList() {
		return biRptQryDefMapList;
	}

	public void setBiRptQryDefMapList(List<Map<String, Object>> biRptQryDefMapList) {
		this.biRptQryDefMapList = biRptQryDefMapList;
	}

	public Map getBiDrlThrough() {
		return biDrlThrough;
	}

	public void setBiDrlThrough(Map biDrlThrough) {
		this.biDrlThrough = biDrlThrough;
	}

	public String[] getDrillStrNames() {
		return drillStrNames;
	}

	public void setDrillStrNames(String[] drillStrNames) {
		this.drillStrNames = drillStrNames;
	}

	public String getDrillString() {
		return drillString;
	}

	public void setDrillString(String drillString) {
		this.drillString = drillString;
	}

	@Override
	public BINOLRPQUERY_Form getModel() {
		return form;
	}

}
