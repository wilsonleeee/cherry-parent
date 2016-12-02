/*		
 * @(#)BINOLCM02_BL.java     1.0 2010/10/27		
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

package com.cherry.cm.cmbussiness.bl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.cmbussiness.service.BINOLCM02_Service;
import com.cherry.cm.cmbussiness.service.BINOLCM05_Service;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherrySecret;
import com.cherry.cm.core.CodeTable;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cp.common.CampConstants;
import com.cherry.mb.mbm.service.BINOLMBMBM11_Service;
import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

/**
 * 弹出datatable 共通
 * @author huzude
 *
 */
@SuppressWarnings("unchecked")
public class BINOLCM02_BL {
	
	@Resource
	private BINOLCM02_Service binOLCM02_Service;
	
	@Resource
	private BINOLCM05_Service binOLCM05_Service;
	
	@Resource
	private BINOLMBMBM11_Service binOLMBMBM11_Service;
	
	/** 系统配置项 共通BL */
	@Resource(name="binOLCM14_BL")
	private transient BINOLCM14_BL binOLCM14_BL;
	
	@Resource(name="CodeTable")
	private CodeTable code;
	
	private static final Logger logger = LoggerFactory.getLogger(BINOLCM02_BL.class); 
	/**
	 * 取得促销产品信息
	 * @param map
	 * @return
	 * @throws Exception 
	 */
	public HashMap getPromotionDialogInfoList (Map<String, Object> map) throws Exception{
		HashMap resultMap = new HashMap();
		List filterList = new ArrayList();
		// 业务日期
		String bussDate = binOLCM02_Service.getBussinessDate(map);
		map.put(CherryConstants.BUSINESS_DATE, bussDate);
		// 需要过滤的字段名
		filterList.add("unitCode");
		filterList.add("barCode");
		filterList.add("nameTotal");
		filterList.add("nameForeign");
		filterList.add("standardCost");
		filterList.add("PrimaryCategoryNameChinese");
		filterList.add("PrimaryCategoryNameForeign");
		filterList.add("SecondryCategoryNameChinese");
		filterList.add("SecondryCategoryNameForeign");
		filterList.add("SmallCategoryNameChinese");
		filterList.add("SmallCategoryNameForeign");
		map.put(CherryConstants.FILTER_LIST_NAME, filterList);
		// 取得件数
		int count = binOLCM02_Service.getPromotionInfoCount(map);
		// 取得促销信息List
		List popPrmProductInfoList = binOLCM02_Service.getPromotionShortInfoList(map);
		if(popPrmProductInfoList != null && !popPrmProductInfoList.isEmpty()) {
			for(int i = 0; i < popPrmProductInfoList.size(); i++) {
				Map popPrmProductInfo = (Map)popPrmProductInfoList.get(i);
				Map popPrmProductInfoTemp = new HashMap();
				// 促销品厂商编码
				popPrmProductInfoTemp.put("unitCode", popPrmProductInfo.get("unitCode"));
				// 促销品条码
				popPrmProductInfoTemp.put("barCode", popPrmProductInfo.get("barCode"));
				// 促销品名称
				popPrmProductInfoTemp.put("nameTotal", popPrmProductInfo.get("nameTotal"));
				// 促销品厂商ID
				popPrmProductInfoTemp.put("promotionProductVendorId", popPrmProductInfo.get("BIN_PromotionProductVendorID"));
				// 促销品厂商ID
				popPrmProductInfoTemp.put("proId", popPrmProductInfo.get("BIN_PromotionProductVendorID"));
				// 促销品结算价格
				Object standardCost = popPrmProductInfo.get("standardCost");
				if(standardCost != null) {
					popPrmProductInfoTemp.put("standardCost", standardCost.toString());
				} else {
					popPrmProductInfoTemp.put("standardCost", "");
				}
				popPrmProductInfo.put("prmProductInfo", JSONUtil.serialize(popPrmProductInfoTemp));
			}
		}
		resultMap.put(CherryConstants.POP_PRMPRODUCT_LIST, popPrmProductInfoList);
		resultMap.put("count", count);
		return resultMap;
	}
	/**
	 * 取得促销产品信息(薇诺娜，合并)
	 * @param map
	 * @return
	 * @throws Exception 
	 */
	public HashMap getPromotionDialogInfoListOne (Map<String, Object> map) throws Exception{
		HashMap resultMap = new HashMap();
		List filterList = new ArrayList();
		// 业务日期
		String bussDate = binOLCM02_Service.getBussinessDate(map);
		map.put(CherryConstants.BUSINESS_DATE, bussDate);
		// 需要过滤的字段名
		filterList.add("barCode");
		filterList.add("nameTotal");
		filterList.add("nameForeign");
		filterList.add("standardCost");
		filterList.add("PrimaryCategoryNameChinese");
		filterList.add("PrimaryCategoryNameForeign");
		filterList.add("SecondryCategoryNameChinese");
		filterList.add("SecondryCategoryNameForeign");
		filterList.add("SmallCategoryNameChinese");
		filterList.add("SmallCategoryNameForeign");
		map.put(CherryConstants.FILTER_LIST_NAME, filterList);
		// 取得件数
		int count = binOLCM02_Service.getPromotionInfoCountOne(map);
		// 取得促销信息List
		List popPrmProductInfoList = binOLCM02_Service.getPromotionShortInfoListOne(map);
		if(popPrmProductInfoList != null && !popPrmProductInfoList.isEmpty()) {
			for(int i = 0; i < popPrmProductInfoList.size(); i++) {
				Map popPrmProductInfo = (Map)popPrmProductInfoList.get(i);
				Map popPrmProductInfoTemp = new HashMap();				
				// 促销品条码
				popPrmProductInfoTemp.put("barCode", popPrmProductInfo.get("barCode"));
				// 促销品名称
				popPrmProductInfoTemp.put("nameTotal", popPrmProductInfo.get("nameTotal"));
				// 促销品厂商ID
				popPrmProductInfoTemp.put("promotionProductVendorId", popPrmProductInfo.get("BIN_PromotionProductVendorID"));
				// 促销品厂商ID
				popPrmProductInfoTemp.put("proId", popPrmProductInfo.get("BIN_PromotionProductVendorID"));
				// 促销品结算价格
				Object standardCost = popPrmProductInfo.get("standardCost");
				if(standardCost != null) {
					popPrmProductInfoTemp.put("standardCost", standardCost.toString());
				} else {
					popPrmProductInfoTemp.put("standardCost", "");
				}
				popPrmProductInfo.put("prmProductInfo", JSONUtil.serialize(popPrmProductInfoTemp));
			}
		}
		resultMap.put(CherryConstants.POP_PRMPRODUCT_LIST, popPrmProductInfoList);
		resultMap.put("count", count);
		return resultMap;
	}

	public HashMap getCategoryTreeInfoList (Map<String, Object> map) throws Exception{
		HashMap resultMap = new HashMap();
		List<Map<String,Object>> categoryTreeResultList = new ArrayList<Map<String, Object>>();
		List<Map<String,Object>> categoryTreeInfoList = binOLCM02_Service.getCategoryTreeInfoList(map);
		for (int j = 0; j < categoryTreeInfoList.size(); j++) {
			Map<String, Object> categoryInfoMap = categoryTreeInfoList.get(j);
			categoryTreeResultList = initCategoryRelationship(categoryTreeResultList, categoryInfoMap);
		}
		resultMap.put(CherryConstants.CATEGORY_TREE_LIST,categoryTreeResultList);
		return resultMap;
	}
	/**
	 * @param categoryRelationship
	 * @param productInfo
	 */
	public List initCategoryRelationship(List<Map<String, Object>> categoryRelationship, Map<String, Object> productInfo) {
		Object categoryBigId = productInfo.get("categoryBigId");
		Object primaryCategoryBig = productInfo.get("primaryCategoryBig");
		if (categoryBigId != null && primaryCategoryBig != null) {
			//如果不为空的话,处理该大分类
			categoryRelationship = initBigCategory(categoryRelationship, productInfo);
			//得到中分类信息
			Object categoryMediumId = productInfo.get("categoryMediumId");
			Object primaryCategoryMedium = productInfo.get("primaryCategoryMedium");
			if (categoryMediumId != null && primaryCategoryMedium != null) {
				//如果中分类不为空的话
				categoryRelationship = initMediumCategory(categoryRelationship, productInfo);
				Object categorySmallId = productInfo.get("categorySmallId");
				Object primaryCategorySmall = productInfo.get("primaryCategorySmall");
				if (categorySmallId != null && primaryCategorySmall != null) {
					//小分类不为空的话
					categoryRelationship = initSmallCategory(categoryRelationship, productInfo);
				}
			}
		}
		return categoryRelationship;
	}
	/**
	 * @param categoryRelationship
	 * @param productInfo
	 */
	public List initBigCategory(List<Map<String, Object>> categoryRelationship, Map<String, Object> productInfo) {
		Object categoryBigId = productInfo.get("categoryBigId");
		Object primaryCategoryBig = productInfo.get("primaryCategoryBig");
		//不为空的话，首先检查是否已经存储了这个大分类
		if (categoryRelationship != null && categoryRelationship.size() > 0) {
			//如果存储有大分类的
			int i = 0;
			for (i = 0; i < categoryRelationship.size(); i++) {
				Map<String, Object> relationMap = categoryRelationship.get(i);
				if (relationMap.get("categoryBigId").toString().equals(categoryBigId.toString())) {
					//如果已经存储的话，不需要进行存储
					break;
				}
			}
			if (i == categoryRelationship.size()) {
				//如果没有这个大分类
				Map<String, Object> newBigMap = new HashMap<String, Object>();
				newBigMap.put("categoryBigId", categoryBigId);
				newBigMap.put("primaryCategoryBig", primaryCategoryBig);
				categoryRelationship.add(newBigMap);
			}
		} else {
			//如果里面是空的话，直接添加这个大分类就可以了
			Map<String, Object> newBigMap = new HashMap<String, Object>();
			newBigMap.put("categoryBigId", categoryBigId);
			newBigMap.put("primaryCategoryBig", primaryCategoryBig);
			categoryRelationship = new ArrayList<Map<String, Object>>();
			categoryRelationship.add(newBigMap);
		}
		return categoryRelationship;
	}

	/**
	 * @param categoryRelationship
	 * @param productInfo
	 */
	public List initMediumCategory(List<Map<String, Object>> categoryRelationship, Map<String, Object> productInfo) {
		//得到中分类信息
		Object categoryMediumId = productInfo.get("categoryMediumId");
		Object primaryCategoryMedium = productInfo.get("primaryCategoryMedium");
		//得到大分类信息
		Object categoryBigId = productInfo.get("categoryBigId");
		Object primaryBig = productInfo.get("primaryBig");
		//中分类是不为空的，判断这个中分类是否已经存在于这个大分类下了
		for (int i = 0; i < categoryRelationship.size(); i++) {
			Map<String, Object> bigMap = categoryRelationship.get(i);
			if (bigMap.get("categoryBigId").toString().equals(categoryBigId.toString())) {
				//找到该中分类所在的大分类,得到大分类下面的中分类
				List<Map<String, Object>> mediumList = (List) bigMap.get("medium");
				if (mediumList != null) {
					//如果里面存储有种分类的话
					int j = 0;
					for (j = 0; j < mediumList.size(); j++) {
						Map<String, Object> mediumMap = mediumList.get(j);
						if (mediumMap.get("categoryMediumId").toString().equals(categoryMediumId.toString())) {
							//如果有存储这个中分类的情况下
							break;
						}
					}
					if (j == mediumList.size()) {
						//如果没有存储的话
						Map<String, Object> mediumMap = new HashMap<String, Object>();
						mediumMap.put("categoryMediumId", categoryMediumId);
						mediumMap.put("primaryCategoryMedium", primaryCategoryMedium);
						mediumList.add(mediumMap);
						//bigMap.put("medium",mediumList);
					}
				} else {
					//如果没有存储的话,直接存储这个中分类
					Map<String, Object> mediumMap = new HashMap<String, Object>();
					mediumMap.put("categoryMediumId", categoryMediumId);
					mediumMap.put("primaryCategoryMedium", primaryCategoryMedium);
					mediumList = new ArrayList<Map<String, Object>>();
					mediumList.add(mediumMap);
					bigMap.put("medium", mediumList);
				}
			}
		}
		//肯定是可以找得到这个大分类的
		return categoryRelationship;
	}

	/**
	 * @param categoryRelationship
	 * @param productInfo
	 */
	public List initSmallCategory(List<Map<String, Object>> categoryRelationship, Map<String, Object> productInfo) {
		//得到中分类信息
		Object categoryMediumId = productInfo.get("categoryMediumId");
		Object primaryMedium = productInfo.get("primaryMedium");
		//得到大分类信息
		Object categoryBigId = productInfo.get("categoryBigId");
		Object primaryCategoryBig = productInfo.get("primaryCategoryBig");
		//得到小分类信息
		Object categorySmallId = productInfo.get("categorySmallId");
		Object primaryCategorySmall = productInfo.get("primaryCategorySmall");
		//找到对应的大分类
		for (int i = 0; i < categoryRelationship.size(); i++) {
			Map<String, Object> bigMap = categoryRelationship.get(i);
			if (bigMap.get("categoryBigId").toString().equals(categoryBigId.toString())) {
				//在该大分类下面，找到对应的中分类
				List<Map<String, Object>> mediumList = (List) bigMap.get("medium");
				//mediumList 不可能为空
				for (int j = 0; j < mediumList.size(); j++) {
					Map<String, Object> mediumMap = mediumList.get(j);
					if (mediumMap.get("categoryMediumId").toString().equals(categoryMediumId.toString())) {
						//找到这个中分类,得到中分类下面的小分类
						List<Map<String, Object>> smallList = (List) mediumMap.get("small");
						if (smallList != null && smallList.size() > 0) {
							//如果里面存储有小分类的话
							int k = 0;
							for (k = 0; k < smallList.size(); k++) {
								Map<String, Object> smallMap = smallList.get(k);
								if (smallMap.get("categorySmallId").toString().equals(categorySmallId.toString())) {
									//如果有一样的话,跳过
									break;
								}
							}
							if (k == smallList.size()) {
								//如果没有的话
								Map<String, Object> smallMap = new HashMap<String, Object>();
								smallMap.put("categorySmallId", categorySmallId);
								smallMap.put("primaryCategorySmall", primaryCategorySmall);
								smallList.add(smallMap);
							}
						} else {
							//如果还没有存储小分类,直接存储即可
							Map<String, Object> smallMap = new HashMap<String, Object>();
							smallMap.put("categorySmallId", categorySmallId);
							smallMap.put("primaryCategorySmall", primaryCategorySmall);
							smallList = new ArrayList<Map<String, Object>>();
							smallList.add(smallMap);
							mediumMap.put("small", smallList);
						}
					}
				}
				//不存在找到中分类
			}
		}
		//不存在找到大分类的情况
		return categoryRelationship;
	}
	/**
	 * 取得产品信息
	 * @param map
	 * @return
	 */
	public HashMap getProductDialogInfoList (Map<String, Object> map) throws Exception{
		HashMap resultMap = new HashMap();
		List filterList = new ArrayList();
		// 业务日期
		String bussDate = binOLCM02_Service.getBussinessDate(map);
		map.put(CherryConstants.BUSINESS_DATE, bussDate);
		// 需要过滤的字段名
		filterList.add("A.unitCode");
		filterList.add("C.barCode");
		filterList.add("A.OriginalBrand");
		filterList.add("C.validFlag");
		filterList.add("A.nameTotal");
		filterList.add("A.nameForeign");
		filterList.add("D.PropValueChinese");
		filterList.add("D.PropValueForeign");
		filterList.add("E.PropValueChinese");
		filterList.add("E.PropValueForeign");
		filterList.add("B.salePrice");
		map.put(CherryConstants.FILTER_LIST_NAME, filterList);
		// 取得件数
		int count = binOLCM02_Service.getProductInfoCount(map);
		
		List ignoreSoluPopProductInfoList = new ArrayList(); // 剔除产品方案中的产品，画面上不显示一码多品，对于一码多品的只显示一条
		Map<String,Integer> soluUnitCodeMap = new HashMap<String, Integer>();
		String ignoreSoluId = ConvertUtil.getString(map.get("ignoreSoluId"));
		int ignorePopProductInfoCount = 0;
		
		List ignorePrtFunPopProductInfoList = new ArrayList(); // 剔除产品功能开启时间中的产品，画面上不显示一码多品，对于一码多品的只显示一条
		Map<String,Integer> prtFunUnitCodeMap = new HashMap<String, Integer>();
		String ignorePrtFunId = ConvertUtil.getString(map.get("ignorePrtFunId"));
		
		// 取得产品信息List
		List popProductInfoList = binOLCM02_Service.getProductShortInfoList(map);
		if(popProductInfoList != null && !popProductInfoList.isEmpty()) {
			for(int i = 0; i < popProductInfoList.size(); i++) {
				Map popProductInfo = (Map)popProductInfoList.get(i);
				Map popProductInfoTemp = new HashMap();
				// 产品厂商编码
				String unitCode = (String)popProductInfo.get("unitCode");
				if(!ignoreSoluId.isEmpty()){
					if(!soluUnitCodeMap.containsKey(unitCode)){
						soluUnitCodeMap.put(unitCode, 1);
						ignoreSoluPopProductInfoList.add(popProductInfo);
					}else{
						ignorePopProductInfoCount++;
					}
				}else if (!ignorePrtFunId.isEmpty()){
					if(!prtFunUnitCodeMap.containsKey(unitCode)){
						prtFunUnitCodeMap.put(unitCode, 1);
						ignorePrtFunPopProductInfoList.add(popProductInfo);
					}else{
						ignorePopProductInfoCount++;
					}
				}
				
				popProductInfoTemp.put("unitCode", popProductInfo.get("unitCode"));
				// 产品条码
				popProductInfoTemp.put("barCode", popProductInfo.get("barCode"));
				// 子品牌 
				String originalBrand = ConvertUtil.getString(popProductInfo.get("originalBrand"));
				popProductInfoTemp.put("originalBrand", CherryChecker.isNullOrEmpty(originalBrand)? originalBrand : code.getVal("1299", originalBrand));
				// 有效区分
				popProductInfoTemp.put("validFlag", popProductInfo.get("validFlag"));
				
				
				// 产品名称
				popProductInfoTemp.put("nameTotal", popProductInfo.get("nameTotal"));
				// 产品价格
				Object salePrice = popProductInfo.get("salePrice");
				if(salePrice != null) {
					popProductInfoTemp.put("salePrice", salePrice);
				} else {
					popProductInfoTemp.put("salePrice", "");
				}
				// 产品厂商ID
				popProductInfoTemp.put("productVendorId", popProductInfo.get("BIN_ProductVendorID"));
				// 产品厂商ID
				popProductInfoTemp.put("proId", popProductInfo.get("BIN_ProductVendorID"));
				// 产品厂商ID
				popProductInfoTemp.put("prtId", popProductInfo.get("bin_ProductID"));
				// 产品结算价格
				Object standardCost = popProductInfo.get("standardCost");
				if(standardCost != null) {
					popProductInfoTemp.put("standardCost", standardCost);
				} else {
					popProductInfoTemp.put("standardCost", "");
				}
				// 产品采购价格
				Object orderPrice = popProductInfo.get("orderPrice");
				if(orderPrice != null) {
					popProductInfoTemp.put("orderPrice", orderPrice);
				} else {
					popProductInfoTemp.put("orderPrice", "");
				}
				// 产品最低销售价
				Object minSalePrice = popProductInfo.get("minSalePrice");
				if(minSalePrice != null) {
					popProductInfoTemp.put("minSalePrice", minSalePrice);
				} else {
					popProductInfoTemp.put("minSalePrice", "");
				}
				// 产品最高销售价
				Object maxSalePrice = popProductInfo.get("maxSalePrice");
				if(minSalePrice != null) {
					popProductInfoTemp.put("maxSalePrice", maxSalePrice);
				} else {
					popProductInfoTemp.put("maxSalePrice", "");
				}
				
				//会员价格
                Object memPrice = popProductInfo.get("memPrice");
                if(memPrice != null) {
                    popProductInfoTemp.put("memPrice", memPrice);
                } else {
                    popProductInfoTemp.put("memPrice", "");
                }
				//大分类
				popProductInfoTemp.put("primaryCategoryBig",popProductInfo.get("primaryCategoryBig"));
				//中分类
				//popProductInfoTemp.put("primaryCategoryMedium",popProductInfo.get("primaryCategoryMedium"));
				//小分类
				popProductInfoTemp.put("primaryCategorySmall",popProductInfo.get("primaryCategorySmall"));
				
				//白金会员价格
                Object platinumPrice = popProductInfo.get("platinumPrice");
                if(platinumPrice != null) {
                    popProductInfoTemp.put("platinumPrice", platinumPrice);
                } else {
                    popProductInfoTemp.put("platinumPrice", "");
                }
                
				// 吊牌价格
                Object tagPrice = popProductInfo.get("tagPrice");
                if(tagPrice != null) {
                    popProductInfoTemp.put("tagPrice", tagPrice);
                } else {
                    popProductInfoTemp.put("tagPrice", "");
                }
				popProductInfo.put("productInfo", JSONUtil.serialize(popProductInfoTemp));
			}
		}
		
		if(!ignoreSoluId.isEmpty()){
			resultMap.put(CherryConstants.POP_PRMPRODUCT_LIST, ignoreSoluId.isEmpty()? popProductInfoList : ignoreSoluPopProductInfoList);
			resultMap.put("count", ignoreSoluId.isEmpty() ? count : count - ignorePopProductInfoCount);
		}else if (!ignorePrtFunId.isEmpty()){
			resultMap.put(CherryConstants.POP_PRMPRODUCT_LIST, ignorePrtFunId.isEmpty()? popProductInfoList : ignorePrtFunPopProductInfoList);
			resultMap.put("count", ignorePrtFunId.isEmpty() ? count : count - ignorePopProductInfoCount);
		}else{
			resultMap.put(CherryConstants.POP_PRMPRODUCT_LIST, popProductInfoList);
			resultMap.put("count", count);
		}
		
		return resultMap;
	}
	

	/**
	 * 取得产品信息（浓妆淡抹订货）
	 * @param map
	 * @return
	 */
	public HashMap getProductDialogInfoListTwo (Map<String, Object> map) throws Exception{
		HashMap resultMap = new HashMap();
		List filterList = new ArrayList();
		if(map.containsKey("FILTER_VALUE")){
			String searchString=map.get("FILTER_VALUE").toString();
			if(!CherryChecker.isNullOrEmpty(searchString)){				
				List<Map<String, Object>> list = (List<Map<String, Object>>) JSONUtil.deserialize(searchString);	
				for(Map<String, Object> param:list){					
					map.put(param.get("key").toString(), param.get("value").toString());	
				}
			}
			// 查询勾选新品时，获取新品定义时间
			if("1".equals(map.get("newProductFlag"))){
		    String newProductTime = binOLCM14_BL.getConfigValue("1380",ConvertUtil.getString(map.get("organizationInfoId")), ConvertUtil.getString(map.get("brandInfoId")));
		    int newProductInitTime = Integer.parseInt(newProductTime);
		    int newProductInitTimeNegative=-newProductInitTime;
             map.put("newProductInitTimeNegative", newProductInitTimeNegative);
		 	
			};
		}
		// 业务日期
		String bussDate = binOLCM02_Service.getBussinessDate(map);
	
		map.put(CherryConstants.BUSINESS_DATE, bussDate);
		// 需要过滤的字段名
		filterList.add("A.unitCode");
		filterList.add("C.barCode");
		filterList.add("A.OriginalBrand");
		filterList.add("C.validFlag");
		filterList.add("A.nameTotal");
		filterList.add("A.nameForeign");
		filterList.add("D.PropValueChinese");
		filterList.add("D.PropValueForeign");
		filterList.add("E.PropValueChinese");
		filterList.add("E.PropValueForeign");
		filterList.add("B.salePrice");
		map.put(CherryConstants.FILTER_LIST_NAME, filterList);
		// 取得件数
		int count = binOLCM02_Service.getProductInfoCountTwo(map);
		
		List ignoreSoluPopProductInfoList = new ArrayList(); // 剔除产品方案中的产品，画面上不显示一码多品，对于一码多品的只显示一条
		Map<String,Integer> soluUnitCodeMap = new HashMap<String, Integer>();
		String ignoreSoluId = ConvertUtil.getString(map.get("ignoreSoluId"));
		int ignorePopProductInfoCount = 0;
		
		List ignorePrtFunPopProductInfoList = new ArrayList(); // 剔除产品功能开启时间中的产品，画面上不显示一码多品，对于一码多品的只显示一条
		Map<String,Integer> prtFunUnitCodeMap = new HashMap<String, Integer>();
		String ignorePrtFunId = ConvertUtil.getString(map.get("ignorePrtFunId"));
		
		// 取得产品信息List
		List<Map<String, Object>> popProductInfoList = (List<Map<String, Object>>)binOLCM02_Service.getProductShortInfoListTwo(map);
		List<Map<String, Object>> tempProductInfoList = new ArrayList<Map<String,Object>>();

		for(Map<String, Object> param:popProductInfoList){
			tempProductInfoList.add(param);
		}

		List<Map<String, Object>> inventoryList = new ArrayList<Map<String,Object>>();//库存集合List
		if(popProductInfoList != null && !popProductInfoList.isEmpty()) {
			for(int i=0;i<tempProductInfoList.size();i++){
				Map<String, Object> tempMap =tempProductInfoList.get(i);
				if(CherryChecker.isNullOrEmpty(tempMap.get("ItemCode"))){
					tempProductInfoList.remove(i--);
				}
			}
			
			if(!tempProductInfoList.isEmpty() && tempProductInfoList.size()>0){//去除ItemCode为空的产品以后的List
				String param = null;//调金蝶库存接口的参数
				for(int i=0;i<tempProductInfoList.size();i++){
					if(i==0){
						param = ConvertUtil.getString(tempProductInfoList.get(i).get("ItemCode")); 
					}else{
						param +=","+ConvertUtil.getString(tempProductInfoList.get(i).get("ItemCode"));

					}
				}
				
				if(!CherryChecker.isNullOrEmpty(param)){//参数不为空
					Map<String, Object> paramMap= new HashMap<String, Object>();
					paramMap.put("ItemCode", param);
					inventoryList= binOLCM02_Service.getInventoryByItemCode(paramMap);//返回的库存信息List
				}
			}
			
			for(Map<String, Object> tempMap:popProductInfoList){
				
				if(!inventoryList.isEmpty() && inventoryList.size()>0){//库存集合不为空的情况
					for(Map<String, Object> stockMap:inventoryList){//给每个产品设置库存
						int stockAmount=0;
						if(!CherryChecker.isNullOrEmpty(stockMap.get("IFProductId")) && !CherryChecker.isNullOrEmpty(tempMap.get("ItemCode"))){									
							if(ConvertUtil.getString(stockMap.get("IFProductId")).equals(ConvertUtil.getString(tempMap.get("ItemCode")))){
								if(!CherryChecker.isNullOrEmpty(stockMap.get("Quantity"))){
									stockAmount= ConvertUtil.getInt(stockMap.get("Quantity"));
								}
							}
						}				
						tempMap.put("stockAmount", stockAmount);
						// stockAmount不为零，表示获取到金蝶库存跳出循环
						if(0!=stockAmount){
							break;
						}
					}
				}else{//库存集合为空的情况
					tempMap.put("stockAmount", 0);
				}
			}
			
			for(int i = 0; i < popProductInfoList.size(); i++) {
				Map popProductInfo = (Map)popProductInfoList.get(i);
				Map popProductInfoTemp = new HashMap();
				// 产品厂商编码
				String unitCode = (String)popProductInfo.get("unitCode");
				if(!ignoreSoluId.isEmpty()){
					if(!soluUnitCodeMap.containsKey(unitCode)){
						soluUnitCodeMap.put(unitCode, 1);
						ignoreSoluPopProductInfoList.add(popProductInfo);
					}else{
						ignorePopProductInfoCount++;
					}
				}else if (!ignorePrtFunId.isEmpty()){
					if(!prtFunUnitCodeMap.containsKey(unitCode)){
						prtFunUnitCodeMap.put(unitCode, 1);
						ignorePrtFunPopProductInfoList.add(popProductInfo);
					}else{
						ignorePopProductInfoCount++;
					}
				}
				
				popProductInfoTemp.put("unitCode", popProductInfo.get("unitCode"));
				// 产品条码
				popProductInfoTemp.put("barCode", popProductInfo.get("barCode"));
				// 子品牌 
				String originalBrand = ConvertUtil.getString(popProductInfo.get("originalBrand"));
				popProductInfoTemp.put("originalBrand", CherryChecker.isNullOrEmpty(originalBrand)? originalBrand : code.getVal("1299", originalBrand));
				// 有效区分
				popProductInfoTemp.put("validFlag", popProductInfo.get("validFlag"));
				
				
				popProductInfoTemp.put("stockAmount", popProductInfo.get("stockAmount"));
				// 产品名称
				popProductInfoTemp.put("nameTotal", popProductInfo.get("nameTotal"));
				// 产品价格
				Object salePrice = popProductInfo.get("salePrice");
				if(salePrice != null) {
					popProductInfoTemp.put("salePrice", salePrice);
				} else {
					popProductInfoTemp.put("salePrice", "");
				}
				// 产品厂商ID
				popProductInfoTemp.put("productVendorId", popProductInfo.get("BIN_ProductVendorID"));
				// 产品厂商ID
				popProductInfoTemp.put("proId", popProductInfo.get("BIN_ProductVendorID"));
				// 产品厂商ID
				popProductInfoTemp.put("prtId", popProductInfo.get("bin_ProductID"));
				// 产品结算价格
				Object standardCost = popProductInfo.get("standardCost");
				if(standardCost != null) {
					popProductInfoTemp.put("standardCost", standardCost);
				} else {
					popProductInfoTemp.put("standardCost", "");
				}
				// 产品采购价格
				Object orderPrice = popProductInfo.get("orderPrice");
				if(orderPrice != null) {
					popProductInfoTemp.put("orderPrice", orderPrice);
				} else {
					popProductInfoTemp.put("orderPrice", "");
				}
				// 产品最低销售价
				Object minSalePrice = popProductInfo.get("minSalePrice");
				if(minSalePrice != null) {
					popProductInfoTemp.put("minSalePrice", minSalePrice);
				} else {
					popProductInfoTemp.put("minSalePrice", "");
				}
				// 产品最高销售价
				Object maxSalePrice = popProductInfo.get("maxSalePrice");
				if(minSalePrice != null) {
					popProductInfoTemp.put("maxSalePrice", maxSalePrice);
				} else {
					popProductInfoTemp.put("maxSalePrice", "");
				}
				
				//会员价格
                Object memPrice = popProductInfo.get("memPrice");
                if(memPrice != null) {
                    popProductInfoTemp.put("memPrice", memPrice);
                } else {
                    popProductInfoTemp.put("memPrice", "");
                }
				// 标准配送价
                Object distributionPrice = popProductInfo.get("distributionPrice");
                if(distributionPrice != null) {
                    popProductInfoTemp.put("distributionPrice", distributionPrice);
                } else {
                    popProductInfoTemp.put("distributionPrice", "");
                }
                
				//大分类
				popProductInfoTemp.put("primaryCategoryBig",popProductInfo.get("primaryCategoryBig"));
				//中分类
				//popProductInfoTemp.put("primaryCategoryMedium",popProductInfo.get("primaryCategoryMedium"));
				//小分类
				popProductInfoTemp.put("primaryCategorySmall",popProductInfo.get("primaryCategorySmall"));
				
				//白金会员价格
                Object platinumPrice = popProductInfo.get("platinumPrice");
                if(platinumPrice != null) {
                    popProductInfoTemp.put("platinumPrice", platinumPrice);
                } else {
                    popProductInfoTemp.put("platinumPrice", "");
                }
                
				// 吊牌价格
                Object tagPrice = popProductInfo.get("tagPrice");
                if(tagPrice != null) {
                    popProductInfoTemp.put("tagPrice", tagPrice);
                } else {
                    popProductInfoTemp.put("tagPrice", "");
                }
				popProductInfo.put("productInfo", JSONUtil.serialize(popProductInfoTemp));
			}
		}
		
		if(!ignoreSoluId.isEmpty()){
			resultMap.put(CherryConstants.POP_PRMPRODUCT_LIST, ignoreSoluId.isEmpty()? popProductInfoList : ignoreSoluPopProductInfoList);
			resultMap.put("count", ignoreSoluId.isEmpty() ? count : count - ignorePopProductInfoCount);
		}else if (!ignorePrtFunId.isEmpty()){
			resultMap.put(CherryConstants.POP_PRMPRODUCT_LIST, ignorePrtFunId.isEmpty()? popProductInfoList : ignorePrtFunPopProductInfoList);
			resultMap.put("count", ignorePrtFunId.isEmpty() ? count : count - ignorePopProductInfoCount);
		}else{
			resultMap.put(CherryConstants.POP_PRMPRODUCT_LIST, popProductInfoList);
			resultMap.put("count", count);
		}
		
		return resultMap;
	}
	
	/**
	 * 取得产品信息(带图片模式)
	 * @param map
	 * @return
	 */
	public HashMap getProductImageDInfoList (Map<String, Object> map) throws Exception{
		HashMap resultMap = new HashMap();
		List filterList = new ArrayList();
		// 业务日期
		String bussDate = binOLCM02_Service.getBussinessDate(map);
		map.put(CherryConstants.BUSINESS_DATE, bussDate);
		// 需要过滤的字段名
		filterList.add("A.unitCode");
		filterList.add("C.barCode");
		filterList.add("A.OriginalBrand");
		filterList.add("C.validFlag");
		filterList.add("A.nameTotal");
		filterList.add("A.nameForeign");
		filterList.add("D.PropValueChinese");
		filterList.add("D.PropValueForeign");
		filterList.add("E.PropValueChinese");
		filterList.add("E.PropValueForeign");
		filterList.add("B.salePrice");
		map.put(CherryConstants.FILTER_LIST_NAME, filterList);
		// 取得件数
		//int count = binOLCM02_Service.getProductInfoCount(map);
		
		List ignoreSoluPopProductInfoList = new ArrayList(); // 剔除产品方案中的产品，画面上不显示一码多品，对于一码多品的只显示一条
		Map<String,Integer> soluUnitCodeMap = new HashMap<String, Integer>();
		String ignoreSoluId = ConvertUtil.getString(map.get("ignoreSoluId"));
		int ignorePopProductInfoCount = 0;
		
		List ignorePrtFunPopProductInfoList = new ArrayList(); // 剔除产品功能开启时间中的产品，画面上不显示一码多品，对于一码多品的只显示一条
		Map<String,Integer> prtFunUnitCodeMap = new HashMap<String, Integer>();
		String ignorePrtFunId = ConvertUtil.getString(map.get("ignorePrtFunId"));
		
		// 取得产品信息List
		List popProductInfoList = binOLCM02_Service.getProductImageDInfoList(map);
		if(popProductInfoList != null && !popProductInfoList.isEmpty()) {
			for(int i = 0; i < popProductInfoList.size(); i++) {
				Map popProductInfo = (Map)popProductInfoList.get(i);
				Map popProductInfoTemp = new HashMap();
				// 产品厂商编码
				String unitCode = (String)popProductInfo.get("unitCode");
				if(!ignoreSoluId.isEmpty()){
					if(!soluUnitCodeMap.containsKey(unitCode)){
						soluUnitCodeMap.put(unitCode, 1);
						ignoreSoluPopProductInfoList.add(popProductInfo);
					}else{
						ignorePopProductInfoCount++;
					}
				}else if (!ignorePrtFunId.isEmpty()){
					if(!prtFunUnitCodeMap.containsKey(unitCode)){
						prtFunUnitCodeMap.put(unitCode, 1);
						ignorePrtFunPopProductInfoList.add(popProductInfo);
					}else{
						ignorePopProductInfoCount++;
					}
				}
				
				popProductInfoTemp.put("unitCode", popProductInfo.get("unitCode"));
				// 产品条码
				popProductInfoTemp.put("barCode", popProductInfo.get("barCode"));
				// 子品牌 
				String originalBrand = ConvertUtil.getString(popProductInfo.get("originalBrand"));
				popProductInfoTemp.put("originalBrand", CherryChecker.isNullOrEmpty(originalBrand)? originalBrand : code.getVal("1299", originalBrand));
				// 有效区分
				popProductInfoTemp.put("validFlag", popProductInfo.get("validFlag"));
				
				
				// 产品名称
				popProductInfoTemp.put("nameTotal", popProductInfo.get("nameTotal"));
				// 产品价格
				Object salePrice = popProductInfo.get("salePrice");
				if(salePrice != null) {
					popProductInfoTemp.put("salePrice", salePrice);
				} else {
					popProductInfoTemp.put("salePrice", "");
				}
				// 产品厂商ID
				popProductInfoTemp.put("productVendorId", popProductInfo.get("BIN_ProductVendorID"));
				// 产品厂商ID
				popProductInfoTemp.put("proId", popProductInfo.get("BIN_ProductVendorID"));
				// 产品厂商ID
				popProductInfoTemp.put("prtId", popProductInfo.get("bin_ProductID"));
				// 产品结算价格
				Object standardCost = popProductInfo.get("standardCost");
				if(standardCost != null) {
					popProductInfoTemp.put("standardCost", standardCost);
				} else {
					popProductInfoTemp.put("standardCost", "");
				}
				// 产品采购价格
				Object orderPrice = popProductInfo.get("orderPrice");
				if(orderPrice != null) {
					popProductInfoTemp.put("orderPrice", orderPrice);
				} else {
					popProductInfoTemp.put("orderPrice", "");
				}
				// 产品最低销售价
				Object minSalePrice = popProductInfo.get("minSalePrice");
				if(minSalePrice != null) {
					popProductInfoTemp.put("minSalePrice", minSalePrice);
				} else {
					popProductInfoTemp.put("minSalePrice", "");
				}
				// 产品最高销售价
				Object maxSalePrice = popProductInfo.get("maxSalePrice");
				if(minSalePrice != null) {
					popProductInfoTemp.put("maxSalePrice", maxSalePrice);
				} else {
					popProductInfoTemp.put("maxSalePrice", "");
				}
				
				//会员价格
                Object memPrice = popProductInfo.get("memPrice");
                if(memPrice != null) {
                    popProductInfoTemp.put("memPrice", memPrice);
                } else {
                    popProductInfoTemp.put("memPrice", "");
                }
				//大分类
				popProductInfoTemp.put("primaryCategoryBig",popProductInfo.get("primaryCategoryBig"));
				//中分类
				//popProductInfoTemp.put("primaryCategoryMedium",popProductInfo.get("primaryCategoryMedium"));
				//小分类
				popProductInfoTemp.put("primaryCategorySmall",popProductInfo.get("primaryCategorySmall"));
				
				//白金会员价格
                Object platinumPrice = popProductInfo.get("platinumPrice");
                if(platinumPrice != null) {
                    popProductInfoTemp.put("platinumPrice", platinumPrice);
                } else {
                    popProductInfoTemp.put("platinumPrice", "");
                }
                
				// 吊牌价格
                Object tagPrice = popProductInfo.get("tagPrice");
                if(tagPrice != null) {
                    popProductInfoTemp.put("tagPrice", tagPrice);
                } else {
                    popProductInfoTemp.put("tagPrice", "");
                }
				popProductInfo.put("productInfo", JSONUtil.serialize(popProductInfoTemp));
			}
		}
		
		if(!ignoreSoluId.isEmpty()){
			resultMap.put(CherryConstants.POP_PRMPRODUCT_LIST, ignoreSoluId.isEmpty()? popProductInfoList : ignoreSoluPopProductInfoList);
			//resultMap.put("count", ignoreSoluId.isEmpty() ? count : count - ignorePopProductInfoCount);
		}else if (!ignorePrtFunId.isEmpty()){
			resultMap.put(CherryConstants.POP_PRMPRODUCT_LIST, ignorePrtFunId.isEmpty()? popProductInfoList : ignorePrtFunPopProductInfoList);
			//resultMap.put("count", ignorePrtFunId.isEmpty() ? count : count - ignorePopProductInfoCount);
		}else{
			resultMap.put(CherryConstants.POP_PRMPRODUCT_LIST, popProductInfoList);
			//resultMap.put("count", count);
		}
		
		return resultMap;
	}
	
	/**
	 * 取得产品分类信息（大分类）
	 * @param map
	 * @return
	 */
	public List getProductCategoryList(Map<String, Object> map){
		return binOLCM02_Service.getProductCategoryList(map);
	}
	
	/**
	 * 根据产品ItemCode得到对应的库存信息（库存从金蝶实时获取）
	 * @param map
	 * @return
	 */
	public List getInventoryByItemCode(Map<String, Object> map){
		return binOLCM02_Service.getInventoryByItemCode(map);
	}
	
	
	/**
	 * 取得产品信息(薇诺娜)
	 * @param map
	 * @return
	 */
	public HashMap getProductDialogInfoListOne (Map<String, Object> map) throws Exception{
		HashMap resultMap = new HashMap();
		List filterList = new ArrayList();
		// 业务日期
		String bussDate = binOLCM02_Service.getBussinessDate(map);
		map.put(CherryConstants.BUSINESS_DATE, bussDate);
		// 需要过滤的字段名
		filterList.add("C.barCode");
		filterList.add("A.OriginalBrand");
		filterList.add("C.validFlag");
		filterList.add("A.nameTotal");
		filterList.add("A.nameForeign");
		filterList.add("D.PropValueChinese");
		filterList.add("D.PropValueForeign");
		filterList.add("E.PropValueChinese");
		filterList.add("E.PropValueForeign");
		filterList.add("B.salePrice");
		map.put(CherryConstants.FILTER_LIST_NAME, filterList);
		// 取得件数
		int count = binOLCM02_Service.getProductInfoCountOne(map);
		
		List ignoreSoluPopProductInfoList = new ArrayList(); // 剔除产品方案中的产品，画面上不显示一码多品，对于一码多品的只显示一条
		Map<String,Integer> soluUnitCodeMap = new HashMap<String, Integer>();
		String ignoreSoluId = ConvertUtil.getString(map.get("ignoreSoluId"));
		int ignorePopProductInfoCount = 0;
		
		List ignorePrtFunPopProductInfoList = new ArrayList(); // 剔除产品功能开启时间中的产品，画面上不显示一码多品，对于一码多品的只显示一条
		Map<String,Integer> prtFunUnitCodeMap = new HashMap<String, Integer>();
		String ignorePrtFunId = ConvertUtil.getString(map.get("ignorePrtFunId"));
		
		// 取得产品信息List
		List popProductInfoList = binOLCM02_Service.getProductShortInfoListOne(map);
		if(popProductInfoList != null && !popProductInfoList.isEmpty()) {
			for(int i = 0; i < popProductInfoList.size(); i++) {
				Map popProductInfo = (Map)popProductInfoList.get(i);
				Map popProductInfoTemp = new HashMap();
				
				// 产品条码
				popProductInfoTemp.put("barCode", popProductInfo.get("barCode"));
				// 子品牌 
				String originalBrand = ConvertUtil.getString(popProductInfo.get("originalBrand"));
				popProductInfoTemp.put("originalBrand", CherryChecker.isNullOrEmpty(originalBrand)? originalBrand : code.getVal("1299", originalBrand));
				// 有效区分
				popProductInfoTemp.put("validFlag", popProductInfo.get("validFlag"));
								
				// 产品名称
				popProductInfoTemp.put("nameTotal", popProductInfo.get("nameTotal"));
				// 产品价格
				Object salePrice = popProductInfo.get("salePrice");
				if(salePrice != null) {
					popProductInfoTemp.put("salePrice", salePrice);
				} else {
					popProductInfoTemp.put("salePrice", "");
				}
				// 产品厂商ID
				popProductInfoTemp.put("productVendorId", popProductInfo.get("BIN_ProductVendorID"));
				// 产品厂商ID
				popProductInfoTemp.put("proId", popProductInfo.get("BIN_ProductVendorID"));
				// 产品厂商ID
				popProductInfoTemp.put("prtId", popProductInfo.get("bin_ProductID"));
				// 产品结算价格
				Object standardCost = popProductInfo.get("standardCost");
				if(standardCost != null) {
					popProductInfoTemp.put("standardCost", standardCost);
				} else {
					popProductInfoTemp.put("standardCost", "");
				}
				// 产品采购价格
				Object orderPrice = popProductInfo.get("orderPrice");
				if(orderPrice != null) {
					popProductInfoTemp.put("orderPrice", orderPrice);
				} else {
					popProductInfoTemp.put("orderPrice", "");
				}
				// 产品最低销售价
				Object minSalePrice = popProductInfo.get("minSalePrice");
				if(minSalePrice != null) {
					popProductInfoTemp.put("minSalePrice", minSalePrice);
				} else {
					popProductInfoTemp.put("minSalePrice", "");
				}
				// 产品最高销售价
				Object maxSalePrice = popProductInfo.get("maxSalePrice");
				if(minSalePrice != null) {
					popProductInfoTemp.put("maxSalePrice", maxSalePrice);
				} else {
					popProductInfoTemp.put("maxSalePrice", "");
				}
				
				//会员价格
				Object memPrice = popProductInfo.get("memPrice");
				if(memPrice != null) {
					popProductInfoTemp.put("memPrice", memPrice);
				} else {
					popProductInfoTemp.put("memPrice", "");
				}
				//大分类
				popProductInfoTemp.put("primaryCategoryBig",popProductInfo.get("primaryCategoryBig"));
				//中分类
				//popProductInfoTemp.put("primaryCategoryMedium",popProductInfo.get("primaryCategoryMedium"));
				//小分类
				popProductInfoTemp.put("primaryCategorySmall",popProductInfo.get("primaryCategorySmall"));
				
				//白金会员价格
				Object platinumPrice = popProductInfo.get("platinumPrice");
				if(platinumPrice != null) {
					popProductInfoTemp.put("platinumPrice", platinumPrice);
				} else {
					popProductInfoTemp.put("platinumPrice", "");
				}
				
				// 吊牌价格
				Object tagPrice = popProductInfo.get("tagPrice");
				if(tagPrice != null) {
					popProductInfoTemp.put("tagPrice", tagPrice);
				} else {
					popProductInfoTemp.put("tagPrice", "");
				}
				popProductInfo.put("productInfo", JSONUtil.serialize(popProductInfoTemp));
			}
		}
		
		if(!ignoreSoluId.isEmpty()){
			resultMap.put(CherryConstants.POP_PRMPRODUCT_LIST, ignoreSoluId.isEmpty()? popProductInfoList : ignoreSoluPopProductInfoList);
			resultMap.put("count", ignoreSoluId.isEmpty() ? count : count - ignorePopProductInfoCount);
		}else if (!ignorePrtFunId.isEmpty()){
			resultMap.put(CherryConstants.POP_PRMPRODUCT_LIST, ignorePrtFunId.isEmpty()? popProductInfoList : ignorePrtFunPopProductInfoList);
			resultMap.put("count", ignorePrtFunId.isEmpty() ? count : count - ignorePopProductInfoCount);
		}else{
			resultMap.put(CherryConstants.POP_PRMPRODUCT_LIST, popProductInfoList);
			resultMap.put("count", count);
		}
		
		return resultMap;
	}
	
	/**
	 * 取得产品信息
	 * @param map
	 * @return
	 */
	public HashMap getCateDialogInfoList (Map<String, Object> map) throws Exception{
		HashMap resultMap = new HashMap();
		List filterList = new ArrayList();
		// 需要过滤的字段名
		filterList.add("B.PropValue");
		filterList.add("A.PropertyNameChinese");
		filterList.add("A.PropertyNameForeign");
		filterList.add("B.PropValueChinese");
		filterList.add("B.PropValueForeign");
		map.put(CherryConstants.FILTER_LIST_NAME, filterList);
		// 取得件数
		int count = binOLCM02_Service.getCateInfoCount(map);
		// 取得产品信息List
		List popCateInfoList = binOLCM02_Service.getCateInfoList(map);
		if(popCateInfoList != null && !popCateInfoList.isEmpty()) {
			for(int i = 0; i < popCateInfoList.size(); i++) {
				Map popCateInfo = (Map)popCateInfoList.get(i);
				Map popCateInfoTemp = new HashMap();
				// 产品分类值id
				popCateInfoTemp.put("cateId", popCateInfo.get("cateId"));
				// 产品分类名称
				popCateInfoTemp.put("cateName", popCateInfo.get("cateName"));
				// 产品分类标志
				popCateInfoTemp.put("cateFlag", "1");
				popCateInfo.put("cateInfo", JSONUtil.serialize(popCateInfoTemp));
			}
		}
		resultMap.put("popCateInfoList", popCateInfoList);
		resultMap.put("count", count);
		return resultMap;
	}
	/**
	 * 取得会员信息
	 * @param map
	 * @return
	 */
	public HashMap getMemberDialogInfoList (Map<String, Object> map) throws Exception{
		HashMap resultMap = new HashMap();
		List filterList = new ArrayList();
		// 业务日期
		String bussDate = binOLCM02_Service.getBussinessDate(map);
		map.put(CherryConstants.BUSINESS_DATE, bussDate);
		// 需要过滤的字段名
		filterList.add("memCode");
		filterList.add("name");
		filterList.add("mobilePhone");
		filterList.add("departName");
		map.put(CherryConstants.FILTER_LIST_NAME, filterList);
		// 取得件数
		int count = binOLCM02_Service.getMemberInfoCount(map);
		// 取得会员信息List
		List popMemberInfoList = binOLCM02_Service.getMemberInfoList(map);
		if(popMemberInfoList != null && !popMemberInfoList.isEmpty()) {
			for(int i = 0; i < popMemberInfoList.size(); i++) {
				Map popMembertInfo = (Map)popMemberInfoList.get(i);
				Map popMemberInfoTemp = new HashMap();
				// 会员ID
				popMemberInfoTemp.put("memberInfoId", popMembertInfo.get("memberInfoId"));
				// 会员卡号
				popMemberInfoTemp.put("memCode", popMembertInfo.get("memCode"));
				// 会员名称
				popMemberInfoTemp.put("memName", popMembertInfo.get("memName"));
				// 会员手机号
				popMemberInfoTemp.put("mobilePhone", popMembertInfo.get("mobilePhone"));
				// 所属部门
				popMemberInfoTemp.put("departName", popMembertInfo.get("departName"));
				popMembertInfo.put("memberInfo", JSONUtil.serialize(popMemberInfoTemp));
			}
		}
		//手机号码解密
		getMemDecryptData(popMemberInfoList,map);
		resultMap.put(CherryConstants.POP_MEMBER_LIST, popMemberInfoList);
		resultMap.put("count", count);
		return resultMap;
	}
	/**
	 * 会员手机号码解密
	 * @param memberList
	 * @param map
	 * @throws Exception
	 */
	public void getMemDecryptData(List<Map<String, Object>> memberList,Map<String, Object> map) throws Exception{
		if (memberList != null && !memberList.isEmpty()) {
			//品牌Code
			String brandCode = ConvertUtil.getString(map.get(CherryConstants.BRAND_CODE));
			for (Map<String, Object> memberMap : memberList) {
				// 会员【手机号】字段解密
				String mobilePhone = ConvertUtil.getString(memberMap.get("mobilePhone"));
				if (!CherryChecker.isNullOrEmpty(mobilePhone)) {
					memberMap.put("mobilePhone", CherrySecret.decryptData(brandCode,mobilePhone));
				}
			}
		}
	}
	
	/**
	 * 取得对象批次信息
	 * @param map
	 * @return
	 */
	public HashMap getObjBatchInfoList (Map<String, Object> map) throws Exception{
		HashMap resultMap = new HashMap();
		List filterList = new ArrayList();
		// 业务日期
		String bussDate = binOLCM02_Service.getBussinessDate(map);
		map.put(CherryConstants.BUSINESS_DATE, bussDate);
		// 需要过滤的字段名
		filterList.add("BIN_SearchCode");
		filterList.add("recordName");
		filterList.add("comments");
		map.put(CherryConstants.FILTER_LIST_NAME, filterList);
		// 取得件数
		int count = binOLCM02_Service.getObjBatchCount(map);
		// 取得对象批次信息List
		List popObjBatchInfoList = binOLCM02_Service.getObjBatchInfoList(map);
		if(popObjBatchInfoList != null && !popObjBatchInfoList.isEmpty()) {
			for(int i = 0; i < popObjBatchInfoList.size(); i++) {
				Map popMembertInfo = (Map)popObjBatchInfoList.get(i);
				Map popObjBatchInfoTemp = new HashMap();
				// 会员ID
				popObjBatchInfoTemp.put("searchCode", popMembertInfo.get("searchCode"));
				// 会员卡号
				popObjBatchInfoTemp.put("recordName", popMembertInfo.get("recordName"));
				// 会员名称
				popObjBatchInfoTemp.put("comments", popMembertInfo.get("comments"));
				popMembertInfo.put("objBatchInfo", JSONUtil.serialize(popObjBatchInfoTemp));
			}
		}
		resultMap.put(CherryConstants.POP_OBJBATCH_LIST, popObjBatchInfoList);
		resultMap.put("count", count);
		return resultMap;
	}
	
	/**
	 * 设定组织部门类型
	 * @param map
	 */
	public void setType (Map<String, Object> map){
		// 所属部门类型
		String departType =(String)map.get("departType");
		if ("0".equals(departType) || "1".equals(departType)){
			// 总部类型部门
			map.put("orgPriceType","0");
		}else{
			// 非总部类型部门
			map.put("orgPriceType","1");
		}
	}
	
	/**
	 * 取得柜台总数
	 * 
	 * @param map 查询条件
	 * @return 返回柜台总数
	 */
	public int getCounterInfoCount(Map<String, Object> map) {
		
		// 取得柜台总数
		return binOLCM02_Service.getCounterInfoCount(map);
	}
	
	/**
	 * 取得柜台List
	 * 
	 * @param map 查询条件
	 * @return 柜台信息List
	 */
	public List<Map<String, Object>> getCounterInfoList(Map<String, Object> map) {
		
		// 取得柜台List
		return binOLCM02_Service.getCounterInfoList(map);
	}
	
	/**
	 * 取得子品牌总数
	 * 
	 * @param map 查询条件
	 * @return 返回子品牌总数
	 */
	public int getOrigBrandCount(Map<String, Object> map) {
		
		// 取得子品牌总数
		return binOLCM02_Service.getOrigBrandCount(map);
	}
	
	/**
	 * 取得子品牌List
	 * 
	 * @param map 查询条件
	 * @return 子品牌信息List
	 */
	public List<Map<String, Object>> getOrigBrandList(Map<String, Object> map) {
		
		// 取得子品牌List
		return binOLCM02_Service.getOrigBrandList(map);
	}
	
	/**
	 * 根据柜台ID取得柜台信息
	 * 
	 * @param map 查询条件
	 * @return 柜台信息
	 */
	public Map<String, Object> getCounterInfo(Map<String, Object> map) {
		
		// 根据柜台ID取得柜台信息
		return binOLCM02_Service.getCounterInfo(map);
	}
	
	
	/**
	 * 取得厂商List
	 * 
	 * @param map 查询条件
	 * @return 厂商信息List
	 */
	public List getFactoryList(Map<String, Object> map) {
		
		// 取得厂商List
		return binOLCM02_Service.getFactoryList(map);
	}
	
	/**
	 * 查询某一区域的所有上级区域
	 * 
	 * @param map 查询条件
	 * @return 区域List
	 */
	public List getHigherRegionList(Map<String, Object> map) {
		
		// 查询某一区域的所有上级区域
		return binOLCM02_Service.getHigherRegionList(map);
	}
	
	/**
	 * 取得部门总数
	 * 
	 * @param map 查询条件
	 * @return 返回部门总数
	 */
	public int getDepartInfoCount(Map<String, Object> map) {
		
		// 取得部门总数
		return binOLCM02_Service.getDepartInfoCount(map);
	}
	
	/**
	 * 取得部门List
	 * 
	 * @param map 查询条件
	 * @return 部门List
	 */
	public List getDepartInfoList(Map<String, Object> map) {
		
		// 取得部门List
		return binOLCM02_Service.getDepartInfoList(map);
	}
	
	/**
	 * 取得员工总数
	 * 
	 * @param map 查询条件
	 * @return 返回员工总数
	 */
	public int getEmployeeCount(Map<String, Object> map) {
		
		// 取得员工总数
		return binOLCM02_Service.getEmployeeCount(map);
	}
	
	/**
	 * 取得员工List
	 * 
	 * @param map 查询条件
	 * @return 员工List
	 */
	public List getEmployeeList(Map<String, Object> map) {
		// 语言类型
		String language = (String)map.get(CherryConstants.SESSION_LANGUAGE);
		
		// 取得员工信息List（不 含员工部门、岗位信息）
		List<Map<String, Object>> list1 = binOLCM02_Service.getEmployeeList(map);
		for (Map<String, Object> empMap1 : list1) {
			// 员工ID
			String employeeId = ConvertUtil.getString(empMap1.get("employeeId"));
			
			// 取得员工部门、岗位信息List
			List<Map<String, Object>> list2 = binOLCM02_Service.getEmpDptPstList(employeeId,language);
			empMap1.put("dpList", list2);
		}
		return list1;
	}
	
	/**
	 * 取得考核问卷总数
	 * @param map
	 * @return
	 */
	public int getCheckPaperCount (Map<String, Object> map){
		
		// 取得考核问卷总数
		return binOLCM02_Service.getCheckPaperCount(map);
	}
	
	/**
	 * 取得考核问卷List
	 * @param map
	 * @return
	 */
	public List getCheckPaperList(Map<String, Object> map){
		
		// 取得考核问卷List
		return binOLCM02_Service.getCheckPaperList(map);
	}
	
	/**
	 * 取得问卷总数
	 * @param map
	 * @return
	 */
	public int getPaperCount (Map<String, Object> map){
		
		// 取得考核问卷总数
		return binOLCM02_Service.getPaperCount(map);
	}
	
	/**
	 * 取得问卷List
	 * @param map
	 * @return
	 */
	public List getPaperList(Map<String, Object> map){
		
		// 取得考核问卷List
		return binOLCM02_Service.getPaperList(map);
	}
	/**
	 * 取得产品分类List
	 * @param map
	 * @return
	 */
	public List getPrtCategoryList(Map<String, Object> map){
		return binOLCM02_Service.getPrtCategoryList(map);
	}
	
	/**
	 * 取得产品分类值List
	 * @param map
	 * @return
	 * @throws JSONException 
	 */
	public List<Map<String, Object>> getCateValList(Map<String, Object> map) throws JSONException{
		List<Map<String, Object>> list = binOLCM02_Service.getCateValList(map);
		if(null != list){
			for(Map<String, Object> temp : list){
				temp.remove("RowNumber");
				temp.put("cateValInfo", JSONUtil.serialize(temp));
			}
		}
		return list;
	}
	
	/**
	 * 取得产品分类值总数
	 * @param map
	 * @return
	 */
	public int getCateValCount (Map<String, Object> map){
		return binOLCM02_Service.getCateValCount(map);
	}
	
	/**
	 * 取得产品分类值List
	 * @param map
	 * @return
	 * @throws JSONException 
	 */
	public List<Map<String, Object>> getPrmCateList(Map<String, Object> map) throws JSONException{
		List<Map<String, Object>> list = binOLCM02_Service.getPrmCateList(map);
		if(null != list){
			for(Map<String, Object> temp : list){
				temp.remove("RowNumber");
				temp.put("prmCateInfo", JSONUtil.serialize(temp));
			}
		}
		return list;
	}
	
	/**
	 * 取得产品分类值总数
	 * @param map
	 * @return
	 */
	public int getPrmCateCount (Map<String, Object> map){
		return binOLCM02_Service.getPrmCateCount(map);
	}
	
	/**
	 * 查询员工信息List
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getEmployeeInfoList(Map<String, Object> map){
		return binOLCM02_Service.getEmployeeInfoList(map);
	}
	
	/**
	 * 查询员工信息总数
	 * @param map
	 * @return
	 */
	public int getEmployeeInfoCount (Map<String, Object> map){
		return binOLCM02_Service.getEmployeeInfoCount(map);
	}
	
	/**
	 * 查询人员信息List
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getUserInfoList(Map<String, Object> map){
		return binOLCM02_Service.getUserInfoList(map);
	}
	
	/**
	 * 查询人员信息总数
	 * @param map
	 * @return
	 */
	public int getUserInfoCount (Map<String, Object> map){
		return binOLCM02_Service.getUserInfoCount(map);
	}
	
	/**
	 * 取得所有部门List
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getAllDepartInfoList(Map<String, Object> map){
		return binOLCM02_Service.getAllDepartInfoList(map);
	}
	
	/**
	 * 取得所有部门总数
	 * @param map
	 * @return
	 */
	public int getAllDepartInfoCount (Map<String, Object> map){
		return binOLCM02_Service.getAllDepartInfoCount(map);
	}
	
	/**
	 * 查询区域信息List
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getRegionList(Map<String, Object> map){
		
		// 查询区域信息List
		List<Map<String, Object>> regionList = binOLCM02_Service.getRegionList(map);
		List<String[]> keysList = new ArrayList<String[]>();
		String[] keys1 = { "regionId", "regionName" };
		String[] keys2 = { "provinceId", "provinceName" };
		String[] keys3 = { "cityId", "cityName" };
		String[] keys4 = { "counterId", "counterName" };
		keysList.add(keys1);
		keysList.add(keys2);
		keysList.add(keys3);
		keysList.add(keys4);
		List<Map<String, Object>> regionTreeList = new ArrayList<Map<String, Object>>();
		// 把线性的结构转化为树结构
		ConvertUtil.jsTreeDataDeepList(regionList, regionTreeList, keysList, 0);
		return regionTreeList;
	}
	
	/**
	 * 查询大区信息List
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getRegionIdList(Map<String, Object> map){
		
		// 查询大区信息List
		return binOLCM02_Service.getRegionIdList(map);
	}
	
	/**
	 * 查询渠道信息List
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getChannelList(Map<String, Object> map){
		
		// 查询区域信息List
		List<Map<String, Object>> channelList = binOLCM02_Service.getChannelList(map);
		List<String[]> keysList = new ArrayList<String[]>();
		String[] keys1 = { "channelId", "channelName" };
		String[] keys2 = { "organizationId", "departName" };
		keysList.add(keys1);
		keysList.add(keys2);
		List<Map<String, Object>> channelTreeList = new ArrayList<Map<String, Object>>();
		// 把线性的结构转化为树结构
		ConvertUtil.jsTreeDataDeepList(channelList, channelTreeList, keysList, 0);
		return channelTreeList;
	}
	
	/**
	 * 查询部门信息List
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getDepartList(Map<String, Object> map){
		// 查询部门信息List
		List<Map<String, Object>> departList = binOLCM02_Service.getDepartList(map);
		departList = ConvertUtil.getTreeList(departList, "nodes");
		ConvertUtil.cleanTreeList(departList);
		return departList;
	}
	
	/**
	 * 查询系统柜台List
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getBelongCounterList(Map<String, Object> map){
		return binOLCM02_Service.getBelongCounterList(map);
	}
	
	/**
	 * 查询定位到的区域ID
	 * @param map
	 * @return
	 */
	public String getLocationRegionId(Map<String, Object> map){
		// 查询定位到的区域ID
		return binOLCM02_Service.getLocationRegionId(map);
	}
	
	/**
	 * 取得领用柜台Code
	 * 
	 * @param map
	 * @return
	 */
	public String getCounterCode(Map<String, Object> map) {
		List<Map<String, Object>> resultList = binOLCM02_Service.getCounterCode(map);
		return this.getString(map, resultList);
	}
	
	/**
	 * 根据输入字符串模糊查询领用柜台Code
	 * 
	 * @param map
	 * @param list
	 * @return
	 */
	private String getString(Map<String, Object> map,
			List<Map<String, Object>> list) {
		StringBuffer sb = new StringBuffer();
		sb.append(CampConstants.ALL_COUNTERCODE);
		sb.append("|");
		sb.append(CampConstants.ALL_COUNTER_ZH_CN);
		sb.append("|");
		for (int i = 0; i < list.size(); i++) {
			Map<String, Object> tempMap = list.get(i);
			sb.append((String) tempMap.get("code"));
			sb.append("|");
			sb.append((String) tempMap.get("name"));
			sb.append("|");
			sb.append(String.valueOf(tempMap.get("id")));
			if (i != list.size()) {
				sb.append("\n");
			}
		}
		return sb.toString();
	}
	
	/**
	 * 查询会员活动信息总数
	 * 
	 * @param map 查询条件
	 * @return 会员活动信息总数
	 */
	public int getMemCampaignCount (Map<String, Object> map){
		// 查询会员活动信息总数
		return binOLCM02_Service.getMemCampaignCount(map);
	}
	
	/**
	 * 查询会员活动信息List
	 * 
	 * @param map 查询条件
	 * @return 会员活动信息List
	 */
	public List<Map<String, Object>> getMemCampaignList(Map<String, Object> map){
		// 查询会员活动信息List
		return binOLCM02_Service.getMemCampaignList(map);
	}
	
	/**
	 * 查询促销活动信息总数
	 * 
	 * @param map 查询条件
	 * @return 促销活动信息总数
	 */
	public int getPrmCampaignCount (Map<String, Object> map){
		// 查询会员活动信息总数
		return binOLCM02_Service.getPrmCampaignCount(map);
	}
	
	/**
	 * 查询促销活动信息List
	 * 
	 * @param map 查询条件
	 * @return 促销活动信息List
	 */
	public List<Map<String, Object>> getPrmCampaignList(Map<String, Object> map){
		// 查询促销活动信息List
		return binOLCM02_Service.getPrmCampaignList(map);
	}
	
	/**
	 * 取得所有部门层级结构信息List
	 * 
	 * @param map 查询条件
	 * @return 所有部门层级结构信息List
	 */
	public List<Map<String, Object>> getAllOrgList(Map<String, Object> map){
		// 取得所有部门信息List
		List<Map<String, Object>> allOrgList = binOLCM02_Service.getAllOrgList(map);
		allOrgList = ConvertUtil.getTreeList(allOrgList, "nodes");
		return allOrgList;
	}
	
	/**
	 * 取得对象批次名称
	 * @param map
	 * @return
	 */
	public Map<String, Object> getSearchMemInfo(Map<String, Object> map){
		// 取得对象批次名称
		return binOLCM02_Service.getSearchMemInfo(map);
	}
	
	/**
	 * 查询销售总数
	 * 
	 * @param map 查询条件
	 * @return 销售总数
	 */
	public int getSaleRecordCount (Map<String, Object> map){
		// 查询会员活动信息总数
		return binOLCM02_Service.getSaleRecordCount(map);
	}
	
	/**
	 * 查询销售信息List
	 * 
	 * @param map 查询条件
	 * @return 销售信息List
	 */
	public List<Map<String, Object>> getSaleRecordList(Map<String, Object> map){
		// 查询促销活动信息List
		return binOLCM02_Service.getSaleRecordList(map);
	}
	
	/**
	 * 查询销售总数
	 * 
	 * @param map 查询条件
	 * @return 销售总数
	 */
	public int getCouponCount (Map<String, Object> map){
		// 查询会员活动信息总数
		return binOLCM02_Service.getCouponCount(map);
	}
	
	/**
	 * 查询经销商总数
	 * @param map
	 * @return
	 */
	public int getResellerCount (Map<String, Object> map){
		List<String> filterList = new ArrayList<String>();
		filterList.add("A.ResellerCode");
		filterList.add("A.ResellerName");
		map.put("filterList", filterList);
		return binOLCM02_Service.getResellerCount(map);
	}
	
	/**
	 * 查询经销商List
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getResellerList(Map<String, Object> map){
		List<String> filterList = new ArrayList<String>();
		filterList.add("A.ResellerCode");
		filterList.add("A.ResellerName");
		return binOLCM02_Service.getResellerList(map);
	}
	
	/**
	 * 将手机号加密后放到天猫加密手机号里
	 * @param map
	 */
	public void addTmallMixMobile(Map map,String mobilePhone, int operateFlag){
		boolean  isOpen = binOLCM14_BL.isConfigOpen("1379", ConvertUtil.getString(map.get("organizationInfoId") == null ? map.get("organizationInfoID") : map.get("organizationInfoId")), ConvertUtil.getString(map.get("brandInfoId") == null ? map.get("brandInfoID") : map.get("brandInfoId")));
		if (isOpen) {
			//新增会员信息时
			if(1 == operateFlag){
				addTmallMixMobileToMap(map,mobilePhone);
				map.put("isSameMobile", 1);
				map.put("bindFlag", 1);
				map.put("sourceFlag", 1);
			//更新会员信息时
			}else {
				if(!StringUtils.isEmpty(ConvertUtil.getString(map.get("memberInfoID")))){
					map.put("memberInfoId", map.get("memberInfoID"));
				}
				Map<String, Object> mobilePhoneAndFlagMap = binOLMBMBM11_Service.getMemberPhoneAndBindFlag(map);
				if(mobilePhoneAndFlagMap != null && !mobilePhoneAndFlagMap.isEmpty()){
					
					String newMobilePhone = ConvertUtil.getString(map.get(mobilePhone));
					String oldMobilePhone = ConvertUtil.getString(mobilePhoneAndFlagMap.get("mobilePhone"));
					
					if(!newMobilePhone.equals(oldMobilePhone)){
						if (!"2".equals(mobilePhoneAndFlagMap.get("bindFlag"))) {
							addTmallMixMobileToMap(map,mobilePhone);
							map.put("isSameMobile", 1);
							//如果是绑定会员,不更新天猫加密手机号
						}else{
							map.put("isSameMobile", 2);
						}
					}				
					
				}
			}
		}		
	}
	
	private void addTmallMixMobileToMap(Map map,String mobilePhone) {	
		try {
			String mobile = CherrySecret.decryptData(ConvertUtil.getString(map.get("brandCode")),(String)map.get(mobilePhone));
			String key = binOLCM05_Service.getTmMixKey(Integer.valueOf(ConvertUtil.getString(map.get("brandInfoId") == null ? map.get("brandInfoID") : map.get("brandInfoId"))));
			map.put("tmallMixMobile", DigestUtils.md5Hex(DigestUtils.md5Hex("tmall" + mobile + key)));
		} catch (Exception e) {			
			e.printStackTrace();
		}				
	}

	
	/**
	 * 查询品牌List
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getBrandCodeListSrh(Map<String, Object> map) {
		return binOLCM02_Service. getBrandCodeListSrh(map);
	}
	
	/**
	 * 查询分类List
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getSortListSrh(Map<String, Object> map) {
		return binOLCM02_Service. getSortListSrh(map);
	}

	/**
	 * 查询活动对象为搜索结果的活动总数
	 *
	 * @param map 查询条件
	 * @return 活动对象为搜索结果的活动总数
	 */
	public int getCampObjCount(Map<String, Object> map) {

		// 取得子品牌总数
		return binOLCM02_Service.getCampObjCount(map);
	}

	/**
	 * 查询活动对象为搜索结果的活动List
	 *
	 * @param map 查询条件
	 * @return 活动对象为搜索结果的活动List
	 */
	public List<Map<String, Object>> getCampObjList(Map<String, Object> map) {
		return binOLCM02_Service.getCampObjList(map);
	}
}
