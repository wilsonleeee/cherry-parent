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

import com.cherry.cm.cmbussiness.bl.*;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.core.CodeTable;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cp.act.util.ActUtil;
import com.cherry.cp.common.CampConstants;
import com.cherry.cp.common.interfaces.BINOLCPCOM03_IF;
import com.cherry.cp.point.bl.BINOLCPPOI01_BL;
import com.cherry.mo.common.interfaces.BINOLMOCOM01_IF;
import com.cherry.ss.common.PromotionConstants;
import com.cherry.ss.prm.core.CouponConstains;
import com.cherry.ss.prm.service.BINOLSSPRM13_Service;
import com.cherry.ss.prm.service.BINOLSSPRM37_Service;
import com.cherry.ss.prm.service.BINOLSSPRM88_Service;
import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;
import com.opensymphony.xwork2.util.LocalizedTextUtil;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.*;

/**
 *
 * 智能促销BL
 *
 * @author lipc
 * @version 1.0 2013.10.17
 */
public class BINOLSSPRM88_BL {

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

	@Resource
	private BINOLCM33_BL binOLCM33_BL;

	@Resource
	private BINOLCPCOM03_IF binolcpcom03IF;

	@Resource(name = "binOLSSPRM13_Service")
	private BINOLSSPRM13_Service prm13Ser;

	@Resource(name = "binOLSSPRM37_Service")
	private BINOLSSPRM37_Service prm37Ser;

	@Resource(name = "binOLSSPRM88_Service")
	private BINOLSSPRM88_Service prm88Ser;

	@Resource
	private BINOLMOCOM01_IF binOLMOCOM01_BL;

	@Resource
	private CodeTable codeTable;

	public String getBusDate(Map<String, Object> map) {
		return prm88Ser.getBusDate(map);
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
		return prm88Ser.getActRuleInfo(map);
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
	public String getPlaceJson(Map<String, String> palceMap,
							   Map<String, Object> map) throws JSONException {
		// 活动树的所有节点
		List<Map<String, Object>> nodeList = null;
		String res = "[]";
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
				} else if (!"".equals(locationType)) {
					Map<String, Object> params = new HashMap<String, Object>(map);
					params.put(CampConstants.LOCATION_TYPE, locationType);
					// 加载柜台
					params.put(CampConstants.LOADING_CNT, 1);
					nodeList = poi01_BL.getActiveLocation(params);
				}
				if (null != nodeList) {
					// 设置节点选中状态
					if (null != checkedNodes) {
						for (int i = 0; i < checkedNodes.size(); i++) {
							Map<String, Object> checkedNode = checkedNodes.get(i);
							ActUtil.setNodes(nodeList, checkedNode);
						}
					}
					res = JSONUtil.serialize(nodeList);
				}
			}
		}
		return res;
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
		if (!"1".equals(templateFlag)) {
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
			prm88Ser.delPrmActRule(map);
			// 删除促销规则分类
			prm88Ser.delPrmActRuleCate(map);
			// 删除促销活动关联表
			prm88Ser.delPrmActivityRule(map);
			// 删除促销活动条件表
			prm88Ser.delPrmActCondition(map);
			// 删除促销活动结果表
			prm88Ser.delPrmActResult(map);
			// 删除柜台黑名单表
			prm88Ser.delCounterListBlack(map);
			map.put("bin_PromotionActivityID", map.get("activeID"));
		}

		// 添加促销活动关联表
		int ruleID = prm13Ser.addPromotionActivityRule(map);
		map.put("bin_PromotionActivityRuleID", ruleID);
		// 插入促销柜台黑名单表
		saveCounterBlack(map);
		// 插入促销活动规则条件明细表
		saveRuleCondition(map);
		// 插入促销规则结果明细表
		saveRuleResult(map);
		// 规则处理【结果json，规则分类】
		exeRule(map);
		// 添加促销规则表
		prm88Ser.addActRuleInfo(map);
		// 添加促销规则履历表
//		prm88Ser.addActRuleHisInfo(map);
		// 添加促销规则分类表
		saveRuleCate(map);
	}

	/**
	 * 设置促销柜台黑名单表
	 * @param map
	 * @throws Exception
     */
	private void saveCounterBlack(Map<String, Object> map) throws Exception {
		// 删除柜台黑名单表中可能存在的

		if (!CherryChecker.isNullOrEmpty(map.get("placeJsonBlack"))){
			List<Map<String,Object>> placeList = ConvertUtil.json2List(ConvertUtil.getString(map.get("placeJsonBlack")));
			if (placeList!=null){
				for (Map<String,Object> placeItem : placeList){
					placeItem.put("ruleCode",map.get("activityCode"));
					placeItem.put("operateType",1);
				}
				prm88Ser.addCounterListBlack(placeList);
			}
		}
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
			int campId = prm88Ser.addCamp(param);
			param.put("campId", campId);
			// 插入会员活动表
			int campRuleId = prm88Ser.addCampRule(param);
			param.put("campRuleId", campRuleId);
			// 删除条件结果
			prm88Ser.delCampRuleResCond(param);
			prm88Ser.addCampRuleResult(param);
			prm88Ser.addCampRuleCondition(param);
			prm88Ser.addCampRuleConditionCust(param);
		}else{
			// 无效会员主题活动表
			int campId = prm88Ser.delCampain(param);
			if(0 != campId){
				param.put("campId", campId);
				prm88Ser.delCampainRule(param);
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
	public String packJson(String vsn, String cptb, String type, String json) {
		String prefix = "{\"Version\":\"" + vsn + "\",\"Compatible\":\"" + cptb
				+ "\",\"Type\":\"" + type + "\",\"Content\":";
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
			prm88Ser.addActRuleCate(list);
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
		List<String> ruleCodeList = prm88Ser.isExistShortCode(map);
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
			List<Object> activePlaceList = getProRulePlaceList(parMap,locationType);
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
	public List<Object> getProRulePlaceList(Map<String,Object> map,String locationType){
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
		return prm88Ser.getProRulePlaceList(map);
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
		List<Map<String,Object>> userAuthorityPlaceList = prm88Ser.getUserAuthorityPlaceList(map);
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
	 * 删除上次错误信息（多次导入时）
	 * @param map
	 */
	private void deleteOldFailInfo(Map<String, Object> map) throws Exception{
		prm88Ser.deleteOldFailInfo(map);
	}

	/**
	 * 券规则设置导入柜台通用
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> tran_importCounterExecl(Map<String, Object> map) throws Exception {
		//删除上次错误信息（多次导入时）
//		deleteOldFailInfo(map);
		Sheet dataSheet = getDataSheet(map, CherryConstants.COUNTER_SHEET_NAME);
		// 柜台数据sheet不存在
		if (null == dataSheet) {
			throw new CherryException("EBS00030",
					new String[] {  CherryConstants.COUNTER_SHEET_NAME });
		}
		int sheetLength = dataSheet.getRows();
		Map<String,Map<String,Object>> counterTotalMap = new HashMap<String,Map<String,Object>>();
		int brandInfoId = ConvertUtil.getInt(map.get("brandInfoId"));
		String filterType = ConvertUtil.getString(map.get("filterType"));
		//时间戳
		String searchCode = ConvertUtil.getString(map.get("searchCode"));
		//导入模式
		String upMode = ConvertUtil.getString(map.get("upMode"));
		//已有柜台名称
		String counterList = ConvertUtil.getString(map.get("counterList"));

		String counterBlackOrWhiteList="";
		//已导入的柜台
		List<Map<String,Object>> counterImportBoWList = new ArrayList<Map<String,Object>>();
		//操作类型：导入柜台
		String operateType = ConvertUtil.getString(map.get("operateType"));
		//失败导入的list
		List<Map<String,Object>> counterFailList = new LinkedList<Map<String,Object>>();

		// 存放柜台数据
		Map<String, Object> counterInfoMap = new HashMap<String, Object>();

		List<Map<String, Object>> counterSuccessList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object> > failDataList= new ArrayList<Map<String, Object>>();
		String BrandCode = prm88Ser.getBrandCode(map);
		Set<String> originSet_success = new HashSet<String>();
		Set<String> originSet_fail = new HashSet<String>();
		int successCount=0;

		//增量时将页面上已拿到的柜台号当成初始值
		if (upMode.equals(CherryConstants.upMode_1)){
			if (CherryConstants.COUNTER_RULE_WHITE_1.equals(map.get("filterType"))){
				//已导入的柜台
				List<String> counterImportList = new ArrayList<String>();
				if(!CherryChecker.isNullOrEmpty(counterList)){
					counterImportList = (List<String>) JSONUtil.deserialize(counterList);
				}
				for (String counterCode : counterImportList) {
					Map<String, Object> searchMap = new HashMap<String, Object>();
					searchMap.put("counterCode",counterCode);
					searchMap.put("brandInfoId",brandInfoId);
					Map<String, Object> counterMap = prm88Ser.getCounterInfo(searchMap);
					if(!StringUtils.isEmpty(counterMap)){
						originSet_success.add(counterCode);
						counterSuccessList.add(counterMap);
					}
				}
			}else{
				//已导入的柜台
				List<Map<String,Object>> counterImportList = new ArrayList<Map<String,Object>>();
				if(!CherryChecker.isNullOrEmpty(counterList)){
					counterImportList = (List<Map<String,Object>>) JSONUtil.deserialize(counterList);
				}
				for (Map<String, Object> counterCodeMap : counterImportList) {
					Map<String, Object> searchMap = new HashMap<String, Object>();
					searchMap.put("counterCode",counterCodeMap.get("id"));
					searchMap.put("brandInfoId",brandInfoId);
					Map<String, Object> counterMap = prm88Ser.getCounterInfo(searchMap);
					if(!StringUtils.isEmpty(counterMap)){
						originSet_success.add(ConvertUtil.getString(counterCodeMap.get("id")));
						counterSuccessList.add(counterMap);
					}
				}
			}

			if (filterType.equals(CherryConstants.COUNTER_RULE_WHITE_1)){
				counterBlackOrWhiteList=ConvertUtil.getString(map.get("excelCounter_w"));
			}else if (filterType.equals(CherryConstants.COUNTER_RULE_WHITE_2)){
				counterBlackOrWhiteList=ConvertUtil.getString(map.get("excelCounter_b"));
			}
			if(!counterBlackOrWhiteList.equals("")){
				counterSuccessList.clear();
				counterImportBoWList = (List<Map<String,Object>>) JSONUtil.deserialize(counterBlackOrWhiteList);
				for (Map<String, Object> counterCodeBlackOrWhiteMap : counterImportBoWList) {
					Map<String, Object> searchMap = new HashMap<String, Object>();
					if (filterType.equals(CherryConstants.COUNTER_RULE_WHITE_1)){
						searchMap.put("counterCode",counterCodeBlackOrWhiteMap.get("id"));
						originSet_success.add(ConvertUtil.getString(counterCodeBlackOrWhiteMap.get("id")));
					}else{
						searchMap.put("counterCode",counterCodeBlackOrWhiteMap.get("counterCode"));
						originSet_success.add(ConvertUtil.getString(counterCodeBlackOrWhiteMap.get("counterCode")));
					}
					searchMap.put("brandInfoId",brandInfoId);
					Map<String, Object> counterTempMap = prm88Ser.getCounterInfo(searchMap);
					if (!StringUtils.isEmpty(counterTempMap)){
						counterSuccessList.add(counterTempMap);
					}
				}
			}
		}

		List<Map<String, Object>> importCounterList = new ArrayList<Map<String, Object>>();
		for(int r = 1; r < sheetLength; r++){
			Map<String, Object> counterMap = new HashMap<String, Object>();
			//品牌code
			String brandCode = dataSheet.getCell(0,r).getContents().trim();
			//柜台号码
			String counterCode = dataSheet.getCell(1,r).getContents().trim();
			//柜台Name
			String counterName = dataSheet.getCell(2,r).getContents().trim();

			if (CherryChecker.isNullOrEmpty(brandCode)
					&& CherryChecker.isNullOrEmpty(counterCode)
					&& CherryChecker.isNullOrEmpty(counterName)) {
				break;
			}

			counterMap.put("brandCode",brandCode);
			counterMap.put("counterCode",counterCode);
			counterMap.put("counterName",counterName);

			//判断品牌
			if (CherryChecker.isNullOrEmpty(brandCode)||
					!BrandCode.equals(brandCode)) {
				counterMap.put("errorMsg","品牌代码出错");
				counterFailList.add(counterMap);
				continue;
			}
			//判断门店code是否为空
			if (CherryChecker.isNullOrEmpty(counterCode)) {
				counterMap.put("errorMsg","门店柜台为空");
				counterFailList.add(counterMap);
				continue;
			}
			counterMap.put("brandInfoId", brandInfoId);
			Map<String, Object> counterNameMap = prm88Ser.getCounterInfo(counterMap);
			if(StringUtils.isEmpty(counterNameMap)){
				counterMap.put("errorMsg","数据有误，门店代码不存在");
				counterFailList.add(counterMap);
				continue;
			}else{
				counterMap.put("counterName",counterNameMap.get("counterName"));
			}
			//判断导入数据是否有重复的
			if (originSet_success.contains(counterCode)) {
				counterMap.put("errorMsg","导入数据中已有柜台号为:"+counterCode+"的数据");
				counterFailList.add(counterMap);
				originSet_fail.add(counterCode);
				continue;
			}
			originSet_success.add(counterCode);

			if (StringUtils.isEmpty(counterMap.get("errorMsg"))){
				importCounterList.add(counterMap);
//				counterSuccessList.add(counterMap);
//				successCount++;
			}
		}
		//剔除重复数据
		if (originSet_fail.size()>0){
			for (String originCounter : originSet_fail){
				for (Map<String, Object> successMap : importCounterList){
					if (originCounter.equals(ConvertUtil.getString(successMap.get("counterCode")))){
						successMap.put("errorMsg","导入数据中已有柜台号为:"+originCounter+"的数据");
						counterFailList.add(successMap);
						importCounterList.remove(successMap);
						break;
					}
				}
			}
		}
		counterSuccessList.addAll(importCounterList);
		successCount=importCounterList.size();

		int failCount = counterFailList.size();
		int resultCode = 0;
		if (failCount>0){
			failDataList = setFailList(counterFailList,filterType,operateType,searchCode);
			prm88Ser.insertFailDataList(failDataList);
			resultCode = 1;
		}
		if(CherryConstants.COUNTER_RULE_WHITE_1.equals(map.get("filterType"))){//柜台白名单
			List resultTreeList = new ArrayList();
			List keysList = new ArrayList();
			String[] keys1 = { "counterCode", "counterName" };
			keysList.add(keys1);
			ConvertUtil
					.jsTreeDataDeepList(counterSuccessList, resultTreeList, keysList, 0);
			counterInfoMap.put("successCount", successCount);
			counterInfoMap.put("failCount", failDataList.size());
			counterInfoMap.put("trueCounterList", resultTreeList);
			counterInfoMap.put("resultCode", resultCode);
			counterInfoMap.put("searchCode", searchCode);
		}else{//柜台黑名单
			counterInfoMap.put("successCount", successCount);
			counterInfoMap.put("failCount", failDataList.size());
			counterInfoMap.put("counterSuccessList", counterSuccessList);
			counterInfoMap.put("resultCode", resultCode);
			counterInfoMap.put("searchCode", searchCode);
		}


		return counterInfoMap;
	}

	public Map<String,Object> tran_importMemberExecl(Map<String,Object> map) throws Exception{
		Sheet dataSheet = getDataSheet(map, PromotionConstants.MEMBER_SHEET_NAME);
		// 会员数据sheet不存在
		if (null == dataSheet) {
			throw new CherryException("EBS00030",
					new String[] { PromotionConstants.MEMBER_SHEET_NAME });
		}
		int sheetLength = dataSheet.getRows();
		// 单次导入上限
		if (sheetLength > CouponConstains.UPLOAD_MAX_COUNT) {
			throw new CherryException("ESS01005",
					new String[]{String.valueOf(CouponConstains.UPLOAD_MAX_COUNT)});
		}
		Map<String,Map<String,Object>> memberTotalMap = new HashMap<String,Map<String,Object>>();
		String filterType = ConvertUtil.getString(map.get("filterType"));
		int brandInfoId = ConvertUtil.getInt(map.get("brandInfoId"));
		int organizationInfoId = ConvertUtil.getInt(map.get("organizationInfoId"));
		String searchCode = ConvertUtil.getString(map.get("searchCode"));

		//失败导入的list
		List<Map<String,Object>> memberFailList = new LinkedList<Map<String,Object>>();

		//导入模式
		String upMode = (String) map.get("upMode");

		//品牌Code
		String BrandCode = prm88Ser.getBrandCode(map);

		List<String> originList = new LinkedList<String>();
		Set<String> originSet = new HashSet<String>();

		// 获取手机号验证规则配置项
		String mobileRule = binOLCM14_BL.getConfigValue("1090", ConvertUtil.getString(organizationInfoId), ConvertUtil.getString(brandInfoId));

		int originCount = 0;
		getUpdMap(map);
		//增量
		if(upMode.equals(PromotionConstants.UPMODE_1)){
			//searchCode对应数据
			originCount = prm88Ser.getOriginMobileCount(map);
			if(originCount!=0){
				originList = prm88Ser.getOriginMobileList(map);
				//插入原始数据
				prm88Ser.insertOriginData(map);
			}
		}
		if(originList.size()>0 && originList!=null){
			//将原有List中的值转为Key存入到Set中
			for(String mobileStr : originList){
				originSet.add(mobileStr);
			}
		}
		//重复导入的set
		Set<String> dupSet = new HashSet<String>();
		if (originSet.size()==0||originSet==null) {
            for (int r = 1; r < sheetLength; r++){
				Map<String, Object> memberMap = new HashMap<String, Object>();
				//品牌code
				String brandCode = dataSheet.getCell(0,r).getContents().trim();
				//会员卡号
				String memberCode = dataSheet.getCell(1,r).getContents().trim();
				//会员手机号码
				String mobile = dataSheet.getCell(2,r).getContents().trim();
				//BPCode
				String bpCode = dataSheet.getCell(3,r).getContents().trim();
				//MemberLevel
				String memberLevel = dataSheet.getCell(4,r).getContents().trim();
				//Name
				String name = dataSheet.getCell(5,r).getContents().trim();

				if (CherryChecker.isNullOrEmpty(brandCode)
						&&CherryChecker.isNullOrEmpty(memberCode)
						&&CherryChecker.isNullOrEmpty(mobile)
						&&CherryChecker.isNullOrEmpty(bpCode)
						&&CherryChecker.isNullOrEmpty(memberLevel)
						&&CherryChecker.isNullOrEmpty(name)){
					break;
				} else {
					memberMap.put("brandCode",brandCode);
					memberMap.put("memberCode",memberCode);
					memberMap.put("mobile",mobile);
					memberMap.put("name",name);
					//判断品牌
					if(CherryChecker.isNullOrEmpty(brandCode)
							||!brandCode.equals(BrandCode)){
						memberMap.put("errorMsg","品牌代码出错");
						memberFailList.add(memberMap);
						continue;
					}
					//判断mobile是否为空
					if(CherryChecker.isNullOrEmpty(mobile)){
						memberMap.put("errorMsg","手机号码为空");
						memberFailList.add(memberMap);
						continue;
					}
					//判断手机号码是否符合标准
					if(!CherryChecker.isPhoneValid(mobile,mobileRule)){
						memberMap.put("errorMsg","手机号码不符合校验规则");
						memberFailList.add(memberMap);
						continue;
					}
					//判断导入数据是否有重复的
					if(memberTotalMap.containsKey(mobile)){
						memberMap.put("errorMsg","导入数据中已有手机为:"+mobile+"的数据");
						memberFailList.add(memberMap);
						dupSet.add(mobile);
						continue;
					}
					//判断会员卡号是否有(针对导入数据无会员卡号)
					if (CherryChecker.isNullOrEmpty(memberCode)){
						memberMap.put("memberCode",mobile);
					}
					// 设置拓展信息
					setExtInfo(memberMap,bpCode,memberLevel);
					// 更新通用map
					getUpdMap(memberMap);
					memberMap.put(CherryConstants.BRANDINFOID,brandInfoId);
					memberMap.put(CherryConstants.ORGANIZATIONINFOID,organizationInfoId);
					memberMap.put("searchCode", searchCode);
					memberTotalMap.put(mobile,memberMap);
				}
			}
		} else {
			//考虑数据库的数据与导入的数据重复校验
			for (int r = 1; r < sheetLength; r++){
				Map<String, Object> memberMap = new HashMap<String, Object>();
				//品牌code
				String brandCode = dataSheet.getCell(0,r).getContents().trim();
				//会员卡号
				String memberCode = dataSheet.getCell(1,r).getContents().trim();
				//会员手机号码
				String mobile = dataSheet.getCell(2,r).getContents().trim();
				//BPCode
				String bpCode = dataSheet.getCell(3,r).getContents().trim();
				//MemberLevel
				String memberLevel = dataSheet.getCell(4,r).getContents().trim();
				//Name
				String name = dataSheet.getCell(5,r).getContents().trim();

				if (CherryChecker.isNullOrEmpty(brandCode)
						&&CherryChecker.isNullOrEmpty(memberCode)
						&&CherryChecker.isNullOrEmpty(mobile)
						&&CherryChecker.isNullOrEmpty(bpCode)
						&&CherryChecker.isNullOrEmpty(memberLevel)
						&&CherryChecker.isNullOrEmpty(name)){
					break;
				} else {
					memberMap.put("brandCode",brandCode);
					memberMap.put("memberCode",memberCode);
					memberMap.put("mobile",mobile);
					memberMap.put("name",name);
					//判断品牌
					if(CherryChecker.isNullOrEmpty(brandCode)
							||!brandCode.equals(BrandCode)){
						memberMap.put("errorMsg","品牌代码出错");
						memberFailList.add(memberMap);
						continue;
					}
					//判断mobile是否为空
					if(CherryChecker.isNullOrEmpty(mobile)){
						memberMap.put("errorMsg","手机号码为空");
						memberFailList.add(memberMap);
						continue;
					}
					//判断手机号码是否符合标准
					if(!CherryChecker.isPhoneValid(mobile,mobileRule)){
						memberMap.put("errorMsg","手机号码不符合校验规则");
						memberFailList.add(memberMap);
						continue;
					}
					//判断导入数据是否与数据库中数据重复
					if(originSet.contains(mobile)){
						memberMap.put("errorMsg","数据库中已经有存在的数据");
						memberFailList.add(memberMap);
						continue;
					}
					//判断导入数据是否有重复的
					if(memberTotalMap.containsKey(mobile)){
						memberMap.put("errorMsg","导入数据中已有手机为:"+mobile+"的数据");
						memberFailList.add(memberMap);
						dupSet.add(mobile);
						continue;
					}
					//判断会员卡号是否有(针对导入数据无会员卡号)
					if (CherryChecker.isNullOrEmpty(memberCode)){
						memberMap.put("memberCode",mobile);
					}
					// 设置拓展信息
					setExtInfo(memberMap,bpCode,memberLevel);
					// 更新通用map
					getUpdMap(memberMap);
					memberMap.put(CherryConstants.BRANDINFOID,brandInfoId);
					memberMap.put(CherryConstants.ORGANIZATIONINFOID,organizationInfoId);
					memberMap.put("searchCode", searchCode);
					memberTotalMap.put(mobile,memberMap);
				}
			}
		}
		//剔除重复数据
		if (dupSet.size()>0){
			for (String dupMobile : dupSet){
				memberTotalMap.remove(dupMobile);
			}
		}
		//白名单需要插入到SearchLog表中
		if (filterType.equals(PromotionConstants.FILTERTYPE_1)){
			if ((memberTotalMap.size()+originCount)>0){
				//customerType 4为不限
				map.put("customerType",4);
				//recordType 2为探索结果
				map.put("recordType",2);
				//recordCount记录条数
				map.put("recordCount",memberTotalMap.size()+originCount);
				//comments
				map.put("comments","Execl导入");
				//fromType 3为导入
				map.put("fromType",3);
				prm88Ser.addMemSearchLog(map);
			}
		}
		if (memberTotalMap.size()>0){
			//循环数据
			List<Map<String,Object>> memberList = new LinkedList<Map<String,Object>>();
			for(Map.Entry<String, Map<String,Object>> entry : memberTotalMap.entrySet()){
				memberList.add(entry.getValue());
			}
			// 插入数据库
			prm88Ser.insertMemberList(memberList);
		}
		//失败件数
		int failCount = memberFailList.size();
		//如果有失败的list,则添加到失败表中存储
		if (failCount>0) {
			List<Map<String, Object> > failDataList = setFailList(memberFailList,filterType,PromotionConstants.Fail_OperateType_3,searchCode);
			prm88Ser.insertFailDataList(failDataList);
		}
		Map<String, Object> resultMap = new HashMap<String, Object>();
		int resultCode = 0;
		if (failCount > 0) {
			resultCode = 1;
		}
		resultMap.put("failCount",failCount);
		// 结果代号
		resultMap.put("resultCode", resultCode);
		resultMap.put("successCount",memberTotalMap.size());
		resultMap.put("searchCode",map.get("searchCode"));
		return resultMap;
	}

	/**
	 * 设置导入会员拓展信息
	 * @param memberMap
	 * @param bpCode
	 * @param memberLevel
     */
	private void setExtInfo(Map<String ,Object> memberMap,String bpCode,String memberLevel) throws Exception{
		Map<String,Object> extInfo = new HashMap<String,Object>();
		extInfo.put("bpCode",ConvertUtil.getString(bpCode));
		extInfo.put("memberLevel",ConvertUtil.getString(memberLevel));
		memberMap.put("extInfo",CherryUtil.map2Json(extInfo));
	}

	/**
	 * 导入产品execl处理
	 * @param map
	 * @return
	 * @throws Exception
     */
	public Map<String,Object> tran_importProductExecl(Map<String,Object> map) throws Exception{
		//删除上次错误信息（多次导入时）
//		deleteOldFailInfo(map);
		Map<String,Object> resultMap = new HashMap<String,Object>();
		Sheet dataSheet = getDataSheet(map, PromotionConstants.PRODUCT_SHEET_NAME);
		// 产品sheet不存在
		if (null == dataSheet) {
			throw new CherryException("EBS00030",
					new String[] { PromotionConstants.PRODUCT_SHEET_NAME });
		}
		int sheetLength = dataSheet.getRows();
		// 单次导入上限
		if (sheetLength > CouponConstains.UPLOAD_MAX_COUNT) {
			throw new CherryException("ESS01005",
					new String[]{String.valueOf(CouponConstains.UPLOAD_MAX_COUNT)});
		}
		Map<String,Map<String,Object>> productTotalMap = new HashMap<String,Map<String,Object>>();
		String filterType = ConvertUtil.getString(map.get("filterType"));
		int brandInfoId = ConvertUtil.getInt(map.get("brandInfoId"));
		int organizationInfoId = ConvertUtil.getInt(map.get("organizationInfoId"));
		String searchCode = ConvertUtil.getString(map.get("searchCode"));
		//导入模式
		String upMode = ConvertUtil.getString(map.get("upMode"));
		String productBlackList = ConvertUtil.getString(map.get("excelCounter_b"));
		//已导入的产品
		List<Map<String,Object>> productImportList = new ArrayList<Map<String,Object>>();
		Map<String,Object> productImportMap = new HashMap<String,Object>();
		if (upMode.equals(CherryConstants.upMode_1)){
			if(!CherryChecker.isNullOrEmpty(productBlackList)){
				productImportMap = (Map<String,Object>) JSONUtil.deserialize(productBlackList);
				productImportList = (List<Map<String,Object>>)productImportMap.get("productJson");
			}
		}


		List<Map<String,Object>> productFailList = new LinkedList<Map<String,Object>>();

		Set<String> dupSet = new HashSet<String>();

		//品牌Code
		String BrandCode = prm88Ser.getBrandCode(map);
		if (PromotionConstants.FILTERTYPE_2.equals(filterType)){
			//黑名单 只需要校验产品
			for (int r = 1; r < sheetLength; r++){
				Map<String, Object> productMap = new HashMap<String, Object>();
				//品牌code
				String brandCode = dataSheet.getCell(0,r).getContents().trim();
				//initCode
				String unitCode =  dataSheet.getCell(1,r).getContents().trim();
				//barCode
				String barCode = dataSheet.getCell(2,r).getContents().trim();
				//productName
				String productName = dataSheet.getCell(3,r).getContents().trim();

				if (CherryChecker.isNullOrEmpty(brandCode)
						&& CherryChecker.isNullOrEmpty(unitCode)
						&& CherryChecker.isNullOrEmpty(barCode)
						&& CherryChecker.isNullOrEmpty(productName)) {
					break;
				}

				productMap.put("brandCode",brandCode);
				productMap.put("unitCode",unitCode);
				productMap.put("barCode",barCode);
				productMap.put("productName",productName);
				//判断品牌
				if (CherryChecker.isNullOrEmpty(brandCode)
						||!brandCode.equals(BrandCode)){
					productMap.put("errorMsg","品牌代码出错");
					productFailList.add(productMap);
					continue;
				}
				//判断产品编码
				if (CherryChecker.isNullOrEmpty(unitCode)){
					productMap.put("errorMsg","产品编码为空");
					productFailList.add(productMap);
					continue;
				}

				//判断产品是否真实存在
				productMap.put("brandInfoId",brandInfoId);
				productMap.put("organizationInfoId",organizationInfoId);
				Map<String,Object> productInfo = prm88Ser.getProductInfo(productMap);
				if(productInfo==null){
					productMap.put("errorMsg","产品编码不存在!");
					productFailList.add(productMap);
					continue;
				}else {
					if (productName.equals("")){
						productMap.put("productName",productInfo.get("productName"));
					}
					if (barCode.equals("")){
						productMap.put("barCode",productInfo.get("barCode"));
					}
				}

				//判断导入数据是否有存在
				if (productTotalMap.containsKey(unitCode)){
					dupSet.add(unitCode);
					productMap.put("errorMsg","导入产品中已经存在相同数据");
					productFailList.add(productMap);
					continue;
				}
				productTotalMap.put(unitCode,productInfo);
			}
		}else{

		}
		//剔除重复数据
		if (dupSet.size()>0){
			for (String unitCodeStr : dupSet){
				Map<String, Object> tempMap =productTotalMap.get(unitCodeStr);
				tempMap.put("errorMsg","导入产品中已经存在相同数据");
				productFailList.add(tempMap);
				productTotalMap.remove(unitCodeStr);
			}
		}
		int failCount = productFailList.size();
		if (failCount>0){
			List<Map<String,Object>> failDataList = setFailList(productFailList,filterType,PromotionConstants.Fail_OperateType_2,searchCode);
			prm88Ser.insertFailDataList(failDataList);
		}
		int resultCode = 0;
		if (failCount > 0) {
			resultCode = 1;
		}
		if (productTotalMap.size()>0){
			//循环数据
			List<Map<String,Object>> productList = new LinkedList<Map<String,Object>>();
			for(Map.Entry<String, Map<String,Object>> entry : productTotalMap.entrySet()){
				productList.add(entry.getValue());
			}
			productList.addAll(productImportList);
			resultMap.put("productJson",productList);
		}else{
			resultMap.put("productJson",productImportList);
		}

		// 结果代号
		resultMap.put("resultCode", resultCode);
		resultMap.put("failCount",failCount);
		resultMap.put("successCount",productTotalMap.size());
		resultMap.put("searchCode",searchCode);
		return resultMap;
	}

	public Map<String, Object> tran_importShopProductExecl(Map<String, Object> map) throws Exception {
		//删除上次错误信息（多次导入时）
//		deleteOldFailInfo(map);
		Sheet dataSheet = getDataSheet(map, PromotionConstants.PRODUCT_SHEET_NAME);
		// 柜台数据sheet不存在
		if (null == dataSheet) {
			throw new CherryException("EBS00030",
					new String[]{CherryConstants.COUNTER_SHEET_NAME});
		}
		int sheetLength = dataSheet.getRows();

		//时间戳
		String searchCode = ConvertUtil.getString(map.get("searchCode"));
		String filterType = ConvertUtil.getString(map.get("filterType"));
		int brandInfoId = ConvertUtil.getInt(map.get("brandInfoId"));
		int organizationInfoId = ConvertUtil.getInt(map.get("organizationInfoId"));
		//失败导入的list
		List<Map<String, Object>> shopProductFailList = new ArrayList<Map<String, Object>>();
		//成功导入的list
		List<Map<String, Object>> shopProducSuccessList = new ArrayList<Map<String, Object>>();
		//全部的list
		List<Map<String, Object>> shopProducAllList = new ArrayList<Map<String, Object>>();
		//list的索引
		int index =-1;
		String productRangeAll="";

		Set<String> dupProduct = new HashSet<String>();
		Set<String> dupProductDelete = new HashSet<String>();
		// 存放活动产品
		Map<String, Object> shopProducInfoMap = new HashMap<String, Object>();
		String BrandCode = prm88Ser.getBrandCode(map);
		//导入模式
		String upMode = ConvertUtil.getString(map.get("upMode"));
		String productWhiteList = ConvertUtil.getString(map.get("excelCounter_w"));
		//已导入的产品
		List<Map<String,Object>> productImportList = new ArrayList<Map<String,Object>>();
		if (upMode.equals(CherryConstants.upMode_1)){
			if(!CherryChecker.isNullOrEmpty(productWhiteList)){
				productImportList = (List<Map<String,Object>>) JSONUtil.deserialize(productWhiteList);
			}
		}
		for (int r = 1; r < sheetLength; r++) {
			Map<String, Object> productMap = new HashMap<String, Object>();
			//品牌code
			String brandCode = dataSheet.getCell(0, r).getContents().trim();
			//是否产品范围
			String productRange = dataSheet.getCell(1, r).getContents().trim();
			//是否特定产品
			String productAppointed = dataSheet.getCell(2, r).getContents().trim();
			//产品编码
			String unitCode = dataSheet.getCell(3, r).getContents().trim();
			//产品条码
			String barCode = dataSheet.getCell(4, r).getContents().trim();
			//产品名称
			String productName = dataSheet.getCell(5, r).getContents().trim();
			//产品数量
			String quantityOrAmount = dataSheet.getCell(6, r).getContents().trim();
			//比较条件
			String compareCondition = dataSheet.getCell(7, r).getContents().trim();
			//比较值
			String compareValue = dataSheet.getCell(8, r).getContents().trim();
			//是否校验数量，比较条件，比较值等字段
			boolean isCheckother =false;
			//该条数据是否有误
			boolean isError = false;
			//是否导入范围的第一条数据
			boolean isRangeFist =false;
			if (CherryChecker.isNullOrEmpty(brandCode)
					&& CherryChecker.isNullOrEmpty(productRange)
					&& CherryChecker.isNullOrEmpty(productAppointed)
					&& CherryChecker.isNullOrEmpty(unitCode)
					&& CherryChecker.isNullOrEmpty(barCode)
					&& CherryChecker.isNullOrEmpty(productName)
					&& CherryChecker.isNullOrEmpty(quantityOrAmount)
					&& CherryChecker.isNullOrEmpty(compareCondition)
					&& CherryChecker.isNullOrEmpty(compareValue)) {
				break;
			}
			if (r==1){
				index++;
				//第一条数据的时候判定是否范围
				productRangeAll=productRange;
			}
			//产品范围为空，即为特定产品
			if(!productRange.equals(PromotionConstants.YES)&&r!=1){
				index++;
				productRangeAll=productRange;
				isCheckother=true;
			}else{
				//产品范围不为空，并且与上次的范围不等，则认为从特定产品转为范围导入
				if (r!=1){
					if (!productRangeAll.equals(productRange)){
						index++;
						productRangeAll=productRange;
						isRangeFist=true;
						isCheckother=true;
					}
				}else {
					isCheckother=true;
				}
			}

			productMap.put("brandCode", brandCode);
			productMap.put("productRange", productRange);
			productMap.put("productAppointed", productAppointed);
			productMap.put("unitCode", unitCode);
			productMap.put("barCode", barCode);
			productMap.put("productName", productName);
			productMap.put("quantityOrAmount", quantityOrAmount);
			productMap.put("compareCondition", compareCondition);
			productMap.put("compareValue", compareValue);
			//判断品牌
			if (CherryChecker.isNullOrEmpty(brandCode)||
					!BrandCode.equals(brandCode)) {
				productMap.put("errorMsg","品牌代码出错");
				isError = true;
			}
			if(!CherryChecker.isNullOrEmpty(productRange)&&
					!PromotionConstants.YES.equals(productRange)){
				productMap.put("errorMsg","是否产品范围出错");
				isError = true;
			}
			if(!PromotionConstants.YES.equals(productAppointed)){
				productMap.put("errorMsg","是否特定产品出错");
				isError = true;
			}
			//放置页面所需数据
			if (PromotionConstants.YES.equals(productAppointed)){
				if(PromotionConstants.YES.equals(productRange)){
					productMap.put("rangeType","RANGE");
				}else{
					productMap.put("rangeType","PRODUCT");
				}
			}
			//rangeOpt 当为产品范围以及特定产品时写死为EQUAL
			productMap.put("rangeOpt","EQUAL");


			if (CherryChecker.isNullOrEmpty(unitCode)){
				productMap.put("errorMsg","产品编码为空");
				isError = true;
			}
//			if (CherryChecker.isNullOrEmpty(barCode)){
//				productMap.put("errorMsg","产品条码为空");
//				isError = true;
//			}
			//判断产品是否真实存在
			productMap.put("brandInfoId",brandInfoId);
			productMap.put("organizationInfoId",organizationInfoId);
			Map<String, Object> productVenderInfo = prm88Ser.getProductInfo(productMap);
			String prtVendorId="";
			if(StringUtils.isEmpty(productVenderInfo)){
				productMap.put("errorMsg","数据有误，产品编码不存在");
				isError = true;
			}else {
				prtVendorId =ConvertUtil.getString(productVenderInfo.get("prtVendorId"));
				barCode=ConvertUtil.getString(productVenderInfo.get("barCode"));
				productName=ConvertUtil.getString(productVenderInfo.get("productName"));
				productMap.put("barCode", barCode);
				productMap.put("productName", productName);
			}
			if(isCheckother){
				if (CherryChecker.isNullOrEmpty(quantityOrAmount)){
					productMap.put("errorMsg","数量或金额为空");
					isError = true;
				}
				if (PromotionConstants.QUANTITY_Shop.equals(quantityOrAmount)){
					productMap.put("propName","QUANTITY");
				}else if (PromotionConstants.AMOUNT_Shop.equals(quantityOrAmount)){
					productMap.put("propName","AMOUNT");
				}else{
					productMap.put("errorMsg","数量金额字段输入值有误");
					isError = true;
				}
				if(PromotionConstants.EQ.equals(compareCondition)){
					productMap.put("propOpt","EQ");
				}else if(PromotionConstants.NE.equals(compareCondition)){
					productMap.put("propOpt","NE");
				}else if(PromotionConstants.LE.equals(compareCondition)){
					productMap.put("propOpt","LE");
				}else if(PromotionConstants.GT.equals(compareCondition)){
					productMap.put("propOpt","GT");
				}else if(PromotionConstants.GE.equals(compareCondition)){
					productMap.put("propOpt","GE");
				}else if(PromotionConstants.LT.equals(compareCondition)){
					productMap.put("propOpt","LT");
				}else{
					productMap.put("errorMsg","比较条件出错");
					isError = true;
				}

				if (CherryChecker.isNullOrEmpty(compareValue)){
					productMap.put("errorMsg","比较值为空");
					isError = true;
				}
				if (PromotionConstants.QUANTITY_Shop.equals(quantityOrAmount)){
					if (!CherryChecker.isNumeric(compareValue)){
						productMap.put("errorMsg","购买条件的数量必须为大于0的整数");
						isError = true;
					}else{
						if (ConvertUtil.getDouble(compareValue)<=0){
							productMap.put("errorMsg","购买条件的数量必须为大于0的整数");
							isError = true;
						}
					}
				}else if (PromotionConstants.AMOUNT_Shop.equals(quantityOrAmount)){
					if (!CherryChecker.isFloatValid(compareValue,6,3)){
						productMap.put("errorMsg","比较值必须为正数");
						isError = true;
					}
				}
				productMap.put("propValue", compareValue);
			}
			List<Map<String, Object>> productList = new ArrayList<Map<String, Object>>();
			Map<String, Object> productInfoTemp = new HashMap<String, Object>();
			productInfoTemp.put("brandCode",brandCode);
			productInfoTemp.put("unitCode", unitCode);
			productInfoTemp.put("barCode", barCode);
			productInfoTemp.put("nameTotal", productName);
			productInfoTemp.put("prtVendorId", prtVendorId);
			if (isError){
				productMap.put("isError",true);
				//将错误信息放入productList中
				productInfoTemp.put("errorMsg",productMap.get("errorMsg"));
			}
			productList.add(productInfoTemp);
			productMap.put("productList",productList);
			String rangeType=ConvertUtil.getString(productMap.get("rangeType"));
			String propName=ConvertUtil.getString(productMap.get("propName"));
			String propOpt=ConvertUtil.getString(productMap.get("propOpt"));
			String compareValueToAdd=ConvertUtil.getString(productMap.get("compareValue"));
			if (rangeType.equals("PRODUCT")){
				String key=unitCode+propName+propOpt+compareValueToAdd;
				productMap.put(key,"特定产品重复校验");
				if (dupProduct.contains(key)){
					dupProductDelete.add(key);
				}
				dupProduct.add(key);
			}

			//处理第一条数据时
			if(shopProducAllList.size()==0){
				shopProducAllList.add(productMap);
			}else{
				if (!PromotionConstants.YES.equals(productRange)){
					shopProducAllList.add(productMap);
				}else{
					if(isRangeFist){
						shopProducAllList.add(productMap);
					}else {
						Map<String, Object> productMapTemp = shopProducAllList.get(index);
						List<Map<String,Object>> productListTemp=(List<Map<String,Object>>)(productMapTemp.get("productList"));
						productListTemp.add(productInfoTemp);
						if (!StringUtils.isEmpty(productInfoTemp.get("errorMsg"))){
							shopProducAllList.get(index).put("isError",true);
						}
						shopProducAllList.get(index).put("productList",productListTemp);
					}
				}
			}
//			int failCount = shopProductFailList.size();
//			List<Map<String, Object>> failDataList = null;
//			if (failCount > 0) {
//				failDataList = setFailList(shopProductFailList, "1", operateType, searchCode);
//				prm88Ser.insertFailDataList(failDataList);
//			}
		}
		int successCount=0;
		for (Map<String,Object> shopProducAllMap:shopProducAllList){
			String isSuccess=ConvertUtil.getString(shopProducAllMap.get("isError"));
			if(isSuccess.equals("true")){
				shopProductFailList.add(shopProducAllMap);
			}else {
				shopProducSuccessList.add(shopProducAllMap);
//				List<Map<String,Object>> successCountListTemp=(List<Map<String,Object>>)(shopProducAllMap.get("productList"));
			}
		}

		for(Map<String,Object> shopProducSuccessMap:shopProducSuccessList){
			//特定产品之间全部字段相同时为重复数据
			 if (ConvertUtil.getString(shopProducSuccessMap.get("rangeType")).equals("RANGE")){
				List<Map<String,Object>> productTempList=(List<Map<String,Object>>)(shopProducSuccessMap.get("productList"));
				Set<String> dupSet = new HashSet<String>();
				Set<String> dupDelete = new HashSet<String>();
				for(Map<String,Object> productTempMap:productTempList){
					if (dupSet.contains(ConvertUtil.getString(productTempMap.get("unitCode")))){
						dupDelete.add(ConvertUtil.getString(productTempMap.get("unitCode")));
					}
					dupSet.add(ConvertUtil.getString(productTempMap.get("unitCode")));
				}
				for(Map<String,Object> productTempMap:productTempList){
					for (String unitCodeStr : dupDelete){
						if (unitCodeStr.equals(ConvertUtil.getString(productTempMap.get("unitCode")))){
							shopProducSuccessMap.put("isError",true);
							productTempMap.put("errorMsg","该产品范围内已经存在相同数据");
						}
					}
				}
			}else{
				 for (String unitCodeStr : dupProductDelete){
					 if (shopProducSuccessMap.containsKey(unitCodeStr)){
						 shopProducSuccessMap.put("isError",true);
						 Map<String,Object> productTempMap=((List<Map<String,Object>>)(shopProducSuccessMap.get("productList"))).get(0);
						 productTempMap.put("errorMsg","导入特定产品中已经存在相同数据");
						 break;
					 }
				 }

			 }
		}
		List<Map<String,Object>> shopProducSuccessListFinal = new ArrayList<Map<String,Object>>();
		for (Map<String,Object> shopProducMap:shopProducSuccessList){
			String isSuccess=ConvertUtil.getString(shopProducMap.get("isError"));
			if(isSuccess.equals("true")) {
				shopProductFailList.add(shopProducMap);
			}else {
				shopProducSuccessListFinal.add(shopProducMap);
			}
		}

		successCount+=shopProducSuccessListFinal.size();
		if (upMode.equals(CherryConstants.upMode_1)){
			shopProducSuccessList.addAll(productImportList);
		}
		int failCount = shopProductFailList.size();
		List<Map<String, Object>> failDataList = null;
		int resultCode = 0;
		if (failCount > 0) {
			resultCode = 1;
			failDataList=setFailList(shopProductFailList,filterType, PromotionConstants.Fail_OperateType_2, searchCode);
			prm88Ser.insertFailDataList(failDataList);
		}

		shopProducInfoMap.put("productJson",shopProducSuccessListFinal);
		// 结果代号
		shopProducInfoMap.put("resultCode", resultCode);
		shopProducInfoMap.put("failCount",failCount);
		shopProducInfoMap.put("successCount",successCount);
		shopProducInfoMap.put("searchCode",searchCode);
		return shopProducInfoMap;
	}


	private Sheet getDataSheet(Map<String, Object> map, String sheetName) throws Exception{
		// 取得上传文件path
		File upExcel = (File) map.get("upExcel");
		if (upExcel == null || !upExcel.exists()) {
			// 上传文件不存在
			throw new CherryException("EBS00042");
		}
		InputStream inStream = null;
		Workbook wb = null;
		try {
			inStream = new FileInputStream(upExcel);
			// 防止GC内存回收的设置
			WorkbookSettings workbookSettings = new WorkbookSettings();
			workbookSettings.setGCDisabled(true);
			wb = Workbook.getWorkbook(inStream, workbookSettings);
		} catch (Exception e) {
			throw new CherryException("EBS00041");
		} finally {
			if (inStream != null) {
				// 关闭流
				inStream.close();
			}
		}
		// 获取sheet
		Sheet[] sheets = wb.getSheets();
		// 门店数据sheet
		Sheet dataSheet = null;
		for (Sheet st : sheets) {
			if (sheetName.equals(st.getName().trim())) {
				dataSheet = st;
				break;
			}
		}
		return dataSheet;
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
			sysDate = prm88Ser.getSYSDate();
			map.put("sysDate", sysDate);
		}
		// 作成日时
		updMap.put(CherryConstants.CREATE_TIME, sysDate);
		// 更新日时
		updMap.put(CherryConstants.UPDATE_TIME, sysDate);
		// 作成程序名
		updMap.put(CherryConstants.CREATEPGM, "BINOLSSPRM88");
		// 更新程序名
		updMap.put(CherryConstants.UPDATEPGM, "BINOLSSPRM88");
		// 作成者
		updMap.put(CherryConstants.CREATEDBY, map.get(CherryConstants.USERID));
		// 更新者
		updMap.put(CherryConstants.UPDATEDBY, map.get(CherryConstants.USERID));
		return updMap;
	}

	/**
	 * 设置失败数据list
	 * @param uploadFailList 失败list
	 * @param filterType 黑白名单 1白名单 2黑名单
	 * @param operateType 操作区分 1柜台 2产品 3会员
	 * @param searchCode 操作标志(为时间)
	 * @return
	 * @throws Exception
	 */
	private List<Map<String,Object>> setFailList(List<Map<String,Object>> uploadFailList,String filterType,
												String operateType,String searchCode) throws Exception{
		List<Map<String,Object>> failListFinal = new LinkedList<Map<String,Object>>();
		if (operateType.equals(PromotionConstants.Fail_OperateType_2)&&
				filterType.equals(PromotionConstants.Fail_OperateType_1)) {
			//产品白名单的数据处理
			for (Map<String, Object> failItem : uploadFailList) {
				List<Map<String, Object>> productList = (List<Map<String, Object>>) failItem.get("productList");
				String isError = ConvertUtil.getString(failItem.get("isError"));
				for (Map<String, Object> productInfo : productList) {
					if (isError.equals("true") && StringUtils.isEmpty(productInfo.get("errorMsg"))) {
						productInfo.put("errorMsg", "该产品范围内数据有误");
					}
					failItem.putAll(productInfo);
					Map<String,Object> tempMap =new HashMap<String, Object>();
					tempMap.put("brandCode",ConvertUtil.getString(failItem.get("brandCode")));
					tempMap.put("productRange",ConvertUtil.getString(failItem.get("productRange")));
					tempMap.put("productAppointed",ConvertUtil.getString(failItem.get("productAppointed")));
					tempMap.put("unitCode",ConvertUtil.getString(failItem.get("unitCode")));
					tempMap.put("barCode",ConvertUtil.getString(failItem.get("barCode")));
					tempMap.put("productName",ConvertUtil.getString(failItem.get("nameTotal")));
					tempMap.put("quantityOrAmount",ConvertUtil.getString(failItem.get("quantityOrAmount")));
					tempMap.put("compareCondition",ConvertUtil.getString(failItem.get("compareCondition")));
					tempMap.put("compareValue",ConvertUtil.getString(failItem.get("compareValue")));
					tempMap.put("errorMsg",ConvertUtil.getString(failItem.get("errorMsg")));
					String failJson = CherryUtil.map2Json(tempMap);
					Map<String, Object> itemMap = new HashMap<String, Object>();
					itemMap.put("searchCode", searchCode);
					itemMap.put("operateType", operateType);
					itemMap.put("filterType", filterType);
					itemMap.put("failJson", failJson);
					failListFinal.add(itemMap);
				}
			}
		}else{
			for(Map<String,Object> failItem:uploadFailList){
				//删除brandInfoId
				failItem.remove(CherryConstants.BRANDINFOID);
				String failJson = CherryUtil.map2Json(failItem);
				Map<String,Object> itemMap =new HashMap<String, Object>();
				itemMap.put("searchCode",searchCode);
				itemMap.put("filterType",filterType);
				itemMap.put("operateType",operateType);
				itemMap.put("failJson",failJson);
				failListFinal.add(itemMap);
			}
		}
		return failListFinal;
	}

	/**
	 * 获取失败导入柜台总数
	 * @param map
	 * @return
	 */
	public int getFailUploadCount(Map<String,Object> map){
		return prm88Ser.getFailUploadCount(map);
	}

	/**
	 * 获取导入失败柜台List
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> getFailUploadList(Map<String, Object> map) throws Exception {
		List<String> list = prm88Ser.getFailUploadList(map);
		List<Map<String,Object>> failList = new LinkedList<Map<String,Object>>();
		for(String counterItem :list){
			Map<String,Object> failMap = ConvertUtil.json2Map(counterItem);
			failList.add(failMap);
		}
		return failList;
	}

	/**
	 * 取得资源的值
	 * @param baseName 资源的文件名称（无语言无后缀）。取共通资源传值null或""
	 * @param language 语言
	 * @param key 资源的键
	 */
	public String getResourceValue(String baseName, String language, String key) {
		try{
			String sysName = "";
			//为空时，查询共通语言资源文件
			String path = "i18n/common/commonText";
			if(null != baseName && !"".equals(baseName)){
				if(baseName.equals("message")){
					path = "i18n/message/message";
				} else {
					sysName = baseName.substring(5, 7);
					//Linux下大小写敏感，传入资源的文件名称全是大写，截取的系统名也是大写
					//但是实际文件夹的名称是小写，这样就取不到目标文件
					//为了在兼容Linux，这里把截取后的系统名转成小写。
					sysName = sysName.toLowerCase();
					path = "i18n/"+sysName+"/"+baseName;
				}
			}
			return LocalizedTextUtil.findResourceBundle(path, new Locale(language.substring(0, 2),language.substring(3,5))).getString(key);
		}catch(Exception e){
			return key;
		}
	}
	public byte[] exportExcel(Map<String, Object> map) throws Exception{
		//获取所有失败数据
		List<String> list = prm88Ser.getFailUploadTotalList(map);
		//json转为map
		List<Map<String,Object>> failList = new LinkedList<Map<String,Object>>();
		for(String item :list){
			Map<String,Object> failMap = ConvertUtil.json2Map(item);
			failList.add(failMap);
		}
		String operateType = ConvertUtil.getString(map.get("operateType"));
		String filterType = ConvertUtil.getString(map.get("filterType"));
		BINOLMOCOM01_IF.ExcelParam ep = new BINOLMOCOM01_IF.ExcelParam();
		if (PromotionConstants.Fail_OperateType_1.equals(operateType)){//为柜台
			String[][] array = {
					{ "brandCode", "brandCodeForCounter", "15", "", "" },
					{ "counterCode", "counterCode", "20", "", "" },
					{ "counterName", "counterName", "20", "", "" },
					{"errorMsg","errorMsg", "20", "", "" }
			};
			ep.setArray(array);
			ep.setSheetLabel("sheetNameForCounter");
		}else if(PromotionConstants.Fail_OperateType_2.equals(operateType)){
			if (filterType.equals(PromotionConstants.FILTERTYPE_2)){
				String[][] array = {
						{ "brandCode", "brandCode", "15", "", "" },
						{ "unitCode", "unitCode", "20", "", "" },
						{ "barCode", "barCode", "20", "", "" },
						{ "productName", "productName", "20", "", "" },
						{"errorMsg","errorMsg", "20", "", "" }
				};
				ep.setArray(array);
				ep.setSheetLabel("sheetNameForProduct");
			}else {
				String[][] array = {
						{ "brandCode", "brandCode", "15", "", "" },
						{ "productRange", "productRange", "15", "", "" },
						{ "productAppointed", "productAppointed", "15", "", "" },
						{ "unitCode", "unitCode", "20", "", "" },
						{ "barCode", "barCode", "20", "", "" },
						{ "productName", "productName", "20", "", "" },
						{ "quantityOrAmount", "quantityOrAmount", "20", "", "" },
						{ "compareCondition", "compareCondition", "20", "", "" },
						{ "compareValue", "compareValue", "20", "", "" },
						{"errorMsg","errorMsg", "20", "", "" }
				};
				ep.setArray(array);
				ep.setSheetLabel("sheetNameForProduct");
			}
		}else if(PromotionConstants.Fail_OperateType_3.equals(operateType)){
			String[][] array = {
					{ "brandCode", "brandCode", "15", "", "" },
					{ "memberCode", "memberCode", "15", "", "" },
					{ "mobile", "mobile", "20", "", "" },
					{ "bpCode", "bpCode", "20", "", "" },
					{ "memberLevel", "memberLevel", "20", "", "" },
					{ "name", "name", "20", "", "" },
					{"errorMsg","errorMsg", "20", "", "" }
			};
			ep.setArray(array);
			ep.setSheetLabel("sheetNameForMember");
		}
		ep.setMap(map);
		ep.setBaseName("BINOLSSPRM88");
		ep.setDataList(failList);
		return binOLMOCOM01_BL.getExportExcel(ep);
	}

	public Map<String,Object> getMemberListInfo(Map<String,Object> map) throws Exception{
		Map<String,Object> retMap = new HashMap<String,Object>();
		int total = 0;
		int totalCount = 0;
		String filterType = ConvertUtil.getString(map.get("filterType"));
		String searchCode;
		String campMebInfo = "";
//		String brandCode = prm88Ser.getBrandCode(map);
		List<Map<String,Object>> memberList = new LinkedList<Map<String,Object>>();
		if (PromotionConstants.FILTERTYPE_1.equals(filterType)){
			searchCode = ConvertUtil.getString(map.get("searchCode"));
			if (!CherryChecker.isNullOrEmpty(searchCode)){
				// 取得searchLog信息
				Map<String, Object> searchLogMap = binolcpcom03IF.getMemberInfo(map);
				if(null!=searchLogMap){
					total = ConvertUtil.getInt(searchLogMap.get("recordCount"));
					if(total == 0){
						campMebInfo = ConvertUtil.getString(searchLogMap.get("conditionInfo"));
					}else {
						totalCount = total;
						 //会员信息list
						memberList = prm88Ser.getMemberList(map);
						setSearchExtInfo(memberList);
					}
				}
				if(total == 0){
					// 会员共通查询
					totalCount = searchMemList(memberList,map,campMebInfo);
				}
			}
		}else {
			totalCount = prm88Ser.getMemberListCount(map);
			memberList = prm88Ser.getMemberList(map);
			setSearchExtInfo(memberList);
		}
		retMap.put("memberList",memberList);
		retMap.put("totalCount",totalCount);
		return retMap;
	}

	/**
	 * 设置查询会员页面拓展信息
	 * @param memberList
	 * @throws Exception
     */
	private void setSearchExtInfo(List<Map<String,Object>> memberList){
		for (Map<String,Object> memberMap : memberList){
			if (!CherryChecker.isNullOrEmpty(memberMap.get("extInfo"))){
				Map<String,Object> extInfo = ConvertUtil.json2Map(ConvertUtil.getString(memberMap.get("extInfo")));
				if (!CherryChecker.isNullOrEmpty(extInfo.get("memberLevel"))){
					memberMap.put("memberLevel",extInfo.get("memberLevel"));
				}
				if (!CherryChecker.isNullOrEmpty(extInfo.get("bpCode"))){
					memberMap.put("bpCode",extInfo.get("bpCode"));
				}
			}
		}
	}

	private int searchMemList(List<Map<String,Object>> memberList,Map<String,Object> map,String campMebInfo){
		int total = 0;
		Map<String, Object> conMap = ConvertUtil.json2Map(campMebInfo);
		conMap.putAll(map);
		Map<String, Object> resMap = binOLCM33_BL.searchMemList(conMap);
		if(null != resMap){
			total = ConvertUtil.getInt(resMap.get("total"));
			//会员信息list
			memberList= (List<Map<String, Object>>)resMap.get("list");
			if(null!=memberList){
//				for(Map<String, Object> memMap : memberList){
//					String birthYear = ConvertUtil.getString(memMap.get(CampConstants.BIRTHYEAR));
//					String birthMonth = ConvertUtil.getString(memMap.get(CampConstants.BIRTHMONTH));
//					String birthDay = ConvertUtil.getString(memMap.get(CampConstants.BIRTHDAY));
//					memMap.put(CampConstants.BIRTHDAY, birthYear + birthMonth + birthDay);
//					// 对象类型【会员】
//					memMap.put(CampConstants.CUSTOMER_TYPE, CampConstants.CUSTOMER_TYPE_1);
//				}

			}
		}
		return total;
	}

	/**
	 * 初始化柜台黑名单
	 * @param map
     */
	public String handleCounterBlackInit(Map<String,Object> map) throws Exception{
		List<Map<String,Object>> placeBlackList = prm88Ser.getPlaceBlackList(map);
		if (!CherryChecker.isNullOrEmpty(placeBlackList)){
			for (Map<String,Object> placeBlackItem : placeBlackList){
				placeBlackItem.put("level",0);
				placeBlackItem.put("half",false);
			}
			return CherryUtil.obj2Json(placeBlackList);
		}else {
			return null;
		}
	}

	/**
	 * 获取execl导入白名单数量 BL
	 * @param serachCode
	 * @return
     */
	public int getExeclMemberCount(String serachCode){
		return prm88Ser.getExeclMemberCount(serachCode);
	}
}
