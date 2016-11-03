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
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.batcmbussiness.interfaces.BINBECM01_IF;
import com.cherry.cm.cmbussiness.bl.BINOLCM18_BL;
import com.cherry.cm.core.BatchExceptionDTO;
import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryBatchException;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.dr.cmbussiness.util.DoubleUtil;
import com.cherry.mq.mes.common.MessageConstants;
import com.cherry.webservice.sale.bl.SaleInfoLogic;
import com.cherry.webserviceout.alicloud.jstTrade.service.BINBAT124_Service;


/**
 *
 * 天猫销售订单转MQ（销售）
 * 
 *
 * @author hxhao
 *
 * @version  2016-10-27
 */
public class BINBAT124_BL {
	
	private static Logger logger = LoggerFactory.getLogger(BINBAT124_BL.class.getName());
	
    @Resource(name="saleInfoLogic")
    private SaleInfoLogic saleInfoLogic;
	
	@Resource
	private BINBAT124_Service binbat124_Service;
	
	/** JOB执行相关共通 IF */
	@Resource(name="binbecm01_IF")
	private BINBECM01_IF binbecm01_IF;
	
	@Resource(name="binOLCM18_BL")
    private BINOLCM18_BL binOLCM18_BL;
	
	/** 每批次(页)处理数量 10 */
	private final int BATCH_SIZE = 20000;
	
	
	/**
	 * batch处理
	 * 
	 * @param 无
	 * 
	 * @return Map
	 * @throws CherryBatchException
	 */
	public int tran_batchBat124(Map<String, Object> paraMap) throws Exception {

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
			return CherryBatchConstants.BATCH_WARNING;
		}
		try{
			// 主程序调用
			 sendTradeByMQ(paraMap);
		}catch(Exception e){
			logger.error("*************************************主程序调用异常:", e);
			return CherryBatchConstants.BATCH_WARNING;
		}
		return CherryBatchConstants.BATCH_SUCCESS;
	}
	
	/**
	 * 主程序
	 * 
	 * @param paramMap
	 * @return
	 * @throws CherryBatchException
	 */
	private synchronized void sendTradeByMQ(Map<String, Object> paramMap)throws Exception {
		while(true) {
			//1.从电商订单主表抽取待转换的数据
			List<Map<String,Object>> order_list = binbat124_Service.ESOrderMain(paramMap);
			if (null == order_list || order_list.isEmpty()) {
				break;
			}
			for(Map<String,Object> orderInfo:order_list){
				orderInfo.put("brandCode", paramMap.get("brandCode"));
				//主单ID
				String ESOrderMainID = ConvertUtil.getString(orderInfo
						.get("ESOrderMainID"));
				try {
					//2-1.通过主表ID从订单明细表获取数据
					List<Map<String, Object>> detail_list = binbat124_Service
							.getESOrderDetail(orderInfo);
					//2-2-1 通过明细的OutCode到产品表查询产品信息（对应产品表的unitcode）
					if (detail_list == null || detail_list.size() == 0) {//没有找到相对应的产品信息，直接更新主表数据
						continue;
					} else {
						List<Map<String, Object>> allDetail = new ArrayList<Map<String, Object>>();
						boolean isExec = true;
						//对不能整除的明细进行拆分处理
						for (Map<String, Object> detail_info : detail_list) {
							// 获取产品信息
							Map<String, Object> prtInfo = binbat124_Service.getPrtInfo(detail_info);
							if (null == prtInfo || prtInfo.isEmpty()) {
								Map<String, Object> update_map = new HashMap<String, Object>();
								update_map.put("ESOrderMainID", ESOrderMainID);
								update_map.put("convertFlag", "2");
								update_map.put("convertErrMsg", "查询不到产品信息");
								binbat124_Service.updateESOrderState(update_map);
								binbat124_Service.manualCommit();
								isExec = false;
								break;
							}
							detail_info.putAll(prtInfo);
							this.convertDetailByCalculate(detail_info, allDetail);
						}
						if (!isExec) {
							continue;
						}
						//检查销售中是否存在快递费
						double expressCost = Double.parseDouble(orderInfo.get("expressCost").toString());
						if (expressCost != 0) {//存在快递费,添加一条快递费的明细
							Map<String, Object> expressInfo = new HashMap<String, Object>();
							expressInfo.put("saleType", "P");
							expressInfo.put("quantity", 1);
							expressInfo.put("UnitCode", "KDCOST");
							expressInfo.put("BarCode", "KDCOST");
							expressInfo.put("discountRate", getFormatDiscount("1"));
							expressInfo.put("salePrice", expressCost);
							expressInfo.put("price", expressCost);
							expressInfo.put("actualAmount", expressCost);
							allDetail.add(expressInfo);
						}
						//获取发送MQ的销售数据与发送MQ操作
						Map<String, Object> paramSendMQ = getMQData_NS(orderInfo,
								allDetail, paramMap);
						saleInfoLogic.sendMQ_NS(paramSendMQ);
						Map<String, Object> update_map = new HashMap<String, Object>();
						update_map.put("ESOrderMainID", ESOrderMainID);
						update_map.put("convertFlag", "1");
						binbat124_Service.updateESOrderState(update_map);
						binbat124_Service.manualCommit();
					}
				} catch (Exception e) {
					try {
						binbat124_Service.manualRollback();
					} catch (Exception ex) {
						
					}
					logger.error(e.getMessage(),e);
					Map<String, Object> update_map = new HashMap<String, Object>();
					update_map.put("ESOrderMainID", ESOrderMainID);
					update_map.put("convertFlag", "2");
					update_map.put("convertErrMsg", "程序发生异常");
					try {
						binbat124_Service.updateESOrderState(update_map);
						binbat124_Service.manualCommit();
					} catch (Exception ex) {
						logger.error("*******************更新转换标志发生异常：" + e.getMessage(),e);
						try {
							binbat124_Service.manualRollback();
						} catch (Exception exp) {
							
						}
					}
				}
			}
			if(order_list.size() == BATCH_SIZE){
				paramMap.put("startId", order_list.get(order_list.size()-1).get("ESOrderMainID"));
			} else {
				break;
			}
		}
	}

	
	private Map<String,Object> getMQData_NS(Map<String,Object> orderMainMap,List<Map<String,Object>> orderDetailList, Map<String,Object> paramMap) throws ParseException{
		double totalAmount=0;
		int totalQuantity=0;
		//通过明细计算出主单的总金额和总数量（促销类型不统计数量）
		for(Map<String,Object> detail_info:orderDetailList){
			String saleType=ConvertUtil.getString(detail_info.get("saleType"));
			int quantity=Integer.parseInt(ConvertUtil.getString(detail_info.get("quantity")));
			double actualAmout = Double.parseDouble(String.valueOf(detail_info.get("actualAmount")));
			totalAmount = DoubleUtil.add(totalAmount, actualAmout);
			if("N".equals(saleType)){
				//统计数量与价格
				totalQuantity += quantity;
			}
		}
    	Map<String,Object> resultMap = new HashMap<String,Object>();
        Map<String,Object> mainData = new HashMap<String,Object>();
        mainData.put("BrandCode", orderMainMap.get("brandCode"));
        mainData.put("TradeNoIF", orderMainMap.get("billcode"));
        mainData.put("ModifyCounts", 0);
        mainData.put("CounterCode", orderMainMap.get("counterCode"));
        mainData.put("RelevantCounterCode", "");
        mainData.put("TotalQuantity", totalQuantity); 
        mainData.put("TotalAmount", totalAmount);
        mainData.put("TradeType", MessageConstants.BUSINESS_TYPE_NS);
        mainData.put("SubType", "");
        mainData.put("RelevantNo", "");
        mainData.put("Reason", orderMainMap.get("Comments"));
        String orderDate = ConvertUtil.getString(orderMainMap.get("billCreateTime"));
        mainData.put("TradeDate", orderDate.substring(0, 10));
        mainData.put("TradeTime", orderDate.substring(11));
        mainData.put("TotalAmountBefore", "");
        mainData.put("TotalAmountAfter", "");
        mainData.put("MemberCode", orderMainMap.get("memberCode")); 
        mainData.put("Counter_ticket_code", "");
        mainData.put("Counter_ticket_code_pre", "");
        mainData.put("Ticket_type", "NE");
        mainData.put("Sale_status", "OK");
        //消费者类型 NP：普通个人  NG：普通团购  MP：会员个人  MG：会员团购。
        mainData.put("Consumer_type",orderMainMap.get("consumerType"));
        mainData.put("Member_level", "");
        mainData.put("Original_amount", totalAmount); // 待定
        mainData.put("Discount", getFormatDiscount("1"));
        mainData.put("Pay_amount", totalAmount);
        mainData.put("Decrease_amount", "0"); 
        mainData.put("Costpoint", "0");
        mainData.put("Costpoint_amount", "0");
        mainData.put("Sale_ticket_time", orderDate);
        mainData.put("Data_source", orderMainMap.get("dataSource"));
        mainData.put("MachineCode", "");
        mainData.put("SaleSRtype", "3");//销售
        mainData.put("BAcode", orderMainMap.get("employeeCode"));
        mainData.put("DepartCodeDX", orderMainMap.get("counterCode"));
        mainData.put("EmployeeCodeDX", orderMainMap.get("employeeCode"));
        resultMap.put("MainData", mainData);
        
        List<Map<String,Object>> saleDetail = new ArrayList<Map<String,Object>>();
        for(int i=0;i<orderDetailList.size();i++){
            Map<String,Object> orderDetail = orderDetailList.get(i);
            Map<String,Object> saleDTO = new HashMap<String,Object>();
            saleDTO.put("TradeNoIF", orderMainMap.get("billcode"));
            saleDTO.put("ModifyCounts", 0);
            saleDTO.put("DetailType", orderDetail.get("saleType"));
            saleDTO.put("BAcode", orderMainMap.get("employeeCode"));
            saleDTO.put("StockType", CherryConstants.STOCK_TYPE_OUT);
            // MQ消息体定义时为Barcode，此程序其他地方都作为变量时使用barCode，作为key时使用BarCode;而电商接口中使用barcode
            saleDTO.put("Barcode", orderDetail.get("BarCode"));
            saleDTO.put("Unitcode", orderDetail.get("UnitCode"));
            saleDTO.put("InventoryTypeCode", ConvertUtil.getString(paramMap.get("inventoryTypeCode")));
            saleDTO.put("Quantity", orderDetail.get("quantity"));
            saleDTO.put("QuantityBefore", "");
            saleDTO.put("Price",orderDetail.get("price"));
            saleDTO.put("Reason", orderDetail.get("memo")); 
            saleDTO.put("Discount", orderDetail.get("discountRate"));
            saleDTO.put("MemberCodeDetail", orderMainMap.get("memberCode"));
            saleDTO.put("ActivityMainCode",  ConvertUtil.getString(orderDetail.get("activityMainCode")));
            saleDTO.put("ActivityCode", ConvertUtil.getString( orderDetail.get("activityCode")));
            saleDTO.put("OrderID", "");
            saleDTO.put("CouponCode", "");
            saleDTO.put("IsStock", "");
            saleDTO.put("InformType", "");
            saleDTO.put("UniqueCode", "");
            saleDetail.add(saleDTO);
        }
        resultMap.put("SaleDetail", saleDetail);
        
        List<Map<String,Object>> payDetail = new ArrayList<Map<String,Object>>();
        
        //全部构建为支付宝付款数据
        Map<String,Object> payDTO = new HashMap<String,Object>();
        payDTO.put("TradeNoIF", orderMainMap.get("billcode"));
        payDTO.put("ModifyCounts", 0);
        payDTO.put("DetailType", "Y");
        payDTO.put("PayTypeCode","PT");
        payDTO.put("PayTypeID", "");
        payDTO.put("PayTypeName", "");
        payDTO.put("Price", totalAmount);
        payDTO.put("Reason", "");
        payDetail.add(payDTO);
        resultMap.put("PayDetail", payDetail);
        
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
	
	
	
	
    private void convertDetailByCalculate(Map<String,Object> detail_info,List<Map<String,Object>> allDetail){
		int quantity=Integer.parseInt(ConvertUtil.getString(detail_info.get("quantity")));
		double actualAmount=Double.parseDouble(ConvertUtil.getString(detail_info.get("actualAmount")));
		if (quantity == 1) {
			detail_info.put("salePrice", actualAmount);
			detail_info.put("price", actualAmount);
			allDetail.add(detail_info);
			return;
		}
		//单价
		double salePrice = DoubleUtil.roundDown(DoubleUtil.div(actualAmount, quantity), 2);
		double saleAmount = DoubleUtil.mul(salePrice, quantity - 1);
		//补差的单价
		double salePriceEx = DoubleUtil.sub(actualAmount, saleAmount);
		for(int i = 0;i < quantity;i++){
			Map<String,Object> detail_map=new HashMap<String, Object>();
			detail_map.putAll(detail_info);
			if(i==0){
				detail_map.put("salePrice", salePrice);
				detail_map.put("price", salePrice);
				detail_map.put("quantity", 1);
				detail_map.put("actualAmount", salePrice);
				allDetail.add(detail_map);
			}else{
				detail_map.put("salePrice", salePriceEx);
				detail_map.put("price", salePriceEx);
				detail_map.put("quantity", 1);
				detail_map.put("actualAmount", salePriceEx);
				allDetail.add(detail_map);
			}
		}
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
//		map.put("BrandCode", map.get(CherryBatchConstants.BRAND_CODE));
//		// BatchCD 来自VSS$/01.Cherry/02.设计文档/01.概要设计/00.各种一览/【新设】CherryBatch一览.xlsx
//		map.put("JobCode", "BAT124");
//		
//		// 程序【开始运行时间】
//		String runStartTime = binbecm01_IF.getSYSDateTime();
//		// 作成日时
//		map.put("RunStartTime", runStartTime);
//		
		map.put("count", BATCH_SIZE);
		//查询逻辑仓库 以下字段待定
        Map<String,Object> logicparamMap = new HashMap<String,Object>();
        logicparamMap.put("BIN_BrandInfoID", map.get("brandInfoId"));
        logicparamMap.put("Type", "1");//终端逻辑仓库
		List<Map<String,Object>> logicDepotList = binOLCM18_BL.getLogicDepotList(logicparamMap);
        String inventoryTypeCode = ConvertUtil.getString(logicDepotList.get(0).get("LogicInventoryCode"));
        map.put("inventoryTypeCode", inventoryTypeCode);
//		comMap.putAll(map);
		
	}
	
	/**
	 * 共通Map
	 * @param map
	 * @return
	 */
	private void setComMap(Map<String, Object> map) {
		
		// 更新程序名
		map.put(CherryBatchConstants.UPDATEPGM, "BINBAT124");
		// 作成程序名
		map.put(CherryBatchConstants.CREATEPGM, "BINBAT124");
		// 作成者
		map.put(CherryBatchConstants.CREATEDBY,CherryBatchConstants.UPDATE_NAME);
		// 更新者
		map.put(CherryBatchConstants.UPDATEDBY,CherryBatchConstants.UPDATE_NAME);
	}
}
