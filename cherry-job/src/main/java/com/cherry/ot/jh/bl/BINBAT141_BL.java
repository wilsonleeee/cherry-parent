/*	
 * @(#)BINBAT141_BL.java     1.0 @2016-3-17
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
package com.cherry.ot.jh.bl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.batcmbussiness.interfaces.BINBECM01_IF;
import com.cherry.cm.cmbussiness.bl.BINOLCM03_BL;
import com.cherry.cm.core.BatchLoggerDTO;
import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryBatchException;
import com.cherry.cm.core.CherryBatchLogger;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.CherryBatchUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.DateUtil;
import com.cherry.cp.common.CampConstants;
import com.cherry.ot.jh.service.BINBAT141_Service;
import com.cherry.ss.common.PromotionConstants;
import com.cherry.webserviceout.kingdee.product.service.BINBEKDPRO01_Service;
import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;
import com.jahwa.common.JahwaWebServiceProxy;
import com.jahwa.pos.crm.Dt_PmsCpCampTran_res;
import com.jahwa.pos.crm.Dt_PmsCpCampTran_resZDEPTITEM;
import com.jahwa.pos.crm.Dt_PmsCpCampTran_resZMAINITEM;
import com.jahwa.pos.crm.Dt_PmsCpCampTran_resZMEMBERITEM;
import com.jahwa.pos.crm.Dt_PmsCpCampTran_resZTIERITEM;

/**
 * 
 * SAP接口(WSDL)：活动导入BL
 * 
 * WebService请求数据，并导入新后台
 * 
 * @author jijw
 * 
 * @version 2016-3-17
 */
public class BINBAT141_BL {

	private static CherryBatchLogger logger = new CherryBatchLogger(
			BINBAT141_BL.class);

	@Resource
	private BINBEKDPRO01_Service binbekdpro01_Service;

	@Resource
	private BINBAT141_Service binBAT141_Service;
	
	@Resource
	private BINOLCM03_BL binOLCM03_BL;

	/** JOB执行相关共通 IF */
	@Resource(name = "binbecm01_IF")
	private BINBECM01_IF binbecm01_IF;


	/** BATCH处理标志 */
	private int flag = CherryBatchConstants.BATCH_SUCCESS;

	private Map<String, Object> comMap;

	private Map<String, Integer> basePropMap;

	private Map<String, Map<String, Object>> channelMap;

	private Map<String, String> counterMap;
	
	private Map<String, String> memLevelMap;
	
	private Map<String, List<String>> memLevelMap2;
	
	private Map<String, List<Map<String, Object>>> memberMap;

	/** 处理总条数 */
	private int totalCount = 0;
	/** 插入条数 */
	private int insertCount = 0;
	/** 更新条数 */
	private int updateCount = 0;
	/** 失败条数 */
	private int failCount = 0;

	/** 导入失败的itemCode */
	private List<String> faildItemList = new ArrayList<String>();

	/** 失败的主要原因，受字段长度限制，这里只要记录主要原因即可 */
	private String fReason = new String();
	private StringBuffer fReasonBuffer = new StringBuffer();

	private Dt_PmsCpCampTran_res getResult(String activityCode) throws Exception {
		Dt_PmsCpCampTran_res res = null;
		try {
			// === 调用WSDL产品接口 ===
			res = JahwaWebServiceProxy.getCampList(activityCode);
			if (null == res) {
				throw new Exception();
			}
			if(null != res.getZMAIN()){
				totalCount = res.getZMAIN().length;
			}
		} catch (Exception e) {
			fReason = "调用SAP Webservice活动接口[JahwaWebServiceProxy.getCampList()]失败。";
			flag = CherryBatchConstants.BATCH_ERROR;
			logger.outLog(fReason, CherryBatchConstants.LOGGER_ERROR);
			throw e;
		}
		return res;
	}

	/**
	 * batch处理
	 * 
	 * @param 无
	 * 
	 * @return Map
	 * @throws CherryBatchException
	 */
	public int tran_batchExecute(Map<String, Object> map)
			throws CherryBatchException, Exception {
		try {
			// === 初始化 ===
			comMap = getComMap(map);
			// 查询规则基础属性
			basePropMap = setBaseProp(map);
			// 获取门店信息
			counterMap = setCounterMap(map);
			// 获取渠道信息
			channelMap = setChannelMap(map);
			// 会员等级信息
			memLevelMap = setMemLevelMap(map);
					
			String activityCode = ConvertUtil.getString(map.get("activityCode"));
			Dt_PmsCpCampTran_res res = getResult(activityCode);
			if (null != res && totalCount > 0) {
				// 门店目标组分组
				Map<String, List<String>> departMap = getDepartMap(res);
				// 会员目标组分组
				memberMap = setMemberMap(res);
				memLevelMap2 = setMemLevelMap2(res);
				// 循环活动内容
				for (Dt_PmsCpCampTran_resZMAINITEM main : res.getZMAIN()) {
					if (!"X".equals(main.getZZAFLD00001W())) {// 非优惠券活动
						// 清除指定原始活动内容
						removeAct(map);
						// 获取活动信息MAP
						Map<String, Object> actMap = setActMap(main);
						// 获取门店目标组
						actMap.put("departList",
								departMap.get(main.getEXTERNAL_ID()));
						// 获取会员目标组
						actMap.put("memList",
								memberMap.get(main.getEXTERNAL_ID()));
						// 获取会员等级目标组
						actMap.put("memLevelList",
								memLevelMap2.get(main.getEXTERNAL_ID()));
						// 保存促销活动
						execAct(actMap);
					} else {// TODO 优惠券活动
						Map<String, Object> pmap = new HashMap<String, Object>();
						pmap.put("RuleCode", ConvertUtil.getString(main.getEXTERNAL_ID()));
						pmap.put("RuleName", ConvertUtil.getString(main.getTEXT1()));
						pmap.put("SendStartTime", ConvertUtil.getString(main.getPLANSTART()));
						pmap.put("SendEndTime", ConvertUtil.getString(main.getPLANFINISH()));
						pmap.put("Description", ConvertUtil.getString(main.getTDLINE_TXT()));
						pmap.put("Description", ConvertUtil.getString(main.getTDLINE_TXT()));
						pmap.put(CherryBatchConstants.BRANDINFOID, map.get(ConvertUtil.getString(CherryBatchConstants.BRANDINFOID)));
						pmap.put(CherryBatchConstants.ORGANIZATIONINFOID, map.get(ConvertUtil.getString(CherryBatchConstants.ORGANIZATIONINFOID)));
						pmap.put("Status", "0");
						pmap.put("createdBy", ConvertUtil.getString(map.get("employeeId")));
						pmap.put("createPGM", "BINBAT141");
						pmap.put("updatedBy", ConvertUtil.getString(map.get("employeeId")));
						pmap.put("updatePGM", "BINBAT141");
						savePromotionCouponRule(pmap);
					}
				}
			}else{
				if(!"".equals(activityCode)){
					throw new Exception("该促销活动CRM系统不存在：" + activityCode);
				}
			}
		} catch (Exception e) {
			fReason = e.getMessage();
			flag = CherryBatchConstants.BATCH_ERROR;
			throw e;
		} finally {
			// 程序数据处理运行结果
			outMessage();
			// 程序结束处理
			programEnd(map);
		}
		return flag;
	}

	private Map<String, List<String>> setMemLevelMap2(Dt_PmsCpCampTran_res res) {
		Map<String, List<String>> map = new HashMap<String, List<String>>();
		// 会员等级根据活动分组
		if (null != res.getZTIER() && res.getZTIER().length > 0) {
			for (Dt_PmsCpCampTran_resZTIERITEM lev : res.getZTIER()) {
				List<String> list = map.get(lev.getEXTERNAL_ID());
				if (null == list) {
					list = new ArrayList<String>();
					list.add(memLevelMap.get(lev.getTIER_LEVEL()));
					map.put(lev.getEXTERNAL_ID(), list);
				} else {
					list.add(memLevelMap.get(lev.getTIER_LEVEL()));
				}
			}
		}
		return map;
	}

	private Map<String, String> setMemLevelMap(Map<String, Object> map) throws Exception {
		Map<String, String> memLevelMap = new HashMap<String, String>();
		// 取得会员等级LIST
		List<Map<String, Object>> list = binBAT141_Service.getMemLevelList(map);
		if (null != list && !list.isEmpty()) {
			for (Map<String, Object> lev : list) {
				String code = ConvertUtil.getString(lev.get("code"));
				String id = ConvertUtil.getString(lev.get("id"));
				memLevelMap.put(code, id);
			}
		}
		if(memLevelMap.isEmpty()){
			throw new Exception("获取【会员等级】基础信息失败！");
		}
		return memLevelMap;
	}

	/**
	 * 清空原始活动内容
	 * @param map
	 * @throws Exception
	 */
	private void removeAct(Map<String, Object> map) throws Exception {
		String activityCode = ConvertUtil.getString(map.get("activityCode"));
		// 清除指定原始活动内容
		if(!"".equals(activityCode)){
			// 获取ID
			int ruleId = ConvertUtil.getInt(binBAT141_Service.getRuleId(map));
			if(ruleId == 0){
				logger.outLog("该促销活动PMS系统不存在：" + activityCode, CherryBatchConstants.LOGGER_WARNING);
//				throw new Exception("该促销活动PMS系统不存在：" + activityCode);
			}else{
				map.put("ruleID", ruleId);
				// 删除促销活动条件
				binBAT141_Service.delPrmActCondition(map);
				// 删除促销活动结果
				binBAT141_Service.delPrmActResult(map);
				// 删除促销活动规则
				binBAT141_Service.delPrmActivityRule(map);
				// 删除促销活动
				binBAT141_Service.delPrmActivity(map);
				// 删除促销规则分类表
				binBAT141_Service.delPrmActRule(map);
				// 删除促销规则分类表
				binBAT141_Service.delPrmActRuleCate(map);
			}
		}
	}

	private Map<String, Map<String, Object>> setChannelMap(
			Map<String, Object> map) throws Exception {
		Map<String, Map<String, Object>> channelMap = new HashMap<String, Map<String, Object>>();
		// 取得渠道LIST
		List<Map<String, Object>> list = binBAT141_Service.getChannelList(map);
		if (null != list && !list.isEmpty()) {
			for (Map<String, Object> item : list) {
				if (null != item.get("code") && !"".equals(item.get("code").toString())) {
					channelMap.put(item.get("code").toString(), item);
				}
			}
		}
		if(channelMap.isEmpty()){
			throw new Exception("获取【渠道】基础信息失败！");
		}
		return channelMap;
	}

	private Map<String, String> setCounterMap(Map<String, Object> map) throws Exception {
		Map<String, String> cntMap = new HashMap<String, String>();
		// 取得柜台LIST
		List<Map<String, String>> cntList = binBAT141_Service.getCntList(map);
		if (null != cntList && !cntList.isEmpty()) {
			for (Map<String, String> cnt : cntList) {
				if (null != cnt.get("code") && !"".equals(cnt.get("code"))) {
					cntMap.put(cnt.get("code"), cnt.get("name"));
				}
			}
		}
		if(cntMap.isEmpty()){
			throw new Exception("获取【柜台】基础信息失败！");
		}
		return cntMap;
	}

	/**
	 * 根据活动分组-门店目标组
	 * 
	 * @param res
	 * @return
	 */
	private Map<String, List<String>> getDepartMap(Dt_PmsCpCampTran_res res) {
		Map<String, List<String>> departMap = new HashMap<String, List<String>>();
		// 门店根据活动分组
		if (null != res.getZDEPT() && res.getZDEPT().length > 0) {
			for (Dt_PmsCpCampTran_resZDEPTITEM depart : res.getZDEPT()) {
				List<String> list = departMap.get(depart.getEXTERNAL_ID());
				if (null == list) {
					list = new ArrayList<String>();
					list.add(depart.getDEPTID());
					departMap.put(depart.getEXTERNAL_ID(), list);
				} else {
					list.add(depart.getDEPTID());
				}
			}
		}
		return departMap;
	}

	/**
	 * 根据活动分组-会员目标组
	 * 
	 * @param res
	 * @return
	 */
	private Map<String, List<Map<String, Object>>> setMemberMap(
			Dt_PmsCpCampTran_res res) {
		Map<String, List<Map<String, Object>>> memberMap = new HashMap<String, List<Map<String, Object>>>();
		// 门店根据活动分组
		if (null != res.getZMEMBER() && res.getZMEMBER().length > 0) {
			for (Dt_PmsCpCampTran_resZMEMBERITEM member : res.getZMEMBER()) {
				List<Map<String, Object>> list = memberMap.get(member
						.getEXTERNAL_ID());
				if (null == list) {
					list = new ArrayList<Map<String, Object>>();
					list.add(getMemMap(member));
					memberMap.put(member.getEXTERNAL_ID(), list);
				} else {
					list.add(getMemMap(member));
				}
			}
		}
		return memberMap;
	}
	private void savePromotionCouponRule(Map<String, Object> actMap){
		try {
			validMap(actMap);
			binBAT141_Service.savePromotionCouponRule_exec(actMap);
		} catch (Exception e) {
			failCount ++;
			flag = CherryBatchConstants.BATCH_WARNING;
			logger.outLog(e.getMessage(), CherryBatchConstants.LOGGER_WARNING);
		}
	}
	/**
	 * 单个活动导入处理
	 * 
	 * @param actMap
	 * @throws Exception
	 */
	private void execAct(Map<String, Object> actMap) throws Exception {
		try {
			// 字段校验
			validParams(actMap);
			// 插入促销活动表,并取得促销活动id
			int prmActId = binBAT141_Service.addPromotionActivity(actMap);
			actMap.put("bin_PromotionActivityID", prmActId);
			// 添加促销活动关联表
			int ruleID = binBAT141_Service.addPromotionActivityRule(actMap);
			actMap.put("bin_PromotionActivityRuleID", ruleID);
			// 插入促销活动规则条件明细表
			saveRuleCondition(actMap);
			// 规则处理【结果json，规则分类】
			exeRule(actMap);
			// 会员信息保存
			saveMemInfo(actMap);
			// 添加促销规则表
			binBAT141_Service.addActRuleInfo(actMap);
			binBAT141_Service.manualCommit();
		} catch (Exception e) {
			failCount ++;
			flag = CherryBatchConstants.BATCH_WARNING;
			// 事务回滚
			binBAT141_Service.manualRollback();
			// 业务日志-写入失败履历表
			Map<String,Object> falidMap = new HashMap<String, Object>();
			falidMap.putAll(comMap);
			falidMap.put("UnionIndex", actMap.get("activityCode"));
			falidMap.put("ErrorMsg", ",{\"" + e.getMessage() + "\"}");
			binbecm01_IF.mergeJobRunFaildHistory(falidMap);
			logger.outLog(e.getMessage(), CherryBatchConstants.LOGGER_WARNING);
		}
	}

	private void saveMemInfo(Map<String, Object> actMap) throws Exception{
		List<Map<String, Object>> memList = (List<Map<String, Object>>)actMap.get("memList");
		int orgId = ConvertUtil.getInt(comMap.get(CherryBatchConstants.ORGANIZATIONINFOID));
		int brandId = ConvertUtil.getInt(comMap.get(CherryBatchConstants.BRANDINFOID));
		Map<String, Object> params = new HashMap<String, Object>();
		params.putAll(comMap);
		if(null == memList || memList.isEmpty()){
			List<String> memLevelList = (List<String>)actMap.get("memLevelList");
			if(null != memLevelList && !memLevelList.isEmpty()){// 保存条件
				// 获取searchCode
				String searchCode = binOLCM03_BL.getTicketNumber(orgId, brandId, "", "EC");
				params.put(CampConstants.SEARCH_CODE,searchCode);
				// 数据来源（导入）
				params.put(CampConstants.FROM_TYPE, CampConstants.FROM_TYPE_2);
				// 对象记录类型-条件
				params.put(CampConstants.RECORD_TYPE,CampConstants.RECORD_TYPE_1);
				// 条件json
				params.put("conditionInfo",getMemJson(memLevelList));
				params.put("customerType","1");
				// 保存活动对象信息
				binBAT141_Service.addMemSearchLog(params);
				
				actMap.put(CampConstants.SEARCH_CODE, searchCode);
				actMap.put("memberType", "1");
			}
		}else{// 保存结果
			// 获取searchCode
			String searchCode = binOLCM03_BL.getTicketNumber(orgId, brandId, "", "EC");
			params.put(CampConstants.SEARCH_CODE,searchCode);
			// 数据来源（导入）
			params.put(CampConstants.FROM_TYPE, CampConstants.FROM_TYPE_2);
			// 对象记录类型-结果
			params.put(CampConstants.RECORD_TYPE,CampConstants.RECORD_TYPE_2);
			params.put("recordCount", memList.size());
			params.put("customerType","1");
			// 保存活动对象信息
			binBAT141_Service.addMemSearchLog(params);
			for(Map<String, Object> mem :memList){
				mem.putAll(comMap);
				mem.put(CampConstants.SEARCH_CODE,searchCode);
				mem.put("customerType","1");
			}
			// 批量保存活动对象信息
			binBAT141_Service.addCustomerInfo(memList);
			actMap.put(CampConstants.SEARCH_CODE, searchCode);
			actMap.put("memberType", "3");
		}
		
	}

	private String getMemJson(List<String> memLevelList) throws JSONException {
		Map<String, Object> map = new HashMap<String, Object>();
		// 所属组织
		map.put(CherryBatchConstants.ORGANIZATIONINFOID,
				comMap.get(CherryBatchConstants.ORGANIZATIONINFOID));
				// 品牌ID
		map.put(CherryBatchConstants.BRANDINFOID,
				comMap.get(CherryBatchConstants.BRANDINFOID));
		map.put(CherryBatchConstants.BRAND_CODE, 
				comMap.get(CherryBatchConstants.BRAND_CODE));
		if(memLevelList.size() == 1){
			map.put("memLevel", memLevelList.get(0));
		}else{
			map.put("memLevel", memLevelList);
		}
		return JSONUtil.serialize(map);
	}
	/**
	 * 参数校验
	 * 
	 * @param actMap
	 * @throws Exception
	 */
	private void validMap(Map<String, Object> pmap) throws Exception {
		// TODO 字段校验
		String RuleCode = ConvertUtil.getString(pmap.get("RuleCode"));
		String RuleName = ConvertUtil.getString(pmap.get("RuleName"));
		String SendStartTime = ConvertUtil.getString(pmap.get("SendStartTime"));
		String SendEndTime = ConvertUtil.getString(pmap.get("SendEndTime"));
		if("".equals(RuleCode)){
			throw new Exception("活动ID不能为空：" + RuleCode);
		}
		if("".equals(RuleName)){
			throw new Exception("活动名称不能为空：" + RuleName);
		}
		if(!DateUtil.checkDate(SendStartTime)){
			throw new Exception("活动计划开始日期错误：" + SendStartTime);
		}
		if(!DateUtil.checkDate(SendEndTime)){
			throw new Exception("活动计划结束日期错误：" + SendEndTime);
		}
	}
	/**
	 * 校验活动信息
	 * 
	 * @param actMap
	 * @throws Exception
	 */
	private void validParams(Map<String, Object> actMap) throws Exception {
		// TODO 字段校验
		String startDate = ConvertUtil.getString(actMap.get("startDate"));
		String endDate = ConvertUtil.getString(actMap.get("endDate"));
		if(!DateUtil.checkDate(startDate, DateUtil.DATE_PATTERN)){
			throw new Exception("活动开始日期格式不正确：" + startDate);
		}
		if(!DateUtil.checkDate(endDate, DateUtil.DATE_PATTERN)){
			throw new Exception("活动结束日期格式不正确：" + endDate);
		}
	}

	/**
	 * 规则处理【规则时间】
	 * 
	 * @param map
	 * @throws Exception
	 */
	private void exeRule(Map<String, Object> map) throws Exception {
		String startDate = ConvertUtil.getString(map.get("startDate"));
		String endDate = ConvertUtil.getString(map.get("endDate"));
		String startTime = ConvertUtil.getString(map.get("startTime"));
		String endTime = ConvertUtil.getString(map.get("endTime"));
		List<Map<String, String>> timeList = new ArrayList<Map<String, String>>();
		Map<String, String> timeMap = new HashMap<String, String>();
		timeMap.put("startDate", startDate);
		timeMap.put("endDate", endDate);
		timeMap.put("startTime", startTime);
		timeMap.put("endTime", endTime);
		timeList.add(timeMap);
		map.put("timeJson", JSONUtil.serialize(timeList));
		// 规则条件
		map.put("ruleCondJson", "{}");
		// 规则结果
		map.put("ruleResultJson", "{}");
		// 规则类型
		map.put("ruleType", "2");
		map.put("startTime", startDate + " " + startTime);
		map.put("endTime", endDate + " " + endTime);
	}

	private Map<String, Object> getNode(String id, String name) {
		Map<String, Object> node = new HashMap<String, Object>();
		node.put("id", id);
		node.put("name", name);
		node.put("half", false);
		node.put("level", 0);
		return node;
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
		List<Map<String, Object>> conList = new ArrayList<Map<String, Object>>();
		// 活动地点List
		List<Map<String, Object>> placeList = new ArrayList<Map<String, Object>>();
		int ruleId = ConvertUtil.getInt(map.get("bin_PromotionActivityRuleID"));
		String valA = ConvertUtil.getString(map.get("startDate")) + " "
				+ ConvertUtil.getString(map.get("startTime"));
		String valB = ConvertUtil.getString(map.get("endDate")) + " "
				+ ConvertUtil.getString(map.get("endTime"));
		Map<String, Object> conMap = setConMap(
				basePropMap.get(PromotionConstants.BASE_PROP_TIME), null,
				ruleId, valA, valB);
		conList.add(conMap);
		// 门店LIST
		List<String> departList = (List<String>) map.get("departList");
		if (null == departList) {
			String channel = ConvertUtil.getString(map.get("channel"));
			// 全部渠道=全部柜台
			if ("299".equals(channel)) {
				Map<String, Object> conMap2 = setConMap(
						basePropMap.get(PromotionConstants.BASE_PROP_CITY),
						PromotionConstants.LOTION_TYPE_ALL_COUNTER, ruleId,
						"all", null);
				conList.add(conMap2);
				map.put("locationType",
						PromotionConstants.LOTION_TYPE_ALL_COUNTER);
				placeList.add(getNode("all", "全部柜台"));
			} else if (!"".equals(channel)) {// 指定渠道
				if ("203".equals(channel)) {// 百货全部 = 百货自营+百货加盟
					Map<String, Object> c2 = channelMap.get("201");
					Map<String, Object> c3 = channelMap.get("202");
					String id2 = ConvertUtil.getString(c2.get("id"));
					String id3 = ConvertUtil.getString(c3.get("id"));
					String name2 = ConvertUtil.getString(c2.get("name"));
					String name3 = ConvertUtil.getString(c3.get("name"));
					// 百货自营
					Map<String, Object> conMap2 = setConMap(
							basePropMap
									.get(PromotionConstants.BASE_PROP_CHANNEL),
							PromotionConstants.LOTION_TYPE_CHANNELS, ruleId, id2, null);
					// 百货加盟
					Map<String, Object> conMap3 = setConMap(
							basePropMap
									.get(PromotionConstants.BASE_PROP_CHANNEL),
							PromotionConstants.LOTION_TYPE_CHANNELS, ruleId, id3, null);
					conList.add(conMap2);
					conList.add(conMap3);
					placeList.add(getNode(id2, name2));
					placeList.add(getNode(id3, name3));
				} else {
					Map<String, Object> c2 = channelMap.get(channel);
					String id2 = ConvertUtil.getString(c2.get("id"));
					String name2 = ConvertUtil.getString(c2.get("name"));
					Map<String, Object> conMap2 = setConMap(
							basePropMap
									.get(PromotionConstants.BASE_PROP_CHANNEL),
							PromotionConstants.LOTION_TYPE_CHANNELS, ruleId, id2, null);
					conList.add(conMap2);
					placeList.add(getNode(id2, name2));
				}
				map.put("locationType", PromotionConstants.LOTION_TYPE_CHANNELS);
			}
		} else {
			for (String departCode : departList) {
				Map<String, Object> conMap2 = setConMap(
						basePropMap.get(PromotionConstants.BASE_PROP_COUNTER),
						PromotionConstants.LOTION_TYPE_IMPORT_COUNTER, ruleId,
						departCode, null);
				conList.add(conMap2);
				placeList.add(getNode(departCode, counterMap.get(departCode)));
			}
			map.put("locationType",
					PromotionConstants.LOTION_TYPE_IMPORT_COUNTER);
		}
		// 活动地点
		map.put("placeJson", JSONUtil.serialize(placeList));
		// 批量插入活动条件表
		if (!conList.isEmpty()) {
			binBAT141_Service.addPromotionRuleCondition(conList);
		}
	}

	/**
	 * 设置条件MAP
	 * 
	 * @param basePropId
	 * @param locationType
	 * @param ruleId
	 * @param valA
	 * @param valB
	 * @return
	 */
	private Map<String, Object> setConMap(int basePropId, String locationType,
			int ruleId, String valA, String valB) {
		Map<String, Object> conMap = new HashMap<String, Object>();
		conMap.put("basePropId", basePropId);
		conMap.put("locationType", locationType);
		conMap.put("conditionGrpId", 1);
		conMap.put("basePropValue", valA);
		conMap.put("basePropValue2", valB);
		conMap.put("bin_PromotionActivityRuleID", ruleId);
		return conMap;
	}
	
	
	/**
	 * 获取会员信息MAP
	 * 
	 * @param basePropId
	 * @param locationType
	 * @param ruleId
	 * @param valA
	 * @param valB
	 * @return
	 */
	private Map<String, Object> getMemMap(Dt_PmsCpCampTran_resZMEMBERITEM member) {
		Map<String, Object> mem = new HashMap<String, Object>();
		mem.put("memName", member.getNAME());
		mem.put("memCode", member.getBPEXT());
		mem.put("mobilePhone", member.getMOB_NUMBER());
		mem.put("counterCode",member.getDEPTID());
		return mem;
	}


	/**
	 * 设置活动信息MAP
	 * 
	 * @param main
	 * @return
	 */
	private Map<String, Object> setActMap(Dt_PmsCpCampTran_resZMAINITEM main) {
		Map<String, Object> actMap = new HashMap<String, Object>();
		// 活动码
		actMap.put("activityCode", main.getEXTERNAL_ID());
		// 活动名
		actMap.put("prmActiveName", main.getTEXT1());
		// 活动描述
		actMap.put("descriptionDtl", main.getTDLINE_TXT());
		// 活动开始日期
		actMap.put("startDate", main.getPLANSTART());
		// 活动结束日期
		actMap.put("endDate", main.getPLANFINISH());
		// 活动开始时分秒
		actMap.put("startTime", "00:00:00");
		// 活动结束时分秒
		actMap.put("endTime", "23:59:59");
		// 活动类型
		actMap.put("activityType", main.getCAMP_TYPE());
//		main.setZZAFLD00001E("203");
		// 活动渠道性质
		actMap.put("channel", main.getZZAFLD00001E());
		if (!"X".equals(main.getZZAFLD000029())) {// 不能使用优惠券
			actMap.put("useCoupon", "0");
		} else {
			actMap.put("useCoupon", "1");
		}
		// 活动组ID
		actMap.put("prmActGrp", 0);
		// 终端可否修改活动
		actMap.put("mainModify", 0);
		// 活动设置者
		actMap.put("userID", 0);
		// 终端可否修改活动
		actMap.put("templateFlag", 0);
		// 审核状态
		actMap.put("status", 0);
		actMap.putAll(comMap);
		return actMap;
	}

	/**
	 * 程序结束时，处理Job共通(更新Job控制表 、插入Job运行履历表)
	 * 
	 * @param paraMap
	 * @throws Exception
	 */
	private void programEnd(Map<String, Object> paraMap) throws Exception {
		paraMap.putAll(comMap);

		// 程序结束时，插入Job运行履历表
		paraMap.put("flag", flag);
		paraMap.put("TargetDataCNT", totalCount);
		paraMap.put("SCNT", totalCount - failCount);
		paraMap.put("FCNT", failCount);
		paraMap.put("FReason", fReasonBuffer.append(fReason).toString());

		binbecm01_IF.insertJobRunHistory(paraMap);
	}

	/**
	 * 共通Map
	 * 
	 * @param map
	 * @return
	 */
	private Map<String, Object> getComMap(Map<String, Object> map) {
		Map<String, Object> baseMap = new HashMap<String, Object>();
		// 程序【开始运行时间】
		String runStartTime = binbekdpro01_Service.getSYSDateTime();
		// 更新程序名
		baseMap.put(CherryBatchConstants.UPDATEPGM, "BINBAT141");
		// 作成程序名
		baseMap.put(CherryBatchConstants.CREATEPGM, "BINBAT141");
		// 作成者
		baseMap.put(CherryBatchConstants.CREATEDBY,
				CherryBatchConstants.UPDATE_NAME);
		// 更新者
		baseMap.put(CherryBatchConstants.UPDATEDBY,
				CherryBatchConstants.UPDATE_NAME);
		// 作成日时
		baseMap.put(CherryConstants.CREATE_TIME, runStartTime);
		// 更新日时
		baseMap.put(CherryBatchConstants.UPDATE_TIME, runStartTime);
		// 所属组织
		baseMap.put(CherryBatchConstants.ORGANIZATIONINFOID,
				map.get(CherryBatchConstants.ORGANIZATIONINFOID));
		// 品牌ID
		baseMap.put(CherryBatchConstants.BRANDINFOID,
				map.get(CherryBatchConstants.BRANDINFOID));
		baseMap.put(CherryBatchConstants.BRAND_CODE, 
				map.get(CherryBatchConstants.BRAND_CODE));
		baseMap.put("JobCode", "BAT141");
		// 作成日时
		baseMap.put("RunStartTime", runStartTime);
		return baseMap;
	}

	/**
	 * 输出处理结果信息
	 * 
	 * @throws CherryBatchException
	 */
	private void outMessage() throws CherryBatchException {
		// 总件数
		BatchLoggerDTO batchLoggerDTO1 = new BatchLoggerDTO();
		batchLoggerDTO1.setCode("IIF00001");
		batchLoggerDTO1.setLevel(CherryBatchConstants.LOGGER_INFO);
		batchLoggerDTO1.addParam(String.valueOf(totalCount));
		// 成功总件数
		BatchLoggerDTO batchLoggerDTO2 = new BatchLoggerDTO();
		batchLoggerDTO2.setCode("IIF00002");
		batchLoggerDTO2.setLevel(CherryBatchConstants.LOGGER_INFO);
		batchLoggerDTO2.addParam(String.valueOf(totalCount - failCount));
		// 插入件数
		BatchLoggerDTO batchLoggerDTO3 = new BatchLoggerDTO();
		batchLoggerDTO3.setCode("IIF00003");
		batchLoggerDTO3.setLevel(CherryBatchConstants.LOGGER_INFO);
		batchLoggerDTO3.addParam(String.valueOf(insertCount));
		// 更新件数
		BatchLoggerDTO batchLoggerDTO4 = new BatchLoggerDTO();
		batchLoggerDTO4.setCode("IIF00004");
		batchLoggerDTO4.setLevel(CherryBatchConstants.LOGGER_INFO);
		batchLoggerDTO4.addParam(String.valueOf(updateCount));
		// 失败件数
		BatchLoggerDTO batchLoggerDTO5 = new BatchLoggerDTO();
		batchLoggerDTO5.setCode("IIF00005");
		batchLoggerDTO5.setLevel(CherryBatchConstants.LOGGER_INFO);
		batchLoggerDTO5.addParam(String.valueOf(failCount));
		// 处理总件数
		logger.BatchLogger(batchLoggerDTO1);
		// 成功总件数
		logger.BatchLogger(batchLoggerDTO2);
		// 插入件数
		logger.BatchLogger(batchLoggerDTO3);
		// 更新件数
		logger.BatchLogger(batchLoggerDTO4);
		// 失败件数
		logger.BatchLogger(batchLoggerDTO5);

		// 失败ItemCode集合
		if (!CherryBatchUtil.isBlankList(faildItemList)) {
			BatchLoggerDTO batchLoggerDTO6 = new BatchLoggerDTO();
			batchLoggerDTO6.setCode("EOT00021");
			batchLoggerDTO6.setLevel(CherryBatchConstants.LOGGER_INFO);
			batchLoggerDTO6.addParam(faildItemList.toString());
			logger.BatchLogger(batchLoggerDTO6);
			fReason = "促销活动导入部分数据处理失败，具体见log日志";
		}
	}

	/**
	 * 设定促销活动基础属性值
	 * 
	 * @param map
	 */
	private Map<String, Integer> setBaseProp(Map<String, Object> map) {
		Map<String, Integer> basePropKeyMap = new HashMap<String, Integer>();
		List<Map<String, Object>> basePropList = binBAT141_Service
				.getPrmBasePropInfoList(map);
		for (int i = 0; i < basePropList.size(); i++) {
			Map<String, Object> basePropMap = basePropList.get(i);
			String propertyName = (String) basePropMap.get("propertyName");
			int basePropKeyId = ConvertUtil.getInt(basePropMap
					.get("bin_PromotionBasePropID"));
			// 基础规则属性 -- 时间
			if (propertyName.equals(PromotionConstants.BASE_PROP_TIME)) {
				basePropKeyMap.put(PromotionConstants.BASE_PROP_TIME,
						basePropKeyId);
			}
			// 基础规则属性 -- 区域市
			if (propertyName.equals(PromotionConstants.BASE_PROP_CITY)) {
				basePropKeyMap.put(PromotionConstants.BASE_PROP_CITY,
						basePropKeyId);
			}
			// 基础规则属性 -- 渠道
			if (propertyName.equals(PromotionConstants.BASE_PROP_CHANNEL)) {
				basePropKeyMap.put(PromotionConstants.BASE_PROP_CHANNEL,
						basePropKeyId);
			}
			// 基础规则属性 -- 柜台
			if (propertyName.equals(PromotionConstants.BASE_PROP_COUNTER)) {
				basePropKeyMap.put(PromotionConstants.BASE_PROP_COUNTER,
						basePropKeyId);
			}
			// 基础规则属性 -- 系统
			if (propertyName.equals(PromotionConstants.BASEPROP_FACTION)) {
				basePropKeyMap.put(PromotionConstants.BASEPROP_FACTION,
						basePropKeyId);
			}
		}
		return basePropKeyMap;
	}
}
