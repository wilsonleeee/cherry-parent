package com.cherry.middledbout.stand.order.bl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.batcmbussiness.interfaces.BINBECM01_IF;
import com.cherry.cm.core.*;
import com.cherry.cm.util.CherryBatchUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.middledbout.stand.order.service.BINBAT134_Service;
import com.cherry.mq.mes.common.MessageConstants;
import com.mqhelper.interfaces.MQHelper_IF;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 标准接口：发货单导入BL
 * 
 * @author chenkuan
 * 
 * @version 2015-12-22
 */
public class BINBAT134_BL {


	private static Logger loger = LoggerFactory.getLogger(BINBAT134_BL.class);

	@Resource
	private BINBAT134_Service bINBAT134_Service;
	
	/** JOB执行相关共通 IF */
	@Resource(name="binbecm01_IF")
	private BINBECM01_IF binbecm01_IF;
	
	/** BATCH处理标志 */
	private int flag = CherryBatchConstants.BATCH_SUCCESS;

	/** 每批次(页)处理数量 1000 */
	private final int BATCH_SIZE = 1000;
	
	/** 发货单接口表同步状态:1 同步中 */
	private final String synchFlag_1 = "1";
	
	/** 发货单接口表同步状态:2 同步完成 */
	private final String synchFlag_2 = "2";

	/** 发货单接口表同步状态:3 同步完成 */
	private final String synchFlag_3 = "3";
	
	private Map<String, Object> comMap;
	
	/** 失败的主要原因，受字段长度限制，这里只要记录主要原因即可 */
	private String fReason = new String();
	private StringBuffer fReasonBuffer = new StringBuffer();
	
	/**MQHelper模块接口*/
	@Resource
	private MQHelper_IF mqHelperImpl;


	/** 失败的单据号集合 */
	List<String> falidBillCodeList = new ArrayList<String>();
	
	/** 处理总条数 */
	private int totalCount = 0;
	/** 失败条数 */
	private int failCount = 0;
	
	/**
	 * 发货退库单的batch处理
	 * 
	 * @param
	 * 
	 * @return Map
	 * @throws CherryBatchException
	 */
	public int tran_batch(Map<String, Object> map)throws CherryBatchException,Exception {

		// 初始化
		try {
			comMap = getComMap(map);
			map.put("JobCode", "BAT134");
			// 程序【开始运行时间】
			String runStartTime = bINBAT134_Service.getSYSDateTime();
			map.put("RunStartTime", runStartTime);
		}catch(Exception e){
			// 初始化失败
			flag = CherryBatchConstants.BATCH_ERROR;
			loger.error("发货单导入【标准接口】初始化失败"+e.getMessage(),e);
			throw new Exception(e);
		}

		try{
			// 业务处理(1、更新单据状态，2、取得新后台发货单信息（SynchFlag=1），3、发送MQ及插入MQ_Log日志等)
			updExpTrans();
		}catch(Exception e){
			loger.error(e.getMessage(),e);
			throw e;
		}finally {
			// 日志
			outMessage();
			// 程序结束时，处理Job共通(更新Job控制表 、插入Job运行履历表)
			programEnd(map);
		}
		return flag;
	}
	
	/**
	 * 标准发货单业务处理逻辑
	 * @param
	 * @throws CherryBatchException
	 * @throws Exception
	 */
	private void updExpTrans() throws CherryBatchException,Exception {
		
		Map<String,Object> paraMap = new HashMap<String, Object>();
		paraMap.clear();
		paraMap.putAll(comMap);

		try {
			// Step1: 更新标准发货单接口表的数据从[SynchFlag=null ]更新为[SynchFlag=1]
			bINBAT134_Service.updateSynchFlagNullToOne(paraMap);
			bINBAT134_Service.tpifManualCommit();
		} catch (Exception e) {
			flag = CherryBatchConstants.BATCH_ERROR;
			loger.error("更新标准发货单接口表的数据从[SynchFlag=null ]更新为[SynchFlag=1]",e);
			throw e;
		}


		while(true) {
			// Step2: 取得标准发货单接口表物理数据(主数据[SynchFlag=1])
			paraMap.put("synchFlag", synchFlag_1);
			paraMap.put("batchSize", BATCH_SIZE);
			List<Map<String,Object>> exportTransList = bINBAT134_Service.getExportTransList(paraMap);

			// 统计总条数
			totalCount += exportTransList.size();
			if (CherryBatchUtil.isBlankList(exportTransList)) {
				break;
			}
			for (Map<String, Object> mainMap : exportTransList) {
				try {
					mainMap.putAll(comMap);

					//Step3:取得标准发货单接口表数据（单据明细）
					List<Map<String, Object>> exportTransListDeatils = bINBAT134_Service.getExportTransListdeatils(mainMap);
					//如果明细数据不存在，仅更新接口主表
					if (CherryBatchUtil.isBlankList(exportTransListDeatils)) {
						mainMap.put("synchFlag",synchFlag_3);
						mainMap.put("synchMsg", "该单明细数据不存在。");
						bINBAT134_Service.updateSynchFlagOneToOther(mainMap);
						//事务提交
						bINBAT134_Service.tpifManualCommit();
						failCount ++;
						flag = CherryBatchConstants.BATCH_ERROR;
						continue;
					}


					if(CherryBatchUtil.getString(mainMap.get("BillStatus")).equals("Y")) {//表示正常发货
						//验证一下发货部门是不是柜台，如果是这单就置为失败
						mainMap.put("departCode", mainMap.get("OutDepartCode"));
						Map<String, Object> departMap = bINBAT134_Service.checkExistsCounter(mainMap);
						if (null != departMap && !departMap.isEmpty()) {
							mainMap.put("synchFlag", synchFlag_3);
							mainMap.put("synchMsg", "发货部门不能为柜台");
							bINBAT134_Service.updateSynchFlagOneToOther(mainMap);
							//事务提交
							bINBAT134_Service.tpifManualCommit();
							failCount++;
							flag = CherryBatchConstants.BATCH_ERROR;
							continue;
						}
					}

					//根据预先判断是否可发送MQ的标志
					String sendMQflag = "2";
					//存储预先失败的明细数据
					List<Map<String, Object>> faildDetailList = new ArrayList<Map<String, Object>>();

					//预先验证该主数据是否可以被插入至新后台（验证收货部门是否存在）
					mainMap.put("counterCode", mainMap.get("InDepartCode"));
					Map<String, Object> existsCounterMap = selCounterDepartmentInfo(mainMap);
					if(null == existsCounterMap || existsCounterMap.isEmpty()){
						sendMQflag = "1";
					}

					//预先验证该明细数据是否可以被插入至新后台（验证产品是否存在）
					for (Map<String, Object> detailMap : exportTransListDeatils) {
						detailMap.put("tradeDateTime", mainMap.get("TradeDate") + " " + mainMap.get("TradeTime"));
						//柜台号
						detailMap.put("counterCode", mainMap.get("InDepartCode"));
						detailMap.put("organizationInfoId", comMap.get("organizationInfoId"));
						detailMap.put("brandInfoId", comMap.get("brandInfoId"));
						//预先验证此产品是否在新后台存在
						Map<String, Object> existsPrtMap = checkExistsPrt(detailMap);
						if (null == existsPrtMap || existsPrtMap.isEmpty()) {
							sendMQflag = "0";
							detailMap.putAll(comMap);
							detailMap.put("detailSynchMsg", MessageConstants.MSG_ERROR_09);
							faildDetailList.add(detailMap);
						}
					}

					//如果收货部门不存在，仅更新接口主表
					if ("1".equals(sendMQflag)) {
						mainMap.put("synchFlag",synchFlag_3);
						mainMap.put("synchMsg", "柜台号为\"" + mainMap.get("InDepartCode") + "\"" + MessageConstants.MSG_ERROR_06);
						bINBAT134_Service.updateSynchFlagOneToOther(mainMap);
						//事务提交
						bINBAT134_Service.tpifManualCommit();
						failCount ++;
						flag = CherryBatchConstants.BATCH_ERROR;
						continue;
					}

					//如果产品不存在，更新接口主表+明细表
					if("0".equals(sendMQflag)){

						bINBAT134_Service.updateDetailSynchMsg(faildDetailList);//更改明细数据

						mainMap.put("synchFlag",synchFlag_3);
						mainMap.put("synchMsg", "该主单下的明细数据所对应的产品不存在，详细请查看明细错误信息");
						bINBAT134_Service.updateSynchFlagOneToOther(mainMap);
						//事务提交
						bINBAT134_Service.tpifManualCommit();
						failCount ++;
						flag = CherryBatchConstants.BATCH_ERROR;
						continue;

					}
					// 拼装MQ,写入日志表，发送
					// 先处理MQ，再提交接口库的事务，理由：
					// ①如果处理MQ出异常，则全部回滚，下次导入时会再处理该数据；
					// ②如果处理MQ正常，接口事务提交出错，下次导入时，该数据会被再次处理，被后台的重复MQ过滤逻辑排除掉；
					//相反，如果先提交接口事务，再处理MQ，则有以下隐患：
					//①接口表数据已经被标记为处理成功，MQ处理失败，则以后的导入都不会再处理该数据，该数据就被遗漏了
					Map<String, Object> propSendMQMap = assemblingData(mainMap, exportTransListDeatils);
					//Step4: 调用MQHelper接口进行数据发送
					mqHelperImpl.sendData(propSendMQMap, "posToCherryMsgQueue");
					mainMap.put("synchFlag", synchFlag_2);
					bINBAT134_Service.updateSynchFlagOneToOther(mainMap);
					//事务提交
					bINBAT134_Service.witManualCommit();
					bINBAT134_Service.tpifManualCommit();


				} catch (Exception ex) {
					loger.error("发货单单号为：" + mainMap.get("BillCode") + "往新后台同步数据失败！"+ex.getMessage(),ex);
					mainMap.put("synchFlag", synchFlag_3);
					//更新为失败
					bINBAT134_Service.updateSynchFlagOneToOther(mainMap);
					//事务提交
					bINBAT134_Service.tpifManualCommit();
					falidBillCodeList.add(mainMap.get("BillCode").toString());
					failCount ++;
					flag = CherryBatchConstants.BATCH_WARNING;
				}
			}

			// 接口产品列表为空或产品数据少于一批次(页)处理数量，跳出循环
			if (exportTransList.size() < BATCH_SIZE) {
				break;
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
		String subType = "SD";
		String billStatus = CherryBatchUtil.getString(mainMap.get("BillStatus"));
		if(!"".equals(billStatus) && "N".equals(billStatus)){
			//如果是拒绝发货的话
			subType = "2";
		}
		//子类型 SD:发货单；RJ:退库单；2：订货拒绝;
		mainData[8] = subType;
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
			temp[2] = "DEALER";//使用默认的员工代号，不从接口库里面读取
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
		loger.info("处理总件数:"+totalCount);
		loger.info("成功总件数:"+(totalCount-failCount));
		loger.info("失败总件数:"+failCount);
		
		// 失败docEntry集合
		if(!CherryBatchUtil.isBlankList(falidBillCodeList)){
			fReason="标准接口发货单导入程序处理单据失败，具体查看Log日志";
		}
	}
	/**
	 * 预先验证该明细数据是否可以被插入至新后台（验证产品是否存在）
	 * @param
	 * @return
	 */
	private Map<String,Object> checkExistsPrt(Map<String,Object> detailMap){
		// 不再过滤无效的产品
		Map<String,Object> resultMap = bINBAT134_Service.selProductInfo(detailMap);
		// 若没有找到则再去查询产品条码对应关系表中的产品数据
		if (null == resultMap || resultMap.isEmpty()){
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
		return resultMap;
	}


	/**
	 * 预先验证该主数据是否可以被插入至新后台（验证收货部门是否存在）
	 * @param
	 * @return
	 */
	private Map<String,Object> selCounterDepartmentInfo(Map<String,Object> detailMap) {
		// 取得部门信息
		Map<String, Object> resultMap = bINBAT134_Service.selCounterDepartmentInfo(detailMap);
		return resultMap;
	}
}
