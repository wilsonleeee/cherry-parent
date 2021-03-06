/*
 * @(#)BINBEIFPRO05_BL.java     1.0 2016/7/4
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
package com.cherry.ia.pro.bl;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import com.cherry.cm.activemq.dto.MQInfoDTO;
import com.cherry.cm.activemq.interfaces.BINOLMQCOM01_IF;
import com.cherry.cm.batcmbussiness.interfaces.BINBECM01_IF;
import com.cherry.cm.cmbussiness.bl.BINOLCM03_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM15_BL;
import com.cherry.cm.core.BatchExceptionDTO;
import com.cherry.cm.core.BatchLoggerDTO;
import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryBatchException;
import com.cherry.cm.core.CherryBatchLogger;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.CherryBatchUtil;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.DateUtil;
import com.cherry.ia.common.ProductConstants;
import com.cherry.ia.pro.service.BINBEIFPRO05_Service;
import com.cherry.mq.mes.common.MessageConstants;
import com.googlecode.jsonplugin.JSONException;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 *特价产品下发下发BL
 * 
 * @author chenkuan
 * @version 1.0 2016/07/04
 */
public class BINBEIFPRO05_BL {
	/**
	 * 打印当前类的日志信息
	 */
	private static Logger loger = LoggerFactory.getLogger(BINBEIFPRO04_BL.class.getName());
	@Resource
	private BINBEIFPRO05_Service binbeifpro05Service;
	
	/** 各类编号取号共通BL */
	@Resource(name="binOLCM15_BL")
	private BINOLCM15_BL binOLCM15_BL;
	
	@Resource(name="binOLCM03_BL")
	private BINOLCM03_BL binOLCM03_BL;
	
	@Resource(name="binOLMQCOM01_BL")
    private BINOLMQCOM01_IF binOLMQCOM01_BL;
	
	/** 系统配置项 共通BL */
	@Resource
	private BINOLCM14_BL binOLCM14_BL;

	/** JOB执行相关共通 IF */
	@Resource(name="binbecm01_IF")
	private BINBECM01_IF binbecm01_IF;

	/** BATCH处理标志 */
	private int flag = CherryBatchConstants.BATCH_SUCCESS;

	/** 处理总条数 */
	private int totalCount = 0;
	/** 插入条数 */
	private int insertCount = 0;
	/** 更新条数 */
	private int updateCount = 0;
	/** 失败条数 */
	private int failCount = 0;

	/**
	 * 特价产品列表的batch处理
	 * 
	 * @param map
	 * 
	 * @return Map
	 * @throws Exception 
	 * @throws JSONException 
	 */
	public int tran_batchCouProducts(Map<String, Object> map)
			throws JSONException, Exception {
		// 初始化
		init(map);
		try{
			// 更新特价产品方案中销售日期在业务日期前后1天内的产品的版本号
			binbeifpro05Service.updSoluDetailVerByPrtSellDate(map);
			// 更新接口数据库
			boolean needSendMqOrNot = updIFDatabase(map);
			if(needSendMqOrNot){
				// 产品表的表版本号在下发成功后+1
				Map<String, Object> seqMap = new HashMap<String, Object>();
				seqMap.putAll(map);
				seqMap.put("type", "T");
				String newTVersion = binOLCM15_BL.getNoPadLeftSequenceId(seqMap);
				map.put("newTVersion", newTVersion);
				// 发送MQ
				// Step5: 调用MQHelper接口进行数据发送
				Map<String,Object> MQMap = getPrtNoticeMqMap(map,"DSO");//柜台特价产品信息
				if(MQMap.isEmpty()){
					loger.error("特价产品实时下发通知组装异常");
					throw new Exception("特价产品实时下发通知组装异常");
				}
				//设定MQInfoDTO
				MQInfoDTO mqDTO = setMQInfoDTO(MQMap,map);
				mqDTO.setMsgQueueName("cherryToPosCMD");
				//调用共通发送MQ消息
				binOLMQCOM01_BL.sendMQMsg(mqDTO,false);
			}
		}catch(Exception e){
			loger.error("特价产品下发失败",e);
			throw e;
		}
		// 输出处理结果信息
		outMessage();
		//插入job运行履历表,应该放到Action中才可以
		tran_programEnd(map);
		return flag;
	}

	/**
	 * 更新接口数据库
	 * 
	 * @param map
	 */
	private boolean updIFDatabase(Map<String, Object> map) throws Exception{
		// Step1.1  取得新后台特价产品方案柜台关联数据版本号version大于tVersion的list(新增/修改/停用/启用等)
		//为什么是大于tversion?产品特价方案部门 这里的version并没有进行更新
		List<Map<String, Object>> prtSoluCouList = binbeifpro05Service.getPrtSoluCouList(map);
		boolean needSendMqOrNot = false;
		if (!CherryBatchUtil.isBlankList(prtSoluCouList)) {
			totalCount += prtSoluCouList.size();
			needSendMqOrNot = true;
			for (Map<String, Object> prtSoluCouItemMap : prtSoluCouList) {
				prtSoluCouItemMap.putAll(map);
				// 设置特价产品方案柜台接口表的状态值
				getPrtSoluCntSCSStatus(prtSoluCouItemMap);
			}
			try {
				// 删除特价产品方案柜台接口表(根据brand、counter)
				binbeifpro05Service.delIFPrtSoluWithCounter(prtSoluCouList);
				// 插入特价产品方案柜台接口表
				binbeifpro05Service.addIFPrtSoluWithCounter(prtSoluCouList);
				// 插入件数加
				insertCount += prtSoluCouList.size();
			} catch (Exception e) {
				failCount += prtSoluCouList.size();
				loger.error("插入产品方案柜台关系接口表时发生异常。");
				throw e;
			}
		}
		// Step1.2 取得新后台特价产品方案明细表数据版本号version大于tVersion的list(新增/修改/停用/启用等)
		List<Map<String, Object>> prtSoluDetailByVersionList = binbeifpro05Service.getPrtSoluDetailByVersionList(map);
		// 保存接口特价产品方案柜台关系表
		if (!CherryBatchUtil.isBlankList(prtSoluDetailByVersionList)) {
			needSendMqOrNot = true;
			for (Map<String, Object> prtSoluDetailItemMap : prtSoluDetailByVersionList) {
					prtSoluDetailItemMap.putAll(map);
					// 设置特价产品方案明细接口表的状态值
					getPrtSoluSCSStatus(prtSoluDetailItemMap);
				}
			try{
					// 删除特价产品方案明细接口表(根据brand、SpecialOfferSoluCode、产品厂商ID)
					binbeifpro05Service.delIFPrtSoluSCS(prtSoluDetailByVersionList);
					// 插入产品方案明细接口表
					binbeifpro05Service.addIFPrtSoluSCS(prtSoluDetailByVersionList);
				} catch(Exception e){
					//插入产品方案接口表时发生异常。
					loger.error("插入产品方案明细接口表时发生异常");
					throw e;
				}
		}
		return needSendMqOrNot;
	}

	/**
	 * 设置特价产品方案柜台接口表的状态值
	 * @param productMap
	 */
	private void getPrtSoluCntSCSStatus(Map<String, Object> productMap){
		String prtSoluWithCnt_status = "1"; // PrtSoluCntSCS 状态值,
		String cntValidFlag = ConvertUtil.getString(productMap.get("cntValidFlag"));
		String pdValidFlag = ConvertUtil.getString(productMap.get("psdValidFlag")); // 产品方案柜台
		String ppsValidFlag = ConvertUtil.getString(productMap.get("ppsValidFlag")); // 产品方案有效区分
		if (CherryConstants.VALIDFLAG_DISABLE.equals(cntValidFlag)
				|| CherryConstants.VALIDFLAG_DISABLE.equals(pdValidFlag)
				|| CherryConstants.VALIDFLAG_DISABLE.equals(ppsValidFlag)) {
			// 设置无效无效
			prtSoluWithCnt_status = "0";
		}else{
			// 产品有效
			prtSoluWithCnt_status = "1";
		}
		productMap.put("prtSoluWithCnt_status", prtSoluWithCnt_status);
	}
	
	/**
	 * 设置特价产品方案明细接口表的状态值
	 * 处理逻辑 （实时下发接口状态值设值(0：正常销售、1：停用、2：下柜、3：未启用*)）
	 1、先判断产品表及产品方案明细表柜台表的validFlag数值，如果 有一个是停用那么直接使用停用
	 2、如果validFlag=1,则判断是否下柜台，如果下柜台则使用下柜，如果未下柜，则判断是否在销售区间内，如果不在，则使用未启用

	 终端对WITPOSA_product_with_counter status定义：
	 E	表示	一个BARCODE对应多个UNITCODE时[product_scs往品牌的product导入时]，全部生效
	 D	表示	产品停用 	其他系统下发的数据，只有停用，无下柜和未启用状态
	 新增加下面两种状态：
	 H	表示	产品下柜	下发到witpos_xx数据库后都归为停用
	 G	表示	产品未启用 	下发到witpos_xx数据库后都归为停用

	 新后台对WITPOSA_product_with_counter status定义：
	 E：正常销售、D：停用、H：下柜、G：未启用
	 * @param productMap
	 */
	private void getPrtSoluSCSStatus(Map<String, Object> productMap){
		String prtSoluDetail_status = "E"; // Product_SCS 状态值,
		String prtVendorValidFlag = ConvertUtil.getString(productMap.get("prtVendorValidFlag"));
		String soluPrtValidFlag = ConvertUtil.getString(productMap.get("soluPrtValidFlag"));
		String prtStatus = ConvertUtil.getString(productMap.get("prtStatus")); // 新后台产品状态 D：表明下柜产品； E：表明产品生效，可销售可订货；
		String sellDateFlag = ConvertUtil.getString(productMap.get("prtSellDateFlag")); // 是否不在销售日期区间  0:未过期 1:已过期
		
		if (CherryConstants.VALIDFLAG_DISABLE.equals(prtVendorValidFlag)
				|| CherryConstants.VALIDFLAG_DISABLE.equals(soluPrtValidFlag)) {
			// 设置无效无效
			prtSoluDetail_status = "D";
		}else{
			// 产品有效
			if(ProductConstants.PRODUCT_STATUS_D.equalsIgnoreCase(prtStatus)){
				// 产品下柜
				prtSoluDetail_status = "H";
			}else {
				//
				if(ProductConstants.SellDateFlag_1.equals(sellDateFlag)){
					// 产品未启用
					prtSoluDetail_status = "G";
				}else{
					// 有效，正常销售
					prtSoluDetail_status = "E";
				}
			}
		}
		prtSoluDetail_status=prtSoluDetail_status.equals("E")?"1":"0";//以这里的最新状态为准
		productMap.put("prtSoluDetail_status", prtSoluDetail_status);
	}
	
	/**
	 * 组装产品下发通知的MQ消息
	 * @param map
	 * @param subType 子类型：PRT、DPRT、PRM、DSO 必填，用于区分该消息体发送的是 产品信息,柜台产品信息,标准促销品信息,柜台特价产品信息
	 * @return
	 * @throws Exception
	 */
	public Map<String,Object> getPrtNoticeMqMap(Map<String,Object> map,String subType) throws Exception{
		//申明要返回的map
		Map<String,Object> prtNoticeMqMap = new HashMap<String,Object>();
		//组装消息体版本	Version
		prtNoticeMqMap.put(MessageConstants.MESSAGE_VERSION_TITLE, "AMQ.014.001");
		//组装消息体数据类型	DataType
		prtNoticeMqMap.put(MessageConstants.MESSAGE_DATATYPE_TITLE, MessageConstants.DATATYPE_APPLICATION_JSON);
		
		Map<String,Object> dataLine = new HashMap<String,Object>();
		//组装消息体主数据	MainData
		Map<String,Object> mainData = new HashMap<String,Object>();
		//品牌代码
		String brandCode = ConvertUtil.getString(map.get(CherryBatchConstants.BRAND_CODE));
		mainData.put("BrandCode", brandCode);
		
		//单据号
		String tradeNoIf = binOLCM03_BL.getTicketNumber(String.valueOf(map.get(CherryConstants.ORGANIZATIONINFOID)), String.valueOf(map.get(CherryConstants.BRANDINFOID)), String.valueOf(map.get("loginName")), MessageConstants.MSG_PR);
		//如果单据号为空抛自定义异常：组装MQ消息体时失败，单据号取号失败！
		if(tradeNoIf==null||tradeNoIf.isEmpty()){
			throw new CherryException("EBS00069");
		}
		mainData.put("TradeNoIF", ConvertUtil.getString(tradeNoIf));
		//业务类型
		mainData.put("TradeType", MessageConstants.MSG_PR);
		//子类型
		mainData.put("SubType", ConvertUtil.getString(subType));
		// 表版本号
		mainData.put("TVersion", ConvertUtil.getString(map.get("newTVersion")));
		
		//一直向前增长的系统时间
		mainData.put("Time", binbeifpro05Service.getForwardSYSDate());
		//操作者
		mainData.put("EmployeeId", ConvertUtil.getString(map.get("employeeId")));
		//将主数据放入dataLine中
		dataLine.put(MessageConstants.MAINDATA_MESSAGE_SIGN, mainData);
		
		prtNoticeMqMap.put(MessageConstants.DATALINE_JSON_XML, dataLine);
		
		return prtNoticeMqMap;
	}
	
	/**
	 * 设定MQInfoDTO
	 * 
	 * 
	 * */
	@SuppressWarnings("unchecked")
	public MQInfoDTO setMQInfoDTO(Map<String,Object> MQMap,Map<String,Object> paramMap){
		
		Map<String,Object> mainData = (Map<String, Object>) ((Map<String, Object>) MQMap.get(MessageConstants.DATALINE_JSON_XML)).get(MessageConstants.MAINDATA_MESSAGE_SIGN);
		
		MQInfoDTO mqDTO = new MQInfoDTO();
		//数据源
		mqDTO.setSource(CherryConstants.MQ_SOURCE_CHERRY);
		//消息方向
		mqDTO.setSendOrRece(CherryConstants.MQ_SENDORRECE_S);
		//组织ID
		mqDTO.setOrganizationInfoId(ConvertUtil.getInt(paramMap.get(CherryConstants.ORGANIZATIONINFOID)));
		//所属品牌
		mqDTO.setBrandInfoId(ConvertUtil.getInt(paramMap.get(CherryConstants.BRANDINFOID)));
		//单据类型
		mqDTO.setBillType(ConvertUtil.getString(mainData.get("TradeType")));
		//单据号
		mqDTO.setBillCode(ConvertUtil.getString(mainData.get("TradeNoIF")));
		//队列名
		mqDTO.setMsgQueueName(CherryConstants.CHERRYTOPOSMSGQUEUE);
		//消息体数据（未封装）
		mqDTO.setMsgDataMap(MQMap);
		//作成者
		mqDTO.setCreatedBy(ConvertUtil.getString(paramMap.get(CherryConstants.CREATEDBY)));
		//做成模块
		mqDTO.setCreatePGM(ConvertUtil.getString(paramMap.get(CherryConstants.CREATEPGM)));
		//更新者
		mqDTO.setUpdatedBy(ConvertUtil.getString(paramMap.get(CherryConstants.UPDATEDBY)));
		//更新模块
		mqDTO.setUpdatePGM(ConvertUtil.getString(paramMap.get(CherryConstants.UPDATEPGM)));
		
		//业务流水
		DBObject dbObject = new BasicDBObject();
		//组织代码
		dbObject.put("OrgCode", ConvertUtil.getString(paramMap.get("orgCode")));
		// 品牌代码，即品牌简称
		dbObject.put("BrandCode", ConvertUtil.getString(mainData.get("BrandCode")));
		// 业务类型
		dbObject.put("TradeType", ConvertUtil.getString(mqDTO.getBillType()));
		// 单据号
		dbObject.put("TradeNoIF", ConvertUtil.getString(mqDTO.getBillCode()));
		// 修改次数
		dbObject.put("ModifyCounts", CherryConstants.DEFAULT_MODIFYCOUNTS);
		//MQ队列名
		dbObject.put("MsgQueueName", ConvertUtil.getString(mqDTO.getMsgQueueName()));
		 // 业务流水
		mqDTO.setDbObject(dbObject);
		
		return mqDTO;
	}

	/**
	 * 输出处理结果信息
	 * 
	 * @throws CherryBatchException
	 */
	private void outMessage() throws CherryBatchException {
		int successCount = totalCount - failCount;
		//打印出总条数，
		loger.info("特价产品下发一共处理的条数为:" + totalCount);
		//成功条数
		loger.info("特价产品下发处理成功的条数为:" + successCount);
		//插入条数
		loger.info("特价产品下发处理成功的条数为:" + insertCount);
		//失败的条数
		loger.info("特价产品下发处理失败的条数为:" + failCount);
		String msg = "===========================特价产品下发";
		String endMsg = "结束===================================";
		switch (flag) {
			case CherryBatchConstants.BATCH_SUCCESS:
				loger.info(msg + "正常" + endMsg);
				break;
			case CherryBatchConstants.BATCH_WARNING:
				loger.warn(msg + "警告" + endMsg);
				break;
			case CherryBatchConstants.BATCH_ERROR:
				loger.error(msg + "失败" + endMsg);
			default:
				loger.info(msg + endMsg);
		}
	}

	/**
	 * 程序结束时，处理Job共通( 插入Job运行履历表)
	 * @param paraMap
	 * @throws Exception
	 */
	public void tran_programEnd(Map<String,Object> paraMap) throws Exception{
		// 程序结束时，插入Job运行履历表
		paraMap.put("flag", flag);
		paraMap.put("TargetDataCNT", totalCount);
		paraMap.put("SCNT", totalCount - failCount);
		paraMap.put("FCNT", failCount);
		paraMap.put("UCNT", updateCount);
		paraMap.put("ICNT", insertCount);
		paraMap.remove("validFlagVal");
		binbecm01_IF.insertJobRunHistory(paraMap);
	}

	private void init(Map<String, Object> map) {
		// 业务日期，日结标志
		Map<String, Object> bussDateMap = binbeifpro05Service.getBussinessDateMap(map);
		// 业务日期
		String businessDate = CherryBatchUtil.getString(bussDateMap.get(CherryBatchConstants.BUSINESS_DATE));
		map.put("businessDate", businessDate);
		// 日结标志
		String closeFlag = CherryBatchUtil.getString(bussDateMap.get("closeFlag"));
		// 业务日期+1
		String nextBussDate = DateUtil.addDateByDays(CherryBatchConstants.DATE_PATTERN, businessDate, 1);
		// 停用日时
		String closingTime = DateUtil.suffixDate(businessDate, 1);
		// 启用日时
		String startTime = DateUtil.suffixDate(nextBussDate, 0);
		// 当天业务结束，下发业务日期下一天的价格，否则下发当天价格
		if("1".equals(closeFlag)){
			map.put("priceDate", nextBussDate);
		}else{
			map.put("priceDate", businessDate);
		}
		// BatchCD 来自VSS$/01.Cherry/02.设计文档/01.概要设计/00.各种一览/【新设】CherryBatch一览.xlsx
		map.put("JobCode", "BAT151");
		// 程序【开始运行时间】
		String sysdateTime = CherryUtil.getSysDateTime(CherryConstants.DATE_PATTERN_24_HOURS);
		map.put("RunStartTime", sysdateTime);
		//comMap
		Map<String,Object> comMap= getComMap(map);
		// 启用日时
		comMap.put(ProductConstants.STARTTIME, startTime);
		// 停用日时
		comMap.put(ProductConstants.CLOSINGTIME, closingTime);
		// 取得当前产品表版本号
		Map<String, Object> seqMap = new HashMap<String, Object>();
		seqMap.putAll(map);
		seqMap.put("type", "T");
		String tVersion = binOLCM15_BL.getCurrentSequenceId(seqMap);
		map.put("tVersion", tVersion);
		map.putAll(comMap);
	}

	private Map<String, Object> getComMap(Map<String, Object> map) {
		Map<String, Object> baseMap = new HashMap<String, Object>();
		// 品牌Code
		String branCode = binbeifpro05Service.getBrandCode(map);
		// 系统时间
		String sysTime = binbeifpro05Service.getSYSDate();

		// 品牌Code
		baseMap.put(ProductConstants.BRANDCODE, branCode);
		// 作成时间
		baseMap.put(CherryBatchConstants.CREATE_TIME, sysTime);
		// 更新时间
		baseMap.put(CherryBatchConstants.UPDATE_TIME, sysTime);
		// 更新程序名
		baseMap.put(CherryBatchConstants.UPDATEPGM, "BINBEIFPRO05");
		// 作成程序名
		baseMap.put(CherryBatchConstants.CREATEPGM, "BINBEIFPRO05");
		// 作成者
		baseMap.put(CherryBatchConstants.CREATEDBY,
				CherryBatchConstants.UPDATE_NAME);
		// 更新者
		baseMap.put(CherryBatchConstants.UPDATEDBY,
				CherryBatchConstants.UPDATE_NAME);
		map.putAll(baseMap);
		return baseMap;
	}
}
