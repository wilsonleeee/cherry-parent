/*	
 * @(#)BINBEMBARC02_BL.java     1.0 2013/04/11		
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
package com.cherry.mb.arc.bl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.cmbussiness.interfaces.BINOLCM31_IF;
import com.cherry.cm.core.BatchLoggerDTO;
import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryBatchException;
import com.cherry.cm.core.CherryBatchLogger;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.PropertiesUtil;
import com.cherry.cm.util.CherryBatchUtil;
import com.cherry.dr.cmbussiness.util.DateUtil;
import com.cherry.mb.arc.service.BINBEMBARC02_Service;

/**
 * 会员积分初始导入处理 BL
 * 
 * @author hub
 * @version 1.0 2013/04/11
 */
public class BINBEMBARC02_BL {
	
	/** 会员积分初始导入处理Service */
	@Resource
	private BINBEMBARC02_Service binBEMBARC02_Service;
	
	@Resource
	private BINOLCM31_IF binOLCM31_BL;
	
	/** BATCH处理标志 */
	private int flag = CherryBatchConstants.BATCH_SUCCESS;

	/** 会员积分失败条数 */
	private int failCount = 0;
	
	/** 会员积分更新失败条数 */
	private int upfailCount = 0;
	
	/** 总积分和可兑换积分一致*/
	private boolean isEqualPt = false;
	
	/** 共通Batch Log处理*/
	private CherryBatchLogger cherryBatchLogger;
	
	/** 共通BatchLogger*/
	private BatchLoggerDTO batchLoggerDTO;
	
	/** 导入失败的记录列表*/
	private List<Map<String, Object>> failRecordList;
	
	private static Logger logger = LoggerFactory.getLogger(BINBEMBARC02_BL.class.getName());
	
	/**
	 * 会员积分初始导入处理
	 * 
	 * @param map 参数集合
	 * @return BATCH处理标志
	 */
	public int tran_ImptMemPoint(Map<String, Object> map) throws Exception {
		if (!"1".equals(map.get("cgptFlag"))) {
			isEqualPt = true;
		}
		// 共通Batch Log处理
		cherryBatchLogger = new CherryBatchLogger(this.getClass());
		// 共通BatchLogger
		batchLoggerDTO = new BatchLoggerDTO();
		// 取得品牌数据库中会员积分信息总数
		int count = binBEMBARC02_Service.getWitMemPointCount(map);
		// 没有新增的会员积分信息
		if (0 == count) {
			// 未新增记录，不执行导入
			batchLoggerDTO.setCode("IMB00002");
			batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_INFO);
			batchLoggerDTO.addParam(PropertiesUtil.getMessage("PMB00002", null));
			cherryBatchLogger.BatchLogger(batchLoggerDTO);
		} else {
			// 从接口数据库中分批取得会员积分列表，并处理
			batchLoggerDTO.setCode("IMB00001");
			batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_INFO);
			batchLoggerDTO.addParam(PropertiesUtil.getMessage("PMB00001", null));
			batchLoggerDTO.addParam(String.valueOf(count));
			cherryBatchLogger.BatchLogger(batchLoggerDTO);
			failRecordList = new ArrayList<Map<String, Object>>();
			// 数据查询长度
			int dataSize = CherryBatchConstants.DATE_SIZE;
			// 查询数据量
			map.put("COUNT", dataSize);
			int pageNum = 0;
			while (true) {
				pageNum++;
				// 查询会员积分信息
				List<Map<String, Object>> witMemPointList = binBEMBARC02_Service.getWitMemPointList(map);
				// 会员积分信息不为空
				if (!CherryBatchUtil.isBlankList(witMemPointList)) {
					// 会员积分信息导入处理
					boolean nextFlag = imptWitMemPoint(witMemPointList, map, pageNum);
					// 有异常不继续进行
					if (!nextFlag) {
						break;
					}
					// 会员积分数据少于一次抽取的数量，即为最后一页，跳出循环
					if(witMemPointList.size() < dataSize) {
						break;
					}
				} else {
					break;
				}
			}
			if (!failRecordList.isEmpty()) {
				try {
					// 导入失败的记录去除标记(积分信息表)
					binBEMBARC02_Service.delFailWitMempoints(failRecordList);
					binBEMBARC02_Service.witManualCommit();
				} catch (Exception e) {
					// 打印错误信息
					logger.error(e.getMessage(),e);
					batchLoggerDTO.clear();
					batchLoggerDTO.setCode("EMB00020");
					batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
					cherryBatchLogger.BatchLogger(batchLoggerDTO);
					flag = CherryBatchConstants.BATCH_WARNING;
					for (Map<String, Object> failRecord : failRecordList) {
						String id = String.valueOf(failRecord.get("id"));
						logger.error(id);
					}
				}
			}
			// 全部导入完成
			batchLoggerDTO.clear();
			batchLoggerDTO.setCode("IMB00004");
			batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_INFO);
			batchLoggerDTO.addParam(PropertiesUtil.getMessage("PMB00003", null));
			batchLoggerDTO.addParam(String.valueOf(count));
			batchLoggerDTO.addParam(String.valueOf(failCount));
			cherryBatchLogger.BatchLogger(batchLoggerDTO);
		}
		// 几天前
		int uptDay = -2;
		if (!CherryChecker.isNullOrEmpty(map.get("uptDay"))) {
			int tDay = Integer.parseInt(map.get("uptDay").toString());
			if (0 == tDay) {
				return flag;
			}
			uptDay = -tDay;
		}
		// 业务日期
		String closeDate = binBEMBARC02_Service.getBussinessDate(map);
		String befDate = DateUtil.addDateByDays("yyyyMMdd", closeDate, uptDay);
		// 更新近期有积分有变化的会员处理
		batchLoggerDTO.clear();
		batchLoggerDTO.setCode("IMB00006");
		batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_INFO);
		batchLoggerDTO.addParam(befDate);
		cherryBatchLogger.BatchLogger(batchLoggerDTO);
		map.put("befDate", befDate);
		// 品牌数据库中最近几天有变化的会员积分总数
		int lastChangeCount = binBEMBARC02_Service.getWitMemLastCount(map);
		if (lastChangeCount > 0) {
			batchLoggerDTO.clear();
			batchLoggerDTO.setCode("IMB00008");
			batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_INFO);
			batchLoggerDTO.addParam(String.valueOf(lastChangeCount));
			cherryBatchLogger.BatchLogger(batchLoggerDTO);
			// 从接口数据库中分批取得会员积分列表，并处理
			// 数据查询长度
			int dataSize = CherryBatchConstants.DATE_SIZE;
			// 数据抽出次数
			int currentNum = 0;
			// 查询开始位置
			int startNum = 0;
			// 查询结束位置
			int endNum = 0;
			// 排序字段
			map.put(CherryBatchConstants.SORT_ID, "ID");
			int pageNum = 0;
			while (true) {
				pageNum++;
				// 查询开始位置
				startNum = dataSize * currentNum + 1;
				// 查询结束位置
				endNum = startNum + dataSize - 1;
				// 数据抽出次数累加
				currentNum++;
				// 查询开始位置
				map.put(CherryBatchConstants.START, startNum);
				// 查询结束位置
				map.put(CherryBatchConstants.END, endNum);
				// 查询会员积分信息
				List<Map<String, Object>> witMemLastList = binBEMBARC02_Service.getWitMemLastList(map);
				// 会员积分数据不为空
				if (!CherryBatchUtil.isBlankList(witMemLastList)) {
					// 更新近期有积分有变化的会员处理
					upLastChangeMem(witMemLastList, map, pageNum);
					// 促销品数据少于一次抽取的数量，即为最后一页，跳出循环
					if(witMemLastList.size() < dataSize) {
						break;
					}
				} else {
					break;
				}
			}
			// 全部更新完成
			batchLoggerDTO.clear();
			batchLoggerDTO.setCode("IMB00005");
			batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_INFO);
			batchLoggerDTO.addParam(String.valueOf(lastChangeCount));
			batchLoggerDTO.addParam(String.valueOf(upfailCount));
			cherryBatchLogger.BatchLogger(batchLoggerDTO);
		} else {
			// 近期会员积分没有变化，不执行更新处理
			batchLoggerDTO.clear();
			batchLoggerDTO.setCode("IMB00007");
			batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_INFO);
			cherryBatchLogger.BatchLogger(batchLoggerDTO);
		}
		return flag;
	}
	
	/**
	 * 更新近期有积分有变化的会员处理
	 * 
	 * @param witMemLastList 
	 * 			近期有积分有变化的会员信息
	 * @param map 
	 * 			共通参数
	 * @param pageNum 
	 * 			批次
	 * @return 
	 * @throws CherryBatchException
	 * 
	 */
	public void upLastChangeMem(List<Map<String, Object>> witMemLastList, 
			Map<String, Object> map, int pageNum) throws CherryBatchException {
		// 组织信息ID
		int organizationInfoId = Integer.parseInt(
				map.get(CherryBatchConstants.ORGANIZATIONINFOID).toString());
		// 品牌信息Id
		int brandInfoId = Integer.parseInt(
				map.get(CherryBatchConstants.BRANDINFOID).toString());
		// 本次更新失败件数
		int pointFailCount = 0;
		// 本批次处理总件数
		int size = witMemLastList.size();
		// 需要更新的记录列表
		List<Map<String, Object>> newWitMemLastList = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < witMemLastList.size(); i++) {
			Map<String, Object> witMemLastInfo = witMemLastList.get(i);
			// 会员卡号
			String memcode = (String) witMemLastInfo.get("memcode");
			int memberInfoId = 0;
			try {
				// 通过卡号取得会员ID
				Map<String, Object> memCardInfo = binBEMBARC02_Service.getMemCardInfo(witMemLastInfo);
				if (null != memCardInfo && !memCardInfo.isEmpty()) {
					if (null != memCardInfo.get("memberInfoId")) {
						memberInfoId = Integer.parseInt(memCardInfo.get("memberInfoId").toString());
					}
				}
			} catch (Exception e) {
				batchLoggerDTO.clear();
				batchLoggerDTO.setCode("EMB00025");
				batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
				batchLoggerDTO.addParam(memcode);
				cherryBatchLogger.BatchLogger(batchLoggerDTO);
				pointFailCount++;
				continue;
			}
			// 该卡号查询不到对应的会员ID
			if (0 == memberInfoId) {
				batchLoggerDTO.clear();
				batchLoggerDTO.setCode("EMB00015");
				batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
				batchLoggerDTO.addParam(memcode);
				cherryBatchLogger.BatchLogger(batchLoggerDTO);
				pointFailCount++;
			} else {
				// 组织信息ID
				witMemLastInfo.put("organizationInfoId", organizationInfoId);
				// 品牌信息Id
				witMemLastInfo.put("brandInfoId", brandInfoId);
				// 会员ID
				witMemLastInfo.put("memberInfoId", memberInfoId);
				if (isEqualPt) {
					// 可兑换积分
					witMemLastInfo.put("changablePoint", witMemLastInfo.get("curpoints"));
				}
				// 根据会员ID取得会员积分信息总数 
				int count = binBEMBARC02_Service.getMemPointCount(witMemLastInfo);
				if (count == 0) {
					batchLoggerDTO.clear();
					batchLoggerDTO.setCode("EMB00026");
					batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
					batchLoggerDTO.addParam(memcode);
					cherryBatchLogger.BatchLogger(batchLoggerDTO);
					pointFailCount++;
					continue;
				}
//				int ptStatus = 0;
//				Object status = witMemLastInfo.get("ptStatus");
//				if (null != status) {
//					ptStatus = Integer.parseInt(status.toString());
//				}
				// 非当前卡
				if (!binOLCM31_BL.isCurCard(memberInfoId, memcode)) {
					continue;
				}
				// 共通的参数设置(更新或者新增)
				commParamsForUp(witMemLastInfo);
				newWitMemLastList.add(witMemLastInfo);
			}
		}
		if (!newWitMemLastList.isEmpty()) {
			try {
				// 更新会员积分信息(批量处理)
				binBEMBARC02_Service.updateMemPointList(newWitMemLastList);
				binBEMBARC02_Service.manualCommit();
			} catch (Exception e) {
				try {
					binBEMBARC02_Service.manualRollback();
				} catch (Exception ex) {	
					
				}
				// 打印错误信息
				logger.error(e.getMessage(),e);
				pointFailCount += newWitMemLastList.size();
				flag = CherryBatchConstants.BATCH_WARNING;
			}
			// 会员积分信息的更新处理
			batchLoggerDTO.clear();
			batchLoggerDTO.setCode("IMB00009");
			batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_INFO);
			batchLoggerDTO.addParam(String.valueOf(pageNum));
			batchLoggerDTO.addParam(String.valueOf(size));
			batchLoggerDTO.addParam(String.valueOf(pointFailCount));
			cherryBatchLogger.BatchLogger(batchLoggerDTO);
			upfailCount += pointFailCount;
		}
	}
	
	/**
	 * 会员积分信息导入处理
	 * 
	 * @param witMemPointList 
	 * 			会员积分信息
	 * @param map 
	 * 			共通参数
	 * @param pageNum 
	 * 			批次
	 * @return 
	 * @throws CherryBatchException
	 * 
	 */
	public boolean imptWitMemPoint(List<Map<String, Object>> witMemPointList, 
			Map<String, Object> map, int pageNum) throws CherryBatchException {
		boolean nextFlag = true;
		// 组织信息ID
		int organizationInfoId = Integer.parseInt(
				map.get(CherryBatchConstants.ORGANIZATIONINFOID).toString());
		// 品牌信息Id
		int brandInfoId = Integer.parseInt(
				map.get(CherryBatchConstants.BRANDINFOID).toString());
		// 本批次处理总件数
		int size = witMemPointList.size();
		// 本次导入失败件数
		int pointFailCount = 0;
		// 本次处理的开始ID
		int startID = Integer.parseInt(witMemPointList.get(0).get("id").toString());
		// 本次处理的结束ID
		int endID = Integer.parseInt(witMemPointList.get(size - 1).get("id").toString());
		// 需要导入的记录里里列表
		List<Map<String, Object>> newWitMemPointList = new ArrayList<Map<String, Object>>();
		// 需要更新的记录里里列表
		List<Map<String, Object>> upMemPointList = new ArrayList<Map<String, Object>>();
		// 会员ID集合
		Map<String, Object> memberMap = new HashMap<String, Object>();
		for (int i = 0; i < witMemPointList.size(); i++) {
			Map<String, Object> witMemPointInfo = witMemPointList.get(i);
			// 会员卡号
			String memcode = (String) witMemPointInfo.get("memcode");
			int memberInfoId = 0;
			try {
				// 通过卡号取得会员ID
				Map<String, Object> memCardInfo = binBEMBARC02_Service.getMemCardInfo(witMemPointInfo);
				if (null != memCardInfo && !memCardInfo.isEmpty()) {
					if (null != memCardInfo.get("memberInfoId")) {
						memberInfoId = Integer.parseInt(memCardInfo.get("memberInfoId").toString());
					}
				}
			} catch (Exception e) {
				Map<String, Object> failRecord = new HashMap<String, Object>();
				// 记录ID
				failRecord.put("id", witMemPointInfo.get("id"));
				if ("1".equals(map.get("zflag"))) {
					failRecord.put("zflag", "1");
				}
				failRecordList.add(failRecord);
				batchLoggerDTO.clear();
				batchLoggerDTO.setCode("EMB00025");
				batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
				batchLoggerDTO.addParam(memcode);
				cherryBatchLogger.BatchLogger(batchLoggerDTO);
				pointFailCount++;
				continue;
			}
			// 该卡号查询不到对应的会员ID
			if (0 == memberInfoId) {
				Map<String, Object> failRecord = new HashMap<String, Object>();
				// 记录ID
				failRecord.put("id", witMemPointInfo.get("id"));
				if ("1".equals(map.get("zflag"))) {
					failRecord.put("zflag", "1");
				}
				failRecordList.add(failRecord);
				batchLoggerDTO.clear();
				batchLoggerDTO.setCode("EMB00015");
				batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
				batchLoggerDTO.addParam(memcode);
				cherryBatchLogger.BatchLogger(batchLoggerDTO);
				pointFailCount++;
			} else {
				// 组织信息ID
				witMemPointInfo.put("organizationInfoId", organizationInfoId);
				// 品牌信息Id
				witMemPointInfo.put("brandInfoId", brandInfoId);
				// 会员ID
				witMemPointInfo.put("memberInfoId", memberInfoId);
				if (isEqualPt) {
					// 可兑换积分
					witMemPointInfo.put("changablePoint", witMemPointInfo.get("curpoints"));
				}
				// 根据会员ID取得会员积分信息总数 
				int count = binBEMBARC02_Service.getMemPointCount(witMemPointInfo);
				if (count > 0) {
					// 有效区分
//					int ptStatus = 0;
//					Object status = witMemPointInfo.get("ptStatus");
//					if (null != status) {
//						ptStatus = Integer.parseInt(status.toString());
//					}
					// 验证是否是当前卡
					if (binOLCM31_BL.isCurCard(memberInfoId, memcode)) {
						// 共通的参数设置(更新或者新增)
						commParamsForUp(witMemPointInfo);
						upMemPointList.add(witMemPointInfo);
						continue;
					}
//					Map<String, Object> failRecord = new HashMap<String, Object>();
//					// 记录ID
//					failRecord.put("id", witMemPointInfo.get("id"));
//					failRecordList.add(failRecord);
					batchLoggerDTO.clear();
					batchLoggerDTO.setCode("EMB00022");
					batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
					batchLoggerDTO.addParam(memcode);
					cherryBatchLogger.BatchLogger(batchLoggerDTO);
//					pointFailCount++;
					continue;
				}
				// 会员ID
				String meberId = String.valueOf(memberInfoId);
				boolean addFlag = true;	
				if (memberMap.containsKey(meberId)) {
//					// 有效区分
//					int ptStatus = 0;
//					Object status = witMemPointInfo.get("ptStatus");
//					if (null != status) {
//						ptStatus = Integer.parseInt(status.toString());
//					}
					// 验证是否是当前卡
					if (binOLCM31_BL.isCurCard(memberInfoId, memcode)) {
						Map<String, Object> memPointInfo = (Map<String, Object>) memberMap.get(meberId);
						newWitMemPointList.remove(memPointInfo);
					} else {
						addFlag = false;
					}
				}
				if (addFlag) {
					// 共通的参数设置(更新或者新增)
					commParamsForUp(witMemPointInfo);
					newWitMemPointList.add(witMemPointInfo);
					memberMap.put(meberId, witMemPointInfo);
				}
			}
		}
		
		try {
			if (!newWitMemPointList.isEmpty()) {
				// 插入会员积分信息表
				binBEMBARC02_Service.addMemPointList(newWitMemPointList);
				binBEMBARC02_Service.manualCommit();
			}
			if (!upMemPointList.isEmpty()) {
				// 更新会员积分信息(批量处理)
				binBEMBARC02_Service.updateMemPointList(upMemPointList);
				binBEMBARC02_Service.manualCommit();
			}
			try {
				map.put("startID", startID);
				map.put("endID", endID);
				// 标记品牌会员积分信息表中已导入的数据
				binBEMBARC02_Service.updateWitMempoints(map);
				binBEMBARC02_Service.witManualCommit();
			} catch (Exception e) {
				nextFlag = false;
				try {
					binBEMBARC02_Service.witManualRollback();
				} catch (Exception ex) {	
					
				}
				// 标记品牌会员信息表已导入记录失败
				batchLoggerDTO.clear();
				batchLoggerDTO.setCode("EMB00017");
				batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
				cherryBatchLogger.BatchLogger(batchLoggerDTO);
				flag = CherryBatchConstants.BATCH_WARNING;
				throw e;
			}
		} catch (Exception e) {
			nextFlag = false;
			try {
				binBEMBARC02_Service.manualRollback();
			} catch (Exception ex) {	
				
			}
			// 打印错误信息
			logger.error(e.getMessage(),e);
			// 批量插入会员积分信息表失败
			batchLoggerDTO.clear();
			batchLoggerDTO.setCode("EMB00016");
			batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
			cherryBatchLogger.BatchLogger(batchLoggerDTO);
			pointFailCount += newWitMemPointList.size();
			flag = CherryBatchConstants.BATCH_WARNING;
		}
		
		// 本批次导入完成
		batchLoggerDTO.clear();
		batchLoggerDTO.setCode("IMB00003");
		batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_INFO);
		batchLoggerDTO.addParam(String.valueOf(pageNum));
		batchLoggerDTO.addParam(String.valueOf(size));
		batchLoggerDTO.addParam(String.valueOf(pointFailCount));
		cherryBatchLogger.BatchLogger(batchLoggerDTO);
		failCount += pointFailCount;
		return nextFlag;
	}
	
	/**
	 * 共通的参数设置(更新或者新增)
	 * 
	 * @param map 
	 * 			参数集合
	 * 
	 */
	private void commParamsForUp(Map<String, Object> map){
		// 作成者
		map.put(CherryBatchConstants.CREATEDBY, "BINBEMBARC02");
		// 作成程序名
		map.put(CherryBatchConstants.CREATEPGM, "BINBEMBARC02");
		// 更新者
		map.put(CherryBatchConstants.UPDATEDBY, "BINBEMBARC02");
		// 更新程序名
		map.put(CherryBatchConstants.UPDATEPGM, "BINBEMBARC02");
	}
}
