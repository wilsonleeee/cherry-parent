package com.cherry.ot.yin.bl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.batcmbussiness.bl.BINBECM01_BL;
import com.cherry.cm.cmbussiness.service.BINOLCM06_Service;
import com.cherry.cm.core.BatchLoggerDTO;
import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryBatchException;
import com.cherry.cm.core.CherryBatchLogger;
import com.cherry.cm.util.CherryBatchUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.ot.yin.service.BINBAT116_Service;

/**
 * 颖通接口：礼品领用单据导出(颖通)BL
 * 
 * @author lzs
 * 
 * @version 2015-07-03
 * 
 */
public class BINBAT116_BL {

	/** BATCH LOGGER */
	private static CherryBatchLogger logger = new CherryBatchLogger(BINBAT116_BL.class);

	@Resource(name = "binBAT116_Service")
	private BINBAT116_Service binBAT116_Service;
	
	@Resource(name = "binOLCM06_Service")
	private BINOLCM06_Service binOLCM06_Service;

	/** JOB执行相关共通 BL **/
	@Resource
	private BINBECM01_BL binBECM01_BL;

	/** BATCH处理标志 */
	private int flag = CherryBatchConstants.BATCH_SUCCESS;

	/** 共通Map参数 */
	private Map<String, Object> comMap;

	/** 同步状态:1:可导出 */
	private final String SYNCH_FLAG_1 = "1";

	/** 同步状态:2：处理中 */
	private final String SYNCH_FLAG_2 = "2";

	/** 同步状态:3：处理完成 */
	private final String SYNCH_FLAG_3 = "3";

	/** 处理总条数 */
	private int totalCount = 0;
	/** 失败条数 */
	private int failCount = 0;

	/** 失败的主要原因，受字段长度限制，这里只要记录主要原因即可 */
	private String fReason = "";

	public int tran_batchExportGiftDraw(Map<String, Object> map) throws Exception {
		// 查询参数初始化
		comMap = getComMap(map);
		try {
			//初始化处理：更新礼品领用单数据状态为【SynchFlag=null】和协同区分为【CounterSynergyFlag=1】的数据
			updGiftDrawFlag(comMap, 3);

			// 新后台数据源提交
			binBAT116_Service.manualCommit();
		} catch (Exception e) {
			try {
				binBAT116_Service.manualRollback();
			} catch (Exception ex) {
			}
			BatchLoggerDTO batchLogger = new BatchLoggerDTO();
			batchLogger.setCode("EOT00085");
			batchLogger.setLevel(CherryBatchConstants.LOGGER_ERROR);
			batchLogger.addParam("null");
			batchLogger.addParam("1");
			logger.BatchLogger(batchLogger, e);
			fReason = String.format("礼品领用单同步状态从【SynchFlag=NUll】更新为【SynchaFlag=1】时更新失败，具体见Log日志");
			flag = CherryBatchConstants.BATCH_ERROR;
		} 
		try {
			// 循环次数
			int i = 0;
			while (true) {
				// 预处理可能导出失败件数
				int giftDrawFailCount = 0;

				// Step1:查看新后台礼品领用单中是否存在处于同步中且未导入到SAP销售接口的数据【synchFlag=2)】
				int noImportCount = getGiftDrawAmountBySynch();

				// 更新新后台礼品领用单数据 从[synchFlag=1]更新为[synchFlag=2]
				Map<String, Object> s1ToS2Map = new HashMap<String, Object>();
				s1ToS2Map.putAll(comMap);
				s1ToS2Map.put("batchSize", CherryBatchConstants.BATCH_SIZE);
				int updCount = updGiftDrawFlag(s1ToS2Map, 1);

				// 新后台数据源事务提交
				binBAT116_Service.manualCommit();

				// 需要导入SAP销售接口表的数量
				int impCount = noImportCount + updCount;

				// 预处理失败的总量
				giftDrawFailCount = impCount;

				// 总数量
				totalCount += impCount;

				// 没有数据则停止程序
				if (impCount == 0) {
					break;
				}

				// 取得新后台礼品领用单数据状态为【SynchFlag=2】的数据，插入数据至SAP销售接口表，成功后更新数据状态为【SynchFlag=3】
				insertDataToGiftDraw(giftDrawFailCount);

				i++;
				// 每次批次处理数量的初始值
				int startSize = (i - 1) * CherryBatchConstants.BATCH_SIZE + 1;
				// 每次批次处理数量的结束值
				int endSize = CherryBatchConstants.BATCH_SIZE * i;
				BatchLoggerDTO processLogger = new BatchLoggerDTO();
				processLogger.setCode("EOT00075");
				processLogger.setLevel(CherryBatchConstants.LOGGER_INFO);
				processLogger.addParam(String.valueOf(startSize));
				processLogger.addParam(String.valueOf(endSize));
				logger.BatchLogger(processLogger);

				// 失败时结束批处理 程序出现异常后，后面循环的批处理依然会遇到这样的问题。
				if (updCount < CherryBatchConstants.BATCH_SIZE || flag == CherryBatchConstants.BATCH_WARNING) {
					break;
				}
			}
		} catch (Exception e) {
			try {
				binBAT116_Service.manualRollback();
			} catch (Exception e1) {
			}
			BatchLoggerDTO batchLoggerDTO1 = new BatchLoggerDTO();
			batchLoggerDTO1.setCode("EOT00084");
			batchLoggerDTO1.setLevel(CherryBatchConstants.BATCH_WARNING);
			logger.BatchLogger(batchLoggerDTO1, e);
			CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
			cherryBatchLogger.BatchLogger(batchLoggerDTO1, e);
			fReason = String.format("礼品领用单数据导出至SAP销售接口失败，具体见Log日志");
			flag = CherryBatchConstants.BATCH_WARNING;
		}
		programEnd(comMap);
		// 输出处理结果信息
		outMessage();
		return flag;
	}

	/**
	 * 产品礼品领用单数据导出至SAP销售接口表
	 * 
	 * @param int：一批导出的数据量
	 * 
	 */
	private void insertDataToGiftDraw(int preFailAmount) throws CherryBatchException {
		Map<String, Object> searchMap = new HashMap<String, Object>();
		searchMap.putAll(comMap);
		searchMap.put("synchFlag", SYNCH_FLAG_2);
		searchMap.put("batchSize", CherryBatchConstants.BATCH_SIZE);
		try {
			// 查询礼品领用单据状态为【SynchFlag=2】的数据
			List<Map<String, Object>> giftDrawList = binBAT116_Service.GiftDrawDetailQuery(searchMap);
			List<String> DocEntryList = new ArrayList<String>();
			if (!CherryBatchUtil.isBlankList(giftDrawList)) {
				try {
					binBAT116_Service.insertCPSImportSales(giftDrawList);
					
					// 第三方数据源提交
					binBAT116_Service.tpifManualCommit();
					try {
						for (Map<String, Object> DocEntryMap : giftDrawList) {
							DocEntryList.add(ConvertUtil.getString(DocEntryMap.get("DocEntry")));
						}
						// 导出数据至SAP销售接口成功后，更新礼品领用单数据
						searchMap.put("billNoList", DocEntryList);
						updGiftDrawFlag(searchMap, 2);

						// 新后台数据源提交
						binBAT116_Service.manualCommit();

						// list使用完毕，置为NULL
						DocEntryList = null;
						
						// List使用完毕，置为NUll
						giftDrawList = null;
					} catch (Exception e) {
						try {
							// 新后台数据源回滚
							binBAT116_Service.manualRollback();
						} catch (Exception e1) {
						}
						BatchLoggerDTO batchLoggerDTO1 = new BatchLoggerDTO();
						batchLoggerDTO1.setCode("EOT00085");
						batchLoggerDTO1.setLevel(CherryBatchConstants.BATCH_WARNING);
						batchLoggerDTO1.addParam("2");
						batchLoggerDTO1.addParam("3");
						logger.BatchLogger(batchLoggerDTO1, e);
						CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
						cherryBatchLogger.BatchLogger(batchLoggerDTO1, e);
						fReason = String.format("礼品领用单同步状态从【SynchFlag=2】更新为【SynchaFlag=3】时更新失败，具体见Log日志");
						flag = CherryBatchConstants.BATCH_WARNING;
					}
				} catch (Exception e) {
					try {
						// 第三方数据源回滚
						binBAT116_Service.tpifManualRollback();
					} catch (Exception e1) {

					}
					BatchLoggerDTO batchLoggerDTO1 = new BatchLoggerDTO();
					batchLoggerDTO1.setCode("EOT00084");
					batchLoggerDTO1.setLevel(CherryBatchConstants.BATCH_WARNING);
					logger.BatchLogger(batchLoggerDTO1, e);
					CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
					cherryBatchLogger.BatchLogger(batchLoggerDTO1, e);
					fReason = String.format("礼品领用单数据导出至SAP销售接口失败，具体见Log日志");
					flag = CherryBatchConstants.BATCH_WARNING;
				}
			}
		} catch (Exception e) {
			// 失败件数
			failCount += preFailAmount;
			BatchLoggerDTO batchLoggerDTO1 = new BatchLoggerDTO();
			batchLoggerDTO1.setCode("EOT00084");
			batchLoggerDTO1.setLevel(CherryBatchConstants.BATCH_WARNING);
			logger.BatchLogger(batchLoggerDTO1, e);
			CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
			cherryBatchLogger.BatchLogger(batchLoggerDTO1, e);
			fReason = String.format("礼品领用单数据导出至SAP销售接口失败，具体见Log日志");
			flag = CherryBatchConstants.BATCH_WARNING;
		}
	}

	/**
	 * 检索是否存在synchFlag=2的新后台数据：
	 * 存在：查看SAP销售接口中是否有相应单据的记录-->有：新后台相应单据更改为【synchFlag=3】；无：礼品领用单据归为正常的需要导出的数据范围【synchFlag=2】
	 * 不存在：新后台没有同步状态为【synchFlag=2】的数据
	 * @param map
	 * @return int: 剔除已成功导出 剩余未导出同步状态为【SynchFlag=2】的礼品领用单的数据
	 * @throws CherryBatchException
	 */
	private int getGiftDrawAmountBySynch() throws CherryBatchException {
		Map<String, Object> giftDrawMap = new HashMap<String, Object>();
		giftDrawMap.putAll(comMap);
		giftDrawMap.put("synchFlag", SYNCH_FLAG_2);

		// 取得礼品领用单主表中synchFlag=2的单据号List
		List<String> billNoList = binBAT116_Service.getBillNoListBySynch(giftDrawMap);
		if (!CherryBatchUtil.isBlankList(billNoList)) {
			giftDrawMap.put("billNoList", billNoList);

			// 根据新后台的单据号->查询SAP销售接口表单据号List
			List<String> billNoFromGiftDrawList = binBAT116_Service.getBillNoFromGiftDraw(giftDrawMap);

			if (!CherryBatchUtil.isBlankList(billNoFromGiftDrawList)) {
				Map<String, Object> updFlagMap = new HashMap<String, Object>();
				updFlagMap.putAll(comMap);
				updFlagMap.put("billNoList", billNoFromGiftDrawList);
				// SAP销售接口表中如果存在synchFlag=2的数据，即导出成功,更新已查出单据号的状态为处理完成(SynchFlag=3)
				updGiftDrawFlag(updFlagMap, 2);

				// 新后台数据源提交
				binBAT116_Service.manualCommit();
			}

			// 剔除已经成功导出至SAP销售接口中的礼品领用单单据号List
			billNoList.removeAll(billNoFromGiftDrawList);

			// List使用完毕，置为NULL
			billNoFromGiftDrawList = null;
		}
		int waitInsertCount = billNoList.size();

		// List使用完毕，置为NUll
		billNoList = null;

		return waitInsertCount;
	}

	/**
	 * 更新处理状态字段SynchFlag identifyId=1:SynchFlag(1->2)
	 * identifyId=2:SynchFlag(2->3) identifyId=3:SynchFlag(null->1)
	 * 
	 * @param map
	 * @param identifyId
	 * @return
	 * @throws CherryBatchException
	 */
	private int updGiftDrawFlag(Map<String, Object> map, int identifyId) throws CherryBatchException {
		Map<String, Object> updateMap = new HashMap<String, Object>();
		updateMap.putAll(map);
		if (identifyId == 1) {
			// 导出状态由可导出（SynchFlag = 1）更新为导出处理中（SynchFlag=2）
			updateMap.put("synchFlag_Old", SYNCH_FLAG_1);
			updateMap.put("synchFlag_New", SYNCH_FLAG_2);
			return binBAT116_Service.updateSynchFlag(updateMap);
		} else if (identifyId == 2) {
			// 导出状态由导出处理中（SynchFlag = 2）更新为处理完成（SynchFlag = 3）
			updateMap.put("synchFlag_Old", SYNCH_FLAG_2);
			updateMap.put("synchFlag_New", SYNCH_FLAG_3);
			return binBAT116_Service.updateSynchFlag(updateMap);
		} else if (identifyId == 3) {
			// 导出状态由空状态（SynchFlag = NULL）更新为可导出（SynchFlag = 1）
			updateMap.put("synchFlag_New", SYNCH_FLAG_1);
			return binBAT116_Service.updateSynchFlagByCounterSync(updateMap);
		} else {
			return -1;
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
			paraMap.put("JobCode", "BAT116");
			paraMap.put("flag", flag);
			paraMap.put("TargetDataCNT", totalCount);
			paraMap.put("SCNT", totalCount - failCount);
			paraMap.put("FCNT", failCount);
			paraMap.put("FReason", fReason);
			paraMap.put("UCNT", "");
			paraMap.put("ICNT", "");
			binBECM01_BL.insertJobRunHistory(paraMap);
	}

	/**
	 * 共通Map
	 * 
	 * @param map
	 * @return
	 */
	private Map<String, Object> getComMap(Map<String, Object> map) {
		Map<String, Object> baseMap = new HashMap<String, Object>();
		baseMap.putAll(map);
		// 程序运行开始时间
		baseMap.put("RunStartTime", binBAT116_Service.getSYSDateTime());
		// 更新程序名
		baseMap.put(CherryBatchConstants.UPDATEPGM, "BINBAT116");
		// 作成程序名
		baseMap.put(CherryBatchConstants.CREATEPGM, "BINBAT116");
		// 作成者
		baseMap.put(CherryBatchConstants.CREATEDBY, CherryBatchConstants.UPDATE_NAME);
		// 更新者
		baseMap.put(CherryBatchConstants.UPDATEDBY, CherryBatchConstants.UPDATE_NAME);
		// 所属组织
		baseMap.put(CherryBatchConstants.ORGANIZATIONINFOID, map.get(CherryBatchConstants.ORGANIZATIONINFOID));
		// 品牌
		baseMap.put(CherryBatchConstants.BRANDINFOID, map.get(CherryBatchConstants.BRANDINFOID));
		
		//品牌code
		baseMap.put(CherryBatchConstants.BRAND_CODE,binOLCM06_Service.getBrandCode(map));

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
		// 导出件数
		BatchLoggerDTO batchLoggerDTO2 = new BatchLoggerDTO();
		batchLoggerDTO2.setCode("IIF00007");
		batchLoggerDTO2.setLevel(CherryBatchConstants.LOGGER_INFO);
		batchLoggerDTO2.addParam(String.valueOf(totalCount - failCount));
		// 失败件数
		BatchLoggerDTO batchLoggerDTO3 = new BatchLoggerDTO();
		batchLoggerDTO3.setCode("IIF00005");
		batchLoggerDTO3.setLevel(CherryBatchConstants.LOGGER_INFO);
		batchLoggerDTO3.addParam(String.valueOf(failCount));
		// 处理总件数
		logger.BatchLogger(batchLoggerDTO1);
		// 插入件数
		logger.BatchLogger(batchLoggerDTO2);
		// 失败件数
		logger.BatchLogger(batchLoggerDTO3);
	}
}
