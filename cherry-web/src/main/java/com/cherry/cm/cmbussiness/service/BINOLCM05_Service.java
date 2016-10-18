/*
 * @(#)BINOLCM05_Service.java     1.0 2010/11/19
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
package com.cherry.cm.cmbussiness.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 促销品共通 Service
 * 
 * @author hub
 * @version 1.0 2010.11.19
 */
@SuppressWarnings("unchecked")
public class BINOLCM05_Service extends BaseService{
	
	/**
	 * 取得所管辖的品牌List
	 * 
	 * @param Map
	 *			查询条件
	 * @return List
	 *			所管辖的品牌List
	 */
	public List getBrandInfoList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
//		// 用户ID
//		paramMap.put(CherryConstants.USERID, map.get(CherryConstants.USERID));
//		// 业务类型
//		paramMap.put(CherryConstants.BUSINESS_TYPE, map
//				.get(CherryConstants.BUSINESS_TYPE));
//		// 操作类型
//		paramMap.put(CherryConstants.OPERATION_TYPE, map
//				.get(CherryConstants.OPERATION_TYPE));
//		// 语言类型
//		paramMap.put(CherryConstants.SESSION_LANGUAGE, map
//				.get(CherryConstants.SESSION_LANGUAGE));
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM05.getBrandInfoList");
		return baseServiceImpl.getList(paramMap);
	}
	
	/**
	 * 取得品牌List
	 * 
	 * @param map 查询条件
	 * @return 品牌List
	 */
	public List<Map<String, Object>> getBrandInfoShowList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM05.getBrandInfoShowList");
		return baseServiceImpl.getList(paramMap);
	}
	
	/**
	 * 取得品牌名称
	 * 
	 * @param map 
	 * 				查询条件
	 * @return String
	 * 				品牌名称
	 */
	public String getBrandName(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM05.getBrandName");
		return (String)baseServiceImpl.get(parameterMap);
	}
	
	/**
	 * 取得会员俱乐部名称
	 * 
	 * @param map 
	 * 				查询条件
	 * @return String
	 * 				会员俱乐部名称
	 */
	public String getClubName(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM05.getCM05ClubName");
		return (String)baseServiceImpl.get(parameterMap);
	}
	
	/**
	 * 取得品牌代码
	 * 
	 * @param map 
	 *
	 * @return String
	 * 
	 */
	public String getBrandCode(int brandInfoId) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM05.getBrandCode");
		paramMap.put(CherryConstants.BRANDINFOID, brandInfoId);
		return (String)baseServiceImpl.get(paramMap);
	}
	
	/**
	 * 取得天猫加密秘钥
	 * 
	 * @param map 
	 *
	 * @return String
	 * 
	 */
	public String getTmMixKey(int brandInfoId) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM05.getTmMixKey");
		paramMap.put(CherryConstants.BRANDINFOID, brandInfoId);
		return (String)baseServiceImpl.get(paramMap);
	}
	
	/**
	 * 取得品牌末尾码
	 * 
	 * @param map 
	 *
	 * @return String
	 * 
	 */
	public String getBrandLastCode(int brandInfoId) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM05.getBrandLastCode");
		paramMap.put(CherryConstants.BRANDINFOID, brandInfoId);
		return (String)baseServiceImpl.get(paramMap);
	}
	
	/**
	 * 根据品牌Code和组织ID取得品牌ID
	 * 
	 * 
	 * */
	public int getBrandInfoId(Map<String,Object> map){
		return (Integer)baseServiceImpl.get(map,"BINOLCM05.getBrandInfoId");
	}
	
	/**
	 * 取得促销产品类型List
	 * 
	 * @param Map
	 *			查询条件
	 * @return List
	 *			促销产品类型List
	 */
	public List getPromPrtCateList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		// 组织信息ID
		paramMap.put(CherryConstants.ORGANIZATIONINFOID, map
				.get(CherryConstants.ORGANIZATIONINFOID));
		// 品牌信息ID
		paramMap.put(CherryConstants.BRANDINFOID, map
				.get(CherryConstants.BRANDINFOID));
		// 语言类型
		paramMap.put(CherryConstants.SESSION_LANGUAGE, map
				.get(CherryConstants.SESSION_LANGUAGE));
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM05.getPromPrtCateList");
		return baseServiceImpl.getList(paramMap);
	}
	
	/**
	 * 取得产品类型List
	 * 
	 * @param Map
	 *			查询条件
	 * @return List
	 *			产品类型List
	 */
	public List getPrtCateList(Map<String, Object> map) {
		
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM05.getPrtCateList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 取得大分类List
	 * 
	 * @param Map
	 *			查询条件
	 * @return List
	 *			大分类List
	 */
	public List getPrimaryCateList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		// 组织信息ID
		paramMap.put(CherryConstants.ORGANIZATIONINFOID, map
				.get(CherryConstants.ORGANIZATIONINFOID));
		// 品牌信息ID
		paramMap.put(CherryConstants.BRANDINFOID, map
				.get(CherryConstants.BRANDINFOID));
		// 语言类型
		paramMap.put(CherryConstants.SESSION_LANGUAGE, map
				.get(CherryConstants.SESSION_LANGUAGE));
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM05.getPrimaryCateList");
		return baseServiceImpl.getList(paramMap);
	}
	
	/**
	 * 取得中分类List
	 * 
	 * @param Map
	 *			查询条件
	 * @return List
	 *			中分类List
	 */
	public List getSecondCateList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		// 组织信息ID
		paramMap.put(CherryConstants.ORGANIZATIONINFOID, map
				.get(CherryConstants.ORGANIZATIONINFOID));
		// 品牌信息ID
		paramMap.put(CherryConstants.BRANDINFOID, map
				.get(CherryConstants.BRANDINFOID));
		// 语言类型
		paramMap.put(CherryConstants.SESSION_LANGUAGE, map
				.get(CherryConstants.SESSION_LANGUAGE));
		// 大分类代码
		paramMap.put("primaryCateCode", map.get("primaryCateCode"));
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM05.getSecondCateList");
		return baseServiceImpl.getList(paramMap);
	}
	
	/**
	 * 取得小分类List
	 * 
	 * @param Map
	 *			查询条件
	 * @return List
	 *			小分类List
	 */
	public List getSmallCateList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		// 组织信息ID
		paramMap.put(CherryConstants.ORGANIZATIONINFOID, map
				.get(CherryConstants.ORGANIZATIONINFOID));
		// 品牌信息ID
		paramMap.put(CherryConstants.BRANDINFOID, map
				.get(CherryConstants.BRANDINFOID));
		// 语言类型
		paramMap.put(CherryConstants.SESSION_LANGUAGE, map
				.get(CherryConstants.SESSION_LANGUAGE));
		// 大分类代码
		paramMap.put("primaryCateCode", map.get("primaryCateCode"));
		// 中分类代码
		paramMap.put("secondCateCode", map.get("secondCateCode"));
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM05.getSmallCateList");
		return baseServiceImpl.getList(paramMap);
	}
	
	/**
	 * 取得最小包装类型List
	 * 
	 * @param Map
	 *			查询条件
	 * @return List
	 *			最小包装类型List
	 */
	public List getMinPackageTypeList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		// 组织信息ID
		paramMap.put(CherryConstants.ORGANIZATIONINFOID, map
				.get(CherryConstants.ORGANIZATIONINFOID));
		// 品牌信息ID
		paramMap.put(CherryConstants.BRANDINFOID, map
				.get(CherryConstants.BRANDINFOID));
		// 语言类型
		paramMap.put(CherryConstants.SESSION_LANGUAGE, map
				.get(CherryConstants.SESSION_LANGUAGE));
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM05.getMinPackageTypeList");
		return baseServiceImpl.getList(paramMap);
	}
	
	/**
	 * 取得促销产品分类ID
	 * 
	 * @param Map
	 *			查询条件
	 * @return Map
	 *			促销产品分类ID
	 */
	public Map getPromPrtTypeId(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		// 组织信息ID
		paramMap.put(CherryConstants.ORGANIZATIONINFOID, map
				.get(CherryConstants.ORGANIZATIONINFOID));
		// 品牌信息ID
		paramMap.put(CherryConstants.BRANDINFOID, map
				.get(CherryConstants.BRANDINFOID));
		// 大分类
		paramMap.put("primaryCateCode", map
				.get("primaryCateCode"));
		// 中分类
		paramMap.put("secondCateCode", map
				.get("secondCateCode"));
		// 小分类
		paramMap.put("smallCateCode", map
				.get("smallCateCode"));
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM05.getPromPrtTypeId");
		return (Map) baseServiceImpl.get(paramMap);
	}
	
	/**
	 * 取得默认显示的生产厂商信息
	 * 
	 * @param Map
	 *			查询条件
	 * @return Map
	 *			默认显示的生产厂商信息
	 */
	public Map getFactoryInfo(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		// 组织信息ID
		paramMap.put(CherryConstants.ORGANIZATIONINFOID, map
				.get(CherryConstants.ORGANIZATIONINFOID));
		// 品牌信息ID
		paramMap.put(CherryConstants.BRANDINFOID, map
				.get(CherryConstants.BRANDINFOID));
		// 语言类型
		paramMap.put(CherryConstants.SESSION_LANGUAGE, map
				.get(CherryConstants.SESSION_LANGUAGE));
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM05.getFactoryList");
		List factoryList = baseServiceImpl.getList(paramMap);
		Map resultMap = null;
		if (factoryList != null && !factoryList.isEmpty()) {
			resultMap = (Map) factoryList.get(0);
		}
		return resultMap;
	}
	
	/**
	 * 验证是否存在同样的促销品分类ID
	 * 
	 * @param map 查询条件
	 * @return 促销品分类ID
	 */
	public boolean getPrmTypeIdCheck(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM05.getPrmTypeIdCheck");
		int count =  baseServiceImpl.getSum(map);
		return count > 0 ? false : true;
	}
	
	/**
	 * 验证厂商编码是否已经存在
	 * 
	 * @param map 查询条件
	 * @return 促销品ID
	 */
	public boolean checkUnitCode(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM05.getUnitCodeCount");
		int count = baseServiceImpl.getSum(map);
		return count > 0 ? false : true;
	}
	
	/**
	 * 验证厂商编码是否已经存在于产品表中
	 * 
	 * @param map 查询条件
	 * @return 
	 */
	public boolean checkUnitCode2(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM05.getUnitCodeCount2");
		int count = baseServiceImpl.getSum(map);
		return count > 0 ? false : true;
	}
	
	/**
	 * 验证厂商编码促销品条码是否已经存在
	 * 
	 * @param map 查询条件
	 * @return 促销品ID
	 */
	public boolean checkUnitCodeBarCode(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM05.getUnitCodeBarCodeList");
		List promPrtIdList = baseServiceImpl.getList(map);
		if (null != promPrtIdList && !promPrtIdList.isEmpty()) {
			if (promPrtIdList.size() > 1) {
				return false;
			} else {
				// 促销产品ID
				String promotionProId = (String) map.get("promotionProId");
				// 已经存在的促销产品ID
				String promProId = String.valueOf(((Map) promPrtIdList.get(0)).get("promotionProductId"));
				if (null == promotionProId || promotionProId.isEmpty() 
						|| !promotionProId.equals(promProId)){
					return false;
				}
			}
		}
		return true;
	}
	
	/**
	 * 取得产品ID(根据barcode)
	 * @param map
	 * @return
	 */
	public List<Integer> getProductIdByBarCode(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM05.getProductIdByBarCode");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 取得促销产品ID(根据barcode)
	 * 
	 * @param map
	 * @return List
	 */
	@SuppressWarnings("unchecked")
	public List<Integer> getPromotionIdByBarCode(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM05.getPromotionIdByBarCode");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 取得产品信息(根据barcode)
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getProductListByBarCode(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM05.getProductListByBarCode");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 取得产品云及产品方案中的产品信息(根据barcode)
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getProductListByBarCodeForSolu(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM05.getProductListByBarCodeForSolu");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 取得有效的促销品/产品件数
	 * 
	 * @param map
	 * @return List
	 */
	public int getValidCount(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM05.getValidCount");
		return baseServiceImpl.getSum(map);
	}
	
	/**
	 * 根据unitCode查询产品/促销品的当前及历史编码条码关系表中是否已存在
	 * 
	 * @param map
	 * @return List
	 */
	public int getExistUnitCodeForPrtAndProm(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM05.getExistUnitCodeForPrtAndProm");
		return baseServiceImpl.getSum(map);
	}
	
	/**
	 * 取得会员俱乐部列表
	 * 
	 * @param Map
	 *			查询条件
	 * @return List
	 *			会员俱乐部列表
	 */
	public List<Map<String, Object>> getClubList(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM05.getCM05ClubList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 取得会员已经拥有的俱乐部列表
	 * 
	 * @param Map
	 *			查询条件
	 * @return List
	 *			会员已经拥有的俱乐部列表
	 */
	public List<Map<String, Object>> getMemClubList(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM05.getCM05MemClubList");
		return baseServiceImpl.getList(map);
	}
}
