/*
 * @(#)BINOTHONG02_BL.java     1.0 2015/12/04
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
package com.cherry.ot.hong.bl;

import java.io.UnsupportedEncodingException;
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
import javax.ws.rs.core.MultivaluedMap;

import org.apache.axis.encoding.Base64;

import com.cherry.cm.batcmbussiness.interfaces.BINBECM01_IF;
import com.cherry.cm.cmbussiness.bl.BINOLCM03_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM18_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM60_BL;
import com.cherry.cm.core.BatchExceptionDTO;
import com.cherry.cm.core.BatchLoggerDTO;
import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryBatchException;
import com.cherry.cm.core.CherryBatchLogger;
import com.cherry.cm.core.CherryBatchSecret;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryMD5Coder;
import com.cherry.cm.core.PropertiesUtil;
import com.cherry.cm.util.CherryBatchUtil;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.DateUtil;
import com.cherry.mq.mes.bl.BINBEMQMES97_BL;
import com.cherry.mq.mes.common.CherryMQException;
import com.cherry.mq.mes.common.MessageConstants;
import com.cherry.ot.hong.interfaces.BINOTHONG01_IF;
import com.cherry.ot.hong.interfaces.BINOTHONG02_IF;
import com.cherry.ot.hong.service.BINOTHONG01_Service;
import com.cherry.webservice.client.WebserviceClient;
import com.cherry.webservice.sale.bl.SaleInfoLogic;
import com.cherry.webservice.sale.service.SaleInfoService;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.MultivaluedMapImpl;

/**
 * 
 * 宏巍电商订单获取 BL
 * 
 * 
 * @author jijw
 * @version 1.0 2015.12.07
 */
public class BINOTHONG02_BL implements BINOTHONG02_IF{

    private static CherryBatchLogger logger = new CherryBatchLogger(BINOTHONG02_BL.class);
    
    @Resource(name="saleInfoLogic")
    private SaleInfoLogic saleInfoLogic;
    
    @Resource(name="saleInfoService")
    private SaleInfoService saleInfoService;
    
    @Resource(name="binOLCM03_BL")
    private BINOLCM03_BL binOLCM03_BL;
    
    @Resource(name="binOLCM18_BL")
    private BINOLCM18_BL binOLCM18_BL;
    
    @Resource(name="binBEMQMES97_BL")
    private BINBEMQMES97_BL binBEMQMES97_BL;
    
    @Resource(name="binOTHONG01_Service")
    private BINOTHONG01_Service binOTHONG01_Service;
    
    /** 系统配置项 共通BL */
	@Resource(name="binOLCM14_BL")
	private BINOLCM14_BL binOLCM14_BL;
	
	/** 电商接口相关共通  */
	@Resource(name="binOLCM60_BL")
	private BINOLCM60_BL binOLCM60_BL;
    
    /** BATCH处理标志 */
    private int flag = CherryBatchConstants.BATCH_SUCCESS;
    
    /** 处理总条数 */
    private int totalCount = 0;
    
    /** 失败条数 */
    private int failCount = 0;
    
	/** JOB执行相关共通 IF */
	@Resource(name="binbecm01_IF")
	private BINBECM01_IF binbecm01_IF;
	
    @Resource(name="binOTHONG01_BL")
    private BINOTHONG01_IF binOTHONG01_BL;
	
	private Map<String, Object> comMap;
	
	/** 每批次(页)处理数量 100 */
	private final int BATCH_SIZE = 50;
	
	
    @Override
    public int tran_batchOTHONG(Map<String, Object> map) throws Exception{
    	
		// 初始化 
		try{
			init(map);
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
    	
    	// 处理订单
    	executeOrder(map);
        
    	// 日志
        outMessage();
        
        // 程序结束时，处理Job共通(更新Job控制表 、插入Job运行履历表)
        programEnd(map);
        
        return flag;
    }
    
    /**
     * 处理订单
     * @param map
     * @throws Exception
     */
    public void executeOrder(Map<String, Object> map) throws Exception{

		
		// 电商第三方接口取得获取产品列表对应的数据
		map.put("ESCode", "hongwei");
		map.put("TradeCode", "GetOrderDetail"); // 订单明细
		
//        String lastAccessTime = CherryUtil.getSysDateTime(CherryConstants.DATE_PATTERN_24_HOURS);
        
        List<Map<String, Object>> esInterfaceInfoList = binOTHONG01_Service.getESInterfaceInfo(map);
        // 判断TradeCode是否存在GetOrderMain:订单主单查询 GetOrderDetail:订单明细查询，不存在抛错
        if (null == esInterfaceInfoList || esInterfaceInfoList.size() == 0) {
            BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO();
            batchExceptionDTO.setBatchName(this.getClass());
            batchExceptionDTO.setErrorCode("EOT00042");
            batchExceptionDTO.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
            throw new CherryBatchException(batchExceptionDTO);
        }  
        
        // 循环将各品牌下各店铺的订单获取
        for (int i = 0; i < esInterfaceInfoList.size(); i++) {
            
            try{
            	
            	Map<String, Object> esInterfaceConf = esInterfaceInfoList.get(i);
            	// 店铺nick
            	String accountName = ConvertUtil.getString(esInterfaceConf.get("AccountName"));
            	map.put("AccountName", accountName);
            	
            	// 处理该店铺的订单号集合
            	try{
            		map.put("dataClass",0); // 处理异常的订单
            		handleShopOrderData(esInterfaceConf,map);
            	}catch(Exception ex){
            		logger.outLog("批处理失败电商订单数据处理失败:", CherryBatchConstants.LOGGER_ERROR);
            		logger.outExceptionLog(ex);
            	}
            	
            	try{
            		// 是否开启宏巍电商订单拉取(没有对应产品的失败订单)
            		String organizationInfoID = ConvertUtil.getString(comMap.get("organizationInfoId"));
            		String brandInfoID = ConvertUtil.getString(comMap.get("brandInfoId"));
            		boolean isPullNoPrt = binOLCM14_BL.isConfigOpen("1342", organizationInfoID, brandInfoID);
            		if(isPullNoPrt){
            			map.put("dataClass",1); // 处理产品没有对应关系的订单
            			handleShopOrderData(esInterfaceConf,map);
            		}
            	}catch(Exception ex){
            		logger.outLog("批处理产品对应关系异常的电商订单数据处理失败:", CherryBatchConstants.LOGGER_ERROR);
            		logger.outExceptionLog(ex);
            	}
            	
            }catch(Exception e){
        		logger.outExceptionLog(e);
        		logger.outLog("电商订单数据处理失败:", CherryBatchConstants.LOGGER_ERROR);
            	// 异常日志
            	flag = CherryBatchConstants.BATCH_ERROR;
            }
            
        }
    }
    

    /**
	 * 处理Job失败数据履历表中，该店铺的订单号集合
	 * 
     * @param esInterfaceConf 电商配置信息
     * @param paraMap
     * @throws CherryBatchException
     * @throws Exception
     */
	@SuppressWarnings("unchecked")
	private void handleShopOrderData(Map<String, Object> esInterfaceConf, Map<String, Object> paraMap) throws CherryBatchException,Exception{
		
        // 检查ZDZK、KDCOST、员工、部门等信息
		
		String organizationInfoID = ConvertUtil.getString(comMap.get("organizationInfoId"));
		String brandInfoID = ConvertUtil.getString(comMap.get("brandInfoId"));
		
		// 员工信息处理
		String baCode = "G00001";// 员工代码（EmployeeCode）暂定
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("BIN_OrganizationInfoID",comMap.get("organizationInfoId"));
		param.put("BIN_BrandInfoID", comMap.get("brandInfoId"));
		param.put("EmployeeCode", baCode);
		// 获取员工信息
		List<Map<String, Object>> employeeList = binOTHONG01_Service.getEmployeeInfo(param);
		String employeeID = "";
		if (!CherryBatchUtil.isBlankList(employeeList)) {
			// 获得员工ID
			employeeID = ConvertUtil.getString(employeeList.get(0).get("BIN_EmployeeID"));
		}
		
        //以下代码为调用共通所使用的getMemInfo方法
		Map<String, Object> departParamMap = new HashMap<String, Object>();
		departParamMap.put("BIN_OrganizationInfoID",organizationInfoID);
		departParamMap.put("BIN_BrandInfoID",brandInfoID);
		// 查询组织结构ID
		departParamMap.put("DepartName", esInterfaceConf.get("AccountName"));
		List<Map<String, Object>> departList = binOTHONG01_Service.getDepartInfo(departParamMap);
		
		String organizationID = "";
		String counterCode = "";
		if (!CherryBatchUtil.isBlankList(departList)) {
			organizationID = ConvertUtil.getString(departList.get(0).get("BIN_OrganizationID"));
			counterCode = ConvertUtil.getString(departList.get(0).get("DepartCode"));
		}
		paraMap.put("baCode", baCode);
		paraMap.put("BIN_EmployeeID", employeeID);
		paraMap.put("BIN_OrganizationID", organizationID);
		paraMap.put("CounterCode", counterCode);
		
		
        // 目前只有雅芳品牌有产品套装的情况 ----只有支持套装的情况才去校验XNTZ9999必须存在
        boolean isSupportTZ = binOLCM14_BL.isConfigOpen("1322", organizationInfoID, brandInfoID);
        String xntzID = null;
        //查询条码为XNTZ9999的虚拟促销品快递费，不存在报错
        if(isSupportTZ) {
            param.put("BIN_OrganizationInfoID", organizationInfoID);
            param.put("BIN_BrandInfoID", brandInfoID);
            param.put("BarCode", "XNTZ9999");
            param.put("UnitCode", "XNTZ9999");
            param.put("TradeDateTime", paraMap.get("RunStartTime"));
            int productVendorID = binBEMQMES97_BL.getProductVendorID(null, param, false);
            if(productVendorID == 0){
                BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO();
                batchExceptionDTO.setBatchName(this.getClass());
                // 宏巍电商订单获取，需要增加一条编码条码为XNTZ9999的虚拟产品作为套装产品。
                batchExceptionDTO.setErrorCode("EOT00078");
                batchExceptionDTO.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
                throw new CherryBatchException(batchExceptionDTO);
            }
            xntzID = ConvertUtil.getString(productVendorID);
        }
        
        /* 2016.02.02 废除运费加入明细的操作
        //查询条码为KDCOST的虚拟套装产品，不存在报错
        param = new HashMap<String,Object>();
        param.put("BIN_OrganizationInfoID", organizationInfoID);
        param.put("BIN_BrandInfoID", brandInfoID);
        param.put("BarCode", "KDCOST");
        param.put("UnitCode", "KDCOST");
        param.put("TradeDateTime", paraMap.get("RunStartTime"));
        Map<String,Object> prmInfo = binBEMQMES97_BL.getPrmInfo(null, param, false);
        if(null == prmInfo || ConvertUtil.getString(prmInfo.get("BIN_PromotionProductVendorID")).equals("")){
            BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO();
            batchExceptionDTO.setBatchName(this.getClass());
            batchExceptionDTO.setErrorCode("EOT00047");
            batchExceptionDTO.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
            throw new CherryBatchException(batchExceptionDTO);
        }
        String kdCOSTID = ConvertUtil.getString(prmInfo.get("BIN_PromotionProductVendorID"));
        */
        String kdCOSTID = "0";
        
        //查询条码为ZDZK的虚拟促销品整单折扣，不存在报错
        param = new HashMap<String,Object>();
        param.put("BIN_OrganizationInfoID", organizationInfoID);
        param.put("BIN_BrandInfoID", brandInfoID);
        param.put("BarCode", "ZDZK");
        param.put("UnitCode", "ZDZK");
        param.put("TradeDateTime", paraMap.get("RunStartTime"));
        Map<String,Object> prmInfo = binBEMQMES97_BL.getPrmInfo(null, param, false);
        if(null == prmInfo || ConvertUtil.getString(prmInfo.get("BIN_PromotionProductVendorID")).equals("")){
            BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO();
            batchExceptionDTO.setBatchName(this.getClass());
            batchExceptionDTO.setErrorCode("EOT00048");
            batchExceptionDTO.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
            throw new CherryBatchException(batchExceptionDTO);
        }
        String zdzkID = ConvertUtil.getString(prmInfo.get("BIN_PromotionProductVendorID"));
        
        paraMap.put("XNTZ9999_BIN_ProductVendorID", xntzID); // 虚拟套装
        paraMap.put("KDCOST_BIN_ProductVendorID", kdCOSTID); // 快递费
        paraMap.put("ZDZK_BIN_ProductVendorID", zdzkID); // 整单折扣
		
		// 上一批次(页)最后一条记录
		String bathLastVal = "";
		
		while (true) {
			
			// 查询失败的订单列表
			Map<String, Object> selOrderMap = new HashMap<String, Object>();
			selOrderMap.putAll(paraMap);
			selOrderMap.put("JobCodes", new String[]{"BAT094","BAT131"}); // 宏巍正常订单失败的、失败订单处理失败后又失败的。
			selOrderMap.put("batchSize", BATCH_SIZE);
			selOrderMap.put("bathLastVal", bathLastVal);
			
			List<Map<String, Object>> orderNoList = new ArrayList<Map<String,Object>>();
			// 处理数据类型 0：失败的数据、1：正常的数据
			int dataClass = ConvertUtil.getInt(paraMap.get("dataClass"));
			if(0 == dataClass){
				orderNoList = binOTHONG01_Service.getFaildOrderNoList(selOrderMap);
			} else{
				orderNoList = binOTHONG01_Service.getNoPrtOrderNoList(selOrderMap);
			}
			
			if (CherryBatchUtil.isBlankList(orderNoList)) {
				break;
			} else {
				// 统计总条数
				totalCount += orderNoList.size();
				try{
					// 批量处理分页的数据
					updateBatchDate(esInterfaceConf, paraMap, orderNoList,dataClass);
					
				}catch(Exception e){
					
					flag = CherryBatchConstants.BATCH_ERROR;
					
					// 待定
//					binbekdcpi01_Service.manualRollback();
					
					BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
					batchLoggerDTO.setCode("EOT00105");
					batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
					logger.BatchLogger(batchLoggerDTO, e);
					
					// 处理失败后，跳转至下一批次(页)
//					continue;
				}
				
			}
			
			// 当前批次最后一条数据的值赋给bathLastVal，用于当前任务下一批次(页)订单数据的筛选条件
			bathLastVal = CherryBatchUtil.getString(orderNoList.get(orderNoList.size()- 1).get("orderNumber"));
			
			// 失败的订单集合为空或少于一批次(页)处理数量，跳出循环
			if (orderNoList.size() < BATCH_SIZE) {
				break;
			}
		}
	}
	
	/**
	 * 处理失败的订单数据
	 * 写入电商订单表、发送销售MQ
	 * @param map
	 * @param faildOrderNoList
	 * @param dataClass 数据类别 1：正常数据 0：失败数据
	 * @throws CherryBatchException,Exception 
	 */
	@SuppressWarnings("unchecked")
	private void updateBatchDate(Map<String,Object> esInterfaceConf, Map<String,Object> paraMap,List<Map<String,Object>> faildOrderNoList,int dataClass) throws CherryBatchException,Exception{
		
		// 检查ZDZK、KDCOST、员工、部门等信息在
		
		// 配置请求宏巍订单详细的参数，请调用webservice取得订单仔细
		for(Map<String,Object> faildOrder :  faildOrderNoList){
			Map<String,Object> selOrderMap = new HashMap<String, Object>();
			selOrderMap.putAll(esInterfaceConf);
			paraMap.put("AccountName", esInterfaceConf.get("AccountName"));
			
			selOrderMap.put("orderNumber", faildOrder.get("orderNumber"));
			paraMap.put("orderNumber", faildOrder.get("orderNumber"));
			
			Map<String, Object> resultJSONDetail = new HashMap<String, Object>();
			try{
				
				resultJSONDetail = getWebServiceErpOrderDetail(selOrderMap);
				if(ConvertUtil.getString(resultJSONDetail.get("isSuccess")).equals("true")){
					
					// 处理单个订单
					handleSingleOrder(resultJSONDetail, paraMap);
                    
				} else{
                    //调用接口erpOrderDetail返回不成功
                    BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO();
                    batchExceptionDTO.setBatchName(this.getClass());
                    batchExceptionDTO.setErrorCode("EOT00045");
                    batchExceptionDTO.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
                    batchExceptionDTO.addErrorParam(ConvertUtil.getString(esInterfaceConf.get("MethodName")));
                    batchExceptionDTO.addErrorParam(ConvertUtil.getString(resultJSONDetail.get("queryParams")));
                    batchExceptionDTO.addErrorParam(ConvertUtil.getString(resultJSONDetail.get("GetOrderDetail_result")));
                    throw new CherryBatchException(batchExceptionDTO);
				}
				
				// 如果当前处理的是Job运行失败履历表的数据，那么当前如果成功就将其从运行失败履历表中删除
				if(0 == dataClass){
					try{
						Map<String,Object> falidMap = new HashMap<String, Object>();
						setInsertInfoMapKey(falidMap);
						falidMap.put("organizationInfoId", comMap.get(CherryBatchConstants.ORGANIZATIONINFOID));
		                falidMap.put("brandInfoId", comMap.get(CherryBatchConstants.BRANDINFOID));
		                falidMap.put("JobCode", faildOrder.get("JobCode")); // 
		                falidMap.put("UnionIndex", ConvertUtil.getString(esInterfaceConf.get("AccountName")));
		                falidMap.put("UnionIndex1", ConvertUtil.getString(faildOrder.get("orderNumber")));
						binbecm01_IF.delJobRunFaildHistory(falidMap);
					}catch(Exception ex){
	        			logger.outExceptionLog(ex);
	            		logger.outLog("异常单据("+ ConvertUtil.getString(faildOrder.get("orderNumber")) +")在失败履历表中删除的处理失败:", CherryBatchConstants.LOGGER_ERROR);
	            		logger.outLog(resultJSONDetail.toString(), CherryBatchConstants.LOGGER_ERROR);
	        		}
				}
				binOTHONG01_Service.manualCommit();
			} catch(Exception e){
				
				flag = CherryBatchConstants.BATCH_WARNING;
				
				binOTHONG01_Service.manualRollback();
				failCount ++;
        		logger.outExceptionLog(e);
        		logger.outLog("单据处理失败:", CherryBatchConstants.LOGGER_ERROR);
        		logger.outLog(resultJSONDetail.toString(), CherryBatchConstants.LOGGER_ERROR);
        		//throw e;
        		
        		try{
        			// 写入单号 
        			// 设置失败履历表的参数
        			Map<String,Object> falidMap = new HashMap<String, Object>();
        			setInsertInfoMapKey(falidMap);
        			falidMap.put("organizationInfoId", comMap.get(CherryBatchConstants.ORGANIZATIONINFOID));
        			falidMap.put("brandInfoId", comMap.get(CherryBatchConstants.BRANDINFOID));
        			String jobCode = ConvertUtil.getString(faildOrder.get("JobCode"));
        			falidMap.put("JobCode", "".equals(jobCode) ? paraMap.get("JobCode") : jobCode); // 原来就是异常的单据，此时使用当时写入履历的程序
        			falidMap.put("UnionIndex", ConvertUtil.getString(esInterfaceConf.get("AccountName")));
        			falidMap.put("UnionIndex1", ConvertUtil.getString(faildOrder.get("orderNumber")));
        			
        			falidMap.put("ErrorMsg", ",{\"" + e.getMessage() + "\"}");
        			
        			falidMap.put("Comments", resultJSONDetail.toString()); // 写入拉取订单原始信息，用于后续异常分析使用
        			binbecm01_IF.mergeJobRunFaildHistory(falidMap);
        		}catch(Exception ex){
        			logger.outExceptionLog(ex);
            		logger.outLog("单据处理异常，但将单据("+ ConvertUtil.getString(faildOrder.get("orderNumber")) +")写入失败履历表处理失败:", CherryBatchConstants.LOGGER_ERROR);
            		logger.outLog(resultJSONDetail.toString(), CherryBatchConstants.LOGGER_ERROR);
        		}
			}
			
		}
		
	}
	
	/**
	 * 处理单个 订单
	 * @param resultJSONDetail
	 * @param paraMap
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	private void handleSingleOrder(Map<String, Object> resultJSONDetail, Map<String,Object> paraMap) throws Exception{
         
		Map<String,Object> erpOrders = new HashMap<String, Object>();
		try{
        	erpOrders = (Map<String, Object>) resultJSONDetail.get("erpOrders");
        	List<Map<String,Object>> erpOrderList = (List<Map<String, Object>>) erpOrders.get("erpOrder");
        	
        	// 定义订单的主数据（里面包括明细）
        	Map<String,Object> erpOrderMain = new HashMap<String,Object>();
        	if(null != erpOrderList.get(0) && !erpOrderList.get(0).isEmpty()){
        		erpOrderMain = (Map<String, Object>) erpOrderList.get(0);
        	}
        	
        	// 定义从订单主数据中取出的明细
        	List<Map<String,Object>> erpDetailList = new ArrayList<Map<String,Object>>();
        	if(null != erpOrderMain && !erpOrderMain.isEmpty()){
        		Map<String, Object> erpOrderItems = (Map<String, Object>)erpOrderMain.get("erpOrderItems");
        		if(null != erpOrderItems && !erpOrderItems.isEmpty()){
        			erpDetailList = (List<Map<String, Object>>) erpOrderItems.get("erpOrderItem");
        		}
        	} else{
        		throw new Exception(ConvertUtil.getString(paraMap.get("orderNumber"))+"订单获取异常");
        	}
        	 
        	erpOrderMain.put("BIN_OrganizationInfoID", comMap.get("BIN_OrganizationInfoID"));
        	erpOrderMain.put("BIN_BrandInfoID", comMap.get("BIN_BrandInfoID"));
        	erpOrderMain.put("BrandCode", comMap.get(CherryBatchConstants.BRAND_CODE));
        	
        	erpOrderMain.put("XNTZ9999_BIN_ProductVendorID", paraMap.get("XNTZ9999_BIN_ProductVendorID"));
        	erpOrderMain.put("KDCOST_BIN_ProductVendorID", paraMap.get("KDCOST_BIN_ProductVendorID"));
        	erpOrderMain.put("ZDZK_BIN_ProductVendorID", paraMap.get("ZDZK_BIN_ProductVendorID"));
        	
        	//下单时间
        	String orderDate = ConvertUtil.getString(erpOrderMain.get("orderDate"));
        	erpOrderMain.put("TradeDateTime", orderDate);
        	/*
        	SimpleDateFormat sdf = new SimpleDateFormat(CherryConstants.DATE_PATTERN_24_HOURS);
        	Date orderDateTime = sdf.parse(orderDate);
        	
        	// 订单号
        	String orderNumber = ConvertUtil.getString(erpOrderMain.get("orderNumber"));
        	
        	*/
        	
        	// 订单状态 (转换成新后台使用的状态值)
        	String billState = getBillStateByOrderStatus(ConvertUtil.getString(erpOrderMain.get("orderStatus")));
        	
        	if("".equals(billState)){
        		// 如果订单状态没查到对应，则试试用中文订单状态
        		billState = getZHBillStateByOrderStatus(ConvertUtil.getString(erpOrderMain.get("orderStatus")));
        	}

        	// ****************** 退款处理  start ****************** 
            boolean hasRefundDetail = false;//明细是否有退款标志
            boolean allRefund = true;//明细全部是退款标志【明细全部为退款则不管主单的状态为何都认为是订单取消】
            DecimalFormat df = new DecimalFormat("#0.00");
            if(null != erpDetailList && erpDetailList.size()>0){
                for(int j=0;j<erpDetailList.size();j++){
                    Map<String,Object> detailDTO = erpDetailList.get(j);
                    String isRefund = ConvertUtil.getString(detailDTO.get("isRefund"));
                    if(isRefund.equals("1") || billState.equals("0")){
                        String totalAmount = ConvertUtil.getString(erpOrderMain.get("totalAmount"));
                        String initialActualAmount = ConvertUtil.getString(erpOrderMain.get("initialActualAmount"));
                        String actualAmount = ConvertUtil.getString(erpOrderMain.get("actualAmount"));
                        String pricePay = ConvertUtil.getString(detailDTO.get("agioPrice"));
                        String price = ConvertUtil.getString(detailDTO.get("price"));
                        int orderCount = CherryUtil.obj2int(detailDTO.get("orderCount"));
                        String payableAmount = df.format(new BigDecimal(pricePay).multiply(new BigDecimal(orderCount)));
                        initialActualAmount = df.format(new BigDecimal(initialActualAmount).subtract(new BigDecimal(payableAmount)));
                        if(new BigDecimal(initialActualAmount).doubleValue() < 0){
                        	erpOrderMain.put("initialActualAmount", "0.00");
                        }else{
                        	erpOrderMain.put("initialActualAmount", initialActualAmount);
                        }
                        actualAmount = df.format(new BigDecimal(actualAmount).subtract(new BigDecimal(payableAmount)));
                        if(new BigDecimal(initialActualAmount).doubleValue() < 0){
                        	erpOrderMain.put("actualAmount", "0.00");
                        }else{
                        	erpOrderMain.put("actualAmount", actualAmount);
                        }
                        // 乘法
                        String amount = df.format(new BigDecimal(price).multiply(new BigDecimal(orderCount)));
                        // 减法
                        totalAmount = df.format(new BigDecimal(totalAmount).subtract(new BigDecimal(amount)));
                        if(new BigDecimal(totalAmount).doubleValue() < 0){
                        	erpOrderMain.put("totalAmount", "0.00");
                        }else{
                        	erpOrderMain.put("totalAmount", totalAmount);
                        }
                        detailDTO.put("orderCount", "0");
                        detailDTO.put("giftCount", "0");
                        detailDTO.put("discountFee", "0.00");
                        hasRefundDetail = true;
                    }else{
                        allRefund = false;
                    }
                }
            }
            
            //  ****************** 退款处理   end ****************** 
            
            int totalQuantity = getTotalQuantity(erpDetailList);
            
            erpOrderMain.put("totalQuantity", totalQuantity);
            
            erpOrderMain.put("BIN_EmployeeID", paraMap.get("BIN_EmployeeID"));
            erpOrderMain.put("EmployeeCode", paraMap.get("baCode"));
            erpOrderMain.put("Ticket_type", "NE");
            erpOrderMain.put("ModifiedTimes", "0");
            erpOrderMain.put("BillType", "1");
            
            // 电商订单表中的手机号字段长度最大为18位，超过此限制的截取前11位作为手机号。
            String mobilePhone = ConvertUtil.getString(erpOrderMain.get("mobilePhone"));
            erpOrderMain.put("mobilePhone", (mobilePhone.length() > 18 ? mobilePhone.substring(0, 11) : mobilePhone));
            
            // ****************** NEWWITPOS-2346 会员信息处理开始使用共通方法 2015.10.12 start ******************
            //以下代码为调用共通所使用的getMemInfo方法
//            Map<String, Object> departParamMap = new HashMap<String, Object>();
//            departParamMap.put("BIN_OrganizationInfoID", comMap.get("BIN_OrganizationInfoID"));
//            departParamMap.put("BIN_BrandInfoID", comMap.get("BIN_BrandInfoID"));
//            // 查询组织结构ID
////    		departParamMap.put("DepartName", erpOrder.get("shopName"));
//    		departParamMap.put("DepartName", paraMap.get("AccountName"));
//    		List<Map<String, Object>> departList = binOTHONG01_Service.getDepartInfo(departParamMap); 
    		
    		String organizationID = ConvertUtil.getString(paraMap.get("BIN_OrganizationID"));
    		String counterCode = ConvertUtil.getString(paraMap.get("CounterCode"));
//    		if (!CherryBatchUtil.isBlankList(departList)) {
//    			organizationID = ConvertUtil.getString(departList.get(0).get("BIN_OrganizationID"));
//    			counterCode = ConvertUtil.getString(departList.get(0).get("DepartCode"));
//    		}
    		
    		erpOrderMain.put("BIN_OrganizationID", organizationID);
    		erpOrderMain.put("CounterCode", counterCode);
    		
    		Map<String,Object> baseInfoMap = getMemberInfoByParam(erpOrderMain);
    		erpOrderMain.put("BIN_MemberInfoID", baseInfoMap.get("BIN_MemberInfoID"));
    		erpOrderMain.put("MemberCode", baseInfoMap.get("MemberCode"));
    		erpOrderMain.put("MemberName", baseInfoMap.get("MemberName"));
    		erpOrderMain.put("MemberLevel", baseInfoMap.get("MemberLevel"));
            
            // ****************** NEWWITPOS-2346 会员信息处理开始使用共通方法 2015.10.12 end ******************
            
            //判断是否已经存在电商订单
            String oldBillState = "";
            if(allRefund || billState.equals("0")){
                billState = "0";
                erpOrderMain.put("totalAmount", "0");
                erpOrderMain.put("initialActualAmount", "0");
                erpOrderMain.put("actualAmount", "0");
            }
            erpOrderMain.put("BillState", billState);
            
            // 查询billCode是否存在等于orderNumber的主单数据
            List<Map<String,Object>> esOrderMainList = binOTHONG01_Service.getESOrderMain(erpOrderMain);
            List<Map<String,Object>> payDetailList = new ArrayList<Map<String,Object>>();
            int billID = 0;
            int modifiedTimes = 0;
            
            // 付款时间
            String payTime = ConvertUtil.getString(erpOrderMain.get("payTime"));
            // 【支持预付定金的订单发送销售MQ】配置
            boolean depositOrderToSale = ConvertUtil.getString(PropertiesUtil.getMessage("DepositOrderToSale", null)).equals("true");
            // 已经接收过的订单的付款时间
            String oldPayTime = "";
            
            /**
             * 对于电商主从表的处理：
             * 		1）电商订单数据已经存在：更新电商订单主表信息，删除电商订单明细表及支付方式明细表后重新插入。
             * 		2）电商订单数据不存在：新增电商订单主从表及电商支付方式表
             */
            if(null != esOrderMainList && esOrderMainList.size()>0){
               
            	//存在更新主表，删除明细后重新插入
                billID = CherryUtil.obj2int(esOrderMainList.get(0).get("BIN_ESOrderMainID"));
                erpOrderMain.put("BIN_ESOrderMainID", billID);
                modifiedTimes = CherryUtil.obj2int(esOrderMainList.get(0).get("ModifiedTimes"))+1;
                erpOrderMain.put("ModifiedTimes", modifiedTimes);
                oldBillState = ConvertUtil.getString(esOrderMainList.get(0).get("BillState"));
                oldPayTime = ConvertUtil.getString(esOrderMainList.get(0).get("BillPayTime"));
                
                try{
                	updateESOrderMain(erpOrderMain);
                }catch(Exception e){
            		logger.outExceptionLog(e);
            		logger.outLog("电商订单主数据更新失败:", CherryBatchConstants.LOGGER_ERROR);
            		logger.outLog(erpOrderMain.toString(), CherryBatchConstants.LOGGER_ERROR);
            		throw e;
                }
                
             // 已经接收的电商订单明细中产品ID为null的数量----不为零，则之前是没有发送过MQ的需要补发MQ
//                int oldNonProIDDetailCount = saleInfoService.getESOrderNonProIDDetailCount(erpOrderMain);
                saleInfoService.deleteESOrderDetail(erpOrderMain);
                saleInfoService.deleteESOrderPayList(erpOrderMain);
                //处理订单明细----产品数据的加工处理
                /**
                 * 用于判断是否要发送销售相关MQ
                 * true：存在新后台不存在的产品信息；----不需要做发送MQ的相关操作
                 * false：不存在新后台不存在的产品信息----需要做发送MQ的相关操作
                 */
                boolean ishasNonGoods = this.handleAndInsertESOrderDetail(erpOrderMain, erpDetailList);
                /**
            	 * 2015-12-21：通过判断电商明细表的产品ID是否存在为空的情况来判断是否发送过MQ不准确。
            	 * 				1）与明细表的数据耦合性太高，一但明细被更改将造成该本程序的误判。
            	 * 				2）在主表中增加【是否发送过MQ】字段来判断，历史数据不好处理，所以不使用此方案。
            	 * 				3）直接去查询销售表，虽然会有MQ延迟的影响，但是不影响数据的接收，目前采用此方案。
            	 * 				
            	 * */
            	Map<String, Object> saleRecord =  binOTHONG01_Service.getSaleRecordbByOrderCode(erpOrderMain);
            	/**是否发送过MQ标记，true：已发送；false：未发送*/
            	boolean isAlreadySendMQFlag = (null != saleRecord && !saleRecord.isEmpty());
            	// 已经接进来的订单是否是需要发送MQ的订单（判断已经写入的订单数据）--用于补发销售MQ
				boolean isNeedSendMQForOldBill = (depositOrderToSale
						&& !"".equals(oldPayTime) && oldBillState.equals("1"))
						|| oldBillState.equals("2")
						|| oldBillState.equals("3")
						|| oldBillState.equals("4");
//                splitBomPrt(orderDetailParam, detailList);
                //处理付款方式明细
                Map<String,Object> payInfo = new HashMap<String,Object>();
                String payType = ConvertUtil.getString(erpOrderMain.get("payType"));
                payInfo.put("PayTypeCode", payType.equals("支付宝") ? "alipay" : payType);
                
                
//              payInfo.put("PayAmount", erpOrder.get("actualAmount"));
	            // 总金额 2016.02.02 应付总金额需要减去运费，但是当运费为0时，则不需要
	            String actualAmount = getFormatDiscount(ConvertUtil.getString(erpOrderMain.get("actualAmount"))); // 应付金额
	            String deliveryCost = getFormatDiscount(ConvertUtil.getString(erpOrderMain.get("deliveryCost"))); // 运费
	            if(! new BigDecimal(actualAmount).equals(new BigDecimal(0))){
	            	actualAmount = df.format(new BigDecimal(actualAmount).subtract(new BigDecimal(deliveryCost))); // 销售的总金额需要去除运费
	            }
	            payInfo.put("PayAmount", actualAmount);
                
                payDetailList.add(payInfo);
                insertESOrderPayList(erpOrderMain, payDetailList);
                
                /**
                 *	订单取消：0
				 *	已下单未付款：1
				 *	已付款：2
				 *	已发货：3
				 *	已收货（领取）：4
                 */
                if(depositOrderToSale) {
                	// 大前提：当前产品都是存在的
                	if(!ishasNonGoods) {
                    	if(oldBillState.equals("1") && (billState.equals("2") || billState.equals("3") || billState.equals("4"))) {
                    		/**
                             *  BillState由1变成2/3/4时有两种情况：
                             *   1、上次接入时应发送MQ：
                             *   		1）未发送MQ：补发
                             *   		2）已发送：修改MQ----此情况只有预售单（付尾款时）
                             *   2、上次接入时不应发送MQ:
                             *   		1）当前应该发送MQ
                             */
                    		if(isNeedSendMQForOldBill) {
                    			// 当前订单在上次接入时已经应该发送MQ了
                    			if(!isAlreadySendMQFlag) {
                    				// 之前应该发送MQ而没有发的，现应补一条销售MQ----MQ如果是延迟的话当前MQ会被忽略
                        			saleInfoLogic.sendMQ_NS(getMQData_NS(erpOrderMain,erpDetailList,payDetailList));
                    			} else {
                    				// 对于已经发送过MQ(通过新逻辑获取的订单)--此为支付尾款的MQ，应发修改销售MQ
                    				erpOrderMain.put("Ticket_type", "MO");
                                    saleInfoLogic.sendMQ_NS(getMQData_NS(erpOrderMain,erpDetailList,payDetailList));
                    			}
                    		} else {
                    			// 之前没有必要发送MQ，现在要发送MQ
                    			saleInfoLogic.sendMQ_NS(getMQData_NS(erpOrderMain,erpDetailList,payDetailList));
                    		}
                			
                    	} else {
                    		/**
                    		 * 订单状态不是:1->(2,3,4)
                    		 * 1、有退款的明细：
                    		 * 				1）未发送过销售MQ：上次接收是应发送MQ的则补发销售MQ
                    		 * 				2）已发送过销售MQ: 发送MO的MQ
                    		 * 2、无退款的明细：
                    		 * 				1）未发送过销售MQ：上次接收是应发送MQ的则补发销售MQ
                    		 * 				2）已发送过销售MQ: 目前订单状态发生变化，发送SC的MQ
                    		 */
            				if(hasRefundDetail){
            					if(!isAlreadySendMQFlag) {
            						// 之前应发送且没有发送MQ的应补发MQ
            						if(isNeedSendMQForOldBill) {
            							saleInfoLogic.sendMQ_NS(getMQData_NS(erpOrderMain,erpDetailList,payDetailList));
            						}
            					} else {
                					// 1、已经发送过MQ的，对于退款明细应发送修改销售MQ
            						erpOrderMain.put("Ticket_type", "MO");
                                    saleInfoLogic.sendMQ_NS(getMQData_NS(erpOrderMain,erpDetailList,payDetailList));
            					}
            				} else {
            					// 没有包含退款的明细
            					if(!isAlreadySendMQFlag) {
            						// 之前应发送且没有发送MQ的应补发MQ
            						if(isNeedSendMQForOldBill) {
            							saleInfoLogic.sendMQ_NS(getMQData_NS(erpOrderMain,erpDetailList,payDetailList));
            						}
            					} else if(!oldBillState.equals(billState)){
                                    //电商订单状态发生改变还需要发送更改销售单据状态MQ--只对状态有发生过更改的
                                	saleInfoLogic.sendMQ_SC(getMQData_SC(erpOrderMain,erpDetailList,payDetailList));
                                }
            				}
                    	}
                	}
                } else {
                	// 大前提：---当前产品全存在
                	if(!ishasNonGoods) {
                    	// 目前其他品牌发送销售MQ的逻辑不变----不支持预售订单的接入
                        if(oldBillState.equals("1") && (billState.equals("2") || billState.equals("3") || billState.equals("4"))){
                            //电商订单更新，BillState：【1-->2/3/4】时，需要发送销售MQ----oldBillState='1'时未发送过销售MQ
                        	if(!isAlreadySendMQFlag) {
                        		// 未发送过MQ
                        		saleInfoLogic.sendMQ_NS(getMQData_NS(erpOrderMain,erpDetailList,payDetailList));
                        	}
                        }else{
                        	/**
                    		 * 订单状态不是:1->(2,3,4)
                    		 * 1、有退款的明细：
                    		 * 				1）未发送过销售MQ：上次接收是应发送MQ的则补发销售MQ
                    		 * 				2）已发送过销售MQ: 发送MO的MQ
                    		 * 2、无退款的明细：
                    		 * 				1）未发送过销售MQ：上次接收是应发送MQ的则补发销售MQ
                    		 * 				2）已发送过销售MQ: 目前订单状态发生变化，发送SC的MQ
                    		 */
            				if(hasRefundDetail){
            					if(!isAlreadySendMQFlag) {
            						// 之前应发送且没有发送MQ的应补发MQ
            						if(isNeedSendMQForOldBill) {
            							saleInfoLogic.sendMQ_NS(getMQData_NS(erpOrderMain,erpDetailList,payDetailList));
            						}
            					} else {
                					// 1、已经发送过MQ的，对于退款明细应发送修改销售MQ
            						erpOrderMain.put("Ticket_type", "MO");
                                    saleInfoLogic.sendMQ_NS(getMQData_NS(erpOrderMain,erpDetailList,payDetailList));
            					}
            				} else {
            					// 没有包含退款的明细
            					if(!isAlreadySendMQFlag) {
            						// 之前应发送且没有发送MQ的应补发MQ
            						if(isNeedSendMQForOldBill) {
            							saleInfoLogic.sendMQ_NS(getMQData_NS(erpOrderMain,erpDetailList,payDetailList));
            						}
            					} else if(!oldBillState.equals(billState)){
                                    //电商订单状态发生改变还需要发送更改销售单据状态MQ--只对状态有发生过更改的
                                	saleInfoLogic.sendMQ_SC(getMQData_SC(erpOrderMain,erpDetailList,payDetailList));
                                }
            				}
                        }
                	}
                }
                
            } else{
            	
            	try{
            		billID = insertESOrderMain(erpOrderMain);
            	} catch(Exception e){
            		logger.outExceptionLog(e);
            		logger.outLog("电商订单主数据新增失败:", CherryBatchConstants.LOGGER_ERROR);
            		logger.outLog(erpOrderMain.toString(), CherryBatchConstants.LOGGER_ERROR);
            		throw e;
            	}
            	
                erpOrderMain.put("BIN_ESOrderMainID", billID);
                //处理订单明细----处理产品明细及插入明细信息
                boolean ishasNonGoods = this.handleAndInsertESOrderDetail(erpOrderMain, erpDetailList);
                
                //处理付款方式明细
                Map<String,Object> payInfo = new HashMap<String,Object>();
                String payType = ConvertUtil.getString(erpOrderMain.get("payType"));
                payInfo.put("PayTypeCode", payType.equals("支付宝") ? "alipay" : payType);
                payInfo.put("PayAmount", erpOrderMain.get("actualAmount"));
                payDetailList.add(payInfo);
                insertESOrderPayList(erpOrderMain, payDetailList);
                // 【薇诺娜】对于下单未付款但付款时间不为空的订单也发送销售MQ
                if((depositOrderToSale && !"".equals(payTime) && billState.equals("1")) || billState.equals("2") || billState.equals("3") || billState.equals("4")){
                    //电商订单新增，BillState是2/3/4，需要发送销售MQ
                	if(!ishasNonGoods) {
                		// 对于订单明细中存在（新后台不存在的商品）时不发送销售MQ
                		saleInfoLogic.sendMQ_NS(getMQData_NS(erpOrderMain,erpDetailList,payDetailList));
                	}
                }
            }
        	
        }catch(Exception e){
    		logger.outExceptionLog(e);
    		logger.outLog("单据处理失败:", CherryBatchConstants.LOGGER_ERROR);
    		logger.outLog(erpOrders.toString(), CherryBatchConstants.LOGGER_ERROR);
    		throw e;
        }
        
	}
    
	/**
	 * init
	 * @param map
	 */
	private void init(Map<String, Object> map)  throws CherryBatchException, Exception{
		comMap = getComMap(map);
		
		// BatchCD 来自VSS$/01.Cherry/02.设计文档/01.概要设计/00.各种一览/【新设】CherryBatch一览.xlsx
		map.put("JobCode", "BAT131");
		
		// 程序【开始运行时间】
		String runStartTime = binOTHONG01_Service.getSYSDateTime();
		// 作成日时
		map.put("RunStartTime", runStartTime);
		
		// 作成日时
		comMap.put(CherryConstants.CREATE_TIME, runStartTime);
		
		// 取得Job控制程序的数据截取开始时间及结束时间
		Map<String, Object> jobControlInfoMap = binbecm01_IF.getJobControlInfo(map);
		
		// 程序【截取数据开始时间】
		map.put("TargetDataStartTime", jobControlInfoMap.get("TargetDataStartTime"));
		// 程序【截取数据结束时间】
		map.put("TargetDataEndTime", jobControlInfoMap.get("TargetDataEndTime"));
	}
    
	/**
	 * 共通Map
	 * @param map
	 * @return
	 */
	private Map<String, Object> getComMap(Map<String, Object> map) {
		Map<String, Object> baseMap = new HashMap<String, Object>();
		
		// 更新程序名
		map.put(CherryBatchConstants.UPDATEPGM, "BINOTHONG02");
		// 作成程序名
		map.put(CherryBatchConstants.CREATEPGM, "BINOTHONG02");
		// 作成者
		map.put(CherryBatchConstants.CREATEDBY,CherryBatchConstants.UPDATE_NAME);
		// 更新者
		map.put(CherryBatchConstants.UPDATEDBY,CherryBatchConstants.UPDATE_NAME);
		// 所属组织
		map.put(CherryBatchConstants.ORGANIZATIONINFOID, map.get(CherryBatchConstants.ORGANIZATIONINFOID).toString());
		map.put("BIN_OrganizationInfoID", map.get(CherryBatchConstants.ORGANIZATIONINFOID).toString());
		// 品牌ID
		map.put(CherryBatchConstants.BRANDINFOID, map.get(CherryBatchConstants.BRANDINFOID).toString());
		map.put("BIN_BrandInfoID", map.get(CherryBatchConstants.BRANDINFOID).toString());
		
		// 品牌Code
		map.put(CherryBatchConstants.BRAND_CODE, map.get(CherryBatchConstants.BRAND_CODE));
		baseMap.putAll(map);
		
		return baseMap;
	}

    /**
     * 查询该订单收货人的会员信息【此方法为得到会员信息的新增方法】
     * @param orderMap
     * @return Map:该订单的会员信息【收货人不是会员时生成会员信息，注：生成会员有两种方式，通过系统配置项1321来控制】
     * @throws Exception
     */
	private Map<String, Object> getMemberInfoByParam(Map<String, Object> orderMap) throws Exception {
		Map<String, Object> memberParamMap=new HashMap<String, Object>();
		
		String mobilePhone = ConvertUtil.getString(orderMap.get("mobilePhone"));
		String brandCode = ConvertUtil.getString(orderMap.get("BrandCode"));
		mobilePhone = CherryBatchSecret.encryptData(brandCode, mobilePhone);
		
		memberParamMap.put("BIN_OrganizationInfoID", comMap.get(CherryBatchConstants.ORGANIZATIONINFOID));
		memberParamMap.put("BIN_BrandInfoID", CherryBatchConstants.BRANDINFOID);
		memberParamMap.put("MobilePhone", mobilePhone);
		memberParamMap.put("BIN_EmployeeID", orderMap.get("BIN_EmployeeID"));
		memberParamMap.put("EmployeeCode", orderMap.get("EmployeeCode"));
		memberParamMap.put("orderDate", orderMap.get("orderDate"));
		memberParamMap.put("telephone", orderMap.get("telephone"));
		memberParamMap.put("MemberAddress", orderMap.get("address"));
		memberParamMap.put("brandCode", brandCode);
		memberParamMap.put("orderWay", orderMap.get("orderWay"));
		memberParamMap.put("shopName", orderMap.get("shopName"));
		memberParamMap.put("BIN_OrganizationID", orderMap.get("BIN_OrganizationID"));
		memberParamMap.put("CounterCode", orderMap.get("CounterCode"));
		memberParamMap.put("MemberName", orderMap.get("consignee"));
		memberParamMap.put("nickName", orderMap.get("memberName"));
		//源手机号，未加密的
		memberParamMap.put("sourceMobilePhone", orderMap.get("mobilePhone"));
		//调用查询会员信息共通方法，得到会员信息
		Map<String, Object> resultMap = binOLCM60_BL.getMemInfo(memberParamMap);
		return resultMap;
	}
    
    /**
     * 设置插入/更新Sale.BIN_ESOrderMain值
     * @param erpOrder
     * @param dataType
     * @return
     */
    private Map<String,Object> setESOrderMain(Map<String,Object> erpOrder,String dataType){
        Map<String,Object> esOrderMain = new HashMap<String,Object>();
        if(dataType.equals("update")){
            esOrderMain.put("BIN_ESOrderMainID",erpOrder.get("BIN_ESOrderMainID"));
        }else if(dataType.equals("insert")){
            //新后台中的单据号
            String orderNo = binOLCM03_BL.getTicketNumber(ConvertUtil.getString(erpOrder.get("BIN_OrganizationInfoID")), ConvertUtil.getString(erpOrder.get("BIN_BrandInfoID")), "BATCH", "DS");
            esOrderMain.put("OrderNo",orderNo);
        }
        //所属组织ID
        esOrderMain.put("BIN_OrganizationInfoID",erpOrder.get("BIN_OrganizationInfoID"));
        //所属品牌ID
        esOrderMain.put("BIN_BrandInfoID",erpOrder.get("BIN_BrandInfoID"));
        //组织结构ID
        esOrderMain.put("BIN_OrganizationID",erpOrder.get("BIN_OrganizationID"));
        //员工ID 对于电商平台，也会虚拟一个BA
        esOrderMain.put("BIN_EmployeeID",erpOrder.get("BIN_EmployeeID"));
        //员工代码code
        esOrderMain.put("EmployeeCode",erpOrder.get("EmployeeCode"));
        //下单组织结构ID
        esOrderMain.put("BIN_OrganizationIDDX", erpOrder.get("BIN_OrganizationID"));
        //下单员工ID
        esOrderMain.put("BIN_EmployeeIDDX", erpOrder.get("BIN_EmployeeID"));
        //下单员工代码code
        esOrderMain.put("EmployeeCodeDX", erpOrder.get("EmployeeCode"));
        //数据来源
        String orderWay = ConvertUtil.getString(erpOrder.get("orderWay"));
        String shopName = ConvertUtil.getString(erpOrder.get("shopName"));
        esOrderMain.put("DataSource",getDataSourceByName(orderWay,shopName));
        //客户数据来源
        esOrderMain.put("DataSourceCustomer",null);
        //店铺名称
        esOrderMain.put("ShopName",shopName);
        //订单编号，来自于电商（有可能被ERP处理过，不再是电商平台上展示给消费者的原始单据号），全局唯一
        esOrderMain.put("BillCode",erpOrder.get("orderNumber"));
        //业务关联单号，保留
        esOrderMain.put("RelevanceBillCode",null);
        //电商平台原始的单据号
        esOrderMain.put("OriginalBillCode",erpOrder.get("oldOrderNumber"));
        //SALE :标准订单 IN_BUY：内买订单 SUPPLEMENT：补差订单 EXCHANGE：换货订单
        //DEALERS：代销订单 JUS：聚划算 LBP：LBP GROUP：团购订单 REISSUE：补发订单 WHOLESALE：批发订单 UNDEFIND：未定义订单
        //交易类型（销售：NS，退货：SR,积分兑换:PX）。PX在旧的版本中也是作为NS处理的，为了能够更好的进行区分，在新版本中采用独立的类型PX，处理逻辑与NS相同。
        esOrderMain.put("SaleType",CherryConstants.BUSINESS_TYPE_NS);
        //单据类型
        esOrderMain.put("TicketType",erpOrder.get("Ticket_type"));
        //单据状态
        esOrderMain.put("BillState",erpOrder.get("BillState"));
        // 单据类型
        esOrderMain.put("BillType", erpOrder.get("BillType"));
        //消费者类型 NP：普通个人  NG：普通团购  MP：会员个人  MG：会员团购。
        if(ConvertUtil.getString(erpOrder.get("BIN_MemberInfoID")).equals("")){
            esOrderMain.put("ConsumerType","NP");
        }else{
            esOrderMain.put("ConsumerType","MP");
        }
        //新后台查到的会员ID
        esOrderMain.put("BIN_MemberInfoID",erpOrder.get("BIN_MemberInfoID"));
        //新后台查到的会员卡号
        esOrderMain.put("MemberCode",erpOrder.get("MemberCode"));
        //新后台查到的会员姓名
        esOrderMain.put("MemberName",erpOrder.get("MemberName"));
        //会员昵称
        esOrderMain.put("MemberNickname",erpOrder.get("memberName"));
        //买家姓名
        esOrderMain.put("BuyerName",erpOrder.get("MemberName"));
        //买家手机号
        esOrderMain.put("BuyerMobilePhone",erpOrder.get("mobilePhone"));
        //买家的其它标识
        esOrderMain.put("BuyerIdentifier",null);
        //收货人姓名
        esOrderMain.put("ConsigneeName",erpOrder.get("consignee"));
        //收货人手机
        esOrderMain.put("ConsigneeMobilePhone",erpOrder.get("mobilePhone"));
        //收货人地址
        String address = ConvertUtil.getString(erpOrder.get("address"));
        esOrderMain.put("ConsigneeAddress",address);
        
        //收件人省市名称
        String orderNumber = ConvertUtil.getString(erpOrder.get("orderNumber"));
        Map<String,Object> provAndCityMap= binOTHONG01_BL.getProvNameAndCityName(address,orderNumber); 
        esOrderMain.putAll(provAndCityMap);
        
        //买家留言
        esOrderMain.put("BuyerMessage",erpOrder.get("buyerMessage"));
        //卖家备注
        esOrderMain.put("SellerMemo",erpOrder.get("sellerMemo"));
        //单据创建时间
        esOrderMain.put("BillCreateTime",erpOrder.get("orderDate"));
        //单据付款时间
        String payTime = ConvertUtil.getString(erpOrder.get("payTime"));
        esOrderMain.put("BillPayTime",payTime);
        //单据关闭时间
        esOrderMain.put("BillCloseTime",null);
        String billState = ConvertUtil.getString(erpOrder.get("BillState"));
        if(billState.equals("4") || billState.equals("0")){
            esOrderMain.put("BillCloseTime",CherryUtil.getSysDateTime(CherryConstants.DATE_PATTERN_24_HOURS));
        }
        //折前金额
        esOrderMain.put("OriginalAmount",erpOrder.get("totalAmount"));
        //整单折扣率
        esOrderMain.put("Discount", getFormatDiscount(erpOrder.get("totalDepositRate")));
        //折后金额
        esOrderMain.put("PayAmount",erpOrder.get("actualAmount"));
        //整单去零
        esOrderMain.put("DecreaseAmount","0");
        //花费积分
        esOrderMain.put("CostPoint","0");
        //花费积分对应的抵扣金额
        esOrderMain.put("CostpointAmount","0");
        //实收金额
        esOrderMain.put("Amount",erpOrder.get("initialActualAmount"));
        //计算明细得到
        esOrderMain.put("Quantity",erpOrder.get("totalQuantity"));
        //销售记录的被修改次数
        esOrderMain.put("ModifiedTimes",erpOrder.get("ModifiedTimes"));
        //快递公司代号
        esOrderMain.put("ExpressCompanyCode",erpOrder.get("logisticName"));
        //快递单编号
        esOrderMain.put("ExpressBillCode",erpOrder.get("expressNumber"));
        //快递费用
        esOrderMain.put("ExpressCost", erpOrder.get("deliveryCost"));
        //工作流ID
        esOrderMain.put("WorkFlowID",null);
        //备注
        esOrderMain.put("Comments",erpOrder.get("erpMemo"));
        // 是否预售单(状态为1，支付时间不为空)---- 做过预售的单子不一定能够设置这个状态（因为可能订单拉进来时状态已经不是状态1了）
        if(billState.equals("1") && !CherryBatchUtil.isBlankString(payTime)){
        	esOrderMain.put("PreSale","1");
        }
        
        setInsertInfoMapKey(esOrderMain);
        return esOrderMain;
    }
    
    /**
     * 对产品条码进行处理后查询相关产品信息
     * 根据明细数据中platOutSkuId作为unitCode（先用原始code查询，查询不到再做头部去零处理后查询）来查询
     * @param paramMap
     * @return 产品的ID、UnitCode、BarCode、SaleType信息，不会返回NULL（在新后台未找到产品，MAP中的BIN_ProductVendorID=NULL）
     * @throws CherryMQException
     */
    private Map<String, Object> getESOrderDetailByHandleCode(Map<String, Object> paramMap) throws CherryMQException {
    	Map<String,Object> resultMap = new HashMap<String,Object>();
    	// 快速费与整单折扣还是作为虚拟促销品来维护
    	if(ConvertUtil.getString(paramMap.get("barcode")).equals("KDCOST")){
            resultMap.put("SaleType", "P");
            resultMap.put("BIN_ProductVendorID", paramMap.get("BIN_ProductVendorID"));
            resultMap.put("UnitCode", "KDCOST");
            resultMap.put("BarCode", "KDCOST");
            return resultMap;
        }else if(ConvertUtil.getString(paramMap.get("barcode")).equals("ZDZK")){
            resultMap.put("SaleType", "P");
            resultMap.put("BIN_ProductVendorID", paramMap.get("BIN_ProductVendorID"));
            resultMap.put("UnitCode", "ZDZK");
            resultMap.put("BarCode", "ZDZK");
            return resultMap;
        }
    	
    	resultMap.put("SaleType", "N");
        Map<String,Object> param = new HashMap<String,Object>();
        param.put("BIN_OrganizationInfoID", paramMap.get("BIN_OrganizationInfoID"));
        param.put("BIN_BrandInfoID", paramMap.get("BIN_BrandInfoID"));
        // 对获取的订单明细中的厂商编码【平台规格编码】进行处理
        String unitCode = ConvertUtil.getString(paramMap.get("platOutSkuId"));
        //当platOutSkuId（平台SKU商家编码）为空时，再判断platOuterId（平台宝贝商家编码）是否有对应我们系统的UnitCode
        if (unitCode.equals("")){
        	unitCode = ConvertUtil.getString(paramMap.get("platOuterId"));
        }       
        // 先用原始code查询产品，若无此产品再做头部去零后再次查询
        param.put("UnitCode", unitCode);
        List<Map<String,Object>> productList = null;
        // 防止获取的unitCode为空的情况
        if(!"".equals(unitCode)) {
        	productList = binOTHONG01_Service.getProductInfo(param);
        }
        if((null == productList || productList.size() == 0) && !"".equals(unitCode)) {
            // 先把开头的"ZP"(不区分)字符去除
        	unitCode = unitCode.replaceFirst("^(?i:ZP)*", "");
            // 去除barCode开头的所有"0"
            unitCode = unitCode.replaceFirst("^0*", "");
            param.put("UnitCode", unitCode);
            productList = binOTHONG01_Service.getProductInfo(param);
        }
        String productVendorID = "";
        String barCode = "";
        if(null != productList && productList.size()>0){
            productVendorID = ConvertUtil.getString(productList.get(0).get("BIN_ProductVendorID"));
            barCode = ConvertUtil.getString(productList.get(0).get("BarCode"));
        }
        if(productVendorID.equals("")){
            resultMap.put("BIN_ProductVendorID", null);
            resultMap.put("BarCode", null);
            resultMap.put("UnitCode", unitCode);
        }else{
            resultMap.put("BIN_ProductVendorID", productVendorID);
            resultMap.put("BarCode", barCode);
            resultMap.put("UnitCode", unitCode);
        }
        return resultMap;
    }

    /**
     * 转换销售MQ需要的数据
     * @param dataMap
     * @return
     * @throws ParseException 
     */
    private Map<String,Object> getMQData_NS(Map<String,Object> erpOrder,List<Map<String,Object>> detailList,List<Map<String,Object>> payDetailList) throws ParseException{
    	
    	
        // 通过原始订单商品明细拆分BOM商品数据--通过系统配置项确定是否调用该方法(电商订单支持套装BOM产品拆分)
        int organizationInfoID = CherryUtil.obj2int(erpOrder.get("BIN_OrganizationInfoID"));
        int brandInfoID = CherryUtil.obj2int(erpOrder.get("BIN_BrandInfoID"));
        boolean isSplitPrtBom = binOLCM14_BL.isConfigOpen("1338", organizationInfoID, brandInfoID);
        if(isSplitPrtBom){
        	// 将销售明细中的产品类型为BOM的商品进行拆分，将拆分后的商品加入到销售MQ明细集合中
        	detailList = binOTHONG01_BL.splitBomPrt(erpOrder, detailList);
        }
        
		// 系统配置项1321:【电商订单线上线下会员合并规则】0：不合并（默认）1：按手机号合并 2:不合并且不存在也不新增(销售MQ不加入会员信息)
		String configValue = binOLCM14_BL.getConfigValue("1321", String.valueOf(organizationInfoID),String.valueOf(brandInfoID));
    	
        Map<String,Object> resultMap = new HashMap<String,Object>();
        Map<String,Object> mainData = new HashMap<String,Object>();
        mainData.put("BrandCode", erpOrder.get("BrandCode"));
        mainData.put("TradeNoIF", erpOrder.get("orderNumber"));
        mainData.put("ModifyCounts", erpOrder.get("ModifiedTimes"));
        mainData.put("CounterCode", erpOrder.get("CounterCode"));
        mainData.put("RelevantCounterCode", "");
        mainData.put("TotalQuantity", erpOrder.get("totalQuantity"));
        
        // 总金额 2016.02.02 应付总金额需要减去运费，但是当运费为0时，则不需要
        String actualAmount = getFormatDiscount(ConvertUtil.getString(erpOrder.get("actualAmount"))); // 应付金额
        String deliveryCost = getFormatDiscount(ConvertUtil.getString(erpOrder.get("deliveryCost"))); // 运费
        DecimalFormat df = new DecimalFormat("#0.00");
        if(new BigDecimal(0).compareTo(new BigDecimal(actualAmount)) != 0){
        	actualAmount = df.format(new BigDecimal(actualAmount).subtract(new BigDecimal(deliveryCost))); // 销售的总金额需要去除运费
        }
//        mainData.put("TotalAmount", erpOrder.get("actualAmount"));
        mainData.put("TotalAmount", actualAmount);
        
        mainData.put("TradeType", MessageConstants.BUSINESS_TYPE_NS);
        mainData.put("SubType", "");
        mainData.put("RelevantNo", "");
        mainData.put("Reason", "");
        String orderDate = ConvertUtil.getString(erpOrder.get("orderDate"));
        SimpleDateFormat sdf = new SimpleDateFormat(CherryConstants.DATE_PATTERN_24_HOURS);
        Date tradeDateTime = sdf.parse(orderDate);
        SimpleDateFormat sdf_YMD = new SimpleDateFormat(CherryConstants.DATE_PATTERN);
        SimpleDateFormat sdf_HMS = new SimpleDateFormat("HH:mm:ss");
        mainData.put("TradeDate", sdf_YMD.format(tradeDateTime));
        mainData.put("TradeTime", sdf_HMS.format(tradeDateTime));
        mainData.put("TotalAmountBefore", erpOrder.get("BillState"));
        mainData.put("TotalAmountAfter", "");
        if("2".equals(configValue)){
        	mainData.put("MemberCode", "");
        	mainData.put("Member_level", "");
        } else{
        	mainData.put("MemberCode", erpOrder.get("MemberCode"));
        	mainData.put("Member_level", erpOrder.get("MemberLevel"));
        }
        
        mainData.put("Counter_ticket_code", "");
        mainData.put("Counter_ticket_code_pre", "");
        mainData.put("Ticket_type", erpOrder.get("Ticket_type"));
        mainData.put("Sale_status", "OK");
        //消费者类型 NP：普通个人  NG：普通团购  MP：会员个人  MG：会员团购。
        if(ConvertUtil.getString(erpOrder.get("BIN_MemberInfoID")).equals("")){
            mainData.put("Consumer_type","NP");
        }else{
        	if(!"2".equals(configValue)){
        		mainData.put("Consumer_type","MP");
        	} else{
        		mainData.put("Consumer_type","NP");
        	}
        }
        mainData.put("Original_amount", erpOrder.get("totalAmount"));
        mainData.put("Discount", getFormatDiscount(erpOrder.get("totalDepositRate")));

        mainData.put("Pay_amount", actualAmount);
        
        mainData.put("Decrease_amount", "0");
        mainData.put("Costpoint", "0");
        mainData.put("Costpoint_amount", "0");
        mainData.put("Sale_ticket_time", sdf.format(tradeDateTime));
        String orderWay = ConvertUtil.getString(erpOrder.get("orderWay"));
        String shopName = ConvertUtil.getString(erpOrder.get("shopName"));
        mainData.put("Data_source", getDataSourceByName(orderWay,shopName));
        mainData.put("MachineCode", "");
        mainData.put("SaleSRtype", "3");//销售
        mainData.put("BAcode", erpOrder.get("EmployeeCode"));
        mainData.put("DepartCodeDX", erpOrder.get("CounterCode"));
        mainData.put("EmployeeCodeDX", erpOrder.get("EmployeeCode"));
        resultMap.put("MainData", mainData);
        
        List<Map<String,Object>> saleDetail = new ArrayList<Map<String,Object>>();
        for(int i=0;i<detailList.size();i++){
            Map<String,Object> detailDTO = detailList.get(i);
            Map<String,Object> saleDTO = new HashMap<String,Object>();
            saleDTO.put("TradeNoIF", erpOrder.get("orderNumber"));
            saleDTO.put("ModifyCounts", erpOrder.get("ModifiedTimes"));
            saleDTO.put("DetailType", detailDTO.get("SaleType"));
            saleDTO.put("BAcode", erpOrder.get("EmployeeCode"));
            saleDTO.put("StockType", CherryConstants.STOCK_TYPE_OUT);
            // MQ消息体定义时为Barcode，此程序其他地方都作为变量时使用barCode，作为key时使用BarCode;而电商接口中使用barcode
            saleDTO.put("Barcode", detailDTO.get("barcode"));
            saleDTO.put("Unitcode", detailDTO.get("UnitCode"));
            saleDTO.put("InventoryTypeCode", detailDTO.get("InventoryTypeCode"));
            int orderCount = CherryUtil.obj2int(detailDTO.get("orderCount"));
            int giftCount = CherryUtil.obj2int(detailDTO.get("giftCount"));
            saleDTO.put("Quantity", orderCount+giftCount);
            saleDTO.put("QuantityBefore", "");
            saleDTO.put("Price",detailDTO.get("PricePay"));
            saleDTO.put("Reason", detailDTO.get("memo"));
            saleDTO.put("Discount", detailDTO.get("discountRate"));
            
            if("2".equals(configValue)){
            	saleDTO.put("MemberCodeDetail", "");
            }else{
            	saleDTO.put("MemberCodeDetail", erpOrder.get("MemberCode"));
            }
            
            //促销活动主码 如果参与了活动要和POS系统对接，需要填写POS系统中的活动代码
//          esOrderDetailMap.put("ActivityMainCode","");
            String activityMainCode = ConvertUtil.getString(detailDTO.get("ActivityMainCode"));
            saleDTO.put("ActivityMainCode","".equals(activityMainCode) ? "" : activityMainCode);
          //活动代码
//          esOrderDetailMap.put("ActivityCode","");
            String activityCode = ConvertUtil.getString(detailDTO.get("ActivityCode"));
            saleDTO.put("ActivityCode","".equals(activityCode) ? "" : activityCode);
            
//          saleDTO.put("ActivityMainCode", "");  
//          saleDTO.put("ActivityCode", "");
            saleDTO.put("OrderID", "");
            saleDTO.put("CouponCode", "");
            saleDTO.put("IsStock", "");
            saleDTO.put("InformType", "");
            saleDTO.put("UniqueCode", "");
            saleDetail.add(saleDTO);
        }
        resultMap.put("SaleDetail", saleDetail);
        
        List<Map<String,Object>> payDetail = new ArrayList<Map<String,Object>>();
        for(int i=0;i<payDetailList.size();i++){
            Map<String,Object> detailDTO = payDetailList.get(i);
            Map<String,Object> payDTO = new HashMap<String,Object>();
            payDTO.put("TradeNoIF", erpOrder.get("orderNumber"));
            payDTO.put("ModifyCounts", erpOrder.get("ModifiedTimes"));
            payDTO.put("DetailType", "Y");
            payDTO.put("PayTypeCode", detailDTO.get("PayTypeCode"));
            payDTO.put("PayTypeID", "");
            payDTO.put("PayTypeName", "");
            
            // 总金额 2016.02.02 应付总金额需要减去运费，但是当运费为0时，则不需要
            String payAmount = getFormatDiscount(ConvertUtil.getString(detailDTO.get("PayAmount"))); // 应付金额
            if(new BigDecimal(0).compareTo(new BigDecimal(payAmount)) != 0){
            	payAmount = df.format(new BigDecimal(payAmount).subtract(new BigDecimal(deliveryCost))); // 销售的总金额需要去除运费
            }
            payDTO.put("Price", payAmount);
//            payDTO.put("Price", detailDTO.get("PayAmount"));
            
            payDTO.put("Reason", "");
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
        resultMap.put("BrandCode", erpOrder.get("BrandCode"));
        resultMap.put("TradeType", MessageConstants.MSG_CHANGESALESTATE);
        resultMap.put("TradeNoIF", erpOrder.get("orderNumber"));
        resultMap.put("BillState", erpOrder.get("BillState"));
        resultMap.put("ChangeTime", CherryUtil.getSysDateTime(CherryConstants.DATE_PATTERN_24_HOURS));
        resultMap.put("Comment", "");
        String orderWay = ConvertUtil.getString(erpOrder.get("orderWay"));
        String shopName = ConvertUtil.getString(erpOrder.get("shopName"));
        resultMap.put("DataSource", getDataSourceByName(orderWay,shopName));
        resultMap.put("ModifyCounts", erpOrder.get("ModifiedTimes"));
        resultMap.put("CounterCode", erpOrder.get("CounterCode"));
        return resultMap;
    }
    
    private int insertESOrderMain(Map<String,Object> erpOrder){
        Map<String,Object> esOrderMain = setESOrderMain(erpOrder, "insert");
        int billID = saleInfoService.insertESOrderMain(esOrderMain);
        return billID;
    }
    
    private int updateESOrderMain(Map<String,Object> erpOrder){
        Map<String,Object> esOrderMain = setESOrderMain(erpOrder, "update");
        int cnt = saleInfoService.updateESOrderMain(esOrderMain);
        return cnt;
    }
    
    /**
     * 处理产品信息及插入电商订单明细
     * @param paramMap 
     * @param detailList
     * @return 是否有在新后台【不存在】的电商订单明细商品  flase：没有  true：有
     * @throws Exception
     */
    private boolean handleAndInsertESOrderDetail(Map<String,Object> paramMap,List<Map<String,Object>> detailList) throws Exception{
        if(null == detailList || detailList.size() == 0){
            return false;
        }
        boolean result = false;
        String organizationInfoID = ConvertUtil.getString(comMap.get("BIN_OrganizationInfoID"));
        String brandInfoID = ConvertUtil.getString(comMap.get("BIN_BrandInfoID"));
        // 目前只有雅芳品牌有产品套装的情况
        boolean isSupportTZ = binOLCM14_BL.isConfigOpen("1322", organizationInfoID, brandInfoID);
        
        // 通过原始订单明细查询智能促销中是否有新增的商品数据--通过系统配置项确定是否调用该方法(电商订单匹配智能促销活动)
        boolean isSmartPromAddPro = binOLCM14_BL.isConfigOpen("1333", organizationInfoID, brandInfoID);
        if(isSmartPromAddPro){
        	List<Map<String,Object>> smartPromotionNewProList = binOTHONG01_BL.getSmartPromotionNewProList(paramMap, detailList);
        	if(!CherryBatchUtil.isBlankList(smartPromotionNewProList)){
        		detailList.addAll(smartPromotionNewProList);
        	}
        }
        
        //查询逻辑仓库
        Map<String,Object> logicparamMap = new HashMap<String,Object>();
        logicparamMap.put("BIN_BrandInfoID", brandInfoID);
        logicparamMap.put("Type", "1");//终端逻辑仓库
        List<Map<String,Object>> logicDepotList = binOLCM18_BL.getLogicDepotList(logicparamMap);
        String defaultInventoryTypeCode = ConvertUtil.getString(logicDepotList.get(0).get("LogicInventoryCode"));
        DecimalFormat df = new DecimalFormat("#0.00");
        
        String billState = ConvertUtil.getString(paramMap.get("BillState"));
        
        //让利金额
        String zdzk = ConvertUtil.getString(paramMap.get("salesOrderAgioMoney"));
        if(zdzk.equals("")){
            zdzk = "0.00";
        }
        BigDecimal zdzkBD = new BigDecimal(zdzk);
        zdzkBD = zdzkBD.setScale(2, BigDecimal.ROUND_HALF_UP); 
        if(!zdzkBD.toString().equals("0.00")){
            // 整单折扣作为一个不扣库存的虚拟促销品加入明细
            Map<String, Object> expressDetail = new HashMap<String, Object>();
            expressDetail.put("BIN_ProductVendorID", paramMap.get("ZDZK_BIN_ProductVendorID"));
            expressDetail.put("barcode", "ZDZK");
            expressDetail.put("orderCount", "1");
            if(billState.equals("0")){
                expressDetail.put("orderCount", "0");
            }
            expressDetail.put("price", zdzkBD.multiply(new BigDecimal(-1)));
            expressDetail.put("agioPrice", zdzkBD.multiply(new BigDecimal(-1)));
            expressDetail.put("amount", "0");
            expressDetail.put("discountRate", "1.0");
            expressDetail.put("memo", "");
            detailList.add(expressDetail);
        }
        
        /* 2016.02.02 废除运费加入明细的操作
        String deliveryCost = ConvertUtil.getString(paramMap.get("deliveryCost"));
        //快递费用
        if(!CherryBatchUtil.isBlankString(deliveryCost)){
        	if(new BigDecimal(0).compareTo(new BigDecimal(deliveryCost)) != 0){
        		// 快递费作为一个不扣库存的虚拟促销品加入明细
        		Map<String, Object> expressDetail = new HashMap<String, Object>();
        		expressDetail.put("BIN_ProductVendorID", paramMap.get("KDCOST_BIN_ProductVendorID"));
        		expressDetail.put("barcode", "KDCOST");
        		expressDetail.put("orderCount", "1");
        		if(billState.equals("0")){
        			expressDetail.put("orderCount", "0");
        		}
        		expressDetail.put("price", deliveryCost);
        		expressDetail.put("agioPrice", deliveryCost); 
        		expressDetail.put("amount", "0");
        		expressDetail.put("discountRate", "1.0");
        		expressDetail.put("memo", "");
        		detailList.add(expressDetail);
        	}
        }
        */
        
        // 用于插入电商订单明细表的LIST
        List<Map<String,Object>> esOrderDetail = new ArrayList<Map<String,Object>>();
        // 这两个条码用于过滤不需要判断（商品是否在新后台存在）的情况----整单折扣、快递费、虚拟套装（此三者是需要在新后台预先加入的商品）
        String unitCode = "";
        String barCode = "";
        for(int i=0;i<detailList.size();i++){
            Map<String,Object> detailDTO = detailList.get(i);
            detailDTO.put("BIN_OrganizationInfoID", organizationInfoID);
            detailDTO.put("BIN_BrandInfoID", brandInfoID);
            detailDTO.put("TradeDateTime", paramMap.get("TradeDateTime"));
            detailDTO.put("BIN_OrganizationID", paramMap.get("BIN_OrganizationID"));
            Map<String,Object> basicDetailMap = null;
            if(!isSupportTZ) {
            	basicDetailMap = binOTHONG01_BL.getESOrderDetailNeedValue(detailDTO);
            } else {
                	/**
                	 * 雅芳特殊情况：只提供platOutSkuId作为unitCode来处理
  					 * 当platOutSkuId（平台SKU商家编码）为空时，再判断platOuterId（平台宝贝商家编码）是否有对应我们系统的UnitCode
                	 * 1)对厂商编码进行处理后作为unitCode来查询，且将其他写入到明细表的OriginalCode字段中（处理后的字段）
                	 * 2)找不到对应的产品时，认为此明细为套组，
                	 * 		在新后台记录为虚拟产品XNTZ9999,对应的platOutSkuId
                	 * 		(套组code写入明细表的OriginalCode字段中，注：无论是否为套组code都将写入到OriginalCode中方便出报表)
                	 */
            		basicDetailMap = getESOrderDetailByHandleCode(detailDTO);
            		if(null == basicDetailMap || null == basicDetailMap.get("BIN_ProductVendorID")) {
            			basicDetailMap = new HashMap<String, Object>();
            			// 支持产品套装时，套装码【平台规格编码】写入OriginalCode字段
//            			basicDetailMap.put("Comment", detailDTO.get("platOutSkuId"));
            			basicDetailMap.put("SaleType", "N");
            			basicDetailMap.put("BIN_ProductVendorID", paramMap.get("XNTZ9999_BIN_ProductVendorID"));
            			basicDetailMap.put("UnitCode", "XNTZ9999");
            			basicDetailMap.put("BarCode", "XNTZ9999");
            		}
            } 
            
            detailDTO.put("SaleType", basicDetailMap.get("SaleType"));
            detailDTO.put("InventoryTypeCode", defaultInventoryTypeCode);
            // 用于插入电商订单明细表的MAP
            Map<String,Object> esOrderDetailMap = new HashMap<String,Object>();
            //电商订单主表ID
            esOrderDetailMap.put("BIN_ESOrderMainID",paramMap.get("BIN_ESOrderMainID"));
            //当为产品时，此字段为产品厂商ID，如果是促销品则为促销品产品厂商ID（根据相应code查询不到商品信息时为NULL）
            esOrderDetailMap.put("BIN_ProductVendorID",basicDetailMap.get("BIN_ProductVendorID"));
            detailDTO.put("BIN_ProductVendorID", basicDetailMap.get("BIN_ProductVendorID"));
            unitCode = ConvertUtil.getString(basicDetailMap.get("UnitCode"));
			barCode = ConvertUtil.getString(basicDetailMap.get("BarCode"));
			// 是否是需要过滤掉的商品码
			boolean isFilterCode = "XNTZ9999".equals(unitCode)
					|| "KDCOST".equals(unitCode)
					|| "ZDZK".equals(unitCode);
            if("".equals(ConvertUtil.getString(basicDetailMap.get("BIN_ProductVendorID"))) && !isFilterCode) {
            	// 当前订单的明细存在新后台没有的商品信息----此时不发送MQ
            	// 对于整单折扣与快递费都已经加入到detailList中，都作为商品明细来处理（对于订单商品在新后台不存在而做的不发送MQ的判断须过滤掉此两种特殊商品）
            	result = true;
            }
            //当前记录的序号
            esOrderDetailMap.put("DetailNo",i+1);
            if(isSupportTZ) {
            	//厂商编码,此unitCode已做头部去零处理
                esOrderDetailMap.put("UnitCode",basicDetailMap.get("UnitCode"));
                // 发送MQ时有用
                detailDTO.put("UnitCode", basicDetailMap.get("UnitCode"));
                // 此barCode为用unitCode匹配到的barCode【此处全小写是将其作为订单原始数据来处理】
                detailDTO.put("barcode", basicDetailMap.get("BarCode"));
                //产品条码
            	esOrderDetailMap.put("BarCode",basicDetailMap.get("BarCode"));
            	// 原始码【新加字段，用于保存电商接口中的原始产品码】
            	String originalCode = ConvertUtil.getString(detailDTO.get("platOutSkuId"));
            	// 先把开头的"ZP"(不区分)字符去除
            	originalCode = originalCode.replaceFirst("^(?i:ZP)*", "");
            	originalCode = originalCode.replaceFirst("^0*", "");
            	esOrderDetailMap.put("OriginalCode", originalCode);
            } else {
            	//厂商编码(默认逻辑为用barCode去取得unitCode及产品id)
                esOrderDetailMap.put("UnitCode",basicDetailMap.get("UnitCode"));
                detailDTO.put("UnitCode", basicDetailMap.get("UnitCode"));
                //产品条码
//            	esOrderDetailMap.put("BarCode",detailDTO.get("barcode"));
            	esOrderDetailMap.put("BarCode",basicDetailMap.get("barcode"));
            	
            	
            	// 原始码【新加字段，用于保存电商接口中的原始产品码】
            	String originalCode = ConvertUtil.getString(detailDTO.get("platOutSkuId"));
            	esOrderDetailMap.put("OriginalCode", originalCode);
            	// 宝贝编码 (产品对应关系使用的编码)
            	String outCode = ConvertUtil.getString(detailDTO.get("platOuterId"));
            	esOrderDetailMap.put("OutCode", outCode);
            }
            //数量 
            int orderCount = CherryUtil.obj2int(detailDTO.get("orderCount"));
            int giftCount = CherryUtil.obj2int(detailDTO.get("giftCount"));
            esOrderDetailMap.put("Quantity",orderCount + giftCount);
            //原始价格
            String price = ConvertUtil.getString(detailDTO.get("price"));
            esOrderDetailMap.put("Price",price);
            //折后单价
            String pricePay = ConvertUtil.getString(detailDTO.get("agioPrice"));
            //商品总金额
//          String amount = ConvertUtil.getString(detailDTO.get("amount"));
            //商品让利金额
            String discountAmount = getFormatDiscount(ConvertUtil.getString(detailDTO.get("discountFee")));
            if(discountAmount.equals("")){
                discountAmount = "0.00";
            }
            //销售单价
            esOrderDetailMap.put("PricePay",pricePay); 
            detailDTO.put("PricePay", esOrderDetailMap.get("PricePay"));
            //应付金额 定价*数量
            String payableAmount = df.format(new BigDecimal(price).multiply(new BigDecimal(orderCount)));
            esOrderDetailMap.put("PayableAmount",payableAmount);
            //折扣率
            esOrderDetailMap.put("Discount",getFormatDiscount(detailDTO.get("discountRate")));
            //让利金额
            esOrderDetailMap.put("AgioAmount",discountAmount);
            //实付金额 应付金额-让利金额
            String actualAmount = df.format(new BigDecimal(payableAmount).subtract(new BigDecimal(discountAmount)));
            esOrderDetailMap.put("ActualAmount",actualAmount);
            //分摊后金额
            esOrderDetailMap.put("AmountPortion","");
            //逻辑仓库代码
            esOrderDetailMap.put("InventoryTypeCode",defaultInventoryTypeCode);
            //批号或其它
            esOrderDetailMap.put("BatchCode","");
            //唯一码
            esOrderDetailMap.put("UniqueCode","");
            //销售类型 正常销售为N，促销为P。
            esOrderDetailMap.put("SaleType",basicDetailMap.get("SaleType"));
            //促销活动主码 如果参与了活动要和POS系统对接，需要填写POS系统中的活动代码
//            esOrderDetailMap.put("ActivityMainCode","");
            String activityMainCode = ConvertUtil.getString(detailDTO.get("ActivityMainCode"));
            esOrderDetailMap.put("ActivityMainCode","".equals(activityMainCode) ? "" : activityMainCode);
            //活动代码
//            esOrderDetailMap.put("ActivityCode","");
            String activityCode = ConvertUtil.getString(detailDTO.get("ActivityCode"));
            esOrderDetailMap.put("ActivityCode","".equals(activityCode) ? "" : activityCode);
            //备注
            esOrderDetailMap.put("Comment",detailDTO.get("memo"));
            
            // 产品是否存在于新后台通过相关条件查询订单明细中的产品是否在新后台存在 ( 不存在：0 、存在：1)
            if("".equals(ConvertUtil.getString(basicDetailMap.get("BIN_ProductVendorID")))){
            	esOrderDetailMap.put("IsExistsPos","0");
            }else{
            	esOrderDetailMap.put("IsExistsPos","1");
            }
            
            // 电商商品名称
            esOrderDetailMap.put("EsProductName",detailDTO.get("productName"));
            // 电商商品标题
            esOrderDetailMap.put("EsProductTitleName",detailDTO.get("platTitle"));
            
            setInsertInfoMapKey(esOrderDetailMap);
            
            if(!ConvertUtil.getString(detailDTO.get("isSmartPrmFlag")).equals("")){
            	// 智能促销返回的商品明细不写入电商明细表
            }else{
            	// 非智能促销返回的商品明细写入电商明细表
            	esOrderDetail.add(esOrderDetailMap);
            }
        }
        
        saleInfoService.insertESOrderDetail(esOrderDetail);
        return result;
    }
    
    /**
     * 插入支付方式表
     * @param paramMap
     * @param payDetailList
     * @return
     */
    private Map<String,Object> insertESOrderPayList(Map<String,Object> paramMap,List<Map<String,Object>> payDetailList){
        List<Map<String,Object>> esOrderPayList = new ArrayList<Map<String,Object>>();
        for(int i=0;i<payDetailList.size();i++){
            Map<String,Object> payDetailDTO = payDetailList.get(i);
            payDetailDTO.put("BIN_ESOrderMainID",paramMap.get("BIN_ESOrderMainID"));
            payDetailDTO.put("DetailNo",i+1);
            payDetailDTO.put("SerialNumber","");
            payDetailDTO.put("Comment","");
            setInsertInfoMapKey(payDetailDTO);
            esOrderPayList.add(payDetailDTO);
        }
        saleInfoService.insertESOrderPayList(esOrderPayList);
        return null;
    }
    
    /**
     * 访问其他系统的WebService得到订单明细
     * @param paramMap
     * @return
     * @throws UnsupportedEncodingException 
     * @throws Exception 
     */
    private Map<String, Object> getWebServiceErpOrderDetail(Map<String, Object> paramMap) throws Exception {
        String webServiceUrl = ConvertUtil.getString(paramMap.get("URL"));
        String method = ConvertUtil.getString(paramMap.get("MethodName"));
        String nick= ConvertUtil.getString(paramMap.get("AccountName"));
        String name = ConvertUtil.getString(paramMap.get("AccountPWD"));
        String orderNumber = ConvertUtil.getString(paramMap.get("orderNumber"));
        String isLog = "0";//是否获取订单操作日志 1:获取,0:不获取
        String isInvoice = "0";//是否获取发票信息 1:获取,0:不获取
        String format = "json";
        
        WebResource webResource = WebserviceClient.getWebResource(webServiceUrl);
        MultivaluedMap<String, String> queryParams = new MultivaluedMapImpl();
        queryParams.add("orderNumber", orderNumber);
        queryParams.add("name", name);
        queryParams.add("isLog", isLog);
        queryParams.add("isInvoice", isInvoice);
        queryParams.add("nick", nick);
        queryParams.add("name", name);
        queryParams.add("method", method);
        queryParams.add("format", format);
        String timestamp = getTimeStamp();
        queryParams.add("timestamp", getTimeStamp());
        String sign= getSign(nick,method,format,timestamp);
        queryParams.add("sign", sign);
        Map<String,Object> resultMap = new HashMap<String,Object>();
        String result = webResource.queryParams(queryParams).get(String.class);
        
        resultMap = CherryUtil.json2Map(result);
        resultMap.put("GetOrderDetail_result", result);
        // 调用参数，用于在报错时，给到调用参数
        resultMap.put("queryParams", "orderNumber=["+orderNumber+"],name=["+name+"],isLog=["+isLog+"],isInvoice=["+isInvoice+
        		"],nick=["+nick+"],name=["+name+"],method=["+method+"],format=["+format+"]");
        return resultMap;
    }
    
    /**
     * 从明细取出总数量
     * @param detailList
     * @return
     */
    private int getTotalQuantity(List<Map<String,Object>> detailList){
        int totalQuantity = 0;
        if(null == detailList || detailList.size() == 0){
            return totalQuantity;
        }
        for(int i=0;i<detailList.size();i++){
            Map<String,Object> tempMap = detailList.get(i);
            int quantity = CherryUtil.obj2int(tempMap.get("orderCount"));
            int giftCount = CherryUtil.obj2int(tempMap.get("giftCount"));
            totalQuantity += quantity+giftCount;
        }
        return totalQuantity;
    }
    
    /**
     * 从明细取出总数量(剔除促销品)
     * @param newDetailList
     * @return
     */
    private int getIgnorePrmTotalQuantity(List<Map<String,Object>> newDetailList){
        int totalQuantity = 0;
        if(null == newDetailList || newDetailList.size() == 0){
            return totalQuantity;
        }
        for(int i=0;i<newDetailList.size();i++){
            Map<String,Object> tempMap = newDetailList.get(i);
            
            String saleType = ConvertUtil.getString(tempMap.get("SaleType"));
            if(saleType.equals("N")){
            	int quantity = CherryUtil.obj2int(tempMap.get("orderCount"));
            	int giftCount = CherryUtil.obj2int(tempMap.get("giftCount"));
            	totalQuantity += quantity+giftCount;
            }
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
     * 设置sign（取nick、method、format、timestamp各自的BASE64加起来的MD5值）
     * @param queryParams
     * @throws Exception
     */
    private String getSign(String nick,String method,String format,String timestamp) throws Exception{
        String base64nick = Base64.encode(nick.getBytes("UTF-8"));
        String base64method = Base64.encode(method.getBytes("UTF-8"));
        String base64format = Base64.encode(format.getBytes("UTF-8"));
        String base64timeStamp = Base64.encode(timestamp.getBytes("UTF-8"));
        String sign = CherryMD5Coder.encryptMD5(base64nick+base64method+base64format+base64timeStamp);
        return sign;
    }
    
    /**
     * 订单状态： 　　　　
     * NO_PAY：未付款(1)；CUSTOMER_AUDIT：客审(2);FINANCIAL_AUDIT：财审 (2) 　　　　
     * PRINT：打印(2) ;DISTRIBUTION：配货 (2) ;WAREHOUSING：出库 (3);
     * ON_THE_WAY：途中 (3)；SETTLEMENT：结算(3) ；SUCCESS：成功 (4)；
     * CANCEL：取消 (0)；OUT_OF_STOCK：缺货(2) ；UNDEFIND：未定义状态("")；
     * 
     * 1:生成；2：付款；3：发货；4：完成；0：取消
     * 
     * 取出最终写入数据库的单据状态
     * @param orderStatus
     * @return
     */
    private String getBillStateByOrderStatus(String orderStatus) {
        // 电商定义的订单状态
        // NO_PAY：未付款
        // CUSTOMER_AUDIT：客审
        // FINANCIAL_AUDIT：财审
        // PRINT：打印
        // DISTRIBUTION：配货
        // WAREHOUSING：出库
        // ON_THE_WAY：途中
        // SETTLEMENT：结算
        // SUCCESS：成功
        // CANCEL：取消
        // OUT_OF_STOCK：缺货
        // UNDEFIND：未定义状态
        String billState = "";
        if (orderStatus.equals("NO_PAY")) {
            billState = "1";// 生成：1
        } else if (orderStatus.equals("CUSTOMER_AUDIT") || orderStatus.equals("FINANCIAL_AUDIT")
                || orderStatus.equals("PRINT") || orderStatus.equals("DISTRIBUTION")
                || orderStatus.equals("OUT_OF_STOCK")) {
            billState = "2";// 付款：2
        } else if (orderStatus.equals("WAREHOUSING") || orderStatus.equals("ON_THE_WAY")
                || orderStatus.equals("SETTLEMENT")) {
            billState = "3";// 发货：3
        } else if (orderStatus.equals("SUCCESS")) {
            billState = "4";// 完成：4
        } else if (orderStatus.equals("CANCEL")) {
            billState = "0";// 订单取消：0
        }
        return billState;
    }
    
    /**
     * 订单状态： 　　　　
     * NO_PAY：未付款(1)；CUSTOMER_AUDIT：客审(2);FINANCIAL_AUDIT：财审 (2) 　　　　
     * PRINT：打印(2) ;DISTRIBUTION：配货 (2) ;WAREHOUSING：出库 (3);
     * ON_THE_WAY：途中 (3)；SETTLEMENT：结算(3) ；SUCCESS：成功 (4)；
     * CANCEL：取消 (0)；OUT_OF_STOCK：缺货(2) ；UNDEFIND：未定义状态("")；
     * 
     * 1:生成；2：付款；3：发货；4：完成；0：取消
     * 
     * 取出最终写入数据库的单据状态
     * @param orderStatus
     * @return
     */
    private String getZHBillStateByOrderStatus(String orderStatus) {
    	// 电商定义的订单状态
    	// NO_PAY：未付款
    	// CUSTOMER_AUDIT：客审
    	// FINANCIAL_AUDIT：财审
    	// PRINT：打印
    	// DISTRIBUTION：配货
    	// WAREHOUSING：出库
    	// ON_THE_WAY：途中
    	// SETTLEMENT：结算
    	// SUCCESS：成功
    	// CANCEL：取消
    	// OUT_OF_STOCK：缺货
    	// UNDEFIND：未定义状态
    	String billState = "";
    	if (orderStatus.equals("未付款订单")) {
    		billState = "1";// 生成：1
    	} else if (orderStatus.equals("客审订单") || orderStatus.equals("财审订单")
    			|| orderStatus.equals("打印快递单") || orderStatus.equals("订单配货")
    			|| orderStatus.equals("缺货订单")) {
    		billState = "2";// 付款：2
    	} else if (orderStatus.equals("订单出库") || orderStatus.equals("途中订单")
    			|| orderStatus.equals("订单结算")) {
    		billState = "3";// 发货：3
    	} else if (orderStatus.equals("订单成功")) {
    		billState = "4";// 完成：4
    	} else if (orderStatus.equals("订单取消")) {
    		billState = "0";// 订单取消：0
    	}
    	return billState;
    }
    
    /**
     * 把中文的来源转成定义的英文名
     * 对于天猫订单，根据店铺名取不同来源
     * @param name
     * @return
     */
    private String getDataSourceByName(String name,String shopName){
        String dataSource = name;
        if(name.equals("官网订单")){
            dataSource = "OfficialWebsite";
        }else if(name.equals("淘宝订单") || name.equals("天猫订单")){
            //薇诺娜贝泰妮专卖店
            dataSource = "Tmall";
            if(shopName.equals("薇诺娜化妆品旗舰店")){
                dataSource = "TmallW";
            }
        }else if(name.equals("京东订单")){
            dataSource = "JD";
        }else if(name.equals("一号店订单")){
            dataSource = "YHD";
        }else if(name.equals("亚马逊订单")){
            dataSource = "Amazon";
        }else if(name.equals("苏宁订单")){
            dataSource = "SN";
        }else if(name.equals("拍拍订单")){
            dataSource = "PP";
        }
        return dataSource;
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
    
    private void setInsertInfoMapKey(Map<String,Object> map){
        map.put("CreatedBy","BINOTHONG02_BL");
        map.put("CreatePGM","BINOTHONG02_BL");
        map.put("UpdatedBy","BINOTHONG02_BL");
        map.put("UpdatePGM","BINOTHONG02_BL");
        map.put("createdBy","BINOTHONG02_BL");
        map.put("createPGM","BINOTHONG02_BL");
        map.put("updatedBy", "BINOTHONG02_BL");
        map.put("updatePGM", "BINOTHONG02_BL");
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
//		paraMap.put("FReason", fReason);
		binbecm01_IF.insertJobRunHistory(paraMap);
	}

}
