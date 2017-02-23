/*		
 * @(#)BINOLSSPRM37_BL.java     1.0 2010/11/10		
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.ws.rs.core.MultivaluedMap;

import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM89_BL;
import com.cherry.cm.core.*;
import com.cherry.cm.mongo.MongoDB;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cp.point.service.BINOLCPPOI01_Service;
import com.cherry.pt.common.ProductConstants;
import com.cherry.ss.common.PromotionConstants;
import com.cherry.ss.prm.service.BINOLSSPRM13_Service;
import com.cherry.ss.prm.service.BINOLSSPRM37_Service;
import com.cherry.webservice.client.WebserviceClient;
import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 促销活动编辑_BL
 * @author huzude
 *
 */
@SuppressWarnings("unchecked")
public class BINOLSSPRM37_BL {

	private static Logger logger = LoggerFactory.getLogger(BINOLSSPRM37_BL.class.getName());
	
	@Resource
	private BINOLCM89_BL binOLCM89_BL;
	
	/** 系统配置项 共通BL */
	@Resource(name="binOLCM14_BL")
	private BINOLCM14_BL binOLCM14_BL;
	
	@Resource
	private BINOLSSPRM13_BL binOLSSPRM13_BL;

	@Resource
	private BINOLSSPRM37_Service binOLSSPRM37_Service;
	
	@Resource
	private BINOLSSPRM13_Service binOLSSPRM13_Service;
	
	@Resource
    private CodeTable codeTable;
	
	@Resource
	private BINOLCPPOI01_Service binOLCPPOI01_Service;
	
	/**
	 * 取得活动详细信息
	 * @param map
	 */
	public void getActiveDetailInfo(Map<String, Object> map){
		// 取得主活动详细信息
		HashMap resultMap = binOLSSPRM37_Service.getDetailActInfo(map);
		
		map.putAll(resultMap);
		
		// 查询规则基础属性
		HashMap basePropMap = binOLCM89_BL.setBaseProp(map);
		map.putAll(basePropMap);
		
		// 取得促销活动详细时间信息
		map.put("baseProp_ID", map.get(PromotionConstants.BASE_PROP_TIME));
		//map.remove("grpID");
		List actTimeList = binOLSSPRM37_Service.getDetailActConList(map);
		for (int i=0;i<actTimeList.size();i++){
			HashMap actTimeMap = (HashMap)actTimeList.get(i);
			String[] startTime = ConvertUtil.getString(actTimeMap.get("basePropValue1")).split(" ");
			String[] endTime = ConvertUtil.getString(actTimeMap.get("basePropValue2")).split(" ");
			actTimeMap.put("startTime", startTime[0]);
			actTimeMap.put("endTime", endTime[0]);
			if(startTime.length > 1){
				actTimeMap.put("startTime2", startTime[1]);
			}
			if(endTime.length > 1){
				actTimeMap.put("endTime2", endTime[1]);
			}
		}
		// 将时间通过不同的分组号进行分组
		List actGrpTimeList = ConvertUtil.listGroup(actTimeList, "grpID", "actSglTimeList");
		map.put("actGrpTimeList", actGrpTimeList);	

	}
	
	/**
	 * 验证活动是否有效
	 * @return
	 */
	public void checkActiveValid(Map<String, Object> map){
		HashMap resultMap = binOLSSPRM37_Service.getValidActCount(map);
		if (resultMap.get("count")!=null){
			String count = String.valueOf(resultMap.get("count"));
			if ("0".equals(count)){
				resultMap = binOLSSPRM37_Service.getActivityRuleID(map);
				map.put("promotionActivityRuleID", resultMap.get("promotionActivityRuleID"));
			}
		}
	}
	
	
	
	public HashMap getActiveBrandInfo(Map<String, Object> map){
		// 取得主活动详细信息
		HashMap resultMap = binOLSSPRM37_Service.getDetailActInfo(map);
		return resultMap;
	}
	
	/**
	 * 取得促销活动详细--活动地点信息
	 * @param map
	 * @throws JSONException 
	 */
	public void getActiveDetailLocation (Map<String, Object> map) throws JSONException{

		// 查询规则基础属性
		HashMap basePropMap = binOLCM89_BL.setBaseProp(map);
		map.putAll(basePropMap);
		List basePropIdList = new ArrayList(); 
		// 基础属性条件-- 柜台
		basePropIdList.add(map.get(PromotionConstants.BASE_PROP_COUNTER));
		// 基础属性条件-- 区域市
		basePropIdList.add(map.get(PromotionConstants.BASE_PROP_CITY));
		// 基础属性条件-- 渠道
		basePropIdList.add(map.get(PromotionConstants.BASE_PROP_CHANNEL));
		basePropIdList.add(map.get(PromotionConstants.BASEPROP_FACTION));
		
		// 取得促销活动详细地点信息
		map.put("basePropIdList", basePropIdList);
		List actLocationList = binOLSSPRM37_Service.getDetailActConList(map);
		List actGrpLocationList = ConvertUtil.listGroup(actLocationList, "grpID", "actSglLocationList");
		
		List grpLocationPageList  = new ArrayList();
		for (int i = 0;i<actGrpLocationList.size();i++){
			// 右边树List(促销地点List)
			List rightTreeList = new ArrayList();
			// 左边树List(候选促销地点List)
			List leftTreeList = new ArrayList();
			
			HashMap actGrpLocationMap = (HashMap)actGrpLocationList.get(i);
			List actSglLocationList = (List)actGrpLocationMap.get("actSglLocationList");
			HashMap pageLocationMap = new HashMap();
			// 取得地点类型
			String actLocationType = String.valueOf(((HashMap)actSglLocationList.get(0)).get("actLocationType"));
			map.put("grpID", String.valueOf(((HashMap)actSglLocationList.get(0)).get("grpID")));
			rightTreeList = this.getRightTreeData(rightTreeList,actSglLocationList,actLocationType, map);
			leftTreeList = this.getLeftTreeData(leftTreeList, actSglLocationList, actLocationType, map);
			pageLocationMap.put("leftTreeList", leftTreeList);
			pageLocationMap.put("rightTreeList", rightTreeList);
			pageLocationMap.put("actLocationType", actLocationType);
			grpLocationPageList.add(pageLocationMap);
		}
		
		map.put("grpLocationPageList", JSONUtil.serialize(grpLocationPageList));
//		// 取得地点类型
//		String actLocationType = String.valueOf(((HashMap)actLocationList.get(0)).get("actLocationType"));
//		rightTreeList = this.getRightTreeData(rightTreeList,actLocationType, map);
//		leftTreeList = this.getLeftTreeData(leftTreeList, actLocationList, actLocationType, map);
//		map.put("leftTreeList", leftTreeList);
//		map.put("rightTreeList", rightTreeList);
//		map.put("actLocationType", actLocationType);
	}
	
	/**
	 * 取得促销活动详细--柜台信息
	 * @param map
	 * @throws Exception 
	 */
	public List getActiveCounter(Map<String, Object> map) throws Exception{
		List resultTreeList = new ArrayList();
		// 查询规则基础属性
		HashMap basePropMap = binOLCM89_BL.setBaseProp(map);
		map.putAll(basePropMap);
		if (map.get("activeID")!=null){
			map.put("detailFlg", "counter");
		}else{
			map.put("treeType", "rightTree");
		}
		// 取得右边树的柜台信息
		List rightCounterList = binOLSSPRM13_Service.getCounterInfoList(map);
		List keysList = new ArrayList();
		String[] keys1 = { "counterCode", "counterName","cityID","channelID","factionID"};
		keysList.add(keys1);
		// 如果点击的是左边的树
		if ("leftTree".equals(map.get("treeType"))){
			map.remove("detailFlg");
			List leftCounterList = binOLSSPRM13_Service.getCounterInfoList(map);
			List copyLeftCounterList = (List)ConvertUtil.byteClone(leftCounterList);
			for (int i=0;i<leftCounterList.size();i++){
				HashMap leftCounterMap = (HashMap)leftCounterList.get(i);
				for (int j=0;j<rightCounterList.size();j++){
					HashMap rightCounterMap = (HashMap)rightCounterList.get(j);
					if (leftCounterMap.get("counterCode").equals(rightCounterMap.get("counterCode"))){
						leftCounterList.remove(i);
						i--;
						break;
					}
				}
			}
			
			if (leftCounterList.isEmpty()){
				leftCounterList = copyLeftCounterList;
			}
			ConvertUtil.jsTreeDataDeepList(leftCounterList, resultTreeList, keysList, 0);
		}else{
			// 如果没有查到右边树的数据,则显示该节点下的所有柜台
			if (rightCounterList.isEmpty()){
				map.remove("detailFlg");
				List leftCounterList = binOLSSPRM13_Service.getCounterInfoList(map);
				rightCounterList = leftCounterList;
			}
			ConvertUtil.jsTreeDataDeepList(rightCounterList, resultTreeList, keysList, 0);
		}
		
		return resultTreeList;
	}
	
	/**
	 * 取得柜台父节点信息
	 * @param map
	 * @return
	 */
	public HashMap getCounterParent(Map<String, Object> map){
		List resultList  = binOLSSPRM37_Service.selCounterParentList(map);
		HashMap resultMap = new HashMap();
		if (resultList!=null && !resultList.isEmpty()){
			resultMap= (HashMap)resultList.get(0);
		}
		return resultMap;
	}
	
	/**
	 * 取得促销地点树数据
	 * @param rightTreeList
	 * @param actLocationType
	 * @param map
	 */
	private List<Map<String, Object>> getRightTreeData (List<Map<String, Object>> rightTreeList,List<Map<String, Object>> actSglLocationList,String actLocationType,Map<String, Object> map){

		String detailFlg ="";
		// 按区域查询
		if (actLocationType.equals(PromotionConstants.LOTION_TYPE_REGION) || actLocationType.equals(PromotionConstants.LOTION_TYPE_REGION_COUNTER)){
			detailFlg = actLocationType.equals(PromotionConstants.LOTION_TYPE_REGION)?"city":"counter";
			// 设定详细标记
			map.put("detailFlg", detailFlg);
			if(detailFlg.equals("counter")){
				map.put("city_counter", "1");
			}
			// 区域List
			List regionList = binOLSSPRM13_Service.getRegionInfoList(map);
			List keysList = new ArrayList();
			String[] keys1 = { "regionId", "regionName" };
			String[] keys2 = { "provinceId", "provinceName" };
			String[] keys3 = { "cityId", "cityName" };
			keysList.add(keys1);
			keysList.add(keys2);
			keysList.add(keys3);
			if(detailFlg.equals("counter")){
				String[] keys4 = { "counterCode", "counterName" };
				keysList.add(keys4);
			}
			ConvertUtil.jsTreeDataDeepList(regionList, rightTreeList, keysList, 0);
		}else if (actLocationType.equals(PromotionConstants.LOTION_TYPE_CHANNELS) || actLocationType.equals(PromotionConstants.LOTION_TYPE_CHANNELS_COUNTER)) {
			detailFlg = actLocationType.equals(PromotionConstants.LOTION_TYPE_CHANNELS)?"channel":"counter";
			// 设定详细标记
			map.put("detailFlg", detailFlg);
			if(detailFlg.equals("counter")){
				map.put("channel_counter", "1");
			}
			List channelList = binOLSSPRM13_Service.getChannelInfoList(map);
			List keysList = new ArrayList();
			String[] keys1 = { "id", "name" };
			keysList.add(keys1);
			if(detailFlg.equals("counter")){
				String[] keys2 = { "counterCode", "counterName" };
				keysList.add(keys2);
			}
			ConvertUtil.jsTreeDataDeepList(channelList, rightTreeList, keysList, 0);
			//rightTreeList = ConvertUtil.getTreeList(resultList, "nodes");
		}else if(actLocationType.equals(PromotionConstants.LOTION_TYPE_IMPORT_COUNTER)){
			Map<String, Object> paramMap = new HashMap<String, Object>(map);
			// 权限柜台
			List<Map<String,Object>> allList = binOLSSPRM13_BL.getCounterInfoList(paramMap);
			if(null != allList && null != actSglLocationList){
				for(Map<String,Object> node : allList){
					String id = ConvertUtil.getString(node.get("id"));
					for(Map<String,Object> temp : actSglLocationList){
						String value1 = ConvertUtil.getString(temp.get("basePropValue1"));
						if(id.equalsIgnoreCase(value1)){
							rightTreeList.add(node);
							break;
						}
					}
				}
			}
		}else if(actLocationType.equals(PromotionConstants.LOTION_TYPE_ALL_COUNTER)){
			Map<String, Object> node = new HashMap<String, Object>();
			node.put("id", "all");
			node.put("name", "ALL_CONTER");
			rightTreeList.add(node);
		}else if (PromotionConstants.LOTION_TYPE_7.equals(actLocationType)) {
			List<Map<String, Object>> list = codeTable.getCodes("1309");
			if(null != list && list.size() > 0){
				Map<Object, Object> codeMap = new HashMap<Object, Object>();
				// list2map
				for(Map<String, Object> item : list){
					codeMap.put(item.get("CodeKey"), item.get("Value"));
				}
				if(null != actSglLocationList && actSglLocationList.size() > 0){
					for(Map<String, Object> result : actSglLocationList){
						result.put("name", codeMap.get(result.get("basePropValue1")));
						result.put("id", result.get("basePropValue1"));
					}
				}
				List<String[]> keysList = new ArrayList<String[]>();
				String[] keys1 = { "id", "name" };
				keysList.add(keys1);
				ConvertUtil.jsTreeDataDeepList(actSglLocationList, rightTreeList,
						keysList, 0);
			}
		} else if (PromotionConstants.LOTION_TYPE_8.equals(actLocationType)) {
			List<Map<String, Object>> list = codeTable.getCodes("1309");
			if(null != list && list.size() > 0){
				Map<Object, Object> codeMap = new HashMap<Object, Object>();
				// list2map
				for(Map<String, Object> item : list){
					codeMap.put(item.get("CodeKey"), item.get("Value"));
				}
				List<Map<String, Object>> resultList = binOLSSPRM13_Service.getCntInfoList(map);
				if(null != resultList && resultList.size() > 0){
					for(Map<String, Object> result : resultList){
						result.put("name", codeMap.get(result.get("id")));
					}
				}
				List<String[]> keysList = new ArrayList<String[]>();
				String[] keys1 = { "id", "name" };
				keysList.add(keys1);
				String[] keys2 = { "counterCode", "counterName" };
				keysList.add(keys2);
				ConvertUtil.jsTreeDataDeepList(resultList, rightTreeList,
						keysList, 0);
			}
		}
		
		if (actLocationType.equals(PromotionConstants.LOTION_TYPE_REGION_COUNTER) || actLocationType.equals(PromotionConstants.LOTION_TYPE_CHANNELS_COUNTER)) {
			//ConvertUtil.addArtificialCounterDeep(rightTreeList);
		}
		return rightTreeList;
	}
	
	/**
	 * 取得候选促销地点树数据
	 * @param leftTreeList
	 * @param actLocationList
	 * @param actLocationType
	 * @param map
	 */
	private List getLeftTreeData (List leftTreeList,List actLocationList ,String actLocationType,Map<String, Object> map){
		map.remove("detailFlg");
		// 按区域查询
		if (actLocationType.equals(PromotionConstants.LOTION_TYPE_REGION) || actLocationType.equals(PromotionConstants.LOTION_TYPE_REGION_COUNTER)){
			if(actLocationType.equals(PromotionConstants.LOTION_TYPE_REGION_COUNTER)){
				map.put("city_counter", "1");
			}else if(map.containsKey("city_counter")){
				map.remove("city_counter");
			}
			// 区域List
			List regionList = binOLSSPRM13_Service.getRegionInfoList(map);
			// 去除促销地点
			for (int i=0;i<regionList.size();i++){
				HashMap regionMap = (HashMap)regionList.get(i);
				for (int j = 0 ;j<actLocationList.size();j++){
					HashMap actLocationMap = (HashMap)actLocationList.get(j);
					// 设定比较的值
					String compareCode=actLocationType.equals(PromotionConstants.LOTION_TYPE_REGION)?"cityId":"counterCode";
					if (actLocationMap.get("basePropValue1").equals(String.valueOf(regionMap.get(compareCode)))){
						regionList.remove(i);
						i--;
						break;
					}
				}
			}
			
			List keysList = new ArrayList();
			String[] keys1 = { "regionId", "regionName" };
			String[] keys2 = { "provinceId", "provinceName" };
			String[] keys3 = { "cityId", "cityName" };
			keysList.add(keys1);
			keysList.add(keys2);
			keysList.add(keys3);
			if(actLocationType.equals(PromotionConstants.LOTION_TYPE_REGION_COUNTER)){
				String[] keys4 = { "counterCode", "counterName" };
				keysList.add(keys4);
			}
			ConvertUtil.jsTreeDataDeepList(regionList, leftTreeList, keysList, 0);
			
		}else if (actLocationType.equals(PromotionConstants.LOTION_TYPE_CHANNELS) || actLocationType.equals(PromotionConstants.LOTION_TYPE_CHANNELS_COUNTER)) {
			if(actLocationType.equals(PromotionConstants.LOTION_TYPE_CHANNELS_COUNTER)){
				map.put("channel_counter", "1");
			}else if(map.containsKey("channel_counter")){
				map.remove("channel_counter");
			}
			List channelList = binOLSSPRM13_Service.getChannelInfoList(map);
			// 去除促销地点
			for (int i=0;i<channelList.size();i++){
				HashMap regionMap = (HashMap)channelList.get(i);
				for (int j = 0 ;j<actLocationList.size();j++){
					HashMap actLocationMap = (HashMap)actLocationList.get(j);
					// 设定比较的值
					String compareCode=actLocationType.equals(PromotionConstants.LOTION_TYPE_CHANNELS)?"id":"counterCode";
					if (actLocationMap.get("basePropValue1").equals(String.valueOf(regionMap.get(compareCode)))){
						channelList.remove(i);
						i--;
						break;
					}
				}
			}
			
			List keysList = new ArrayList();
			String[] keys1 = { "id", "name" };
			keysList.add(keys1);
			if(actLocationType.equals(PromotionConstants.LOTION_TYPE_CHANNELS_COUNTER)){
				String[] keys2 = { "counterCode", "counterName" };
				keysList.add(keys2);
			}
			ConvertUtil.jsTreeDataDeepList(channelList, leftTreeList, keysList, 0);
			
			//leftTreeList = ConvertUtil.getTreeList(channelList, "nodes");
		}else if (PromotionConstants.LOTION_TYPE_7.equals(actLocationType)) {
			List<Map<String, Object>> list = codeTable.getCodes("1309");
			List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
			List<String> keyList = new ArrayList<String>();
			//
			if(null != actLocationList && actLocationList.size() > 0){
				List<Map<String, Object>> mapList = (List<Map<String, Object>>)actLocationList;
				for(Map<String, Object> result : mapList){
					keyList.add(ConvertUtil.getString(result.get("basePropValue1")));
				}
			}
			if(null != list && list.size() > 0){
				for(Map<String, Object> item : list){
					String key = ConvertUtil.getString(item.get("CodeKey"));
					if(!keyList.contains(key)){
						Map<String, Object> temp = new HashMap<String, Object>();
						temp.put("id", key);
						temp.put("name", item.get("Value"));
						resultList.add(temp);
					}
				}
				List keysList = new ArrayList();
				String[] keys1 = { "id", "name" };
				keysList.add(keys1);
				ConvertUtil.jsTreeDataDeepList(resultList, leftTreeList,
						keysList, 0);
			}
		} else if (PromotionConstants.LOTION_TYPE_8.equals(actLocationType)) {
			List<Map<String, Object>> list = codeTable.getCodes("1309");
			if(null != list && list.size() > 0){
				Map<Object, Object> codeMap = new HashMap<Object, Object>();
				// list2map
				for(Map<String, Object> item : list){
					codeMap.put(item.get("CodeKey"), item.get("Value"));
				}
				List<Map<String, Object>> resultList = binOLSSPRM13_Service.getExCntInfoList(map);
				if(null != resultList && resultList.size() > 0){
					for(Map<String, Object> result : resultList){
						result.put("name", codeMap.get(result.get("id")));
					}
				}
				List keysList = new ArrayList();
				String[] keys1 = { "id", "name" };
				keysList.add(keys1);
				String[] keys2 = { "counterCode", "counterName" };
				keysList.add(keys2);
				ConvertUtil.jsTreeDataDeepList(resultList, leftTreeList,
						keysList, 0);
			}
		}
		
		if (actLocationType.equals(PromotionConstants.LOTION_TYPE_REGION_COUNTER) || actLocationType.equals(PromotionConstants.LOTION_TYPE_CHANNELS_COUNTER)) {
			//ConvertUtil.addArtificialCounterDeep(leftTreeList);
		}
		
		return leftTreeList;
	}
	
	/**
	 * 取得促销活动结果详细
	 * @param map
	 */
	public List<Map<String, Object>> getActiveResultDetail(Map<String, Object> map){
		String orgId = ConvertUtil.getString(map.get(CherryConstants.ORGANIZATIONINFOID));
		String brandId = ConvertUtil.getString(map.get(CherryConstants.BRANDINFOID));
		String showType = ConvertUtil.getString(map.get("showType"));
		// 虚拟促销品生成方式
		String virtualPrmFlag = binOLCM14_BL.getConfigValue("1068",orgId,brandId);
		// 取得促销活动结果详细List
		List<Map<String, Object>> resultList = binOLSSPRM37_Service.getDetailActRelList(map);
		resultList = ConvertUtil.listGroup(resultList,PromotionConstants.GROUPTYPE,ProductConstants.LIST);
		if("3".equals(virtualPrmFlag)){
			this.getActiveResultDetail3(resultList);
		}else{
			this.getActiveResultDetail1(resultList,showType);
		}
		return resultList;
	}
	
	/**
	 * 更新促销活动(事务控制)
	 * @param map
	 * @throws Exception 
	 */
	public void tran_updPrmActive (Map<String, Object> map) throws Exception{
		// 更新促销活动
		int updCount = binOLSSPRM37_Service.updPrmActivity(map);
		if (updCount == 0){
			throw new CherryException("ECM00038");
		}
		// 删除原有活动明细
		binOLSSPRM37_Service.delPrmActivityRule(map);
		binOLSSPRM37_Service.delPrmActivityRuleCondition(map);
		binOLSSPRM37_Service.delPrmActivityRuleResult(map);
		this.deleteMongoRuleDrlData(map);
		// 插入新的促销活动规则明细
		binOLSSPRM13_BL.tran_savePromotionActive(map);
	}
	
	/**
	 * 删除/停止活动(事务控制)
	 * @param map
	 * @throws Exception 
	 */
	public void tran_stopPrmActive (String activeId,String couponFlag) throws Exception{
		Map<String,Object> p = new HashMap<String, Object>();
		p.put("activeID", activeId);
		int count = binOLSSPRM37_Service.getActivityTransHisCount(p);
		if(count != 0){
			// 停止
			binOLSSPRM37_Service.stopPrmActivity(activeId);
		}else{
			// 伦理删除
			binOLSSPRM37_Service.disablePrmActivity(activeId);
		}
		// 无效会员主题活动表
		binOLSSPRM37_Service.delCampain(p);
		if("1".equals(couponFlag)){
			String activityCode = binOLSSPRM37_Service.getActivityCode(activeId);
			delLinkActivity(activityCode);
		}
	}

	private void delLinkActivity(String activityCode) throws Exception {
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
		params.put("pekonCode",activityCode);
		params.put("deleteFlag","1");
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
	/**
	 * 向mongoDB中删除规则drl数据
	 * @param map
	 * @throws Exception 
	 */
	public void deleteMongoRuleDrlData(Map map) throws Exception{
		DBObject dbObject= new BasicDBObject();
		// 设定活动ID
		String promotionActivityID = String.valueOf(map.get("activeID"));
		dbObject.put("PrmActiveID", promotionActivityID);
		MongoDB.removeAll(PromotionConstants.PRM_RULE_COLL_NAME, dbObject);
	}
	
	/**
	 * 检查柜台节点，如果没有读取，则进行读取
	 * @param map
	 * @throws Exception
	 */
	public List checkCounterNodes (Map map) throws Exception{
		HashMap basePropMap = binOLCM89_BL.setBaseProp(map);
		map.putAll(basePropMap);
		List timeLocationDataArr = (List)JSONUtil.deserialize((String)map.get("timeLocationJSON"));
		for (int i=0;i<timeLocationDataArr.size();i++){
			Map timeLocationData = (Map)timeLocationDataArr.get(i);
			Map locationData = (Map)timeLocationData.get("locationData");
			if (locationData == null){
				break;
			}
			// 取得地点类型
			String locationType = (String)locationData.get("locationType");
			// 取得柜台数据
			List locationDataList = (List)locationData.get("locationDataList");
			if (locationType.equals(PromotionConstants.LOTION_TYPE_CHANNELS_COUNTER) || locationType.equals(PromotionConstants.LOTION_TYPE_REGION_COUNTER)){
				String locationName = ((String)locationDataList.get(0)).split("_")[0];
				String locationId = ((String)locationDataList.get(0)).split("_")[1];
				if (locationDataList.size() == 1 && !locationName.equals("counter")){
						HashMap parameterMap = new HashMap();
						parameterMap.putAll(map);
						
						if (locationName.equals("city")){
							parameterMap.put("cityID", locationId);
						}else if (locationName.equals("channel")){
							parameterMap.put("channelID", locationId);
						}
						String showType = (String)map.get("showType");
						if (showType!=null && (showType.equals("edit") || showType.equals("copy"))){
							parameterMap.put("grpID", timeLocationData.get("conditionGrpId"));
							// 需要关联活动结果数据
							//parameterMap.put("detailFlg", "counter");
						}
					// 查询柜台
					List counterList = binOLSSPRM13_BL.getCounterInfoList(parameterMap);
					if (counterList.isEmpty()){
						parameterMap.remove("detailFlg");
						counterList = binOLSSPRM13_BL.getCounterInfoList(parameterMap);
					}
					locationDataList.clear();
					for (int j=0;j<counterList.size();j++){
						Map counterMap = (Map)counterList.get(j);
						locationDataList.add("counter_"+(String)counterMap.get("id"));
					}
				}
			}
		}
		return timeLocationDataArr;
	}
	
	/**
	 * 取得系统时间(年月日)
	 * 
	 * 
	 * @return String
	 *			系统时间(年月日)
	 */
	public String getDateYMD() {
		return binOLSSPRM37_Service.getDateYMD();
	}
	
	private void getActiveResultDetail1(List<Map<String, Object>> resultList,String showType){
		for (Map<String, Object> resultMap : resultList){
			List<Map<String, Object>> list = (List<Map<String, Object>>)resultMap.get(ProductConstants.LIST);
			if(null == list || list.size() == 0){
				continue;
			}
			// 组
			String groupType = ConvertUtil.getString(list.get(0).get(PromotionConstants.GROUPTYPE));
			// 第一条礼品的类型
			String prmCate1 = ConvertUtil.getString(list.get(0).get(PromotionConstants.PRMCATE));
			resultMap.put(PromotionConstants.GROUPTYPE, groupType);
			int sumQuantity = 0;
			if(PromotionConstants.GROUPTYPE_2.equals(groupType)){// 套装折扣
				// 套装原价sum
				float sumOldPrice = 0;
				// 套装价sum
				float sumPrice = 0;
				// 折扣价
				float discount = 0;
				// 全部折扣(奖励产品只有一条虚拟TZZK)
				if(list.size() == 1 && PromotionConstants.PROMOTION_TZZK_TYPE_CODE.equals(prmCate1)){
					discount = ConvertUtil.getFloat(list.get(0).get(PromotionConstants.PRICE));
					sumQuantity = ConvertUtil.getInt(list.get(0).get(PromotionConstants.QUANTITY));
					resultMap.put("allDiscount", discount);
					resultMap.put("sumOldPrice", 0);
					resultMap.put("sumPrice", 0);
					resultMap.put("discount", 0);
				}else{
					for(int i = 0; i < list.size(); i++){
						Map<String, Object> item = list.get(i);
						// 礼品类型
						String prmCate = ConvertUtil.getString(item.get(PromotionConstants.PRMCATE));
						// 礼品原价
						float oldPrice = ConvertUtil.getFloat(item.get(PromotionConstants.OLD_PRICE));
						// 礼品单价
						float price = ConvertUtil.getFloat(item.get(PromotionConstants.PRICE));
						
						if(PromotionConstants.PROMOTION_TZZK_TYPE_CODE.equals(prmCate)){// TZZK类型
							discount = price;
							if("detail".equals(showType)||"edit_add".equals(showType)){
								sumQuantity += ConvertUtil.getInt(item.get(PromotionConstants.QUANTITY));
							}
						}else{
							// 数量
							int quantity = ConvertUtil.getInt(item.get(PromotionConstants.QUANTITY));
							sumQuantity += quantity;
							sumOldPrice += oldPrice * quantity;
							sumPrice += price * quantity;
						}
					}
					if(discount == 0){
						discount = sumOldPrice - sumPrice;
					}else{
						sumPrice = sumOldPrice - discount;
					}
					resultMap.put("sumOldPrice", sumOldPrice);
					resultMap.put("sumPrice", sumPrice);
					resultMap.put("discount", discount);
				}
				
			}else if(PromotionConstants.GROUPTYPE_1.equals(groupType)){// 赠送礼品
				// 加价购
				float addAmount = 0;
				for(int i = 0; i < list.size(); i++){
					Map<String, Object> item = list.get(i);
					// 礼品类型
					String prmCate = ConvertUtil.getString(item.get(PromotionConstants.PRMCATE));
					if(PromotionConstants.PROMOTION_TZZK_TYPE_CODE.equals(prmCate)){// TZZK类型
						addAmount =  ConvertUtil.getFloat(item.get(PromotionConstants.PRICE));
						if("detail".equals(showType)||"edit_add".equals(showType)){
							sumQuantity += ConvertUtil.getInt(item.get(PromotionConstants.QUANTITY));
						}
					}else{
						sumQuantity += ConvertUtil.getInt(item.get(PromotionConstants.QUANTITY));
					}
				}
				if(addAmount != 0){addAmount*=-1;}
				resultMap.put("addAmount",addAmount);
			}else if(PromotionConstants.GROUPTYPE_3.equals(groupType)){// 积分兑礼
				// 积分
				int exPoint = 0;
				float discount = 0;
				for(int i = 0; i < list.size(); i++){
					Map<String, Object> item = list.get(i);
					// 礼品类型
					String prmCate = ConvertUtil.getString(item.get(PromotionConstants.PRMCATE));
					if(PromotionConstants.PROMOTION_DHCP_TYPE_CODE.equals(prmCate)){// DHCP类型
						exPoint = ConvertUtil.getInt(item.get(PromotionConstants.EX_POINT));
						discount = ConvertUtil.getFloat(item.get(PromotionConstants.PRICE));
						if("detail".equals(showType)||"edit_add".equals(showType)){
							sumQuantity += ConvertUtil.getInt(item.get(PromotionConstants.QUANTITY));
						}
					}else{
						sumQuantity += ConvertUtil.getInt(item.get(PromotionConstants.QUANTITY));
					}
				}
				resultMap.put("discount", discount);
				resultMap.put(PromotionConstants.EX_POINT, exPoint);
			}
			resultMap.put("sumQuantity", sumQuantity);
		}
	}
	
	private void getActiveResultDetail3(List<Map<String, Object>> resultList){
		for (Map<String, Object> resultMap : resultList){
			List<Map<String, Object>> list = (List<Map<String, Object>>)resultMap.get(ProductConstants.LIST);
			if(null == list || list.size() == 0){
				continue;
			}
			// 组
			String groupType = ConvertUtil.getString(list.get(0).get(PromotionConstants.GROUPTYPE));
			resultMap.put(PromotionConstants.GROUPTYPE, groupType);
			int sumQuantity = 0;
			// 套装价sum
			float sumPrice = 0;
			// 套装原价sum
			float sumOldPrice = 0;
			for(int i = 0; i < list.size(); i++){
				Map<String, Object> item = list.get(i);
				// 礼品原价
				float oldPrice = ConvertUtil.getFloat(item.get(PromotionConstants.OLD_PRICE));
				// 礼品单价
				float price = ConvertUtil.getFloat(item.get(PromotionConstants.PRICE));
				// 数量
				int quantity = ConvertUtil.getInt(item.get(PromotionConstants.QUANTITY));
				String prmCate = ConvertUtil.getString(item.get("prmCate"));
				sumQuantity += quantity;
				if(PromotionConstants.PROMOTION_DHCP_TYPE_CODE.equals(prmCate) 
						||PromotionConstants.PROMOTION_TZZK_TYPE_CODE.equals(prmCate)){
					sumPrice -= price * quantity;
				}else{
					sumPrice += price * quantity;
					sumOldPrice += oldPrice * quantity;
				}
			}
			resultMap.put("sumPrice", sumPrice);
			resultMap.put("sumOldPrice", sumOldPrice);
			resultMap.put("sumQuantity", sumQuantity);
		}
	}
}
