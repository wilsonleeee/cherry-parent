/*	
 * @(#)BINBAT123_BL.java     1.0 @2016-09-26
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
import com.cherry.webservice.client.WebserviceClient;
import com.cherry.webservice.sale.service.SaleInfoService;
import com.cherry.webserviceout.alicloud.jstTrade.service.BINBAT122_Service;
import com.googlecode.jsonplugin.JSONUtil;

/**
*
*  聚石塔接口：订单(退款)数据导入BL
* 
* @author 
*
* @version  2016-09-26
*/
public class BINBAT123_BL {
	
	private static Logger logger = LoggerFactory.getLogger(BINBAT123_BL.class.getName());
	
	@Resource(name="saleInfoService")
    private SaleInfoService saleInfoService;
	
	@Resource(name="binbat122_Service")
	private BINBAT122_Service binbat122_Service;
	
	@Resource
	private BINOLCM31_IF binOLCM31_BL;
	
	/** 每批次(页)处理数量 20 */
	private final int BATCH_SIZE = 50;
	
	/** 还存在符合条件的数据 **/
	private final String HAS_MORE_DATA = "1";
	
	public int tran_getRefund(Map<String, Object> map) throws Exception {
		// 程序初始化参数
		init(map);
		return syncOrder(map);
	}
	
	/**
	 * 程序初始化参数
	 * @param map
	 * @throws Exception 
	 * @throws CherryBatchException 
	 */
	private void init(Map<String, Object> map) {
		map.put("esCode", "pekon");
		map.put("tradeCode", "GetTbRefund");
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
		Map<String, Object> esInterfaceInfo = binbat122_Service.getESInterfaceInfo(paramMap);
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
			return printError("GetTbRefund 电商接口信息表配置接口信息不正确，appID为空！");
		}
		String url = (String) esInterfaceInfo.get("url");
		if (CherryChecker.isNullOrEmpty(url)) {
			return printError("GetTbRefund 电商接口信息表配置接口信息不正确，url为空！");
		}
		Map<String, Object> extMap = (Map<String, Object>) JSONUtil.deserialize(extJson);
		// 员工代码
		String employeeCode = (String) extMap.get("employeeCode");
		if (CherryChecker.isNullOrEmpty(employeeCode)) {
			return printError("GetTbRefund 电商接口信息表配置接口信息不正确，员工代码为空！");
		}
		// 柜台代码
		String departCode = (String) extMap.get("departCode");
		if (CherryChecker.isNullOrEmpty(departCode)) {
			return printError("GetTbRefund 电商接口信息表配置接口信息不正确，柜台代码为空！");
		}
		paramMap.put("employeeCode", extMap.get("employeeCode"));
		// 查询指定的员工信息
		Map<String, Object> employeeInfo = binbat122_Service.getEmployeeInfo(paramMap);
		if (null == employeeInfo || employeeInfo.isEmpty()) {
			return printError("GetTbRefund 电商接口信息表配置接口信息不正确，员工代码在员工表不存在！");
		}
		// 员工ID
		paramMap.put("employeeId", employeeInfo.get("employeeId"));
		// 柜台代码
		paramMap.put("departCode", departCode);
		// 查找部门信息
		Map<String, Object> departInfo = binbat122_Service.getDepartInfo(paramMap);
		if (null == departInfo || departInfo.isEmpty()) {
			return printError("GetTbRefund 电商接口信息表配置接口信息不正确，柜台代码在部门表不存在！");
		}
		// 部门ID
		paramMap.put("organizationId", departInfo.get("organizationId"));
		Map<String, Object> wsParamMap = new HashMap<String, Object>();
		// 应用ID
		wsParamMap.put("appID", extMap.get("appID"));
		// 卖家昵称
		String accountName = (String) esInterfaceInfo.get("accountName");
		wsParamMap.put("Nick", accountName);
		paramMap.put("ShopName", accountName);
		// 聚石塔品牌代码
		wsParamMap.put("brandCode", "JST");
		// 业务类型:淘宝交易
		wsParamMap.put("TradeType", "GetTbRefund");
		// 退款订单ID
		String refundId = (String) paramMap.get("refundId");
		boolean isRefundId = !CherryChecker.isNullOrEmpty(refundId, true);
		if (isRefundId) {
			wsParamMap.put("RefundId", refundId);
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
						return printError("GetTbRefund 发生异常！没有返回值");
			        }
			        String errorCode = ConvertUtil.getString(wsResultMap.get("ERRORCODE"));
			        if(!"0".equals(errorCode)){
			        	String errorMsg = "GetTbRefund 发生异常！ERRORCODE：" + errorCode + " ERRORMSG: " +
			        			ConvertUtil.getString(wsResultMap.get("ERRORMSG"));
			        	return printError(errorMsg);
			        }
			        Map<String, Object> resultContent = (Map<String, Object>)wsResultMap.get("ResultContent");
		        	if (null == resultContent || resultContent.isEmpty()){
		        		return printError("GetTbRefund 发生异常！ResultContent返回为空");
		        	}
		        	// 主订单列表
		        	List<Map<String, Object>> refundList = (List<Map<String, Object>>) resultContent.get("RefundList");
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
	        				return printError("GetTbRefund 发生异常！LastModifiedTime 或 LastModifiedIds返回为空");
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
	        		handleOrders(refundList, paramMap);
	        		esInterfaceInfo.put("GetDataEndTime", startTime);
	        		esInterfaceInfo.put("dataJson", dataJson);
	        		// 更新电商接口信息表
	        		binbat122_Service.updateESConfigLastTime(esInterfaceInfo);
	        		binbat122_Service.manualCommit();
	        		long bfendTime = System.currentTimeMillis();
	        		builder.delete(0, builder.length());
	        		int size = (null == refundList || refundList.isEmpty()) ? 0 : refundList.size();
	        		builder.append("******************************第").append(index).append("批处理结束，耗时：").append(bfendTime - bfstartTime)
	        		.append("ms 件数：").append(size);
	        		logger.info(builder.toString());
	        		if (!hasMore) {
	        			break;
	        		}
	        		index++;
	        	} catch (Exception e) {
	        		try {
	        			binbat122_Service.manualRollback();
	        		} catch (Exception ex) {
	        			
	        		}
	        		logger.error("******************************处理退款单发生异常******************************");
					logger.error(e.getMessage(),e);
					flag = CherryBatchConstants.BATCH_WARNING;
					break;
	        	}
			}
		}
		return flag;
	}
	
	private int handleOneOrder(Map<String, Object> wsParamMap, String url, Map<String, Object> paramMap) {
		try {
			// 调用第三方WebService获取电商订单数据
	        Map<String,Object> wsResultMap = WebserviceClient.accessPekonWebService(wsParamMap, url);
	        if (null == wsResultMap || wsResultMap.isEmpty()) {
				return printError("GetTbRefund 发生异常！没有返回值");
	        }
	        String errorCode = ConvertUtil.getString(wsResultMap.get("ERRORCODE"));
	        if(!"0".equals(errorCode)){
	        	String errorMsg = "GetTbRefund 发生异常！ERRORCODE：" + errorCode + " ERRORMSG: " +
	        			ConvertUtil.getString(wsResultMap.get("ERRORMSG"));
	        	return printError(errorMsg);
	        }
	        Map<String, Object> resultContent = (Map<String, Object>)wsResultMap.get("ResultContent");
	    	if (null == resultContent || resultContent.isEmpty()){
	    		return printError("GetTbRefund 发生异常！ResultContent返回为空");
	    	}
	    	// 主订单列表
	    	List<Map<String, Object>> refundList = (List<Map<String, Object>>) resultContent.get("RefundList");
    		// 处理订单
    		handleOrders(refundList, paramMap);
    		binbat122_Service.manualCommit();
    	} catch (Exception e) {
    		try {
    			binbat122_Service.manualRollback();
    		} catch (Exception ex) {
    			
    		}
    		logger.error("******************************处理订单发生异常******************************");
			logger.error(e.getMessage(),e);
			return CherryBatchConstants.BATCH_WARNING;
    	}
    	return CherryBatchConstants.BATCH_SUCCESS;
	}
	
	private void handleOrders(List<Map<String, Object>> refundList, Map<String, Object> params) {
		if (null != refundList && !refundList.isEmpty()) {
			Map<String, Object> searchMap = new HashMap<String, Object>();
    		for (Map<String, Object> ordersMap : refundList) {
    			if (!searchMap.isEmpty()) {
    				searchMap.clear();
    			}
    			searchMap.put("originalBillCode", ordersMap.get("RefundId"));
    			searchMap.put("dataSource", "Tmall");
    			// 查找电商订单信息
    			Map<String, Object> billInfo = binbat122_Service.getESOrderMain(searchMap);
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
    			orderMainMap.put("originalStatus", ordersMap.get("RefundStatus"));
    			// 最后修改时间
    			orderMainMap.put("lastUpdateTime", modifiedTime);
    			if (isExist) {
    				// 更新天猫电商订单主表
    				saleInfoService.updateESOrderMain(orderMainMap);
    			} else {
    				// 新增天猫电商订单主表
    				int orderMainId = addESOrderMain(orderMainMap, ordersMap, params, searchMap);
					// 新增天猫电商订单明细表
					addESOrderDetail(ordersMap, orderMainId);
    			}
    		}
    	}
	}
	
	private int addESOrderMain(Map<String, Object> orderMainMap, Map<String, Object> ordersMap, Map<String, Object> params, Map<String, Object> searchMap) {
		// 订单编号
		String orderNumber = (String) ordersMap.get("RefundId");
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
		orderMainMap.put("ShopName", params.get("ShopName"));
		//订单编号，来自于电商（有可能被ERP处理过，不再是电商平台上展示给消费者的原始单据号），全局唯一
		orderMainMap.put("BillCode", orderNumber);    
		//电商平台原始的单据号
		orderMainMap.put("OriginalBillCode", orderNumber);
		//交易类型（销售：NS，退货：SR,积分兑换:PX）
		orderMainMap.put("SaleType",CherryConstants.BUSINESS_TYPE_SR);
		//单据类型
		orderMainMap.put("TicketType", "NE");
		//单据交易状态
		orderMainMap.put("BillState", "4");
		// 业务类型
		orderMainMap.put("BillType", "1");
		// 会员昵称
		String buyerNick = (String) ordersMap.get("BuyerNick");
		searchMap.put("nickName", buyerNick);
		// 查找会员信息
		Map<String, Object> memberInfo = binbat122_Service.getMemberInfo(searchMap);
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
        //单据创建时间
		orderMainMap.put("BillCreateTime", ordersMap.get("RefundCreated"));
		// 折后金额
		orderMainMap.put("PayAmount", ordersMap.get("RefundFee"));
		// 实收金额
		orderMainMap.put("Amount", ordersMap.get("RefundFee"));
        // 订单转销售标志
        orderMainMap.put("ConvertFlag", "0");
        // 销售记录的被修改次数
        orderMainMap.put("ModifiedTimes", 0);
        orderMainMap.put("BIN_OrganizationInfoID", params.get("organizationInfoId"));
        orderMainMap.put("BIN_BrandInfoID", params.get("brandInfoId"));
        // 退款理由
        orderMainMap.put("comments", binbat122_Service.subStringByLimit(
				(String) ordersMap.get("Reason"), 100));
        // 关联的主单
        orderMainMap.put("RelevanceBillCode", ordersMap.get("Tid"));
        // 关联的子单
        orderMainMap.put("RefundOrderCode", ordersMap.get("Oid"));
        // 设置共通参数
     	setComMap(orderMainMap);
        // 插入电商订单主表，返回主表ID
        return saleInfoService.insertESOrderMain(orderMainMap);
	}
	
	private void addESOrderDetail(Map<String, Object> ordersMap, int orderMainId) {
		List<Map<String,Object>> esOrderDetailList = new ArrayList<Map<String,Object>>();
		Map<String,Object> esOrderDetailMap = new HashMap<String, Object>();
		// 主表中的主ID
		esOrderDetailMap.put("BIN_ESOrderMainID", orderMainId);
		// 当前记录的序号
		esOrderDetailMap.put("DetailNo", 1);
		// 商品标题
		String title = (String) ordersMap.get("Title");
		// 电商商品名称
		esOrderDetailMap.put("EsProductName", title);
		// 电商商品名称
		esOrderDetailMap.put("EsProductTitleName", title);
		// 数量
		double quantity = 0;
		if (!CherryChecker.isNullOrEmpty(ordersMap.get("ItemCount"))) {
			quantity = Double.parseDouble(String.valueOf(ordersMap.get("ItemCount")));
		}
		esOrderDetailMap.put("Quantity", quantity);
		// 定价
		double price = 0;
		if (!CherryChecker.isNullOrEmpty(ordersMap.get("Price"))) {
			price = Double.parseDouble(String.valueOf(ordersMap.get("Price")));
		}
		esOrderDetailMap.put("Price", price);
		// 宝贝编码
		esOrderDetailMap.put("OutCode", ordersMap.get("NumIID"));
		// 退还金额
		esOrderDetailMap.put("ActualAmount", ordersMap.get("RefundFee"));
		// 退款后子订单剩余金额
		esOrderDetailMap.put("RemainPayment", ordersMap.get("Payment"));
		// 是否退货
		esOrderDetailMap.put("HasGoodReturn", Boolean.parseBoolean(String.valueOf(ordersMap.get("HasGoodReturn"))) ? "1" : "0");
		// 销售类型
		esOrderDetailMap.put("SaleType", "N");
		// 设置共通参数
     	setComMap(esOrderDetailMap);
		esOrderDetailList.add(esOrderDetailMap);
		// 插入电商订单明细表
		saleInfoService.insertESOrderDetail(esOrderDetailList);
	}
	
	private int printError(String errorMsg) {
		logger.error("*************" + errorMsg);
		return CherryBatchConstants.BATCH_WARNING;
	}
	
	private String formatTime(String time) {
		int index = time.indexOf(".");
		if (index > 0) {
			time = time.substring(0, index + 1) + Integer.parseInt(time.substring(index + 1));
		}
		return time;
	}
	
	/**
	 * 共通Map
	 * @param map
	 * @return
	 */
	private void setComMap(Map<String, Object> map) {
		// 更新程序名
		map.put(CherryBatchConstants.UPDATEPGM, "BINBAT123");
		// 作成程序名
		map.put(CherryBatchConstants.CREATEPGM, "BINBAT123");
		// 作成者
		map.put(CherryBatchConstants.CREATEDBY, "BINBAT123");
		// 更新者
		map.put(CherryBatchConstants.UPDATEDBY, "BINBAT123");
		// 更新程序名
		map.put("UpdatePGM", "BINBAT123");
		// 作成程序名
		map.put("CreatePGM", "BINBAT123");
		// 作成者
		map.put("CreatedBy", "BINBAT123");
		// 更新者
		map.put("UpdatedBy", "BINBAT123");
	}

}
