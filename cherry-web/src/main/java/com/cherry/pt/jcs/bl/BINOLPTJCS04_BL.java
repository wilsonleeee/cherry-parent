/*
 * @(#)BINOLPTJCS04_BL.java     1.0 2011/03/28
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
package com.cherry.pt.jcs.bl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;


import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;

import com.cherry.bs.common.bl.BINOLBSCOM01_BL;
import com.cherry.cm.activemq.dto.MQInfoDTO;
import com.cherry.cm.activemq.interfaces.BINOLMQCOM01_IF;
import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM15_BL;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.DateUtil;
import com.cherry.mo.common.interfaces.BINOLMOCOM01_IF;
import com.cherry.mq.mes.common.MessageConstants;
import com.cherry.pt.common.ProductConstants;
import com.cherry.pt.jcs.interfaces.BINOLPTJCS04_IF;
import com.cherry.pt.jcs.service.BINOLPTJCS03_Service;
import com.cherry.pt.jcs.service.BINOLPTJCS04_Service;
import com.cherry.pt.jcs.service.BINOLPTJCS05_Service;
import com.cherry.webservice.client.WebserviceClient;
import com.googlecode.jsonplugin.JSONUtil;

/**
 * 产品查询 BL
 * 
 * @author lipc
 * @version 1.0 2011.03.28
 */
public class BINOLPTJCS04_BL implements BINOLPTJCS04_IF {
	@Resource
	private BINOLMOCOM01_IF binOLMOCOM01_BL;

	@Resource
	private BINOLPTJCS04_Service binOLPTJCS04_Service;
	
	@Resource
	private BINOLPTJCS03_Service binOLPTJCS03_Service;
	
	@Resource
	private BINOLPTJCS05_Service binolptjcs05Service;
	
	@Resource(name="binOLBSCOM01_BL")
	private BINOLBSCOM01_BL binOLBSCOM01_BL;
	
	@Resource(name="binOLMQCOM01_BL")
    private BINOLMQCOM01_IF binOLMQCOM01_BL;
	
	/** 各类编号取号共通BL */
	@Resource(name="binOLCM15_BL")
	private BINOLCM15_BL binOLCM15_BL;
	
	/** 系统配置项 共通BL */
	@Resource(name="binOLCM14_BL")
	private BINOLCM14_BL binOLCM14_BL;
	
	private static final Logger logger = LoggerFactory.getLogger(BINOLPTJCS04_BL.class);

	/**
	 * 取得业务日期
	 * 
	 * @param map
	 * @return
	 */
	@Override
	public String getBussinessDate(Map<String, Object> map) {
		return binOLPTJCS04_Service.getBusDate(map);
	}
	
	/**
	 * 取得业务日期
	 * 
	 * @param map
	 * @return
	 */
	public Map<String, Object> getBussinessDateMap(Map<String, Object> map) {
		return binOLPTJCS04_Service.getBussinessDateMap(map);
	}

	/**
	 * 取得产品总数
	 * 
	 * @param map
	 * @return
	 */
	@Override
	public int getProCount(Map<String, Object> map) {
		// 产品分类节点位置
		String path = ConvertUtil.getString(map.get(CherryConstants.PATH));
		Map<String, Object> paramMap = new HashMap<String, Object>(map);
		
		if (!CherryChecker.isNullOrEmpty(path)) {
			List<Map<String, Object>> cateList = new ArrayList<Map<String,Object>>();
			String[] level = path.split(CherryConstants.SLASH);
			// 品牌ID
			paramMap.put(CherryConstants.BRANDINFOID, level[0]);
			// 取得树形显示顺序List
			List<Integer> seqList = binOLPTJCS04_Service.getCatPropList(paramMap);
			for(int i = 1; i < level.length; i++){
				Map<String, Object> cateMap = new HashMap<String, Object>();
				// 分类类型ID
				cateMap.put(ProductConstants.PROPID, seqList.get(i - 1));
				// 分类类型值ID
				cateMap.put(ProductConstants.PROPVALID, level[i]);
				cateList.add(cateMap);
			}
			paramMap.put("cateList", cateList);
		}
		return binOLPTJCS04_Service.getProCount(paramMap);
	}

	/**
	 * 取得产品信息List
	 * 
	 * @param map
	 * @return
	 */
	@Override
	public List<Map<String, Object>> getProList(Map<String, Object> map) {
		// 产品分类节点位置
		String path = ConvertUtil.getString(map.get(CherryConstants.PATH));
		Map<String, Object> paramMap = new HashMap<String, Object>(map);
		
		if (!CherryChecker.isNullOrEmpty(path)) {
			List<Map<String, Object>> cateList = new ArrayList<Map<String,Object>>();
			String[] level = path.split(CherryConstants.SLASH);
			// 品牌ID
			paramMap.put(CherryConstants.BRANDINFOID, level[0]);
			// 取得树形显示顺序List
			List<Integer> seqList = binOLPTJCS04_Service.getCatPropList(paramMap);
			for(int i = 1; i < level.length; i++){
				Map<String, Object> cateMap = new HashMap<String, Object>();
				// 分类类型ID
				cateMap.put(ProductConstants.PROPID, seqList.get(i - 1));
				// 分类类型值ID
				cateMap.put(ProductConstants.PROPVALID, level[i]);
				cateList.add(cateMap);
			}
			paramMap.put("cateList", cateList);
		}
		return binOLPTJCS04_Service.getProList(paramMap);
	}

	/**
	 * 取得产品分类List
	 * 
	 * @param map
	 *            检索条件
	 * @return 树结构字符串
	 */
	@Override
	public String getCategoryList(Map<String, Object> map) throws Exception {
		// 产品分类节点位置
		String path = ConvertUtil.getString(map.get(CherryConstants.PATH));
		if (CherryChecker.isNullOrEmpty(path)) {
			// 取得品牌ID
			String brandInfoId = ConvertUtil.getString(map
					.get(CherryConstants.BRANDINFOID));
			if (CherryConstants.BLANK.equals(brandInfoId)) {
				// 取得品牌
				List<Map<String, Object>> list = binOLPTJCS04_Service.getBrandList(map);
				// JSON字符串作成
				return getBrandJSON(list);
			} else {
				path = brandInfoId + CherryConstants.SLASH;
				map.put(CherryConstants.PATH, path);
				// 分类JSON字符串作成
				return getCateJSON(map);
			}
		} else {
			// 分类JSON字符串作成
			return getCateJSON(map);
		}
	}
	
	private Map<String, Object> getParamMap(Map<String, Object> map,String path, List<Integer> seqList){
		Map<String, Object> paramMap = new HashMap<String, Object>();
		String[] level = path.split(CherryConstants.SLASH);
		if(level.length >= seqList.size()+1){
			return null;
		}
		// 语言
		paramMap.put(CherryConstants.SESSION_LANGUAGE,
				map.get(CherryConstants.SESSION_LANGUAGE));
		// 产品分类节点位置
		paramMap.put(CherryConstants.PATH, path);
		// 产品分类有效区分
		paramMap.put(CherryConstants.VALID_FLAG,
				CherryConstants.VALIDFLAG_ENABLE);
		// 只查询有效产品对应的产品分类数据
		paramMap.put("validProduct", map.get("validProduct"));
		// 分类类型ID
		paramMap.put(ProductConstants.PROPID, seqList.get(level.length - 1));
		if (level.length > 1) {
			List<Map<String, Object>> cateList = new ArrayList<Map<String, Object>>();
			for (int i = 1; i < level.length; i++) {
				Map<String, Object> cateMap = new HashMap<String, Object>();
				// 分类类型ID
				cateMap.put(ProductConstants.PROPID, seqList.get(i - 1));
				// 分类类型值ID
				cateMap.put(ProductConstants.PROPVALID, level[i]);
				cateList.add(cateMap);
			}
			paramMap.put("cateList", cateList);
		}
		return paramMap;
	}
	/**
	 * 取得下挂产品的分类List
	 * @param paramMap
	 * @param level
	 * @param seqList
	 * @return
	 */
//	private List<Map<String, Object>> getCateValList(Map<String, Object> map,
//			String path, List<Integer> seqList) {
//		Map<String, Object> paramMap = new HashMap<String, Object>();
//		String[] level = path.split(CherryConstants.SLASH);
//		if(level.length >= seqList.size()+1){
//			return null;
//		}
//		// 语言
//		paramMap.put(CherryConstants.SESSION_LANGUAGE,
//				map.get(CherryConstants.SESSION_LANGUAGE));
//		// 产品分类节点位置
//		paramMap.put(CherryConstants.PATH, path);
//		// 产品分类有效区分
//		paramMap.put(CherryConstants.VALID_FLAG,
//				CherryConstants.VALIDFLAG_ENABLE);
//		// 分类类型ID
//		paramMap.put(ProductConstants.PROPID, seqList.get(level.length - 1));
//		if (level.length > 1) {
//			List<Map<String, Object>> cateList = new ArrayList<Map<String, Object>>();
//			for (int i = 1; i < level.length; i++) {
//				Map<String, Object> cateMap = new HashMap<String, Object>();
//				// 分类类型ID
//				cateMap.put(ProductConstants.PROPID, seqList.get(i - 1));
//				// 分类类型值ID
//				cateMap.put(ProductConstants.PROPVALID, level[i]);
//				cateList.add(cateMap);
//			}
//			paramMap.put("cateList", cateList);
//		}
//		// 取得分类List
//		return binOLPTJCS04_Service.getCateValList(paramMap);
//	}
	
//	private boolean hasChild(Map<String, Object> map,String path, List<Integer> seqList) {
//		List<Map<String, Object>> list = getCateValList(map,path,seqList);
//		if(null == list || list.size() == 0){
//			return false;
//		}
//		return true;
//	}

	/**
	 * 分类JSON字符串作成
	 * 
	 * @param list
	 * @param orgInfoId
	 * @return
	 */
	private String getCateJSON(Map<String, Object> map) throws Exception {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		List<Map<String, Object>> list = null;
		// 产品分类节点位置
		String path = ConvertUtil.getString(map.get(CherryConstants.PATH));
		String[] level = path.split(CherryConstants.SLASH);
		if (null == level || level.length == 0) {
			return "[]";
		}
		// 品牌ID
		paramMap.put(CherryConstants.BRANDINFOID, level[0]);
		// 取得树形显示顺序List
		List<Integer> seqList = binOLPTJCS04_Service.getCatPropList(paramMap);
		if (null == seqList || seqList.size() == 0) {
			return "[]";
		}
		// 取得分类List
		paramMap = getParamMap(map, path, seqList);
		list = binOLPTJCS04_Service.getCateValList(paramMap);
		// 把取得的产品分类List转换成树结构的字符串
		StringBuffer jsonTree = new StringBuffer();
		if (list != null && !list.isEmpty()) {
			jsonTree.append("[");
			for (int i = 0; i < list.size(); i++) {
				Map<String, Object> resultMap = list.get(i);
				// 节点位置
				path = ConvertUtil.getString(resultMap
						.get(CherryConstants.PATH));
				// 子节点数
				paramMap = getParamMap(map, path, seqList);
				int childCount = 0;
				if(null != paramMap){
					childCount = binOLPTJCS04_Service.getChildCount(paramMap);
				}
				// 节点名称
				String name = ConvertUtil.getString(resultMap.get("name"));
				name = JSONUtil.serialize(name);
				// 树结构作成
				jsonTree.append("{\"data\":{\"title\":" + name + ","
						+ "\"attr\":{\"id\":\"" + path + "\"}},"
						+ "\"attr\":{\"id\":\"" + path + "\"},\"state\":\"");
				// 有子节点的场合
				if (childCount > 0) {
					jsonTree.append("closed");
				}
				jsonTree.append("\"}");
				if (i + 1 != list.size()) {
					jsonTree.append(",");
				}
			}
			jsonTree.append("]");
		}
		return jsonTree.toString();
	}

	/**
	 * 品牌JSON字符串作成
	 * 
	 * @param list
	 * @param orgInfoId
	 * @return
	 */
	private String getBrandJSON(List<Map<String, Object>> list)
			throws Exception {
		StringBuffer jsonTree = new StringBuffer();
		// 把取得的产品分类List转换成树结构的字符串
		if (list != null && !list.isEmpty()) {
			jsonTree.append("[");
			for (int i = 0; i < list.size(); i++) {
				Map<String, Object> resultMap = list.get(i);

				// 节点位置
				String path = ConvertUtil.getString(resultMap
						.get(CherryConstants.PATH));
				// 节点名称
				String name = ConvertUtil.getString(resultMap.get("name"));
				name = JSONUtil.serialize(name);
				// 树结构作成
				jsonTree.append("{\"data\":{\"title\":" + name + ","
						+ "\"attr\":{\"id\":\"" + path + "\"}},"
						+ "\"attr\":{\"id\":\"" + path
						+ "\"},\"state\":\"closed\"}");
				if (i + 1 != list.size()) {
					jsonTree.append(",");
				}
			}
			jsonTree.append("]");
		}
		return jsonTree.toString();
	}

	/**
	 * 导出产品信息Excel
	 * 
	 * @param map
	 * @return 返回导出产品信息List
	 * @throws Exception
	 */
	@Override
	public byte[] exportExcel(Map<String, Object> map) throws Exception {
		List<Map<String, Object>> dataList = binOLPTJCS04_Service
				.getProductInfoListExcel(map);

		String[][] array = {
				// 1
				{ "brandName", "pro.brandName", "20", "", "" },
				// 2
				{ "barCode", "pro.barCode", "20", "", "" },
				// 3
				{ "unitCode", "pro.unitCode", "20", "", "" },
				// 4
				{ "nameTotal", "pro.nameTotal", "25", "", "" },
				{ "validFlag", "pro.validFlag", "15", "", "1137" },
				// 分类
				// 5
				{ "", "", "20", "", "" },
				// 6
				{ "", "", "20", "", "" },
				// 7
				{ "", "", "20", "", "" },
				// 8
				{ "", "", "20", "", "" },
				// 9
				{ "", "", "20", "", "" },
				// 10
				{ "", "", "20", "", "" },
				// 11
				{ "", "", "20", "", "" },
				// 12
				{ "", "", "20", "", "" },
				// 13
				{ "", "", "20", "", "" },
				// 14
				{ "", "", "20", "", "" },
				// 15
				{ "", "", "20", "", "" },
				// 16
				{ "", "", "20", "", "" },
				// 17
				{ "", "", "20", "", "" },
				// 18
				{ "", "", "20", "", "" },
				// 19
				{ "", "", "20", "", "" },
				// 20
				{ "", "", "20", "", "" },
				// 21
				{ "", "", "20", "", "" },
				// 22
				{ "", "", "20", "", "" },
				// 23
				{ "", "", "20", "", "" },
				// 24
				{ "", "", "20", "", "" },
				// 25
				{ "", "", "20", "", "" },
				// 26
				{ "", "", "20", "", "" },
				// 27
				{ "", "", "20", "", "" },
				// 28
				{ "", "", "20", "", "" },
				// 29
				{ "", "", "20", "", "" },
				// 30
				{ "", "", "20", "", "" },
				// 31
				{ "", "", "20", "", "" },
				// 32
				{ "", "", "20", "", "" },
				// 33
				{ "", "", "20", "", "" },
				// 34
				{ "", "", "20", "", "" },
				// 35
				{ "", "", "20", "", "" },
				// 36
				{ "", "", "20", "", "" },
				// 37
				{ "", "", "20", "", "" },
				// 38
				{ "", "", "20", "", "" },
				// 39
				{ "", "", "20", "", "" },
				// 40
				{ "", "", "20", "", "" },
				// 41
				{ "", "", "20", "", "" },
				// 42
				{ "", "", "20", "", "" },
				// 43
				{ "", "", "20", "", "" },
				// 44
				{ "", "", "20", "", "" },
				// 45
				{ "salePrice", "pro.salePrice", "", "right", "" },
				// 46
				{ "standardCost", "pro.standardCost", "", "right", "" },//结算（终端成本）价格
				// 47
				{ "orderPrice", "pro.orderPrice", "", "right", "" },//成本（终端采购）价格
				// 48
				{ "memPrice", "pro.memPrice", "", "right", "" },
				// 49
				{ "status", "pro.status", "15", "", "1016" },
				// 50
				{ "mode", "pro.mode", "", "", "1136" },
				// 51
				{ "nameShort", "pro.nameShort", "", "", "" },
				// 52
				{ "nameAlias", "pro.nameAlias", "", "", "" },
				// 53
				{ "nameForeign", "pro.nameForeign", "20", "", "" },
				// 54
				{ "nameShortForeign", "pro.nameShortForeign", "20", "", "" },
				// 55
				{ "operationStyle", "pro.operationStyle", "", "", "1003" },
				// 56
				{ "styleCode", "pro.styleCode", "", "", "1002" },
				// 57
				{ "sellStartDate", "pro.sellStartDate", "20", "", "" },
				// 58
				{ "sellEndDate", "pro.sellEndDate", "20", "", "" },
				// 59
				{ "recommendedNumDay", "pro.recommendedNumDay", "15", "", "" },
				// 60
				{ "starProduct", "pro.starProduct", "15", "", "1148" },
				// 61
				{ "shelfLife", "pro.shelfLife", "15", "", "" },
				// 62
				{ "isBOMCompatible", "pro.isBOMCompatible", "15", "", "1021" },
				// 63
				{ "isReplenish", "pro.isReplenish", "", "", "1004" },
				// 64
				{ "lackFlag", "pro.lackFlag", "", "", "1056" },
				// 65
				{ "discontinueCounter", "pro.discontinueCounter", "15", "",
						"1023" },
				// 66
				{ "discontinueReseller", "pro.discontinueReseller", "15", "",
						"1005" },
				// 67
				{ "moduleCode", "pro.moduleCode", "", "", "1190" },
				// 68
				{ "saleUnit", "pro.saleUnit", "", "", "" } ,
				// 69
				{ "originalBrand", "pro.originalBrand", "", "", "1299" },
				// 70
				{ "itemType", "pro.itemType", "", "", "1245" },
				// 71
				{ "isExchanged", "pro.isExchanged", "", "", "1220" } 
				};

		// 取大中小分类名称
		List<Map<String, Object>> prtCatPropertyList = binOLPTJCS04_Service
				.getPrtCatPropertyList(map);
		// 动态设置产品类别基本属性定义
		int startIndex = 5;
		String language = ConvertUtil.getString(map.get("language"));
		for (int i = 0; i < prtCatPropertyList.size(); i++) {
			String cateCodeSuffix = binOLMOCOM01_BL.getResourceValue(
					"BINOLPTJCS04", language, "excel.CateCode");
			String cateNameSuffix = binOLMOCOM01_BL.getResourceValue(
					"BINOLPTJCS04", language, "excel.CateName");
			array[startIndex + 2 * i][0] = "viewSeq-code"
					+ ConvertUtil.getString(prtCatPropertyList.get(i).get(
							"viewSeq"));
			array[startIndex + 2 * i][1] = ConvertUtil
					.getString(prtCatPropertyList.get(i).get("propertyName")
							+ cateCodeSuffix);
			array[startIndex + 2 * i + 1][0] = "viewSeq-name"
					+ ConvertUtil.getString(prtCatPropertyList.get(i).get(
							"viewSeq"));
			array[startIndex + 2 * i + 1][1] = ConvertUtil
					.getString(prtCatPropertyList.get(i).get("propertyName")
							+ cateNameSuffix);
		}

		// 清空为空字符串的数组
		List<String[]> arrList = new ArrayList<String[]>();
		for (int i = 0; i < array.length; i++) {
			if (!"".equals(array[i][0])) {
				arrList.add(array[i]);
			}
		}
		String[][] newArray = new String[arrList.size()][5];
		for (int i = 0; i < arrList.size(); i++) {
			newArray[i][0] = arrList.get(i)[0];
			newArray[i][1] = arrList.get(i)[1];
			newArray[i][2] = arrList.get(i)[2];
			newArray[i][3] = arrList.get(i)[3];
			newArray[i][4] = arrList.get(i)[4];
		}

		// String[][] newArray = (String[][]) arrList.toArray();

		// 展开List中的List
		for (int i = 0; i < dataList.size(); i++) {
			List<Map<String, Object>> cateList = (List) dataList.get(i).get(
					"list");
			for (int j = 0; j < cateList.size(); j++) {
				String viewSeq = ConvertUtil.getString(cateList.get(j).get(
						"viewSeq"));
				String cateName = ConvertUtil.getString(cateList.get(j).get(
						"propValCN"));
				/*String propValue = ConvertUtil.getString(cateList.get(j).get(
						"propValue"));*/
				String propValue = ConvertUtil.getString(cateList.get(j).get(
						"propValueCherry"));
				dataList.get(i).put("viewSeq-code" + viewSeq, propValue);
				dataList.get(i).put("viewSeq-name" + viewSeq, cateName);
			}
		}

		BINOLMOCOM01_IF.ExcelParam ep = new BINOLMOCOM01_IF.ExcelParam();
		ep.setMap(map);
		ep.setArray(newArray);
		ep.setBaseName("BINOLPTJCS04");
		ep.setSheetLabel("sheetName");
		ep.setDataList(dataList);
		return binOLMOCOM01_BL.getExportExcel(ep);
	}
	
    /**
     * 取得产品分类的上级分类
     * @param map
     * @return
     */
    @SuppressWarnings("unchecked")
	public String getLocateCatHigher(Map<String, Object> map){
    	
    	// 定义产品绑定的分类List的常量值
    	final String BING_PROCAT = "bingProCat";
    	
    	// 拼接ID
		List<String> resultList = new ArrayList<String>();
    	
		// 模糊查询产品分类定位的分类预设值ID
    	String prtCatValId = binOLPTJCS04_Service.getLocationPrtCatValId(map);
    	
    	if(null != prtCatValId){
    		map.put("BIN_PrtCatPropValueID", prtCatValId);
    	} else {
    		return "[{}]";
    	}
    	
    	// 取得产品分类顺序
    	List<Map<String, Object>> catSortList = binOLPTJCS04_Service.getCatSortByBing(map);
    	// 取得已绑定分类的产品
    	map.put("column", "DISTINCT T2.BIN_ProductID");
    	List<Map<String, Object>> bingProductList = binOLPTJCS04_Service.getLocateCatHigher(map);
    	// 取得产品分类的上级分类
    	map.put("column", "T1.*,T2.BIN_ProductID");
    	List<Map<String, Object>> catHigherList = binOLPTJCS04_Service.getLocateCatHigher(map);
    	
    	// 每个产品绑定的分类
    	List<Map<String, Object>> proCatGroupList = new ArrayList<Map<String, Object>>();
    	for(Map<String, Object> bingProductMap : bingProductList) {
    		Integer productID = (Integer)bingProductMap.get("BIN_ProductID");
    		
    		Map<String, Object> proCatGroupMap = new HashMap<String, Object>();
    		proCatGroupMap.put("productID", productID);
    		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

    		for(Map<String, Object> catHigherMap : catHigherList){
    			if(catHigherMap.get("BIN_ProductID").equals(productID)){
    				list.add(catHigherMap);
    			}
    		}
    		proCatGroupMap.put(BING_PROCAT, list);
    		proCatGroupList.add(proCatGroupMap);
    	}
    	
    	// 取得包含定位分类的proCatGroupList分类
    	List<Map<String, Object>> selProCatGroupList = new ArrayList<Map<String, Object>>();
    	for(Map<String, Object> proCatGroupMap :proCatGroupList){
    		List<Map<String, Object>> list = (List<Map<String, Object>>)proCatGroupMap.get(BING_PROCAT);
    		
    		for(Map<String, Object> itemMap : list){
    			Integer bin_PrtCatPropValueID = (Integer)itemMap.get("BIN_PrtCatPropValueID");
    			if(prtCatValId.equals(bin_PrtCatPropValueID.toString())){
    				selProCatGroupList.add(proCatGroupMap);
    			}
    		}
    	}
    	
    	// 将定位到的产品分类根据画面树形结构定位要求的结构进行拼接
		for(Map<String, Object> selProCatGroupMap : selProCatGroupList){
			StringBuffer resultSbf = new StringBuffer();
//			resultSbf.append("#").append(map.get(CherryConstants.BRANDINFOID).toString());
			resultSbf.append("#");
    		List<Map<String, Object>> list = (List<Map<String, Object>>)selProCatGroupMap.get(BING_PROCAT);
    		
    		int flag = 0;
    		for(Map<String, Object> catSortMap : catSortList){
    			Integer bin_PrtCatPropertyID = (Integer)catSortMap.get("BIN_PrtCatPropertyID");
    			
    			for(Map<String, Object> itemMap : list){
    				Integer itemBin_PrtCatPropertyID = (Integer)itemMap.get("BIN_PrtCatPropertyID");
    				if(bin_PrtCatPropertyID.equals(itemBin_PrtCatPropertyID)){
    					resultSbf.append("_").append(itemMap.get("BIN_PrtCatPropValueID"));
    					flag++;
    				}
    			}
    		}
    		if(flag != 0){
    			String [] resultArr = resultSbf.toString().split("_");
    			StringBuffer realResultStr = new StringBuffer();
    			realResultStr.append("#").append(map.get(CherryConstants.BRANDINFOID).toString()).append("\\\\").append(CherryConstants.SLASH);
    			for(int i = 1; i < resultArr.length-1 ; i++){
    				realResultStr.append(resultArr[i]).append("\\\\").append(CherryConstants.SLASH);
    				resultList.add(realResultStr.toString());
    			}
    		}
		}
		removeDuplicateWithCat(resultList);
		
		// 拼接成JSON格式
		StringBuffer locationJson = new StringBuffer();
		locationJson.append("[");
		if(null != resultList && resultList.size() != 0){
			for(int i = 0 ; i < resultList.size(); i++){
				locationJson.append("\"").append(resultList.get(i)).append("\"");
				if(i != resultList.size() - 1 ){
					locationJson.append(",");
				}
			}
		}
		locationJson.append("]");
		
		
    	return locationJson.toString();
    }
    
    
    /**
     * 去除list里面重复的数据
     * @param list
     */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void removeDuplicateWithCat(List list) {
		Set set = new HashSet();
		List newList = new ArrayList();
		for (Iterator iter = list.iterator(); iter.hasNext();) {
			Object element = iter.next();
			if (set.add(element)) {
				newList.add(element);
			}
		}
		list.clear();
		list.addAll(newList);
	}

	/**
	 * 停用/启用
	 */
	@CacheEvict(value="CherryProductCache",allEntries=true,beforeInvocation=false)
	public void tran_disOrEnable(Map<String, Object> map) {
		
		// 取得当前产品表版本号
		Map<String, Object> seqMap = new HashMap<String, Object>();
		seqMap.putAll(map);
		seqMap.put("type", "E");
		String tVersion = binOLCM15_BL.getCurrentSequenceId(seqMap);
		map.put("tVersion", tVersion);
		
		// 取得当前部门(柜台)产品表版本号
		seqMap.put("type", "F");
		String pdTVersion = binOLCM15_BL.getCurrentSequenceId(seqMap);
		map.put("pdTVersion", pdTVersion);
		// 产品变动后更新产品方案明细表的version字段
		binOLPTJCS04_Service.updPrtSolutionDetail(map);
		
		// 停用
		if(CherryConstants.VALIDFLAG_DISABLE.equals(map.get(CherryConstants.VALID_FLAG).toString())){
			// 判断停用的条码是否为当前产品的唯一有效条码，若是则停用产品表
			Map<String,Object> paramMap = new HashMap<String, Object>();
			paramMap.put("productId", map.get("productId"));
			paramMap.put(CherryConstants.VALID_FLAG, CherryConstants.VALIDFLAG_ENABLE);
			paramMap.put("prtVendorId", map.get("prtVendorId"));
			List<Map<String,Object>> prtVendorList = getPrtVendorDetailList(paramMap);
			if(prtVendorList.isEmpty()){
				// 更新产品信息表
				binOLPTJCS04_Service.updProduct(map);
			}
			
			// 更新产品厂商表
			binOLPTJCS04_Service.updPrtVendor(map);
			
			// 更新产品厂商关系表
			binOLPTJCS04_Service.updateClosingTime(map);
		} 
		// 启用
		else {
			// 更新产品信息表
			binOLPTJCS04_Service.updProduct(map);
			
			boolean isU2M = (Boolean)map.get("isU2M");
			// 一品一码
			if(!isU2M){
				
				// 产品条码list
				List<Map<String, Object>> barList = binolptjcs05Service.getBarCodeList(map);
				
				boolean noExistsFlag = true;
				if (null != barList && !barList.isEmpty()) {
					// 当前需要启用的条码
					String barCode = ConvertUtil.getString(map.get(CherryConstants.BARCODE));
					
					for(Map<String, Object> itemMap : barList){
						String code = ConvertUtil.getString(itemMap.get(CherryConstants.BARCODE));
						if(code.equalsIgnoreCase(barCode)){
							
							// 查看当前产品是否已存在有效的厂商ID且有效的厂商ID的条码不是当前要启用的,若存在则先停用
							Map<String,Object> paramMap = new HashMap<String, Object>();
							paramMap.put("productId", map.get("productId"));
							paramMap.put("validFlag",CherryConstants.VALIDFLAG_ENABLE);
							Map<String,Object> prtVendorMap = binOLPTJCS04_Service.getPrtDetailInfo(paramMap);
							
							if(null != prtVendorMap){
								// 停用当前有效厂商ID
								Map<String,Object> disMap = new HashMap<String, Object>();
								disMap.put("prtVendorId", prtVendorMap.get("prtVendorId"));
								disMap.put(CherryConstants.VALID_FLAG, CherryConstants.VALIDFLAG_DISABLE);
								binOLPTJCS04_Service.updPrtVendor(disMap);
								// 更新产品厂商关系表
								binOLPTJCS04_Service.updateClosingTime(disMap);
							}
							
							// 重新启用在厂商表中已存在的厂商ID
							Map<String,Object> enableMap = new HashMap<String, Object>();
							enableMap.put("prtVendorId", itemMap.get("proVendorId"));
							enableMap.put(CherryConstants.VALID_FLAG, CherryConstants.VALIDFLAG_ENABLE);
							enableMap.put("barCode", map.get("barCode"));
							binOLPTJCS04_Service.updPrtVendor(enableMap);
							
							noExistsFlag = false;
							break;
						}
					}
					
					if(noExistsFlag){
						// 当前启用的条码在产品对应的厂商表中不存在，那么取产品厂商列表的第一条
						Map<String,Object> enableMap = new HashMap<String, Object>();
						enableMap.putAll(map);
						// barList如果其中存在有效，则排在第一条（兼容一品多码）
						enableMap.put("prtVendorId",barList.get(0).get("proVendorId"));
						binOLPTJCS04_Service.updPrtVendor(enableMap);
						
						if(barList.get(0).get("validFlag").toString().equals("1")){
							binOLPTJCS04_Service.updPrtBarCode(enableMap);
						}
						
						// 启用时，若编码条码关系表中最后一条为老编码条码为非当前编码条码(closingtime is null unitcode is null barcode is null)那么启用时相当于修改编码条码。
						List<Map<String, Object>> notcloseUB = binOLPTJCS04_Service.getNotCloseUB(enableMap);
						if(!CherryUtil.isBlankList(notcloseUB)){
							binOLPTJCS04_Service.updPrtBarCode(enableMap);
						}
					}
				}
				
			} 
			// 一品多码
			else {
				
				// ***** 查询将来启用产品的条码在厂商表中是否存在，若存在则更新成有效，
				// ***** 若不存在，则需要看一下当前启用条码对应的厂商ID对应的数据是否有效
				// ********** 若有效，则新增
				// ********** 若无效，则更新
				Map<String,Object> paramMap = new HashMap<String, Object>();
				paramMap.put("productId", map.get("productId"));
				paramMap.put("barCode", map.get("barCode"));
				Map<String,Object> prtVendorMap = binOLPTJCS04_Service.getPrtVendorInfo(paramMap);
				if(null != prtVendorMap){
					// 若存在则将存在这个条码的厂商ID更新成有效
					map.put("prtVendorId", prtVendorMap.get("prtVendorId"));
					binOLPTJCS04_Service.updPrtVendor(map);
					
				} else {
					// 若不存在，则需要看一下当前启用条码对应的厂商ID对应的数据是否有效
					paramMap.clear();
					paramMap.put("prtVendorId", map.get("prtVendorId"));
					prtVendorMap = binOLPTJCS04_Service.getPrtVendorInfo(paramMap);
					
					if(prtVendorMap.get("validFlag").toString().equals(CherryConstants.VALIDFLAG_DISABLE)){
						//  若无效，则更新
						binOLPTJCS04_Service.updPrtVendor(map);
					} else {
						// 若有效，则新增
						int prtVendorId = binOLPTJCS03_Service.insertProductVendor(map);
					}
					
				}
				
			}
			
		}
	}
	
    /**
     * 取得同产品下非当前厂商ID的有效厂商记录集合
     * @param map
     * @return
     */
    public List<Map<String,Object>> getPrtVendorDetailList(Map<String, Object> map){
    	return binOLPTJCS04_Service.getPrtVendorDetailList(map);
    }
    /**
     * 取得同产品下有效或无效的厂商信息
     * @param map
     * @return
     */
    public Map<String,Object> getPrtDetailInfo(Map<String, Object> map){
    	return binOLPTJCS04_Service.getPrtDetailInfo(map);
    }
    
	/**
	 * 查询产品是否已存在有效的相同条码
	 * 
	 * @param map
	 * @return
	 */
	public Map<String, Object> getPrtBarCodeVF(Map<String, Object> map){
		return binOLPTJCS04_Service.getPrtBarCodeVF(map);
	}
	
	/**
	 * 批量停用产品
	 * @param map
	 * @throws Exception
	 */
	public void tran_delProductInfo(Map<String, Object> map) throws Exception{
		// 产品
		List<String> productInfoIdsList = (List<String>) map.get("productInfoIds");
		String [] prtIDsList = new String[productInfoIdsList.size()];
		prtIDsList = (String[]) productInfoIdsList.toArray(prtIDsList);
		
		for(String prtIDs : prtIDsList){
			String []prtIDsArr = prtIDs.split("_");
			String productId = prtIDsArr[0];
			String prtVendorId = prtIDsArr[1];	
			String prtUpdateTime = prtIDsArr[2];	
			map.put("prtUpdateTime", prtUpdateTime);
			String prtModifyCount = prtIDsArr[3];	
			map.put("prtModifyCount", prtModifyCount);
			
			// 判断停用的条码是否为当前产品的唯一有效条码，若是则停用产品表
			Map<String,Object> paramMap = new HashMap<String, Object>();
			paramMap.put(CherryConstants.VALID_FLAG, CherryConstants.VALIDFLAG_ENABLE);
			paramMap.put("productId", productId);
			map.put("productId", productId);
			paramMap.put("prtVendorId", prtVendorId);
			map.put("prtVendorId", prtVendorId);
			List<Map<String,Object>> prtVendorList = getPrtVendorDetailList(paramMap);
			
			// 取得当前产品表版本号
			Map<String, Object> seqMap = new HashMap<String, Object>();
			seqMap.putAll(map);
			seqMap.put("type", "E");
			String tVersion = binOLCM15_BL.getCurrentSequenceId(seqMap);
			map.put("tVersion", tVersion);
			
			// 取得当前部门(柜台)产品表版本号
			seqMap.put("type", "F");
			String pdTVersion = binOLCM15_BL.getCurrentSequenceId(seqMap);
			map.put("pdTVersion", pdTVersion);
			
			// 产品变动后更新产品方案明细表的version字段
			binOLPTJCS04_Service.updPrtSolutionDetail(map);
			// 更新产品信息表版本号
			binOLPTJCS04_Service.updProductVersion(map);
			
			if(prtVendorList.isEmpty()){
				// 更新产品信息表
				binOLPTJCS04_Service.updProduct(map);
			}
			
			// 更新产品厂商表
			binOLPTJCS04_Service.updPrtVendor(map);
			
			// 更新产品厂商关系表
			binOLPTJCS04_Service.updateClosingTime(map);
		}
		
	}
	
	/* ############################################################################################################################################# */
	/**
	 *通过WebService进行产品实时下发
	 * @param map
	 * @throws Exception
	 */
	public Map<String,Object> tran_issuedPrtByWS(Map<String, Object> map) throws Exception{
		Map<String,Object> resultMap;
		String errCode = "";
		String errMsg = "OK";
		
		Map<String, Object> msgMap = new HashMap<String, Object>();
		try {
			logger.info("*********产品webService实时下发处理开始**********");
			
			// 品牌是否支持产品下发
			boolean isPrtIss = binOLCM14_BL.isConfigOpen("1295", String.valueOf(map.get("organizationInfoId")), String.valueOf(map.get("brandInfoId")));
			if(!isPrtIss){
				msgMap.put("result", "2"); // 品牌的系统配置项不支持产品下发功能，请联系管理员！
	            logger.error("********* BINOLPTJCS04品牌的系统配置项不支持产品下发功能，请联系管理员！*********");
				return msgMap;
			}
			Map<String,Object> paramMap=new HashMap<String, Object>();
			
			//WebService传送指定调用类
			paramMap.put("TradeType", "ProductLogic");
			paramMap.put("brandInfoId", map.get("brandInfoId"));
			paramMap.put("organizationInfoId", map.get("organizationInfoId"));
			paramMap.put("brandCode", map.get("brandCode"));
			paramMap.put("programFlag", "0");
			paramMap.put("EmployeeId", map.get("EmployeeId"));
			// 柜台产品下发模式
			paramMap.put("cntIssuedPrtMode", map.get("cntIssuedPrtMode"));
			
			//通过调用WebService进行产品实时下发
			resultMap = WebserviceClient.accessBatchWebService(paramMap);
			
			if (null != resultMap && !resultMap.isEmpty()) {
				errCode=ConvertUtil.getString(resultMap.get("ERRORCODE"));
				errMsg=ConvertUtil.getString(resultMap.get("ERRORMSG"));
				if(!"0".equals(errCode)){
					msgMap.put("result","1");
                    msgMap.put("ERRORCODE", errCode);
                    msgMap.put("ERRORMSG", errMsg);
                    logger.error("*********产品webService实时下发处理异常ERRORCODE【"+errCode+"】*********");
                    logger.error("*********产品webService实时下发处理异常ERRORMSG【"+errMsg+"】*********");
				}else{
					msgMap.put("result", "0");
                    msgMap.put("ERRORCODE", errCode);
                    msgMap.put("ERRORMSG", errMsg);
				}
			}else{
            	msgMap.put("result", "1");
                errCode = "-1";
                errMsg = "webService访问返回结果信息为空";
                msgMap.put("ERRORCODE", errCode);
                msgMap.put("ERRORMSG", errMsg);
                logger.error("********* 产品webService实时下发处理异常ERRORCODE【"+errCode+"】*********");
                logger.error("********* 产品webService实时下发处理异常ERRORMSG【"+errMsg+"】*********");
			}
			
		    logger.info("*********产品webService实时下发处理结束【"+errCode+"】**********");
		} catch (Exception e) {
			msgMap.put("result", "1");
			logger.error(e.getMessage(),e);
		}
		return msgMap;
	}
	/**
	 * 产品实时下发
	 * @param map
	 * @throws Exception
	 */
	@SuppressWarnings("finally")
	public Map<String,Object> tran_issuedPrt(Map<String, Object> map) throws Exception{
		
		Map<String, Object> result = new HashMap<String, Object>();
		
		// 品牌是否支持产品下发
		boolean isPrtIss = binOLCM14_BL.isConfigOpen("1295", String.valueOf(map.get("organizationInfoId")), String.valueOf(map.get("brandInfoId")));
		if(!isPrtIss){
			result.put("result", "2"); // 品牌的系统配置项不支持产品下发功能，请联系管理员！
            logger.error("********* BINOLPTJCS04品牌的系统配置项不支持产品下发功能，请联系管理员！*********");
			return result;
		}
		
		// Step1: 数据插入Brand接口数据库
		
		// 取得当前产品表版本号
		Map<String, Object> seqMap = new HashMap<String, Object>();
		seqMap.putAll(map);
		seqMap.put("type", "E");
		String tVersion = binOLCM15_BL.getCurrentSequenceId(seqMap);
		map.put("tVersion", tVersion);
		// 取得产品表的行版本号version大于tVersion的产品list(新增/修改/停用/启用等)
		List<Map<String, Object>> productsList = binOLPTJCS04_Service.getProductList(map);
		// 根据指定Version取产品功能开启时间的产品信息List
		List<Map<String, Object>> prtFunDetailByVersionList = binOLPTJCS04_Service.getPrtFunDetailByVersionList(map);
		
		if (!CherryUtil.isBlankList(productsList) || !CherryUtil.isBlankList(prtFunDetailByVersionList)) {
			
			try{
				// 更新接口数据库
				if(!CherryUtil.isBlankList(productsList)){
					updIFDatabase(productsList,map);
				}
				
				// 备份产品下发数据
				binOLPTJCS04_Service.backProductIssue(map);
				
				// 更新产品功能开启接口表
				if(!CherryUtil.isBlankList(prtFunDetailByVersionList)){
					updIFPrtFunEnable(prtFunDetailByVersionList, map);
				}
				
				// Step2: 发送MQ通知
				
				// 产品表的表版本号在下发成功后+1
				String newTVersion = binOLCM15_BL.getNoPadLeftSequenceId(seqMap);
				map.put("newTVersion", newTVersion);
				
				Map<String,Object> MQMap = binOLBSCOM01_BL.getPrtNoticeMqMap(map, MessageConstants.MSG_SPRT_PRT);
				if(MQMap.isEmpty()){
					throw new Exception("产品实时下发通知组装异常");
				}
				
				//设定MQInfoDTO
				MQInfoDTO mqDTO = binOLBSCOM01_BL.setMQInfoDTO(MQMap,map);
				//调用共通发送MQ消息
				mqDTO.setMsgQueueName(CherryConstants.cherryToPosCMD);
				binOLMQCOM01_BL.sendMQMsg(mqDTO,false);
				
			}catch(Exception e){
				try{
					// 所有数据回滚，否则版本号无法控制
					binOLPTJCS04_Service.ifManualRollback();
					binOLPTJCS04_Service.manualRollback();
					logger.error(e.getMessage(),e);
//					result.put("result", "1");
				} catch(Exception ee){
					ee.printStackTrace();
					logger.error(e.getMessage(),e);
//					result.put("result", "1");
				}finally{
					result.put("result", "1");
					return result;
				}
			}
			
		}
		// 成功
		result.put("result", "0");
		
		return result;
	}
	
	/**
	 * 更新接口数据库
	 * 编码条码历史
	 * WITPOSA_ProductSetting编码条码变更记录
	 * 
	 * @param list
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	private void updIFDatabase(List<Map<String, Object>> list,Map<String, Object> paraMap) throws CherryException,Exception {
		for (Map<String, Object> itemMap : list) {
			// 取得产品分类list
			List<Map<String, Object>> cateList = (List<Map<String, Object>>) itemMap.get("list");
			// 取得产品分类信息Map
			Map<String, Object> cateMap = getCateMap(cateList);
			cateMap.putAll(itemMap);
			cateMap.put("tVersion", paraMap.get("tVersion"));
			try {
				// 保存接口产品表
				cateMap.put(CherryConstants.BRAND_CODE, paraMap.get(CherryConstants.BRAND_CODE));
				savePrt(cateMap);
				
				// 插入产品条码对应关系表

//			itemMap.put(ProductConstants.STARTTIME, paraMap.get(ProductConstants.STARTTIME));
				// 开始时间
				String sysdateTime = binOLPTJCS04_Service.getSYSDateTime();
				String sysHHSSMM = DateUtil.getSpecificDate(sysdateTime,DateUtil.TIME_PATTERN );
				itemMap.put(ProductConstants.CLOSINGTIME, ConvertUtil.getString(paraMap.get(CherryConstants.BUSINESS_DATE)) + " " + sysHHSSMM);
				itemMap.put(ProductConstants.STARTTIME, ConvertUtil.getString(paraMap.get(CherryConstants.BUSINESS_DATE)) + " " + sysHHSSMM);
				itemMap.put("enable_time", ConvertUtil.getString(paraMap.get(CherryConstants.BUSINESS_DATE)) + " " + sysHHSSMM);
				
				itemMap.put(CherryConstants.CREATEDBY, paraMap.get(CherryConstants.CREATEDBY));
				itemMap.put(CherryConstants.UPDATEDBY, paraMap.get(CherryConstants.UPDATEDBY));
				itemMap.put(CherryConstants.CREATEPGM, paraMap.get(CherryConstants.CREATEPGM));
				itemMap.put(CherryConstants.UPDATEPGM, paraMap.get(CherryConstants.UPDATEPGM));
				
				insertPrtBarCode(itemMap);
			} catch(CherryException ce){
				logger.error(ce.getErrMessage(),ce);
				throw new Exception(ce);
			} catch (Exception e) {
				logger.error(e.getMessage(),e);
				throw new Exception(e);
			}
		}
	}
	
	private void updIFPrtFunEnable(List<Map<String, Object>> list,Map<String, Object> paraMap) throws CherryException,Exception {
		for (Map<String, Object> prtFunDetailItemMap : list) {
			try{
				prtFunDetailItemMap.putAll(paraMap);
				// 删除产品功能开启接口表(根据brandArr、PrtFunDateCode、产品厂商ID)
				binOLPTJCS04_Service.delIFPrtFunEnable(prtFunDetailItemMap);
				// 插入产品功能开启接口表
				binOLPTJCS04_Service.addIFPrtFunEnable(prtFunDetailItemMap);
				
			} catch(Exception e){
				logger.error(e.getMessage(),e);
				String prtFunDateCode = ConvertUtil.getString(prtFunDetailItemMap.get("PrtFunDateCode"));
				String productVendorID = ConvertUtil.getString(prtFunDetailItemMap.get("BIN_ProductVendorID"));
				String unitCode = ConvertUtil.getString(prtFunDetailItemMap.get("UnitCode"));
				String barCode = ConvertUtil.getString(prtFunDetailItemMap.get("BarCode"));
				
				throw new CherryException("EBS00140",new String[]{prtFunDateCode,productVendorID,unitCode,barCode},e);
			}
		}
	}
	
	/**
	 * 插入接口数据库产品表
	 * 
	 * @param map
	 * @return
	 */
	private void savePrt(Map<String, Object> productMap)
			throws CherryException,Exception {
		try {
			
			// 设置产品接口表的状态值
			getPrtScsStatus(productMap);	
			
			// 查询是否有变化的unitcode barcode 
			Map<String,Object> barCodeModifyMap = binOLPTJCS04_Service.getBarCodeModify(productMap);
			if(null != barCodeModifyMap){
				productMap.put("OldUnitCode", barCodeModifyMap.get("OldUnitCode"));
				productMap.put("OldBarCode", barCodeModifyMap.get("OldBarCode"));
				// 有变化，原编码条码在product_SCS对应的记录的status设为D
				// step1: 更新老的编码条码，将其在Product_SCS停用，version为当前tVersion+1
				Integer updRes = binOLPTJCS04_Service.disProductSCS(productMap);
				// step2: 插入新的编码条码，使用merge(brand,productId,unitcode,barcode)
				Map<String,Object> mergeRes = binOLPTJCS04_Service.mergeProductSCS(productMap);
			}
			else{
				// 无变化，编码条码没有变化的话将当前产品属性通过merge(brand,productId,unitcode,barcode)更新到product_scs上
				Map<String,Object> mergeRes = binOLPTJCS04_Service.mergeProductSCS(productMap);
			}
			
			// 删除产品接口表(根据brand、unitcode、barcode)
//			binOLPTJCS04_Service.delIFProductInfo(productMap);
			// 插入产品接口表
//			productMap.put(CherryConstants.UNITCODE, "444444444444444444444444444444444444444444ssssssssssssssssssssssssssss");
//			binOLPTJCS04_Service.addProductSCS(productMap);
			// 插入件数加一
//			insertCount += 1;
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			String prtVendorId = ConvertUtil.getString(productMap.get("prtVendorId"));
			String unitCode = ConvertUtil.getString(productMap.get(CherryConstants.UNITCODE));
			String barCode = ConvertUtil.getString(productMap.get(CherryConstants.BARCODE));
			String name = ConvertUtil.getString(productMap.get(CherryConstants.NAMETOTAL));
			
			throw new CherryException("EBS00138",new String[]{prtVendorId,unitCode,barCode,name},e);
		}
	}
	
	/**
	 * 
	 * 插入接口数据库产品表
	 * 
	 * @param map
	 * @return
	 */
	private void savePrtOld(Map<String, Object> productMap)
			throws CherryException,Exception {
		try {
			
			// 设置产品接口表的状态值
			getPrtScsStatus(productMap);			
			
			// 删除产品接口表(根据brand、unitcode、barcode)
			binOLPTJCS04_Service.delIFProductInfo(productMap);
			// 插入产品接口表
//			productMap.put(CherryConstants.UNITCODE, "444444444444444444444444444444444444444444ssssssssssssssssssssssssssss");
			binOLPTJCS04_Service.addProductSCS(productMap);
			// 插入件数加一
//			insertCount += 1;
		} catch (Exception e) {
			
			String unitCode = ConvertUtil.getString(productMap.get(CherryConstants.UNITCODE));
			String barCode = ConvertUtil.getString(productMap.get(CherryConstants.BARCODE));
			String name = ConvertUtil.getString(productMap.get(CherryConstants.NAMETOTAL));
			
			throw new CherryException("EBS00138",new String[]{unitCode,barCode,name},e);
		}
	}
	
	/**
	 * 设置产品接口表的状态值
	 * @param map
	 */
	private void getPrtScsStatus(Map<String, Object> productMap){

		/*
		 处理逻辑 （实时下发接口状态值设值(0：正常销售、1：停用、2：下柜、3：未启用*)）
			1、先判断validFlag数值 如果停用直接使用停用
			2、如果validFlag=1,则判断是否下柜台，如果下柜台则使用下柜，如果未下柜，则判断是否在销售区间内，如果不在，则使用未启用

		终端对product_SCS status定义：
		E	表示	一个BARCODE对应多个UNITCODE时[product_scs往品牌的product导入时]，全部生效	
		D	表示	产品停用 	其他系统下发的数据，只有停用，无下柜和未启用状态	
		新增加下面两种状态：	
		H	表示	产品下柜	下发到witpos_xx数据库后都归为停用	
		G	表示	产品未启用 	下发到witpos_xx数据库后都归为停用
		
		新后台对product_SCS status定义：
		E：正常销售、D：停用、H：下柜、G：未启用
		*/
		
		String prtscs_status = "E"; // Product_SCS 状态值,
		
		String ValidFlag = ConvertUtil.getString(productMap.get("ValidFlag"));
		String prtStatus = ConvertUtil.getString(productMap.get("status")); // 新后台产品状态 D：表明下柜产品； E：表明产品生效，可销售可订货；
		String sellDateFlag = ConvertUtil.getString(productMap.get("SellDateFlag")); // 是否不在销售日期区间  0:未过期 1:已过期
		
		if(CherryConstants.VALIDFLAG_DISABLE.equals(ValidFlag)){
			// 产品无效
			prtscs_status = "D";
		}else{
			// 产品有效
			
			if(ProductConstants.PRODUCT_STATUS_D.equalsIgnoreCase(prtStatus)){
				// 产品下柜
				prtscs_status = "H";
			}else {
				//
				if(ProductConstants.SellDateFlag_1.equals(sellDateFlag)){
					// 产品未启用
					prtscs_status = "G";
				}else{
					// 有效，正常销售
					prtscs_status = "E";
				}
			}
		}
		
		productMap.put("prtscs_status", prtscs_status);
		
		
		// 转换ModuleCode的 key值为val值
		String moduleCode = ConvertUtil.getString(productMap.get("ModuleCode"));
		
		Map<String, Object> paraMap = new HashMap<String, Object>();
		paraMap.put("codeType", "1190");
		paraMap.put("codeKey", moduleCode);
		List<Map<String, Object>> codeList = binOLPTJCS04_Service.getCodeByKey(paraMap);
		
		String brandCode = ConvertUtil.getString(productMap.get(CherryConstants.BRAND_CODE));
		String moduleValBrand = null;
		String moduleValOrg = null;
		
		for(Map<String, Object> codeMap : codeList){
			if(ConvertUtil.getString(codeMap.get("BrandCode")).equals(brandCode)){
				moduleValBrand = ConvertUtil.getString(codeMap.get("Value1"));
				break;
			}else if(ConvertUtil.getString(codeMap.get("BrandCode")).equals("-9999")){
				moduleValOrg = ConvertUtil.getString(codeMap.get("Value1"));
			}
		}
		
//		String codeValue = code.getVal("1190", moduleCode);
		productMap.put("ModuleCode", moduleValBrand != null ? moduleValBrand : moduleValOrg);

	}
	
	/**
	 * 插入产品条码对应关系表
	 * 
	 * @param map
	 * @throws Exception 
	 */
	private void insertPrtBarCode(Map<String, Object> map) throws CherryException,Exception {
		// 查询产品条码对应关系件数
		int count = binOLPTJCS04_Service.getBarCodeCount(map);
		if (count == 0) {
//			map.putAll(comMap);
			try {  
				// 插入变化的unitcode barcode 
				Map<String,Object> barCodeModifyMap = binOLPTJCS04_Service.getBarCodeModify(map);
				if(null != barCodeModifyMap){
					Map<String,Object> insertPrtSettingMap = new HashMap<String, Object>();
					insertPrtSettingMap.put("brand", map.get(CherryConstants.BRAND_CODE)); // 品牌code
					insertPrtSettingMap.put("new_barcode", barCodeModifyMap.get("BarCode")); // 产品条码
					insertPrtSettingMap.put("new_unitcode", barCodeModifyMap.get("UnitCode")); // 产品编码
					insertPrtSettingMap.put("old_barcode", barCodeModifyMap.get("OldBarCode")); // 老产品条码
					insertPrtSettingMap.put("old_unitcode", barCodeModifyMap.get("OldUnitCode")); // 老产品编码
					insertPrtSettingMap.put("prt_id", barCodeModifyMap.get("BIN_ProductVendorID"));  // 产品厂商ID（新后台）
					insertPrtSettingMap.put("prt_type", "N"); // 产品（N）/促销品区分(P)
					insertPrtSettingMap.put("enable_time", map.get("enable_time")); // 新产品生效日期
					// 插入ProductSetting
					binOLPTJCS04_Service.addProductSetting(insertPrtSettingMap);
				}
				
				// 更新停用日时
				binOLPTJCS04_Service.updateClosingTime(map);
				// 插入产品条码对应关系表
				Map<String,Object> praMap = new HashMap<String,Object>();
				praMap.putAll(map);
				binOLPTJCS04_Service.insertPrtBarCode(praMap);
			} catch (Exception e) {
				String unitCode = ConvertUtil.getString(map.get(CherryConstants.UNITCODE));
				String barCode = ConvertUtil.getString(map.get(CherryConstants.BARCODE));
				String name = ConvertUtil.getString(map.get(CherryConstants.NAMETOTAL));
				
				throw new CherryException("EBS00125",new String[]{unitCode,barCode,name});
			}
		}
	}

	
	/**
	 * 取得产品分类信息Map
	 * 
	 * @param list
	 * @return
	 */
	private Map<String, Object> getCateMap(List<Map<String, Object>> list) {
		Map<String, Object> cateMap = new HashMap<String, Object>();
		if (!CherryUtil.isBlankList(list)) {
			for (Map<String, Object> map : list) {
				// 分类类型
				String cateType = ConvertUtil.getString(map
						.get(ProductConstants.CATE_TYPE));
				// 分类代码
				String cateCode = ConvertUtil.getString(map
						.get(ProductConstants.CATE_CODE));
				// 分类名
				String cateName = ConvertUtil.getString(map
						.get(ProductConstants.CATE_NAME));
				if (ProductConstants.CATE_TYPE_1.equals(cateType)) {
					// 大分类
					cateMap.put(ProductConstants.BCLASSCODE, cateCode);
					cateMap.put(ProductConstants.BCLASSNAME, cateName);
					
				} else if (ProductConstants.CATE_TYPE_3.equals(cateType)) {
					// 中分类
					cateMap.put(ProductConstants.MCLASSCODE, cateCode);
					cateMap.put(ProductConstants.MCLASSNAME, cateName);
				} else if (ProductConstants.CATE_TYPE_2.equals(cateType)) {
					// 小分类
					cateMap.put(ProductConstants.LCLASSCODE, cateCode);
					cateMap.put(ProductConstants.LCLASSNAME, cateName);
				}
			}
		}
		return cateMap;
	}
	
	
}
