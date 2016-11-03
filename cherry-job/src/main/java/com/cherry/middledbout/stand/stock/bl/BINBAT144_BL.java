package com.cherry.middledbout.stand.stock.bl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.batcmbussiness.bl.BINBECM01_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.core.BatchExceptionDTO;
import com.cherry.cm.core.BatchLoggerDTO;
import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryBatchException;
import com.cherry.cm.core.CherryBatchLogger;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.CherryBatchUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.middledbout.stand.stock.service.BINBAT144_Service;

/**
 * 标准接口：实时库存数据导出到标准接口表(实时库存接口表)BL
 * 
 * @author lzs
 * 
 */
public class BINBAT144_BL {

	/** 打印当前类的日志信息 **/
	private static CherryBatchLogger logger = new CherryBatchLogger(BINBAT144_BL.class);

	@Resource
	private BINBAT144_Service binBAT144_Service;
	
	@Resource(name = "binOLCM14_BL")
	private BINOLCM14_BL binOLCM14_BL;

	/** BATCH处理标志 */
	private int flag = CherryBatchConstants.BATCH_SUCCESS;

	/** JOB执行相关共通 BL **/
	@Resource
	private BINBECM01_BL binBECM01_BL;

	/** 处理数据的上限数量 **/
	private final int BATCH_SIZE = 1000;

	/** 处理总件数 **/
	private int totalCount = 0;

	/** 失败件数 **/
	private int failCount = 0;

	/** 插入条数 **/
	private int insertCount = 0;

	/** 更新条数 **/
	private int updateCount = 0;

	/** 失败的主要原因，受字段长度限制，这里只要记录主要原因即可 */
	private String fReason = "";

	private Map<String, Object> comMap = new HashMap<String, Object>();

	/**
	 * 实时库存数据导出到标准接口表的Batch处理
	 * 
	 * @param map
	 * @return
	 * @throws CherryBatchException
	 */
	public int tran_binBAT144(Map<String, Object> map) throws CherryBatchException, Exception {
		try {
			// 初始化
			init(map);
		} catch (Exception e) {
			e.printStackTrace();
			// 初始化失败
			BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO();
			batchExceptionDTO.setBatchName(this.getClass());
			batchExceptionDTO.setErrorCode("ECM00005");
			batchExceptionDTO.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
			batchExceptionDTO.setException(e);
			flag = CherryBatchConstants.BATCH_ERROR;
			fReason = String.format("程序init方法初始化失败，具体查看Log日志", e.getMessage());
			throw new CherryBatchException(batchExceptionDTO);
		}
		try {
			// 循环次数
			int i = 0;
			// 上一批次(页)最后一条记录
			String lastProductStockId = "";
			// 物理删除标准接口库存表(IF_Stock)数据
			binBAT144_Service.delIFStock(map);
			while (true) {
				Map<String, Object> listMap = new HashMap<String, Object>();
				listMap.putAll(comMap);
				listMap.put("batchSize", BATCH_SIZE);
				listMap.put("lastProductStockId", lastProductStockId);
				List<Map<String, Object>> productStockList = binBAT144_Service.getProductStockList(listMap);
				if (CherryBatchUtil.isBlankList(productStockList)) {
					fReason = String.format("查询实时库存数据结果为空,程序执行结束");
					// List使用完毕，设置值为null
					productStockList = null;
					break;
				} else {
					i++;
					// 每次批次处理数量的初始值
					int startSize = (i - 1) * BATCH_SIZE + 1;
					// 每次批次处理数量的结束值
					int endSize = BATCH_SIZE * i;
					// 根据新后台查询出的库存数据进行库存接口表的数据插入操作
					addProductStock(productStockList, startSize, endSize);
					// 打印正常输出数量
					BatchLoggerDTO processLogger = new BatchLoggerDTO();
					processLogger.setCode("EOT00075");
					processLogger.setLevel(CherryBatchConstants.LOGGER_INFO);
					processLogger.addParam(String.valueOf(startSize));
					processLogger.addParam(String.valueOf(endSize));
					logger.BatchLogger(processLogger);
				}
				lastProductStockId = CherryBatchUtil.getString(productStockList.get(productStockList.size() - 1).get("productStockId"));
				// 数据总量
				totalCount += productStockList.size();
			}
		} catch (Exception e) {
			BatchLoggerDTO batchLogger = new BatchLoggerDTO();
			batchLogger.setCode("EOT00155");
			batchLogger.setLevel(CherryBatchConstants.LOGGER_ERROR);
			fReason = "新后台库存数据导出到库存接口表时失败! 具体见Log日志";
			flag = CherryBatchConstants.BATCH_WARNING;
			logger.BatchLogger(batchLogger, e);
			logger.outExceptionLog(e);
		}
		programEnd(map);
		outMessage();
		return flag;
	}

	/**
	 * 实时库存数据导出至标准接口库存表
	 * 
	 * @param productStockList
	 * @throws CherryBatchException
	 */
	private void addProductStock(List<Map<String, Object>> productStockList,int startSize, int endSize) throws CherryBatchException {
		try {
			// 数据插入标准接口库存表
			binBAT144_Service.insertIFStock(productStockList);
			// 分批数据第三方数据源手动提交
			binBAT144_Service.tpifManualCommit();
			// 插入数据量
			insertCount += productStockList.size();
		} catch (Exception e) {
			try {
				// 分批数据第三方数据源手动RollBack
				binBAT144_Service.tpifManualRollback();
			} catch (Exception ex) {
				logger.outExceptionLog(ex);
			}
			BatchLoggerDTO processLogger = new BatchLoggerDTO();
			processLogger.setCode("EOT00075");
			processLogger.setLevel(CherryBatchConstants.LOGGER_ERROR);
			processLogger.addParam(String.valueOf(startSize));
			processLogger.addParam(String.valueOf(endSize));
			logger.BatchLogger(processLogger);
			logger.outExceptionLog(e);
			flag = CherryBatchConstants.BATCH_WARNING;
			fReason = "新后台实时库存数据导出至库存接口表时失败！具体见Log日志";
		}
		// list使用完毕，归置为null
		productStockList = null;
	}

	/**
	 * 程序初始化参数
	 * 
	 * @param map
	 * @throws Exception
	 * @throws CherryBatchException
	 */
	private void init(Map<String, Object> map) throws CherryBatchException,Exception {
		// 设置共通参数
		setComMap(map);
		// BatchCD
		// 来自VSS$/01.Cherry/02.设计文档/01.概要设计/00.各种一览/【新设】CherryBatch一览.xlsx
		map.put("JobCode", "BAT144");
		// 程序【开始运行时间】
		String runStartTime = binBECM01_BL.getSYSDateTime();
		// 作成日时
		map.put("RunStartTime", runStartTime);
		// 取得Job控制程序的数据截取开始时间及结束时间
		Map<String, Object> jobControlInfoMap = binBECM01_BL.getJobControlInfo(map);
		// 程序【截取数据开始时间】
		map.put("TargetDataStartTime",jobControlInfoMap.get("TargetDataStartTime"));
		// 程序【截取数据结束时间】
		map.put("TargetDataEndTime", jobControlInfoMap.get("TargetDataEndTime"));
		
		//是否测试模式（若是则包含测试部门）
		String testMod = binOLCM14_BL.getConfigValue("1080", ConvertUtil.getString(map.get(CherryConstants.ORGANIZATIONINFOID)),ConvertUtil.getString(map.get(CherryConstants.BRANDINFOID)));
		map.put("testMod", testMod);
		
		comMap.putAll(map);
	}

	/**
	 * 共通Map
	 * 
	 * @param map
	 * @return
	 */
	private void setComMap(Map<String, Object> map) {
		// 更新程序名
		map.put(CherryBatchConstants.UPDATEPGM, "BINBAT144");
		// 作成程序名
		map.put(CherryBatchConstants.CREATEPGM, "BINBAT144");
		// 作成者
		map.put(CherryBatchConstants.CREATEDBY,CherryBatchConstants.UPDATE_NAME);
		// 更新者
		map.put(CherryBatchConstants.UPDATEDBY,CherryBatchConstants.UPDATE_NAME);
		// 所属组织
		map.put(CherryBatchConstants.ORGANIZATIONINFOID,map.get(CherryBatchConstants.ORGANIZATIONINFOID).toString());
		// 品牌ID
		map.put(CherryBatchConstants.BRANDINFOID,map.get(CherryBatchConstants.BRANDINFOID).toString());
	}

	/**
	 * 程序结束时，处理Job共通(更新Job控制表 、插入Job运行履历表)
	 * 
	 * @param paraMap
	 * @throws Exception
	 */
	private void programEnd(Map<String, Object> paraMap) throws Exception {
		// 程序结束时，插入Job运行履历表
		paraMap.put("flag", flag);
		paraMap.put("TargetDataCNT", totalCount);
		paraMap.put("SCNT", insertCount + updateCount);
		paraMap.put("FCNT", totalCount - (insertCount + updateCount));
		paraMap.put("FReason", fReason);
		paraMap.put("UCNT", updateCount);
		paraMap.put("ICNT", insertCount);
		binBECM01_BL.insertJobRunHistory(paraMap);
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
		batchLoggerDTO2.addParam(String.valueOf(insertCount));
		// 失败件数
		BatchLoggerDTO batchLoggerDTO5 = new BatchLoggerDTO();
		batchLoggerDTO5.setCode("IIF00005");
		batchLoggerDTO5.setLevel(CherryBatchConstants.LOGGER_INFO);
		batchLoggerDTO5.addParam(String.valueOf(totalCount - insertCount));
		// 处理总件数
		logger.BatchLogger(batchLoggerDTO1);
		// 成功总件数
		logger.BatchLogger(batchLoggerDTO2);
		// 失败件数
		logger.BatchLogger(batchLoggerDTO5);
	}
}
