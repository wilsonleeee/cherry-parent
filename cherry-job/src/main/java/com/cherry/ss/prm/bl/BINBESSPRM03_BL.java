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
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.CherryBatchUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.DateUtil;
import com.cherry.ss.prm.service.BINBESSPRM03_Service;

/**
 * 
 *促销品下发BL
 * 
 * 
 * @author hub
 * @version 1.0 2010.12.20
 */
public class BINBESSPRM03_BL {

	@Resource
	private BINBESSPRM03_Service binbessprm03Service;

	/** BATCH处理标志 */
	private int flag = CherryBatchConstants.BATCH_SUCCESS;

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
	public int tran_batchPromPrt(Map<String, Object> map) throws CherryBatchException {
		// 处理虚拟促销品
		virtualPrm(map);
		
		// 业务日期
		String closeDate = binbessprm03Service.getBussinessDate(map);
		Map<String, Object> bussDateMap = binbessprm03Service.getBussinessDateMap(map);
		String bussDate = (String) bussDateMap.get(CherryBatchConstants.BUSINESS_DATE);
		// 日结标志
		String closeFlag = CherryBatchUtil.getString(bussDateMap.get("closeFlag"));
		if("1".equals(closeFlag)){
			bussDate = DateUtil.addDateByDays(CherryBatchConstants.DATE_PATTERN, bussDate, 1);
		}
		map.put("bussDate", bussDate);
		// 取得促销产品总数
		int promPrtCount = binbessprm03Service.getPromPrtCount(map);	
		if (promPrtCount > 0) {
			try {
				// 清空促销品中间表数据
				binbessprm03Service.clearPromotionTable(map);
				// 提交事务
				binbessprm03Service.ifManualCommit();
			} catch (Exception e) {

				BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO();
				batchExceptionDTO.setBatchName(this.getClass());
				batchExceptionDTO.setErrorCode("ESS00004");
				batchExceptionDTO.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
				batchExceptionDTO.setException(e);
				flag = CherryBatchConstants.BATCH_WARNING;
				try {
					// 事务回滚
					binbessprm03Service.ifManualRollback();
				} catch (Exception ex) {	
					
				}
				
				throw new CherryBatchException(batchExceptionDTO);
			}
			// 停用日时
			String closingTime = DateUtil.suffixDate(closeDate, 1);
			// 启用日时
			String startTime = DateUtil.suffixDate(bussDate, 0);
			Map<String, Object> baseMap = new HashMap<String, Object>();
			// 作成者
			baseMap.put(CherryBatchConstants.CREATEDBY, CherryBatchConstants.UPDATE_NAME);
			// 作成程序名
			baseMap.put(CherryBatchConstants.CREATEPGM, "BINBESSPRM03");
			// 更新者
			baseMap.put(CherryBatchConstants.UPDATEDBY, CherryBatchConstants.UPDATE_NAME);
			// 更新程序名
			baseMap.put(CherryBatchConstants.UPDATEPGM, "BINBESSPRM03");
			// 有效区分
			baseMap.put("validFlagVal", "0");
			baseMap.put("startTime", startTime);
			baseMap.put("closingTime", closingTime);
			// 新产品生效日期
			String enable_time = DateUtil.suffixDate(closeDate, 1);
			baseMap.put("enable_time", enable_time);

			// 从接口数据库中分批取得柜台列表，并处理
			// 数据查询长度
			int dataSize = CherryBatchConstants.DATE_SIZE;
			// 数据抽出次数
			int currentNum = 0;
			// 查询开始位置
			int startNum = 0;
			// 查询结束位置
			int endNum = 0;
			// 排序字段
			map.put(CherryBatchConstants.SORT_ID, "UnitCode");
			while (true) {
				// 查询开始位置
				startNum = dataSize * currentNum + 1;
				// 查询结束位置
				endNum = startNum + dataSize - 1;
				// 数据抽出次数累加
				currentNum++;
				// 查询开始位置
				map.put(CherryBatchConstants.START, startNum);
				// 查询结束位置
				map.put(CherryBatchConstants.END, endNum);
				// 查询促销品信息
				List<Map<String, Object>> promPrtList = binbessprm03Service.getPromPrtList(map);
				// 柜台数据不为空
				if (!CherryBatchUtil.isBlankList(promPrtList)) {
					// 促销品下发主处理
					exportPromPrt(promPrtList, baseMap);
					// 促销品数据少于一次抽取的数量，即为最后一页，跳出循环
					if(promPrtList.size() < dataSize) {
						break;
					}
				} else {
					break;
				}
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
		
		return flag;
	}
	
	/**
	 * 处理品牌在没有虚拟促销品的情况下，新增虚拟促销品
	 * 'TZZK999999','CXLP999999'
	 * 2013/08/27  虚拟促销品（CXLP999999）添加功能废除
	 * @param map
	 */
	private void virtualPrm(Map<String, Object> map){
		
		// 取得"套装折扣"及"虚拟促销品"的数据
		List<String> virtualPrmUnitCodeList = binbessprm03Service.getVirtualPrmList(map);
		
		// 若促销产品表中没有"套装折扣"及"虚拟促销品"的的数据则新增数据
		if(!virtualPrmUnitCodeList.contains(TZZK_CODE)){
			// 插入TZZK999999
			Map<String, Object> tzzkMap = new HashMap<String,Object>();
			tzzkMap.put(CherryBatchConstants.UNITCODE, TZZK_CODE);
			tzzkMap.put(CherryBatchConstants.BARCODE, TZZK_CODE);
			tzzkMap.put("nameTotal", "套装折扣");
			tzzkMap.put("promCate", "TZZK");
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
		prmMap.put(CherryBatchConstants.CREATEPGM, "BINBESSPRM03");
		// 更新者
		prmMap.put(CherryBatchConstants.UPDATEDBY, CherryBatchConstants.UPDATE_NAME);
		// 更新程序名
		prmMap.put(CherryBatchConstants.UPDATEPGM, "BINBESSPRM03");
		
		prmMap.put(CherryBatchConstants.ORGANIZATIONINFOID, paramMap.get("organizationInfoId"));
		prmMap.put(CherryBatchConstants.BRANDINFOID, paramMap.get("brandInfoId"));
		prmMap.put("isStock", "0"); // 是否管理库存
		
		int prmId = binbessprm03Service.insertPromotionProductBackId(prmMap);
		
		prmMap.put("promProductId", prmId);  // 促销品ID
		prmMap.put("manuFactId", 1); // 生产厂商ID
		
		binbessprm03Service.insertPromProductVendor(prmMap);
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
			Map<String, Object> baseMap) throws CherryBatchException {
		totalCount += promPrtList.size();
		Map<String, Object> insertMap = new HashMap<String, Object>();		
		for (Map<String, Object> promPrt : promPrtList) {
			// 促销产品类别
			String promotionCateCd = (String) promPrt.get("promotionCateCd");
			insertMap.put("promotionCateCd", promotionCateCd);
			// 促销产品大类名称
			insertMap.put("promPrtBClassName", CherryBatchConstants.PROMOTION_CODE_NAME
					.getName(promotionCateCd));
			// 促销产品大类代码
			insertMap.put("promPrtBClassCode", CherryBatchConstants.PROMOTION_CODE_NAME
					.getCode(promotionCateCd));
			// 促销产品中类名称
			insertMap.put("promPrtMClassName", CherryBatchConstants.PROMOTION_CODE_NAME
					.getName(promotionCateCd));
			// 促销产品中类代码
			insertMap.put("promPrtMClassCode", CherryBatchConstants.PROMOTION_CODE_NAME
					.getCode(promotionCateCd));
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
				insertMap.put("promPrtLClassName", CherryBatchConstants.PROMOTION_CODE_NAME
						.getName(promotionCateCd));
			}
			// 兑换所需积分
			insertMap.put("exPoint", exPoint);
			// 促销产品小类代码
			insertMap.put("promPrtLClassCode", CherryBatchConstants.PROMOTION_CODE_NAME
					.getCode(promotionCateCd));
			// 库存管理
			insertMap.put("promPrtStock", promPrt.get("isStock"));
			// 品牌代码
			String brandCode = (String) promPrt.get("brandCode");
			// 厂商编码
			String unitCode = (String) promPrt.get("unitCode");
			// 品牌代码
			insertMap.put("brandCode", brandCode);
			// 促销品全称
			String nameTotal = (String) promPrt.get("nameTotal");
			if (null != nameTotal && nameTotal.length() > 40) {
				nameTotal = nameTotal.substring(0, 40);
			}
			// 促销产品代码
			insertMap.put("promPrtCode", nameTotal);
			// 销售价格
			int salePrice = 0;
			if (null != promPrt.get("salePrice")) {
				salePrice = (int) Math.round(Double.parseDouble(String.valueOf(promPrt.get("salePrice"))));
			}
			insertMap.put("promPrtPrice", salePrice);
			// 促销产品条码
			String barCode = (String) promPrt.get("barCode");
			insertMap.put("promPrtBarcode", barCode);
			// 促销产品厂商编码
			insertMap.put("promPrtUnitcode", unitCode);
			try {
				// 插入促销品中间表
				binbessprm03Service.insertPromotionProduct(insertMap);
				// 提交事务
				binbessprm03Service.ifManualCommit();
				try {
					// 查询对应关系件数
					int count = binbessprm03Service.getBarCodeCount(promPrt);
					if (0 == count) {
						promPrt.putAll(baseMap);
						
						// 查询变化的编码条码
						Map<String,Object> barCodeModifyMap = binbessprm03Service.getBarCodeModify(promPrt);
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
							binbessprm03Service.addProductSetting(insertPrtSettingMap);
						}
						
						// 更新停用日时
						binbessprm03Service.updateClosingTime(promPrt);
						// 插入促销产品条码对应关系表
						Map<String,Object> praMap = new HashMap<String,Object>();
						praMap.putAll(promPrt);
						praMap.remove("validFlagVal");
						// 插入编码条码
						binbessprm03Service.insertPromotionPrtBarCode(praMap);
						binbessprm03Service.manualCommit();
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
					try {
						binbessprm03Service.manualRollback();
					} catch (Exception ex) {	
						
					}
					continue;
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
				
				try {
					// 事务回滚
					binbessprm03Service.ifManualRollback();
				} catch (Exception ex) {	
					
				}
				
				continue;
			}
		}
	}
}
