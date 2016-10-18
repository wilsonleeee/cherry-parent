/*
 * @(#)BINOLPTJCS07_BL.java     1.0 2011/04/28
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
package com.cherry.pt.jcs.bl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.cache.annotation.CacheEvict;

import com.cherry.cm.cmbussiness.bl.BINOLCM15_BL;
import com.cherry.cm.cmbussiness.service.BINOLCM12_Service;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.DateUtil;
import com.cherry.pt.common.ProductConstants;
import com.cherry.pt.jcs.interfaces.BINOLPTJCS07_IF;
import com.cherry.pt.jcs.service.BINOLPTJCS03_Service;
import com.cherry.pt.jcs.service.BINOLPTJCS04_Service;
import com.cherry.pt.jcs.service.BINOLPTJCS07_Service;
import com.googlecode.jsonplugin.JSONUtil;

/**
 * 产品编辑 BL
 * 
 * @author lipc
 * @version 1.0 2011.04.28
 */
public class BINOLPTJCS07_BL implements BINOLPTJCS07_IF {

	@Resource(name="binOLCM12_Service")
	private BINOLCM12_Service binOLCM12_Service;

	@Resource(name="binOLPTJCS03_Service")
	private BINOLPTJCS03_Service binOLPTJCS03_Service;

	@Resource(name="binOLPTJCS07_Service")
	private BINOLPTJCS07_Service binOLPTJCS07_Service;
	
	@Resource
	private BINOLPTJCS04_Service binOLPTJCS04_Service;
	
	/** 各类编号取号共通BL */
	@Resource(name="binOLCM15_BL")
	private BINOLCM15_BL binOLCM15_BL;

	/**
	 * 更新产品信息
	 * 
	 * @param map
	 * @return
	 * 
	 * */
	@Override
	@SuppressWarnings("unchecked")
	@CacheEvict(value="CherryProductCache",allEntries=true,beforeInvocation=false)
	public int tran_updProduct(Map<String, Object> map) throws Exception {
		int result;
		// Map添加更新共通信息
		map = getUpdMap(map);
		String codeStr = ConvertUtil.getString(map.get(CherryConstants.BARCODE));
		List<Map<String, Object>> barCodeList = (List<Map<String, Object>>) JSONUtil.deserialize(codeStr);
		
		// 新产品编码
		String newUnitCode = ConvertUtil.getString(map.get(CherryConstants.UNITCODE));
		// 原产品编码
		String oldUnitCode = ConvertUtil.getString(map.get(ProductConstants.OLD_UNITCODE));
		// 若新老编码忽略大小写后一致则使用老编码更新产品表（为了保持与条码相同的处理）
		if(newUnitCode.equalsIgnoreCase(oldUnitCode)){
			map.put("unitCode", oldUnitCode);
		}
		// 取得当前产品表的表版本号
		Map<String, Object> seqMap = new HashMap<String, Object>();
		seqMap.putAll(map);
		seqMap.put("type", "E");
		String tVersion = binOLCM15_BL.getCurrentSequenceId(seqMap);
		map.put("tVersion", tVersion);
		// 产品属性信息更新
		result = binOLPTJCS07_Service.updProduct(map);
		
		// 取得当前部门(柜台)产品表版本号
		seqMap.put("type", "F");
		String pdTVersion = binOLCM15_BL.getCurrentSequenceId(seqMap);
		map.put("pdTVersion", pdTVersion);
		// 产品变动后更新产品方案明细表的version字段
		binOLPTJCS04_Service.updPrtSolutionDetail(map);
		
		if (result == 0) {
			return 0;
		}
		// 产品条码更新
		int prtVendorId = updPrtVendor(map);
		// 停用时,不再进行以下处理，直接return
		if (CherryConstants.VALIDFLAG_ENABLE.equals(map.get("validFlag"))) {
			// 产品扩展属性信息更新
			updPrtExtValue(map);
			// // =========== 更新产品BOM信息 ============ //
			updPrtBOM(map, barCodeList, prtVendorId);
			// 产品价格更新
			updProductPrice(map);
			// 产品分类更新
			updPrtCategory(map);
			// 保存产品图片
			addImage(map);
		}
		return result;
	}

	/**
	 * 产品扩展属性信息更新
	 * 
	 * @param map
	 * @throws Exception
	 */
	private void updPrtExtValue(Map<String, Object> map) throws Exception {
		// 删除产品扩展属性值表
		binOLCM12_Service.delPrtExtValue(map);
		// 插入产品扩展属性值表
		binOLCM12_Service.insertPrtExtValue(map);
	}

	/**
	 * 产品条码更新
	 * 
	 * @param map
	 */
	@SuppressWarnings("unchecked")
	private int updPrtVendor(Map<String, Object> map) throws Exception {
		// BOM产品产品厂商ID(产品条码唯一)
		int prtVendorId = 0;
		String codeStr = ConvertUtil
				.getString(map.get(CherryConstants.BARCODE));
		// 新产品编码
		String newUnitCode = ConvertUtil.getString(map
				.get(CherryConstants.UNITCODE));
		// 原产品编码
		String oldUnitCode = ConvertUtil.getString(map
				.get(ProductConstants.OLD_UNITCODE));
		// 产品对应的所有条码(包含无效)
		String barCodeInfo = ConvertUtil.getString(map
				.get(ProductConstants.BARCODE_INFO));
		// 业务日期
		String bussDate = binOLPTJCS07_Service.getBusDate(map);
		
		String sysdateTime = binOLPTJCS07_Service.getSYSDateTime();
		String sysHHSSMM = DateUtil.getSpecificDate(sysdateTime,DateUtil.TIME_PATTERN );
		// 停用时间
		String closingTime = bussDate + " " + sysHHSSMM;
		
		Map<String, Object> paramMap = new HashMap<String, Object>();
		List<Map<String, Object>> barCode = (List<Map<String, Object>>) JSONUtil
				.deserialize(codeStr);
		// 产品对应的所有条码(包含无效)List
		List<Map<String, Object>> allList = (List<Map<String, Object>>) JSONUtil
				.deserialize(barCodeInfo);
		// 新添加的产品条码List
		List<Map<String, Object>> addCodeList = getAddCodeList(barCode, allList);
		// 更新的产品条码
		List<Map<String, Object>> updCodeList = getUpdCodeList(barCode);
		// 不变的产品条码
		List<Map<String, Object>> unChangeCodeList = getUnChangeCodeList(
				barCode, allList);
		// 删除的产品条码
		List<Map<String, Object>> delCodeList = getDelCodeList(barCode);
		map.remove(CherryConstants.BARCODE);
		
		// ********* 2012-11-05 产品维护增加产品停用功能(NEWWITPOS-1654) start *********//
		// 停用
		if (CherryConstants.VALIDFLAG_DISABLE.equals(map.get("validFlag"))) {
			for(Map<String, Object>closeTimeMap : allList){
				Map<String, Object> paramsMap = new HashMap<String, Object>();
				paramsMap.put("closingTime", closingTime);
				paramsMap.put("prtVendorId", closeTimeMap.get("prtVendorId"));
				paramsMap.put(CherryConstants.VALID_FLAG, CherryConstants.VALIDFLAG_DISABLE);
				// 更新产品厂商的validFlag为0
				binOLPTJCS07_Service.updPrtVendor(paramsMap);
				// 更新关系表的ClosingTime字段为当前时间（业务时间）
				binOLPTJCS07_Service.updateClosingTime(paramsMap);
			}
			return prtVendorId;
		}
		// ********* 2012-11-05 产品维护增加产品停用功能(NEWWITPOS-1654) end *********//
		
		// =========== 添加的产品条码 ============ //
		for (Map<String, Object> code : addCodeList) {
			code.putAll(map);
			prtVendorId = binOLPTJCS03_Service.insertProductVendor(code);
		}
		// =========== 更新的产品条码 ============ //
		for (Map<String, Object> updMap : updCodeList) {
			paramMap.clear();
			paramMap.putAll(map);
			paramMap.putAll(updMap);
			binOLPTJCS07_Service.updPrtVendor(paramMap);
			// 更新产品条码对应关系信息
			this.updPrtBarCode(paramMap);
		}
		// =========== 删除的产品条码 ============ //
		for (Map<String, Object> delMap : delCodeList) {
			delMap.putAll(map);
			delMap.put(CherryConstants.VALID_FLAG, 0);
			binOLPTJCS07_Service.updPrtVendor(delMap);
			// 更新产品条码对应关系信息
			delMap.put(ProductConstants.CLOSING_TIME, closingTime);
			binOLPTJCS07_Service.updPrtBarCode(delMap);
		}
		// =========== 不变的产品条码 ============ //
		for (Map<String, Object> unMap : unChangeCodeList) {
			String validFlag = ConvertUtil.getString(unMap
					.get(CherryConstants.VALID_FLAG));
			unMap.putAll(map);
			if ("0".equals(validFlag)) {
				// 无效的产品厂商ID
				prtVendorId = CherryUtil.obj2int(unMap
						.get(ProductConstants.PRT_VENDORID));
				unMap.put(CherryConstants.VALID_FLAG, 1);
				binOLPTJCS07_Service.updPrtVendor(unMap);
				// 更新停用日时
				// 2013.06.19 此处应该不用将ClosingTime置为null,因为产品下发的时间还是会新增一条记录的，待产品下发对应的进行测试
				//binOLPTJCS07_Service.updClosingTime(unMap);
			}
			if (!newUnitCode.equalsIgnoreCase(oldUnitCode)) {
				// 更新产品条码对应关系信息
				this.updPrtBarCode(unMap);
			}
		}
		return prtVendorId;
	}

	/**
	 * 取得新添加的产品条码
	 * 
	 * @param barCode
	 * @param allList
	 * @return
	 */
	private List<Map<String, Object>> getAddCodeList(
			List<Map<String, Object>> barCode, List<Map<String, Object>> allList) {
		// 产品条码信息添加操作List
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		for (Map<String, Object> code : barCode) {
			boolean isContains = false;
			// 操作
			String option = ConvertUtil.getString(code
					.get(ProductConstants.OPTION));
			if ("".equals(option)) {
				// 新产品条码
				String nbarCode = ConvertUtil.getString(code
						.get(CherryConstants.BARCODE));
				for (Map<String, Object> map : allList) {
					// 原产品条码
					String obarCode = ConvertUtil.getString(map
							.get(CherryConstants.BARCODE));
					if (nbarCode.equalsIgnoreCase(obarCode)) {
						isContains = true;
						break;
					}
				}
				if (!isContains) {
					list.add(code);
				}
			}
		}
		return list;
	}

	/**
	 * 取得更新产品条码
	 * 
	 * @param barCode
	 * @param allList
	 * @return
	 */
	private List<Map<String, Object>> getUpdCodeList(
			List<Map<String, Object>> barCode) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		for (Map<String, Object> code : barCode) {
			// 操作
			String option = ConvertUtil.getString(code
					.get(ProductConstants.OPTION));
			// 新产品条码
			String nbarCode = ConvertUtil.getString(code
					.get(CherryConstants.BARCODE));
			// 原产品条码
			String obarCode = ConvertUtil.getString(code
					.get(ProductConstants.OLD_BARCODE));
			if (ProductConstants.OPTION_1.equals(option)
					&& !nbarCode.equalsIgnoreCase(obarCode)) {
				list.add(code);
			}
		}
		return list;
	}

	/**
	 * 取得删除的产品条码
	 * 
	 * @param barCode
	 * @param allList
	 * @return
	 */
	private List<Map<String, Object>> getDelCodeList(
			List<Map<String, Object>> barCode) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		for (Map<String, Object> code : barCode) {
			// 操作
			String option = ConvertUtil.getString(code
					.get(ProductConstants.OPTION));
			if (ProductConstants.OPTION_2.equals(option)) {
				list.add(code);
			}
		}
		return list;
	}

	/**
	 * 取得不变的，重新启用的产品条码
	 * 
	 * @param barCode
	 * @param allList
	 * @return
	 */
	private List<Map<String, Object>> getUnChangeCodeList(
			List<Map<String, Object>> barCode, List<Map<String, Object>> allList) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		for (Map<String, Object> code : barCode) {
			// 操作
			String option = ConvertUtil.getString(code
					.get(ProductConstants.OPTION));
			// 新产品条码
			String nbarCode = ConvertUtil.getString(code
					.get(CherryConstants.BARCODE));
			// 原产品条码
			String obarCode = ConvertUtil.getString(code
					.get(ProductConstants.OLD_BARCODE));
			if (nbarCode.equalsIgnoreCase(obarCode)
					&& ProductConstants.OPTION_1.equals(option)) {
				list.add(code);
			} else if (CherryConstants.BLANK.equals(option)) {
				// 添加操作
				for (Map<String, Object> map : allList) {
					// 无效barCode
					if ("0".equals(ConvertUtil.getString(map
							.get(CherryConstants.VALID_FLAG)))) {
						// 新产品条码=无效barCode
						if (ConvertUtil.getString(
								map.get(CherryConstants.BARCODE))
								.equalsIgnoreCase(nbarCode)) {
							list.add(map);
							break;
						}
					}
				}
			}
		}
		return list;
	}

	/**
	 * 产品价格更新
	 * 
	 * @param map
	 */
	@SuppressWarnings("unchecked")
	private void updProductPrice(Map<String, Object> map) throws Exception {
		String priceInfo = ConvertUtil.getString(map
				.get(ProductConstants.PRICE_INFO));
		if (!CherryConstants.BLANK.equals(priceInfo)) {
			// 标准价格信息List
			List<Map<String, Object>> priceInfoList = (List<Map<String, Object>>) JSONUtil
					.deserialize(priceInfo);
			// =======2012-04-13 lipc 多时间段价格处理 开始============= //
//			if (null != priceInfoList) {
//				for (Map<String, Object> price : priceInfoList) {
//					Map<String, Object> priceMap = CherryUtil
//							.removeEmptyVal(price);
//					// 操作
//					String option = ConvertUtil.getString(priceMap
//							.get(ProductConstants.OPTION));
//					priceMap.putAll(map);
//					if (CherryChecker.isNullOrEmpty(priceMap
//							.get(ProductConstants.PRICEENDDATE))) {
//						priceMap.put(ProductConstants.PRICEENDDATE,
//								CherryConstants.longLongAfter);
//					}
//					if (CherryConstants.BLANK.equals(option)) {
//						// 插入产品销售价格
//						binOLPTJCS03_Service.insertProductPrice(priceMap);
//					} else if (ProductConstants.OPTION_1.equals(option)) {
//						// 更新产品销售价格
//						binOLPTJCS07_Service.updProductPrice(priceMap);
//					} else if (ProductConstants.OPTION_2.equals(option)) {
//						// 删除产品销售价格
//						binOLPTJCS07_Service.delProductPrice(priceMap);
//					}
//				}
//			}
			if (null != priceInfoList) {
				int size = priceInfoList.size();
				for (int i = 0; i < size; i++) {
					Map<String, Object> priceMap = priceInfoList.get(i);
					priceMap = CherryUtil.removeEmptyVal(priceMap);
					// 操作
					String option = ConvertUtil.getString(priceMap
							.get(ProductConstants.OPTION));
					// 价格失效日期为空
					if (CherryChecker.isNullOrEmpty(priceMap
							.get(ProductConstants.PRICEENDDATE))) {
						// 最后一个价格段
						if (i == size - 1) {
							priceMap.put(ProductConstants.PRICEENDDATE,
									CherryConstants.longLongAfter);
						} else {
							// 当前价格日期段的next日期段价格信息
							Map<String, Object> priceMap2 = priceInfoList
									.get(i + 1);
							String priceStartDate2 = ConvertUtil
									.getString(priceMap2
											.get(ProductConstants.PRICESTARTDATE));
							// 当前价格的失效日期为next价格段的生效日期-1
							priceMap.put(ProductConstants.PRICEENDDATE,
									DateUtil.addDateByDays(
											CherryConstants.DATE_PATTERN,
											priceStartDate2, -1));
						}
					}
					priceMap.putAll(map);
					if (CherryConstants.BLANK.equals(option)) {
						// 插入产品销售价格
						binOLPTJCS03_Service.insertProductPrice(priceMap);
					} else if (ProductConstants.OPTION_1.equals(option)) {
						// 更新产品销售价格
						binOLPTJCS07_Service.updProductPrice(priceMap);
					} else if (ProductConstants.OPTION_2.equals(option)) {
						// 删除产品销售价格
						binOLPTJCS07_Service.delProductPrice(priceMap);
					}
				}
			}
			// ======2012-04-13 lipc 多时间段价格处理 结束=========== //
		}
	}

	/**
	 * 产品分类更新
	 * 
	 * @param map
	 */
	@SuppressWarnings("unchecked")
	private void updPrtCategory(Map<String, Object> map) throws Exception {
		// 删除数据库中产品分类信息
		binOLPTJCS07_Service.delPrtCategory(map);
		// 产品分类信息
		String cateInfo = ConvertUtil.getString(map
				.get(ProductConstants.CATE_INFO));
		if (!CherryConstants.BLANK.equals(cateInfo)) {
			// 产品分类信息List
			List<Map<String, Object>> cateInfoList = (List<Map<String, Object>>) JSONUtil
					.deserialize(cateInfo);
			if (null != cateInfoList) {
				for (Map<String, Object> cate : cateInfoList) {
					if (!CherryChecker.isNull(cate
							.get(ProductConstants.PROPVALID))) {
						cate.putAll(map);
						// 插入产品分类信息表
						binOLPTJCS03_Service.insertPrtCategory(cate);
					}
				}
			}
		}
	}

	/**
	 * 保存图片
	 * 
	 * @param map
	 */
	private void addImage(Map<String, Object> map) {
		// 产品图片
		String[] imagePath = (String[]) map.get(ProductConstants.IMAGE_PATH);
		if (null != imagePath && imagePath.length > 0) {
			for (String path : imagePath) {
				Map<String, Object> imageMap = new HashMap<String, Object>();
				// 产品路径
				imageMap.put(CherryConstants.PATH, path);
				imageMap.putAll(map);
				// 插入产品图片信息表
				binOLPTJCS03_Service.insertProductImage(imageMap);
			}
		}
	}

	/**
	 * 更新产品BOM信息
	 * 
	 * @param map
	 * @param barCodeList
	 * @param prtVendorId
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private void updPrtBOM(Map<String, Object> map,
			List<Map<String, Object>> barCodeList, int prtVendorId)
			throws Exception {
		String mode = ConvertUtil.getString(map.get(ProductConstants.MODE));
		// 产品类型为BOM,PBOM时删除原来的产品BOM数据，新增新的BOM信息
		if (mode.equals(ProductConstants.MODE_1)
				|| mode.equals(ProductConstants.MODE_3)) {
			String bomInfo = ConvertUtil.getString(map
					.get(ProductConstants.BOM_INFO));
			// BOM信息List
			List<Map<String, Object>> bomInfoList = (List<Map<String, Object>>) JSONUtil
					.deserialize(bomInfo);
			for (Map<String, Object> barCode : barCodeList) {
				// 操作
				String option = ConvertUtil.getString(barCode
						.get(ProductConstants.OPTION));
				int tempId = CherryUtil.obj2int(barCode
						.get(ProductConstants.PRT_VENDORID));
				if (tempId == 0) {
					tempId = prtVendorId;
				}
				Map<String, Object> prtMap = new HashMap<String, Object>();
				prtMap.put(ProductConstants.PRT_VENDORID, tempId);
				// 删除产品BOM表
				binOLPTJCS07_Service.delProductBOM(prtMap);
				if (!ProductConstants.OPTION_2.equals(option)) {
					for (Map<String, Object> bom : bomInfoList) {
						bom.put(ProductConstants.PRT_VENDORID, tempId);
						bom.putAll(map);
						// 插入产品BOM表
						binOLPTJCS03_Service.insertProductBOM(bom);
					}
				}
			}
		} 
		// 产品类型是产品、促销品时，删除产品BOM信息
		else {
			for (Map<String, Object> barCode : barCodeList) {
				int tempId = CherryUtil.obj2int(barCode
						.get(ProductConstants.PRT_VENDORID));
				if (tempId == 0) {
					tempId = prtVendorId;
				}
				Map<String, Object> prtMap = new HashMap<String, Object>();
				prtMap.put(ProductConstants.PRT_VENDORID, tempId);
				// 删除产品BOM表
				binOLPTJCS07_Service.delProductBOM(prtMap);
			}
		}
		
	}

	/**
	 * Map添加更新共通信息
	 * 
	 * @param map
	 * @return
	 */
	private Map<String, Object> getUpdMap(Map<String, Object> map) {
		map = CherryUtil.removeEmptyVal(map);
		// 系统时间
		String sysDate = binOLPTJCS07_Service.getSYSDate();
		// 作成日时
		map.put(CherryConstants.CREATE_TIME, sysDate);
		// 更新日时
		map.put(CherryConstants.UPDATE_TIME, sysDate);
		// 作成程序名
		map.put(CherryConstants.CREATEPGM, "BINOLPTJCS07");
		// 更新程序名
		map.put(CherryConstants.UPDATEPGM, "BINOLPTJCS07");
		// 作成者
		map.put(CherryConstants.CREATEDBY, map.get(CherryConstants.USERID));
		// 更新者
		map.put(CherryConstants.UPDATEDBY, map.get(CherryConstants.USERID));
		return map;
	}
	
	/**
	 * 取得促销活动所用的产品件数(已下发)
	 * 
	 * @param map
	 */
	public int getActUsePrtCount(Map<String, Object> map){
		return binOLPTJCS07_Service.getActUsePrtCount(map);
	}
	
	/**
	 * 取得促销活动所用的产品件数
	 * 
	 * @param map
	 */
	public int getActPrtCount(Map<String, Object> map){
		String bussDate = binOLPTJCS07_Service.getBussinessDate(map);
		map.put("bussDate", bussDate); // 业务时间
		return binOLPTJCS07_Service.getActPrtCount(map);
		
	}
	
	/**
	 * 更新产品条码对应关系信息
	 */
	public void updPrtBarCode(Map<String, Object> map){
		
		Map<String, Object> prtBCMap = binOLPTJCS07_Service.getPrtBarCode(map);
		
		if(null != prtBCMap){
			// 根据厂商ID及ClosingTime不为空查出的编码条码关系不为空，则判断新的编码条码与老编码条码是否一致，若一致，则将新编码条码置为null
			// 编码条码关系表中ClosingTime不为空的OldUnitCode、OldBarCode信息
			String oldUnitCode = ConvertUtil.getString(prtBCMap.get("OldUnitCode"));
			String oldBarCode = ConvertUtil.getString(prtBCMap.get("OldBarCode"));
			// 当前unitCode、barCode
			String unitCode = ConvertUtil.getString(map.get("unitCode"));
			String barCode = ConvertUtil.getString(map.get("barCode"));
			
			if(oldUnitCode.equalsIgnoreCase(unitCode) && oldBarCode.equalsIgnoreCase(barCode)){
				map.put("unitCode", null);
				map.put("barCode", null);
			}
		}
		
		binOLPTJCS07_Service.updPrtBarCode(map);
	}
}
