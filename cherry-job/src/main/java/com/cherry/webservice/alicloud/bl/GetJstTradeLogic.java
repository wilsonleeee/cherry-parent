/*	
 * @(#)GetJstTradeLogic.java     1.0 @2015-9-16		
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
package com.cherry.webservice.alicloud.bl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.core.BatchExceptionDTO;
import com.cherry.cm.core.BatchLoggerDTO;
import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryBatchException;
import com.cherry.cm.core.CherryBatchLogger;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.util.CherryBatchUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.DateUtil;
import com.cherry.webservice.alicloud.service.GetJstTradeService;
import com.cherry.webservice.common.IWebservice;
import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

/**
 * 获取聚石塔订单数据
 * 
 * @author jijw
 *
 * @version  2015-9-16
 *
 */
public class GetJstTradeLogic implements IWebservice  {
	
	/** BATCH LOGGER */
	private static CherryBatchLogger logger = new CherryBatchLogger(GetJstTradeLogic.class);

	/** 获取聚石塔订单数据Service **/
	@Resource(name = "getJstTradeService")
	private GetJstTradeService getJstTradeService;
	
	/**共通 回执信息**/
	private Map<String,Object> resultMap = new HashMap<String, Object>();
	
	
	/** 排序 **/
	private static final String SORT_ID = "jdp_modified";

	@SuppressWarnings("unchecked")
	@Override
	public Map tran_execute(Map map) throws Exception {
		
    	try{
    		// 初始化
    		init(map);
    	}catch(Exception e){
    		// 初始化失败
			BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO();
			batchExceptionDTO.setBatchName(this.getClass());
			batchExceptionDTO.setErrorCode("ECM00005");
			batchExceptionDTO.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
			batchExceptionDTO.setException(e);
			resultMap.put("ERRORCODE", "WSE9999");
			resultMap.put("ERRORMSG", "初始化处理过程中发生未知异常");
    	}
    	
    	try{
    		// 参数校验
    		resultMap = validateParam(map);
			if(null != resultMap && resultMap.size() != 0 && resultMap.containsKey("ERRORCODE")){
				return resultMap;
			}
    	}catch(Exception e){
    		BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO();
    		batchExceptionDTO.setBatchName(this.getClass());
    		batchExceptionDTO.setErrorCode("ECM00006");
    		batchExceptionDTO.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
    		batchExceptionDTO.setException(e);
			resultMap.put("ERRORCODE", "WSE9999");
			resultMap.put("ERRORMSG", "参数校验处理过程中发生未知异常");
    	}
    	try {
    		// 取得订单集合
    		Map<String,Object> resultContentMap = getJstTradeList(map);
    		resultMap.put("ResultMap", resultContentMap);
    		
    	}catch(Exception e){
    		BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO();
    		batchExceptionDTO.setBatchName(this.getClass());
    		batchExceptionDTO.setErrorCode("ECM00007");
    		batchExceptionDTO.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
    		batchExceptionDTO.setException(e);
    		
            logger.outExceptionLog(e);
            logger.outLog("WS ERROR Nike:"+ ConvertUtil.getString(map.get("Nick")),  CherryBatchConstants.LOGGER_ERROR);
            logger.outLog("WS ERROR paramData:"+ "开始时间(" + ConvertUtil.getString(map.get("StartTime")) + ")、结束时间(" + ConvertUtil.getString(map.get("EndTime")) +")、起始页(" 
            + ConvertUtil.getString(map.get("PageNo")) +")、每页条数("+ ConvertUtil.getString(map.get("PageSize")) + ")" ,  CherryBatchConstants.LOGGER_ERROR);
			resultMap.put("ERRORCODE", "WSE9999");
			resultMap.put("ERRORMSG", "获取订单集合处理过程中发生未知异常");
    	}
    	
		return resultMap;
	}
	
	/**
	 * 取得订单集合
	 * @param map
	 * @return
	 * @throws JSONException
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private Map<String,Object> getJstTradeList(Map<String,Object> map) throws JSONException,Exception{
		Map<String,Object> paramMap = new HashMap<String, Object>();
		
		String nick = ConvertUtil.getString(map.get("Nick")); // 卖家
		String startTime = ConvertUtil.getString(map.get("StartTime")); // 开始时间
		String endTime = ConvertUtil.getString(map.get("EndTime")); // 结束时间
		String searchType = ConvertUtil.getString(map.get("SearchType")); // 查询类型：crt按照订单创建时间，upd按照订单修改时间
		String pageNo = ConvertUtil.getString(map.get("PageNo")); // 起始页
		String pageSize = ConvertUtil.getString(map.get("PageSize")); // 每页条数
		
		// (1457-1) * 5 计算开始条数   (pageNo -1) * pageSize + 1
		int startInt = (Integer.parseInt(pageNo) - 1) * Integer.parseInt(pageSize) + 1;
		// (1457-0) * 5 计算结束条数   (pageNo -0) * pageSize
		int endInt = (Integer.parseInt(pageNo) - 0) * Integer.parseInt(pageSize);
		
		paramMap.put("SORT_ID", map.get("SORT_ID")); 
		paramMap.put("START", startInt); 
		paramMap.put("END", endInt); 
		
		paramMap.put("Nick", nick); 
		paramMap.put("StartTime", startTime); 
		paramMap.put("EndTime", endTime); 
		paramMap.put("SearchType", searchType); 
		paramMap.put("PageNo", pageNo); 
		paramMap.put("PageSize", pageSize); 
		
        if(Integer.parseInt(pageNo) == 1){
            //查询条件写入日志
            BatchLoggerDTO batchLoggerDTO1 = new BatchLoggerDTO();
            batchLoggerDTO1.setCode("IOT00003");
            batchLoggerDTO1.setLevel(CherryBatchConstants.LOGGER_INFO);
            batchLoggerDTO1.addParam(nick);
            batchLoggerDTO1.addParam(startTime);
            batchLoggerDTO1.addParam(endTime);
            batchLoggerDTO1.addParam(pageNo);
            batchLoggerDTO1.addParam(pageSize);
            logger.BatchLogger(batchLoggerDTO1);
        }
		
		// 订数总数
//		int orderTotalCount  = 0; 
//		orderTotalCount = getJstTradeService.getJstTradeCount(paramMap);
        
        // webservice返回resultContentMap
        Map<String,Object> resultContentMap = new HashMap<String, Object>();
		
        // 取得聚石塔订单信息
		List<Map<String, Object>> jstTradeList = getJstTradeService.getJstTradeList(paramMap);
		
		 // 定义订单集合
		List<Map<String, Object>> orderList = new ArrayList<Map<String,Object>>();
		
		for(Map<String, Object> jstTradeMap : jstTradeList){
			String tid = ConvertUtil.getString(jstTradeMap.get("tid")); // 交易订单id
			String status = ConvertUtil.getString(jstTradeMap.get("status")); // 交易状态
			String type = ConvertUtil.getString(jstTradeMap.get("type")); // 交易类型
			String seller_nick = ConvertUtil.getString(jstTradeMap.get("seller_nick")); // 卖家昵称
			String created = ConvertUtil.getString(jstTradeMap.get("created")); // 交易创建时间
			String jdp_modified = ConvertUtil.getString(jstTradeMap.get("jdp_modified")); // 数据推送的修改时间
			String jdp_response = ConvertUtil.getString(jstTradeMap.get("jdp_response")); // 交易详细信息
			
			// 定义订单主数据
			Map<String,Object> orderMainMap = new HashMap<String, Object>(); 
			
			orderMainMap.put("OrderNumber", tid); // 订单编号(Trade.tid) 
			orderMainMap.put("OldOrderNumber", tid); // 原订单编号(Trade.tid) 
			orderMainMap.put("OrderStatus", status); // 订单状态(见订单状态备注) Trade.status
			orderMainMap.put("OrderType", type); // 交易类型
			orderMainMap.put("OrderWay", "天猫订单"); // 来源方式 ，固定值“天猫订单”
			orderMainMap.put("CreatedTime", created); // 订单创建/成交时间
			orderMainMap.put("ModifiedTime", jdp_modified); // 订单推送修改时间
			
//			Map<String, Object> jdp_responseMap = CherryUtil.json2Map(jdp_response);
			Map<String, Object> jdpResponseMap = (Map<String, Object>) JSONUtil.deserialize(jdp_response);
			Map<String, Object> tradeFullinfoGetResponseMap = (Map<String, Object>)jdpResponseMap.get("trade_fullinfo_get_response");
			Map<String, Object> tradeMap = (Map<String, Object>)tradeFullinfoGetResponseMap.get("trade");
			
			
			orderMainMap.put("ShopName", ConvertUtil.getString(tradeMap.get("title"))); // 店铺名称
			orderMainMap.put("SellerNick", ConvertUtil.getString(tradeMap.get("seller_nick"))); // 卖家昵称
			orderMainMap.put("PayType", "PT"); // 付款类型  
			
			orderMainMap.put("BuyerNick", ConvertUtil.getString(tradeMap.get("buyer_nick"))); // 买家昵称
			orderMainMap.put("BuyerEmail", ConvertUtil.getString(tradeMap.get("buyer_email"))); // 买家email
			
			orderMainMap.put("ReceiverName", ConvertUtil.getString(tradeMap.get("receiver_name"))); // 收货人的姓名
			orderMainMap.put("ReceiverAddress", ConvertUtil.getString(tradeMap.get("receiver_address"))); // 收货人的姓名
			orderMainMap.put("ReceiverMobile", ConvertUtil.getString(tradeMap.get("receiver_mobile"))); // 收货人的手机号码
			orderMainMap.put("ReceiverPhone", ConvertUtil.getString(tradeMap.get("receiver_phone"))); // 收货人的电话号码
			orderMainMap.put("ReceiverCity", ConvertUtil.getString(tradeMap.get("receiver_city"))); // 收货人的所在城市
			orderMainMap.put("ReceiveDistrict", ConvertUtil.getString(tradeMap.get("receiver_district"))); // 收货人的所在地区
			
			orderMainMap.put("BuyerMessage", ConvertUtil.getString(tradeMap.get("buyer_message"))); // 买家留言
			orderMainMap.put("BuyerMemo", ConvertUtil.getString(tradeMap.get("buyer_memo"))); // 买家备注
			orderMainMap.put("SellerMemo", ConvertUtil.getString(tradeMap.get("seller_memo"))); // 卖家备注
			
			orderMainMap.put("Payment", ConvertUtil.getString(tradeMap.get("payment"))); // 实付金额
			orderMainMap.put("PostFee", ConvertUtil.getString(tradeMap.get("post_fee"))); // 快递费用
			orderMainMap.put("PointFee", ConvertUtil.getString(tradeMap.get("point_fee"))); // 积分抵扣 (Trade.point_fee买家使用积分,下单时生成，且一直不变。格式:100;单位:个.)
			
//			orderMainMap.put("num", ConvertUtil.getString(tradeMap.get("num"))); // 商品购买数量。取值范围：大于零的整数,对于一个trade对应多个order的时候（一笔主订单，对应多笔子订单），num=0，num是一个跟商品关联的属性，一笔订单对应多比子订单的时候，主订单上的num无意义。
			orderMainMap.put("PayTime", ConvertUtil.getString(tradeMap.get("pay_time"))); // 付款时间。格式:yyyy-MM-dd HH:mm:ss。
			
			Map<String, Object> orders = (Map<String, Object>)tradeMap.get("orders");
			List<Map<String, Object>> ordersList = (List<Map<String, Object>>)orders.get("order");
			
			List<Map<String, Object>> orderDetailList = new ArrayList<Map<String,Object>>(); // 定义订单集合
			for(Map<String, Object> ordersMap : ordersList){
				
				// 定义订单明细数据 
				Map<String,Object> orderDetailMainMap = new HashMap<String, Object>(); 
				orderDetailMainMap.put("ItemOrderNumber", ConvertUtil.getString(ordersMap.get("oid"))); // 子订单编号
				orderDetailMainMap.put("OrderNumber", tid); // 订单编号
				orderDetailMainMap.put("NumIID", ConvertUtil.getString(ordersMap.get("num_iid"))); // 商品数据ID
				orderDetailMainMap.put("OuterIID", ConvertUtil.getString(ordersMap.get("outer_iid"))); // 商家自定义的商品Item的id
				orderDetailMainMap.put("ItemCount", ConvertUtil.getString(ordersMap.get("num"))); // 购买数量
				orderDetailMainMap.put("Price", ConvertUtil.getString(ordersMap.get("price"))); // 商品价格（精确两位小数）（Trade. Order. price）
				orderDetailMainMap.put("TotalFee", ConvertUtil.getString(ordersMap.get("total_fee"))); // 应付金额（商品价格 * 商品数量 + 手工调整金额 - 子订单级订单优惠金额）
				orderDetailMainMap.put("Payment", ConvertUtil.getString(ordersMap.get("payment"))); // 实付金额（Trade. Order. payment）payment = price*num+adjust_fee-discount_fee
				orderDetailMainMap.put("DiscountFee", ConvertUtil.getString(ordersMap.get("discount_fee"))); // 优惠金额
				orderDetailMainMap.put("AdjustFee", ConvertUtil.getString(ordersMap.get("adjust_fee"))); // 手工调整金额
				orderDetailMainMap.put("DivideOrderFee", ConvertUtil.getString(ordersMap.get("divide_order_fee"))); // 分摊后实付金额
				orderDetailMainMap.put("PartMjzDiscount", ConvertUtil.getString(ordersMap.get("part_mjz_discount"))); // 优惠分摊（Trade. Order. part_mjz_discount）
				orderDetailMainMap.put("InvoiceNo", ConvertUtil.getString(ordersMap.get("invoice_no"))); // 运单号（Trade. Order. invoice_no）
				orderDetailMainMap.put("LogisticsCompany", ConvertUtil.getString(ordersMap.get("logistics_company"))); // 快递物流公司（Trade. Order. logisticsCompany）
				orderDetailMainMap.put("RefundStatus", ConvertUtil.getString(ordersMap.get("refund_status"))); // 退款状态（见退款备注）（Trade. Order. refundStatus）
				orderDetailMainMap.put("OrderFrom", ConvertUtil.getString(ordersMap.get("order_from"))); // 子订单来源（Trade. Order. order_from）
				
				orderDetailList.add(orderDetailMainMap);
			}
			
			orderMainMap.put("OrderDetailList", orderDetailList); // 订单明细集合
			orderList.add(orderMainMap);
			
		}
		resultContentMap.put("SellerNick", nick); // 卖家昵称
		resultContentMap.put("TotalCount", 0); // 订单总数，暂时不使用
		resultContentMap.put("OrderList", orderList); // 订单集合
		
		return resultContentMap;
	}
	
	/**
	 * 校验参数
	 * @param map
	 * @return
	 */
	private Map<String,Object> validateParam(Map<String,Object> map){
		
		// 判断必填字段是否为空
		if (CherryBatchUtil.isBlankString(ConvertUtil.getString(map.get("Nick")))) {
			resultMap.put("ERRORCODE", "WSE0063");
			resultMap.put("ERRORMSG", "Nick卖家不能为空");
			return resultMap;
		}
		// 判断必填字段是否为空
		if (CherryBatchUtil.isBlankString(ConvertUtil.getString(map.get("DataSource")))) {
			resultMap.put("ERRORCODE", "WSE0063");
			resultMap.put("ERRORMSG", "DataSource数据来源不能为空");
			return resultMap;
		}
		// 判断必填字段是否为空
		if (CherryBatchUtil.isBlankString(ConvertUtil.getString(map.get("SearchType")))) {
			resultMap.put("ERRORCODE", "WSE0063");
			resultMap.put("ERRORMSG", "SearchType查询类型不能为空");
			return resultMap;
		}
		
		String startTime = ConvertUtil.getString(map.get("StartTime"));
		String endTime = ConvertUtil.getString(map.get("EndTime"));
		
		// 判断必填字段是否为空
		if (CherryBatchUtil.isBlankString(startTime)) {
			resultMap.put("ERRORCODE", "WSE0063");
			resultMap.put("ERRORMSG", "StartTime开始时间不能为空");
			return resultMap;
		}else{
			if(!(CherryChecker.checkDate(startTime,DateUtil.DATETIME_PATTERN) || CherryChecker.checkDate(startTime,DateUtil.DATETIME2_PATTERN) )){
				// 校验时间格式
				resultMap.put("ERRORCODE", "WSE0040");
				resultMap.put("ERRORMSG", "StartTime时间格式不正确(请确认为[yyyy-MM-dd HH:mm:ss]或[yyyy-MM-dd HH:mm:ss.S])");
				return resultMap;
			}
		}
		
		// 判断必填字段是否为空
		if (CherryBatchUtil.isBlankString(endTime)) {
			resultMap.put("ERRORCODE", "WSE0063");
			resultMap.put("ERRORMSG", "EndTime开始时间不能为空");
			return resultMap;
		}else{
			if(!(CherryChecker.checkDate(endTime,DateUtil.DATETIME_PATTERN) || CherryChecker.checkDate(endTime,DateUtil.DATETIME2_PATTERN) )){
				// 校验时间格式
				resultMap.put("ERRORCODE", "WSE0040");
				resultMap.put("ERRORMSG", "StartTime时间格式不正确(请确认为[yyyy-MM-dd HH:mm:ss]或[yyyy-MM-dd HH:mm:ss.S])");
				return resultMap;
			}
		}
		
		if (!CherryBatchUtil.isBlankString(startTime) && !CherryBatchUtil.isBlankString(endTime)){
			// 开始日期在结束日期之后
			if(CherryChecker.compareDate(startTime, endTime) > 0){
				resultMap.put("ERRORCODE", "WSE0074");
				resultMap.put("ERRORMSG", "StartTime时间不能大于EndTime");
				return resultMap;
			}
				
		}
		
		String pageNo = ConvertUtil.getString(map.get("PageNo"));
		// 判断必填字段是否为空
		if (CherryBatchUtil.isBlankString(pageNo)) {
			resultMap.put("ERRORCODE", "WSE0063");
			resultMap.put("ERRORMSG", "PageNo起始不能为空");
			return resultMap;
		}else {
			// 校验是否为正整数
			if (!CherryChecker.isNumeric(pageNo)) {
				resultMap.put("ERRORCODE", "WSE0075");
				resultMap.put("ERRORMSG", "PageNo必须是正整数");
				return resultMap;
			}
			
		}
		
		String pageSize = ConvertUtil.getString(map.get("PageSize"));
		// 判断必填字段是否为空
		if (CherryBatchUtil.isBlankString(pageSize)) {
			resultMap.put("ERRORCODE", "WSE0063");
			resultMap.put("ERRORMSG", "PageSize每页条数不能为空");
			return resultMap;
		}else{
			// 校验是否为正整数
			if (!CherryChecker.isNumeric(pageSize)) {
				resultMap.put("ERRORCODE", "WSE0075");
				resultMap.put("ERRORMSG", "PageSize必须是正整数");
				return resultMap;
			}
		}
		return resultMap;
	}
	
    /**
	 * init
	 * @param map
	 */
	private void init(Map<String, Object> map)throws CherryBatchException, Exception{
		
		// 默认以jdp_modified进行排序
		map.put("SORT_ID", SORT_ID);
	}

}
