/*	
 * @(#)BINBEMBLEL03_BL.java     1.0 2014/03/21		
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

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.cmbussiness.interfaces.BINOLCM31_IF;
import com.cherry.cm.core.BatchExceptionDTO;
import com.cherry.cm.core.BatchLoggerDTO;
import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryBatchException;
import com.cherry.cm.core.CherryBatchLogger;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.PropertiesUtil;
import com.cherry.cm.util.CherryBatchUtil;
import com.cherry.dr.cmbussiness.util.DateUtil;
import com.cherry.dr.cmbussiness.util.DoubleUtil;
import com.cherry.mb.lel.service.BINBEMBLEL02_Service;
import com.cherry.mb.lel.service.BINBEMBLEL03_Service;

/**
 * 会员等级计算及报表导出 BL
 * 
 * @author HUB
 * @version 1.0 2014/03/21
 */
public class BINBEMBLEL03_BL {

	private static Logger logger = LoggerFactory.getLogger(BINBEMBLEL03_BL.class.getName());
	
	/** 推算等级变化明细处理Service */
	@Resource
	private BINBEMBLEL03_Service binBEMBLEL03_Service;
	
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
	
	private static final String levelFromTime = "2014-01-01 00:00:00";
	
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
	 * 从老后台导入会员等级及有效期处理
	 * 
	 * @param map 参数集合
	 * @return BATCH处理标志
	 */
	public int tran_LevelWitImpt(Map<String, Object> map) throws Exception {
		// 共通Batch Log处理
		cherryBatchLogger = new CherryBatchLogger(this.getClass());
		// 共通BatchLogger
		batchLoggerDTO = new BatchLoggerDTO();
		int count = 0;
		totalFail = 0;
		if ("0".equals(map.get("rangeKbn"))) {
			try {
				// 更新执行标识
				count = binBEMBLEL03_Service.updateExecFlag04(map);
				if (count == 0) {
					// 该区间没有会员需要处理
					batchLoggerDTO.clear();
					batchLoggerDTO.setCode("IMB00017");
					batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_INFO);
					cherryBatchLogger.BatchLogger(batchLoggerDTO);
					return flag;
				}
				// 提交事务
				binBEMBLEL03_Service.manualCommit();
			} catch (Exception e) {
				try {
					// 事务回滚
					binBEMBLEL03_Service.manualRollback();
				} catch (Exception ex) {	
					
				}
				logger.error("update BatchExecFlag exception：" + e.getMessage(),e);
				throw e;
			}
		} else {
			// 取得需要处理的会员件数(手动设定的会员)
			count = binBEMBLEL03_Service.getMemExecCount(map);
			if (count == 0) {
				// 该区间没有会员需要处理
				batchLoggerDTO.clear();
				batchLoggerDTO.setCode("IMB00017");
				batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_INFO);
				cherryBatchLogger.BatchLogger(batchLoggerDTO);
				return flag;
			}
		}
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
			List<Map<String, Object>> memList = binBEMBLEL03_Service.getImptLevelList(map);
			// 会员信息不为空
			if (!CherryBatchUtil.isBlankList(memList)) {
				// 会员信息等级处理
				boolean nextFlag = witImptLevel(memList, map, pageNum);
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
		batchLoggerDTO.addParam(PropertiesUtil.getMessage("PMB00017", null));
		batchLoggerDTO.addParam(String.valueOf(count));
		batchLoggerDTO.addParam(String.valueOf(totalFail));
		cherryBatchLogger.BatchLogger(batchLoggerDTO);
		return flag;
	}
	
	/**
	 * 导入会员当前等级处理
	 * 
	 * @param map 参数集合
	 * @return BATCH处理标志
	 */
	public int tran_LevelImpt(Map<String, Object> map) throws Exception {
		// 共通Batch Log处理
		cherryBatchLogger = new CherryBatchLogger(this.getClass());
		// 共通BatchLogger
		batchLoggerDTO = new BatchLoggerDTO();
		int count = 0;
		totalFail = 0;
		if ("0".equals(map.get("rangeKbn"))) {
			try {
				// 更新执行标识
				count = binBEMBLEL03_Service.updateBTExecFlag(map);
				if (count == 0) {
					// 该区间没有会员需要处理
					batchLoggerDTO.clear();
					batchLoggerDTO.setCode("IMB00017");
					batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_INFO);
					cherryBatchLogger.BatchLogger(batchLoggerDTO);
					return flag;
				}
				// 提交事务
				binBEMBLEL03_Service.manualCommit();
			} catch (Exception e) {
				try {
					// 事务回滚
					binBEMBLEL03_Service.manualRollback();
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
		// 初始导入日期
		String initialDate = (String) map.get("initialDate");
		map.put("initialTime", initialDate + " 23:59:59");
		while (true) {
			pageNum++;
			// 查询会员信息
			List<Map<String, Object>> memList = binBEMBLEL03_Service.getMemList(map);
			// 会员信息不为空
			if (!CherryBatchUtil.isBlankList(memList)) {
				// 会员信息等级处理
				boolean nextFlag = imptLevel(memList, map, pageNum);
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
		batchLoggerDTO.addParam(PropertiesUtil.getMessage("PMB00013", null));
		batchLoggerDTO.addParam(String.valueOf(count));
		batchLoggerDTO.addParam(String.valueOf(totalFail));
		cherryBatchLogger.BatchLogger(batchLoggerDTO);
		return flag;
	}
	
	/**
	 * 根据销售计算等级处理
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
				// 更新执行标识
				count = binBEMBLEL03_Service.updateExecFlag01(map);
				if (count == 0) {
					// 该区间没有会员需要处理
					batchLoggerDTO.clear();
					batchLoggerDTO.setCode("IMB00017");
					batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_INFO);
					cherryBatchLogger.BatchLogger(batchLoggerDTO);
					return flag;
				}
				// 提交事务
				binBEMBLEL03_Service.manualCommit();
			} catch (Exception e) {
				try {
					// 事务回滚
					binBEMBLEL03_Service.manualRollback();
				} catch (Exception ex) {	
					
				}
				logger.error("update BatchExecFlag exception：" + e.getMessage(),e);
				throw e;
			}
		} else {
			// 取得会员当前等级表需要处理的会员件数(手动设定的会员)
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
		// 业务日期
//		String busDate = binBEMBLEL03_Service.getBussinessDate(map);
//		map.put("busDate", busDate);
		int pageNum = 0;
		while (true) {
			pageNum++;
			// 查询会员信息
			List<Map<String, Object>> memList = binBEMBLEL03_Service.getCurLevelList(map);
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
		batchLoggerDTO.addParam(PropertiesUtil.getMessage("PMB00014", null));
		batchLoggerDTO.addParam(String.valueOf(count));
		batchLoggerDTO.addParam(String.valueOf(totalFail));
		cherryBatchLogger.BatchLogger(batchLoggerDTO);
		return flag;
	}
	
	/**
	 * 会员等级重算处理
	 * 
	 * @param map 参数集合
	 * @return BATCH处理标志
	 */
	public int tran_RecalcLevel(Map<String, Object> map) throws Exception {
		// 共通Batch Log处理
		cherryBatchLogger = new CherryBatchLogger(this.getClass());
		// 共通BatchLogger
		batchLoggerDTO = new BatchLoggerDTO();
		int count = 0;
		totalFail = 0;
		if ("0".equals(map.get("rangeKbn"))) {
			try {
				// 更新执行标识
				count = binBEMBLEL03_Service.updateExecFlag03(map);
				if (count == 0) {
					// 该区间没有会员需要处理
					batchLoggerDTO.clear();
					batchLoggerDTO.setCode("IMB00017");
					batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_INFO);
					cherryBatchLogger.BatchLogger(batchLoggerDTO);
					return flag;
				}
				// 提交事务
				binBEMBLEL03_Service.manualCommit();
			} catch (Exception e) {
				try {
					// 事务回滚
					binBEMBLEL03_Service.manualRollback();
				} catch (Exception ex) {	
					
				}
				logger.error("update BatchExecFlag exception：" + e.getMessage(),e);
				throw e;
			}
		} else {
			// 取得会员当前等级表需要处理的会员件数(手动设定的会员)
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
		// 业务日期
//		String busDate = binBEMBLEL03_Service.getBussinessDate(map);
//		map.put("busDate", busDate);
		int pageNum = 0;
		while (true) {
			pageNum++;
			// 查询会员信息
			List<Map<String, Object>> memList = binBEMBLEL03_Service.getCurLevelList(map);
			// 会员信息不为空
			if (!CherryBatchUtil.isBlankList(memList)) {
				// 会员信息等级处理
				boolean nextFlag = recalcDetail(memList, map, pageNum);
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
		batchLoggerDTO.addParam(PropertiesUtil.getMessage("PMB00016", null));
		batchLoggerDTO.addParam(String.valueOf(count));
		batchLoggerDTO.addParam(String.valueOf(totalFail));
		cherryBatchLogger.BatchLogger(batchLoggerDTO);
		return flag;
	}
	
	/**
	 * 记录某个时间点的会员等级处理
	 * 
	 * @param map 参数集合
	 * @return BATCH处理标志
	 */
	public int tran_RecordLevel(Map<String, Object> map) throws Exception {
		// 共通Batch Log处理
		cherryBatchLogger = new CherryBatchLogger(this.getClass());
		// 共通BatchLogger
		batchLoggerDTO = new BatchLoggerDTO();
		int count = 0;
		totalFail = 0;
		if ("0".equals(map.get("rangeKbn"))) {
			try {
				// 等级日期
				String levelDate = (String) map.get("levelDate");
				// 日期区分
				String dateKbn = (String) map.get("dateKbn");
				map.put("joinFlag", "1");
				// 期初
				if ("1".equals(dateKbn)) {
					// 入会日期限制
					map.put("joinDateLimit", levelDate);
					// 期末
				} else {
					// 入会日期限制
					map.put("joinDateLimit", DateUtil.addDateByDays(DateUtil.DATE_PATTERN, levelDate, 1));
				}
				// 更新执行标识
				count = binBEMBLEL03_Service.updateExecFlag02(map);
				if (count == 0) {
					// 该区间没有会员需要处理
					batchLoggerDTO.clear();
					batchLoggerDTO.setCode("IMB00017");
					batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_INFO);
					cherryBatchLogger.BatchLogger(batchLoggerDTO);
					return flag;
				}
				// 提交事务
				binBEMBLEL03_Service.manualCommit();
			} catch (Exception e) {
				try {
					// 事务回滚
					binBEMBLEL03_Service.manualRollback();
				} catch (Exception ex) {	
					
				}
				logger.error("update BatchExecFlag exception：" + e.getMessage(),e);
				throw e;
			}
		} else {
			// 入会日期限制
			map.put("joinDateLimit", map.get("levelDate"));
			// 取得会员当前等级表需要处理的会员件数(手动设定的会员)
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
			List<Map<String, Object>> memList = binBEMBLEL03_Service.getLevelRecordList(map);
			// 会员信息不为空
			if (!CherryBatchUtil.isBlankList(memList)) {
				// 会员信息等级处理
				boolean nextFlag = recordLevel(memList, map, pageNum);
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
		batchLoggerDTO.addParam(PropertiesUtil.getMessage("PMB00015", null));
		batchLoggerDTO.addParam(String.valueOf(count));
		batchLoggerDTO.addParam(String.valueOf(totalFail));
		cherryBatchLogger.BatchLogger(batchLoggerDTO);
		return flag;
	}
	
	/**
	 * 取得导入时间
	 * 
	 * @param map
	 * 			查询参数
	 * @return String
	 * 			导入时间
	 * 
	 */
	public String getMaxInitTime(Map<String, Object> map) {
		// 取得导入时间
		return binBEMBLEL03_Service.getMaxInitTime(map);
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
	 * 从老后台导入会员等级及有效期处理
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
	public boolean witImptLevel(List<Map<String, Object>> memList, 
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
			binBEMBLEL03_Service.clearExecFlag04(memList);
			binBEMBLEL03_Service.manualCommit();
		} catch (Exception e) {
			nextFlag = false;
			try {
				binBEMBLEL03_Service.manualRollback();
			} catch (Exception ex) {	
				
			}
			// 去除执行标识失败
			batchLoggerDTO.clear();
			batchLoggerDTO.setCode("EMB00038");
			batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
			cherryBatchLogger.BatchLogger(batchLoggerDTO);
			flag = CherryBatchConstants.BATCH_WARNING;
			throw e;
		}
		try {
			// 更新列表(会员初始等级导入表)
			List<Map<String, Object>> upList = new ArrayList<Map<String, Object>>();
			List<Map<String, Object>> errorList = new ArrayList<Map<String, Object>>();
			for (Map<String, Object> memInfo : memList) {
				// 共通的参数设置
				commParams(memInfo);
				// 会员ID
				//int memberInfoId = Integer.parseInt(memInfo.get("memberInfoId").toString());
				// 会员卡号
				String memCode = (String) memInfo.get("memCode");
				// 取得会员卡号列表
				List<Map<String, Object>> cardList = binBEMBLEL03_Service.getMemCardList(memInfo);
				if (null == cardList || cardList.isEmpty()) {
					memInfo.put("errorMsg", PropertiesUtil.getMessage("EMB00055", null));
					errorList.add(memInfo);
					continue;
				}
				String[] cardArr = new String[cardList.size()];
				boolean errFlag = true;
				for (int i = 0; i < cardList.size(); i++) {
					Map<String, Object> cardInfo = cardList.get(i);
					String memberCode = ((String) cardInfo.get("memCode")).trim();
					cardArr[i] = memberCode;
					if (memCode.equalsIgnoreCase(memberCode)) {
						errFlag = false;
					}
				}
				if (errFlag) {
					memInfo.put("errorMsg", PropertiesUtil.getMessage("EMB00047", null));
					errorList.add(memInfo);
					continue;
				}
				if (cardArr.length > 1) {
					memInfo.put("cardArr", cardArr);
				}
				// 从老后台取得会员等级变化列表
				List<Map<String, Object>> witLevelList = binBEMBLEL03_Service.getWitLevelList(memInfo);
				if (null == witLevelList || witLevelList.isEmpty()) {
					memInfo.put("errorMsg", PropertiesUtil.getMessage("EMB00048", null));
					errorList.add(memInfo);
					continue;
				}
//				if (witLevelList.size() == 1) {
//					memInfo.put("errorMsg", PropertiesUtil.getMessage("EMB00049", null));
//					errorList.add(memInfo);
//					continue;
//				}
				int index = 0;
				Map<String, Object> levelDateInfo = null;
				boolean noLevel = false;
				for (int i = 0; i < witLevelList.size(); i++) {
					Map<String, Object> witLevelInfo = witLevelList.get(i);
					// 当前等级
					String curLevel = (String) witLevelInfo.get("curLevel");
					if (null == curLevel) {
						noLevel = true;
						break;
					} else if (curLevel.trim().equalsIgnoreCase("WMLC002")) {
						continue;
					} else {
						if (null != witLevelInfo.get("levelStartDate")) {
							levelDateInfo = witLevelInfo;
							index = i;
							break;
						}
					}
				}
				if (noLevel) {
					memInfo.put("errorMsg", PropertiesUtil.getMessage("EMB00050", null));
					errorList.add(memInfo);
					continue;
				}
				if (null != levelDateInfo) {
					// 等级有效期开始日
					String levelStartDate = (String) levelDateInfo.get("levelStartDate");
					if (compTime(levelStartDate, levelFromTime) <= 0) {
						// 等级有效期结束日
						String levelEndDate = (String) levelDateInfo.get("levelEndDate");
						if (null != levelEndDate && compTime(levelEndDate, levelFromTime) < 0) {
							for (int i = index + 1; i < witLevelList.size(); i++) {
								Map<String, Object> witLevelInfo = witLevelList.get(i);
								// 当前等级
								String curLevel = (String) witLevelInfo.get("curLevel");
								if (null != curLevel && !(curLevel.trim().equalsIgnoreCase("WMLC002"))
										&& null != witLevelInfo.get("levelStartDate") 
										&& null != witLevelInfo.get("levelEndDate")) {
									// 等级有效期开始日
									String startDate = (String) witLevelInfo.get("levelStartDate");
									// 等级有效期结束日
									String endDate = (String) witLevelInfo.get("levelEndDate");
									if (compTime(startDate, levelFromTime) <= 0 && compTime(endDate, levelFromTime) >=0) {
										levelDateInfo = witLevelInfo;
										break;
									}
								}
							}
						}
						// 等级
						memInfo.put("curLevel", levelDateInfo.get("curLevel"));
						// 等级有效期开始日
						memInfo.put("levelStartDate", levelDateInfo.get("levelStartDate"));
						// 等级有效期结束日
						memInfo.put("levelEndDate", levelDateInfo.get("levelEndDate"));
						upList.add(memInfo);
						continue;
					} 
				} else {
					index = witLevelList.size() - 1;
				}
				// 初始导入的等级
				String preLevel = ((String) memInfo.get("imptLevel")).trim();
				String memberLevel = null;
				String levelStartDate = null;
				String levelEndDate = null;
				boolean isError = false;
				for (int i = 0; i <= index; i++) {
					Map<String, Object> witLevelInfo = witLevelList.get(i);
					// 当前等级
					String curLevel = ((String) witLevelInfo.get("curLevel")).trim();
					if (i < index) {
						if (preLevel.equalsIgnoreCase(curLevel)) {
							continue;
						} else {
							// 等级导入日期
							String insertday = (String) witLevelInfo.get("insertday");
							// 降级
							if (!("WMLC003".equalsIgnoreCase(preLevel) && 
									"WMLC004".equalsIgnoreCase(curLevel))) {
								insertday = DateUtil.addDateByDays(DateUtil.DATE_PATTERN, insertday, -2);
							}
							memberLevel = preLevel;
							levelEndDate = insertday + " 00:00:00";
							levelStartDate = DateUtil.addDateByDays(DateUtil.DATE_PATTERN, insertday, 1);
							levelStartDate = DateUtil.addDateByMonth(DateUtil.DATE_PATTERN, levelStartDate, -12) + " 00:00:00";
						}
						preLevel = curLevel;
						if (null != levelStartDate) {
							break;
						}
					} else {
						// 等级有效期开始日
						String startDate = null;
						if (!"WMLC002".equalsIgnoreCase(curLevel)) {
							startDate = (String) witLevelInfo.get("levelStartDate");
						}
						if (null == startDate && preLevel.equalsIgnoreCase(curLevel)) {
							memInfo.put("errorMsg", PropertiesUtil.getMessage("EMB00051", null));
							errorList.add(memInfo);
							isError = true;
							break;
						}
						if (null != startDate) {
							levelEndDate = DateUtil.coverTime2YMD(startDate, DateUtil.DATE_PATTERN);
							levelEndDate = DateUtil.addDateByDays(DateUtil.DATE_PATTERN, levelEndDate, -1);
						} else {
							// 等级导入日期
							String insertday = (String) witLevelInfo.get("insertday");
							// 降级
							if (!("WMLC003".equalsIgnoreCase(preLevel) && 
									"WMLC004".equalsIgnoreCase(curLevel))) {
								levelEndDate = DateUtil.addDateByDays(DateUtil.DATE_PATTERN, insertday, -2);
							} else {
								levelEndDate = insertday;
							}
						}
						memberLevel = preLevel;
						levelStartDate = DateUtil.addDateByDays(DateUtil.DATE_PATTERN, levelEndDate, 1);
						levelStartDate = DateUtil.addDateByMonth(DateUtil.DATE_PATTERN, levelStartDate, -12) + " 00:00:00";
						levelEndDate = levelEndDate + " 00:00:00";
					}
				}
				if (isError) {
					continue;
				}
				if (null == levelStartDate) {
					memInfo.put("errorMsg", PropertiesUtil.getMessage("EMB00052", null));
					errorList.add(memInfo);
					continue;
				}
				if (compTime(levelStartDate, levelFromTime) > 0) {
					memInfo.put("errorMsg", PropertiesUtil.getMessage("EMB00053", null));
					errorList.add(memInfo);
					continue;
				} else {
					// 等级
					memInfo.put("curLevel", memberLevel);
					// 等级有效期开始日
					memInfo.put("levelStartDate", levelStartDate);
					// 等级有效期结束日
					memInfo.put("levelEndDate", levelEndDate);
					upList.add(memInfo);
				}
			}
			boolean flag = false;
			if (!upList.isEmpty()) {
				// 更新会员等级有效期
				binBEMBLEL03_Service.updateLevelDateInfo(upList);
				flag = true;
			}
			if (!errorList.isEmpty()) {
				// 记录错误信息
				binBEMBLEL03_Service.updateLevelErrInfo(errorList);
				flag = true;
			}
			if (flag) {
				binBEMBLEL03_Service.manualCommit();
			}
		} catch (Exception e) {
			try {
				binBEMBLEL03_Service.manualRollback();
			} catch (Exception ex) {	
				
			}
			failCount = size;
			totalFail += size;
			// 导入会员初始等级失败
			batchLoggerDTO.clear();
			batchLoggerDTO.setCode("EMB00054");
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
	 * 导入会员当前等级
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
	public boolean imptLevel(List<Map<String, Object>> memList, 
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
			binBEMBLEL03_Service.manualCommit();
		} catch (Exception e) {
			nextFlag = false;
			try {
				binBEMBLEL03_Service.manualRollback();
			} catch (Exception ex) {	
				
			}
			// 去除执行标识失败
			batchLoggerDTO.clear();
			batchLoggerDTO.setCode("EMB00038");
			batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
			cherryBatchLogger.BatchLogger(batchLoggerDTO);
			flag = CherryBatchConstants.BATCH_WARNING;
			throw e;
		}
		try {
//			map.put("startId", startId);
//			map.put("endId", endId);
//			// 取得已经导入的会员列表
//			List<Map<String, Object>> imptedList = binBEMBLEL03_Service.getImptedMemList(map);
			// 初始导入日期
			String initialDate = (String) map.get("initialDate");
			// 初始导入时间
			String initialTime = (String) map.get("initialTime");
			// 新增列表(会员当前等级表)
			List<Map<String, Object>> addList = new ArrayList<Map<String, Object>>();
			for (Map<String, Object> memInfo : memList) {
				// 会员ID
//				int memberInfoId = Integer.parseInt(memInfo.get("memberInfoId").toString());
//				if (null != imptedList && !imptedList.isEmpty()) {
//					boolean flag = false;
//					for (int i = 0; i < imptedList.size(); i++) {
//						int imptedId = Integer.parseInt(imptedList.get(i).get("memberInfoId").toString());
//						// 已经导入过
//						if (memberInfoId == imptedId) {
//							imptedList.remove(i);
//							flag = true;
//							break;
//						}
//					}
//					if (flag) {
//						continue;
//					}
//				}
				// 会员当前等级
				int memberLevel = 0;
				if (null != memInfo.get("memberLevel")) {
					memberLevel = Integer.parseInt(memInfo.get("memberLevel").toString());
				}
				// 等级有效期开始日
				String levelStartDate = (String) memInfo.get("levelStartDate");
				// 等级有效期结束日
				String levelEndDate = (String) memInfo.get("levelEndDate");
				// 初始导入的等级
				int initialMemLevel = memberLevel;
				// 初始导入的等级起始日
				String initLevelStartDate = levelStartDate;
				// 初始导入的等级结束日
				String initLevelEndDate = levelEndDate;
				memInfo.put("initialTime", initialTime);
				// 普通会员
				if (memberLevel == 0) {
					levelStartDate = null;
					levelEndDate = null;
					initLevelStartDate = null;
					initLevelEndDate = null;
					// 入会日期
					String joinDate = (String) memInfo.get("joinDate");
					memInfo.put("limitTime", initialTime);
					// 新会员
					if (null != joinDate && 
							CherryChecker.compareDate(joinDate, initialDate) > 0
							&& binBEMBLEL03_Service.getSaleCount(memInfo) == 0) {
						memberLevel = 0;
						initialMemLevel = 0;
					} else {
						memberLevel = leve1;
						initialMemLevel = leve1;
						memInfo.put("lastBusiTime", initialTime);
						memInfo.put("initlastBusiTime", initialTime);
					}
					// VIP会员
				} else if (memberLevel == leve2) {
					String lastBusiTime = initialTime;
					if (null != levelStartDate) {
						lastBusiTime = DateUtil.coverTime2YMD(levelStartDate, DateUtil.DATE_PATTERN);
						lastBusiTime = DateUtil.addDateByDays(DateUtil.DATE_PATTERN, lastBusiTime, -1) + " 23:59:59";
					}
					memInfo.put("lastBusiTime", lastBusiTime);
					memInfo.put("initlastBusiTime", lastBusiTime);
				}
				memInfo.put("memberLevel", memberLevel);
				memInfo.put("levelStartDate", levelStartDate);
				memInfo.put("levelEndDate", levelEndDate);
				memInfo.put("initialMemLevel", initialMemLevel);
				memInfo.put("initLevelStartDate", initLevelStartDate);
				memInfo.put("initLevelEndDate", initLevelEndDate);
				if (null == memInfo.get("memCode")) {
					//  取得会员卡号
					memInfo.put("memCode", binOLCM31_BL.getMemCard(memInfo));
				}
				// 共通的参数设置
				commParams(memInfo);
				addList.add(memInfo);
			}
			if (!addList.isEmpty()) {
				// 插入会员等级调整履历表
				binBEMBLEL03_Service.addMemCurLevelList(addList);
				binBEMBLEL03_Service.manualCommit();
			}
		} catch (Exception e) {
			try {
				binBEMBLEL03_Service.manualRollback();
			} catch (Exception ex) {	
				
			}
			failCount = size;
			totalFail += size;
			// 导入会员初始等级失败
			batchLoggerDTO.clear();
			batchLoggerDTO.setCode("EMB00040");
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
			binBEMBLEL03_Service.manualCommit();
		} catch (Exception e) {
			nextFlag = false;
			try {
				binBEMBLEL03_Service.manualRollback();
			} catch (Exception ex) {	
				
			}
			// 去除执行标识失败
			batchLoggerDTO.clear();
			batchLoggerDTO.setCode("EMB00038");
			batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
			cherryBatchLogger.BatchLogger(batchLoggerDTO);
			flag = CherryBatchConstants.BATCH_WARNING;
			throw e;
		}
		try {
			// 初始导入日期
			String initDate = (String) map.get("initDate");
			// 初始导入时间
			String initTime = (String) map.get("initTime");
			// 新增列表(会员当前等级表)
			List<Map<String, Object>> addList = new ArrayList<Map<String, Object>>();
			// 更新列表(会员当前等级表)
			List<Map<String, Object>> upList = new ArrayList<Map<String, Object>>();
			// 新增列表(会员等级变化明细表)
			List<Map<String, Object>> addDetailList = new ArrayList<Map<String, Object>>();
			// 错误日志列表
			List<Map<String, Object>> errorList = new ArrayList<Map<String, Object>>();
			map.put("ADDDETAILLIST", addDetailList);
			map.put("ERRORLIST", errorList);
			for (Map<String, Object> memInfo : memList) {
				// 会员当前等级表ID
				Object memCurLevelIdObj = memInfo.get("memCurLevelId");
				// 共通的参数设置
				commParams(memInfo);
				if (null == memInfo.get("memCode")) {
					//  取得会员卡号
					memInfo.put("memCode", binOLCM31_BL.getMemCard(memInfo));
				}
				if (null == memCurLevelIdObj) {
					// 入会日期
					String joinDate = (String) memInfo.get("joinDate");
					memInfo.put("limitTime", initTime);
					// 未初始导入的老会员
					if (!(null != joinDate && 
							CherryChecker.compareDate(joinDate, initDate) > 0
							&& binBEMBLEL03_Service.getSaleCount(memInfo) == 0)) {
						memInfo.put("errorMsg", PropertiesUtil.getMessage("EMB00041", null));
						errorList.add(memInfo);
						continue;
					}
					memInfo.put("lastBusiTime", initTime);
					memInfo.put("memberLevel", 0);
				}
				// 等级计算
				calcLevel(map, memInfo);
				// 需要更新
				if (memInfo.containsKey("UPFLAG")) {
					if (null == memInfo.get("memCurLevelId")) {
						addList.add(memInfo);
					} else {
						upList.add(memInfo);
					}
				}
			}
			boolean flag = false;
			if (!addList.isEmpty()) {
				// 新增会员等级
				binBEMBLEL03_Service.addMemCurLevelList(addList);
				flag = true;
			}
			if (!upList.isEmpty()) {
				// 更新会员等级
				binBEMBLEL03_Service.updateMemCurLevelList(upList);
				flag = true;
			}
			if (!addDetailList.isEmpty()) {
				// 新增会员等级变化明细
				binBEMBLEL02_Service.addLevelChangeReport(addDetailList);
				flag = true;
			}
			if (!errorList.isEmpty()) {
				// 新增错误处理日志
				binBEMBLEL03_Service.addLevelCalcErrorList(errorList);
				flag = true;
			}
			if (flag) {
				binBEMBLEL03_Service.manualCommit();
			}
		} catch (Exception e) {
			try {
				binBEMBLEL03_Service.manualRollback();
			} catch (Exception ex) {	
				
			}
			failCount = size;
			totalFail += size;
			// 会员等级计算处理失败
			batchLoggerDTO.clear();
			batchLoggerDTO.setCode("EMB00043");
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
	public boolean recalcDetail(List<Map<String, Object>> memList, 
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
			binBEMBLEL03_Service.manualCommit();
		} catch (Exception e) {
			nextFlag = false;
			try {
				binBEMBLEL03_Service.manualRollback();
			} catch (Exception ex) {	
				
			}
			// 去除执行标识失败
			batchLoggerDTO.clear();
			batchLoggerDTO.setCode("EMB00038");
			batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
			cherryBatchLogger.BatchLogger(batchLoggerDTO);
			flag = CherryBatchConstants.BATCH_WARNING;
			throw e;
		}
		try {
			// 初始导入日期
			String initDate = (String) map.get("initDate");
			// 初始导入时间
			String initTime = (String) map.get("initTime");
			// 新增列表(会员当前等级表)
			List<Map<String, Object>> addList = new ArrayList<Map<String, Object>>();
			// 更新列表(会员当前等级表)
			List<Map<String, Object>> upList = new ArrayList<Map<String, Object>>();
			// 新增列表(会员等级变化明细表)
			List<Map<String, Object>> addDetailList = new ArrayList<Map<String, Object>>();
			// 错误日志列表
			List<Map<String, Object>> errorList = new ArrayList<Map<String, Object>>();
			map.put("ADDDETAILLIST", addDetailList);
			map.put("ERRORLIST", errorList);
			// 删除会员等级变化履历
			binBEMBLEL03_Service.delLevelChange(memList);
			// 删除会员等级履历
			binBEMBLEL03_Service.delLevelHistory(memList);
			for (Map<String, Object> memInfo : memList) {
				// 会员当前等级表ID
				Object memCurLevelIdObj = memInfo.get("memCurLevelId");
				// 共通的参数设置
				commParams(memInfo);
				if (null == memInfo.get("memCode")) {
					//  取得会员卡号
					memInfo.put("memCode", binOLCM31_BL.getMemCard(memInfo));
				}
				if (null == memCurLevelIdObj) {
					// 入会日期
					String joinDate = (String) memInfo.get("joinDate");
					memInfo.put("limitTime", initTime);
					// 未初始导入的老会员
					if (!(null != joinDate && 
							CherryChecker.compareDate(joinDate, initDate) > 0
							&& binBEMBLEL03_Service.getSaleCount(memInfo) == 0)) {
						memInfo.put("errorMsg", PropertiesUtil.getMessage("EMB00041", null));
						errorList.add(memInfo);
						continue;
					}
					memInfo.put("lastBusiTime", initTime);
					memInfo.put("memberLevel", 0);
				} else {
					memInfo.put("lastBusiTime", memInfo.get("initLastBusiTime"));
					memInfo.put("memberLevel", memInfo.get("initialMemLevel"));
					memInfo.put("levelStartDate", memInfo.get("initLevelStartDate"));
					memInfo.put("levelEndDate", memInfo.get("initLevelEndDate"));
				}
				// 等级计算
				calcLevel(map, memInfo);
				if (null == memInfo.get("memCurLevelId")) {
					addList.add(memInfo);
				} else {
					upList.add(memInfo);
				}
			}
			if (!addList.isEmpty()) {
				// 新增会员等级
				binBEMBLEL03_Service.addMemCurLevelList(addList);
			}
			if (!upList.isEmpty()) {
				// 更新会员等级
				binBEMBLEL03_Service.updateMemCurLevelList(upList);
			}
			if (!addDetailList.isEmpty()) {
				// 新增会员等级变化明细
				binBEMBLEL02_Service.addLevelChangeReport(addDetailList);
			}
			if (!errorList.isEmpty()) {
				// 新增错误处理日志
				binBEMBLEL03_Service.addLevelCalcErrorList(errorList);
			}
			binBEMBLEL03_Service.manualCommit();
		} catch (Exception e) {
			try {
				binBEMBLEL03_Service.manualRollback();
			} catch (Exception ex) {	
				
			}
			failCount = size;
			totalFail += size;
			// 会员等级计算处理失败
			batchLoggerDTO.clear();
			batchLoggerDTO.setCode("EMB00045");
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
	 * 记录会员某个时间点的等级
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
	public boolean recordLevel(List<Map<String, Object>> memList, 
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
			binBEMBLEL03_Service.manualCommit();
		} catch (Exception e) {
			nextFlag = false;
			try {
				binBEMBLEL03_Service.manualRollback();
			} catch (Exception ex) {	
				
			}
			// 去除执行标识失败
			batchLoggerDTO.clear();
			batchLoggerDTO.setCode("EMB00038");
			batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
			cherryBatchLogger.BatchLogger(batchLoggerDTO);
			flag = CherryBatchConstants.BATCH_WARNING;
			throw e;
		}
		try {
			// 等级日期
			String levelDate = (String) map.get("levelDate");
			// 日期区分
			String dateKbn = (String) map.get("dateKbn");
			// 新增列表(会员当前等级表)
			List<Map<String, Object>> addList = new ArrayList<Map<String, Object>>();
			for (Map<String, Object> memInfo : memList) {
				memInfo.put("joinDateLimit", map.get("joinDateLimit"));
				// 取得会员某个时间点的等级
				int level = binBEMBLEL03_Service.getHistoryLevel(memInfo);
				if (0 == level) {
					continue;
				}
				memInfo.put("memLevel", level);
				memInfo.put("levelDate", levelDate);
				memInfo.put("dateFlag", dateKbn);
				if (null == memInfo.get("memCode")) {
					//  取得会员卡号
					memInfo.put("memCode", binOLCM31_BL.getMemCard(memInfo));
				}
				// 共通的参数设置
				commParams(memInfo);
				addList.add(memInfo);
			}
			if (!addList.isEmpty()) {
				// 插入会员等级调整履历表
				binBEMBLEL02_Service.addLevelHistoryInfo(addList);
				binBEMBLEL03_Service.manualCommit();
			}
		} catch (Exception e) {
			try {
				binBEMBLEL03_Service.manualRollback();
			} catch (Exception ex) {	
				
			}
			failCount = size;
			totalFail += size;
			// 导入会员初始等级失败
			batchLoggerDTO.clear();
			batchLoggerDTO.setCode("EMB00044");
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
	 * 等级计算
	 * 
	 * @param map 
	 * 			参数集合
	 * @param memInfo 
	 * 			会员信息
	 * @throws Exception 
	 * 
	 */
	private void calcLevel(Map<String, Object> map, Map<String, Object> memInfo) throws Exception{
		// 错误日志列表
		List<Map<String, Object>> errorList = (List<Map<String, Object>>) map.get("ERRORLIST");
		// 会员当前等级
		int memberLevel = Integer.parseInt(memInfo.get("memberLevel").toString());
		// 业务日期
		String busDate = (String) map.get("busDate");
		// 等级有效期截止日期
		String levelEndDate = (String) memInfo.get("levelEndDate");
		if (null == levelEndDate && (memberLevel == leve3 || memberLevel == leve2)) {
			memInfo.put("errorMsg", PropertiesUtil.getMessage("EMB00042", null));
			errorList.add(memInfo);
			return;
		}
		// 铂金会员
		if (memberLevel == leve3) {
			String levelEndDay = DateUtil.coverTime2YMD(levelEndDate, DateUtil.DATE_PATTERN);
			if (CherryChecker.compareDate(busDate, levelEndDay) > 0) {
				// 降级处理
				downLevel(map, memInfo, levelEndDay);
				calcLevel(map, memInfo);
			}
		} else {
			// 销售记录
			List<Map<String, Object>> saleList = null;
			// 全部销售记录
			List<Map<String, Object>> allList = null;
			String lastBusiTime = (String) memInfo.get("lastBusiTime");
			if (!memInfo.containsKey("SALELIST")) {
				memInfo.put("fromTime", lastBusiTime);
				memInfo.put("toTime", map.get("levelLimitTime"));
				// 销售记录
				saleList = binBEMBLEL03_Service.getSaleRecordList(memInfo);
				memInfo.put("SALELIST", saleList);
				if (null != lastBusiTime) {
					String fromTime = DateUtil.coverTime2YMD(lastBusiTime, DateUtil.DATE_PATTERN);
					fromTime = DateUtil.addDateByMonth(DateUtil.DATE_PATTERN, fromTime, -12);
					fromTime = DateUtil.addDateByDays(DateUtil.DATE_PATTERN, fromTime, 1) + " 00:00:00";
					memInfo.put("fromTime", fromTime);
					allList = binBEMBLEL03_Service.getSaleRecordList(memInfo);
				} else {
					allList = new ArrayList<Map<String, Object>>();
					allList.addAll(saleList);
				}
				memInfo.put("ALLTICKETS", allList);
			} else {
				saleList = (List<Map<String, Object>>) memInfo.get("SALELIST");
				allList = (List<Map<String, Object>>) memInfo.get("ALLTICKETS");
				if (null != lastBusiTime && null != saleList) {
					for (int i = 0; i < saleList.size(); i++) {
						if (compTime(lastBusiTime, String.valueOf(saleList.get(i).get("ticketDate"))) >= 0) {
							saleList.remove(i);
							i--;
						} else {
							break;
						}
					}
				}
			}
			boolean isNoSale = null == saleList || saleList.isEmpty();
			// 最近未销售的会员，等级不变
			if (isNoSale && memberLevel != leve2) {
				return;
			}
			// VIP会员
			if (memberLevel == leve2) {
				String levelEndDay = DateUtil.coverTime2YMD(levelEndDate, DateUtil.DATE_PATTERN);
				if (!isNoSale) {
					for (int i = 0; i < saleList.size(); i++) {
						Map<String, Object> saleMap = saleList.get(i);
						// 单据时间
						String ticketDay = (String) saleMap.get("ticketDay");
						int compResult = CherryChecker.compareDate(ticketDay, levelEndDay);
						if (compResult <= 0) {
							int changeType = upLevel(map, memInfo, saleMap);
							// 升级
							if (changeType != 0) {
								calcLevel(map, memInfo);
								return;
							}
						} else {
							// 降级处理
							downLevel(map, memInfo, levelEndDay);
							calcLevel(map, memInfo);
							return;
						}
					}
				}
				if (CherryChecker.compareDate(busDate, levelEndDay) > 0) {
					// 降级处理
					downLevel(map, memInfo, levelEndDay);
					calcLevel(map, memInfo);
				}
			} else {
				for (int i = 0; i < saleList.size(); i++) {
					Map<String, Object> saleMap = saleList.get(i);
					int changeType = upLevel(map, memInfo, saleMap);
					// 升级
					if (changeType != 0) {
						calcLevel(map, memInfo);
						return;
					}
				}
			}
		}
	}
	
	/**
	 * 升级处理
	 * 
	 * @param map 
	 * 			参数集合
	 * @param memInfo 
	 * 			会员信息
	 * @param saleMap 
	 * 			销售信息
	 * @return int
	 * 			0:等级未变化  1:升级  2:降级
	 * 
	 */
	private int upLevel(Map<String, Object> map, Map<String, Object> memInfo, Map<String, Object> saleMap) throws Exception {
		// 单据时间
		String toTime = (String) saleMap.get("ticketDate");
		String fromTime = DateUtil.coverTime2YMD(toTime, DateUtil.DATE_PATTERN);
		fromTime = DateUtil.addDateByMonth(DateUtil.DATE_PATTERN, fromTime, -12) + " 23:59:59";
		// 计算区间内的累计金额
		double totalAmount = 0;
		if (memInfo.containsKey("ALLTICKETS")) {
			// 计算区间内的累计金额
			totalAmount = calcTotalAmount(memInfo, fromTime, toTime);
		} else {
			memInfo.put("fromTime", fromTime);
			memInfo.put("toTime", toTime);
			totalAmount = binBEMBLEL03_Service.getTtlAmount(memInfo);
		}
		// 新的等级
		int level = getLevelByAmount(totalAmount);
		// 生成等级变化明细
		return detailSetting(map, memInfo, level, toTime, 1);
	}
	
	/**
	 * 降级处理
	 * 
	 * @param map 
	 * 			参数集合
	 * @param memInfo 
	 * 			会员信息
	 * @param levelEndDay 
	 * 			等级有效期截止日
	 * 
	 */
	private void downLevel(Map<String, Object> map, Map<String, Object> memInfo, String levelEndDay) throws Exception {
		String fromTime = DateUtil.addDateByMonth(DateUtil.DATE_PATTERN, levelEndDay, -12) + " 23:59:59";
		String toTime = levelEndDay + " 23:59:59";
		// 计算区间内的累计金额
		double totalAmount = 0;
		if (memInfo.containsKey("ALLTICKETS")) {
			// 计算区间内的累计金额
			totalAmount = calcTotalAmount(memInfo, fromTime, toTime);
		} else {
			memInfo.put("fromTime", fromTime);
			memInfo.put("toTime", toTime);
			totalAmount = binBEMBLEL03_Service.getTtlAmount(memInfo);
		}
		// 新的等级
		int level = getLevelByAmount(totalAmount);
		// 生成等级变化明细
		detailSetting(map, memInfo, level, toTime, 0);
	}
	
	/**
	 * 根据累计金额推算等级
	 * 
	 * @param ttlAmount 累计金额
	 * 
	 * @return int 当时的等级
	 */
	private int getLevelByAmount(double ttlAmount) {
		if (ttlAmount < 2000) {
			return leve1;
		} else if (ttlAmount < 5000) {
			return leve2;
		} else {
			return leve3;
		}
	}
	
	/**
	 * 等级变化明细生成
	 * 
	 * @param map 
	 * 			参数集合
	 * @param memInfo 
	 * 			会员信息
	 * @param newLevel 
	 * 			新等级
	 * @param changeTime 
	 * 			业务时间
	 * @param flag 
	 * 			0:降级  1:其它
	 * @return int
	 * 			0:等级未变化  1:升级  2:降级
	 * 
	 */
	private int detailSetting(Map<String, Object> map, Map<String, Object> memInfo, int newLevel, String changeTime, int flag) throws Exception {
		// 新增列表(会员等级变化明细表)
		List<Map<String, Object>> addDetailList = (List<Map<String, Object>>) map.get("ADDDETAILLIST");
		// 会员当前等级
		int memberLevel = Integer.parseInt(memInfo.get("memberLevel").toString());
		memInfo.put("lastBusiTime", changeTime);
		// 等级表需要更新
		memInfo.put("UPFLAG", "1");
		if (flag != 0 && newLevel < memberLevel) {
			return 0;
		}
		// 升降级区分 
		int changeType = 0;
		if (newLevel != memberLevel || flag == 0) {
			// 新的有效期开始日
			String levelStartDate = null;
			// 新的有效期截止日
			String levelEndDate = null;
			// VIP或铂金会员
			if (newLevel > leve1) {
				// 新的有效期开始日
				levelStartDate = DateUtil.coverTime2YMD(changeTime, DateUtil.DATE_PATTERN);
				// 新的有效期截止日
				levelEndDate = DateUtil.addDateByMonth(DateUtil.DATE_PATTERN, levelStartDate, 12) + " 00:00:00";
				levelStartDate = DateUtil.addDateByDays(DateUtil.DATE_PATTERN, levelStartDate, 1) + " 00:00:00";
			}
			if (newLevel != memberLevel) {
				Map<String, Object> detailMap = new HashMap<String, Object>();
				detailMap.putAll(memInfo);
				// 升降级区分 : 升级
				changeType = 1;
				if (newLevel < memberLevel) {
					// 升降级区分 : 降级
					changeType = 2;
				}
				detailMap.put("changeType", changeType);
				detailMap.put("beginLevel", memberLevel);
				detailMap.put("endLevel", newLevel);
				if (null != levelStartDate) {
					detailMap.put("changeDate", levelStartDate);
				} else {
					changeTime = DateUtil.coverTime2YMD(changeTime, DateUtil.DATE_PATTERN);
					detailMap.put("changeDate", DateUtil.addDateByDays(DateUtil.DATE_PATTERN, changeTime, 1) + " 00:00:00");
				}
				detailMap.remove("ALLTICKETS");
				detailMap.remove("SALELIST");
				addDetailList.add(detailMap);
				memInfo.put("memberLevel", newLevel);
			}
			memInfo.put("levelStartDate", levelStartDate);
			memInfo.put("levelEndDate", levelEndDate);
		}
		return changeType;
	}
	
	/**
	 * 计算区间内的累计金额
	 * 
	 * @param memInfo 
	 * 			会员信息
	 * @param fromTime 
	 * 			开始时间
	 * @param toTime 
	 * 			截止时间
	 * @return double
	 * 			累计金额
	 * 
	 */
	private double calcTotalAmount(Map<String, Object> memInfo, String fromTime, String toTime) {
		List<Map<String, Object>> allTicketList = (List<Map<String, Object>>) memInfo.get("ALLTICKETS");
		if (null != allTicketList && !allTicketList.isEmpty()) {
			// 累计金额
			double ttlAmount = 0;
			for (int i = 0; i < allTicketList.size(); i++) {
				Map<String, Object> allMap = allTicketList.get(i);
				// 销售时间
				String ticketDate = (String) allMap.get("ticketDate");
				if (compTime(fromTime, ticketDate) >= 0) {
					allTicketList.remove(i);
					i--;
				} else if (compTime(toTime, ticketDate) >= 0) {
					// 销售金额
					double Amount = Double.parseDouble(allMap.get("amount").toString());
					ttlAmount = DoubleUtil.add(ttlAmount, Amount);
				} else {
					break;
				}
			}
			return ttlAmount;
		}
		return 0;
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
		map.put(CherryBatchConstants.CREATEDBY, "BINBEMBLEL03");
		// 作成程序名
		map.put(CherryBatchConstants.CREATEPGM, "BINBEMBLEL03");
		// 更新者
		map.put(CherryBatchConstants.UPDATEDBY, "BINBEMBLEL03");
		// 更新程序名
		map.put(CherryBatchConstants.UPDATEPGM, "BINBEMBLEL03");
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
	
	/**
	 * 取得铂金会员购买总数
	 * 
	 * @param map
	 * @return
	 */
	public int getVipPlusBuyCount(Map<String, Object> map) {
		// 取得铂金会员购买总数
		return binBEMBLEL03_Service.getVipPlusBuyCount(map);
	}
	
	/**
	 * 导出CSV处理
	 * 
	 * @param map 查询条件
	 * @param fetchDataHandler 查询数据处理器
	 */
	public byte[] exportCSV(Map<String, Object> map) throws Exception {
		
		// 标题行格式数组
        String[][] titleRows = (String[][])map.get("titleRows");
		ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(byteOut, "GBK"));
		try {
			// 文件头部信息
	        String header = (String)map.get("header");
	        if(header != null && !"".equals(header)) {
	        	bw.write(header.replaceAll(",", "，").replaceAll("\r", "").replaceAll("\n", "")+"\r\n");
	        	bw.flush();
	        }
			
			StringBuffer str = new StringBuffer();
			// 写标题行处理
			for(int j = 0; j < titleRows.length; j++) {
				str.append(titleRows[j][1].replaceAll(",", "，").replaceAll("\r", "").replaceAll("\n", ""));
				if(j == titleRows.length-1) {
					str.append("\r\n");
				} else {
					str.append(",");
				}
			}
			bw.write(str.toString());
			bw.flush();
			
			// CSV导出最大数据量
			int maxCount = CherryConstants.EXPORTCSV_MAXCOUNT;
			// 导出数据总数
			int count = 0;
			// 数据查询长度
	 		int dataSize = CherryConstants.BATCH_PAGE_MAX_NUM;
	 		// 数据抽出次数
	 		int currentNum = 0;
	 		// 查询开始位置
	 		int startNum = 0;
	 		// 查询结束位置
	 		int endNum = 0;
	 		// 排序字段
	 		map.put(CherryBatchConstants.SORT_ID, "memberCode,billCode");
	 		while (true) {
	 			// 查询开始位置
	 			startNum = dataSize * currentNum + 1;
	 			// 查询结束位置
	 			endNum = startNum + dataSize - 1;
	 			// 数据抽出次数累加
	 			currentNum++;
	 			// 查询开始位置
	 			map.put(CherryConstants.START, startNum);
	 			// 查询结束位置
	 			map.put(CherryConstants.END, endNum);
	 			
	 			// 取得需要转成Excel文件的数据List
	 			List<Map<String, Object>> dataList = binBEMBLEL03_Service.getVipPlusBuyList(map);
	 			if(dataList != null && !dataList.isEmpty()) {
	 				for(int i = 0; i < dataList.size(); i++) {
	 					// 达到CSV导出最大数据量时停止导出
	 					if(count == maxCount) {
	 						break;
	 					}
	 					count++;
						Map<String, Object> dataMap = dataList.get(i);
						str.setLength(0);
						// 添加数据行处理
						for(int j = 0; j < titleRows.length; j++) {
							String data = "";
							String key = titleRows[j][0];
							Object value = dataMap.get(key);
							if(value != null && !"".equals(value.toString())) {
								data = value.toString();
							}
							if(data != null) {
								data = data.replaceAll(",", "，").replaceAll("\r", "").replaceAll("\n", "");
							}
							str.append(data);
							if(j == titleRows.length-1) {
								str.append("\r\n");
							} else {
								str.append(",");
							}
						}
						bw.write(str.toString());
					}
	 				bw.flush();
	 				// 达到CSV导出最大数据量时停止导出
 					if(count == maxCount) {
 						break;
 					}
	 				if(dataList.size() < dataSize) {
	 					break;
	 				}
	 				dataList.clear();
	 			} else {
	 				break;
	 			}
	 		}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return null;
		} finally {
			bw.close();
		}
		return byteOut.toByteArray();
	}
	
	/**
	 * 压缩文件处理
	 * 
	 * @param byteArray 待压缩的字节数组
	 * @param fileName 待压缩的文件名
	 * @return 压缩后字节数组
	 */
	public byte[] fileCompression(byte[] byteArray, String fileName) throws Exception {
		
		InputStream byteIn = new ByteArrayInputStream(byteArray);
        ByteArrayOutputStream zipByteOut = new ByteArrayOutputStream();
        ZipOutputStream zipOut = new ZipOutputStream(zipByteOut); 
        zipOut.setEncoding("utf-8");
        zipOut.putNextEntry(new ZipEntry(fileName)); 
        int b; 
        while ((b = byteIn.read()) != -1) {
        	zipOut.write(b); 
        }
        byteIn.close();
        zipOut.flush(); 
        zipOut.close(); 
        return zipByteOut.toByteArray();
	}
}
