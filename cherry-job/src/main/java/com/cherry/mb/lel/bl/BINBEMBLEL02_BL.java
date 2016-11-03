/*	
 * @(#)BINBEMBLEL02_BL.java     1.0 2014/03/21		
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
package com.cherry.mb.lel.bl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.cmbussiness.interfaces.BINOLCM31_IF;
import com.cherry.cm.core.BatchExceptionDTO;
import com.cherry.cm.core.BatchLoggerDTO;
import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryBatchException;
import com.cherry.cm.core.CherryBatchLogger;
import com.cherry.cm.core.PropertiesUtil;
import com.cherry.cm.util.CherryBatchUtil;
import com.cherry.dr.cmbussiness.util.DateUtil;
import com.cherry.dr.cmbussiness.util.DoubleUtil;
import com.cherry.mb.lel.service.BINBEMBLEL02_Service;

/**
 * 推算等级变化明细(雅漾)处理BL
 * 
 * @author HUB
 * @version 1.0 2014/03/21
 */
public class BINBEMBLEL02_BL {
	
	private static Logger logger = LoggerFactory.getLogger(BINBEMBLEL02_BL.class.getName());
	
	/** 推算等级变化明细处理Service */
	@Resource
	private BINBEMBLEL02_Service binBEMBLEL02_Service;
	
	@Resource
	private BINOLCM31_IF binOLCM31_BL;
	
	/** 共通Batch Log处理*/
	private CherryBatchLogger cherryBatchLogger;
	
	/** 共通BatchLogger*/
	private BatchLoggerDTO batchLoggerDTO;
	
	/** BATCH处理标志 */
	private int flag = CherryBatchConstants.BATCH_SUCCESS;
	
	/** 普通会员 */
	private int leve1 = 0;
	
	/** VIP会员 */
	private int leve2 = 0;
	
	/** 铂金会员 */
	private int leve3 = 0;
	
	/** 总失败件数 */
	private int totalFail = 0;
	
	/**
	 * 计算某个时间点的会员等级处理
	 * 
	 * @param map 参数集合
	 * @return BATCH处理标志
	 */
	public int tran_CalcLevel(Map<String, Object> map) throws Exception {
		// 共通Batch Log处理
		cherryBatchLogger = new CherryBatchLogger(this.getClass());
		// 共通BatchLogger
		batchLoggerDTO = new BatchLoggerDTO();
		int count = 0;
		totalFail = 0;
		if ("0".equals(map.get("rangeKbn"))) {
			try {
				if ("1".equals(map.get("memFlag"))) {
					// 更新执行标识(仅新增)
					count = binBEMBLEL02_Service.updateBTExecFlagOnlyAdd(map);
				} else {
					// 更新执行标识
					count = binBEMBLEL02_Service.updateBTExecFlag(map);
				}
				if (count == 0) {
					// 该区间没有会员需要处理
					batchLoggerDTO.clear();
					batchLoggerDTO.setCode("IMB00017");
					batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_INFO);
					cherryBatchLogger.BatchLogger(batchLoggerDTO);
					return flag;
				}
				// 提交事务
				binBEMBLEL02_Service.manualCommit();
			} catch (Exception e) {
				try {
					// 事务回滚
					binBEMBLEL02_Service.manualRollback();
				} catch (Exception ex) {	
					
				}
				logger.error("update BatchExecFlag exception：" + e.getMessage(),e);
				throw e;
			}
		} else {
			// 取得需要处理的会员件数(手动设定的会员)
			count = binBEMBLEL02_Service.getMemExecCount(map);
			if (count == 0) {
				// 该区间没有会员需要处理
				batchLoggerDTO.clear();
				batchLoggerDTO.setCode("IMB00017");
				batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_INFO);
				cherryBatchLogger.BatchLogger(batchLoggerDTO);
				return flag;
			}
		}
		// 会员等级初始化设置失败
		if (!levelIdInit(map)) {
			BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO();
			batchExceptionDTO.setBatchName(this.getClass());
			batchExceptionDTO.setErrorCode("EMB00036");
			batchExceptionDTO.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
			throw new CherryBatchException(batchExceptionDTO);
		}
		// 等级日期
		String endDate = (String) map.get("endDate");
		// 截止日期
		String limitDate = endDate;
		// 类别为期末
		if ("2".equals(map.get("dateKbn"))) {
			limitDate = DateUtil.addDateByDays(DateUtil.DATE_PATTERN, limitDate, 1);
		}
		// 累计金额统计开始日期
		String startDate = DateUtil.addDateByMonth(DateUtil.DATE_PATTERN, limitDate, -12);
		map.put("limitDate", limitDate);
		map.put("startDate", startDate);
		// 分批取得会员信息，并处理
		batchLoggerDTO.clear();
		batchLoggerDTO.setCode("IMB00018");
		batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_INFO);
		batchLoggerDTO.addParam(PropertiesUtil.getMessage("PMB00010", null));
		batchLoggerDTO.addParam(String.valueOf(count));
		cherryBatchLogger.BatchLogger(batchLoggerDTO);
		// 数据查询长度
		int dataSize = CherryBatchConstants.DATE_SIZE;
		// 查询数据量
		map.put("COUNT", dataSize);
		int pageNum = 0;
		while (true) {
			pageNum++;
			// 查询会员信息
			List<Map<String, Object>> memList = binBEMBLEL02_Service.getMemList(map);
			// 会员信息不为空
			if (!CherryBatchUtil.isBlankList(memList)) {
				// 会员信息等级处理
				boolean nextFlag = calcLevel(memList, map, pageNum);
				// 有异常不继续进行
				if (!nextFlag) {
					break;
				}
				// 会员数据少于一次抽取的数量，即为最后一页，跳出循环
				if(memList.size() < dataSize) {
					break;
				}
			} else {
				break;
			}
		}
		// 全部等级计算已完成
		batchLoggerDTO.clear();
		batchLoggerDTO.setCode("IMB00019");
		batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_INFO);
		batchLoggerDTO.addParam(PropertiesUtil.getMessage("PMB00011", null));
		batchLoggerDTO.addParam(String.valueOf(count));
		batchLoggerDTO.addParam(String.valueOf(totalFail));
		cherryBatchLogger.BatchLogger(batchLoggerDTO);
		return flag;
	}
	
	/**
	 * 会员等级初始化设置
	 * 
	 * @param map 
	 * 			共通参数
	 * @return 
	 * @throws CherryBatchException
	 * 
	 */
	private boolean levelIdInit (Map<String, Object> map) {
		// 取得等级列表
		List<Map<String, Object>> levelList = binBEMBLEL02_Service.getLevelList(map);
		if (null != levelList) {
			for (int i = 0; i < levelList.size(); i++) {
				Map<String, Object> levelInfo = levelList.get(i);
				// 等级ID
				int levelId = Integer.parseInt(String.valueOf(levelInfo.get("memberLevelId")));
				// 等级代号
				String levelCode = (String) levelInfo.get("levelCode");
				if ("WMLC002".equals(levelCode)) {
					// 普通会员
					leve1 = levelId;
				} else if ("WMLC003".equals(levelCode)){
					// VIP会员
					leve2 = levelId;
				} else if ("WMLC004".equals(levelCode)){
					// 铂金会员
					leve3 = levelId;
				}
				if (i != levelList.size() - 1) {
					levelList.remove(i);
					i--;
				}
			}
		}
		if (leve1 == 0 || leve2 == 0 || leve3 == 0) {
			return false;
		}
		return true;
	}
	
	/**
	 * 会员等级处理
	 * 
	 * @param memList 
	 * 			会员信息
	 * @param map 
	 * 			共通参数
	 * @param pageNum 
	 * 			批次
	 * @return 
	 * @throws CherryBatchException
	 * 
	 */
	public boolean calcLevel(List<Map<String, Object>> memList, 
			Map<String, Object> map, int pageNum) throws Exception {
		boolean nextFlag = true;
		// 本批次处理总件数
		int size = memList.size();
		// 起始会员ID
		int startId = Integer.parseInt(String.valueOf(memList.get(0).get("memberInfoId")));
		// 结束会员ID
		int endId = Integer.parseInt(String.valueOf(memList.get(size - 1).get("memberInfoId")));
		int failCount = 0;
		try {
			// 去除执行标识
			binBEMBLEL02_Service.clearExecFlag(memList);
			binBEMBLEL02_Service.manualCommit();
		} catch (Exception e) {
			nextFlag = false;
			try {
				binBEMBLEL02_Service.manualRollback();
			} catch (Exception ex) {	
				
			}
			// 标记品牌会员信息表已导入记录失败
			batchLoggerDTO.clear();
			batchLoggerDTO.setCode("EMB00038");
			batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
			cherryBatchLogger.BatchLogger(batchLoggerDTO);
			flag = CherryBatchConstants.BATCH_WARNING;
			throw e;
		}
		try {
			// 新增列表
			List<Map<String, Object>> addList = new ArrayList<Map<String, Object>>();
			// 删除列表
			List<Map<String, Object>> delList = null;
			map.put("startId", startId);
			map.put("endId", endId);
			// 查询区间内会员等级履历数量
			int recordCount = binBEMBLEL02_Service.getLevelHistCount(map);
			if (recordCount > 0) {
				delList = new ArrayList<Map<String, Object>>();
			}
			Map<String, Object> searchMap = new HashMap<String, Object>();
			for (Map<String, Object> memInfo : memList) {
				searchMap.put("memberInfoId", memInfo.get("memberInfoId"));
				searchMap.put("limitDate", map.get("limitDate"));
				searchMap.put("startDate", map.get("startDate"));
				// 推算等级变化明细处理
				memInfo.put("memLevel", getLevelByTime(searchMap, 0));
				// 等级时间点
				memInfo.put("levelDate", map.get("endDate"));
				// 时间区分
				memInfo.put("dateFlag", map.get("dateKbn"));
				if (null == memInfo.get("memCode")) {
					//  取得会员卡号
					memInfo.put("memCode", binOLCM31_BL.getMemCard(memInfo));
				}
				// 共通的参数设置
				commParams(memInfo);
				addList.add(memInfo);
				if (null != delList) {
					delList.add(memInfo);
				}
			}
			if (!addList.isEmpty()) {
				if (null != delList && !delList.isEmpty()) {
					// 删除会员等级履历
					binBEMBLEL02_Service.delLevelHistory(delList);
				}
				// 插入会员等级调整履历表
				binBEMBLEL02_Service.addLevelHistoryInfo(addList);
				binBEMBLEL02_Service.manualCommit();
			}
		} catch (Exception e) {
			try {
				binBEMBLEL02_Service.manualRollback();
			} catch (Exception ex) {	
				
			}
			failCount = size;
			totalFail += size;
			// 新增会员等级履历历史记录失败
			batchLoggerDTO.clear();
			batchLoggerDTO.setCode("EMB00037");
			batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
			batchLoggerDTO.addParam(String.valueOf(startId));
			batchLoggerDTO.addParam(String.valueOf(endId));
			cherryBatchLogger.BatchLogger(batchLoggerDTO);
			logger.error(e.getMessage(),e);
			flag = CherryBatchConstants.BATCH_WARNING;
		}
		// 本批次处理完成
		batchLoggerDTO.clear();
		batchLoggerDTO.setCode("IMB00020");
		batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_INFO);
		batchLoggerDTO.addParam(String.valueOf(pageNum));
		batchLoggerDTO.addParam(String.valueOf(size));
		batchLoggerDTO.addParam(String.valueOf(failCount));
		cherryBatchLogger.BatchLogger(batchLoggerDTO);
		return nextFlag;
	}
	
	/**
	 * 推算等级变化明细处理
	 * 
	 * @param map 参数集合
	 * @param flag 区分 0: 不返回累计金额  1: 返回累计金额
	 * 
	 * @return Integer 当时的等级
	 */
	private Integer getLevelByTime(Map<String, Object> map, int flag) {
		// 取得等级列表
		Object ttlAmountObj = binBEMBLEL02_Service.getTtlAmount(map);
		if (1 == flag) {
			map.put("TTLAMOUNT", ttlAmountObj);
		}
		if (null != ttlAmountObj) {
			double ttlAmount = Double.parseDouble(ttlAmountObj.toString());
			if (ttlAmount < 2000) {
				return leve1;
			} else if (ttlAmount < 5000) {
				return leve2;
			} else {
				return leve3;
			}
		}
		return null;
	}
	
	/**
	 * 推算等级变化明细处理
	 * 
	 * @param map 参数集合
	 * @return BATCH处理标志
	 */
	public int tran_LevelDetail(Map<String, Object> map) throws Exception {
		// 共通Batch Log处理
		cherryBatchLogger = new CherryBatchLogger(this.getClass());
		// 共通BatchLogger
		batchLoggerDTO = new BatchLoggerDTO();
		int count = 0;
		totalFail = 0;
		if ("0".equals(map.get("rangeKbn"))) {
			try {
				if ("1".equals(map.get("memFlag"))) {
					// 更新执行标识(仅新增)
					count = binBEMBLEL02_Service.updateFlagDetailOnlyAdd(map);
				} else {
					// 更新执行标识
					count = binBEMBLEL02_Service.updateBTExecFlag(map);
				}
				if (count == 0) {
					// 该区间没有会员需要处理
					batchLoggerDTO.clear();
					batchLoggerDTO.setCode("IMB00017");
					batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_INFO);
					cherryBatchLogger.BatchLogger(batchLoggerDTO);
					return flag;
				}
				// 提交事务
				binBEMBLEL02_Service.manualCommit();
			} catch (Exception e) {
				try {
					// 事务回滚
					binBEMBLEL02_Service.manualRollback();
				} catch (Exception ex) {	
					
				}
				logger.error("update BatchExecFlag exception：" + e.getMessage(),e);
				throw e;
			}
		} else {
			// 取得需要处理的会员件数(手动设定的会员)
			count = binBEMBLEL02_Service.getMemExecCount(map);
			if (count == 0) {
				// 该区间没有会员需要处理
				batchLoggerDTO.clear();
				batchLoggerDTO.setCode("IMB00017");
				batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_INFO);
				cherryBatchLogger.BatchLogger(batchLoggerDTO);
				return flag;
			}
		}
		// 会员等级初始化设置失败
		if (!levelIdInit(map)) {
			BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO();
			batchExceptionDTO.setBatchName(this.getClass());
			batchExceptionDTO.setErrorCode("EMB00036");
			batchExceptionDTO.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
			throw new CherryBatchException(batchExceptionDTO);
		}
		// 期末
		String endDate = (String) map.get("endDate");
		// 截止日期
		String limitDate = DateUtil.addDateByDays(DateUtil.DATE_PATTERN, endDate, 1);
		// 累计金额统计开始日期
		String startDate = DateUtil.addDateByMonth(DateUtil.DATE_PATTERN, limitDate, -12);
		map.put("limitDate", limitDate);
		map.put("startDate", startDate);
		
		// 期初
		String beginDate = (String) map.get("beginDate");
		// 累计金额统计开始日期
		String beginStartDate = DateUtil.addDateByMonth(DateUtil.DATE_PATTERN, beginDate, -12);
		map.put("beginLimitDate", beginDate);
		map.put("beginStartDate", beginStartDate);
		// 分批取得会员信息，并处理
		batchLoggerDTO.clear();
		batchLoggerDTO.setCode("IMB00018");
		batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_INFO);
		batchLoggerDTO.addParam(PropertiesUtil.getMessage("PMB00010", null));
		batchLoggerDTO.addParam(String.valueOf(count));
		cherryBatchLogger.BatchLogger(batchLoggerDTO);
		// 数据查询长度
		int dataSize = CherryBatchConstants.DATE_SIZE;
		// 查询数据量
		map.put("COUNT", dataSize);
		int pageNum = 0;
		while (true) {
			pageNum++;
			// 查询会员信息
			List<Map<String, Object>> memList = binBEMBLEL02_Service.getMemList(map);
			// 会员信息不为空
			if (!CherryBatchUtil.isBlankList(memList)) {
				// 会员信息等级处理
				boolean nextFlag = calcDetail(memList, map, pageNum);
				// 有异常不继续进行
				if (!nextFlag) {
					break;
				}
				// 会员数据少于一次抽取的数量，即为最后一页，跳出循环
				if(memList.size() < dataSize) {
					break;
				}
			} else {
				break;
			}
		}
		// 全部等级计算已完成
		batchLoggerDTO.clear();
		batchLoggerDTO.setCode("IMB00019");
		batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_INFO);
		batchLoggerDTO.addParam(PropertiesUtil.getMessage("PMB00012", null));
		batchLoggerDTO.addParam(String.valueOf(count));
		batchLoggerDTO.addParam(String.valueOf(totalFail));
		cherryBatchLogger.BatchLogger(batchLoggerDTO);
		return flag;
	}
	
	/**
	 * 会员等级变化明细处理
	 * 
	 * @param memList 
	 * 			会员信息
	 * @param map 
	 * 			共通参数
	 * @param pageNum 
	 * 			批次
	 * @return 
	 * @throws CherryBatchException
	 * 
	 */
	public boolean calcDetail(List<Map<String, Object>> memList, 
			Map<String, Object> map, int pageNum) throws Exception {
		boolean nextFlag = true;
		// 本批次处理总件数
		int size = memList.size();
		// 起始会员ID
		int startId = Integer.parseInt(String.valueOf(memList.get(0).get("memberInfoId")));
		// 结束会员ID
		int endId = Integer.parseInt(String.valueOf(memList.get(size - 1).get("memberInfoId")));
		int failCount = 0;
		try {
			// 去除执行标识
			binBEMBLEL02_Service.clearExecFlag(memList);
			binBEMBLEL02_Service.manualCommit();
		} catch (Exception e) {
			nextFlag = false;
			try {
				binBEMBLEL02_Service.manualRollback();
			} catch (Exception ex) {	
				
			}
			// 标记品牌会员信息表已导入记录失败
			batchLoggerDTO.clear();
			batchLoggerDTO.setCode("EMB00038");
			batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
			cherryBatchLogger.BatchLogger(batchLoggerDTO);
			flag = CherryBatchConstants.BATCH_WARNING;
			throw e;
		}
		try {
			// 新增列表(等级历史履历)
			List<Map<String, Object>> addList = new ArrayList<Map<String, Object>>();
			// 新增列表(等级变化明细)
			List<Map<String, Object>> addChangeList = new ArrayList<Map<String, Object>>();
			// 删除列表(等级历史履历)
			List<Map<String, Object>> delList = null;
			// 删除列表(等级变化明细)
			List<Map<String, Object>> delChangeList = null;
			map.put("startId", startId);
			map.put("endId", endId);
			// 查询区间内会员等级履历数量
			int recordCount = binBEMBLEL02_Service.getLevelChangeCount(map);
			boolean delFlag = false;
			if (recordCount > 0) {
				delList = new ArrayList<Map<String, Object>>();
				delChangeList = new ArrayList<Map<String, Object>>();
				delFlag = true;
			}
			for (Map<String, Object> memInfo : memList) {
				boolean beginFlag = true;
				boolean endFlag = true;
				String beginDate = (String) map.get("beginDate");
				String endDate = (String) map.get("endDate");
				memInfo.put("beginDate", beginDate);
				memInfo.put("endDate", endDate);
				// 期初等级
				Integer beginLevel = null;
				// 期末等级
				Integer endLevel = null;
				if (!delFlag) {
					// 等级时间点
					memInfo.put("levelDate", beginDate);
					// 时间区分
					memInfo.put("dateFlag", 1);
					// 取得会员历史等级
					Map<String, Object> levelInfo = binBEMBLEL02_Service.getHistoryLevelInfo(memInfo);
					if (null != levelInfo && !levelInfo.isEmpty()) {
						beginLevel = (Integer) levelInfo.get("memLevel");
						beginFlag = false; 
					}
					// 等级时间点
					memInfo.put("levelDate", endDate);
					// 时间区分
					memInfo.put("dateFlag", 2);
					// 取得会员历史等级
					levelInfo = binBEMBLEL02_Service.getHistoryLevelInfo(memInfo);
					if (null != levelInfo && !levelInfo.isEmpty()) {
						endLevel = (Integer) levelInfo.get("memLevel");
						endFlag = false; 
					}
				}
				if (beginFlag) {
					// 期初时间点
					memInfo.put("limitDate", map.get("beginLimitDate"));
					memInfo.put("startDate", map.get("beginStartDate"));
					// 期初等级
					beginLevel = getLevelByTime(memInfo, 0);
				}
				if (endFlag) {
					// 期末时间点
					memInfo.put("limitDate", map.get("limitDate"));
					memInfo.put("startDate", map.get("startDate"));
					// 期末等级
					endLevel = getLevelByTime(memInfo, 1);
				}
				// 是否需要生成明细
				boolean detailFlag = true;
				int changeType = 0;
				if (null == endLevel) {
					detailFlag = false;
				} else {
					int el = endLevel;
					int bl = 0;
					if (null != beginLevel) {
						bl = beginLevel;
					}
					if (el == bl) {
						detailFlag = false;
					} else if (el > bl) {
						// 升降级区分 : 升级
						changeType = 1;
					} else {
						// 升降级区分 : 降级
						changeType = 2;
					}
				}
				if (detailFlag || beginFlag || endFlag) {
					if (null == memInfo.get("memCode")) {
						//  取得会员卡号
						memInfo.put("memCode", binOLCM31_BL.getMemCard(memInfo));
					}
					// 共通的参数设置
					commParams(memInfo);
					if (detailFlag) {
						String limitDate = (String) map.get("limitDate");
						String startDate = (String) map.get("startDate");
						String beginStartDate = (String) map.get("beginStartDate");
						memInfo.put("changeType", changeType);
						Object ttlAmountObj = null;
						if (!memInfo.containsKey("TTLAMOUNT")) {
							// 期末时间点
							memInfo.put("limitDate", limitDate);
							memInfo.put("startDate", startDate);
							ttlAmountObj = binBEMBLEL02_Service.getTtlAmount(memInfo);
						} else {
							ttlAmountObj = memInfo.get("TTLAMOUNT");
						}
						if (null != ttlAmountObj) {
							// 截止时间为期末的一年内累计金额
							double ttlAmount = Double.parseDouble(ttlAmountObj.toString());
							// 期末时间点
							memInfo.put("limitDate", limitDate);
							memInfo.put("startDate", startDate);
							// 取得截止时间为期末的消费记录
							List<Map<String, Object>> endTicketList = binBEMBLEL02_Service.getBTBuyList(memInfo);
							// 期初时间点
							memInfo.put("limitDate", startDate);
							memInfo.put("startDate", beginStartDate);
							// 取得截止时间为期初的消费记录
							List<Map<String, Object>> beginTicketList = binBEMBLEL02_Service.getBTBuyList(memInfo);
							String changeDate = null;
							if ((null == endTicketList || endTicketList.isEmpty()) &&
									null != beginTicketList && !beginTicketList.isEmpty()) {
								for (int i = 0; i < beginTicketList.size(); i++) {
									Map<String, Object> beginTicketMap = beginTicketList.get(i);
									// 单据金额
									double amount = Double.parseDouble(beginTicketMap.get("amount").toString());
									ttlAmount = DoubleUtil.add(ttlAmount, amount);
									if (!checkLevelByAmount(endLevel, ttlAmount)) {
										changeDate = (String) beginTicketMap.get("saleDate");
										changeDate = DateUtil.addDateByMonth(DateUtil.DATE_PATTERN, changeDate, 12) + " 00:00:00";
										break;
									}
								}
							} else {
								if (null != beginTicketList && !beginTicketList.isEmpty()) {
									for (Map<String, Object> beginTicketMap : beginTicketList) {
										// 单据日期
										String saleDate = (String) beginTicketMap.get("saleDate");
										saleDate = DateUtil.addDateByMonth(DateUtil.DATE_PATTERN, saleDate, 12);
										beginTicketMap.put("saleTime", saleDate + " 00:00:00");
										beginTicketMap.put("BEKBN", "1");
									}
								}
								if (null != endTicketList && !endTicketList.isEmpty()) {
									if (null != beginTicketList && !beginTicketList.isEmpty()) {
										endTicketList.addAll(beginTicketList);
										// 按时间倒序排列
										dateSort(endTicketList);
									}
									for (int i = 0; i < endTicketList.size(); i++) {
										Map<String, Object> endTicketMap = endTicketList.get(i);
										// 单据金额
										double amount = Double.parseDouble(endTicketMap.get("amount").toString());
										if ("1".equals(endTicketMap.get("BEKBN"))) {
											ttlAmount = DoubleUtil.add(ttlAmount, amount);
										} else {
											ttlAmount = DoubleUtil.sub(ttlAmount, amount);
										}
										if (!checkLevelByAmount(endLevel, ttlAmount) 
												|| i == endTicketList.size() - 1) {
											changeDate = (String) endTicketMap.get("saleTime");
											break;
										}
									}
								}
							}
							if (null != changeDate) {
								memInfo.put("changeDate", changeDate);
								memInfo.put("beginLevel", beginLevel);
								memInfo.put("endLevel", endLevel);
								addChangeList.add(memInfo);
							}
						}
					}
					if (beginFlag) {
						Map<String, Object> beginMap = new HashMap<String, Object>();
						beginMap.putAll(memInfo);
						// 等级时间点
						beginMap.put("levelDate", beginDate);
						// 时间区分
						beginMap.put("dateFlag", 1);
						// 等级
						beginMap.put("memLevel", beginLevel);
						addList.add(beginMap);
					}
					if (endFlag) {
						// 等级时间点
						memInfo.put("levelDate", endDate);
						// 时间区分
						memInfo.put("dateFlag", 2);
						// 等级
						memInfo.put("memLevel", endLevel);
						addList.add(memInfo);
					}
				}
				if (delFlag) {
					Map<String, Object> beginDelMap = new HashMap<String, Object>();
					beginDelMap.putAll(memInfo);
					// 等级时间点
					beginDelMap.put("levelDate", beginDate);
					// 时间区分
					beginDelMap.put("dateFlag", 1);
					delList.add(beginDelMap);
					delList.add(memInfo);
					delChangeList.add(memInfo);
				}
			}
			boolean cmtFlag = false;
			if (null != delList && !delList.isEmpty()) {
				// 删除会员等级履历
				binBEMBLEL02_Service.delLevelHistory(delList);
				cmtFlag = true;
			}
			if (!addList.isEmpty()) {
				// 插入会员等级调整履历表
				binBEMBLEL02_Service.addLevelHistoryInfo(addList);
				cmtFlag = true;
			}
			if (null != delChangeList && !delChangeList.isEmpty()) {
				// 删除会员等级变化履历
				binBEMBLEL02_Service.delLevelChange(delChangeList);
				cmtFlag = true;
			}
			if (!addChangeList.isEmpty()) {
				// 插入会员等级变化年度统计表
				binBEMBLEL02_Service.addLevelChangeReport(addChangeList);
				cmtFlag = true;
			}
			if (cmtFlag) {
				binBEMBLEL02_Service.manualCommit();
			}
		} catch (Exception e) {
			try {
				binBEMBLEL02_Service.manualRollback();
			} catch (Exception ex) {	
				
			}
			failCount = size;
			totalFail += size;
			// 新增会员等级履历历史记录失败
			batchLoggerDTO.clear();
			batchLoggerDTO.setCode("EMB00039");
			batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
			batchLoggerDTO.addParam(String.valueOf(startId));
			batchLoggerDTO.addParam(String.valueOf(endId));
			cherryBatchLogger.BatchLogger(batchLoggerDTO);
			logger.error(e.getMessage(),e);
			flag = CherryBatchConstants.BATCH_WARNING;
		}
		// 本批次处理完成
		batchLoggerDTO.clear();
		batchLoggerDTO.setCode("IMB00020");
		batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_INFO);
		batchLoggerDTO.addParam(String.valueOf(pageNum));
		batchLoggerDTO.addParam(String.valueOf(size));
		batchLoggerDTO.addParam(String.valueOf(failCount));
		cherryBatchLogger.BatchLogger(batchLoggerDTO);
		return nextFlag;
	}
	/**
	 * 检查是否等级是否正确
	 * 
	 * @param map 参数集合
	 * @return 检查结果 true: 正确 false: 不正确
	 */
	private boolean checkLevelByAmount(Integer level, double amount) throws Exception {
		if ((level == null || level == leve1) && amount < 2000 ||
				level == leve2 && amount >= 2000 && amount < 5000 ||
				level == leve3 && amount > 5000) {
			return true;
		}
		return false;
	}
	
	/**
	 * 检查是否需要继续执行
	 * 
	 * @param map 参数集合
	 * @return 检查结果 true: 继续执行 false: 中断执行
	 */
	public boolean checkExec(Map<String, Object> map) throws Exception {
		// 取得BATCH执行记录数
		int count = binBEMBLEL02_Service.getBTExecCount(map);
		if (count > 0) {
			return false;
		}
		return true;
	}
	
	/**
	 * 共通的参数设置
	 * 
	 * @param map 
	 * 			参数集合
	 * 
	 */
	private void commParams(Map<String, Object> map){
		// 作成者
		map.put(CherryBatchConstants.CREATEDBY, "BINBEMBLEL02");
		// 作成程序名
		map.put(CherryBatchConstants.CREATEPGM, "BINBEMBLEL02");
		// 更新者
		map.put(CherryBatchConstants.UPDATEDBY, "BINBEMBLEL02");
		// 更新程序名
		map.put(CherryBatchConstants.UPDATEPGM, "BINBEMBLEL02");
	}
	
	/**
	 * 
	 * 按积分清零日期进行排序(降序)
	 * 
	 * @param list 需要排序的list
	 * 
	 */
	private void dateSort(List<Map<String, Object>> list) {
		Collections.sort(list, new Comparator<Map<String, Object>>() {
		 	public int compare(Map<String, Object> detail1, Map<String, Object> detail2) {
		 		// 单据日期1
            	String date1 = (String) detail1.get("saleTime");
            	// 单据日期2
            	String date2 = (String) detail2.get("saleTime");
            	if(compTime(date1, date2) >= 0) {
            		return -1;
            	} else {
            		return 1;
            	}
            }
		});
	}
	
	/**
	 * 比较第一个参数时间和第二个参数时间的大小
	 * 
	 * @param value1
	 *            时间1
	 * @param value2
	 *            时间2
	 * @return int 小于0 : value1 小于 value2  
	 * 				0 : value1 等于value2
	 * 				大于0 : value1 大于value2
	 */
	private int compTime(String value1, String value2) {
		Calendar cal1 = Calendar.getInstance();
		Calendar cal2 = Calendar.getInstance();
		cal1.setTime(DateUtil.coverString2Date(value1, DateUtil.DATETIME_PATTERN));
		cal2.setTime(DateUtil.coverString2Date(value2, DateUtil.DATETIME_PATTERN));
		return cal1.compareTo(cal2);
	}
}
