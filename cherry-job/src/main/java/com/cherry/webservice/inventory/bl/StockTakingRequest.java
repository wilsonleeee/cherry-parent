package com.cherry.webservice.inventory.bl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.util.CherryBatchUtil;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.mq.mes.common.MessageConstants;
import com.cherry.webservice.common.IWebservice;
import com.mqhelper.interfaces.MQHelper_IF;

public class StockTakingRequest implements IWebservice{

	private static Logger logger = LoggerFactory.getLogger(StockTakingRequest.class.getName());
	
	/**MQHelper模块接口*/
    @Resource(name="mqHelperImpl")
    private MQHelper_IF mqHelperImpl;
	
    //盘点申请信息MQ消息体MainData的顺序
	private final static String[] MQ_DB_MAINLINEKEY_ARR = new String[]{
		"BrandCode","TradeNoIF","ModifyCounts","TradeType","SubType"
		,"CounterCode","BAcode","InventoryTypeCode","TotalQuantity","TotalAmount"
		,"Reason","TradeDate","TradeTime","StockReason"
	};
	
	//盘点申请信息MQ消息体DetailData信息
	private final static String[] MQ_DB_DETAILLINEKEY_ARR = new String[]{
		"BAcode","InventoryTypeCode","Unitcode","Barcode","BookQuantity",
		"GainQuantity","Price","Reason","HandleType","DetailType",
		"ProductId"
	};
	
	@Override
	public Map tran_execute(Map map) throws Exception {
		Map<String,Object> retMap = new HashMap<String, Object>();
		
		Map<String, Object> paramMap = new HashMap<String, Object>();
		
		Map<String, Object> mainData = new HashMap<String, Object>();
		mainData.put("BrandCode", map.get("BrandCode"));
		
		List<Map<String, Object>> deliveryBillDetail = new ArrayList<Map<String, Object>>();
		
		try{
			
			//业务验证
			String tradeType = ConvertUtil.getString(map.get("TradeType"));
			if(CherryBatchUtil.isBlankString(tradeType) ||(null != tradeType && !"StockTakingRequest".equals(tradeType))) {
				retMap.put("ERRORCODE", "WSE9994");
				retMap.put("ERRORMSG", "参数TradeType错误,TradeType=" + tradeType);
				return retMap;
			}
			mainData.put("TradeType", "CR");
			
			//单据号验证
			String billCode = ConvertUtil.getString(map.get("BillCode"));
			if( null == billCode || CherryBatchUtil.isBlankString(billCode) ) {
				retMap.put("ERRORCODE", "EKC02001");
				retMap.put("ERRORMSG", "参数BillCode错误,BillCode=" + billCode);
				return retMap;
			}
			mainData.put("TradeNoIF", map.get("BillCode"));
			
			//修改回数
			mainData.put("ModifyCounts", null);
			
			//商品盘点类型->子类型
			String stockTakingType = ConvertUtil.getString(map.get("StockTakingType"));
			if( null == stockTakingType || CherryBatchUtil.isBlankString(stockTakingType) ) {
				retMap.put("ERRORCODE", "EKC02002");
				retMap.put("ERRORMSG", "参数StockTakingType错误,StockTakingType=" + stockTakingType);
				return retMap;
			}
			mainData.put("SubType", map.get("StockTakingType"));
			
			//员工工号
			String employeeCode = ConvertUtil.getString(map.get("EmployeeCode"));
			if( null == employeeCode || CherryBatchUtil.isBlankString(employeeCode) ) {
				retMap.put("ERRORCODE", "EKC02004");
				retMap.put("ERRORMSG", "参数EmployeeCode错误,EmployeeCode=" + employeeCode);
				return retMap;
			}
			mainData.put("BAcode", map.get("EmployeeCode"));
			
			//盘点业务部门代号->柜台编号
			String departCode = ConvertUtil.getString(map.get("DepartCode"));
			if( null == departCode || CherryBatchUtil.isBlankString(departCode) ) {
				retMap.put("ERRORCODE", "EKC02003");
				retMap.put("ERRORMSG", "参数DepartCode错误,DepartCode=" + departCode);
				return retMap;
			}
			mainData.put("CounterCode", map.get("DepartCode"));
			
			//逻辑仓库代码
			String logicDepotCode = ConvertUtil.getString(map.get("LogicDepotCode"));
			if( null == logicDepotCode || CherryBatchUtil.isBlankString(logicDepotCode) ) {
				retMap.put("ERRORCODE", "EKC02005");
				retMap.put("ERRORMSG", "参数LogicDepotCode错误,LogicDepotCode=" + billCode);
				return retMap;
			}
			mainData.put("InventoryTypeCode", map.get("LogicDepotCode"));
			
			//总数量
			String totalQuantity = ConvertUtil.getString(map.get("TotalQuantity"));
			if( null == totalQuantity ) {
				retMap.put("ERRORCODE", "EKC02006");
				retMap.put("ERRORMSG", "参数TotalQuantity错误,TotalQuantity=" + totalQuantity);
				return retMap;
			}else{
				try{ 
					CherryBatchUtil.string2double(totalQuantity);
					
				}catch(Exception e){
					retMap.put("ERRORCODE", "EKC02006");
					retMap.put("ERRORMSG", "参数TotalQuantity错误,TotalQuantity=" + totalQuantity);
					return retMap;
				}
			}
			mainData.put("TotalQuantity", map.get("TotalQuantity"));
			
			//总金额
			String totalAmount = ConvertUtil.getString(map.get("TotalAmount"));
			if( null == totalAmount ) {
				retMap.put("ERRORCODE", "EKC02007");
				retMap.put("ERRORMSG", "参数TotalAmount错误,TotalAmount=" + totalAmount);
				return retMap;
			}else{
				try{ 
					CherryBatchUtil.string2double(totalAmount);
					
				}catch(Exception e){
					retMap.put("ERRORCODE", "EKC02007");
					retMap.put("ERRORMSG", "参数TotalAmount错误,TotalAmount=" + totalAmount);
					return retMap;
				}
			}
			mainData.put("TotalAmount", map.get("TotalAmount"));
			
			//理由
			mainData.put("Reason", map.get("Reason"));
			
			//申请日期
			String tradeDate = ConvertUtil.getString(map.get("TradeDate"));
			if( null == tradeDate || CherryBatchUtil.isBlankString(tradeDate) ) {
				retMap.put("ERRORCODE", "EKC02008");
				retMap.put("ERRORMSG", "参数TradeDate错误,TradeDate=" + tradeDate);
				return retMap;
			}
			mainData.put("TradeDate", map.get("TradeDate"));
			
			//申请时间
			String tradeTime = ConvertUtil.getString(map.get("TradeTime"));
			if( null == tradeTime || CherryBatchUtil.isBlankString(tradeTime) ) {
				retMap.put("ERRORCODE", "EKC02009");
				retMap.put("ERRORMSG", "参数TradeTime错误,TradeTime=" + tradeTime);
				return retMap;
			}
			mainData.put("TradeTime", map.get("TradeTime"));
			
			//盘点原因CODE
			mainData.put("StockReason", map.get("Comment"));
			
			paramMap.put("MainData", mainData);
			
			//详细列表
			String detailListStr = ConvertUtil.getString(map.get("DetailList"));
			List<Map<String, Object>> detailList = CherryUtil.json2ArryList(detailListStr);
			Map<String, Object> deliveryBill = null;
			for(int i=0;i < detailList.size();i++){
				
				Map<String ,Object> detailMap = detailList.get(i);
				
				deliveryBill = new HashMap<String, Object>();
				
				deliveryBill.put("BAcode", map.get("EmployeeCode"));
				deliveryBill.put("InventoryTypeCode", map.get("LogicDepotCode"));
				
				//商品编码
				String unitcode = (String) detailMap.get("Unitcode");
				if( null == unitcode || CherryBatchUtil.isBlankString(unitcode) ) {
					retMap.put("ERRORCODE", "EKC02010");
					retMap.put("ERRORMSG", "参数Unitcode错误,Unitcode=" + unitcode);
					return retMap;
				}
				deliveryBill.put("Unitcode", unitcode);
				
				//商品条码
				String barcode = (String) detailMap.get("Barcode");
				if( null == barcode || CherryBatchUtil.isBlankString(barcode) ) {
					retMap.put("ERRORCODE", "EKC02011");
					retMap.put("ERRORMSG", "参数Barcode错误,Barcode=" + barcode);
					return retMap;
				}
				deliveryBill.put("Barcode", barcode);
				
				//账面数量
				Integer bookQuantity = null;
				try {
					bookQuantity = (Integer) detailMap.get("BookQuantity");
					if(bookQuantity ==null ){
						retMap.put("ERRORCODE", "EKC02012");
						retMap.put("ERRORMSG", "参数BookQuantity错误,BookQuantity必须为整数");
						return retMap;
					}
				} catch (Exception e) {
					retMap.put("ERRORCODE", "EKC02012");
					retMap.put("ERRORMSG", "参数BookQuantity错误,BookQuantity必须为整数");
					return retMap;
				}
				deliveryBill.put("BookQuantity", bookQuantity);
				
				//盘差
				Integer gainQuantity = null;
				try {
					gainQuantity = (Integer) detailMap.get("GainQuantity");
					if(gainQuantity ==null ){
						retMap.put("ERRORCODE", "EKC02013");
						retMap.put("ERRORMSG", "参数GainQuantity错误,GainQuantity必须为整数");
						return retMap;
					}
				} catch (Exception e) {
					retMap.put("ERRORCODE", "EKC02013");
					retMap.put("ERRORMSG", "参数GainQuantity错误,GainQuantity必须为整数");
					return retMap;
				}
				deliveryBill.put("GainQuantity", gainQuantity);
				
				//价格
				Double price = null;
				try {
					price = (Double) detailMap.get("Price");
					if(price ==null ){
						retMap.put("ERRORCODE", "EKC02014");
						retMap.put("ERRORMSG", "参数Price错误,Price=" + price);
						return retMap;
					}
				} catch (Exception e) {
					retMap.put("ERRORCODE", "EKC02014");
					retMap.put("ERRORMSG", "参数Price错误,Price=" + price);
					return retMap;
				}
				deliveryBill.put("Price", price);
				
				deliveryBill.put("Reason", detailMap.get("Comment"));
				
				//盘点处理方式
				String checkType = (String) detailMap.get("CheckType");
				if(null == checkType || CherryBatchUtil.isBlankString(checkType) ){
					retMap.put("ERRORCODE", "EKC02015");
					retMap.put("ERRORMSG", "参数CheckType错误,CheckType=" + checkType);
				}
				deliveryBill.put("HandleType", checkType);
				
				//预留数据
				deliveryBill.put("DetailType", null);
				deliveryBill.put("ProductId", null);
				
				deliveryBillDetail.add(deliveryBill);
			}
			
			paramMap.put("DeliveryBillDetail", deliveryBillDetail);
			
			this.sendMQ_DB(paramMap);
			
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			retMap.put("ERRORCODE", "WSE9999");
			retMap.put("ERRORMSG", "处理过程中发生未知异常");
			return retMap;
		}
		
		retMap.put("ERRORCODE", "0");
		retMap.put("ERRORMSG", "正常返回");
		return retMap;
	}
	
	/**
	 * 发送盘点申请信息MQ
	 * @param paramSendMQ
	 * @return
	 * @throws Exception
	 */
	public boolean sendMQ_DB(Map<String ,Object> paramSendMQ) throws Exception{
		
		String queueName = "posToCherryMsgQueue";
		
		//主数据
		Map<String,Object> mainDataMap = (Map<String, Object>) paramSendMQ.get("MainData");
		Map<String, Object> mainLineMap = new HashMap<String,Object>();
		for(int i=0;i< MQ_DB_MAINLINEKEY_ARR.length;i++){
			String keyName = MQ_DB_MAINLINEKEY_ARR[i];
			mainLineMap.put(keyName, mainDataMap.get(keyName));
		}
		
		//JMSXGroupID 品牌编号+柜台号
        String groupName = ConvertUtil.getString(mainDataMap.get("BrandCode"))+ConvertUtil.getString(mainDataMap.get("CounterCode"));
        
        //明细数据
        List<Map<String,Object>> deliveryBillDetailList = (List<Map<String, Object>>) paramSendMQ.get("DeliveryBillDetail");
        List<Map<String,Object>> detailLineList = new ArrayList<Map<String,Object>>();
        for(int i=0;i<deliveryBillDetailList.size();i++){
        	Map<String,Object> tempMap = deliveryBillDetailList.get(i);
        	Map<String,Object> detailDataMap = new HashMap<String,Object>();
        	for(int j=0; j<MQ_DB_DETAILLINEKEY_ARR.length;j++){
        		String keyName = MQ_DB_DETAILLINEKEY_ARR[j];
        		detailDataMap.put( keyName, tempMap.get(keyName) );
        	}
        	detailLineList.add(detailDataMap);
        }
        
        Map<String,Object> dataLineMap = new HashMap<String,Object>();
        dataLineMap.put("MainData", mainLineMap);
        dataLineMap.put("DetailDataDTOList", detailLineList);
        
        Map<String,Object> mqMessageParam = new HashMap<String,Object>();
        mqMessageParam.put("Version", MessageConstants.MESSAGE_VERSION);
        mqMessageParam.put("Type", MessageConstants.MESSAGE_TYPE_SALE_STOCK );
        mqMessageParam.put("DataLine", CherryUtil.map2Json(dataLineMap));
        mqMessageParam.put("DataType", MessageConstants.DATATYPE_APPLICATION_JSON);
        
        //设定MQ_Log日志需要的数据
        Map<String, Object> mqLog = new HashMap<String,Object>();
        mqLog.put("BillType",MessageConstants.MSG_StocktakeRequest);
        mqLog.put("BillCode", mainDataMap.get("TradeNoIF"));
        mqLog.put("CounterCode", mainDataMap.get("CounterCode"));
        mqLog.put("Txddate", ConvertUtil.getString(mainDataMap.get("TradeDate")).replaceAll("-", "").substring(2));
        mqLog.put("Txdtime", ConvertUtil.getString(mainDataMap.get("TradeTime")).replaceAll(":", ""));
        mqLog.put("Source", "WS");
        mqLog.put("SendOrRece", "S");
        mqLog.put("ModifyCounts", 0);
        
        mqMessageParam.put("Mq_Log", mqLog);
        
        boolean sendFlag = mqHelperImpl.sendData(mqMessageParam, queueName, groupName);
		return sendFlag;
	}
	

}
