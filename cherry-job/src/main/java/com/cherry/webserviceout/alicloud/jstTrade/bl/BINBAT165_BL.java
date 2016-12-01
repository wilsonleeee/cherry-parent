/*	
 * @(#)BINBAT165_BL.java     1.0 @2016-09-26
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.cmbussiness.interfaces.BINOLCM31_IF;
import com.cherry.cm.core.BatchExceptionDTO;
import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryBatchException;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.DateUtil;
import com.cherry.dr.cmbussiness.util.DoubleUtil;
import com.cherry.webservice.client.WebserviceClient;
import com.cherry.webservice.sale.service.SaleInfoService;
import com.cherry.webserviceout.alicloud.jstTrade.service.BINBAT165_Service;
import com.googlecode.jsonplugin.JSONUtil;

/**
*
*  聚石塔接口：订单(销售)数据导入BL
* 
* 从聚石塔获取订单数据并存入新后台电商接口表及发送销售MQ
* @author 
*
* @version  2016-09-26
*/
public class BINBAT165_BL {
	
	private static Logger logger = LoggerFactory.getLogger(BINBAT165_BL.class.getName());
	
	@Resource(name="binBAT165_Service")
	private BINBAT165_Service binBAT165_Service;
	
	@Resource(name="saleInfoService")
    private SaleInfoService saleInfoService;
	
	@Resource
	private BINOLCM31_IF binOLCM31_BL;
	
	/** 每批次(页)处理数量 20 */
	private final int BATCH_SIZE = 50;
	
	/** 还存在符合条件的数据 **/
	private final String HAS_MORE_DATA = "1";
	
	/**
	 * 同步会员处理
	 * 
	 * @param map 参数集合
	 * @return BATCH处理标志
	 */
//	public int tran_getTrade(Map<String, Object> map) throws Exception {
//		int flag = CherryBatchConstants.BATCH_SUCCESS;
//		// 程序初始化参数
//		init(map);
//		
//		// 配置调用聚石塔WebService接口的参数信息
//		Map<String, Object> wsParamMap = new HashMap<String, Object>();
//		wsParamMap.put("appID", "pekonbatch");
//		wsParamMap.put("Nick", "巧迪尚惠旗舰店");
//		//wsParamMap.put("brandCode", "mgp");
//		wsParamMap.put("TradeId", "2265693717983463");
//		//wsParamMap.put("RefundId", "11042364761220501");
//		wsParamMap.put("brandCode", "JST");
//		wsParamMap.put("TradeType", "GetTbTrade");
//	//	wsParamMap.put("TradeType", "GetTbRefund");
//		// 调用第三方WebService获取电商订单数据
//        Map<String,Object> wsResultMap = WebserviceClient.accessPekonWebService(wsParamMap,"http://121.41.160.246:30001/Pekonws/webservice/business");
//		return flag;
//	}
	
	public int tran_getTrade(Map<String, Object> map) throws Exception {
		// 程序初始化参数
		init(map);
		return syncOrder(map);
}
	
	/**
	 * 主程序
	 * 
	 * @param paramMap
	 * @return
	 * @throws CherryBatchException
	 */
	private synchronized int syncOrder(Map<String, Object> paramMap)throws Exception {
		int flag = CherryBatchConstants.BATCH_SUCCESS;
		// 定义品牌对应的店铺配置信息
		Map<String, Object> esInterfaceInfo = binBAT165_Service.getESInterfaceInfo(paramMap);
		if (null == esInterfaceInfo || esInterfaceInfo.isEmpty()) {
			BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO();
			batchExceptionDTO.setBatchName(this.getClass());
			batchExceptionDTO.setErrorCode("EOT00091");
			batchExceptionDTO.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
			flag = CherryBatchConstants.BATCH_WARNING;
			return flag;
		}
		String extJson = (String) esInterfaceInfo.get("extJson");
		if (CherryChecker.isNullOrEmpty(extJson)) {
			return printError("GetTbTrade 电商接口信息表配置接口信息不正确，appID为空！");
		}
		String url = (String) esInterfaceInfo.get("url");
		if (CherryChecker.isNullOrEmpty(url)) {
			return printError("GetTbTrade 电商接口信息表配置接口信息不正确，url为空！");
		}
		Map<String, Object> extMap = (Map<String, Object>) JSONUtil.deserialize(extJson);
		// 员工代码
		String employeeCode = (String) extMap.get("employeeCode");
		if (CherryChecker.isNullOrEmpty(employeeCode)) {
			return printError("GetTbTrade 电商接口信息表配置接口信息不正确，员工代码为空！");
		}
		// 柜台代码
		String departCode = (String) extMap.get("departCode");
		if (CherryChecker.isNullOrEmpty(departCode)) {
			return printError("GetTbTrade 电商接口信息表配置接口信息不正确，柜台代码为空！");
		}
		paramMap.put("employeeCode", extMap.get("employeeCode"));
		// 查询指定的员工信息
		Map<String, Object> employeeInfo = binBAT165_Service.getEmployeeInfo(paramMap);
		if (null == employeeInfo || employeeInfo.isEmpty()) {
			return printError("GetTbTrade 电商接口信息表配置接口信息不正确，员工代码在员工表不存在！");
		}
		// 员工ID
		paramMap.put("employeeId", employeeInfo.get("employeeId"));
		// 柜台代码
		paramMap.put("departCode", departCode);
		// 查找部门信息
		Map<String, Object> departInfo = binBAT165_Service.getDepartInfo(paramMap);
		if (null == departInfo || departInfo.isEmpty()) {
			return printError("GetTbTrade 电商接口信息表配置接口信息不正确，柜台代码在部门表不存在！");
		}
		// 部门ID
		paramMap.put("organizationId", departInfo.get("organizationId"));
		Map<String, Object> wsParamMap = new HashMap<String, Object>();
		// 应用ID
		wsParamMap.put("appID", extMap.get("appID"));
		// 卖家昵称
		wsParamMap.put("Nick", esInterfaceInfo.get("accountName"));
		// 聚石塔品牌代码
		wsParamMap.put("brandCode", "JST");
		// 业务类型:淘宝交易
		wsParamMap.put("TradeType", "GetTbTrade");
		// 订单ID
		String tradeId = (String) paramMap.get("tradeId");
		boolean isTradeId = !CherryChecker.isNullOrEmpty(tradeId, true);
		if (isTradeId) {
			wsParamMap.put("TradeId", tradeId);
			// 通过订单ID处理
			handleOneOrder(wsParamMap, url, paramMap);
		} else {
			// 订单创建时间的开始时间
			wsParamMap.put("CreatedStart", extMap.get("CreatedStart"));
			// 订单创建时间的结束时间
			wsParamMap.put("CreatedEnd", extMap.get("CreatedEnd"));
			// 显示条数
			wsParamMap.put("PageSize", BATCH_SIZE);
			String startTime = ConvertUtil.getString(esInterfaceInfo.get("GetDataEndTime")); // 程序上一次截取订单数据的截止时间
			int timeStep = CherryUtil.obj2int(esInterfaceInfo.get("timeStep")); // 步进
			String endTime = DateUtil.addDateByMinutes(DateUtil.DATETIME2_PATTERN,startTime, timeStep); // 计算数据截取结束时间(上一次截取订单数据的截止时间)
			// 开始时间
			wsParamMap.put("StartTime", formatTime(startTime));
			// 结束时间
			wsParamMap.put("EndTime", formatTime(endTime));
			String dataJson = (String) esInterfaceInfo.get("dataJson");
			// 本次排除的订单ID
			String unincludeIds = null;
			if (!CherryChecker.isNullOrEmpty(dataJson)) {
				Map<String, Object> dataMap = (Map<String, Object>) JSONUtil.deserialize(dataJson);
				unincludeIds = (String) dataMap.get("UnincludeIds");
			}
			wsParamMap.put("UnincludeIds", unincludeIds);
			// 设置共通参数
			setComMap(esInterfaceInfo);
			int index = 1;
			StringBuilder builder = new StringBuilder();
			while (true) {
				// 处理本批次订单
	        	try {
	        		long bfstartTime = System.currentTimeMillis();
					// 调用第三方WebService获取电商订单数据
			        Map<String,Object> wsResultMap = WebserviceClient.accessPekonWebService(wsParamMap, url);
			        if (null == wsResultMap || wsResultMap.isEmpty()) {
						return printError("GetTbTrade 发生异常！没有返回值");
			        }
			        String errorCode = ConvertUtil.getString(wsResultMap.get("ERRORCODE"));
			        if(!"0".equals(errorCode)){
			        	String errorMsg = "GetTbTrade 发生异常！ERRORCODE：" + errorCode + " ERRORMSG: " +
			        			ConvertUtil.getString(wsResultMap.get("ERRORMSG"));
			        	return printError(errorMsg);
			        }
			        Map<String, Object> resultContent = (Map<String, Object>)wsResultMap.get("ResultContent");
		        	if (null == resultContent || resultContent.isEmpty()){
		        		return printError("GetTbTrade 发生异常！ResultContent返回为空");
		        	}
		        	// 主订单列表
		        	List<Map<String, Object>> ordersList = (List<Map<String, Object>>) resultContent.get("OrderList");
	        		boolean hasMore = HAS_MORE_DATA.equals(resultContent.get("HasMoreData"));
	        		String maxJdpModified = (String) resultContent.get("MaxJdpModified");
	        		// 取得最大的数据推送修改时间
	        		if (null != maxJdpModified) {
	        			if (!CherryChecker.isNullOrEmpty(dataJson)) {
	        				Map<String, Object> dataMap = (Map<String, Object>) JSONUtil.deserialize(dataJson);
	        				dataMap.remove("UnincludeIds");
	        				if (dataMap.isEmpty()) {
	        					dataJson = null;
	        				} else {
	        					dataJson = JSONUtil.serialize(dataMap);
	        				}
	        			}
	        			if (CherryChecker.compareDate(endTime, maxJdpModified) > 0) {
	        				startTime = maxJdpModified;
	        			} else {
	        				startTime = endTime;
	        			}
	        		} else {
	        			// 本批次最后的订单修改时间
	        			String lastModifiedTime = (String) resultContent.get("LastModifiedTime");
	        			// 本批次最后的修改的订单
	        			String lastModifiedIds = (String) resultContent.get("LastModifiedIds");
	        			if (CherryChecker.isNullOrEmpty(lastModifiedTime) 
	        					|| CherryChecker.isNullOrEmpty(lastModifiedIds)) {
	        				return printError("GetTbTrade 发生异常！LastModifiedTime 或 LastModifiedIds返回为空");
	        			}
	        			if (hasMore) {
		        			// 下一次开始时间
		        			wsParamMap.put("StartTime", formatTime(lastModifiedTime));
		        			// 下一次不包含的订单ID
		        			wsParamMap.put("UnincludeIds", lastModifiedIds);
	        			}
	        			Map<String, Object> dataMap = null;
	        			if (!CherryChecker.isNullOrEmpty(dataJson)) {
	        				dataMap = (Map<String, Object>) JSONUtil.deserialize(dataJson);
	        			} else {
	        				dataMap = new HashMap<String, Object>();
	        			}
	        			dataMap.put("UnincludeIds", lastModifiedIds);
	        			dataJson = JSONUtil.serialize(dataMap);
	        			startTime = lastModifiedTime;
	        		}
	        		// 处理订单
	        		handleOrders(ordersList, paramMap);
	        		esInterfaceInfo.put("GetDataEndTime", startTime);
	        		esInterfaceInfo.put("dataJson", dataJson);
	        		// 更新电商接口信息表
	        		binBAT165_Service.updateESConfigLastTime(esInterfaceInfo);
	        		binBAT165_Service.manualCommit();
	        		long bfendTime = System.currentTimeMillis();
	        		builder.delete(0, builder.length());
					int size = (null == ordersList || ordersList.isEmpty()) ? 0 : ordersList.size();
	        		builder.append("******************************第").append(index).append("批处理结束，耗时：").append(bfendTime - bfstartTime)
	        		.append("ms 件数：").append(size);
	        		logger.info(builder.toString());
	        		if (!hasMore) {
	        			break;
	        		}
	        		index++;
	        	} catch (Exception e) {
	        		try {
	        			binBAT165_Service.manualRollback();
	        		} catch (Exception ex) {
	        			
	        		}
	        		logger.error("******************************处理订单发生异常******************************");
					logger.error(e.getMessage(),e);
					flag = CherryBatchConstants.BATCH_WARNING;
					break;
	        	}
			}
		}
		return flag;
	}
	
	private String formatTime(String time) {
		int index = time.indexOf(".");
		if (index > 0) {
			time = time.substring(0, index + 1) + Integer.parseInt(time.substring(index + 1));
		}
		return time;
	}
	
	private int handleOneOrder(Map<String, Object> wsParamMap, String url, Map<String, Object> paramMap) {
		try {
			// 调用第三方WebService获取电商订单数据
	        Map<String,Object> wsResultMap = WebserviceClient.accessPekonWebService(wsParamMap, url);
	        if (null == wsResultMap || wsResultMap.isEmpty()) {
				return printError("GetTbTrade 发生异常！没有返回值");
	        }
	        String errorCode = ConvertUtil.getString(wsResultMap.get("ERRORCODE"));
	        if(!"0".equals(errorCode)){
	        	String errorMsg = "GetTbTrade 发生异常！ERRORCODE：" + errorCode + " ERRORMSG: " +
	        			ConvertUtil.getString(wsResultMap.get("ERRORMSG"));
	        	return printError(errorMsg);
	        }
	        Map<String, Object> resultContent = (Map<String, Object>)wsResultMap.get("ResultContent");
	    	if (null == resultContent || resultContent.isEmpty()){
	    		return printError("GetTbTrade 发生异常！ResultContent返回为空");
	    	}
	    	// 主订单列表
	    	List<Map<String, Object>> ordersList = (List<Map<String, Object>>) resultContent.get("OrderList");
    		// 处理订单
    		handleOrders(ordersList, paramMap);
    		binBAT165_Service.manualCommit();
    	} catch (Exception e) {
    		try {
    			binBAT165_Service.manualRollback();
    		} catch (Exception ex) {
    			
    		}
    		logger.error("******************************处理订单发生异常******************************");
			logger.error(e.getMessage(),e);
			return CherryBatchConstants.BATCH_WARNING;
    	}
    	return CherryBatchConstants.BATCH_SUCCESS;
	}
	private void handleOrders(List<Map<String, Object>> ordersList, Map<String, Object> params) {
		if (null != ordersList && !ordersList.isEmpty()) {
			Map<String, Object> searchMap = new HashMap<String, Object>();
    		for (Map<String, Object> ordersMap : ordersList) {
    			if (!searchMap.isEmpty()) {
    				searchMap.clear();
    			}
    			searchMap.put("originalBillCode", ordersMap.get("OrderNumber"));
    			searchMap.put("dataSource", "Tmall");
    			// 查找电商订单信息
    			Map<String, Object> billInfo = binBAT165_Service.getESOrderMain(searchMap);
    			boolean isExist = false;
    			Map<String, Object> orderMainMap = new HashMap<String, Object>();
    			String modifiedTime = (String) ordersMap.get("ModifiedTime");
    			if (null != billInfo && !billInfo.isEmpty()) {
    				String billLastUpdateTime = (String) billInfo.get("billLastUpdateTime");
    				// 最近一次修改时间一致，无须处理
    				if (modifiedTime.equals(billLastUpdateTime)) {
    					continue;
    				}
    				isExist = true;
    				// 电商订单主表ID
    				orderMainMap.put("BIN_ESOrderMainID", billInfo.get("esOrderMainId"));
    			}
    			orderMainMap.put("originalStatus", ordersMap.get("OrderStatus"));
    			// 单据付款时间
    			orderMainMap.put("BillPayTime", ordersMap.get("PayTime"));
    			// 最后修改时间
    			orderMainMap.put("lastUpdateTime", modifiedTime);
    			// 一个主单据下可包含多个明细订单数据 循环多个明细数据
				List<Map<String, Object>> orderDetailList = (List<Map<String, Object>>) ordersMap.get("OrderDetailList");
				boolean detailFlag = true;
				if (null == orderDetailList || orderDetailList.isEmpty()) {
					detailFlag = false;
					orderMainMap.put("Comments", "没有子订单");
				}
    			if (isExist) {
    				// 更新天猫电商订单主表
    				saleInfoService.updateESOrderMain(orderMainMap);
    				// 更新天猫电商订单明细表
    				updateESOrderDetail(orderMainMap, orderDetailList);
    			} else {
    				// 新增天猫电商订单主表
    				int orderMainId = addESOrderMain(orderMainMap, ordersMap, params, searchMap);
    				if (detailFlag) {
    					// 新增天猫电商订单明细表
    					addESOrderDetail(orderDetailList, orderMainId);
    				}
    			}
    		}
    	}
	}
	
	private void addESOrderDetail(List<Map<String, Object>> orderDetailList, int orderMainId) {
		List<Map<String,Object>> esOrderDetailList = new ArrayList<Map<String,Object>>();
		for (int i = 0 ; i < orderDetailList.size(); i++) {
			Map<String,Object> esOrderDetail = orderDetailList.get(i);
			Map<String,Object> esOrderDetailMap = new HashMap<String, Object>();
			// 主表中的主ID
			esOrderDetailMap.put("BIN_ESOrderMainID", orderMainId);
			// 当前记录的序号
			esOrderDetailMap.put("DetailNo", i + 1);
			// 商品标题
			String title = (String) esOrderDetail.get("Title");
			// 电商商品名称
			esOrderDetailMap.put("EsProductName", title);
			// 电商商品名称
			esOrderDetailMap.put("EsProductTitleName", title);
			// 数量
			double quantity = 0;
			if (!CherryChecker.isNullOrEmpty(esOrderDetail.get("ItemCount"))) {
				quantity = Double.parseDouble(String.valueOf(esOrderDetail.get("ItemCount")));
			}
			esOrderDetailMap.put("Quantity", quantity);
			// 定价
			double price = 0;
			if (!CherryChecker.isNullOrEmpty(esOrderDetail.get("Price"))) {
				price = Double.parseDouble(String.valueOf(esOrderDetail.get("Price")));
			}
			esOrderDetailMap.put("Price", price);
			// 商家外部编码
			String outerIid = (String) esOrderDetail.get("OuterIID");
			// 商品的最小库存单位Sku的id
			String skuId = (String) esOrderDetail.get("SkuId");
			// 自定义sku编码
			String outerSkuId = (String) esOrderDetail.get("OuterSkuId");
			esOrderDetailMap.put("OuterIid", outerIid);
			esOrderDetailMap.put("SkuId", skuId);
			esOrderDetailMap.put("OuterSkuId", outerSkuId);
			// 原始产品码
			String originalCode = null;
			if (!CherryChecker.isNullOrEmpty(skuId) && 
					!CherryChecker.isNullOrEmpty(outerSkuId)) {
				originalCode = outerSkuId;
			} else if (!CherryChecker.isNullOrEmpty(outerIid)) {
				originalCode = outerIid;
			}
			esOrderDetailMap.put("OriginalCode", originalCode);
			// 宝贝编码
			esOrderDetailMap.put("OutCode", originalCode);
			// 子订单编号
			esOrderDetailMap.put("OrderId", esOrderDetail.get("ItemOrderNumber"));
			// 子订单发货时间
			esOrderDetailMap.put("ConsignTime", esOrderDetail.get("ConsignTime"));
			// 手工调整金额
			double adjustFee = 0;
			if (!CherryChecker.isNullOrEmpty(esOrderDetail.get("AdjustFee"))) {
				adjustFee = Double.parseDouble(String.valueOf(esOrderDetail.get("AdjustFee")));
			}
			// 子订单级订单优惠金额
			double discountFee = 0;
			if (!CherryChecker.isNullOrEmpty(esOrderDetail.get("DiscountFee"))) {
				discountFee = Double.parseDouble(String.valueOf(esOrderDetail.get("DiscountFee")));
			}
			// 优惠分摊
			double partMjzDiscount = 0;
			if (!CherryChecker.isNullOrEmpty(esOrderDetail.get("PartMjzDiscount"))) {
				partMjzDiscount = Double.parseDouble(String.valueOf(esOrderDetail.get("PartMjzDiscount")));
			}
			esOrderDetailMap.put("AdjustFee", adjustFee);
			esOrderDetailMap.put("DiscountFee", discountFee);
			esOrderDetailMap.put("PartMjzDiscount", partMjzDiscount);
			// 实付金额
			double actualAmount = 0;
			actualAmount = DoubleUtil.mul(price, quantity);
			// 实付金额 = 价格*数量 + 手工调整金额 - 子订单级订单优惠金额 - 优惠分摊
			actualAmount = DoubleUtil.sub(DoubleUtil.sub(DoubleUtil.add(actualAmount, adjustFee), discountFee), partMjzDiscount);
			esOrderDetailMap.put("ActualAmount", actualAmount);
			// 销售类型
			esOrderDetailMap.put("SaleType", "N");
			// 设置共通参数
	     	setComMap(esOrderDetailMap);
			esOrderDetailList.add(esOrderDetailMap);
		}
		// 插入电商订单明细表
		saleInfoService.insertESOrderDetail(esOrderDetailList);
	}
	
	private void updateESOrderDetail(Map<String, Object> orderMainMap, List<Map<String, Object>> orderDetailList) {
		if (null != orderDetailList && !orderDetailList.isEmpty()) {
			// 查找订单明细表
			List<Map<String, Object>> preDetailList = binBAT165_Service.getESOrderDetail(orderMainMap);
			if (null != preDetailList && !preDetailList.isEmpty()) {
				List<Map<String, Object>> upList = new ArrayList<Map<String, Object>>();
				for (Map<String, Object> preDetail : preDetailList) {
					// 子订单号
					String orderId = (String) preDetail.get("orderId");
					for (Map<String, Object> orderDetail : orderDetailList) {
						if (orderId.equals(orderDetail.get("ItemOrderNumber"))) {
							if (null == preDetail.get("ConsignTime") 
									&& !CherryChecker.isNullOrEmpty(orderDetail.get("ConsignTime"))) {
								Map<String, Object> upMap = new HashMap<String, Object>();
								upMap.put("esOrderDetailID", preDetail.get("esOrderDetailID"));
								// 子订单发货时间
								upMap.put("ConsignTime", orderDetail.get("ConsignTime"));
								// 设置共通参数
						     	setComMap(upMap);
						     	upList.add(upMap);
							}
					     	orderDetailList.remove(orderDetail);
					     	break;
						}
					}
				}
				// 更新订单明细表
				if (!upList.isEmpty()) {
					binBAT165_Service.updateESOrderDetail(upList);
				}
			}
		}
	}
	
	
	private int addESOrderMain(Map<String, Object> orderMainMap, Map<String, Object> ordersMap, Map<String, Object> params, Map<String, Object> searchMap) {
		// 订单编号
		String orderNumber = (String) ordersMap.get("OrderNumber");
		// 原始订单状态
		String orderStatus = (String) ordersMap.get("OrderStatus");
		// 组织结构ID
		orderMainMap.put("BIN_OrganizationID", params.get("organizationId"));
		// 员工ID
		orderMainMap.put("BIN_EmployeeID", params.get("employeeId"));
		// 员工代码code
		orderMainMap.put("EmployeeCode", params.get("employeeCode"));
		//下单组织结构ID
		orderMainMap.put("BIN_OrganizationIDDX", params.get("organizationId"));
        //下单员工ID
		orderMainMap.put("BIN_EmployeeIDDX", params.get("employeeId"));
        //下单员工代码code
		orderMainMap.put("EmployeeCodeDX", params.get("employeeCode"));
		// 数据来源
		orderMainMap.put("DataSource", "Tmall");
		// 店铺名称
		orderMainMap.put("ShopName", ordersMap.get("ShopName"));
		//订单编号，来自于电商（有可能被ERP处理过，不再是电商平台上展示给消费者的原始单据号），全局唯一
		orderMainMap.put("BillCode", orderNumber);
		//电商平台原始的单据号
		orderMainMap.put("OriginalBillCode", orderNumber);
		//交易类型（销售：NS，退货：SR,积分兑换:PX）
		orderMainMap.put("SaleType",CherryConstants.BUSINESS_TYPE_NS);
		//单据类型
		orderMainMap.put("TicketType", "NE");
		//单据交易状态
		orderMainMap.put("BillState", binBAT165_Service.convertOrderStatus(orderStatus));
		// 业务类型
		orderMainMap.put("BillType", "1");
		// 会员昵称
		String buyerNick = (String) ordersMap.get("BuyerNick");
		searchMap.put("nickName", buyerNick);
		// 查找会员信息
		Map<String, Object> memberInfo = binBAT165_Service.getMemberInfo(searchMap);
		String memCode = null;
		if (null != memberInfo && !memberInfo.isEmpty()) {
			orderMainMap.put("ConsumerType","MP");
			//新后台查到的会员ID
			orderMainMap.put("BIN_MemberInfoID", memberInfo.get("BIN_MemberInfoID"));
			memCode = (String) memberInfo.get("MemberCode");
			if (null == memCode) {
				searchMap.put("memberInfoId", memberInfo.get("BIN_MemberInfoID"));
				// 取得会员卡号
				memCode = binOLCM31_BL.getMemCard(searchMap);
			}
			 //新后台查到的会员姓名
			orderMainMap.put("MemberName", memberInfo.get("MemberName"));
		} else {
			orderMainMap.put("ConsumerType","NP");
			memCode = CherryBatchConstants.NOT_MEMBER_CARD;
		}
		// 会员卡号
		orderMainMap.put("MemberCode", memCode);
		//会员昵称
		orderMainMap.put("MemberNickname", buyerNick);
        //买家姓名 = 买家昵称
		orderMainMap.put("BuyerName", buyerNick);
		//收货人姓名
		orderMainMap.put("ConsigneeName", ordersMap.get("ReceiverName"));
        //收货人手机
		orderMainMap.put("ConsigneeMobilePhone", ordersMap.get("ReceiverMobile"));
        //收货人城市
		orderMainMap.put("ConsigneeCity", ordersMap.get("ReceiverCity"));
        //收货人地址
		orderMainMap.put("ConsigneeAddress", binBAT165_Service.subStringByLimit(
				(String) ordersMap.get("ReceiverAddress"), 200));
        //买家留言
		orderMainMap.put("BuyerMessage", binBAT165_Service.subStringByLimit(
				(String) ordersMap.get("BuyerMessage"), 300));
        //卖家备注
		orderMainMap.put("SellerMemo", binBAT165_Service.subStringByLimit(
				(String) ordersMap.get("SellerMemo"), 500));
        //单据创建时间
		orderMainMap.put("BillCreateTime", ordersMap.get("CreatedTime"));
		// 折后金额
		orderMainMap.put("PayAmount", ordersMap.get("Payment"));
		// 实收金额
		orderMainMap.put("Amount", ordersMap.get("Payment"));
		// 花费积分
        String pointFee = ConvertUtil.getString(ordersMap.get("PointFee"));
        if (!"".equals(pointFee)) {
        	orderMainMap.put("CostPoint",pointFee);
        }
        //快递公司代号
//        orderMainMap.put("ExpressCompanyCode",ordersMap.get("LogisticsCompany"));
//        //快递单编号
//        orderMainMap.put("ExpressBillCode",ordersMap.get("InvoiceNo"));
        //快递费用
        orderMainMap.put("ExpressCost", ordersMap.get("PostFee"));
        // 订单转销售标志
        orderMainMap.put("ConvertFlag", "0");
        // 销售记录的被修改次数
        orderMainMap.put("ModifiedTimes", 0);
        orderMainMap.put("BIN_OrganizationInfoID", params.get("organizationInfoId"));
        orderMainMap.put("BIN_BrandInfoID", params.get("brandInfoId"));
        // 设置共通参数
     	setComMap(orderMainMap);
        // 插入电商订单主表，返回主表ID
        return saleInfoService.insertESOrderMain(orderMainMap);
	}
	
	private int printError(String errorMsg) {
		logger.error("*************" + errorMsg);
		return CherryBatchConstants.BATCH_WARNING;
	}
	
	/**
	 * 程序初始化参数
	 * @param map
	 * @throws Exception 
	 * @throws CherryBatchException 
	 */
	private void init(Map<String, Object> map) {
		map.put("esCode", "pekon");
		map.put("tradeCode", "GetTbTrade");
	}
	
	/**
	 * 共通Map
	 * @param map
	 * @return
	 */
	private void setComMap(Map<String, Object> map) {
		// 更新程序名
		map.put(CherryBatchConstants.UPDATEPGM, "BINBAT122");
		// 作成程序名
		map.put(CherryBatchConstants.CREATEPGM, "BINBAT122");
		// 作成者
		map.put(CherryBatchConstants.CREATEDBY, "BINBAT122");
		// 更新者
		map.put(CherryBatchConstants.UPDATEDBY, "BINBAT122");
		// 更新程序名
		map.put("UpdatePGM", "BINBAT122");
		// 作成程序名
		map.put("CreatePGM", "BINBAT122");
		// 作成者
		map.put("CreatedBy", "BINBAT122");
		// 更新者
		map.put("UpdatedBy", "BINBAT122");
	}

}
