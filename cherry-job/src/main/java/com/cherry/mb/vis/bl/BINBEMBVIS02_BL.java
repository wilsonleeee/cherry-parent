/*	
 * @(#)BINBEMBVIS02_BL.java     1.0 @2012-12-14		
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
package com.cherry.mb.vis.bl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.core.BatchExceptionDTO;
import com.cherry.cm.core.BatchLoggerDTO;
import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryBatchException;
import com.cherry.cm.core.CherryBatchLogger;
import com.cherry.cm.util.CherryBatchUtil;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.DateUtil;
import com.cherry.mb.vis.service.BINBEMBVIS02_Service;

/**
 *
 * 会员回访任务生成BL
 *
 * @author jijw
 *
 * @version  2012-12-14
 */
public class BINBEMBVIS02_BL {
	
	private static CherryBatchLogger logger = new CherryBatchLogger(
			BINBEMBVIS02_BL.class);	
	@Resource
	private BINBEMBVIS02_Service binBEMBVIS02_Service;
	
	/** BATCH处理标志 */
	private int flag = CherryBatchConstants.BATCH_SUCCESS;

	/** 处理总条数 */
	private int totalCount = 0;
	/** 插入条数 */
	private int insertCount = 0;
	/** 失败条数 */
	private int failCount = 0;
	/** 共通map */
	private Map<String, Object> comMap;
	/**  柜台List */
	private List<String> counterList;

	/**
	 * 产品列表的batch处理
	 * 
	 * @param 无
	 * 
	 * @return Map
	 * @throws CherryBatchException
	 */
	public int tran_batchMemVistTask(Map<String, Object> map)
			throws CherryBatchException {
		// 初始化
		init(map);
		// 柜台List
		counterList = binBEMBVIS02_Service.getCounterList(map);
		
		// 分组柜台的BA信息
		Map<String, Object> counterMap = groupBAOfCounterList(map);
		
		// 数据抽出次数
		int currentNum = 0;
		// 从会员信息表中分批取得会员信息列表，并处理
		while (true) {
			// 查询开始位置
			int startNum = CherryBatchConstants.DATE_SIZE * currentNum + 1;
			currentNum += 1;
			// 查询结束位置
			int endNum = startNum + CherryBatchConstants.DATE_SIZE - 1;
			map.put(CherryBatchConstants.START, startNum);
			map.put(CherryBatchConstants.END, endNum);
			map.put(CherryBatchConstants.SORT_ID, "counterCode");
			// 查询会员信息列表
			List<Map<String, Object>> membersList = binBEMBVIS02_Service.getMembersList(map);
			if (CherryBatchUtil.isBlankList(membersList)) {
				break;
			}
			// 统计总条数
			totalCount += membersList.size();
			// 写入会员回访任务表
			addMemberVistTask(map, membersList, counterMap);
			// 产品数据少于一页，跳出循环
			if (membersList.size() < CherryBatchConstants.DATE_SIZE) {
				break;
			}
		}
		// 输出处理结果信息
		outMessage();
		return flag;
	}
	
	/**
	 * 写入会员回访任务表
	 * @param map
	 * @param membersList
	 */
	@SuppressWarnings("unchecked")
	private void addMemberVistTask(Map<String, Object> map,List<Map<String, Object>> membersList,Map<String, Object> counterMap){
		
		// 分组柜台的会员信息
		List<Map<String, Object>> membersList2 = groupMembersList(map, membersList);
		
		// 循环取得柜台的会员信息及BA信息并在处理后写入会员回访任务表
		for(Map<String, Object> memberMap : membersList2){
			// 柜台Code
			String counterCode = memberMap.get("counterCode").toString();
			// 取得当前柜台的会员信息
			List<Map<String, Object>> meList = (List<Map<String, Object>>) memberMap.get("meList");
			// 取得当前柜台
			List<Map<String, Object>> baList = (List<Map<String, Object>>) counterMap.get(counterCode);
			
			if(meList.size() != 0 && baList.size() != 0){
				// 将柜台的会员信息遍历至柜台的BA并插入会员回访信息表
				insertMeVisDB(meList, baList);
			} else {
				continue;
			}
		}
	}
	
	/**
	 * 将柜台的会员信息遍历至柜台的BA并插入会员回访信息表
	 * @param meList
	 * @param baList
	 */
	private void insertMeVisDB(List<Map<String, Object>> meList,List<Map<String, Object>> baList){

		// 定义会员信息list及BA信息list的索引
		int meListIndex = 0 , baListIndex = 0;
		
		while(meList.size() != meListIndex){

			// 定义写入会员回访信息
			Map<String,Object> insMeVisMap = new HashMap<String, Object>();
			
			// 取得会员回访需要的部分会员信息
			Map<String,Object> meMap = meList.get(meListIndex);
			insMeVisMap.putAll(meMap);
			// 取得会员回访需要的BA信息
			Map<String,Object> baMap = baList.get(baListIndex);
			insMeVisMap.putAll(baMap);
			// 设置会员回访需要的其他信息
			insMeVisMap.putAll(comMap);
			
			try{
				// 插入会员回访信息表
				binBEMBVIS02_Service.insertMemVisitInfo(insMeVisMap);
				// 插入件数加一
				insertCount += 1;
				
				binBEMBVIS02_Service.manualCommit();
			}catch(Exception e){
				binBEMBVIS02_Service.manualRollback();
				
				BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO();
				batchExceptionDTO.setBatchName(this.getClass());
				batchExceptionDTO.setErrorCode("EMB00003");
				batchExceptionDTO
						.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
				// 柜台Code
				batchExceptionDTO.addErrorParam(CherryBatchUtil.getString(insMeVisMap
						.get("counterCode")));
				// 会员ID
				batchExceptionDTO.addErrorParam(CherryBatchUtil.getString(insMeVisMap
						.get("memberInfoID")));
				// BACode
				batchExceptionDTO.addErrorParam(CherryBatchUtil.getString(insMeVisMap
						.get("baCode")));
				// AHEAD_DAY
				batchExceptionDTO.addErrorParam(CherryBatchUtil.getString(insMeVisMap
						.get("AHEAD_DAY")));
				// START_DAY
				batchExceptionDTO.addErrorParam(CherryBatchUtil.getString(insMeVisMap
						.get("START_DAY")));
				// END_DAY
				batchExceptionDTO.addErrorParam(CherryBatchUtil.getString(insMeVisMap
						.get("END_DAY")));
				// 回访问卷ID
				batchExceptionDTO.addErrorParam(CherryBatchUtil.getString(insMeVisMap
						.get("PAPERID")));
				
				batchExceptionDTO.setException(e);
				failCount += 1;
				flag = CherryBatchConstants.BATCH_WARNING;
			}
			
			meListIndex++;
			baListIndex++;
			
			if(baList.size() == baListIndex){
				baListIndex = 0;
			}
		}
		
	}
	
	/**
	 * 	
	 * 分组柜台的会员信息
	 * @param map
	 * @param membersList 会员信息
	 * @return
	 */
	private List<Map<String, Object>> groupMembersList(Map<String, Object> map,List<Map<String, Object>> membersList){
		// 定义存放BA的集合
		List<Map<String, Object>> resultList = new ArrayList<Map<String,Object>>();
		
		// 分组每个柜台的BA
		for(String counterCode : counterList){
			Map<String,Object> resultMap = new HashMap<String, Object>();
			resultMap.put("counterCode", counterCode);
			
			List<Map<String, Object>> baList = new ArrayList<Map<String,Object>>();
			for(Map<String, Object> itemMap : membersList){
				if(counterCode.equals(itemMap.get("counterCode").toString())){
					baList.add(itemMap);
				}
			}
			resultMap.put("meList", baList); // 柜台的会员信息
			resultList.add(resultMap);
		}
		
		return resultList;
	}
	
	/**
	 * 分组柜台的BA信息
	 * @param map
	 * @return
	 */
	private Map<String, Object> groupBAOfCounterList(Map<String, Object> map){
		// 定义存放BA的集合
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		// 查询会员信息列表
		List<Map<String, Object>> bAOfCounterList = binBEMBVIS02_Service.getBAOfCounterList(map);
		
		// 分组每个柜台的BA
		for(String counterCode : counterList){
			
			List<Map<String, Object>> baList = new ArrayList<Map<String,Object>>();
			for(Map<String, Object> itemMap : bAOfCounterList){
				if(counterCode.equals(itemMap.get("counterCode").toString())){
					baList.add(itemMap);
				}
			}
			resultMap.put(counterCode, baList); // 柜台的BA信息
		}
		
		return resultMap;
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
		// 失败件数
		BatchLoggerDTO batchLoggerDTO5 = new BatchLoggerDTO();
		batchLoggerDTO5.setCode("IIF00005");
		batchLoggerDTO5.setLevel(CherryBatchConstants.LOGGER_INFO);
		batchLoggerDTO5.addParam(String.valueOf(failCount));
		// 处理总件数
		logger.BatchLogger(batchLoggerDTO1);
		// 插入件数
		logger.BatchLogger(batchLoggerDTO3);
		// 成功总件数
		logger.BatchLogger(batchLoggerDTO2);
		// 失败件数
		logger.BatchLogger(batchLoggerDTO5);
	}

	/**
	 * init
	 * @param map
	 */
	private void init(Map<String, Object> map) {
		// 取得更新共通信息map
		comMap = getComMap(map);
		
		// 系统时间
		String sysTime = binBEMBVIS02_Service.getSYSDate();
		
		// 查找N天后生日的会员
		String birthDay = DateUtil.addDateByDays("yyyyMMdd", sysTime.substring(0,10), Integer.parseInt(map.get("AHEAD_DAY").toString()));
		
		// 如果查出来的生日是2月28日，且生日回访所属年为平年，则把2月29日生日的会员一起取出来,START_DAY及END_DAY按照2月28日生日进行计算处理
		if(birthDay.substring(4, 8).equals("0228") && !DateUtil.getLeapYear(birthDay.substring(0, 4))){
			map.put("birthDay", birthDay.substring(4, 8) + "," + "0229");
		}else {
			map.put("birthDay", birthDay.substring(4, 8));
		}
		
		// 任务的开始时间
		String startTime = DateUtil.addDateByDays("yyyy-MM-dd", birthDay, -Integer.parseInt(map.get("START_DAY").toString()));
		startTime = CherryUtil.suffixDate(startTime, 0);
		comMap.put("startTime", startTime);
		// 任务的结束时间
		String endTime = DateUtil.addDateByDays("yyyy-MM-dd", birthDay, -Integer.parseInt(map.get("END_DAY").toString()));
		endTime = CherryUtil.suffixDate(endTime, 1);
		comMap.put("endTime", endTime);
		// 绑定回访问卷ID
		if(map.get("PAPERID") !=null && !map.get("PAPERID").equals("")){
			comMap.put("paperId", map.get("PAPERID"));
		} else {
			comMap.put("paperId", null);
		}
		
		// 回访类型:VISIT_TYPE004(生日回访)
		comMap.put("visitType", "VISIT_TYPE004");
		
		// 回访任务时间 ：回访类型为生日回访时，插入年份(格式yyyy)。
		comMap.put("visitTaskTime", birthDay.substring(0, 4));
	}

	/**
	 * 共通Map
	 * @param map
	 * @return
	 */
	private Map<String, Object> getComMap(Map<String, Object> map) {
		Map<String, Object> baseMap = new HashMap<String, Object>();

		// 更新程序名
		baseMap.put(CherryBatchConstants.UPDATEPGM, "BINBEIFPRO02");
		// 作成程序名
		baseMap.put(CherryBatchConstants.CREATEPGM, "BINBEIFPRO02");
		// 作成者
		baseMap.put(CherryBatchConstants.CREATEDBY,
				CherryBatchConstants.UPDATE_NAME);
		// 更新者
		baseMap.put(CherryBatchConstants.UPDATEDBY,CherryBatchConstants.UPDATE_NAME);
		// 所属组织
		baseMap.put(CherryBatchConstants.ORGANIZATIONINFOID, map.get(CherryBatchConstants.ORGANIZATIONINFOID).toString());
		// 品牌
		baseMap.put(CherryBatchConstants.BRANDINFOID, map.get(CherryBatchConstants.BRANDINFOID).toString());
		
		return baseMap;
	}

}
