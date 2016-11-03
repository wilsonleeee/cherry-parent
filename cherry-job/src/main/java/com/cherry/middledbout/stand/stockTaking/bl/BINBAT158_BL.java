package com.cherry.middledbout.stand.stockTaking.bl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.batcmbussiness.interfaces.BINBECM01_IF;
import com.cherry.cm.core.BatchLoggerDTO;
import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryBatchException;
import com.cherry.cm.core.CherryBatchLogger;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.CherryBatchUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.middledbout.stand.order.bl.BINBAT134_BL;
import com.cherry.middledbout.stand.stockTaking.service.BINBAT158_Service;
import com.cherry.mq.mes.common.CherryMQException;
import com.mqhelper.interfaces.MQHelper_IF;

/**
 * 标准接口：审核单导入BL
 *
 */
public class BINBAT158_BL {
	
	private static CherryBatchLogger logger = new CherryBatchLogger(BINBAT158_BL.class);
	
	@Resource(name = "binBAT158_Service")
	private BINBAT158_Service binBAT158_Service;
	
	/** JOB执行相关共通 IF */
	@Resource(name="binbecm01_IF")
	private BINBECM01_IF binbecm01_IF;
	
	/** BATCH处理标志 */
	private int flag = CherryBatchConstants.BATCH_SUCCESS;
	
	/** 库存主数据每次导出数量:1000条 */
	private final int UPDATE_SIZE = 1000;
	
	/** 审核单接口表同步状态:1 同步中 */
	private final String synchFlag_1 = "1";
	
	/** 审核单接口表同步状态:2 同步完成 */
	private final String synchFlag_2 = "2";
	
	private Map<String, Object> comMap;
	
	/** 失败的主要原因，受字段长度限制，这里只要记录主要原因即可 */
	private String fReason = new String();
	private StringBuffer fReasonBuffer = new StringBuffer();
	
	/**MQHelper模块接口*/
	@Resource
	private MQHelper_IF mqJSONHelperImpl;
	
	/** 审核接口表分组，用于拼装MQ */
	List<List<Map<String,Object>>> retList = new ArrayList<List<Map<String,Object>>>();
	
	/** 失败的单据号集合 */
	List<String> failedBillCodeList = new ArrayList<String>();
	
	/** 处理总条数 */
	private int totalCount = 0;
	/** 失败条数 */
	private int failCount = 0;

	/**
	 * 盘点审核单的batch处理
	 * @param map
	 * @return
	 * @throws CherryBatchException 
	 */
	public int tran_batch(Map<String, Object> map) throws CherryBatchException,Exception {
		// 初始化
		comMap = getComMap(map);
		map.put("JobCode", "BAT158");
		comMap.put("JobCode", "BAT158");
		// 程序【开始运行时间】
		String runStartTime = binBAT158_Service.getSYSDateTime();
		// 作成日时
		map.put("RunStartTime", runStartTime);
		comMap.put("RunStartTime",runStartTime);
		
		while(true){
			
			// 取得标准审核单接口表中条件[synchFlag is null ]的单据号集合
			Map<String, Object> trxStatusNullMap = new HashMap<String, Object>();
			trxStatusNullMap.putAll(comMap);
			trxStatusNullMap.put("upCount", UPDATE_SIZE);
			trxStatusNullMap.put("falidBillCodeList", failedBillCodeList);
			List<String> billCodeList = binBAT158_Service.getBillCodeList(trxStatusNullMap);
			
			if(CherryBatchUtil.isBlankList(billCodeList)){
				break;
			}
			
			// 统计总条数
			totalCount += billCodeList.size();
			
			// 业务处理(1、更新单据状态，2、取得新后台审核单信息，3、发送MQ及插入MQ_Log日志等)
			updExpTrans();
			
			// 发货单据数少于一页，跳出循环
			if (billCodeList.size() < UPDATE_SIZE) {
				break;
			}
			
		}
		
		outMessage();
		programEnd(map);
		return flag;
	}
	
	/**
	 * 标准审核单业务处理逻辑
	 * @throws Exception 
	 */
	private void updExpTrans() throws Exception {
		
		Map<String,Object> paraMap = new HashMap<String, Object>();
		paraMap.clear();
		paraMap.putAll(comMap);
		
		// Step1: 更新标准审核单接口表的数据从[SynchFlag=null ]更新为[SynchFlag=1]
		paraMap.put("synchFlag_New", synchFlag_1);
		binBAT158_Service.updateSynchFlag(paraMap);
		
		// Step2: 取得标准审核单接口表物理数据(主数据)
		paraMap.put("synchFlag", synchFlag_1);
		List<Map<String,Object>> exportTransList = binBAT158_Service.getExportTransList(paraMap);
		
		for(Map<String,Object> mainMap : exportTransList){
			try {
				
				//Step3:取得标准审核单接口表数据（单据明细）
				List<Map<String,Object>> exportTransListDeatils = binBAT158_Service.getExportTransListdeatils(mainMap);
				
				for (Map<String, Object> detailMap : exportTransListDeatils) {
					detailMap.put("tradeDateTime",mainMap.get("TradeDate")+" "+mainMap.get("TradeTime"));
					//柜台号
					detailMap.put("organizationInfoId",comMap.get("organizationInfoId"));
					detailMap.put("brandInfoId",comMap.get("brandInfoId"));
				}
				
				try {
					Map<String, Object> updateMap = new HashMap<String, Object>();
					updateMap.putAll(comMap);
					updateMap.put("billCode", mainMap.get("BillCode"));
					
					// 拼装MQ
					Map<String,Object> propSendMQJsonStr = assemblingData(mainMap, exportTransListDeatils);
					//Step4: 调用MQHelper接口进行数据发送
					mqJSONHelperImpl.sendData(propSendMQJsonStr, "posToCherryMsgQueueJSON");
					binBAT158_Service.witManualCommit();
					updateMap.put("synchFlag_New", synchFlag_2);
					
					try {
						// Step5: 审核单接口表数据从[synchFlag=1]更新为[synchFlag=2 || synchFlag=3]
						updateMap.put("synchFlag_Old", synchFlag_1);
						binBAT158_Service.updateSynchFlag(updateMap);
						//事务提交
						binBAT158_Service.tpifManualCommit();
					} catch (Exception e) {
						binBAT158_Service.tpifManualRollback();
					}
					
				} catch (Exception e) {
					//MQ_Log表中事物回滚
					binBAT158_Service.witManualRollback();
				}
			} catch (Exception ex) {
				failCount += 1;
				failedBillCodeList.add(mainMap.get("BillCode").toString());
				flag = CherryBatchConstants.BATCH_WARNING;
				
				// 错误日志
				BatchLoggerDTO batchLoggerDTO1 = new BatchLoggerDTO();
				batchLoggerDTO1.setCode("EOT00107");
				batchLoggerDTO1.setLevel(CherryBatchConstants.LOGGER_ERROR);
                //组织ID：
				batchLoggerDTO1.addParam(CherryBatchUtil.getString(paraMap.get(CherryBatchConstants.ORGANIZATIONINFOID)));
                //品牌ID：
				batchLoggerDTO1.addParam(CherryBatchUtil.getString(paraMap.get(CherryBatchConstants.BRANDINFOID)));
                //接口单据号
				batchLoggerDTO1.addParam(mainMap.get("BillCode").toString());
				logger.BatchLogger(batchLoggerDTO1);
				CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
				cherryBatchLogger.BatchLogger(batchLoggerDTO1, ex);
			}
		}
		
	}
	
	/**
	 * 将申请单的主数据和明细数据组装到一个JSON字符串中，供调用MQHelper模块时使用
	 * @param mainMap
	 * @param detailList
	 * @return
	 * @throws CherryMQException 
	 */
	private Map<String,Object> assemblingData(Map<String,Object> mainMap ,List<Map<String,Object>> detailList) throws CherryMQException{
		
		Map<String,Object> resultMap = new HashMap<String,Object>();
		
		//消息主体数据key
		resultMap.put("BrandCode", CherryBatchUtil.getString(mainMap.get("BrandCode")));
		resultMap.put("TradeType", "PDSH");
		//?--?
		resultMap.put("SubType", CherryBatchUtil.getString(mainMap.get("BillStatus")));
		String billCode = CherryBatchUtil.getString(mainMap.get("BillCode"));
		resultMap.put("TradeNoIF", billCode);
		//?--?
		resultMap.put("RelevantNo", CherryBatchUtil.getString(mainMap.get("RelateBillCode")));
		resultMap.put("DepartCode", CherryBatchUtil.getString(mainMap.get("DepartCode")));
		
		resultMap.put("EmployeeCode", CherryBatchUtil.getString(mainMap.get("OperatorCode")));
		resultMap.put("TradeDate", CherryBatchUtil.getString(mainMap.get("TradeDate")));
		resultMap.put("TradeTime", CherryBatchUtil.getString(mainMap.get("TradeTime")));
		resultMap.put("TotalQuantity", CherryBatchUtil.getString(mainMap.get("TotalQuantity")));
		resultMap.put("TotalAmount", CherryBatchUtil.getString(mainMap.get("TotalAmount")));
		resultMap.put("Comments", CherryBatchUtil.getString(mainMap.get("Comment")));
		
		//消息明细数据
		List<Map<String,Object>> resultDetailList = new ArrayList<Map<String,Object>>();
		Map<String,Object> resultDetailListMap = null;
		for(Map<String,Object> tempMap : detailList){
			resultDetailListMap = new HashMap<String,Object>();
			//resultDetailListMap.put("ProductId",CherryBatchUtil.getString(tempMap.get("IFProductId")));
			resultDetailListMap.put("ProductId",null);
			resultDetailListMap.put("Unitcode",CherryBatchUtil.getString(tempMap.get("UnitCode")));
			resultDetailListMap.put("Barcode",CherryBatchUtil.getString(tempMap.get("BarCode")));
			resultDetailListMap.put("LogicInventoryCode",null);
			resultDetailListMap.put("BookQuantity", CherryBatchUtil.getString(tempMap.get("AccountsQuantity")));
			resultDetailListMap.put("GainQuantity", CherryBatchUtil.getString(tempMap.get("GainQuantity")));
			resultDetailListMap.put("Price", CherryBatchUtil.getString(tempMap.get("Price")));
			resultDetailListMap.put("Comments", CherryBatchUtil.getString(tempMap.get("Comment")));
			resultDetailList.add(resultDetailListMap);
		}
		
		resultMap.put("DetailList", resultDetailList);
		
		//设定MQ_Log日志需要的数据
		Map<String, Object> mqLog = new HashMap<String,Object>();
		mqLog.put("BillType", "PDSH");
		mqLog.put("BillCode", CherryBatchUtil.getString(mainMap.get("BillCode")));
		//?--?
		mqLog.put("CounterCode", CherryBatchUtil.getString(mainMap.get("DepartCode")));
		String date = CherryBatchUtil.getString(mainMap.get("TradeDate")).replaceAll("-","");
		mqLog.put("Txddate", date);
		String time = CherryBatchUtil.getString(mainMap.get("TradeTime")).replaceAll(":","");
		mqLog.put("Txdtime", time);
		mqLog.put("Source", "K3");
		mqLog.put("SendOrRece", "S");
		mqLog.put("ModifyCounts", "0");
		mqLog.put("MsgQueueName", "posToCherryMsgQueueJSON");
		
		resultMap.put("Mq_Log", mqLog);
		
		return resultMap;
	}	
	

	/**
	 * 共通Map
	 * @param map
	 * @return
	 */
	private Map<String, Object> getComMap(Map<String, Object> map) {
		Map<String, Object> baseMap = new HashMap<String, Object>();

		// 更新程序名
		baseMap.put(CherryBatchConstants.UPDATEPGM, "BINBAT158_BL");
		// 作成程序名
		baseMap.put(CherryBatchConstants.CREATEPGM, "BINBAT158_BL");
		// 作成者
		baseMap.put(CherryBatchConstants.CREATEDBY,
				CherryBatchConstants.UPDATE_NAME);
		// 更新者
		baseMap.put(CherryBatchConstants.UPDATEDBY,CherryBatchConstants.UPDATE_NAME);
		// 所属组织
		baseMap.put(CherryBatchConstants.ORGANIZATIONINFOID, map.get(CherryBatchConstants.ORGANIZATIONINFOID).toString());
		// 品牌ID
		baseMap.put(CherryBatchConstants.BRANDINFOID, map.get(CherryBatchConstants.BRANDINFOID).toString());
		// 品牌Code
		String branCode = binBAT158_Service.getBrandCode(map);
		// 品牌Code
		baseMap.put(CherryConstants.BRAND_CODE, branCode);
		
		// 业务日期，日结标志
		Map<String, Object> bussDateMap = binBAT158_Service.getBussinessDateMap(map);
		// 业务日期
		String businessDate = CherryBatchUtil.getString(bussDateMap.get(CherryBatchConstants.BUSINESS_DATE));
		baseMap.put("businessDate", businessDate);
		
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
		
		// 失败docEntry集合
		if(!CherryBatchUtil.isBlankList(failedBillCodeList)){
			BatchLoggerDTO batchLoggerDTO6 = new BatchLoggerDTO();
			batchLoggerDTO6.setCode("EOT00108");
			batchLoggerDTO6.setLevel(CherryBatchConstants.LOGGER_INFO);
			batchLoggerDTO6.addParam(failedBillCodeList.toString());
			fReason="标准接口发货单导入程序处理单据失败，具体查看Log日志";
			logger.BatchLogger(batchLoggerDTO6);
		}
	}
	
	/**
	 * 程序结束时，处理Job共通(更新Job控制表 、插入Job运行履历表)
	 * @param paraMap
	 * @throws Exception
	 */
	private void programEnd(Map<String,Object> paraMap) throws Exception{
		
		paraMap.putAll(comMap);
		
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
		paraMap.put("FReason", fReasonBuffer.append(fReason).toString());
 
		binbecm01_IF.insertJobRunHistory(paraMap);
	}
	

}
