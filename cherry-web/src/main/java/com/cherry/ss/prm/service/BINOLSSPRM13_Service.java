/*		
 * @(#)BINOLSSPRM13_Service.java     1.0 2010/10/27		
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
package com.cherry.ss.prm.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;
import com.cherry.cm.util.ConvertUtil;

@SuppressWarnings("unchecked")
public class BINOLSSPRM13_Service extends BaseService{
	
	public List getActiveGrpList(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM13.getActiveGrpList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 取得品牌信息
	 * @param map
	 * @return
	 */
	public List getBrandInfoList (Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM13.getBrandInfoList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 取得品牌信息
	 * @param map
	 * @return
	 */
	public HashMap getBrandCode (Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM13.getBrandCode");
		return (HashMap)baseServiceImpl.get(map);
	}
	
	/**
	 * 取得区域信息
	 * @param map
	 * @return
	 */
	public List getRegionInfoList (Map<String, Object> map){
		map.put("businessType", "1");
		map.put("operationType", "1");
		map.put("userId", map.get("userID"));
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM13.getRegionInfoList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 取得柜台信息
	 * @param map
	 * @return
	 */
	public List<Map<String,Object>> getCntInfoList(Map<String, Object> map){
		map.put("businessType", "1");
		map.put("operationType", "1");
		map.put("userId", map.get("userID"));
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM13.getCntInfoList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 取得柜台信息
	 * @param map
	 * @return
	 */
	public List<Map<String,Object>> getExCntInfoList(Map<String, Object> map){
		map.put("businessType", "1");
		map.put("operationType", "1");
		map.put("userId", map.get("userID"));
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM13.getExCntInfoList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 取得柜台信息
	 * @param map
	 * @return
	 */
	public List getCounterInfoList(Map<String, Object> map){
		map.put("businessType", "1");
		map.put("operationType", "1");
		map.put("userId", map.get("userID"));
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM13.getCounterInfoList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 取得渠道信息
	 * @param map
	 * @return
	 */
	public List getChannelInfoList (Map<String,Object> map){
		map.put("businessType", "1");
		map.put("operationType", "1");
		map.put("userId", map.get("userID"));
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM13.getChannelInfoList");
		return baseServiceImpl.getList(map);
	}
	
//	/**
//	 * 取得产品总数
//	 * @param map
//	 * @return
//	 */
//	public int getProductInfoCount(Map<String, Object> map){
//		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM13.getProductCount");
//		return baseServiceImpl.getSum(map);
//	}
//	
//	/**
//	 * 取得产品信息
//	 * @param map
//	 * @return
//	 */
//	public List getProductShortInfoList(Map<String, Object> map){
//		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM13.getProductShortInfoList");
//		return baseServiceImpl.getList(map);
//	}

	/**
	 * 取得基础规则属性信息
	 * @param map
	 * @return
	 */
	public List getPrmBasePropInfoList(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM13.getPrmBasePropInfoList");
		return baseServiceImpl.getList(map);
	}
	
	
	/**
	 * 新增促销活动
	 * @param map
	 * @return
	 */
	public int addPromotionActivity(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM13.addPromotionActivity");
		return baseServiceImpl.saveBackId(map);
	}
	
	/**
	 * 新增促销活动组
	 * @param map
	 * @return
	 */
	public int addPrmActiveGrp(Map<String, Object> map){
		if (map.get("selectBrand")!=null && !"".equals("selectBrand")){
			map.put("brandInfoID", map.get("selectBrand"));
		}
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM13.addPrmActiveGrp");
		return baseServiceImpl.saveBackId(map);
	}
	
	/**
	 * 添加活动组代号
	 * @param map
	 * @return
	 */
	public int addGroupCode (Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM13.addGroupCode");
		return baseServiceImpl.update(map);
	}
	
	/**
	 * 新增促销活动规则
	 * @param map
	 * @return
	 */
	public int addPromotionActivityRule(Map<String, Object> map){
		// 促销活动规则文件名
		map.put("ruleFileName", "");
		// 促销活动规则
		map.put("activityRule", "");
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM13.addPromotionActivityRule");
		return baseServiceImpl.saveBackId(map);
	}
	
	/**
	 * 新增促销活动规则条件
	 * @param map
	 */
	public void addPromotionRuleCondition(Map<String, Object> map){
		List conditionList = (List) map.get("conditionList");
		for (int i=0;i<conditionList.size();i++){
			HashMap conditionMap = (HashMap) conditionList.get(i);
			// 促销规则明细id
			conditionMap.put("bin_PromotionActivityRuleID", map.get("bin_PromotionActivityRuleID"));
			conditionMap.put("createdBy", map.get("createdBy"));
		}
		// 批量插入
		baseServiceImpl.saveAll(conditionList, "BINOLSSPRM13.addPromotionRuleCondition");
	}
	
	/**
	 * 新增促销活动规则结果
	 * @param map
	 * @return
	 */
	public void addPromotionRuleResult(Map<String, Object> map){
		List resultList = (List) map.get("resultList");
		for (int i=0;i<resultList.size();i++){
			HashMap resultMap = (HashMap) resultList.get(i);
			// 促销规则明细id
			resultMap.put("bin_PromotionActivityRuleID", map.get("bin_PromotionActivityRuleID"));
			resultMap.put("createdBy", map.get("createdBy"));
		}
		// 批量插入
		baseServiceImpl.saveAll(resultList, "BINOLSSPRM13.addPromotionRuleResult");
	}
	
	/**
	 * 取得活动组信息
	 * @param map
	 * @return
	 */
	public Map<String, Object> getActiveGrpInfo(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM13.getActiveGrpInfo");
		return (Map<String, Object>) baseServiceImpl.get(map);
	}
	/**
	 * 取得活动地点信息
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getLocationInfo(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM13.getLocationInfo");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 新增促销活动规则条件
	 * @param map
	 */
	public void addPromotionRuleCondition(List<Map<String, Object>> list){
		// 批量插入
		baseServiceImpl.saveAll(list, "BINOLSSPRM13.addPromotionRuleCondition");
	}
	
	/**
	 * 删除活动地点信息
	 * @param map
	 */
	public void delPromotionRuleCondition(Map<String, Object> map){
		baseServiceImpl.remove(map,"BINOLSSPRM13.delPromotionRuleCondition");
	}
	
	
	/**
	 * 取得主活动总数
	 * @param map
	 * @return
	 */
	public int getactGrpInfoCount (Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM13.getactGrpInfoCount");
		return baseServiceImpl.getSum(map);
	}
	
	/**
	 * 取得主活动信息
	 * @param map
	 * @return
	 */
	public List getactGrpInfoList(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM13.getactGrpList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 取得主活动总数
	 * @param map
	 * @return
	 */
	public int deleteactGrpInfo (Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM13.deleteactGrpInfo");
		return baseServiceImpl.remove(map);
	}
	
	/**
	 * 取得主活动总数
	 * @param map
	 * @return
	 */
	public void updateactGrpInfo (Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM13.updateactGrpInfo");
		baseServiceImpl.update(map);
	}
	
	/**
	 * 查询权限柜台数量
	 * @param map
	 * @return
	 */
	public int getCounterCount (Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM13.getCounterCount");
		return baseServiceImpl.getSum(map);
	}
	
	/**
	 * 查询虚拟条码数量
	 * @param map
	 * @return
	 */
	public int getBarCodeCount (Map<String, Object> map){
		Map<String, Object> param = new HashMap<String, Object>(map);
		param.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM13.getBarCodeCount");
		return baseServiceImpl.getSum(param);
	}
	
	/**
	 * 添加活动组代号
	 * @param map
	 * @return
	 */
	public List<Integer> getActIdByName (Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM13.getActIdByName");
		return baseServiceImpl.getList(map);
	}
}
