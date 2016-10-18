/*		
 * @(#)BINOLCM07_Action.java     1.0 2011/05/31      
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM07_BL;
import com.cherry.cm.cmbussiness.form.BINOLCM07_Form;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.Bean2Map;
import com.cherry.cm.util.ConvertUtil;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 促销产品弹出table共通Action(以库存表中的促销品为主表)
 * @author Dingyc
 *
 */
@SuppressWarnings("unchecked")
public class BINOLCM07_Action extends BaseAction implements ModelDriven<BINOLCM07_Form>{

	private static final long serialVersionUID = -390400538803426653L;
	
	@Resource
	private BINOLCM07_BL binOLCM07_BL;

	/** 参数FORM */
	private BINOLCM07_Form form = new BINOLCM07_Form();

	@Override
	public BINOLCM07_Form getModel() {
		return form;
	}
	
	/**
	 * 取得促销产品信息
	 * @return
	 * @throws Exception 
	 */
	public String getPromotionInfo () throws Exception{
		// 取得session信息
		Map<String, Object> map  = this.getSessionInfo();
		// form参数设置到paramMap中
		ConvertUtil.setForm(form, map);
		// 取得组织部门类型
		binOLCM07_BL.setType(map);
		// 取得促销产品信息
		HashMap resultMap  = binOLCM07_BL.getPromotionDialogInfoList(map);
		
		form.setPopPrmProductInfoList((List)resultMap.get(CherryConstants.POP_PRMPRODUCT_LIST));
		
		int count = Integer.parseInt((String.valueOf(resultMap.get("count"))));
		// form表单设置
		form.setITotalDisplayRecords(count);
		form.setITotalRecords(count);
		return "PRMProductTable_1";
	}
	
	/**
	 * 取得session的信息
	 * @param map
	 * @throws Exception 
	 */
	private Map getSessionInfo() throws Exception{
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
		Map<String, Object> map = (Map<String, Object>) Bean2Map.toHashMap(form);
		// 取得所属组织
		map.put("bin_OrganizationInfoID", userInfo.getBIN_OrganizationInfoID());
		String brandInfoID = (String.valueOf(userInfo.getBIN_BrandInfoID()));
		if (!brandInfoID.equals("-9999")){
			// 取得所属品牌
			map.put("brandInfoID", userInfo.getBIN_BrandInfoID());
		}
		// 取得语言区分
		map.put("language", userInfo.getLanguage());
		// 所属组织ID
		map.put("organizationID", userInfo.getBIN_OrganizationID());
		// 所属部门类型
		map.put("departType", userInfo.getDepartType());
		return map;
	}
//	
//	/**
//	 * 柜台datatable一览画面生成处理
//	 * 
//	 * @return 柜台datatable一览画面
//	 */
//	public String getCounterList() {
//		
//		Map<String, Object> map = new HashMap<String, Object>();
//		// 登陆用户信息
//		UserInfo userInfo = (UserInfo) session
//				.get(CherryConstants.SESSION_USERINFO);
//		// 所属组织
//		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
//		// 不是总部的场合
//		if(userInfo.getBIN_BrandInfoID() != -9999) {
//			// 所属品牌
//			map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
//		}
//		// 画面查询条件
//		if(form.getSSearch() != null && !"".equals(form.getSSearch())) {
//			map.put("counterKw", form.getSSearch());
//		}
//		// dataTable上传的参数设置到map
//		ConvertUtil.setForm(form, map);
//		// 取得柜台总数
//		int count = binOLCM07_BL.getCounterInfoCount(map);
//		form.setITotalDisplayRecords(count);
//		form.setITotalRecords(count);
//		if(count != 0) {
//			// 取得柜台List
//			form.setCounterInfoList(binOLCM07_BL.getCounterInfoList(map));
//		}
//		return SUCCESS;
//	}
//	
//	/**
//	 * 根据柜台ID取得柜台信息
//	 * 
//	 * @return 返回柜台信息
//	 */
//	public String getCounterInfo() throws Exception {
//		
//		Map<String, Object> map = new HashMap<String, Object>();
//		// 柜台ID
//		map.put("counterInfoId", form.getCounterInfoId());
//		// 根据柜台ID取得柜台信息
//		Map<String, Object> counterMap = binOLCM07_BL.getCounterInfo(map);
//		if(counterMap != null && !counterMap.isEmpty()) {
//			// 柜台所属区域存在的场合
//			if(counterMap.get("path") != null && !"".equals(counterMap.get("path"))) {
//				Map<String, Object> regionMap = new HashMap<String, Object>();
//				regionMap.put("path", counterMap.get("path"));
//				List<Map<String, Object>> regionList = binOLCM07_BL.getHigherRegionList(regionMap);
//				if(regionList != null && !regionList.isEmpty()) {
//					for(int i = 0; i < regionList.size(); i++) {
//						if("1".equals(regionList.get(i).get("regionType").toString())) {
//							counterMap.put("province", regionList.get(i).get("regionId"));
//						} else if("2".equals(regionList.get(i).get("regionType").toString()) || "3".equals(regionList.get(i).get("regionType").toString())) {
//							counterMap.put("city", regionList.get(i).get("regionId"));
//						} else if("4".equals(regionList.get(i).get("regionType").toString())) {
//							counterMap.put("county", regionList.get(i).get("regionId"));
//						}
//					}
//				}
//				counterMap.remove("path");
//			}
//			
//			counterMap.put("departCode", counterMap.get("counterCode"));
//			counterMap.remove("counterCode");
//			counterMap.put("departName", counterMap.get("counterNameIF"));
//			counterMap.remove("counterNameIF");
//			String result = "";
//			for(String key : counterMap.keySet()) {
//				if(counterMap.get(key) != null && !"".equals(counterMap.get(key))) {
//					result += key + "_" + counterMap.get(key).toString() + ",";
//				}
//			}
//			if(!"".equals(result)) {
//				response.setCharacterEncoding("UTF-8");
//				response.getWriter().write(result);
//			}
//		}
//		return null;
//	}
//	
//	/**
//	 * 厂商一览画面生成处理
//	 * 
//	 * @return 厂商一览画面
//	 */
//	public String popFactory() {	
//		Map<String, Object> map = new HashMap<String, Object>();
//		// 登陆用户信息
//		UserInfo userInfo = (UserInfo) session
//				.get(CherryConstants.SESSION_USERINFO);
//		// 所属组织
//		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
//		// 所属品牌
//		map.put(CherryConstants.BRANDINFOID, form.getBrandInfoId());
//		// 语言类型
//		map.put(CherryConstants.SESSION_LANGUAGE, session
//				.get(CherryConstants.SESSION_LANGUAGE));
//		// 生产厂商ID
//		map.put("manuFactId", form.getManuFactId());
//		// 画面查询条件
//		if(form.getSSearch() != null && !"".equals(form.getSSearch())) {
//			map.put("factoryKw", form.getSSearch());
//		}
//		// 取得厂商List
//		form.setFactoryList(binOLCM07_BL.getFactoryList(map));
//		return SUCCESS;
//	}
//	
//	/**
//	 * 部门一览画面生成处理
//	 * 
//	 * @return 部门一览画面
//	 */
//	public String popDepart() {	
//		Map<String, Object> map = new HashMap<String, Object>();
//		// 登陆用户信息
//		UserInfo userInfo = (UserInfo) session
//				.get(CherryConstants.SESSION_USERINFO);
//		// 用户ID
//		map.put(CherryConstants.USERID, userInfo.getBIN_UserID());
//		// 所属组织
//		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
//		// 所属品牌
//		map.put(CherryConstants.BRANDINFOID, form.getBrandInfoId());
//		// 语言类型
//		map.put(CherryConstants.SESSION_LANGUAGE, session
//				.get(CherryConstants.SESSION_LANGUAGE));
//		//  部门ID(办事处或柜台)
//		map.put("departInfoId", form.getDepartInfoId());
//		// 画面查询条件
//		if(form.getSSearch() != null && !"".equals(form.getSSearch())) {
//			map.put("factoryKw", form.getSSearch());
//		}
//		// dataTable上传的参数设置到map
//		ConvertUtil.setForm(form, map);
//		// 取得取得部门(办事处或柜台)总数
//		int count = binOLCM07_BL.getDepartInfoCount(map);
//		form.setITotalDisplayRecords(count);
//		form.setITotalRecords(count);
//		if(count != 0) {
//			// 取得部门(办事处或柜台)List
//			form.setDepartInfoList(binOLCM07_BL.getDepartInfoList(map));
//		}
//		return SUCCESS;
//	}
}
