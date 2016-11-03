package com.cherry.middledbout.stand.allocation.bl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.batcmbussiness.interfaces.BINBECM01_IF;
import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.core.BatchExceptionDTO;
import com.cherry.cm.core.BatchLoggerDTO;
import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryBatchException;
import com.cherry.cm.core.CherryBatchLogger;
import com.cherry.cm.util.CherryBatchUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.middledbout.stand.allocation.service.BINBAT125_Service;

/**
 * 标准接口：柜台调拨单据数据导出到标准接口表(调拨单)BL
 * 
 * @author lzs
 * 
 */
public class BINBAT125_BL {
	/** 打印当前类的日志信息 **/
	private static CherryBatchLogger logger = new CherryBatchLogger(BINBAT125_BL.class);
	@Resource
	private BINBAT125_Service binbat125_Service;

	/** BATCH处理标志 */
	private int flag = CherryBatchConstants.BATCH_SUCCESS;

	/** 系统配置项 共通 **/
	@Resource(name = "binOLCM14_BL")
	private BINOLCM14_BL binOLCM14_BL;

	/** JOB执行相关共通 IF */
	@Resource(name = "binbecm01_IF")
	private BINBECM01_IF binbecm01_IF;

	/** 处理数据的上限数量 **/
	private final int BATCH_SIZE = 500;

	/** 处理总件数 **/
	private int totalCount = 0;
	/** 失败件数 **/
	private int failCount = 0;
	/** 调拨单记录同步状态，1:可同步 **/
	private final String SYNCH_FLAG_1 = "1";
	/** 调拨单记录同步状态，2:同步处理中 */
	private final String SYNCH_FLAG_2 = "2";
	/** 调拨单记录同步状态，3:已完成 **/
	private final String SYNCH_FLAG_3 = "3";

	/** 失败的主要原因，受字段长度限制，这里只要记录主要原因即可 */
	private String fReason = "";

	/**
	 * 柜台调出单据数据导出到标准接口表的Batch处理
	 * 
	 * @param map
	 * @return
	 * @throws CherryBatchException
	 */
	public int tran_binbat125(Map<String, Object> map) throws CherryBatchException, Exception {
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
				fReason = String.format("程序init方法初始化失败，具体查看Log日志",e.getMessage());
				throw new CherryBatchException(batchExceptionDTO);
			}
			while (true) {
				// 预处理可能导出失败件数
				int preFailAmount = 0;
				try {
					// 获得【SynchFlag=2】的调拨单据的数据量【即处于导出处理中且上次导出失败的数据】
					preFailAmount = getPrtAllocatOutAmountBySynch(map);

					map.put("batchSize",BATCH_SIZE);
					// 获得【SynchFlag=1-->SynchFlag=2】的调拨单据（调出）的数据量
					int expAmount = updatePrtAllocatOutFlag(map, 1);
					// 新后台数据源提交
					binbat125_Service.manualCommit();
					// 当前处于导出处理中的数据量
					expAmount += preFailAmount;
					// 若此次状态设置失败，则失败次数为expAmount
					preFailAmount = expAmount;
					// 没有可导出的数据跳出循环
					if (expAmount == 0) {
						break;
					}
					//柜台调拨（调出）单据导出至调拨（调出）单据标准接口表中，并更改同步状态【SynchFlag=2-->SynchFlag=3】
					exportDataIFPrtAllocation(map,preFailAmount);
					
					totalCount += expAmount;
					// 程序出现异常后，后面循环的批处理依然会遇到这样的问题。
					if (expAmount < BATCH_SIZE || flag == CherryBatchConstants.BATCH_WARNING) {
						break;
					}
				} catch (Exception e) {
					try {
						// 新后台数据源回滚
						binbat125_Service.manualRollback();
					} catch (Exception ex) {
						ex.printStackTrace();
					}
					// 失败件数
					failCount += preFailAmount;
					flag = CherryBatchConstants.BATCH_WARNING;
					
					BatchLoggerDTO batchLoggerDTO1 = new BatchLoggerDTO();
					// 数据同步状态从"可同步"更新为"同步处理中"失败！
					batchLoggerDTO1.setCode("EOT00113");
					batchLoggerDTO1.setLevel(CherryBatchConstants.LOGGER_ERROR);
					batchLoggerDTO1.addParam("调出");
					logger.BatchLogger(batchLoggerDTO1);
					CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
					cherryBatchLogger.BatchLogger(batchLoggerDTO1, e);
					fReason = String.format("更新柜台调拨单据（调出）数据同步状态从【SynchFlag=1】更新为【SynchFlag=2】时失败，具体查看Log日志",e.getMessage());
					logger.outExceptionLog(e);
					break;
				}
			}
			programEnd(map);
			outMessage();
			return flag;
	}
	/**
	 * 检索是否存在synchFlag=2的新后台数据: 1）若存在，查看标准接口中是否有相应单据的记录:
	 * 1、若都存在，将新后台相应单据的synchFlag置为3； 2、若不存在，则synchFlag=2【单据归为正常的需要导出的数据范围】;
	 * 2）若不存在，synchFlag=2【单据归为正常的需要导出的数据范围】。
	 * 
	 * @param map
	 * @return int: 剔除已成功导出但synchFlag=2的调拨单据后的数据量
	 * @throws CherryException
	 */
	private int getPrtAllocatOutAmountBySynch(Map<String, Object> map) throws CherryBatchException {
		map.put("synchFlag", SYNCH_FLAG_2);
		List<String> prtAllocOutListBySynch = binbat125_Service.getPrtAllocatOutListBySynch(map);
		// 判断是否有之前程序执行失败时同步状态为SynchFlag=2的单据
		if (!CherryBatchUtil.isBlankList(prtAllocOutListBySynch)) {
			Map<String,Object> updateSynchMap = new HashMap<String, Object>();
			updateSynchMap.putAll(map);
			updateSynchMap.put("allocationOutNoIFList", prtAllocOutListBySynch);
			// 根据新后台的单据号->查询柜台调拨单据标准接口表的单据号List
			List<String> existsFromPrtAllocOutList = binbat125_Service.getExistsPrtAllocatOutList(updateSynchMap);
			try {
				// 柜台调拨单据标准接口表中存在synchFlag=2的数据，即导出成功，状态更新不成功
				if (!CherryBatchUtil.isBlankList(existsFromPrtAllocOutList)) {
					for (String billCode : existsFromPrtAllocOutList) {
						updateSynchMap.put("billCode",billCode);
						// 将已经成功导出到标准接口表中的对应新后台的调拨单单据的同步状态由2置为3
						updatePrtAllocatOutFlag(updateSynchMap, 2);
						}
					// 新后台数据源提交
					binbat125_Service.manualCommit();
				}
			} catch (Exception e) {
				try {
					// 新后台数据源回滚
					binbat125_Service.manualRollback();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				flag = CherryBatchConstants.BATCH_WARNING;

				BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
				// 数据同步状态从"同步处理中"更新为"已完成"失败
				batchLoggerDTO.setCode("EOT00111");
				batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
				batchLoggerDTO.addParam("调出");
				logger.BatchLogger(batchLoggerDTO);
				CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
				cherryBatchLogger.BatchLogger(batchLoggerDTO, e);
				fReason = String.format("更新柜台调拨单据（调出）数据同步状态从【SynchFlag=2】更新为【SynchFlag=3】时失败，具体查看Log日志",e.getMessage());
				logger.outExceptionLog(e);
			}
			// 去除已经成功导出到标准接口中的调拨单号list
			prtAllocOutListBySynch.removeAll(existsFromPrtAllocOutList);
		}
		// 返回synchFlag=2且还未导出到标准接口中的数据量
		return prtAllocOutListBySynch.size();
	}
	/**
	 * 设置处理状态字段SynchFlag identifyId=1:SynchFlag(1->2)
	 * identifyId=2:SynchFlag(2->3)
	 * 
	 * @param map
	 * @param identifyId
	 * @return
	 * @throws CherryBatchException
	 */
	private int updatePrtAllocatOutFlag(Map<String, Object> map, int identifyId) throws CherryBatchException {
		Map<String, Object> updateMap = new HashMap<String, Object>();
		updateMap.putAll(map);
		if (identifyId == 1) {
			// 导出状态由可导出（SynchFlag：1）设置为导出处理中（SynchFlag：2）
			updateMap.put("synchFlag_Old", SYNCH_FLAG_1);
			updateMap.put("synchFlag_New", SYNCH_FLAG_2);
			return binbat125_Service.updateSynchFlag(updateMap);
		} else if (identifyId == 2) {
			// 导出状态由导出处理中（SynchFlag：2）设置为导出完成（SynchFlag：3）
			updateMap.put("synchFlag_Old", SYNCH_FLAG_2);
			updateMap.put("synchFlag_New", SYNCH_FLAG_3);
			return binbat125_Service.updateSynchFlag(updateMap);
		} else {
			return -1;
		}
	}
	/**
	 * 获得调拨单（调出）主单数据及明细数据导入相关柜台调拨单据标准接口表
	 * 
	 * @param map
	 * @throws CherryBatchException
	 */
	private void exportDataIFPrtAllocation(Map<String, Object> map,int preFailAmount) throws CherryBatchException {
		try {
			// 获得同步状态【SynchFlag=2】的调拨主单详细数据,SQL中直接查出
			List<Map<String, Object>> allocaOutList = binbat125_Service.getPrtAllocatOut(map);
			if (!CherryBatchUtil.isBlankList(allocaOutList)) {
					String[] allocatiobIdArr = new String[allocaOutList.size()];
					for (int i = 0; i < allocaOutList.size(); i++) {
						allocatiobIdArr[i] = ConvertUtil.getString(allocaOutList.get(i).get("allocationOutID"));
					}
					map.put("allocatiobIdArr", allocatiobIdArr);
					// 获得同步状态【SynchFlag=2】的调拨明细单详细数据，SQL中直接查出
					List<Map<String, Object>> allocaOutDetailList = binbat125_Service.getPrtAllocatOutDetail(map);
					//获得调拨单（调出）主单数据及明细数据导入相关柜台调拨单据标准接口表
					binbat125_Service.insertIFAllocation(allocaOutList);
					binbat125_Service.insertIFAllocationDetail(allocaOutDetailList);
					//第三方数据源提交
					binbat125_Service.tpifManualCommit();
				}
			try {
				// 更新新后台同步状态【SynchFlag=3】
				updatePrtAllocatOutFlag(map, 2);
				// 新后台数据源提交
				binbat125_Service.manualCommit();
			} catch (Exception e) {
				try {
					// 新后台数据源回滚
					binbat125_Service.manualRollback();
				} catch (Exception ex) {
					logger.outExceptionLog(ex);
				}
				failCount += preFailAmount;
				flag = CherryBatchConstants.BATCH_WARNING;

				BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
				// 数据同步状态从"同步处理中"更新为"已完成"失败
				batchLoggerDTO.setCode("EOT00111");
				batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
				batchLoggerDTO.addParam("调出");
				logger.BatchLogger(batchLoggerDTO);
				CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
				cherryBatchLogger.BatchLogger(batchLoggerDTO, e);
				fReason = String.format("更新柜台调拨单据（调出）数据同步状态从【SynchFlag=2】更新为【SynchFlag=3】时失败，具体查看Log日志",e.getMessage());
				logger.outExceptionLog(e);
			}
		} catch (Exception e) {
			try {
				binbat125_Service.tpifManualRollback();
			} catch (Exception ex) {
				
			}
			failCount += preFailAmount;
			flag = CherryBatchConstants.BATCH_ERROR;

			BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
			batchLoggerDTO.setCode("EOT00112");
			batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
			batchLoggerDTO.addParam("调出");
			logger.BatchLogger(batchLoggerDTO);
			CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
			cherryBatchLogger.BatchLogger(batchLoggerDTO, e);
			fReason = String.format("新后台柜台调拨单据（调出）数据导入相关柜台调拨单据标准接口表时失败，具体查看Log日志",e.getMessage());
			logger.outExceptionLog(e);
		}
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
		paraMap.put("SCNT", totalCount - failCount);
		paraMap.put("FCNT", failCount);
		paraMap.put("FReason", fReason);
		binbecm01_IF.insertJobRunHistory(paraMap);
	}
//	/**
//	 * 程序出现主要异常时，记录（插入）Job运行失败履历表 暂时不使用
//	 * @param map
//	 */
//	private void programFaildRecord(Map<String, Object> map) {
//		map.put("UnionIndex", unionIndex);// 单据号
//		map.put("UnionIndex1", "");
//		map.put("UnionIndex2", "");
//		map.put("UnionIndex3", "");
//		map.put("ErrorMsg", errorMsg);
//		map.put("Comments", comments);
//		binbecm01_IF.insertJobRunFaildHistory(map);
//	}
	/**
	 * 程序初始化 init
	 * 
	 * @param map
	 * @return
	 */
	private void init(Map<String, Object> map) {
		// BatchCD
		// 来自VSS$/01.Cherry/02.设计文档/01.概要设计/00.各种一览/【新设】CherryBatch一览.xlsx
		map.put("JobCode", "BAT125");

		// 程序【开始运行时间】
		String runStartTime = binbat125_Service.getSYSDateTime();
		// 作成日时
		map.put("RunStartTime", runStartTime);
		// 更新程序名
		map.put(CherryBatchConstants.UPDATEPGM, "BINBAT125_BL");
		// 作成程序名
		map.put(CherryBatchConstants.CREATEPGM, "BINBAT125_BL");
		// 作成者
		map.put(CherryBatchConstants.CREATEDBY,CherryBatchConstants.UPDATE_NAME);
		// 更新者
		map.put(CherryBatchConstants.UPDATEDBY,CherryBatchConstants.UPDATE_NAME);
		// 是否测试模式
		String testMod = binOLCM14_BL.getConfigValue("1080",String.valueOf(map.get("organizationInfoId")),String.valueOf(map.get("brandInfoId")));
		map.put("testMod", testMod);
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
		// 失败件数
		BatchLoggerDTO batchLoggerDTO5 = new BatchLoggerDTO();
		batchLoggerDTO5.setCode("IIF00005");
		batchLoggerDTO5.setLevel(CherryBatchConstants.LOGGER_INFO);
		batchLoggerDTO5.addParam(String.valueOf(failCount));
		// 处理总件数
		logger.BatchLogger(batchLoggerDTO1);
		// 成功总件数
		logger.BatchLogger(batchLoggerDTO2);
		// 失败件数
		logger.BatchLogger(batchLoggerDTO5);
	}
}
