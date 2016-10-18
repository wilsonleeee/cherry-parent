/*
 * @(#)BINOLCTTPL01_Action.java     1.0 2012/11/06
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import com.cherry.cm.cmbussiness.service.BINOLCM32_Service;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;

/**
 * 沟通模板一览Action
 * 
 * @author ZhangGS
 * @version 1.0 2012.11.06
 */
public class BINOLCM32_BL {
	
	@Resource
	private BINOLCM32_Service binOLCM32_Service;
	
	@Resource
	private BINOLCM33_BL binOLCM33_BL;
	/**
	 * 取得活动List
	 * 
	 * @param Map
	 *			查询条件
	 * @return List
	 *			活动List
	 */
	@SuppressWarnings("rawtypes")
	public List getActvityList(Map<String, Object> map) {
		return binOLCM32_Service.getActivityList(map);
	}
	
	/**
	 * 取得活动名称
	 * 
	 * @param Map
	 *			查询条件
	 * @return List
	 *			活动List
	 */
	public Map<String, Object> getActivityInfo(Map<String, Object> map) {
		return binOLCM32_Service.getActivityInfo(map);
	}
	
	/**
	 * 根据活动编号取得沟通计划信息
	 * 
	 * @param Map
	 *			查询条件
	 * @return String
	 *			沟通对象集合名称
	 */
	public Map<String, Object> getPlanInfoByCampaign(Map<String, Object> map) {
		return binOLCM32_Service.getPlanInfoByCampaign(map);
	}
	
	/**
	 * 取得沟通对象集合名称
	 * 
	 * @param Map
	 *			查询条件
	 * @return String
	 *			沟通对象集合信息
	 */
	public Map<String, Object> getObjRecordInfo(Map<String, Object> map) {
		return binOLCM32_Service.getObjRecordInfo(map);
	}
	
	/**
	 * 根据活动ID或Code获取活动的沟通对象搜索集合List
	 * 
	 * @param Map
	 *			查询条件
	 * @return List
	 *			沟通对象List
	 */
	@SuppressWarnings("rawtypes")
	public List getCommObjListByCampaign(Map<String, Object> map) {
		return binOLCM32_Service.getCommObjListByCampaign(map);
	}
	
	/**
	 * 获取沟通模板List
	 * 
	 * @param map
	 * @return 沟通模板List
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getMsgTemplateDialogList(Map<String, Object> map) throws Exception{
		// 获取沟通模板List
		List<Map<String, Object>> templateList = binOLCM32_Service.getMsgTemplateList(map);
		return templateList;
	}
	
	/**
	 * 获取沟通模板数量
	 * 
	 * @param map
	 * @return 沟通模板数量
	 */
	public int getMsgTemplateDialogCount(Map<String, Object> map){
		// 获取沟通模板数量
		return binOLCM32_Service.getMsgTemplateCount(map);
	}
	
	// 比较第一个时间是否在第二个时间之前
	public boolean dateBefore(String value1, String value2, String pattern){
		try{
			if(null==value1 || "".equals(value1)){
				return false;
			}else if(null==value2 || "".equals(value2)){
				return true;
			}else{	
				SimpleDateFormat sdf = new SimpleDateFormat(pattern);
				Date date1 = sdf.parse(value1);
				Date date2 = sdf.parse(value2);
				int result = date1.compareTo(date2);
				if(result <= 0){
					return true;
				}else{
					return false;
				}
			}
		}catch(Exception ex){
			return false;
		}
	}
	
	/**
	 * 根据秒钟添加日期
	 * @param date
	 * @param formatType
	 * @return
	 */
	public String addDateSecond (String dateTime,int second){
		try{
			SimpleDateFormat spf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date = spf.parse(dateTime);
			Calendar cal = Calendar.getInstance(); 
			cal.setTime(date); 
			cal.add(Calendar.SECOND, second);
			String strDate = spf.format(cal.getTime());
			return strDate;
		}catch(Exception ex){
			return "";
		}
	}
	
	@SuppressWarnings("unchecked")
	public Map<String, Object> getSearchCustomerInfo(Map<String, Object> map) throws Exception{
		String recordType = ConvertUtil.getString(map.get("recordType"));
		String customerType = ConvertUtil.getString(map.get("customerType"));
		String conditionInfo = ConvertUtil.getString(map.get("conditionInfo"));
		String referDate = CherryUtil.getSysDateTime(CherryConstants.DATE_PATTERN);
		Map<String, Object> conditionMap = new HashMap<String, Object>();
		conditionMap.putAll(map);
		conditionMap.remove("conditionInfo");
		// 判断搜索记录类型
		if("1".equals(recordType)){
			// 搜索记录类型为搜索条件时
			if("1".equals(customerType)){
				// 搜索记录对应的客户类型为会员时
				List<String> fieldList = new ArrayList<String>();
				fieldList.add("memId");
				fieldList.add("memCode");
				fieldList.add("memName");
				fieldList.add("gender");
				fieldList.add("mobilePhone");
				fieldList.add("email");
				fieldList.add("telephone");
				fieldList.add("totalPoint");
				fieldList.add("changablePoint");
				fieldList.add("counterCode");
				fieldList.add("counterName");
				fieldList.add("receiveMsgFlg");
				Map<String, Object> argMap = new HashMap<String, Object>();
				if(!"ALL".equals(conditionInfo) && !"ALL_MEMBER".equals(conditionInfo)){
					if(conditionInfo!=null && !"".equals(conditionInfo)){
						argMap = CherryUtil.json2Map(conditionInfo);
					}
				}
				argMap.putAll(conditionMap);
				argMap.put("referDate", referDate);
				argMap.put("selectors", fieldList);
				argMap.remove("searchName");
				Map<String, Object> resultMap = binOLCM33_BL.searchMemList(argMap);
				if(resultMap != null && !resultMap.isEmpty()){
					resultMap.put("customerType", customerType);
				}
				return resultMap;
			}else if("2".equals(customerType)){
				// 搜索记录对应的客户类型为非会员时（暂不支持）
				return null;
			}else if("3".equals(customerType)){
				// 搜索记录对应的客户类型为员工时（暂不支持）
				return null;
			}else if("4".equals(customerType)){
				// 搜索记录对应的客户类型为不限时（暂不支持）
				return null;
			}else{
				return null;
			}
		}else{
			// 搜索记录类型为搜索结果时
			if("1".equals(customerType)){
				// 搜索记录对应的客户类型为会员时
				Map<String, Object> resultMap = new HashMap<String, Object>();
				conditionMap.put("searchCode", conditionMap.get("recordCode"));
				List<Map<String, Object>> resultList = binOLCM32_Service.getSearchResultList(conditionMap);
				int resultCount = binOLCM32_Service.getSearchResultCount(conditionMap);
				resultMap.put("customerType", customerType);
				resultMap.put("total",resultCount);
				if(resultCount>0){
					resultMap.put("list",resultList);
				}
				return resultMap;
			}else if("2".equals(customerType)){
				// 搜索记录对应的客户类型为非会员时
				Map<String, Object> resultMap = new HashMap<String, Object>();
				conditionMap.put("searchCode", conditionMap.get("recordCode"));
				List<Map<String, Object>> resultList = binOLCM32_Service.getNoMemberSearchResultList(conditionMap);
				int resultCount = binOLCM32_Service.getNoMemberSearchResultCount(conditionMap);
				resultMap.put("total",resultCount);
				if(resultCount>0){
					resultMap.put("list",resultList);
				}
				return resultMap;
			}else if("3".equals(customerType)){
				// 搜索记录对应的客户类型为员工时（暂不支持）
				return null;
			}else if("4".equals(customerType)){
				// 搜索记录对应的客户类型为不限时
				Map<String, Object> resultMap = new HashMap<String, Object>();
				conditionMap.put("searchCode", conditionMap.get("recordCode"));
				List<Map<String, Object>> resultList = binOLCM32_Service.getSearchResultList(conditionMap);
				int resultCount = binOLCM32_Service.getSearchResultCount(conditionMap);
				resultMap.put("customerType", customerType);
				resultMap.put("total",resultCount);
				if(resultCount>0){
					resultMap.put("list",resultList);
				}
				return resultMap;
			}else{
				return null;
			}
		}
	}
	
	/**
	 * 获取沟通模版变量List
	 * @param queryType 查询类型 1：查询基础变量  2：按照用途查询（若无此参数根据是否有templateUse参数判断类型，若无则为按类型查询）
	 * @param templateUse 模版用途
	 * @return
	 */
	public List<Map<String, Object>> getVariableList(Map<String, Object> map) {
		//查询类型
		if(CherryChecker.isNullOrEmpty(map.get("queryType"), true)){
			if(CherryChecker.isNullOrEmpty(map.get("templateUse"), true)){
				map.put("queryType", "1");
			}else{
				map.put("queryType", "2");
			}
		}
		List<Map<String,Object>> variableList = binOLCM32_Service.getVariableList(map);
		if(variableList != null){
			for(Map<String, Object> fm : variableList){
				String commentsCut = ConvertUtil.getString(fm.get("comments"));
				if(!CherryChecker.isNullOrEmpty(commentsCut, true) && commentsCut.length() > 20){
					commentsCut = commentsCut.substring(0, 18) + "...";
				}
				fm.put("commentsCut", commentsCut);
			}
		}
		return variableList;
	}

	/**
	 * 获取单个沟通模版变量
	 * @param map
	 * @return
	 */
	public Map<String, Object> getVariable(Map<String, Object> map) {
		return binOLCM32_Service.getVariable(map);
	}
	
	/**
	 * 获取沟通模版内容非法字符List
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getIllegalCharList(Map<String, Object> map) {
		return binOLCM32_Service.getIllegalCharList(map);
	}
	
	/**
	 * 根据沟通阶段标识检查沟通是否存在
	 * @param map
	 * @return 沟通编号
	 */
	public String getCommunicationByPhaseNum(Map<String, Object> map) {
		return binOLCM32_Service.getCommunicationByPhaseNum(map);
	}
}
