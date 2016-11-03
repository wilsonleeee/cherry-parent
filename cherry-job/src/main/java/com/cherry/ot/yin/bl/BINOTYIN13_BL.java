/*	
 * @(#)BINOTYIN01_BL.java     1.0 @2015-1-28
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
package com.cherry.ot.yin.bl;

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
import com.cherry.ia.pro.bl.BINBEIFPRO04_BL;
import com.cherry.ia.pro.service.BINBEIFPRO01_Service;
import com.cherry.ot.yin.service.BINOTYIN13_Service;

/**
 *
 * 颖通接口：颖通大陆产品导入BL
 * 
 * 颖通使用增量导入，导入到Cherry后，删除第三方数据
 * @author jijw
 *
 * @version  2015-1-28
 */
public class BINOTYIN13_BL {
	
	private static CherryBatchLogger logger = new CherryBatchLogger(BINOTYIN13_BL.class);	
	@Resource
	private BINOTYIN13_Service binOTYIN13_Service;
	
	@Resource
	private BINBEIFPRO01_Service binbeifpro01Service;
	
	/** 各类编号取号共通BL */
	@Resource(name="binOLCM15_BL")
	private BINOLCM15_BL binOLCM15_BL;
	
	@Resource(name="binOLCM05_BL")
	private BINOLCM05_BL binOLCM05_BL;
	
	/** 系统配置项 共通BL */
	@Resource(name="binOLCM14_BL")
	private BINOLCM14_BL binOLCM14_BL;
	
	/** JOB执行相关共通 IF */
	@Resource(name="binbecm01_IF")
	private BINBECM01_IF binbecm01_IF;
	
	/** 产品实时下发 */
	@Resource(name="binbeifpro04_BL")
	private BINBEIFPRO04_BL binbeifpro04_BL;
	
	/** BATCH处理标志 */
	private int flag = CherryBatchConstants.BATCH_SUCCESS;
	
	/** 每批次(页)处理数量 1000 */
	private final int BTACH_SIZE = 1000;
	
	private Map<String, Object> comMap;
	
	/** 更新添加操作flag */
	private int optFlag = 0;
	
	/** 产品大分类ID */
	private int prtCatPropId_B = 0;
	/** 产品中分类ID */
	private int prtCatPropId_M = 0;
	/** 产品小分类ID */
	private int prtCatPropId_L = 0;
	/** 产品动态分类ID(U_SubCategory) */
	private int prtCatPropId_D1 = 0;
	/** 产品动态分类ID(U_LineMF) */
	private int prtCatPropId_D2 = 0;
	/** 产品动态分类ID(U_LineCategory) */
	private int prtCatPropId_D3 = 0;
	
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
	private String fReason = "";
	
	/**
	 * batch处理
	 * 
	 * @param 无
	 * 
	 * @return Map
	 * @throws CherryBatchException
	 */
	@SuppressWarnings("unchecked")
	public int tran_batchOTYIN13(Map<String, Object> map)
			throws CherryBatchException,Exception {
		
		// 初始化
		try{
			init(map);
			
			String targetDataStartTime = ConvertUtil.getString(map.get("TargetDataStartTime"));
			if(CherryBatchUtil.isBlankString(targetDataStartTime)){
				fReason = "程序没有配置相应的Job控制信息，无法执行当前程序，请配置后再操作。";
				outMessage();
				programEnd(map);
				return CherryBatchConstants.BATCH_ERROR;
			}
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
		
		// 上一批次(页)最后一条itemCode
		String bathLastItemCode = "";
		
		while (true) {

			// 查询接口产品列表
			Map<String, Object> paraMap = new HashMap<String, Object>();
			paraMap.putAll(comMap);
			paraMap.put("batchSize", BTACH_SIZE);
			paraMap.put("bathLastItemCode", bathLastItemCode);
			List<Map<String, Object>> itemList = binOTYIN13_Service.getItemListForOT(paraMap);
			if (CherryBatchUtil.isBlankList(itemList)) {
				break;
			} else {
				try {
					// 备份产品接口表产品数据
					binOTYIN13_Service.backupItems(itemList);
					binOTYIN13_Service.manualCommit();
				} catch (Exception e) {
					binOTYIN13_Service.manualRollback();
					BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
					batchLoggerDTO.setCode("EOT00012");
					batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
					logger.BatchLogger(batchLoggerDTO, e);
					fReason = "备份产品接口数据失败，具体见log日志";
					// 备份失败后，跳转至下一批次(页)
					continue;
				}
			}
			
			 // 当前批次最后一个产品的ItemCode赋给bathLastItemCode，用于当前任务下一批次(页)产品数据的筛选条件
			 bathLastItemCode = CherryBatchUtil.getString(itemList.get(itemList.size()- 1).get("ItemCode"));
			 // 统计总条数
			 totalCount += itemList.size();
			 // 更新新后台数据库产品/促销品相关表
			 updateBackEnd(itemList,map);
			 // 接口产品列表为空或产品数据少于一批次(页)处理数量，跳出循环
			 if (itemList.size() < BTACH_SIZE) {
				 break;
			 }
		}
		
		// 日志
		outMessage();
		// 程序结束时，处理Job共通(插入Job运行履历表)
		programEnd(map);
		
		/* 此处调用出现问题，等稳定后再行调用
		// 调用产品实时下发
		try{
			Map<String,Object> issPrtMap = new HashMap<String, Object>();
			issPrtMap.put(CherryBatchConstants.ORGANIZATIONINFOID, map.get(CherryBatchConstants.ORGANIZATIONINFOID));
			issPrtMap.put(CherryBatchConstants.BRANDINFOID, map.get(CherryBatchConstants.BRANDINFOID));
			// Job运行履历表的运行方式
			issPrtMap.put("RunType", map.get("RunType"));
			// 是否实时发送MQ 1:是、 0或空：否
			issPrtMap.put("IsSendMQ", "1");
			
			binbeifpro04_BL.tran_batchProducts(issPrtMap);
		}catch(Exception e){
			BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
			batchLoggerDTO.setCode("EKD00016");
			batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
			logger.BatchLogger(batchLoggerDTO, e);
		}
		*/
		
		return flag;
	}
	
	/**
	 * 更新新后台数据库产品/促销品相关表
	 * @param itemList
	 */
	private void updateBackEnd(List<Map<String, Object>> itemList,Map<String,Object> map) {
		
		// 删除成功导入的产品
		List<Map<String, Object>> delExportItemList = new ArrayList<Map<String,Object>>();
		
		// 取得当前产品表版本号
		Map<String, Object> seqMap = new HashMap<String, Object>();
		seqMap.putAll(map);
		seqMap.put("type", "E");
		String prtTVersion = binOLCM15_BL.getCurrentSequenceId(seqMap);
		comMap.put("prtTVersion", prtTVersion);
		
		// 取得当前促销产品表版本号
		seqMap.put("type", "H");
		String promTVersion = binOLCM15_BL.getCurrentSequenceId(seqMap);
		comMap.put("promTVersion", promTVersion);
		
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
				
				// Coupon会用来做区分，为N的导成产品，为Y的导成TZZK类型的促销品
				String coupon = CherryBatchUtil.getString(itemMap.get("Coupon"));
				if("N".equals(coupon.trim())){
					// 保存或更新产品信息
//					saveOrUpdPro(itemMap);
					saveOrUpdProNew(itemMap);
					// 更新产品价格信息
					updPrtPrice(itemMap);
					// 更新产品条码信息
					updProVendor(itemMap);
					// 更新分类信息  --暂时注释掉（20150601）待重新确认可放开时，再去掉注释 。
					updPrtCategory(itemMap);
				} else if("Y".equals(coupon.trim())){
					// 保存或更新促销品信息
					saveOrUpdPrm(itemMap);
					// 更新促销品条码信息
					updPrmVendor(itemMap);
				}
				
				binOTYIN13_Service.manualCommit();
				// 记载成功导入新后台的接口产品数据
				delExportItemList.add(itemMap);
				
			} catch (Exception e) {
				fReason = "部分产品处理失败，具体见log日志";
				binOTYIN13_Service.manualRollback();
				// 记载导入新后台失败的ItemCode
				faildItemList.add(CherryBatchUtil.getString(itemMap.get("ItemCode")));

				// 失败件数加一
				failCount += 1;
				flag = CherryBatchConstants.BATCH_WARNING;
				
				try{
					// 更新颖通产品接口表导入失败的产品信息的CreateDate
					binOTYIN13_Service.updateOtItem(itemMap);
				}catch(Exception ex){
					BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO();
					batchExceptionDTO.setBatchName(this.getClass());
					batchExceptionDTO.setErrorCode("EOT00083");
					batchExceptionDTO.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
					// 产品唯一性编码
					batchExceptionDTO.addErrorParam(CherryBatchUtil.getString(itemMap.get("ItemCode")));
					// 品牌CODE
					batchExceptionDTO.addErrorParam(CherryBatchUtil.getString(comMap.get(ProductConstants.BRANDCODE)));
					batchExceptionDTO.setException(ex);
					flag = CherryBatchConstants.BATCH_ERROR;
				}
				
			}
		}
		
		// 删除已成功导入新后台的接口产品数据
		/* 2015.7.14 删除数据会导致下一品牌执行时，没有可读取的数据。
		if (!CherryBatchUtil.isBlankList(delExportItemList)) {
			try {
				binOTYIN13_Service.deleteItemForOT(delExportItemList);
				binOTYIN13_Service.tpifManualCommit();
			} catch(Exception e){
				binOTYIN13_Service.tpifManualRollback();
			}
		}
		*/
		
	}
	
	/**
	 * 数据截断转换等非SQL处理
	 * @param itemMap
	 */
	private void screenMap(Map<String, Object> itemMap){
		
		// 截取产品名称(中英混合字符最大40--兼容老后台varchar数据类型) 
		String itemDesc = CherryBatchUtil.getString(itemMap.get("ItemDesc"));
		String nameRule = binOLCM14_BL.getConfigValue("1132", ConvertUtil.getString(comMap.get(CherryBatchConstants.ORGANIZATIONINFOID)),ConvertUtil.getString(comMap.get(CherryBatchConstants.BRANDINFOID)));
		int nameRuleLen = Integer.parseInt(nameRule);
		itemDesc = CherryBatchUtil.mixStrsub(itemDesc, nameRuleLen); // 使用系统配置项控制产品名称长度
//		itemDesc = CherryBatchUtil.mixStrsub(itemDesc, 100); //由40改成100 --(WITPOSQA-12144)
		itemDesc = itemDesc.replace("'", " "); 
		itemMap.put("ItemDesc", itemDesc); 
		
		// 接口表Status字段转换
		String status = CherryBatchUtil.getString(itemMap.get("Status"));
		if("Y".equals(status.trim())){
			itemMap.put("Status", "E"); 
			itemMap.put("validFlag", "1"); // 仅用于促销品
		}else if("N".equals(status.trim())){
			itemMap.put("Status", "D");
			itemMap.put("validFlag", "0"); // 仅用于促销品
		}
		
		// 产品大类(中英混合字符最大20--兼容老后台varchar数据类型)
		String uLineHeaddesc = CherryBatchUtil.getString(itemMap.get("U_LineHeaddesc"));
		uLineHeaddesc = CherryBatchUtil.mixStrsub(uLineHeaddesc, 20);
		uLineHeaddesc = uLineHeaddesc.replace("'", " "); 
		itemMap.put("U_LineHeaddesc", uLineHeaddesc);
		
		// 产品小类(中英混合字符最大20--兼容老后台varchar数据类型)
		String lineName = CherryBatchUtil.getString(itemMap.get("LineName"));
		lineName = CherryBatchUtil.mixStrsub(lineName, 20);
		lineName = lineName.replace("'", " "); 
		itemMap.put("LineName", lineName);
		
		// 产品中分类（U_Category）
		String uCategory = CherryBatchUtil.getString(itemMap.get("U_Category"));
		uCategory = CherryBatchUtil.mixStrsub(uCategory, 20);
		uCategory = uCategory.replace("'", " "); 
		itemMap.put("U_Category", uCategory);
		
		// 编码、条码单引号用空格替换
		String barCode = CherryBatchUtil.getString(itemMap.get("BarCode"));
		barCode = barCode.replace("'", " ");
		itemMap.put("BarCode", barCode);
		
		String u_OldItemNo = CherryBatchUtil.getString(itemMap.get("U_OldItemNo"));
		u_OldItemNo = u_OldItemNo.replace("'", " "); 
		itemMap.put("U_OldItemNo", u_OldItemNo);
	}
	
	/**
	 * 更新或插入产品信息
	 * 
	 * @param map
	 * @return
	 */
	private void saveOrUpdProNew(Map<String, Object> itemMap)
			throws CherryBatchException {
		
		// 查询产品ID
		int productId = 0;
		int oldPrtId = binOTYIN13_Service.searchProductId(itemMap);
		
		Map<String,Object> prtMap = new HashMap<String, Object>();
		prtMap.putAll(itemMap);
		if(oldPrtId != 0){
			prtMap.put(ProductConstants.PRODUCT_ID, oldPrtId);
		}else{
			// 判断U_OldItemNo是否存在
			oldPrtId = binOTYIN13_Service.searchProductIdByUnitCode(itemMap);
			
			if(oldPrtId != 0){
				prtMap.put(ProductConstants.PRODUCT_ID, oldPrtId);
			}
		}
		
		// ************************** 产品编码唯一性验证 **************************
		
		try {
			prtMap.put("unitCode", itemMap.get("U_OldItemNo"));
			int unitCodeCount = binOLCM05_BL.getExistUnitCodeForPrtAndProm(prtMap);	
			
			if(unitCodeCount != 0){
				throw new CherryBatchException();
			}
			
		} catch(CherryBatchException e){
			BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO();
			batchExceptionDTO.setBatchName(this.getClass());
			batchExceptionDTO.setErrorCode("EOT00054");
			batchExceptionDTO.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
			// 产品唯一性编码K3Product
			batchExceptionDTO.addErrorParam(CherryBatchUtil .getString(itemMap.get("ItemCode")));
			// 厂商编码
			batchExceptionDTO.addErrorParam(CherryBatchUtil .getString(itemMap.get("U_OldItemNo")));
			// 产品条码
			batchExceptionDTO.addErrorParam(CherryBatchUtil .getString(itemMap.get("BarCode")));
			// 中文名
			batchExceptionDTO.addErrorParam(CherryBatchUtil .getString(itemMap.get("ItemDesc")));
			batchExceptionDTO.setException(e);
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
			batchExceptionDTO.addErrorParam(CherryBatchUtil .getString(itemMap.get("ItemCode")));
			// 厂商编码
			batchExceptionDTO.addErrorParam(CherryBatchUtil .getString(itemMap.get("U_OldItemNo")));
			// 产品条码
			batchExceptionDTO.addErrorParam(CherryBatchUtil .getString(itemMap.get("BarCode")));
			// 中文名
			batchExceptionDTO.addErrorParam(CherryBatchUtil .getString(itemMap.get("ItemDesc")));
			throw new CherryBatchException(batchExceptionDTO);
		}
		
		// ********************** 验证当前barCode在促销品中不存在   **********************
		
		
		if (0 == oldPrtId) {
			// 插入操作
			optFlag = 1;
			try {
				// 将有效区分转换为BINBECMINC99共通SQL指定的属性名
				itemMap.put("validFlagVal", itemMap.get("validFlag"));
				// 插入产品信息
				productId = binOTYIN13_Service.insertProductInfo(itemMap);
				// 把产品Id设入条件map
				itemMap.put(ProductConstants.PRODUCT_ID, productId);
				// 插入件数加一
				insertCount += 1;
			} catch (Exception e) {
				BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO();
				batchExceptionDTO.setBatchName(this.getClass());
				batchExceptionDTO.setErrorCode("EOT00013");
				batchExceptionDTO.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
				// 产品唯一性编码
				batchExceptionDTO.addErrorParam(CherryBatchUtil.getString(itemMap.get("ItemCode")));
				// 中文名
				batchExceptionDTO.addErrorParam(CherryBatchUtil.getString(itemMap.get("ItemDesc")));
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
				binOTYIN13_Service.updateProductInfo(itemMap);
				
				// 产品变动后更新产品方案明细表的version字段
				binbeifpro01Service.updPrtSolutionDetail(itemMap);
				// 更新件数加一
				updateCount += 1;
			} catch (Exception e) {
				BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO();
				batchExceptionDTO.setBatchName(this.getClass());
				batchExceptionDTO.setErrorCode("EOT00014");
				batchExceptionDTO
				.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
				// 产品唯一性编码
				batchExceptionDTO.addErrorParam(CherryBatchUtil.getString(itemMap.get("ItemCode")));
				// 中文名
				batchExceptionDTO.addErrorParam(CherryBatchUtil.getString(itemMap.get("ItemDesc")));
				batchExceptionDTO.setException(e);
				throw new CherryBatchException(batchExceptionDTO);
			}
		}
	}
	
	/**
	 * 更新或插入产品信息
	 * 
	 * @param map
	 * @return
	 */
	@Deprecated
	private void saveOrUpdPro(Map<String, Object> itemMap)
			throws CherryBatchException {
		
		// 查询产品ID
		int productId = 0;
		int oldPrtId = binOTYIN13_Service.searchProductId(itemMap);
		
		// ************************** 产品编码唯一性验证 **************************
		
		Map<String,Object> prtMap = new HashMap<String, Object>();
		prtMap.putAll(itemMap);
		if(oldPrtId != 0){
			prtMap.put(ProductConstants.PRODUCT_ID, oldPrtId);
		}
		
		try {
			prtMap.put("unitCode", itemMap.get("U_OldItemNo"));
			int unitCodeCount = binOLCM05_BL.getExistUnitCodeForPrtAndProm(prtMap);	
			
			if(unitCodeCount != 0){
				throw new CherryBatchException();
			}
			
		} catch(CherryBatchException e){
			BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO();
			batchExceptionDTO.setBatchName(this.getClass());
			batchExceptionDTO.setErrorCode("EOT00054");
			batchExceptionDTO.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
			// 产品唯一性编码K3Product
			batchExceptionDTO.addErrorParam(CherryBatchUtil .getString(itemMap.get("ItemCode")));
			// 厂商编码
			batchExceptionDTO.addErrorParam(CherryBatchUtil .getString(itemMap.get("U_OldItemNo")));
			// 产品条码
			batchExceptionDTO.addErrorParam(CherryBatchUtil .getString(itemMap.get("BarCode")));
			// 中文名
			batchExceptionDTO.addErrorParam(CherryBatchUtil .getString(itemMap.get("ItemDesc")));
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
			batchExceptionDTO.addErrorParam(CherryBatchUtil .getString(itemMap.get("ItemCode")));
			// 厂商编码
			batchExceptionDTO.addErrorParam(CherryBatchUtil .getString(itemMap.get("U_OldItemNo")));
			// 产品条码
			batchExceptionDTO.addErrorParam(CherryBatchUtil .getString(itemMap.get("BarCode")));
			// 中文名
			batchExceptionDTO.addErrorParam(CherryBatchUtil .getString(itemMap.get("ItemDesc")));
			throw new CherryBatchException(batchExceptionDTO);
		}
		
		// ********************** 验证当前barCode在促销品中不存在   **********************
		
		
		if (0 == oldPrtId) {
			// 插入操作
			optFlag = 1;
			try {
				// 插入产品信息
				productId = binOTYIN13_Service.insertProductInfo(itemMap);
				// 把产品Id设入条件map
				itemMap.put(ProductConstants.PRODUCT_ID, productId);
				// 插入件数加一
				insertCount += 1;
			} catch (Exception e) {
				BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO();
				batchExceptionDTO.setBatchName(this.getClass());
				batchExceptionDTO.setErrorCode("EOT00013");
				batchExceptionDTO.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
				// 产品唯一性编码
				batchExceptionDTO.addErrorParam(CherryBatchUtil.getString(itemMap.get("ItemCode")));
				// 中文名
				batchExceptionDTO.addErrorParam(CherryBatchUtil.getString(itemMap.get("ItemDesc")));
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
				binOTYIN13_Service.updateProductInfo(itemMap);
				
				// 产品变动后更新产品方案明细表的version字段
				binbeifpro01Service.updPrtSolutionDetail(itemMap);
				// 更新件数加一
				updateCount += 1;
			} catch (Exception e) {
				BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO();
				batchExceptionDTO.setBatchName(this.getClass());
				batchExceptionDTO.setErrorCode("EOT00014");
				batchExceptionDTO
						.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
				// 产品唯一性编码
				batchExceptionDTO.addErrorParam(CherryBatchUtil.getString(itemMap.get("ItemCode")));
				// 中文名
				batchExceptionDTO.addErrorParam(CherryBatchUtil.getString(itemMap.get("ItemDesc")));
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
		
		try {
				// 删除产品价格
				binOTYIN13_Service.delProductPrice(tempMap);
				
				// 默认产品价格生效日期
				tempMap.put(ProductConstants.STRAT_DATE, tempMap.get("businessDate"));
				// 默认产品价格失效日期
				tempMap.put(ProductConstants.END_DATE,ProductConstants.DEFAULT_END_DATE);
				
				// 新增价格
				if (CherryBatchChecker.isNull(tempMap.get("Price_Sales"))) {
					tempMap.put("Price_Sales", "0");
				}
				// 插入新的产品价格信息
				binOTYIN13_Service.insertProductPrice(tempMap);
			
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
					.getString(tempMap.get("ItemCode")));
			// 中文名
			batchExceptionDTO.addErrorParam(CherryBatchUtil
					.getString(tempMap.get("ItemDesc")));
			batchExceptionDTO.setException(e);
			throw new CherryBatchException(batchExceptionDTO);
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
				// 将有效区分转换为BINBECMINC99共通SQL指定的属性名
				itemMap.put("validFlagVal", tempMap.get("validFlag"));
				// 添加产品厂商信息
				binOTYIN13_Service.insertProductVendor(tempMap);
			} 
			else if(2 == optFlag){
				// 编辑产品厂商信息
				Map<String, Object> prtVendorMap = binOTYIN13_Service.getProductVendorInfo(tempMap);
				int proVendorId = (Integer)prtVendorMap.get("proVendorId");
				tempMap.put("prtVendorId", proVendorId);
				// 更新产品厂商
				binOTYIN13_Service.updPrtVendor(tempMap);

				// 接口表validFlag
				String validFlagOT = CherryBatchUtil.getString(tempMap.get("validFlag"));
				// 新后台厂商表validFlag
				String validFlagBE = CherryBatchUtil.getString(prtVendorMap.get("validFlag"));
				
				// 新后台有效
				if("1".equals(validFlagBE)){
					
					if("1".equals(validFlagOT)){
						// 接口表有效时更新编码条码,更新产品条码对应关系信息
						binOTYIN13_Service.updPrtBarCode(tempMap);
					}else{
						// 接口表无效时则停用编码条码,更新产品停用日时
						binOTYIN13_Service.updatePrtClosingTime(tempMap);
					}
				}else{
					// 新后台无效，接口表无效时则不做任何操作、接口表有效时不做任何操作（下一次下发时会新增编码条码）
				}
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
					.getString(tempMap.get("ItemCode")));
			// 厂商编码
			batchExceptionDTO.addErrorParam(CherryBatchUtil.getString(tempMap
					.get("U_OldItemNo")));
			// 产品条码
			batchExceptionDTO.addErrorParam(CherryBatchUtil.getString(tempMap
					.get("BarCode")));
			batchExceptionDTO.setException(e);
			throw new CherryBatchException(batchExceptionDTO);
		}
	}
	
	/**
	 * 更新产品分类
	 * 
	 * @param itemMap
	 */
	private void updPrtCategory(Map<String, Object> itemMap)
			throws CherryBatchException {
		
		// 删除产品分类属性关系信息
		binOTYIN13_Service.delPrtCategory(itemMap);
		// 添加产品分类关系(大分类)
		addPrtcategory(itemMap, ProductConstants.CATE_TYPE_1);
		// 添加产品分类关系(中分类)
		addPrtcategory(itemMap, ProductConstants.CATE_TYPE_3);
		// 添加产品分类关系(小分类)
		addPrtcategory(itemMap, ProductConstants.CATE_TYPE_2);
		
//		// 添加产品动态分类关系D1（U_SubCategory）
//		addPrtcategoryDynaCate(itemMap, "D1");
//		// 添加产品动态分类关系D2（U_LineMF）
//		addPrtcategoryDynaCate(itemMap, "D2");
//		// 添加产品动态分类关系D3（U_LineCategory）
//		addPrtcategoryDynaCate(itemMap, "D3");
		
	}
	
	/**
	 * 添加产品分类关系
	 * 
	 * @param itemMap
	 * @param cateType
	 */
	private void addPrtcategory(Map<String, Object> itemMap, String cateType)
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
			temp.put(ProductConstants.PROPVALUECHERRY,itemMap.get("U_Brand"));
			// 大分类属性名称
			temp.put(ProductConstants.PROPVALUE_CN,itemMap.get("U_BrandName"));
		} else if (ProductConstants.CATE_TYPE_3.equals(cateType)) {
			// 中分类ID
			temp.put(ProductConstants.PRTCATPROPID, prtCatPropId_M);
			// 中分类属性值
			temp.put(ProductConstants.PROPVALUECHERRY,itemMap.get("U_LineMF"));
			// 中分类属性名称q
			temp.put(ProductConstants.PROPVALUE_CN,itemMap.get("U_LineMFName"));
		} else if (ProductConstants.CATE_TYPE_2.equals(cateType)) {
			// 小分类ID
			temp.put(ProductConstants.PRTCATPROPID, prtCatPropId_L);
			// 小分类属性值
			temp.put(ProductConstants.PROPVALUECHERRY,itemMap.get("U_LineCategory"));
			// 小分类属性名称
			temp.put(ProductConstants.PROPVALUE_CN,itemMap.get("U_LineCategoryName"));
		}
		try {
			// 取得分类属性值ID
			int catePropValId = getCatPropValId(temp);
			if (catePropValId != 0) {
				temp.put(ProductConstants.CATPROPVALID, catePropValId);
				// 插入产品分类关系表
				binOTYIN13_Service.insertPrtCategory(temp);
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
					.getString(itemMap.get("ItemCode")));
			// 分类属性名称
			batchExceptionDTO.addErrorParam(CherryBatchUtil.getString(temp
					.get(ProductConstants.PROPVALUE_CN)));
			batchExceptionDTO.setException(e);
			throw new CherryBatchException(batchExceptionDTO);
		}
	}
	
	
	/**
	 * 添加产品分类关系(动态分类)
	 * 
	 * @param itemMap
	 * @param cateType
	 */
	@Deprecated
	private void addPrtcategoryDynaCate(Map<String, Object> itemMap, String cateType)
			throws CherryBatchException {
		Map<String, Object> temp = new HashMap<String, Object>();
		temp.putAll(comMap);
		// 产品ID
		temp.put(ProductConstants.PRODUCT_ID,itemMap.get(ProductConstants.PRODUCT_ID));
		// 分类类型
		temp.put(ProductConstants.CATE_TYPE, cateType);
		// 动态分类:U_SubCategory
		if ("D1".equals(cateType)) {
			// 动态分类ID(U_SubCategory)
			temp.put(ProductConstants.PRTCATPROPID, prtCatPropId_D1);
			// 动态分类属性名称
			temp.put(ProductConstants.PROPVALUE_CN,itemMap.get("U_SubCategory"));
			
		} else if ("D2".equals(cateType)) {
			// 动态分类ID(U_LineMF)
			temp.put(ProductConstants.PRTCATPROPID, prtCatPropId_D2);
			// 动态分类属性名称
			temp.put(ProductConstants.PROPVALUE_CN,itemMap.get("U_LineMF"));
		} else if ("D3".equals(cateType)) {
			// 动态分类ID(U_LineCategory)
			temp.put(ProductConstants.PRTCATPROPID, prtCatPropId_D3);
			// 小分类属性名称
			temp.put(ProductConstants.PROPVALUE_CN,itemMap.get("U_LineCategory"));
		}
		try {
			// 取得分类属性值ID
			int catePropValId = getCatPropValIdDynaCate(temp);
			if (catePropValId != 0) {
				temp.put(ProductConstants.CATPROPVALID, catePropValId);
				// 插入产品分类关系表
				binOTYIN13_Service.insertPrtCategory(temp);
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
					.getString(itemMap.get("ItemCode")));
			// 分类属性名称
			batchExceptionDTO.addErrorParam(CherryBatchUtil.getString(temp
					.get(ProductConstants.PROPVALUE_CN)));
			batchExceptionDTO.setException(e);
			throw new CherryBatchException(batchExceptionDTO);
		}
	}
	
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
		if (!CherryBatchConstants.BLANK.equals(propValueCN) && !CherryBatchConstants.BLANK.equals(propValueCherry)) {
			// 根据属性【值】查询分类属性值ID
			catPropValId = binOTYIN13_Service.getCatPropValId1(map);
			if (catPropValId == 0) {
				
				// 如果接口编码大于4位，则将编码存入PropValueCherry，再随机生成4位唯一的编码存入PropValue;
				if(propValueCherry.length() <= 4){
					// 判断propValueCherry属性值在表中的propValue字段是否已经存在，若不存在，则使用propValueCherry，存在则随机生成4位
					Map<String,Object> tempMap = new HashMap<String, Object>();
					tempMap.put(ProductConstants.PRTCATPROPID, map.get(ProductConstants.PRTCATPROPID));
					tempMap.put(ProductConstants.PROPVALUE, propValueCherry);
					int propValIdByPV = binOTYIN13_Service.getCatPropValId1(tempMap);
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
				catPropValId = binOTYIN13_Service.addPropVal(map);
			} else {
				map.put(ProductConstants.CATPROPVALID, catPropValId);
				// 更新分类属性值
				binOTYIN13_Service.updPropVal(map);
			}
		}
		return catPropValId;
	}
	
	/**
	 * 取得分类属性值ID(动态分类)
	 * 
	 * @param Map
	 * @return
	 */
	@Deprecated
	private int getCatPropValIdDynaCate(Map<String, Object> map) throws Exception {
		// 分类属性值ID
		int catPropValId = 0;
		// 动态分类属性值名称
		String propValueCN = CherryBatchUtil.getString(map.get(ProductConstants.PROPVALUE_CN));
		
		// 动态分类属性值,名不为空
		if (!CherryBatchConstants.BLANK.equals(propValueCN)) {
			// 根据属性名称查询分类属性值ID
			catPropValId = binOTYIN13_Service.getCatPropValId2(map);
			if (catPropValId == 0) {
				
				// 分类属性值4位【终端用】
				String propValue = getPropValue(map,ProductConstants.PROPVALUE);
				map.put(ProductConstants.PROPVALUE,propValue);
				
				// 判断propValue属性编码是否已存在于propvaslueCherry，不存在则使用propValue，存在则再随机生成
				Map<String,Object> tempMap = new HashMap<String, Object>();
				tempMap.put(ProductConstants.PRTCATPROPID, map.get(ProductConstants.PRTCATPROPID));
				tempMap.put(ProductConstants.PROPVALUECHERRY, propValue);
				int propValIdByPV = binOTYIN13_Service.getCatPropValId1(tempMap);
				
				if(0 == propValIdByPV){
					map.put(ProductConstants.PROPVALUECHERRY,propValue);
				} else {
					// 分类属性值4位【新后台用】
					String propValueCherry = getPropValue(map,ProductConstants.PROPVALUECHERRY);
					map.put(ProductConstants.PROPVALUECHERRY,propValueCherry);
				}
				
				// 添加分类属性值
				catPropValId = binOTYIN13_Service.addPropVal(map);
			} else {
				map.put(ProductConstants.CATPROPVALID, catPropValId);
				// 更新分类属性值
				binOTYIN13_Service.updPropVal(map);
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
			int propValId = binOTYIN13_Service.getCatPropValId1(temp);
			// 随机产生的4位字符串不重复
			if (propValId == 0) {
				return randomStr;
			}
		}
	}
	
	/**
	 * 更新或插入促销产品信息
	 * 
	 * @param map
	 * @return
	 */
	private void saveOrUpdPrm(Map<String, Object> itemMap)
			throws CherryBatchException {
		
		// 查询促销产品ID
		int prmProductId = 0;
		int oldPrmId = binOTYIN13_Service.searchPromProductId(itemMap);
		
		
		// ************************** 产品编码唯一性验证 **************************
		
		Map<String,Object> prtMap = new HashMap<String, Object>();
		prtMap.putAll(itemMap);
		if(oldPrmId != 0){
			prtMap.put("promotionProId", oldPrmId);
		}
		
		try {
			prtMap.put("unitCode", itemMap.get("U_OldItemNo"));
			int unitCodeCount = binOLCM05_BL.getExistUnitCodeForPrtAndProm(prtMap);	
			
			if(unitCodeCount != 0){
				throw new CherryBatchException();
			}
			
		} catch(CherryBatchException e){
			BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO();
			batchExceptionDTO.setBatchName(this.getClass());
			batchExceptionDTO.setErrorCode("EOT00056");
			batchExceptionDTO.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
			// 产品唯一性编码K3Product
			batchExceptionDTO.addErrorParam(CherryBatchUtil .getString(itemMap.get("ItemCode")));
			// 厂商编码
			batchExceptionDTO.addErrorParam(CherryBatchUtil .getString(itemMap.get("U_OldItemNo")));
			// 产品条码
			batchExceptionDTO.addErrorParam(CherryBatchUtil .getString(itemMap.get("BarCode")));
			// 中文名
			batchExceptionDTO.addErrorParam(CherryBatchUtil .getString(itemMap.get("ItemDesc")));
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
			batchExceptionDTO.setErrorCode("EOT00057");
			batchExceptionDTO.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
			// 产品唯一性编码K3Product
			batchExceptionDTO.addErrorParam(CherryBatchUtil .getString(itemMap.get("ItemCode")));
			// 厂商编码
			batchExceptionDTO.addErrorParam(CherryBatchUtil .getString(itemMap.get("U_OldItemNo")));
			// 产品条码
			batchExceptionDTO.addErrorParam(CherryBatchUtil .getString(itemMap.get("BarCode")));
			// 中文名
			batchExceptionDTO.addErrorParam(CherryBatchUtil .getString(itemMap.get("ItemDesc")));
			throw new CherryBatchException(batchExceptionDTO);
		}
		
		// ********************** 验证当前barCode在促销品中不存在   **********************
		
		if (0 == oldPrmId) {
			// 插入操作
			optFlag = 1;
			try {
				// 将有效区分转换为BINBECMINC99共通SQL指定的属性名
				itemMap.put("validFlagVal", itemMap.get("validFlag"));
				// 插入促销产品信息
				prmProductId = binOTYIN13_Service.insertPromProductInfo(itemMap);
				// 把促销产品Id设入条件map
				itemMap.put("prmProductId", prmProductId);
				// 插入件数加一
				insertCount += 1;
			} catch (Exception e) {
				BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO();
				batchExceptionDTO.setBatchName(this.getClass());
				batchExceptionDTO.setErrorCode("EOT00018");
				batchExceptionDTO
						.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
				// 促销产品唯一性编码
				batchExceptionDTO.addErrorParam(CherryBatchUtil
						.getString(itemMap.get("ItemCode")));
				// 中文名
				batchExceptionDTO.addErrorParam(CherryBatchUtil
						.getString(itemMap.get("ItemDesc")));
				batchExceptionDTO.setException(e);
				throw new CherryBatchException(batchExceptionDTO);
			}
		} else {
			// 更新操作
			optFlag = 2;
			prmProductId = oldPrmId;
			itemMap.put("prmProductId", prmProductId);
			try {
				// 更新促销产品信息表
				binOTYIN13_Service.updatePromProductInfo(itemMap);
				// 更新件数加一
				updateCount += 1;
			} catch (Exception e) {
				BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO();
				batchExceptionDTO.setBatchName(this.getClass());
				batchExceptionDTO.setErrorCode("EOT00019");
				batchExceptionDTO
						.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
				// 促销产品唯一性编码
				batchExceptionDTO.addErrorParam(CherryBatchUtil
						.getString(itemMap.get("ItemCode")));
				// 中文名
				batchExceptionDTO.addErrorParam(CherryBatchUtil
						.getString(itemMap.get("ItemDesc")));
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
	private void updPrmVendor(Map<String, Object> itemMap)
			throws CherryBatchException {

		Map<String, Object> tempMap = new HashMap<String, Object>();
		tempMap.putAll(itemMap);
		
		try {
			
			if(1 == optFlag){
				// 将有效区分转换为BINBECMINC99共通SQL指定的属性名
				itemMap.put("validFlagVal", itemMap.get("validFlag"));
				// 添加产品厂商信息
				binOTYIN13_Service.insertPrmProductVendor(tempMap);
			} 
			else if(2 == optFlag){
				// 编辑产品厂商信息
				Map<String, Object> prtVendorMap = binOTYIN13_Service.getPrmProductVendorInfo(tempMap);
				int prmVendorId = (Integer)prtVendorMap.get("prmVendorId");
				tempMap.put("prmVendorId", prmVendorId);
				binOTYIN13_Service.updPrmVendor(tempMap);
				
				// 接口表validFlag
				String validFlagOt = CherryBatchUtil.getString(itemMap.get("validFlag"));
				// 新后台厂商表validFlag
				String validFlagBE = CherryBatchUtil.getString(prtVendorMap.get("validFlag"));
				
				// 新后台有效，接口表时无效则停用编码条码、接口表有效时更新编码条码
				// 新后台无效，接口表时无效则不做任何操作、接口表有效时不做任何操作（下一次下发时会新增编码条码）
				if("1".equals(validFlagBE) && "1".equals(validFlagOt)){
					// 更新促销产品条码对应关系信息
					binOTYIN13_Service.updPrmBarCode(tempMap);
				} 
				else if("1".equals(validFlagBE) && "0".equals(validFlagOt)){
					// 更新促销品停用日时
					binOTYIN13_Service.updatePrmClosingTime(tempMap);
				}
				
			}
			
		} catch (Exception e) {
			if (optFlag == 1) {
				insertCount--;
			} else if (optFlag == 2) {
				updateCount--;
			}
			BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO();
			batchExceptionDTO.setBatchName(this.getClass());
			batchExceptionDTO.setErrorCode("EOT00020");
			batchExceptionDTO.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
			// 产品唯一性编码
			batchExceptionDTO.addErrorParam(CherryBatchUtil
					.getString(tempMap.get("ItemCode")));
			// 厂商编码
			batchExceptionDTO.addErrorParam(CherryBatchUtil.getString(tempMap
					.get("U_OldItemNo")));
			// 产品条码
			batchExceptionDTO.addErrorParam(CherryBatchUtil.getString(tempMap
					.get("BarCode")));
			batchExceptionDTO.setException(e);
			throw new CherryBatchException(batchExceptionDTO);
		}
	}
	
	/**
	 * 程序结束时，处理Job共通( 插入Job运行履历表)
	 * @param paraMap
	 * @throws Exception
	 */
	private void programEnd(Map<String,Object> paraMap) throws Exception{
		 
		String targetDataStartTime = ConvertUtil.getString(paraMap.get("TargetDataStartTime"));
		if(!CherryBatchUtil.isBlankString(targetDataStartTime)){
			// 程序结束时，更新Job控制表 
			binbecm01_IF.updateJobControl(paraMap);
		}
		
		// 程序结束时，插入Job运行履历表
		paraMap.putAll(comMap);
		paraMap.put("flag", flag);
		paraMap.put("TargetDataCNT", totalCount);
		paraMap.put("SCNT", totalCount - failCount);
		paraMap.put("FCNT", failCount);
		paraMap.put("UCNT", updateCount);
		paraMap.put("ICNT", insertCount);
		paraMap.put("TargetDataStartTime", targetDataStartTime);
		paraMap.put("TargetDataEndTime", ConvertUtil.getString(paraMap.get("TargetDataEndTime")));
		paraMap.put("FReason", fReason);
		binbecm01_IF.insertJobRunHistory(paraMap);
	}
	
	/**
	 * init
	 * @param map
	 */
	private void init(Map<String, Object> map) throws CherryBatchException, Exception{
		comMap = getComMap(map);
		
		// BatchCD 来自VSS$/01.Cherry/02.设计文档/01.概要设计/00.各种一览/【新设】CherryBatch一览.xlsx
		map.put("JobCode", "BAT101");
		
		// 程序【开始运行时间】
		String runStartTime = binbecm01_IF.getSYSDateTime();
		
		// 作成日时
		map.put(CherryConstants.CREATE_TIME, runStartTime);
		
		// 取得Job控制程序的【数据截取开始时间】及【数据截取结束时间】
		String tpifRunStartTime = binOTYIN13_Service.getTpifSYSDateTime();
		Map<String, Object> selJobControlMap = new HashMap<String, Object>();
		selJobControlMap.putAll(comMap); // 加入组织ID、品牌ID
		selJobControlMap.put(CherryBatchConstants.RUN_STARTTIME, tpifRunStartTime); // 专门用于取得计算【截取数据时间】
		map.put(CherryBatchConstants.RUN_STARTTIME, tpifRunStartTime); // 用于程序结束时更新【截取数据时间】
		selJobControlMap.put("JobCode", map.get("JobCode"));  
		map.put("tpifRunStartTime", tpifRunStartTime); // 专门用于取得计算【截取数据时间】
		Map<String, Object> jobControlInfoMap = binbecm01_IF.getJobControlInfo(selJobControlMap);
		
		// 程序【截取数据开始时间】
		map.put("TargetDataStartTime", jobControlInfoMap.get("TargetDataStartTime"));
		// 程序【截取数据结束时间】
		map.put("TargetDataEndTime", jobControlInfoMap.get("TargetDataEndTime"));
		
		// 业务日期
		String bussDate = binOTYIN13_Service.getBussinessDate(map);
		map.put("businessDate", bussDate);
		
		// 业务日期+1
		String nextBussDate = DateUtil.addDateByDays(
				CherryBatchConstants.DATE_PATTERN, bussDate, 1);
		map.put("nextBussDate", nextBussDate);
		
		// 停用时间
		String closingTime = CherryUtil.suffixDate(bussDate, 1);
		map.put(ProductConstants.CLOSINGTIME, closingTime);
		
		// 品牌Code
		String branCode = binOTYIN13_Service.getBrandCode(map);
		map.put(ProductConstants.BRANDCODE, branCode);
		
		comMap.putAll(map);
		
		// 取得大中小分类对应的分类Id
		getCatePropIds(comMap);
	}

	/**
	 * 共通Map
	 * @param map
	 * @return
	 */
	private Map<String, Object> getComMap(Map<String, Object> map) {
		Map<String, Object> baseMap = new HashMap<String, Object>();
		
		// 更新程序名
		baseMap.put(CherryBatchConstants.UPDATEPGM, "BINOTYIN13");
		// 作成程序名
		baseMap.put(CherryBatchConstants.CREATEPGM, "BINOTYIN13");
		// 作成者
		baseMap.put(CherryBatchConstants.CREATEDBY,CherryBatchConstants.UPDATE_NAME);
		// 更新者
		baseMap.put(CherryBatchConstants.UPDATEDBY,CherryBatchConstants.UPDATE_NAME);
		// 所属组织
		baseMap.put(CherryBatchConstants.ORGANIZATIONINFOID, map.get(CherryBatchConstants.ORGANIZATIONINFOID).toString());
		// 品牌ID
		baseMap.put(CherryBatchConstants.BRANDINFOID, map.get(CherryBatchConstants.BRANDINFOID).toString());
		
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
		prtCatPropId_B = binOTYIN13_Service.getCatPropId2(temp);
		if (prtCatPropId_B == 0) {
			// 根据分类名查询分类Id
			prtCatPropId_B = binOTYIN13_Service.getCatPropId1(temp);
			if (prtCatPropId_B == 0) {
				// 添加大分类
				prtCatPropId_B = binOTYIN13_Service.addCatProperty(temp);
			} else {
				temp.put(ProductConstants.PRTCATPROPID, prtCatPropId_B);
				// 更新分类终端显示区分
				binOTYIN13_Service.updProp(temp);
			}
		}
		// 中分类名
		temp.put(ProductConstants.PROPNAMECN, ProductConstants.CATE_NAME_M);
		temp.put(ProductConstants.TEMINALFLAG, ProductConstants.CATE_TYPE_3);
		// 根据分类终端显示区分查询分类Id
		prtCatPropId_M = binOTYIN13_Service.getCatPropId2(temp);
		if (prtCatPropId_M == 0) {
			// 根据分类名查询分类Id
			prtCatPropId_M = binOTYIN13_Service.getCatPropId1(temp);
			if (prtCatPropId_M == 0) {
				// 添加中分类
				prtCatPropId_M = binOTYIN13_Service.addCatProperty(temp);
			} else {
				temp.put(ProductConstants.PRTCATPROPID, prtCatPropId_M);
				// 更新分类终端显示区分
				binOTYIN13_Service.updProp(temp);
			}
		}
		// 小分类名
		temp.put(ProductConstants.PROPNAMECN, ProductConstants.CATE_NAME_L);
		temp.put(ProductConstants.TEMINALFLAG, ProductConstants.CATE_TYPE_2);
		// 根据分类终端显示区分查询分类Id
		prtCatPropId_L = binOTYIN13_Service.getCatPropId2(temp);
		if (prtCatPropId_L == 0) {
			// 根据分类名查询分类Id
			prtCatPropId_L = binOTYIN13_Service.getCatPropId1(temp);
			if (prtCatPropId_L == 0) {
				// 添加小分类
				prtCatPropId_L = binOTYIN13_Service.addCatProperty(temp);
			} else {
				temp.put(ProductConstants.PRTCATPROPID, prtCatPropId_L);
				// 更新分类终端显示区分
				binOTYIN13_Service.updProp(temp);
			}
		}
		
		/*
		// 动态分类:U_SubCategory
		temp.put(ProductConstants.PROP_PROPERTY, "U_SubCategory");
		temp.put(ProductConstants.PROPNAMECN, "U_SubCategory");
		temp.put(ProductConstants.TEMINALFLAG, "0");
		prtCatPropId_D1 = binOTYIN13_Service.getCatPropId3(temp);
		if (prtCatPropId_D1 == 0) {
			// 添加动态分类:U_SubCategory
			prtCatPropId_D1 = binOTYIN13_Service.addCatProperty(temp);
		}
		
		// 动态分类:U_LineMF
		temp.put(ProductConstants.PROP_PROPERTY, "U_LineMF");
		temp.put(ProductConstants.PROPNAMECN, "U_LineMF");
		temp.put(ProductConstants.TEMINALFLAG, "0");
		prtCatPropId_D2 = binOTYIN13_Service.getCatPropId3(temp);
		if (prtCatPropId_D2 == 0) {
			// 添加动态分类:U_LineMF
			prtCatPropId_D2 = binOTYIN13_Service.addCatProperty(temp);
		}
		
		// 动态分类:U_LineCategory
		temp.put(ProductConstants.PROP_PROPERTY, "U_LineCategory");
		temp.put(ProductConstants.PROPNAMECN, "U_LineCategory");
		temp.put(ProductConstants.TEMINALFLAG, "0");
		prtCatPropId_D3 = binOTYIN13_Service.getCatPropId3(temp);
		if (prtCatPropId_D3 == 0) {
			// 添加动态分类:U_LineCategory
			prtCatPropId_D3 = binOTYIN13_Service.addCatProperty(temp);
		}
		*/
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
		
		// 处理品牌code
		BatchLoggerDTO batchLoggerDTO7 = new BatchLoggerDTO();
		batchLoggerDTO7.setCode("IIF00010");
		batchLoggerDTO7.setLevel(CherryBatchConstants.LOGGER_INFO);
		batchLoggerDTO7.addParam(String.valueOf(comMap.get(ProductConstants.BRANDCODE)));
		
		// 处理品牌
		logger.BatchLogger(batchLoggerDTO7);
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
			batchLoggerDTO6.setCode("EOT00021");
			batchLoggerDTO6.setLevel(CherryBatchConstants.LOGGER_INFO);
			batchLoggerDTO6.addParam(faildItemList.toString());
			logger.BatchLogger(batchLoggerDTO6);
			
			fReason = "产品导入部分数据处理失败，具体见log日志";
		}else{
			fReason = "品牌CODE：(" + String.valueOf(comMap.get(ProductConstants.BRANDCODE)) + ")" ;
		}
		
	}
	

}
