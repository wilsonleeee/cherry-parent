/*	
 * @(#)BINBAT122_BL.java     1.0 @2015-10-13		
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
package com.cherry.middledbout.stand.product.bl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.batcmbussiness.interfaces.BINBECM01_IF;
import com.cherry.cm.cmbussiness.bl.BINOLCM05_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM15_BL;
import com.cherry.cm.core.BatchExceptionDTO;
import com.cherry.cm.core.BatchLoggerDTO;
import com.cherry.cm.core.CherryBatchChecker;
import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryBatchException;
import com.cherry.cm.core.CherryBatchLogger;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.CherryBatchUtil;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.DateUtil;
import com.cherry.ia.common.ProductConstants;
import com.cherry.middledbout.stand.product.service.BINBAT122_Service;


/**
 * 
 * 产品导入(标准接口)(IF_Product)BL
 * @author jijw
 *  * @version 1.0 2015.10.13
 *
 */
public class BINBAT122_BL {
	
	/** 打印当前类的日志信息 **/
	private static CherryBatchLogger logger = new CherryBatchLogger(BINBAT122_BL.class);
	
	/** BATCH处理标志 */
	private int flag = CherryBatchConstants.BATCH_SUCCESS;
	
	/** JOB执行相关共通 IF */
	@Resource(name="binbecm01_IF")
	private BINBECM01_IF binbecm01_IF;
	
	@Resource
	private BINBAT122_Service binBAT122_Service;
	
	/** 各类编号取号共通BL */
	@Resource(name="binOLCM15_BL")
	private BINOLCM15_BL binOLCM15_BL;
	
	/** 系统配置项 共通BL */
	@Resource(name="binOLCM14_BL")
	private BINOLCM14_BL binOLCM14_BL;
	
	@Resource(name="binOLCM05_BL")
	private BINOLCM05_BL binOLCM05_BL;
	
	/** 每批次(页)处理数量 1000 */
	private final int BATCH_SIZE = 1000;
	
	private Map<String, Object> comMap;
	
	/** 更新添加操作flag */
	private int optFlag = 0;
	
	/** 产品大分类ID */
	private int prtCatPropId_B = 0;
	/** 产品中分类ID */
	private int prtCatPropId_M = 0;
	/** 产品小分类ID */
	private int prtCatPropId_L = 0;
	
	/** 处理总条数 */
	private int totalCount = 0;
	/** 插入条数 */
	private int insertCount = 0;
	/** 更新条数 */
	private int updateCount = 0;
	/** 失败条数 */
	private int failCount = 0;
	
	/** 导入失败的itemCode */
	private List<String> faildItemList = new ArrayList<String>();
	
	/** 失败的主要原因，受字段长度限制，这里只要记录主要原因即可 */
	private String fReason = new String();
	private StringBuffer fReasonBuffer = new StringBuffer();

	/**
	 * batch处理
	 * 
	 * @param 无
	 * 
	 * @return Map
	 * @throws CherryBatchException
	 */
	@SuppressWarnings("unchecked")
	public int tran_batchExec(Map<String, Object> paraMap) throws CherryBatchException,Exception {
		
		// 初始化(包括取得大中小类)
		try{
			init(paraMap);
		}catch(Exception e){
			// 初始化失败
			BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO();
			batchExceptionDTO.setBatchName(this.getClass());
			batchExceptionDTO.setErrorCode("ECM00005");
			batchExceptionDTO.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
			batchExceptionDTO.setException(e);
			flag = CherryBatchConstants.BATCH_ERROR;
			throw new CherryBatchException(batchExceptionDTO);
		}
		
		// 处理之前失败的产品信息数据
		handleFaildData(paraMap);
		
		// 处理符合条件的产品信息数据
		handleNormalData(paraMap);
		
		// 日志
		outMessage();
		
		// 程序结束时，处理Job共通(更新Job控制表 、插入Job运行履历表)
		programEnd(paraMap);
		
		
		return flag;
		
	}

	/**
	 * 处理符合条件的产品信息数据
	 * @param paraMap
	 * @throws CherryBatchException
	 * @throws Exception
	 */
	private void handleNormalData(Map<String, Object> paraMap) throws CherryBatchException, Exception {
		
		// 上一批次(页)最后一条itemCode
		String bathLastIFProductId = "";
		
		while (true) {

			// 查询接口产品列表
			Map<String, Object> paraMap2 = new HashMap<String, Object>();
			paraMap2.putAll(paraMap);
			paraMap2.put("batchSize", BATCH_SIZE);
			paraMap2.put("bathLastIFProductId", bathLastIFProductId);
			List<Map<String, Object>> itemList = binBAT122_Service.getStandardProductList(paraMap2);
			if (CherryBatchUtil.isBlankList(itemList)) {
				break;
			} else {
				try {
					// 备注标准产品接口数据到履历表
					binBAT122_Service.backupItems(itemList);
					binBAT122_Service.manualCommit();
				} catch (Exception e) {
					binBAT122_Service.manualRollback();
					BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
					batchLoggerDTO.setCode("EOT00012");
					batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
					logger.BatchLogger(batchLoggerDTO, e);
					// 备份失败后，跳转至下一批次(页)
					continue;
				}
			}
			
			 // 当前批次最后一个产品的ItemCode赋给bathLastIFProductId，用于当前任务下一批次(页)产品数据的筛选条件
			 bathLastIFProductId = CherryBatchUtil.getString(itemList.get(itemList.size()- 1).get("IFProductId"));
			 // 统计总条数
			 totalCount += itemList.size();
			 // 更新新后台数据库产品相关表
			 updateBackEnd(itemList,paraMap,1);
			 // 接口产品列表为空或产品数据少于一批次(页)处理数量，跳出循环
			 if (itemList.size() < BATCH_SIZE) {
				 break;
			 }
		}
	}
	
	/**
	 * 处理之前失败的产品数据
	 * 
	 * @param paraMap
	 * @throws CherryBatchException,Exception 
	 */
	@SuppressWarnings("unchecked")
	private void handleFaildData(Map<String, Object> paraMap) throws CherryBatchException,Exception{
		// 上一批次(页)最后一条记录
		String bathLastIFProductId = "";
		
		while (true) {
			
			// 查询失败的产品数据履历
			Map<String, Object> selPrtStockMap = new HashMap<String, Object>();
			selPrtStockMap.putAll(comMap);
			selPrtStockMap.put("batchSize", BATCH_SIZE);
			selPrtStockMap.put("bathLastIFProductId", bathLastIFProductId);
			List<Map<String, Object>> jobRunFaildHistoryList = binBAT122_Service.getJobRunFaildHistoryList(selPrtStockMap);
			
			if (CherryBatchUtil.isBlankList(jobRunFaildHistoryList)) {
				break;
			} else {
				// 取得失败的标准产品列表
				selPrtStockMap.put("jobRunFaildHistoryList", jobRunFaildHistoryList);
				List<Map<String, Object>> faildPrtList = binBAT122_Service.getFaildProductList(selPrtStockMap);
				
				// 统计总条数
				totalCount += faildPrtList.size();
				try{
					// 处理前面失败的产品数据
					updateBackEnd(faildPrtList,paraMap,0);
					
				}catch(Exception e){
					
					flag = CherryBatchConstants.BATCH_ERROR;
					
					failCount += faildPrtList.size();
					
					// 待定
//					binbekdcpi01_Service.manualRollback();
					
					BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
					batchLoggerDTO.setCode("EOT00072");
					batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
					logger.BatchLogger(batchLoggerDTO, e);
					
					// 处理失败后，跳转至下一批次(页)
//					continue;
				}
				
			}
			
			// 当前批次最后一条数据的RowID赋给bathLastRowID，用于当前任务下一批次(页)标准产品信息数据的筛选条件
			bathLastIFProductId = CherryBatchUtil.getString(jobRunFaildHistoryList.get(jobRunFaildHistoryList.size()- 1));
			
			// 标准产品信息为空或少于一批次(页)处理数量，跳出循环
			if (jobRunFaildHistoryList.size() < BATCH_SIZE) {
				break;
			}
		}
	}
	
	/**
	 * 更新新后台数据库产品相关表
	 * @param itemList
	 */
	
	
	/**
	 * 
	 * @param itemList 产品集合
	 * @param map 参数
	 * @param dataClass 数据类别 1：正常数据 0：失败数据
	 * @throws Exception
	 */
	private void updateBackEnd(List<Map<String, Object>> itemList,Map<String,Object> map, int dataClass) throws Exception {
		// 删除成功导入的产品
		List<Map<String, Object>> delExportItemList = new ArrayList<Map<String,Object>>();
		
		// 取得当前产品表版本号
		Map<String, Object> seqMap = new HashMap<String, Object>();
		seqMap.putAll(map);
		seqMap.put("type", "E");
		String prtTVersion = binOLCM15_BL.getCurrentSequenceId(seqMap);
		comMap.put("prtTVersion", prtTVersion);
		
		// 取得当前部门(柜台)产品表版本号
		seqMap.put("type", "F");
		String pdTVersion = binOLCM15_BL.getCurrentSequenceId(seqMap);
		comMap.put("pdTVersion", pdTVersion);
		
		for (Map<String, Object> itemMap : itemList) {
			
			itemMap = CherryBatchUtil.removeEmptyVal(itemMap);
			// 数据字符截断转换等非SQL处理
			screenMap(itemMap);
			itemMap.putAll(comMap);
			
			try {
				
				// 接口中的产品有效区分
				String validFlag = CherryBatchUtil.getString(itemMap.get("ValidFlag")); // ValidFlag 1：停用 0：启用
				
				int oldPrtId = binBAT122_Service.searchProductId(itemMap);
				
				// 导入来的产品是停用的,用以下方式处理
				if("0".equals(validFlag)){
//				if("1".equals(fDeleteD)){
					if(oldPrtId == 0){
						// 如果是新增的产品，且产品为无效，就不导入到新后台
						continue;
					}else{
						
						try{
							// 更新时，停用新后台产品  
							itemMap.put(ProductConstants.PRODUCT_ID, oldPrtId);
							Map<String, Object> prtVendorMap = binBAT122_Service.getProductVendorInfo(itemMap);
							int proVendorId = (Integer)prtVendorMap.get("proVendorId");
							itemMap.put("prtVendorId", proVendorId);
							
							// 删除无效的产品数据
							binBAT122_Service.delInvalidProducts(itemMap);
							// 删除无效的产品厂商数据
							binBAT122_Service.delInvalidProVendors(itemMap);
							// 更新停用日时
							binBAT122_Service.updateClosingTime(itemMap);
							
							// 更新件数加一
							updateCount += 1;
							
							continue;
						} catch(Exception ex){
							BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO();
							batchExceptionDTO.setBatchName(this.getClass());
							batchExceptionDTO.setErrorCode("EOT00099");
							batchExceptionDTO
									.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
							// 产品唯一性编码
							batchExceptionDTO.addErrorParam(CherryBatchUtil
									.getString(itemMap.get("IFProductId")));
							// 中文名
							batchExceptionDTO.addErrorParam(CherryBatchUtil
									.getString(itemMap.get("ProductNameCN")));
							batchExceptionDTO.setException(ex);
							throw new CherryBatchException(batchExceptionDTO);
						}

					}
				}
				
				itemMap.put("oldPrtId", oldPrtId);
				// 保存或更新产品信息
				saveOrUpdPro(itemMap);
//				saveOrUpdProNew(itemMap);
				
				// 更新产品价格信息
				updPrtPrice(itemMap);
				// 更新产品条码信息
				updProVendor(itemMap);
				// 更新分类信息
				updPrtCategory(itemMap);
			
				binBAT122_Service.manualCommit();
				// 记载成功导入新后台的接口产品数据
				delExportItemList.add(itemMap);
				
				// 如果当前处理的是Job运行失败履历表的数据，那么当前如果成功就将其从运行失败履历表中删除
				if( 0 == dataClass){
					// 设置失败履历表的参数
					Map<String,Object> falidMap = new HashMap<String, Object>();
					falidMap.putAll(comMap);
					falidMap.put("UnionIndex", itemMap.get("IFProductId"));
					binbecm01_IF.delJobRunFaildHistory(falidMap);
				}
				
			} catch (Exception e) {
				binBAT122_Service.manualRollback();
				// 记载导入新后台失败的ItemCode
				faildItemList.add(CherryBatchUtil.getString(itemMap.get("IFProductId")));
				// 失败件数加一
				failCount += 1;
				flag = CherryBatchConstants.BATCH_WARNING;
				
				try {
					// 设置失败履历表的参数
					Map<String,Object> falidMap = new HashMap<String, Object>();
					falidMap.putAll(comMap);
					falidMap.put("UnionIndex", itemMap.get("IFProductId"));
					
					falidMap.put("ErrorMsg", ",{\"" + e.getMessage() + "\"}");
					binbecm01_IF.mergeJobRunFaildHistory(falidMap);
					
						
				}catch(Exception ex){
					BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
					batchLoggerDTO.setCode("EOT00098");
					batchLoggerDTO.addParam(itemMap.toString()); // 请求接口
					batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
					logger.BatchLogger(batchLoggerDTO);
				}
			}
		}
		
	}
	
	/**
	 * 更新或插入产品信息
	 * 
	 * @param map
	 * @return
	 */
	private void saveOrUpdPro(Map<String, Object> itemMap)
			throws CherryBatchException,Exception {
		
		// 查询产品ID
		int productId = 0;
//		int oldPrtId = binbekdpro01_Service.searchProductId(itemMap);
		int oldPrtId = (Integer)itemMap.get("oldPrtId");
		
		// ************************** 产品编码唯一性验证 **************************
		
		Map<String,Object> prtMap = new HashMap<String, Object>();
		prtMap.putAll(itemMap);
		if(oldPrtId != 0){
			prtMap.put(ProductConstants.PRODUCT_ID, oldPrtId);
		}
		
		try {
			prtMap.put("unitCode", itemMap.get("UnitCode"));
			int unitCodeCount = binOLCM05_BL.getExistUnitCodeForPrtAndProm(prtMap);	
			
			if(unitCodeCount != 0){
				throw new CherryBatchException();
			}
			
		} catch(CherryBatchException e){
			BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO();
			batchExceptionDTO.setBatchName(this.getClass());
			batchExceptionDTO.setException(e);
			batchExceptionDTO.setErrorCode("EOT00054");
			batchExceptionDTO.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
			// 产品唯一性编码K3Product
			batchExceptionDTO.addErrorParam(CherryBatchUtil .getString(itemMap.get("IFProductId")));
			// 厂商编码
			batchExceptionDTO.addErrorParam(CherryBatchUtil .getString(itemMap.get("UnitCode")));
			// 产品条码
			batchExceptionDTO.addErrorParam(CherryBatchUtil .getString(itemMap.get("BarCode")));
			// 中文名
			batchExceptionDTO.addErrorParam(CherryBatchUtil .getString(itemMap.get("ProductNameCN")));
			throw new CherryBatchException(batchExceptionDTO);
		}
		
		// ************************** 产品编码唯一性验证 **************************
		
		// ********************** 验证当前barCode在促销品中不存在   **********************
		try{
			// 查询促销品中是否存在当前需要添加的barCode
			prtMap.put("barCode", itemMap.get("BarCode"));
			List<Integer> promPrtIdList = binOLCM05_BL.getPromotionIdByBarCode(prtMap);
			if(promPrtIdList.size() != 0){
				throw new CherryBatchException();
			}
			
		} catch(CherryBatchException e){
			BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO();
			batchExceptionDTO.setBatchName(this.getClass());
			batchExceptionDTO.setErrorCode("EOT00055");
			batchExceptionDTO.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
			// 产品唯一性编码K3Product
			batchExceptionDTO.addErrorParam(CherryBatchUtil .getString(itemMap.get("IFProductId")));
			// 厂商编码
			batchExceptionDTO.addErrorParam(CherryBatchUtil .getString(itemMap.get("UnitCode")));
			// 产品条码
			batchExceptionDTO.addErrorParam(CherryBatchUtil .getString(itemMap.get("BarCode")));
			// 中文名
			batchExceptionDTO.addErrorParam(CherryBatchUtil .getString(itemMap.get("ProductNameCN")));
			throw new CherryBatchException(batchExceptionDTO);
		}
		
		// ********************** 验证当前barCode在促销品中不存在   **********************
		
		if (0 == oldPrtId) {
			// 插入操作
			optFlag = 1;
			try {
				// 插入产品信息
				productId = binBAT122_Service.insertProductInfo(itemMap);
				// 把产品Id设入条件map
				itemMap.put(ProductConstants.PRODUCT_ID, productId);
				// 插入件数加一
				insertCount += 1;
			} catch (Exception e) {
				BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO();
				batchExceptionDTO.setBatchName(this.getClass());
				batchExceptionDTO.setErrorCode("EOT00013");
				batchExceptionDTO
						.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
				// 产品唯一性编码
				batchExceptionDTO.addErrorParam(CherryBatchUtil
						.getString(itemMap.get("IFProductId")));
				// 中文名
				batchExceptionDTO.addErrorParam(CherryBatchUtil
						.getString(itemMap.get("ProductNameCN")));
				batchExceptionDTO.setException(e);
				throw new CherryBatchException(batchExceptionDTO);
			}
		} else {
			// 更新操作
			optFlag = 2;
			productId = oldPrtId;
			itemMap.put(ProductConstants.PRODUCT_ID, productId);
			try {
				// 更新产品信息表
				binBAT122_Service.updateProductInfo(itemMap);
				
				// 产品变动后更新产品方案明细表的version字段
//				binbeifpro01Service.updPrtSolutionDetail(itemMap);
				// 更新件数加一
				updateCount += 1;
			} catch (Exception e) {
				BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO();
				batchExceptionDTO.setBatchName(this.getClass());
				batchExceptionDTO.setErrorCode("EOT00014");
				batchExceptionDTO
						.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
				// 产品唯一性编码
				batchExceptionDTO.addErrorParam(CherryBatchUtil
						.getString(itemMap.get("IFProductId")));
				// 中文名
				batchExceptionDTO.addErrorParam(CherryBatchUtil
						.getString(itemMap.get("ProductNameCN")));
				batchExceptionDTO.setException(e);
				throw new CherryBatchException(batchExceptionDTO);
			}
		}
	}
	
	/**
	 * 更新产品价格信息
	 * 
	 * @param map
	 * @throws CherryBatchException
	 */
	private void updPrtPrice(Map<String, Object> itemMap)
			throws CherryBatchException {
		
		Map<String, Object> tempMap = new HashMap<String, Object>();
		tempMap.putAll(itemMap);
		
		if (!CherryBatchChecker.isNull(tempMap.get("SalePrice"))) {
			try {
					
				// 新产品
				if(1 == optFlag){
					// 默认产品价格生效日期
					tempMap.put(ProductConstants.STRAT_DATE, tempMap.get("businessDate"));
					// 默认产品价格失效日期
					tempMap.put(ProductConstants.END_DATE,ProductConstants.DEFAULT_END_DATE);
					// 插入新的产品价格信息
					binBAT122_Service.insertProductPrice(tempMap);
					
				} 
				// 老产品
				else {
					// 查询产品价格是否修改
//					String productPriceID = binBAT122_Service.selProductPrice(tempMap);
					
					// 产品价格已修改
//					if(null == productPriceID){
						// 默认产品价格生效日期
						tempMap.put(ProductConstants.STRAT_DATE, tempMap.get("businessDate"));
						binBAT122_Service.updProductPrice(tempMap);
//					}
				}
				
			} catch (Exception e) {
				if (optFlag == 1) {
					insertCount--;
				} else if (optFlag == 2) {
					updateCount--;
				}
				BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO();
				batchExceptionDTO.setBatchName(this.getClass());
				batchExceptionDTO.setErrorCode("EOT00015");
				batchExceptionDTO
						.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
				// 产品唯一性编码
				batchExceptionDTO.addErrorParam(CherryBatchUtil
						.getString(tempMap.get("IFProductId")));
				// 中文名
				batchExceptionDTO.addErrorParam(CherryBatchUtil
						.getString(tempMap.get("ProductNameCN")));
				batchExceptionDTO.setException(e);
				throw new CherryBatchException(batchExceptionDTO);
			}
		}
	}
	
	/**
	 * 更新产品条码信息
	 * 
	 * @param itemMap
	 * @throws CherryBatchException
	 */
	private void updProVendor(Map<String, Object> itemMap)
			throws CherryBatchException {
		
		Map<String, Object> tempMap = new HashMap<String, Object>();
		tempMap.putAll(itemMap);
		
		try {
			
			if(1 == optFlag){
				// 添加产品厂商信息
				binBAT122_Service.insertProductVendor(tempMap);
			} 
			else if(2 == optFlag){
				// 编辑产品厂商信息
				Map<String, Object> prtVendorMap = binBAT122_Service.getProductVendorInfo(tempMap);
				int proVendorId = (Integer)prtVendorMap.get("proVendorId");
				tempMap.put("prtVendorId", proVendorId);
				binBAT122_Service.updPrtVendor(tempMap);
				// 更新产品条码对应关系信息
				binBAT122_Service.updPrtBarCode(tempMap);
			}
			
		} catch (Exception e) {
			if (optFlag == 1) {
				insertCount--;
			} else if (optFlag == 2) {
				updateCount--;
			}
			BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO();
			batchExceptionDTO.setBatchName(this.getClass());
			batchExceptionDTO.setErrorCode("EOT00016");
			batchExceptionDTO.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
			// 产品唯一性编码
			batchExceptionDTO.addErrorParam(CherryBatchUtil
					.getString(tempMap.get("IFProductId")));
			// 厂商编码
			batchExceptionDTO.addErrorParam(CherryBatchUtil.getString(tempMap
					.get("UnitCode")));
			// 产品条码
			batchExceptionDTO.addErrorParam(CherryBatchUtil.getString(tempMap
					.get("BarCode")));
			batchExceptionDTO.setException(e);
			throw new CherryBatchException(batchExceptionDTO);
		}
	}
	
	
	/**
	 * 数据截断转换等非SQL处理
	 * @param itemMap
	 */
	private void screenMap(Map<String, Object> itemMap){
		
		// 截取产品名称(中英混合字符最大40--兼容老后台varchar数据类型) 
		String itemDesc = CherryBatchUtil.getString(itemMap.get("ProductNameCN"));
		String nameRule = binOLCM14_BL.getConfigValue("1132", ConvertUtil.getString(comMap.get(CherryBatchConstants.ORGANIZATIONINFOID)),ConvertUtil.getString(comMap.get(CherryBatchConstants.BRANDINFOID)));
		int nameRuleLen = Integer.parseInt(nameRule);
		itemDesc = CherryBatchUtil.mixStrsub(itemDesc, nameRuleLen); // 使用系统配置项控制产品名称长度
		
		// 接口表Status字段转换
		String validFlag = CherryBatchUtil.getString(itemMap.get("ValidFlag"));
		if("1".equals(validFlag)){
			itemMap.put("Status", "E"); 
		}else if("0".equals(validFlag)){
			itemMap.put("Status", "D");
		}
		
		// 产品类型，N：一般产品 P：促销品  替代Category
		String mode = CherryBatchUtil.getString(itemMap.get("Mode"));
		if(CherryBatchUtil.isBlankString(mode)){
			itemMap.put("Mode", "N");
		}
		
		// 是否积分兑换 0：不可 1：可以  默认为1
		String isExchanged = CherryBatchUtil.getString(itemMap.get("isExchanged"));
		if(CherryBatchUtil.isBlankString(isExchanged)){
			itemMap.put("isExchanged", "1");
		}
		
	}
	
	/**
	 * 更新产品分类
	 * 
	 * @param itemMap
	 */
	/*private void updPrtCategory(Map<String, Object> itemMap)
			throws CherryBatchException {
		
		// 删除产品分类属性关系信息
		binBAT122_Service.delPrtCategory(itemMap);
		// 添加产品分类关系(大分类)
		String fnumber1 = ConvertUtil.getString(itemMap.get("ClassCode1"));
		String fName1 = ConvertUtil.getString(itemMap.get("ClassName1"));
		if(!CherryBatchUtil.isBlankString(fnumber1) && !CherryBatchUtil.isBlankString(fName1) ){
			addPrtcategory(itemMap, ProductConstants.CATE_TYPE_1);
		}
		
		// 添加产品分类关系(中分类)
		String fnumber2 = ConvertUtil.getString(itemMap.get("ClassCode2"));
		String fName2 = ConvertUtil.getString(itemMap.get("ClassName2"));
		if(!CherryBatchUtil.isBlankString(fnumber2) && !CherryBatchUtil.isBlankString(fName2) ){
			addPrtcategory(itemMap, ProductConstants.CATE_TYPE_3);
		}
		
		// 添加产品分类关系(小分类)
		String fnumber3 = ConvertUtil.getString(itemMap.get("ClassCode3"));
		String fName3 = ConvertUtil.getString(itemMap.get("ClassName3"));
		if(!CherryBatchUtil.isBlankString(fnumber3) && !CherryBatchUtil.isBlankString(fName3) ){
			addPrtcategory(itemMap, ProductConstants.CATE_TYPE_2);
		}
		
		
		// 添加产品动态分类关系D1（U_SubCategory）
		addPrtcategoryDynaCate(itemMap, "D1");
		// 添加产品动态分类关系D2（U_LineMF）
		addPrtcategoryDynaCate(itemMap, "D2");
		// 添加产品动态分类关系D3（U_LineCategory）
		addPrtcategoryDynaCate(itemMap, "D3");
		
		
	}
	*/

	/**
	 * 更新产品分类
	 * 
	 * @param itemMap
	 */
	private void updPrtCategory(Map<String, Object> itemMap)
			throws CherryBatchException {
		List<Map<String, Object>> tempList = new ArrayList<Map<String, Object>>();
		// 添加产品分类关系(大分类)
		String fName1 = ConvertUtil.getString(itemMap.get("ClassName1"));
		if(!CherryBatchUtil.isBlankString(fName1) ){
			Map<String, Object> temp = addPrtcategory(itemMap, ProductConstants.CATE_TYPE_1);			
			int catPropValID = CherryBatchUtil.Object2int(temp.get(ProductConstants.CATPROPVALID));
			if (catPropValID > 0) {
				tempList.add(temp);
			}
		}	
		// 添加产品分类关系(中分类)
		String fName2 = ConvertUtil.getString(itemMap.get("ClassName2"));
		if(!CherryBatchUtil.isBlankString(fName2) ){
			Map<String, Object> temp = addPrtcategory(itemMap, ProductConstants.CATE_TYPE_3);			
			int catPropValID = CherryBatchUtil.Object2int(temp.get(ProductConstants.CATPROPVALID));
			if (catPropValID > 0) {
				tempList.add(temp);
			}
		}		
		// 添加产品分类关系(小分类)
		String fName3 = ConvertUtil.getString(itemMap.get("ClassName3"));
		if(!CherryBatchUtil.isBlankString(fName3) ){
			Map<String, Object> temp = addPrtcategory(itemMap, ProductConstants.CATE_TYPE_2);			
			int catPropValID = CherryBatchUtil.Object2int(temp.get(ProductConstants.CATPROPVALID));
			if (catPropValID > 0) {
				tempList.add(temp);
			}
		}
		
		if (!tempList.isEmpty()) {
			// 删除产品分类属性关系信息
			binBAT122_Service.delPrtCategory(itemMap);
			// 循环插入
			for(Map<String,Object> temp:tempList) {
				// 插入产品分类关系表
				binBAT122_Service.insertPrtCategory(temp);
			}
		}
	}
	/**
	 * 添加产品分类关系
	 * 
	 * @param itemMap
	 * @param cateType
	 */
	private Map<String, Object> addPrtcategory(Map<String, Object> itemMap, String cateType)
			throws CherryBatchException {
		Map<String, Object> temp = new HashMap<String, Object>();
		temp.putAll(comMap);
		// 产品ID
		temp.put(ProductConstants.PRODUCT_ID,itemMap.get(ProductConstants.PRODUCT_ID));
		// 分类类型
		temp.put(ProductConstants.CATE_TYPE, cateType);
		// 大分类
		if (ProductConstants.CATE_TYPE_1.equals(cateType)) {
			// 大分类ID
			temp.put(ProductConstants.PRTCATPROPID, prtCatPropId_B);
			// 大分类属性值
			temp.put(ProductConstants.PROPVALUECHERRY,itemMap.get("ClassCode1"));
			// 大分类属性名称
			temp.put(ProductConstants.PROPVALUE_CN,itemMap.get("ClassName1"));
		} else if (ProductConstants.CATE_TYPE_3.equals(cateType)) {
			// 中分类ID
			temp.put(ProductConstants.PRTCATPROPID, prtCatPropId_M);
			// 中分类属性值
			temp.put(ProductConstants.PROPVALUECHERRY,itemMap.get("ClassCode2"));
			// 中分类属性名称q
			temp.put(ProductConstants.PROPVALUE_CN,itemMap.get("ClassName2"));
		} else if (ProductConstants.CATE_TYPE_2.equals(cateType)) {
			// 小分类ID
			temp.put(ProductConstants.PRTCATPROPID, prtCatPropId_L);
			// 小分类属性值
			temp.put(ProductConstants.PROPVALUECHERRY,itemMap.get("ClassCode3"));
			// 小分类属性名称
			temp.put(ProductConstants.PROPVALUE_CN,itemMap.get("ClassName3"));
		}
		try {
			// 取得分类属性值ID 
			int catePropValId = getCatPropValId(temp);
			// 基本属性预设值ID
			temp.put(ProductConstants.CATPROPVALID, catePropValId);
			
			if (catePropValId > 0) {
				// 名称重复
			} else if (catePropValId == -1) {
				BatchLoggerDTO processLogger = new BatchLoggerDTO();
				processLogger.setCode("IPT00008");
				processLogger.setLevel(CherryBatchConstants.LOGGER_INFO);
				processLogger.addParam(String.valueOf(itemMap.get(ProductConstants.PRODUCT_ID)));
				// 大分类
				if (ProductConstants.CATE_TYPE_1.equals(cateType)) {
					processLogger.addParam(ProductConstants.CATE_NAME_B);
					
				} else if (ProductConstants.CATE_TYPE_3.equals(cateType)) {
					processLogger.addParam(ProductConstants.CATE_NAME_M);
					
				} else if (ProductConstants.CATE_TYPE_2.equals(cateType)) {
					processLogger.addParam(ProductConstants.CATE_NAME_L);
				}
				processLogger.addParam(String.valueOf(temp.get(ProductConstants.PROPVALUE_CN)));
				processLogger.addParam(String.valueOf(temp.get(ProductConstants.PROPVALUECHERRY)));
				logger.BatchLogger(processLogger);
				// 名称和值不整合
			} else if (catePropValId == -2) {
				BatchLoggerDTO processLogger = new BatchLoggerDTO();
				processLogger.setCode("IPT00009");
				processLogger.setLevel(CherryBatchConstants.LOGGER_INFO);
				processLogger.addParam(String.valueOf(itemMap.get(ProductConstants.PRODUCT_ID)));
				// 大分类
				if (ProductConstants.CATE_TYPE_1.equals(cateType)) {
					processLogger.addParam(ProductConstants.CATE_NAME_B);
					
				} else if (ProductConstants.CATE_TYPE_3.equals(cateType)) {
					processLogger.addParam(ProductConstants.CATE_NAME_M);
					
				} else if (ProductConstants.CATE_TYPE_2.equals(cateType)) {
					processLogger.addParam(ProductConstants.CATE_NAME_L);
				}
				processLogger.addParam(String.valueOf(temp.get(ProductConstants.PROPVALUE_CN)));
				processLogger.addParam(String.valueOf(temp.get(ProductConstants.PROPVALUECHERRY)));
				logger.BatchLogger(processLogger);
			}
		} catch (Exception e) {
			if (optFlag == 1) {
				insertCount--;
			} else if (optFlag == 2) {
				updateCount--;
			}
			BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO();
			batchExceptionDTO.setBatchName(this.getClass());
			batchExceptionDTO.setErrorCode("EOT00017");
			batchExceptionDTO.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
			// 产品唯一性编码
			batchExceptionDTO.addErrorParam(CherryBatchUtil
					.getString(itemMap.get("IFProductId")));
			// 分类属性名称
			batchExceptionDTO.addErrorParam(CherryBatchUtil.getString(temp
					.get(ProductConstants.PROPVALUE_CN)));
			batchExceptionDTO.setException(e);
			throw new CherryBatchException(batchExceptionDTO);
		}
		return temp;
	}
	
	/**
	 * 取得分类属性值ID
	 * 
	 * @param Map
	 * @return
	 */
	/*
	private int getCatPropValId(Map<String, Object> map) throws Exception {
		// 分类属性值ID
		int catPropValId = 0;
		// 分类属性值
		String propValueCherry = CherryBatchUtil.getString(map.get(ProductConstants.PROPVALUECHERRY));
		
		// 分类属性值名称
		String propValueCN = CherryBatchUtil.getString(map.get(ProductConstants.PROPVALUE_CN));
		
		// 分类属性值,名不为空 
		if (!CherryBatchConstants.BLANK.equals(propValueCN) && !CherryBatchConstants.BLANK.equals(propValueCherry)) {
			// 根据属性【值】查询分类属性值ID
			catPropValId = binBAT122_Service.getCatPropValId1(map);
			if (catPropValId == 0) {
				
				// 如果接口编码大于4位，则将编码存入PropValueCherry，再随机生成4位唯一的编码存入PropValue;
				if(propValueCherry.length() <= 4){
					// 判断propValueCherry属性值在表中的propValue字段是否已经存在，若不存在，则使用propValueCherry，存在则随机生成4位
					Map<String,Object> tempMap = new HashMap<String, Object>();
					tempMap.put(ProductConstants.PRTCATPROPID, map.get(ProductConstants.PRTCATPROPID));
					tempMap.put(ProductConstants.PROPVALUE, propValueCherry);
					int propValIdByPV = binBAT122_Service.getCatPropValId1(tempMap);
					if(0 == propValIdByPV){
						// 分类属性值【终端用】
						map.put(ProductConstants.PROPVALUE, propValueCherry);
					} else {
						// 分类属性值4位【终端用】
						String propValue = getPropValue(map,ProductConstants.PROPVALUE);
						map.put(ProductConstants.PROPVALUE,propValue);
					}
					
				} else {
					// 分类属性值4位【终端用】
					String propValue = getPropValue(map,ProductConstants.PROPVALUE);
					map.put(ProductConstants.PROPVALUE,propValue);
				}
				
				// 添加分类属性值
				catPropValId = binBAT122_Service.addPropVal(map);
			} else {
				map.put(ProductConstants.CATPROPVALID, catPropValId);
				// 更新分类属性值
				binBAT122_Service.updPropVal(map);
			}
		}
		return catPropValId;
	}
	*/
	

	/**
	 * 取得分类属性值ID
	 * 
	 * @param Map
	 * @return
	 */
	private int getCatPropValId(Map<String, Object> map) throws Exception {
		// 分类属性值ID
		int catPropValId = 0;
		// 分类属性值
		String propValueCherry = CherryBatchUtil.getString(map.get(ProductConstants.PROPVALUECHERRY));
		
		// 分类属性值名称
		String propValueCN = CherryBatchUtil.getString(map.get(ProductConstants.PROPVALUE_CN));
		
		// 分类属性值,名不为空 
		if (!CherryBatchUtil.isBlankString(propValueCN)) {			
			/*// 根据属性【值】查询分类属性值ID
			catPropValId = binBAT122_Service.getCatPropValId1(map);*/
			Map<String, Object>  propValueInfo = null;
			List<Map<String, Object>> propValueInfoLst = null;
			Map<String, Object>  propValueParam = new HashMap<String, Object>();
			propValueParam.put(ProductConstants.PRTCATPROPID, map.get(ProductConstants.PRTCATPROPID));
			propValueParam.put(ProductConstants.PROPVALUE_CN, propValueCN);
			// 根据基本属性值ID和属性名称查询属性信息
			propValueInfoLst = binBAT122_Service.getCatPropValInfo(propValueParam);
			if(propValueInfoLst != null && !propValueInfoLst.isEmpty()) {
				// 属性值不存在的场合,名称重复
				if (propValueInfoLst.size() > 1) {
					return -1;
				}
				propValueInfo = propValueInfoLst.get(0);
				catPropValId = CherryUtil.obj2int(propValueInfo.get("propValId"));
			}
			
			if (catPropValId == 0) {
				// 分类属性值4位【终端用】
				String propValue = getPropValue(map,ProductConstants.PROPVALUE);
				map.put(ProductConstants.PROPVALUE,propValue);
				
				// 分类属性值空的场合，用随机生成4位填充【新后台用的属性值】
				if (CherryBatchUtil.isBlankString(propValueCherry)) {
					map.put(ProductConstants.PROPVALUECHERRY, propValue);
				} 
				
				// 添加分类属性值
				catPropValId = binBAT122_Service.addPropVal(map);
				
			} else {
				// 根据新后台用的属性值作为条件查询，若存在数据，抛错-2（属性值和已存在名称不匹配）
				String retPropValueCherry = CherryBatchUtil.getString(propValueInfo.get(ProductConstants.PROPVALUECHERRY));
				if (!CherryBatchUtil.isBlankString(propValueCherry) && !retPropValueCherry.equals(propValueCherry)) {
					return -2;
				}
				map.put(ProductConstants.CATPROPVALID, catPropValId);
				// 更新分类属性值
				//binBAT122_Service.updPropVal(map);
			}
		} 
		return catPropValId;
	}
	
	/**
	 * 生成品牌下不重复的分类属性值【4位随机】
	 * 
	 * @param map
	 * @return
	 */
	private String getPropValue(Map<String, Object> map,String key) {
		// 添加产品分类选项值
		Map<String, Object> temp = new HashMap<String, Object>();
		// 分类类别ID
		temp.put(ProductConstants.PRTCATPROPID, map.get(ProductConstants.PRTCATPROPID));
		while (true) {
			// 随机产生4位的字符串
			String randomStr = CherryUtil.getRandomStr(ProductConstants.CATE_LENGTH);
			temp.put(key, randomStr);
			// 取得分类属性值ID
			int propValId = binBAT122_Service.getCatPropValId1(temp);
			// 随机产生的4位字符串不重复
			if (propValId == 0) {
				return randomStr;
			}
		}
	}
	
	/**
	 * init
	 * @param map
	 */
	private void init(Map<String, Object> map) throws CherryBatchException, Exception{
		comMap = getComMap(map);
		
		// BatchCD 来自VSS$/01.Cherry/02.设计文档/01.概要设计/00.各种一览/【新设】CherryBatch一览.xlsx
		map.put("JobCode", "BAT122");
		comMap.put("JobCode", "BAT122");
		
		// 程序【开始运行时间】
		String runStartTime = binBAT122_Service.getSYSDateTime();
		// 作成日时
		map.put("RunStartTime", runStartTime);
		
		// 取得Job控制程序的数据截取开始时间及结束时间
		Map<String, Object> jobControlInfoMap = binbecm01_IF.getJobControlInfo(map);
		
		// 程序【截取数据开始时间】
		map.put("TargetDataStartTime", jobControlInfoMap.get("TargetDataStartTime"));
		// 程序【截取数据结束时间】
		map.put("TargetDataEndTime", jobControlInfoMap.get("TargetDataEndTime"));
		
		// 取得当前产品表版本号
		Map<String, Object> seqMap = new HashMap<String, Object>();
		seqMap.putAll(map);
		seqMap.put("type", "E");
		String prtTVersion = binOLCM15_BL.getCurrentSequenceId(seqMap);
		comMap.put("prtTVersion", prtTVersion);
		
		// 取得当前部门(柜台)产品表版本号
		seqMap.put("type", "F");
		String pdTVersion = binOLCM15_BL.getCurrentSequenceId(seqMap);
		comMap.put("pdTVersion", pdTVersion);
		
		// 系统时间
//		String sysDate = binBAT122_Service.getSYSDate();
		// 作成日时
		comMap.put(CherryConstants.CREATE_TIME, runStartTime);
		
		// 业务日期
		String bussDate = binBAT122_Service.getBussinessDate(map);
		comMap.put("businessDate", bussDate);
		map.put("businessDate", bussDate);
		
		// 业务日期+1
		String nextBussDate = DateUtil.addDateByDays(
				CherryBatchConstants.DATE_PATTERN, bussDate, 1);
		comMap.put("nextBussDate", nextBussDate);
		
		// 停用时间
		String closingTime = CherryUtil.suffixDate(bussDate, 1);
		comMap.put(ProductConstants.CLOSINGTIME, closingTime);
		
		// 取得大中小分类对应的分类Id
		getCatePropIds(comMap);
	}
	
	/**
	 * 程序结束时，处理Job共通(更新Job控制表 、插入Job运行履历表)
	 * @param paraMap
	 * @throws Exception
	 */
	private void programEnd(Map<String,Object> paraMap) throws Exception{
		
		paraMap.putAll(comMap);
		
		String targetDataStartTime = ConvertUtil.getString(paraMap.get("TargetDataStartTime"));
		if(!CherryBatchUtil.isBlankString(targetDataStartTime)){
			// 程序结束时，更新Job控制表 
			binbecm01_IF.updateJobControl(paraMap);
		}		
		// 程序结束时，插入Job运行履历表
		paraMap.put("flag", flag);
		paraMap.put("TargetDataCNT", totalCount);
		paraMap.put("SCNT", totalCount - failCount);
		paraMap.put("FCNT", failCount);
		paraMap.put("UCNT", updateCount);
		paraMap.put("ICNT", insertCount);
		paraMap.put("FReason", fReasonBuffer.append(fReason).toString());
 
		binbecm01_IF.insertJobRunHistory(paraMap);
	}

	/**
	 * 共通Map
	 * @param map
	 * @return
	 */
	private Map<String, Object> getComMap(Map<String, Object> map) {
		Map<String, Object> baseMap = new HashMap<String, Object>();
		
		// 更新程序名
		baseMap.put(CherryBatchConstants.UPDATEPGM, "BINBAT122");
		// 作成程序名
		baseMap.put(CherryBatchConstants.CREATEPGM, "BINBAT122");
		// 作成者
		baseMap.put(CherryBatchConstants.CREATEDBY,CherryBatchConstants.UPDATE_NAME);
		// 更新者
		baseMap.put(CherryBatchConstants.UPDATEDBY,CherryBatchConstants.UPDATE_NAME);
		// 所属组织
		baseMap.put(CherryBatchConstants.ORGANIZATIONINFOID, map.get(CherryBatchConstants.ORGANIZATIONINFOID).toString());
		// 品牌ID
		baseMap.put(CherryBatchConstants.BRANDINFOID, map.get(CherryBatchConstants.BRANDINFOID).toString());
		// 品牌ID
		baseMap.put(CherryBatchConstants.BRAND_CODE, map.get(CherryBatchConstants.BRAND_CODE).toString());
		
		return baseMap;
	}
	
	/**
	 * 取得大中小分类对应的分类Id
	 */
	private void getCatePropIds(Map<String, Object> paramsMap){
		Map<String, Object> temp = new HashMap<String, Object>();
		temp.putAll(paramsMap);
		// 大分类名
		temp.put(ProductConstants.PROPNAMECN, ProductConstants.CATE_NAME_B);
		temp.put(ProductConstants.TEMINALFLAG, ProductConstants.CATE_TYPE_1);
		// 根据分类终端显示区分查询分类Id
		prtCatPropId_B = binBAT122_Service.getCatPropId2(temp);
		if (prtCatPropId_B == 0) {
			// 根据分类名查询分类Id
			prtCatPropId_B = binBAT122_Service.getCatPropId1(temp);
			if (prtCatPropId_B == 0) {
				// 添加大分类
				prtCatPropId_B = binBAT122_Service.addCatProperty(temp);
			} else {
				temp.put(ProductConstants.PRTCATPROPID, prtCatPropId_B);
				// 更新分类终端显示区分
				binBAT122_Service.updProp(temp);
			}
		}
		// 中分类名
		temp.put(ProductConstants.PROPNAMECN, ProductConstants.CATE_NAME_M);
		temp.put(ProductConstants.TEMINALFLAG, ProductConstants.CATE_TYPE_3);
		// 根据分类终端显示区分查询分类Id
		prtCatPropId_M = binBAT122_Service.getCatPropId2(temp);
		if (prtCatPropId_M == 0) {
			// 根据分类名查询分类Id
			prtCatPropId_M = binBAT122_Service.getCatPropId1(temp);
			if (prtCatPropId_M == 0) {
				// 添加中分类
				prtCatPropId_M = binBAT122_Service.addCatProperty(temp);
			} else {
				temp.put(ProductConstants.PRTCATPROPID, prtCatPropId_M);
				// 更新分类终端显示区分
				binBAT122_Service.updProp(temp);
			}
		}
		// 小分类名
		temp.put(ProductConstants.PROPNAMECN, ProductConstants.CATE_NAME_L);
		temp.put(ProductConstants.TEMINALFLAG, ProductConstants.CATE_TYPE_2);
		// 根据分类终端显示区分查询分类Id
		prtCatPropId_L = binBAT122_Service.getCatPropId2(temp);
		if (prtCatPropId_L == 0) {
			// 根据分类名查询分类Id
			prtCatPropId_L = binBAT122_Service.getCatPropId1(temp);
			if (prtCatPropId_L == 0) {
				// 添加小分类
				prtCatPropId_L = binBAT122_Service.addCatProperty(temp);
			} else {
				temp.put(ProductConstants.PRTCATPROPID, prtCatPropId_L);
				// 更新分类终端显示区分
				binBAT122_Service.updProp(temp);
			}
		}
		
	}
	
	/**
	 * 输出处理结果信息
	 * 
	 * @throws CherryBatchException
	 */
	private void outMessage() throws CherryBatchException {
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
		// 成功总件数
		logger.BatchLogger(batchLoggerDTO2);
		// 插入件数
		logger.BatchLogger(batchLoggerDTO3);
		// 更新件数
		logger.BatchLogger(batchLoggerDTO4);
		// 失败件数
		logger.BatchLogger(batchLoggerDTO5);
		
		
		// 失败ItemCode集合
		if(!CherryBatchUtil.isBlankList(faildItemList)){
			BatchLoggerDTO batchLoggerDTO6 = new BatchLoggerDTO();
			batchLoggerDTO6.setCode("EOT00100");
			batchLoggerDTO6.setLevel(CherryBatchConstants.LOGGER_INFO);
			batchLoggerDTO6.addParam(faildItemList.toString());
			logger.BatchLogger(batchLoggerDTO6);
			
			fReason = "产品导入部分数据处理失败，具体见log日志";
		}
		
	}
}
