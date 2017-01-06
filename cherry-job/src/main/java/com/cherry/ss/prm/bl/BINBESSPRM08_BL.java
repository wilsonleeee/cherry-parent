/*		
 * @(#)BINBESSPRM08_BL.java     1.0 2016/05/26          	
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
package com.cherry.ss.prm.bl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.dr.cmbussiness.util.DateUtil;
import com.cherry.mq.mes.interfaces.CherryMessageHandler_IF;
import com.cherry.ss.prm.service.BINBESSPRM08_Service;
import com.jahwa.common.Config;
import com.jahwa.common.JahwaWebServiceProxy;
import com.jahwa.pos.crm.Dt_PmsCpGenTran_req;
import com.jahwa.pos.crm.Dt_PmsCpGenTran_res;
import com.jahwa.pos.crm.Dt_PmsCpListTran_req;
import com.jahwa.pos.crm.Dt_PmsCpListTran_reqZLISTITEM;
import com.jahwa.pos.crm.Dt_PmsCpListTran_res;
import com.jahwa.pos.crm.Dt_PmsCpStaTran_req;
import com.jahwa.pos.crm.Dt_PmsCpStaTran_reqZLIST1ITEM;
import com.jahwa.pos.crm.Dt_PmsCpStaTran_reqZLIST2ITEM;
import com.jahwa.pos.crm.Dt_PmsCpStaTran_res;

public class BINBESSPRM08_BL implements CherryMessageHandler_IF {
	
	@Override
	public void handleMessage(Map<String, Object> map) throws Exception {
		// 品牌代码
		String brandCode = ConvertUtil.getString( map.get("brandCode"));
		logger.info("******************************发送优惠券处理器开始！品牌代号：" + brandCode + " ***************************");
		try {
		String allCoupon=ConvertUtil.getString(map.get("allCoupon"));
		String[] allCoupon_arr=allCoupon.split(",");
		Map<String,Object> param=new HashMap<String, Object>();
		param.put("couponArr", allCoupon_arr);
		List<Map<String, Object>> couponRule_list=binbessprm08_Service.getCouponRuleDistinctList(param);
		for(Map<String,Object> rule:couponRule_list){
			Map<String,Object> param_rule=new HashMap<String, Object>();
			param_rule.put("ruleCodes", rule.get("RuleCode"));
			this.tran_sendCoupon(param_rule);
		}
			this.tran_sendCoupon(map);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			logger.info("******************************发送优惠券处理器异常结束！品牌代号：" + brandCode + " ***************************");
		}
		logger.info("******************************发送优惠券处理器正常结束！品牌代号：" + brandCode + " ***************************");
	}
	
	
	private static Logger logger = LoggerFactory.getLogger(BINBESSPRM08_BL.class.getName());
	
	@Resource(name="binbessprm08_Service")
	private BINBESSPRM08_Service binbessprm08_Service;
	
	/** 优惠券的状态: 已使用 */
	public final static String COUPON_STATUS_OK = "OK";
	
	/** 优惠券的状态: 未使用 */
	public final static String COUPON_STATUS_AR = "AR";
	
	/** 优惠券的状态: 已废弃 */
	public final static String COUPON_STATUS_CA = "CA";
	
	/** 优惠券的状态: 已使用（退货） */
	public final static String COUPON_STATUS_CK = "CK";
	
	/** 接口使用状态: 已使用 */
	public final static String USE_FLAG_X = "X";
	
	/** 接口使用状态: 已废弃 */
	public final static String USE_FLAG_F = "F";
	
	/** 接口使用状态: 已使用（该单据退货） */
	public final static String USE_FLAG_K = "K";
	
	/**
	 * CBI039-CRM-优惠券发放清单传输接口
	 * 
	 * @param map 基本参数
	 * @return int 执行结果
	 */
	public int tran_sendCoupon(Map<String, Object> map) throws Exception {
		int result = CherryBatchConstants.BATCH_SUCCESS;
		// 选择的活动代码
		String ruleCodes = ConvertUtil.getString( map.get("ruleCodes"));
		if (!CherryChecker.isNullOrEmpty(ruleCodes)) {
			String[] ruleCodeArr = ruleCodes.split(",");
			map.put("ruleCodeArr", ruleCodeArr);
		}
		// 查询券活动列表
		List<Map<String, Object>> couponRuleList = binbessprm08_Service.getCouponRuleList(map);
		if (null == couponRuleList || couponRuleList.isEmpty()) {
			logger.info("********************************没有查询到券活动信息，无需推送********************************");
			return result;
		}
		for (Map<String, Object> couponRule : couponRuleList) {
			// 循环推送券活动
			if (!sendRule(couponRule)) {
				result = CherryBatchConstants.BATCH_WARNING;
			}
		}
		return result;
	}
	
	/**
	 * CBI038-CRM-优惠券状态调整接收接口
	 * 
	 * @param map 基本参数
	 * @return int 执行结果
	 */
	public int tran_sendUpdateCoupon(Map<String, Object> map) throws Exception {
		int result = CherryBatchConstants.BATCH_SUCCESS;
		// 选择的活动代码
		String ruleCodes = ConvertUtil.getString(map.get("ruleCodes"));
		if (!CherryChecker.isNullOrEmpty(ruleCodes)) {
			String[] ruleCodeArr = ruleCodes.split(",");
			map.put("ruleCodeArr", ruleCodeArr);
		}
		// 查询券活动列表
		List<Map<String, Object>> couponRuleList = binbessprm08_Service.getCouponRuleList(map);
		if (null == couponRuleList || couponRuleList.isEmpty()) {
			logger.info("********************************没有查询到券活动信息，无需推送********************************");
			return result;
		}
		for (Map<String, Object> couponRule : couponRuleList) {
			// 循环推送券活动
			if (!sendUpdateRule(couponRule)) {
				result = CherryBatchConstants.BATCH_WARNING;
			}
		}
		return result;
	}
	
	
	/**
	 * CBI037-CRM-优惠券生成接收接口
	 * 
	 * @param map 基本参数
	 * @return int 执行结果
	 */
	public int sendGenerate(Map<String, Object> map) throws Exception {
		int result = CherryBatchConstants.BATCH_SUCCESS;
		// 选择的活动代码
		String ruleCodes = ConvertUtil.getString( map.get("ruleCodes"));
		if (!CherryChecker.isNullOrEmpty(ruleCodes)) {
			String[] ruleCodeArr = ruleCodes.split(",");
			map.put("ruleCodeArr", ruleCodeArr);
		}
		// 查询券活动列表
		List<Map<String, Object>> couponRuleList = binbessprm08_Service.getCouponRuleList(map);
		if (null == couponRuleList || couponRuleList.isEmpty()) {
			logger.info("********************************没有查询到券活动信息，无需推送优惠券汇总信息********************************");
			return result;
		}
		for (Map<String, Object> couponRule : couponRuleList) {
			// 循环推送券活动(优惠券汇总)
			if (!sendCouponTotal(couponRule)) {
				result = CherryBatchConstants.BATCH_WARNING;
			}
		}
		return result;
	}
	
	/**
	 * 推送单个券活动(优惠券汇总)
	 * 
	 * @param ruleMap 券活动信息
	 * @return
	 */
	public boolean sendCouponTotal(Map<String, Object> ruleMap) {
		String ruleCode = ConvertUtil.getString(ruleMap.get("ruleCode"));
		logger.info("********************************优惠券汇总信息推送处理开始，券活动代码：" + ruleCode);
		// 取得优惠券总数
		int count = binbessprm08_Service.getCouponCount(ruleMap);
		if (count == 0) {
			logger.info("********************************需要推送的券数为0，不执行推送优惠券汇总信息，券活动代码：" + ruleCode);
			return true;
		}
		Dt_PmsCpGenTran_req req = convertGenHead(ruleMap);
		// 优惠券码总数
		req.setNUM(new BigDecimal(count));
		boolean result = true;
		try {
			// CBI037-CRM-优惠券生成接收接口
			Dt_PmsCpGenTran_res res = JahwaWebServiceProxy.sendGenerate(req);
			if ("S".equals(res.getZTYPE())) {
				logger.info("********************************优惠券汇总信息推送接口成功，券活动代码：" + ruleCode + " 总件数：" + count);
			} else {
				result = false;
				logger.error("********************************优惠券汇总信息推送接口返回错误信息，券活动代码：" + ruleCode + " 错误信息：" + res.getZMESSAGE());
			}
		} catch (Exception e) {
			logger.error("********************************优惠券汇总信息推送接口发生异常，券活动代码：" + ruleCode + " 异常信息：" + e.getMessage());
			return false;
		}
		return result;
	}
	
	/**
	 * 推送单个券活动
	 * 
	 * @param ruleMap 券活动信息
	 * @return
	 */
	public boolean sendRule(Map<String, Object> ruleMap) {
		String ruleCode = ConvertUtil.getString(ruleMap.get("ruleCode"));
		logger.info("********************************推送处理开始，券活动代码：" + ruleCode);
		// 查询件数设置
		ruleMap.put("COUNT", CherryBatchConstants.DATE_SIZE);
		Dt_PmsCpListTran_req req = null;
		boolean result = true;
		while(true) {
			// 查询需要发送的优惠券列表
			List<Map<String, Object>> couponList = binbessprm08_Service.getCouponList(ruleMap);
			if (null == couponList || couponList.isEmpty()) {
				logger.info("********************************需要推送的券数为0，不执行推送，券活动代码：" + ruleCode);
				return true;
			}
			Dt_PmsCpListTran_reqZLISTITEM[] details = convertDetailList(couponList, ruleMap);
			if (null == details || details.length == 0) {
				logger.info("********************************转换成接口明细的数量为0，不执行推送，券活动代码：" + ruleCode);
				return true;
			}
			if (null == req) {
				req = convertHead(ruleMap);
			}
			// 明细项目数
			req.setLIST_NUM(new BigDecimal(details.length));
			// 明细
			req.setZLIST(details);
			try {
				// CBI039-CRM-优惠券发放清单传输接口
				Dt_PmsCpListTran_res res = JahwaWebServiceProxy.sendCouponList(req);
				String sendFlag = null;
				String syncFlag = null;
				if ("S".equals(res.getZTYPE())) {
					sendFlag = "X";
					syncFlag = "S";
				} else {
					sendFlag = "E";
					syncFlag = "E";
					result = false;
					logger.error("********************************推送接口返回错误信息，券活动代码：" + ruleCode + " 错误信息：" + res.getZMESSAGE());
				}
				for (Map<String, Object> couponMap : couponList) {
					couponMap.put("sendFlag", sendFlag);
					couponMap.put("syncFlag", syncFlag);
					commParamsForUp(couponMap);
				}
				binbessprm08_Service.updateSendResultList(couponList);
				binbessprm08_Service.manualCommit();
				logger.info("********************************推送接口成功，券活动代码：" + ruleCode + " 本批次件数：" + details.length);
			} catch (Exception e) {
				logger.error("********************************推送接口发生异常，券活动代码：" + ruleCode + " 异常信息：" + e.getMessage());
				result = false;
				break;
			}
		}
		return result;
	}
	
	/**
	 * 推送单个券活动(券状态推送)
	 * 
	 * @param ruleMap 券活动信息
	 * @return
	 */
	public boolean sendUpdateRule(Map<String, Object> ruleMap) {
		String ruleCode = ConvertUtil.getString(ruleMap.get("ruleCode"));
		logger.info("********************************券状态推送处理开始，券活动代码：" + ruleCode);
		// 查询件数设置
		ruleMap.put("COUNT", CherryBatchConstants.DATE_SIZE);
		Dt_PmsCpStaTran_req req = null;
		boolean result = true;
		while(true) {
			// 查询需要发送的优惠券列表
			List<Map<String, Object>> couponList = binbessprm08_Service.getCouponUpdateList(ruleMap);
			if (null == couponList || couponList.isEmpty()) {
				logger.info("********************************需要券状态推送的券数为0，不执行推送，券活动代码：" + ruleCode);
				return true;
			}
			ruleMap.put("couponType", couponList.get(0).get("couponType"));
			Dt_PmsCpStaTran_reqZLIST1ITEM[] details = convertDt_PmsCpStaTran_reqZLIST1ITEM(couponList, ruleMap);
			Dt_PmsCpStaTran_reqZLIST2ITEM[] detail2 = convertDt_PmsCpStaTran_reqZLIST2ITEM(couponList, ruleMap);
			if (null == details || details.length == 0) {
				logger.info("********************************转换成接口明细的数量为0，不执行券状态推送，券活动代码：" + ruleCode);
				return true;
			}
			if (null == req) {
				req = convertUpdateCouponHead(ruleMap);
			}
			// 明细1项目数
			req.setLIST_NUM1(new BigDecimal(details.length));
			//明细2项目数
			req.setLIST_NUM2(new BigDecimal(detail2.length));
			// 明细
			req.setZLIST1(details);
			req.setZLIST2(detail2);
			try {
				// CBI039-CRM-优惠券发放清单传输接口
				Dt_PmsCpStaTran_res res = JahwaWebServiceProxy.sendUpdateCouponList(req);
//				Dt_PmsCpStaTran_res res = new Dt_PmsCpStaTran_res();
//				res.setZTYPE("S");
				String sendFlag = null;
				String syncFlag = null;
				if ("S".equals(res.getZTYPE())) {
					sendFlag = "X";
					syncFlag = "S";
				} else {
					sendFlag = "E";
					syncFlag = "E";
					result = false;
					logger.error("********************************券状态推送接口返回错误信息，券活动代码：" + ruleCode + " 错误信息：" + res.getZMESSAGE());
				}
				for (Map<String, Object> couponMap : couponList) {
					couponMap.put("sendFlag", sendFlag);
					couponMap.put("syncFlag", syncFlag);
					commParamsForUp(couponMap);
				}
				binbessprm08_Service.updateSendResultList(couponList);
				binbessprm08_Service.manualCommit();
				logger.info("********************************券状态推送接口成功，券活动代码：" + ruleCode + " 本批次件数：" + details.length);
			} catch (Exception e) {
				logger.error("********************************券状态推送接口发生异常，券活动代码：" + ruleCode + " 异常信息：" + e.getMessage());
				result = false;
				break;
			}
		}
		return result;
	}
	
	
	/**
	 * 共通的参数设置(更新或者新增)
	 * 
	 * @param map 
	 * 			参数集合
	 * 
	 */
	private void commParamsForUp(Map<String, Object> map){
		// 作成者
		map.put(CherryBatchConstants.CREATEDBY, "BINBESSPRM08");
		// 作成程序名
		map.put(CherryBatchConstants.CREATEPGM, "BINBESSPRM08");
		// 更新者
		map.put(CherryBatchConstants.UPDATEDBY, "BINBESSPRM08");
		// 更新程序名
		map.put(CherryBatchConstants.UPDATEPGM, "BINBESSPRM08");
	}
	
	/**
	 * 取得抬头信息(优惠券汇总)
	 * 
	 * @param ruleMap 券活动信息
	 * @return Dt_PmsCpListTran_req 接口请求参数
	 */
	public Dt_PmsCpGenTran_req convertGenHead(Map<String, Object> ruleMap){
		Dt_PmsCpGenTran_req req = new Dt_PmsCpGenTran_req();
		// 销售组织
		req.setMKT_ORG(Config.VKORG);
		// 数据源
		req.setSOURCE(Config.DATASOURCE);
		// 活动渠道
		req.setZCHANNEL("101");
		// 发送活动号
		String ruleCode = ConvertUtil.getString(ruleMap.get("ruleCode"));
		req.setEXTERNAL_ID1(ruleCode);
		// 优惠券类型
		String couponType = ConvertUtil.getString(ruleMap.get("couponType"));
		req.setZCP_TYPE(getType(couponType));
		// 优惠券号
		req.setZCP_YHQ(getBatchNo(ruleCode));
		// 优惠券描述
		req.setZCP_YHQDES(ConvertUtil.getString(ruleMap.get("ruleName")));
		// 使用券对应活动号
		req.setEXTERNAL_ID2(ruleCode);
		return req;
	}
	
	/**
	 * 取得抬头信息(优惠券状态调整接口)
	 * 
	 * @param ruleMap 券活动信息
	 * @return Dt_PmsCpListTran_req 接口请求参数
	 */
	public Dt_PmsCpStaTran_req convertCpStaTranHead(Map<String, Object> ruleMap){
		Dt_PmsCpStaTran_req req = new Dt_PmsCpStaTran_req();
		// 销售组织
		req.setMKT_ORG(Config.VKORG);
		// 数据源
		req.setSOURCE(Config.DATASOURCE);
		// 活动渠道
		req.setZCHANNEL("101");
		// 发送活动号
		String ruleCode = ConvertUtil.getString(ruleMap.get("ruleCode"));
		req.setEXTERNAL_ID1(ruleCode);
		// 优惠券类型
		String couponType = ConvertUtil.getString(ruleMap.get("couponType"));
		req.setZCP_TYPE(getType(couponType));
		// 优惠券号
		req.setZCP_YHQ(getBatchNo(ruleCode));
		// 优惠券描述
		req.setZCP_YHQDES(ConvertUtil.getString(ruleMap.get("ruleName")));
		// 使用券对应活动号
		req.setEXTERNAL_ID2(ruleCode);
		return req;
	}
	
	
	/**
	 * 取得抬头信息
	 * 
	 * @param ruleMap 券活动信息
	 * @return Dt_PmsCpListTran_req 接口请求参数
	 */
	public Dt_PmsCpListTran_req convertHead(Map<String, Object> ruleMap){
		Dt_PmsCpListTran_req req = new Dt_PmsCpListTran_req();
		// 销售组织
		req.setMKT_ORG(Config.VKORG);
		// 数据源
		req.setSOURCE(Config.DATASOURCE);
		// 活动渠道
		req.setZCHANNEL("101");
		// 发送活动号
		String ruleCode = ConvertUtil.getString(ruleMap.get("ruleCode"));
		req.setEXTERNAL_ID1(ruleCode);
		// 优惠券类型
		String couponType = ConvertUtil.getString(ruleMap.get("couponType"));
		req.setZCP_TYPE(getType(couponType));
		// 优惠券号
		req.setZCP_YHQ(getBatchNo(ruleCode));
		// 优惠券描述
		req.setZCP_YHQDES(ConvertUtil.getString(ruleMap.get("ruleName")));
		// 使用券对应活动号
		req.setEXTERNAL_ID2(ruleCode);
		return req;
	}
	
	/**
	 * 取得抬头信息(券状态推送接口)
	 * 
	 * @param ruleMap 券活动信息
	 * @return Dt_PmsCpListTran_req 接口请求参数
	 */
	public Dt_PmsCpStaTran_req convertUpdateCouponHead(Map<String, Object> ruleMap){
		Dt_PmsCpStaTran_req req = new Dt_PmsCpStaTran_req();
		// 销售组织
		req.setMKT_ORG(Config.VKORG);
		// 数据源
		req.setSOURCE(Config.DATASOURCE);
		// 活动渠道
		req.setZCHANNEL("101");
		// 发送活动号
		String ruleCode = ConvertUtil.getString(ruleMap.get("ruleCode"));
		req.setEXTERNAL_ID1(ruleCode);
		// 优惠券类型
		String couponType = ConvertUtil.getString(ruleMap.get("couponType"));
		req.setZCP_TYPE(getType(couponType));
		// 优惠券号
		req.setZCP_YHQ(getBatchNo(ruleCode));
		// 优惠券描述
		req.setZCP_YHQDES(ConvertUtil.getString(ruleMap.get("ruleName")));
		// 使用券对应活动号
		req.setEXTERNAL_ID2(ruleCode);
		return req;
	}
	
	/**
	 * 获取优惠券批次号
	 * 
	 * @param ruleCode 券活动代码
	 * @return String 优惠券批次号
	 */
	private static String getBatchNo(String ruleCode) {
		ruleCode = ruleCode.replaceAll("-", "").replaceAll("_", "");
		int length = ruleCode.length();
		int index = length - 12;
		int i = 3;
		if (index > 0)  {
			ruleCode = ruleCode.substring(index);
		} else {
			i = 14 - length;
		}
		ruleCode += String.format("%0" + i + "d", 1);
		return ruleCode;
	}
	
	/**
	 * 转换券类型
	 * 
	 * @param couponType 券类型
	 * @return String 券类型(接口用)
	 */
	private String getType(String couponType) {
		couponType=couponType.trim();
		// 代金券
		if ("1".equals(couponType) || "5".equals(couponType)) {
			return "DJQ";
			// 代物券
		} else if ("2".equals(couponType)) {
			return "DWQ";
			// 资格券
		} else if ("3".equals(couponType)) {
			return "ZGQ";
			// 积分抵用券
		}else {
//			return "JFDY";
			return couponType;
		}
	}
	
	/**
	 * 设置接口明细
	 * 
	 * @param couponList 券列表
	 * @param ruleMap 券活动
	 * @return Dt_PmsCpListTran_reqZLISTITEM[] 接口明细
	 */
	private Dt_PmsCpListTran_reqZLISTITEM[] convertDetailList(List<Map<String, Object>> couponList, Map<String, Object> ruleMap) {
		if (null != couponList && !couponList.isEmpty()) {
			Dt_PmsCpListTran_reqZLISTITEM[] details = new Dt_PmsCpListTran_reqZLISTITEM[couponList.size()];
			// 优惠券列表
			for (int i = 0; i < couponList.size(); i++) {
				Map<String, Object> couponInfo = couponList.get(i);
				Dt_PmsCpListTran_reqZLISTITEM detail = new Dt_PmsCpListTran_reqZLISTITEM();
				/*
				// 优惠券码
				detail.setZCP_NUM(String.valueOf(couponInfo.get("couponNo")));
				// 校验密码
				detail.setZCP_PASSW(String.valueOf(couponInfo.get("couponCode")));
				// 发送开始日期
				detail.setZCP_BDATE(String.valueOf(ruleMap.get("sendStartTime")));
				// 发券结束时间
				detail.setZCP_EDATE(String.valueOf(ruleMap.get("sendEndTime")));
				// 使用开始日期
				detail.setZCP_UBDATE(String.valueOf(couponInfo.get("useStartTime")));
				// 使用结束日期
				detail.setZCP_UEDATE(String.valueOf(couponInfo.get("useEndTime")));
				// 会员BP号
				detail.setPARTNER(String.valueOf(couponInfo.get("bpCode")));
				*/
				// 优惠券码
				detail.setZCP_NUM(ConvertUtil.getString(couponInfo.get("couponNo")));
				// 校验密码
				detail.setZCP_PASSW(ConvertUtil.getString(couponInfo.get("couponCode")));
				// 发送开始日期
				detail.setZCP_BDATE(ConvertUtil.getString(ruleMap.get("sendStartTime")));
				// 发券结束时间
				detail.setZCP_EDATE(ConvertUtil.getString(ruleMap.get("sendEndTime")));
				// 使用开始日期
				detail.setZCP_UBDATE(ConvertUtil.getString(couponInfo.get("useStartTime")));
				// 使用结束日期
				detail.setZCP_UEDATE(ConvertUtil.getString(couponInfo.get("useEndTime")));
				// 会员BP号
				detail.setPARTNER(ConvertUtil.getString(couponInfo.get("bpCode")));
				
				// 会员卡号
				String memCodeKey = null;
				// 手机号码
				String mobileKey = null;
				// 使用状态
				String useFlag = null;
				// 已使用
				/*String status = (String) couponInfo.get("status");*/
				String status = ConvertUtil.getString(couponInfo.get("status"));
				if (COUPON_STATUS_OK.equals(status)) {
					memCodeKey = "userMemCode";
					mobileKey = "userMobile";
					useFlag = USE_FLAG_X;
				} else {
					memCodeKey = "memCode";
					mobileKey = "mobile";
					// 已废弃
					if (COUPON_STATUS_CA.equals(status)) {
						useFlag = USE_FLAG_F;
					}
				}
				/*String memCode = (String) couponInfo.get(memCodeKey);*/
				String memCode = ConvertUtil.getString(couponInfo.get(memCodeKey));
				if (!CherryChecker.isNullOrEmpty(memCode)) {
					detail.setBPEXT(memCode);
				}
				// 手机号码
				/*String userMobile = (String) couponInfo.get(mobileKey);*/
				String userMobile = ConvertUtil.getString(couponInfo.get(mobileKey));
				if (!CherryChecker.isNullOrEmpty(userMobile)) {
					detail.setMOB_NUMBER(userMobile);
				}
				/*detail.setZCPSDATE(DateUtil.coverString2Date(String.valueOf(couponInfo.get("orderTime"))));*/
				detail.setZCPSDATE(DateUtil.coverString2Date(ConvertUtil.getString(couponInfo.get("orderTime"))));
				// 发送标记
				detail.setZCP_SEND_FLAG("X");
				
				// 使用状态
				detail.setZCP_USE_FLAG(useFlag);
				details[i] = detail;
			}
			return details;
		}
		return null;
	}
	
	/**
	 * 设置接口明细(券状态推送)
	 * 
	 * @param couponList 券列表
	 * @param ruleMap 券活动
	 * @return Dt_PmsCpListTran_reqZLISTITEM[] 接口明细
	 */
	private Dt_PmsCpStaTran_reqZLIST1ITEM[] convertDt_PmsCpStaTran_reqZLIST1ITEM(List<Map<String, Object>> couponList, Map<String, Object> ruleMap) {
		if (null != couponList && !couponList.isEmpty()) {
			Dt_PmsCpStaTran_reqZLIST1ITEM[] details = new Dt_PmsCpStaTran_reqZLIST1ITEM[couponList.size()];
			// 优惠券列表
			for (int i = 0; i < couponList.size(); i++) {
				Map<String, Object> couponInfo = couponList.get(i);
				Dt_PmsCpStaTran_reqZLIST1ITEM detail = new Dt_PmsCpStaTran_reqZLIST1ITEM();
				// 优惠券码
				detail.setZCP_NUM(ConvertUtil.getString(couponInfo.get("couponNo")));
				//会员BP号
				detail.setPARTNER(ConvertUtil.getString(couponInfo.get("userBP")));
				// 会员卡号
				detail.setBPEXT(ConvertUtil.getString(couponInfo.get("userMemCode")));
				// 手机号码
				detail.setMOB_NUMBER(ConvertUtil.getString(couponInfo.get("userMobile")));
				//发送标记
				detail.setZCP_SEND_FLAG("X");
				//实际发送时间
				detail.setZCPSDATE(DateUtil.coverString2Date(ConvertUtil.getString(couponInfo.get("createTime"))));
				// 使用状态
				String useFlag = null;
				// 已使用
				String status = ConvertUtil.getString( couponInfo.get("status"));
				if (COUPON_STATUS_OK.equals(status)) {
					useFlag = USE_FLAG_X;
				} else {
					// 已废弃
					if (COUPON_STATUS_CA.equals(status)) {
						useFlag = USE_FLAG_F;
					}else if(COUPON_STATUS_CK.equals(status)){
						useFlag = USE_FLAG_K;
					}
				}
				// 使用状态
				detail.setZCP_USE_FLAG(useFlag);
				//状态调整日期
				detail.setZCPUDATE(DateUtil.coverString2Date(ConvertUtil.getString(couponInfo.get("finshTime"))));
				//外部单据号
				detail.setWB_ORDER_ID(ConvertUtil.getString(couponInfo.get("relatedNoB")));
				logger.info("券状态推送----->>WB_ORDER_ID="+ConvertUtil.getString(couponInfo.get("relatedNoB")));
				//单据日期
				detail.setPO_DATE_SOLD(DateUtil.coverString2Date(ConvertUtil.getString(couponInfo.get("finshTime"))));
				// 销售组织
				detail.setZVKORG(Config.VKORG);
				//分销渠道 固定为线下42
				detail.setZVTWEG("42");
				// 门店编码
				detail.setZMD_ID(ConvertUtil.getString(couponInfo.get("counterCode")));
				// 负责BA
				detail.setEMPID(ConvertUtil.getString(couponInfo.get("employeeCode")));

				details[i] = detail;
			}
			return details;
		}
		return null;
	}
	
	/**
	 * 设置接口明细(券状态推送)
	 * 
	 * @param couponList 券列表
	 * @param ruleMap 券活动
	 * @return Dt_PmsCpStaTran_reqZLIST2ITEM[]  接口明细
	 */
	private Dt_PmsCpStaTran_reqZLIST2ITEM[] convertDt_PmsCpStaTran_reqZLIST2ITEM(List<Map<String, Object>> couponList, Map<String, Object> ruleMap) {
		if (null != couponList && !couponList.isEmpty()) {
			List<Map<String,Object>> result_coupon=new ArrayList<Map<String,Object>>();
			for(Map<String,Object> coupon:couponList){
				List<Map<String,Object>> couponCart_list=binbessprm08_Service.getCouponBycart(coupon);
				List<Map<String, Object>> order_list=ConvertUtil.listGroup(couponCart_list, "unitcode", "order_list");
				for(Map<String,Object> order:order_list){
					List<Map<String,Object>> couponOrder_list=(List<Map<String,Object>>)order.get("order_list");
					Map<String,Object> coupon0=couponOrder_list.get(0);
					Map<String,Object> order_map=new HashMap<String, Object>();
					order_map.putAll(coupon0);
					order_map.put("quantity", couponOrder_list.size());
					result_coupon.add(order_map);
				}
			}
			
			Dt_PmsCpStaTran_reqZLIST2ITEM[] details = new Dt_PmsCpStaTran_reqZLIST2ITEM[result_coupon.size()];
			
			
			for (int i = 0; i < result_coupon.size(); i++) {
				Map<String, Object> couponInfo = result_coupon.get(i);
				Dt_PmsCpStaTran_reqZLIST2ITEM detail = new Dt_PmsCpStaTran_reqZLIST2ITEM();
				//销售组织
				detail.setMKT_ORG(Config.VKORG);
				//数据源类型
				detail.setSOURCE(Config.DATASOURCE);
				//优惠券类型
				String couponType = ConvertUtil.getString( couponInfo.get("couponType"));
				detail.setZCP_TYPE(getType(couponType));
				//优惠券
				String ruleCode = ConvertUtil.getString( couponInfo.get("ruleCode"));
				detail.setZCP_YHQ(getBatchNo(ruleCode));
				//优惠券码
				detail.setZCP_NUM(ConvertUtil.getString(couponInfo.get("couponNo")));
				//外部单据号
				detail.setWB_ORDER_ID(ConvertUtil.getString(couponInfo.get("relatedNoB")));
				int quantity=Integer.parseInt(ConvertUtil.getString(couponInfo.get("quantity")));
				BigInteger bigInteger = BigInteger.valueOf(quantity);
				detail.setQUANTITY(bigInteger);
				detail.setZCP_PROD(ConvertUtil.getString(couponInfo.get("unitcode")));
				details[i] = detail;
			}
			return details;
		}
		return null;
	}
	
	
}
