/*  
 * @(#)BINOLCM02_Action.java     1.0 2011/05/31      
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

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM02_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM05_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.cmbussiness.form.BINOLCM02_Form;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CodeTable;
import com.cherry.cm.core.PropertiesUtil;
import com.cherry.cm.util.Bean2Map;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.pt.common.ProductConstants;
import com.cherry.ss.common.PromotionConstants;
import com.googlecode.jsonplugin.JSONUtil;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 弹出框共通Action
 * @author huzude
 *
 */
@SuppressWarnings("unchecked")
public class BINOLCM02_Action extends BaseAction implements ModelDriven<BINOLCM02_Form>{

	private static final long serialVersionUID = -390400538803426653L;
	
	@Resource(name="binOLCM02_BL")
	private BINOLCM02_BL binOLCM02_BL;
	@Resource(name="binOLCM05_BL")
	private BINOLCM05_BL binOLCM05_BL;
	
	/** 系统配置项 共通BL */
	@Resource
	private BINOLCM14_BL binOLCM14_BL;
	/** 参数FORM */
	private BINOLCM02_Form form = new BINOLCM02_Form();

	@Override
	public BINOLCM02_Form getModel() {
		return form;
	}
	
	@Resource(name="CodeTable")
	private CodeTable code;

	/** 品牌List */
	private List<Map<String, Object>> brandCodeListSrh; 
	/** 分类List */
	private List<Map<String, Object>> sortListSrh;
	/**分类树*/
	private String categoryResult;
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
		// 套装折扣unitCode
		map.put("TZZK_UNIT_CODE", PromotionConstants.PROMOTION_TZZK_UNIT_CODE);
		// 取得促销产品信息
		HashMap resultMap  = binOLCM02_BL.getPromotionDialogInfoList(map);
		
		form.setPopPrmProductInfoList((List)resultMap.get(CherryConstants.POP_PRMPRODUCT_LIST));
		// 促销品类型不为促销礼品时，radioFlag为0，多选；radioFlag为1或没有，单选
		if(!PromotionConstants.PROMOTION_CXLP_TYPE_CODE.equals(form.getPromotionCateCD()) && 
				(null == form.getRadioFlag() || "1".equals(form.getRadioFlag()))){
			form.setCheckType("radio");
		}
		int count = Integer.parseInt((String.valueOf(resultMap.get("count"))));
		// form表单设置
		form.setITotalDisplayRecords(count);
		form.setITotalRecords(count);
		return "PRMProductTable_1";
	}
	
	/**
	 * 取得产品信息
	 * @return
	 * @throws Exception 
	 */
	public String getProductInfo () throws Exception{
		// 取得session信息
		Map<String, Object> map  = this.getSessionInfo();
		// form参数设置到paramMap中
		ConvertUtil.setForm(form, map);
		// 取得组织部门类型
		//binOLCM02_BL.setType(map);
		// 取得产品信息
		
		map.put("originalBrand", form.getParam());
		HashMap resultMap  = binOLCM02_BL.getProductDialogInfoList(map);
		form.setPopProductInfoList((List)resultMap.get(CherryConstants.POP_PRODUCT_LIST));
		int count = Integer.parseInt((String.valueOf(resultMap.get("count"))));
		// form表单设置
		form.setITotalDisplayRecords(count);
		form.setITotalRecords(count);
		return "ProductTable_1";
	}
	
	/**
	 * 取得产品信息
	 * @return
	 * @throws Exception 
	 */
	public String getProCateInfo () throws Exception{
		// 取得session信息
		Map<String, Object> map  = this.getSessionInfo();
		// form参数设置到paramMap中
		ConvertUtil.setForm(form, map);
		// 取得组织部门类型
		binOLCM02_BL.setType(map);
		// 取得促销产品信息
		HashMap resultMap  = binOLCM02_BL.getCateDialogInfoList(map);
		form.setPopCateInfoList((List)resultMap.get("popCateInfoList"));
		int count = Integer.parseInt((String.valueOf(resultMap.get("count"))));
		// form表单设置
		form.setITotalDisplayRecords(count);
		form.setITotalRecords(count);
		return "CateTable_1";
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
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		int brandInfoId = CherryUtil.obj2int(userInfo.getCurrentBrandInfoID());
		if (brandInfoId != CherryConstants.BRAND_INFO_ID_VALUE){
			// 取得所属品牌
			map.put(CherryConstants.BRANDINFOID, brandInfoId);
		}else{
			// 取得所属品牌
			map.put(CherryConstants.BRANDINFOID, form.getBrandInfoId());
		}
		// 取得语言区分
		map.put(CherryConstants.SESSION_LANGUAGE, userInfo.getLanguage());
		return map;
	}
	
	/**
	 * 柜台datatable一览画面生成处理
	 * 
	 * @return 柜台datatable一览画面
	 */
	public String getCounterList() {
		
		Map<String, Object> map = new HashMap<String, Object>();
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		// 所属品牌
		if(form.getBrandInfoId() != null && !"".equals(form.getBrandInfoId())) {
			map.put(CherryConstants.BRANDINFOID, form.getBrandInfoId());
		} else {
			// 不是总部的场合
			if(userInfo.getBIN_BrandInfoID() != CherryConstants.BRAND_INFO_ID_VALUE) {
				// 所属品牌
				map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
			}
		}
		// 画面查询条件
		if(form.getSSearch() != null && !"".equals(form.getSSearch())) {
			map.put("counterKw", form.getSSearch());
		}
		// dataTable上传的参数设置到map
		ConvertUtil.setForm(form, map);
		// 取得柜台总数
		int count = binOLCM02_BL.getCounterInfoCount(map);
		form.setITotalDisplayRecords(count);
		form.setITotalRecords(count);
		if(count != 0) {
			// 取得柜台List
			form.setCounterInfoList(binOLCM02_BL.getCounterInfoList(map));
		}
		return SUCCESS;
	}
	
	/**
	 * 根据柜台ID取得柜台信息
	 * 
	 * @return 返回柜台信息
	 */
	public String getCounterInfo() throws Exception {
		
		Map<String, Object> map = new HashMap<String, Object>();
		// 柜台ID
		map.put("counterInfoId", form.getCounterInfoId());
		// 根据柜台ID取得柜台信息
		Map<String, Object> counterMap = binOLCM02_BL.getCounterInfo(map);
		if(counterMap != null && !counterMap.isEmpty()) {
			// 柜台所属区域存在的场合
			if(counterMap.get("path") != null && !"".equals(counterMap.get("path"))) {
				Map<String, Object> regionMap = new HashMap<String, Object>();
				regionMap.put("path", counterMap.get("path"));
				List<Map<String, Object>> regionList = binOLCM02_BL.getHigherRegionList(regionMap);
				if(regionList != null && !regionList.isEmpty()) {
					for(int i = 0; i < regionList.size(); i++) {
						if("1".equals(regionList.get(i).get("regionType").toString())) {
							counterMap.put("province", regionList.get(i).get("regionId"));
						} else if("2".equals(regionList.get(i).get("regionType").toString()) || "3".equals(regionList.get(i).get("regionType").toString())) {
							counterMap.put("city", regionList.get(i).get("regionId"));
						} else if("4".equals(regionList.get(i).get("regionType").toString())) {
							counterMap.put("county", regionList.get(i).get("regionId"));
						}
					}
				}
				counterMap.remove("path");
			}
			
			counterMap.put("departCode", counterMap.get("counterCode"));
			counterMap.remove("counterCode");
			counterMap.put("departName", counterMap.get("counterNameIF"));
			counterMap.remove("counterNameIF");
			StringBuffer result = new StringBuffer();
			for(Map.Entry entry : counterMap.entrySet()) {
				if(entry.getValue() != null && !"".equals(entry.getValue())) {
					result.append(entry.getKey() + "_" + entry.getValue() + ",");
				}
			}
			if(result.length() > 0) {
				ConvertUtil.setResponseByAjax(response, result.toString());
			}
		}
		return null;
	}
	
	/**
	 * 厂商一览画面生成处理
	 * 
	 * @return 厂商一览画面
	 */
	public String popFactory() {	
		Map<String, Object> map = new HashMap<String, Object>();
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		// 所属品牌
		map.put(CherryConstants.BRANDINFOID, form.getBrandInfoId());
		// 语言类型
		map.put(CherryConstants.SESSION_LANGUAGE, session
				.get(CherryConstants.SESSION_LANGUAGE));
		// 生产厂商ID
		map.put("manuFactId", form.getManuFactId());
		// 画面查询条件
		if(form.getSSearch() != null && !"".equals(form.getSSearch())) {
			map.put("factoryKw", form.getSSearch());
		}
		// 取得厂商List
		form.setFactoryList(binOLCM02_BL.getFactoryList(map));
		return SUCCESS;
	}
	
	/**
	 * 部门一览画面生成处理
	 * 
	 * @return 部门一览画面
	 */
	public String popDepart() throws Exception {
		
		Map<String, Object> map = (Map<String, Object>) Bean2Map.toHashMap(form);
		map.put("validFlag", map.get("param"));
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 语言类型
		map.put(CherryConstants.SESSION_LANGUAGE, session
				.get(CherryConstants.SESSION_LANGUAGE));
		// 用户ID
		map.put(CherryConstants.USERID, userInfo.getBIN_UserID());
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		if(form.getBrandInfoId() == null || "".equals(form.getBrandInfoId())) {
			// 所属品牌
			map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		}
		if(userInfo.getBIN_BrandInfoID() ==CherryConstants.BRAND_INFO_ID_VALUE){
			map.put("privilegeFlg", "0");
		}
		// dataTable上传的参数设置到map
		ConvertUtil.setForm(form, map);
		// 取得部门总数
		int count = binOLCM02_BL.getDepartInfoCount(map);
		form.setITotalDisplayRecords(count);
		form.setITotalRecords(count);
		if(count != 0) {
			// 取得部门List
			form.setDepartInfoList(binOLCM02_BL.getDepartInfoList(map));
		}
		return SUCCESS;
	}
	
	/**
	 * 子品牌一览画面生成处理
	 * 
	 * @return 子品牌一览画面
	 */
	public String popOrigBrand() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("brandInfoId", form.getBrandInfoId());
		// dataTable上传的参数设置到map
		ConvertUtil.setForm(form, map);
		// 取得子品牌总数
		int count = binOLCM02_BL.getOrigBrandCount(map);
		form.setITotalDisplayRecords(count);
		form.setITotalRecords(count);
		if(count != 0) {
			// 取得子品牌List
			List<Map<String, Object>> origBrandList = binOLCM02_BL.getOrigBrandList(map);
			for (Map<String, Object> brandInfo : origBrandList) {
				// 子品牌代码
				String key = (String) brandInfo.get("originalBrand");
				// 子品牌名称
				String name = code.getVal("1299", key);
				brandInfo.put("origBrandName", name);
			}
			// 子品牌List
			form.setOrigBrandList(origBrandList);
		}
		return SUCCESS;
	}
	
	/**
	 * 员工一览画面生成处理
	 * 
	 * @return 员工一览画面
	 */
	public String popEmployee() {	
		Map<String, Object> map = new HashMap<String, Object>();
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 用户ID
		map.put(CherryConstants.USERID, userInfo.getBIN_UserID());
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		// 所属品牌
		if(form.getBrandInfoId() != null && !"".equals(form.getBrandInfoId())) {
			map.put(CherryConstants.BRANDINFOID, form.getBrandInfoId());
		} else {
			map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		}
		// 语言类型
		map.put(CherryConstants.SESSION_LANGUAGE, session
				.get(CherryConstants.SESSION_LANGUAGE));
		//  员工ID
		map.put("employeeId", form.getEmployeeId());
		// 画面查询条件
		if(form.getSSearch() != null && !"".equals(form.getSSearch())) {
			map.put("factoryKw", form.getSSearch());
		}
		// dataTable上传的参数设置到map
		ConvertUtil.setForm(form, map);
		// 取得员工总数
		int count = binOLCM02_BL.getEmployeeCount(map);
		form.setITotalDisplayRecords(count);
		form.setITotalRecords(count);
		if(count != 0) {
			// 取得员工List
			form.setEmployeeList(binOLCM02_BL.getEmployeeList(map));
		}
		return SUCCESS;
	}
	
	/**
	 * 考核问卷一览画面生产处理
	 * 
	 * @return 考核问卷一览画面
	 */
	public String popCheckPaper() {
		
		Map<String, Object> map = new HashMap<String, Object>();
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		// 所属品牌
		if(form.getBrandInfoId() != null && !"".equals(form.getBrandInfoId())) {
			map.put(CherryConstants.BRANDINFOID, form.getBrandInfoId());
		} else {
			// 不是总部的场合
			if(userInfo.getBIN_BrandInfoID() != CherryConstants.BRAND_INFO_ID_VALUE) {
				// 所属品牌
				map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
			}
		}
		// 画面查询条件
		if(form.getSSearch() != null && !"".equals(form.getSSearch())) {
			map.put("paperName", form.getSSearch());
		}
		// dataTable上传的参数设置到map
		ConvertUtil.setForm(form, map);
		// 取得考核问卷总数
		int count = binOLCM02_BL.getCheckPaperCount(map);
		form.setITotalDisplayRecords(count);
		form.setITotalRecords(count);
		if(count != 0) {
			// 取得考核问卷List
			form.setCheckPaperList(binOLCM02_BL.getCheckPaperList(map));
		}
		return SUCCESS;
	}
	
	/**
	 * 考核问卷一览画面生产处理
	 * 
	 * @return 考核问卷一览画面
	 */
	public String popPaperAnswer() {
		
		Map<String, Object> map = new HashMap<String, Object>();
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		// 所属品牌
		if(form.getBrandInfoId() != null && !"".equals(form.getBrandInfoId())) {
			map.put(CherryConstants.BRANDINFOID, form.getBrandInfoId());
		} else {
			// 不是总部的场合
			if(userInfo.getBIN_BrandInfoID() != CherryConstants.BRAND_INFO_ID_VALUE) {
				// 所属品牌
				map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
			}
		}
		// 画面查询条件
		if(form.getSSearch() != null && !"".equals(form.getSSearch())) {
			map.put("paperName", form.getSSearch());
		}
		// dataTable上传的参数设置到map
		ConvertUtil.setForm(form, map);
		// 取得考核问卷总数
		int count = binOLCM02_BL.getPaperCount(map);
		form.setITotalDisplayRecords(count);
		form.setITotalRecords(count);
		if(count != 0) {
			// 取得考核问卷List
			form.setCheckPaperList(binOLCM02_BL.getPaperList(map));
		}
		return SUCCESS;
	}
	
	/**
	 * 初始化产品分类信息弹出框
	 * @return
	 * @throws Exception
	 */
	public String initPrtCateDialog() throws Exception {
		Map<String,Object> map = getCommMap();
		if(CherryChecker.isNullOrEmpty(form.getParam1()) && CherryChecker.isNullOrEmpty(form.getTeminalFlag())) {
			List<Map<String, Object>> prtCategoryList = binOLCM02_BL.getPrtCategoryList(map);
			form.setPrtCategoryList(prtCategoryList);
		}
		
		// 是否小店云系统模式 1:是  0:否
		String isPosCloud = binOLCM14_BL.getConfigValue("1304", String.valueOf(map.get("organizationInfoId")), String.valueOf(map.get("brandInfoId")));
//		isPosCloud = "0";
		form.setIsPosCloud(isPosCloud);;
		return SUCCESS;
	}

	/**
	 * 初始化产品分类信息弹出框
	 * @return
	 * @throws Exception
	 */
	public void initTreeCategory() throws Exception {
		Map<String,Object> map = getCommMap();
		Map<String,Object> resultMap = binOLCM02_BL.getCategoryTreeInfoList(map);
		List<Map<String,Object>>resultList = (List)resultMap.get(CherryConstants.CATEGORY_TREE_LIST);
		categoryResult = CherryUtil.obj2Json(resultList);
		ConvertUtil.setResponseByAjax(response, categoryResult);
	}

	/**
	 * 取得产品分类信息弹出框
	 * @return
	 * @throws Exception
	 */
	public String popPrtCateDialog() throws Exception {
		Map<String,Object> map = getCommMap();
		
		// 是否小店云系统模式 1:是  0:否
		String isPosCloud = binOLCM14_BL.getConfigValue("1304", String.valueOf(map.get("organizationInfoId")), String.valueOf(map.get("brandInfoId")));
		
		List<String> filterList = new ArrayList<String>();
		// 弹出框输入框匹配字段
		filterList.add("PropValueCherry");
		filterList.add("PropValueChinese");
		filterList.add("PropValueForeign");
		map.put(CherryConstants.FILTER_LIST_NAME, filterList);
		// 分类属性值是否已绑定
		if(CherryConstants.IS_POSCLOUD_1.equals(isPosCloud)){
			map.put("isBind", "0"); // 小店云场景模式时，取得全部分类属性值
		}else{
			map.put("isBind", form.getParam());
		}
		
		// validFlag 产品类别对应表添加有效区分条件
//		map.put(CherryConstants.VALID_FLAG, CherryConstants.VALIDFLAG_ENABLE);  NEWWITPOS-1561 去除
		// 分类属性ID
		map.put(ProductConstants.PROPID, form.getParam1());
		List<Map<String, Object>> cateList = null;
		if(!CherryChecker.isNullOrEmpty(form.getParam2())){
			cateList = (List<Map<String, Object>>)JSONUtil.deserialize(form.getParam2());
		}
		map.put("cateList", cateList);
		// 取得产品分类总数
		int count = binOLCM02_BL.getCateValCount(map);
		form.setITotalDisplayRecords(count);
		form.setITotalRecords(count);
		if(count != 0) {
			form.setResultList(binOLCM02_BL.getCateValList(map));
		}
		return SUCCESS;
	}
	/**
	 * 初始化产品分类信息弹出框
	 * @return
	 * @throws Exception
	 */
	public String initPrmCateDialog() throws Exception {
		return SUCCESS;
	}
	/**
	 * 取得促销品分类信息弹出框
	 * @return
	 * @throws Exception
	 */
	public String popPrmCateDialog() throws Exception {
		Map<String,Object> map = getCommMap();
		List filterList = new ArrayList();
		map.put("cateType", form.getParam());
		// 弹出框输入框匹配字段
		filterList.add("cateCode");
		filterList.add("cateName");
		map.put(CherryConstants.FILTER_LIST_NAME, filterList);
		// 取得促销品分类总数
		int count = binOLCM02_BL.getPrmCateCount(map);
		form.setITotalDisplayRecords(count);
		form.setITotalRecords(count);
		if(count != 0) {
			form.setResultList(binOLCM02_BL.getPrmCateList(map));
		}
		return SUCCESS;
	}
	
	/**
	 * 初始化产品信息弹出框
	 * @return
	 * @throws Exception
	 */
	public String initPrtDialog() throws Exception {
		Map<String,Object> map = getCommMap();
		// 是否小店云系统模式 1:是  0:否
		String isPosCloud = binOLCM14_BL.getConfigValue("1304", String.valueOf(map.get("organizationInfoId")), String.valueOf(map.get("brandInfoId")));
		isPosCloud = "0";
		form.setIsPosCloud(isPosCloud);
		//读取配置项，将最大选择数目读取出来
		String maxCount = binOLCM14_BL.getConfigValue("1394", String.valueOf(map.get("organizationInfoId")), String.valueOf(map.get("brandInfoId")));
		form.setMaxCount(Integer.parseInt(maxCount));
		return SUCCESS;
	}
	
	/**
	 * 初始化产品信息弹出框(薇诺娜)
	 * @return
	 * @throws Exception
	 */
	public String initPrtDialogOne() throws Exception {
		Map<String,Object> map = getCommMap();
		// 是否小店云系统模式 1:是  0:否
		String isPosCloud = binOLCM14_BL.getConfigValue("1304", String.valueOf(map.get("organizationInfoId")), String.valueOf(map.get("brandInfoId")));
		isPosCloud = "0";
		form.setIsPosCloud(isPosCloud);
		
		return SUCCESS;
	}
	
	/**
	 * 初始化产品信息弹出框(浓妆淡抹订货)
	 * @return
	 * @throws Exception
	 */
	public String initPrtDialogTwo() throws Exception {
		Map<String,Object> map = getCommMap();
		/** 品牌List */
	    brandCodeListSrh = binOLCM02_BL.getBrandCodeListSrh(map); 
	    form.setBrandCodeListSrh(brandCodeListSrh);
		/** 分类List */
		sortListSrh = binOLCM02_BL.getSortListSrh(map);
		form.setSortListSrh(sortListSrh);
		
		return SUCCESS;
	}
	
	/**
	 * 初始化部门弹出框
	 * 
	 * */
	public String initDepDialog()throws Exception{
		return SUCCESS;
	}
	
	/**
	 * 初始会员导入弹出框
	 * @return
	 * @throws Exception
	 */
	public String initMemImportDialog() throws Exception {
		String searchCode =ConvertUtil.getString(form.getSearchCode());
		if(!"".equals(searchCode)&&searchCode!=null){
			Map<String, Object> map = getCommMap();
			map.put("searchCode", searchCode);
			Map<String, Object> recordMap =binOLCM02_BL.getSearchMemInfo(map);
			//对象批次名称
			form.setRecordName(ConvertUtil.getString(recordMap.get("recordName")));
			//对象总数量
			form.setRecordCount(ConvertUtil.getString(recordMap.get("recordCount")));
			//对象类型
			form.setCustomerType(ConvertUtil.getString(recordMap.get("customerType")));
		}
		return SUCCESS;
	}
	
	/**
	 * 初始会员导入弹出框
	 * @return
	 * @throws Exception
	 */
	public String initCouponDialog() throws Exception {
		String batchCode =ConvertUtil.getString(form.getBatchCode());
		if(!"".equals(batchCode)&&batchCode!=null){
			Map<String, Object> map = getCommMap();
			map.put("batchCode", batchCode);
			int couponCount =binOLCM02_BL.getCouponCount(map);
			//Coupon总数量
			form.setRecordCount(ConvertUtil.getString(couponCount));
		}
		return SUCCESS;
	}
	
	/**
	 * 取得产品信息弹出框
	 * @return
	 * @throws Exception
	 */
	public String popPrtDialog() throws Exception {
		Map<String,Object> map = getCommMap();
		
		// 剔除产品或促销品
		if(null != form.getIgnorePrtPrmVendorID() && !"".equals(form.getIgnorePrtPrmVendorID())){
			List<String> ingorePrtList = getIgnorePrtPrm(map, "N");
			map.put("ingorePrtList", ingorePrtList);
		}
		
		// 取得系统配置项产品方案添加模式
		String config = binOLCM14_BL.getConfigValue("1288", String.valueOf(map.get("organizationInfoId")), String.valueOf(map.get("brandInfoId")));
		if(ProductConstants.SOLU_ADD_MODE_CONFIG_2.equals(config) 
				|| ProductConstants.SOLU_ADD_MODE_CONFIG_3.equals(config)){
			map.put("soluCateConfig", config);
		}
		
		// 子品牌
		map.put("originalBrand", form.getParam());
		
		// 自选有效区分
		String validFlag = form.getParam1();
		if(!ConvertUtil.isBlank(validFlag)){
			map.put("popValidFlag", validFlag); // 使用自选有效区分代替默认有效区分
		}
		
		// 取得产品信息
		HashMap resultMap  = binOLCM02_BL.getProductDialogInfoList(map);
		form.setPopProductInfoList((List)resultMap.get(CherryConstants.POP_PRODUCT_LIST));
		int count = Integer.parseInt((String.valueOf(resultMap.get("count"))));
		// form表单设置
		form.setITotalDisplayRecords(count);
		form.setITotalRecords(count);
		return SUCCESS;
	}
	
	/**
	 * 取得产品信息(带图片模式)
	 * @return
	 * @throws Exception
	 */
	public String popPrtImageDialog() throws Exception {
		Map<String,Object> map = getCommMap();
		form.setProductCategoryList(binOLCM02_BL.getProductCategoryList(map));
		form.setProductCategoryTemp(form.getProductCategoryId());
		// 剔除产品或促销品
		if(null != form.getIgnorePrtPrmVendorID() && !"".equals(form.getIgnorePrtPrmVendorID())){
			List<String> ingorePrtList = getIgnorePrtPrm(map, "N");
			map.put("ingorePrtList", ingorePrtList);
		}
		
		// 取得系统配置项产品方案添加模式
		String config = binOLCM14_BL.getConfigValue("1288", String.valueOf(map.get("organizationInfoId")), String.valueOf(map.get("brandInfoId")));
		if(ProductConstants.SOLU_ADD_MODE_CONFIG_2.equals(config) 
				|| ProductConstants.SOLU_ADD_MODE_CONFIG_3.equals(config)){
			map.put("soluCateConfig", config);
		}
		
		//只有新品被选中了，才会去查新品
		if(!CherryChecker.isNullOrEmpty(form.getIsNewProductFlag())){
			// 取得系统配置项新品的天数
			config = binOLCM14_BL.getConfigValue("1380", String.valueOf(map.get("organizationInfoId")), String.valueOf(map.get("brandInfoId")));
			map.put("newProductDays", "-"+config);//取反
		}
		
		// 子品牌
		map.put("originalBrand", form.getOriginalBrand());
		// 产品名称
		map.put("nameTotal", form.getNameTotal());
		
		// 自选有效区分
		String validFlag = form.getParam1();
		if(!ConvertUtil.isBlank(validFlag)){
			map.put("popValidFlag", validFlag); // 使用自选有效区分代替默认有效区分
		}
		
		// 取得产品信息
		HashMap resultMap  = binOLCM02_BL.getProductImageDInfoList(map);
		
		String json = form.getQueStr();
		List<Map<String, Object>> prtVendorIdlist =  (List<Map<String, Object>>) JSONUtil.deserialize(json);
		

		List<Map<String, Object>> tempList = (List<Map<String, Object>>)ConvertUtil.byteClone(resultMap.get(CherryConstants.POP_PRODUCT_LIST));
		List<Map<String, Object>> list = (List<Map<String, Object>>)ConvertUtil.byteClone(resultMap.get(CherryConstants.POP_PRODUCT_LIST));	
		List<Map<String, Object>> inventoryList = new ArrayList<Map<String,Object>>();//库存集合List
		
		if(!list.isEmpty() && list.size()>0){

			for(int i=0; i<tempList.size(); i++){
				Map<String, Object> tempMap = tempList.get(i);
				if(CherryChecker.isNullOrEmpty(tempMap.get("ItemCode"))){
					tempList.remove(i--);

				}
			}
			
			int pageNo=form.getPageNo();//当前为第几页
			int pageSize=form.getPageSize();//每页显示多少条
			int startNumber= ((pageNo-1)*pageSize)+1;//当前页的开始数
			int endNumber=pageNo*pageSize;//当前页的结束数
			
			if(!tempList.isEmpty() && tempList.size()>0){//去除ItemCode为空的产品以后的List
				String param = null;//调金蝶库存接口的参数
				for(Map<String, Object> tempMap:tempList){
					if(pageNo==0){//表示初始化刚进来
						if((ConvertUtil.getInt(tempMap.get("RowNumber")))>=1 &&(ConvertUtil.getInt(tempMap.get("RowNumber")))<=20){
							if(CherryChecker.isNullOrEmpty(param)){
								param = ConvertUtil.getString(tempMap.get("ItemCode")); 
							}else{
								param +=","+ConvertUtil.getString(tempMap.get("ItemCode"));
							}
						}
					}else{
						if((ConvertUtil.getInt(tempMap.get("RowNumber")))>=startNumber &&(ConvertUtil.getInt(tempMap.get("RowNumber")))<=endNumber){
							if(CherryChecker.isNullOrEmpty(param)){
								param = ConvertUtil.getString(tempMap.get("ItemCode")); 
							}else{
								param +=","+ConvertUtil.getString(tempMap.get("ItemCode"));
							}
						}
					}
				}
				
				if(!CherryChecker.isNullOrEmpty(param)){//参数不为空
					Map<String, Object> paramMap= new HashMap<String, Object>();
					paramMap.put("ItemCode", param);
					inventoryList= binOLCM02_BL.getInventoryByItemCode(paramMap);//返回的库存信息List
				}
			}
			
			
			
			for(Map<String, Object> productMap:list){
				String barCode = ConvertUtil.getString(productMap.get("barCode"));//产品条码
				String productImagePath  = PropertiesUtil.pps.getProperty("virtualdirectory.product")+"/"+barCode+".jpg";//图片保存的路径,图片默认在某一目录下面，图片名称和产品条码一致，尾缀.jpg
				String virtualdirectoryPath = PropertiesUtil.pps.getProperty("virtualdirectory.product.url");//产品图片显示的虚拟目录
				
				File file=new File(productImagePath);    
				if(file.exists()){ //表示图片存在  
					productMap.put("productImagePath",virtualdirectoryPath+"/"+barCode+".jpg");
				} else{//表示图片不存在，使用默认图片  
					productMap.put("productImagePath", virtualdirectoryPath+"/default.jpg");
				}
				
				if(!inventoryList.isEmpty() && inventoryList.size()>0){//库存集合不为空的情况
					for(Map<String, Object> stockMap:inventoryList){//给每个产品设置库存
						int amount=0;
						if(!CherryChecker.isNullOrEmpty(stockMap.get("IFProductId")) && !CherryChecker.isNullOrEmpty(productMap.get("ItemCode"))){									
							if(ConvertUtil.getString(stockMap.get("IFProductId")).equals(ConvertUtil.getString(productMap.get("ItemCode")))){
								if(!CherryChecker.isNullOrEmpty(stockMap.get("Quantity"))){
									amount= ConvertUtil.getInt(stockMap.get("Quantity"));
								}
							}
						}				
						productMap.put("amount", amount);
						// amount不为零，表示获取到金蝶库存跳出循环
						if(0!=amount){
							break;
						}
					}
				}else{//库存集合为空的情况
					productMap.put("amount", 0);
				}
						
				for(Map<String, Object> tempMap:prtVendorIdlist){
					if(ConvertUtil.getString(tempMap.get("prtVendorId")).equals(ConvertUtil.getString(productMap.get("BIN_ProductVendorID")))){
						tempMap.put("unitCodeArr", productMap.get("unitCode"));//产品厂商编码
						tempMap.put("barCodeArr", productMap.get("barCode"));//产品条码						
						tempMap.put("productName", productMap.get("nameTotal"));//产品名称	
						if(0 != ConvertUtil.getInt(productMap.get("amount"))) {
							tempMap.put("amount", productMap.get("amount"));//库存数量
						}
						tempMap.put("distributionPrice", productMap.get("distributionPrice"));//配送价
					}
				}
				
				
			}
		}
		form.setQueStr(CherryUtil.obj2Json(prtVendorIdlist));
		form.setPopProductInfoList(list);
		return SUCCESS;
	}
	
	/**
	 * 取得产品信息弹出框(薇诺娜)
	 * @return
	 * @throws Exception
	 */
	public String popPrtDialogOne() throws Exception {
		Map<String,Object> map = getCommMap();
		
		// 剔除产品或促销品
		if(null != form.getIgnorePrtPrmVendorID() && !"".equals(form.getIgnorePrtPrmVendorID())){
			List<String> ingorePrtList = getIgnorePrtPrm(map, "N");
			String[] ingorePrtListArr = new String[ingorePrtList.size()];
			for(int i =0;i<ingorePrtList.size();i++){
				ingorePrtListArr[i]=ingorePrtList.get(i).toString();
			}
			map.put("ingorePrtListArr", ingorePrtListArr);
		}
		
		// 取得系统配置项产品方案添加模式
		String config = binOLCM14_BL.getConfigValue("1288", String.valueOf(map.get("organizationInfoId")), String.valueOf(map.get("brandInfoId")));
		if(ProductConstants.SOLU_ADD_MODE_CONFIG_2.equals(config) 
				|| ProductConstants.SOLU_ADD_MODE_CONFIG_3.equals(config)){
			map.put("soluCateConfig", config);
		}
		
		// 子品牌
		map.put("originalBrand", form.getParam());
		
		// 自选有效区分
		String validFlag = form.getParam1();
		if(!ConvertUtil.isBlank(validFlag)){
			map.put("popValidFlag", validFlag); // 使用自选有效区分代替默认有效区分
		}
		
		// 取得产品信息
		HashMap resultMap  = binOLCM02_BL.getProductDialogInfoListOne(map);
		form.setPopProductInfoOneList((List)resultMap.get(CherryConstants.POP_PRODUCT_LIST));
		int count = Integer.parseInt((String.valueOf(resultMap.get("count"))));
		// form表单设置
		form.setITotalDisplayRecords(count);
		form.setITotalRecords(count);
		return SUCCESS;
	}
	
	/**
	 * 取得产品信息弹出框（浓妆淡抹订货）
	 * @return
	 * @throws Exception
	 */
	public String popPrtDialogTwo() throws Exception {
		Map<String,Object> map = getCommMap();
		// 剔除产品或促销品
		if(null != form.getIgnorePrtPrmVendorID() && !"".equals(form.getIgnorePrtPrmVendorID())){
			List<String> ingorePrtList = getIgnorePrtPrm(map, "N");
			map.put("ingorePrtList", ingorePrtList);
		}
		
		// 取得系统配置项产品方案添加模式
		String config = binOLCM14_BL.getConfigValue("1288", String.valueOf(map.get("organizationInfoId")), String.valueOf(map.get("brandInfoId")));
		if(ProductConstants.SOLU_ADD_MODE_CONFIG_2.equals(config) 
				|| ProductConstants.SOLU_ADD_MODE_CONFIG_3.equals(config)){
			map.put("soluCateConfig", config);
		}
		
		// 子品牌
		map.put("originalBrand", form.getParam());
		
		// 自选有效区分
		String validFlag = form.getParam1();
		if(!ConvertUtil.isBlank(validFlag)){
			map.put("popValidFlag", validFlag); // 使用自选有效区分代替默认有效区分
		}
		
		// 取得产品信息
		HashMap resultMap  = binOLCM02_BL.getProductDialogInfoListTwo(map);
		
		// 添加库存
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		list =(List<Map<String, Object>>)resultMap.get(CherryConstants.POP_PRODUCT_LIST);
		
//		if(!list.isEmpty() && list.size()>0){
//			for(Map<String, Object> productMap:list){
//				int stockAmount=0;//库存数量暂时写死（后续从金蝶那边读取）
//				if(!CherryChecker.isNullOrEmpty(productMap.get("ItemCode"))){					
//					Map<String, Object> inventoryMap = binOLCM02_BL.getInventoryByItemCode(productMap);//返回的库存信息					
//					if(inventoryMap!=null && !inventoryMap.isEmpty()) {
//						if(!CherryChecker.isNullOrEmpty(inventoryMap.get("Quantity"))){
//							stockAmount= ConvertUtil.getInt(inventoryMap.get("Quantity"));
//						}
//					}
//				}				
//				productMap.put("stockAmount", stockAmount);
//			}
//		}
		form.setPopProductInfoList(list);
		int count = Integer.parseInt((String.valueOf(resultMap.get("count"))));
		// form表单设置
		form.setITotalDisplayRecords(count);
		form.setITotalRecords(count);
		return SUCCESS;
	}
	/**
	 * 剔除产品或促销品
	 * @param map
	 * @param type
	 * @return
	 */
	private List<String> getIgnorePrtPrm(Map<String,Object> map, String type ){
		// 用于查询各商品统计的产品查询list
		List<String> ingorePrtList = new ArrayList<String>();
		// 用于查询各商品统计的促销品查询list
		List<String> ingorePrmList = new ArrayList<String>();
		
		String [] ignorePrtVendorIDArr = form.getIgnorePrtPrmVendorID().split(",");
		
		for(String itemString : ignorePrtVendorIDArr){
			String prmPrtVendorId = itemString.split("_")[0];
			String prtType = itemString.split("_")[1];
			
			if("N".equals(prtType)){
				ingorePrtList.add(prmPrtVendorId);
			} 
			else if ("P".equals(prtType)){
				ingorePrmList.add(prmPrtVendorId);
			}
		}
		
		return type.equals("N") ? ingorePrtList : ingorePrmList;
	}
	
	/**
	 * 初始化促销品信息弹出框
	 * @return
	 * @throws Exception
	 */
	public String initPrmDialog() throws Exception {
		return SUCCESS;
	}
	
	/**
	 * 初始化促销品信息弹出框(薇诺娜，合并)
	 * @return
	 * @throws Exception
	 */
	public String initPrmDialogOne() throws Exception {
		return SUCCESS;
	}
	/**
	 * 取得促销品信息弹出框
	 * @return
	 * @throws Exception
	 */
	public String popPrmDialog() throws Exception {
		Map<String,Object> map = getCommMap();
		map.put("promotionCateCD", form.getParam());
		// 套装折扣unitCode
		map.put("TZZK_UNIT_CODE", PromotionConstants.PROMOTION_TZZK_UNIT_CODE);
		// 剔除产品或促销品
		if(null != form.getIgnorePrtPrmVendorID() && !"".equals(form.getIgnorePrtPrmVendorID())){
			List<String> ingorePrmList = getIgnorePrtPrm(map, "P");
			map.put("ingorePrmList", ingorePrmList);
		}
		// 取得促销产品信息
		HashMap resultMap  = binOLCM02_BL.getPromotionDialogInfoList(map);
		List list = (List)resultMap.get(CherryConstants.POP_PRMPRODUCT_LIST);
		int count = Integer.parseInt((String.valueOf(resultMap.get("count"))));
		// form表单设置
		form.setPopPrmProductInfoList(list);
		form.setITotalDisplayRecords(count);
		form.setITotalRecords(count);
		return SUCCESS;
	}
	
	/**
	 * 取得促销品信息弹出框(薇诺娜，合并)
	 * @return
	 * @throws Exception
	 */
	public String popPrmDialogOne() throws Exception {
		Map<String,Object> map = getCommMap();
		map.put("promotionCateCD", form.getParam());
		// 套装折扣unitCode
		map.put("TZZK_UNIT_CODE", PromotionConstants.PROMOTION_TZZK_UNIT_CODE);
		// 剔除产品或促销品
		if(null != form.getIgnorePrtPrmVendorID() && !"".equals(form.getIgnorePrtPrmVendorID())){
			List<String> ingorePrmList = getIgnorePrtPrm(map, "P");
			String[] ingorePrmListArr = new String[ingorePrmList.size()];
			for(int i =0;i<ingorePrmList.size();i++){
				ingorePrmListArr[i]=ingorePrmList.get(i).toString();
			}
			map.put("ingorePrmListArr", ingorePrmListArr);
		}
		// 取得促销产品信息
		HashMap resultMap  = binOLCM02_BL.getPromotionDialogInfoListOne(map);
		List list = (List)resultMap.get(CherryConstants.POP_PRMPRODUCT_LIST);
		int count = Integer.parseInt((String.valueOf(resultMap.get("count"))));
		// form表单设置
		form.setPopPrmProductInfoList(list);
		form.setITotalDisplayRecords(count);
		form.setITotalRecords(count);
		return SUCCESS;
	}
	/**
	 * 初始化会员信息弹出框
	 * @return
	 * @throws Exception
	 */
	public String initMemberDialog() throws Exception {
		return SUCCESS;
	}
	/**
	 * 取得会员信息弹出框
	 * @return
	 * @throws Exception
	 */
	public String popMemberDialog() throws Exception {
		Map<String,Object> map = getCommMap();
		// 取得产品信息
		HashMap resultMap  = binOLCM02_BL.getMemberDialogInfoList(map);
		form.setPopMemberInfoList((List)resultMap.get(CherryConstants.POP_MEMBER_LIST));
		int count = Integer.parseInt((String.valueOf(resultMap.get("count"))));
		// form表单设置
		form.setITotalDisplayRecords(count);
		form.setITotalRecords(count);
		return SUCCESS;
	}
	/**
	 * 初始化对象批次信息弹出框
	 * @return
	 * @throws Exception
	 */
	public String initObjBatchDialog() throws Exception {
		return SUCCESS;
	}
	/**
	 * 取得对象批次信息弹出框
	 * @return
	 * @throws Exception
	 */
	public String popObjBatchDialog() throws Exception {
		Map<String,Object> map = getCommMap();
		// 取得产品信息
		HashMap resultMap  = binOLCM02_BL.getObjBatchInfoList(map);
		form.setPopObjBatchList((List)resultMap.get(CherryConstants.POP_OBJBATCH_LIST));
		int count = Integer.parseInt((String.valueOf(resultMap.get("count"))));
		// form表单设置
		form.setITotalDisplayRecords(count);
		form.setITotalRecords(count);
		return SUCCESS;
	}
	/**
	 * 初始化柜台信息弹出框
	 * @return
	 * @throws Exception
	 */
	public String initCounterDialog() throws Exception {
		return SUCCESS;
	}
	/**
	 * 取得柜台信息弹出框
	 * @return
	 * @throws Exception
	 */
	public String popCounterDialog() throws Exception {
		Map<String,Object> map = getCommMap();
		// 画面查询条件
		if(form.getSSearch() != null && !"".equals(form.getSSearch())) {
			map.put("counterKw", form.getSSearch());
		}
		if(form.getPrivilegeFlg() != null && "1".equals(form.getPrivilegeFlg())) {
			// 登陆用户信息
			UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
			if(form.getUserId() != null && !"".equals(form.getUserId())) {
				map.put("userId", form.getUserId());
			}else{
				// 用户ID
				map.put("userId", userInfo.getBIN_UserID());
			}
			// 业务类型
			map.put("businessType", "0");
			// 操作类型
			map.put("operationType", "1");
			// 是否带权限查询
			map.put("privilegeFlag", form.getPrivilegeFlg());
		}
		if (!CherryChecker.isNullOrEmpty(form.getMemberClubId())) {
			map.put("memberClubId", form.getMemberClubId());
		}
		// 取得柜台总数
		int count  = binOLCM02_BL.getCounterInfoCount(map);
		
		// form表单设置
		form.setITotalDisplayRecords(count);
		form.setITotalRecords(count);
		if(count > 0) {
			// 取得柜台List
			form.setCounterInfoList(binOLCM02_BL.getCounterInfoList(map));
		}
		return SUCCESS;
	}
	/**
	 * 初始化员工信息弹出框
	 * @return
	 * @throws Exception
	 */
	public String initEmployeeDialog() throws Exception {
		return SUCCESS;
	}
	/**
	 * 取得员工信息弹出框
	 * @return
	 * @throws Exception
	 */
	public String popEmployeeDialog() throws Exception {
		Map<String,Object> map = getCommMap();
		// 画面查询条件
		if(form.getSSearch() != null && !"".equals(form.getSSearch())) {
			map.put("employeeKw", form.getSSearch());
		}
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
		// 用户ID
		map.put("userId", userInfo.getBIN_UserID());
		// 业务类型
		map.put("businessType", "0");
		// 操作类型
		map.put("operationType", "1");
		// 是否带权限查询
		map.put("privilegeFlag", form.getPrivilegeFlg());
		// 是否带权限查询
		map.put("privilegeFlag", session.get(CherryConstants.SESSION_PRIVILEGE_FLAG));
		// 取得员工总数
		int count  = binOLCM02_BL.getEmployeeInfoCount(map);
		// form表单设置
		form.setITotalDisplayRecords(count);
		form.setITotalRecords(count);
		if(count > 0) {
			// 取得员工List
			form.setEmployeeList(binOLCM02_BL.getEmployeeInfoList(map));
		}
		return SUCCESS;
	}
	
	/**
	 * 初始化人员信息弹出框
	 * @return
	 * @throws Exception
	 */
	public String initUserDialog() throws Exception {
		return SUCCESS;
	}
	
	/**
	 * 取得员工信息弹出框
	 * @return
	 * @throws Exception
	 */
	public String popUserDialog() throws Exception {
		Map<String,Object> map = getCommMap();
		// 画面查询条件
		if(form.getSSearch() != null && !"".equals(form.getSSearch())) {
			map.put("userKw", form.getSSearch());
		}
		// 取得员工总数
		int count  = binOLCM02_BL.getUserInfoCount(map);
		// form表单设置
		form.setITotalDisplayRecords(count);
		form.setITotalRecords(count);
		if(count > 0) {
			// 取得人员List
			form.setUserList(binOLCM02_BL.getUserInfoList(map));
		}
		return SUCCESS;
	}
	
	/**
	 * 初始化部门信息弹出框
	 * @return
	 * @throws Exception
	 */
	public String initAllDepartDialog() throws Exception {
		return SUCCESS;
	}
	
	/**
	 * 取得部门信息弹出框
	 * @return
	 * @throws Exception
	 */
	public String popAllDepartDialog() throws Exception {
		Map<String,Object> map = getCommMap();
		// 画面查询条件
		if(form.getSSearch() != null && !"".equals(form.getSSearch())) {
			map.put("departKw", form.getSSearch());
		}
		// 取得员工总数
		int count  = binOLCM02_BL.getAllDepartInfoCount(map);
		// form表单设置
		form.setITotalDisplayRecords(count);
		form.setITotalRecords(count);
		if(count > 0) {
			// 取得人员List
			form.setDepartList(binOLCM02_BL.getAllDepartInfoList(map));
		}
		return SUCCESS;
	}
	
	/**
	 * 初始化区域树弹出框
	 * @return
	 * @throws Exception
	 */
	public String initRegionDialog() throws Exception {
		
		if(form.getHasSelExclusiveFlag() == null || "".equals(form.getHasSelExclusiveFlag())) {
			form.setHasSelExclusiveFlag("1");
		}
		return SUCCESS;
	}
	/**
	 * 取得区域树信息
	 * @return
	 * @throws Exception
	 */
	public void popRegionDialog() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		// 所属品牌
		if(form.getBrandInfoId() != null && !"".equals(form.getBrandInfoId())) {
			map.put(CherryConstants.BRANDINFOID, form.getBrandInfoId());
		} else {
			// 不是总部的场合
			if(userInfo.getBIN_BrandInfoID() != CherryConstants.BRAND_INFO_ID_VALUE) {
				// 所属品牌
				map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
			}
		}
		if (!CherryChecker.isNullOrEmpty(form.getMemberClubId())) {
			map.put("memberClubId", form.getMemberClubId());
		}
		map.put("couValidFlag", form.getPopCouValidFlag());
		if(form.getPrivilegeFlg() != null && "1".equals(form.getPrivilegeFlg())) {
			if(form.getUserId() != null && !"".equals(form.getUserId())) {
				map.put("userId", form.getUserId());
			}else{
				// 用户ID
				map.put("userId", userInfo.getBIN_UserID());
			}
			// 业务类型
			map.put("businessType", "0");
			// 操作类型
			map.put("operationType", "1");
			// 是否带权限查询
			map.put("privilegeFlag", form.getPrivilegeFlg());
		}
		if("1".equals(form.getSelMode())) {
			// 查询区域信息List
			List<Map<String, Object>> provinceCityList = binOLCM02_BL.getRegionList(map);
			ConvertUtil.setResponseByAjax(response, provinceCityList);
		} else if("2".equals(form.getSelMode())) {
			// 查询大区信息List
			List<Map<String, Object>> regionIdList = binOLCM02_BL.getRegionIdList(map);
			ConvertUtil.setResponseByAjax(response, regionIdList);
		} else if("3".equals(form.getSelMode())) {
			// 查询部门信息List
			List<Map<String, Object>> departList = binOLCM02_BL.getDepartList(map);
			ConvertUtil.setResponseByAjax(response, departList);
			// 按所属系统
		} else if ("4".equals(form.getSelMode())) {
			List<Map<String, Object>> resultTreeList = new ArrayList<Map<String, Object>>();
			List<Map<String, Object>> list = code.getCodes("1309");
			List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
			if(null != list && list.size() > 0){
				for(Map<String, Object> item : list){
					Map<String, Object> temp = new HashMap<String, Object>();
					temp.put("id", "B" + item.get("CodeKey"));
					temp.put("name", item.get("Value"));
					resultList.add(temp);
				}
				List keysList = new ArrayList();
				String[] keys1 = { "id", "name" };
				keysList.add(keys1);
				ConvertUtil.jsTreeDataDeepList(resultList, resultTreeList,
						keysList, 0);
				ConvertUtil.setResponseByAjax(response, resultTreeList);
			}
			// 按所属系统并指定柜台
		} else if ("5".equals(form.getSelMode())) {
			
			List<Map<String, Object>> list = code.getCodes("1309");
			if(null != list && list.size() > 0){
				List<Map<String, Object>> resultTreeList = new ArrayList<Map<String, Object>>();
				Map<Object, Object> codeMap = new HashMap<Object, Object>();
				// list2map
				for(Map<String, Object> item : list){
					codeMap.put("B" + item.get("CodeKey"), item.get("Value"));
				}
				List<Map<String, Object>> resultList = binOLCM02_BL.getBelongCounterList(map);
				if(null != resultList && resultList.size() > 0){
					for(Map<String, Object> result : resultList){
						result.put("name", codeMap.get(result.get("belongId")));
					}
				}
				List keysList = new ArrayList();
				String[] keys1 = { "belongId", "name" };
				keysList.add(keys1);
				String[] keys2 = { "counterId", "counterName" };
				keysList.add(keys2);
				ConvertUtil.jsTreeDataDeepList(resultList, resultTreeList,
						keysList, 0);
				ConvertUtil.setResponseByAjax(response, resultTreeList);
			}
		}
	}
	/**
	 * 取得渠道树信息
	 * @return
	 * @throws Exception
	 */
	public void popChannelDialog() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		// 所属品牌
		if(form.getBrandInfoId() != null && !"".equals(form.getBrandInfoId())) {
			map.put(CherryConstants.BRANDINFOID, form.getBrandInfoId());
		} else {
			// 不是总部的场合
			if(userInfo.getBIN_BrandInfoID() != CherryConstants.BRAND_INFO_ID_VALUE) {
				// 所属品牌
				map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
			}
		}
		if (!CherryChecker.isNullOrEmpty(form.getMemberClubId())) {
			map.put("memberClubId", form.getMemberClubId());
		}
		map.put("regionId", form.getChannelRegionId());
		map.put("couValidFlag", form.getPopCouValidFlag());
		if(form.getPrivilegeFlg() != null && "1".equals(form.getPrivilegeFlg())) {
			if(form.getUserId() != null && !"".equals(form.getUserId())) {
				map.put("userId", form.getUserId());
			}else{
				// 用户ID
				map.put("userId", userInfo.getBIN_UserID());
			}
			// 业务类型
			map.put("businessType", "0");
			// 操作类型
			map.put("operationType", "1");
			// 是否带权限查询
			map.put("privilegeFlag", form.getPrivilegeFlg());
		}
		// 查询渠道信息List
		List<Map<String, Object>> channelList = binOLCM02_BL.getChannelList(map);
		ConvertUtil.setResponseByAjax(response, channelList);
	}
	/**
	 * 查询定位到的区域ID
	 * @return
	 * @throws Exception
	 */
	public void locationRegion() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		// 所属品牌
		if(form.getBrandInfoId() != null && !"".equals(form.getBrandInfoId())) {
			map.put(CherryConstants.BRANDINFOID, form.getBrandInfoId());
		} else {
			// 不是总部的场合
			if(userInfo.getBIN_BrandInfoID() != CherryConstants.BRAND_INFO_ID_VALUE) {
				// 所属品牌
				map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
			}
		}
		map.put("locationPosition", form.getLocationPosition());
		map.put("couValidFlag", form.getPopCouValidFlag());
		if(form.getPrivilegeFlg() != null && "1".equals(form.getPrivilegeFlg())) {
			if(form.getUserId() != null && !"".equals(form.getUserId())) {
				map.put("userId", form.getUserId());
			}else{
				// 用户ID
				map.put("userId", userInfo.getBIN_UserID());
			}
			// 业务类型
			map.put("businessType", "0");
			// 操作类型
			map.put("operationType", "1");
			// 是否带权限查询
			map.put("privilegeFlag", form.getPrivilegeFlg());
		}
		// 查询定位到的区域ID
		String locationId = binOLCM02_BL.getLocationRegionId(map);
		ConvertUtil.setResponseByAjax(response, locationId);
	}
	/**
	 * 根据输入的字符串模糊查询领用柜台
	 * @throws Exception
	 */
	public void getCounterCode() throws Exception {
		Map<String, Object> map = getCommMap();
		// 活动名称模糊查询
		map.put("counterStr", form.getCounterStr().trim());
		String resultStr = binOLCM02_BL.getCounterCode(map);
		ConvertUtil.setResponseByAjax(response, resultStr);

	}
	/**
	 * 初始化活动信息弹出框
	 * @return
	 * @throws Exception
	 */
	public String initCampaignDialog() throws Exception {
		return SUCCESS;
	}
	/**
	 * 取得活动信息弹出框
	 * @return
	 * @throws Exception
	 */
	public String popCampaignDialog() throws Exception {
		Map<String,Object> map = getCommMap();
		// 画面查询条件
		if(form.getSSearch() != null && !"".equals(form.getSSearch())) {
			map.put("campaignKw", form.getSSearch());
		}
		// 活动类型为促销活动的场合
		if(form.getCampaignMode() != null && "1".equals(form.getCampaignMode()) 
				|| "CXHD".equals(form.getParam2())) {
			// 取得促销活动总数
			int count  = binOLCM02_BL.getPrmCampaignCount(map);
			// form表单设置
			form.setITotalDisplayRecords(count);
			form.setITotalRecords(count);
			if(count > 0) {
				// 取得促销活动List
				form.setCampaignList(binOLCM02_BL.getPrmCampaignList(map));
			}
		} else {// 活动类型为会员活动的场合
			form.setCampaignMode("0");
			// 取得会员活动总数
			int count  = binOLCM02_BL.getMemCampaignCount(map);
			// form表单设置
			form.setITotalDisplayRecords(count);
			form.setITotalRecords(count);
			if(count > 0) {
				// 取得会员活动List
				form.setCampaignList(binOLCM02_BL.getMemCampaignList(map));
			}
		}
		return SUCCESS;
	}
	/**
	 * 初始化销售信息信息弹出框
	 * @return
	 * @throws Exception
	 */
	public String initSaleRecordDialog() throws Exception {
		return SUCCESS;
	}
	/**
	 * 取得销售信息弹出框
	 * @return
	 * @throws Exception
	 */
	public String popSaleRecordDialog() throws Exception {
		Map<String,Object> map = getCommMap();
		// 画面查询条件
		if(form.getSSearch() != null && !"".equals(form.getSSearch())) {
			map.put("billCode", form.getSSearch());
		}
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
		if(form.getUserId() != null && !"".equals(form.getUserId())) {
			map.put("userId", form.getUserId());
		}else{
			// 用户ID
			map.put("userId", userInfo.getBIN_UserID());
		}
		// 业务类型
		map.put("businessType", "0");
		// 操作类型
		map.put("operationType", "1");
		// 是否带权限查询
		map.put("privilegeFlag", form.getPrivilegeFlg());
		// 是否带权限查询
		map.put("privilegeFlag", session.get(CherryConstants.SESSION_PRIVILEGE_FLAG));
		// 查询销售总数
		int count  = binOLCM02_BL.getSaleRecordCount(map);
		// form表单设置
		form.setITotalDisplayRecords(count);
		form.setITotalRecords(count);
		if(count > 0) {
			// 查询销售信息List
			form.setSaleRecordList(binOLCM02_BL.getSaleRecordList(map));
		}
		return SUCCESS;
	}
	/**
	 * 初始化经销商信息弹出框
	 * @return
	 */
	public String initResellerDialog(){
		return SUCCESS;
	}
	/**
	 * 取得经销商信息弹出框
	 * @return
	 * @throws Exception
	 */
	public String popResellerDialog() throws Exception {
		// 取得session信息
		Map<String, Object> map  = this.getCommMap();
		// form参数设置到paramMap中
		ConvertUtil.setForm(form, map);
		int count = binOLCM02_BL.getResellerCount(map);
		if(count > 0){
			form.setResellerList(binOLCM02_BL.getResellerList(map));
		}
		form.setITotalDisplayRecords(count);
		form.setITotalRecords(count);		
		return SUCCESS;
	}
	private Map<String,Object> getCommMap() throws Exception{
		Map<String, Object> map = (Map<String, Object>) Bean2Map.toHashMap(form);
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		// 所属品牌
		if(form.getBrandInfoId() != null && !"".equals(form.getBrandInfoId())) {
			map.put(CherryConstants.BRANDINFOID, form.getBrandInfoId());
			String brandCode = binOLCM05_BL.getBrandCode(ConvertUtil.getInt(form.getBrandInfoId()));
			map.put(CherryConstants.BRAND_CODE, brandCode);
		} else {
			// 不是总部的场合
			if(userInfo.getBIN_BrandInfoID() != CherryConstants.BRAND_INFO_ID_VALUE) {
				// 所属品牌
				map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
				map.put(CherryConstants.BRAND_CODE, userInfo.getBrandCode());
			}
		}
		map.put("param", ConvertUtil.getString(form.getParam()));
		map.put(CherryConstants.SESSION_LANGUAGE, session.get(CherryConstants.SESSION_LANGUAGE));
		// dataTable上传的参数设置到map
		ConvertUtil.setForm(form, map);
		map = CherryUtil.removeEmptyVal(map);
		return map;
	}
	public List<Map<String, Object>> getBrandCodeListSrh() {
		return brandCodeListSrh;
	}

	public void setBrandCodeListSrh(List<Map<String, Object>> brandCodeListSrh) {
		this.brandCodeListSrh = brandCodeListSrh;
	}

	public List<Map<String, Object>> getSortListSrh() {
		return sortListSrh;
	}

	public void setSortListSrh(List<Map<String, Object>> sortListSrh) {
		this.sortListSrh = sortListSrh;
	}

	public String getCategoryResult() {
		return categoryResult;
	}

	public void setCategoryResult(String categoryResult) {
		this.categoryResult = categoryResult;
	}

	public BINOLCM02_Form getForm() {
		return form;
	}

	public void setForm(BINOLCM02_Form form) {
		this.form = form;
	}
}
