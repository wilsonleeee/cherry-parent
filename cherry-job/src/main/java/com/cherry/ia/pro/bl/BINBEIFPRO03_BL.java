/*
 * @(#)BINBEIFPRO03_BL.java     1.0 2014/5/26
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

import java.math.BigDecimal;
import java.util.ArrayList;
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
import com.cherry.ia.pro.service.BINBEIFPRO03_Service;
import com.cherry.mq.mes.common.MessageConstants;
import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 *产品柜台下发BL
 * 
 * @author jijw
 * @version 1.0 2014/5/26
 */
public class BINBEIFPRO03_BL {


	/** 每批次(页)处理数量 2000 */
	private final int BATCH_SIZE = 1500;

	/** BATCH LOGGER */
	private static CherryBatchLogger logger = new CherryBatchLogger(
			BINBEIFPRO03_BL.class);
	private static final Logger loger = LoggerFactory.getLogger(BINBEIFPRO03_BL.class);
	@Resource
	private BINBEIFPRO03_Service binbeifpro03Service;


	/** JOB执行相关共通 IF */
	@Resource(name="binbecm01_IF")
	private BINBECM01_IF binbecm01_IF;
	
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

	/** 失败的主要原因，受字段长度限制，这里只要记录主要原因即可 */
	private String fReason = "";
	/** 共通map */
	private Map<String, Object> comMap;

	/**
	 * 柜台产品列表的batch处理
	 * 
	 * @param map
	 * 
	 * @return Map
	 * @throws Exception 
	 * @throws JSONException 
	 */
	public int tran_batchCouProducts(Map<String, Object> map)
			throws JSONException, Exception {
		loger.info("###############柜台产品Batch下发模式==="+map.get("cntIssuedPrtMode")+"##########");
		// 初始化
		init(map);
		
		try{
			
			// 产品表的表版本号在下发成功后+1
			Map<String, Object> seqMap = new HashMap<String, Object>();
			seqMap.putAll(map);
			seqMap.put("type", "F");
			String newTVersion = binOLCM15_BL.getNoPadLeftSequenceId(seqMap);
			map.put("newTVersion", newTVersion);
			
			
			// 更新方案中销售日期在业务日期前后1天内的产品的版本号
			binbeifpro03Service.updSoluDetailVerByPrtSellDate(map);
			
			// 取得方案配置的区域或渠道实际的的柜台与以前配置的差异(区域城市/渠道)List
			updPrtSoluCityChannelDiff(map);
		
				// 统计总条数
//				totalCount += couProductList.size();
				// 更新接口数据库
			String cntIssuedPrtMode = ConvertUtil.getString(map.get("cntIssuedPrtMode"));
			int result = 0;
			if (ConvertUtil.isBlank(cntIssuedPrtMode)) {
				result = updIFDatabase(map);
			} else if (!ConvertUtil.isBlank(cntIssuedPrtMode) && "YT".equals(cntIssuedPrtMode)) {
				result = updIFDatabaseYT(map);
			}

			loger.info("柜台产品下发结果："+result);
//				// 柜台产品数据少于一页，跳出循环
//				if (couProductList.size() < CherryBatchConstants.DATE_SIZE) {
//					break;
//				}
			if(result > 0){
				if(failCount == 0){

				}else {
					try{
						fReason = "部分柜台产品下发失败，详细日志文件。";
						flag = CherryBatchConstants.BATCH_ERROR;
						// 失败后，全部回滚（保证版本号正确）
						binbeifpro03Service.manualRollback();
						binbeifpro03Service.ifManualRollback();
						
					}catch(Exception e){
						flag = CherryBatchConstants.BATCH_ERROR;
//						logger.outLog(e.getMessage(), CherryBatchConstants.LOGGER_ERROR);
						logger.outExceptionLog(e);
					}
				}
			}
			
		}catch(Exception e){
			fReason = "柜台产品下发失败，详细日志文件。";
			flag = CherryBatchConstants.BATCH_ERROR;
			logger.outExceptionLog(e);
//			logger.outLog(e.getMessage(), CherryBatchConstants.LOGGER_ERROR);
			// 失败后，全部回滚（保证版本号正确）
			binbeifpro03Service.manualRollback();
			binbeifpro03Service.ifManualRollback();
		}
		// 输出处理结果信息
		//outMessage();
		return flag;
	}

	/**
	 * 产品列表的batch处理
	 *
	 * @param map
	 *
	 * @return Map
	 * @throws CherryBatchException
	 * @throws CherryException,Exception
	 */
	public Map<String,Object> tran_batchCntProductsMQSend(Map<String, Object> map)
			throws CherryBatchException, CherryException,Exception {

		Map<String,Object> resMap=new HashMap<String, Object>();

		try {

			// 取得当前柜台产品表版本号
			Map<String, Object> seqMap = new HashMap<String, Object>();
			seqMap.putAll(map);
			seqMap.put("type", "F");
			String tVersion = binOLCM15_BL.getCurrentSequenceId(seqMap);
			map.put("tVersion", tVersion);
			// 产品表的表版本号在下发成功后+1
			String newTVersion = binOLCM15_BL.getNoPadLeftSequenceId(seqMap);
			map.put("newTVersion", newTVersion);

			// 发送MQ
			// Step5: 调用MQHelper接口进行数据发送
			Map<String,Object> MQMap = getPrtNoticeMqMap(map, MessageConstants.MSG_SPRT_DPRT);
			if(MQMap.isEmpty()){
				throw new Exception("柜台产品实时下发通知组装异常");
			}

			//设定MQInfoDTO
			MQInfoDTO mqDTO = setMQInfoDTO(MQMap,map);
			mqDTO.setMsgQueueName("cherryToPosCMD");
			//调用共通发送MQ消息
			binOLMQCOM01_BL.sendMQMsg(mqDTO,false);

		} catch (Exception e) {
			logger.outExceptionLog(e);
			flag=CherryBatchConstants.BATCH_ERROR;
			resMap.put("ERRORMSG", e.getMessage());
			try {
				// 失败后，全部回滚（保证版本号正确）
				binbeifpro03Service.manualRollback();
				binbeifpro03Service.ifManualRollback();
			} catch (Exception ex) {
				resMap.put("ERRORMSG", ex.getMessage());
				logger.outExceptionLog(ex);
			}
		}
		// 输出处理结果信息
		//outMessage();
		resMap.put("flag", flag);
		return resMap;
	}

	/**
	 * 处理方案配置的区域或渠道实际的的柜台与以前配置的差异
	 * Step1 : 比较方案案配置的区域或渠道实际的的柜台与以前配置的差异
	 * Step2 : 将差异更新到部门产品表及部门产品价格
	 * Step3 : 删除方案对应的履历数据，并将最新的方案配置信息插入到履历表
	 * @param map
	 * @throws JSONException 
	 */
	@SuppressWarnings("unchecked")
	private void updPrtSoluCityChannelDiff(Map<String, Object> map) throws JSONException,Exception{
		
		// 定义查询的方案配置信息为区域城市/渠道
//		String [] palceTypeList = new String [] {"1","3"};
//		map.put("palceTypeList", palceTypeList);
		
		// 取得产品方案配置信息List(UpdateTime升序,包括区域、城市、指定柜台等)
		List<Map<String, Object>> dpcdList = binbeifpro03Service.getDPConfigDetailList(map);
		
		if (!CherryBatchUtil.isBlankList(dpcdList)) {
			
			for(Map<String, Object> itemMap : dpcdList){
				
				String placeType = (String)itemMap.get("PlaceType");
				if(placeType.equals(ProductConstants.LOTION_TYPE_REGION)){
					itemMap.put("city", 1);
				}
				else if(placeType.equals(ProductConstants.LOTION_TYPE_CHANNELS)){
					itemMap.put("channel", 1);
				}else if(placeType.equals(ProductConstants.LOTION_TYPE_CHANNELS_COUNTER)
						|| placeType.equals(ProductConstants.LOTION_TYPE_REGION_COUNTER)
						|| placeType.equals(ProductConstants.LOTION_TYPE_IMPORT_COUNTER)
						|| placeType.equals("7")){
					itemMap.put("counter", 1);
				}
				
				itemMap.putAll(map);
				// String productPriceSolutionID = (String)itemMap.get("productPriceSolutionID");
				
				// Step1 : 取得方案配置的区域或渠道实际的的柜台与以前配置的差异(区域城市/渠道)List
				List<Map<String, Object>> cntForPrtSoluCityChannelDiff = binbeifpro03Service.getCntForPrtSoluCityChannelDiff(itemMap);
				if (!CherryBatchUtil.isBlankList(cntForPrtSoluCityChannelDiff)) {
					
					for(Map<String, Object> diffMap : cntForPrtSoluCityChannelDiff){
						
						// Step2 : 将差异更新到部门产品表及部门产品价格
						diffMap.putAll(map);
						diffMap.put("productPriceSolutionID", diffMap.get("BIN_SolutionId"));
						String modifyFlag = (String)diffMap.get("modifyFlag"); // modifyFlag  add 增加的柜台 、sub减少的柜台
						// 取得当前方案对应的产品及增加的柜台,merge到部门产品表及价格表 validFlag = 1,version = tversion +1
						if("add".equals(modifyFlag)){
							
							diffMap.put("CounterCode", diffMap.get("CntPD"));
							// 1: 插入产品部门表
							diffMap.put("ValidFlag", CherryBatchConstants.VALIDFLAG_ENABLE);
							binbeifpro03Service.mergePrtSoluDepartRelation(diffMap);
							
						}
						// 取得当前方案对应的产品及增加的柜台,merge到部门产品表及价格表 validFlag = 0,version = tversion +1
						else if ("sub".equals(modifyFlag)){
							
							diffMap.put("CounterCode", diffMap.get("CntPDH"));
									
							// 1：将方案与原柜台关联的数据无效掉
							diffMap.put("psdValidFlag", CherryConstants.VALIDFLAG_DISABLE);
							binbeifpro03Service.updPrtSoluDepartRelation(diffMap);
						}
						
					}
					
					// Step3 : 更新产品方案配置履历表
					updPrtSoluWithDepartHis(itemMap);
				}
				
			}
			
		}
	}
	
	/**
	 * 更新产品方案配置履历表
	 * 
	 * 删除方案对应的履历数据，并将最新的方案配置信息插入到履历表
	 * 
     * @param map
     * praMap参数说明：productPriceSolutionID （方案ID）,
     * praMap参数说明：placeType（地点类型）,
     * praMap参数说明：SaveJson（地点集合）,
     * praMap参数说明：组织ID,品牌ID等共通信息,
	 * @throws JSONException,Exception 
	 */
	@SuppressWarnings("unchecked")
	public void updPrtSoluWithDepartHis(Map<String,Object> map) throws JSONException,Exception{
		
		//  更新产品方案配置履历表(后加的，为了解决区域)
	
		// 删除方案配置履历表中指定的方案
		binbeifpro03Service.delPrtSoluWithDepartHis(map);
		
		Map<String, Object> hisMap = new HashMap<String, Object>();
		hisMap.putAll(map);
		
		// 地点类型
		String placeType = (String)map.get("PlaceType");
		hisMap.put("placeType", placeType);
		// 地点集合
		String saveJson = (String)map.get("SaveJson");
		List<Long> saveJsonList = (List<Long>) JSONUtil.deserialize(saveJson);
		
		String placeTypeFlag = "counter"; // 区别方案分配地点类型属性柜台类还是区域城市/渠道类（用于ibatis查询时动态生成SQL）
		if(placeType.equals(ProductConstants.LOTION_TYPE_REGION_COUNTER)
				|| placeType.equals(ProductConstants.LOTION_TYPE_CHANNELS_COUNTER)
				|| placeType.equals(ProductConstants.LOTION_TYPE_IMPORT_COUNTER)
				|| placeType.equals("7")) {
			
			placeTypeFlag = "counter";
			hisMap.put("placeTypeFlag", placeTypeFlag);
			hisMap.put("place", 0);
			binbeifpro03Service.mergePrtSoluWithDepartHis(hisMap);
		} 
		else if (placeType.equals(ProductConstants.LOTION_TYPE_REGION)
				|| placeType.equals(ProductConstants.LOTION_TYPE_CHANNELS)) {
			
			placeTypeFlag = "CityOrChannel";
			if(placeType.equals(ProductConstants.LOTION_TYPE_REGION)){
				hisMap.put("city", 1);
			}else {
				hisMap.put("channel", 1);
			}
			// 将方案对应地点集合中每个地点所有的柜台更新到履历表
			for(Long place : saveJsonList){
				hisMap.put("place", place);
				hisMap.put("placeTypeFlag", placeTypeFlag);
				binbeifpro03Service.mergePrtSoluWithDepartHis(hisMap);
			}
		}
		
		// Step5: END
			
	}
	
	/**
	 * 价格处理
	 * @param prtSoluMap
	 * @throws JSONException
	 */
	@SuppressWarnings("unchecked")
	private void optPriceNew(Map<String, Object> prtSoluMap) throws JSONException{
		// 价格
		String priceInfo = ConvertUtil.getString(prtSoluMap.get("priceJson"));
		
		if (!CherryConstants.BLANK.equals(priceInfo)) {
			List<Map<String, Object>> priceInfoList = (List<Map<String, Object>>) JSONUtil.deserialize(priceInfo);
			
			// 价格按折扣率计算
			setPriceRate(priceInfoList, prtSoluMap);
			
			if (null != priceInfoList) {
				int size = priceInfoList.size();
				for (int i = 0; i < size; i++) {
					Map<String, Object> priceMap = priceInfoList.get(i);
					priceMap = CherryUtil.removeEmptyVal(priceMap);
					// 操作
					String option = ConvertUtil.getString(priceMap.get(ProductConstants.OPTION));
					
					priceMap.putAll(prtSoluMap);
					priceMap.put(ProductConstants.PRICESTARTDATE, prtSoluMap.get("soluStartDate"));
					priceMap.put(ProductConstants.PRICEENDDATE, prtSoluMap.get("soluEndDate"));
					
					if (CherryConstants.BLANK.equals(option) || ProductConstants.OPTION_1.equals(option)) {
						// 插入产品销售价格
						binbeifpro03Service.insertProductPrice(priceMap);
					} 
				}
			}
		}
		
	}
	
	/**
	 * 价格按折扣率计算
	 * @param priceInfoList
	 * @param map
	 */
	private void setPriceRate(List<Map<String, Object>> priceInfoList,Map<String,Object> map){
		
		String priceMode = ConvertUtil.getString(map.get("priceMode")); 
		
		if("2".equals(priceMode)){
			float saleRate = Float.parseFloat(ConvertUtil.getString(map.get("saleRate"))); 
			for(Map<String, Object> priceInfoMap : priceInfoList){
				float salePrice = Float.parseFloat(ConvertUtil.getString(priceInfoMap.get("salePrice")));
				salePrice = salePrice*saleRate/100;
				BigDecimal sp = new BigDecimal(salePrice);
				salePrice = sp.setScale(2,BigDecimal.ROUND_HALF_UP).floatValue();
				priceInfoMap.put("salePrice", salePrice);
			}
		}
	}

	/**
	 * 更新接口数据库
	 * 
	 * @param map
	 */
	private int updIFDatabase(Map<String, Object> map) {
		
		// 定义新后台数据是否有写入接口表
		int result = 0;
		
		try {
			// Step1.1  取得新后台产品方案柜台关联数据版本号version大于tVersion的list(新增/修改/停用/启用等)
			List<Map<String, Object>> prtSoluCouList = binbeifpro03Service.getPrtSoluCouList(map);
			if (!CherryBatchUtil.isBlankList(prtSoluCouList)) {
				result++;
				for (Map<String, Object> prtSoluCouItemMap : prtSoluCouList) {
					
					try {
						prtSoluCouItemMap.putAll(map);
						
						// 设置产品方案柜台接口表的状态值
						getPrtSoluCntSCSStatus(prtSoluCouItemMap);
						
						// 删除产品方案柜台接口表(根据brand、prtSolutionCode、counter)
						binbeifpro03Service.delIFPrtSoluWithCounter(prtSoluCouItemMap);
						
						// 插入柜台产品接口表
	//					productMap.put(CherryConstants.UNITCODE, "444444444444444444444444444444444444444444ssssssssssssssssssssssssssss");
						binbeifpro03Service.addIFPrtSoluWithCounter(prtSoluCouItemMap);
						
						// 插入件数加一
						insertCount += 1;
					} catch (Exception e) {
						failCount += 1;
						// 插入柜台产品接口表(WITPOSA_product_with_counter)时发生异常。品牌Code:{0}，柜台Code:{1}，产品ID:{2}({3})，产品编码：{4}，产品条码：{5}。
						BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO();
						batchExceptionDTO.setBatchName(this.getClass());
						batchExceptionDTO.setErrorCode("EIF00018");
						batchExceptionDTO.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
						// 品牌Code
						batchExceptionDTO.addErrorParam(CherryBatchUtil.getString(comMap.get("BrandCode")));
						// 柜台Code
						batchExceptionDTO.addErrorParam(CherryBatchUtil.getString(prtSoluCouItemMap.get("DepartCode")));
						// 方案Code
						batchExceptionDTO.addErrorParam(CherryBatchUtil.getString(prtSoluCouItemMap.get("SolutionCode")));
						
						batchExceptionDTO.setException(e);
						throw new CherryBatchException(batchExceptionDTO);
					}
					// 保存Brand数据库不同柜台不同产品表
//					saveCouPro(map);
					// 事务提交
//				binbeifpro03Service.ifManualCommit();
				}
			}
			
			// Step1.2 取得新后台产品方案明细表数据版本号version大于tVersion的list(新增/修改/停用/启用等)
			
			// 取得系统配置项产品方案添加模式
			String config = binOLCM14_BL.getConfigValue("1288", String.valueOf(map.get("organizationInfoId")), String.valueOf(map.get("brandInfoId")));
			map.put("soluAddModeConf", config);
			if(ProductConstants.SOLU_ADD_MODE_CONFIG_2.equals(config) 
					|| ProductConstants.SOLU_ADD_MODE_CONFIG_3.equals(config)){
				
				// Step1.2.1取得系统配置项产品方案添加模式,为颖通模式时，检查方案明细添加的分类是否有动态添加减少产品的变动情况
				// 所有产品价格方案
				List<Map<String, Object>> prtPriceSolutionList = binbeifpro03Service.getPrtPriceSolutionList(map);
				if (!CherryBatchUtil.isBlankList(prtPriceSolutionList)) {
					for(Map<String, Object> soluMap : prtPriceSolutionList){
						map.put("productPriceSolutionID", soluMap.get("solutionID"));
						// 取得当前产品方案明细表的产品与以前配置的差异List
						List<Map<String, Object>> prtForPrtSoluDetailDiff = binbeifpro03Service.getPrtForPrtSoluDetailDiff(map);
						if (!CherryBatchUtil.isBlankList(prtForPrtSoluDetailDiff)) {
							for(Map<String, Object> diffMap : prtForPrtSoluDetailDiff){
								// 将差异更新到产品方案明细表
								diffMap.putAll(map);
								String modifyFlag = (String)diffMap.get("modifyFlag"); // modifyFlag  add 增加的柜台 、sub减少的柜台
								
								// 取得当前方案及增加的产品,merge到产品方案明细表 validFlag = 1,version = tversion +1,isCate =1
								if("add".equals(modifyFlag)){
									diffMap.put("ValidFlag", CherryConstants.VALIDFLAG_ENABLE);
									diffMap.put("productId", diffMap.get("prtPD"));
									// 1: 插入产品方案明细表
									binbeifpro03Service.mergeProductPriceSolutionDetail(diffMap);
									
								}
								// 取得当前方案明细减少的产品,merge到产品方案部门关系表 validFlag = 0,version = tversion +1
								else if ("sub".equals(modifyFlag)){
									
									diffMap.put("ValidFlag", CherryConstants.VALIDFLAG_DISABLE);
									diffMap.put("productId", diffMap.get("prtPDH"));
									// 1: 将方案明细表的产品数据无效掉
									binbeifpro03Service.updPrtSoluDetail(diffMap);
								}
							}
							
						}
					}
				}
				
				// Step1.2.2 颖通模式时，方案价格根据当前标准产品当前业务日期的价格
				binbeifpro03Service.mergePPSDPrice(map);
			}

			List<Map<String, Object>> prtSoluDetailByVersionList = binbeifpro03Service.getPrtSoluDetailByVersionList(map);
			loger.info("需要下发的柜台产品明细数量为：prtSoluDetailByVersionList.size="+ (null==prtSoluDetailByVersionList?0:prtSoluDetailByVersionList.size()));
			List<Map<String,Object>> prtUpdList = new ArrayList<Map<String,Object>>();

			if (!CherryBatchUtil.isBlankList(prtSoluDetailByVersionList)) {
				for (int i = 0;i<prtSoluDetailByVersionList.size();i++) {
					// 保存接口产品方案柜台关系表
//					for (Map<String, Object> prtSoluDetailItemMap : prtSoluDetailByVersionList) {
					result++;
					Map<String, Object> prtSoluDetailItemMap = prtSoluDetailByVersionList.get(i);
					try{
						prtSoluDetailItemMap.putAll(map);
						// 设置产品方案柜台接口表的状态值
						getPrtSoluSCSStatus(prtSoluDetailItemMap);
						/*// 删除产品方案柜台接口表(根据brand、prtSolutionCode、产品厂商ID)
						binbeifpro03Service.delIFPrtSoluSCS(prtSoluDetailItemMap);
						// 插入产品方案明细接口表
						binbeifpro03Service.addIFPrtSoluSCS(prtSoluDetailItemMap);*/
						prtUpdList.add(prtSoluDetailItemMap);
					} catch(Exception e){

						// 插入柜台产品接口表(WITPOSA_product_with_counter)时发生异常。品牌Code:{0}，柜台Code:{1}，产品ID:{2}({3})，产品编码：{4}，产品条码：{5}。
						BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO();
						batchExceptionDTO.setBatchName(this.getClass());
						batchExceptionDTO.setErrorCode("EIF00019");
						batchExceptionDTO.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
						// 品牌Code
						batchExceptionDTO.addErrorParam(CherryBatchUtil.getString(comMap.get("BrandCode")));
						batchExceptionDTO.addErrorParam(CherryBatchUtil.getString(prtSoluDetailItemMap.get("SolutionCode")));
						batchExceptionDTO.addErrorParam(CherryBatchUtil.getString(prtSoluDetailItemMap.get("BIN_ProductVendorID")));
						batchExceptionDTO.addErrorParam(CherryBatchUtil.getString(prtSoluDetailItemMap.get("UnitCode")));
						batchExceptionDTO.addErrorParam(CherryBatchUtil.getString(prtSoluDetailItemMap.get("BarCode")));

						batchExceptionDTO.setException(e);
						throw new CherryBatchException(batchExceptionDTO);
					}
					if((i>0 && i%BATCH_SIZE==0) || i == prtSoluDetailByVersionList.size()-1) {
						Map<String,Object> delMap = new HashMap<String,Object>();
						delMap.put("prtUpdList", prtUpdList);
						loger.info("柜台产品下发，开始处理i="+i+",prtUpdList.size="+prtUpdList.size());
						// 删除产品方案柜台接口表(根据brand、prtSolutionCode、产品厂商ID)
						binbeifpro03Service.delIFPrtSoluSCS(delMap);
						loger.info("柜台产品下发，已删除到i="+i);
						// 插入产品方案明细接口表
						binbeifpro03Service.addIFPrtSoluSCS(prtUpdList);
						loger.info("柜台产品下发，已插入到i="+i);
						prtUpdList = new ArrayList<Map<String, Object>>();
					}
				}
			}
//			}
		} catch (Exception e) {
			// 失败件数加一
			failCount += 1;
			// 事务回滚
//				binbeifpro03Service.ifManualRollback();
			flag = CherryBatchConstants.BATCH_WARNING;
			logger.outLog(e.getMessage(), CherryBatchConstants.LOGGER_ERROR);
		}
		
		return result;
	}


	/**
	 * 更新接口数据库
	 *
	 * @param map
	 */
	private int updIFDatabaseYT(Map<String, Object> map) {

		// 定义新后台数据是否有写入接口表
		int result = 0;

		try {
			// Step1.1  取得新后台产品方案柜台关联数据版本号version大于tVersion的list(新增/修改/停用/启用等)
			List<Map<String, Object>> prtSoluCouList = binbeifpro03Service.getPrtSoluCouList(map);
			if (!CherryBatchUtil.isBlankList(prtSoluCouList)) {
				result++;
				for (Map<String, Object> prtSoluCouItemMap : prtSoluCouList) {

					try {
						prtSoluCouItemMap.putAll(map);

						// 设置产品方案柜台接口表的状态值
						getPrtSoluCntSCSStatus(prtSoluCouItemMap);

						// 删除产品方案柜台接口表(根据brand、prtSolutionCode、counter)
						binbeifpro03Service.delIFPrtSoluWithCounter(prtSoluCouItemMap);

						// 插入柜台产品接口表
						//					productMap.put(CherryConstants.UNITCODE, "444444444444444444444444444444444444444444ssssssssssssssssssssssssssss");
						binbeifpro03Service.addIFPrtSoluWithCounter(prtSoluCouItemMap);

						// 插入件数加一
						insertCount += 1;
					} catch (Exception e) {
						failCount += 1;
						// 插入柜台产品接口表(WITPOSA_product_with_counter)时发生异常。品牌Code:{0}，柜台Code:{1}，产品ID:{2}({3})，产品编码：{4}，产品条码：{5}。
						BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO();
						batchExceptionDTO.setBatchName(this.getClass());
						batchExceptionDTO.setErrorCode("EIF00018");
						batchExceptionDTO.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
						// 品牌Code
						batchExceptionDTO.addErrorParam(CherryBatchUtil.getString(comMap.get("BrandCode")));
						// 柜台Code
						batchExceptionDTO.addErrorParam(CherryBatchUtil.getString(prtSoluCouItemMap.get("DepartCode")));
						// 方案Code
						batchExceptionDTO.addErrorParam(CherryBatchUtil.getString(prtSoluCouItemMap.get("SolutionCode")));

						batchExceptionDTO.setException(e);
						throw new CherryBatchException(batchExceptionDTO);
					}
					// 保存Brand数据库不同柜台不同产品表
//					saveCouPro(map);
					// 事务提交
//				binbeifpro03Service.ifManualCommit();
				}
			}

			// Step1.2 取得新后台产品方案明细表数据版本号version大于tVersion的list(新增/修改/停用/启用等)

			// 取得系统配置项产品方案添加模式
			/*String config = binOLCM14_BL.getConfigValue("1288", String.valueOf(map.get("organizationInfoId")), String.valueOf(map.get("brandInfoId")));
			map.put("soluAddModeConf", config);
			if(ProductConstants.SOLU_ADD_MODE_CONFIG_2.equals(config)
					|| ProductConstants.SOLU_ADD_MODE_CONFIG_3.equals(config)){*/

				// Step1.2.1取得系统配置项产品方案添加模式,为颖通模式时，检查方案明细添加的分类是否有动态添加减少产品的变动情况
				// 所有产品价格方案
				List<Map<String, Object>> prtPriceSolutionList = binbeifpro03Service.getPrtPriceSolutionList(map);
				if (!CherryBatchUtil.isBlankList(prtPriceSolutionList)) {
					for(Map<String, Object> soluMap : prtPriceSolutionList){
						map.put("productPriceSolutionID", soluMap.get("solutionID"));
						// 取得当前产品方案明细表的产品与以前配置的差异List
						List<Map<String, Object>> prtForPrtSoluDetailDiff = binbeifpro03Service.getPrtForPrtSoluDetailDiffYT(map);
						if (!CherryBatchUtil.isBlankList(prtForPrtSoluDetailDiff)) {
							for(Map<String, Object> diffMap : prtForPrtSoluDetailDiff){
								// 将差异更新到产品方案明细表
								diffMap.putAll(map);
								String modifyFlag = (String)diffMap.get("modifyFlag"); // modifyFlag  add 增加的柜台 、sub减少的柜台

								// 取得当前方案及增加的产品,merge到产品方案明细表 validFlag = 1,version = tversion +1,isCate =1
								if("add".equals(modifyFlag)){
									diffMap.put("ValidFlag", CherryConstants.VALIDFLAG_ENABLE);
									diffMap.put("productId", diffMap.get("prtPD"));
									// 1: 插入产品方案明细表
									binbeifpro03Service.mergeProductPriceSolutionDetail(diffMap);

								}
								// 取得当前方案明细减少的产品,merge到产品方案部门关系表 validFlag = 0,version = tversion +1
								else if ("sub".equals(modifyFlag)){

									diffMap.put("ValidFlag", CherryConstants.VALIDFLAG_DISABLE);
									diffMap.put("productId", diffMap.get("prtPDH"));
									// 1: 将方案明细表的产品数据无效掉
									binbeifpro03Service.updPrtSoluDetail(diffMap);
								}
							}

						}
					}
				}

				// Step1.2.2 颖通模式时，方案价格根据当前标准产品当前业务日期的价格
				binbeifpro03Service.mergePPSDPrice(map);
			//}

			List<Map<String, Object>> prtSoluDetailByVersionList = binbeifpro03Service.getPrtSoluDetailByVersionList(map);
			loger.info("需要下发的柜台产品明细数量为：prtSoluDetailByVersionList.size="+ (null==prtSoluDetailByVersionList?0:prtSoluDetailByVersionList.size()));
			List<Map<String,Object>> prtUpdList = new ArrayList<Map<String,Object>>();

			if (!CherryBatchUtil.isBlankList(prtSoluDetailByVersionList)) {
				for (int i = 0;i<prtSoluDetailByVersionList.size();i++) {
					// 保存接口产品方案柜台关系表
//					for (Map<String, Object> prtSoluDetailItemMap : prtSoluDetailByVersionList) {
					result++;
					Map<String, Object> prtSoluDetailItemMap = prtSoluDetailByVersionList.get(i);
					try{
						prtSoluDetailItemMap.putAll(map);
						// 设置产品方案柜台接口表的状态值
						getPrtSoluSCSStatus(prtSoluDetailItemMap);
						/*// 删除产品方案柜台接口表(根据brand、prtSolutionCode、产品厂商ID)
						binbeifpro03Service.delIFPrtSoluSCS(prtSoluDetailItemMap);
						// 插入产品方案明细接口表
						binbeifpro03Service.addIFPrtSoluSCS(prtSoluDetailItemMap);*/
						prtUpdList.add(prtSoluDetailItemMap);
					} catch(Exception e){

						// 插入柜台产品接口表(WITPOSA_product_with_counter)时发生异常。品牌Code:{0}，柜台Code:{1}，产品ID:{2}({3})，产品编码：{4}，产品条码：{5}。
						BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO();
						batchExceptionDTO.setBatchName(this.getClass());
						batchExceptionDTO.setErrorCode("EIF00019");
						batchExceptionDTO.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
						// 品牌Code
						batchExceptionDTO.addErrorParam(CherryBatchUtil.getString(comMap.get("BrandCode")));
						batchExceptionDTO.addErrorParam(CherryBatchUtil.getString(prtSoluDetailItemMap.get("SolutionCode")));
						batchExceptionDTO.addErrorParam(CherryBatchUtil.getString(prtSoluDetailItemMap.get("BIN_ProductVendorID")));
						batchExceptionDTO.addErrorParam(CherryBatchUtil.getString(prtSoluDetailItemMap.get("UnitCode")));
						batchExceptionDTO.addErrorParam(CherryBatchUtil.getString(prtSoluDetailItemMap.get("BarCode")));

						batchExceptionDTO.setException(e);
						throw new CherryBatchException(batchExceptionDTO);
					}
					if((i>0 && i%BATCH_SIZE==0) || i == prtSoluDetailByVersionList.size()-1) {
						Map<String,Object> delMap = new HashMap<String,Object>();
						delMap.put("prtUpdList", prtUpdList);
						loger.info("柜台产品下发，开始处理i="+i+",prtUpdList.size="+prtUpdList.size());
						// 删除产品方案柜台接口表(根据brand、prtSolutionCode、产品厂商ID)
						binbeifpro03Service.delIFPrtSoluSCS(delMap);
						loger.info("柜台产品下发，已删除到i="+i);
						// 插入产品方案明细接口表
						binbeifpro03Service.addIFPrtSoluSCS(prtUpdList);
						loger.info("柜台产品下发，已插入到i="+i);
						prtUpdList = new ArrayList<Map<String, Object>>();
					}
				}
			}
//			}
		} catch (Exception e) {
			// 失败件数加一
			failCount += 1;
			// 事务回滚
//				binbeifpro03Service.ifManualRollback();
			flag = CherryBatchConstants.BATCH_WARNING;
			logger.outLog(e.getMessage(), CherryBatchConstants.LOGGER_ERROR);
		}

		return result;
	}

	/**
	 * 插入接口数据库柜台产品配置表(WITPOSA_product_with_counter)
	 * 
	 * @param couProductMap
	 * @return
	 */
	@Deprecated
	private void saveCouPro(Map<String, Object> couProductMap)
			throws CherryBatchException {
		try {
			
			couProductMap.putAll(comMap);
			
			// 删除产品接口表(根据brand、unitcode、barcode)
			binbeifpro03Service.delIFProductWithCounter(couProductMap);
			// 插入产品接口表
//			getPrtScsStatus(couProductMap);
			binbeifpro03Service.addProductWithCounter(couProductMap);
			// 插入件数加一
			insertCount += 1;
		} catch (Exception e) {
			// 插入柜台产品接口表(WITPOSA_product_with_counter)时发生异常。品牌Code:{0}，柜台Code:{1}，产品ID:{2}({3})，产品编码：{4}，产品条码：{5}。
			BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO();
			batchExceptionDTO.setBatchName(this.getClass());
			batchExceptionDTO.setErrorCode("EIF00017");
			batchExceptionDTO.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
			// 品牌Code
			batchExceptionDTO.addErrorParam(CherryBatchUtil.getString(comMap.get("BrandCode")));
			// 柜台Code
			batchExceptionDTO.addErrorParam(CherryBatchUtil
					.getString(couProductMap.get("DepartCode")));
			// 产品ID（产品名称）
			batchExceptionDTO.addErrorParam(CherryBatchUtil
					.getString(couProductMap.get("BIN_ProductVendorID")));
			batchExceptionDTO.addErrorParam(CherryBatchUtil
					.getString(couProductMap.get("NameTotal")));
			// 产品编码
			batchExceptionDTO.addErrorParam(CherryBatchUtil
					.getString(couProductMap.get("UnitCode")));
			// 产品条码
			batchExceptionDTO.addErrorParam(CherryBatchUtil
					.getString(couProductMap.get("BarCode")));
			
			batchExceptionDTO.setException(e);
			throw new CherryBatchException(batchExceptionDTO);
		}
	}

	/**
	 * 程序结束时，处理Job共通( 插入Job运行履历表)
	 * @param paraMap
	 * @throws Exception
	 */
	public void tran_programEnd(Map<String,Object> paraMap) throws Exception{

		// 程序结束时，插入Job运行履历表
		paraMap.putAll(comMap);
		paraMap.put("flag", flag);
		paraMap.put("TargetDataCNT", totalCount);
		paraMap.put("SCNT", totalCount - failCount);
		paraMap.put("FCNT", failCount);
		paraMap.put("UCNT", updateCount);
		paraMap.put("ICNT", insertCount);
		paraMap.put("FReason", fReason);
		paraMap.remove("validFlagVal");
		binbecm01_IF.insertJobRunHistory(paraMap);
	}

	/**
	 * 设置柜台产品接口表的状态值
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
	 * 设置柜台产品接口表的状态值
	 * @param productMap
	 */
	private void getPrtSoluSCSStatus(Map<String, Object> productMap){

		/*
		 处理逻辑 （实时下发接口状态值设值(0：正常销售、1：停用、2：下柜、3：未启用*)）
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
		*/
		
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
		
		productMap.put("prtSoluDetail_status", prtSoluDetail_status);

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
		String brandCode = ConvertUtil.getString(comMap.get(CherryBatchConstants.BRAND_CODE));
		mainData.put("BrandCode", brandCode);
		
		//单据号
		String tradeNoIf = binOLCM03_BL.getTicketNumber(String.valueOf(map.get(CherryConstants.ORGANIZATIONINFOID)), String.valueOf(map.get(CherryConstants.BRANDINFOID)), String.valueOf(map.get("loginName")), MessageConstants.MSG_PR);
		//如果单据号为空抛自定义异常：组装MQ消息体时失败，单据号取号失败！
		if(tradeNoIf==null||tradeNoIf.isEmpty()){
			throw new CherryException("EBS00069");
		}
		mainData.put("TradeNoIF", tradeNoIf);
		//业务类型
		mainData.put("TradeType", MessageConstants.MSG_PR);
		//子类型
		mainData.put("SubType", subType);
		// 表版本号
		mainData.put("TVersion", map.get("newTVersion"));
		//操作者
		mainData.put("EmployeeId", ConvertUtil.getString(map.get("EmployeeId")));
		
		//一直向前增长的系统时间
		mainData.put("Time", binbeifpro03Service.getForwardSYSDate());
		
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
	 * 输出处理结果信息
	 * 
	 * @throws CherryBatchException
	 */
	public void outMessage() throws CherryBatchException {
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
		// 插入件数
		BatchLoggerDTO batchLoggerDTO3 = new BatchLoggerDTO();
		batchLoggerDTO3.setCode("IIF00003");
		batchLoggerDTO3.setLevel(CherryBatchConstants.LOGGER_INFO);
		batchLoggerDTO3.addParam(String.valueOf(insertCount));
		// 更新件数
		BatchLoggerDTO batchLoggerDTO4 = new BatchLoggerDTO();
		batchLoggerDTO4.setCode("IIF00004");
		batchLoggerDTO4.setLevel(CherryBatchConstants.LOGGER_INFO);
		batchLoggerDTO4.addParam(String.valueOf(updateCount));
		// 失败件数
		BatchLoggerDTO batchLoggerDTO5 = new BatchLoggerDTO();
		batchLoggerDTO5.setCode("IIF00005");
		batchLoggerDTO5.setLevel(CherryBatchConstants.LOGGER_INFO);
		batchLoggerDTO5.addParam(String.valueOf(failCount));
		// 处理总件数
		logger.BatchLogger(batchLoggerDTO1);
		// 插入件数
		logger.BatchLogger(batchLoggerDTO3);
		// 更新件数
		logger.BatchLogger(batchLoggerDTO4);
		// 成功总件数
		logger.BatchLogger(batchLoggerDTO2);
		// 失败件数
		logger.BatchLogger(batchLoggerDTO5);
	}

	private void init(Map<String, Object> map) {
		// 业务日期，日结标志
		Map<String, Object> bussDateMap = binbeifpro03Service.getBussinessDateMap(map);
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

		String sysdateTime = binbeifpro03Service.getSYSDateTime();
		// BatchCD 来自VSS$/01.Cherry/02.设计文档/01.概要设计/00.各种一览/【新设】CherryBatch一览.xlsx
		map.put("JobCode", "BAT081");

		// 程序【开始运行时间】
		map.put("RunStartTime", sysdateTime);

		// 取得更新共通信息map
		comMap = getComMap(map);
		// 启用日时
		comMap.put(ProductConstants.STARTTIME, startTime);
		// 停用日时
		comMap.put(ProductConstants.CLOSINGTIME, closingTime);
		// 新产品生效日期
//		String enable_time = DateUtil.suffixDate(businessDate, 1);
//		comMap.put("enable_time", enable_time);

//		binbeifpro03Service.delIFCouProduct(comMap);

		// 取得当前产品表版本号
		Map<String, Object> seqMap = new HashMap<String, Object>();
		seqMap.putAll(map);
		seqMap.put("type", "F");
		String tVersion = binOLCM15_BL.getCurrentSequenceId(seqMap);
		map.put("tVersion", tVersion);
	}

	private Map<String, Object> getComMap(Map<String, Object> map) {
		Map<String, Object> baseMap = new HashMap<String, Object>();
		// 品牌Code
		String branCode = binbeifpro03Service.getBrandCode(map);
		// 系统时间
		String sysTime = binbeifpro03Service.getSYSDate();

		// 品牌Code
		baseMap.put(ProductConstants.BRANDCODE, branCode);
		// 作成时间
		baseMap.put(CherryBatchConstants.CREATE_TIME, sysTime);
		// 更新时间
		baseMap.put(CherryBatchConstants.UPDATE_TIME, sysTime);
		// 更新程序名
		baseMap.put(CherryBatchConstants.UPDATEPGM, "BINBEIFPRO03");
		// 作成程序名
		baseMap.put(CherryBatchConstants.CREATEPGM, "BINBEIFPRO03");
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
