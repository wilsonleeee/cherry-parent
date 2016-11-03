/*	
 * @(#)BINBAT121_BL.java     1.0 @2015-9-16
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
package com.cherry.webserviceout.alicloud.jstTrade.bl;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.batcmbussiness.interfaces.BINBECM01_IF;
import com.cherry.cm.cmbussiness.bl.BINOLCM03_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM15_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM18_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM60_BL;
import com.cherry.cm.core.BatchExceptionDTO;
import com.cherry.cm.core.BatchLoggerDTO;
import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryBatchException;
import com.cherry.cm.core.CherryBatchLogger;
import com.cherry.cm.core.CherryBatchSecret;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.PropertiesUtil;
import com.cherry.cm.util.CherryBatchUtil;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.DateUtil;
import com.cherry.mq.mes.bl.BINBEMQMES97_BL;
import com.cherry.mq.mes.common.CherryMQException;
import com.cherry.mq.mes.common.MessageConstants;
import com.cherry.ot.hong.service.BINOTHONG01_Service;
import com.cherry.webservice.client.WebserviceClient;
import com.cherry.webservice.sale.bl.SaleInfoLogic;
import com.cherry.webservice.sale.service.SaleInfoService;
import com.cherry.webserviceout.alicloud.jstTrade.service.BINBAT121_Service;
import com.googlecode.jsonplugin.JSONUtil;

/**
 *
 *  聚石塔接口：订单(销售)数据导入Action
 * 
 * 从聚石塔获取订单数据并存入新后台电商接口表及发送销售MQ
 * @author jijw
 *
 * @version  2015-9-16
 */
public class BINBAT121_BL {
	
	private static CherryBatchLogger logger = new CherryBatchLogger(BINBAT121_BL.class);	
	
    @Resource(name="saleInfoLogic")
    private SaleInfoLogic saleInfoLogic;
	
	@Resource
	private BINBAT121_Service binbat121_Service;
	
	/** JOB执行相关共通 IF */
	@Resource(name="binbecm01_IF")
	private BINBECM01_IF binbecm01_IF;
	
    @Resource(name="saleInfoService")
    private SaleInfoService saleInfoService;
    
    @Resource(name="binOLCM03_BL")
    private BINOLCM03_BL binOLCM03_BL;
    
    @Resource(name="binOLCM15_BL")
    private BINOLCM15_BL binOLCM15_BL;
    
    @Resource(name="binOLCM18_BL")
    private BINOLCM18_BL binOLCM18_BL;
    
    @Resource(name="binBEMQMES97_BL")
    private BINBEMQMES97_BL binBEMQMES97_BL;
    
    @Resource(name="binOTHONG01_Service")
    private BINOTHONG01_Service binOTHONG01_Service;
    
    /** 系统配置项 共通BL */
	@Resource(name="binOLCM14_BL")
	private BINOLCM14_BL binOLCM14_BL;
	
	@Resource(name="binOLCM60_BL")
    private BINOLCM60_BL binOLCM60_BL;
	
	/** BATCH处理标志 */
	private int flag = CherryBatchConstants.BATCH_SUCCESS;
	
	/** 每批次(页)处理数量 10 */
	private final int BATCH_SIZE = 10;
	
	private Map<String, Object> comMap = new HashMap<String, Object>();
	
	/** 处理总条数 */
	private int totalCount = 0;
	/** 插入条数 */
	private int insertCount = 0;
	/** 更新条数 */
	private int updateCount = 0;
	/** 失败条数 */
	private int failCount = 0;
	
	/** 失败的主要原因，受字段长度限制，这里只要记录主要原因即可 */
	private String fReason = "";
	
	/** 导入失败的单据 */
	private List<String> faildSaleBillList = new ArrayList<String>();
	
	/**
	 * batch处理
	 * 
	 * @param 无
	 * 
	 * @return Map
	 * @throws CherryBatchException
	 */
	@SuppressWarnings("unchecked")
	public int tran_batchBat121(Map<String, Object> paraMap)
			throws CherryBatchException,Exception {

		// 初始化
		try{
			init(paraMap);
		}catch(Exception e){
			
			// 初始化失败
			BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO();
			batchExceptionDTO.setBatchName(this.getClass());
			batchExceptionDTO.setErrorCode("ECM00005");
			batchExceptionDTO.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
			batchExceptionDTO.setException(e);
			flag = CherryBatchConstants.BATCH_ERROR;
			throw new CherryBatchException(batchExceptionDTO);
		}
		
		// 主程序调用
		Map<String,Object> resultMap = syncOrder(paraMap);
		
		// 日志
		outMessage();
		
		// 程序结束时，处理Job共通(更新Job控制表 、插入Job运行履历表)
		programEnd(paraMap);
		
		return flag;
	}
	
	/**
	 * 主程序
	 * 
	 * @param paramMap
	 * @return
	 * @throws CherryBatchException
	 */
	@SuppressWarnings("unchecked")
	private Map<String, Object> syncOrder(Map<String, Object> paramMap)throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		// 定义品牌对应的店铺配置信息
		List<Map<String, Object>> esInterfaceConfList = null;
		
		try{
			// 查询电商接口配置信息取得品牌相关店铺的配置信息
			Map<String, Object> esInterFaceMap = new HashMap<String, Object>();
			esInterFaceMap.put("BIN_OrganizationInfoID",paramMap.get(CherryBatchConstants.ORGANIZATIONINFOID));
			esInterFaceMap.put("BIN_BrandInfoID",paramMap.get(CherryBatchConstants.BRANDINFOID));
			esInterFaceMap.put("ESCode", "pekon"); // ESCode暂定为Pekon
			esInterFaceMap.put("TradeCode", "GetJstTrade"); // 聚石塔订单查询
			
			esInterfaceConfList = binbat121_Service.getESInterfaceInfo(esInterFaceMap);
			
		}catch(Exception e){
			// 异常信息，未
			BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO();
			batchExceptionDTO.setBatchName(this.getClass());
			batchExceptionDTO.setErrorCode("EOT00091");
			batchExceptionDTO.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
			batchExceptionDTO.setException(e);
			flag=CherryBatchConstants.BATCH_ERROR;
			throw new CherryBatchException(batchExceptionDTO); 
		}
		
		//判断电商接口配置信息是否为空
		if (CherryBatchUtil.isBlankList(esInterfaceConfList)) {
			BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO();
			batchExceptionDTO.setBatchName(this.getClass());
			batchExceptionDTO.setErrorCode("EOT00091");
			batchExceptionDTO.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
			flag=CherryBatchConstants.BATCH_ERROR;
			throw new CherryBatchException(batchExceptionDTO); 
		}
		
		// 循环多个商铺接口地址信息
		for (int i = 0; i < esInterfaceConfList.size(); i++) {

			try{
				
				// 取得品牌对应店铺的电商接口配置信息
				Map<String, Object> esInterfaceConfMap = esInterfaceConfList.get(i);
				
				// 校检电商接口表的数据是否完整
				chkESInterfaceConf(esInterfaceConfMap);
				
				
				// 配置调用聚石塔WebService接口的参数信息
				Map<String, Object> wsParamMap = configWebServiceParam(esInterfaceConfMap);
				
				// 翻页取得聚石塔订单数据
				int pageNo = 1; // 起始页
				boolean isSuc = true; // 定义处理订单成功
				while(true){
					
					try{
						// 设置参数(包括请求起始页)
						wsParamMap.put("PageNo", ConvertUtil.getString(pageNo));
						wsParamMap.put("brandCode", comMap.get(CherryBatchConstants.BRAND_CODE)); //
						wsParamMap.put("brandCode", "JST"); // 测试用，上线时删除
						wsParamMap.put("TimeStamp", binbat121_Service.getSYSDateTime());
						
						// 写入调用参数日志
			            writeLog(wsParamMap, pageNo);
						
						// 调用第三方WebService获取电商订单数据
				        Map<String,Object> wsResultMap = WebserviceClient.accessPekonWebService(wsParamMap,ConvertUtil.getString(esInterfaceConfMap.get("URL")));
				        
				        if(null == wsResultMap && wsResultMap.isEmpty()){
				        	// 返回为空，报错
			        		resultMap.put("Result", "WSE0041");
			        		resultMap.put("ErrMsg", "WebService返回异常(调用业务接口【\"GetJstTrade\"】)");
							logger.outLog("调用WebService返回内容为空。",CherryBatchConstants.LOGGER_ERROR);
							flag = CherryBatchConstants.BATCH_ERROR;
							isSuc = false;
							break;
				        }
				        
				        if(null != wsResultMap && !wsResultMap.isEmpty()){
				        	
				        	if(!"0".equals(ConvertUtil.getString(wsResultMap.get("ERRORCODE")))){
				        		//调用WebServicve返回ErrorCode不等于0时，代表出现异常，需记录日志信息
				        		logger.outLog("调用WebService异常。ERRORCODE：" + ConvertUtil.getString(wsResultMap.get("ERRORCODE")),CherryBatchConstants.LOGGER_ERROR);
				        		logger.outLog("调用WebService异常。ERRORMSG：" + ConvertUtil.getString(wsResultMap.get("ERRORMSG")),CherryBatchConstants.LOGGER_ERROR);
				        		flag = CherryBatchConstants.BATCH_ERROR;
				        		isSuc = false;
				        		break;
				        	}
				        	
				        	Map<String, Object> orderResultMap = (Map<String, Object>)wsResultMap.get("ResultContent");
				        	if(null == orderResultMap && orderResultMap.isEmpty()){
				        		//当调用webService返回ErrorCode等于0,但返回的ResultMap为空时，返回异常，记录日志信息
				        		resultMap.put("Result", "WSE9992");//未能查询到指定的数据
				        		resultMap.put("ErrMsg", "WebService调用成功，电商订单单据【ResultContent】返回为空");
								logger.outLog("调用WebService返回ResultContent内容为空.",CherryBatchConstants.LOGGER_ERROR);
								flag = CherryBatchConstants.BATCH_ERROR; // 暂定为错误
								isSuc = false;
								break;
				        	}
				        	
				        	List<Map<String, Object>> ordersList = (List<Map<String, Object>>)orderResultMap.get("OrderList");
				        	totalCount += ordersList.size();
				        	try{
				        		// 调用聚石塔获取订单数据Webservice接口获取订单数据，进行逻辑操作(1.插入电商相关信息表 2.发送销售MQ)
				        		if(!CherryBatchUtil.isBlankList(ordersList)){
				        			handleOrder(ordersList,wsParamMap);
				        		}
				        		
				        		// 处理成功后，记录当前页最后的ModifiedTime，处理失败时，写入上一页的ModifiedTime到电商接口表的GetDataEndTime字段 并break当前while
				        		// 待写**********************************
				        	}catch(Exception ex){
				        		// 处理成功后，记录当前页最后的ModifiedTime，处理失败时，写入上一页的ModifiedTime到电商接口表的GetDataEndTime字段 并break当前while
				        		fReason += ex.getMessage();
				        		
				        		// 待写**********************************
				        		isSuc = false;
				        		break;
				        	}
				        	
				        	if(ordersList.size() < BATCH_SIZE){
				        		break;
				        	}
				        }
				        
						pageNo++;
						
					}catch(Exception whileException){
						// 当前店铺处理失败
						
					    BatchLoggerDTO batchLoggerDTO1 = new BatchLoggerDTO();
					    batchLoggerDTO1.setCode("IOT00003");
					    batchLoggerDTO1.setLevel(CherryBatchConstants.LOGGER_INFO);
					    batchLoggerDTO1.addParam(ConvertUtil.getString(wsParamMap.get("Nick")));
					    batchLoggerDTO1.addParam(ConvertUtil.getString(wsParamMap.get("StartTime")));
					    batchLoggerDTO1.addParam(ConvertUtil.getString(wsParamMap.get("EndTime")));
					    batchLoggerDTO1.addParam(ConvertUtil.getString(pageNo));
					    batchLoggerDTO1.addParam(ConvertUtil.getString(BATCH_SIZE));
					    logger.BatchLogger(batchLoggerDTO1);
					    logger.outLog(wsParamMap.toString(),CherryBatchConstants.LOGGER_ERROR);
					    logger.outLog("当前分页处理失败,异常信息如下",CherryBatchConstants.LOGGER_ERROR);
					    logger.outExceptionLog(whileException);
					    
					    fReason += whileException.getMessage();
					    isSuc = false;
					    break;
					}
					
				}
				
				// 如果成功，则将[esInterfaceInfoID+"_GetDataEndTime"]写入电商接口表
				if(isSuc){
					//处理完毕，更新电商接口信息表的最后一次访问时间，最后一次获取数据的截止时间
					Map<String,Object> updateESInterfaceInfo = new HashMap<String,Object>();
					updateESInterfaceInfo.putAll(comMap);
					updateESInterfaceInfo.put("BIN_OrganizationInfoID",comMap.get("organizationInfoId"));
					updateESInterfaceInfo.put("BIN_BrandInfoID",comMap.get("brandInfoId"));
					updateESInterfaceInfo.put("BIN_ESInterfaceInfoID", esInterfaceConfMap.get("BIN_ESInterfaceInfoID"));
					// 当前店铺截取数据截止时间，若订单处理成功 ，将其存入电商接口表的GetDataEndTime字段 
					String esInterfaceInfoID = ConvertUtil.getString(esInterfaceConfMap.get("BIN_ESInterfaceInfoID"));
					updateESInterfaceInfo.put("GetDataEndTime", wsParamMap.get(esInterfaceInfoID + "_GetDataEndTime"));
					updateESInterfaceInfo.put("LastAccessTime", comMap.get("RunStartTime"));
					
					binbat121_Service.updateESInterfaceInfoLastTime(updateESInterfaceInfo);
				}
				
			}catch(Exception e){
				// 需要足够的日志信息
				// 处理完后，继续continue
				flag = CherryBatchConstants.BATCH_ERROR;
				// 处理当前店铺发生异常
				logger.outLog("处理当前店铺(" + ConvertUtil.getString(esInterfaceConfList.get(i).get("AccountName")) + ")发生异常：" + e.getMessage(),CherryBatchConstants.LOGGER_ERROR);
				logger.outLog(esInterfaceConfList.get(i).toString(),CherryBatchConstants.LOGGER_ERROR);
				
				 fReason += e.getMessage();
				
				// 继续处理品牌下一个店铺的订单数据
				continue;
			}
			
		}
		return resultMap;
	}

	/**
	 * 校检接口信息 参数
	 * @param esInterfaceInfoMap
	 * @throws CherryBatchException
	 */
	private void chkESInterfaceConf(Map<String, Object> esInterfaceInfoMap) throws CherryBatchException {
		// 校检接口信息 参数 
		checkESInterfaceInfo(esInterfaceInfoMap, "GetJstTrade", "AccountName"); // nick
//		checkESInterfaceInfo(esInterfaceInfoMap, "GetJstTrade", "URL"); 
		checkESInterfaceInfo(esInterfaceInfoMap, "GetJstTrade", "LastAccessTime");
		checkESInterfaceInfo(esInterfaceInfoMap, "GetJstTrade", "GetDataEndTime");
		checkESInterfaceInfo(esInterfaceInfoMap, "GetJstTrade", "TimeStep");
	}

	/**
	 * 写入调用参数日志
	 * @param wsParamMap
	 * @param pageNo
	 * @throws CherryBatchException
	 */
	private void writeLog(Map<String, Object> wsParamMap, int pageNo)
			throws CherryBatchException {
		//此记录订单数量为单个商铺下的所有订单的总量
//		if(pageNo == 1){
		    //查询条件写入日志
		    BatchLoggerDTO batchLoggerDTO1 = new BatchLoggerDTO();
		    batchLoggerDTO1.setCode("IOT00003");
		    batchLoggerDTO1.setLevel(CherryBatchConstants.LOGGER_INFO);
		    batchLoggerDTO1.addParam(ConvertUtil.getString(wsParamMap.get("Nick")));
		    batchLoggerDTO1.addParam(ConvertUtil.getString(wsParamMap.get("StartTime")));
		    batchLoggerDTO1.addParam(ConvertUtil.getString(wsParamMap.get("EndTime")));
		    batchLoggerDTO1.addParam(ConvertUtil.getString(pageNo));
		    batchLoggerDTO1.addParam(ConvertUtil.getString(BATCH_SIZE));
		    logger.BatchLogger(batchLoggerDTO1);
		    logger.outLog(wsParamMap.toString());
//		}
	}
	
	/**
	 * 配置调用聚石塔WebService接口的参数信息
	 * @param orderInfoMap
	 * @return
	 * @throws Exception
	 */
	private Map<String,Object> configWebServiceParam(Map<String,Object> esInterfaceConfMap) throws Exception{
		
		// 定义调用相关参数
		Map<String,Object> wsParams = new HashMap<String,Object>();
		
		String startTime = ConvertUtil.getString(esInterfaceConfMap.get("GetDataEndTime")); // 程序上一次截取订单数据的截止时间
		
		int timeStep = CherryUtil.obj2int(esInterfaceConfMap.get("TimeStep")); // 步进 
		String endTime = DateUtil.addDateByMinutes(DateUtil.DATETIME_PATTERN,startTime, timeStep); // 计算数据截取结束时间(上一次截取订单数据的截止时间)
//		String sysDateTime = CherryUtil.getSysDateTime(DateUtil.DATETIME_PATTERN);  
		String curSysDateTime = binbat121_Service.getSYSDateTime();  
		if(DateUtil.compareDate(endTime,curSysDateTime ) > 0 ){
        	// 控制截止时间不能超过当前服务器时间-1
			endTime = DateUtil.addDateByMinutes(DateUtil.DATETIME_PATTERN, curSysDateTime, -1);
		}
		
		// 当前店铺截取数据截止时间，若订单处理成功 ，将其存入电商接口表的GetDataEndTime字段 
		String esInterfaceInfoID = ConvertUtil.getString(esInterfaceConfMap.get("BIN_ESInterfaceInfoID"));
		wsParams.put(esInterfaceInfoID + "_GetDataEndTime",endTime);  
		
		// --------------------------定义调用WebSevice使用的参数 start-----------------------------------------------------------------------------------
		String searchType = "upd"; // crt按照订单创建时间，upd按照订单修改时间
		
		
		String extJson = ConvertUtil.getString(esInterfaceConfMap.get("ExtJson"));
    	Map<String, Object> extMap = (Map<String, Object>) JSONUtil.deserialize(extJson);
		wsParams.put("appID", extMap.get("appID"));
		
		// //不处理订单创建为initCreateTime的数据
		comMap.put("initCreateTime", extMap.get("initCreateTime"));
		
//		wsParams.put("BrandCode", "JST");// 仅仅是作为必填参数作用webservice服务端验证使用（上线时，RDS的CherryBrand_XXX数据库需要有该BrandCode）
		
		wsParams.put("Nick", ConvertUtil.getString(esInterfaceConfMap.get("AccountName")));
		
		wsParams.put("TradeType", "GetJstTrade");
		
		wsParams.put("StartTime", startTime);
		
		wsParams.put("EndTime", endTime);
		
		wsParams.put("SearchType", extMap.get("searchType"));
//		wsParam.put("PageNo", pageNo); // 起始页，在外边通过while循环控制
		
		wsParams.put("PageSize", ConvertUtil.getString(BATCH_SIZE));
		
//		wsParams.put("TimeStamp", curSysDateTime);
		
		wsParams.put("DataSource", "Other");
		
		// --------------------------定义调用WebSevice使用的参数  end -----------------------------------------------------------------------------------
		
		return wsParams;
	}
	
	/**
	 * 处理订单数据
	 * 1、写入电商信息相关表
	 * 2、调用销售MQ
	 * @param orderList
	 * @param wsParamMap
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private Map<String, Object> handleOrder(List<Map<String, Object>> orderList,Map<String, Object> wsParamMap) throws Exception {
		DecimalFormat df = new DecimalFormat("#0.00");

		// 预处理可能失败的件数
		int prepFailCount = 0;

		// 记录单个商铺下的单据数量
		prepFailCount = orderList.size();
		StringBuffer billCodes = new StringBuffer();
		
		try {
			
			// 员工信息处理
			String baCode = "G00001";// 员工代码（EmployeeCode）暂定
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("BIN_OrganizationInfoID",comMap.get("organizationInfoId"));
			param.put("BIN_BrandInfoID", comMap.get("brandInfoId"));
			param.put("EmployeeCode", baCode);
			// 获取员工信息
			List<Map<String, Object>> employeeList = binbat121_Service.getEmployeeInfo(param);
			String employeeID = "";
			if (!CherryBatchUtil.isBlankList(employeeList)) {
				// 获得员工ID
				employeeID = ConvertUtil.getString(employeeList.get(0).get("BIN_EmployeeID"));
			}
			
            //以下代码为调用共通所使用的getMemInfo方法
    		Map<String, Object> departParamMap = new HashMap<String, Object>();
    		departParamMap.put("BIN_OrganizationInfoID",comMap.get("organizationInfoId"));
    		departParamMap.put("BIN_BrandInfoID",comMap.get("brandInfoId"));
    		// 查询组织结构ID
    		departParamMap.put("DepartName", wsParamMap.get("Nick"));
    		List<Map<String, Object>> departList = binbat121_Service.getDepartInfo(departParamMap);
    		
    		String organizationID = "";
    		String counterCode = "";
    		if (!CherryBatchUtil.isBlankList(departList)) {
    			organizationID = ConvertUtil.getString(departList.get(0).get("BIN_OrganizationID"));
    			counterCode = ConvertUtil.getString(departList.get(0).get("DepartCode"));
    		}
    		
    		// 循环处理订单
			for (int i = 0; i < orderList.size(); i++) {

				// 得到商铺下的单个主单据（包括明细数据）
				Map<String, Object> orderMainMap = orderList.get(i);
				orderMainMap.put("BIN_OrganizationInfoID",comMap.get("organizationInfoId"));
				orderMainMap.put("BIN_BrandInfoID",comMap.get("brandInfoId"));

				String orderNumber = ConvertUtil.getString(orderMainMap.get("OrderNumber"));
				// 获取订单编号
				billCodes.append("\"").append(orderNumber).append("\" ");

				// 交易状态转换新后台可用值 待确定  1:生成；2：付款；3：发货；4：完成；0：取消；  -1：付款后退款成功
				String billState = convertOrderStatus(ConvertUtil.getString(orderMainMap.get("OrderStatus")));

				// 退款处理 
				boolean hasRefundDetail = false;// 明细是否有退款标志
				boolean allRefund = true;// 明细全部是退款标志【明细全部为退款则不管主单的状态为何都认为是订单取消】
				
				// 一个主单据下可包含多个明细订单数据 循环多个明细数据
				List<Map<String, Object>> orderDetailList = (List<Map<String, Object>>) orderMainMap.get("OrderDetailList");
				
				// 定义订单明细参数
				Map<String, Object> orderDetailParamMap = new HashMap<String, Object>();
				
				if (!CherryBatchUtil.isBlankList(orderDetailList)) {
					for (int j = 0; j < orderDetailList.size(); j++) {
						// 获得单个主单据下的单个明细单数据
  						Map<String, Object> orderDetailMap = orderDetailList.get(j);
 						orderDetailParamMap.putAll(orderDetailMap);
 						String createdTime = ConvertUtil.getString(orderMainMap.get("CreatedTime"));
						orderDetailParamMap.put("TradeDateTime", createdTime);
						
						//不处理initCreateTime时间之前的数据
						String initCreateTime = ConvertUtil.getString(comMap.get("initCreateTime"));
                        SimpleDateFormat sdf = new SimpleDateFormat(CherryConstants.DATE_PATTERN_24_HOURS);
                        Date orderDateTime = sdf.parse(createdTime);
                        if(DateUtil.compareDate(sdf.format(orderDateTime), initCreateTime) < 0){
                            continue;
                        }
                        
						// 获得明细数据的退款状态
						String refundStatus = convertRefundStatus(ConvertUtil.getString(orderDetailMap.get("RefundStatus")));
						// billState 交易状态为0：退款中 1：退款成功
  						if ("1".equals(refundStatus) || "0".equals(billState) ) {
							hasRefundDetail = true;
							
							orderDetailMap.put("ItemCount", "0");
							orderDetailMap.put("discountFee", "0.00");
							
						} else {
							allRefund = false;
						}
  						
					}
				} else {
					// 订单明细没有，需要有提示信息
				}

				// 计算明细的总数量
//				int totalQuantity = getTotalQuantity(orderDetailList);
				Map<String,Object> jsTotalInfo = getJSTotalInfo(orderDetailList, orderMainMap);
				
//				orderMainMap.put("totalQuantity", totalQuantity);
				orderMainMap.put("totalQuantity", jsTotalInfo.get("totalQuantity"));
				orderMainMap.put("jsTotalAmount", jsTotalInfo.get("jsTotalAmount")); // 整个订单的实付金额
				orderMainMap.put("jsZDZKAmount", jsTotalInfo.get("jsZDZKAmount")); // 整个订单的优惠金额(作为整单折扣[促销品])
				orderDetailParamMap.put("jsZDZKAmount", jsTotalInfo.get("jsZDZKAmount")); // 整个订单的优惠金额(作为整单折扣[促销品])
				
				orderMainMap.put("BIN_EmployeeID", employeeID);
				orderMainMap.put("EmployeeCode", baCode);

				// 以下字段待确定,是哪里会使用到，需要注明
				orderMainMap.put("Ticket_type", "NE");
				orderMainMap.put("ModifiedTimes", "0");
				orderMainMap.put("BillType", "1");
				
        		orderMainMap.put("BIN_OrganizationID", organizationID);
        		orderMainMap.put("CounterCode", counterCode);
        		
                String mobilePhone = ConvertUtil.getString(orderMainMap.get("ReceiverMobile"));
                // 电商订单表中的手机号字段长度最大为18位，超过此限制的截取前11位作为手机号。

                orderMainMap.put("ReceiverMobile", (mobilePhone.length() > 18 ? mobilePhone.substring(0, 11) : mobilePhone));
				// 获得会员信息
				Map<String, Object> memberInfoMap = getMemberInfo(orderMainMap);

				// 以下参数可能会出现空的情况，需注意
				orderMainMap.put("BIN_MemberInfoID",memberInfoMap.get("BIN_MemberInfoID"));
				orderMainMap.put("MemberCode", memberInfoMap.get("MemberCode"));
				orderMainMap.put("MemberName", memberInfoMap.get("MemberName"));
				orderMainMap.put("MemberLevel", memberInfoMap.get("MemberLevel"));
				
				// 处理电商订单主表
				// 根据接口返回的数据在新后台查出需要的数据
				// 判断是否已经存在电商订单
				String oldBillState = "";
				if (allRefund || billState.equals("0") ) {
					billState = "0";
					orderMainMap.put("totalAmount", "0");
					orderMainMap.put("jsTotalAmount", "0");
					orderMainMap.put("jsZDZKAmount", "0");
				}
				orderDetailParamMap.put("BillState", billState);
				orderMainMap.put("BillState", billState);
				// 查询billCode是否存在等于orderNumber的主单数据
				// 单个单据查询应为单条，但如改变为Map方式则会出现多条会出现错误，现还保留List的使用方式
				List<Map<String, Object>> existsOrderList = binbat121_Service.getESOrderMain(orderMainMap);
				List<Map<String, Object>> payDetailList = new ArrayList<Map<String, Object>>();
				int esOrderMainID = 0;
				int modifiedTimes = 0;
				orderDetailParamMap.put("BIN_OrganizationID",organizationID);
				orderDetailParamMap.put("BrandCode", wsParamMap.get("brandCode"));
				orderDetailParamMap.put("CounterCode", orderMainMap.get("CounterCode"));
				
                /**
                 * 对于预付定金的订单的处理：
                 * 一般逻辑：因为主单状态为下单未付款，故不发送销售MQ
                 * 特殊逻辑：对于下单未付款且付款日期不为空的订单也将发送MQ
                 */
                // 付款时间
                String payTime = ConvertUtil.getString(orderMainMap.get("PayTime"));
                // 【支持预付定金的订单发送销售MQ】
                boolean depositOrderToSale = ConvertUtil.getString(PropertiesUtil.getMessage("DepositOrderToSale", null)).equals("true");
                // 已经接收过的订单的付款时间
                String oldPayTime = "";
                // 已经接在系统中查到的产品厂商ID
                String oldPrtVendorId = "";

                /**
				 * 对于电商主从表的处理： 1）电商订单数据已经存在：更新电商订单主表信息，删除电商订单明细表及支付方式明细表后重新插入。
				 * 2）电商订单数据不存在：新增电商订单主从表及电商支付方式表
				 */
				if (!CherryBatchUtil.isBlankList(existsOrderList)) {
					// 存在更新主表，删除明细后重新插入
					esOrderMainID = CherryUtil.obj2int(existsOrderList.get(0).get("BIN_ESOrderMainID")); // 电商订单主表ID
					orderMainMap.put("BIN_ESOrderMainID", esOrderMainID);
					modifiedTimes = CherryUtil.obj2int(existsOrderList.get(0).get("ModifiedTimes")) + 1;
					orderMainMap.put("ModifiedTimes", modifiedTimes);
					oldBillState = ConvertUtil.getString(existsOrderList.get(0).get("BillState"));
					
					oldPayTime = ConvertUtil.getString(existsOrderList.get(0).get("BillPayTime")); // 订单支持时间
					oldPrtVendorId = ConvertUtil.getString(existsOrderList.get(0).get("BIN_ProductVendorID")); // 产品厂商 ID
					
					updateESOrderMain(orderMainMap);
					saleInfoService.deleteESOrderDetail(orderMainMap);
					saleInfoService.deleteESOrderPayList(orderMainMap);
					
					// 处理订单明细
					orderDetailParamMap.put("BIN_ESOrderMainID", esOrderMainID);
			    	
					// 快递费
			    	orderDetailParamMap.put("PostFee", orderMainMap.get("PostFee"));
			    	
					boolean ishasNonGoods = addESOrderDetail(orderDetailParamMap, orderDetailList);
					
					// 处理付款方式明细
					Map<String, Object> payInfo = new HashMap<String, Object>();
					// 付款类型是否需要转换，待确定
					payInfo.put("PayTypeCode", orderMainMap.get("PayType"));
					
					// 支付金额（jsTotalAmount减去积分抵扣金额）
					BigDecimal payAmount = new BigDecimal(ConvertUtil.getString(orderMainMap.get("jsTotalAmount")));
					BigDecimal pointPrice = new BigDecimal(ConvertUtil.getString(orderMainMap.get("PointFee"))).multiply(new BigDecimal("0.01"));
					payAmount = payAmount.subtract(pointPrice);
					payInfo.put("PayAmount",payAmount);	
					
					payDetailList.add(payInfo);
					insertESOrderPayList(orderMainMap,orderDetailParamMap, payDetailList);
					
					// 明细中的产品在新后台都存在时，才能发送MQ
					if(ishasNonGoods){
						
						if (oldBillState.equals("1") && (billState.equals("2") || billState.equals("3") || billState.equals("4"))) {
							
							if(depositOrderToSale && !"".equals(oldPayTime) && !"".equals(oldPrtVendorId)) {
								// 对于已经发送过MQ(通过新逻辑获取的订单)--此为支付尾款的MQ，应发修改销售MQ
								orderMainMap.put("Ticket_type", "MO");
								saleInfoLogic.sendMQ_NS(getMQData_NS(orderMainMap,orderDetailList,payDetailList));
								
							} else{
								// 电商订单更新，BillState由1变成2/3/4时，需要发送销售MQ 
								saleInfoLogic.sendMQ_NS(getMQData_NS(orderMainMap,orderDetailList,payDetailList));
							}
						} else {
							
							if (hasRefundDetail) {
								
								if(depositOrderToSale && !"".equals(oldPayTime) && oldBillState.equals("1") && !"".equals(oldPrtVendorId)) {
									// 预售单
									// 1、已经发送过MQ的，对于退款明细应发送修改销售MQ
									orderMainMap.put("Ticket_type", "MO");
									saleInfoLogic.sendMQ_NS(getMQData_NS(orderMainMap,orderDetailList,payDetailList));
									
								} else{
									// 非预付定金的订单--走老逻辑
									if ((oldBillState.equals("0") && billState.equals("0"))
											|| (oldBillState.equals("1") && billState.equals("0"))
											|| billState.equals("1") 
											|| "".equals(oldPrtVendorId)) {
										// 单据状态1->0,1或其他->1没有生成过销售MQ，不需要发送修改销售MQ 
										// 0->0，也不发修改销售MQ
										
									}else {
										// 明细isRefund=1，表示退货，如果都是1，单据状态改为订单取消。发送修改销售MQ
										orderMainMap.put("Ticket_type", "MO");
										saleInfoLogic.sendMQ_NS(getMQData_NS(orderMainMap,orderDetailList,payDetailList));
									}
								}
								
							} else{
								if((!oldBillState.equals(billState) && !billState.equals("1")) && !"".equals(oldPrtVendorId)) {
									// 电商订单状态发生改变还需要发送更改销售单据状态MQ 
									saleInfoLogic.sendMQ_SC(getMQData_SC(orderMainMap,orderDetailList,payDetailList));
								} else{
									// 预售 且刚付款的单子
									if(depositOrderToSale){
										if(billState.equals("1") && "".equals(oldPayTime) && !"".equals(payTime) ){
											// 订单新老状态为1，付款后发送MQ
											saleInfoLogic.sendMQ_NS(getMQData_NS(orderMainMap,orderDetailList,payDetailList));
										}
										
									} else if("".equals(oldPrtVendorId)){
										// 非预售单子，之前没有发送MQ，补发MQ
										saleInfoLogic.sendMQ_NS(getMQData_NS(orderMainMap,orderDetailList,payDetailList));
									}
									
								}
							} 
						} 
					}
					
				} else {
					// 处理订单主数据
					esOrderMainID = addESOrderMain(orderMainMap);
					orderDetailParamMap.put("BIN_ESOrderMainID", esOrderMainID);
					
					// 快递费
			    	orderDetailParamMap.put("PostFee", orderMainMap.get("PostFee"));
					
					// 处理订单明细
			    	boolean ishasNonGoods = addESOrderDetail(orderDetailParamMap, orderDetailList);
					
					// 处理付款方式明细
					Map<String, Object> payInfo = new HashMap<String, Object>();
					payInfo.put("PayTypeCode", orderMainMap.get("PayType"));// 付款类型
					
					// 应付金额（jsTotalAmount减去积分抵扣金额）
					BigDecimal payAmount = new BigDecimal(ConvertUtil.getString(orderMainMap.get("jsTotalAmount")));
					BigDecimal pointPrice = new BigDecimal(ConvertUtil.getString(orderMainMap.get("PointFee"))).multiply(new BigDecimal("0.01"));
					payAmount = payAmount.subtract(pointPrice);
//					payInfo.put("PayAmount",orderMainMap.get("jsTotalAmount"));// 应付金额，
					payInfo.put("PayAmount",payAmount);	
					payDetailList.add(payInfo);
					insertESOrderPayList(orderMainMap,orderDetailParamMap, payDetailList);

					// 明细中的产品在新后台都存在时，才能发送MQ
					if(ishasNonGoods){
						if ( ( depositOrderToSale && !"".equals(payTime) && billState.equals("1") ) || billState.equals("2") || billState.equals("3") || billState.equals("4")) {
							// 电商订单新增，[BillState是2/3/4]或[BillState为且payTime不为空]，需要发送销售MQ 
							saleInfoLogic.sendMQ_NS(getMQData_NS(orderMainMap,orderDetailList,payDetailList));
						}
					}
				}
			}
			binbat121_Service.manualCommit();

		} catch (Exception e) {

			failCount += prepFailCount;
			// 回滚
			binOTHONG01_Service.manualRollback();

			BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
			batchLoggerDTO.setCode("EOT00096");
			batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
			batchLoggerDTO.addParam(ConvertUtil.getString(billCodes));
			batchLoggerDTO.addParam(ConvertUtil.getString(wsParamMap.get("Nick")));
			batchLoggerDTO.addParam(ConvertUtil.getString(wsParamMap.get("StartTime")));
			batchLoggerDTO.addParam(ConvertUtil.getString(wsParamMap.get("EndTime")));
			batchLoggerDTO.addParam(ConvertUtil.getString(wsParamMap.get("PageNo")));
			batchLoggerDTO.addParam(ConvertUtil.getString(wsParamMap.get("PageSize")));
			CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
			cherryBatchLogger.BatchLogger(batchLoggerDTO, e);
			flag = CherryBatchConstants.BATCH_WARNING;
			// 扔出异常，记录详细日志
			throw new Exception(e);
		}
		return null;
	}
	/**
	 * 查询该订单收货人的会员信息
	 * @param orderMainMap
	 * @param wsParamMap
	 * @return
	 * @throws Exception
	 */
	private Map<String, Object> getMemberInfo(Map<String, Object> orderMainMap) throws Exception {
		Map<String, Object> memberParamMap=new HashMap<String, Object>();

		String mobilePhone = ConvertUtil.getString(orderMainMap.get("ReceiverMobile"));
		String brandCode = ConvertUtil.getString(comMap.get(CherryBatchConstants.BRAND_CODE));
		mobilePhone = CherryBatchSecret.encryptData(brandCode, mobilePhone);
		
		memberParamMap.put("BIN_OrganizationInfoID", comMap.get("organizationInfoId"));
		memberParamMap.put("BIN_BrandInfoID", comMap.get("brandInfoId"));
		memberParamMap.put("BIN_EmployeeID", orderMainMap.get("BIN_EmployeeID"));
		memberParamMap.put("EmployeeCode", orderMainMap.get("EmployeeCode"));
		memberParamMap.put("orderDate", orderMainMap.get("CreatedTime"));
		memberParamMap.put("telephone", orderMainMap.get("ReceiverPhone"));
		memberParamMap.put("MemberAddress", orderMainMap.get("ReceiverAddress"));
		memberParamMap.put("brandCode", brandCode);
		memberParamMap.put("shopName", orderMainMap.get("ShopName"));// 柜台名称=店铺名称
		memberParamMap.put("orderWay", orderMainMap.get("OrderWay"));// 会员数据来源=来源方式
		memberParamMap.put("MobilePhone", mobilePhone);// 会员手机号=收货人手机号
		memberParamMap.put("BIN_OrganizationID", orderMainMap.get("BIN_OrganizationID"));
		memberParamMap.put("CounterCode", orderMainMap.get("CounterCode"));
		memberParamMap.put("MemberName", orderMainMap.get("ReceiverName"));// 会员名称=收货人
		memberParamMap.put("nickName", orderMainMap.get("BuyerNick"));// 淘宝昵称
		//调用查询会员信息共通方法，得到会员信息
		Map<String, Object> resultMap = binOLCM60_BL.getMemInfo(memberParamMap);
		return resultMap;
	}
	
	/**
     * 转换销售MQ需要的数据
     * @param dataMap
     * @return
     * @throws ParseException 
     */
    private Map<String,Object> getMQData_NS(Map<String,Object> orderMainMap,List<Map<String,Object>> orderDetailList,List<Map<String,Object>> payDetailList) throws ParseException{
       
    	/*
        // 通过原始订单商品明细拆分BOM商品数据--通过系统配置项确定是否调用该方法(电商订单支持套装BOM产品拆分)
        int organizationInfoID = CherryUtil.obj2int(orderMainMap.get("BIN_OrganizationInfoID"));
        int brandInfoID = CherryUtil.obj2int(orderMainMap.get("BIN_BrandInfoID"));
        boolean isSplitPrtBom = binOLCM14_BL.isConfigOpen("1338", organizationInfoID, brandInfoID);
        if(isSplitPrtBom){
        	// 将销售明细中的产品类型为BOM的商品进行拆分，将拆分后的商品加入到销售MQ明细集合中
        	orderDetailList = splitBomPrt(orderMainMap, orderDetailList);
        }
        */
    	
    	Map<String,Object> resultMap = new HashMap<String,Object>();
        Map<String,Object> mainData = new HashMap<String,Object>();
        mainData.put("BrandCode", comMap.get(CherryBatchConstants.BRAND_CODE));
        mainData.put("TradeNoIF", orderMainMap.get("OrderNumber"));
        mainData.put("ModifyCounts", orderMainMap.get("ModifiedTimes"));
        mainData.put("CounterCode", orderMainMap.get("CounterCode"));
        mainData.put("RelevantCounterCode", "");
        mainData.put("TotalQuantity", orderMainMap.get("totalQuantity")); 
        
        // 总金额 2016.02.02 应付总金额需要减去运费，但是当运费为0时，则不需要
        String jsTotalAmount = getFormatDiscount(ConvertUtil.getString(orderMainMap.get("jsTotalAmount"))); // 应付金额
        String deliveryCost = getFormatDiscount(ConvertUtil.getString(orderMainMap.get("PostFee"))); // 运费
        DecimalFormat df = new DecimalFormat("#0.00");
        if(new BigDecimal(0).compareTo(new BigDecimal(jsTotalAmount)) != 0){
        	jsTotalAmount = df.format(new BigDecimal(jsTotalAmount).subtract(new BigDecimal(deliveryCost))); // 销售的总金额需要去除运费
        }
        
        mainData.put("TotalAmount", jsTotalAmount);
        mainData.put("TradeType", MessageConstants.BUSINESS_TYPE_NS);
        mainData.put("SubType", "");
        mainData.put("RelevantNo", "");
        mainData.put("Reason", orderMainMap.get("Comments"));
        String orderDate = ConvertUtil.getString(orderMainMap.get("CreatedTime"));
        SimpleDateFormat sdf = new SimpleDateFormat(CherryConstants.DATE_PATTERN_24_HOURS);
        Date tradeDateTime = sdf.parse(orderDate);
        SimpleDateFormat sdf_YMD = new SimpleDateFormat(CherryConstants.DATE_PATTERN);
        SimpleDateFormat sdf_HMS = new SimpleDateFormat("HH:mm:ss");
        mainData.put("TradeDate", sdf_YMD.format(tradeDateTime));
        mainData.put("TradeTime", sdf_HMS.format(tradeDateTime));
        mainData.put("TotalAmountBefore", orderMainMap.get("BillState"));
        mainData.put("TotalAmountAfter", "");
        mainData.put("MemberCode", orderMainMap.get("MemberCode")); 
        mainData.put("Counter_ticket_code", "");
        mainData.put("Counter_ticket_code_pre", "");
        mainData.put("Ticket_type", orderMainMap.get("Ticket_type"));
        mainData.put("Sale_status", "OK");
        //消费者类型 NP：普通个人  NG：普通团购  MP：会员个人  MG：会员团购。
        if(ConvertUtil.getString(orderMainMap.get("BIN_MemberInfoID")).equals("")){
            mainData.put("Consumer_type","NP");
        }else{
            mainData.put("Consumer_type","MP");
        }
        mainData.put("Member_level", orderMainMap.get("MemberLevel"));
        mainData.put("Original_amount", orderMainMap.get("jsTotalAmount")); // 待定
        mainData.put("Discount", getFormatDiscount("1"));
        mainData.put("Pay_amount", jsTotalAmount);
        mainData.put("Decrease_amount", "0"); 
        mainData.put("Costpoint", "0");
//        mainData.put("Costpoint", orderMainMap.get("PointFee"));   // 这里的积分是天猫积分，到底能不能用，需要确定 
        mainData.put("Costpoint_amount", "0");
        mainData.put("Sale_ticket_time", sdf.format(tradeDateTime));
        String orderWay = ConvertUtil.getString(orderMainMap.get("orderWay"));
        String shopName = ConvertUtil.getString(orderMainMap.get("shopName"));
        mainData.put("Data_source", binOLCM60_BL.getDataSourceByName(orderWay,shopName));
        mainData.put("MachineCode", "");
        mainData.put("SaleSRtype", "3");//销售
        mainData.put("BAcode", orderMainMap.get("EmployeeCode"));
        mainData.put("DepartCodeDX", orderMainMap.get("CounterCode"));
        mainData.put("EmployeeCodeDX", orderMainMap.get("EmployeeCode"));
        resultMap.put("MainData", mainData);
        
        List<Map<String,Object>> saleDetail = new ArrayList<Map<String,Object>>();
        for(int i=0;i<orderDetailList.size();i++){
            Map<String,Object> orderDetail = orderDetailList.get(i);
            Map<String,Object> saleDTO = new HashMap<String,Object>();
            saleDTO.put("TradeNoIF", orderMainMap.get("OrderNumber"));
            saleDTO.put("ModifyCounts", orderMainMap.get("ModifiedTimes"));
            saleDTO.put("DetailType", orderDetail.get("SaleType"));
            saleDTO.put("BAcode", orderMainMap.get("EmployeeCode"));
            saleDTO.put("StockType", CherryConstants.STOCK_TYPE_OUT);
            // MQ消息体定义时为Barcode，此程序其他地方都作为变量时使用barCode，作为key时使用BarCode;而电商接口中使用barcode
            saleDTO.put("Barcode", orderDetail.get("BarCode"));
            saleDTO.put("Unitcode", orderDetail.get("UnitCode"));
            saleDTO.put("InventoryTypeCode", orderDetail.get("InventoryTypeCode"));
            int orderCount = CherryUtil.obj2int(orderDetail.get("ItemCount"));
//            int giftCount = CherryUtil.obj2int(orderDetail.get("giftCount"));
            saleDTO.put("Quantity", orderCount);
            saleDTO.put("QuantityBefore", "");
            saleDTO.put("Price",orderDetail.get("Price"));
            saleDTO.put("Reason", orderDetail.get("memo")); 
            saleDTO.put("Discount", orderDetail.get("discountRate"));
            saleDTO.put("MemberCodeDetail", orderMainMap.get("MemberCode"));
            saleDTO.put("ActivityMainCode",  ConvertUtil.getString(orderDetail.get("ActivityMainCode")));
            saleDTO.put("ActivityCode", ConvertUtil.getString( orderDetail.get("ActivityCode")));
            saleDTO.put("OrderID", "");
            saleDTO.put("CouponCode", "");
            saleDTO.put("IsStock", "");
            saleDTO.put("InformType", "");
            saleDTO.put("UniqueCode", "");
            saleDetail.add(saleDTO);
        }
        resultMap.put("SaleDetail", saleDetail);
        
        List<Map<String,Object>> payDetail = new ArrayList<Map<String,Object>>();
        
        /* 已在上面添加过
		Map<String, Object> payInfo = new HashMap<String, Object>();
		
		// 付款类型是否需要转换，待确定
		payInfo.put("PayTypeCode", "EX"); // 天猫积分
		// 应付金额
        //积分抵扣----使用淘金币（天猫积分)
        String pointFee = ConvertUtil.getString(orderMainMap.get("PointFee"));
        //积分抵扣的金额
        DecimalFormat df = new DecimalFormat("#0.00");
    	String pointPrice = df.format(new BigDecimal(pointFee).multiply(new BigDecimal("0.01")));
		payInfo.put("PayAmount",pointPrice);
        payDetailList.add(payInfo);
        */
        
        for(int i=0;i<payDetailList.size();i++){
            Map<String,Object> detailDTO = payDetailList.get(i);
            Map<String,Object> payDTO = new HashMap<String,Object>();
            payDTO.put("TradeNoIF", orderMainMap.get("OrderNumber"));
            payDTO.put("ModifyCounts", orderMainMap.get("ModifiedTimes"));
            payDTO.put("DetailType", "Y");
            payDTO.put("PayTypeCode", detailDTO.get("PayTypeCode"));
            payDTO.put("PayTypeID", "");
            payDTO.put("PayTypeName", "");
            
            String payAmount = getFormatDiscount(ConvertUtil.getString(detailDTO.get("PayAmount"))); // 应付金额
            
            String payTypeCode = ConvertUtil.getString(detailDTO.get("PayTypeCode"));
            if("PT".equals(payTypeCode)){
                // 总金额 2016.02.02 应付总金额需要减去运费，但是当运费为0时，则不需要
                if(new BigDecimal(0).compareTo(new BigDecimal(payAmount)) != 0){
                	// 
                	payAmount = df.format(new BigDecimal(payAmount).subtract(new BigDecimal(deliveryCost))); // 销售的总金额需要去除运费
                }
            }
            
            payDTO.put("Price", payAmount);
//            payDTO.put("Price", detailDTO.get("PayAmount"));
            
            payDTO.put("Reason", detailDTO.get("Comment"));
            payDetail.add(payDTO);
        }
        resultMap.put("PayDetail", payDetail);
        
        return resultMap;
    }
    /**
     * 转换销售MQ需要的数据
     * @param dataMap
     * @return
     * @throws ParseException 
     */
    private Map<String,Object> getMQData_SC(Map<String,Object> erpOrder,List<Map<String,Object>> detailList,List<Map<String,Object>> payDetailList){
        Map<String,Object> resultMap = new HashMap<String,Object>();
        resultMap.put("BrandCode", comMap.get(CherryBatchConstants.BRAND_CODE));
        resultMap.put("TradeType", MessageConstants.MSG_CHANGESALESTATE);
        resultMap.put("TradeNoIF", erpOrder.get("OrderNumber"));
        resultMap.put("BillState", erpOrder.get("BillState"));//字段名需注意
        resultMap.put("ChangeTime", CherryUtil.getSysDateTime(CherryConstants.DATE_PATTERN_24_HOURS));
        resultMap.put("Comment", "");
        String orderWay = ConvertUtil.getString(erpOrder.get("OrderWay"));
        String shopName = ConvertUtil.getString(erpOrder.get("ShopName"));
        resultMap.put("DataSource", binOLCM60_BL.getDataSourceByName(orderWay,shopName));
        resultMap.put("ModifyCounts", erpOrder.get("ModifiedTimes"));
        resultMap.put("CounterCode", erpOrder.get("CounterCode"));
        return resultMap;
    }
    /**
     * 把可能以科学计数法表示的折扣率转成小数点
     * @param obj
     * @return
     */
    private String getFormatDiscount(Object obj){
        String discount = ConvertUtil.getString(obj);
        if(!discount.equals("")){
            discount = new DecimalFormat("0.0000").format(Double.parseDouble(discount));
        }
        return discount;
    }
	
    /**
     * 校验接口信息是否存在
     * @param dataMap
     * @param tradeCode
     * @param fieldName
     * @throws CherryBatchException
     */
    private void checkESInterfaceInfo(Map<String,Object> dataMap,String tradeCode,String fieldName) throws CherryBatchException{
        if(tradeCode.equals("GetJstTrade")){
			// GetJstTrade以Map形式存在
			if (null == dataMap || dataMap.isEmpty()) {
				BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO();
				batchExceptionDTO.setBatchName(this.getClass());
				batchExceptionDTO.setErrorCode("EOT00092");
				batchExceptionDTO.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
				batchExceptionDTO.addErrorParam("GetJstTrade");
				throw new CherryBatchException(batchExceptionDTO);
			}
			String value = ConvertUtil.getString(dataMap.get(fieldName));
			if (value.equals("")) {
				BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO();
				batchExceptionDTO.setBatchName(this.getClass());
				batchExceptionDTO.setErrorCode("EOT00093");
				batchExceptionDTO.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
				batchExceptionDTO.addErrorParam(tradeCode);
				batchExceptionDTO.addErrorParam(fieldName);
				throw new CherryBatchException(batchExceptionDTO);
			}
        }
    }
    

    /**
     * 通过明细计算主单的金额及数量等
     * @param orderDetailList
     * @param orderMainMap
     * @return
     */
    	
    private Map<String,Object> getJSTotalInfo(List<Map<String,Object>> orderDetailList,Map<String,Object> orderMainMap){
    	Map<String,Object> resultMap = new HashMap<String, Object>();
    	
    	// 快递费
    	String postFeeStr = ConvertUtil.getString(orderMainMap.get("PostFee"));
    	BigDecimal postFee = new BigDecimal(postFeeStr);
    	
    	int totalQuantity = 0;
    	
    	// 定义订单的实际支付金额，作为主单的总金额使用
    	BigDecimal jsTotalAmount = new BigDecimal(0);
    	
    	// 定义订单下所有明细的优惠金额，作为整单折扣[促销品]费用
    	BigDecimal jsZDZKAmount = new BigDecimal(0);
    	
    	if(null == orderDetailList || orderDetailList.size() == 0){
    		resultMap.put("totalQuantity", totalQuantity);
    		resultMap.put("jsTotalAmount", postFee);
    		resultMap.put("jsZDZKAmount", jsZDZKAmount);
    		return resultMap;
    	}
    	for(int i=0;i<orderDetailList.size();i++){
    		Map<String,Object> orderDetail = orderDetailList.get(i);
    		
    		// 明细未退款时，计算各项
    		String refundStatus = convertRefundStatus(ConvertUtil.getString(orderDetail.get("RefundStatus")));
    		if(refundStatus.equals("-1")){
//    		String paymentStr = ConvertUtil.getString(orderDetail.get("Price"));
//    		BigDecimal payment = new BigDecimal(paymentStr);
    			// 正品数量 
//    		if(!payment.equals(new BigDecimal(0))){
    			int quantity = CherryUtil.obj2int(orderDetail.get("ItemCount"));
    			totalQuantity += quantity;
//    		}
    			// 正品总金额 
    			String totalFeeStr = ConvertUtil.getString(orderDetail.get("TotalFee"));
    			jsTotalAmount = jsTotalAmount.add(new BigDecimal(totalFeeStr));
    			
    			// 优惠金额
    			String discountFeeStr = ConvertUtil.getString(orderDetail.get("DiscountFee"));
    			jsZDZKAmount = jsZDZKAmount.add(new BigDecimal(discountFeeStr));
    		}
    	}
    	
		resultMap.put("totalQuantity", totalQuantity); // 正品总数量（用于主单的总数上面 ）
		jsTotalAmount = jsTotalAmount.add(postFee);
		resultMap.put("jsTotalAmount", jsTotalAmount); //  总金额（用于主单的总金额上面 ）
		resultMap.put("jsZDZKAmount", jsZDZKAmount);
		
    	return resultMap;
    }
    
    /**
     * 从明细取出总数量
     * 通过ItemCount(Trade. Order.num)购买数量累加
     * @param orderDetailList
     * @return
     */
    private int getTotalQuantity(List<Map<String,Object>> orderDetailList){
        int totalQuantity = 0;
        if(null == orderDetailList || orderDetailList.size() == 0){
            return totalQuantity;
        }
        for(int i=0;i<orderDetailList.size();i++){
            Map<String,Object> tempMap = orderDetailList.get(i);
            int quantity = CherryUtil.obj2int(tempMap.get("ItemCount"));
            
            //该字段待确定
//            int giftCount = CherryUtil.obj2int(tempMap.get("giftCount"));
            totalQuantity += quantity;
        }
        return totalQuantity;
    }
    
    /**
     * 取得时间戳
     * @return
     */
    private String getTimeStamp(){
        return  ConvertUtil.getString(new Date().getTime()/1000);
    }
    
    /**
     * 订单状态： 　　　　
     * NO_PAY：未付款(1)；CUSTOMER_AUDIT：客审(2);FINANCIAL_AUDIT：财审 (2) 　　　　
     * PRINT：打印(2) ;DISTRIBUTION：配货 (2) ;WAREHOUSING：出库 (3);
     * ON_THE_WAY：途中 (3)；SETTLEMENT：结算(3) ；SUCCESS：成功 (4)；
     * CANCEL：取消 (0)；OUT_OF_STOCK：缺货(2) ；UNDEFIND：未定义状态("")；
     * 
     * 1:生成；2：付款；3：发货；4：完成；0：取消 -1:付款后退款成功: -1
     * 
     * 取出最终写入数据库的单据状态
     * @param orderStatus
     * @return
     */
    //以下订单状态转换待定
    private String convertOrderStatus(String orderStatus) {
//    		  * TRADE_NO_CREATE_PAY(没有创建支付宝交易)  (1)
//    		  * WAIT_BUYER_PAY(等待买家付款)  (1)
//    		  * SELLER_CONSIGNED_PART(卖家部分发货)  ???
//    		  * WAIT_SELLER_SEND_GOODS(等待卖家发货,即:买家已付款) (2)
//    		  * WAIT_BUYER_CONFIRM_GOODS(等待买家确认收货,即:卖家已发货) (3)
//    		  * TRADE_BUYER_SIGNED(买家已签收,货到付款专用) (4)
//    		  * TRADE_FINISHED(交易成功)   (4)
//    		  * TRADE_CLOSED(付款以后用户退款成功，交易自动关闭)   ??? 0
//    		  * TRADE_CLOSED_BY_TAOBAO(付款以前，卖家或买家主动关闭交易)  0
//    		  * PAY_PENDING(国际信用卡支付付款确认中)  ???
//    		  * WAIT_PRE_AUTH_CONFIRM(0元购合约中) ???
    	
    	String billState = "";
    	if (orderStatus.equals("TRADE_NO_CREATE_PAY") 
    			|| orderStatus.equals("WAIT_BUYER_PAY") ) {
    		billState = "1";// 生成：1
    	} else if (orderStatus.equals("WAIT_SELLER_SEND_GOODS")) {
    		billState = "2";// 付款：2
    	} else if (orderStatus.equals("WAIT_BUYER_CONFIRM_GOODS")) {
    		billState = "3";// 发货：3
    	} else if (orderStatus.equals("TRADE_BUYER_SIGNED")
    			|| orderStatus.equals("TRADE_FINISHED") ) {
    		billState = "4";// 完成：4
    	} else if (orderStatus.equals("TRADE_CLOSED_BY_TAOBAO") || orderStatus.equals("TRADE_CLOSED")) {
    		billState = "0";// 订单取消：0
    	} 
    	
//    	else if (orderStatus.equals("TRADE_CLOSED")){
//    		billState = "-1";// 付款后退款成功: -1
//    	}
    	return billState;
    }
    
    /**
     * 退款状态
     * @param refundStatus
     * @return
     * 
     * return -1: 未退款 、 0：退款中 、1：退款成功
     * 
     */
    private String convertRefundStatus(String refund){
    	
//        * WAIT_SELLER_AGREE (买家已经申请退款，等待卖家同意)  （0）
//        * WAIT_BUYER_RETURN_GOODS (卖家已经同意退款，等待买家退货) （0）
//        * WAIT_SELLER_CONFIRM_GOODS (买家已经退货，等待卖家确认收货) （0）
//        * SELLER_REFUSE_BUYER (卖家拒绝退款) （0）
//        * CLOSED (退款关闭)  （0）
//        * SUCCESS (退款成功) （1）
    	
    	String refundStatus="";
		if ("NO_REFUND".equals(refund)) {
			refundStatus = "-1";
		} else if ("SUCCESS".equals(refund)) {
			refundStatus = "1";
		} else {
			refundStatus = "0";
		}
		return refundStatus;
    }
    
    private void setInsertInfoMapKey(Map<String,Object> map){
        map.put("CreatedBy","BINBAT121_BL");
        map.put("CreatePGM","BINBAT121_BL");
        map.put("UpdatedBy","BINBAT121_BL");
        map.put("UpdatePGM","BINBAT121_BL");
        map.put("createdBy","BINBAT121_BL");
        map.put("createPGM","BINBAT121_BL");
        map.put("updatedBy","BINBAT121_BL");
        map.put("updatePGM","BINBAT121_BL");
    }
   /**
    * 新增主单据
    * @param orderMainMap
    * @return
    */
    private int addESOrderMain(Map<String,Object> orderMainMap){
        Map<String,Object> esOrderMain = setESOrderMain(orderMainMap, "insert");
        int billID = saleInfoService.insertESOrderMain(esOrderMain);
        return billID;
    }
    /**
     * 更新主单据
     * @param orderMainMap
     * @return
     */
    private int updateESOrderMain(Map<String,Object> orderMainMap){
        Map<String,Object> esOrderMain = setESOrderMain(orderMainMap, "update");
        int cnt = saleInfoService.updateESOrderMain(esOrderMain);
        return cnt;
    }
    /**
     * 设置插入/更新Sale.BIN_ESOrderMain值
     * @param erpOrder
     * @param dataType
     * @return
     */
    private Map<String,Object> setESOrderMain(Map<String,Object> orderMainMap,String dataType){
        Map<String,Object> esOrderMain = new HashMap<String,Object>();
        if(dataType.equals("update")){
            esOrderMain.put("BIN_ESOrderMainID",orderMainMap.get("BIN_ESOrderMainID"));
        }else if(dataType.equals("insert")){
            //新后台中的单据号
            String orderNo = binOLCM03_BL.getTicketNumber(ConvertUtil.getString(orderMainMap.get("BIN_OrganizationInfoID")), ConvertUtil.getString(orderMainMap.get("BIN_BrandInfoID")), "BATCH", "DS");
            esOrderMain.put("OrderNo",orderNo);
        }
        //所属组织ID
        esOrderMain.put("BIN_OrganizationInfoID",orderMainMap.get("BIN_OrganizationInfoID"));
        //所属品牌ID
        esOrderMain.put("BIN_BrandInfoID",orderMainMap.get("BIN_BrandInfoID"));
        //组织结构ID
        esOrderMain.put("BIN_OrganizationID",orderMainMap.get("BIN_OrganizationID"));
        //员工ID 对于电商平台，也会虚拟一个BA
        esOrderMain.put("BIN_EmployeeID",orderMainMap.get("BIN_EmployeeID"));
        //员工代码code
        esOrderMain.put("EmployeeCode",orderMainMap.get("EmployeeCode"));
        //下单组织结构ID
        esOrderMain.put("BIN_OrganizationIDDX", orderMainMap.get("BIN_OrganizationID"));
        //下单员工ID
        esOrderMain.put("BIN_EmployeeIDDX", orderMainMap.get("BIN_EmployeeID"));
        //下单员工代码code
        esOrderMain.put("EmployeeCodeDX", orderMainMap.get("EmployeeCode"));
        //数据来源
        String orderWay=ConvertUtil.getString(orderMainMap.get("OrderWay"));
        String shopName = ConvertUtil.getString(orderMainMap.get("ShopName"));
        esOrderMain.put("DataSource",binOLCM60_BL.getDataSourceByName(orderWay,shopName));
        //客户数据来源
        esOrderMain.put("DataSourceCustomer",null);
        //店铺名称
        esOrderMain.put("ShopName",orderMainMap.get("ShopName"));
        //订单编号，来自于电商（有可能被ERP处理过，不再是电商平台上展示给消费者的原始单据号），全局唯一
        esOrderMain.put("BillCode",orderMainMap.get("OrderNumber"));
        //业务关联单号，保留
        esOrderMain.put("RelevanceBillCode",null);
        //电商平台原始的单据号
        esOrderMain.put("OriginalBillCode",orderMainMap.get("OldOrderNumber"));
        //SALE :标准订单 IN_BUY：内买订单 SUPPLEMENT：补差订单 EXCHANGE：换货订单
        //DEALERS：代销订单 JUS：聚划算 LBP：LBP GROUP：团购订单 REISSUE：补发订单 WHOLESALE：批发订单 UNDEFIND：未定义订单
        //交易类型（销售：NS，退货：SR,积分兑换:PX）。PX在旧的版本中也是作为NS处理的，为了能够更好的进行区分，在新版本中采用独立的类型PX，处理逻辑与NS相同。
        esOrderMain.put("SaleType",CherryConstants.BUSINESS_TYPE_NS);
        //单据类型 以下三个字段待确定
        esOrderMain.put("TicketType",orderMainMap.get("Ticket_type"));
        //单据交易状态 OrderStatus
        esOrderMain.put("BillState",orderMainMap.get("BillState"));
//        // 单据类型
        esOrderMain.put("BillType", orderMainMap.get("BillType"));
        //消费者类型 NP：普通个人  NG：普通团购  MP：会员个人  MG：会员团购
        if(ConvertUtil.getString(orderMainMap.get("BIN_MemberInfoID")).equals("")){
            esOrderMain.put("ConsumerType","NP");
        }else{
            esOrderMain.put("ConsumerType","MP");
        }
        
        //新后台查到的会员ID 以下字段可能为空
        esOrderMain.put("BIN_MemberInfoID",orderMainMap.get("BIN_MemberInfoID"));
        //新后台查到的会员卡号 以下字段可能会为空
        esOrderMain.put("MemberCode",orderMainMap.get("MemberCode"));
        //新后台查到的会员姓名
        esOrderMain.put("MemberName",orderMainMap.get("MemberName"));
        //会员昵称
        esOrderMain.put("MemberNickname",orderMainMap.get("BuyerNick"));
        
        //买家姓名 = 买家昵称
        esOrderMain.put("BuyerName",orderMainMap.get("BuyerNick"));
        //买家手机号 字段已有需确定
        esOrderMain.put("BuyerMobilePhone",orderMainMap.get(""));
        //买家的其它标识
        esOrderMain.put("BuyerIdentifier",null);
        //收货人姓名
        esOrderMain.put("ConsigneeName",orderMainMap.get("ReceiverName"));
        //收货人手机
        esOrderMain.put("ConsigneeMobilePhone",orderMainMap.get("ReceiverMobile"));
        //收货人省份
        esOrderMain.put("ConsigneeProvince",null);
        //收货人城市
        esOrderMain.put("ConsigneeCity",orderMainMap.get("ReceiverCity"));
        //收货人地址
        esOrderMain.put("ConsigneeAddress",orderMainMap.get("ReceiverAddress"));
        //买家留言
        esOrderMain.put("BuyerMessage",orderMainMap.get("BuyerMessage"));
        //卖家备注
        esOrderMain.put("SellerMemo",orderMainMap.get("SellerMemo"));
        //单据创建时间
        esOrderMain.put("BillCreateTime",orderMainMap.get("CreatedTime"));
//        //单据付款时间 
        String payTime = ConvertUtil.getString(orderMainMap.get("PayTime"));
        esOrderMain.put("BillPayTime",payTime);
//        //单据关闭时间 字段待确定
//        esOrderMain.put("BillCloseTime",null);
        String billState = ConvertUtil.getString(orderMainMap.get("BillState"));
        //4或0在 成功和取消(退款)
        if(billState.equals("4") || billState.equals("0")){ 
        	String curSysDateTime = binbat121_Service.getSYSDateTime();  
//            esOrderMain.put("BillCloseTime",CherryUtil.getSysDateTime(CherryConstants.DATE_PATTERN_24_HOURS));
            esOrderMain.put("BillCloseTime",curSysDateTime);
        }
        
        
        //折前金额 
        esOrderMain.put("OriginalAmount",orderMainMap.get("jsTotalAmount"));
//        //整单折扣率 
        esOrderMain.put("Discount", getFormatDiscount("1"));
        //折后金额 字段需要使用，名字待确定 实付金额-优惠金额（DiscountFee）？
        esOrderMain.put("PayAmount",orderMainMap.get("jsTotalAmount"));
        
        //整单去零
        esOrderMain.put("DecreaseAmount","0");
        
        //积分抵扣----使用淘金币（天猫积分)
        String pointFee = ConvertUtil.getString(orderMainMap.get("PointFee"));
        //积分抵扣的金额
        DecimalFormat df = new DecimalFormat("#0.00");
    	String pointPrice = df.format(new BigDecimal(pointFee).multiply(new BigDecimal("0.01")));
        //花费积分 是否使用待确定(如果有退款可能不准)
//        esOrderMain.put("CostPoint",pointFee);
        esOrderMain.put("CostPoint",pointFee);
        //花费积分对应的抵扣金额
        esOrderMain.put("CostpointAmount",pointPrice);
        
    	//判断是否使用淘金币（天猫积分)
//        if(CherryBatchUtil.isBlankString(pointFee)){
        
        
        
        // 实收金额 = 总金额（未去除天猫积分抵扣金额）
        esOrderMain.put("Amount",orderMainMap.get("jsTotalAmount"));
//        }else{
//        	//减法
//        	BigDecimal deductPrice = new BigDecimal(pointPrice);
//        	BigDecimal payment = new BigDecimal(ConvertUtil.getString(orderMain.get("Payment")));
//        	double actuallyPay = payment.subtract(deductPrice).doubleValue();
//        	esOrderMain.put("Amount",actuallyPay);
//        	
//        }
        
        //计算明细总数量
        esOrderMain.put("Quantity",orderMainMap.get("totalQuantity"));
        //销售记录的被修改次数
        esOrderMain.put("ModifiedTimes",orderMainMap.get("ModifiedTimes"));
        //快递公司代号
        esOrderMain.put("ExpressCompanyCode",orderMainMap.get("LogisticsCompany"));
        //快递单编号
        esOrderMain.put("ExpressBillCode",orderMainMap.get("InvoiceNo"));
        //快递费用
        esOrderMain.put("ExpressCost", orderMainMap.get("PostFee"));
        //工作流ID
        esOrderMain.put("WorkFlowID",null);
        //来源方式，为天猫订单时积分名称为：“天猫积分”，为淘宝订单时积分名称则为：“淘金币”
        String deductName="";
        if("天猫订单".equals(orderWay)){
        	deductName="天猫积分";
        }else if("淘宝订单".equals(orderWay)){
        	deductName="淘金币";
        }
        //备注
        
        if(!pointFee.equals("0")){
        	esOrderMain.put("Comments","使用" + deductName + pointFee+"->抵扣" + pointPrice + "元");
        	orderMainMap.put("Comments","使用" + deductName + pointFee+"->抵扣" + pointPrice + "元");
        }
        
        // 是否预售单(状态为1，支付时间不为空)---- 做过预售的单子不一定能够设置这个状态（因为可能订单拉进来时状态已经不是状态1了）
        if(billState.equals("1") && !CherryBatchUtil.isBlankString(payTime)){
        	esOrderMain.put("PreSale","1");
        }

        
        setInsertInfoMapKey(esOrderMain);
        return esOrderMain;
    }
    
    /**
     * 传入订单中商品相关信息取得智能促销返回的新增商品
     * @return
     * @throws ParseException,Exception  
     */
    @SuppressWarnings("unchecked")
	private List<Map<String,Object>> getSmartPromotionNewProList(Map<String,Object> orderDetailParamMap,List<Map<String,Object>> orderDetailList) throws ParseException,Exception {
    	
    	// 定义查询智能促销接口的参数Map
    	Map<String,Object> paramActMap = new HashMap<String, Object>();
    	
    	paramActMap.put("brandCode", orderDetailParamMap.get("BrandCode")); // 品牌CODE
    	paramActMap.put("organizationId", orderDetailParamMap.get("BIN_OrganizationID")); // 柜台组织结构ID
    	
    	Map<String,Object> mainInfo = new HashMap<String, Object>();
        String orderDate = ConvertUtil.getString(orderDetailParamMap.get("TradeDateTime")); // 交易时间
        SimpleDateFormat sdf = new SimpleDateFormat(CherryConstants.DATE_PATTERN_24_HOURS);
        SimpleDateFormat sdf_YMD = new SimpleDateFormat("yyMMdd");
        SimpleDateFormat sdf_HMS = new SimpleDateFormat("HHmmss");
        Date tradeDateTime = sdf.parse(orderDate);
        mainInfo.put("saleDate", sdf_YMD.format(tradeDateTime));
        mainInfo.put("saleTime", sdf_HMS.format(tradeDateTime));
    	mainInfo.put("counterCode", ConvertUtil.getString(orderDetailParamMap.get("CounterCode")));
    	paramActMap.put("mainInfo", mainInfo); // 主单数据
    	
    	List<Map<String,Object>> actDetailList = new ArrayList<Map<String,Object>>();
    	for( Map<String,Object> detailItemMap : orderDetailList){
    		Map<String,Object> prtMap = getPrtInfo(detailItemMap);
    		
    		Map<String,Object> detailMap = new HashMap<String, Object>();
    		detailMap.put("barCode", ConvertUtil.getString(prtMap.get("BarCode")));
    		detailItemMap.put("barCode", ConvertUtil.getString(prtMap.get("BarCode")));
    		detailMap.put("unitCode", ConvertUtil.getString(prtMap.get("UnitCode")));
    		detailItemMap.put("unitCode", ConvertUtil.getString(prtMap.get("UnitCode")));
    		detailMap.put("price", ConvertUtil.getString(detailItemMap.get("Price")));
    		detailMap.put("oriPrice", ConvertUtil.getString(detailItemMap.get("Price")));
    		detailMap.put("proType", "N"); // 作为产品使用
    		detailMap.put("quantity", ConvertUtil.getString(detailItemMap.get("ItemCount")));
    		actDetailList.add(detailMap);
    	}
    	
    	paramActMap.put("detailList", actDetailList); // 明细数据
    	
    	
    	// 匹配活动规则，取得最优解活动的产品信息集合
    	Map<String,Object> resultActProMap = binOLCM60_BL.getActProMap(paramActMap);
    	List<Map<String,Object>> newSmartPromotionNewProList = (List<Map<String,Object>>)resultActProMap.get("newFlagOutDetailList"); // 智能促销添加的商品
//    	List<Map<String,Object>> outResultList = (List<Map<String,Object>>)resultActProMap.get("outResultList");
    	
    	// 智能促销返回已选中的活动
//    	Map<String,Object> actResult = new HashMap<String, Object>();
//    	for(Map<String,Object> outResult : outResultList){
//    		String checkflag = ConvertUtil.getString(outResult.get("checkflag"));
//    		if("1".equals(checkflag)){
//    			actResult.putAll(outResult);
//    		}
//    	}
    	
    	// 智能促销返回的新增的商品
    	List<Map<String,Object>> resultNewProList = new ArrayList<Map<String,Object>>();
    	
    	if(!CherryBatchUtil.isBlankList(newSmartPromotionNewProList)){
    		for(Map<String,Object> newSmartPromotionNewPro : newSmartPromotionNewProList){
    			Map<String,Object> detailMap2 = new HashMap<String, Object>();
    			
    			detailMap2.put("BIN_ProductVendorID", ConvertUtil.getString(newSmartPromotionNewPro.get("productid")));
    			detailMap2.put("BarCode", ConvertUtil.getString(newSmartPromotionNewPro.get("barcode")));
    			detailMap2.put("UnitCode", ConvertUtil.getString(newSmartPromotionNewPro.get("unitcode"))); // 商品编码，后面作为 unitcode查询使用
    			
    			// 退货则置为0
    			String billState = ConvertUtil.getString(orderDetailParamMap.get("BillState"));
				// 获得明细数据的退款状态
//				String refundStatus = convertRefundStatus(ConvertUtil.getString(orderDetailMap.get("RefundStatus")));
//                if(refundStatus.equals("1") || billState.equals("0")){
            	if(billState.equals("0")){
	            	detailMap2.put("ItemCount", "0");
	            }else{
	            	detailMap2.put("ItemCount", ConvertUtil.getString(newSmartPromotionNewPro.get("quantity")));
	            }
    			
    			detailMap2.put("Price", ConvertUtil.getString(newSmartPromotionNewPro.get("ori_price"))); // 新后台返回的商品原价
    			detailMap2.put("Payment", ConvertUtil.getString(newSmartPromotionNewPro.get("price"))); // 新后台返回的销售价格
    			detailMap2.put("discountRate", "1.0");
    			detailMap2.put("memo", "["+ConvertUtil.getString(newSmartPromotionNewPro.get("mainname")) + "]活动，智能促销返回的商品 "); // 
    			
    			detailMap2.put("SaleType", ConvertUtil.getString(newSmartPromotionNewPro.get("type"))); // N P
    			
    			detailMap2.put("ActivityMainCode", ConvertUtil.getString(newSmartPromotionNewPro.get("activitycode"))); //促销活动主码 如果参与了活动要和POS系统对接，需要填写POS系统中的活动代码
    			detailMap2.put("ActivityCode", ConvertUtil.getString(newSmartPromotionNewPro.get("maincode"))); //活动代码
    			
    			detailMap2.put("isSmartPrmFlag", "1"); // 表示当前明细智能促销返回的新增商品
    			
    			resultNewProList.add(detailMap2);
    		}
    	}
    	
    	return resultNewProList;
    }
    
    /**
     * 将销售明细中的产品类型为BOM的商品进行拆分，将拆分后的商品加入到销售MQ明细集合中（注：电商信息表只记录原始信息不做拆分）
     * 未完全写完或测试，暂不能调用2015.11.13
     * @param orderDetailList
     * @return
     */    
    @Deprecated 
    private List<Map<String,Object>> splitBomPrt(Map<String,Object> orderMainMap, List<Map<String,Object>> orderDetailList){
    	// 定义返回的新的明细集合
    	List<Map<String,Object>> newDetailList = new ArrayList<Map<String,Object>>();
    	
    	Map<String,Object> sMap = new HashMap<String, Object>();
    	sMap.put("BIN_OrganizationInfoID",orderMainMap.get("BIN_OrganizationInfoID"));
    	sMap.put("BIN_BrandInfoID",orderMainMap.get("BIN_BrandInfoID"));
    	
    	for(int i=0; i< orderDetailList.size(); i++ ){
    		
    		Map<String,Object> detailItem = orderDetailList.get(i);
    		
    		// 产品必须在新后台存在 且 产品不是智能促销送的，才能进行套装产品拆分。
    		String BIN_ProductVendorID = ConvertUtil.getString(detailItem.get("BIN_ProductVendorID"));
    		String isSmartPrmFlag = ConvertUtil.getString(detailItem.get("isSmartPrmFlag")); // 有值：是智能促销返回的 、无值：不是智能促销返回的
    		if(!CherryBatchUtil.isBlankString(BIN_ProductVendorID) && CherryBatchUtil.isBlankString(isSmartPrmFlag)){
    			
    			String barCode = ConvertUtil.getString(detailItem.get("BarCode"));
    			String unitCode = ConvertUtil.getString(detailItem.get("UnitCode"));
    			sMap.put("BarCode", barCode);
    			sMap.put("UnitCode", unitCode);
    			List<Map<String, Object>> prtBomList = binOLCM60_BL.getBomPrtList(sMap);
    			if(CherryBatchUtil.isBlankList(prtBomList)){
    				// 非BOM 直接添加到新的明细
    				newDetailList.add(detailItem);
    			} 
    			else{
    				// 产品BOM集合不为空，此时需要进行拆分 
    				
    				// 求和BOM的产品数量（用于计算拆分时，将接口的套装产品金额按比例放到拆分后的商品明细中）
    				int bomTatolQuantity = 0; //ConvertUtil.getInt(comMap.get("Quantity"));
    				for(Map<String, Object> prtBom : prtBomList){
    					int quantity = ConvertUtil.getInt(prtBom.get("Quantity"));
    					bomTatolQuantity += quantity;
    				}
    				
    				// BOM商品的数量
    				int orderCount = CherryUtil.obj2int(detailItem.get("ItemCount")); 
    				
    				// 除最后一个明细商品之外的所有bom明细商品之和
    				BigDecimal preAgioPrice = new BigDecimal(0);
    				BigDecimal preAmount = new BigDecimal(0);
    				BigDecimal preDiscountFee = new BigDecimal(0);
    				
    				for(int j = 0; j < prtBomList.size(); j++){
    					
    					Map<String, Object> prtBom = prtBomList.get(j);
    					
    					// 定义拆分后新后明细行
    					Map<String, Object> newDetailItem = new HashMap<String, Object>();
    					newDetailItem.putAll(detailItem);
    					newDetailItem.put("BIN_OrganizationInfoID",orderMainMap.get("BIN_OrganizationInfoID"));
    					newDetailItem.put("BIN_BrandInfoID",orderMainMap.get("BIN_BrandInfoID"));
    					
    					// 产品核心属性
    					newDetailItem.put("BIN_ProductVendorID", prtBom.get("BIN_ProductVendorID"));
    					newDetailItem.put("UnitCode", prtBom.get("UnitCode"));
    					newDetailItem.put("barcode", prtBom.get("BarCode"));
    					
    					// 拆分后的商品数量
    					int bomQuantity = ConvertUtil.getInt(prtBom.get("Quantity"));
    					DecimalFormat df = new DecimalFormat("#0.00");
//    					bomQuantity = df.format(new BigDecimal(orderCount).multiply(new BigDecimal(bomQuantity)));
    					String billState = ConvertUtil.getString(orderMainMap.get("BillState"));
                        String isRefund = ConvertUtil.getString(detailItem.get("isRefund"));
                        if(isRefund.equals("1") || billState.equals("0")){
    		            	newDetailItem.put("ItemCount", "0");
    		            }else{
//    		            	newDetailItem.put("orderCount",  df.format(new BigDecimal(orderCount).multiply(new BigDecimal(bomQuantity))));
    		            	newDetailItem.put("ItemCount",  orderCount * bomQuantity);
    		            }
    		            
    		            // bom明细商品原价 (取BOM的产品价格)
    		            newDetailItem.put("price", prtBom.get("Price"));
    		            
    		            // 实付金额
    		            String totalFee = ConvertUtil.getString(detailItem.get("TotalFee"));
    		            
    		            // 商品折后价
    		            String price = ConvertUtil.getString(detailItem.get("Price"));

    		            // 明细让利金额
    		            String discountFee = ConvertUtil.getString(detailItem.get("DiscountFee"));
    		            
    		            if(j != 0 && (j == (prtBomList.size() - 1))){
    		            	// 最后一个BOM明细商品
    		            	
    		            	// bom明细商品--折后价计算
    		            	if(new BigDecimal(price).compareTo(new BigDecimal(0)) != 0 && bomTatolQuantity != 0 ){
//    		            		String bomPrice = df.format(new BigDecimal(price).divide(new BigDecimal(bomTatolQuantity),2));
//    		            		String bomPrice = df.format(new BigDecimal(price).subtract(preAgioPrice));
    		            		BigDecimal lastTotalAgioPrice = new BigDecimal(price).subtract(preAgioPrice);
    		            		String bomPrice = df.format(lastTotalAgioPrice.divide(new BigDecimal(bomQuantity),2));
    		            		
    		            		newDetailItem.put("Price", bomPrice);
//    		            		newDetailItem.put("PricePay", bomPrice);
    		            		
    		            	}else{
    		            		newDetailItem.put("Price", "0");
//    		            		newDetailItem.put("PricePay", "0");
    		            	}
    		            	
    		            	// bom明细商品--实付金额
    		            	if(new BigDecimal(totalFee).compareTo(new BigDecimal(0)) != 0 && bomQuantity != 0 && bomTatolQuantity != 0 ){
    		            		String bomTotalFee = df.format(new BigDecimal(totalFee).subtract(preAmount));
    		            		newDetailItem.put("TotalFee", bomTotalFee);
    		            	}else{
    		            		newDetailItem.put("TotalFee", "0");
    		            	}
    		            	
    		            	// bom明细商品--让利金额
    		            	if(new BigDecimal(discountFee).compareTo(new BigDecimal(0)) != 0 && bomQuantity != 0 && bomTatolQuantity != 0 ){
    		            		String bomDiscountFee = df.format(new BigDecimal(discountFee).subtract(preDiscountFee));
    		            		newDetailItem.put("discountFee", bomDiscountFee);
    		            	}else{
    		            		newDetailItem.put("discountFee", "0");
    		            	}
    		            	
    		            }else{
    		            	
    		            	// bom明细商品--折后价计算
    		            	if(new BigDecimal(price).compareTo(new BigDecimal(0)) != 0 && bomTatolQuantity != 0 ){
    		            		String bomAgioPrice = df.format(new BigDecimal(price).divide(new BigDecimal(bomTatolQuantity),2));
    		            		newDetailItem.put("agioPrice", bomAgioPrice);
    		            		newDetailItem.put("PricePay", bomAgioPrice);
//    		            		preAgioPrice = preAgioPrice.add(new BigDecimal(bomAgioPrice));
    		            		BigDecimal bomTotalAgioPrice = new BigDecimal(bomAgioPrice).multiply(new BigDecimal(bomQuantity));
    		            		preAgioPrice = preAgioPrice.add(bomTotalAgioPrice);
    		            		
    		            	}else{
    		            		newDetailItem.put("agioPrice", "0");
    		            	}
    		            	
    		            	// bom明细商品--实付金额
    		            	if(new BigDecimal(totalFee).compareTo(new BigDecimal(0)) != 0 && bomQuantity != 0 && bomTatolQuantity != 0 ){
    		            		String bomTotalFee = df.format(new BigDecimal(totalFee).multiply(new BigDecimal(bomQuantity).divide(new BigDecimal(bomTatolQuantity),2)));
    		            		newDetailItem.put("TotalFee", bomTotalFee);
    		            		preAmount = preAmount.add(new BigDecimal(bomTotalFee));
    		            	}else{
    		            		newDetailItem.put("TotalFee", "0");
    		            	}
    		            	
    		            	// bom明细商品--让利金额
    		            	if(new BigDecimal(discountFee).compareTo(new BigDecimal(0)) != 0 && bomQuantity != 0 && bomTatolQuantity != 0 ){
    		            		String bomDiscountFee = df.format(new BigDecimal(discountFee).multiply(new BigDecimal(bomQuantity).divide(new BigDecimal(bomTatolQuantity),2)));
    		            		newDetailItem.put("discountFee", bomDiscountFee);
    		            		preDiscountFee = preDiscountFee.add(new BigDecimal(bomDiscountFee));
    		            	}else{
    		            		newDetailItem.put("discountFee", "0");
    		            	}
    		            	
    		            }
    		            
    		            // bom明细商品折扣率
    		            newDetailItem.put("discountRate", "1.0");
    		            
    		            // bom明细商品--备注
    		            newDetailItem.put("memo", "此销售明细来自产品套装拆分,原套装产品信息为：" + "厂商ID["+BIN_ProductVendorID + "]、厂商编码[" + unitCode + "]、产品条码[" + barCode +"]。"   );
    		            
    		            newDetailItem.put("SaleType", prtBom.get("SaleType"));
    		            newDetailItem.put("InventoryTypeCode", detailItem.get("InventoryTypeCode"));
    		            
    		            newDetailList.add(newDetailItem);
    					
    				}
    			}
    			
    			sMap.remove("BarCode");
    			sMap.remove("UnitCode");
    		} else{
    			newDetailList.add(detailItem);
    		}
    	}
    	
    	
    	return newDetailList;
    }
    
    
    /**
     * 插入电商订单明细
     * @param orderDetailParamMap
     * @param orderDetailList
     * @return result 返回false说明有产品在新后台没有找到
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
	private boolean addESOrderDetail(Map<String,Object> orderDetailParamMap,List<Map<String,Object>> orderDetailList) throws Exception{
        boolean ishasNonGoods = true;
    	if(CherryBatchUtil.isBlankList(orderDetailList)){
            return false;
        }
//        String organizationInfoID = ConvertUtil.getString(orderDetailParamMap.get("BIN_OrganizationInfoID"));
//        String brandInfoID = ConvertUtil.getString(orderDetailParamMap.get("BIN_BrandInfoID"));
        String organizationInfoID = ConvertUtil.getString(comMap.get("organizationInfoId"));
        String brandInfoID = ConvertUtil.getString(comMap.get("brandInfoId"));
        
        // 通过原始订单明细查询智能促销中是否有新增的商品数据--通过系统配置项确定是否调用该方法(电商订单匹配智能促销活动)
        boolean isSmartPromAddPro = binOLCM14_BL.isConfigOpen("1333", organizationInfoID, brandInfoID);
        if(isSmartPromAddPro){
        	List<Map<String,Object>> smartPromotionNewProList = getSmartPromotionNewProList(orderDetailParamMap, orderDetailList);
        	if(!CherryBatchUtil.isBlankList(smartPromotionNewProList)){
        		orderDetailList.addAll(smartPromotionNewProList);
        	}
        }
        
        //查询逻辑仓库 以下字段待定
        Map<String,Object> logicparamMap = new HashMap<String,Object>();
        logicparamMap.put("BIN_BrandInfoID", brandInfoID);
        logicparamMap.put("Type", "1");//终端逻辑仓库
        List<Map<String,Object>> logicDepotList = binOLCM18_BL.getLogicDepotList(logicparamMap);
        String defaultInventoryTypeCode = ConvertUtil.getString(logicDepotList.get(0).get("LogicInventoryCode"));
        DecimalFormat df = new DecimalFormat("#0.00");
        
    	// 交易状态
        String billState = ConvertUtil.getString(orderDetailParamMap.get("BillState"));
        
        //快递费用
        /*
        String deliverCost = ConvertUtil.getString(orderDetailParamMap.get("PostFee"));
        if(!CherryBatchUtil.isBlankString(deliverCost) && new BigDecimal(0).compareTo(new BigDecimal(deliverCost)) != 0){
            // 快递费作为一个不扣库存的虚拟促销品加入明细
            Map<String, Object> expressDetail = setPromotionProduct(organizationInfoID, brandInfoID, billState, deliverCost);
            orderDetailList.add(expressDetail);
        }
         */
        
        // 整单折扣--整个订单的优惠金额(作为整单折扣[促销品])
        String jsZDZKAmount = ConvertUtil.getString(orderDetailParamMap.get("jsZDZKAmount"));
        if(new BigDecimal(0).compareTo(new BigDecimal(jsZDZKAmount)) != 0){
        	String tradeDateTime = ConvertUtil.getString(orderDetailParamMap.get("TradeDateTime"));
        	Map<String, Object> zkzkDetail = setZDZKProduct(organizationInfoID, brandInfoID, tradeDateTime,jsZDZKAmount ,billState);
        	orderDetailList.add(zkzkDetail);
        }
        
        List<Map<String,Object>> esOrderDetailList = new ArrayList<Map<String,Object>>();
        for(int i=0;i<orderDetailList.size();i++){
        	
        	Map<String,Object> orderDetail = orderDetailList.get(i);
            
            orderDetail.put("BIN_OrganizationInfoID", organizationInfoID);
            orderDetail.put("BIN_BrandInfoID", brandInfoID);
            //字段待定
            orderDetail.put("TradeDateTime", orderDetailParamMap.get("TradeDateTime"));
            orderDetail.put("BIN_OrganizationID", orderDetailParamMap.get("BIN_OrganizationID"));
            
            Map<String,Object> prtMap = getPrtInfo(orderDetail);
            String productVendorID = ConvertUtil.getString(prtMap.get("BIN_ProductVendorID"));
            if(CherryBatchUtil.isBlankString(productVendorID)){
            	ishasNonGoods = false;
            }
            
            
            //销售类型 正常销售为N，促销为P。 字段待定
            orderDetail.put("SaleType", prtMap.get("SaleType"));
            orderDetail.put("InventoryTypeCode", defaultInventoryTypeCode);
            Map<String,Object> esOrderDetailMap = new HashMap<String,Object>();
            //电商订单主表ID
            esOrderDetailMap.put("BIN_ESOrderMainID",orderDetailParamMap.get("BIN_ESOrderMainID"));
            
            //当前记录的序号
            esOrderDetailMap.put("DetailNo",i+1);
            
			esOrderDetailMap.put("UnitCode", prtMap.get("UnitCode"));
			orderDetail.put("UnitCode", prtMap.get("UnitCode"));
			esOrderDetailMap.put("BarCode", prtMap.get("BarCode"));
			orderDetail.put("BarCode", prtMap.get("BarCode"));
			String productVendorId = ConvertUtil.getString(prtMap.get("BIN_ProductVendorID"));
			esOrderDetailMap.put("BIN_ProductVendorID", productVendorId);
			
            //购买数量
            int orderCount = CherryUtil.obj2int(orderDetail.get("ItemCount"));
            //字段待定
//            int giftCount = CherryUtil.obj2int(orderDetail.get("giftCount"));
            esOrderDetailMap.put("Quantity",orderCount);
            //原始价格
            String price = ConvertUtil.getString(orderDetail.get("Price"));
            esOrderDetailMap.put("Price",price);
            
            String totalFee = ConvertUtil.getString(orderDetail.get("TotalFee")); // 应付金额（商品价格 * 商品数量 + 手工调整金额 - 子订单级订单优惠金额）
            
            /* 
            //折后单价 待定
            if((new BigDecimal(0).compareTo(new BigDecimal(totalFee)) != 0) && orderCount != 0){
            	String pricePay = df.format(new BigDecimal(totalFee).divide(new BigDecimal(orderCount)));
            	esOrderDetailMap.put("PricePay",pricePay);
            }else{
            	esOrderDetailMap.put("PricePay",totalFee);
            }
            */
            
            // 电商订单表的折后单价还是按照原价使用
            esOrderDetailMap.put("PricePay",price);
            
            //应付金额 定价*数量
            String payableAmount = df.format(new BigDecimal(price).multiply(new BigDecimal(orderCount)));
            esOrderDetailMap.put("PayableAmount",payableAmount);
            
            //实付金额 实付价格*数量
//            String actualAmount = df.format(new BigDecimal(pricePay).multiply(new BigDecimal(orderCount)));
            esOrderDetailMap.put("ActualAmount",totalFee);
            
            //折扣率 待确定
            esOrderDetailMap.put("Discount",getFormatDiscount("1"));
            orderDetail.put("discountRate",getFormatDiscount("1"));
            
            //让利金额 
            String discountFee = ConvertUtil.getString(orderDetail.get("DiscountFee")); // 优惠金额（Trade. Order. discount_fee）
            esOrderDetailMap.put("AgioAmount",discountFee);
            //分摊后金额
            esOrderDetailMap.put("AmountPortion","");
            //逻辑仓库代码
            esOrderDetailMap.put("InventoryTypeCode",defaultInventoryTypeCode);
            //批号或其它
            esOrderDetailMap.put("BatchCode","");
            //唯一码
            esOrderDetailMap.put("UniqueCode","");
            //销售类型 正常销售为N，促销为P。
            esOrderDetailMap.put("SaleType",prtMap.get("SaleType"));
            //促销活动主码 如果参与了活动要和POS系统对接，需要填写POS系统中的活动代码
//            esOrderDetailMap.put("ActivityMainCode","");
            String activityMainCode = ConvertUtil.getString(orderDetail.get("ActivityMainCode"));
            esOrderDetailMap.put("ActivityMainCode","".equals(activityMainCode) ? "" : activityMainCode);
            //活动代码
//            esOrderDetailMap.put("ActivityCode","");
            String activityCode = ConvertUtil.getString(orderDetail.get("ActivityCode"));
            esOrderDetailMap.put("ActivityCode","".equals(activityCode) ? "" : activityCode);
            //备注
            esOrderDetailMap.put("Comment",orderDetail.get("memo"));
            
            // 产品是否存在于新后台通过相关条件查询订单明细中的产品是否在新后台存在 ( 不存在：0 、存在：1)
            if("".equals(productVendorId)){
            	esOrderDetailMap.put("IsExistsPos","0");
            }else{
            	esOrderDetailMap.put("IsExistsPos","1");
            }
            
            //批号或其他  暂存NumIID（商品数据ID（电商系统定义）（Trade. Order.num_iid））
            esOrderDetailMap.put("BatchCode",orderDetail.get("NumIID"));
            //唯一码  暂存OuterIID（商家自定义的商品Item的id（Trade. Order.outer_iid），这个字段与新后台的产品表ItemCode对应）
            esOrderDetailMap.put("UniqueCode",orderDetail.get("OuterIID"));
            
            setInsertInfoMapKey(esOrderDetailMap);
            
            if(!ConvertUtil.getString(orderDetail.get("isSmartPrmFlag")).equals("")){
            	// 智能促销返回的商品明细不写入电商明细表
            }else{
            	// 非智能促销返回的商品明细写入电商明细表
            	esOrderDetailList.add(esOrderDetailMap);
            }
//            esOrderDetailList.add(esOrderDetailMap);
        }
        
        saleInfoService.insertESOrderDetail(esOrderDetailList);
        return ishasNonGoods;
    }
    
    /**
     * 取得产品的barcode unitocde bin_productvendorId
     * @param paramMap
     * @return
     * @throws CherryMQException
     */
    private Map<String,Object> getPrtInfo(Map<String,Object> paramMap) throws CherryMQException{
        Map<String,Object> resultMap = new HashMap<String,Object>();
        
        String saleType = ConvertUtil.getString(paramMap.get("SaleType")); 
        saleType = "".equals(saleType) ? "N" : saleType;
//        resultMap.put("SaleType", "N");
        resultMap.put("SaleType", saleType); // 从智能促销返回的新增商品是有saleType的
       
        Map<String,Object> param = new HashMap<String,Object>();
        param.put("BIN_OrganizationInfoID", paramMap.get("BIN_OrganizationInfoID"));
        param.put("BIN_BrandInfoID", paramMap.get("BIN_BrandInfoID"));
        List<Map<String,Object>> productList = null;
        
        // 电商订单匹配线下商品规则(1:使用barcode匹配、2:使用unitcode匹配)
        String organizationInfoID = ConvertUtil.getString(paramMap.get("BIN_OrganizationInfoID"));
        String brandInfoID = ConvertUtil.getString(paramMap.get("BIN_BrandInfoID"));
        String esPrtRuleConf = binOLCM14_BL.getConfigValue("1332", organizationInfoID, brandInfoID);
        
        if("N".equals(saleType)){
        	if("1".equals(esPrtRuleConf)){
        		// 防止barCode为空时，SQL查询不带此参数而查出所有产品数据
        		param.put("BarCode", paramMap.get("OuterIID"));
        		productList = binOTHONG01_Service.getProductInfo(param);
        	} else if ("2".equals(esPrtRuleConf)){
        		// productNumber 商品编码
        		param.put("UnitCode", paramMap.get("OuterIID"));
        		productList = binOTHONG01_Service.getProductInfo(param);
        	} else if ("3".equals(esPrtRuleConf)){
        		// ItemCode 产品唯一性编码
        		param.put("ItemCode", paramMap.get("OuterIID"));
        		productList = binbat121_Service.getProductInfo(param);
        	} 
        }
        
        String productVendorID = "";
        String unitCode = "";
        String barCode = "";
        if(null != productList && productList.size()>0){
            productVendorID = ConvertUtil.getString(productList.get(0).get("BIN_ProductVendorID"));
            unitCode = ConvertUtil.getString(productList.get(0).get("UnitCode"));
            barCode = ConvertUtil.getString(productList.get(0).get("BarCode"));
        }else{
        	param.put("BarCode", paramMap.get("BarCode"));
            productList = binOTHONG01_Service.getPrmProductInfo(param);
            if(null != productList && productList.size()>0){
                resultMap.put("SaleType", "P");
                productVendorID = ConvertUtil.getString(productList.get(0).get("BIN_PromotionProductVendorID"));
                unitCode = ConvertUtil.getString(productList.get(0).get("UnitCode"));
                barCode = ConvertUtil.getString(productList.get(0).get("BarCode"));
            }
        }
        if(productVendorID.equals("")){
            resultMap.put("BIN_ProductVendorID", null);
            resultMap.put("UnitCode", null);
            resultMap.put("BarCode", null);
        }else{
            resultMap.put("BIN_ProductVendorID", productVendorID);
            resultMap.put("UnitCode", unitCode);
            resultMap.put("BarCode", barCode);
        }
        return resultMap;
    }

    /**
     * 设置虚拟快递促销品
     * @param organizationInfoID
     * @param brandInfoID
     * @param billState
     * @param deliverCost
     * @return
     * @throws CherryMQException
     * @throws CherryBatchException
     * @throws Exception
     */
    private Map<String, Object> setZDZKProduct(String organizationInfoID,
    		String brandInfoID ,String tradeDateTime ,String jsZDZKAmount,String billState)
    				throws CherryMQException, CherryBatchException,Exception {
    	
        //查询条码为ZDZK的虚拟促销品整单折扣，不存在报错
        Map<String,Object> param = new HashMap<String,Object>();
        param.put("BIN_OrganizationInfoID", organizationInfoID);
        param.put("BIN_BrandInfoID", brandInfoID);
        param.put("BarCode", "ZDZK");
        param.put("UnitCode", "ZDZK");
        param.put("TradeDateTime", tradeDateTime);
        Map<String,Object> prmInfo = binBEMQMES97_BL.getPrmInfo(null, param, false);
        if(null == prmInfo || ConvertUtil.getString(prmInfo.get("BIN_PromotionProductVendorID")).equals("")){
            BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO();
            batchExceptionDTO.setBatchName(this.getClass());
            batchExceptionDTO.setErrorCode("EOT00048");
            batchExceptionDTO.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
            throw new CherryBatchException(batchExceptionDTO);
        }
//        String zdzkID = ConvertUtil.getString(prmInfo.get("BIN_PromotionProductVendorID"));
    	
    	
    	Map<String, Object> expressDetail = new HashMap<String, Object>();
    	//以下字段暂确定，之后通过unitCode查询产品厂商Id
    	expressDetail.put("BIN_ProductVendorID", prmInfo.get("BIN_PromotionProductVendorID"));
    	expressDetail.put("BarCode", "ZDZK");
    	expressDetail.put("UnitCode", "ZDZK");
    	expressDetail.put("ItemCount", "1");
    	if(billState.equals("0")){
    		expressDetail.put("ItemCount", "0");
    	}
        BigDecimal zdzkBD = new BigDecimal(jsZDZKAmount);
        zdzkBD = zdzkBD.setScale(2, BigDecimal.ROUND_HALF_UP);
    	expressDetail.put("Price", zdzkBD.multiply(new BigDecimal(-1)));
    	expressDetail.put("Payment", zdzkBD.multiply(new BigDecimal(-1)));
    	expressDetail.put("TotalFee", zdzkBD.multiply(new BigDecimal(-1)));
    	expressDetail.put("DiscountFee", "0");
    	
    	expressDetail.put("discountRate", "1.0");
    	expressDetail.put("SaleType", "P");
    	expressDetail.put("memo", "");
    	
    	return expressDetail;
    }
    
    
    /**
     * 设置虚拟快递促销品
     * @param organizationInfoID
     * @param brandInfoID
     * @param billState
     * @param deliverCost
     * @return
     * @throws CherryMQException
     * @throws CherryBatchException
     * @throws Exception
     */
	private Map<String, Object> setPromotionProduct(String organizationInfoID,
			String brandInfoID, String billState, String deliverCost)
			throws CherryMQException, CherryBatchException,Exception {
		//查询条码为KDCOST的虚拟套装产品，不存在报错
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("BIN_OrganizationInfoID", organizationInfoID);
		param.put("BIN_BrandInfoID", brandInfoID);
		param.put("BarCode", "KDCOST");
		param.put("UnitCode", "KDCOST");
		Map<String,Object> prmInfo = binBEMQMES97_BL.getPrmInfo(null, param, false);
		if(null == prmInfo || ConvertUtil.getString(prmInfo.get("BIN_PromotionProductVendorID")).equals("")){
		    BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO();
		    batchExceptionDTO.setBatchName(this.getClass());
		    batchExceptionDTO.setErrorCode("EOT00101");
		    batchExceptionDTO.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
		    throw new CherryBatchException(batchExceptionDTO);
		}
		
		Map<String, Object> expressDetail = new HashMap<String, Object>();
		//以下字段暂确定，之后通过unitCode查询产品厂商Id
		expressDetail.put("BIN_ProductVendorID", prmInfo.get("BIN_PromotionProductVendorID"));
		expressDetail.put("BarCode", "KDCOST");
		expressDetail.put("ItemCount", "1");
		if(billState.equals("0")){
		    expressDetail.put("ItemCount", "0");
		}
		expressDetail.put("Price", deliverCost);
		expressDetail.put("Payment", deliverCost);
		expressDetail.put("TotalFee", deliverCost);
		expressDetail.put("DiscountFee", "0");
		
//                expressDetail.put("amount", "0"); 
		expressDetail.put("discountRate", "1.0");
		expressDetail.put("SaleType", "P");
//		expressDetail.put("memo", "快递费虚拟促销品作为虚拟出来的促销品存在于销售明细中");
		expressDetail.put("memo", "");
		
		return expressDetail;
	}
	
    /**
     * 插入支付方式表
     * @param orderDetailParamMap
     * @param payDetailList
     * @return
     */
    private Map<String,Object> insertESOrderPayList(Map<String,Object> orderMainMap, Map<String,Object> orderDetailParamMap, List<Map<String,Object>> payDetailList){
        List<Map<String,Object>> esOrderPayList = new ArrayList<Map<String,Object>>();
        
		// 处理付款方式明细
		Map<String, Object> payInfo = new HashMap<String, Object>();
		
		// 付款类型是否需要转换，待确定
		payInfo.put("PayTypeCode", "EX"); // 积分
		// 应付金额
        //积分抵扣----使用淘金币（天猫积分)
        String pointFee = ConvertUtil.getString(orderMainMap.get("PointFee"));
        //积分抵扣的金额
        DecimalFormat df = new DecimalFormat("#0.00");
    	String pointPrice = df.format(new BigDecimal(pointFee).multiply(new BigDecimal("0.01")));
		payInfo.put("PayAmount",pointPrice);
		
        // 备注填写使用使用哪种积分
        String deductName="";
        String orderWay = ConvertUtil.getString(orderMainMap.get("OrderWay"));
        if("天猫订单".equals(orderWay)){
        	deductName="天猫积分";
        }else if("淘宝订单".equals(orderWay)){
        	deductName="淘金币";
        }
		payInfo.put("Comment", deductName);
        payDetailList.add(payInfo);
        
        for(int i=0;i<payDetailList.size();i++){
            Map<String,Object> payDetailDTO = payDetailList.get(i);
            payDetailDTO.put("BIN_ESOrderMainID",orderDetailParamMap.get("BIN_ESOrderMainID"));
            payDetailDTO.put("DetailNo",i+1);
            payDetailDTO.put("SerialNumber","");
            payDetailDTO.put("Comment",payDetailDTO.get("Comment"));
            setInsertInfoMapKey(payDetailDTO);
            esOrderPayList.add(payDetailDTO);
        }
        saleInfoService.insertESOrderPayList(esOrderPayList);
        return null;
    }
	/**
	 * 程序结束时，处理Job共通( 插入Job运行履历表)
	 * @param paraMap
	 * @throws Exception
	 */
	private void programEnd(Map<String,Object> paraMap) throws Exception{
		 
		// 程序结束时，插入Job运行履历表
		paraMap.put("flag", flag);
		paraMap.put("TargetDataCNT", totalCount);
		paraMap.put("SCNT", totalCount - failCount);
		paraMap.put("FCNT", failCount);
		paraMap.put("FReason", fReason);
		binbecm01_IF.insertJobRunHistory(paraMap);
	}
	
	/**
	 * 程序初始化参数
	 * @param map
	 * @throws Exception 
	 * @throws CherryBatchException 
	 */
	private void init(Map<String, Object> map) throws CherryBatchException, Exception {
		// 设置共通参数
		setComMap(map);
		
		// BatchCD 来自VSS$/01.Cherry/02.设计文档/01.概要设计/00.各种一览/【新设】CherryBatch一览.xlsx
		map.put("JobCode", "BAT121");
		
		// 程序【开始运行时间】
		String runStartTime = binbecm01_IF.getSYSDateTime();
		// 作成日时
		map.put("RunStartTime", runStartTime);
		
		comMap.putAll(map);
		
	}

	/**
	 * 共通Map
	 * @param map
	 * @return
	 */
	private void setComMap(Map<String, Object> map) {
		
		// 更新程序名
		map.put(CherryBatchConstants.UPDATEPGM, "BINBAT121");
		// 作成程序名
		map.put(CherryBatchConstants.CREATEPGM, "BINBAT121");
		// 作成者
		map.put(CherryBatchConstants.CREATEDBY,CherryBatchConstants.UPDATE_NAME);
		// 更新者
		map.put(CherryBatchConstants.UPDATEDBY,CherryBatchConstants.UPDATE_NAME);
		// 所属组织
		map.put(CherryBatchConstants.ORGANIZATIONINFOID, map.get(CherryBatchConstants.ORGANIZATIONINFOID).toString());
		// 品牌ID
		map.put(CherryBatchConstants.BRANDINFOID, map.get(CherryBatchConstants.BRANDINFOID).toString());

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
//		// 插入件数
//		BatchLoggerDTO batchLoggerDTO3 = new BatchLoggerDTO();
//		batchLoggerDTO3.setCode("IIF00003");
//		batchLoggerDTO3.setLevel(CherryBatchConstants.LOGGER_INFO);
//		batchLoggerDTO3.addParam(String.valueOf(insertCount));
//		// 更新件数
//		BatchLoggerDTO batchLoggerDTO4 = new BatchLoggerDTO();
//		batchLoggerDTO4.setCode("IIF00004");
//		batchLoggerDTO4.setLevel(CherryBatchConstants.LOGGER_INFO);
//		batchLoggerDTO4.addParam(String.valueOf(updateCount));
		// 失败件数
		BatchLoggerDTO batchLoggerDTO5 = new BatchLoggerDTO();
		batchLoggerDTO5.setCode("IIF00005");
		batchLoggerDTO5.setLevel(CherryBatchConstants.LOGGER_INFO);
		batchLoggerDTO5.addParam(String.valueOf(failCount));
		// 处理总件数
		logger.BatchLogger(batchLoggerDTO1);
		// 成功总件数
		logger.BatchLogger(batchLoggerDTO2);
		// 插入件数
//		logger.BatchLogger(batchLoggerDTO3);
//		// 更新件数
//		logger.BatchLogger(batchLoggerDTO4);
		// 失败件数
		logger.BatchLogger(batchLoggerDTO5);
		
		// 失败订单号集合
		if(!CherryBatchUtil.isBlankList(faildSaleBillList)){
			BatchLoggerDTO batchLoggerDTO6 = new BatchLoggerDTO();
			batchLoggerDTO6.setCode("EKD00018");
			batchLoggerDTO6.setLevel(CherryBatchConstants.LOGGER_INFO);
			batchLoggerDTO6.addParam(faildSaleBillList.toString());
			logger.BatchLogger(batchLoggerDTO6);
			
			fReason = "WebService服务端处理数据失败，具体见log日志";
		}
	}
	

}
