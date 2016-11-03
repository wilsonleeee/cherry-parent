/*	
 * @(#)BINCPMEACT03_BL.java     1.0 2013/02/20		
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
package com.cherry.cp.act.bl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.core.BatchExceptionDTO;
import com.cherry.cm.core.BatchLoggerDTO;
import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryBatchException;
import com.cherry.cm.core.CherryBatchLogger;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cp.act.service.BINCPMEACT03_Service;

/**
 * 导入会员活动和会员活动预约信息处理BL
 * 
 * @author WangCT
 * @version 1.0 2013/02/20
 */
public class BINCPMEACT03_BL {
	
	/** 导入会员活动和会员活动预约信息处理Service **/
	@Resource
	private BINCPMEACT03_Service binCPMEACT03_Service;
	
	/** 保存所有会员活动基础属性IDList **/
	private List<Map<String, Object>> campaignBasePropIdList;
	
	/** 保存所有产品信息List **/
	private List<Map<String, Object>> productList;
	
	/** 保存所有促销品信息List **/
	private List<Map<String, Object>> promotionPrtList;
	
	/** 保存所有活动代码List **/
	private List<String> campaignCodeList;
	
	// BATCH处理标志
	private int flag = CherryBatchConstants.BATCH_SUCCESS;
	
	/**
	 * 导入会员活动和会员活动预约信息处理
	 * 
	 * @param map 传入参数包括组织ID、品牌ID等
	 * @return BATCH处理标志
	 */
	public int tran_importCampaignInfo(Map<String, Object> map) throws Exception {
		
		productList = new ArrayList<Map<String,Object>>();
		promotionPrtList = new ArrayList<Map<String,Object>>();
		
		// 取得所有会员活动基础属性ID
		campaignBasePropIdList = binCPMEACT03_Service.getCampaignBasePropId(map);
		if(campaignBasePropIdList == null || campaignBasePropIdList.size() != 4) {
			BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
			batchLoggerDTO.setCode("ECP00001");
			batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
			CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
			cherryBatchLogger.BatchLogger(batchLoggerDTO);
			flag = CherryBatchConstants.BATCH_WARNING;
			return flag;
		}
		// 取得所有活动代码
		campaignCodeList = binCPMEACT03_Service.getCampaignCodeList(map);
		
		try {
			// 导入会员活动
			this.importCampaign(map);
		} catch (Exception e) {
			try {
				// Cherry数据库回滚事务
				binCPMEACT03_Service.manualRollback();
			} catch (Exception ex) {
				
			}
			BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
			batchLoggerDTO.setCode("ECP00005");
			batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
			CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
			cherryBatchLogger.BatchLogger(batchLoggerDTO,e);
			flag = CherryBatchConstants.BATCH_WARNING;
			return flag;
		}
		
		if(flag != CherryBatchConstants.BATCH_SUCCESS) {
			return flag;
		}
		
		try {
			// 导入会员活动预约信息
			this.importCampaignOrder(map);
		} catch (Exception e) {
			try {
				// Cherry数据库回滚事务
				binCPMEACT03_Service.manualRollback();
			} catch (Exception ex) {
				
			}
			BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
			batchLoggerDTO.setCode("ECP00004");
			batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
			CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
			cherryBatchLogger.BatchLogger(batchLoggerDTO,e);
			flag = CherryBatchConstants.BATCH_WARNING;
			return flag;
		}
		
		if(flag != CherryBatchConstants.BATCH_SUCCESS) {
			return flag;
		}
		
		BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
		batchLoggerDTO.setCode("ICP00001");
		batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_INFO);
		CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
		cherryBatchLogger.BatchLogger(batchLoggerDTO);
		
		return flag;
	}
	
	/**
	 * 导入会员活动
	 * 
	 * @param map 传入参数包括组织ID、品牌ID等
	 */
	public void importCampaign(Map<String, Object> map) throws Exception {
		
		// 从会员活动接口表取得未下发的活动记录
		List<Map<String, Object>> campaignImportList = binCPMEACT03_Service.getCampaignImport(map);
		if(campaignImportList != null && !campaignImportList.isEmpty()) {
			List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
			List<String[]> keyList = new ArrayList<String[]>();
			String[] key1 = {"campaignCode","campaignType","campaignName","campaignOrderFromDate","campaignOrderToDate","obtainFromDate","obtainToDate","needBuyFlag"};
			String[] key2 = {"subCampaignCode","subCampaignType","subCampaignName","subCampaignValid","localValidRule","isCollectInfo"};
			keyList.add(key1);
			keyList.add(key2);
			// 把会员活动记录按主活动、子活动、结果明细分层处理
			ConvertUtil.convertList2DeepList(campaignImportList,list,keyList,0);
			
			// 导入失败的活动List
			List<Map<String, Object>> updCampErrorList = new ArrayList<Map<String,Object>>();
			for(int i = 0; i < list.size(); i++) {
				Map<String, Object> campaignMap = list.get(i);
				campaignMap.put(CherryBatchConstants.ORGANIZATIONINFOID, map.get(CherryBatchConstants.ORGANIZATIONINFOID));
				campaignMap.put(CherryBatchConstants.BRANDINFOID, map.get(CherryBatchConstants.BRANDINFOID));
				try {
					// 导入单个会员活动
					this.importCampaignOne(campaignMap);
					binCPMEACT03_Service.manualCommit();
				} catch (CherryBatchException cherryBatchException) {
					try {
						// Cherry数据库回滚事务
						binCPMEACT03_Service.manualRollback();
					} catch (Exception ex) {
						
					}
					// 品牌代码
					String brandCode = (String)map.get("brandCode");
					// 活动代码
					String campaignCode = (String)campaignMap.get("campaignCode");
					// 添加导入失败的活动
					Map<String, Object> updCampErrorMap = new HashMap<String, Object>();
					updCampErrorMap.put("brandCode", brandCode);
					updCampErrorMap.put("campaignCode", campaignCode);
					updCampErrorList.add(updCampErrorMap);
					flag = CherryBatchConstants.BATCH_WARNING;
				} catch (Exception e) {
					try {
						// Cherry数据库回滚事务
						binCPMEACT03_Service.manualRollback();
					} catch (Exception ex) {
						
					}
					// 品牌代码
					String brandCode = (String)map.get("brandCode");
					// 活动代码
					String campaignCode = (String)campaignMap.get("campaignCode");
					// 添加导入失败的活动
					Map<String, Object> updCampErrorMap = new HashMap<String, Object>();
					updCampErrorMap.put("brandCode", brandCode);
					updCampErrorMap.put("campaignCode", campaignCode);
					updCampErrorList.add(updCampErrorMap);
					
					BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
					batchLoggerDTO.setCode("ECP00008");
					batchLoggerDTO.addParam(brandCode);
					batchLoggerDTO.addParam(campaignCode);
					batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
					CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
					cherryBatchLogger.BatchLogger(batchLoggerDTO, e);
					flag = CherryBatchConstants.BATCH_WARNING;
				}
			}
			try {
				// 存在导入失败的活动的场合
				if(!updCampErrorList.isEmpty()) {
					// 更新会员活动接口表为下发失败状态
					binCPMEACT03_Service.updCampaignImportError(updCampErrorList);
				}
				// 更新会员活动接口表为下发成功状态
				binCPMEACT03_Service.updCampaignImportSuccess(map);
				// 存在导入失败的活动的场合
				if(!updCampErrorList.isEmpty()) {
					// 更新会员活动接口表把下发失败的数据更新成未下发状态
					binCPMEACT03_Service.updCampaignImportWait(map);
				}
				binCPMEACT03_Service.manualCommit();
			} catch (Exception e) {
				try {
					// Cherry数据库回滚事务
					binCPMEACT03_Service.manualRollback();
				} catch (Exception ex) {
					
				}
			}
		}
	}
	
	/**
	 * 导入单个会员活动
	 * 
	 * @param campaignMap 会员活动信息
	 */
	public void importCampaignOne(Map<String, Object> campaignMap) throws Exception {
		
		// 活动预约开始日期
		String campaignOrderFromDate = (String)campaignMap.get("campaignOrderFromDate");
		// 活动预约结束日期
		String campaignOrderToDate = (String)campaignMap.get("campaignOrderToDate");
		// 活动领用开始日期
		String obtainFromDate = (String)campaignMap.get("obtainFromDate");
		// 活动领用结束日期
		String obtainToDate = (String)campaignMap.get("obtainToDate");
		// 活动有效期开始日期设定逻辑： 活动预约开始时间存在的场合使用活动预约开始时间，不存在的场合使用活动领用开始日期
		if(campaignOrderFromDate != null && !"".equals(campaignOrderFromDate)) {
			campaignMap.put("campaignFromDate", campaignOrderFromDate);
		} else {
			campaignMap.put("campaignFromDate", obtainFromDate);
		}
		// 活动有效期结束日期使用活动领用结束日期
		campaignMap.put("campaignToDate", obtainToDate);
		// 取得活动ID
		String campaignId = binCPMEACT03_Service.getCampaignId(campaignMap);
		// 主题活动不存在的场合添加主题活动
		if(campaignId == null || "".equals(campaignId)) {
			// 设置新增和更新时的共通参数
			setSysParam(campaignMap);
			// 添加活动
			campaignId = String.valueOf(binCPMEACT03_Service.addCampaign(campaignMap));
		}
		// 取得子活动List
		List<Map<String, Object>> subCampaignList = (List)campaignMap.get("list");
		if(subCampaignList != null && !subCampaignList.isEmpty()) {
			for(int j = 0; j < subCampaignList.size(); j++) {
				Map<String, Object> subCampaignMap = subCampaignList.get(j);
				subCampaignMap.put("campaignId", campaignId);
				// 取得子活动ID
				String subCampaignId = binCPMEACT03_Service.getSubCampaignId(subCampaignMap);
				// 子活动存在的场合不进行更新处理，打错误日志并继续下一个子活动处理
				if(subCampaignId != null && !"".equals(subCampaignId)) {
					BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
					batchLoggerDTO.setCode("ECP00007");
					batchLoggerDTO.addParam((String)subCampaignMap.get("subCampaignCode"));
					batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
					CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
					cherryBatchLogger.BatchLogger(batchLoggerDTO);
					continue;
				} else {
					// 设置新增和更新时的共通参数
					setSysParam(subCampaignMap);
					// 添加子活动
					subCampaignId = String.valueOf(binCPMEACT03_Service.addSubCampaign(subCampaignMap));
					subCampaignMap.put("subCampaignId", subCampaignId);
				}
				
				// 会员活动规则条件明细List
				List<Map<String, Object>> camRuleConditionList = new ArrayList<Map<String,Object>>();
				// 会员活动规则条件明细保存预约时间、领用时间、区域市、活动对象4个会员活动基础属性
				for(Map<String, Object> campaignBasePropIdMap : campaignBasePropIdList) {
					Map<String, Object> _campaignBasePropIdMap = new HashMap<String, Object>();
					_campaignBasePropIdMap.putAll(campaignBasePropIdMap);
					_campaignBasePropIdMap.put("subCampaignId", subCampaignId);
					String propertyName = (String)campaignBasePropIdMap.get("propertyName");
					// 会员活动基础属性为预约时间的场合，如果存在预约时间那么需要保存，如果不存在那么就不保存
					if("baseProp_rese_time".equals(propertyName)) {
						if(campaignOrderFromDate != null && !"".equals(campaignOrderFromDate)) {
							_campaignBasePropIdMap.put("basePropValue1", campaignOrderFromDate);
							_campaignBasePropIdMap.put("basePropValue2", campaignOrderToDate);
						} else {
							continue;
						}
					} else if("baseProp_obtain_time".equals(propertyName)) { // 会员活动基础属性为领用时间的场合保存领用时间
						_campaignBasePropIdMap.put("basePropValue1", obtainFromDate);
						_campaignBasePropIdMap.put("basePropValue2", obtainToDate);
					} else if("baseProp_city".equals(propertyName)) { // 会员活动基础属性为区域市的场合保存"ALL"
						_campaignBasePropIdMap.put("basePropValue1", "ALL");
						_campaignBasePropIdMap.put("actLocationType", "0");
					} else if("baseProp_customer".equals(propertyName)) { // 会员活动基础属性为活动对象的场合保存"ALL"
						_campaignBasePropIdMap.put("basePropValue1", "ALL");
						_campaignBasePropIdMap.put("actLocationType", "5");
					}
					// 设置新增和更新时的共通参数
					setSysParam(_campaignBasePropIdMap);
					camRuleConditionList.add(_campaignBasePropIdMap);
				}
				// 添加会员活动规则条件明细
				binCPMEACT03_Service.addCamRuleCondition(camRuleConditionList);
				
				// 取得活动结果明细List
				List<Map<String, Object>> campaignRuleResultList = (List)subCampaignMap.get("list");
				if(campaignRuleResultList != null && !campaignRuleResultList.isEmpty()) {
					for(Map<String, Object> campaignRuleResultMap : campaignRuleResultList) {
						campaignRuleResultMap.put("subCampaignId", subCampaignId);
						// 产品类型
						String prtType = (String)campaignRuleResultMap.get("prtType");
						// 产品条码
						String barCode = (String)campaignRuleResultMap.get("barCode");
						// 厂商编码
						String unitCode = (String)campaignRuleResultMap.get("unitCode");
						// 根据产品类型、厂商编码、产品条码找到产品厂商ID或者促销品厂商ID
						String productVendorId = this.getProductVendorId(prtType, unitCode, barCode);
						if(productVendorId != null && !"".equals(productVendorId)) {
							campaignRuleResultMap.put("productVendorId", productVendorId);
						} else {
							BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO();
							batchExceptionDTO.setBatchName(this.getClass());
							batchExceptionDTO.setErrorCode("ECP00006");
							batchExceptionDTO.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
							batchExceptionDTO.addErrorParam(unitCode);
							batchExceptionDTO.addErrorParam(barCode);
							throw new CherryBatchException(batchExceptionDTO);
						}
						// 设置新增和更新时的共通参数
						setSysParam(campaignRuleResultMap);
					}
					// 添加会员活动规则结果明细
					binCPMEACT03_Service.addCampaignRuleResult(campaignRuleResultList);
				}
			}
		}
	}
	
	/**
	 * 导入会员活动预约信息
	 * 
	 * @param map 传入参数包括组织ID、品牌ID等
	 */
	public void importCampaignOrder(Map<String, Object> map) throws Exception {
		
		// 数据查询长度
		int dataSize = CherryBatchConstants.DATE_SIZE;
		// 查询数据量
		map.put("COUNT", dataSize);
		while (true) {
			// 取得未下发的会员活动预约记录
			List<Map<String, Object>> campaignOrderImportList = binCPMEACT03_Service.getCampaignOrderImport(map);
			
			// 会员活动预约记录不为空
			if (campaignOrderImportList != null && !campaignOrderImportList.isEmpty()) {
				List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
				String id = null;
				List<Map<String,Object>> detailList = null;
				Object preMemberInfoId = null;
				// 按会员预约主从分层处理
				for(int i = 0; i < campaignOrderImportList.size(); i++) {
					Map<String, Object> campaignOrderImportMap = campaignOrderImportList.get(i);
					String orderId = (String)campaignOrderImportMap.get("orderId");
					Object memberInfoId = campaignOrderImportMap.get("memberInfoId");
					boolean isNotEqual = true;
					if(i == 0) {
						isNotEqual = true;
					} else {
						if(!id.equals(orderId)) {
							isNotEqual = true;
						} else {
							isNotEqual = false;
						}
					}
					// 单据号与前一条记录的单据号不相同的场合，新生产主单据map和明细list
					if(isNotEqual) {
						detailList = new ArrayList<Map<String,Object>>();
						Map<String,Object> campaignOrderDetailMap = new HashMap<String, Object>();
						campaignOrderDetailMap.put("campSubCode", campaignOrderImportMap.get("campSubCode"));
						campaignOrderDetailMap.put("giftType", campaignOrderImportMap.get("giftType"));
						campaignOrderDetailMap.put("unitCode", campaignOrderImportMap.get("unitCode"));
						campaignOrderDetailMap.put("barCode", campaignOrderImportMap.get("barCode"));
						campaignOrderDetailMap.put("quantity", campaignOrderImportMap.get("quantity"));
						detailList.add(campaignOrderDetailMap);
						
						campaignOrderImportMap.put("list", detailList);
						list.add(campaignOrderImportMap);
						id = orderId;
						preMemberInfoId = memberInfoId;
					} else { // 单据号与前一条记录的单据号相同的场合，往前一个单据的明细list中继续添加明细
						// 存在同一预约单有不同会员ID的记录忽略
						if(preMemberInfoId != null && !"".equals(preMemberInfoId.toString()) 
								&& memberInfoId != null && !"".equals(memberInfoId.toString()) 
								&& !preMemberInfoId.toString().equals(memberInfoId.toString())) {
							continue;
						}
						Map<String,Object> campaignOrderDetailMap = new HashMap<String, Object>();
						campaignOrderDetailMap.put("campSubCode", campaignOrderImportMap.get("campSubCode"));
						campaignOrderDetailMap.put("giftType", campaignOrderImportMap.get("giftType"));
						campaignOrderDetailMap.put("unitCode", campaignOrderImportMap.get("unitCode"));
						campaignOrderDetailMap.put("barCode", campaignOrderImportMap.get("barCode"));
						campaignOrderDetailMap.put("quantity", campaignOrderImportMap.get("quantity"));
						detailList.add(campaignOrderDetailMap);
					}
				}
				
				try {
					// 需要添加的会员预约记录List
					List<Map<String, Object>> addCampaignOrderList = new ArrayList<Map<String,Object>>();
					for(int i = 0; i < list.size(); i++) {
						Map<String, Object> campaignOrderMap = list.get(i);
						campaignOrderMap.put(CherryBatchConstants.ORGANIZATIONINFOID, map.get(CherryBatchConstants.ORGANIZATIONINFOID));
						campaignOrderMap.put(CherryBatchConstants.BRANDINFOID, map.get(CherryBatchConstants.BRANDINFOID));
						// 设置新增和更新时的共通参数
						setSysParam(campaignOrderMap);
						// 活动代码
						String campaignCode = (String)campaignOrderMap.get("campaignCode");
						// 预约活动存在的场合需要判断预约记录是否存在，否则直接添加预约记录
						if(campaignCodeList != null && campaignCodeList.contains(campaignCode)) {
							// 取得会员活动预约信息
							Map<String, Object> campaignOrderInfo = binCPMEACT03_Service.getCampaignOrderInfo(campaignOrderMap);
							if(campaignOrderInfo != null) {
								String state = (String)campaignOrderInfo.get("state");
								// 预约单为已领用状态，不进行导入处理
								if(state != null && "OK".equals(state)) {
									campaignOrderMap.put("errorTxt", "预约单已领用");
									// 更新会员活动预约接口表主表为下发失败状态
									binCPMEACT03_Service.updCampOrderErrorTxt(campaignOrderMap);
									// 更新会员活动预约接口表明细表为下发失败状态
									binCPMEACT03_Service.updCampOrderDetailErrorTxt(campaignOrderMap);
									continue;
								}
								String campaignOrderId = campaignOrderInfo.get("campaignOrderId").toString();
								String sendFlag = (String)campaignOrderInfo.get("sendFlag");
								// 下发区分为已下发的场合，更新成需要再次下发
								if(sendFlag != null && "1".equals(sendFlag)) {
									campaignOrderMap.put("sendFlag", "2");
								}
								campaignOrderMap.put("campaignOrderId", campaignOrderId);
								// 更新会员活动预约信息
								binCPMEACT03_Service.updCampaignOrder(campaignOrderMap);
								// 删除会员活动预约明细信息
								binCPMEACT03_Service.delCampaignOrderDetail(campaignOrderMap);
								
								// 取得未下发的会员活动预约明细记录
								List<Map<String, Object>> campaignOrderDetailList = (List)campaignOrderMap.get("list");
								for(Map<String, Object> campaignOrderDetailMap : campaignOrderDetailList) {
									campaignOrderDetailMap.put("campaignOrderId", campaignOrderId);
									// 产品类型
									String giftType = (String)campaignOrderDetailMap.get("giftType");
									// 厂商编码
									String unitCode = (String)campaignOrderDetailMap.get("unitCode");
									// 产品条码
									String barCode = (String)campaignOrderDetailMap.get("barCode");
									// 根据产品类型、厂商编码、产品条码找到产品厂商ID或者促销品厂商ID
									String productVendorId = this.getProductVendorId(giftType, unitCode, barCode);
									if(productVendorId != null && !"".equals(productVendorId)) {
										campaignOrderDetailMap.put("productVendorId", productVendorId);
									} else {
										BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO();
										batchExceptionDTO.setBatchName(this.getClass());
										batchExceptionDTO.setErrorCode("ECP00006");
										batchExceptionDTO.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
										batchExceptionDTO.addErrorParam(unitCode);
										batchExceptionDTO.addErrorParam(barCode);
										throw new CherryBatchException(batchExceptionDTO);
									}
									// 设置新增和更新时的共通参数
									setSysParam(campaignOrderDetailMap);
								}
								// 添加会员活动预约明细信息
								binCPMEACT03_Service.addCampaignOrderDetail(campaignOrderDetailList);
							} else {
								addCampaignOrderList.add(campaignOrderMap);
							}
						} else {
							addCampaignOrderList.add(campaignOrderMap);
						}
					}
					if(!addCampaignOrderList.isEmpty()) {
						// 批量添加会员活动预约信息
						binCPMEACT03_Service.addCampaignOrderList(addCampaignOrderList);
						// 取得会员活动预约信息IDList
						List<Map<String, Object>> campaignOrderIdList = binCPMEACT03_Service.getCampaignOrderIdList(map);
						// 需要添加的会员预约明细List
						List<Map<String, Object>> addCampaignOrderDetailList = new ArrayList<Map<String,Object>>();
						for(int i = 0; i < addCampaignOrderList.size(); i++) {
							Map<String, Object> campaignOrderMap = addCampaignOrderList.get(i);
							String orderId = (String)campaignOrderMap.get("orderId");
							String campaignOrderId = null;
							for(Map<String, Object> campaignOrderIdMap : campaignOrderIdList) {
								String _orderId = (String)campaignOrderIdMap.get("orderId");
								if(orderId.equals(_orderId)) {
									campaignOrderId = campaignOrderIdMap.get("campaignOrderId").toString();
									campaignOrderIdList.remove(campaignOrderIdMap);
									break;
								}
							}
							// 取得未下发的会员活动预约明细记录
							List<Map<String, Object>> campaignOrderDetailList = (List)campaignOrderMap.get("list");
							for(Map<String, Object> campaignOrderDetailMap : campaignOrderDetailList) {
								campaignOrderDetailMap.put("campaignOrderId", campaignOrderId);
								// 产品类型
								String giftType = (String)campaignOrderDetailMap.get("giftType");
								// 厂商编码
								String unitCode = (String)campaignOrderDetailMap.get("unitCode");
								// 产品条码
								String barCode = (String)campaignOrderDetailMap.get("barCode");
								// 根据产品类型、厂商编码、产品条码找到产品厂商ID或者促销品厂商ID
								String productVendorId = this.getProductVendorId(giftType, unitCode, barCode);
								if(productVendorId != null && !"".equals(productVendorId)) {
									campaignOrderDetailMap.put("productVendorId", productVendorId);
								} else {
									BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO();
									batchExceptionDTO.setBatchName(this.getClass());
									batchExceptionDTO.setErrorCode("ECP00006");
									batchExceptionDTO.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
									batchExceptionDTO.addErrorParam(unitCode);
									batchExceptionDTO.addErrorParam(barCode);
									throw new CherryBatchException(batchExceptionDTO);
								}
								// 设置新增和更新时的共通参数
								setSysParam(campaignOrderDetailMap);
							}
							addCampaignOrderDetailList.addAll(campaignOrderDetailList);
						}
						// 添加会员活动预约明细信息
						binCPMEACT03_Service.addCampaignOrderDetail(addCampaignOrderDetailList);
						// 更新会员活动预约信息下发状态
						binCPMEACT03_Service.updCampaignOrderSendFlag(map);
					}
					
					try {
						// 更新会员活动预约接口表主表为下发成功状态
						binCPMEACT03_Service.updCampOrderSuccess(list);
						// 更新会员活动预约接口表明细表为下发成功状态
						binCPMEACT03_Service.updCampOrderDetailSuccess(list);
						binCPMEACT03_Service.manualCommit();
					} catch (Exception e) {
						try {
							// Cherry数据库回滚事务
							binCPMEACT03_Service.manualRollback();
						} catch (Exception ex) {
			
						}
						BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
						batchLoggerDTO.setCode("ECP00009");
						batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
						CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
						cherryBatchLogger.BatchLogger(batchLoggerDTO,e);
						flag = CherryBatchConstants.BATCH_WARNING;
						break;
					}
				} catch (Exception e) {
					try {
						// Cherry数据库回滚事务
						binCPMEACT03_Service.manualRollback();
					} catch (Exception ex) {
						
					}
					BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
					batchLoggerDTO.setCode("ECP00004");
					batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
					CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
					cherryBatchLogger.BatchLogger(batchLoggerDTO,e);
					flag = CherryBatchConstants.BATCH_WARNING;
					try {
						// 更新会员活动预约接口表主表为下发失败状态
						binCPMEACT03_Service.updCampOrderError(list);
						// 更新会员活动预约接口表明细表为下发失败状态
						binCPMEACT03_Service.updCampOrderDetailError(list);
						binCPMEACT03_Service.manualCommit();
					} catch (Exception e2) {
						try {
							// Cherry数据库回滚事务
							binCPMEACT03_Service.manualRollback();
						} catch (Exception ex) {
							
						}
						BatchLoggerDTO batchLoggerDTO1 = new BatchLoggerDTO();
						batchLoggerDTO1.setCode("ECP00009");
						batchLoggerDTO1.setLevel(CherryBatchConstants.LOGGER_ERROR);
						CherryBatchLogger cherryBatchLogger1 = new CherryBatchLogger(this.getClass());
						cherryBatchLogger1.BatchLogger(batchLoggerDTO1,e);
						flag = CherryBatchConstants.BATCH_WARNING;
						break;
					}
				}
				// 会员活动预约记录少于一次抽取的数量，即为最后一页，跳出循环
				if(list.size() < dataSize) {
					break;
				}
			} else {
				break;
			}
		}
		try {
			// 更新会员活动预约接口表主表为待下发状态
			binCPMEACT03_Service.updCampOrderWait(map);
			// 更新会员活动预约接口表明细表为待下发状态
			binCPMEACT03_Service.updCampOrderDetailWait(map);
			binCPMEACT03_Service.manualCommit();
		} catch (Exception e) {
			try {
				// Cherry数据库回滚事务
				binCPMEACT03_Service.manualRollback();
			} catch (Exception ex) {
				
			}
			BatchLoggerDTO batchLoggerDTO1 = new BatchLoggerDTO();
			batchLoggerDTO1.setCode("ECP00009");
			batchLoggerDTO1.setLevel(CherryBatchConstants.LOGGER_ERROR);
			CherryBatchLogger cherryBatchLogger1 = new CherryBatchLogger(this.getClass());
			cherryBatchLogger1.BatchLogger(batchLoggerDTO1,e);
			flag = CherryBatchConstants.BATCH_WARNING;
		}
	}
	
	/**
	 * 导入单个会员活动预约信息
	 * 
	 * @param campaignOrderMap 会员活动预约信息
	 */
	public void importCampaignOrderOne(Map<String, Object> campaignOrderMap) throws Exception {
		
		// 设置新增和更新时的共通参数
		setSysParam(campaignOrderMap);
		// 活动代码
		String campaignCode = (String)campaignOrderMap.get("campaignCode");
		// 会员活动预约ID
		String campaignOrderId = null;
		// 预约活动存在的场合需要判断预约记录是否存在，否则直接添加预约记录
		if(campaignCodeList != null && campaignCodeList.contains(campaignCode)) {
			// 取得会员活动预约信息
			Map<String, Object> campaignOrderInfo = binCPMEACT03_Service.getCampaignOrderInfo(campaignOrderMap);
			if(campaignOrderInfo != null) {
				String state = (String)campaignOrderInfo.get("state");
				// 预约单为已领用状态，不进行导入处理
				if(state != null && "OK".equals(state)) {
					campaignOrderMap.put("errorTxt", "预约单已领用");
					// 更新会员活动预约接口表主表为下发失败状态
					binCPMEACT03_Service.updCampOrderErrorTxt(campaignOrderMap);
					// 更新会员活动预约接口表明细表为下发失败状态
					binCPMEACT03_Service.updCampOrderDetailErrorTxt(campaignOrderMap);
					return;
				}
				campaignOrderId = campaignOrderInfo.get("campaignOrderId").toString();
				String sendFlag = (String)campaignOrderInfo.get("sendFlag");
				// 下发区分为已下发的场合，更新成需要再次下发
				if(sendFlag != null && "1".equals(sendFlag)) {
					campaignOrderMap.put("sendFlag", "2");
				}
				campaignOrderMap.put("campaignOrderId", campaignOrderId);
				// 更新会员活动预约信息
				binCPMEACT03_Service.updCampaignOrder(campaignOrderMap);
				// 删除会员活动预约明细信息
				binCPMEACT03_Service.delCampaignOrderDetail(campaignOrderMap);
			} else {
				// 添加会员活动预约信息
				campaignOrderId = String.valueOf(binCPMEACT03_Service.addCampaignOrder(campaignOrderMap));
				campaignOrderMap.put("campaignOrderId", campaignOrderId);
			}
		} else {
			// 添加会员活动预约信息
			campaignOrderId = String.valueOf(binCPMEACT03_Service.addCampaignOrder(campaignOrderMap));
			campaignOrderMap.put("campaignOrderId", campaignOrderId);
		}

		// 取得未下发的会员活动预约明细记录
		List<Map<String, Object>> campaignOrderDetailList = (List)campaignOrderMap.get("list");
		for(Map<String, Object> campaignOrderDetailMap : campaignOrderDetailList) {
			campaignOrderDetailMap.put("campaignOrderId", campaignOrderId);
			// 产品类型
			String giftType = (String)campaignOrderDetailMap.get("giftType");
			// 厂商编码
			String unitCode = (String)campaignOrderDetailMap.get("unitCode");
			// 产品条码
			String barCode = (String)campaignOrderDetailMap.get("barCode");
			// 根据产品类型、厂商编码、产品条码找到产品厂商ID或者促销品厂商ID
			String productVendorId = this.getProductVendorId(giftType, unitCode, barCode);
			if(productVendorId != null && !"".equals(productVendorId)) {
				campaignOrderDetailMap.put("productVendorId", productVendorId);
			} else {
				BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO();
				batchExceptionDTO.setBatchName(this.getClass());
				batchExceptionDTO.setErrorCode("ECP00006");
				batchExceptionDTO.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
				batchExceptionDTO.addErrorParam(unitCode);
				batchExceptionDTO.addErrorParam(barCode);
				throw new CherryBatchException(batchExceptionDTO);
			}
			// 设置新增和更新时的共通参数
			setSysParam(campaignOrderDetailMap);
		}
		// 添加会员活动预约明细信息
		binCPMEACT03_Service.addCampaignOrderDetail(campaignOrderDetailList);
		
	}
	
	/**
	 * 取得产品厂商ID或者促销品厂商ID
	 * 
	 * @param type 产品类型
	 * @param unitCode 厂商编码
	 * @param barCode 产品条码
	 * @return 产品厂商ID或者促销品厂商ID
	 */
	public String getProductVendorId(String type, String unitCode, String barCode) {
		if("N".equals(type)) {
			for(Map<String, Object> productMap : productList) {
				String _barCode = (String)productMap.get("barCode");
				String _unitCode = (String)productMap.get("unitCode");
				if(barCode.equals(_barCode) && unitCode.equals(_unitCode)) {
					return productMap.get("productVendorId").toString();
				}
			}
		} else {
			for(Map<String, Object> promotionPrtMap : promotionPrtList) {
				String _barCode = (String)promotionPrtMap.get("barCode");
				String _unitCode = (String)promotionPrtMap.get("unitCode");
				if(barCode.equals(_barCode) && unitCode.equals(_unitCode)) {
					return promotionPrtMap.get("promotionProductVendorId").toString();
				}
			}
		}
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("unitCode", unitCode);
		map.put("barCode", barCode);
		if("N".equals(type)) {
			String productVendorId = binCPMEACT03_Service.getProductVendorId(map);
			if(productVendorId != null && !"".equals(productVendorId)) {
				map.put("productVendorId", productVendorId);
				productList.add(map);
			}
			return productVendorId;
		} else {
			String promotionProductVendorId = binCPMEACT03_Service.getPromotionProductVendorId(map);
			if(promotionProductVendorId != null && !"".equals(promotionProductVendorId)) {
				map.put("promotionProductVendorId", promotionProductVendorId);
				promotionPrtList.add(map);
			}
			return promotionProductVendorId;
		}
	}
	
	/**
	 * 设置新增和更新时的共通参数
	 * 
	 * @param map 需要新增和更新的map对象
	 */
	public void setSysParam(Map<String, Object> map) {
		map.put("createdBy", "BATCH");
		map.put("createPGM", "BATCH");
		map.put("updatedBy", "BATCH");
		map.put("updatePGM", "BATCH");
	}

}
