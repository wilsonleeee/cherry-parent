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

import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.dr.cmbussiness.util.DateUtil;
import com.cherry.pt.common.ProductConstants;
import com.cherry.pt.common.PrtUtil;
import com.cherry.pt.jcs.interfaces.BINOLPTJCS14_IF;
import com.cherry.pt.jcs.service.BINOLPTJCS04_Service;
import com.cherry.pt.jcs.service.BINOLPTJCS06_Service;
import com.cherry.pt.jcs.service.BINOLPTJCS14_Service;
import com.cherry.ss.common.PromotionConstants;
import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

/**
 * 柜台产品价格维护BL
 * 
 * @author JiJW
 * @version 1.0 2014-6-12
 */
public class BINOLPTJCS14_BL implements BINOLPTJCS14_IF {
	
	@Resource(name="binOLPTJCS14_Service")
	private BINOLPTJCS14_Service binOLPTJCS14_Service;
	
	@Resource
	private BINOLPTJCS04_Service binOLPTJCS04_Service;
	
	@Resource
	private BINOLPTJCS06_Service binOLPTJCS06_Service;
	
	private static final Logger logger = LoggerFactory.getLogger(BINOLPTJCS14_BL.class);
	
	/**
	 * 取得柜台产品配置主表信息
	 * @param map
	 * @return
	 */
	public Map<String,Object> getDepartProductConfig(Map<String, Object> map){
		return binOLPTJCS14_Service.getDepartProductConfig(map);
	}
	
	/**
	 * 插入柜台产品配置表
	 * 
	 * @param map
	 * @return int
	 */
	public int tran_addDepartProductConfig(Map<String, Object> map){
		return binOLPTJCS14_Service.insertDepartProductConfig(map);
	}

	/**
	 * 取得地点
	 * @param map
	 * @return
	 * @throws JSONException 
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List getLocation(Map map) throws JSONException {
		List resultTreeList = new ArrayList();
		// 取得促销活动地点类型
		String locationType = (String) map.get("locationType");
		int loadingCnt = ConvertUtil.getInt(map.get("loadingCnt"));
		// 促销活动地点选择类型--按区域
		if (locationType.equals(PromotionConstants.LOTION_TYPE_REGION) || locationType.equals(PromotionConstants.LOTION_TYPE_REGION_COUNTER)) {
			if (locationType.equals(PromotionConstants.LOTION_TYPE_REGION_COUNTER)) {
				map.put("city_counter", "1");
			}
			// 取得区域信息
			List resultList = binOLPTJCS14_Service.getRegionInfoList(map);
			List keysList = new ArrayList();
			String[] keys1 = { "regionId", "regionName" };
			String[] keys2 = { "provinceId", "provinceName" };
			String[] keys3 = { "cityId", "cityName" };
			keysList.add(keys1);
			keysList.add(keys2);
			keysList.add(keys3);
			if (locationType
					.equals(PromotionConstants.LOTION_TYPE_REGION_COUNTER)
					&& loadingCnt != 0) {
				String[] keys4 = { "counterCode", "counterName" };
				keysList.add(keys4);
			}
			ConvertUtil.jsTreeDataDeepList(resultList, resultTreeList,keysList, 0);
		} else if (locationType.equals(PromotionConstants.LOTION_TYPE_CHANNELS)
				|| locationType
						.equals(PromotionConstants.LOTION_TYPE_CHANNELS_COUNTER)) {
			if (locationType
					.equals(PromotionConstants.LOTION_TYPE_CHANNELS_COUNTER)) {
				map.put("channel_counter", "1");
			}
			List resultList = binOLPTJCS14_Service.getChannelInfoList(map);
			List keysList = new ArrayList();
			String[] keys1 = { "id", "name" };
			keysList.add(keys1);
			if (locationType
					.equals(PromotionConstants.LOTION_TYPE_CHANNELS_COUNTER)
					&& loadingCnt != 0) {
				String[] keys2 = { "counterCode", "counterName" };
				keysList.add(keys2);
			}
			ConvertUtil.jsTreeDataDeepList(resultList, resultTreeList,
					keysList, 0);
		}

		if ((locationType.equals(PromotionConstants.LOTION_TYPE_REGION_COUNTER) || locationType
				.equals(PromotionConstants.LOTION_TYPE_CHANNELS_COUNTER))
				&& loadingCnt == 0) {
			ConvertUtil.addArtificialCounterDeep(resultTreeList);
		}
		
		// 处理方案树形结构选中（方案中设置过的产品会被选中）
		Map<String,Object> departProductConfig = getDepartProductConfig(map);
		String placeJson = ConvertUtil.getString(departProductConfig.get("PlaceJson"));
		List<Map<String, Object>> checkedNodes = (List<Map<String, Object>>) JSONUtil.deserialize(placeJson);
		
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
	 * 取得产品价格方案List
	 * @param map
	 * @return
	 */
	public List getPrtPriceSolutionList(Map<String, Object> map) {
		return binOLPTJCS14_Service.getPrtPriceSolutionList(map);
	}
	
	/**
	 * 取柜台对应的产品价格配置的方案
	 * @param map
	 * @return
	 */
	public Map<String,Object> getPrtPriceSoluInfoByCnt(Map<String, Object> map){
		
		Map<String,Object> resultMap =  new HashMap<String, Object>();
		
		// 配置表ID ，目前配置表只设置1个固定的配置信息
//		Integer departProductConfigID = 1;
//		map.put("departProductConfigID", departProductConfigID);
		
		// 柜台数组集合
		String cntArrStr = ConvertUtil.getString(map.get("cntArr"));
		if(cntArrStr != ""){
			String [] cntArrList = cntArrStr.split(",");
			map.put("counterCode", cntArrList[0]);
			resultMap = binOLPTJCS14_Service.getPrtPriceSoluInfoByCnt(map);
		}
		
		return resultMap;
	}
	
	/**
	 * 保存柜台产品配置信息
	 * @param map
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public int tran_addConfigDetailSave(Map<String, Object> map) throws Exception{
		
		int result = 1;
		try{
			
			// Step1: 更新柜台产品配置主表信息(placeJson等)
			result = binOLPTJCS14_Service.updDepartProductConfig(map);
			
			if (result == 0) {
				throw new Exception("更新柜台产品配置主表失败");
			}
			
			// 柜台数组集合
			String cntArrStr = ConvertUtil.getString(map.get("cntArr"));
			if(cntArrStr != ""){
				String [] cntArrList = cntArrStr.split(",");
				for(String counterCode : cntArrList){
					map.put("counterCode", counterCode);
					map.put("departCode", counterCode);
					
					// Step2-1: 删除指定品牌及对应部门Code的产品部门信息
					binOLPTJCS14_Service.delPrtDepartByDepartCode(map);
					
					// Step2-2: 删除产品价格表中的柜台价格（根据柜台号、产品ID）
					map.put("type", "3"); // 价格类型为部门价格
					binOLPTJCS14_Service.delProductPrice(map);
					
					// Step2-3: 删除柜台号对应的柜台产品配置信息
					binOLPTJCS14_Service.delConfigDetail(map);
					
					if(null != map.get("productPriceSolutionID")){
						// Step3-1: 插入新的柜台号对应的柜台产品方案配置信息
						result = binOLPTJCS14_Service.insertConfigDetail(map);
						
						// Step3-2: 插入产品部门表、插入柜台价格到产品价格表中
						List<Map<String, Object>>  prtSoluList = binOLPTJCS14_Service.getPrtSoluByConfig(map);
						for(Map<String, Object> prtSoluMap: prtSoluList){
							prtSoluMap.putAll(map);
							prtSoluMap.put("type", "3"); // 价格类型为部门价格
							
							// 插入产品部门表
							binOLPTJCS14_Service.insertProductDepartInfo(prtSoluMap);
							
							// 插入柜台价格到产品价格表中
							optPriceNew(prtSoluMap);
						}
					} 
					if (result == 0) {
						throw new Exception("处理柜台产品配置明细表失败");
					}
				}
			}
			
		} catch(Exception e){
			result = 0;
			logger.error(e.getMessage(), e);
		}
		return result;
	}
	
	/**
	 * 保存产品价格方案主表
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public int tran_addPrtPriceSolu(Map<String, Object> map) throws Exception{
		return binOLPTJCS14_Service.insertPrtPriceSolu(map);
	}
	
	/**
	 * 修改产品价格方案主表
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public int tran_updPrtPriceSolu(Map<String, Object> map) throws Exception{
		return binOLPTJCS14_Service.updPrtPriceSolution(map);
	}
	
	/**
	 * 取得产品树形
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String getPrtTree(Map<String, Object> map) throws Exception{
		
		String resultStr = "[]";
		
		// 产品分类节点位置
		String path = ConvertUtil.getString(map.get(CherryConstants.PATH));
		if (CherryChecker.isNullOrEmpty(path)) {
			// 取得品牌ID
			String brandInfoId = ConvertUtil.getString(map.get(CherryConstants.BRANDINFOID));
			
			if (CherryConstants.BLANK.equals(brandInfoId)) {
				// 取得品牌
				List<Map<String, Object>> list = binOLPTJCS04_Service.getBrandList(map);
				// JSON字符串作成
				resultStr = getBrandJSON(list);
			} else {
				path = brandInfoId + CherryConstants.SLASH;
				map.put(CherryConstants.PATH, path);
				// 分类JSON字符串作成
				resultStr = getCateJSON(map);
			}
		} else {
			
			Object isLastCate =  map.get("isLastCate");
			
			if(isLastCate !=null && isLastCate.equals(true)){
				resultStr = getPrtJSON(map);
			} else {
				// 分类JSON字符串作成
				resultStr = getCateJSON(map);
			}
		}
		
		// 处理方案树形结构选中（方案中设置过的产品会被选中）
		Map<String,Object> prtPriceSoluInfo = binOLPTJCS14_Service.getPrtPriceSoluInfo(map);
		String prtJson = ConvertUtil.getString(prtPriceSoluInfo.get("PrtJson"));
		List<Map<String, Object>> checkedNodes = (List<Map<String, Object>>) JSONUtil.deserialize(prtJson);
		
		List<Map<String, Object>> nodeList = (List<Map<String, Object>>) JSONUtil.deserialize(resultStr);
		
		if (null != nodeList) {
			// 设置节点选中状态
			if (null != checkedNodes) {
				for (int i = 0; i < checkedNodes.size(); i++) {
					Map<String, Object> checkedNode = checkedNodes.get(i);
					PrtUtil.setNodes(nodeList, checkedNode);
				}
			}
			resultStr = JSONUtil.serialize(nodeList);
		}
		
		return resultStr;
	}
	
	/**
	 * 取得产品树形结构
	 * @param map
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private String getPrtJSON(Map<String, Object> map) throws Exception {
		String path = ConvertUtil.getString(map.get(CherryConstants.PATH));
		String[] level = path.split(CherryConstants.SLASH);
		map.put("propValId", level[level.length - 1]);
		
		List<Map<String, Object>> list = binOLPTJCS14_Service.getPrtByCateVal(map);
		
		if(null == list || list.isEmpty()){
			return "[]";
		}else {
			// 把取得的产品分类List转换成树结构的字符串
			StringBuffer jsonTree = new StringBuffer();
			jsonTree.append("[");
			for (int i = 0; i < list.size(); i++) {
				Map<String, Object> resultMap = list.get(i);
				// 产品Id
				String productId = ConvertUtil.getString(resultMap.get("productId"));
				productId = JSONUtil.serialize(productId);
				
				// 节点名称
				String name = ConvertUtil.getString(resultMap.get("name"));
				name = JSONUtil.serialize(name);
				
				jsonTree.append("{")
					.append("\"path\":").append(productId).append(",")
					.append("\"name\":").append(name).append(",")
					.append("\"isPrt\":").append(true).append(",")
					.append("\"isParent\":\"").append(false).append("\"}");
				
				if (i + 1 != list.size()) {
					jsonTree.append(",");
				}
				//jsonTree.append("{\"path\":\""+ path+"\"," + "\"name\":"+ name+"," + "\"isPrt\":"+ isPrt+"," + "\"isLastCate\":"+ isLastCate+"," + "\"isParent\":\"");
			}
			jsonTree.append("]");
			return jsonTree.toString();
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
				// 分类级别 1 大 3中 2小
//				String teminalFlag = ConvertUtil.getString(resultMap.get("teminalFlag"));
				boolean isPrt = false;
				boolean isLastCate = childCount > 0 ? false : true; // 是否最后一个分类层级
				
				// 树结构作成
//				jsonTree.append("{\"data\":{\"title\":" + name + ","
//						+ "\"attr\":{\"id\":\"" + path + "\"}},"
//						+ "\"attr\":{\"id\":\"" + path + "\"},\"state\":\"");
				jsonTree.append("{\"path\":\""+ path+"\"," + "\"name\":"+ name+"," + "\"isPrt\":"+ isPrt+"," + "\"isLastCate\":"+ isLastCate+"," + "\"isParent\":\"");
				
				// 有子节点的场合
				if (childCount > 0 || isLastCate) {
//					jsonTree.append("closed");
					jsonTree.append("true");
				}else {
					jsonTree.append("false");
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
	 * 取得方案中的产品信息
	 * @param map
	 * @return
	 * @throws JSONException 
	 */
	@SuppressWarnings({ "unchecked", "null" })
	public Map<String,Object> getPrtDetailInfo(Map<String, Object> map) throws JSONException{
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		Map<String, Object> soluInfo = binOLPTJCS14_Service.getPrtPriceSoluInfo(map);
		
		String soluEndDate = ConvertUtil.getString(soluInfo.get("EndDate"));
		
		List<Map<String, Object>> priceList = new ArrayList<Map<String,Object>>();
		
		Map<String, Object> prtDetailMap = binOLPTJCS14_Service.getPrtPriceSoluDetailInfo(map);
		
		if(null == prtDetailMap){
			// 取得品牌当前有效的价格
			priceList = binOLPTJCS14_Service.getSellPriceList(map);
			resultMap.put("priceFrom", "1"); // 1：来自品牌价格， 2：来自产品方案
		} else {
			resultMap.put("priceFrom", "2"); // 1：来自品牌价格， 2：来自产品方案
			String priceJson =  ConvertUtil.getString(prtDetailMap.get("priceJson"));
			priceJson = priceJson.replace("priceStartDate", "startDate");
			priceJson = priceJson.replace("priceEndDate", "endDate");
			priceList = (List<Map<String, Object>>) JSONUtil.deserialize(priceJson);
			
			String productPriceSolutionDetailID = ConvertUtil.getString(prtDetailMap.get("productPriceSolutionDetailID"));
			resultMap.put("productPriceSolutionDetailID",productPriceSolutionDetailID); 
		}
		// 失效价格不能编辑/删除
		if(null != priceList || priceList.size() != 0){

			// 业务日期
			String businessDate = binOLPTJCS14_Service.getBussinessDate(map);
			
			boolean bol = false;
			if(DateUtil.compareDate(soluEndDate, businessDate) < 0){
				bol = true;
			}
			// 比较失效日期与业务日期，小于时，不能删除与编辑
			for(Map<String, Object> sellPriceMap : priceList){
				
				sellPriceMap.put("compareDateFlag", bol);
//				String endDate = (String)sellPriceMap.get("endDate");
//				if(null != endDate){
//					if(DateUtil.compareDate(endDate, businessDate) < 0){
//						sellPriceMap.put("compareDateFlag", true);
//					} else {
//						sellPriceMap.put("compareDateFlag", false);
//					}
//				} else {
//					sellPriceMap.put("compareDateFlag", false);
//				}
			}
			
		}
		resultMap.put("priceList", priceList);
		resultMap.put("option","1"); 
		
		Map<String, Object> proMap = binOLPTJCS06_Service.getDetail(map);
		resultMap.put("proMap",proMap); 
		
		return resultMap;
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
	 * 保存产品价格方案明细表
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public int tran_addPrtPriceSoluDetail(Map<String, Object> map)
			throws Exception {
		
		int result = 1;
		try{
			
			// 更新产品方案主表的树形结构等信息
			result = binOLPTJCS14_Service.updPrtPriceSolution(map);
			
			if (result == 0){
				throw new Exception("更新方案主表失败");
			}
			// 删除指定产品方案明细ID的数据
			binOLPTJCS14_Service.delPrtPriceSoluDetail(map);

			// 保存产品方案明细信息
			result = binOLPTJCS14_Service.insertPrtPriceSoluDetail(map);
			
			if (result == 0) {
				throw new Exception("保存产品方案明细失败");
			}
			
		} catch(Exception e){
			result = 0;
			logger.error(e.getMessage(), e);
		}

		return result;
	}
	
	public int tran_delPrtPriceSoluDetail(Map<String, Object> map)
			throws Exception {
		
		int result = 1;
		try{
			
			// 更新产品方案主表的树形结构等信息
			result = binOLPTJCS14_Service.updPrtPriceSolution(map);
			
			if (result == 0){
				throw new Exception("更新方案主表失败");
			}
			// 删除指定产品方案明细ID的数据
			result = binOLPTJCS14_Service.delPrtPriceSoluDetail(map);
			
			
			if (result == 0) {
				throw new Exception("当前产品撤消失败");
			}
			
		} catch(Exception e){
			result = 0;
			logger.error(e.getMessage(), e);
		}
		
		return result;
	}
	
	/**
	 * 取得产品价格方案主表信息
	 * @param map
	 * @return
	 */
	public Map<String,Object> getPrtPriceSoluInfo(Map<String, Object> map){
		return binOLPTJCS14_Service.getPrtPriceSoluInfo(map);
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
						binOLPTJCS14_Service.insertProductPrice(priceMap);
					} 
				}
			}
		}
		
	}
	
	/**
	 * 价格处理
	 * @param map
	 * @throws JSONException
	 */
	@SuppressWarnings("unchecked")
	@Deprecated
	private void optPrice(Map<String, Object> map) throws JSONException{
		// 价格
		String priceInfo = ConvertUtil.getString(map.get("priceJson"));
		if (!CherryConstants.BLANK.equals(priceInfo)) {
			List<Map<String, Object>> priceInfoList = (List<Map<String, Object>>) JSONUtil.deserialize(priceInfo);
			// 价格按折扣率计算
			setPriceRate(priceInfoList, map);
			
			if (null != priceInfoList) {
				int size = priceInfoList.size();
				for (int i = 0; i < size; i++) {
					Map<String, Object> priceMap = priceInfoList.get(i);
					priceMap = CherryUtil.removeEmptyVal(priceMap);
					// 操作
					String option = ConvertUtil.getString(priceMap
							.get(ProductConstants.OPTION));
					// 价格失效日期为空
					if (CherryChecker.isNullOrEmpty(priceMap
							.get(ProductConstants.PRICEENDDATE))) {
						// 最后一个价格段
						if (i == size - 1) {
							priceMap.put(ProductConstants.PRICEENDDATE,
									CherryConstants.longLongAfter);
						} else {
							// 当前价格日期段的next日期段价格信息
							Map<String, Object> priceMap2 = priceInfoList
									.get(i + 1);
							String priceStartDate2 = ConvertUtil
									.getString(priceMap2
											.get(ProductConstants.PRICESTARTDATE));
							// 当前价格的失效日期为next价格段的生效日期-1
							priceMap.put(ProductConstants.PRICEENDDATE,
									DateUtil.addDateByDays(
											CherryConstants.DATE_PATTERN,
											priceStartDate2, -1));
						}
					}
					priceMap.putAll(map);
					if (CherryConstants.BLANK.equals(option) || ProductConstants.OPTION_1.equals(option)) {
						// 插入产品销售价格
						binOLPTJCS14_Service.insertProductPrice(priceMap);
					} 
				}
			}
			
		}
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
	
	/**
	 * 获取产品对应的按显示顺序排序的分类属性ID
	 * 
	 * */
	public String getPrtCateVal(Map<String,Object> map){
		String brandId = ConvertUtil.getString(map.get(CherryConstants.BRANDINFOID));
		String prtCateVal = binOLPTJCS14_Service.getPrtCateVal(map);
		
		List<String> prtCateList = new ArrayList<String>();
		StringBuffer sbf = new StringBuffer();
		
		if(null != prtCateVal){
			
			String [] prtCateValArr = prtCateVal.split(",");
			if(prtCateValArr.length != 0){
				for(int i= 0 ; i < prtCateValArr.length ; i++){
					if(i == 0){
						sbf.append(brandId).append("/").append(prtCateValArr[0]).append("/");
						prtCateList.add(sbf.toString());
					}else{
						String str = prtCateValArr[i] + "/";
						prtCateList.add(prtCateList.get(i-1)+str);
						sbf.append(prtCateList.get(i-1)+str);
					}
					if(i != prtCateValArr.length-1){
						sbf.append(",");
					}
				}
			}
		}
		
//		StringBuffer sbf2 = new StringBuffer();
//		sbf2.append(brandId).append("/").append(prtCateValArr[0]).append("/").append(",")
//		.append(brandId).append("/").append(prtCateValArr[0]).append("/").append(prtCateValArr[1]).append("/").append(",")
//		.append(brandId).append("/").append(prtCateValArr[0]).append("/").append(prtCateValArr[1]).append("/").append(prtCateValArr[2]).append("/");
//		
		
		return sbf.toString();
	}
	

}
