/*	
 * @(#)BINOLPTRPS27_BL.java     1.0.0 2013/08/08		
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
package com.cherry.pt.rps.bl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;

import com.cherry.cm.activemq.bl.BINOLMQCOM01_BL;
import com.cherry.cm.activemq.dto.MQInfoDTO;
import com.cherry.cm.cmbussiness.bl.BINOLCM03_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM37_BL;
import com.cherry.cm.cmbussiness.interfaces.BINOLCM37_IF;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CodeTable;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.mq.mes.common.MessageConstants;
import com.cherry.pt.rps.interfaces.BINOLPTRPS27_IF;
import com.cherry.pt.rps.service.BINOLPTRPS27_Service;

/**
 * 进销存统计查询BL
 * 
 * @author zhangle
 * @version 1.0.0 2013.08.08
 * 
 */
public class BINOLPTRPS27_BL implements BINOLPTRPS27_IF ,BINOLCM37_IF{

	@Resource
	private BINOLPTRPS27_Service binOLPTRPS27_Service;
	@Resource
	private BINOLCM37_BL binOLCM37_BL;
	@Resource
	private CodeTable codeTable;
	@Resource
	private BINOLMQCOM01_BL binOLMQCOM01_BL;
	@Resource
	private BINOLCM03_BL binOLCM03_BL;
	
	/**
	 * 获得统计的总数
	 */
	@Override
	public int getCount(Map<String, Object> map) {

		return binOLPTRPS27_Service.getInventoryOperationStatisticCount(map);
	}

	/**
	 * 获得统计数据List
	 */
	@Override
	public List<Map<String, Object>> getList(Map<String, Object> map) {
		return binOLPTRPS27_Service.getInventoryOperationStatisticList(map);
	}
	
	/**
	 * 设置部门参数
	 */
	@Override
	public void tran_setDepartParameter(Map<String, Object> map) throws Exception{
		//设置行参数
		map.put("parameterType", "DP");
		binOLPTRPS27_Service.delParameter(map);	
		String dpParameter = ConvertUtil.getString(map.get("dpParameter"));
		if(!CherryChecker.isNullOrEmpty(dpParameter, true)){
			String[] dpParameters = dpParameter.split(",");
			for(String parameterData : dpParameters){
				map.put("parameterData", parameterData);
				int inventoryOperationParamId = 0;
				inventoryOperationParamId = binOLPTRPS27_Service.insertParameter(map);
				if(inventoryOperationParamId == 0){
					throw new Exception("ECM00089");
				}
			}
		}
		//设置列参数
		map.put("parameterType", "DT");
		binOLPTRPS27_Service.delParameter(map);	
		String dtParameter = ConvertUtil.getString(map.get("dtParameter"));
		if(!CherryChecker.isNullOrEmpty(dtParameter, true)){
			String[] dtParameters = dtParameter.split(",");
			for(String parameterData : dtParameters){
				map.put("parameterData", parameterData);
				int inventoryOperationParamId = 0;
				inventoryOperationParamId = binOLPTRPS27_Service.insertParameter(map);
				if(inventoryOperationParamId == 0){
					throw new Exception("ECM00089");
				}
			}
		}
	}
	
	/**
	 * 获得部门统计参数
	 */
	@Override
	public List<Map<String, Object>> getDepartParameter(Map<String, Object> map){
		return binOLPTRPS27_Service.getDepartParameter(map);
	}

	/**
	 * 获得部门类型模式部门树List
	 */
	@Override
	public List<Map<String, Object>> getDepartList(Map<String, Object> map) {
		List<Map<String, Object>> list = binOLPTRPS27_Service.getDepartList(map);
		for(Map<String, Object> mp : list){
			mp.put("departTypeName", codeTable.getVal("1000", mp.get("departType")));
			if(mp.get("departTypeName")==null){
				String language = ConvertUtil.getString(map.get("language"));
				if(CherryChecker.isNullOrEmpty(language, true)){
					language = "zh_CN";
				}
				mp.put("departTypeName",  binOLCM37_BL.getResourceValue("BINOLPTRPS27", language, "PTRPS27_unknowDepartType"));
			}
		}
		List<Map<String, Object>> resultTreeList =new ArrayList<Map<String,Object>>();
		List<String[]> keysList = new ArrayList<String[]>();
		String[] keys1 = { "departType","departTypeName" };
		String[] keys2 = { "departId","departCodeName" };
		keysList.add(keys1);
		keysList.add(keys2);
		ConvertUtil.jsTreeDataDeepList(list, resultTreeList, keysList, 0);
		return resultTreeList;
	}
	
	/**
	 * 设置时间参数
	 */
	@Override
	public void tran_setSchedules(Map<String, Object> map) throws Exception {
		map.put("taskType", "IS");
		Map<String, Object> schedules = binOLPTRPS27_Service.getSchedules(map);
		if(schedules == null && !CherryChecker.isNullOrEmpty(map.get("runTime"), true)){
			map.put("taskType", "IS");
			map.put("allowRepeat", "1");
			map.put("status", "1");
			map.put("loadFlag", "0");
			map.put("runCount", "0");
			binOLPTRPS27_Service.insertSchedules(map);
		}else{
			binOLPTRPS27_Service.updateSchedules(map);
		}
		String brandCode = ConvertUtil.getString(map.get(CherryConstants.BRAND_CODE));
		String organizationInfoId = ConvertUtil.getString(map.get(CherryConstants.ORGANIZATIONINFOID));
		String brandInfoId = ConvertUtil.getString(map.get(CherryConstants.BRANDINFOID));
		// 发送MQ
		MQInfoDTO mqInfoDTO = new MQInfoDTO();
		// 品牌代码
		mqInfoDTO.setBrandCode(brandCode);
		String billType = CherryConstants.MESSAGE_TYPE_RT;
		String billCode = binOLCM03_BL.getTicketNumber(Integer.parseInt(organizationInfoId), Integer.parseInt(brandInfoId), "", billType);
		// 业务类型
		mqInfoDTO.setBillType(billType);
		// 单据号
		mqInfoDTO.setBillCode(billCode);
		// 消息发送队列名
		mqInfoDTO.setMsgQueueName(CherryConstants.CHERRYSCHEDULETASKMSGQUEUE);
		// 设定消息内容
		Map<String,Object> msgDataMap = new HashMap<String,Object>();
		// 设定消息版本号
		msgDataMap.put("Version", CherryConstants.MESSAGE_VERSION_GT);
		// 设定消息命令类型
		msgDataMap.put("Type", CherryConstants.MESSAGE_TYPE_1006);
		// 设定消息数据类型
		msgDataMap.put("DataType", MessageConstants.DATATYPE_APPLICATION_JSON);
		// 设定消息的数据行
		Map<String,Object> dataLine = new HashMap<String,Object>();
		// 消息的主数据行
		Map<String,Object> mainData = new HashMap<String,Object>();
		// 品牌代码
		mainData.put("BrandCode", brandCode);
		// 业务类型
		mainData.put("TradeType", billType);
		// 单据号
		mainData.put("TradeNoIF", billCode);
		
		dataLine.put("MainData", mainData);
		msgDataMap.put("DataLine", dataLine);
		mqInfoDTO.setMsgDataMap(msgDataMap);
		// 发送MQ消息
		binOLMQCOM01_BL.sendMQMsg(mqInfoDTO, false);
	}

	/**
	 * 获取时间参数
	 */
	@Override
	public Map<String, Object> getSchedules(Map<String, Object> map) {
		map.put("taskType", "IS");
		Map<String, Object> schedulesMap = binOLPTRPS27_Service.getSchedules(map);
		return schedulesMap;
	}
	
	/**
	 * 获得业务参数
	 */
	@Override
	public List<Map<String, Object>> getBussnissParameter(Map<String, Object> map) {
		map.put("isRoot", "1");
		List<Map<String, Object>> parameterList =  binOLPTRPS27_Service.getBussnissParameter(map);
		return getDeepBussinessParamter(parameterList,map);
	}
	
	/**
	 * 设置业务参数
	 */
	@Override
	public void tran_setBussnissParameter(Map<String, Object> map) {
		map.put("parameterType","BT");
		map.put("validFlag", "0");
		binOLPTRPS27_Service.updateBussnissParameter(map);
		String[] parameter =(String[])map.get("parameterData");
		if(parameter != null){
			for(String str : parameter){
				map.put("validFlag", "1");
				map.put("inventoryOperationParamId", str);
				binOLPTRPS27_Service.updateBussnissParameter(map);
			}
		}
	}
	
	/**
	 * 获取子业务参数List，并转换为具有层级关系的List
	 * @param parameterList
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getDeepBussinessParamter(List<Map<String, Object>> parameterList,Map<String,Object> map) {
		for(Map<String, Object> fm : parameterList){
			Map<String,Object> paramMap = new HashMap<String, Object>();
			paramMap.put("organizationInfoId", map.get("organizationInfoId"));
			paramMap.put("brandInfoId", map.get("brandInfoId"));
			paramMap.put("validFlag", map.get("validFlag"));
			paramMap.put("parameterType", fm.get("parameterType"));
			paramMap.put("parameterParent", fm.get("parameterData"));
			List<Map<String, Object>> childrenList = binOLPTRPS27_Service.getBussnissParameter(paramMap);
			int childrenListSize = 0;
			for(Map<String, Object> fmap : childrenList){
				Map<String,Object> paramMap1 = new HashMap<String, Object>();
				paramMap1.put("organizationInfoId", map.get("organizationInfoId"));
				paramMap1.put("brandInfoId", map.get("brandInfoId"));
				paramMap1.put("validFlag", map.get("validFlag"));
				paramMap1.put("parameterType", fmap.get("parameterType"));
				paramMap1.put("parameterParent", fmap.get("parameterData"));
				List<Map<String, Object>> childrenList1 = binOLPTRPS27_Service.getBussnissParameter(paramMap1);
				fmap.put("childrenList", childrenList1);
				fmap.put("childrenListSize", childrenList1.size());
				childrenListSize += childrenList1.size();
			}
			fm.put("childrenList", childrenList);
			fm.put("childrenListSize", childrenListSize);
		}
		return parameterList;
	}
	
	/**
	 * 将层级业务List转换为线型
	 * @param departList 部门Id
	 * @param bussinessParameterList 具有层级关系的业务参数
	 * @return
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<Object> getLineBussinessParamter(Map<String, Object> map) {
		List<String> departList = (List<String>) map.get("departList");
		List<Map<String,Object>> bussinessParameterList = (List<Map<String, Object>>) map.get("bussinessParameterList");
		List<Object> lineBussinessParamter = new ArrayList<Object>();
		for(Map<String, Object> fm : bussinessParameterList){
			if("DPT".equals(fm.get("parameterParent")) == false){
				for(Map<String, Object> sm : (List<Map<String,Object>>)fm.get("childrenList")){
					for(Map<String, Object> tm : (List<Map<String,Object>>)sm.get("childrenList")){
						lineBussinessParamter.add(tm.get("parameterData"));
					}
				}
			}else{
				for(Map<String, Object> sm : (List<Map<String,Object>>)fm.get("childrenList")){
					for(Map<String, Object> tm : (List<Map<String,Object>>)sm.get("childrenList")){
						for(String departType : departList){
							lineBussinessParamter.add(tm.get("parameterData")+departType.trim());
						}
					}
				}
			}
		}
		return lineBussinessParamter;
	}
	
	/**
	 * 获得导出Excel
	 * 
	 * @param dataList
	 * @param map
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Override
	public byte[] exportExcel(Map<String, Object> map) throws Exception {
		String language = (String)map.get(CherryConstants.SESSION_LANGUAGE);
		//部门参数
		map.put("parameterType", "DT");
		List<Map<String, Object>> departList = binOLPTRPS27_Service.getDepartParameter(map);
		List<String> departTypeList = new ArrayList<String>();
		if(departList!=null){
			for(Map<String, Object> fm : departList){
				if(!CherryChecker.isNullOrEmpty(fm.get("parameterData"), true)){
					departTypeList.add(ConvertUtil.getString(fm.get("parameterData")).trim());
				}
			}
		}
		//任务参数
		map.put("taskType", "IS");
		Map<String,Object> schedules = binOLPTRPS27_Service.getSchedules(map);
		//业务参数
		map.put("isRoot", "1");
		map.put("validFlag", "1");
		List<Map<String, Object>> bussinessParamterList =  binOLPTRPS27_Service.getBussnissParameter(map);
		bussinessParamterList = getDeepBussinessParamter(bussinessParamterList, map);
		map.put("departList", departTypeList);
		map.put("bussinessParameterList", bussinessParamterList);
		List<Object> bussinessParamter = this.getLineBussinessParamter(map);
		map.put("bussinessParamter", bussinessParamter);
		//标题行,第一行
		List<String[]> titles1 = new ArrayList<String[]>(Arrays.<String[]>asList());
		//标题行,第二行
		List<String[]> titles2 = new ArrayList<String[]>(Arrays.<String[]>asList());
		//数据行
		List<String[]> dataArray = new ArrayList<String[]>(Arrays.<String[]>asList());
		for(Map<String, Object> fm : bussinessParamterList){
			//为柜台和柜台主管的业务时
			if(!"DPT".equals(fm.get("parameterParent"))){
				String[] title1 = new String[]{binOLCM37_BL.getResourceValue("BINOLPTRPS27", language, "PTRPS27_operator")+"："+ConvertUtil.getString(fm.get("parameterName")),ConvertUtil.getString(fm.get("childrenListSize")),"1"};
				titles1.add(title1);
				List<Map<String,Object>> childrenList1 = (List<Map<String, Object>>) fm.get("childrenList");
				for(Map<String, Object> sm : childrenList1){
					String[] title2 = new String[]{ConvertUtil.getString(sm.get("parameterName")),ConvertUtil.getString(sm.get("childrenListSize")),"1"};
					titles2.add(title2);
					List<Map<String,Object>> childrenList2 = (List<Map<String, Object>>) sm.get("childrenList");
					for(Map<String, Object> tm : childrenList2){
						String[] data = new String[]{ConvertUtil.getString(tm.get("parameterData")),ConvertUtil.getString(tm.get("parameterName")),"15","",""};
						dataArray.add(data);
					}
				}
			}
			//为部门业务时
			if("DPT".equals(fm.get("parameterParent"))){
				for(Map<String, Object> depart : departList){
					String departType = codeTable.getVal("1000",depart.get("parameterData"));
					if(CherryChecker.isNullOrEmpty(departType, true)){
						departType =  binOLCM37_BL.getResourceValue("BINOLPTRPS27", language, "PTRPS27_unknowDepartType");
					}
					String[] title1 = new String[]{binOLCM37_BL.getResourceValue("BINOLPTRPS27", language, "PTRPS27_operator")+"："+departType,ConvertUtil.getString(fm.get("childrenListSize")),"1"};
					titles1.add(title1);
					List<Map<String,Object>> childrenList1 = (List<Map<String, Object>>) fm.get("childrenList");
					for(Map<String, Object> sm : childrenList1){
						String[] title2 = new String[]{ConvertUtil.getString(sm.get("parameterName")),ConvertUtil.getString(sm.get("childrenListSize")),"1"};
						titles2.add(title2);
						List<Map<String,Object>> childrenList2 = (List<Map<String, Object>>) sm.get("childrenList");
						for(Map<String, Object> tm : childrenList2){
							String[] data = new String[]{ConvertUtil.getString(tm.get("parameterData"))+ConvertUtil.getString(depart.get("parameterData")),ConvertUtil.getString(tm.get("parameterName")),"15","",""};
							dataArray.add(data);
						}
					}
				}
			}
		}
		//基础数据
		String[][] titles01 = {
				//基础数据
				{binOLCM37_BL.getResourceValue("BINOLPTRPS27", language, "PTRPS27_statisticTime1")+schedules.get("taskCode"),"4","1"}
		};
		String[][] titles02 = {
				{binOLCM37_BL.getResourceValue("BINOLPTRPS27", language, "PTRPS27_statisticTime4")+this.getLastStatisticDate(map),"4","1"}
		};
		String[][] array0 = { 
				//基础数据
				{ "StatisticDate", binOLCM37_BL.getResourceValue("BINOLPTRPS27", language, "PTRPS27_date"), "15", "", "" },
				{ "weekday", binOLCM37_BL.getResourceValue("BINOLPTRPS27", language, "PTRPS27_week"), "15", "", "" },
				{ "departName", binOLCM37_BL.getResourceValue("BINOLPTRPS27", language, "PTRPS27_depart"), "50", "", "" },
				{ "departType", binOLCM37_BL.getResourceValue("BINOLPTRPS27", language, "PTRPS27_departType"), "15", "", "1000" }
		};
		//标题List
		List<String[][]> titleList = new ArrayList<String[][]>();
		titleList.add(this.unite(titles01, titles1.toArray(new String[0][])));
		titleList.add(this.unite(titles02, titles2.toArray(new String[0][])));
		String[][] array = this.unite(array0, dataArray.toArray(array0));
		int dataLen = binOLPTRPS27_Service.getInventoryOperationStatisticCount(map);
		map.put("titleList", titleList);
		map.put("dataLen", dataLen);
		map.put("sheetName", binOLCM37_BL.getResourceValue("BINOLPTRPS27", language, "sheetName"));
		map.put("titleRows", array);
		map.put(CherryConstants.SORT_ID, "StatisticDate desc");
		return binOLCM37_BL.exportExcel(map, this);
	}

	/**
	 * 得到Excel导出的数据
	 */
	@Override
	public List<Map<String, Object>> getDataList(Map<String, Object> map) throws Exception {
		return binOLPTRPS27_Service.getInventoryOperationStatisticList(map);
	}
	
	/**
	 * 合并两个二维数组
	 * @param arr1
	 * @param arr2
	 * @return
	 */
	public  String[][] unite(String[][] arr1, String[][] arr2) {
		  List<String[]> list = new ArrayList<String[]>(Arrays.<String[]>asList(arr1));
		  list.addAll(Arrays.<String[]>asList(arr2));
		  return list.toArray(arr1);
	}
	
	/**
	 * 获取最后一次统计的数据日期
	 */
	@Override
	public String getLastStatisticDate(Map<String, Object> map) {
		return binOLPTRPS27_Service.getLastStatisticDate(map);
	}


}
