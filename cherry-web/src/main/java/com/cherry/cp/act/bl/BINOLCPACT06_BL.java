package com.cherry.cp.act.bl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.cmbussiness.bl.BINOLCM03_BL;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.core.CodeTable;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.DateUtil;
import com.cherry.cp.act.service.BINOLCPACT06_Service;
import com.cherry.cp.common.CampConstants;
import com.cherry.cp.common.interfaces.BINOLCPCOM05_IF;
import com.cherry.mo.common.interfaces.BINOLMOCOM01_IF;

public class BINOLCPACT06_BL {
	
	/**异常日志*/
	private static final Logger logger = LoggerFactory.getLogger(BINOLCPACT06_BL.class);
	/** 会员活动Service */
	@Resource(name="binOLCPACT06_Service")
	private BINOLCPACT06_Service ser;
	
	@Resource(name = "binolcpcom05IF")
	private BINOLCPCOM05_IF com05IF;
	
	@Resource(name = "binOLCM03_BL")
	private BINOLCM03_BL cm03bl;
	@Resource
	private BINOLMOCOM01_IF binOLMOCOM01_BL;
	@Resource
	private CodeTable codeTable;
	
	/**
	 * 会员活动结果List
	 * 
	 * @param map 
	 * @return List
	 */
	public List<Map<String, Object>> getCampOrderList(Map<String, Object> map) {
		
		// 活动结果List
		return ser.getCampOrderList(map);
	}
	/**
	 * 会员活动结果数
	 * 
	 * @param map
	 * @return
	 */
	public int getOrderCount(Map<String, Object> map) {
		// 活动结果数
		return ser.getOrderCount(map);
	}
	/**
	 * 更新活动明细
	 * @param map
	 * @throws Exception
	 */
	public int tran_updateDetailInfo(Map<String, Object> map) throws Exception {
		//更新前领用开始日期
		String getFromTimeOld = ConvertUtil.getString(map.get("getFromTimeOld"));
		//领用开始日期
		String getFromTime = ConvertUtil.getString(map.get("getFromTime"));
		//领用结束时间
		String getToTime = ConvertUtil.getString(map.get("getToTime"));
		//更新前领用时间
		String getToTimeOld = ConvertUtil.getString(map.get("getToTimeOld"));
		//领用柜台
		String counterGot = ConvertUtil.getString(map.get("counterGot"));
		//更新前领用柜台
		String counterGotOld = ConvertUtil.getString(map.get("counterGotOld"));
		//系统时间
		String sysTime = ser.getSYSDate();
		map.put("sysTime", sysTime);
		String sysDate = sysTime.substring(0, 10);
		int updCount = 0;
		boolean errorFlag = true;
		if(getToTime.equals(getToTimeOld) && counterGot.equals(counterGotOld) && getFromTime.equals(getFromTimeOld)){
			updCount = 0;//保存信息没有变更
		}else{
			int counterId = ConvertUtil.getInt(ser.getCounterId(map));
			if(counterId>0||"ALL".equals(counterGot)){
				// --------领用结束日期验证----------//
				// 领用开始日期不正确
				if (!CherryChecker.checkDate(getToTime,
						CherryConstants.DATE_PATTERN)) {
					updCount = 3;
					errorFlag = false;
				}
				if (getFromTimeOld.equals(getFromTime)) {
					// 领用开始不能大于领用结束
					if (CherryChecker.compareDate(getFromTimeOld, getToTime) > 0) {
						updCount = 4;
						errorFlag = false;
					}
				} else {
					// 领用开始不能大于领用结束
					if (CherryChecker.compareDate(getFromTime, getToTime) > 0) {
						updCount = 4;
						errorFlag = false;
					}
				}
				// 领用结束不能小于当前日期
				if (CherryChecker.compareDate(sysDate, getToTime) > 0) {
					updCount = 6;
					errorFlag = false;
				}
				// --------领用开始日期验证----------//
				// 领用开始日期不正确
				if (!CherryChecker.checkDate(getFromTime,
						CherryConstants.DATE_PATTERN)) {
					updCount = 8;
					errorFlag = false;
				}
				// 根据领用结束时间来判断领用开始时间
				if (getToTime.equals(getToTimeOld)) {
					// 领用开始不能大于领用结束
					if (CherryChecker.compareDate(getToTimeOld, getFromTime) < 0) {
						updCount = 9;
						errorFlag = false;
					}
				} else {
					if (CherryChecker.compareDate(getToTime, getFromTime) < 0) {
						updCount = 9;
						errorFlag = false;
					}
				}
				if(errorFlag){
					//开始时间转化为00:00:00.000格式
					String fromTimeUtil = DateUtil.suffixDate(getFromTime, 0);
					map.put("getFromTime",fromTimeUtil);
					//结束时间转化为23:59:59.000格式
					String endTimeUtil = DateUtil.suffixDate(getToTime, 1);
					map.put("getToTime",endTimeUtil);
					updCount = ser.updateDetailInfo(map);
					if(updCount==1){//保存成功发MQ
						String billNo = ConvertUtil.getString(map.get("tradeNoIF"));
						try {
							//发送预约单据MQ到老后台
							com05IF.sendPOSMQ(map,billNo);
						} catch (Exception e) {
							logger.error(e.getMessage(),e);
						}
					}else{//已被其他人修改
						updCount=7;
					}
				}
			}else{//领用柜台错误
				updCount = 2;
			}
		}
		return updCount;
	}
	
	
	/**
	 * 批量更新活动单据
	 * @param map
	 * @throws Exception
	 */
	public int tran_batchUpdate(Map<String, Object> map,Map<String, Object> searchMap) throws Exception {
		//领用结束时间
		String batchToTime = ConvertUtil.getString(map.get("getToTime"));
		//领用柜台
		String counterGot = ConvertUtil.getString(map.get("counterGot"));
		//领用柜台类型
		String counterType = ConvertUtil.getString(map.get("counterType"));
		//系统时间
		String sysTime = ser.getSYSDate();
		map.put("sysTime", sysTime);
		//系统当前日期
		String sysDate = sysTime.substring(0, 10);
		int updCount = 0;
		boolean errorFlag = true;
		if(!CherryChecker.isNullOrEmpty(batchToTime)){
			// 领用结束不能小于当前日期
			if (CherryChecker.compareDate(sysDate, batchToTime) > 0) {
				updCount = 6;
				errorFlag = false;
			}
		}
		if("0".equals(counterType)){
			//领用柜台Id
			int counterId = ConvertUtil.getInt(ser.getCounterId(map));	
			if(counterId>0||"ALL".equals(counterGot)){
				if(errorFlag){
					saveCampOrderInfo(map,searchMap);
					updCount = 1;
				}
			}else{
				if(!CherryChecker.isNullOrEmpty(counterGot)){
					//领用柜台错误
					updCount = 2;
				}else{
					if(errorFlag){
						saveCampOrderInfo(map,searchMap);
						updCount = 1;
					}
				}
			}	
		}else{
			if(errorFlag){
				saveCampOrderInfo(map,searchMap);
				updCount = 1;
			}
		}
		
		return updCount;
	}
	
	/**
	 * 从数据库分批取数据并更新
	 * 
	 * @param map
	 * @throws Exception
	 */
	public void saveCampOrderInfo(Map<String, Object> map,Map<String, Object> searchMap) throws Exception {
		// 领用柜台类型
		String counterType = ConvertUtil.getString(map.get("counterType"));
		if("1".equals(counterType)){
			//预约柜台
			ser.updResCounter(searchMap);
		}else if("2".equals(counterType)){
			//发卡柜台
			ser.updCounterIssue(searchMap);
		}		
		// 数据抽出次数
		int currentNum = 0;
		while (true) {
			// 查询开始位置
			int startNum = CherryConstants.BATCH_PAGE_MAX_NUM * currentNum + 1;
			// 查询结束位置
			int endNum = startNum + CherryConstants.BATCH_PAGE_MAX_NUM - 1;
			// 数据抽出次数累加
			currentNum++;
			// 查询开始位置
			searchMap.put(CherryConstants.START, startNum);
			// 查询结束位置
			searchMap.put(CherryConstants.END, endNum);
			// 排序字段(明细ID)
			searchMap.put(CherryConstants.SORT_ID, "campOrderId");
			//单据信息List
			List<Map<String, Object>> sucList = getCampOrderList(searchMap);
			for(Map<String, Object> sucMap : sucList){
				//转化日期
				this.convertDate(map,sucMap);
			}
			// 数据不为空
			if (null != sucList && sucList.size() > 0) {
				//批量更新单据信息
				ser.batchUpdCampOrder(sucList);
				//发送MQ消息
				this.send_MQ(map,sucList);
				if (sucList.size() < CherryConstants.BATCH_PAGE_MAX_NUM) {
					break;
				}
			} else {
				break;
			}
		}
	}

	/**
	 * 日期转化
	 * @param map
	 * @param order
	 */
	private void convertDate (Map<String, Object> map,Map<String, Object> sendMap){
		//领用开始日期
		String batchFromTime = ConvertUtil.getString(map.get("getFromTime"));
		//领用结束时间
		String batchToTime = ConvertUtil.getString(map.get("getToTime"));
		//领用柜台
		String counterGot = ConvertUtil.getString(map.get("counterGot"));
		//转化领用开始日期
		if("1".equals(map.get("referFromType"))){
			int referFromValue = ConvertUtil.getInt(map.get("referFromValue"));
			if("1".equals(map.get("referFromParam"))){
				//提前天数
				map.put("fromValue", referFromValue*(-1));
			}else{
				//延后天数
				map.put("fromValue", referFromValue);
			}
			map.put("getFromTime", sendMap.get("getFromTime"));
			String convertDateFrom= ConvertUtil.getString(ser.getDateAddFrom(map));
			sendMap.put("getFromTime", convertDateFrom);
		}else{
			if(!CherryChecker.isNullOrEmpty(batchFromTime)){
				sendMap.put("getFromTime", batchFromTime);
			}
		}
		//转化领用开始日期
		if("1".equals(map.get("referToType"))){
			int referToValue = ConvertUtil.getInt(map.get("referToValue"));
			if("1".equals(map.get("referToParam"))){
				//提前天数
				map.put("toValue", referToValue*(-1));
			}else{
				//延后天数
				map.put("toValue", referToValue);
			}	
			map.put("getToTime", sendMap.get("getToTime"));
			String convertDateTo = ConvertUtil.getString(ser.getDateAddTo(map));
			sendMap.put("getToTime", convertDateTo);
		}else{
			if(!CherryChecker.isNullOrEmpty(batchToTime)){
				sendMap.put("getToTime", batchToTime);
			}
		}
		if(!CherryChecker.isNullOrEmpty(counterGot)){
			sendMap.put("counterGot", counterGot);
		}
		//开始时间转化为00:00:00.000格式
		String fromTimeUtil = DateUtil.suffixDate(ConvertUtil.getString(sendMap.get("getFromTime")), 0);
		sendMap.put("getFromTime",fromTimeUtil);
		//结束时间转化为23:59:59.000格式
		String endTimeUtil = DateUtil.suffixDate(ConvertUtil.getString(sendMap.get("getToTime")), 1);
		sendMap.put("getToTime",endTimeUtil);
	}
	
	/**
	 * 单据发送MQ
	 * @param map
	 * @param orderList
	 */
	private void send_MQ(Map<String, Object> map,List<Map<String, Object>> orderList){
		if(orderList!=null && orderList.size() > 0){
			for(Map<String, Object> order : orderList){
				String state = ConvertUtil.getString(order.get("state"));
				if(!"CA".equals(state) && !"OK".equals(state)){
					String billNo = ConvertUtil.getString(order.get("billNo"));
					try {
						//发送预约单据MQ到老后台
						com05IF.sendPOSMQ(map,billNo);
					} catch (Exception e) {
						logger.error(e.getMessage(),e);
					}
				}	
			}
		}
	}
	
	/**
	 * 活动明细基本信息
	 * @param map
	 * @return
	 */
	public Map<String, Object> getCampDetailMap(Map<String, Object> map) {
		//活动明细基本信息
		return ser.getCampDetailMap(map);
		
	}
	
	/**
	 * 礼品信息List
	 * 
	 * @param map 
	 * @return List
	 */
	public List<Map<String, Object>> getPrtInfoList(Map<String, Object> map) {
		
		// 活动结果List
		return ser.getPrtInfoList(map);
	}
	
	public String getSYSDateTime(){
		return ser.getSYSDateTime();
	}
	
	public String getBusDate(Map<String, Object> map){
		return ser.getBussinessDate(map);
	}
    /**
     * 取得单据信息
     */
    public Map<String,Object> getTradeNo(Map<String,Object> map){
        return ser.getTradeNo(map);
    }
    /**
     * 取得编辑后的信息
     */
    public Map<String,Object> getEditInfo(Map<String,Object> map){
        return ser.getEditInfo(map);
    }
	/**
	 * 单据批量操作（更新）
	 * @param paramMap
	 * @param billState
	 * @param comMap
	 * @return
	 */
	public int updOrder(Map<String, Object> paramMap,String billState, Map<String, Object> comMap){
		int result = CherryConstants.SUCCESS;
		int start = 1;
		int sucessCount = 0;
		int failureCount = 0;
		String orgId = ConvertUtil.getString(comMap.get(CherryConstants.ORGANIZATIONINFOID));
		String brandId = ConvertUtil.getString(comMap.get(CherryConstants.BRANDINFOID));
		String batchNo = cm03bl.getTicketNumber(orgId, brandId, "", "OP");
		comMap.put(CampConstants.BATCHNO, batchNo);
		Map<String, Object> campInfo = null;
		logger.info("=========更新活动单据状态开始：batchNo=" + batchNo + ",更新状态为："+billState+"=========");
		while(true){
			paramMap.put("SORT_ID", "campOrderId");
			paramMap.put("START", start);
			paramMap.put("END", start + CherryConstants.BATCH_PAGE_MAX_NUM - 1);
			List<Map<String, Object>> orderList = getCampOrderList(paramMap);
			if(null == orderList || orderList.size() == 0){
				break;
			}
			if(null == campInfo && CampConstants.BILL_STATE_CA.equals(billState)){
				String campCode = ConvertUtil.getString(orderList.get(0).get(CampConstants.CAMP_CODE));
				campInfo = ser.getCampInfo(campCode);
			}
			for(Map<String, Object> order : orderList){
				order.put(CampConstants.BILL_STATE, billState);
				order.put(CampConstants.BATCHNO, batchNo);
			}
			int size = orderList.size();
			try {
				int r = com05IF.tran_campOrderBAT(comMap, null, billState, orderList);
				if( r > result){
					result = r;
					start += size;
					failureCount +=size;
				}else{
					sucessCount += size;
				}
			} catch (CherryException e) {
				start += size;
				failureCount +=size;
				result = CherryConstants.ERROR;
				logger.error(e.getMessage(),e);
			}
			if(orderList.size() < CherryConstants.BATCH_PAGE_MAX_NUM){
				break;
			}
		}
		paramMap.put("sucessCount", sucessCount);
		paramMap.put("failureCount", failureCount);

		// 发送沟通MQ
		int r = com05IF.sendGTMQ(comMap, batchNo, billState);
		if( r > result){
			result = r;
		}
		// 发送积分维护MQ
		if(null != campInfo && CampConstants.BILL_STATE_CA.equals(billState)){
			try {
				com05IF.sendPointMQ(comMap, campInfo);
			} catch (Exception e) {
				logger.error(e.getMessage(),e);
			}
		}
		return result;
	}
	/**
	 * 单据批量操作（更新）
	 * @param paramMap
	 * @param comMap
	 * @return
	 */
	public int tran_updOrder2(Map<String, Object> paramMap, Map<String, Object> comMap){
		int result = CherryConstants.SUCCESS;
		String orgId = ConvertUtil.getString(comMap.get(CherryConstants.ORGANIZATIONINFOID));
		String brandId = ConvertUtil.getString(comMap.get(CherryConstants.BRANDINFOID));
		String billNo = ConvertUtil.getString(paramMap.get(CampConstants.BILL_NO));
		String batchNo = cm03bl.getTicketNumber(orgId, brandId, "", "OP");
		comMap.put(CampConstants.BATCHNO, batchNo);
		paramMap.putAll(comMap);
		try {
			ser.updCampOrder(paramMap);
			ser.updCampOrderHis(paramMap);
			// 发送活动单据MQ-->POS
			com05IF.sendPOSMQ(comMap, billNo);
			// 发送沟通MQ
			int r = com05IF.sendGTMQ(comMap, batchNo, "AR");
			if( r > result){
				result = r;
			}
		} catch (Exception e) {
			result = 9999;
			logger.error(e.getMessage(),e);
		}
		return result;
	}
	// Excel导出数据查询条件数组
	private final static String[] campCondition = {"tradeNoIF", "memCode",
		"mobile","state","startDate","endDate","testType","couponCode","counterGot","counterOrder","sendFlag"};
	// Excel一览导出列数组
	private final static String[][] campArray = {
		// 1
		{ "campCode", "ACT06_campCode", "20", "", "" },
		// 2
		{ "campaignName", "ACT06_campName", "20", "", "" },
		// 3
		{ "subCampCode", "ACT06_subCampCode", "20", "", "" },
		// 4
		{ "subCampaignName", "ACT06_subCampName", "20", "", "" },
		// 5
		{ "campOrderTime", "ACT06_campOrderTime", "20", "", "" },
		// 6
		{ "counterOrder", "ACT06_ordercountCode", "20", "", "" },
		// 7
		{ "memName", "ACT06_memName", "20", "", "" },
		// 8
		{ "memCode", "ACT06_memCode", "20", "", "" },
		// 9
		{ "mobile", "ACT06_mobile", "20", "", "" },
		// 10
		{ "messageId", "ACT06_messageId", "20", "", "" },
		// 11
		{ "testType", "ACT06_testType", "10", "", "1256" },
		// 12
		{ "getFromTime", "ACT06_getFromTime", "20", "", "" },
		// 13
		{ "getToTime", "ACT06_getToTime", "20", "", "" },
		// 14
		{ "counterGot", "ACT06_counterGot", "20", "", "" },
		// 14
		{ "counterGotName", "ACT06_counterGotName", "20", "", "" },
		// 15
		{ "couponCode", "ACT06_couponCode", "20", "", "" },
		// 16
		{ "billNo", "ACT06_tradeNoIF", "30", "", "" },
		// 17
		{ "state", "ACT06_state", "10", "", "1116" },
		// 18
		{ "nameTotal", "ACT06_actGiftName", "20", "", "" },
		// 19
		{ "prtTpye", "ACT06_prtType", "10", "", "1134" },
		// 20
		{ "unitCode", "ACT06_actGiftUnitCode", "20", "", "" },
		// 21
		{ "barCode", "ACT06_actGiftName", "20", "", "" },
		// 22
		{ "quantity", "ACT06_actGiftQuantity", "10", "right", "" },
		// 23
		{ "amout", "ACT06_actGiftPrice", "10", "right", "" },
		// 24
		{ "pointRequired", "ACT06_pointRequired", "10", "right", "" }};
	/**
	 * Excel导出一览
	 * 
	 * @param map
	 * @return 返回导出信息
	 * @throws CherryException
	 */
	public byte[] exportExcel(Map<String, Object> map) throws Exception {
		//Excel导出总数量
		int dataLen = ser.getExcelCount(map);
		map.put("dataLen", dataLen);
		BINOLMOCOM01_IF.ExcelParam ep = new BINOLMOCOM01_IF.ExcelParam();
		// 排序字段(明细ID)
		map.put(CherryConstants.SORT_ID, "campOrderId");
		// 必须设置sqlID（必须）,用于批查询
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCPACT06.getExcelResultList");
		ep.setMap(map);
		// 导出数据列数组
		ep.setArray(campArray);
		// 导出数据列头国际化资源文件
		ep.setBaseName("BINOLCPACT06");
		ep.setShowTitleStyle(true);
		ep.setSearchCondition(getConditionStr(map));
		return binOLMOCOM01_BL.getBatchExportExcel(ep);
	}
	/**
	 * 取得条件字符串
	 * 
	 * @param map
	 * @return
	 */
	private String getConditionStr(Map<String, Object> map) {
		String language = ConvertUtil.getString(map
				.get(CherryConstants.SESSION_LANGUAGE));
		StringBuffer condition = new StringBuffer();
		int num = 0;
		for (String con : campCondition) {
			// 条件值
			String paramValue = ConvertUtil.getString(map.get(con));
			if (!"".equals(paramValue)) {
				// 条件名
				String paramName = "";
				if ("tradeNoIF".equals(con)) {
					// 活动单据
					paramName = binOLMOCOM01_BL.getResourceValue(
							"BINOLCPACT06", language, "ACT06_tradeNoIF");
				} else if ("memCode".equals(con)) {
					// 会员卡号
					paramName = binOLMOCOM01_BL.getResourceValue(
							"BINOLCPACT06", language, "ACT06_memCode");
				} else if ("mobile".equals(con)) {
					// 会员手机
					paramName = binOLMOCOM01_BL
							.getResourceValue("BINOLCPACT06", language,
									"ACT06_mobile");
				}else if ("testType".equals(con)) {
					// 会员类型
					paramName = binOLMOCOM01_BL.getResourceValue(
							"BINOLCPACT06", language, "ACT06_testType");
					paramValue = codeTable.getVal("1256", paramValue);
				}else if ("state".equals(con)) {
					// 单据状态
					paramName = binOLMOCOM01_BL.getResourceValue(
							"BINOLCPACT06", language, "ACT06_state");
					paramValue = codeTable.getVal("1116", paramValue);
				}else if ("startDate".equals(con) || "endDate".equals(con)) {
					// 预约日期
					paramName = binOLMOCOM01_BL.getResourceValue(
							"BINOLCPACT06",language, "ACT06_orderTimeDate");
					// 日期
					paramName = binOLMOCOM01_BL.getResourceValue(
							"BINOLCPACT06",language, "ACT06_order" + con);
				}else if ("counterOrder".equals(con)) {
					// 预约柜台
					paramName = binOLMOCOM01_BL.getResourceValue(
							"BINOLCPACT06", language, "ACT06_ordercountCode");
				} else if ("counterGot".equals(con)) {
					// 领用柜台
					paramName = binOLMOCOM01_BL.getResourceValue(
							"BINOLCPACT06", language, "ACT06_counterGot");
				}else if ("couponCode".equals(con)) {
					// couponCode码
					paramName = binOLMOCOM01_BL.getResourceValue(
							"BINOLCPACT06", language, "ACT06_couponCode");
				}else if ("sendFlag".equals(con)) {
					// 下发区分
					paramName = binOLMOCOM01_BL.getResourceValue(
							"BINOLCPACT06", language, "ACT06_state");
					if("0".equals(paramValue)){
						paramValue=CherryConstants.SEND_FLAG_0;
					}else if("1".equals(paramValue)){
						paramValue=CherryConstants.SEND_FLAG_1;
					}else if("2".equals(paramValue)){
						paramValue=CherryConstants.SEND_FLAG_2;
					}
				}  
				num++;
				String splitStr;
				// 换行，空格控制
				if (num % 6 == 0) {
					splitStr = "\n";
				} else {
					splitStr = "\0\0\0\0\0";
				}
				condition.append(paramName + "：" + paramValue + splitStr);
			}
		}
		return condition.toString().trim();
	}
	
	// 优惠券一览导出列数组
	private final static String[][] couPonArray = {
		// 1
		{ "campCode", "ACT06_subCampCode", "20", "", "" },
		// 3
		{ "mobile", "ACT06_mobile", "20", "", "" },
		// 4
		{ "couponCode", "ACT06_couponCode", "20", "", "" },
		// 5
		{ "getFromTime", "ACT06_couStart", "25", "", "" },
		// 6
        { "getToTime", "ACT06_couEnd", "25", "", "" }
		};
	
	/**
	 * 优惠券Excel导出一览
	 * 
	 * @param map
	 * @return 返回导出信息
	 * @throws CherryException
	 */
	public byte[] couponExportExcel(Map<String, Object> map) throws Exception {
		//Excel导出总数量
		int dataLen = ser.getExcelCouponCount(map);
		map.put("dataLen", dataLen);
		BINOLMOCOM01_IF.ExcelParam ep = new BINOLMOCOM01_IF.ExcelParam();
		// 排序字段(明细ID)
		map.put(CherryConstants.SORT_ID, "campOrderId");
		// 必须设置sqlID（必须）,用于批查询
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCPACT06.getExcelCouponResultList");
		ep.setMap(map);
		// 导出数据列数组
		ep.setArray(couPonArray);
		// 导出数据列头国际化资源文件
		ep.setBaseName("BINOLCPACT06");
		ep.setShowTitleStyle(true);
		return binOLMOCOM01_BL.getBatchExportExcel(ep);
	}
}
