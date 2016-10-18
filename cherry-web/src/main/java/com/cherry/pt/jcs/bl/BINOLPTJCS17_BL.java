/*
 * @(#)BINOLPTJCS14_BL.java v1.0 2014-6-12
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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;

import com.cherry.bs.common.bl.BINOLBSCOM01_BL;
import com.cherry.cm.activemq.dto.MQInfoDTO;
import com.cherry.cm.activemq.interfaces.BINOLMQCOM01_IF;
import com.cherry.cm.cmbeans.CounterInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM15_BL;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.mq.mes.common.MessageConstants;
import com.cherry.pt.common.ProductConstants;
import com.cherry.pt.jcs.interfaces.BINOLPTJCS17_IF;
import com.cherry.pt.jcs.service.BINOLPTJCS14_Service;
import com.cherry.pt.jcs.service.BINOLPTJCS16_Service;
import com.cherry.pt.jcs.service.BINOLPTJCS17_Service;
import com.cherry.pt.jcs.service.BINOLPTJCS19_Service;
import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

/**
 * 产品方案柜台分配维护BL
 * 
 * @author JiJW
 * @version 1.0 2014-6-12
 */
public class BINOLPTJCS17_BL implements BINOLPTJCS17_IF {
	
	@Resource(name="binOLPTJCS17_Service")
	private BINOLPTJCS17_Service binOLPTJCS17_Service;
	
	@Resource(name="binOLBSCOM01_BL")
	private BINOLBSCOM01_BL binOLBSCOM01_BL;
	
	@Resource(name="binOLMQCOM01_BL")
    private BINOLMQCOM01_IF binOLMQCOM01_BL;
	
	@Resource(name="binOLPTJCS16_Service")
	private BINOLPTJCS16_Service binOLPTJCS16_Service;
	
	@Resource(name="binOLPTJCS14_Service")
	private BINOLPTJCS14_Service binOLPTJCS14_Service;
	
	@Resource
	private BINOLPTJCS19_Service binOLPTJCS19_Service;
	
	/** 取得系统各类编号 */
	@Resource(name="binOLCM15_BL")
	private BINOLCM15_BL binOLCM15_BL;
	
	/** 系统配置项 共通BL */
	@Resource
	private BINOLCM14_BL binOLCM14_BL;
	
	private static final Logger logger = LoggerFactory.getLogger(BINOLPTJCS17_BL.class);
	
	
	/**
	 * 取得方案对应的配置信息
	 */
	public Map<String, Object> getDPConfigDetailBySolu(Map<String, Object> map) throws Exception{
		return binOLPTJCS17_Service.getDPConfigDetailBySolu(map);
	}
	
	/**
	 * 取得分配地点
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List getAllotLocationOld(Map map) throws JSONException ,Exception{
		List resultTreeList = new ArrayList();
		// 取得方案分配活动地点类型
		String locationType = (String) map.get("locationType");
		int loadingCnt = ConvertUtil.getInt(map.get("loadingCnt"));
		// 方案分配地点选择类型--按区域
		if (locationType.equals(ProductConstants.LOTION_TYPE_REGION) || locationType.equals(ProductConstants.LOTION_TYPE_REGION_COUNTER)) {
			if (locationType.equals(ProductConstants.LOTION_TYPE_REGION_COUNTER)) {
				map.put("city_counter", "1");
			}
			// 取得区域信息
			List resultList = binOLPTJCS17_Service.getRegionInfoListOld(map);
			List keysList = new ArrayList();
			String[] keys1 = { "regionId", "regionName" };
			String[] keys2 = { "provinceId", "provinceName" };
			String[] keys3 = { "cityId", "cityName" };
			keysList.add(keys1);
			keysList.add(keys2);
			keysList.add(keys3);
			if (locationType.equals(ProductConstants.LOTION_TYPE_REGION_COUNTER)&& loadingCnt != 0) {
				String[] keys4 = { "counterCode", "counterName" };
				keysList.add(keys4);
			}
			ConvertUtil.jsTreeDataDeepList(resultList, resultTreeList,keysList, 0);
		} else if (locationType.equals(ProductConstants.LOTION_TYPE_CHANNELS)
				|| locationType
				.equals(ProductConstants.LOTION_TYPE_CHANNELS_COUNTER)) {
			if (locationType
					.equals(ProductConstants.LOTION_TYPE_CHANNELS_COUNTER)) {
				map.put("channel_counter", "1");
			}
			List resultList = binOLPTJCS17_Service.getChannelInfoListOld(map);
			List keysList = new ArrayList();
			String[] keys1 = { "id", "name" };
			keysList.add(keys1);
			if (locationType.equals(ProductConstants.LOTION_TYPE_CHANNELS_COUNTER)&& loadingCnt != 0) {
				String[] keys2 = { "counterCode", "counterName" };
				keysList.add(keys2);
			}
			ConvertUtil.jsTreeDataDeepList(resultList, resultTreeList,keysList, 0);
		}
		
		if ((locationType.equals(ProductConstants.LOTION_TYPE_REGION_COUNTER) || locationType.equals(ProductConstants.LOTION_TYPE_CHANNELS_COUNTER))
				&& loadingCnt == 0) {
			ConvertUtil.addArtificialCounterDeep(resultTreeList);
		}
		
		// 处理方案树形结构选中（方案中设置过的产品会被选中）
//		Map<String,Object> departProductConfig = getDepartProductConfig(map);
//		String placeJson = ConvertUtil.getString(departProductConfig.get("PlaceJson"));
//		List<Map<String, Object>> checkedNodes = (List<Map<String, Object>>) JSONUtil.deserialize(placeJson);
		
//		List<Map<String, Object>> nodeList = (List<Map<String, Object>>) JSONUtil.deserialize(resultStr);
		List<Map<String, Object>> nodeList = resultTreeList;
		
		if (null != nodeList) {
			// 设置节点选中状态
//			if (null != checkedNodes) {
//				for (int i = 0; i < checkedNodes.size(); i++) {
//					Map<String, Object> checkedNode = checkedNodes.get(i);
//					ActUtil.setNodes(nodeList, checkedNode);
//				}
//			}
//			resultStr = JSONUtil.serialize(nodeList);
		}
		
		return nodeList;
		
	}
	/**
	 * 取得分配地点
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Map<String,Object> getAllotLocation(Map map) throws JSONException,Exception {
		
		Map<String,Object> resultMap = new HashMap<String, Object>();
		
		// 取得方案分配活动地点类型
		String locationType = (String) map.get("locationType");
		int loadingCnt = ConvertUtil.getInt(map.get("loadingCnt"));
		String placeTypeOld = (String)map.get("placeTypeOld");
		
		// 右边树List(促销地点List)
		List rightTreeList = new ArrayList();
		// 左边树List(候选促销地点List)
		List leftTreeList = new ArrayList();
		
		List<String> saveJsonList = null;
		if(locationType.equalsIgnoreCase(placeTypeOld)){
			// Step1：产品方案与原分配的柜台（区域、渠道、柜台）的产品方案部门关系表记录无效掉validFlag= 0,version=tVeresion+1
			Map<String, Object> oldDpcdMap = binOLPTJCS17_Service.getDPConfigDetailBySolu(map);
			if(null != oldDpcdMap && !oldDpcdMap.isEmpty()){
				String saveJson = (String)oldDpcdMap.get("SaveJson");
				saveJsonList = (List<String>) JSONUtil.deserialize(saveJson);
				map.put("saveJsonList", saveJsonList);
			}
//			List<Map<String, Object>> oldSoluDetailProductDepa rtPriceList = binOLPTJCS17_Service.getSoluDetailProductDepartPriceList(map);
//			if(null != oldSoluDetailProductDepartPriceList && !oldSoluDetailProductDepartPriceList.isEmpty()){
//				String saveJson = (String)oldSoluDetailProductDepartPriceList.get(0).get("SaveJson");
//				saveJsonList = (List<String>) JSONUtil.deserialize(saveJson);
//				map.put("saveJsonList", saveJsonList);
//			}
			
			rightTreeList = this.getRightTreeData(rightTreeList,saveJsonList,locationType, map);
			resultMap.put("rightTreeList", rightTreeList);
			String rightRootNodes = JSONUtil.serialize(rightTreeList);
			resultMap.put("rightRootNodes", rightRootNodes);
			
			leftTreeList = this.getLeftTreeData(leftTreeList, null, locationType, map);
			resultMap.put("leftTreeList", leftTreeList);
			String leftTreNodes = JSONUtil.serialize(leftTreeList);
			resultMap.put("leftTreNodes", leftTreNodes);
		} else{
			// 未绑定方案的地点类型对应的树形
			
			leftTreeList = getAllotLocationOld(map);
			String leftTreNodes = JSONUtil.serialize(leftTreeList);
			resultMap.put("leftTreNodes", leftTreNodes);
			
			resultMap.put("rightRootNodes", "[]");
			
			
		}
		
		
		return resultMap;
	}
	/**
	 * 取得分配地点(门店自设专用)
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Map<String,Object> getAllotLocationCnt(Map map) throws JSONException,Exception {
		
		Map<String,Object> resultMap = new HashMap<String, Object>();
		
		// 门店信息
		CounterInfo counterInfo = (CounterInfo)map.get("cntOwnInfo");
		
		// 右边树List(地点List)
		List rightTreeList = new ArrayList();
		// 左边树List(候选地点List)
		List leftTreeList = new ArrayList();
		
		List<String> saveJsonList = null;
//		if(locationType.equalsIgnoreCase(placeTypeOld)){
			// 取得方案对应的配置信息
			Map<String, Object> oldDpcdMap = binOLPTJCS17_Service.getDPConfigDetailBySolu(map);
			if(null != oldDpcdMap && !oldDpcdMap.isEmpty()){
				String saveJson = (String)oldDpcdMap.get("SaveJson");
				saveJsonList = (List<String>) JSONUtil.deserialize(saveJson);
				map.put("saveJsonList", saveJsonList);
			}
			
			List<Map<String, Object>> cntOwnList = new ArrayList<Map<String,Object>>();
			Map<String,Object> cntOwnMap = new HashMap<String,Object>();
			cntOwnMap.put("id", counterInfo.getCounterCode());
			cntOwnMap.put("name", counterInfo.getCounterName());
			cntOwnList.add(cntOwnMap);
			
			List resultTreeList = new ArrayList();
			// 取得区域信息
			List<Map<String, Object>> allList = new ArrayList<Map<String,Object>>();
			if(!"[]".equals(ConvertUtil.getString(map.get("saveJsonList"))) && !"".equals(ConvertUtil.getString(map.get("saveJsonList")))){
//				allList.addAll(cntOwnList);
				// 设定详细标记
				map.put("detailFlg", "counter");
				map.put("rightTree", "1");
				map.put("city_counter", "1");
				allList = binOLPTJCS17_Service.getRegionInfoListCnt(map);
			}
			if(null != allList && null != saveJsonList){
				for(Map<String,Object> node : allList){
					String counterCode = ConvertUtil.getString(node.get("counterCode"));
					for(String temp : saveJsonList){
						if(counterCode.equalsIgnoreCase(temp)){
						
							
							Map<String,Object> cntMap = new HashMap<String,Object>();
							cntMap.put("id", node.get("counterCode"));
							cntMap.put("name", node.get("counterName"));
							resultTreeList.add(cntMap);
							break;
						}
					}
				}
			}
			
			List keysList = new ArrayList();
			String[] keys1 = { "counterCode", "counterName" };
			keysList.add(keys1);
			ConvertUtil.jsTreeDataDeepList(allList, resultTreeList,keysList, 0);
			
			
			if(CherryUtil.isBlankList(resultTreeList)){
				// 左树
				leftTreeList.addAll(allList.isEmpty()?cntOwnList:resultTreeList);
				resultMap.put("leftTreeList", leftTreeList);
				String leftTreNodes = JSONUtil.serialize(leftTreeList);
				resultMap.put("leftTreNodes", leftTreNodes);
				
				// 右树
				resultMap.put("rightTreeList", new ArrayList<Map<String,Object>>());
				resultMap.put("rightRootNodes", "[]");
			} else{
				// 左树
				resultMap.put("leftTreeList", new ArrayList<Map<String,Object>>());
				resultMap.put("leftTreNodes", "[]");
				
				// 右树
				rightTreeList.addAll(resultTreeList);
				resultMap.put("rightTreeList", rightTreeList);
				String rightRootNodes = JSONUtil.serialize(rightTreeList);
				resultMap.put("rightRootNodes", rightRootNodes);
			}
			
//		} else{
//			// 未绑定方案的地点类型对应的树形
//			
//			leftTreeList = getAllotLocationOld(map);
//			String leftTreNodes = JSONUtil.serialize(leftTreeList);
//			resultMap.put("leftTreNodes", leftTreNodes);
//			
//			resultMap.put("rightRootNodes", "[]");
//			
//			
//		}
		
		
		return resultMap;
	}
	
	/**
	 * 取得分配地点
	 * @param rightTreeList
	 * @param actSglLocationList
	 * @param actLocationType
	 * @param map
	 * @return
	 * @throws JSONException
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List getRightTreeData(List<Map<String, Object>> rightTreeList,List<String> actSglLocationList,String actLocationType,Map<String, Object> map) throws JSONException {
		map.remove("leftTree");
		String detailFlg ="";
		
		map.put("rightTree", "1");
		
		List resultTreeList = new ArrayList();
		// 取得方案分配活动地点类型
		String locationType = (String) map.get("locationType");
		int loadingCnt = ConvertUtil.getInt(map.get("loadingCnt"));
		// 方案分配地点选择类型--按区域
		if (locationType.equals(ProductConstants.LOTION_TYPE_REGION) || locationType.equals(ProductConstants.LOTION_TYPE_REGION_COUNTER)) {
			detailFlg = locationType.equals(ProductConstants.LOTION_TYPE_REGION)?"city":"counter";
			// 设定详细标记
			map.put("detailFlg", detailFlg);
			if (locationType.equals(ProductConstants.LOTION_TYPE_REGION_COUNTER)) {
				map.put("city_counter", "1");
			}
			
			// 取得区域信息
			List resultList = new ArrayList();
			if(!"[]".equals(ConvertUtil.getString(map.get("saveJsonList"))) 
					&& !CherryChecker.isNullOrEmpty(ConvertUtil.getString(map.get("saveJsonList")))){
				resultList = binOLPTJCS17_Service.getRegionInfoList(map);
			}
			List keysList = new ArrayList();
			String[] keys1 = { "regionId", "regionName" };
			String[] keys2 = { "provinceId", "provinceName" };
			String[] keys3 = { "cityId", "cityName" };
			keysList.add(keys1);
			keysList.add(keys2);
			keysList.add(keys3);
			if (locationType.equals(ProductConstants.LOTION_TYPE_REGION_COUNTER)&& loadingCnt != 0) {
				String[] keys4 = { "counterCode", "counterName" };
				keysList.add(keys4);
			}
			ConvertUtil.jsTreeDataDeepList(resultList, resultTreeList,keysList, 0);
		} else if (locationType.equals(ProductConstants.LOTION_TYPE_CHANNELS)|| locationType.equals(ProductConstants.LOTION_TYPE_CHANNELS_COUNTER)) {
			detailFlg = actLocationType.equals(ProductConstants.LOTION_TYPE_CHANNELS)?"channel":"counter";
			// 设定详细标记
			map.put("detailFlg", detailFlg);
			if (locationType.equals(ProductConstants.LOTION_TYPE_CHANNELS_COUNTER)) {
				map.put("channel_counter", "1");
			}
			List resultList = new ArrayList();
			if(!"[]".equals(ConvertUtil.getString(map.get("saveJsonList")))){
				resultList = binOLPTJCS17_Service.getChannelInfoList(map);
			}
			List keysList = new ArrayList();
			String[] keys1 = { "id", "name" };
			keysList.add(keys1);
			if (locationType.equals(ProductConstants.LOTION_TYPE_CHANNELS_COUNTER)&& loadingCnt != 0) {
				String[] keys2 = { "counterCode", "counterName" };
				keysList.add(keys2);
			}
			ConvertUtil.jsTreeDataDeepList(resultList, resultTreeList,keysList, 0);
		} 
		// 导入柜台
		else if (locationType.equals(ProductConstants.LOTION_TYPE_IMPORT_COUNTER)){
			// 设定详细标记
			map.put("detailFlg", "counter");
			map.put("city_counter", "1");
			// 取得区域信息
			List resultList = new ArrayList();
			List<Map<String, Object>> allList = new ArrayList<Map<String,Object>>();
			if(!"[]".equals(ConvertUtil.getString(map.get("saveJsonList")))){
				allList = binOLPTJCS17_Service.getRegionInfoList(map);
			}
			if(null != allList && null != actSglLocationList){
				for(Map<String,Object> node : allList){
					String counterCode = ConvertUtil.getString(node.get("counterCode"));
					for(String temp : actSglLocationList){
						if(counterCode.equalsIgnoreCase(temp)){
							
							Map<String,Object> cntMap = new HashMap<String,Object>();
							cntMap.put("id", node.get("counterCode"));
							cntMap.put("name", node.get("counterName"));
							resultTreeList.add(cntMap);
							break;
						}
					}
				}
			}
			
			List keysList = new ArrayList();
			String[] keys1 = { "counterCode", "counterName" };
			keysList.add(keys1);
			ConvertUtil.jsTreeDataDeepList(allList, resultTreeList,keysList, 0);
		}

		if ((locationType.equals(ProductConstants.LOTION_TYPE_REGION_COUNTER) || locationType.equals(ProductConstants.LOTION_TYPE_CHANNELS_COUNTER))
				&& loadingCnt == 0) {
			ConvertUtil.addArtificialCounterDeep(resultTreeList);
		}
		
		// 处理方案树形结构选中（方案中设置过的产品会被选中）
//		Map<String,Object> departProductConfig = getDepartProductConfig(map);
//		String placeJson = ConvertUtil.getString(departProductConfig.get("PlaceJson"));
//		List<Map<String, Object>> checkedNodes = (List<Map<String, Object>>) JSONUtil.deserialize(placeJson);
		
//		List<Map<String, Object>> nodeList = (List<Map<String, Object>>) JSONUtil.deserialize(resultStr);
		List<Map<String, Object>> nodeList = resultTreeList;
		
		if (null != nodeList) {
			// 设置节点选中状态
//			if (null != checkedNodes) {
//				for (int i = 0; i < checkedNodes.size(); i++) {
//					Map<String, Object> checkedNode = checkedNodes.get(i);
//					ActUtil.setNodes(nodeList, checkedNode);
//				}
//			}
//			resultStr = JSONUtil.serialize(nodeList);
		}
		
		return nodeList;

	}
	
	/**
	 * 取得分配地点
	 * @param rightTreeList
	 * @param actSglLocationList
	 * @param actLocationType
	 * @param map
	 * @return
	 * @throws JSONException
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List getLeftTreeData(List<Map<String, Object>> rightTreeList,List<Map<String, Object>> actSglLocationList,String actLocationType,Map<String, Object> map) throws JSONException {
		map.remove("rightTree");
		String detailFlg ="";
		map.put("leftTree", "1");
		
		List resultTreeList = new ArrayList();
		// 取得方案分配活动地点类型
		String locationType = (String) map.get("locationType");
		int loadingCnt = ConvertUtil.getInt(map.get("loadingCnt"));
		// 方案分配地点选择类型--按区域
		if (locationType.equals(ProductConstants.LOTION_TYPE_REGION) || locationType.equals(ProductConstants.LOTION_TYPE_REGION_COUNTER)) {
			detailFlg = locationType.equals(ProductConstants.LOTION_TYPE_REGION)?"city":"counter";
			// 设定详细标记
			map.put("detailFlg", detailFlg);

			if (locationType.equals(ProductConstants.LOTION_TYPE_REGION_COUNTER)) {
				map.put("city_counter", "1");
			}
			
			// 取得区域信息
			List resultList = binOLPTJCS17_Service.getRegionInfoList(map);
			List keysList = new ArrayList();
			String[] keys1 = { "regionId", "regionName" };
			String[] keys2 = { "provinceId", "provinceName" };
			String[] keys3 = { "cityId", "cityName" };
			keysList.add(keys1);
			keysList.add(keys2);
			keysList.add(keys3);
			if (locationType.equals(ProductConstants.LOTION_TYPE_REGION_COUNTER)&& loadingCnt != 0) {
				String[] keys4 = { "counterCode", "counterName" };
				keysList.add(keys4);
			}
			ConvertUtil.jsTreeDataDeepList(resultList, resultTreeList,keysList, 0);
		} else if (locationType.equals(ProductConstants.LOTION_TYPE_CHANNELS) || locationType.equals(ProductConstants.LOTION_TYPE_CHANNELS_COUNTER)) {
			detailFlg = actLocationType.equals(ProductConstants.LOTION_TYPE_CHANNELS)?"channel":"counter";
			// 设定详细标记
			map.put("detailFlg", detailFlg);
			if (locationType.equals(ProductConstants.LOTION_TYPE_CHANNELS_COUNTER)) {
				map.put("channel_counter", "1");
			}
			List resultList = binOLPTJCS17_Service.getChannelInfoList(map);
			List keysList = new ArrayList();
			String[] keys1 = { "id", "name" };
			keysList.add(keys1);
			if (locationType.equals(ProductConstants.LOTION_TYPE_CHANNELS_COUNTER)&& loadingCnt != 0) {
				String[] keys2 = { "counterCode", "counterName" };
				keysList.add(keys2);
			}
			ConvertUtil.jsTreeDataDeepList(resultList, resultTreeList,keysList, 0);
		}
		// 导入柜台
		else if (locationType.equals(ProductConstants.LOTION_TYPE_IMPORT_COUNTER)){
			// 因为是导入柜台，所以左树直接显示为空
		}
		
		if ((locationType.equals(ProductConstants.LOTION_TYPE_REGION_COUNTER) || locationType.equals(ProductConstants.LOTION_TYPE_CHANNELS_COUNTER))
				&& loadingCnt == 0) {
			ConvertUtil.addArtificialCounterDeep(resultTreeList);
		}
		
		// 处理方案树形结构选中（方案中设置过的产品会被选中）
//		Map<String,Object> departProductConfig = getDepartProductConfig(map);
//		String placeJson = ConvertUtil.getString(departProductConfig.get("PlaceJson"));
//		List<Map<String, Object>> checkedNodes = (List<Map<String, Object>>) JSONUtil.deserialize(placeJson);
		
//		List<Map<String, Object>> nodeList = (List<Map<String, Object>>) JSONUtil.deserialize(resultStr);
		List<Map<String, Object>> nodeList = resultTreeList;
		
		if (null != nodeList) {
			// 设置节点选中状态
//			if (null != checkedNodes) {
//				for (int i = 0; i < checkedNodes.size(); i++) {
//					Map<String, Object> checkedNode = checkedNodes.get(i);
//					ActUtil.setNodes(nodeList, checkedNode);
//				}
//			}
//			resultStr = JSONUtil.serialize(nodeList);
		}
		
		return nodeList;
		
	}
	
	/**
	 * 保存柜台产品配置信息---方案地点
	 * @param map
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@CacheEvict(value="CherryProductCache",allEntries=true,beforeInvocation=false)
	public int tran_addConfigDetailSave(Map<String, Object> map) throws CherryException,Exception{
		int result = 1;
		try{
			
			Map<String, Object> seqMap = new HashMap<String, Object>();
			seqMap.putAll(map);
			seqMap.put("type", "F");
			String tVersion = binOLCM15_BL.getCurrentSequenceId(seqMap);
			map.put("tVersion", tVersion);
			
			// Step1：产品方案与原分配的柜台（区域、渠道、柜台）的产品方案部门关系表记录无效掉validFlag= 0,version=tVeresion+1
			Map<String, Object> oldDpcdMap = binOLPTJCS17_Service.getDPConfigDetailBySolu(map);
			
			if(null != oldDpcdMap && !oldDpcdMap.isEmpty()){
				oldDpcdMap.putAll(map);
				
				List<Map<String, Object>> cntList = getPrtSoluWithDepartHis(map);
				if(null != cntList && !cntList.isEmpty()){
					for(Map<String,Object> cntMap : cntList){
						oldDpcdMap.put("CounterCode", cntMap.get("CounterCode"));
						oldDpcdMap.put("psdValidFlag", CherryConstants.VALIDFLAG_DISABLE);
						binOLPTJCS17_Service.updPrtSoluDepartRelation(oldDpcdMap);
					}
				}
				
				/*
				// 地点类型
				String placeType = (String)oldDpcdMap.get("PlaceType");
				String saveJson = (String)oldDpcdMap.get("SaveJson");
				List<String> saveJsonList = (List<String>) JSONUtil.deserialize(saveJson);
				
				// 区域城市
				if(placeType.equals(ProductConstants.LOTION_TYPE_REGION)){
					// 取得权限区域城市的柜台
					oldDpcdMap.put("city", 1);
					oldDpcdMap.put("cityList", saveJsonList);
					List<Map<String, Object>> cntList = binOLPTJCS17_Service.getCounterInfoList(oldDpcdMap);
					if(null != cntList && !cntList.isEmpty()){
						for(Map<String,Object> cntMap : cntList){
							oldDpcdMap.putAll(cntMap);
							// Step1.1：将方案原关联的柜台对应的关系表记录无效掉
							oldDpcdMap.put("psdValidFlag", CherryConstants.VALIDFLAG_DISABLE);
							binOLPTJCS17_Service.updPrtSoluDepartRelation(oldDpcdMap);
						}
					}
				}
				// 按区域并且指定柜台 或 按渠道并且指定柜台
				else if (placeType.equals(ProductConstants.LOTION_TYPE_CHANNELS_COUNTER)
						|| placeType.equals(ProductConstants.LOTION_TYPE_REGION_COUNTER) 
						|| placeType.equals(ProductConstants.LOTION_TYPE_IMPORT_COUNTER)) {
					
					for(String cntCode : saveJsonList){
						oldDpcdMap.put("CounterCode", cntCode);
						// Step1.1：将方案原关联的柜台对应的柜台产品记录无效掉
						oldDpcdMap.put("psdValidFlag", CherryConstants.VALIDFLAG_DISABLE);
						binOLPTJCS17_Service.updPrtSoluDepartRelation(oldDpcdMap);
					}
				}
				
				// 渠道
				else if(placeType.equals(ProductConstants.LOTION_TYPE_CHANNELS)){
					// 取得权限渠道的柜台
					oldDpcdMap.put("channel", 1);
					oldDpcdMap.put("channelList", saveJsonList);
					List<Map<String, Object>> channelList = binOLPTJCS17_Service.getCounterInfoList(oldDpcdMap);
					if(null != channelList && !channelList.isEmpty()){
						for(Map<String,Object> channelMap : channelList){
							oldDpcdMap.putAll(channelMap);
							// Step1.1：将方案原关联的柜台对应的柜台产品记录无效掉
							oldDpcdMap.put("psdValidFlag", CherryConstants.VALIDFLAG_DISABLE);
							binOLPTJCS17_Service.updPrtSoluDepartRelation(oldDpcdMap);
						}
					}
				}
				*/
				
			}
			
			// Step2: 更merge新柜台产品配置明细信息(saveJson、placeType等)
			binOLPTJCS17_Service.mergeDepartProductConfigDetail(map);
			
			// Step3: 方案与新分配柜台（区域、渠道、柜台）对应的柜台 merge 到产品方案部门关系表
			Map<String, Object> newDpcdMap = binOLPTJCS17_Service.getDPConfigDetailBySolu(map);
			
			if(null != newDpcdMap && !newDpcdMap.isEmpty()){
				// 如果方案无效，那么插入的产品方案部门关系数据都是无效的。（为什么只用方案ValidFlag判定，不加上柜台？因为这里抽出的柜台都是有效的）
				newDpcdMap.put("ValidFlag", newDpcdMap.get("ppsValidFlag")); 
				newDpcdMap.putAll(map);
				// 地点类型
				String placeType = (String)newDpcdMap.get("PlaceType");
				String saveJson = (String)newDpcdMap.get("SaveJson");
				List<String> saveJsonList = (List<String>) JSONUtil.deserialize(saveJson);
				
				if(!CherryUtil.isBlankList(saveJsonList)){
					
					// 区域城市
					if(placeType.equals(ProductConstants.LOTION_TYPE_REGION)){
						// 取得权限区域城市的柜台
						newDpcdMap.put("city", 1);
						newDpcdMap.put("cityList", saveJsonList);
						List<Map<String, Object>> cntList = binOLPTJCS17_Service.getCounterInfoList(newDpcdMap);
						if(null != cntList && !cntList.isEmpty()){
							for(Map<String,Object> cntMap : cntList){
								newDpcdMap.putAll(cntMap);
								// Step3.1: merge产品方案部门关系表
								binOLPTJCS17_Service.mergePrtSoluDepartRelation(newDpcdMap);
								
							}
						}
					}
					// 按区域并且指定柜台 或 按渠道并且指定柜台
					else if (placeType.equals(ProductConstants.LOTION_TYPE_CHANNELS_COUNTER)
							|| placeType.equals(ProductConstants.LOTION_TYPE_REGION_COUNTER)
							|| placeType.equals(ProductConstants.LOTION_TYPE_IMPORT_COUNTER)
							|| placeType.equals("7")) {
						
						for(String cntCode : saveJsonList){
							newDpcdMap.put("CounterCode", cntCode);
							// Step3.1: merge产品方案部门关系表
							binOLPTJCS17_Service.mergePrtSoluDepartRelation(newDpcdMap);
						}
					}
					
					// 渠道
					else if(placeType.equals(ProductConstants.LOTION_TYPE_CHANNELS)){
						// 取得权限渠道的柜台
						newDpcdMap.put("channel", 1);
						newDpcdMap.put("channelList", saveJsonList);
						List<Map<String, Object>> channelList = binOLPTJCS17_Service.getCounterInfoList(newDpcdMap);
						if(null != channelList && !channelList.isEmpty()){
							for(Map<String,Object> channelMap : channelList){
								newDpcdMap.putAll(channelMap);
								// Step3.1: merge产品方案部门关系表
								binOLPTJCS17_Service.mergePrtSoluDepartRelation(newDpcdMap);
							}
						}
					}
				}
				
			}
			// Step4: 更新产品方案配置履历表(后加的，为了解决区域)
			Map<String, Object> hisMap = new HashMap<String, Object>();
			hisMap.putAll(map);
			// 地点类型
			String placeType = (String)newDpcdMap.get("PlaceType");
			hisMap.put("PlaceType", placeType);
			// 地点集合
			String saveJson = (String)newDpcdMap.get("SaveJson");
			hisMap.put("SaveJson", saveJson);
			
			// 更新产品方案配置履历表
			updPrtSoluWithDepartHis(hisMap);
			
		} catch(Exception e){
			result = 0;
			logger.error(e.getMessage(), e);
			throw new CherryException("方案分配失败，请联系管理员。");
		}
		return result;
	}
	
	/**
	 * 取得方案对应的原柜台List
	 * @param map
	 */
	public List<Map<String, Object>> getPrtSoluWithDepartHis(Map<String, Object> map) throws Exception {
		List<Map<String, Object>> cntList =  binOLPTJCS17_Service.getPrtSoluWithDepartHis(map); 
		return cntList;
	}
	
	/**
	 * 保存柜台产品配置信息---产品地点
	 * @param map
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public int tran_addConfigDetailSaveOld(Map<String, Object> map) throws CherryException,Exception{
		int result = 1;
		/*
		try{
			
			Map<String, Object> seqMap = new HashMap<String, Object>();
			seqMap.putAll(map);
			seqMap.put("type", "F");
			String tVersion = binOLCM15_BL.getCurrentSequenceId(seqMap);
			map.put("tVersion", tVersion);
			
			// Step1：产品方案明细与原分配的柜台（区域、渠道、柜台）的柜台产品表记录无效掉validFlag= 0,version=tVeresion+1
			List<Map<String, Object>> oldSoluDetailProductDepartPriceList = binOLPTJCS17_Service.getSoluDetailProductDepartPriceList(map);
			if(null != oldSoluDetailProductDepartPriceList && !oldSoluDetailProductDepartPriceList.isEmpty()){
				for(Map<String, Object> itemMap : oldSoluDetailProductDepartPriceList){
					itemMap.put("soluStartDate", itemMap.get("StartDate"));
					itemMap.put("soluEndDate", itemMap.get("EndDate"));
					itemMap.putAll(map);
					// 地点类型
					String placeType = (String)itemMap.get("PlaceType");
					String saveJson = (String)itemMap.get("SaveJson");
					List<String> saveJsonList = (List<String>) JSONUtil.deserialize(saveJson);
					
					// 区域城市
					if(placeType.equals(ProductConstants.LOTION_TYPE_REGION)){
						// 取得权限区域城市的柜台
						itemMap.put("city", 1);
						itemMap.put("cityList", saveJsonList);
						List<Map<String, Object>> cntList = binOLPTJCS17_Service.getCounterInfoList(itemMap);
						if(null != cntList && !cntList.isEmpty()){
							for(Map<String,Object> cntMap : cntList){
								itemMap.putAll(cntMap);
								// Step1.1：将方案原关联的柜台对应的柜台产品记录无效掉
								binOLPTJCS17_Service.updProductDepart(itemMap);
								// Step1.2: 将方案原关联的柜台对应的部门产品价格删除。
								binOLPTJCS17_Service.delProductDepartPrice(itemMap);
							}
						}
					}
					// 按区域并且指定柜台 或 按渠道并且指定柜台
					else if (placeType.equals(ProductConstants.LOTION_TYPE_CHANNELS_COUNTER)
							|| placeType.equals(ProductConstants.LOTION_TYPE_REGION_COUNTER) 
							|| placeType.equals(ProductConstants.LOTION_TYPE_IMPORT_COUNTER)) {
						
						for(String cntCode : saveJsonList){
							itemMap.put("CounterCode", cntCode);
							// Step1.1：将方案原关联的柜台对应的柜台产品记录无效掉
							binOLPTJCS17_Service.updProductDepart(itemMap);
							// Step1.2: 将方案原关联的柜台对应的部门产品价格删除。
							binOLPTJCS17_Service.delProductDepartPrice(itemMap);
						}
					}
					
					// 渠道
					else if(placeType.equals(ProductConstants.LOTION_TYPE_CHANNELS)){
						// 取得权限渠道的柜台
						itemMap.put("channel", 1);
						itemMap.put("channelList", saveJsonList);
						List<Map<String, Object>> channelList = binOLPTJCS17_Service.getCounterInfoList(itemMap);
						if(null != channelList && !channelList.isEmpty()){
							for(Map<String,Object> channelMap : channelList){
								itemMap.putAll(channelMap);
								// Step1.1：将方案原关联的柜台对应的柜台产品记录无效掉
								binOLPTJCS17_Service.updProductDepart(itemMap);
								// Step1.2：将方案原关联的柜台对应的部门产品价格删除。
								binOLPTJCS17_Service.delProductDepartPrice(itemMap);
							}
						}
					}
					
				}
			}
			
			// Step2: 更merge新柜台产品配置明细信息(saveJson、placeType等)
			binOLPTJCS17_Service.mergeDepartProductConfigDetail(map);
			
			// Step3: 方案明细与新分配柜台（区域、渠道、柜台）对应的柜台产品 merge 到柜台产品表
			List<Map<String, Object>> newSoluDetailProductDepartPriceList = binOLPTJCS17_Service.getSoluDetailProductDepartPriceList(map);
			
			if(null != newSoluDetailProductDepartPriceList && !newSoluDetailProductDepartPriceList.isEmpty()){
				for(Map<String, Object> newItemMap : newSoluDetailProductDepartPriceList){
					newItemMap.put("soluStartDate", newItemMap.get("StartDate"));
					newItemMap.put("soluEndDate", newItemMap.get("EndDate"));
					newItemMap.putAll(map);
					// 地点类型
					String placeType = (String)newItemMap.get("PlaceType");
					String saveJson = (String)newItemMap.get("SaveJson");
					List<String> saveJsonList = (List<String>) JSONUtil.deserialize(saveJson);
					
					// 区域城市
					if(placeType.equals(ProductConstants.LOTION_TYPE_REGION)){
						// 取得权限区域城市的柜台
						newItemMap.put("city", 1);
						newItemMap.put("cityList", saveJsonList);
						List<Map<String, Object>> cntList = binOLPTJCS17_Service.getCounterInfoList(newItemMap);
						if(null != cntList && !cntList.isEmpty()){
							for(Map<String,Object> cntMap : cntList){
								newItemMap.putAll(cntMap);
								// 设置部门产品表相关属性(status、validFlag)
//								setProductDepart(newItemMap);
								
								// Step3.1: 插入产品部门表
								binOLPTJCS17_Service.mergeProductDepartInfo(newItemMap);
								
								// Step3.2: 插入柜台价格到产品价格表中
								newItemMap.put("type", "3"); // 价格类型为部门价格
								newItemMap.put("priceJson", newItemMap.get("PriceJson")); 
								newItemMap.put("productId", newItemMap.get("BIN_ProductID")); 
								newItemMap.put("departCode", newItemMap.get("CounterCode")); 
								optPriceNew(newItemMap);
							}
						}
					}
					// 按区域并且指定柜台 或 按渠道并且指定柜台
					else if (placeType.equals(ProductConstants.LOTION_TYPE_CHANNELS_COUNTER)
							|| placeType.equals(ProductConstants.LOTION_TYPE_REGION_COUNTER)
							|| placeType.equals(ProductConstants.LOTION_TYPE_IMPORT_COUNTER)) {
						
						for(String cntCode : saveJsonList){
							newItemMap.put("CounterCode", cntCode);
							// 设置部门产品表相关属性(status、validFlag)
//							setProductDepart(newItemMap);
							
							// Step3.1: 插入产品部门表
							binOLPTJCS17_Service.mergeProductDepartInfo(newItemMap);
							
							// Step3.2: 插入柜台价格到产品价格表中
							newItemMap.put("type", "3"); // 价格类型为部门价格
							newItemMap.put("priceJson", newItemMap.get("PriceJson")); 
							newItemMap.put("productId", newItemMap.get("BIN_ProductID")); 
							newItemMap.put("departCode", newItemMap.get("CounterCode")); 
							optPriceNew(newItemMap);
						}
					}
					
					// 渠道
					else if(placeType.equals(ProductConstants.LOTION_TYPE_CHANNELS)){
						// 取得权限渠道的柜台
						newItemMap.put("channel", 1);
						newItemMap.put("channelList", saveJsonList);
						List<Map<String, Object>> channelList = binOLPTJCS17_Service.getCounterInfoList(newItemMap);
						if(null != channelList && !channelList.isEmpty()){
							for(Map<String,Object> channelMap : channelList){
								newItemMap.putAll(channelMap);
								// 设置部门产品表相关属性(status、validFlag)
//								setProductDepart(newItemMap);
								
								// Step3.1: 插入产品部门表
								binOLPTJCS17_Service.mergeProductDepartInfo(newItemMap);
								
								// Step3.2: 插入柜台价格到产品价格表中
								newItemMap.put("type", "3"); // 价格类型为部门价格
								newItemMap.put("priceJson", newItemMap.get("PriceJson")); 
								newItemMap.put("productId", newItemMap.get("BIN_ProductID")); 
								newItemMap.put("departCode", newItemMap.get("CounterCode")); 
								optPriceNew(newItemMap);
							}
						}
					}
				}
			}
			
			// Step4: 更新产品方案配置履历表(后加的，为了解决区域)
			Map<String, Object> hisMap = binOLPTJCS17_Service.getDPConfigDetailBySolu(map);
			hisMap.putAll(map);
			// 地点类型
			String placeType = (String)newSoluDetailProductDepartPriceList.get(0).get("PlaceType");
			hisMap.put("placeType", placeType);
			// 地点集合
			String saveJson = (String)newSoluDetailProductDepartPriceList.get(0).get("SaveJson");
			hisMap.put("SaveJson", saveJson);
			
			// 更新产品方案配置履历表
			updPrtSoluWithDepartHis(hisMap);
				
				// Step5: END
				
			
		} catch(Exception e){
			result = 0;
			logger.error(e.getMessage(), e);
			throw new CherryException("方案分配失败，请联系管理员。");
		}
		*/
		return result;
	}
	
	/**
	 * 更新产品方案配置履历表  
	 * @param map
	 * @return 
	 */
	public String mergePrtSoluWithDepartHis(Map<String, Object> map){
		return binOLPTJCS17_Service.mergePrtSoluWithDepartHis(map);
	}
	
	/**
	 * 价格处理
	 * @param prtSoluMap
	 * @throws JSONException
	 */
	@SuppressWarnings("unchecked")
	private void optPriceNew(Map<String, Object> prtSoluMap) throws JSONException{
		// 价格
		String priceInfo = ConvertUtil.getString(prtSoluMap.get("priceJson"));
		
		if (!CherryConstants.BLANK.equals(priceInfo)) {
			List<Map<String, Object>> priceInfoList = (List<Map<String, Object>>) JSONUtil.deserialize(priceInfo);
			
			// 价格按折扣率计算
			setPriceRate(priceInfoList, prtSoluMap);
			
			if (null != priceInfoList) {
				int size = priceInfoList.size();
				for (int i = 0; i < size; i++) {
					Map<String, Object> priceMap = priceInfoList.get(i);
					priceMap = CherryUtil.removeEmptyVal(priceMap);
					// 操作
					String option = ConvertUtil.getString(priceMap.get(ProductConstants.OPTION));
					
					priceMap.putAll(prtSoluMap);
					priceMap.put(ProductConstants.PRICESTARTDATE, prtSoluMap.get("soluStartDate"));
					priceMap.put(ProductConstants.PRICEENDDATE, prtSoluMap.get("soluEndDate"));
					
					if (CherryConstants.BLANK.equals(option) || ProductConstants.OPTION_1.equals(option)) {
						// 插入产品销售价格
						binOLPTJCS16_Service.insertProductPrice(priceMap);
					} 
				}
			}
		}
		
	}
	
	/**
	 * 取得业务日期
	 * 
	 * @param map
	 * @return
	 */
	public Map<String, Object> getBussinessDateMap(Map<String, Object> map) {
		return binOLPTJCS17_Service.getBussinessDateMap(map);
	}
	
	/**
	 * 柜台方案实时下发
	 * @param map
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@CacheEvict(value="CherryProductCache",allEntries=true,beforeInvocation=false)
	public Map<String,Object> tran_issuedCntPrt(Map<String, Object> map) throws Exception{
		
		Map<String, Object> result = new HashMap<String, Object>();
		
		// 取得当前柜台产品表版本号
		Map<String, Object> seqMap = new HashMap<String, Object>();
		seqMap.putAll(map);
		seqMap.put("type", "F");
		String tVersion = binOLCM15_BL.getCurrentSequenceId(seqMap);
		map.put("tVersion", tVersion);
		
		try{
			// Step0: 取得方案配置的区域或渠道实际的的柜台与以前配置的差异(区域城市/渠道)List
			updPrtSoluCityChannelDiff(map);
			
			// Step1: 更新接口数据库
			int updResult = updIFDatabase(map);
			
			// Step2: 发送MQ通知
			if(updResult > 0){
				// 产品表的表版本号在下发成功后+1
				String newTVersion = binOLCM15_BL.getNoPadLeftSequenceId(seqMap);
				map.put("newTVersion", newTVersion);
				
				Map<String,Object> MQMap = binOLBSCOM01_BL.getPrtNoticeMqMap(map, MessageConstants.MSG_SPRT_DPRT);
				if(MQMap.isEmpty()){
					throw new Exception("柜台产品实时下发通知组装异常");
				}
				
				//设定MQInfoDTO
				MQInfoDTO mqDTO = binOLBSCOM01_BL.setMQInfoDTO(MQMap,map);
				//调用共通发送MQ消息
				mqDTO.setMsgQueueName(CherryConstants.cherryToPosCMD);
				binOLMQCOM01_BL.sendMQMsg(mqDTO,false);
				
			}
		}catch(Exception e){
			
			logger.error(e.getMessage(),e);
			e.printStackTrace();
			
			try{
				// 所有数据回滚，否则版本号无法控制
				binOLPTJCS17_Service.manualRollback();
				binOLPTJCS17_Service.ifManualRollback();
				throw new Exception(e);
			} catch(Exception ee){
				logger.error(ee.getMessage(),ee);
				ee.printStackTrace();
				throw new Exception(ee);
				
			}
			
		}
			
		// 成功
		result.put("result", "0");
		
		return result;
	}
	
	/**
	 * 处理方案配置的区域或渠道实际的的柜台与以前配置的差异
	 * Step1 : 比较方案案配置的区域或渠道实际的的柜台与以前配置的差异
	 * Step2 : 将差异更新到部门产品表及部门产品价格
	 * Step3 : 删除方案对应的履历数据，并将最新的方案配置信息插入到履历表
	 * @param map
	 * @throws JSONException 
	 */
	@SuppressWarnings("unchecked")
	private void updPrtSoluCityChannelDiff(Map<String, Object> map) throws JSONException,Exception{
		
		// 定义查询的方案配置信息为区域城市/渠道
//		String [] palceTypeList = new String [] {"1","3"};
//		map.put("palceTypeList", palceTypeList);
		
		// 取得产品方案配置信息List(UpdateTime升序,包括区域、城市、指定柜台等)
		List<Map<String, Object>> dpcdList = binOLPTJCS17_Service.getDPConfigDetailList(map);
		
		if (!CherryUtil.isBlankList(dpcdList)) {
			
			for(Map<String, Object> itemMap : dpcdList){
				
				String placeType = (String)itemMap.get("PlaceType");
				if(placeType.equals(ProductConstants.LOTION_TYPE_REGION)){
					itemMap.put("city", 1);
				}
				else if(placeType.equals(ProductConstants.LOTION_TYPE_CHANNELS)){
					itemMap.put("channel", 1);
				}else if(placeType.equals(ProductConstants.LOTION_TYPE_CHANNELS_COUNTER)
						|| placeType.equals(ProductConstants.LOTION_TYPE_REGION_COUNTER)
						|| placeType.equals(ProductConstants.LOTION_TYPE_IMPORT_COUNTER)
						|| placeType.equals("7")){
					itemMap.put("counter", 1);
				}
				
				itemMap.putAll(map);
				
				// Step1 : 取得方案配置的区域或渠道实际的的柜台与以前配置的差异(区域城市/渠道)List
				List<Map<String, Object>> cntForPrtSoluCityChannelDiff = binOLPTJCS17_Service.getCntForPrtSoluCityChannelDiff(itemMap);
				if (!CherryUtil.isBlankList(cntForPrtSoluCityChannelDiff)) {
					for(Map<String, Object> diffMap : cntForPrtSoluCityChannelDiff){
						
						// Step2 : 将差异更新到产品方案部门关系表
						diffMap.putAll(map);
						diffMap.put("productPriceSolutionID", diffMap.get("BIN_SolutionId"));
						String modifyFlag = (String)diffMap.get("modifyFlag"); // modifyFlag  add 增加的柜台 、sub减少的柜台
						
						// 取得当前方案及增加的柜台,merge到产品方案部门关系表 validFlag = 1,version = tversion +1
						if("add".equals(modifyFlag)){
							diffMap.put("ValidFlag", CherryConstants.VALIDFLAG_ENABLE);
							diffMap.put("CounterCode", diffMap.get("CntPD"));
							// 1: 插入产品部门表
							binOLPTJCS17_Service.mergePrtSoluDepartRelation(diffMap);
							
						}
						// 取得当前方案及减少的柜台,merge到产品方案部门关系表 validFlag = 0,version = tversion +1
						else if ("sub".equals(modifyFlag)){
							
							diffMap.put("CounterCode", diffMap.get("CntPDH"));
							// 1: 将方案与原柜台关联的数据无效掉
							diffMap.put("psdValidFlag", CherryConstants.VALIDFLAG_DISABLE);
							binOLPTJCS17_Service.updPrtSoluDepartRelation(diffMap);
						}
						
					}
					
					// Step3 : 更新产品方案配置履历表
					updPrtSoluWithDepartHis(itemMap);
				}
				
			}
			
		}
	}
	
	/**
	 * 处理方案配置的区域或渠道实际的的柜台与以前配置的差异
	 * Step1 : 比较方案案配置的区域或渠道实际的的柜台与以前配置的差异
	 * Step2 : 将差异更新到部门产品表及部门产品价格
	 * Step3 : 删除方案对应的履历数据，并将最新的方案配置信息插入到履历表
	 * @param map
	 * @throws JSONException 
	 */
	@SuppressWarnings("unchecked")
	private void updPrtSoluCityChannelDiffOld(Map<String, Object> map) throws JSONException,Exception{
		/*
		// 定义查询的方案配置信息为区域城市/渠道
		String [] palceTypeList = new String [] {"1","3"};
		map.put("palceTypeList", palceTypeList);
		
		// 取得产品方案配置信息List(UpdateTime升序)
		List<Map<String, Object>> dpcdList = binOLPTJCS17_Service.getDPConfigDetailList(map);
		
		if (!CherryUtil.isBlankList(dpcdList)) {
			
			for(Map<String, Object> itemMap : dpcdList){
				
				String placeType = (String)itemMap.get("PlaceType");
				if(placeType.equals(ProductConstants.LOTION_TYPE_REGION)){
					itemMap.put("city", 1);
				}
				else if(placeType.equals(ProductConstants.LOTION_TYPE_CHANNELS)){
					itemMap.put("channel", 1);
				}
				
				itemMap.putAll(map);
				// String productPriceSolutionID = (String)itemMap.get("productPriceSolutionID");
				
				// Step1 : 取得方案配置的区域或渠道实际的的柜台与以前配置的差异(区域城市/渠道)List
				List<Map<String, Object>> cntForPrtSoluCityChannelDiff = binOLPTJCS17_Service.getCntForPrtSoluCityChannelDiff(itemMap);
				if (!CherryUtil.isBlankList(cntForPrtSoluCityChannelDiff)) {
					for(Map<String, Object> diffMap : cntForPrtSoluCityChannelDiff){

						// 取方案明细的产品信息List
						List<Map<String, Object>> prtPriceSoluDetailList = binOLPTJCS17_Service.getPrtPriceSoluDetailList(itemMap);
						
						// Step2 : 将差异更新到部门产品表及部门产品价格
						String modifyFlag = (String)diffMap.get("modifyFlag"); // modifyFlag  add 增加的柜台 、sub减少的柜台
						// 取得当前方案对应的产品及增加的柜台,merge到部门产品表及价格表 validFlag = 1,version = tversion +1
						if("add".equals(modifyFlag)){
							
							if (!CherryUtil.isBlankList(prtPriceSoluDetailList)) {
								for (Map<String, Object> prtPriceSoluDetailMap : prtPriceSoluDetailList) {
									prtPriceSoluDetailMap.putAll(map);
									prtPriceSoluDetailMap.put("CounterCode", diffMap.get("CntPD"));
									// 设置部门产品表相关属性(status、validFlag)
									setProductDepart(prtPriceSoluDetailMap);
									
									// 1: 插入产品部门表
									binOLPTJCS17_Service.mergeProductDepartInfo(prtPriceSoluDetailMap);

									// 2: 插入柜台价格到产品价格表中
									prtPriceSoluDetailMap.put("type", "3"); // 价格类型为部门价格
									prtPriceSoluDetailMap.put("departCode", diffMap.get("CntPD"));
									prtPriceSoluDetailMap.put("productId",prtPriceSoluDetailMap.get("BIN_ProductID"));
									prtPriceSoluDetailMap.put("priceJson",prtPriceSoluDetailMap.get("PriceJson"));
									prtPriceSoluDetailMap.put("soluStartDate", prtPriceSoluDetailMap.get("StartDate"));
									prtPriceSoluDetailMap.put("soluEndDate", prtPriceSoluDetailMap.get("EndDate"));
									optPriceNew(prtPriceSoluDetailMap);
								}
							}
							
						}
						// 取得当前方案对应的产品及增加的柜台,merge到部门产品表及价格表 validFlag = 0,version = tversion +1
						else if ("sub".equals(modifyFlag)){
							
							if (!CherryUtil.isBlankList(prtPriceSoluDetailList)) {
								for(Map<String, Object> prtPriceSoluDetailMap : prtPriceSoluDetailList){
									prtPriceSoluDetailMap.putAll(map);
									prtPriceSoluDetailMap.put("CounterCode", diffMap.get("CntPDH"));
									
									prtPriceSoluDetailMap.put("organizationInfoId", map.get("organizationInfoId"));
									prtPriceSoluDetailMap.put("brandInfoId", map.get("brandInfoId"));
									
									// 1：将方案原关联的柜台对应的柜台产品记录无效掉
									binOLPTJCS17_Service.updProductDepart(prtPriceSoluDetailMap);
									// 2: 将方案原关联的柜台对应的部门产品价格删除。
									binOLPTJCS17_Service.delProductDepartPrice(prtPriceSoluDetailMap);
								}
							}
						}
						
					}
				}
				
				// Step3 : 更新产品方案配置履历表
				updPrtSoluWithDepartHis(itemMap);
				
			}
			
		}
		*/
	}
	
	/**
	 * 更新产品方案配置履历表
	 * 
	 * 删除方案对应的履历数据，并将最新的方案配置信息插入到履历表
	 * 
     * @param praMap
     * praMap参数说明：productPriceSolutionID （方案ID）,
     * praMap参数说明：placeType（地点类型）,
     * praMap参数说明：SaveJson（地点集合）,
     * praMap参数说明：组织ID,品牌ID等共通信息,
	 * @throws JSONException,Exception 
	 */
	@SuppressWarnings("unchecked")
	public void updPrtSoluWithDepartHis(Map<String,Object> map) throws JSONException,Exception{
		
		//  更新产品方案配置履历表(后加的，为了解决区域)
	
		// 删除方案配置履历表中指定的方案
		binOLPTJCS17_Service.delPrtSoluWithDepartHis(map);
		
		Map<String, Object> hisMap = new HashMap<String, Object>();
		hisMap.putAll(map);
		
		// 地点类型
		String placeType = (String)map.get("PlaceType");
		hisMap.put("placeType", placeType);
		// 地点集合
		String saveJson = (String)map.get("SaveJson");
		List<Long> saveJsonList = (List<Long>) JSONUtil.deserialize(saveJson);
		
		if(!CherryUtil.isBlankList(saveJsonList)){
			String placeTypeFlag = "counter"; // 区别方案分配地点类型属性柜台类还是区域城市/渠道类（用于ibatis查询时动态生成SQL）
			if(placeType.equals(ProductConstants.LOTION_TYPE_REGION_COUNTER)
					|| placeType.equals(ProductConstants.LOTION_TYPE_CHANNELS_COUNTER)
					|| placeType.equals(ProductConstants.LOTION_TYPE_IMPORT_COUNTER)
					|| placeType.equals("7")) {
				
				placeTypeFlag = "counter";
				hisMap.put("placeTypeFlag", placeTypeFlag);
				hisMap.put("place", 0);
				this.mergePrtSoluWithDepartHis(hisMap);
			} 
			else if (placeType.equals(ProductConstants.LOTION_TYPE_REGION)
					|| placeType.equals(ProductConstants.LOTION_TYPE_CHANNELS)) {
				
				placeTypeFlag = "CityOrChannel";
				if(placeType.equals(ProductConstants.LOTION_TYPE_REGION)){
					hisMap.put("city", 1);
				}else {
					hisMap.put("channel", 1);
				}
				// 将方案对应地点集合中每个地点所有的柜台更新到履历表
				for(Long place : saveJsonList){
					hisMap.put("place", place);
					hisMap.put("placeTypeFlag", placeTypeFlag);
					this.mergePrtSoluWithDepartHis(hisMap);
				}
			}
		}
		
		// Step5: END
			
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
	private int updIFDatabase(Map<String, Object> paraMap) throws CherryException,Exception {
		
		// 定义新后台数据是否有写入接口表
		int result = 0;
		
		// 保存接口产品方案柜台关系表
			try {
				// Step1.1  取得新后台产品方案柜台关联数据版本号version大于tVersion的list(新增/修改/停用/启用等)
				List<Map<String, Object>> prtSoluCouList = binOLPTJCS17_Service.getPrtSoluCouList(paraMap);
				// 保存接口产品方案柜台关系表 
				if (!CherryUtil.isBlankList(prtSoluCouList)) {
					result++;
					for (Map<String, Object> prtSoluCouItemMap : prtSoluCouList) {
						try {
							prtSoluCouItemMap.putAll(paraMap);
							// 设置产品方案柜台接口表的状态值
							getPrtSoluCntSCSStatus(prtSoluCouItemMap);
							// 删除产品方案柜台接口表(根据brand、prtSolutionCode、counter)
							binOLPTJCS17_Service.delIFPrtSoluWithCounter(prtSoluCouItemMap);
							// 插入柜台产品接口表
		//					productMap.put(CherryConstants.UNITCODE, "444444444444444444444444444444444444444444ssssssssssssssssssssssssssss");
							binOLPTJCS17_Service.addIFPrtSoluWithCounter(prtSoluCouItemMap);
							// 插入件数加一
		//					insertCount += 1;
						} catch (Exception e) {
							logger.error(e.getMessage(),e);
							String departCode = ConvertUtil.getString(prtSoluCouItemMap.get("DepartCode"));
							String solutionCode = ConvertUtil.getString(prtSoluCouItemMap.get("SolutionCode"));
							
							throw new CherryException("EBS00135",new String[]{solutionCode,departCode},e);
						}
					}
				}
				// Step1.2 取得新后台产品方案明细表数据版本号version大于tVersion的list(新增/修改/停用/启用等)
				
				String config = binOLCM14_BL.getConfigValue("1288", String.valueOf(paraMap.get("organizationInfoId")), String.valueOf(paraMap.get("brandInfoId")));
				paraMap.put("soluAddModeConf", config);
				if(ProductConstants.SOLU_ADD_MODE_CONFIG_2.equals(config) 
						|| ProductConstants.SOLU_ADD_MODE_CONFIG_3.equals(config)){
					
					// Step1.2.1取得系统配置项产品方案添加模式,为颖通模式时，检查方案明细添加的分类是否有动态添加减少产品的变动情况
					// 所有产品价格方案
					List<Map<String, Object>> prtPriceSolutionList = binOLPTJCS16_Service.getPrtPriceSolutionList(paraMap);
					if (!CherryUtil.isBlankList(prtPriceSolutionList)) {
						for(Map<String, Object> soluMap : prtPriceSolutionList){
							paraMap.put("productPriceSolutionID", soluMap.get("solutionID"));
							// 取得当前产品方案明细表的产品与以前配置的差异List
							List<Map<String, Object>> prtForPrtSoluDetailDiff = binOLPTJCS19_Service.getPrtForPrtSoluDetailDiff(paraMap);
							if (!CherryUtil.isBlankList(prtForPrtSoluDetailDiff)) {
								for(Map<String, Object> diffMap : prtForPrtSoluDetailDiff){
									// 将差异更新到产品方案明细表
									diffMap.putAll(paraMap);
									String modifyFlag = (String)diffMap.get("modifyFlag"); // modifyFlag  add 增加的柜台 、sub减少的柜台
									
									// 取得当前方案及增加的产品,merge到产品方案明细表 validFlag = 1,version = tversion +1,isCate =1
									if("add".equals(modifyFlag)){
										diffMap.put("ValidFlag", CherryConstants.VALIDFLAG_ENABLE);
										diffMap.put("productId", diffMap.get("prtPD"));
										// 1: 插入产品方案明细表
										binOLPTJCS19_Service.mergeProductPriceSolutionDetail(diffMap);
										
									}
									// 取得当前方案明细减少的产品,merge到产品方案部门关系表 validFlag = 0,version = tversion +1
									else if ("sub".equals(modifyFlag)){
										
										diffMap.put("ValidFlag", CherryConstants.VALIDFLAG_DISABLE);
										diffMap.put("productId", diffMap.get("prtPDH"));
										// 1: 将方案明细表的产品数据无效掉
										binOLPTJCS19_Service.updPrtSoluDetail(diffMap);
									}
								}
								
							}
						}
					}
					
					// Step1.2.2 颖通模式时，方案价格根据当前标准产品当前业务日期的价格
					binOLPTJCS19_Service.mergePPSDPrice(paraMap);
				}
				
				
				
				List<Map<String, Object>> prtSoluDetailByVersionList = binOLPTJCS17_Service.getPrtSoluDetailByVersionList(paraMap);
				// 保存接口产品方案柜台关系表 
				if (!CherryUtil.isBlankList(prtSoluDetailByVersionList)) {
					result++;
					for (Map<String, Object> prtSoluDetailItemMap : prtSoluDetailByVersionList) {
						try{
							prtSoluDetailItemMap.putAll(paraMap);
							// 设置产品方案柜台接口表的状态值
							getPrtSoluSCSStatus(prtSoluDetailItemMap);
							// 删除产品方案柜台接口表(根据brand、prtSolutionCode、产品厂商ID)
							binOLPTJCS17_Service.delIFPrtSoluSCS(prtSoluDetailItemMap);
							// 插入产品方案明细接口表
							binOLPTJCS17_Service.addIFPrtSoluSCS(prtSoluDetailItemMap);
							
						} catch(Exception e){
							logger.error(e.getMessage(),e);
							String solutionCode = ConvertUtil.getString(prtSoluDetailItemMap.get("SolutionCode"));
							String productVendorID = ConvertUtil.getString(prtSoluDetailItemMap.get("BIN_ProductVendorID"));
							String unitCode = ConvertUtil.getString(prtSoluDetailItemMap.get("UnitCode"));
							String barCode = ConvertUtil.getString(prtSoluDetailItemMap.get("BarCode"));
							
							throw new CherryException("EBS00136",new String[]{solutionCode,productVendorID,unitCode,barCode},e);
						}
					}
				}
				
			} catch(CherryException ce){
				logger.error(ce.getErrMessage(),ce);
				throw new Exception(ce);
			} catch (Exception e) {
				logger.error(e.getMessage(),e);
				throw new Exception(e);
			}
		
		return result;
	}
	
	/**
	 * 废除 -- 在柜台实时下发时，还会做一次的，这里做也可能不准。所以这里没有必要做。
	 * 设置部门产品表相关属性(status、validFlag)
	 * @param itemMap
	 */
	@Deprecated
	public void setProductDepart(Map<String, Object> itemMap){
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
		String pdValidFlag = "1"; // 部门产品表的有效状态
		
		String ValidFlag = ConvertUtil.getString(itemMap.get("ValidFlag"));
		String prtStatus = ConvertUtil.getString(itemMap.get("Status")); // 新后台产品状态 D：表明下柜产品； E：表明产品生效，可销售可订货；
		String sellDateFlag = ConvertUtil.getString(itemMap.get("SellDateFlag")); // 是否不在销售日期区间  0:未过期 1:已过期
		
		if(CherryConstants.VALIDFLAG_DISABLE.equals(ValidFlag)){
			// 产品无效
			prtscs_status = "D";
			pdValidFlag = "0";
		}else{
			// 产品有效
			pdValidFlag = "1";
			
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
		itemMap.put("prtscs_status", prtscs_status);
		itemMap.put("pdValidFlag", pdValidFlag);
	}
	
	/**
	 * 取得当前方案分配的地点是否已被其他方案分配过的List
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String,Object> getExistCnt(Map<String, Object> map){
		
		Map<String,Object> resultMap = new HashMap<String, Object>();
		
		// 定义已被其他方案分配过的区域城市/渠道List
		List<Map<String,Object>> existList = null;
		// 定义已被其他方案分配过的区域城市/渠道/指定柜台地点的柜台List
		List<Map<String,Object>> existCntList = null;
		
		// 地点集合
		String locationArr = (String)map.get("locationArr");
		String [] locationList = locationArr.split(",");
		map.put("locationList", locationList);
		
		// 地点类型
		String placeType = (String)map.get("locationType");
		map.put("placeType", placeType);
		if(placeType.equals(ProductConstants.LOTION_TYPE_REGION_COUNTER)
				|| placeType.equals(ProductConstants.LOTION_TYPE_CHANNELS_COUNTER)
				|| placeType.equals(ProductConstants.LOTION_TYPE_IMPORT_COUNTER)
				|| placeType.equals("7")) {
			map.put("counter", 1);
			existList = new ArrayList<Map<String,Object>>();
			existCntList = binOLPTJCS17_Service.getExistCntForPrtSoluWithDepartHis(map);
		} 
		else if (placeType.equals(ProductConstants.LOTION_TYPE_REGION) || placeType.equals(ProductConstants.LOTION_TYPE_CHANNELS)) {
			if(placeType.equals(ProductConstants.LOTION_TYPE_REGION)){
				map.put("city", 1);
			}
			else if(placeType.equals(ProductConstants.LOTION_TYPE_CHANNELS)){
				map.put("channel", 1);
			}
			existList = binOLPTJCS17_Service.getExistPrtSoluWithDepartHis(map);
			existCntList = binOLPTJCS17_Service.getExistCntForPrtSoluWithDepartHis(map);
		}

		resultMap.put("existList", existList);
		resultMap.put("existCntList", existCntList);
		if(!existList.isEmpty() || !existCntList.isEmpty()){
			resultMap.put("result", "result0");
		}
		
		return resultMap;
	}
	
	/**
	 * 设置柜台产品接口表的状态值
	 * @param map
	 */
	private void getPrtSoluCntSCSStatus(Map<String, Object> productMap){
		
		
		String prtSoluWithCnt_status = "1"; // PrtSoluCntSCS 状态值,
		
		String cntValidFlag = ConvertUtil.getString(productMap.get("cntValidFlag")); // 柜台有效区分
		String pdValidFlag = ConvertUtil.getString(productMap.get("psdValidFlag")); // 产品方案柜台有效区分
		String ppsValidFlag = ConvertUtil.getString(productMap.get("ppsValidFlag")); // 产品方案有效区分
		
		if (CherryConstants.VALIDFLAG_DISABLE.equals(cntValidFlag)
				|| CherryConstants.VALIDFLAG_DISABLE.equals(pdValidFlag)
				|| CherryConstants.VALIDFLAG_DISABLE.equals(ppsValidFlag)) {
			// 设置无效无效
			prtSoluWithCnt_status = "0";
		}else{
			// 产品有效
			prtSoluWithCnt_status = "1";
			
		}
		
		productMap.put("prtSoluWithCnt_status", prtSoluWithCnt_status);
		
	}
	
	/**
	 * 设置产品方案明细接口表的状态值
	 * @param map
	 */
	private void getPrtSoluSCSStatus(Map<String, Object> productMap){

		/*
		 处理逻辑 （实时下发接口状态值设值(0：正常销售、1：停用、2：下柜、3：未启用*)）
			1、先判断产品表及产品方案明细表柜台表的validFlag数值，如果 有一个是停用那么直接使用停用
			2、如果validFlag=1,则判断是否下柜台，如果下柜台则使用下柜，如果未下柜，则判断是否在销售区间内，如果不在，则使用未启用

		终端对WITPOSA_product_with_counter status定义：
		E	表示	一个BARCODE对应多个UNITCODE时[product_scs往品牌的product导入时]，全部生效	
		D	表示	产品停用 	其他系统下发的数据，只有停用，无下柜和未启用状态	
		新增加下面两种状态：	
		H	表示	产品下柜	下发到witpos_xx数据库后都归为停用	
		G	表示	产品未启用 	下发到witpos_xx数据库后都归为停用
		
		新后台对WITPOSA_product_with_counter status定义：
		E：正常销售、D：停用、H：下柜、G：未启用
		*/
		
		String prtSoluDetail_status = "E"; // Product_SCS 状态值,
		
		String prtVendorValidFlag = ConvertUtil.getString(productMap.get("prtVendorValidFlag"));
		String soluPrtValidFlag = ConvertUtil.getString(productMap.get("soluPrtValidFlag"));
		String prtStatus = ConvertUtil.getString(productMap.get("prtStatus")); // 新后台产品状态 D：表明下柜产品； E：表明产品生效，可销售可订货；
		String sellDateFlag = ConvertUtil.getString(productMap.get("prtSellDateFlag")); // 是否不在销售日期区间  0:未过期 1:已过期
		
		if (CherryConstants.VALIDFLAG_DISABLE.equals(prtVendorValidFlag)
				|| CherryConstants.VALIDFLAG_DISABLE.equals(soluPrtValidFlag)) {
			// 设置无效无效
			prtSoluDetail_status = "D";
		}else{
			// 产品有效
			if(ProductConstants.PRODUCT_STATUS_D.equalsIgnoreCase(prtStatus)){
				// 产品下柜
				prtSoluDetail_status = "H";
			}else {
				//
				if(ProductConstants.SellDateFlag_1.equals(sellDateFlag)){
					// 产品未启用
					prtSoluDetail_status = "G";
				}else{
					// 有效，正常销售
					prtSoluDetail_status = "E";
				}
			}
		}
		
		productMap.put("prtSoluDetail_status", prtSoluDetail_status);

	}
	
	/**
	 * 价格按折扣率计算
	 * @param priceInfoList
	 * @param map
	 */
	private void setPriceRate(List<Map<String, Object>> priceInfoList,Map<String,Object> map){
		
		String priceMode = ConvertUtil.getString(map.get("priceMode")); 
		
		if("2".equals(priceMode)){
			float saleRate = Float.parseFloat(ConvertUtil.getString(map.get("saleRate"))); 
			for(Map<String, Object> priceInfoMap : priceInfoList){
				float salePrice = Float.parseFloat(ConvertUtil.getString(priceInfoMap.get("salePrice")));
				salePrice = salePrice*saleRate/100;
				BigDecimal sp = new BigDecimal(salePrice);
				salePrice = sp.setScale(2,BigDecimal.ROUND_HALF_UP).floatValue();
				priceInfoMap.put("salePrice", salePrice);
			}
		}
	}
	
	/**
	 * 取得业务日期
	 * 
	 * @param map
	 * @return
	 */
	@Override
	public String getBussinessDate(Map<String, Object> map) {
		return binOLPTJCS14_Service.getBussinessDate(map);
	}

}
