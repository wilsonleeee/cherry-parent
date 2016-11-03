package com.cherry.middledbout.stand.refund.bl;

import java.util.ArrayList;
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
import com.cherry.middledbout.stand.refund.service.BINBAT135_Service;
import com.cherry.mq.mes.common.MessageConstants;
import com.mqhelper.interfaces.MQHelper_IF;


/**
 * 标准接口：退库单导入BL
 * 
 * @author chenkuan
 * 
 * @version 2015-12-24
 */
public class BINBAT135_BL {

	private static CherryBatchLogger logger = new CherryBatchLogger(BINBAT135_BL.class);	
	@Resource
	private BINBAT135_Service bINBAT135_Service;
	
	/** JOB执行相关共通 IF */
	@Resource(name="binbecm01_IF")
	private BINBECM01_IF binbecm01_IF;
	
	/** BATCH处理标志 */
	private int flag = CherryBatchConstants.BATCH_SUCCESS;
	
	/** 销售主数据每次导出数量:2000条 */
	private final int UPDATE_SIZE = 1000;
	
	/** 发货单接口表同步状态:1 同步中 */
	private final String synchFlag_1 = "1";
	
	/** 发货单接口表同步状态:2 同步完成 */
	private final String synchFlag_2 = "2";
	
	private Map<String, Object> comMap;
	
	/** 失败的主要原因，受字段长度限制，这里只要记录主要原因即可 */
	private String fReason="" ;
	private StringBuffer fReasonBuffer = new StringBuffer();
	
	/**MQHelper模块接口*/
	@Resource
	private MQHelper_IF mqHelperImpl;
	
	/** 发货接口表分组，用于拼装MQ */
	List<List<Map<String,Object>>> retList = new ArrayList<List<Map<String,Object>>>();
	
	/** 失败的单据号集合 */
	List<String> falidBillCodeList = new ArrayList<String>();
	
	/** 处理总条数 */
	private int totalCount = 0;
	/** 失败条数 */
	private int failCount = 0;
	
	/**
	 * 退库单的batch处理
	 * 
	 * @param 无
	 * 
	 * @return Map
	 * @throws CherryBatchException
	 */
	public int tran_batch(Map<String, Object> map)
			throws CherryBatchException,Exception {
		// 初始化
		comMap = getComMap(map);
		map.put("JobCode", "BAT135");
		comMap.put("JobCode", "BAT135");
		// 程序【开始运行时间】
		String runStartTime = bINBAT135_Service.getSYSDateTime();
		// 作成日时
		map.put("RunStartTime", runStartTime);
		comMap.put("RunStartTime",runStartTime);
		while (true) {
				
			// 取得标准退库接口表中条件[synchFlag is null ]的单据号集合
			Map<String, Object> trxStatusNullMap = new HashMap<String, Object>();
			trxStatusNullMap.putAll(comMap);
			trxStatusNullMap.put("upCount", UPDATE_SIZE);
			trxStatusNullMap.put("falidBillCodeList", falidBillCodeList);
			List<String> billCodeList = bINBAT135_Service.getBillCodeList(trxStatusNullMap);
			
			if (CherryBatchUtil.isBlankList(billCodeList)) {
				break;
			}
			
			// 统计总条数(此处只统计退库单据数)
			totalCount += billCodeList.size();
			
			// 业务处理(1、更新单据状态，2、取得新后台退库单信息，3、发送MQ及插入MQ_Log日志等)
			updExpTrans();
			
			// 退库单据数少于一页，跳出循环
			if (billCodeList.size() < UPDATE_SIZE) {
				break;
			}

		}
		outMessage();
		programEnd(map);
		return flag;
	}
	
	/**
	 * 标准退库单业务处理逻辑
	 * @throws CherryBatchException
	 * @throws Exception
	 */
	private void updExpTrans() throws CherryBatchException {
		
		Map<String,Object> paraMap = new HashMap<String, Object>();
		paraMap.clear();
		paraMap.putAll(comMap);
		
		// Step1: 更新标准退库单接口表的数据从[SynchFlag=null ]更新为[SynchFlag=1]
		paraMap.put("synchFlag_Old", "");
		paraMap.put("synchFlag_New", synchFlag_1);
		bINBAT135_Service.updateSynchFlag(paraMap);
		
		
		// Step2: 取得标准退库单接口表物理数据(主数据)BillType=TKSH
		paraMap.put("synchFlag", synchFlag_1);
		List<Map<String,Object>> exportTransList = bINBAT135_Service.getExportTransList(paraMap);
		for(Map<String,Object> mainMap : exportTransList){
			try {
				
				//Step3:取得标准退库单接口表数据（单据明细）
				List<Map<String,Object>> exportTransListDeatils = bINBAT135_Service.getExportTransListdeatils(mainMap);				
				
				// 拼装MQ
				Map<String,Object> propSendMQMap = assemblingData(mainMap, exportTransListDeatils);				
				try{
					
					//Step4: 调用MQHelper接口进行数据发送
					boolean sendMQFlag = mqHelperImpl.sendData(propSendMQMap, "posToCherryMsgQueue");
					
					try{
						
						// Step5: 退库单接口表数据从[synchFlag=1]更新为[synchFlag=2]
						if(sendMQFlag){
							Map<String, Object> t1ToT2Map = new HashMap<String, Object>();
							t1ToT2Map.putAll(comMap);
							t1ToT2Map.put("billCode", mainMap.get("BillCode"));
							t1ToT2Map.put("synchFlag_Old", synchFlag_1);
							t1ToT2Map.put("synchFlag_New", synchFlag_2);
							bINBAT135_Service.updateSynchFlag(t1ToT2Map);
						}
						//事务提交
						bINBAT135_Service.tpifManualCommit();
						
					}catch(Exception ex){
						// 标准接口数据源回滚
						bINBAT135_Service.tpifManualRollback();
					}
					
					bINBAT135_Service.witManualCommit();	
				} catch (Exception e) {
					//MQ_Log表中事物回滚
					bINBAT135_Service.witManualRollback();
					throw e;
				}
				
			} catch(Exception ex){

				failCount += 1;
				falidBillCodeList.add(mainMap.get("BillCode").toString());
				flag = CherryBatchConstants.BATCH_WARNING;
				
				// 错误日志
				BatchLoggerDTO batchLoggerDTO1 = new BatchLoggerDTO();
				batchLoggerDTO1.setCode("EOT00109");
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
	 * 将退库单的主数据和明细数据组装到一个MAP中，供调用MQHelper模块时使用
	 * @param mainMap 主数据行
	 * @param detailList 明细数据行
	 * @return
	 * @throws Exception
	 */
	private Map<String,Object> assemblingData(Map<String,Object> mainMap ,List<Map<String,Object>> detailList) throws Exception{
		
		Map<String,Object> resultMap = new HashMap<String,Object>();
		
		//MQ版本号
		resultMap.put("Version", MessageConstants.MESSAGE_VERSION);
		
		//MQ消息体数据类型
		resultMap.put("Type", MessageConstants.MESSAGE_TYPE_SALE_STOCK);
		
		//消息主体数据key
		resultMap.put("MainLineKey", new String[]{"BrandCode","TradeNoIF","ModifyCounts",
				"CounterCode","RelevantCounterCode","TotalQuantity","TotalAmount","TradeType","SubType",
				"RelevantNo","Reason","TradeDate","TradeTime","TotalAmountBefore","TotalAmountAfter","MemberCode"});
		
		//消息主体数据
		String[] mainData = new String[((String[])resultMap.get("MainLineKey")).length];
		
		//品牌代码
		mainData[0] = CherryBatchUtil.getString(mainMap.get("BrandCode"));
		//单据号
		mainData[1] = CherryBatchUtil.getString(mainMap.get("BillCode"));
		//更改次数
		mainData[2] = null;
		//退库柜台
		mainData[3] =CherryBatchUtil.getString(mainMap.get("OutDepartCode"));
		//关联柜台号
		mainData[4] = null;
		//总数量
		mainData[5] = CherryBatchUtil.getString(mainMap.get("TotalQuantity"));
		//总金额
		mainData[6] = CherryBatchUtil.getString(mainMap.get("TotalAmount"));
		//单据类型
		mainData[7] = "KS";
		//子类型 SD:发货单；RJ:退库单； 
		mainData[8]="RJ";
		
		//关联单号		
		if("".equals(CherryBatchUtil.getString(mainMap.get("RelateBillCode")))){
			mainData[9]=null;
		}else{
			mainData[9] = CherryBatchUtil.getString(mainMap.get("RelateBillCode"));	
		}
				
		//退库理由		
		if("".equals(CherryBatchUtil.getString(mainMap.get("Comment")))){
			mainData[10] =null;
		}else{
			mainData[10] = CherryBatchUtil.getString(mainMap.get("Comment"));
		}
		
		//日期
		mainData[11] = CherryBatchUtil.getString(mainMap.get("TradeDate"));
		//时间
		mainData[12] = CherryBatchUtil.getString(mainMap.get("TradeTime"));
		
		// 发货前柜台库存总金额
		mainData[13] = null;
		// 发货后柜台库存总金额
		mainData[14] = null;
		// 会员号
		mainData[15] = null;
		
		resultMap.put("MainDataLine", mainData);
		
		//明细数据Key
		resultMap.put("DetailLineKey", new String[]{"TradeNoIF","ModifyCounts","BAcode","StockType",
				"Barcode","Unitcode","InventoryTypeCode","Quantity","QuantityBefore","Price","Reason"});
		
		//明细数据
		List<String[]> detailData = new ArrayList<String[]>();
		for(int i = detailList.size()-1 ; i >= 0 ; i--){
			Map<String,Object> temMap = detailList.get(i);
			String[] temp = new String[((String[])resultMap.get("DetailLineKey")).length];
			
			// 单据号
			temp[0] = CherryBatchUtil.getString(temMap.get("BillCode"));
			// 修改次数
			temp[1] = null;
			// 员工code
			temp[2] = "DEALER";
			// 入出库区分
			temp[3] = null;
			// 产品条码
			temp[4] = CherryBatchUtil.getString(temMap.get("BarCode"));
			// 厂商编码
			temp[5] = CherryBatchUtil.getString(temMap.get("UnitCode"));
			// 仓库类型
			temp[6] = null;
			// 单品数量

			temp[7] = CherryBatchUtil.getString(temMap.get("Quantity"));
			// 入出库前该商品柜台账面数量（针对盘点）
			temp[8] = null;
			//单品价格
			temp[9] = CherryBatchUtil.getString(temMap.get("Price"));
			// 理由
			temp[10] = null;
			detailData.add(temp);
		}
		
		resultMap.put("DetailDataLine", detailData);
		
		//设定MQ_Log日志需要的数据
		Map<String, Object> mqLog = new HashMap<String,Object>();
		mqLog.put("BillType", "KS");
		mqLog.put("BillCode", mainData[1]);
		mqLog.put("CounterCode", mainData[3]);
		String date = mainData[11].replaceAll("-","");
		mqLog.put("Txddate", date);
		String time = mainData[12].replaceAll(":","");
		mqLog.put("Txdtime", time);
		mqLog.put("Source", "K3");
		mqLog.put("SendOrRece", "S");
		mqLog.put("ModifyCounts", "0");
		
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
		baseMap.put(CherryBatchConstants.UPDATEPGM, "BINBAT135_BL");
		// 作成程序名
		baseMap.put(CherryBatchConstants.CREATEPGM, "BINBAT135_BL");
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
		String branCode = bINBAT135_Service.getBrandCode(map);
		// 品牌Code
		baseMap.put(CherryConstants.BRAND_CODE, branCode);
		
		// 业务日期，日结标志
		Map<String, Object> bussDateMap = bINBAT135_Service.getBussinessDateMap(map);
		// 业务日期
		String businessDate = CherryBatchUtil.getString(bussDateMap.get(CherryBatchConstants.BUSINESS_DATE));
		baseMap.put("businessDate", businessDate);
		
		return baseMap;
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
		
		// 失败BillCode集合
		if(!CherryBatchUtil.isBlankList(falidBillCodeList)){
			BatchLoggerDTO batchLoggerDTO6 = new BatchLoggerDTO();
			batchLoggerDTO6.setCode("EOT00110");
			batchLoggerDTO6.setLevel(CherryBatchConstants.LOGGER_INFO);
			batchLoggerDTO6.addParam(falidBillCodeList.toString());
			fReason="标准接口退库单导入程序处理单据失败，具体查看Log日志";
			logger.BatchLogger(batchLoggerDTO6);
		}
	}
	
}
