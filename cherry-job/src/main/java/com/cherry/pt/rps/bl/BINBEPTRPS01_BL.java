/*	
 * @(#)BINBEPTRPS01_BL.java     1.0.0 2013/08/15		
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.core.BatchLoggerDTO;
import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryBatchException;
import com.cherry.cm.core.CherryBatchLogger;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.DateUtil;
import com.cherry.pt.rps.service.BINBEPTRPS01_Service;

/**
 * 进销存操作统计BL
 * 
 * @author zhangle
 * @version 1.0.0 2013.08.15
 * 
 */
public class BINBEPTRPS01_BL{

	
	@Resource
	private BINBEPTRPS01_Service binBEPTRPS01_Service;
	@Resource
	private BINOLCM14_BL binOLCM14_BL;
	
	private static Logger logger = LoggerFactory.getLogger(BINBEPTRPS01_BL.class.getName());
	
	/**
	 * 运行调度
	 * @param schedulesId 调度ID
	 * @return
	 * @throws Exception
	 */
	public int runSchedules(Map<String,Object> map) throws Exception {
		int flag = CherryBatchConstants.BATCH_SUCCESS;
		try {
			//取业务日期
			Map<String, Object> bussinessDateMap = binBEPTRPS01_Service.getBussinessDateMap(map);
			String date = ConvertUtil.getString(bussinessDateMap.get("businessDate"));
			//若日结状态为"未结束"，则取前一天日期
			if("0".equals(bussinessDateMap.get("closeFlag"))){
				date = DateUtil.addDateByDays(CherryBatchConstants.DATE_PATTERN, date, -1);
			}
			map.put("date", date);
			flag = tran_ScheduleTask(map);
		} catch (CherryBatchException cbx) {
			flag = CherryBatchConstants.BATCH_WARNING;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			flag = CherryBatchConstants.BATCH_ERROR;
		}
		return flag;
	}
	

	/**
	 * 进销存操作统计
	 * @return
	 * @throws Exception
	 */
	public  int tran_ScheduleTask(Map<String, Object> map) throws Exception{
		// BATCH处理标志
		int flag = CherryBatchConstants.BATCH_SUCCESS;
		// 总条数
		int totalCount = 0;
		// 成功条数
		int successCount = 0;
		// 失败条数
		int failCount = 0;
		//执行日期
		String date = ConvertUtil.getString(map.get("date"));
		//删除指定时间点数据
		flag = delStatisticDataBefore(map);
		//删除当前日期数据
		flag = delStatisticDataNow(map);
		CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
		map.putAll(getCommonMap());
		map.put("parameterType", "BT");
		map.put("isRoot", "1");
		//获取业务大分类
		List<Map<String, Object>> bussinessTypeRoot = binBEPTRPS01_Service.getBussnissParameter(map);
		map.remove("isRoot");
		for(Map<String, Object> fm : bussinessTypeRoot){
			map.put("parameterParent", fm.get("parameterData"));
			//非部门大分类
			if(!"DPT".equals(fm.get("parameterData"))){
				map.put("parameterType", "BT");
				//获取业务类型
				List<Map<String, Object>> typesList = binBEPTRPS01_Service.getBussnissParameter(map);
				//获取业务数据
				List<Map<String, Object>> dataList = this.getDataList(map);
				Map<String, Object> paramMap = new HashMap<String, Object>();
				paramMap.put("types", typesList);
				paramMap.put("dataList", dataList);
				paramMap.put("departIdOperate", null);
				paramMap.put("date", date);
				//将业务数据插入到统计表
				Map<String, Object> resultMap = this.insertStatisticData(paramMap);
				totalCount += ConvertUtil.getInt(resultMap.get("totalCount"));
				successCount += ConvertUtil.getInt(resultMap.get("successCount"));
				failCount += ConvertUtil.getInt(resultMap.get("failCount"));
				flag = ConvertUtil.getInt(resultMap.get("flag"));
			}else{//部门大分类
				map.put("parameterType", "BT");
				//获取业务类型
				List<Map<String, Object>> typesList = binBEPTRPS01_Service.getBussnissParameter(map);
				//获取部门参数
				List<Map<String,Object>> departParameterList = binBEPTRPS01_Service.getDepartParameter(map);
				for(Map<String,Object> departInfo : departParameterList){
					//部门Id
					String departIdOperate = ConvertUtil.getString(departInfo.get("organizationId"));
					if(CherryChecker.isNullOrEmpty(departIdOperate, true)){
						continue;
					}
					map.put("departId", departIdOperate);
					//获取部门业务数据
					List<Map<String, Object>> dataList = this.getDataList(map);
					Map<String, Object> paramMap = new HashMap<String, Object>();
					paramMap.put("types", typesList);
					paramMap.put("dataList", dataList);
					paramMap.put("departIdOperate", departIdOperate);
					paramMap.put("date", date);
					//插入部门业务数据
					Map<String, Object> resultMap = this.insertStatisticData(paramMap);
					totalCount += ConvertUtil.getInt(resultMap.get("totalCount"));
					successCount += ConvertUtil.getInt(resultMap.get("successCount"));
					failCount += ConvertUtil.getInt(resultMap.get("failCount"));
					flag = ConvertUtil.getInt(resultMap.get("flag"));
				}
			}
		}
		
		String brandCode = ConvertUtil.getString(map.get("brandCode"));
		// 总件数
		BatchLoggerDTO batchLoggerDTO1 = new BatchLoggerDTO();
		batchLoggerDTO1.setCode("IPT00001");
		batchLoggerDTO1.setLevel(CherryBatchConstants.LOGGER_INFO);
		batchLoggerDTO1.addParam(brandCode);
		batchLoggerDTO1.addParam(String.valueOf(totalCount));
		// 成功总件数
		BatchLoggerDTO batchLoggerDTO2 = new BatchLoggerDTO();
		batchLoggerDTO2.setCode("IPT00002");
		batchLoggerDTO2.setLevel(CherryBatchConstants.LOGGER_INFO);
		batchLoggerDTO2.addParam(brandCode);
		batchLoggerDTO2.addParam(String.valueOf(successCount));
		// 失败件数
		BatchLoggerDTO batchLoggerDTO3 = new BatchLoggerDTO();
		batchLoggerDTO3.setCode("IPT00003");
		batchLoggerDTO3.setLevel(CherryBatchConstants.LOGGER_INFO);
		batchLoggerDTO3.addParam(brandCode);
		batchLoggerDTO3.addParam(String.valueOf(failCount));
		
		// 处理总件数
		cherryBatchLogger.BatchLogger(batchLoggerDTO1);
		// 成功总件数
		cherryBatchLogger.BatchLogger(batchLoggerDTO2);
		// 失败件数
		cherryBatchLogger.BatchLogger(batchLoggerDTO3);
		return flag;
	}
	
	/**
	 * 获取指定大分类的数据
	 * @param parameterParent 指定大分类
	 * @return
	 */
	public List<Map<String, Object>> getDataList(Map<String, Object> map) {
		Object handle = map.get("parameterParent");
		if("CNT".equals(handle)){
			return binBEPTRPS01_Service.getList(map);
		}
		if("BAS".equals(handle)){
			return binBEPTRPS01_Service.getBASDataList(map);
		}
		if("OFW".equals(handle)){
			return binBEPTRPS01_Service.getOFWDataList(map);
		}
		if("DPT".equals(handle)){
			return binBEPTRPS01_Service.getDepartDataList(map);
		}
		return null;
	}
	
	/**
	 * 插入统计数据
	 * @param types 业务类型
	 * @param dataList 数据List
	 * @param departIdOperate 操作部门
	 * @param date 统计时间
	 * @param flag 运行结果
	 * @param totalCount 总条数
	 * @param successCount 成功条数
	 * @param failCount 失败条数
	 * @return
	 * @throws CherryBatchException
	 */
	@SuppressWarnings("unchecked")
	public Map<String,Object> insertStatisticData(Map<String,Object> map) throws CherryBatchException {
		// BATCH处理标志
		int flag = CherryBatchConstants.BATCH_SUCCESS;
		CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
		// 总条数
		int totalCount = 0;
		// 成功条数
		int successCount = 0;
		// 失败条数
		int failCount = 0;
		List<Map<String, Object>> types = (List<Map<String, Object>>) map.get("types");
		List<Map<String, Object>> dataList = (List<Map<String, Object>>) map.get("dataList");
		Object departIdOperate = map.get("departIdOperate");
		String date = ConvertUtil.getString(map.get("date"));
		for(Map<String, Object> fm : dataList){
			Map<String,Object> commMap = getCommonMap();
			commMap.put("date", date);
			commMap.put(CherryConstants.ORGANIZATIONINFOID, fm.get(CherryConstants.ORGANIZATIONINFOID));
			commMap.put(CherryConstants.BRANDINFOID, fm.get(CherryConstants.BRANDINFOID));
			commMap.put("departId", fm.get("organizationId"));
			commMap.put("departIdOperate", departIdOperate);
			for(Map<String, Object> typeInfo : types){
				String type = ConvertUtil.getString(typeInfo.get("parameterData"));
				Map<String, Object> infoMap = new HashMap<String, Object>();
				infoMap.putAll(commMap);
				infoMap.put("type",type);
				String data = ConvertUtil.getString(fm.get(type));
				infoMap.put("data", data);
				if(CherryChecker.isNullOrEmpty(data, true) || "0".equals(data) || "0.00".equals(data) || "0.0000".equals(data) || "0.000000".equals(data)){
					continue;
				}
				try{
					totalCount++;
					binBEPTRPS01_Service.insertData(infoMap);
					successCount++;
				}catch (Exception e) {
					failCount++;
					List<String> paramsList = new ArrayList<String>();
					paramsList.add(ConvertUtil.getString(fm.get("organizationId")));
					paramsList.add(type);
					paramsList.add(ConvertUtil.getString(fm.get("organizationId")));
					paramsList.add(date);
					flag = CherryBatchConstants.BATCH_WARNING;
					BatchLoggerDTO batchLoggerDTO1 = new BatchLoggerDTO();
					batchLoggerDTO1.setCode("EPT00001");
					batchLoggerDTO1.setParamsList(paramsList);
					batchLoggerDTO1.setLevel(CherryBatchConstants.LOGGER_ERROR);
					cherryBatchLogger.BatchLogger(batchLoggerDTO1, e);
				}
			}
		}
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("flag", flag);
		resultMap.put("totalCount", totalCount);
		resultMap.put("successCount", successCount);
		resultMap.put("failCount", failCount);
		return resultMap;
	}
	
	
	/**
	 * 清除指定时间点以前的数据
	 * @param map
	 * @return
	 */
	public  int delStatisticDataBefore(Map<String, Object> map) throws Exception{
		int flag = CherryBatchConstants.BATCH_SUCCESS;
		CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
		int count=0;
		String brandCode = ConvertUtil.getString(map.get("brandCode"));
		String brandInfoId = ConvertUtil.getString(map.get("brandInfoId"));
		String organizationInfoId = ConvertUtil.getString(map.get("organizationInfoId"));
		//现在业务日期
		String nowDate = ConvertUtil.getString(map.get("date"));
		String delMoth = binOLCM14_BL.getConfigValue("1093",organizationInfoId,brandInfoId);
		//参数Map
		Map<String, Object> paramMap = new HashMap<String, Object>();
		//品牌ID
		paramMap.put("brandInfoId", brandInfoId);
		//组织ID
		paramMap.put("organizationInfoId", organizationInfoId);
		//删除的时间点，不包含该时间点
		String delDate = DateUtil.addDateByMonth("yyyy-MM-dd", nowDate, -ConvertUtil.getInt(delMoth));
		paramMap.put("delDate", delDate);
		try{
			count = binBEPTRPS01_Service.delData(paramMap);
		} catch (Exception e) {
			flag = CherryBatchConstants.BATCH_ERROR;
			e.printStackTrace();
		} finally {
			BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
			if (flag == CherryBatchConstants.BATCH_SUCCESS) {
				batchLoggerDTO.setCode("IPT00004");
				batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_INFO);
				batchLoggerDTO.addParam(brandCode);
				batchLoggerDTO.addParam(delDate);
				batchLoggerDTO.addParam(String.valueOf(count));
			} else if (flag == CherryBatchConstants.BATCH_ERROR) {
				batchLoggerDTO.setCode("IPT00005");
				batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_INFO);
				batchLoggerDTO.addParam(brandCode);
				batchLoggerDTO.addParam(delDate);
			}
			cherryBatchLogger.BatchLogger(batchLoggerDTO);
		} 	
		
		
		return flag;
	}
	
	/**
	 * 删除指定日期数据
	 * @param map 
	 * @param date 指定的日期
	 * @return
	 * @throws Exception
	 */
	public  int delStatisticDataNow(Map<String, Object> map) throws Exception{
		int flag = CherryBatchConstants.BATCH_SUCCESS;
		CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
		int count=0;
		String brandCode = ConvertUtil.getString(map.get("brandCode"));
		String nowDate = ConvertUtil.getString(map.get("date"));
		Map<String, Object> paramMap = new HashMap<String, Object>();
		//品牌ID
		paramMap.put("brandInfoId", map.get("brandInfoId"));
		//组织ID
		paramMap.put("organizationInfoId", map.get("organizationInfoId"));
		//删除当天的数据
		paramMap.put("nowDate", nowDate);
		try{
			count = binBEPTRPS01_Service.delData(paramMap);
		} catch (Exception e) {
			flag = CherryBatchConstants.BATCH_ERROR;
			e.printStackTrace();
		} finally {
			BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
			if (flag == CherryBatchConstants.BATCH_SUCCESS) {
				batchLoggerDTO.setCode("IPT00006");
				batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_INFO);
				batchLoggerDTO.addParam(brandCode);
				batchLoggerDTO.addParam(nowDate);
				batchLoggerDTO.addParam(String.valueOf(count));
			} else if (flag == CherryBatchConstants.BATCH_ERROR) {
				batchLoggerDTO.setCode("IPT00007");
				batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_INFO);
				batchLoggerDTO.addParam(brandCode);
				batchLoggerDTO.addParam(nowDate);
			}
			cherryBatchLogger.BatchLogger(batchLoggerDTO);
		} 	
		return flag;
	}
	
	public String getBussinessDate(Map<String, Object> map) {
		return binBEPTRPS01_Service.getBussinessDate(map);
	}
	
	/**
	 * 获取共通Map
	 * @return
	 */
	public Map<String, Object> getCommonMap() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("createdBy", "BINBEPTRPS01");
		map.put("createPGM", "BINBEPTRPS01");
		map.put("updatedBy", "BINBEPTRPS01");
		map.put("updatePGM", "BINBEPTRPS01");
		return map;
	}
}
