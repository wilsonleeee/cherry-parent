/*		
 * @(#)BINOLSSPRM13_BL.java     1.0 2010/10/27		
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
package com.cherry.ss.prm.bl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.ws.rs.core.MultivaluedMap;

import com.cherry.cm.cmbussiness.bl.BINOLCM03_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM05_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM89_BL;
import com.cherry.cm.core.*;
import com.cherry.cm.mongo.MongoDB;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.DateUtil;
import com.cherry.ss.common.PromotionConstants;
import com.cherry.ss.prm.service.BINOLSSPRM13_Service;
import com.cherry.webservice.client.WebserviceClient;
import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("unchecked")
public class BINOLSSPRM13_BL {

	private static Logger logger = LoggerFactory.getLogger(BINOLSSPRM13_BL.class.getName());

	@Resource(name="binOLSSPRM13_Service")
	private BINOLSSPRM13_Service binOLSSPRM13_Service;
	
	@Resource
    private CodeTable codeTable;
	
	@Resource(name="binOLCM03_BL")
	private BINOLCM03_BL binOLCM03_BL;
	
	@Resource(name="binOLCM89_BL")
	private BINOLCM89_BL binOLCM89_BL;
	
	/** 系统配置项 共通BL */
	@Resource(name="binOLCM14_BL")
	private BINOLCM14_BL binOLCM14_BL;
	
	@Resource(name="binOLCM05_BL")
	private BINOLCM05_BL binOLCM05_BL;
	
	
	
	/**
	 * 取得日历起始日期
	 * @return
	 */
	public String getCalendarStartDate(){
		// 取得系统时间
		Date sysDate = new Date();
		//Date calStartDate = DateUtil.addDateByHours(sysDate, Integer.parseInt(PromotionConstants.PROMOTION_PUBLIC_TIMELAG));
		return DateUtil.date2String(sysDate, CherryConstants.DATE_PATTERN);
	}
	
	/**
	 * 取得总部所管辖的品牌信息
	 * @return
	 */
	public List getBrandInfoList(Map<String, Object> map){
		List brandInfoList = binOLSSPRM13_Service.getBrandInfoList(map);
		return brandInfoList;
	}
	
	
	/**
	 * 取得促销活动组信息
	 * @param map
	 * @return
	 */
	public List getActiveGrpInfo(Map<String, Object> map){
		Map<String, Object> searchMap = new HashMap<String, Object>();
		// 组织ID
		searchMap.put(CherryConstants.ORGANIZATIONINFOID, map.get(CherryConstants.ORGANIZATIONINFOID));
		// 品牌ID
		searchMap.put(CherryConstants.BRANDINFOID, map.get(CherryConstants.BRANDINFOID));
		String showType = ConvertUtil.getString(map.get("showType"));
		if("".equalsIgnoreCase(showType)){
			// 业务日期
			String bussinessDate = binOLSSPRM13_Service.getBusDate(searchMap);
			map.put("bussiDate", bussinessDate);
		}
		List resultList = binOLSSPRM13_Service.getActiveGrpList(map);
		if(null != resultList && !resultList.isEmpty()){
			if(null == map.get("actGrpID")){
				map.put("actGrpID", ((Map<String,Object>) resultList.get(0)).get("promotionActGrpID"));
			}
			// 取得活动组信息
			Map<String, Object> grpInfoMap = binOLSSPRM13_Service.getActiveGrpInfo(map);
			if(null != grpInfoMap){
				map.putAll(grpInfoMap);
			}
		}
		return resultList;
	}
	
//	/**
//	 * 取得产品信息
//	 * 
//	 * @param map
//	 * @return
//	 */
//	public List getProductShortInfoList(Map<String, Object> map) {
//		List resultList = binOLSSPRM13_Service.getProductShortInfoList(map);
//		return resultList;
//	}
//
//	/**
//	 * 取得产品数量
//	 * 
//	 * @param map
//	 * @return
//	 */
//	public int getProductCount(Map<String, Object> map) {
//		int count = binOLSSPRM13_Service.getProductInfoCount(map);
//		return count;
//	}

	/**
	 * 取得促销规则条件信息
	 * 
	 * @param map
	 * @throws JSONException
	 */
	public void getRuleConditionInfo(Map<String, Object> map) throws JSONException, CherryException{
		String activityCode = ConvertUtil.getString(map.get("activityCode"));
		if("".equals(activityCode)){
			// 取得促销活动代号
			String code = binOLCM03_BL.getTicketNumber(String.valueOf(map.get(CherryConstants.ORGANIZATIONINFOID)), String.valueOf(map.get(CherryConstants.BRANDINFOID)), String.valueOf(map.get("userName")), "9");
			map.put("activityCode", code);
		}
		// 查询规则基础属性
		HashMap basePropMap = binOLCM89_BL.setBaseProp(map);
		map.putAll(basePropMap);
		List conditionList = new ArrayList();
		List timeLocationList = (List)JSONUtil.deserialize((String)map.get("timeLocationJSON"));
		// 遍历时间地点List
		for (int i = 0;i<timeLocationList.size();i++){
			// 取得时间地点Map
			HashMap timeLocationMap  = (HashMap)timeLocationList.get(i);
			// 取得分组标记
			String conditionGrpId = String.valueOf(timeLocationMap.get("conditionGrpId"));
			// 取得地点Map
			HashMap locationDataMap = (HashMap)timeLocationMap.get("locationData");
			// 取得地点类型
			String locationType = String.valueOf(locationDataMap.get("locationType"));
			Object basePropIdObj = null;
			String basePropId = "";
			// 根据区域类型判断
			if (locationType.equals(PromotionConstants.LOTION_TYPE_REGION) || locationType.equals(PromotionConstants.LOTION_TYPE_ALL_COUNTER)) {
				basePropIdObj = map.get(PromotionConstants.BASE_PROP_CITY);
				if (null != basePropIdObj) {
					basePropId = String.valueOf(basePropIdObj);
				}
			} else if (locationType.equals(PromotionConstants.LOTION_TYPE_CHANNELS)) {
				basePropIdObj = map.get(PromotionConstants.BASE_PROP_CHANNEL);
				if (null != basePropIdObj) {
					basePropId = String.valueOf(basePropIdObj);
				}
			} else if (locationType.equals(PromotionConstants.LOTION_TYPE_7)) {
				basePropIdObj = map.get(PromotionConstants.BASEPROP_FACTION);
				if (null != basePropIdObj) {
					basePropId = String.valueOf(basePropIdObj);
				}
			} else if (locationType.equals(PromotionConstants.LOTION_TYPE_IMPORT_COUNTER) 
					|| locationType.equals(PromotionConstants.LOTION_TYPE_CHANNELS_COUNTER) 
					|| locationType.equals(PromotionConstants.LOTION_TYPE_REGION_COUNTER)
					|| locationType.equals(PromotionConstants.LOTION_TYPE_8)) {
				basePropIdObj = map.get(PromotionConstants.BASE_PROP_COUNTER);
				if (null != basePropIdObj) {
					basePropId = String.valueOf(basePropIdObj);
				}
			}
			if ("".equals(basePropId)) {
				throw new CherryException("ECM00005");
			}
			// 取得活动地点codeList
			List locationDataList = (List)locationDataMap.get("locationDataList");
			for (int j = 0;j<locationDataList.size();j++){
				HashMap conLocationMap = new HashMap();
				String[] locationIDArr = (String.valueOf(locationDataList.get(j)).split("_"));
				boolean searchFlg = false;
				// 如果是柜台模式
				if (locationType.equals(PromotionConstants.LOTION_TYPE_CHANNELS_COUNTER) || locationType.equals(PromotionConstants.LOTION_TYPE_REGION_COUNTER)){
					// 需要将没有查询出柜台的区域市,渠道的柜台信息
					HashMap parameterMap = new HashMap();
					parameterMap.putAll(map);
					if (locationIDArr[0].equals("city")){
						parameterMap.put("cityID", locationIDArr[1]);
						searchFlg = true;
					}else if (locationIDArr[0].equals("channel")){
						parameterMap.put("channelID", locationIDArr[1]);
						searchFlg = true;
					}
					if (searchFlg){
						String showType = (String)map.get("showType");
						if (showType!=null && (showType.equals("edit") || showType.equals("copy"))){
							parameterMap.put("grpID", conditionGrpId);
							// 需要关联活动结果数据
							//parameterMap.put("detailFlg", "counter");
						}
						// 查询柜台
						List counterList = this.getCounterInfoList(parameterMap);
						if (counterList.isEmpty()){
							parameterMap.remove("detailFlg");
							counterList = this.getCounterInfoList(parameterMap);
						}
						for (int k = 0;k<counterList.size();k++){
							HashMap counterMap = (HashMap)counterList.get(k);
							HashMap conLocationMap2 = new HashMap();
							// 设定规则--活动地点Map
							conLocationMap2.put("basePropId", basePropId);
							conLocationMap2.put("basePropValue", counterMap.get("id"));
							conLocationMap2.put("conditionGrpId", conditionGrpId);
							conLocationMap2.put("locationType", locationType);
							conditionList.add(conLocationMap2);
						}
					}

				}
				if (!searchFlg){
					// 设定规则--活动地点Map
					conLocationMap.put("basePropId", basePropId);
					conLocationMap.put("basePropValue", locationIDArr[1]);
					conLocationMap.put("conditionGrpId", conditionGrpId);
					conLocationMap.put("locationType", locationType);
					conditionList.add(conLocationMap);
				}
			}
//			if("5".equals(locationType)) {
//				// 设定规则--活动地点Map
//				Map<String, Object> allCounterMap = new HashMap<String, Object>();
//				allCounterMap.put("basePropId", basePropId);
//				allCounterMap.put("basePropValue", "all");
//				allCounterMap.put("conditionGrpId", conditionGrpId);
//				allCounterMap.put("locationType", locationType);
//				conditionList.add(allCounterMap);
//			}
			
			// 取得时间List
			List timeDataList = (List)timeLocationMap.get("timeDataList");
			for (int j = 0;j<timeDataList.size();j++){
				HashMap timeDataMap = (HashMap)timeDataList.get(j);
				HashMap conTimeMap = new HashMap();
				conTimeMap.put("basePropId", map.get(PromotionConstants.BASE_PROP_TIME));
				String startTime = (String)timeDataMap.get("startTime") + " " + (String)timeDataMap.get("startTime2");
				conTimeMap.put("basePropValue", startTime);
				// 结束时间
				String endTime = (String)timeDataMap.get("endTime") + " " + (String)timeDataMap.get("endTime2");
				if (null==endTime || "".equals(endTime)){
					// 如果为空,设定一个默认值
					conTimeMap.put("basePropValue2", PromotionConstants.DEFAULT_END_DATE + " 23:59:59");
				}else{
					conTimeMap.put("basePropValue2", endTime);
				}
				conTimeMap.put("conditionGrpId", conditionGrpId);
				conditionList.add(conTimeMap);
				map.put("actStartTime", startTime);
				map.put("actEndTime", endTime);
			}
		}

		map.put("conditionList",conditionList);
	}
	
	/**
	 * 取得促销活动规则结果信息
	 * @param map
	 */
	public void getRuleResultInfo(Map<String, Object> map) throws Exception{
		String orgId = ConvertUtil.getString(map.get(CherryConstants.ORGANIZATIONINFOID));
		String brandId = ConvertUtil.getString(map.get(CherryConstants.BRANDINFOID));
		// 虚拟促销品生成方式
		String virtualPrmFlag = binOLCM14_BL.getConfigValue("1068",orgId,brandId);
		if("3".equals(virtualPrmFlag)){
			this.getRuleResultInfo3(map);
		}else{
			this.getRuleResultInfo1(map);
		}
	}
	
	/**
	 * 添加促销活动组
	 * @param map
	 */
	public String addPrmActiveGrp (Map map){
		// 新增促销活动组
		int bin_PromotionActGrpID = binOLSSPRM13_Service.addPrmActiveGrp(map);
		map.put("bin_PromotionActGrpID", bin_PromotionActGrpID);
		String groupCode ="";
		if (map.get("brandCode")!=null && !"-9999".equals(map.get("brandCode"))){
			groupCode = (String)(map.get("brandCode"))+String.valueOf(bin_PromotionActGrpID);
		}else{
			HashMap resultMap = binOLSSPRM13_Service.getBrandCode(map);
			if (resultMap!=null){
				String brandCode = (String)resultMap.get("brandCode");
				groupCode = brandCode + String.valueOf(bin_PromotionActGrpID);
			}
		}
		
		// 新增促销活动组代码
		map.put("groupCode", groupCode);
		binOLSSPRM13_Service.addGroupCode(map);
		return String.valueOf(bin_PromotionActGrpID);
	}

	/**
	 * 新增促销活动(事务控制)
	 * @param map
	 * @throws Exception 
	 */
	public void tran_savePromotionActive(Map map) throws Exception {
		
		if (null!=map.get("activeID")){
			map.put("bin_PromotionActivityID", Integer.parseInt((String)map.get("activeID")));
		}else{
			// 插入促销活动表,并取得促销活动id
			int bin_PromotionActivityID = binOLSSPRM13_Service.addPromotionActivity(map);
			map.put("bin_PromotionActivityID", bin_PromotionActivityID);
		}

		// 插入促销规则明细表,并取得促销规则明细id
		int bin_PromotionActivityRuleID = binOLSSPRM13_Service.addPromotionActivityRule(map);
		// 取得活动规则id
		map.put("bin_PromotionActivityRuleID", bin_PromotionActivityRuleID);
		// 插入促销活动规则条件明细表
		binOLSSPRM13_Service.addPromotionRuleCondition(map);
		// 插入促销规则结果明细表
		binOLSSPRM13_Service.addPromotionRuleResult(map);
		// 将规则Drl插入MongoDB中
		this.insertMongoRuleDrlData(map);
	}

	/**
	 * 取得活动地点
	 */
	public List getActiveLocation(Map map) {
		List resultTreeList = new ArrayList();
		// 取得促销活动地点类型
		String locationType = (String) map.get("locationType");
		// 促销活动地点选择类型--按区域
		if (locationType.equals(PromotionConstants.LOTION_TYPE_REGION) || locationType.equals(PromotionConstants.LOTION_TYPE_REGION_COUNTER)) {
			if(locationType.equals(PromotionConstants.LOTION_TYPE_REGION_COUNTER)){
				map.put("city_counter", "1");
			}else if(map.containsKey("city_counter")){
				map.remove("city_counter");
			}
			// 取得区域信息
			List resultList = binOLSSPRM13_Service.getRegionInfoList(map);
			List keysList = new ArrayList();
			String[] keys1 = { "regionId", "regionName" };
			String[] keys2 = { "provinceId", "provinceName" };
			String[] keys3 = { "cityId", "cityName" };
			keysList.add(keys1);
			keysList.add(keys2);
			keysList.add(keys3);
			ConvertUtil.jsTreeDataDeepList(resultList, resultTreeList, keysList, 0);
		} else if (locationType.equals(PromotionConstants.LOTION_TYPE_CHANNELS) || locationType.equals(PromotionConstants.LOTION_TYPE_CHANNELS_COUNTER)) {
			if(locationType.equals(PromotionConstants.LOTION_TYPE_CHANNELS_COUNTER)){
				map.put("channel_counter", "1");
			}else if(map.containsKey("channel_counter")){
				map.remove("channel_counter");
			}
			List resultList = binOLSSPRM13_Service.getChannelInfoList(map);
			List keysList = new ArrayList();
			String[] keys1 = { "id", "name" };
			keysList.add(keys1);
			ConvertUtil.jsTreeDataDeepList(resultList, resultTreeList, keysList, 0);
			//resultTreeList = ConvertUtil.getTreeList(resultList, "nodes");
		}else if (PromotionConstants.LOTION_TYPE_7.equals(locationType)
				|| PromotionConstants.LOTION_TYPE_8.equals(locationType)) {
			List<Map<String, Object>> list = codeTable.getCodes("1309");
			List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
			if(null != list && list.size() > 0){
				for(Map<String, Object> item : list){
					Map<String, Object> temp = new HashMap<String, Object>();
					temp.put("id", item.get("CodeKey"));
					temp.put("name", item.get("Value"));
					resultList.add(temp);
				}
				List<String[]> keysList = new ArrayList<String[]>();
				String[] keys1 = { "id", "name" };
				keysList.add(keys1);
				ConvertUtil.jsTreeDataDeepList(resultList, resultTreeList,
						keysList, 0);
			}
		}

		if (locationType.equals(PromotionConstants.LOTION_TYPE_REGION_COUNTER) 
				|| locationType.equals(PromotionConstants.LOTION_TYPE_CHANNELS_COUNTER)
				|| PromotionConstants.LOTION_TYPE_8.equals(locationType)) {
			ConvertUtil.addArtificialCounterDeep(resultTreeList);
		}

		return resultTreeList;
	}
		
	/**
	 * 根据城市取得柜台信息
	 * @param map
	 */
	public List getCounterInfoList(Map map){
		List resultTreeList = new ArrayList();
		//根据城市取得柜台信息
		List resultList = binOLSSPRM13_Service.getCounterInfoList(map);
		List keysList = new ArrayList();
		String[] keys1 = { "counterCode", "counterName"};
		keysList.add(keys1);
		ConvertUtil.jsTreeDataDeepList(resultList, resultTreeList, keysList, 0);
		return resultTreeList;
	}
	
	/**
	 * 向mongoDB中插入规则drl数据
	 * @param map
	 * @throws Exception 
	 */
	public void insertMongoRuleDrlData(Map map) throws Exception{
		DBObject dbObject= new BasicDBObject();
		// 设定活动ID
		String promotionActivityID = String.valueOf(map.get("bin_PromotionActivityID"));
		// 添加规则名称
		String ruleDrl = "rule \""+promotionActivityID + "\"\n" +(String)map.get("ruleDrl") +"\n end \n\n";
		dbObject.put("PrmActiveID", promotionActivityID);
		// 设定规则Drl
		dbObject.put("RuleDrl", ruleDrl);
		// 设定组织id
		dbObject.put(CherryConstants.ORGANIZATIONINFOID, String.valueOf(map.get(CherryConstants.ORGANIZATIONINFOID)));
		// 设定品牌Code
		dbObject.put("BrandCode", map.get("brandCode"));
		MongoDB.insert(PromotionConstants.PRM_RULE_COLL_NAME, dbObject);
	}
	
	/**
	 * 取得促销活动组信息
	 * @param map
	 * @return
	 */
	public Map<String, Object> getActiveGrpTimeInfo(Map<String, Object> map){
		return binOLSSPRM13_Service.getActiveGrpInfo(map);
	}

	/**
	 * 更新活动地点信息
	 * @param ruleId
	 * @param locList
	 * @param timeList
	 * @throws Exception
     */
	public void tran_updActLocation(String ruleId,List<Object> locList,List<Map<String, Object>> timeList)throws Exception{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("ruleId", ruleId);
		List<Map<String, Object>> conList = binOLSSPRM13_Service.getLocationInfo(map);
		Map<String, Object> locMap = null;
		Map<String, Object> timeMap = null;
		for(Map<String, Object> con : conList){
			if(null == con.get("locationType")){
				timeMap = con;
			}else{
				locMap = con;
			}
		}
		List<Map<String, Object>> locInfoList = new ArrayList<Map<String,Object>>();
		for(int i=0; i < locList.size(); i++){
			Map<String, Object> item = new HashMap<String, Object>(locMap);
			item.put("basePropValue", locList.get(i));
			locInfoList.add(item);
		}
		for(int i=0; i < timeList.size(); i++){
			Map<String,Object> time = timeList.get(i);
			Map<String, Object> item = new HashMap<String, Object>(timeMap);
			item.put("basePropValue", time.get("startTime"));
			item.put("basePropValue2", time.get("endTime"));
			locInfoList.add(item);
		}
		// 删除原来的活动地点时间信息
		binOLSSPRM13_Service.delPromotionRuleCondition(map);
		binOLSSPRM13_Service.addPromotionRuleCondition(locInfoList);
	}
	/**
	 * 取得促销产品主活动信息
	 * @param map
	 * @return
	 * @throws Exception 
	 */
	public HashMap getactGrpInfoList (Map<String, Object> map) throws Exception{
		HashMap resultMap = new HashMap();
		String bussinessDate = binOLSSPRM13_Service.getBusDate(map);
		
		List filterList = new ArrayList();
		// 需要过滤的字段名
		filterList.add("A.GroupCode");
		filterList.add("A.GroupName");
		filterList.add("A.ActivityType");
		filterList.add("A.ActivityBeginDate");
		filterList.add("A.ActivityEndDate");
				map.put(CherryConstants.FILTER_LIST_NAME, filterList);
		// 取得件数
		int count = binOLSSPRM13_Service.getactGrpInfoCount(map);
		
		// 取得主活动信息List
		List actGrpInfoList = binOLSSPRM13_Service.getactGrpInfoList(map);
		if(actGrpInfoList != null && !actGrpInfoList.isEmpty()) {
			for(int i = 0; i < actGrpInfoList.size(); i++) {
				Map actGrpInfo = (Map)actGrpInfoList.get(i);
				Map actGrpInfoTemp = new HashMap();
				// 主活动Code
				actGrpInfoTemp.put("GroupCode", actGrpInfo.get("GroupCode"));
				// 主活动名称
				actGrpInfoTemp.put("GroupName", actGrpInfo.get("GroupName"));
				// 主活动类型
				actGrpInfoTemp.put("ActivityType", actGrpInfo.get("ActivityType"));
				// 主活动ID
				actGrpInfoTemp.put("promotionActGrpID", actGrpInfo.get("BIN_PromotionActGrpID"));
				// 主活动领用开始时间
				String activityBeginDate = (String)actGrpInfo.get("ActivityBeginDate");
				actGrpInfoTemp.put("ActivityBeginDate", activityBeginDate);
				// 主活动领用结束时间
				actGrpInfoTemp.put("ActivityEndDate", actGrpInfo.get("ActivityEndDate"));
				int dateEditFlag = 1;
				if(null != activityBeginDate && !"".equals(activityBeginDate)){
					// 开始日期是否小于等于业务日期
					dateEditFlag = DateUtil.compareDate(activityBeginDate, bussinessDate);
				}
				actGrpInfo.put("dateEditFlag", dateEditFlag);
				
				actGrpInfo.put("actGrpInfo", JSONUtil.serialize(actGrpInfoTemp));
			}
		}
		
		resultMap.put("actGrpInfoList", actGrpInfoList);
		resultMap.put("count", count);
		return resultMap;
	}
	
	/**
	 * 删除主活动信息
	 * 
	 * @param map
	 * @return
	 */
	public int deleteactGrpInfo(Map<String, Object> map) {
		int count = binOLSSPRM13_Service.deleteactGrpInfo(map);
		return count;
	}
	
	/**
	 * 
	 * 
	 */
	public void updateactGrpInfo(Map<String, Object> map) throws Exception{
		
		binOLSSPRM13_Service.updateactGrpInfo(map);
	}
	
	/**
	 * 查询权限柜台数量
	 * @param map
	 * @return
	 */
	public int getCounterCount (Map<String, Object> map){
		return binOLSSPRM13_Service.getCounterCount(map);
	}
	
	private void getRuleResultInfo1(Map<String, Object> map) throws Exception{
		int orgId = ConvertUtil.getInt(map.get(CherryConstants.ORGANIZATIONINFOID));
		int brandId = ConvertUtil.getInt(map.get(CherryConstants.BRANDINFOID));
		// 促销奖励
		String resultInfo = ConvertUtil.getString(map.get("resultInfo"));
		List<Map<String, Object>> infoList = (List<Map<String, Object>>)JSONUtil.deserialize(resultInfo);
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		// ===================系统生成虚拟促销品+合并礼品奖励List====================== //
		for(Map<String, Object> info : infoList){
			// 礼品box类型
			String groupType = ConvertUtil.getString(info.get(PromotionConstants.GROUPTYPE));
			HashMap<String, Object> resultMap = new HashMap<String, Object>();
			// 组类型
			resultMap.put(PromotionConstants.GROUPTYPE, groupType);
			// 虚拟促销品厂商ID
			resultMap.put(PromotionConstants.PRMVENDORID, info.get(PromotionConstants.PRMVENDORID));
			// 虚拟促销品编码
			resultMap.put(CherryConstants.UNITCODE, info.get(CherryConstants.UNITCODE));
			// 虚拟促销品条码
			resultMap.put(CherryConstants.BARCODE, info.get(CherryConstants.BARCODE));	
			resultMap.put("quantity", 1);
			// 套装折扣（全部产品折扣）
			if(PromotionConstants.GROUPTYPE_2.equals(groupType)){
				// 折扣金额
				float allDiscount = ConvertUtil.getFloat(info.get("allDiscount"));
				if(allDiscount >= 0){
					resultMap.put(PromotionConstants.PRICE, allDiscount);
					resultMap.put(CherryConstants.NAMETOTAL, map.get("prmActiveName"));
					// 取得套装折扣类型的促销品
					Map<String, Object> prmInfo = binOLCM05_BL.getPrmInfo(resultMap, orgId, brandId, PromotionConstants.PROMOTION_TZZK_TYPE_CODE);
					resultList.add(prmInfo);
				}
			}else if(PromotionConstants.GROUPTYPE_3.equals(groupType)){// 任意积分兑换
				int allExPoint = ConvertUtil.getInt(info.get("allExPoint"));
				// 抵扣金额
				float allExPrice = ConvertUtil.getFloat(info.get("allExPrice"));
				if(allExPoint >= 0){
					resultMap.put(PromotionConstants.PRICE, allExPrice);
					resultMap.put(PromotionConstants.EX_POINT, allExPoint);
					resultMap.put(CherryConstants.NAMETOTAL, map.get("prmActiveName"));
					// 系统产生DHCP类型的促销品
					Map<String, Object> prmInfo = binOLCM05_BL.getPrmInfo(resultMap, orgId, brandId, PromotionConstants.PROMOTION_DHCP_TYPE_CODE);
					resultList.add(prmInfo);
				}
			}else{
				// 礼品List
				List<Map<String, Object>> list = (List<Map<String, Object>>)info.get("list");
				if(list != null && list.size() > 0){
					groupType = ConvertUtil.getString(list.get(0).get(PromotionConstants.GROUPTYPE));
					resultMap.put(PromotionConstants.GROUPTYPE, groupType);
					if(PromotionConstants.GROUPTYPE_1.equals(groupType)){// 赠送礼品
						// 加价购
						float addAmount = ConvertUtil.getFloat(info.get("addAmount"));
						if(addAmount != 0){
							resultMap.put(PromotionConstants.PRICE, addAmount*-1);
							Map<String, Object> prmInfo = binOLCM05_BL.getPrmInfo(resultMap, orgId, brandId, null);
							list.add(prmInfo);
						}
					}else if(PromotionConstants.GROUPTYPE_2.equals(groupType)){// 套装折扣
						// 折扣金额
						float discount = ConvertUtil.getFloat(info.get("discount"));
						// 折扣策略
						String discountStrategy = binOLCM14_BL.getConfigValue("1054", orgId+"", brandId+"");
						// 整单折扣
						if(discount > 0 && "1".equals(discountStrategy)){
							resultMap.put(PromotionConstants.PRICE, discount);
							resultMap.put(CherryConstants.NAMETOTAL, map.get("prmActiveName"));
							// 取得套装折扣类型的促销品
							Map<String, Object> prmInfo = binOLCM05_BL.getPrmInfo(resultMap, orgId, brandId, PromotionConstants.PROMOTION_TZZK_TYPE_CODE);
							list.add(prmInfo);
						}
					}else if(PromotionConstants.GROUPTYPE_3.equals(groupType)){// 积分兑换
						// 积分
						int exPoint = CherryUtil.obj2int(info.get(PromotionConstants.EX_POINT));
						// 计算积分礼品的总价格
						float sumPrice = 0;
						for(Map<String, Object> temp : list){
							float price = ConvertUtil.getFloat(temp.get(PromotionConstants.PRICE));
							// 数量
							int quantity = ConvertUtil.getInt(temp.get("quantity"));
							sumPrice += price * quantity;
						}
						resultMap.put(PromotionConstants.PRICE, sumPrice);
						resultMap.put(PromotionConstants.EX_POINT, exPoint);
						resultMap.put(CherryConstants.NAMETOTAL, map.get("prmActiveName"));
						Map<String, Object> prmInfo = binOLCM05_BL.getPrmInfo(resultMap, orgId, brandId, PromotionConstants.PROMOTION_DHCP_TYPE_CODE);
						list.add(prmInfo);
					}
					resultList.addAll(list);
				}
			}
			// ===================系统生成虚拟促销品+合并礼品奖励List====================== //
		}
		map.put("resultList",resultList);
	}

	private void getRuleResultInfo3(Map<String, Object> map)throws Exception{
		int orgId = ConvertUtil.getInt(map.get(CherryConstants.ORGANIZATIONINFOID));
		int brandId = ConvertUtil.getInt(map.get(CherryConstants.BRANDINFOID));
		// 促销奖励
		String resultInfo = ConvertUtil.getString(map.get("resultInfo"));
		List<Map<String, Object>> infoList = (List<Map<String, Object>>)JSONUtil.deserialize(resultInfo);
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		for(Map<String, Object> info : infoList){
			// 礼品List
			List<Map<String, Object>> list = (List<Map<String, Object>>)info.get("list");
			if(list != null && list.size() > 0){
				// 礼品box类型
				String groupType = ConvertUtil.getString(list.get(0).get(PromotionConstants.GROUPTYPE));
				if(PromotionConstants.GROUPTYPE_1.equals(groupType)){// 赠送礼品
					// 加价购
					float addAmount = ConvertUtil.getFloat(info.get("addAmount"));
					if(addAmount != 0){
						HashMap<String, Object> resultMap = new HashMap<String, Object>();
						resultMap.put(CherryConstants.BRANDINFOID, brandId);
						resultMap.put("quantity", 1);
						resultMap.put(PromotionConstants.GROUPTYPE, groupType);
						resultMap.put(PromotionConstants.PRICE, addAmount*-1);
						Map<String, Object> prmInfo = binOLCM05_BL.getPrmInfo(resultMap, orgId, brandId, null);
						list.add(prmInfo);
					}
				}
				resultList.addAll(list);
			}
		}
		map.put("resultList",resultList);
	}
	
	/**
	 * 查询虚拟条码数量
	 * @param map
	 * @return
	 */
	public int getBarCodeCount (Map<String, Object> map){
		return binOLSSPRM13_Service.getBarCodeCount(map);
	}
	
	public List<Integer> getActIdByName(String name, String brandInfoId){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(CherryConstants.BRANDINFOID, brandInfoId);
		map.put("activityName", name);
		return binOLSSPRM13_Service.getActIdByName(map);
	}


	/**
	 * 活动关联推送到券平台
	 * @param map
	 */
	private void linkActivity(Map<String, Object> map) throws Exception {
		String couponFlag = ConvertUtil.getString(map.get("couponFlag"));
		String otherPlatformCode = ConvertUtil.getString(map.get("systemCode"));
		if("1".equals(couponFlag) && !"".equals(otherPlatformCode)){
			WebserviceConfigDTO wsconfigDTO = SystemConfigManager.getWebserviceConfigDTO("couponws");
			if(null == wsconfigDTO){
				logger.error("券平台WS访问配置内容为null");
				return;
			}
			// 券平台接口url
			String url = wsconfigDTO.getWebserviceURL();
			// 券平台接口appId
			String appId = wsconfigDTO.getAppID();
			// 券平台接口AESKEY
			String aesKey = wsconfigDTO.getSecretKey();

			WebResource webResource = WebserviceClient.getWebResource(url);
			//对传递的参数进行加密
			MultivaluedMap<String, String> queryParams = new MultivaluedMapImpl();
			queryParams.add("appId", appId);
			queryParams.add("method", "LinkActivity");
			queryParams.add("ts", ConvertUtil.getString(System.currentTimeMillis() / 1000));
			Map<String,Object> params = new HashMap<String, Object>();
			params.put("otherPlatformCode",map.get("systemCode"));
			params.put("otherCode",map.get("linkMainCode"));
			params.put("pekonCode",map.get("activityCode"));
			params.put("pekonName",map.get("prmActiveName"));
			params.put("fromTime",map.get("minTime"));
			params.put("toTime",map.get("maxTime"));
			String paramStr = CherryUtil.map2Json(params);
//			logger.info("券平台访问appId={},aesKey={}",appId,aesKey);
//			logger.info("券平台访问url={},params={}",url,paramStr);
			queryParams.add("params", CherryAESCoder.encrypt(paramStr,aesKey));
			String result = webResource.queryParams(queryParams).get(String.class);
			// 券平台返回结果
			Map<String, Object> retMap = CherryUtil.json2Map(result);
			if(null == retMap){
				logger.error("券平台无返回结果");
			}else{
				String code = ConvertUtil.getString(retMap.get("code"));
				if("0".equals(code)){
					logger.info("调用券平台LinkActiviy成功");
				}else{
					logger.error("调用券平台LinkActiviy失败，错误码:{}",code);
				}
			}
		}
	}

	public int validLinkMainCode(Map map){
		int result = 0;
		String couponFlag = ConvertUtil.getString(map.get("couponFlag"));
		String systemCode = ConvertUtil.getString(map.get("systemCode"));
		String linkMainCode = ConvertUtil.getString(map.get("linkMainCode"));
		if("1".equals(couponFlag)){
			if(!"".equals(systemCode) && "".equals(linkMainCode)){
				result = 1;
			}else if("".equals(systemCode) && !"".equals(linkMainCode)){
				result = 2;
			}else if(!"".equals(systemCode) && !"".equals(linkMainCode)){
				int opt = ConvertUtil.getInt(map.get("OPT_KBN"));
				if(opt == 0){
					String showType = ConvertUtil.getString(map.get("showType"));
					if("edit".equals(showType)){
						opt = 2;
					}else{
						opt = 1;
					}
				}
				// 验证 systemCode&&linkMainCode是否已经存在
				List<String> list = binOLSSPRM13_Service.getActivityCodeList(systemCode,linkMainCode);
				if(null != list && list.size() > 0){
					if(opt == 1 || opt == 3){// 新增,复制
						if(list.size() > 0){
							result = 3;
						}
					}else if(opt == 2){// 编辑
						String activityCode = ConvertUtil.getString(map.get("activityCode"));
						if (list.size() > 1){
							result = 4;
						}else if(list.size() == 1 && !list.contains(activityCode)){
							result = 5;
						}
					}
				}
			}
		}
		return result;
	}
}
