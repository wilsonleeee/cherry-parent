package com.cherry.middledbout.stand.sale.bl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.batcmbussiness.interfaces.BINBECM01_IF;
import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.core.BatchLoggerDTO;
import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryBatchException;
import com.cherry.cm.core.CherryBatchLogger;
import com.cherry.cm.util.CherryBatchUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.ia.common.ProductConstants;
import com.cherry.middledbout.stand.sale.service.SaleInfo_Service;

/**
 * 标准接口：销售数据导出到标准接口表(销售)BL
 * 
 * @author lzs
 * 
 */
public class SaleInfo_BL {
	/** 打印当前类的日志信息 **/
	private static CherryBatchLogger logger = new CherryBatchLogger(SaleInfo_BL.class);
	Logger log = LoggerFactory.getLogger(SaleInfo_BL.class.getName());
	@Resource
	private SaleInfo_Service saleInfo_Service;
	
	static boolean lockFlag = true;

	/** BATCH处理标志 */
	private int flag = CherryBatchConstants.BATCH_SUCCESS;

	/** 系统配置项 共通 **/
	@Resource(name = "binOLCM14_BL")
	private BINOLCM14_BL binOLCM14_BL;
	
	/** JOB执行相关共通 IF */
	@Resource(name="binbecm01_IF")
	private BINBECM01_IF binbecm01_IF;

	/** 处理数据的上限数量 **/
	private final int UPDATE_SIZE = 2000;
	
	/** 每批次(页)处理数量 100 */
	private final int BATCH_SIZE = 200;
	
	/** 处理总件数 **/
	private int totalCount = 0;
	/** 失败件数 **/
	private int failCount = 0;
	/** 销售记录同步状态，1:可同步 **/
	private final String SYNCH_FLAG_1 = "1";
	/** 销售记录同步状态，2:同步处理中 */
	private final String SYNCH_FLAG_2 = "2";
	/** 销售记录同步状态，3:已完成 **/
	private final String SYNCH_FLAG_3 = "3";
	
	/** 失败的主要原因，受字段长度限制，这里只要记录主要原因即可 */
	private String fReason = "";

	private Map<String, Object> comMap;

	
	/** JOB执行锁*/
	private static int execFlag = 0;
	
	/**
	 * 产品列表的Batch处理
	 * 
	 * @param map
	 * @return
	 * @throws CherryBatchException
	 */
	@SuppressWarnings("unchecked")
	public int tran_batchSaleInfo(Map<String, Object> map)
			throws CherryBatchException,Exception {
		synchronized (this) {
			// 已有其他线程正在执行该JOB
			if (1 == execFlag) {
				// 启动JOB发生异常
				BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
				batchLoggerDTO.setCode("ECM00004");
				batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
				CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(
						this.getClass());
				try {
					cherryBatchLogger.BatchLogger(batchLoggerDTO);
				} catch (CherryBatchException e) {
					logger.outExceptionLog(e);
				}
				return flag;
			}
			// 锁定
			execFlag = 1;
		}
		
		try{
			// BatchCD 来自VSS$/01.Cherry/02.设计文档/01.概要设计/00.各种一览/【新设】CherryBatch一览.xlsx
			map.put("JobCode", "BAT106");
			
			// 程序【开始运行时间】
			String runStartTime = saleInfo_Service.getSYSDateTime();
			// 作成日时
			map.put("RunStartTime", runStartTime);
			
			// 取得Job控制程序的数据截取开始时间及结束时间
			Map<String, Object> jobControlInfoMap = binbecm01_IF.getJobControlInfo(map);
			
			// 程序【截取数据开始时间】
			map.put("TargetDataStartTime", jobControlInfoMap.get("TargetDataStartTime"));
			// 程序【截取数据结束时间】
			map.put("TargetDataEndTime", jobControlInfoMap.get("TargetDataEndTime"));

			// 初始化
			comMap = getComMap(map);
			comMap.putAll(map);
			
			// 处理之前失败的销售数据
			handleFaildData(map);
			
			// 处理符合条件的销售数据
			handleNormalData(map);
			
			// 日志
			outMessage();
			
			// 程序结束时，处理Job共通(更新Job控制表 、插入Job运行履历表)
			programEnd(comMap);

		}catch(Exception e){
			throw e;
		}finally{
			// 释放锁
			execFlag = 0;
		}
		return flag;
	}
	
	/**
	 * 处理符合条件的销售数据
	 * 
	 * @param paraMap
	 * @throws CherryBatchException,Exception 
	 */
	@SuppressWarnings("unchecked")
	private void handleNormalData(Map<String, Object> paraMap) throws CherryBatchException,Exception{
		// 上一批次(页)最后一条记录
		String bathLastRowID = "";
		
		while (true) {

			// 查询销售列表
			Map<String, Object> selSaleRecordMap = new HashMap<String, Object>();
			selSaleRecordMap.putAll(comMap);
			selSaleRecordMap.put("batchSize", BATCH_SIZE);
			selSaleRecordMap.put("bathLastRowID", bathLastRowID);
			
			// 查询新后台销售数据信息
			List<Map<String, Object>> saleRecordList = saleInfo_Service.getSaleRecordList(selSaleRecordMap);
			if (CherryBatchUtil.isBlankList(saleRecordList)) {
				break;
			} 
			String[] saleRecordIDArr = new String[saleRecordList.size()];
			for(int i =0;i<saleRecordList.size();i++){
				saleRecordIDArr[i]=saleRecordList.get(i).get("BIN_SaleRecordID").toString();
			}
			selSaleRecordMap.put("saleRecordIDArr", saleRecordIDArr);
			// 查询新后台销售数据明细信息
			List<Map<String, Object>> saleRecordDetailList = saleInfo_Service.getSaleRecordDetailList(selSaleRecordMap);
			//查询新后台销售数据支付明细信息
			List<Map<String,Object>> salePayList = saleInfo_Service.getSalePayList(selSaleRecordMap);
			
			 // 统计总条数
			 totalCount += saleRecordList.size();
			try{
				// 处理新后台销售的数据,插入到标准接口表
				updateDate(paraMap, saleRecordList,saleRecordDetailList,salePayList,1);
				
			}catch(Exception e){
				
				flag = CherryBatchConstants.BATCH_ERROR;
				
				failCount += saleRecordList.size();
				
				// 待定
//					binbekdcpi01_Service.manualRollback();
				
				BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
				batchLoggerDTO.setCode("EOT00081");
				batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
				logger.BatchLogger(batchLoggerDTO, e);
				
				// 处理失败后，跳转至下一批次(页)
//					continue;
			}
			
			 // 当前批次最后一条数据的RowID赋给bathLastRowID，用于当前任务下一批次(页)产品库存数据的筛选条件
			bathLastRowID = CherryBatchUtil.getString(saleRecordList.get(saleRecordList.size()- 1).get("BIN_SaleRecordID"));
			 
			 // 销售为空或少于一批次(页)处理数量，跳出循环
			 if (saleRecordList.size() < BATCH_SIZE) {
				 // GC
				 saleRecordList = null;
				 
				 break;
			 }
			 // GC
			 saleRecordList = null;
		}
	}
	
	/**
	 * 处理之前失败的销售数据
	 * 
	 * @param paraMap
	 * @throws CherryBatchException,Exception 
	 */
	@SuppressWarnings("unchecked")
	private void handleFaildData(Map<String, Object> paraMap) throws CherryBatchException,Exception{
		// 上一批次(页)最后一条记录
		String bathLastRowID = "";
		
		while (true) {
			
			// 查询销售列表
			Map<String, Object> selFaildSaleRecordMap = new HashMap<String, Object>();
			selFaildSaleRecordMap.putAll(comMap);
			selFaildSaleRecordMap.put("batchSize", BATCH_SIZE);
			selFaildSaleRecordMap.put("bathLastRowID", bathLastRowID);
			// 查询新后台销售数据信息
			List<Map<String, Object>> faildSaleRecordList = saleInfo_Service.getFaildSaleRecordList(selFaildSaleRecordMap);

			
			if (CherryBatchUtil.isBlankList(faildSaleRecordList)) {
				break;
			} else {
				// 统计总条数
				totalCount += faildSaleRecordList.size();
				try{
					
					// 查询新后台销售数据明细信息
					String[] saleRecordIDArr = new String[faildSaleRecordList.size()];
					for(int i =0;i<faildSaleRecordList.size();i++){
						saleRecordIDArr[i]=faildSaleRecordList.get(i).get("BIN_SaleRecordID").toString();
					}
					selFaildSaleRecordMap.put("saleRecordIDArr", saleRecordIDArr);
					List<Map<String, Object>> falidSaleRecordDetailList = saleInfo_Service.getFaildSaleRecordDetailList(selFaildSaleRecordMap);
					
					//查询新后台销售数据支付信息
					List<Map<String,Object>> salePayList = saleInfo_Service.getSalePayList(selFaildSaleRecordMap);
					
					// 处理新后台销售的数据
					updateDate(paraMap, faildSaleRecordList,falidSaleRecordDetailList,salePayList,0);
					
				}catch(Exception e){
					
					flag = CherryBatchConstants.BATCH_ERROR;
					
					failCount += faildSaleRecordList.size();
					
					// 待定
//					binbekdcpi01_Service.manualRollback();
					
					BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
					batchLoggerDTO.setCode("EOT00080");
					batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
					logger.BatchLogger(batchLoggerDTO, e);
					
					// 处理失败后，跳转至下一批次(页)
//					continue;
				}
				
			}
			
			// 当前批次最后一条数据的RowID赋给bathLastRowID，用于当前任务下一批次(页)产品库存数据的筛选条件
			bathLastRowID = CherryBatchUtil.getString(faildSaleRecordList.get(faildSaleRecordList.size()- 1).get("BillCode"));
			
			// 销售为空或少于一批次(页)处理数量，跳出循环
			if (faildSaleRecordList.size() < BATCH_SIZE) {
				break;
			}
		}
	}
	
	/**
	 * 处理新后台销售数据
	 * @param map
	 * @param saleRecordList
	 * @param saleRecordDetailList
	 * @param dataClass 数据类别 1：正常数据 0：失败数据
	 * @throws CherryBatchException,Exception 
	 */
	@SuppressWarnings("unchecked")
	private void updateDate(Map<String,Object> map,List<Map<String,Object>> saleRecordList,List<Map<String,Object>> saleRecordDetailList,List<Map<String,Object>> salePayList,int dataClass) 
			throws CherryBatchException,Exception{
		
		try {

			// 插入销售单接口表
			saleInfo_Service.insertIFSale(saleRecordList);
			// 插入销售单明细接口表
			saleInfo_Service.insertIFSaleDetail(saleRecordDetailList);
			// 插入销售单支付接口表
			saleInfo_Service.insertIFSalePayDetail(salePayList);
			
			try {

				// 如果当前处理的是Job运行失败履历表的数据，那么当前如果成功就将其从运行失败履历表中删除
				if( 0 == dataClass){
					List<Map<String,Object>> faildSaleRecordList = new ArrayList<Map<String,Object>>();
					faildSaleRecordList.addAll(saleRecordList);
					for(Map<String,Object> faildSaleRecord : faildSaleRecordList){
						faildSaleRecord.putAll(comMap);
						faildSaleRecord.put("UnionIndex", faildSaleRecord.get("BillCode"));
					}
					// 删除 Job运行数据失败履历表
					binbecm01_IF.delAllJobRunFaildHistory(faildSaleRecordList);
					
					// GC
					faildSaleRecordList = null;
				}
				
				// 新后台数据源Commit
				saleInfo_Service.manualCommit();
				
				// 第三方数据源commit
				saleInfo_Service.tpifManualCommit();

			} catch (Exception e) {
				try {

					// 新后台数据源回滚
					saleInfo_Service.manualRollback();
					
					// 第三方数据源回滚
					saleInfo_Service.tpifManualRollback();

				} catch (Exception e2) {

				}
				
				fReason = "插入标准接口表失败，详细见日志。";
				
				// 失败件数
				failCount += saleRecordList.size();
				flag = CherryBatchConstants.BATCH_WARNING;
				BatchLoggerDTO batchLogger = new BatchLoggerDTO();
				batchLogger.setCode("EOT00082");
				batchLogger.setLevel(CherryBatchConstants.LOGGER_ERROR);
				logger.BatchLogger(batchLogger);
				CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
				cherryBatchLogger.BatchLogger(batchLogger, e);
			}
		} catch (Exception e) {
			log.error("===", e);
			try {

				// 第三方数据源回滚
				saleInfo_Service.tpifManualRollback();
				
				// 将失败的单据号存入Job运行失败履历表
				for( Map<String,Object> saleRecordMap : saleRecordList){
					// 设置失败履历表的参数
					Map<String,Object> falidMap = new HashMap<String, Object>();
					falidMap.putAll(comMap);
					falidMap.put("UnionIndex", saleRecordMap.get("BillCode"));
					
					falidMap.put("ErrorMsg", ",{\"" +e.getMessage().toString()+ "\"}");
					falidMap.put("Comments", "该批次某些数据失败，导致该批次整体失败。");
					// merge JobRunFaildHistory表
					binbecm01_IF.mergeJobRunFaildHistory(falidMap);
				}
				
				// 新后台数据源Commit
				saleInfo_Service.manualCommit();
				

			} catch (Exception e2) {

			}
			
			fReason = "插入标准接口表失败，详细见日志。";
			
			// 失败件数
			failCount += saleRecordList.size();
			flag = CherryBatchConstants.BATCH_WARNING;
			BatchLoggerDTO batchLogger = new BatchLoggerDTO();
			batchLogger.setCode("EOT00082");
			batchLogger.setLevel(CherryBatchConstants.LOGGER_ERROR);
			logger.BatchLogger(batchLogger);
			CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
			cherryBatchLogger.BatchLogger(batchLogger, e);
		}

	}

	/**
	 * 查询新后台销售数据是否存在同步处理中[synchFlag=2]
	 * 若存在，则查询新后台销售数据是否已被导出到销售接口表中【是：更改新后台状态,synchFlag
	 * =2--->synchFlag=3;否：等待下次数据处理】
	 * 
	 * @return
	 * @throws CherryBatchException
	 */
	@SuppressWarnings("unchecked")
	@Deprecated
	private int getSCRCountBySynch() throws CherryBatchException {
		// 取得新后台（syschFlag=2）的销售单数据号List
		Map<String, Object> synch2Map = new HashMap<String, Object>();
		synch2Map.putAll(comMap);
		synch2Map.put("synchFlag", SYNCH_FLAG_2);
		List<String> billCodeOfBackENDList = saleInfo_Service
				.getBillCodeSRListSync(synch2Map);

		if (null != billCodeOfBackENDList && !billCodeOfBackENDList.isEmpty()) {
			// 根据新后台[synchFlag=2]的销售单据号List查询销售接口是否存在（存在则说明已被新后台相应销售数据已导出到销售接口）
			Map<String, Object> bcMap = new HashMap<String, Object>();
			bcMap.put("billCodeList", billCodeOfBackENDList);
			List<String> billCodeList = saleInfo_Service
					.getBillCodeOfISList(bcMap);

			// 将已导出到销售接口的单据号作为条件更新新后台的同步状态([synchFlag=2]更新为[synchFlag=3])
			if (null != billCodeList && !billCodeList.isEmpty()) {
				Map<String, Object> upTo3Map = new HashMap<String, Object>();
				upTo3Map.putAll(comMap);
				upTo3Map.put("billCodeList", billCodeList);
				upTo3Map.put("synchFlag_New", SYNCH_FLAG_3);
				saleInfo_Service.upSaleRecordBySync(upTo3Map);

				// 新后台数据源Commit
				saleInfo_Service.manualCommit();

			}
			// 计算去除掉在销售接口上存在并更新成[synchFlag=3]的数据后还剩余为[synchFlag=2]的销售数据量
			billCodeOfBackENDList.removeAll(billCodeList);
		}
		return billCodeOfBackENDList.size();
	}

	/**
	 * 查询新后台销售数据表中同步状态为同步处理中[SynchFlag=2]的数据 新后台销售业务数据（详细数据）分别导入到销售主单接口（明细）表中
	 * 更新新后台销售数据表中的同步状态[SynchFlag=2----->SynchFlag=3]
	 * 
	 * @param prepFailCount
	 * @throws CherryBatchException
	 */
	@Deprecated
	private void insertSaleAndPay(int prepFailCount)
			throws CherryBatchException {
		try {
			Map<String, Object> queryMap = new HashMap<String, Object>();
			queryMap.putAll(comMap);
			queryMap.put("synchFlag", SYNCH_FLAG_2);
			// 查询新后台销售数据信息
			List<Map<String, Object>> srBySyncList = saleInfo_Service.getSaleRecordList(queryMap);

			// 查询新后台销售数据明细信息
			List<Map<String, Object>> srdBySyncList = saleInfo_Service.getSaleRecordDetailList(queryMap);

			if (!CherryBatchUtil.isBlankList(srBySyncList)
					&& !CherryBatchUtil.isBlankList(srBySyncList)) {
				// 插入销售单接口表
				saleInfo_Service.insertIFSale(srBySyncList);
				// 插入销售单明细接口表
				saleInfo_Service.insertIFSaleDetail(srdBySyncList);
				// 第三方数据源commit
				saleInfo_Service.tpifManualCommit();
				try {
					// 更改新后台销售业务数据同步状态[SynchFlag=2------->SynchFlag=3]
					Map<String, Object> upSyn2To3Map = new HashMap<String, Object>();
					upSyn2To3Map.putAll(comMap);
					upSyn2To3Map.put("synchFlag_Old", SYNCH_FLAG_2);
					upSyn2To3Map.put("synchFlag_New", SYNCH_FLAG_3);
					saleInfo_Service.upSaleRecordBySync(upSyn2To3Map);

					// 新后台数据源Commit
					saleInfo_Service.manualCommit();

				} catch (Exception e) {
					try {

						// 新后台数据源回滚
						saleInfo_Service.manualRollback();

					} catch (Exception e2) {

					}
					// 失败件数
					failCount += prepFailCount;
					flag = CherryBatchConstants.BATCH_WARNING;
					BatchLoggerDTO batchLogger = new BatchLoggerDTO();
					batchLogger.setCode("EOT00064");
					batchLogger.setLevel(CherryBatchConstants.LOGGER_ERROR);
					logger.BatchLogger(batchLogger);
					CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(
							this.getClass());
					cherryBatchLogger.BatchLogger(batchLogger, e);
				}
			}
		} catch (Exception e) {
			try {

				// 第三方数据源回滚
				saleInfo_Service.tpifManualRollback();

			} catch (Exception e2) {

			}
			// 失败件数
			failCount += prepFailCount;
			flag = CherryBatchConstants.BATCH_WARNING;
			BatchLoggerDTO batchLogger = new BatchLoggerDTO();
			batchLogger.setCode("EOT00063");
			batchLogger.setLevel(CherryBatchConstants.LOGGER_ERROR);
			logger.BatchLogger(batchLogger);
			CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(
					this.getClass());
			cherryBatchLogger.BatchLogger(batchLogger, e);
		}

	}
	
	/**
	 * 程序结束时，处理Job共通(更新Job控制表 、插入Job运行履历表)
	 * @param paraMap
	 * @throws Exception
	 */
	private void programEnd(Map<String,Object> paraMap) throws Exception{
		
		String targetDataStartTime = ConvertUtil.getString(paraMap.get("TargetDataStartTime"));
		if(!CherryBatchUtil.isBlankString(targetDataStartTime)){
			// 程序结束时，更新Job控制表 
			binbecm01_IF.updateJobControl(paraMap);
		}
		 
		// 程序结束时，插入Job运行履历表
		paraMap.put("flag", flag);
		paraMap.put("TargetDataCNT", totalCount);
		paraMap.put("SCNT", totalCount - failCount);
		paraMap.put("FCNT", failCount);
		paraMap.put("TargetDataStartTime", targetDataStartTime);
		paraMap.put("TargetDataEndTime", ConvertUtil.getString(paraMap.get("TargetDataEndTime")));
		paraMap.put("FReason", fReason);
		binbecm01_IF.insertJobRunHistory(paraMap);
	}

	/**
	 * 共通Map
	 * 
	 * @param map
	 * @return
	 */
	private Map<String, Object> getComMap(Map<String, Object> map) {
		Map<String, Object> baseMap = new HashMap<String, Object>();
		// 品牌Code
		String branCode = saleInfo_Service.getBrandCode(map);

		baseMap.put(ProductConstants.BRANDCODE, branCode);
		// 更新程序名
		baseMap.put(CherryBatchConstants.UPDATEPGM, "SaleInfo_BAT106");
		// 作成程序名
		baseMap.put(CherryBatchConstants.CREATEPGM, "SaleInfo_BAT106");
		// 作成者
		baseMap.put(CherryBatchConstants.CREATEDBY,
				CherryBatchConstants.UPDATE_NAME);
		// 更新者
		baseMap.put(CherryBatchConstants.UPDATEDBY,
				CherryBatchConstants.UPDATE_NAME);
		// 所属组织
		baseMap.put(CherryBatchConstants.ORGANIZATIONINFOID,
				map.get(CherryBatchConstants.ORGANIZATIONINFOID).toString());
		// 品牌
		baseMap.put(CherryBatchConstants.BRANDINFOID,
				map.get(CherryBatchConstants.BRANDINFOID).toString());

		// 是否测试模式
		String testMod = binOLCM14_BL.getConfigValue("1080",
				String.valueOf(baseMap.get("organizationInfoId")),
				String.valueOf(baseMap.get("brandInfoId")));
		baseMap.put("testMod", testMod);

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
