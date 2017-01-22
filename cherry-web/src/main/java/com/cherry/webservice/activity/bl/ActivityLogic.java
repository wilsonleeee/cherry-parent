package com.cherry.webservice.activity.bl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.cmbussiness.bl.BINOLCM03_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM33_BL;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cp.act.util.ActUtil;
import com.cherry.cp.common.CampConstants;
import com.cherry.cp.common.bl.BINOLCPCOMCOUPON_10_BL;
import com.cherry.cp.common.interfaces.BINOLCPCOM05_IF;
import com.cherry.cp.common.service.BINOLCPCOM05_Service;
import com.cherry.cp.common.util.CampUtil;
import com.cherry.mq.mes.bl.BINBEMQMES97_BL;
import com.cherry.mq.mes.common.CherryMQException;
import com.cherry.ss.common.PromotionConstants;
import com.cherry.webservice.activity.interfaces.Activity_IF;
import com.cherry.webservice.activity.service.ActivityService;

public class ActivityLogic implements Activity_IF {

	private static final Logger logger = LoggerFactory.getLogger(ActivityLogic.class);
	
	private static final Map<String,Lock> lockMap = new HashMap<String, Lock>();

	@Resource(name = "binOLCM03_BL")
	private BINOLCM03_BL cm03bl;

	@Resource(name = "binOLCM33_BL")
	private BINOLCM33_BL cm33_bl;

	@Resource(name = "binolcpcom05IF")
	private BINOLCPCOM05_IF com05IF;

	@Resource(name = "activityService")
	private ActivityService service;

	@Resource(name = "binolcpcom05_Service")
	private BINOLCPCOM05_Service ser05;

	@Resource(name = "binBEMQMES97_BL")
	private BINBEMQMES97_BL mq97BL;

	/** 系统配置项 共通BL */
	@Resource(name = "binOLCM14_BL")
	private BINOLCM14_BL cm14bl;
	
	@Resource(name="binolcpcomcoupon10bl")
	private BINOLCPCOMCOUPON_10_BL binolcpcomcoupon10bl;

	@Override
	public Map<String, Object> tran_receiveOrder(Map<String, Object> map) throws Exception {
		int result = CherryConstants.SUCCESS;
		Map<String, Object> retMap = new HashMap<String, Object>();
		// 参数MAP
		Map<String, Object> comMap = getCommMap(map);
		String orgId = ConvertUtil.getString(comMap.get(CherryConstants.ORGANIZATIONINFOID));
		String brandId = ConvertUtil.getString(comMap.get(CherryConstants.BRANDINFOID));
		String subCampCode = ConvertUtil.getString(map.get("SubCampCode"));
		String cntGot = ConvertUtil.getString(map.get("CounterCodeGet"));
		String validPoints = ConvertUtil.getString(map.get("ValidPoints"));
		if ("".equals(subCampCode)) {
			retMap.put("ERRORCODE", 21);
			retMap.put("ERRORMSG", "活动码参数缺失");
			logger.error(">>>>>>>>>>>>>>>>>活动码参数缺失>>>>>>>>>>>>>>>>>>>>>>");
			return retMap;
		}
		if ("".equals(cntGot)) {
			retMap.put("ERRORCODE", 22);
			retMap.put("ERRORMSG", "领取柜台参数缺失");
			logger.error(">>>>>>>>>>>>>>>>>领取柜台缺失>>>>>>>>>>>>>>>>>>>>>>");
			return retMap;
		} else {
			String validCnt = ConvertUtil.getString(map.get("ValidCounter"));
			if (!"ALL".equalsIgnoreCase(cntGot) && !"1".equals(validCnt)) {
				Map<String, Object> p = new HashMap<String, Object>();
				p.put("BIN_OrganizationInfoID", orgId);
				p.put("BIN_BrandInfoID", brandId);
				p.put("CounterCode", cntGot);
				boolean f = true;
				try {
					Map<String, Object> depart = mq97BL.getOrganizationInfo(p, false);
					if (null == depart) {
						f = false;
					}
				} catch (CherryMQException e) {
					f = false;
					logger.error(">>>>>>>>>>>>>>>>>领用柜台号验证查询异常>>>>>>>>>>>>>>>>>>>>>>" + e.getMessage(),e);
				}
				if (!f) {
					retMap.put("ERRORCODE", 23);
					retMap.put("ERRORMSG", "领取柜台不存在");
					logger.error(">>>>>>>>>>>>>>>>>领取柜台不存在>>>>>>>>>>>>>>>>>>>>>>");
					return retMap;
				}
			}
		}
		// 单据明细
		List<Map<String, Object>> prtList = (List<Map<String, Object>>) map.get("DetailList");
		if(null == prtList || prtList.isEmpty()){
			retMap.put("ERRORCODE", 36);
			retMap.put("ERRORMSG", "DetailList参数不存在或者为空");
			logger.error(">>>>>>>>>>>>>>>>>DetailList参数不存在或者为空>>>>>>>>>>>>>>>>>>>>>>");
			return retMap;
		}
		String memCode = ConvertUtil.getString(map.get("MemberCode"));
		float totalPoint = 0;
		float expoint = 0;
		Map<String, Object> memInfo = null;
		if (!"".equals(memCode)) {
			Map<String, Object> p = new HashMap<String, Object>();
			p.put("memCode", memCode);
			p.put("testTypeIgnore", 1);
			p.put("brandInfoId", map.get("BIN_BrandInfoID"));
			p.put("organizationInfoId", map.get("BIN_OrganizationInfoID"));
			p.put("brandCode", map.get("BrandCode"));
			// 获取会员信息
			memInfo = searchMemInfo(p);
			if (null != memInfo) {
				memInfo.put("cntCodeBelong", memInfo.get("counterCode"));
				memInfo.remove("counterCode");
				totalPoint = ConvertUtil.getFloat(memInfo.get("totalPoint"));
			} else {
				retMap.put("ERRORCODE", 28);
				retMap.put("ERRORMSG", "会员不存在");
				logger.error(">>>>>>>>>>>>>>>>>会员不存在>>>>>>>>>>>>>>>>>>>>>>");
				return retMap;
			}
		}
		String bookTimeRange = ConvertUtil.getString(map.get("BookTimeRange"));
		if (!"".equals(bookTimeRange)) {
			// 时间段校验
			if(bookTimeRange.length() != 11){
				retMap.put("ERRORCODE", 35);
				retMap.put("ERRORMSG", "服务时间段格式不正确");
			}
			Map<String, Object> p = new HashMap<String, Object>();
			p.put("BIN_BrandInfoID", brandId);
			p.put("BookDate", map.get("BookDate"));
			p.put("CounterCodeGet", cntGot);
			p.put("StateArr", new String[] { "RV", "ST", "AR", "NG" });
			List<String> list = service.getOrderTimeRange(p);
			if(null != list && !list.isEmpty()) {
				//同一个柜台同一天同一个时间段可预约次数
				int defaultTimes = ConvertUtil.getInt(cm14bl.getConfigValue("1345", orgId, brandId)); 
				if(defaultTimes < 1)
					defaultTimes = 1;
				int times = 0;//已被预约的次数
				for(String temp : list) {
					if(bookTimeRange.equals(temp))
						times ++;
				}
				if(times >= defaultTimes) {
					retMap.put("ERRORCODE", 26);
					retMap.put("ERRORMSG", "预约超出同一个柜台同一个天同一个时间段最大预约数，请修改预约日期或时间段");
					logger.error(">>>>>>>>>>>>>>>>>预约超出同一个柜台同一个天同一个时间段最大预约数>>>>>>>>>>>>>>>>>>>>>>, praMap = " + p);
					return retMap;
				}
			}
		}
		String subType = ConvertUtil.getString(map.get("SubType"));
		if ("SV".equals(subType)) {// 服务领用预约
			// 服务预约每天最大预约数
			int maxSize = ConvertUtil.getInt(cm14bl.getConfigValue("1290", orgId, brandId));
			if (maxSize > 0) {
				Map<String, Object> p = new HashMap<String, Object>();
				p.put(CherryConstants.BRANDINFOID, brandId);
				p.put(CampConstants.SUBCAMP_CODE, subCampCode);
				p.put("messageId", map.get("MessageID"));
				p.put("mobilePhone", map.get("MobilePhone"));
				p.put("memCode", map.get("MemberCode"));
				p.put("bookDate", map.get("BookDate"));
				int orderCnt = ser05.getOrderCount(p);
				if (orderCnt >= maxSize) {
					logger.error(">>>>>>>>>>>>>>服务预约超出每天最大预约数：" + maxSize);
					retMap.put("ERRORCODE", 27);
					retMap.put("ERRORMSG", "服务预约超出每天最大预约数：" + maxSize);
					return retMap;
				}
			}
		}
		// 添加到活动预约相关表
		Map<String, Object> order = new HashMap<String, Object>();
		if (null != memInfo) {
			order.putAll(memInfo);
		}
		try {
			expoint = setOrder(order, map);
		} catch (Exception e) {
			retMap.put("ERRORCODE", 24);
			retMap.put("ERRORMSG", "单据填充失败");
			logger.error(">>>>>>>>>>>>>>>>>" + e.getMessage() + ">>>>>>>>>>>>>>>>>>>>>>",e);
			return retMap;
		}
		// 会员积分值不够
		if("PX".equals(subType) && expoint > totalPoint
				&& ("1".equals(validPoints)|| "".equals(validPoints))){
			retMap.put("ERRORCODE", 29);
			retMap.put("ERRORMSG", "会员积分值不足");
			logger.error(">>>>>>>>>>>>>>>>>29：会员积分值不足>>>>>>>>>>>>>>>>>>>>>>");
			return retMap;
		}
		// MQ插入预约相关表
		try {
			result = com05IF.tran_campOrderMQ(comMap, order);
			if (CherryConstants.SUCCESS == result) {
				Map<String, Object> content = new HashMap<String, Object>();
				content.put(CampConstants.BILL_NO, order.get(CampConstants.BILL_NO));
				content.put(CampConstants.COUPON_CODE, order.get(CampConstants.COUPON_CODE));
				content.put(CampConstants.OBTAIN_FROMDATE, order.get(CampConstants.OBTAIN_FROMDATE));
				content.put(CampConstants.OBTAIN_TODATE, order.get(CampConstants.OBTAIN_TODATE));
				retMap.put("ResultMap", content);
			} else {
				retMap.put("ERRORCODE", result);
				retMap.put("ERRORMSG", com05IF.getErrMsg(result + ""));
				return retMap;
			}

		} catch (CherryException e) {
			retMap.put("ERRORCODE", 30);
			retMap.put("ERRORMSG", "预约异常");
			return retMap;
		}
		if (result == CherryConstants.SUCCESS) {
			String handlePoints = ConvertUtil.getString(map.get("HandlePoints"));
			if (!"0".equals(handlePoints)) {
				// 活动预约积分维护
				try {
					comMap.put(CampConstants.OPT_TIME, order.get(CampConstants.OPT_TIME));
					com05IF.sendPointMQ(comMap, order);
				} catch (Exception e) {
					retMap.put("ERRORCODE", 25);
					retMap.put("ERRORMSG", "积分维护失败");
					return retMap;
				}
			}
		}
		if (result == CherryConstants.SUCCESS) {
			try {
				String batchNo = ConvertUtil.getString(comMap.get(CampConstants.BATCHNO));
				// 发送沟通MQ
				com05IF.sendGTMQ(comMap, batchNo, CampConstants.BILL_STATE_RV);
			} catch (Exception e) {
				retMap.put("ERRORCODE", 26);
				retMap.put("ERRORMSG", "发送沟通信息失败");
				return retMap;
			}
		}
		return retMap;
	}

	@Override
	@SuppressWarnings("unchecked")
	public Map<String, Object> getGetSubCampInfo(Map<String, Object> map) {
		Map<String, Object> retMap = new HashMap<String, Object>();
		List<Map<String, Object>> list = service.getActivityList(map);
		if (list == null || list.size() == 0) {
			retMap.put("ResultContent", new ArrayList<Map<String, Object>>());
		} else {

			int brandId = ConvertUtil.getInt(map.get("BIN_BrandInfoID"));
			for (int i = 0; i < list.size(); i++) {
				Map<String, Object> act = list.get(i);
				// ====================================================================//
				setPlaceList(act, brandId);
				// ====================================================================//
				// ====================================================================//
				List<Map<String, Object>> giftList = (List<Map<String, Object>>) act.get("giftList");
				List<Map<String, Object>> resList = new ArrayList<Map<String, Object>>();
				String type = ConvertUtil.getString(act.get("subCampType"));
				//
				if ("PX".equals(type)) {
					if (null != giftList) {
						for (int j = 0; j < giftList.size(); j++) {
							Map<String, Object> gift = giftList.get(j);
							String cateType = ConvertUtil.getString(gift.get("cateType"));
							if (CampConstants.SUB_CAMPTYPE_DHCP.equals(cateType)) {
								act.put(CampConstants.EXPOINT, gift.get(CampConstants.EXPOINT));
								act.put("sumAmount", gift.get(CampConstants.PRICE));
								act.put(CherryConstants.UNITCODE, gift.get(CherryConstants.UNITCODE));
								act.put(CherryConstants.BARCODE, gift.get(CherryConstants.BARCODE));
								giftList.remove(j);
								break;
							}
						}
					}
				}
				List<String[]> keyList = new ArrayList<String[]>();
				String[] key = { CampConstants.GROUP_NO, CampConstants.GROUP_TYPE, CampConstants.LOGIC_OPT };
				keyList.add(key);
				ConvertUtil.convertList2DeepList(giftList, resList, keyList, 0);
				// 按照序号排序
				Collections.sort(resList, new ActUtil.CampComparator(CampConstants.GROUP_NO));
				act.put("giftList", resList);
				// ====================================================================//
			}
			retMap.put("ResultContent", list);
		}
		return retMap;
	}

	/**
	 * 取得品牌促销活动
	 */
	@Override
	@SuppressWarnings("unchecked")
	public Map<String, Object> getPromotionInfo(Map<String, Object> map) {
		Map<String, Object> retMap = new HashMap<String, Object>();
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> prmList = service.getPromotionList(map);
		if (null != prmList && prmList.size() > 0) {
			List<String[]> keyList = new ArrayList<String[]>();
			String[] key = { "ruleId", "activityCode", "activityName", "description", "locationType", "fromDate", "toDate" };
			keyList.add(key);
			ConvertUtil.convertList2DeepList(prmList, resultList, keyList, 0);
			for (Map<String, Object> item : resultList) {
				int ruleId = ConvertUtil.getInt(item.get("ruleId"));
				List<Map<String, Object>> list = (List<Map<String, Object>>) item.get(CampConstants.KEY_LIST);
				List<String> placeList = ConvertUtil.getKeyList(list, "counterCode");
				item.put("placeList", placeList);
				item.put("giftList", service.getPrmGiftList(ruleId));
				item.remove(CampConstants.KEY_LIST);
				item.remove("ruleId");
			}
		}
		retMap.put("ResultContent", resultList);
		return retMap;
	}

	@Override
	public Map<String, Object> getOrderInfo(Map<String, Object> map) {
		// map参数处理
		Map<String, Object> retMap = new HashMap<String, Object>();
		map = CherryUtil.remEmptyVal(map);
		String orgId = ConvertUtil.getString(map.get("BIN_OrganizationInfoID"));
		String brandId = ConvertUtil.getString(map.get("BIN_BrandInfoID"));
		String state = ConvertUtil.getString(map.get("State"));
		if (!"".equals(state) && state.indexOf("_") > -1) {
			String[] stateArr = state.split("_");
			map.put("StateArr", stateArr);
			map.remove("State");
		}
		String campCode = ConvertUtil.getString(map.get("CampCode"));
		if (!"".equals(campCode) && campCode.indexOf("_") > -1) {
			String[] campCodeArr = campCode.split("_");
			map.put("CampCodeArr", campCodeArr);
			map.remove("CampCode");
		}
		// 分页查询
		int startPage = ConvertUtil.getInt(map.get("StartPage"));
		int pageSize = ConvertUtil.getInt(map.get("PageSize"));
		int defSize = ConvertUtil.getInt(cm14bl.getConfigValue("1289", orgId, brandId));
		if(startPage < 1) {
			startPage = 1;
		}
		if(pageSize > defSize) {
			pageSize = defSize;
			retMap.put("WARNCODE", "1");
			retMap.put("WARNMSG", "警告：返回结果size=" + pageSize + "大于" + defSize);
		}else if(pageSize < 1){
			pageSize = 10;
		}
		int start = (startPage - 1) * pageSize + 1;
		int end = start + pageSize - 1;
		String sort = "TradeNoIF DESC";
		map.put("SORT_ID", sort);
		map.put("START", start);
		map.put("END", end);
		
		int count = service.getOrderCount(map);
		retMap.put("ResultTotalCNT", count);
		if(count > 0) {
			retMap.put("ResultContent", service.getOrderList(map));
		}else{
			retMap.put("ResultContent", new ArrayList<Map<String, Object>>());
		}
		return retMap;
	}

	@Override
	public Map<String, Object> getOrderTimeRange(Map<String, Object> map) {
		// map参数处理
		Map<String, Object> retMap = new HashMap<String, Object>();
		map = CherryUtil.remEmptyVal(map);
		String state = ConvertUtil.getString(map.get("State"));
		if (!"".equals(state) && state.indexOf("_") > -1) {
			String[] stateArr = state.split("_");
			map.put("StateArr", stateArr);
			map.remove("State");
		}
		List<String> list = service.getOrderTimeRange(map);
		if (list == null || list.size() == 0) {
			list = new ArrayList<String>();
		}
		retMap.put("ResultContent", list);
		return retMap;
	}

	@Override
	public Map<String, Object> tran_changeOrderState(Map<String, Object> map) throws Exception {
		Map<String, Object> retMap = new HashMap<String, Object>();
		String subType = ConvertUtil.getString(map.get("SubType"));
		String state = ConvertUtil.getString(map.get("State"));
		String sendPos = ConvertUtil.getString(map.get("SendPos"));
		String handlePoints = ConvertUtil.getString(map.get("HandlePoints"));
		if ("".equals(handlePoints)) {
			handlePoints = "1";
			map.put("HandlePoints",handlePoints);
		}
		// if(!"SV".equals(subType)){
		// retMap.put("ERRORCODE", 4);
		// retMap.put("ERRORMSG", "暂不支持此类型：" + subType);
		// return retMap;
		// }
		if (!CampConstants.BILL_STATE_CA.equals(state) && !CampConstants.BILL_STATE_OK.equals(state)) {
			retMap.put("ERRORCODE", 5);
			retMap.put("ERRORMSG", "暂不支持此单据状态：" + state);
			return retMap;
		}
		String curSate = service.getOrderState(map);
		if (null == curSate || "".equals(curSate)) {
			retMap.put("ERRORCODE", 1);
			retMap.put("ERRORMSG", "当前活动单据不存在");
			return retMap;
		} else if (CampConstants.BILL_STATE_OK.equals(curSate)) {
			retMap.put("ERRORCODE", 2);
			retMap.put("ERRORMSG", "当前活动单据状态【OK】，无法当前操作");
			return retMap;
		} else if (CampConstants.BILL_STATE_CA.equals(curSate)) {
			retMap.put("ERRORCODE", 3);
			retMap.put("ERRORMSG", "当前活动单据状态【CA】，无法当前操作");
			return retMap;
		}
		String billNo = ConvertUtil.getString(map.get("BillNo"));
		Map<String, Object> p = new HashMap<String, Object>();
		p.put(CampConstants.BILL_NO, billNo);
		p.put(CherryConstants.BRAND_CODE, map.get("BrandCode"));
		Map<String, Object> comMap = getComMap(map);
		// 更新单据状态
		service.changeOrderState(map);
		// 追加历史记录
		List<Map<String, Object>> hisList = ser05.getCampHistory(p);
		if (null != hisList && hisList.size() > 0) {
			for (Map<String, Object> his : hisList) {
				his.put(CampConstants.BILL_STATE, state);
				his.put(CampConstants.OPT_TIME, map.get("OptTime"));
				his.putAll(comMap);
			}
			ser05.addCampHistory(hisList);
		}
		if (CampConstants.SUBCAMP_TYPE_PX.equals(subType) && CampConstants.BILL_STATE_CA.equals(state) && "1".equals(handlePoints)) {
			// 活动预约取消-积分维护
			Map<String, Object> order = new HashMap<String, Object>();
			comMap.put(CampConstants.OPT_TIME, map.get("OptTime"));
			order.put(CampConstants.SUBCAMP_TYPE, subType);
			order.put("exPointDeductFlag", "1");
			order.put(CampConstants.BILL_NO, billNo);
			order.put(CampConstants.BILL_STATE, state);
			com05IF.sendPointMQ(comMap, order);
		}
		if ("1".equals(sendPos)) {
			// 发送MQ到终端
			com05IF.sendPOSMQ(comMap, billNo);
		}
		// String batchNo =
		// ConvertUtil.getString(comMap.get(CampConstants.BATCHNO));
		// // 发送沟通MQ
		// com05IF.sendGTMQ(comMap, batchNo, state);
		// retMap.put("ResultString", "");
		return retMap;
	}

	/**
	 * 共通参数Map
	 * 
	 * @return
	 */
	private Map<String, Object> getCommMap(Map<String, Object> map) {
		Map<String, Object> result = new HashMap<String, Object>();
		String orgId = ConvertUtil.getString(map.get("BIN_OrganizationInfoID"));
		String brandId = ConvertUtil.getString(map.get("BIN_BrandInfoID"));
		// 用户Id
		result.put(CherryConstants.USERID, "");
		// 员工code
		result.put("employeeCode", "");
		// 组织Id
		result.put(CherryConstants.ORGANIZATIONINFOID, orgId);
		// 组织Code
		result.put(CherryConstants.ORG_CODE, map.get("OrgCode"));
		// 品牌Id
		result.put(CherryConstants.BRANDINFOID, brandId);
		// 品牌Code
		result.put(CherryConstants.BRAND_CODE, map.get("BrandCode"));
		result.put(CampConstants.OPT_TIME, map.get("BookDateTime"));
		result.put(CampConstants.DATA_CHANNEL, map.get("DataSourse"));
		result.put(CherryConstants.CREATEDBY, "");
		result.put(CherryConstants.UPDATEDBY, "");
		result.put(CherryConstants.CREATEPGM, "BINOLCPACT05");
		result.put(CherryConstants.UPDATEPGM, "BINOLCPACT05");
		String batchNo = cm03bl.getTicketNumber(orgId, brandId, "", "OP");
		result.put(CampConstants.BATCHNO, batchNo);
		return result;
	}

	/**
	 * Map转换
	 * 
	 * @param map
	 * @return
	 * @throws CherryMQException
	 */
	@SuppressWarnings("unchecked")
	private float setOrder(Map<String, Object> order, Map<String, Object> map) throws Exception {

		String memCode = ConvertUtil.getString(map.get("MemberCode"));
		String mPhone = ConvertUtil.getString(map.get("MobilePhone"));
		String ticketType = ConvertUtil.getString(map.get("TicketType"));
		String billNo = ConvertUtil.getString(map.get("TradeNoIF"));
		String orgId = ConvertUtil.getString(map.get("BIN_OrganizationInfoID"));
		String brandId = ConvertUtil.getString(map.get("BIN_BrandInfoID"));
		String subType = ConvertUtil.getString(map.get("SubType"));
		String subCampCode = ConvertUtil.getString(map.get("SubCampCode"));
		String handlePoints = ConvertUtil.getString(map.get("HandlePoints"));
		// 单据号
		if ("".equals(billNo)) {
			order.put(CampConstants.BILL_NO, cm03bl.getTicketNumber(orgId, brandId, "", "PB"));
		} else {
			order.put(CampConstants.BILL_NO, billNo);
		}
		// 操作类型
		if ("0".equals(ticketType)) {
			order.put(CampConstants.BILL_STATE, CampConstants.BILL_STATE_RV);
		} else {
			order.put(CampConstants.BILL_STATE, CampConstants.BILL_STATE_CA);
		}
		// 活动档次
		order.put(CampConstants.SUBCAMP_CODE, subCampCode);

		if ("0".equals(handlePoints)) {// 不维护积分
			order.put("mqWaitFlag", "0");
		}
		order.put("name", map.get("Name"));
		order.put("sendPos", map.get("SendPos"));
		order.put("bookDate", map.get("BookDate"));
		order.put("bookTimeRange", map.get("BookTimeRange"));
		if (!"".equals(mPhone)) {
			order.put("mobilePhone", mPhone);
		}
		order.put("memCode", memCode);
		order.put("messageId", map.get("MessageID"));
		order.put("orderCntCode", map.get("CounterCode"));
		order.put("counterCode", map.get("CounterCodeGet"));
		order.put(CampConstants.OPT_TIME, map.get("BookDateTime"));
		order.put(CampConstants.COUPON_CODE, map.get("CouponCode"));
		order.put("opeartor", map.get("EmployeeCode"));
		order.put("deliveryMothod", map.get("DeliveryType"));
		order.put("address", map.get("DeliveryAddress"));
		order.put("receiverName",map.get("ReceiverName"));
		order.put("receiverMobile",map.get("ReceiverMobile"));
		order.put("deliveryProvince",map.get("DeliveryProvince"));
		order.put("deliveryCity",map.get("DeliveryCity"));
		order.put("deliveryCounty",map.get("DeliveryCounty"));
		// 单据明细
		List<Map<String, Object>> prtList = (List<Map<String, Object>>) map.get("DetailList");
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		float exPoint = 0;
		boolean dpcpFlag = false;
		List<String> codeList = new ArrayList<String>();
		if (null != prtList) {
			for (Map<String, Object> prt : prtList) {
				Map<String, Object> p = new HashMap<String, Object>();
				String code = ConvertUtil.getString(prt.get("SubCampCode"));
				if ("".equals(code)) {
					code = subCampCode;
				}
				if(!codeList.contains(code)){
					codeList.add(code);
				}
				p.put(CampConstants.SUBCAMP_CODE, code);
				String barCode = ConvertUtil.getString(prt.get("BarCode"));
				if(barCode.startsWith("DH")){
					dpcpFlag = true;
				}
				p.put(CampConstants.PRT_TYPE, prt.get("DetailType"));
				p.put(CherryConstants.UNITCODE, prt.get("UnitCode"));
				p.put(CherryConstants.BARCODE,barCode);
				p.put(CampConstants.QUANTITY, prt.get("Quantity"));
				p.put(CampConstants.PRICE, prt.get("Price"));
				float point = ConvertUtil.getFloat(prt.get("ExPoint"));
				exPoint += point;
				p.put(CampConstants.EXPOINT, point);
				setProId(map, p);
				list.add(p);
			}
//			if (CampConstants.SUBCAMP_TYPE_PX.equals(subType) && 0 == exPoint && !"0".equals(handlePoints)) {// 无虚拟促销品
//				Map<String, Object> p = new HashMap<String, Object>();
//				p.put(CampConstants.SUBCAMP_CODE, subCampCode);
//				p.put("prmCate", "DHCP");
//				// 获取积分兑换DHCP类型的虚拟促销品
//				Map<String, Object> dhcp = service.getPrmInfo(p);
//				if (null != dhcp) {
//					list.add(dhcp);
//				}
//			}
			if (CampConstants.SUBCAMP_TYPE_PX.equals(subType) && !dpcpFlag) {// 无虚拟促销品
				Map<String, Object> p = new HashMap<String, Object>();
				p.put("subCampCodeArr", codeList);
				// 获取积分兑换DHCP类型的虚拟促销品
				List<Map<String, Object>> dhcpList = service.getDHCPInfoList(p);
				if (null != dhcpList && !dhcpList.isEmpty()) {
					list.addAll(dhcpList);
				}
			}
			
		}
		order.put(CampConstants.KEY_LIST, list);
		return exPoint;
	}

	@SuppressWarnings("unchecked")
	private void setPlaceList(Map<String, Object> act, int brandId) {
		Map<String, Object> p = new HashMap<String, Object>();
		p.put("BIN_BrandInfoID", brandId);
		p.put(CherryConstants.OPERATION_TYPE, "1");
		p.put(CherryConstants.BUSINESS_TYPE, "1");
		p.put("DEPARTTYPE", "4");
		p.put(CherryConstants.USERID, act.get("setBy"));
		List<String> placeList = new ArrayList<String>();
		// 活动地点条件
		List<Map<String, Object>> conList = (List<Map<String, Object>>) act.get("conList");
		if (null != conList && conList.size() > 0) {
			for (Map<String, Object> con : conList) {
				String propName = ConvertUtil.getString(con.get("propName"));
				String propValue = ConvertUtil.getString(con.get("propValue"));
				// 城市
				if (CampUtil.BASEPROP_CITY.equalsIgnoreCase(propName)) {
					if ("ALL".equalsIgnoreCase(propValue)) {
						placeList.add("ALL");
					} else {
						p.put("cityId", propValue);
						List<String> list = service.getPlaceListByCity(p);
						if (null != list && list.size() > 0) {
							placeList.addAll(list);
						}
					}
				} else if (CampUtil.BASEPROP_CHANNAL.equalsIgnoreCase(propName)) {
					p.put("channelId", propValue);
					List<String> list = service.getPlaceListByChannel(p);
					if (null != list && list.size() > 0) {
						placeList.addAll(list);
					}
				} else {
					placeList.add(propValue);
				}
			}
		}
		act.put("placeList", placeList);
		act.remove("conList");
		act.remove("setBy");
	}

	/**
	 * 取得礼品ID
	 * 
	 * @param map
	 * @param detail
	 * @return
	 * @throws CherryMQException
	 */
	private void setProId(Map<String, Object> map, Map<String, Object> detail) throws Exception {
		int proId = 0;
		String isStock = "1";
		String prtType = ConvertUtil.getString(detail.get(CampConstants.PRT_TYPE)).toUpperCase();
		Map<String, Object> param = new HashMap<String, Object>(map);
		param.put("UnitCode", detail.get(CherryConstants.UNITCODE));
		param.put("BarCode", detail.get(CherryConstants.BARCODE));
		param.put("TradeDateTime", map.get("BookDateTime"));
		if (CampConstants.PRT_TYPE_P.equals(prtType)) {
			Map<String, Object> prmInfo = mq97BL.getPrmInfo(map, param, false);
			proId = ConvertUtil.getInt(prmInfo.get("BIN_PromotionProductVendorID"));
			isStock = ConvertUtil.getString(prmInfo.get("IsStock"));
		}else{
			proId = mq97BL.getProductVendorID(map, param, true);
		}
		if (proId == 0) {
			String unitCode = ConvertUtil.getString(detail.get("UnitCode"));
			String barCode = ConvertUtil.getString(detail.get("BarCode"));
			throw new Exception(">>>>>>>>>>>>>>>>>>>[unitCode=" + unitCode + ",barCode=" + barCode + "]不存在>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		}else{
			detail.put(CampConstants.PRO_ID,proId);
			detail.put(CampConstants.ISSTOCK,isStock);
		}
	}

	/**
	 * 取得共同信息
	 * 
	 * @param map
	 * @return
	 */
	private Map<String, Object> getComMap(Map<String, Object> map) {
		String orgId = ConvertUtil.getString(map.get("BIN_OrganizationInfoID"));
		String brandId = ConvertUtil.getString(map.get("BIN_BrandInfoID"));
		String batchNo = cm03bl.getTicketNumber(orgId, brandId, "", "OP");
		Map<String, Object> comMap = new HashMap<String, Object>();
		comMap.put(CherryConstants.BRANDINFOID, brandId);
		comMap.put(CherryConstants.ORGANIZATIONINFOID, orgId);
		comMap.put(CampConstants.BATCHNO, batchNo);
		comMap.put("employeeCode", map.get("BAcode"));
		comMap.put(CherryConstants.BUSINESS_DATE, service.getBusDate(comMap));
		comMap.put(CherryConstants.ORG_CODE, map.get("OrgCode"));
		comMap.put(CherryConstants.BRAND_CODE, map.get("BrandCode"));
		comMap.put(CherryConstants.CREATEDBY, "WS");
		comMap.put(CherryConstants.CREATEPGM, "WS");
		comMap.put(CherryConstants.UPDATEDBY, "WS");
		comMap.put(CherryConstants.UPDATEPGM, "WS");
		return comMap;
	}

	/**
	 * 修改单据信息
	 */
	@Override
	public Map<String, Object> tran_changeOrder(Map<String, Object> map) throws Exception {
		Map<String, Object> retMap = new HashMap<String, Object>();
		String curSate = service.getOrderState(map);
		if (null == curSate || "".equals(curSate)) {
			retMap.put("ERRORCODE", 1);
			retMap.put("ERRORMSG", "当前活动单据不存在");
			return retMap;
		} else if (CampConstants.BILL_STATE_OK.equals(curSate)) {
			retMap.put("ERRORCODE", 2);
			retMap.put("ERRORMSG", "当前活动单据状态【OK】，无法当前操作");
			return retMap;
		} else if (CampConstants.BILL_STATE_CA.equals(curSate)) {
			retMap.put("ERRORCODE", 3);
			retMap.put("ERRORMSG", "当前活动单据状态【CA】，无法当前操作");
			return retMap;
		}
		String sendPos = ConvertUtil.getString(map.get("SendPos"));
		String billNo = ConvertUtil.getString(map.get("BillNo"));
		Map<String, Object> p = new HashMap<String, Object>();
		p.put(CampConstants.BILL_NO, billNo);
		p.put(CherryConstants.BRAND_CODE, map.get("BrandCode"));
		Map<String, Object> comMap = getComMap(map);
		map.put(CherryConstants.UPDATEDBY, "WS");
		map.put(CherryConstants.UPDATEPGM, "WS");
		// 更新单据
		service.changeOrder(map);
		// 追加历史记录
		List<Map<String, Object>> hisList = ser05.getCampHistory(p);
		if (null != hisList && hisList.size() > 0) {
			for (Map<String, Object> his : hisList) {
				his.put("memId", map.get("MemberInfoID"));
				his.put("state", curSate);
				his.put(CampConstants.OPT_TIME, map.get("OptTime"));
				his.putAll(comMap);
			}
			ser05.addCampHistory(hisList);
		}
		if ("1".equals(sendPos)) {
			// 发送MQ到终端
			com05IF.sendPOSMQ(comMap, billNo);
		}
		// String batchNo =
		// ConvertUtil.getString(comMap.get(CampConstants.BATCHNO));
		// // 发送沟通MQ
		// com05IF.sendGTMQ(comMap, batchNo, state);
		// retMap.put("ResultString", "");
		return retMap;
	}

	/**
	 * 取得会员List
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private Map<String, Object> searchMemInfo(Map<String, Object> map) throws Exception {
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
		// 积分
		selectors.add("totalPoint");
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
			if (null != list && list.size() > 0) {
				memMap = list.get(0);
			}
		}
		return memMap;
	}

	@Override
	public Map<String, Object> tran_applyCoupon(Map<String, Object> map) throws Exception {
		int result = CherryConstants.SUCCESS;
		Map<String, Object> retMap = new HashMap<String, Object>();
		/////////////////////////////////////////////////////////
		try {
			logger.info("######" + CherryUtil.map2Json(map) +"######");
		} catch (Exception e1) {
			logger.error("######CherryUtil.map2Json(map)######");
		}
		/////////////////////////////////////////////////////////
		// 参数MAP
		Map<String, Object> comMap = getCommMap(map);
		String orgId = ConvertUtil.getString(comMap.get(CherryConstants.ORGANIZATIONINFOID));
		String brandId = ConvertUtil.getString(comMap.get(CherryConstants.BRANDINFOID));
		String subCampCode = ConvertUtil.getString(map.get("SubCampCode"));
		String cntGot = ConvertUtil.getString(map.get("CounterCodeGet"));
		if ("".equals(subCampCode)) {
			retMap.put("ERRORCODE", 21);
			retMap.put("ERRORMSG", "活动码参数缺失");
			logger.error(">>>>>>>>>>>>>>>>>活动码参数缺失>>>>>>>>>>>>>>>>>>>>>>");
			return retMap;
		}
		if ("".equals(cntGot)) {
			retMap.put("ERRORCODE", 22);
			retMap.put("ERRORMSG", "领取柜台参数缺失");
			logger.error(">>>>>>>>>>>>>>>>>领取柜台缺失>>>>>>>>>>>>>>>>>>>>>>");
			return retMap;
		} else {
			String validCnt = ConvertUtil.getString(map.get("ValidCounter"));
			if (!"ALL".equalsIgnoreCase(cntGot) && !"1".equals(validCnt)) {
				Map<String, Object> p = new HashMap<String, Object>();
				p.put("BIN_OrganizationInfoID", orgId);
				p.put("BIN_BrandInfoID", brandId);
				p.put("CounterCode", cntGot);
				boolean f = true;
				try {
					Map<String, Object> depart = mq97BL.getOrganizationInfo(p, false);
					if (null == depart) {
						f = false;
					}
				} catch (CherryMQException e) {
					f = false;
					logger.error(">>>>>>>>>>>>>>>>>领用柜台号验证查询异常>>>>>>>>>>>>>>>>>>>>>>" + e.getMessage(),e);
				}
				if (!f) {
					retMap.put("ERRORCODE", 23);
					retMap.put("ERRORMSG", "领取柜台不存在");
					logger.error(">>>>>>>>>>>>>>>>>领取柜台不存在>>>>>>>>>>>>>>>>>>>>>>");
					return retMap;
				}
			}
		}
		String memCode = ConvertUtil.getString(map.get("MemberCode"));
		Map<String, Object> memInfo = null;
		if (!"".equals(memCode)) {
			Map<String, Object> p = new HashMap<String, Object>();
			p.put("memCode", memCode);
			p.put("testTypeIgnore", 1);
			p.put("brandInfoId", map.get("BIN_BrandInfoID"));
			p.put("organizationInfoId", map.get("BIN_OrganizationInfoID"));
			p.put("brandCode", map.get("BrandCode"));
			// 获取会员信息
			memInfo = searchMemInfo(p);
			if (null != memInfo) {
				memInfo.put("cntCodeBelong", memInfo.get("counterCode"));
				memInfo.remove("counterCode");
			} else {
				retMap.put("ERRORCODE", 28);
				retMap.put("ERRORMSG", "会员不存在");
				logger.error(">>>>>>>>>>>>>>>>>"+memCode+"会员不存在>>>>>>>>>>>>>>>>>>>>>>");
				return retMap;
			}
		}
		// 添加到活动预约相关表
		Map<String, Object> order = new HashMap<String, Object>();
		if (null != memInfo) {
			order.putAll(memInfo);
		}
		try {
			setCouponOrder(order, map);
		} catch (Exception e) {
			retMap.put("ERRORCODE", 24);
			retMap.put("ERRORMSG", "单据填充失败");
			logger.error(">>>>>>>>>>>>>>>>>" + e.getMessage() + ">>>>>>>>>>>>>>>>>>>>>>",e);
			return retMap;
		}
		//===================获取锁2016-04-26开始===================//
		String lockKey = getLockKey(order,"A");
		Lock lock = getLock(lockKey);
		lock.lock();
		//===================获取锁2016-04-26结束===================//
		// MQ插入预约相关表
		try {
			result = com05IF.tran_campOrderMQ(comMap, order);
			if (CherryConstants.SUCCESS == result) {
				Map<String, Object> content = new HashMap<String, Object>();
				content.put(CampConstants.BILL_NO, order.get(CampConstants.BILL_NO));
				content.put(CampConstants.COUPON_CODE, order.get(CampConstants.COUPON_CODE));
				content.put(CampConstants.OBTAIN_FROMDATE, order.get(CampConstants.OBTAIN_FROMDATE));
				content.put(CampConstants.OBTAIN_TODATE, order.get(CampConstants.OBTAIN_TODATE));
				retMap.put("ResultMap", content);
			} else {
				retMap.put("ERRORCODE", result);
				retMap.put("ERRORMSG", com05IF.getErrMsg(result + ""));
				return retMap;
			}

		} catch (CherryException e) {
			retMap.put("ERRORCODE", 30);
			retMap.put("ERRORMSG", "预约异常");
			return retMap;
		}finally{
			//===================释放锁2016-04-26开始===================//
			lock = lockMap.get(lockKey);
			if(null != lock){
				lockMap.remove(lockKey);
			}
			//===================释放锁2016-04-26结束===================//
		}
		if (result == CherryConstants.SUCCESS) {
			try {
				String batchNo = ConvertUtil.getString(comMap.get(CampConstants.BATCHNO));
				// 发送沟通MQ
				com05IF.sendGTMQ(comMap, batchNo, CampConstants.BILL_STATE_RV);
			} catch (Exception e) {
				retMap.put("ERRORCODE", 26);
				retMap.put("ERRORMSG", "发送沟通信息失败");
				return retMap;
			}
		}
		return retMap;
	}
	
	private synchronized Lock getLock(String lockKey){
		Lock lock = lockMap.get(lockKey);
		if(null == lock){
			lock = new ReentrantLock();
			lockMap.put(lockKey, lock);
		}
		return lock;
	}

	/**
	 * 获取对象锁KEY
	 * @param order
	 * @param key
	 * @return
	 */
	private String getLockKey(Map<String, Object> order, String key) {
		String memCode = ConvertUtil.getString(order.get("memCode"));
		if (!"".equals(memCode)) {
			key += memCode;
		}else{
			String mobilePhone = ConvertUtil.getString(order.get("mobilePhone"));
			if (!"".equals(mobilePhone)) {
				key += mobilePhone;
			}else{
				String messageId = ConvertUtil.getString(order.get("messageId"));
				if (!"".equals(messageId)) {
					key += messageId;
				}
			}
		}
		return key;
	}

	/**
	 * map:(BIN_OrganizationInfoID,BIN_BrandInfoID,OrgCode,BrandCode,MemberCode,SubCampCode,BookDateTime,CounterCodeGet,CouponCode,DataSourse)
	 * campInfo:活动信息
	 * prtList: SubCampCode对应的活动礼品LIST
	 */
	@Override
	public Map<String, Object> makeOrder(Map<String, Object> map,Map<String, Object> campInfo,List<Map<String, Object>> prtList) throws Exception {
		int result = CherryConstants.SUCCESS;
		Map<String, Object> retMap = new HashMap<String, Object>();
		// 参数MAP
		Map<String, Object> comMap = getCommMap(map);
		String memCode = ConvertUtil.getString(map.get("MemberCode"));
		Map<String, Object> memInfo = null;
		if (!"".equals(memCode)) {
			Map<String, Object> p = new HashMap<String, Object>();
			p.put("memCode", memCode);
			p.put("testTypeIgnore", 1);
			p.put("brandInfoId", map.get("BIN_BrandInfoID"));
			p.put("organizationInfoId", map.get("BIN_OrganizationInfoID"));
			p.put("brandCode", map.get("BrandCode"));
			// 获取会员信息
			memInfo = searchMemInfo(p);
			if (null != memInfo) {
				memInfo.put("cntCodeBelong", memInfo.get("counterCode"));
				memInfo.remove("counterCode");
			} else {
				retMap.put("ERRORCODE", 28);
				retMap.put("ERRORMSG", "会员不存在");
				logger.error(">>>>>>>>>>>>>>>>>会员不存在>>>>>>>>>>>>>>>>>>>>>>");
				return retMap;
			}
		}
		// 添加到活动预约相关表
		Map<String, Object> order = new HashMap<String, Object>();
		if (null != memInfo) {
			order.putAll(memInfo);
		}
		try {
			String mPhone = ConvertUtil.getString(map.get("MobilePhone"));
			String orgId = ConvertUtil.getString(map.get("BIN_OrganizationInfoID"));
			String brandId = ConvertUtil.getString(map.get("BIN_BrandInfoID"));
			// 单据号
			order.put(CampConstants.BILL_NO, cm03bl.getTicketNumber(orgId, brandId, "", "SP"));
			// 操作类型
			order.put(CampConstants.BILL_STATE, CampConstants.BILL_STATE_AR);
			// 不维护积分
			order.put("mqWaitFlag", "0");
			order.put("sendPos", "1");
			if (!"".equals(mPhone)) {
				order.put("mobilePhone", mPhone);
			}
			if (!"".equals(memCode)) {
				order.put("memCode", memCode);
			}
			order.put("orderCntCode", map.get("CounterCode"));
			order.put(CampConstants.OPT_TIME, map.get("BookDateTime"));
			order.put(CampConstants.COUPON_CODE, map.get("CouponCode"));
			// 单据明细
			order.put(CampConstants.KEY_LIST, prtList);
		} catch (Exception e) {
			retMap.put("ERRORCODE", 24);
			retMap.put("ERRORMSG", "单据填充失败");
			logger.error(">>>>>>>>>>>>>>>>>" + e.getMessage() + ">>>>>>>>>>>>>>>>>>>>>>",e);
			return retMap;
		}
		// MQ插入预约相关表
		try {
			result = com05IF.makeCampOrder(comMap, campInfo, order);
			if (CherryConstants.SUCCESS != result) {
				retMap.put("ERRORCODE", result);
				retMap.put("ERRORMSG", com05IF.getErrMsg(result + ""));
				return retMap;
			}
		} catch (CherryException e) {
			retMap.put("ERRORCODE", 30);
			retMap.put("ERRORMSG", "预约异常");
			return retMap;
		}
		return retMap;
	}
	
	/**
	 * 领用活动申请（benefit使用）
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> tran_campaignBespeak(Map<String, Object> map) throws Exception {
		int result = CherryConstants.SUCCESS;
		int departId = 0;
		Map<String, Object> retMap = new HashMap<String, Object>();
		// 参数MAP
		Map<String, Object> comMap = getCommMap(map);
		String orgId = ConvertUtil.getString(comMap.get(CherryConstants.ORGANIZATIONINFOID));
		String brandId = ConvertUtil.getString(comMap.get(CherryConstants.BRANDINFOID));
		String subCampCode = ConvertUtil.getString(map.get("SubCampCode"));
		String cntGot = ConvertUtil.getString(map.get("CounterCodeGet"));
		comMap.put("CouponPrefix", subCampCode);
		if ("".equals(subCampCode)) {
			retMap.put("ERRORCODE", 21);
			retMap.put("ERRORMSG", "活动码参数缺失");
			logger.error(">>>>>>>>>>>>>>>>>活动码参数缺失>>>>>>>>>>>>>>>>>>>>>>");
			return retMap;
		}
		if ("".equals(cntGot)) {
			retMap.put("ERRORCODE", 22);
			retMap.put("ERRORMSG", "领取柜台参数缺失");
			logger.error(">>>>>>>>>>>>>>>>>领取柜台缺失>>>>>>>>>>>>>>>>>>>>>>");
			return retMap;
		} else {
			String validCnt = ConvertUtil.getString(map.get("ValidCounter"));
			if (!"ALL".equalsIgnoreCase(cntGot) && !"1".equals(validCnt)) {
				Map<String, Object> p = new HashMap<String, Object>();
				p.put("BIN_OrganizationInfoID", orgId);
				p.put("BIN_BrandInfoID", brandId);
				p.put("CounterCode", cntGot);
				boolean f = true;
				try {
					Map<String, Object> depart = mq97BL.getOrganizationInfo(p, false);
					if (null == depart) {
						f = false;
					} else {
						// 柜台部门ID
						departId = ConvertUtil.getInt(depart.get("BIN_OrganizationID"));
					}
				} catch (CherryMQException e) {
					f = false;
					logger.error(">>>>>>>>>>>>>>>>>领用柜台号验证查询异常>>>>>>>>>>>>>>>>>>>>>>" + e.getMessage(),e);
				}
				if (!f) {
					retMap.put("ERRORCODE", 23);
					retMap.put("ERRORMSG", "领取柜台不存在");
					logger.error(">>>>>>>>>>>>>>>>>领取柜台不存在>>>>>>>>>>>>>>>>>>>>>>");
					return retMap;
				}
			}
		}
		String memCode = ConvertUtil.getString(map.get("MemberCode"));
		Map<String, Object> memInfo = null;
		if (!"".equals(memCode)) {
			Map<String, Object> p = new HashMap<String, Object>();
			p.put("memCode", memCode);
			p.put("testTypeIgnore", 1);
			p.put("brandInfoId", map.get("BIN_BrandInfoID"));
			p.put("organizationInfoId", map.get("BIN_OrganizationInfoID"));
			p.put("brandCode", map.get("BrandCode"));
			// 获取会员信息
			memInfo = searchMemInfo(p);
			if (null != memInfo) {
				memInfo.put("cntCodeBelong", memInfo.get("counterCode"));
				memInfo.remove("counterCode");
			} else {
				retMap.put("ERRORCODE", 28);
				retMap.put("ERRORMSG", "会员不存在");
				logger.error(">>>>>>>>>>>>>>>>>会员不存在>>>>>>>>>>>>>>>>>>>>>>");
				return retMap;
			}
		} else {
			String messageId = ConvertUtil.getString(map.get("MessageID"));
			if (!"".equals(messageId)) {
				Map<String, Object> p = new HashMap<String, Object>();
				p.put("messageId", messageId);
				p.put("testTypeIgnore", 1);
				p.put("brandInfoId", map.get("BIN_BrandInfoID"));
				p.put("organizationInfoId", map.get("BIN_OrganizationInfoID"));
				p.put("brandCode", map.get("BrandCode"));
				// 获取会员信息
				memInfo = searchMemInfo(p);
				if (null != memInfo) {
					memInfo.put("cntCodeBelong", memInfo.get("counterCode"));
					memInfo.remove("counterCode");
				} else {
					// retMap.put("ERRORCODE", 28);
					// retMap.put("ERRORMSG", "会员不存在");
					// logger.error(">>>>>>>>>>>>>>>>>会员不存在>>>>>>>>>>>>>>>>>>>>>>");
					// return retMap;
				}
			}
		}
		// 添加到活动预约相关表
		Map<String, Object> order = new HashMap<String, Object>();
		if (null != memInfo) {
			order.putAll(memInfo);
		}
		try {
			setCouponOrder(order, map);
		} catch (Exception e) {
			retMap.put("ERRORCODE", 24);
			retMap.put("ERRORMSG", "单据填充失败");
			logger.error(">>>>>>>>>>>>>>>>>" + e.getMessage() + ">>>>>>>>>>>>>>>>>>>>>>",e);
			return retMap;
		}
		// 取得主题活动信息
		Map<String, Object> campInfo = com05IF.getCampInfo(subCampCode);
		if (null == campInfo) {
			retMap.put("ERRORCODE", 3);
			retMap.put("ERRORMSG", "活动【" + subCampCode + "】不存在");
			logger.error(">>>>>>>>>>>>>>活动【" + subCampCode + "】不存在>>>>>>>>>>>>>>>");
			return retMap;
		}
		comMap.put("CAMPINFO", campInfo);
		String manageGift = ConvertUtil.getString(campInfo.get("manageGift"));
		cntGot = ConvertUtil.getString(order.get("counterCode"));
		List<Map<String, Object>> giftList = null;
		int next = 1;
		if ("1".equals(manageGift) && !"ALL".equalsIgnoreCase(cntGot)) {
			giftList = new ArrayList<Map<String, Object>>();
			List<Map<String, Object>> prtList = (List<Map<String, Object>>) order.get(CampConstants.KEY_LIST);
			if (null == prtList) {
				retMap.put("ERRORCODE", 15);
				retMap.put("ERRORMSG", "活动无绑定礼品");
				logger.error(">>>>>>>>>>>>>>>>>活动无绑定礼品>>>>>>>>>>>>>>>>>>>>>>");
				return retMap;
			}
			// 获取需要查询库存的礼品
			for (Map<String, Object> prt : prtList) {
				String prmCateCode = ConvertUtil.getString(prt.get("prmCateCode"));
				if (!PromotionConstants.PROMOTION_DHCP_TYPE_CODE.equals(prmCateCode) && !PromotionConstants.PROMOTION_DHMY_TYPE_CODE.equals(prmCateCode)
						&& !PromotionConstants.PROMOTION_TZZK_TYPE_CODE.equals(prmCateCode)) {
					Map<String, Object> i = new HashMap<String, Object>();
					i.put("proId", prt.get("proId"));
					i.put("prtType", prt.get("prtType"));
					i.put("quantity", prt.get("quantity"));
					i.put("departId", departId);
					i.put(CampConstants.SUBCAMP_CODE, subCampCode);
					i.put(CherryConstants.BRANDINFOID, brandId);
					giftList.add(i);
				}
			}
			if (giftList.size() > 0) {
				for (Map<String, Object> i : giftList) {
					// 柜台活动库存查询
					Integer num = service.getCampaignStock(i);
					if (null == num || num < 0) {
						retMap.put("ERRORCODE", 14);
						retMap.put("ERRORMSG", "活动礼品库存不足");
						logger.error(">>>>>>>>>>>>>>>>>活动礼品库存不足>>>>>>>>>>>>>>>>>>>>>>");
						return retMap;
					} else {
						if (next != 0) {
							int q = ConvertUtil.getInt(i.get("quantity"));
							// 当前库存不能继续申请
							if (num < q) {
								next = 0;
							}
						}
					}
				}
			}
		}
		// MQ插入预约相关表
		try {
			result = com05IF.tran_campOrderMQ(comMap, order);
			if (CherryConstants.SUCCESS == result) {
				Map<String, Object> content = new HashMap<String, Object>();
				content.put("CounterApplyFlag", next);
				content.put("CouponCode", order.get(CampConstants.COUPON_CODE));
				content.put("ObtainFromDate", order.get(CampConstants.OBTAIN_FROMDATE));
				content.put("ObtainToDate", order.get(CampConstants.OBTAIN_TODATE));
				retMap.put("ResultMap", content);
				if ("1".equals(manageGift) && !"ALL".equalsIgnoreCase(cntGot) && null != giftList) {
					// 扣减库存
					service.updCampaignStock(giftList);
				}
			} else {
				retMap.put("ERRORCODE", result);
				retMap.put("ERRORMSG", com05IF.getErrMsg(result + ""));
				return retMap;
			}

		} catch (CherryException e) {
			retMap.put("ERRORCODE", 30);
			retMap.put("ERRORMSG", "预约异常");
			return retMap;
		}
		// if(result == CherryConstants.SUCCESS){
		// try {
		// // 单据操作状态
		// String billState =
		// ConvertUtil.getString(order.get(CampConstants.BILL_STATE));
		// String batchNo =
		// ConvertUtil.getString(comMap.get(CampConstants.BATCHNO));
		// // 发送沟通MQ
		// com05IF.sendGTMQ(comMap, batchNo, billState);
		// } catch (Exception e) {
		// retMap.put("ERRORCODE", 26);
		// retMap.put("ERRORMSG", "发送沟通信息失败");
		// return retMap;
		// }
		// }
		return retMap;
	}

	/**
	 * Map转换
	 * 
	 * @param map
	 * @return
	 * @throws CherryMQException
	 */
	private void setCouponOrder(Map<String, Object> order, Map<String, Object> map) throws Exception {
		String memCode = ConvertUtil.getString(map.get("MemberCode"));
		String mPhone = ConvertUtil.getString(map.get("MobilePhone"));
		String billNo = ConvertUtil.getString(map.get("TradeNoIF"));
		String orgId = ConvertUtil.getString(map.get("BIN_OrganizationInfoID"));
		String brandId = ConvertUtil.getString(map.get("BIN_BrandInfoID"));
		String subCampCode = ConvertUtil.getString(map.get("SubCampCode"));
		String sendPos = ConvertUtil.getString(map.get("SendPos"));
		// 单据号
		if ("".equals(billNo)) {
			order.put(CampConstants.BILL_NO, cm03bl.getTicketNumber(orgId, brandId, "", "SP"));
		} else {
			order.put(CampConstants.BILL_NO, billNo);
		}
		if ("".equals(sendPos)) {
			sendPos = "0";
		}
		// 操作类型
		order.put(CampConstants.BILL_STATE, CampConstants.BILL_STATE_RV);
		// 活动档次
		order.put(CampConstants.SUBCAMP_CODE, subCampCode);
		// 不维护积分
		order.put("mqWaitFlag", "0");
		order.put("name", map.get("Name"));
		order.put("sendPos", sendPos);
		order.put("bookDate", map.get("BookDate"));
		order.put("bookTimeRange", map.get("BookTimeRange"));
		if (!"".equals(mPhone)) {
			order.put("mobilePhone", mPhone);
		}
		if (!"".equals(memCode)) {
			order.put("memCode", memCode);
		}
		order.put("dataSource1", map.get("DataSource1"));
		order.put("dataSource2", map.get("DataSource2"));
		order.put("dataSource3", map.get("DataSource3"));
		order.put("messageId", map.get("MessageID"));
		order.put("orderCntCode", map.get("CounterCode"));
		order.put("cityCode", map.get("CityCode"));
		order.put("counterCode", map.get("CounterCodeGet"));
		order.put(CampConstants.OPT_TIME, map.get("BookDateTime"));
		order.put(CampConstants.COUPON_CODE, map.get("CouponCode"));
		order.put("deliveryMothod", map.get("DeliveryType"));
		order.put("address", map.get("DeliveryAddress"));
		Map<String, Object> p = new HashMap<String, Object>();
		p.put(CherryConstants.BRANDINFOID, brandId);
		p.put(CampConstants.SUBCAMP_CODE, subCampCode);
		// 单据明细
		List<Map<String, Object>> prtList = service.getActResultList(p);
		if (null != prtList) {
			order.put(CampConstants.KEY_LIST, prtList);
		} else {
			throw new Exception("无法获取活动礼品信息");
		}
	}
	
	/**
	 * 获取会员活动历史
	 */
	public Map<String,Object> getCampHistory(Map<String,Object> map){
		Map<String, Object> retMap = new HashMap<String, Object>();
		String campCode = ConvertUtil.getString(map.get("CampCode"));
		if(!"".equals(campCode)) {
			String[] campCodeArray = campCode.split(",");
			map.put("CampCode", campCodeArray);
		}
		
		List<Map<String, Object>> list = service.getCampHistoryList(map);
		if (list == null || list.size() == 0) {
			list = new ArrayList<Map<String, Object>>();
		}
		retMap.put("ResultContent", list);
		return retMap;
	}
	
	/**
	 * 获取会员历史活动码
	 */
	public Map<String,Object> getCampHistoryCode(Map<String,Object> map){
		Map<String, Object> retMap = new HashMap<String, Object>();
		List<Map<String, Object>> list = service.getCampHistoryCodeList(map);
		if (list == null || list.size() == 0) {
			list = new ArrayList<Map<String, Object>>();
		}
		retMap.put("ResultContent", list);
		return retMap;
	}

	/**
	 * 获取活动礼品LIST
	 */
	@Override
	public List<Map<String,Object>> getPrtList(int brandInfoId, String subCampCode) {
		Map<String, Object> p = new HashMap<String, Object>();
		p.put(CherryConstants.BRANDINFOID, brandInfoId);
		p.put(CampConstants.SUBCAMP_CODE, subCampCode);
		// 单据明细
		return service.getActResultList(p);
	}

	@Override
	public Map<String,Object> getCouponList(Map<String, Object> map) {
		Map<String, Object> retMap = new HashMap<String, Object>();
		Map<String,Object> param = new HashMap<String, Object>();
		String campCode = (String)map.get("CampCode");
		String couponCount = (String)map.get("CouponCount");
		if(campCode == null || "".equals(campCode)) {
			retMap.put("ERRORCODE", "WSE0063");
			retMap.put("ERRORMSG", "主题活动代码不能为空");
			logger.error("主题活动代码不能为空");
			return retMap;
		}
		if(couponCount == null || "".equals(couponCount)) {
			couponCount = "1000";
		}
		param.put("organizationInfoId", map.get("BIN_OrganizationInfoID"));
		param.put("brandInfoId", map.get("BIN_BrandInfoID"));
		param.put("campCode", campCode);
		param.put("couponCount", couponCount);
		
		try {
			List<String> couponList = binolcpcomcoupon10bl.generateCoupon(param);
			retMap.put("ResultContent", couponList);
		} catch (Exception e) {
			retMap.put("ERRORCODE", "WSE9999");
			retMap.put("ERRORMSG", "生成coupon信息失败");
			logger.error(e.getMessage(), e);
			return retMap;
			
		}
		return retMap;
	}

	@Override
	public Map<String, Object> tran_updateOrderInfo(Map<String, Object> map) {
		Map<String, Object> retMap = new HashMap<String, Object>();
		String campaignCode = (String)map.get("CampaignCode");
		String couponCode = (String)map.get("CouponCode");
		String messageId = (String)map.get("MessageId");
		if(campaignCode == null || "".equals(campaignCode)) {
			retMap.put("ERRORCODE", "WSE0063");
			retMap.put("ERRORMSG", "campaignCode不能为空");
			logger.error("campaignCode不能为空");
			return retMap;
		}
		if(couponCode == null || "".equals(couponCode)) {
			retMap.put("ERRORCODE", "WSE0063");
			retMap.put("ERRORMSG", "couponCode不能为空");
			logger.error("couponCode不能为空");
			return retMap;
		}
		if(messageId == null || "".equals(messageId)) {
			retMap.put("ERRORCODE", "WSE0063");
			retMap.put("ERRORMSG", "messageId不能为空");
			logger.error("messageId不能为空");
			return retMap;
		}
		try {
			service.updateOrderInfo(map);
		} catch (Exception e) {
			retMap.put("ERRORCODE", "WSE9999");
			retMap.put("ERRORMSG", "更新预约单据失败");
			logger.error(e.getMessage(), e);
			return retMap;
		}
		return retMap;
	}
}
