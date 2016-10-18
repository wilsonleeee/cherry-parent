/*	
 * @(#)BINOLCPCOM05_BL.java     1.0 2013/4/18		
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
package com.cherry.cp.common.bl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.activemq.dto.MQInfoDTO;
import com.cherry.cm.activemq.interfaces.BINOLMQCOM01_IF;
import com.cherry.cm.cmbussiness.bl.BINOLCM03_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM33_BL;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.DateUtil;
import com.cherry.cp.common.CampConstants;
import com.cherry.cp.common.interfaces.BINOLCPCOM05_IF;
import com.cherry.cp.common.interfaces.BINOLCPCOMCOUPON_IF;
import com.cherry.cp.common.service.BINOLCPCOM05_Service;
import com.cherry.mq.mes.common.CherryMQException;
import com.cherry.mq.mes.common.MessageConstants;
import com.cherry.webservice.client.WebserviceClient;
import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

/**
 * 活动预约表状态操作 BL
 * 
 * @author lipc
 * @version 1.0 2013.4.18
 */
public class BINOLCPCOM05_BL implements BINOLCPCOM05_IF {

	private static final Logger logger = LoggerFactory
			.getLogger(BINOLCPCOM05_BL.class);
	
	private static final Map<String,String> msgMap = new HashMap<String, String>();

	@Resource(name = "binOLMQCOM01_BL")
	private BINOLMQCOM01_IF binOLMQCOM01_BL;
	
	@Resource(name = "binOLCM03_BL")
	private BINOLCM03_BL cm03bl;
	
	@Resource(name = "binOLCM33_BL")
	private BINOLCM33_BL cm33_bl;

	@Resource(name = "binolcpcomcouponIF")
	private BINOLCPCOMCOUPON_IF cpnIF;

	@Resource(name = "binolcpcom05_Service")
	private BINOLCPCOM05_Service ser;

	/** 系统配置项 共通BL */
	@Resource(name = "binOLCM14_BL")
	private BINOLCM14_BL cm14bl;
	
	static {
		msgMap.put("3", "活动不存在");
		msgMap.put("4", "预约单据总数已达活动上限");
		msgMap.put("5", "单个对象预约次数大于活动上限");
		msgMap.put("6", "活动无预约阶段,无法预约");
		msgMap.put("7", "活动已结束");
		msgMap.put("8", "活动未开始");
		msgMap.put("9", "活动领用日期参考规则JSON解析失败");
		msgMap.put("10", "生成会员单据领用日期失败");
		msgMap.put("11", "领用柜台号为空");
		msgMap.put("12", "预约日期大于预约截止日期");
		msgMap.put("13", "预约日期小于预约开始日期");
		msgMap.put("14", "活动礼品库存不足");
	}

	/**
	 * MQ操作
	 * 
	 * @param map
	 * @throws CherryException
	 */
	@Override
	public int tran_campOrderMQ(Map<String, Object> comMap,
			Map<String, Object> order) throws CherryException {
		int result = CherryConstants.SUCCESS;
		// 设置共通参数MAP
		setComMap(comMap);
		// 单据操作状态
		String billState = ConvertUtil.getString(order
				.get(CampConstants.BILL_STATE));
		// 业务日期
		String busDate = ConvertUtil.getString(comMap
				.get(CherryConstants.BUSINESS_DATE));
		String includeState = ConvertUtil.getString(order.get(CampConstants.INCLUDE_STATE));
		// 预约操作
		if (CampConstants.BILL_STATE_RV.equals(billState)) {
			String subCampCode = ConvertUtil.getString(order
					.get(CampConstants.SUBCAMP_CODE));
			// 取得主题活动信息
			Map<String, Object> campInfo = (Map<String, Object>)comMap.get("CAMPINFO");
			if (null == campInfo) {
				campInfo = getCampInfo(subCampCode);
			}
			if (null == campInfo) {
				logger.error(">>>>>>>>>>>>>>活动【" + subCampCode + "】不存在>>>>>>>>>>>>>>>");
				return 3;
			}
			// 活动状态
			String actState = ConvertUtil.getString(campInfo.get("actState"));
			if (CampConstants.STATE_0.equals(actState)) {
				logger.error(">>>>>>>>>>>>>>活动【" + subCampCode + "】未开始>>>>>>>>>>>>>>>");
				return 8;
			}else if(CampConstants.STATE_2.equals(actState) && !CampConstants.STATE_2.equals(includeState)){
				logger.error(">>>>>>>>>>>>>>活动【" + subCampCode + "】已结束>>>>>>>>>>>>>>>");
				return 7;
			}else if(CampConstants.STATE_3.equals(actState)){
				logger.error(">>>>>>>>>>>>>>活动【" + subCampCode + "】草稿中>>>>>>>>>>>>>>>");
				return 8;
			}
			int brandInfoId = ConvertUtil.getInt(comMap.get(CherryConstants.BRANDINFOID));
			// 活动总上限
			int topLimit= ConvertUtil.getInt(campInfo.get(CampConstants.TOPLIMIT));
			int times = ConvertUtil.getInt(campInfo.get(CampConstants.TIMES));
			if(topLimit != 0){
				Map<String,Object> p = new HashMap<String, Object>();
				p.put(CherryConstants.BRANDINFOID, brandInfoId);
				p.put(CampConstants.SUBCAMP_CODE, subCampCode);
				int orderCnt = ser.getOrderCount(p);
				if(orderCnt >= topLimit){
					logger.error(">>>>>>>>>>>>>>预约单据总数已达活动上限=" + topLimit);
					return 4;
				}
			}
			if(times != 0){
				Map<String,Object> p = new HashMap<String, Object>();
				p.put(CherryConstants.BRANDINFOID, brandInfoId);
				p.put(CampConstants.SUBCAMP_CODE, subCampCode);
				p.put("messageId", order.get("messageId"));
				p.put("mobilePhone", order.get("mobilePhone"));
				p.put("memCode", order.get("memCode"));
				try {
					setHisParamMap(campInfo,busDate,p);
				} catch (JSONException e) {
					return 9;
				}
				int orderCnt = ser.getOrderCount(p);
				if(orderCnt >= times){
					logger.error(">>>>>>>>>>>>>>单个对象预约次数大于活动上限=" + times);
					return 5;
				}
			}
			
			String orderFromDate = ConvertUtil.getString(campInfo.get(CampConstants.ORDER_FROMDATE));
			String orderToDate = ConvertUtil.getString(campInfo.get(CampConstants.ORDER_TODATE));
			if("".equals(orderFromDate)){
				logger.error(">>>>>>>>>>>>>>活动【" + subCampCode + "】无预约阶段,无法预约>>>>>>>>>>>>>>>");
				return 6;
			}
			String orderDate = ConvertUtil.getString(order.get(CampConstants.OPT_TIME)).split(CherryConstants.SPACE)[0];
			if(CherryChecker.compareDate(orderDate, orderToDate) > 0){
				logger.error(">>>>>>>>>>>>>>预约日期【" + orderDate + "】大于预约截止日期" + orderToDate + "】>>>>>>>>>>>>>>>");
				return 12;
			}
			if(CherryChecker.compareDate(orderDate, orderFromDate) <  0){
				logger.error(">>>>>>>>>>>>>>预约日期【" + orderDate + "】小于预约开始日期" + orderFromDate + "】>>>>>>>>>>>>>>>");
				return 13;
			}
			// ======设置单据领用日期范围========
			List<Map<String, Object>> orderList = new ArrayList<Map<String,Object>>();
			orderList.add(order);
			campInfo.put(CampConstants.OPT_TIME, comMap.get(CampConstants.OPT_TIME));
			setObtainDate(campInfo, busDate, orderList);
			if(orderList.size() > 0){
				// 设置领用柜台号
				if(CherryChecker.isNullOrEmpty(order.get("counterCode"))){
					String gotCounter = ConvertUtil.getString(campInfo.get("gotCounter"));
					 if("1".equals(gotCounter)){// 任意柜台
						order.put("counterCode", "ALL");
					}else if("2".equals(gotCounter)){//发卡柜台
						order.put("counterCode", order.get("cntCodeBelong"));
					}else if("3".equals(gotCounter)){//预约柜台
						order.put("counterCode", order.get("orderCntCode"));
					}else if("4".equals(gotCounter)){// 首次购买柜台
						order.put("counterCode", order.get("firstSaleCounterCode"));
					}
				}
				if(CherryChecker.isNullOrEmpty(order.get("counterCode"))){
					logger.error(">>>>>>>>>>>>>>领用柜台号为空>>>>>>>>>>>>>>>");
					return 11;
				}
				order.putAll(campInfo);
				// 新增预约单据
				addOrder(comMap, order);
				if(CherryChecker.isNullOrEmpty(campInfo.get(CampConstants.STOCK_FROMDATE))){
					order.put(CampConstants.BILL_STATE, CampConstants.BILL_STATE_AR);
					// 更新活动预约主单据
					updCampOrder(comMap,order);
				}else{
					String sendPos = ConvertUtil.getString(order.get("sendPos"));
					if(!"0".equals(sendPos)){
						String billNo = ConvertUtil.getString(order.get(CampConstants.BILL_NO));
						// 发送活动单据MQ-->POS
						sendPOSMQ(comMap, billNo);
					}
				}
			}else{
				String mobilePhone = ConvertUtil.getString(order.get("mobilePhone"));
				logger.error(">>>>>>>>>>>>>>生成会员单据领用日期失败【手机号="+mobilePhone+"】>>>>>>>>>>>>>>>");
				return 10;
			}
		} else {
			// 更新活动预约主单据
			updCampOrder(comMap,order);
		}
		return result;
	}
	
	/**
	 * MQ操作
	 * 
	 * @param map
	 * @throws CherryException
	 */
	@Override
	public int makeCampOrder(Map<String, Object> comMap,Map<String, Object> campInfo,
			Map<String, Object> order) throws CherryException {
		int result = CherryConstants.SUCCESS;
		// 设置共通参数MAP
		setComMap(comMap);
		// 业务日期
		String busDate = ConvertUtil.getString(comMap.get(CherryConstants.BUSINESS_DATE));
		// ======设置单据领用日期范围========
		List<Map<String, Object>> orderList = new ArrayList<Map<String,Object>>();
		orderList.add(order);
		campInfo.put(CampConstants.OPT_TIME, comMap.get(CampConstants.OPT_TIME));
		setObtainDate(campInfo, busDate, orderList);
		// 设置领用柜台号
		if(CherryChecker.isNullOrEmpty(order.get("counterCode"))){
			String gotCounter = ConvertUtil.getString(campInfo.get("gotCounter"));
			 if("1".equals(gotCounter)){// 任意柜台
				order.put("counterCode", "ALL");
			}else if("2".equals(gotCounter)){//发卡柜台
				order.put("counterCode", order.get("cntCodeBelong"));
			}else if("3".equals(gotCounter)){//预约柜台
				order.put("counterCode", order.get("orderCntCode"));
			}else if("4".equals(gotCounter)){// 首次购买柜台
				order.put("counterCode", order.get("firstSaleCounterCode"));
			}
		}
		if(CherryChecker.isNullOrEmpty(order.get("counterCode"))){
			logger.error(">>>>>>>>>>>>>>领用柜台号为空>>>>>>>>>>>>>>>");
			return 11;
		}
		order.putAll(campInfo);
		// 新增单据
		addOrder(comMap, order);
		String sendPos = ConvertUtil.getString(order.get("sendPos"));
		if(!"0".equals(sendPos)){
			String billNo = ConvertUtil.getString(order.get(CampConstants.BILL_NO));
			// 发送活动单据MQ-->POS
			sendPOSMQ(comMap, billNo);
		}
		return result;
	}

	/**
	 * 更新活动单据
	 * @param comMap
	 * @param order
	 */
	@Override
	public void updCampOrder(Map<String,Object> comMap,Map<String,Object> order){
		// 更新活动预约主单据
		order.putAll(comMap);
		ser.updCampOrder(order);
		List<Map<String,Object>> hisList = ser.getCampHistory(order);
		Object state = order.get(CampConstants.BILL_STATE);
		Object optTime = order.get(CampConstants.OPT_TIME);
		if(null != hisList && hisList.size() > 0){
			for(Map<String,Object> his : hisList){
				his.put(CampConstants.BILL_STATE,state);
				his.put(CampConstants.OPT_TIME,optTime);
				his.putAll(comMap);
			}
			ser.addCampHistory(hisList);
		}
		String sendPos = ConvertUtil.getString(order.get("sendPos"));
		if(!"0".equals(sendPos)){
			String billNo = ConvertUtil.getString(order.get(CampConstants.BILL_NO));
			// 发送活动单据MQ-->POS
			sendPOSMQ(comMap, billNo);
		}
	}
	/**
	 * 新后台批量操作
	 * 
	 * @param dto
	 * @throws CherryException
	 */
	@Override
	public int tran_campOrderBAT(Map<String, Object> comMap,
			Map<String, Object> campInfo, String billState,
			List<Map<String, Object>> orderList) throws CherryException {
		int result = CherryConstants.SUCCESS;
		// 业务日期
		String busDate = ConvertUtil.getString(comMap.get(CherryConstants.BUSINESS_DATE));
		
		if (null != orderList && orderList.size() > 0) {
			int brandInfoId = ConvertUtil.getInt(comMap.get(CherryConstants.BRANDINFOID));
			// 设置共通参数MAP
			setComMap(comMap);
			String dataChannel = ConvertUtil.getString(comMap.get(CampConstants.DATA_CHANNEL));
			if("".equals(dataChannel)){
				// 预约渠道
				comMap.put(CampConstants.DATA_CHANNEL, CampConstants.DATA_CHANNEL_2);	
			}
			// 单据操作状态
			comMap.put(CampConstants.BILL_STATE, billState);
			if (CampConstants.BILL_STATE_RV.equals(billState)) {// 预约
				// 子活动码
				String subCampCode = ConvertUtil.getString(campInfo.get(CampConstants.SUBCAMP_CODE));
				// 活动总上限
				int topLimit= ConvertUtil.getInt(campInfo.get(CampConstants.TOPLIMIT));
				// 单个会员预约次数上限
				int times = ConvertUtil.getInt(campInfo.get(CampConstants.TIMES));
				if(times != 0){
					Map<String,Object> p = new HashMap<String, Object>();
					p.put(CherryConstants.BRANDINFOID, brandInfoId);
					p.put(CampConstants.SUBCAMP_CODE, subCampCode);
					for(int i=0; i< orderList.size(); i ++){
						Map<String, Object> order = orderList.get(i);
						p.put("messageId", order.get("messageId"));
						p.put("mobilePhone", order.get("mobilePhone"));
						p.put("memCode", order.get("memCode"));
						try {
							setHisParamMap(campInfo,busDate,p);
						} catch (JSONException e) {
							return 9;
						}
						int orderCnt = ser.getOrderCount(p);
						if(orderCnt >= times){
							logger.warn(">>>>>>>>>>>>>>【"+order.get("memCode")+","+order.get("mobilePhone")+"】预约次数大于活动上限=" + times);
							orderList.remove(i);
							i--;
						}
					}
				}
				if(topLimit != 0){
					Map<String,Object> p = new HashMap<String, Object>();
					p.put(CherryConstants.BRANDINFOID, brandInfoId);
					p.put(CampConstants.SUBCAMP_CODE, campInfo.get(CampConstants.SUBCAMP_CODE));
					int orderCnt = ser.getOrderCount(p);
					// 能够继续预约的次数
					int cnt = topLimit - orderCnt;
					if(cnt <= 0){
						// 预约已达活动上限
						logger.error(">>>>>>>>>>>>>>预约单据总数已达活动上限=" + topLimit);
						return 4;
					}else{
						// 能够继续预约的次数 < 本次预约次数
						if(cnt < orderList.size()){
							orderList = orderList.subList(0, cnt);
						}
					}
				}
				campInfo.put(CampConstants.ISSTOCK, comMap.get(CampConstants.ISSTOCK));
				// 单据操作状态
				campInfo.put(CampConstants.BILL_STATE, billState);
				// ======设置单据领用日期范围========
	
				campInfo.put(CampConstants.OPT_TIME, comMap.get(CampConstants.OPT_TIME));
				setObtainDate(campInfo, busDate, orderList);
				
				try{
					// 插入活动预约主单据
					addOrderBAT(comMap, campInfo, orderList);
					// 如果当前活动无备货阶段-更新单据状态为“可领用”
					if(CherryChecker.isNullOrEmpty(campInfo.get(CampConstants.STOCK_FROMDATE))){
						for(Map<String,Object> order : orderList){
							order.put(CampConstants.BILL_STATE, CampConstants.BILL_STATE_AR);
						}
						// 更新活动预约主单据
						ser.updCampOrder(orderList);
						// 备份活动预约主单据
						backupOrderBAT(orderList);
					}
					// commit
					ser.manualCommit();
				} catch (Exception e) {
					result = CherryConstants.WARN;
					// rollback
					ser.manualRollback();
					logger.error(e.getMessage(),e);
				}
			} else {
				for (Map<String, Object> order : orderList) {
					order.putAll(comMap);
				}
				try {
					// 更新活动预约主单据
					ser.updCampOrder(orderList);
					// 备份活动预约主单据
					backupOrderBAT(orderList);
					// commit
					ser.manualCommit();
				} catch (Exception e) {
					result = CherryConstants.WARN;
					// rollback
					ser.manualRollback();
					logger.error(e.getMessage(),e);
				}
			}
			// 发送MQ--> POS
			int r = sendPOSMQ(comMap, null);
			if(r > result){
				result = r;
			}
		}
		return result;
	}

	/**
	 * 生成单据号
	 * 
	 * @param dto
	 * @param campInfo
	 */
	@SuppressWarnings("unchecked")
	private void addOrder(Map<String, Object> comMap,Map<String, Object> order)
			throws CherryException {
		List<Map<String, Object>> list = (List<Map<String, Object>>) order
				.get(CampConstants.KEY_LIST);
		String mqWaitFlag = ConvertUtil.getString(order.get("mqWaitFlag"));
		String couponPrefix = ConvertUtil.getString(comMap.get("CouponPrefix"));
		if("".equals(mqWaitFlag)){
			mqWaitFlag = "1";
		}
		if (null != list) {
			// 总积分
			float sumPoint = 0;
			// 总数量
			int sumQuantity = 0;
			// 总金额
			float sumAmount = 0;
			// 是否管理库存
//			String isStock = cm14bl.getConfigValue("1036",
//					ConvertUtil.getString(comMap
//							.get(CherryConstants.ORGANIZATIONINFOID)),
//					ConvertUtil.getString(comMap
//							.get(CherryConstants.BRANDINFOID)));
			String isStock = "1";

			for (Map<String, Object> detail : list) {
				int quantity = ConvertUtil.getInt(detail
						.get(CampConstants.QUANTITY));
				float exPoint = ConvertUtil.getFloat(detail
						.get(CampConstants.EXPOINT));
				float price = ConvertUtil.getFloat(detail
						.get(CampConstants.PRICE));
				String dIsStock = ConvertUtil.getString(detail
						.get(CampConstants.ISSTOCK));
				if(!"0".equals(dIsStock)){
					sumQuantity += quantity;
				}
				sumPoint += exPoint * quantity;
				sumAmount += price * quantity;
			}
			order.put(CampConstants.ISSTOCK, isStock);
			order.put(CampConstants.SUMQUANTITY, sumQuantity);
			order.put(CampConstants.SUMAMOUT, sumAmount);
			order.put(CampConstants.SUMPOINT, sumPoint);
			order.put("mqWaitFlag", mqWaitFlag);
			order.putAll(comMap);
			// 设置操作年-月-日
			setOptionDate(order);
			// 活动验证方式
			String subCampValid = ConvertUtil.getString(order
					.get(CampConstants.SUBCAMPAIGN_VALID)).trim();
			// 在线验证
			if ("2".equals(subCampValid)) {
				String couponCode = ConvertUtil.getString(order.get(CampConstants.COUPON_CODE));
				if("".equals(couponCode)){
					Map<String,Object> param = new HashMap<String, Object>(comMap);
					param.put(CampConstants.CAMP_CODE,order.get(CampConstants.CAMP_CODE));
					param.put("couponCount", 1);
					try {
						// 批量生成COUPON码
						List<String> couponList = cpnIF.generateCoupon(param);
						order.put(CampConstants.COUPON_CODE, couponPrefix + couponList.get(0));
					} catch (Exception e) {
						logger.error("===========生成COUPON码Exception===========");
						logger.error(e.getMessage(),e);
						logger.error("===========================================");
						// 获取COUPON码异常
						throw new CherryException("ACT00023");
					}
				}
			}
			int campOrderId = ser.addCampOrder(order);
			for (Map<String, Object> detail : list) {
				detail.put(CampConstants.CAMPORDERID, campOrderId);
				detail.putAll(comMap);
			}
			ser.addCampOrdDetail(list);
			backupOrder(order);
		}
	}
	/**
	 * 生成单据号
	 * 
	 * @param dto
	 * @param campInfo
	 */
	@SuppressWarnings("unchecked")
	private void setOrderList(Map<String, Object> comMap,
			Map<String, Object> campInfo, List<Map<String, Object>> orderList)
			throws CherryException {
		Map<String, Object> paramMap = new HashMap<String, Object>(comMap);
		//
		String tradeType = ConvertUtil.getString(campInfo.get("tradeType"));
		// 批量生成单据号
		List<String> orderNoList = cm03bl.getTicketNumberList(paramMap,
				tradeType, orderList.size());
		// 批量生成COUPON码
		List<String> couponList = null;
		// 活动验证方式
		String subCampValid = ConvertUtil.getString(campInfo
				.get(CampConstants.SUBCAMPAIGN_VALID)).trim();
		// 在线验证
		if ("2".equals(subCampValid)) {
			paramMap.put("couponCount", orderList.size());
			paramMap.put(CampConstants.CAMP_CODE,
					campInfo.get(CampConstants.CAMP_CODE));
			try {
				couponList = cpnIF.generateCoupon(paramMap);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				// 获取COUPON码异常
				throw new CherryException("ACT00023");
			}
		}
		for (int i = 0; i < orderList.size(); i++) {
			Map<String, Object> orderMap = orderList.get(i);
			orderMap.put(CampConstants.BILL_NO, orderNoList.get(i));
			if (null != couponList) {
				orderMap.put(CampConstants.COUPON_CODE, couponList.get(i));
			}
			orderMap.putAll(campInfo);
			orderMap.putAll(comMap);
			// 设置操作年-月-日
			setOptionDate(orderMap);
			orderMap.put("sendFlag", "3");
			orderMap.put("mqWaitFlag", "1");
			// 总积分
			float sumPoint = 0;
			// 总数量
			int sumQuantity = 0;
			// 总金额
			float sumAmount = 0;

			// 预约礼品List
			List<Map<String, Object>> list = (List<Map<String, Object>>) orderMap
					.get(CampConstants.KEY_LIST);
			for (Map<String, Object> res : list) {
				int quantity = ConvertUtil.getInt(res
						.get(CampConstants.QUANTITY));
				float exPoint = ConvertUtil.getFloat(res
						.get(CampConstants.EXPOINT));
				float price = ConvertUtil
						.getFloat(res.get(CampConstants.PRICE));
				String dIsStock = ConvertUtil.getString(res
						.get(CampConstants.ISSTOCK));
				if(!"0".equals(dIsStock)){
					sumQuantity += quantity;
				}
				sumPoint += exPoint * quantity;
				sumAmount += price * quantity;
				res.put(CampConstants.BILL_NO, orderNoList.get(i));
				res.put(CampConstants.EXPOINT, exPoint);
			}
			orderMap.put(CampConstants.SUMQUANTITY, sumQuantity);
			orderMap.put(CampConstants.SUMAMOUT, sumAmount);
			orderMap.put(CampConstants.SUMPOINT, sumPoint);
		}
	}

	/**
	 * 批量插入活动预约主单据
	 * 
	 * @param dto
	 * @param campInfo
	 */
	@SuppressWarnings("unchecked")
	private void addOrderBAT(Map<String, Object> comMap,
			Map<String, Object> campInfo, List<Map<String, Object>> orderList)
			throws CherryException {
		Map<String, Object> map = new HashMap<String, Object>(comMap);
		// 生成单据号及单据明细List
		setOrderList(comMap, campInfo, orderList);
		List<Map<String, Object>> detailList = new ArrayList<Map<String, Object>>();
		for (Map<String, Object> order : orderList) {
			List<Map<String, Object>> list = (List<Map<String, Object>>) order
					.get(CampConstants.KEY_LIST);
			detailList.addAll(list);
		}
		// 插入活动预约主单据
		ser.addCampOrder(orderList);
		map.put(CampConstants.CAMP_CODE, campInfo.get(CampConstants.CAMP_CODE));
		// 返回预约主单据ID
		List<Map<String, Object>> campOrderIdList = ser
				.getCampOrderIdList(map);
		// 更新会员活动预约信息下发状态【临时状态-->未下发】
		ser.updCampOrderSendFlag(map);
		for (Map<String, Object> detail : detailList) {
			String billNo = ConvertUtil.getString(detail
					.get(CampConstants.BILL_NO));
			// 追加主单据ID
			for (Map<String, Object> idInfo : campOrderIdList) {
				String no = ConvertUtil.getString(idInfo
						.get(CampConstants.BILL_NO));
				if (billNo.equals(no)) {
					detail.putAll(idInfo);
					break;
				}
			}
			detail.putAll(comMap);
		}
		// 插入活动预约明细数据
		ser.addCampOrdDetail(detailList);
		// 备份活动预约主单据
		backupOrderBAT(orderList);
	}

	/**
	 * 备份主单据
	 * 
	 * @param orderList
	 */
	private void backupOrderBAT(List<Map<String, Object>> orderList) {
		List<Map<String, Object>> backupList = new ArrayList<Map<String, Object>>();
		for (Map<String, Object> order : orderList) {
			getBackupList(backupList, order);
		}
		ser.addCampHistory(backupList);
	}

	/**
	 * 备份主单据
	 * 
	 * @param order
	 */
	private void backupOrder(Map<String, Object> order) {
		List<Map<String, Object>> backupList = new ArrayList<Map<String, Object>>();
		getBackupList(backupList, order);
		ser.addCampHistory(backupList);
	}

	/**
	 * 取得单据备份信息List
	 * 
	 * @param backupList
	 * @param order
	 */
	@SuppressWarnings("unchecked")
	private void getBackupList(List<Map<String, Object>> backupList,
			Map<String, Object> order) {
		// 明细List
		List<Map<String, Object>> detailList = (List<Map<String, Object>>) order
				.get(CampConstants.KEY_LIST);
		List<String> keyList = ConvertUtil.getKeyList(detailList,
				CampConstants.SUBCAMP_CODE);
		if (keyList.size() > 0) {
			for (String subCampCode : keyList) {
				if(!CampConstants.SUBCAMP_CODE_DEF.equals(subCampCode)){
					Map<String, Object> item = new HashMap<String, Object>(order);
					item.put(CampConstants.SUBCAMP_CODE, subCampCode);
					backupList.add(item);
				}
			}
		}
	}

	/**
	 * 根据活动档次mainCode取得主题活动信息
	 * 
	 * @param subCampCode
	 * @return
	 */
	@Override
	public Map<String, Object> getCampInfo(String subCampCode) {
		return ser.getCampInfo(subCampCode);
	}

	private int sendPointMQ(List<Map<String, Object>> orderList,
			Map<String, Object> comMap) throws Exception {
		int result = CherryConstants.SUCCESS;
		if(null !=orderList && orderList.size() > 0){
			int size = orderList.size();
			// MQ单据号LIST
			List<String> billNoList = cm03bl.getTicketNumberList(comMap,
						CherryConstants.MESSAGE_TYPE_PT, size);
			for(int i=0; i < size; i++){
				Map<String, Object> order = orderList.get(i);
				try {
					sendPointMQ(order,billNoList.get(i),comMap);
				} catch (Exception e) {
					orderList.remove(i);
					i--;
					logger.error(e.getMessage(),e);
					result = CherryConstants.WARN;
				}
			}
			// 更新单据MQ发送等待标记
			ser.updCampOrderMQ(orderList);
		}
		return result;
	}

	/**
	 * 发送积分MQ
	 * 
	 * @param order
	 * @param billCode
	 * @param comMap
	 * @throws Exception
	 */
	private void sendPointMQ(Map<String, Object> order, String billCode,
			Map<String, Object> comMap) throws Exception {
		
		int orgId = ConvertUtil.getInt(comMap
				.get(CherryConstants.ORGANIZATIONINFOID));
		int brandInfoId = ConvertUtil.getInt(comMap
				.get(CherryConstants.BRANDINFOID));
		String orgCode = ConvertUtil.getString(comMap
				.get(CherryConstants.ORG_CODE));
		String brandCode = ConvertUtil.getString(comMap
				.get(CherryConstants.BRAND_CODE));
		String sysTime = ConvertUtil.getString(comMap
				.get("sysTime"));
		String billState = ConvertUtil.getString(order
				.get(CampConstants.BILL_STATE));
		if("".equals(sysTime)){
			sysTime = ser.getSYSDate();
		}
		// 设定MQ消息DTO
		MQInfoDTO mq = new MQInfoDTO();
		// 品牌代码
		mq.setBrandCode(brandCode);
		// 组织代码
		mq.setOrgCode(orgCode);
		// 组织ID
		mq.setOrganizationInfoId(orgId);
		// 品牌ID
		mq.setBrandInfoId(brandInfoId);
		// 
		mq.setInsertTime(sysTime);
		// 单据类型
		String billType = CherryConstants.MESSAGE_TYPE_PT;
		// 业务类型
		mq.setBillType(billType);
		// 单据号
		mq.setBillCode(billCode);
		// 消息发送队列名
		mq.setMsgQueueName(CherryConstants.CHERRYPOINTMSGQUEUE);
		// 消息的主数据行
		Map<String, Object> mainData = new HashMap<String, Object>();
		// 品牌代码
		mainData.put("BrandCode", brandCode);
		// 业务类型
		mainData.put("TradeType", billType);
		// 单据号
		mainData.put("TradeNoIF", billCode);
		// 操作柜台号
		mainData.put("CounterCode", order.get("orderCntCode"));
		// 关联单号
		mainData.put("RelevantNo", order.get(CampConstants.BILL_NO));
		// 数据来源
		mainData.put("Sourse", "Cherry");
		// 积分维护方式 2：修改积分差值
		mainData.put("SubTradeType", "2");
		if(CampConstants.BILL_STATE_CA.equals(billState)){
			// 积分类型:积分兑换预约取消
			mainData.put("MaintainType", "7");
		} else {
			// 积分类型:积分兑换预约
			mainData.put("MaintainType", "5");
		}
		// 设定消息的数据行
		Map<String, Object> dataLine = new HashMap<String, Object>();
		dataLine.put("MainData", mainData);
		// 积分明细
		dataLine.put("DetailDataDTOList", getPointDataList(order,comMap));
		// 设定消息内容
		Map<String, Object> msgDataMap = new HashMap<String, Object>();
		// 设定消息版本号
		msgDataMap.put("Version", CherryConstants.MESSAGE_VERSION_PT);
		// 设定消息命令类型
		msgDataMap.put("Type", CherryConstants.MESSAGE_TYPE_1004);
		// 设定消息数据类型
		msgDataMap.put("DataType", MessageConstants.DATATYPE_APPLICATION_JSON);
		msgDataMap.put("DataLine", dataLine);
		mq.setMsgDataMap(msgDataMap);
		// 设定插入到MongoDB的信息
		DBObject dbObject = new BasicDBObject();
		// 组织代码
		dbObject.put("OrgCode", orgCode);
		// 品牌代码
		dbObject.put("BrandCode", brandCode);
		// 业务类型
		dbObject.put("TradeType", billType);
		// 单据号
		dbObject.put("TradeNoIF", billCode);
		// 单据号
		dbObject.put("OccurTime", sysTime);
		// 数据来源
		dbObject.put("Sourse", "Cherry");
		mq.setDbObject(dbObject);
		// 发送MQ消息
		binOLMQCOM01_BL.sendMQMsg(mq);
	}

	/**
	 * 取得积分维护会员List
	 * 
	 * @param orderList
	 * @return
	 */
	private List<Map<String, Object>> getPointDataList(
			Map<String, Object> order,Map<String, Object> comMap) {
		// 积分维护明细数据
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		String state = ConvertUtil.getString(order
				.get(CampConstants.BILL_STATE));
		Map<String, Object> map = new HashMap<String, Object>();
		float sumPoint = ConvertUtil.getFloat(order
				.get(CampConstants.SUMPOINT));
		if (CampConstants.BILL_STATE_CA.equals(state)) {
			// 修改的积分
			map.put("ModifyPoint", sumPoint);
			map.put("Reason", "积分活动预约取消");
		} else {
			map.put("ModifyPoint", -sumPoint);
			map.put("Reason", "积分活动预约");
		}
		// 会员卡号
		map.put("MemberCode", order.get("memCode"));
		// 业务时间
		map.put("BusinessTime", order.get("orderTime"));
		// 员工Code
		map.put("EmployeeCode", comMap.get("employeeCode"));
		// 会员俱乐部ID
		map.put("MemberClubId", order.get("memberClubId"));
		
		list.add(map);
		return list;
	}

	/**
	 * 发送积分维护MQ
	 * @param comMap
	 * @param campInfo
	 * @return
	 * @throws Exception
	 */
	@Override
	public int sendPointMQ(Map<String, Object> comMap,Map<String, Object> campInfo) throws Exception{
		int result = CherryConstants.SUCCESS;
		String subCampType = ConvertUtil.getString(campInfo.get(CampConstants.SUBCAMP_TYPE));
		String exPointDeductFlag = ConvertUtil.getString(campInfo.get("exPointDeductFlag"));
		// 预约阶段扣减积分
		if(CampConstants.SUBCAMP_TYPE_PX.equals(subCampType) && "1".equals(exPointDeductFlag)){
			Map<String,Object> p = new HashMap<String, Object>();
			p.put(CherryConstants.BRANDINFOID, comMap.get(CherryConstants.BRANDINFOID));
			// 终端MQ时
			if(null != campInfo.get(CampConstants.BILL_NO)){
				p.put(CampConstants.BILL_NO, campInfo.get(CampConstants.BILL_NO));
			}else{
				p.put(CampConstants.CAMP_CODE, campInfo.get(CampConstants.CAMP_CODE));
			}
			int from = 1;
			while (true) {
				p.put("SORT_ID", CampConstants.BILL_NO);
				p.put("START", from);
				p.put("END", from + CherryConstants.BATCH_PAGE_MAX_NUM - 1);
				List<Map<String,Object>> billList = ser.getMqMemList(p);
				int size = billList.size();
				if(null != billList && size > 0){
					// 发送积分MQ
					int r = sendPointMQ(billList, comMap);
					if (r > result) {
						result = r;
					}
					if(size < CherryConstants.BATCH_PAGE_MAX_NUM){
						break;
					}
					// 开始索引跳过失败的单据
					from += size - billList.size();
				}else{
					break;
				}
			}
		}
		return result;
	}
	
	/**
	 * 发送预约单据MQ到老后台
	 * 
	 * @param order
	 */
	@SuppressWarnings("unchecked")
	public int sendPOSMQ(Map<String, Object> comMap,String billNo) {
		int result = CherryConstants.SUCCESS;
		String couponFlag =  ConvertUtil.getString(comMap.get("couponFlag"));
		Map<String, Object> map = new HashMap<String, Object>();
		if(CherryChecker.isNullOrEmpty(billNo)){
			map.put(CampConstants.BATCHNO, comMap.get(CampConstants.BATCHNO));
		}else{
			map.put(CampConstants.BILL_NO, billNo);
		}
		List<Map<String, Object>> orderList = ser.getOrderList(map);
		if(null != orderList){
			for(Map<String, Object> order : orderList){
				int orgId = ConvertUtil.getInt(comMap
						.get(CherryConstants.ORGANIZATIONINFOID));
				int brandInfoId = ConvertUtil.getInt(comMap
						.get(CherryConstants.BRANDINFOID));
				String orgCode = ConvertUtil.getString(comMap
						.get(CherryConstants.ORG_CODE));
				String brandCode = ConvertUtil.getString(comMap
						.get(CherryConstants.BRAND_CODE));
				String sysTime = ConvertUtil.getString(comMap.get("sysTime"));
				// 单据号
				String billCode = ConvertUtil.getString(order.get(CampConstants.BILL_NO));
				// 单据渠道
				String sourse = ConvertUtil.getString(order.get(CampConstants.DATA_CHANNEL));
				String orderDate = ConvertUtil.getString(order.get("orderDate"));
				String orderTime = ConvertUtil.getString(order.get("orderTime"));
				String getFromTime = ConvertUtil.getString(order.get("getFromTime"));
				String getToTime = ConvertUtil.getString(order.get("getToTime"));
				String sumExPoint = ConvertUtil.getString(order.get("sumExPoint"));
				String msgId = ConvertUtil.getString(order.get("msgId"));
				String modifyCount = ConvertUtil.getString(order.get("modifyCount"));
				String orderCntCode = ConvertUtil.getString(order.get("orderCntCode"));
				String tradeType = ConvertUtil.getString(order.get("tradeType"));
				String subType = ConvertUtil.getString(order.get("subType"));
				String campaignCode = ConvertUtil.getString(order.get("campaignCode"));
				String couponCode = ConvertUtil.getString(order.get("couponCode"));
				String state = ConvertUtil.getString(order.get("state"));
				String memCode = ConvertUtil.getString(order.get("memCode"));
				String mobile = ConvertUtil.getString(order.get("mobile"));
				String telephone = ConvertUtil.getString(order.get("telephone"));
				String cntGot = ConvertUtil.getString(order.get("cntGot"));
				String isStock = ConvertUtil.getString(order.get("isStock"));
				String priceControl = ConvertUtil.getString(order.get("priceControl"));
				String saleBatchNo = ConvertUtil.getString(order.get("saleBatchNo"));
				String needBuyFlag = ConvertUtil.getString(order.get("needBuyFlag"));
				String subCampValid = ConvertUtil.getString(order.get("subCampValid"));
				String name = ConvertUtil.getString(order.get("name"));
				String quantity = ConvertUtil.getString(order.get("quantity"));
				String amout = ConvertUtil.getString(order.get("amout"));
				String deliveryState = ConvertUtil.getString(order.get("deliveryState"));
				String deliveryType = ConvertUtil.getString(order.get("deliveryType"));
				String deliveryAddress = ConvertUtil.getString(order.get("deliveryAddress"));
				String relationNo = ConvertUtil.getString(order.get("relationNo"));
				
				String receiverName = ConvertUtil.getString(order.get("receiverName"));
				String receiverMobile = ConvertUtil.getString(order.get("receiverMobile"));
				String deliveryProvince = ConvertUtil.getString(order.get("deliveryProvince"));
				String deliveryCity = ConvertUtil.getString(order.get("deliveryCity"));
				String deliveryCounty = ConvertUtil.getString(order.get("deliveryCounty"));
				
				if("".equals(couponCode) && "1".equals(couponFlag)){
					couponCode = mobile;
				}
				if("".equals(sysTime)){
					sysTime = ser.getSYSDate();
				}
				// 设定MQ消息DTO
				MQInfoDTO mq = new MQInfoDTO();
				// 品牌代码
				mq.setBrandCode(brandCode);
				// 组织代码
				mq.setOrgCode(orgCode);
				// 组织ID
				mq.setOrganizationInfoId(orgId);
				// 品牌ID
				mq.setBrandInfoId(brandInfoId);
				// 
				mq.setInsertTime(sysTime);
				// 业务类型
				mq.setBillType(tradeType);
				// 单据号
				mq.setBillCode(billCode);
				// JMS协议头中的JMSGROUPID
				mq.setJmsGroupId(brandCode + billCode.substring(billCode.length()-1));
				// 修改回数
				mq.setSaleRecordModifyCount(ConvertUtil.getInt(modifyCount));
				// 消息发送队列名
				mq.setMsgQueueName(CherryConstants.CHERRYTOPOSSP);
				// 消息的主数据行
				Map<String, Object> mainData = new HashMap<String, Object>();
				// 品牌代码
				mainData.put("BrandCode", brandCode);
				// 单据号
				mainData.put("TradeNoIF", billCode);
				// 修改回数
				mainData.put("ModifyCounts", modifyCount);
				// 预约柜台号
				mainData.put("CounterCode", orderCntCode);
				// 业务类型
				mainData.put("TradeType", tradeType);
				// 子类型
				mainData.put("SubType", subType);
				// 关联单号
				mainData.put("RelevantNo", billCode);
				// 主活动代号
				mainData.put("CampaignCode",campaignCode);
				// Coupon码
				mainData.put("CouponCode", couponCode);
				// 预约日期
				mainData.put("BookDate",orderDate);
				// 预约时间
				mainData.put("BookTime", orderTime);
				// 领用开始日期
				mainData.put("GetFromDate", getFromTime);
				// 领用结束日期
				mainData.put("GetToDate", getToTime);
				// 本次预约扣除的总积分
				mainData.put("PointDeducted", sumExPoint);
				// 单据状态
				mainData.put("State", state);
				// 会员号
				mainData.put("MemberCode", memCode);
				mainData.put("Mobile", mobile);
				mainData.put("Telephone", telephone);
				mainData.put("Weixin",msgId);
				// 领取柜台
				mainData.put("CounterGotCode", cntGot);
				// 数据来源
				mainData.put("Data_source", sourse);
				// 是否管理库存
				mainData.put("IsStock", isStock);
				// 购买金额条件
				mainData.put("PriceControl", priceControl);
				mainData.put("SaleBatchNo", saleBatchNo);
				// 是否需要购买
				mainData.put("NeedBuyFlag", needBuyFlag);
				// 单据所属人姓名
				mainData.put("Name", name);
				// 单据校验方式
				mainData.put("SubCampValid", subCampValid);
				// 总数量
				mainData.put("Quantity", quantity);
				// 总金额
				mainData.put("Amout", amout);
				mainData.put("DeliveryState", deliveryState);
				mainData.put("DeliveryType", deliveryType);
				mainData.put("DeliveryAddress", deliveryAddress);
				mainData.put("RelationNo", relationNo);
				
				mainData.put("ReceiverName", receiverName);
				mainData.put("ReceiverMobile", receiverMobile);
				mainData.put("DeliveryProvince", deliveryProvince);
				mainData.put("DeliveryCity", deliveryCity);
				mainData.put("DeliveryCounty", deliveryCounty);
				// 会员俱乐部代号
				String clubCode = ConvertUtil.getString(order.get("clubCode"));
				if (!"".equals(clubCode)) {
					mainData.put("clubCode", clubCode);
				}
				// 设定消息的数据行
				Map<String, Object> dataLine = new HashMap<String, Object>();
				dataLine.put("MainData", mainData);
				
				List<Map<String, Object>> list = (List<Map<String, Object>>)order.get(CampConstants.KEY_LIST);
				if(null != list && list.size() > 0){
					for(Map<String, Object> d : list){
						d.put("TradeNoIF", billCode);
						d.put("ModifyCounts", modifyCount);
						d.put("ActivityQuantity", "");
					}
					// 数据明细
					dataLine.put("DetailDataDTOList", list);
				}
				// 设定消息内容
				Map<String, Object> msgDataMap = new HashMap<String, Object>();
				// 设定消息版本号
				msgDataMap.put("Version", "AMQ.012.001");
				// 设定消息数据类型
				msgDataMap.put("DataType", MessageConstants.DATATYPE_APPLICATION_JSON);
				msgDataMap.put("DataLine", dataLine);
				mq.setMsgDataMap(msgDataMap);
				// 设定插入到MongoDB的信息
				DBObject dbObject = new BasicDBObject();
				// 组织代码
				dbObject.put("OrgCode", orgCode);
				// 品牌代码
				dbObject.put("BrandCode", brandCode);
				// 业务类型
				dbObject.put("TradeType", tradeType);
				// 单据号
				dbObject.put("TradeNoIF", billCode);
				// 系统时间
				dbObject.put("OccurTime", sysTime);
				// 修改回数
				dbObject.put("ModifyCounts", modifyCount);
				// 数据来源
				dbObject.put("Source", sourse);
				mq.setDbObject(dbObject);
				// 发送MQ消息
				try {
					binOLMQCOM01_BL.sendMQMsg(mq);
				} catch (Exception e) {
					result = CherryConstants.ERROR;
					logger.info("=========发送MQ[CHERRY2POS]失败=========");
					logger.error("=========发送MQ[CHERRY2POS]失败=========");
					logger.error(e.getMessage(),e);
					logger.error("=======================================");
				}
			}
		}
		// 更新单据为已下发
		ser.updCampOrderSendFlag1(orderList);
		return result;
	}

	/**
	 * 发送沟通MQ
	 * @param map
	 * @param batchNo
	 * @param eventType
	 * @return
	 */
	@Override
	public int sendGTMQ(Map<String,Object> comMap, String batchNo, String billState){
		int result = CherryConstants.SUCCESS;
		int orgId = ConvertUtil.getInt(comMap
				.get(CherryConstants.ORGANIZATIONINFOID));
		int brandInfoId = ConvertUtil.getInt(comMap
				.get(CherryConstants.BRANDINFOID));
		boolean saveLog = true;
		String eventType = "";
		String sysConfigGT = "0";
		if(CampConstants.BILL_STATE_RV.endsWith(billState)){
			eventType = "1";
			saveLog = false;
			sysConfigGT = cm14bl.getConfigValue("1081", orgId+"", brandInfoId+"");
		}else if(CampConstants.BILL_STATE_AR.endsWith(billState)){
			eventType = "2";
			sysConfigGT = cm14bl.getConfigValue("1083", orgId+"", brandInfoId+"");
		}else if(CampConstants.BILL_STATE_CA.endsWith(billState)){
			eventType = "4";
			saveLog = false;
			sysConfigGT = cm14bl.getConfigValue("1082", orgId+"", brandInfoId+"");
		}
		if("1".equals(sysConfigGT)){
			logger.info("=========发送沟通MQ开始：batchNo=" + batchNo + ",eventType="+eventType+"=========");
			String orgCode = ConvertUtil.getString(comMap
					.get(CherryConstants.ORG_CODE));
			String brandCode = ConvertUtil.getString(comMap
					.get(CherryConstants.BRAND_CODE));
			String sysTime = ConvertUtil.getString(comMap
					.get("sysTime"));
			if("".equals(sysTime)){
				sysTime = ser.getSYSDate();
			}
			// 设定MQ消息DTO
			MQInfoDTO mq = new MQInfoDTO();
			// 品牌代码
			mq.setBrandCode(brandCode);
			// 组织代码
			mq.setOrgCode(orgCode);
			// 组织ID
			mq.setOrganizationInfoId(orgId);
			// 品牌ID
			mq.setBrandInfoId(brandInfoId);
			// 
			mq.setInsertTime(sysTime);
			// 单据类型
			String billType = CherryConstants.MESSAGE_TYPE_ES;
			// 业务类型
			mq.setBillType(billType);
			// 
			String billCode = cm03bl.getTicketNumber(orgId, brandInfoId, "", billType);
			// 单据号
			mq.setBillCode(billCode);
			// 消息发送队列名
			mq.setMsgQueueName(CherryConstants.CHERRYEVENTSCHEDULEMSGQUEUE);
			// 消息的主数据行
			Map<String, Object> mainData = new HashMap<String, Object>();
			// 品牌代码
			mainData.put("BrandCode", brandCode);
			// 业务类型
			mainData.put("TradeType", billType);
			// 单据号
			mainData.put("TradeNoIF", billCode);
			// 事件类型
			mainData.put("EventType", eventType);
			// 事件ID
			mainData.put("EventId", batchNo);
			// 事件日期
			mainData.put("EventDate", sysTime);
			// 数据来源
			mainData.put("Sourse", "Cherry");
			// 设定消息的数据行
			Map<String, Object> dataLine = new HashMap<String, Object>();
			dataLine.put("MainData", mainData);
			// 设定消息内容
			Map<String, Object> msgDataMap = new HashMap<String, Object>();
			// 设定消息版本号
			msgDataMap.put("Version", CherryConstants.MESSAGE_VERSION_ES);
			// 设定消息命令类型
			msgDataMap.put("Type", CherryConstants.MESSAGE_TYPE_1007);
			// 设定消息数据类型
			msgDataMap.put("DataType", MessageConstants.DATATYPE_APPLICATION_JSON);
			msgDataMap.put("DataLine", dataLine);
			mq.setMsgDataMap(msgDataMap);
			// 设定插入到MongoDB的信息
			DBObject dbObject = new BasicDBObject();
			// 组织代码
			dbObject.put("OrgCode", orgCode);
			// 品牌代码
			dbObject.put("BrandCode", brandCode);
			// 业务类型
			dbObject.put("TradeType", billType);
			// 单据号
			dbObject.put("TradeNoIF", billCode);
			// 事件类型
			dbObject.put("EventType", eventType);
			// 事件ID
			dbObject.put("EventId", batchNo);
			// 单据号
			dbObject.put("OccurTime", sysTime);
			// 数据来源
			dbObject.put("Sourse", "Cherry");
			mq.setDbObject(dbObject);
			// 发送MQ消息
			try {
				binOLMQCOM01_BL.sendMQMsg(mq,saveLog);
			} catch (Exception e) {
				result = CherryConstants.ERROR;
				logger.info("=========发送沟通MQ失败=========");
				logger.error("=========发送沟通MQ失败=========");
				logger.error(e.getMessage(),e);
				logger.error("===============================");
				
			}
		}
		return result;
	}
	
	/**
	 * 设置单据领用日期范围
	 * 
	 * @param obtainRule
	 * @param memList
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	private void setObtainDate(Map<String, Object> campInfo,String busDate,
			List<Map<String, Object>> orderList) throws CherryException {
		String obtainRule = ConvertUtil.getString(campInfo.get(CampConstants.OBTAIN_RULE));
		if (null != obtainRule && !"".equals(obtainRule)) {
			Map<String, Object> rule = null;
			try {
				rule = (Map<String, Object>) JSONUtil.deserialize(obtainRule);
			} catch (JSONException e) {
				logger.info("====JSON【" + obtainRule + "】 to Map ERROR!=====");
				logger.error(e.getMessage(),e);
				throw new CherryException("JSON【" + obtainRule + "】 to Map ERROR!");
			}
			// 移除领用开始结束日期
			campInfo.remove(CampConstants.OBTAIN_TODATE);
			campInfo.remove(CampConstants.OBTAIN_FROMDATE);
			// 参考类型
			String referType = ConvertUtil.getString(rule
					.get(CampConstants.REFER_TYPE));
			// 前/后
			String attrA = ConvertUtil.getString(rule
					.get(CampConstants.ATTR_A));
			// 天/月
			String attrB = ConvertUtil.getString(rule
					.get(CampConstants.ATTR_B));
			// 天/月【有效期】
			String attrC = ConvertUtil.getString(rule
					.get(CampConstants.ATTR_C));
			// 提前或者延后值
			int valA = ConvertUtil.getInt(rule.get(CampConstants.VAL_A));
			// 有效期值
			int valB = ConvertUtil.getInt(rule.get(CampConstants.VAL_B));
			// 业务日期
			Calendar busCal = DateUtil.getCalendar(busDate);
			// 业务年
			int busYear = busCal.get(Calendar.YEAR);
			for (int i=0; i < orderList.size(); i ++) {
				Map<String, Object> order = orderList.get(i);
				Calendar calA = null;
				// 参考会员生日当天
				if (CampConstants.REFER_TYPE_1.equals(referType)) {
					int birthMonth = ConvertUtil.getInt(order
							.get("birthMonth"));
					int birthDay = ConvertUtil
							.getInt(order.get("birthDay"));
					if(birthMonth != 0 && birthDay != 0){
						calA = DateUtil.getCalendar(busYear, birthMonth,birthDay);
					}else{
						String memCode = ConvertUtil.getString(order.get("memCode"));
						orderList.remove(i);
						i--;
						logger.info("====警告：会员【" + memCode + "】 生日信息不完整，被剔除=====");
						continue;
					}
				}
				// 参考会员生日当月
				else if (CampConstants.REFER_TYPE_2.equals(referType)) {
					int birthMonth = ConvertUtil.getInt(order.get("birthMonth"));
					if(birthMonth != 0){
						calA = DateUtil.getCalendar(busYear, birthMonth, 1);
					}else{
						String memCode = ConvertUtil.getString(order.get("memCode"));
						orderList.remove(i);
						i--;
						logger.info("====警告：会员【" + memCode + "】 生日信息不完整，被剔除=====");
						continue;
					}
				}
				// 参考会员入会当天
				else if (CampConstants.REFER_TYPE_3.equals(referType)) {
					String joinDate = ConvertUtil.getString(order
							.get("joinDate"));
					if(!"".equals(joinDate)){
						calA = DateUtil.getCalendar(joinDate);
					}else{
						String memCode = ConvertUtil.getString(order.get("memCode"));
						orderList.remove(i);
						i--;
						logger.info("====警告：会员【" + memCode + "】 入会时间信息不完整，被剔除=====");
						continue;
					}
				}
				// 参考会员入会当月
				else if (CampConstants.REFER_TYPE_4.equals(referType)) {
					String joinDate = ConvertUtil.getString(order
							.get("joinDate"));
					if(!"".equals(joinDate)){
						calA = DateUtil.getCalendar(joinDate.substring(0, 8) + "01");
					}else{
						String memCode = ConvertUtil.getString(order.get("memCode"));
						orderList.remove(i);
						i--;
						logger.info("====警告：会员【" + memCode + "】 入会时间信息不完整，被剔除=====");
						continue;
					}
				}
				// 参考预约日期
				else if (CampConstants.REFER_TYPE_5.equals(referType)) {
					String orderTime = ConvertUtil.getString(campInfo
							.get(CampConstants.OPT_TIME));
					calA = DateUtil.getCalendar(orderTime.split(CherryConstants.SPACE)[0]);
				}
				// 参考系统业务日期月初
				else if (CampConstants.REFER_TYPE_6.equals(referType)) {
					calA =  DateUtil.getCalendar(busCal.get(Calendar.YEAR),busCal.get(Calendar.MONTH) + 1,1);
				}
				// 参考会员升级日期
				else if (CampConstants.REFER_TYPE_7.equals(referType)) {
					String levelAdjustDay = ConvertUtil.getString(order.get("levelAdjustDay"));
					if (!"".equals(levelAdjustDay)) {
						calA = DateUtil.getCalendar(levelAdjustDay.substring(0,10));
					} else {
						String memCode = ConvertUtil.getString(order.get("memCode"));
						orderList.remove(i);
						i--;
						logger.info("====警告：会员【" + memCode + "】 升级时间信息不完整，被剔除=====");
						continue;
					}
				}// 参考会员首次购买日期
				else if (CampConstants.REFER_TYPE_8.equals(referType)) {
					String firstSaleDate = ConvertUtil.getString(order.get("firstSaleDate"));
					if (!"".equals(firstSaleDate)) {
						calA = DateUtil.getCalendar(firstSaleDate.substring(0,10));
					} else {
						String memCode = ConvertUtil.getString(order.get("memCode"));
						orderList.remove(i);
						i--;
						logger.info("====警告：会员【" + memCode + "】 首次购买日期信息不完整，被剔除=====");
						continue;
					}
				}// 参考系统时间
				else if (CampConstants.REFER_TYPE_9.equals(referType)) {
					calA =  busCal;
				}
				if (null != calA) {
					// 提前
					if (CampConstants.ATTR_A_1.equals(attrA)) {
						// 天
						if (CampConstants.ATTR_B_1.equals(attrB)) {
							calA.add(Calendar.DAY_OF_MONTH, -valA);
						}
						// 月
						else if (CampConstants.ATTR_B_2.equals(attrB)) {
							calA.add(Calendar.MONTH, -valA);
						}
					} else {
						// 天
						if (CampConstants.ATTR_B_1.equals(attrB)) {
							calA.add(Calendar.DAY_OF_MONTH, valA);
						}
						// 月
						else if (CampConstants.ATTR_B_2.equals(attrB)) {
							calA.add(Calendar.MONTH, valA);
						}
					}
					String obtainFromDate  = DateUtil.date2String(calA.getTime());
					order.put(CampConstants.OBTAIN_FROMDATE,
							DateUtil.date2String(calA.getTime()));
					// 天
					if (CampConstants.ATTR_C_1.equals(attrC)) {
						calA.add(Calendar.DAY_OF_MONTH, valB - 1);
					}
					// 月
					else if (CampConstants.ATTR_C_2.equals(attrC)) {
						calA.add(Calendar.MONTH, valB);
						calA.add(Calendar.DAY_OF_MONTH, -1);
					}
					if(busCal.compareTo(calA) > 0){
						String memCode = ConvertUtil.getString(order.get("memCode"));
						orderList.remove(i);
						i--;
						logger.info("====警告：会员【" + memCode + "】领用截止日期小于当前业务日期，被剔除=====");
					}else{
						order.put(CampConstants.OBTAIN_FROMDATE,obtainFromDate);
						order.put(CampConstants.OBTAIN_TODATE,DateUtil.date2String(calA.getTime()));
					}
				}
			}
		}
	}
	
	/**
	 * 设置操作年-月-日
	 * @param order
	 */
	private void setOptionDate(Map<String, Object> order){
		String optTime = ConvertUtil.getString(order.get(CampConstants.OPT_TIME));
		if(optTime.length() >= 10){
			order.put(CampConstants.OPT_YEAR, optTime.substring(0, 4));
			order.put(CampConstants.OPT_MONTH, optTime.substring(5, 7));
			order.put(CampConstants.OPT_DAY, optTime.substring(8, 10));
		}
	}
	/**	
	 * 设置共通参数MAP
	 * @param comMap
	 */
	private void setComMap(Map<String, Object> comMap){
		// 业务日期
		String busDate = ConvertUtil.getString(comMap
				.get(CherryConstants.BUSINESS_DATE));
		String batchNo = ConvertUtil.getString(comMap
				.get(CampConstants.BATCHNO));
		String orgId = ConvertUtil.getString(comMap
				.get(CherryConstants.ORGANIZATIONINFOID));
		String brandId = ConvertUtil.getString(comMap
				.get(CherryConstants.BRANDINFOID));
		if("".equals(busDate)){
			busDate = ser.getBussinessDate(comMap);
			comMap.put(CherryConstants.BUSINESS_DATE, busDate);
		}
		if("".equals(batchNo)){
			batchNo = cm03bl.getTicketNumber(orgId, brandId, "", "OP");
			comMap.put(CampConstants.BATCHNO, batchNo);
		}
		// 是否管理库存
//		comMap.put(CampConstants.ISSTOCK, cm14bl.getConfigValue("1036",orgId,brandId));
		comMap.put(CampConstants.ISSTOCK, "1");
		comMap.put("couponFlag", cm14bl.getConfigValue("1138", orgId,brandId));
	}
	
	@SuppressWarnings("unchecked")
	private void setHisParamMap(Map<String, Object> subCamp,String busDate,
			Map<String, Object> map) throws JSONException {
		// 领用日期参考规则
		String obtainRule = ConvertUtil.getString(subCamp
				.get(CampConstants.OBTAIN_RULE));
		if (!"".equals(obtainRule)) {
			try {
				Map<String, Object> rule = (Map<String, Object>) JSONUtil
						.deserialize(obtainRule);
				// 参考类型
				String referType = ConvertUtil.getString(rule
						.get(CampConstants.REFER_TYPE));
				// 参考会员生日
				if (CampConstants.REFER_TYPE_1.equals(referType)
						|| CampConstants.REFER_TYPE_2.equals(referType)) {
					map.put(CampConstants.OPT_YEAR, busDate.substring(0, 4));
				} else if (CampConstants.REFER_TYPE_6.equals(referType)) {
					// 参考系统当月月初
//					map.put(CampConstants.OPT_YEAR, busDate.substring(0, 4));
//					map.put(CampConstants.OPT_MONTH, busDate.substring(5, 7));
				}
			} catch (JSONException e) {
				logger.error("====JSON【" + obtainRule + "】 to Map ERROR!=====" + e.getMessage(),e);
				throw e;
			}
		}
	}
	
	/**
	 * 单据实时生成
	 * 
	 * @param brandCode
	 * @param memId
	 * @param subCampType
	 *            会员活动子类型
	 * @throws Exception
	 */
	public int makeOrderWS(String orgId,String brandId,String orgCode,String brandCode, int memId,
			String subCampType) {
		int result = CherryConstants.SUCCESS;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("TradeType", "MakeOrder");
		params.put("brandCode", brandCode);
		params.put("MemId", memId);
		params.put("SubCampType", subCampType);
		String batchNo = cm03bl.getTicketNumber(orgId, brandId, "", "OP");
		params.put("BatchNo", batchNo);
		// 调用WS生成活动单据
		logger.info("*********调用WS生成活动单据*********");
		try{
			Map<String, Object> resultMap = WebserviceClient.accessBatchWebService(params);
			if(null != resultMap){
				result = ConvertUtil.getInt(resultMap.get("ERRORCODE"));
				if(0 != result){
					logger.info("*********调用WS生成活动单据errorCode="+result+"*********");
					return result;
				}
				logger.info("*********发送单据MQ批次号："+batchNo+"*********");
				Map<String, Object> p = new HashMap<String, Object>();
				p.put(CherryConstants.ORGANIZATIONINFOID, orgId);
				p.put(CherryConstants.ORG_CODE, orgCode);
				p.put(CherryConstants.BRANDINFOID, brandId);
				p.put(CherryConstants.BRAND_CODE, brandCode);
				p.put(CampConstants.BATCHNO, batchNo);
				// 发送单据MQ
				this.sendPOSMQ(p,null);
				return result;
			}else{
				logger.info("*********调用WS生成活动单据返回为NULL*********");
				return -1;
			}
		} catch (Exception e) {
			logger.error("******************************************************");
			logger.error("**********"+e.getMessage()+"**********",e);
			logger.error("******************************************************");
			return -2;
		}
	}
	
	
	/**
	 * 单据实时生成
	 * 
	 * @param brandCode
	 * @param memId
	 * @param subCampType
	 *            会员活动子类型
	 * @throws Exception
	 */
	@Override
	public int makeOrderMQ(int orgId,int brandId,String orgCode,String brandCode, int memId,
			String subCampType) {
		// 生日礼-入会礼实时生成单据，是否触发沟通
		String gt = cm14bl.getConfigValue("1324",orgId+"",brandId+"");
		if("1".equals(gt)){
			return makeOrderMQ(orgId,brandId,orgCode,brandCode,null,memId,subCampType,null,CampConstants.BILL_STATE_AR,0);
		}else{
			return makeOrderMQ(orgId,brandId,orgCode,brandCode,null,memId,subCampType,null,null,0);
		}
	}
	
	
	/**
	 * 单据实时生成
	 * 
	 * @param brandCode
	 * @param memId
	 * @throws Exception
	 */
	@Override
	public int makeOrderMQ(int orgId, int brandId, String orgCode,
			String brandCode,String orderCntCode, int memId,String configType) {
		int result = CherryConstants.SUCCESS;
		// 实时生成活动单据的活动码
		String subCampCode = cm14bl.getConfigValue(configType,orgId+"",brandId+"");
		if(!"".equals(subCampCode) && null != subCampCode){
			String[] subCampCodeArr = subCampCode.split(",");
			for(String code : subCampCodeArr){
				result = makeOrderMQ(orgId,brandId,orgCode,brandCode,orderCntCode,memId,null,code,CampConstants.BILL_STATE_AR,0);
			}
		}
		return result;
	}

	/**
	 * 单据实时生成
	 * 
	 * @param brandCode
	 * @param memId
	 * @param subCampType
	 *            会员活动子类型
	 * @throws Exception
	 */
	private int makeOrderMQ(int orgId,int brandId,String orgCode,String brandCode,String orderCntCode, int memId,
			String subCampType,String subCampCode, String gtType,int repeatFlag) {
		int result = CherryConstants.SUCCESS;
		String batchNo = cm03bl.getTicketNumber(orgId, brandId, "", "OP");
		String sysTime = ser.getSYSDate();
		// 设定MQ消息DTO
		MQInfoDTO mq = new MQInfoDTO();
		// 品牌代码
		mq.setBrandCode(brandCode);
		// 组织代码
		mq.setOrgCode(orgCode);
		// 组织ID
		mq.setOrganizationInfoId(orgId);
		// 品牌ID
		mq.setBrandInfoId(brandId);
		// 
		mq.setInsertTime(sysTime);
		// 业务类型
		mq.setBillType("MakeOrder");
		// 单据号
		mq.setBillCode(batchNo);
		// 修改回数
		mq.setSaleRecordModifyCount(0);
		// 消息发送队列名
		mq.setMsgQueueName(CherryConstants.CHERRYTOBATCHMSGQUEUE);
		// 消息的主数据行
		Map<String, Object> mainData = new HashMap<String, Object>();
		// 品牌代码
		mainData.put("BrandCode", brandCode);
		mainData.put("TradeNoIF", batchNo);
		mainData.put("TradeType", "MakeOrder");
		mainData.put("BatchNo", batchNo);
		mainData.put("ModifyCounts", 0);
		mainData.put("MemId", memId);
		mainData.put("SubCampType", subCampType);
		mainData.put("SubCampCode", subCampCode);
		mainData.put("GtType", gtType);
		mainData.put("SysTime", sysTime);
		mainData.put("RepeatFlag", repeatFlag);
		mainData.put("OrderCntCode", orderCntCode);
		// 设定消息的数据行
		Map<String, Object> dataLine = new HashMap<String, Object>();
		dataLine.put("MainData", mainData);
		// 设定消息内容
		Map<String, Object> msgDataMap = new HashMap<String, Object>();
		// 设定消息版本号
		msgDataMap.put("Version", "AMQ.109.001");
		msgDataMap.put("Type", "1009");
		// 设定消息数据类型
		msgDataMap.put("DataType", MessageConstants.DATATYPE_APPLICATION_JSON);
		msgDataMap.put("DataLine", dataLine);
		mq.setMsgDataMap(msgDataMap);
		// 发送MQ消息
		try {
			binOLMQCOM01_BL.sendMQMsg(mq,false);
		} catch (Exception e) {
			result = CherryConstants.ERROR;
			logger.info("=========发送MQ["+CherryConstants.CHERRYTOBATCHMSGQUEUE+"]失败=========");
			logger.error(e.getMessage(),e);
			return result;
		}
		return result;
	}
	
	public String getErrMsg(String errCode){
		return msgMap.get(errCode) == null ? errCode : msgMap.get(errCode);
	}
	
	/**
	 * 取得会员List
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> searchMemInfo(Map<String, Object> map){
		// 会员结果List
		Map<String, Object> memMap = null;
		List<String> selectors = new ArrayList<String>();
		// 会员Id
		selectors.add("memId");
		// 会员Code
		selectors.add("memCode");
		// 会员生日-月
		selectors.add("birthMonth");
		// 会员生日-日
		selectors.add("birthDay");
		// 会员入会日期
		selectors.add("joinDate");
		// 会员升级日期
		selectors.add("levelAdjustDay");
		// 电话号码
		selectors.add("telephone");
		// 会员手机
		selectors.add("mobilePhone");
		// 柜台Code
		selectors.add("counterCode");
		// 首次购买柜台Code
		selectors.add("firstSaleCounterCode");
		// 首次购买时间
		selectors.add("firstSaleDate");
		map.put("selectors", selectors);
		map.put("resultMode", "2");
		Map<String, Object> resultMap = cm33_bl.searchMemList(map);
		if (resultMap != null) {
			List<Map<String, Object>> list = (List<Map<String, Object>>) resultMap.get("list");
			if(null != list && list.size() > 0){
				memMap = list.get(0);
			}
		}
		return memMap;
	}
	
	/**
	 * Map转换
	 * @param map
	 * @return
	 * @throws CherryMQException 
	 */
	private void setCouponOrder(Map<String, Object> order,Map<String, Object> map) throws Exception {
//		String memCode = ConvertUtil.getString(map.get("MemberCode"));
//		String mPhone = ConvertUtil.getString(map.get("MobilePhone"));
//		String billNo = ConvertUtil.getString(map.get("TradeNoIF"));
		String orgId = ConvertUtil.getString(map.get(CherryConstants.ORGANIZATIONINFOID));
		String brandId = ConvertUtil.getString(map.get(CherryConstants.BRANDINFOID));
		String subCampCode = ConvertUtil.getString(map.get(CampConstants.SUBCAMP_CODE));
//		String sendPos = ConvertUtil.getString(map.get("SendPos"));
		// 单据号
		order.put(CampConstants.BILL_NO, cm03bl.getTicketNumber(orgId, brandId, "", "SP"));
		// 操作类型
		order.put(CampConstants.BILL_STATE, CampConstants.BILL_STATE_RV);
		// 活动档次
		order.put(CampConstants.SUBCAMP_CODE, subCampCode);
		// 不维护积分
		order.put("mqWaitFlag", "0");
//		order.put("name", map.get("Name"));
		order.put("sendPos", "1");
//		order.put("bookDate", map.get("BookDate"));
//		order.put("bookTimeRange", map.get("BookTimeRange"));
//		order.put("dataSource1",map.get("DataSource1"));
//		order.put("dataSource2", map.get("DataSource2"));
//		order.put("dataSource3", map.get("DataSource3"));
//		order.put("messageId", map.get("MessageID"));
//		order.put("orderCntCode", map.get("CounterCode"));
//		order.put("cityCode", map.get("CityCode"));
//		order.put("counterCode", map.get("CounterCodeGet"));
		order.put(CampConstants.OPT_TIME, map.get(CampConstants.OPT_TIME));
//		order.put(CampConstants.COUPON_CODE, map.get("CouponCode"));
//		order.put("deliveryMothod",map.get("DeliveryType"));
//		order.put("address",map.get("DeliveryAddress"));
		Map<String,Object> p = new HashMap<String, Object>();
		p.put(CherryConstants.BRANDINFOID, brandId);
		p.put(CampConstants.SUBCAMP_CODE, subCampCode);
		// 单据明细
		List<Map<String, Object>> prtList = ser.getActResultList(p);
		if(null != prtList){
			order.put(CampConstants.KEY_LIST, prtList);
		}else{
			throw new Exception("无法获取活动礼品信息");
		}
	}
	
	@Override
	public Map<String,Object> tran_applyCoupon(int orgId, int brandId, String orgCode,
			String brandCode, int memId) {
		int result = CherryConstants.SUCCESS;
		Map<String, Object> retMap = new HashMap<String, Object>();
		// 添加到活动预约相关表
		Map<String, Object> order = new HashMap<String, Object>();
		String batchNo = cm03bl.getTicketNumber(orgId, brandId, "", "OP");
		String optTime = DateUtil.date2String(new Date(), DateUtil.DATETIME_PATTERN);
		Map<String, Object> comMap = new HashMap<String, Object>();
		comMap.put(CherryConstants.BRANDINFOID, brandId);
		comMap.put(CherryConstants.ORGANIZATIONINFOID, orgId);
		comMap.put(CampConstants.BATCHNO, batchNo);
		comMap.put(CherryConstants.ORG_CODE,orgCode);
		comMap.put(CherryConstants.BRAND_CODE,brandCode);
		comMap.put(CampConstants.OPT_TIME,optTime);
		comMap.put(CherryConstants.CREATEDBY, "BINOLCPCOM05");
		comMap.put(CherryConstants.CREATEPGM, "BINOLCPCOM05");
		comMap.put(CherryConstants.UPDATEDBY, "BINOLCPCOM05");
		comMap.put(CherryConstants.UPDATEPGM, "BINOLCPCOM05");

		String subCampCode = cm14bl.getConfigValue("1312",orgId+"",brandId+"");
		comMap.put(CampConstants.SUBCAMP_CODE, subCampCode);
		if(!"".equals(subCampCode)){
			Map<String, Object> memInfo = null;
			Map<String, Object> p = new HashMap<String, Object>();
			p.put("memberInfoId", memId);
			p.put("testTypeIgnore", 1);
			p.put("brandInfoId", brandId);
			p.put("organizationInfoId", orgId);
			p.put("brandCode", brandCode);
            p.put(CherryConstants.ORG_CODE,orgCode);
			// 获取会员信息
			memInfo = searchMemInfo(p);
			if(null != memInfo){
				memInfo.put("cntCodeBelong", memInfo.get("counterCode"));
				memInfo.remove("counterCode");
				order.putAll(memInfo);
			}
			try {
				setCouponOrder(order,comMap);
			} catch (Exception e) {
				retMap.put("ERRORCODE", 24);
				retMap.put("ERRORMSG", "单据填充失败");
				logger.error(">>>>>>>>>>>>>>>>>"+e.getMessage()+">>>>>>>>>>>>>>>>>>>>>>",e);
				return retMap;
			}
			// MQ插入预约相关表
			try {
				result = tran_campOrderMQ(comMap, order);
				if(CherryConstants.SUCCESS == result){
					Map<String,Object> content = new HashMap<String, Object>();
					content.put(CampConstants.BILL_NO, order.get(CampConstants.BILL_NO));
					content.put(CampConstants.COUPON_CODE, order.get(CampConstants.COUPON_CODE));
					content.put(CampConstants.OBTAIN_FROMDATE, order.get(CampConstants.OBTAIN_FROMDATE));
					content.put(CampConstants.OBTAIN_TODATE, order.get(CampConstants.OBTAIN_TODATE));
					retMap.put("ResultMap", content);
				}else{
					retMap.put("ERRORCODE", result);
					retMap.put("ERRORMSG", getErrMsg(result + ""));
					return retMap;
				}
			} catch (CherryException e) {
				retMap.put("ERRORCODE", 30);
				retMap.put("ERRORMSG", "预约异常");
				return retMap;
			}
			if(result == CherryConstants.SUCCESS){
				try {
					// 发送沟通MQ
					sendGTMQ(comMap, batchNo, CampConstants.BILL_STATE_RV);
				} catch (Exception e) {
					retMap.put("ERRORCODE", 26);
					retMap.put("ERRORMSG", "发送沟通信息失败");
					return retMap;
				}
			}
		}
		return retMap;
	}
}
