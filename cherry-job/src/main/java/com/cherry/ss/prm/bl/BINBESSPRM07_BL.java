/*
 * @(#)BINBESSPRM03_BL.java     1.0 2010/12/20
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.activemq.dto.MQInfoDTO;
import com.cherry.cm.activemq.interfaces.BINOLMQCOM01_IF;
import com.cherry.cm.cmbussiness.bl.BINOLCM03_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM15_BL;
import com.cherry.cm.core.BatchExceptionDTO;
import com.cherry.cm.core.BatchLoggerDTO;
import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryBatchException;
import com.cherry.cm.core.CherryBatchLogger;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.CherryBatchUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.DateUtil;
import com.cherry.ia.common.ProductConstants;
import com.cherry.mq.mes.common.MessageConstants;
import com.cherry.ss.prm.service.BINBESSPRM07_Service;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

/**
 * 
 *促销品下发(实时)BL
 * 
 * 
 * @author hub
 * @version 1.0 2010.12.20
 */
public class BINBESSPRM07_BL {

	@Resource
	private BINBESSPRM07_Service binbessprm07Service;
	
	/** BATCH LOGGER */
	private static CherryBatchLogger logger = new CherryBatchLogger(
			BINBESSPRM07_BL.class);

	/** BATCH处理标志 */
	private int flag = CherryBatchConstants.BATCH_SUCCESS;
	
	@Resource(name="binOLCM15_BL")
	private BINOLCM15_BL binOLCM15_BL;
	
	@Resource(name="binOLMQCOM01_BL")
    private BINOLMQCOM01_IF binOLMQCOM01_BL;
	
	@Resource(name="binOLCM03_BL")
	private BINOLCM03_BL binOLCM03_BL;
	
	@Resource(name="binOLCM14_BL")
	private BINOLCM14_BL binOLCM14_BL;

	/** 处理总条数 */
	private int totalCount = 0;

	/** 失败条数 */
	private int failCount = 0;
	
	/** 套装折扣unitCode,barCode数值 ：TZZK999999 */
	private final String TZZK_CODE = "TZZK999999";
	
	/** 虚拟促销品unitCode,barCode数值 ：CXLP999999 */
//	private final String CXLP_CODE = "CXLP999999";

	/**
	 * 促销品下发的batch处理
	 * 
	 * @param map
	 * 
	 * @return int
	 * @throws CherryBatchException
	 * 
	 */
	public Map<String,Object> tran_batchPromPrt(Map<String, Object> map) throws CherryBatchException,Exception {
		
		Map<String,Object> resultMap = new HashMap<String, Object>();
		
		String errorMsg = null;
		
		// 取得促销品当前表版本号
		Map<String, Object> seqMap = new HashMap<String, Object>();
		seqMap.putAll(map);
		seqMap.put("type", "H");
		String tVersion = binOLCM15_BL.getCurrentSequenceId(seqMap);
		map.put("tVersion", tVersion);
		
		// 业务日期
//		String closeDate = binbessprm07Service.getBussinessDate(map);
		String bussDate = binbessprm07Service.getBusDate(map);
		map.put("bussDate", bussDate);
		
		// 更新销售日期在业务日期前后1天内的促销产品的版本号
		binbessprm07Service.updVerByPromSellDate(map);
		
		// 处理虚拟促销品
		virtualPrm(map);
		
		String sysdateTime = binbessprm07Service.getSYSDateTime();
		String sysHHSSMM = DateUtil.getSpecificDate(sysdateTime,DateUtil.TIME_PATTERN );
		
		// 停用日时
//		String closingTime = DateUtil.suffixDate(closeDate, 1);
		String closingTime = bussDate + " " + sysHHSSMM;
		
		// 启用日时
//		String startTime = DateUtil.suffixDate(bussDate, 0);
		String startTime = bussDate + " " + sysHHSSMM;
		Map<String, Object> baseMap = new HashMap<String, Object>();
		// 作成者
		baseMap.put(CherryBatchConstants.CREATEDBY, CherryBatchConstants.UPDATE_NAME);
		// 作成程序名
		baseMap.put(CherryBatchConstants.CREATEPGM, "BINBESSPRM07");
		// 更新者
		baseMap.put(CherryBatchConstants.UPDATEDBY, CherryBatchConstants.UPDATE_NAME);
		// 更新程序名
		baseMap.put(CherryBatchConstants.UPDATEPGM, "BINBESSPRM07");
		// 有效区分
		baseMap.put("validFlagVal", "0");
		baseMap.put("startTime", startTime);
		baseMap.put("closingTime", closingTime);
		baseMap.put("tVersion", tVersion);
		// 新产品生效日期
		
//		String enable_time = DateUtil.suffixDate(closeDate, 1);
		String enable_time =  bussDate + " " + sysHHSSMM;
		baseMap.put("enable_time", enable_time);

		baseMap.put(CherryConstants.ORGANIZATIONINFOID, map.get(CherryConstants.ORGANIZATIONINFOID));
		baseMap.put(CherryConstants.BRANDINFOID, map.get(CherryConstants.BRANDINFOID));
		
		try {
			// 查询促销品信息
			List<Map<String, Object>> promPrtList = binbessprm07Service.getPromPrtList(map);
			if (!CherryBatchUtil.isBlankList(promPrtList)) {
				// 促销品下发主处理
				exportPromPrt(promPrtList, baseMap);
			
				Object sendMQFlag = map.get("sendMQFlag");
				if(null != sendMQFlag){
					try{
						if(failCount == 0){
							// Step2: 发送MQ通知
							// 产品表的表版本号在下发成功后+1
							String newTVersion = binOLCM15_BL.getNoPadLeftSequenceId(seqMap);
							map.put("newTVersion", newTVersion);
							
							Map<String,Object> MQMap = getPrtNoticeMqMap(map, MessageConstants.MSG_SPRT_PRM);
							if(MQMap.isEmpty()){
								throw new Exception("促销产品实时下发通知组装异常");
							}
							
							//设定MQInfoDTO
							MQInfoDTO mqDTO = setMQInfoDTO(MQMap,map);
							//调用共通发送MQ消息
							mqDTO.setMsgQueueName(CherryConstants.cherryToPosCMD);
							binOLMQCOM01_BL.sendMQMsg(mqDTO,false);
						}
					} catch (Exception e) {
						logger.outExceptionLog(e);
						flag = CherryBatchConstants.BATCH_ERROR;
						resultMap.put("errorMsg", e.getMessage());
						try{
							binbessprm07Service.manualRollback();
							binbessprm07Service.ifManualRollback();
						}catch(Exception ex){
							resultMap.put("errorMsg", ex.getMessage());
							logger.outExceptionLog(ex);
						}
					}
				}else {
					if(failCount == 0){
						// 产品表的表版本号在下发成功后+1
						String newTVersion = binOLCM15_BL.getNoPadLeftSequenceId(seqMap);
						map.put("newTVersion", newTVersion);
					}
				}
			}
		}catch(Exception e){
			logger.outExceptionLog(e);
			flag = CherryBatchConstants.BATCH_ERROR;
			resultMap.put("errorMsg", e.getMessage());
			try{
				binbessprm07Service.manualRollback();
				binbessprm07Service.ifManualRollback();
			}catch(Exception ex){
				logger.outExceptionLog(ex);
				resultMap.put("errorMsg", e.getMessage());
			}
		}
		
		// 总件数
		BatchLoggerDTO batchLoggerDTO1 = new BatchLoggerDTO();
		batchLoggerDTO1.setCode("IIF00001");
		batchLoggerDTO1.setLevel(CherryBatchConstants.LOGGER_INFO);
		batchLoggerDTO1.addParam(String.valueOf(totalCount));
		// 成功总件数
		BatchLoggerDTO batchLoggerDTO2 = new BatchLoggerDTO();
		batchLoggerDTO2.setCode("IIF00002");
		batchLoggerDTO2.setLevel(CherryBatchConstants.LOGGER_INFO);
		batchLoggerDTO2.addParam(String.valueOf(totalCount - failCount));
		// 失败件数
		BatchLoggerDTO batchLoggerDTO5 = new BatchLoggerDTO();
		batchLoggerDTO5.setCode("IIF00005");
		batchLoggerDTO5.setLevel(CherryBatchConstants.LOGGER_INFO);
		batchLoggerDTO5.addParam(String.valueOf(failCount));
		CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this
				.getClass());
		// 处理总件数
		cherryBatchLogger.BatchLogger(batchLoggerDTO1);
		// 成功总件数
		cherryBatchLogger.BatchLogger(batchLoggerDTO2);
		// 失败件数
		cherryBatchLogger.BatchLogger(batchLoggerDTO5);
		
		resultMap.put("totalCount", totalCount);
		resultMap.put("flag", flag);
		resultMap.put("errorMsg", errorMsg);
		return resultMap;
	}
	
	/**
	* 设置促销产品接口表的状态值
	* @param map
	*/
	private void getPromScsStatus(Map<String, Object> promMap){

	   /*
		处理逻辑 （实时下发接口状态值设值(0：正常销售、1：停用、2：下柜、3：未启用*)）
			   1、先判断validFlag数值 如果停用直接使用停用
			   2、如果validFlag=1,则判断是否下柜台，如果下柜台则使用下柜，如果未下柜，则判断是否在销售区间内，如果不在，则使用未启用

	   终端对product_SCS status定义：
	   E       表示  一个BARCODE对应多个UNITCODE时[product_scs往品牌的product导入时]，全部生效    
	   D       表示  产品停用    其他系统下发的数据，只有停用，无下柜和未启用状态        
	   新增加下面 状态：  
	   G       表示  产品未启用         下发到witpos_xx数据库后都归为停用
	   
	   新后台对PromotionTable_SCS status定义：
	   E：正常销售、D：停用、H：下柜、G：未启用
	   */
	   
	   String promscs_status = "E"; // PromotionTable_SCS 状态值,
	   
	   String ValidFlag = CherryBatchUtil.getString(promMap.get("ValidFlag"));
	   String sellDateFlag = CherryBatchUtil.getString(promMap.get("SellDateFlag")); // 是否不在销售日期区间  0:未过期 1:已过期
	   
	   if(CherryBatchConstants.VALIDFLAG_DISABLE.equals(ValidFlag)){
			   // 产品无效
			   promscs_status = "D";
	   }else{
		   // 产品有效
		   
		   //
		   if(ProductConstants.SellDateFlag_1.equals(sellDateFlag)){
				   // 产品未启用
				   promscs_status = "G";
		   }else{
				   // 有效，正常销售
				   promscs_status = "E";
		   }
	   }
	   
	   promMap.put("status", promscs_status);
	}
	
	/**
	 * 组装产品下发通知的MQ消息
	 * @param map
	 * @param subType 子类型：PRT、DPRT	 必填，用于区分该消息体发送的是产品信息还是柜台产品信息
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
			BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO();
			batchExceptionDTO.setBatchName(this.getClass());
			batchExceptionDTO.setErrorCode("EMQ00038");
			batchExceptionDTO.setErrorLevel(CherryBatchConstants.BATCH_ERROR);
			throw new CherryBatchException(batchExceptionDTO);
		}
		mainData.put("TradeNoIF", tradeNoIf);
		//业务类型
		mainData.put("TradeType", MessageConstants.MSG_PR);
		//子类型
		mainData.put("SubType", subType);
		// 表版本号
		mainData.put("TVersion", map.get("newTVersion"));
		
		//一直向前增长的系统时间
		mainData.put("Time", binbessprm07Service.getForwardSYSDate());
		
		//操作者
		mainData.put("EmployeeId", map.get("EmployeeId"));
		
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
		mqDTO.setBillType((String)mainData.get("TradeType"));
		//单据号
		mqDTO.setBillCode((String)mainData.get("TradeNoIF"));
		//队列名
		mqDTO.setMsgQueueName(CherryConstants.CHERRYTOPOSMSGQUEUE);
		//消息体数据（未封装）
		mqDTO.setMsgDataMap(MQMap);
		//作成者
		mqDTO.setCreatedBy(String.valueOf(paramMap.get(CherryConstants.CREATEDBY)));
		//做成模块
		mqDTO.setCreatePGM(String.valueOf(paramMap.get(CherryConstants.CREATEPGM)));
		//更新者
		mqDTO.setUpdatedBy(String.valueOf(paramMap.get(CherryConstants.UPDATEDBY)));
		//更新模块
		mqDTO.setUpdatePGM(String.valueOf(paramMap.get(CherryConstants.UPDATEPGM)));
		
		//业务流水
		DBObject dbObject = new BasicDBObject();
		//组织代码
		dbObject.put("OrgCode", paramMap.get("orgCode"));
		// 品牌代码，即品牌简称
		dbObject.put("BrandCode", mainData.get("BrandCode"));
		// 业务类型
		dbObject.put("TradeType", mqDTO.getBillType());
		// 单据号
		dbObject.put("TradeNoIF", mqDTO.getBillCode());
		// 修改次数
		dbObject.put("ModifyCounts", CherryConstants.DEFAULT_MODIFYCOUNTS);
		//MQ队列名
		dbObject.put("MsgQueueName", mqDTO.getMsgQueueName());
		 // 业务流水
		mqDTO.setDbObject(dbObject);
		
		return mqDTO;
	}
	
	
	/**
	 * 处理品牌在没有虚拟促销品的情况下，新增虚拟促销品
	 * 'TZZK999999','CXLP999999'
	 * 2013/08/27  虚拟促销品（CXLP999999）添加功能废除
	 * @param map
	 */
	private void virtualPrm(Map<String, Object> map){
		
		// 取得"套装折扣"及"虚拟促销品"的数据
		List<String> virtualPrmUnitCodeList = binbessprm07Service.getVirtualPrmList(map);
		
		// 若促销产品表中没有"套装折扣"及"虚拟促销品"的的数据则新增数据
		if(!virtualPrmUnitCodeList.contains(TZZK_CODE)){
			// 插入TZZK999999
			Map<String, Object> tzzkMap = new HashMap<String,Object>();
			tzzkMap.put(CherryBatchConstants.UNITCODE, TZZK_CODE);
			tzzkMap.put(CherryBatchConstants.BARCODE, TZZK_CODE);
			tzzkMap.put("nameTotal", "套装折扣");
			tzzkMap.put("promCate", "TZZK");
			tzzkMap.put("tVersion", map.get("tVersion"));
			insertPrm(tzzkMap,map);
		}
		
		/* 
		if(!virtualPrmUnitCodeList.contains(CXLP_CODE)){
			// 插入CXLP999999
			Map<String, Object> cxlpMap = new HashMap<String,Object>();
			cxlpMap.put(CherryBatchConstants.UNITCODE, CXLP_CODE);
			cxlpMap.put(CherryBatchConstants.BARCODE, CXLP_CODE);
			cxlpMap.put("nameTotal", "虚拟促销品");
			cxlpMap.put("promCate", "CXLP");
			insertPrm(cxlpMap,map);
		}
		*/
		
	}
	
	/**
	 * 新增促销品
	 * @param virtualMap
	 * @param paramMap
	 */
	private void insertPrm(Map<String, Object> virtualMap, Map<String, Object> paramMap){
		Map<String, Object> prmMap = new HashMap<String,Object>();
		prmMap.putAll(virtualMap);
		
		// 作成者
		prmMap.put(CherryBatchConstants.CREATEDBY, CherryBatchConstants.UPDATE_NAME);
		// 作成程序名
		prmMap.put(CherryBatchConstants.CREATEPGM, "BINBESSPRM07");
		// 更新者
		prmMap.put(CherryBatchConstants.UPDATEDBY, CherryBatchConstants.UPDATE_NAME);
		// 更新程序名
		prmMap.put(CherryBatchConstants.UPDATEPGM, "BINBESSPRM07");
		
		prmMap.put(CherryBatchConstants.ORGANIZATIONINFOID, paramMap.get("organizationInfoId"));
		prmMap.put(CherryBatchConstants.BRANDINFOID, paramMap.get("brandInfoId"));
		prmMap.put("isStock", "0"); // 是否管理库存
		
		int prmId = binbessprm07Service.insertPromotionProductBackId(prmMap);
		
		prmMap.put("promProductId", prmId);  // 促销品ID
		prmMap.put("manuFactId", 1); // 生产厂商ID
		
		binbessprm07Service.insertPromProductVendor(prmMap);
	}

	
	/**
	 * 促销品下发主处理
	 * 
	 * @param promPrtList 
	 * 			促销品List
	 * @param map 
	 * 			共通参数
	 * @return 
	 * @throws CherryBatchException
	 * 
	 */
	public void exportPromPrt(List<Map<String, Object>> promPrtList, 
			Map<String, Object> baseMap) throws CherryBatchException,Exception {
		totalCount += promPrtList.size();
		Map<String, Object> insertMap = new HashMap<String, Object>();		
		
		// 活动价小数处理配置项
		String priceFlag = binOLCM14_BL.getConfigValue("1285",String.valueOf(baseMap.get(CherryConstants.ORGANIZATIONINFOID)), String.valueOf(baseMap.get(CherryConstants.BRANDINFOID)));
		
		for (Map<String, Object> promPrt : promPrtList) {
			// 促销产品类别
			String promotionCateCd = (String) promPrt.get("promotionCateCd");
			insertMap.put("promotionCateCd", promotionCateCd);
			// 促销产品大类名称
			insertMap.put("promPrtBClassName", CherryBatchConstants.PROMOTION_CODE_NAME.getName(promotionCateCd));
			// 促销产品大类代码
			insertMap.put("promPrtBClassCode", CherryBatchConstants.PROMOTION_CODE_NAME.getCode(promotionCateCd));
			// 促销产品中类名称
			insertMap.put("promPrtMClassName", CherryBatchConstants.PROMOTION_CODE_NAME.getName(promotionCateCd));
			// 促销产品中类代码
			insertMap.put("promPrtMClassCode", CherryBatchConstants.PROMOTION_CODE_NAME.getCode(promotionCateCd));
			// 兑换所需积分
			Object exPoint = promPrt.get("exPoint");
			if (CherryBatchConstants.PROMOTION_DHCP_TYPE_CODE.equals(promotionCateCd)
					|| CherryBatchConstants.PROMOTION_DHMY_TYPE_CODE.equals(promotionCateCd)) {
				// 兑换所需积分
				if(null == exPoint) {
					exPoint = new BigDecimal("0"); 
				}
				// 兑换所需积分
				insertMap.put("promPrtLClassName", exPoint);
			} else {
				// 促销产品小类名称
				insertMap.put("promPrtLClassName", CherryBatchConstants.PROMOTION_CODE_NAME.getName(promotionCateCd));
			}
			// 兑换所需积分
			insertMap.put("exPoint", exPoint);
			// 促销产品小类代码
			insertMap.put("promPrtLClassCode", CherryBatchConstants.PROMOTION_CODE_NAME.getCode(promotionCateCd));
			// 库存管理
			insertMap.put("promPrtStock", promPrt.get("isStock"));
			// 品牌代码
			String brandCode = (String) promPrt.get("brandCode");
			// 厂商编码
			String unitCode = (String) promPrt.get("unitCode");
			// 品牌代码
			insertMap.put("brandCode", brandCode);
			
			// 促销品类型
			String mode = (String) promPrt.get("mode");
			insertMap.put("mode", mode);
			// 促销品全称
			String nameTotal = (String) promPrt.get("nameTotal");
			if (null != nameTotal && nameTotal.length() > 40) {
				nameTotal = nameTotal.substring(0, 40);
			}
			nameTotal = nameTotal.replaceAll("\n", "").replaceAll("\r", "").replaceAll("\t", "");
			promPrt.put("nameTotal",nameTotal);
			// 促销产品代码
			insertMap.put("promPrtCode", nameTotal);
			// 销售价格
			String salePrice = ConvertUtil.getString(promPrt.get("salePrice"));
			if (!"".equals(salePrice)) {
				if("1".equals(priceFlag)){
					float salePriceF = Math.round(ConvertUtil.getFloat(promPrt.get("salePrice")));
					salePrice = ConvertUtil.getString(salePriceF);
				}
			}
			insertMap.put("promPrtPrice", ConvertUtil.getFloat(salePrice));
			// 促销产品条码
			String barCode = (String) promPrt.get("barCode");
			insertMap.put("promPrtBarcode", barCode);
			// 促销产品厂商编码
			insertMap.put("promPrtUnitcode", unitCode);
			// 设置促销品接口表status值
			getPromScsStatus(promPrt);
			insertMap.put("BIN_PromotionProductVendorID", promPrt.get("BIN_PromotionProductVendorID"));
			insertMap.put("status", promPrt.get("status"));
			insertMap.put("Version", promPrt.get("Version"));
			promPrt.put("tVersion", baseMap.get("tVersion"));
			
			try {
				
				// 查询变化的编码条码
				Map<String,Object> barCodeModifyMap = binbessprm07Service.getBarCodeModify(promPrt);
				if(null != barCodeModifyMap){
					promPrt.put("OldUnitCode", barCodeModifyMap.get("OldUnitCode"));
					promPrt.put("OldBarCode", barCodeModifyMap.get("OldBarCode"));
					// 有变化，原编码条码在PromotionTable对应的记录的status设为D
					// step1: 更新老的编码条码，将其在PromotionTable停用，version为当前tVersion+1
					Integer updRes = binbessprm07Service.disPromotionTable(promPrt);
					// step2: 插入新的编码条码，使用merge(brand,productId,unitcode,barcode)
					Map<String,Object> mergeRes = binbessprm07Service.mergePromotionTable(insertMap);
				}
				else{
					// 无变化，编码条码没有变化的话将当前产品属性通过merge(brand,productId,unitcode,barcode)更新到product_scs上
					Map<String,Object> mergeRes = binbessprm07Service.mergePromotionTable(insertMap);
				}
				
				// 删除促销品中间表当前产品
//				binbessprm07Service.delPromotionTable(promPrt);
				// 插入促销品中间表
//				binbessprm07Service.insertPromotionTable(insertMap);
				// 提交事务
//				binbessprm07Service.ifManualCommit();
				try {
					// 查询对应关系件数
					int count = binbessprm07Service.getBarCodeCount(promPrt);
					if (0 == count) {
						promPrt.putAll(baseMap);
						
						// 查询变化的编码条码
//						Map<String,Object> barCodeModifyMap = binbessprm07Service.getBarCodeModify(promPrt);
						if(null != barCodeModifyMap){
							Map<String,Object> insertPrtSettingMap = new HashMap<String, Object>();
							insertPrtSettingMap.put("brand", brandCode); // 品牌code
							insertPrtSettingMap.put("new_barcode", barCodeModifyMap.get("BarCode")); // 产品条码
							insertPrtSettingMap.put("new_unitcode", barCodeModifyMap.get("UnitCode")); // 产品编码
							insertPrtSettingMap.put("old_barcode", barCodeModifyMap.get("OldBarCode")); // 老产品条码
							insertPrtSettingMap.put("old_unitcode", barCodeModifyMap.get("OldUnitCode")); // 老产品编码
							insertPrtSettingMap.put("prt_id", barCodeModifyMap.get("BIN_PromotionProductVendorID"));  // 促销品厂商ID（新后台）
							insertPrtSettingMap.put("prt_type", "P"); // 产品（N）/促销品区分(P)
							insertPrtSettingMap.put("enable_time", baseMap.get("enable_time")); // 新产品生效日期
							// 插入ProductSetting
							binbessprm07Service.addProductSetting(insertPrtSettingMap);
						}
						
						// 更新停用日时
						binbessprm07Service.updateClosingTime(promPrt);
						// 插入促销产品条码对应关系表
						Map<String,Object> praMap = new HashMap<String,Object>();
						praMap.putAll(promPrt);
						praMap.remove("validFlagVal");
						// 插入编码条码
						binbessprm07Service.insertPromotionPrtBarCode(praMap);
//						binbessprm07Service.manualCommit();
					}
				} catch (Exception e) {

					BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
					batchLoggerDTO.setCode("ESS00017");
					// 品牌代码
					batchLoggerDTO.addParam(brandCode);
					// 促销品厂商Id
					batchLoggerDTO.addParam(ConvertUtil.getString(promPrt.get("BIN_PromotionProductVendorID")));
					// 促销产品代码
					batchLoggerDTO.addParam(nameTotal);
					// 促销产品条码
					batchLoggerDTO.addParam(barCode);
					// 促销产品厂商编码
					batchLoggerDTO.addParam(unitCode);
					batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
					CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(
							this.getClass());
					cherryBatchLogger.BatchLogger(batchLoggerDTO, e);
					flag = CherryBatchConstants.BATCH_WARNING;
					throw new Exception(e);
				}
			} catch (Exception e) {
				// 失败件数加一
				failCount++;
				BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
				batchLoggerDTO.setCode("ESS00005");
				// 品牌代码
				batchLoggerDTO.addParam(brandCode);
				// 促销品厂商Id
				batchLoggerDTO.addParam(ConvertUtil.getString(promPrt.get("BIN_PromotionProductVendorID")));
				// 促销产品代码
				batchLoggerDTO.addParam(nameTotal);
				// 促销产品条码
				batchLoggerDTO.addParam(barCode);
				// 促销产品厂商编码
				batchLoggerDTO.addParam(unitCode);
				batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
				CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(
						this.getClass());
				cherryBatchLogger.BatchLogger(batchLoggerDTO, e);
				flag = CherryBatchConstants.BATCH_WARNING;
				
				throw new Exception(e);
			}
		}
	}
}
