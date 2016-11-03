/*
 * @(#)BINBEIFPRO02_BL.java     1.0 2011/6/15
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

import com.cherry.cm.core.BatchExceptionDTO;
import com.cherry.cm.core.BatchLoggerDTO;
import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryBatchException;
import com.cherry.cm.core.CherryBatchLogger;
import com.cherry.cm.util.CherryBatchUtil;
import com.cherry.cm.util.DateUtil;
import com.cherry.ia.common.ProductConstants;
import com.cherry.ia.pro.service.BINBEIFPRO02_Service;

/**
 * 
 *产品下发BL
 * 
 * @author lipc
 * @version 1.0 2011/6/15
 */
public class BINBEIFPRO02_BL {

	/** BATCH LOGGER */
	private static CherryBatchLogger logger = new CherryBatchLogger(
			BINBEIFPRO02_BL.class);

	@Resource
	private BINBEIFPRO02_Service binbeifpro02Service;

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
	/** 共通map */
	private Map<String, Object> comMap;

	/**
	 * 产品列表的batch处理
	 * 
	 * @param 无
	 * 
	 * @return Map
	 * @throws CherryBatchException
	 */
	public int tran_batchProducts(Map<String, Object> map)
			throws CherryBatchException {
		// 初始化
		init(map);
		// 数据抽出次数
		int currentNum = 0;
		// 从接口数据库中分批取得产品列表，并处理
		while (true) {
			// 查询开始位置
			int startNum = CherryBatchConstants.DATE_SIZE * currentNum + 1;
			currentNum += 1;
			// 查询结束位置
			int endNum = startNum + CherryBatchConstants.DATE_SIZE - 1;
			map.put(CherryBatchConstants.START, startNum);
			map.put(CherryBatchConstants.END, endNum);
			map.put(CherryBatchConstants.SORT_ID, ProductConstants.UNITCODE);
			// 查询新后台产品列表
			List<Map<String, Object>> productsList = binbeifpro02Service
					.getProductList(map);
			if (CherryBatchUtil.isBlankList(productsList)) {
				break;
			}
			// 统计总条数
			totalCount += productsList.size();
			// 更新接口数据库
			updIFDatabase(productsList);
			// 产品数据少于一页，跳出循环
			if (productsList.size() < CherryBatchConstants.DATE_SIZE) {
				break;
			}
		}
		// 输出处理结果信息
		outMessage();
		return flag;
	}

	/**
	 * 更新接口数据库
	 * 
	 * @param list
	 */
	@SuppressWarnings("unchecked")
	private void updIFDatabase(List<Map<String, Object>> list) {
		for (Map<String, Object> map : list) {
			// 取得产品分类list
			List<Map<String, Object>> cateList = (List<Map<String, Object>>) map
					.get("list");
			// 取得产品分类信息Map
			Map<String, Object> cateMap = getCateMap(cateList);
			cateMap.putAll(map);
			try {
				// 保存接口产品表
				savePro(cateMap);
				// 事务提交
				binbeifpro02Service.ifManualCommit();
			} catch (Exception e) {
				// 失败件数加一
				failCount += 1;
				// 事务回滚
				binbeifpro02Service.ifManualRollback();
				flag = CherryBatchConstants.BATCH_WARNING;
			}
			// 插入产品条码对应关系表
			insertPrtBarCode(map);
		}
	}

	/**
	 * 插入产品条码对应关系表
	 * 
	 * @param map
	 */
	private void insertPrtBarCode(Map<String, Object> map) {
		// 查询产品条码对应关系件数
		int count = binbeifpro02Service.getBarCodeCount(map);
		if (count == 0) {
			map.putAll(comMap);
			try {  
				// 插入变化的unitcode barcode 
				Map<String,Object> barCodeModifyMap = binbeifpro02Service.getBarCodeModify(map);
				if(null != barCodeModifyMap){
					Map<String,Object> insertPrtSettingMap = new HashMap<String, Object>();
					insertPrtSettingMap.put("brand", map.get(CherryBatchConstants.BRAND_CODE)); // 品牌code
					insertPrtSettingMap.put("new_barcode", barCodeModifyMap.get("BarCode")); // 产品条码
					insertPrtSettingMap.put("new_unitcode", barCodeModifyMap.get("UnitCode")); // 产品编码
					insertPrtSettingMap.put("old_barcode", barCodeModifyMap.get("OldBarCode")); // 老产品条码
					insertPrtSettingMap.put("old_unitcode", barCodeModifyMap.get("OldUnitCode")); // 老产品编码
					insertPrtSettingMap.put("prt_id", barCodeModifyMap.get("BIN_ProductVendorID"));  // 产品厂商ID（新后台）
					insertPrtSettingMap.put("prt_type", "N"); // 产品（N）/促销品区分(P)
					insertPrtSettingMap.put("enable_time", map.get("enable_time")); // 新产品生效日期
					// 插入ProductSetting
					binbeifpro02Service.addProductSetting(insertPrtSettingMap);
				}
				
				// 更新停用日时
				binbeifpro02Service.updateClosingTime(map);
				// 插入产品条码对应关系表
				Map<String,Object> praMap = new HashMap<String,Object>();
				praMap.putAll(map);
				praMap.remove("validFlagVal");
				binbeifpro02Service.insertPrtBarCode(praMap);
				binbeifpro02Service.manualCommit();
			} catch (Exception e) {
				binbeifpro02Service.manualRollback();
				BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO();
				batchExceptionDTO.setBatchName(this.getClass());
				batchExceptionDTO.setErrorCode("EIF00013");
				batchExceptionDTO
						.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
				// 品牌CODE
				batchExceptionDTO.addErrorParam(CherryBatchUtil.getString(comMap.get(ProductConstants.BRANDCODE)));
				// 产品厂商ID
				batchExceptionDTO.addErrorParam(CherryBatchUtil.getString(map.get("prtVendorId")));
				// 产品编码
				batchExceptionDTO.addErrorParam(CherryBatchUtil.getString(map.get(ProductConstants.UNITCODE)));
				// 产品条码
				batchExceptionDTO.addErrorParam(CherryBatchUtil.getString(map.get(ProductConstants.BAR_CODE)));
				// 中文名
				batchExceptionDTO.addErrorParam(CherryBatchUtil.getString(map.get(ProductConstants.NAMETOTAL)));
				batchExceptionDTO.setException(e);
				flag = CherryBatchConstants.BATCH_WARNING;
			}
		}
	}

	/**
	 * 取得产品分类信息Map
	 * 
	 * @param list
	 * @return
	 */
	private Map<String, Object> getCateMap(List<Map<String, Object>> list) {
		Map<String, Object> cateMap = new HashMap<String, Object>();
		if (!CherryBatchUtil.isBlankList(list)) {
			for (Map<String, Object> map : list) {
				// 分类类型
				String cateType = CherryBatchUtil.getString(map
						.get(ProductConstants.CATE_TYPE));
				// 分类代码
				String cateCode = CherryBatchUtil.getString(map
						.get(ProductConstants.CATE_CODE));
				// 分类名
				String cateName = CherryBatchUtil.getString(map
						.get(ProductConstants.CATE_NAME));
				if (ProductConstants.CATE_TYPE_1.equals(cateType)) {
					// 大分类
					cateMap.put(ProductConstants.BCLASSCODE, cateCode);
					cateMap.put(ProductConstants.BCLASSNAME, cateName);
					
				} else if (ProductConstants.CATE_TYPE_3.equals(cateType)) {
					// 中分类
					cateMap.put(ProductConstants.MCLASSCODE, cateCode);
					cateMap.put(ProductConstants.MCLASSNAME, cateName);
				} else if (ProductConstants.CATE_TYPE_2.equals(cateType)) {
					// 小分类
					cateMap.put(ProductConstants.LCLASSCODE, cateCode);
					cateMap.put(ProductConstants.LCLASSNAME, cateName);
				}
			}
		}
		return cateMap;
	}

	/**
	 * 插入接口数据库产品表
	 * 
	 * @param map
	 * @return
	 */
	private void savePro(Map<String, Object> productMap)
			throws CherryBatchException {
		try {
			// 插入产品接口表
			binbeifpro02Service.addProduct(productMap);
			// 插入件数加一
			insertCount += 1;
		} catch (Exception e) {
			BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO();
			batchExceptionDTO.setBatchName(this.getClass());
			batchExceptionDTO.setErrorCode("EIF00011");
			batchExceptionDTO.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
			// 品牌CODE
			batchExceptionDTO.addErrorParam(CherryBatchUtil.getString(comMap.get(ProductConstants.BRANDCODE)));
			// 产品厂商ID
			batchExceptionDTO.addErrorParam(CherryBatchUtil.getString(productMap.get("prtVendorId")));
			// 产品编码
			batchExceptionDTO.addErrorParam(CherryBatchUtil.getString(productMap.get(ProductConstants.UNITCODE)));
			// 产品条码
			batchExceptionDTO.addErrorParam(CherryBatchUtil.getString(productMap.get(ProductConstants.BAR_CODE)));
			// 中文名
			batchExceptionDTO.addErrorParam(CherryBatchUtil.getString(productMap.get(ProductConstants.NAMETOTAL)));
			batchExceptionDTO.setException(e);
			throw new CherryBatchException(batchExceptionDTO);
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
		//BatchLoggerDTO batchLoggerDTO4 = new BatchLoggerDTO();
		//batchLoggerDTO4.setCode("IIF00004");
		//batchLoggerDTO4.setLevel(CherryBatchConstants.LOGGER_INFO);
		//batchLoggerDTO4.addParam(String.valueOf(updateCount));
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
		//logger.BatchLogger(batchLoggerDTO4);
		// 成功总件数
		logger.BatchLogger(batchLoggerDTO2);
		// 失败件数
		logger.BatchLogger(batchLoggerDTO5);
	}

	private void init(Map<String, Object> map) {
		// 业务日期，日结标志
		Map<String, Object> bussDateMap = binbeifpro02Service
				.getBussinessDateMap(map);
		// 业务日期
		String businessDate = CherryBatchUtil.getString(bussDateMap
				.get(CherryBatchConstants.BUSINESS_DATE));
		map.put("businessDate", businessDate);
		// 日结标志
		String closeFlag = CherryBatchUtil.getString(bussDateMap
				.get("closeFlag"));
		// 业务日期+1
		String nextBussDate = DateUtil.addDateByDays(
				CherryBatchConstants.DATE_PATTERN, businessDate, 1);
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
		// 取得更新共通信息map
		comMap = getComMap(map);
		// 启用日时
		comMap.put(ProductConstants.STARTTIME, startTime);
		// 停用日时
		comMap.put(ProductConstants.CLOSINGTIME, closingTime);
		// 新产品生效日期
		String enable_time = DateUtil.suffixDate(businessDate, 1);
		comMap.put("enable_time", enable_time);
		// 物理删除接口数据库某品牌的产品信息
		binbeifpro02Service.delIFProduct(comMap);
	}

	private Map<String, Object> getComMap(Map<String, Object> map) {
		Map<String, Object> baseMap = new HashMap<String, Object>();
		// 品牌Code
		String branCode = binbeifpro02Service.getBrandCode(map);
		// 系统时间
		String sysTime = binbeifpro02Service.getSYSDate();

		// 品牌Code
		baseMap.put(ProductConstants.BRANDCODE, branCode);
		// 作成时间
		baseMap.put(CherryBatchConstants.CREATE_TIME, sysTime);
		// 更新时间
		baseMap.put(CherryBatchConstants.UPDATE_TIME, sysTime);
		// 更新程序名
		baseMap.put(CherryBatchConstants.UPDATEPGM, "BINBEIFPRO02");
		// 作成程序名
		baseMap.put(CherryBatchConstants.CREATEPGM, "BINBEIFPRO02");
		// 作成者
		baseMap.put(CherryBatchConstants.CREATEDBY,
				CherryBatchConstants.UPDATE_NAME);
		// 更新者
		baseMap.put(CherryBatchConstants.UPDATEDBY,
				CherryBatchConstants.UPDATE_NAME);
		// 有效区分
		baseMap.put("validFlagVal", "0");
		return baseMap;
	}
}
