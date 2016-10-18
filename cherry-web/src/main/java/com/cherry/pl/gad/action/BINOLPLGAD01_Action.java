/*	
 * @(#)BINOLPLGAD01_Action.java     1.0 @2012-12-5		
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
package com.cherry.pl.gad.action;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.Bean2Map;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.pl.gad.form.BINOLPLGAD01_Form;
import com.cherry.pl.gad.interfaces.BINOLPLGAD01_IF;
import com.cherry.pt.jcs.interfaces.BINOLPTJCS01_IF;
import com.opensymphony.xwork2.ModelDriven;

/**
 *
 * 小工具管理
 *
 * @author jijw
 *
 * @version  2012-12-5
 */
public class BINOLPLGAD01_Action extends BaseAction implements ModelDriven<BINOLPLGAD01_Form> {

	private static final long serialVersionUID = -2146435173576072942L;
	
	private BINOLPLGAD01_Form form = new BINOLPLGAD01_Form();
    
	@Resource(name="binOLPLGAD01_IF")
	private BINOLPLGAD01_IF binOLPLGAD01_IF;
	
	@Resource(name="binOLPTJCS01_IF")
	private BINOLPTJCS01_IF binolptjcs01IF;
	
	/** 小工具信息list **/
	public List<Map<String,Object>> gadgetInfoList;
	
	/** 小工具信息 **/
	public Map gadgetInfoMap;
	
	/** 配置集合：存放各小工具配置信息 */
	private Map confMap = new HashMap();
	
	/**
	 * 一览画面初始化
	 * @return
	 */
	public String init(){
//		Map<String,Object> map = getMap();
		
		return SUCCESS;
	}
	
	/**
	 * 
	 * 获取列表
	 * */
	public String search() throws Exception {
		
		//查询参数
		Map<String,Object> map = getMap();
		
		// form参数设置到paramMap中
		ConvertUtil.setForm(form, map);
		// 所属画面
		map.put("pageId", form.getPageId()); 
		// 小工具名称
		map.put("gadgetName", form.getGadgetName()); 
		
		// map参数trim处理
		CherryUtil.trimMap(map);
		// 取得总数
		int count = binOLPLGAD01_IF.getGadgetInfoCount(map);
		if (count > 0) {
			// 仓库List
			gadgetInfoList = binOLPLGAD01_IF.getGadgetInfoList(map);
		}
		
		// form表单设置
		form.setITotalDisplayRecords(count);
		form.setITotalRecords(count);
		// AJAX返回至dataTable结果页面
		return "BINOLPLGAD01_1";
	}
	
	/**
	 * 产品分类配置详细
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String confCategoryInit() throws Exception{
		Map<String,Object> map = getMap();
		// 小工具信息Id
		String gadgetInfoId = form.getGadgetInfoId();
		map.put("gadgetInfoId", gadgetInfoId);
		gadgetInfoMap = binOLPLGAD01_IF.getGadgetInfoById(map);
		
		// 取得产品分类信息List
		List<Map<String,Object>> list = binolptjcs01IF.getCategoryList(map);
		Map<String,Object> categoryMap = new LinkedHashMap<String, Object>();
		for(Map<String,Object> itemMap : list){
			categoryMap.put(String.valueOf(itemMap.get("propId")), itemMap.get("propNameCN"));
		}
		confMap.put("categoryMap", categoryMap);
		
		// 取得小工具参数 {"max":"7","ids":["16","17","18","268"]}
		if(null != gadgetInfoMap.get("gadgetParam")){
			String gadgetParam = gadgetInfoMap.get("gadgetParam").toString();
			Map<String,Object> gadgetParamMap = CherryUtil.json2Map(gadgetParam);
			
			// 需要显示的分类
			List<String> idsList = (List<String>)gadgetParamMap.get("ids");
			confMap.put("ids", idsList);
			
			// 分类属性显示的数量
			String cateMax = String.valueOf(gadgetParamMap.get("max")); 
			confMap.put("cateMax", cateMax);
		}
		
		return "BINOLPLGAD01_2";
	}
	
	/**
	 * 产品分类配置详细保存
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String confCategorySave() throws Exception{
		Map<String,Object> map = getMap();
		Map<String,Object> formMap = (Map<String, Object>) Bean2Map.toHashMap(form);
		map.putAll(formMap);
		// map参数trim处理
		CherryUtil.trimMap(map);
		
		// 拼接小工具参数(MapToJson)
		Map<String,Object> gadgetParamMap = new HashMap<String,Object>();
		gadgetParamMap.put("ids", form.getCategoryList());
		gadgetParamMap.put("max", form.getCateMax());
		String gadgetParam = CherryUtil.map2Json(gadgetParamMap);
		map.put("gadgetParam", gadgetParam);
		
		binOLPLGAD01_IF.updGadgetInfo(map);
		
		// 处理成功
		this.addActionMessage(getText("ICM00002"));
		return CherryConstants.GLOBAL_ACCTION_RESULT_BODY;
	}
	
	/**
	 * 取得共通map
	 * 
	 * @return
	 */
	private Map<String, Object> getMap() {
		// 参数Map
		Map<String, Object> map = new HashMap<String, Object>();
		// 用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 语言类型
		map.put(CherryConstants.SESSION_LANGUAGE, session
				.get(CherryConstants.SESSION_LANGUAGE));
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo
				.getBIN_OrganizationInfoID());
		// 所属品牌
		map.put(CherryConstants.BRANDINFOID, userInfo
				.getBIN_BrandInfoID());
		// 用户ID
		map.put(CherryConstants.USERID, userInfo.getBIN_UserID());
		
		map.put("createdBy", userInfo.getLoginName());
		map.put("createPGM", "BINOLPLGAD01");
		map.put("updatedBy",  userInfo.getLoginName());
		map.put("updatePGM", "BINOLPLGAD01");
		return map;
	}
	
	public List<Map<String, Object>> getGadgetInfoList() {
		return gadgetInfoList;
	}

	public void setGadgetInfoList(List<Map<String, Object>> gadgetInfoList) {
		this.gadgetInfoList = gadgetInfoList;
	}
	
	public Map getGadgetInfoMap() {
		return gadgetInfoMap;
	}

	public void setGadgetInfoMap(Map gadgetInfoMap) {
		this.gadgetInfoMap = gadgetInfoMap;
	}
	
	public Map getConfMap() {
		return confMap;
	}

	public void setConfMap(Map confMap) {
		this.confMap = confMap;
	}
	
	@Override
	public BINOLPLGAD01_Form getModel() {
		return form;
	}

}
