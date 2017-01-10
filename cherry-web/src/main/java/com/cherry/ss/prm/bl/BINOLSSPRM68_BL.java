/*
 * @(#)BINOLSSPRM67_BL.java     1.0 2013/10/17
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

import java.util.*;

import javax.annotation.Resource;

import com.cherry.cm.cmbussiness.bl.BINOLCM03_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM05_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM89_BL;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.core.CodeTable;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cp.act.util.ActUtil;
import com.cherry.cp.common.CampConstants;
import com.cherry.cp.point.bl.BINOLCPPOI01_BL;
import com.cherry.ss.common.PromotionConstants;
import com.cherry.ss.prm.service.BINOLSSPRM13_Service;
import com.cherry.ss.prm.service.BINOLSSPRM37_Service;
import com.cherry.ss.prm.service.BINOLSSPRM68_Service;
import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

/**
 * 
 * 智能促销BL
 * 
 * @author lipc
 * @version 1.0 2013.10.17
 */
public class BINOLSSPRM68_BL {

	@Resource(name = "binOLCPPOI01_BL")
	private BINOLCPPOI01_BL poi01_BL;

	@Resource(name = "binOLCM03_BL")
	private BINOLCM03_BL binOLCM03_BL;

	@Resource(name = "binOLCM89_BL")
	private BINOLCM89_BL binOLCM89_BL;

	@Resource(name = "binOLCM05_BL")
	private BINOLCM05_BL binOLCM05_BL;
	
	@Resource
	private BINOLCM14_BL binOLCM14_BL;

	@Resource(name = "binOLSSPRM13_Service")
	private BINOLSSPRM13_Service prm13Ser;

	@Resource(name = "binOLSSPRM37_Service")
	private BINOLSSPRM37_Service prm37Ser;

	@Resource(name = "binOLSSPRM68_Service")
	private BINOLSSPRM68_Service prm68Ser;

	@Resource
	private CodeTable codeTable;

	public String getBusDate(Map<String, Object> map) {
		return prm68Ser.getBusDate(map);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> getActiveGrpList(Map<String, Object> map) {
		map.put("prmGrpType", "CXHD");
		return prm13Ser.getActiveGrpList(map);
	}

	/**
	 * 取得活动信息
	 *
	 * @param map
	 * @return
	 */
	public Map<String, Object> getActRuleInfo(Map<String, Object> map) {
		return prm68Ser.getActRuleInfo(map);
	}

	/**
	 * 取得活动地点JSON
	 *
	 * @param palceMap
	 * @param map
	 * @return
	 * @throws JSONException
	 */
	@SuppressWarnings("unchecked")
	public String[] getPlaceJson(Map<String, String> palceMap,
			Map<String, Object> map) throws JSONException {
		String[] resArr = new String[2];
		resArr[0] = "[]";
		resArr[1] = "[]";
		// 活动树的所有节点
		List<Map<String, Object>> nodeList = null;
		String palceJson = palceMap.get(CampConstants.PLACE_JSON);
		if(null != palceJson && !"".equals(palceJson)){
			String locationType = palceMap.get("locationType");
			List<Map<String, Object>> checkedNodes = (List<Map<String, Object>>) JSONUtil
					.deserialize(palceJson);
			if (null != checkedNodes && checkedNodes.size() > 0) {
				// 全部柜台或导入柜台
				if (CampConstants.LOTION_TYPE_0.equals(locationType)
						|| CampConstants.LOTION_TYPE_5.equals(locationType)) {
					nodeList = checkedNodes;
					resArr[0] = JSONUtil.serialize(nodeList);
					resArr[1] = "[\"ALL\"]";
				} else if (!"".equals(locationType)) {
					Map<String, Object> params = new HashMap<String, Object>(map);
					params.put(CampConstants.LOCATION_TYPE, locationType);
					// 加载柜台
					params.put(CampConstants.LOADING_CNT, 1);
					nodeList = poi01_BL.getActiveLocation(params);
					resArr[0] = JSONUtil.serialize(nodeList);
					String opt = ConvertUtil.getString(map.get("OPT_KBN"));
					if(!"1".equals(opt) && (CampConstants.LOTION_TYPE_2.equals(locationType)
							|| CampConstants.LOTION_TYPE_4.equals(locationType)
							|| CampConstants.LOTION_TYPE_8.equals(locationType)
							|| CampConstants.LOTION_TYPE_10.equals(locationType)
							)){
						int step = ConvertUtil.getInt(map.get("step"));
						List<String> checkedList = null;
						if(step == 1){
							params.put("activeID",map.get("activeID"));
							params.put("basePropName","baseProp_counter");
							checkedList = prm68Ser.getProRulePlaceList(params);
							if(null != checkedList && !checkedList.isEmpty()){
								resArr[1] = JSONUtil.serialize(checkedList);
							}
						}else{
							checkedList = new LinkedList<String>();
							for(Map<String, Object> checked : checkedNodes) {
								boolean halfCheck = (boolean)checked.get("half");
								if(!halfCheck){
									checkedList.add(ConvertUtil.getString(checked.get("id")));
								}
							}
							resArr[1] = JSONUtil.serialize(checkedList);
						}
					}else{
						List<String> checkedList = new LinkedList<String>();
						for(Map<String, Object> checked : checkedNodes) {
							boolean halfCheck = (boolean)checked.get("half");
							if(!halfCheck){
								checkedList.add(ConvertUtil.getString(checked.get("id")));
							}
						}
						resArr[1] = JSONUtil.serialize(checkedList);
					}
				}
			}
		}
		return resArr;
	}

	/**
	 * 保存促销规则
	 *
	 * @param map
	 * @return
	 */
	public void tran_saveRule(Map<String, Object> map) throws Exception {
		saveRule(map);
		String templateFlag = ConvertUtil.getString(map.get("templateFlag"));
		if(!"1".equals(templateFlag)){
			saveCampRule(map);
		}
	}
	/**
	 * 保存促销规则
	 *
	 * @param map
	 * @return
	 */
	private void saveRule(Map<String, Object> map) throws Exception {
		String orgId = ConvertUtil.getString(map
				.get(CherryConstants.ORGANIZATIONINFOID));
		String brandId = ConvertUtil.getString(map
				.get(CherryConstants.BRANDINFOID));
		map.putAll(getUpdMap(map));
		// 是否需要审核
		String isCheck = binOLCM14_BL.getConfigValue("1350",orgId, brandId);
		if("1".equals(isCheck)){
			map.put("status", 0);
		}
		// 操作区分
		String opt = ConvertUtil.getString(map.get(CampConstants.OPT_KBN));
		if (CampConstants.OPT_KBN1.equals(opt)
				|| CampConstants.OPT_KBN3.equals(opt)) {// 新建OR复制
			// 取得促销活动代号
			String code = binOLCM03_BL.getTicketNumber(orgId, brandId, "", "9");
			map.put("activityCode", code);
			// 插入促销活动表,并取得促销活动id
			int prmActId = prm13Ser.addPromotionActivity(map);
			map.put("bin_PromotionActivityID", prmActId);
		} else {// 编辑
				// 更新促销活动表
			int updCount = prm37Ser.updPrmActivity(map);
			if (updCount == 0) {
				throw new CherryException("ECM00038");
			}
			// 删除促销规则表
			prm68Ser.delPrmActRule(map);
			// 删除促销规则分类
			prm68Ser.delPrmActRuleCate(map);
			// 删除促销活动关联表
			prm68Ser.delPrmActivityRule(map);
			// 删除促销活动条件表
			prm68Ser.delPrmActCondition(map);
			// 删除促销活动结果表
			prm68Ser.delPrmActResult(map);
			map.put("bin_PromotionActivityID", map.get("activeID"));
		}
		
		// 添加促销活动关联表
		int ruleID = prm13Ser.addPromotionActivityRule(map);
		map.put("bin_PromotionActivityRuleID", ruleID);
		// 插入促销活动规则条件明细表
		saveRuleCondition(map);
		// 插入促销规则结果明细表
		saveRuleResult(map);
		// 规则处理【结果json，规则分类】
		exeRule(map);
		// 添加促销规则表
		prm68Ser.addActRuleInfo(map);
		// 添加促销规则履历表
//		prm68Ser.addActRuleHisInfo(map);
		// 添加促销规则分类表
		saveRuleCate(map);
	}
	
	/**
	 * 保存促销规则
	 *
	 * @param map
	 * @return
	 */
	private void saveCampRule(Map<String, Object> map) throws Exception {
		String subCampValid = ConvertUtil.getString(map.get("subCampValid")).trim();
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("ruleCode", map.get("activityCode"));
		param.put(CherryConstants.BRANDINFOID, map.get(CherryConstants.BRANDINFOID));
		param.put("needBuyFlag", map.get("needBuyFlag"));
		
		if(!"0".equals(subCampValid) && !"1".equals(subCampValid)){
			param.putAll(getUpdMap(map));
			// 插入会员主题活动表
			int campId = prm68Ser.addCamp(param);
			param.put("campId", campId);
			// 插入会员活动表
			int campRuleId = prm68Ser.addCampRule(param);
			param.put("campRuleId", campRuleId);
			// 删除条件结果
			prm68Ser.delCampRuleResCond(param);
			prm68Ser.addCampRuleResult(param);
			prm68Ser.addCampRuleCondition(param);
			prm68Ser.addCampRuleConditionCust(param);
		}else{
			// 无效会员主题活动表
			int campId = prm68Ser.delCampain(param);
			if(0 != campId){
				param.put("campId", campId);
				prm68Ser.delCampainRule(param);
			}
		}
	}

	/**
	 * 规则条件结果JSON处理
	 * 
	 * @param vsn
	 *            版本
	 * @param cptb
	 *            向上兼容
	 * @param type
	 *            条件/结果
	 * @param json
	 *            内容
	 * @return
	 */
	public String packJson(String vsn, String cptb, String type,String basePrice, String json) {
		String prefix = "{\"Version\":\"" + vsn + "\",\"Compatible\":\"" + cptb
				+ "\",\"Type\":\"" + type + "\",\"BasePrice\":\"" + basePrice + "\",\"Content\":";
		String suffix = "}";
		json = json.replace("\"[", "[").replace("]\"", "]");
		return prefix + json + suffix;
	}

	/**
	 * 规则处理【结果json，规则分类】
	 * @param map
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private void exeRule(Map<String, Object> map) throws Exception{
		List<String> cateList = new ArrayList<String>();
		// 虚拟促销品信息
		Map<String,Object> prmInfo = (Map<String,Object>)map.get("prmInfo");
		// 规则条件json
		String ruleCondJson = ConvertUtil.getString(map.get("ruleCondJson"));
		// 规则结果json
		String ruleResultJson = ConvertUtil.getString(map.get("ruleResultJson"));
		Map<String,Object> conTemp = ConvertUtil.json2Map(ruleCondJson);
		Map<String,Object> conContent = (Map<String,Object>)conTemp.get("Content");
		// 规则条件类型
		String condType = ConvertUtil.getString(conContent.get("condType"));
		List<Map<String,Object>> arr = (List<Map<String,Object>>)conContent.get("logicObjArr");
		boolean containZd = false;
		if(null != arr && !arr.isEmpty()){
			for (Map<String, Object> map2 : arr) {
				List<Map<String,Object>> arr2 = (List<Map<String,Object>>)map2.get("logicObjArr");
				if(null != arr2 && !arr2.isEmpty()){
					for (Map<String, Object> map3 : arr2) {
						String rangeType = ConvertUtil.getString(map3.get("rangeType"));
						if("ZD".equals(rangeType)){
							containZd = true;
							break;
						}
					}
					if(containZd){
						break;
					}
				}
			}
		}
		// 规则类型默认=非整单类
		String ruleType = "2";
		Map<String,Object> resTemp = ConvertUtil.json2Map(ruleResultJson);
		Map<String,Object> resContent = (Map<String,Object>)resTemp.get("Content");
		List<Map<String,Object>> resList = (List<Map<String,Object>>)resContent.get("logicObjArr");
		
		for(Map<String,Object> item : resList){
			String rewardType = ConvertUtil.getString(item.get("rewardType"));
			cateList.add(rewardType);
			if("ZDZK".equals(rewardType) || "ZDYH".equals(rewardType) || "1".equals(condType) || containZd){
				// 整单类
				ruleType = "1";
			}
		}
		map.put("ruleType", ruleType);
		map.put("cateList", cateList);
		resContent.putAll(prmInfo);
		map.put("ruleResultJson",JSONUtil.serialize(resTemp));
	}
	
	/**
	 * 保存规则分类
	 * @param map
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private void saveRuleCate(Map<String, Object> map) throws Exception {
		List<String> cateList = (List<String>)map.get("cateList");
		if(null != cateList){
			int orgId = ConvertUtil.getInt(map.get(CherryConstants.ORGANIZATIONINFOID));
			int brandId = ConvertUtil.getInt(map.get(CherryConstants.BRANDINFOID));
			String activityCode = ConvertUtil.getString(map.get("activityCode"));
			List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
			for(String cate : cateList){
				Map<String, Object> cateMap = new HashMap<String, Object>();
				cateMap.put(CherryConstants.ORGANIZATIONINFOID,orgId);
				cateMap.put(CherryConstants.BRANDINFOID,brandId);
				cateMap.put("activityCode",activityCode);
				cateMap.put("cateValue",cate);
				list.add(cateMap);
			}
			prm68Ser.addActRuleCate(list);
		}
	}
	/**
	 * 插入促销活动规则条件明细表
	 * 
	 * @param map
	 * @throws JSONException
	 */
	private void saveRuleResult(Map<String, Object> map) throws Exception {
		int orgId = ConvertUtil.getInt(map
				.get(CherryConstants.ORGANIZATIONINFOID));
		int brandId = ConvertUtil.getInt(map.get(CherryConstants.BRANDINFOID));
		String opt = ConvertUtil.getString(map.get(CampConstants.OPT_KBN));
		// 规则结果json
		String ruleResultJson = ConvertUtil.getString(map.get("ruleResultJson"));
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		Map<String, Object> resMap = new HashMap<String, Object>();
		// 虚拟促销品名称
		resMap.put(CherryConstants.NAMETOTAL, map.get("prmActiveName"));
		resMap.put(PromotionConstants.OLD_PRICE, 0);
		resMap.put(PromotionConstants.QUANTITY, 1);
		resMap.put("saleType", "P");
		Map<String,Object> resTemp = ConvertUtil.json2Map(ruleResultJson);
		Map<String,Object> resContent = (Map<String,Object>)resTemp.get("Content");
		List<Map<String,Object>> resList = (List<Map<String,Object>>)resContent.get("logicObjArr");
		String rewardType = null;
		int point = 0;
		for(Map<String,Object> item : resList){
			rewardType = ConvertUtil.getString(item.get("rewardType"));
			point = ConvertUtil.getInt(item.get("point"));
		}
		if("JFDK".equals(rewardType)){
			// TODO
			resMap.put(PromotionConstants.PRICE, 0);
			resMap.put(PromotionConstants.EX_POINT, point);
		}else{
			resMap.put(PromotionConstants.PRICE, 0);
		}
		// 组类型
		resMap.put(PromotionConstants.GROUPTYPE, PromotionConstants.GROUPTYPE_2);
		// 编辑操作
		if(CampConstants.OPT_KBN2.equals(opt)){
			// 虚拟促销品厂商ID
			resMap.put(PromotionConstants.PRMVENDORID,
					map.get(PromotionConstants.PRMVENDORID));
			// 虚拟促销品编码
			resMap.put(CherryConstants.UNITCODE, map.get(CherryConstants.UNITCODE));
			// 虚拟促销品条码
			resMap.put(CherryConstants.BARCODE, map.get(CherryConstants.BARCODE));
		}
		String prmType = PromotionConstants.PROMOTION_TZZK_TYPE_CODE;
		if("JFDK".equals(rewardType)){
			prmType = PromotionConstants.PROMOTION_DHCP_TYPE_CODE;
		}
		// 取得套装折扣类型的促销品
		Map<String, Object> prm = binOLCM05_BL.getPrmInfo(resMap, orgId,
				brandId, prmType);
		Map<String,Object> prmInfo = new HashMap<String, Object>();
		prmInfo.put(PromotionConstants.PRMVENDORID, prm.get(PromotionConstants.PRMVENDORID)+"");
		prmInfo.put("unitCodeTzzk", prm.get(CherryConstants.UNITCODE));
		prmInfo.put("barCodeTzzk", prm.get(CherryConstants.BARCODE));
		resultList.add(prm);
		map.put("resultList", resultList);
		map.put("prmInfo", prmInfo);
		prm13Ser.addPromotionRuleResult(map);
	}

	/**
	 * 插入促销活动规则条件明细表
	 * 
	 * @param map
	 * @throws JSONException
	 */
	@SuppressWarnings("unchecked")
	private void saveRuleCondition(Map<String, Object> map) throws Exception {
		// 插入促销活动规则条件明细表
		// 查询规则基础属性
		Map<String, Object> basePropMap = binOLCM89_BL.setBaseProp(map);
		map.putAll(basePropMap);
		List<Map<String, Object>> conList = new ArrayList<Map<String, Object>>();
		String timeJson = ConvertUtil.getString(map.get("timeJson"));
		String saveJson = ConvertUtil.getString(map
				.get(CampConstants.SAVE_JSON));
		String locationType = ConvertUtil.getString(map.get("locationType"));
		if ("0".equals(locationType)) {
			locationType = PromotionConstants.LOTION_TYPE_ALL_COUNTER;
		} else if ("5".equals(locationType)) {
			locationType = PromotionConstants.LOTION_TYPE_IMPORT_COUNTER;
		}
		setCondList(conList, timeJson, "startDate", "endDate", null,
				basePropMap);
		setCondList(conList, saveJson, null, null, locationType, basePropMap);
		map.put("conditionList", conList);
		prm13Ser.addPromotionRuleCondition(map);
	}

	/**
	 * 设置条件LIST
	 * 
	 * @param conList
	 * @param json
	 * @param valA
	 * @param valB
	 * @param type
	 * @param propMap
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private void setCondList(List<Map<String, Object>> conList, String json,
			String valA, String valB, String type, Map<String, Object> propMap)
			throws Exception {
		List<Object> list = (List<Object>) JSONUtil.deserialize(json);
		int propId = 0;
		if (null != list) {
			if (null == type) {
				propId = ConvertUtil.getInt(propMap
						.get(PromotionConstants.BASE_PROP_TIME));
			} else if (type.equals(PromotionConstants.LOTION_TYPE_REGION)
					|| type.equals(PromotionConstants.LOTION_TYPE_ALL_COUNTER)) {
				propId = ConvertUtil.getInt(propMap
						.get(PromotionConstants.BASE_PROP_CITY));
			} else if (type.equals(PromotionConstants.LOTION_TYPE_CHANNELS)) {
				propId = ConvertUtil.getInt(propMap
						.get(PromotionConstants.BASE_PROP_CHANNEL));
			}else if (type.equals(PromotionConstants.LOTION_TYPE_7)) {
				propId = ConvertUtil.getInt(propMap
						.get(PromotionConstants.BASEPROP_FACTION));
			}else if(type.equals(PromotionConstants.LOTION_TYPE_ORGANIZATION)){
				propId = ConvertUtil.getInt(propMap
						.get(PromotionConstants.BASE_PROP_ORGANIZATION));
			} 
			else if (type
					.equals(PromotionConstants.LOTION_TYPE_IMPORT_COUNTER)
					|| type.equals(PromotionConstants.LOTION_TYPE_CHANNELS_COUNTER)
					|| type.equals(PromotionConstants.LOTION_TYPE_REGION_COUNTER)
					|| type.equals(PromotionConstants.LOTION_TYPE_8)
					||type.equals(PromotionConstants.LOTION_TYPE_ORGANIZATION_COUNTER)){
				propId = ConvertUtil.getInt(propMap
						.get(PromotionConstants.BASE_PROP_COUNTER));
			}
			if (0 == propId) {
				throw new CherryException("ECM00005");
			}
			for (Object obj : list) {
				Map<String, Object> conMap = new HashMap<String, Object>();
				conMap.put("basePropId", propId);
				conMap.put("conditionGrpId", 1);
				if (null == valA) {
					conMap.put("basePropValue", obj);
				} else {
					Map<String, String> map = (Map<String, String>) obj;
					conMap.put("basePropValue", map.get(valA) + " " + map.get("startTime"));
					if (null != valB) {
						conMap.put("basePropValue2", map.get(valB) + " " + map.get("endTime"));
					}
				}
				if (null != type) {
					conMap.put("locationType", type);
				}
				conList.add(conMap);
			}
		}
	}
	
	/**
	 * 判断促销键是否存在
	 * @param map
	 * @return
	 */
	public boolean isExistShortCode(Map<String, Object> map) {
		List<String> ruleCodeList = prm68Ser.isExistShortCode(map);
		if(null != ruleCodeList && ruleCodeList.size() > 0){
			// 操作区分
			String opt = ConvertUtil.getString(map.get(CampConstants.OPT_KBN));
			if( (CampConstants.OPT_KBN2.equals(opt) && ruleCodeList.indexOf(map.get("ruleCode")) == -1) //编辑
					|| CampConstants.OPT_KBN1.equals(opt) //新建
					|| CampConstants.OPT_KBN3.equals(opt)) {//复制

				return true;
			}
		}
		return false;
	}

	/**
	 * 返回处理后的权限地点JSON
	 * @param parMap
	 * @param locationType
     * @return
     */
	public List<Map<String,Object>> getReturnPlaceJson(Map<String,Object> parMap,String locationType) throws Exception{
		parMap.put("locationType",locationType);
		List<Map<String,Object>> resultList = new LinkedList<Map<String,Object>>();
  		List<Map<String,Object>> userAuthorityPlaceList = getUserAuthorityPlaceList(parMap,locationType);
		if("0".equals(locationType)){
			resultList.addAll(userAuthorityPlaceList);
		}else{
			//将用户权限地点list转为map
			Map<Object,Object> userAuthorityMap = new HashMap<Object,Object>();
			for (Map<String,Object> userMap :userAuthorityPlaceList){
				userAuthorityMap.put(userMap.get("code"),userMap.get("name"));
			}
			List<String> activePlaceList = getProRulePlaceList(parMap,locationType);
//			if(CampConstants.LOTION_TYPE_2.equals(locationType)
//					||CampConstants.LOTION_TYPE_4.equals(locationType)
//					||CampConstants.LOTION_TYPE_5.equals(locationType)
//					||CampConstants.LOTION_TYPE_8.equals(locationType)
//					||CampConstants.LOTION_TYPE_10.equals(locationType)){
//				for(Object obj :activePlaceList){
//					String objStr = ConvertUtil.getString(obj);
//					for(Map<String,Object> userMap :userAuthorityPlaceList){
//						if(objStr.equals(ConvertUtil.getString(userMap.get("code")))){
//							userAuthorityPlaceList.remove(userMap);
//							resultSet.add(userMap);
//							break;
//						}
//					}
//				}
//			}else{
//				for(Object obj :activePlaceList){
//					int objInt = ConvertUtil.getInt(obj);
//					for(Map<String,Object> userMap :userAuthorityPlaceList){
//						if(objInt == ConvertUtil.getInt(userMap.get("code"))){
//							userAuthorityPlaceList.remove(userMap);
//							resultSet.add(userMap);
//							break;
//						}
//					}
//				}
//			}
			for(Object obj :activePlaceList){
				if (userAuthorityMap.containsKey(obj)){
					Map<String,Object> userRetMap = new HashMap<String,Object>();
					userRetMap.put("code",obj);
					userRetMap.put("name",userAuthorityMap.get(obj));
					resultList.add(userRetMap);
				}
			}
		}
		return resultList;
	}

	/**
	 * 获取活动保存的地点
	 * @param map
	 * @param locationType
     * @return
     */
	public List<String> getProRulePlaceList(Map<String,Object> map,String locationType){
		if(CampConstants.LOTION_TYPE_2.equals(locationType)
				||CampConstants.LOTION_TYPE_4.equals(locationType)
				||CampConstants.LOTION_TYPE_5.equals(locationType)
				||CampConstants.LOTION_TYPE_8.equals(locationType)
				||CampConstants.LOTION_TYPE_10.equals(locationType)){
			map.put("basePropName","baseProp_counter");
		}else if(CampConstants.LOTION_TYPE_1.equals(locationType)){
			map.put("basePropName","baseProp_city");
		}else if(CampConstants.LOTION_TYPE_3.equals(locationType)){
			map.put("basePropName","baseProp_channal");
		}else if(CampConstants.LOTION_TYPE_7.equals(locationType)){
			map.put("basePropName","baseProp_faction");
		}else if(CampConstants.LOTION_TYPE_9.equals(locationType)){
			map.put("basePropName","baseProp_organization");
		}
		return prm68Ser.getProRulePlaceList(map);
	}

	/**
	 * 获得用户权限地点List
	 * @param map
	 * @param locationType
     * @return
     */
	public List<Map<String,Object>> getUserAuthorityPlaceList(Map<String,Object> map,String locationType){
		if("0".equals(locationType)
				||CampConstants.LOTION_TYPE_2.equals(locationType)
				||CampConstants.LOTION_TYPE_4.equals(locationType)
				||CampConstants.LOTION_TYPE_5.equals(locationType)
				||CampConstants.LOTION_TYPE_8.equals(locationType)
				||CampConstants.LOTION_TYPE_10.equals(locationType)){
			//取得用户权限柜台
			map.put("userCounterFlag","1");
		}
		List<Map<String,Object>> userAuthorityPlaceList = prm68Ser.getUserAuthorityPlaceList(map);
		if(CampConstants.LOTION_TYPE_7.equals(locationType)){//所属系统
			List<Map<String, Object>> list = codeTable.getCodes("1309");
			if(null != list && list.size() > 0){
				List<Map<String,Object>> userAuthorityPlaceList2 = new ArrayList<Map<String, Object>>();
				//获取用户权限柜台所属系统(Factory)
				for (Map<String, Object> item : list){
					int itemId = ConvertUtil.getInt(item.get("CodeKey"));
					for(Map<String, Object> userItem :userAuthorityPlaceList){
						int userItemId = ConvertUtil.getInt(userItem.get("code"));
						if(itemId==userItemId){
							Map<String,Object> retMap = new HashMap<String, Object>();
							retMap.put("code",userItemId);
							retMap.put("name",item.get("Value"));
							userAuthorityPlaceList2.add(retMap);
							userAuthorityPlaceList.remove(userItem);
							break;
						}
					}
				}
				return userAuthorityPlaceList2;
			}
		}
		return userAuthorityPlaceList;
	}

	/**
	 * Map添加更新共通信息
	 * 
	 * @param map
	 * @return
	 */
	private Map<String, Object> getUpdMap(Map<String, Object> map) {
		Map<String, Object> updMap = new HashMap<String, Object>();
		String sysDate = ConvertUtil.getString(map.get("sysDate"));
		if ("".equals(sysDate)) {
			sysDate = prm68Ser.getSYSDate();
			map.put("sysDate", sysDate);
		}
		// 作成日时
		updMap.put(CherryConstants.CREATE_TIME, sysDate);
		// 更新日时
		updMap.put(CherryConstants.UPDATE_TIME, sysDate);
		// 作成程序名
		updMap.put(CherryConstants.CREATEPGM, "BINOLSSPRM68");
		// 更新程序名
		updMap.put(CherryConstants.UPDATEPGM, "BINOLSSPRM68");
		// 作成者
		updMap.put(CherryConstants.CREATEDBY, map.get(CherryConstants.USERID));
		// 更新者
		updMap.put(CherryConstants.UPDATEDBY, map.get(CherryConstants.USERID));
		return updMap;
	}
}
