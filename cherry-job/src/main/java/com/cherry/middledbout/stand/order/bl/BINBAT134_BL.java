package com.cherry.middledbout.stand.order.bl;

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
import com.cherry.middledbout.stand.order.service.BINBAT134_Service;
import com.cherry.mq.mes.common.MessageConstants;
import com.mqhelper.interfaces.MQHelper_IF;


/**
 * 标准接口：发货单导入BL
 * 
 * @author chenkuan
 * 
 * @version 2015-12-22
 */
public class BINBAT134_BL {

	private static CherryBatchLogger logger = new CherryBatchLogger(BINBAT134_BL.class);	
	@Resource
	private BINBAT134_Service bINBAT134_Service;
	
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
	private String fReason = new String();
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
	 * 发货退库单的batch处理
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
		map.put("JobCode", "BAT134");
		comMap.put("JobCode", "BAT134");
		// 程序【开始运行时间】
		String runStartTime = bINBAT134_Service.getSYSDateTime();
		// 作成日时
		map.put("RunStartTime", runStartTime);
		comMap.put("RunStartTime",runStartTime);
		while (true) {
				
			// 取得标准发货单接口表中条件[synchFlag is null ]的单据号集合
			Map<String, Object> trxStatusNullMap = new HashMap<String, Object>();
			trxStatusNullMap.putAll(comMap);
			trxStatusNullMap.put("upCount", UPDATE_SIZE);
			trxStatusNullMap.put("falidBillCodeList", falidBillCodeList);
			List<String> billCodeList = bINBAT134_Service.getBillCodeList(trxStatusNullMap);
			
			if (CherryBatchUtil.isBlankList(billCodeList)) {
				break;
			}
			
			// 统计总条数(单据数量[1个发货单据具有一条或多条物理数据]，此处只统计发货单据数)
			totalCount += billCodeList.size();
			
			// 业务处理(1、更新单据状态，2、取得新后台发货单信息，3、发送MQ及插入MQ_Log日志等)
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
	 * 标准发货单业务处理逻辑
	 * @param docEntryList
	 * @throws CherryBatchException
	 * @throws Exception
	 */
	private void updExpTrans() throws CherryBatchException {
		
		Map<String,Object> paraMap = new HashMap<String, Object>();
		paraMap.clear();
		paraMap.putAll(comMap);
		
		// Step1: 更新标准发货单接口表的数据从[SynchFlag=null ]更新为[SynchFlag=1]
		paraMap.put("synchFlag_Old", "");
		paraMap.put("synchFlag_New", synchFlag_1);
		bINBAT134_Service.updateSynchFlag(paraMap);
		
		
		// Step2: 取得标准发货单接口表物理数据(主数据)
		paraMap.put("synchFlag", synchFlag_1);
		List<Map<String,Object>> exportTransList = bINBAT134_Service.getExportTransList(paraMap);
		for(Map<String,Object> mainMap : exportTransList){
			try {
				
				//Step3:取得标准发货单接口表数据（单据明细）
				List<Map<String,Object>> exportTransListDeatils = bINBAT134_Service.getExportTransListdeatils(mainMap);
				//根据预先判断是否可发送MQ的标志
				String flag = "2";
				//存储预先失败的明细数据
				List<Map<String,Object>> faildDetailList = new ArrayList<Map<String,Object>>();
				//预先验证该明细数据是否可以被插入至新后台（验证产品是否存在）
				for (Map<String, Object> detailMap : exportTransListDeatils) {
					detailMap.put("tradeDateTime",mainMap.get("TradeDate")+" "+mainMap.get("TradeTime"));
					//柜台号
					detailMap.put("counterCode",mainMap.get("InDepartCode"));
					detailMap.put("organizationInfoId",comMap.get("organizationInfoId"));
					detailMap.put("brandInfoId",comMap.get("brandInfoId"));
					//预先验证此产品是否在新后台存在
					Map<String,Object> existsPrtMap = checkExistsPrt(detailMap);
					if(null == existsPrtMap || existsPrtMap.isEmpty()){
						flag = "0";
						faildDetailList.add(detailMap);
					}else if("1".equals(existsPrtMap.get("counterFlag"))){
						flag = "1";
					}
				}
				try{
					Map<String, Object> updateMap = new HashMap<String, Object>();
					updateMap.putAll(comMap);
					updateMap.put("billCode", mainMap.get("BillCode"));
					if("0".equals(flag) || "1".equals(flag)){
						//更新接口表表数据表示该单据因产品问题无法插入至新后台，并同时更新同步字段为失败
						updateMap.put("synchFlag_New", "3");
						if("0".equals(flag)){
							try {
								for (Map<String, Object> faildDetailMap : faildDetailList) {
									updateMap.put("detailSynchMsg", "厂商编码为\""+faildDetailMap.get("UnitCode")+"\"产品条码为\""+faildDetailMap.get("BarCode")+"\""+MessageConstants.MSG_ERROR_09);
									updateMap.put("unitCode", faildDetailMap.get("UnitCode"));
									updateMap.put("barCode", faildDetailMap.get("BarCode"));
									bINBAT134_Service.updateDetailSynchMsg(updateMap);
									//事务提交
									bINBAT134_Service.tpifManualCommit();
								}
							} catch (Exception e) {
								//事物回滚
								bINBAT134_Service.tpifManualCommit();
							}
							updateMap.put("synchMsg", "该主单下的明细数据所对应的产品或促销品不存在，详细请查看明细错误信息");
						}else{
							updateMap.put("synchMsg", "柜台号为\""+mainMap.get("InDepartCode")+"\""+MessageConstants.MSG_ERROR_06);
						}
					}else{
						// 拼装MQ
						Map<String,Object> propSendMQMap = assemblingData(mainMap, exportTransListDeatils);
						//Step4: 调用MQHelper接口进行数据发送
						mqHelperImpl.sendData(propSendMQMap, "posToCherryMsgQueue");
						bINBAT134_Service.witManualCommit();
						updateMap.put("synchFlag_New", synchFlag_2);
					}
					
					try{
						// Step5: 发货单接口表数据从[synchFlag=1]更新为[synchFlag=2 || synchFlag=3]
						updateMap.put("synchFlag_Old", synchFlag_1);
						bINBAT134_Service.updateSynchFlag(updateMap);
						//事务提交
						bINBAT134_Service.tpifManualCommit();
						
					}catch(Exception ex){
						// 标准接口数据源回滚
						bINBAT134_Service.tpifManualRollback();
					}
				} catch (Exception e) {
					//MQ_Log表中事物回滚
					bINBAT134_Service.witManualRollback();
					throw e;
				}
				
			} catch(Exception ex){

				failCount += 1;
				falidBillCodeList.add(mainMap.get("BillCode").toString());
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
	 * 将发货单/退库单的主数据和明细数据组装到一个MAP中，供调用MQHelper模块时使用
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
				"RelevantNo","Reason","TradeDate","TradeTime","TotalAmountBefore","TotalAmountAfter","MemberCode","OutDepartCode"});
		
		//消息主体数据
		String[] mainData = new String[((String[])resultMap.get("MainLineKey")).length];
		
		//品牌代码
		mainData[0] = CherryBatchUtil.getString(mainMap.get("BrandCode"));
		//单据号
		mainData[1] = CherryBatchUtil.getString(mainMap.get("BillCode"));
		//更改次数
		mainData[2] = null;
		//接受订货柜台
		mainData[3] =CherryBatchUtil.getString(mainMap.get("InDepartCode"));
		//关联柜台号
		mainData[4] = null;
		//总数量
		mainData[5] = CherryBatchUtil.getString(mainMap.get("TotalQuantity"));
		//总金额
		mainData[6] = CherryBatchUtil.getString(mainMap.get("TotalAmount"));
		//单据类型
		mainData[7] = "KS";
		//子类型 SD:发货单；RJ:退库单； 
		mainData[8]="SD";
		
		//关联单号		
		if("".equals(CherryBatchUtil.getString(mainMap.get("RelateBillCode")))){
			mainData[9]=null;
		}else{
			mainData[9] = CherryBatchUtil.getString(mainMap.get("RelateBillCode"));	
		}
				
		//发货理由		
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
		//出库部门代号
		mainData[16] =  CherryBatchUtil.getString(mainMap.get("OutDepartCode"));
		
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
		baseMap.put(CherryBatchConstants.UPDATEPGM, "BINBAT134_BL");
		// 作成程序名
		baseMap.put(CherryBatchConstants.CREATEPGM, "BINBAT134_BL");
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
		String branCode = bINBAT134_Service.getBrandCode(map);
		// 品牌Code
		baseMap.put(CherryConstants.BRAND_CODE, branCode);
		
		// 业务日期，日结标志
		Map<String, Object> bussDateMap = bINBAT134_Service.getBussinessDateMap(map);
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
		
		// 失败docEntry集合
		if(!CherryBatchUtil.isBlankList(falidBillCodeList)){
			BatchLoggerDTO batchLoggerDTO6 = new BatchLoggerDTO();
			batchLoggerDTO6.setCode("EOT00108");
			batchLoggerDTO6.setLevel(CherryBatchConstants.LOGGER_INFO);
			batchLoggerDTO6.addParam(falidBillCodeList.toString());
			fReason="标准接口发货单导入程序处理单据失败，具体查看Log日志";
			logger.BatchLogger(batchLoggerDTO6);
		}
	}
	/**
	 * 预先验证该明细数据是否可以被插入至新后台（验证产品是否存在）
	 * @param map
	 * @return
	 */
	private Map<String,Object> checkExistsPrt(Map<String,Object> detailMap){
		Map<String,Object> resultMap = new HashMap<String, Object>();
		// 取得部门信息
		resultMap = bINBAT134_Service.selCounterDepartmentInfo(detailMap);
		if(null != resultMap && !resultMap.isEmpty()){
			//优先查询此产品是否为促销品
			detailMap.put("organizationID", resultMap.get("organizationID"));
			resultMap = bINBAT134_Service.selPrmProductInfo(detailMap);
			if (resultMap == null || resultMap.get("promotionProductVendorID") == null) {
				resultMap = bINBAT134_Service.selPrmProductPrtBarCodeInfo(detailMap);
				if(null == resultMap || resultMap.isEmpty()){
					//在促销产品条码对应关系表里找不到，放开时间条件再找一次，还是找不到的接下来查产品
					try {
						List<Map<String,Object>> prmPrtBarCodeList = bINBAT134_Service.selPrmPrtBarCodeList(detailMap);
						if (!CherryBatchUtil.isBlankList(prmPrtBarCodeList)) {
							//取tradeDateTime与StartTime最接近的第一条
							Map<String,Object> temp = new HashMap<String, Object>();
							temp.put("promotionProductVendorID", prmPrtBarCodeList.get(0).get("promotionProductVendorID"));
							//查询促销产品信息  根据促销产品厂商ID，不区分有效状态
							List list = bINBAT134_Service.selPrmByPrmVenID(temp);
							if(list!=null&&!list.isEmpty()){
								resultMap = (HashMap)list.get(0);
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			if(null == resultMap || resultMap.isEmpty()){
				//如促销品未查到则再次查询此产品是否为产品
				// 不再过滤无效的产品
				resultMap = bINBAT134_Service.selProductInfo(detailMap);
				// 若没有找到则再去查询产品条码对应关系表中的产品数据
				if (null == resultMap || "".equals(ConvertUtil.getString(resultMap.get("productVendorID")))){
					// 查找对应的产品条码对应关系表【业务时间在起止时间内】
					resultMap = bINBAT134_Service.selPrtBarCode(detailMap);
					// 在产品条码对应关系表里找不到，放开时间条件再找一次
					if(null == resultMap || resultMap.isEmpty()){
						List<Map<String,Object>> prtBarCodeList = bINBAT134_Service.selPrtBarCodeList(detailMap);
						if(null != prtBarCodeList && prtBarCodeList.size()>0){
							//取tradeDateTime与StartTime最接近的第一条
							resultMap = new HashMap<String, Object>();
							resultMap.put("productVendorID", prtBarCodeList.get(0).get("productVendorID"));
						}
					}
				}
			}
		}else{
			//该标识表示为部门代号不存在
			resultMap = new HashMap<String, Object>();
			resultMap.put("counterFlag", "1");
		}
		return resultMap;
	}
}
