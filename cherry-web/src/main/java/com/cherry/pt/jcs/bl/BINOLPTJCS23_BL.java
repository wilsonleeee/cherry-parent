/*
 * @(#)BINOLPTJCS03_BL.java     1.0 2011/03/21
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.springframework.cache.annotation.CacheEvict;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM15_BL;
import com.cherry.cm.cmbussiness.service.BINOLCM12_Service;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.DateUtil;
import com.cherry.pt.common.ProductConstants;
import com.cherry.pt.jcs.interfaces.BINOLPTJCS23_IF;
import com.cherry.pt.jcs.service.BINOLPTJCS16_Service;
import com.cherry.pt.jcs.service.BINOLPTJCS23_Service;
import com.googlecode.jsonplugin.JSONUtil;

/**
 * 产品添加 BL
 * 
 * @author lipc
 * @version 1.0 2011.03.21
 */
public class BINOLPTJCS23_BL implements BINOLPTJCS23_IF {

	@Resource(name="binOLPTJCS23_Service")
	private BINOLPTJCS23_Service binOLPTJCS23_Service;

	@Resource(name="binOLCM12_Service")
	private BINOLCM12_Service binOLCM12_Service;
	
	/** 系统配置项 共通BL */
	@Resource(name="binOLCM14_BL")
	private BINOLCM14_BL binOLCM14_BL;
	
	/** 各类编号取号共通BL */
	@Resource(name="binOLCM15_BL")
	private BINOLCM15_BL binOLCM15_BL;
	
	@Resource(name="binOLPTJCS16_Service")
	private BINOLPTJCS16_Service binOLPTJCS16_Service;

	/**
	 * 产品添加插表处理
	 * 
	 * @param map
	 * @return 无
	 * @throws Exception
	 */
	@Override
	@CacheEvict(value="CherryProductCache",allEntries=true,beforeInvocation=false)
	public void tran_addProduct(Map<String, Object> map) throws Exception {
		// 取得当前产品表的表版本号
		Map<String, Object> seqMap = new HashMap<String, Object>();
		seqMap.putAll(map);
		seqMap.put("type", "E");
		String tVersion = binOLCM15_BL.getCurrentSequenceId(seqMap);
		map.put("tVersion", tVersion);
		
		// 取得更新共通信息
		map = getUpdMap(map);
		// 插入产品表并返回产品ID
		
		map.put("mode", ProductConstants.MODE_0);
		map.put("status", ProductConstants.PRODUCT_DEF_STATUS);
		// 将产品编码转换为大写
		String unitCode = ConvertUtil.getString(map.get(CherryConstants.UNITCODE));
		map.put(CherryConstants.UNITCODE, unitCode.toUpperCase());
		
		int productId = binOLPTJCS23_Service.insertProduct(map);
		// 产品ID
		map.put(ProductConstants.PRODUCTID, productId);
		// 插入产品厂商表
		insertProductVendor(map);
		// 产品扩展属性添加
		binOLCM12_Service.insertPrtExtValue(map);
		// 添加分类信息
		addCategory(map);
		// 添加产品价格
		addPrice(map);
		// 添加产品图片
		addImage(map);
		
		// 添加到方案明细中去
		String productPriceSolutionID = ConvertUtil.getString(map.get("productPriceSolutionID"));
		if(!CherryChecker.isNullOrEmpty(productPriceSolutionID)){
			addSoluPrt(map);
		}
		
	}

	/**
	 * 添加分类信息
	 * 
	 * @param map
	 */
	@SuppressWarnings("unchecked")
	private void addCategory(Map<String, Object> map) throws Exception {
		// 剔除分类属性值ID
		map.remove(ProductConstants.PROPVALID);
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
						binOLPTJCS23_Service.insertPrtCategory(cate);
					}
				}
			}
		}
	}

	/**
	 * 添加价格信息
	 * 
	 * @param map
	 */
	@SuppressWarnings("unchecked")
	private void addPrice(Map<String, Object> map) throws Exception {
		// 标准价格信息
		String priceInfo = ConvertUtil.getString(map
				.get(ProductConstants.PRICE_INFO));
		if (!CherryConstants.BLANK.equals(priceInfo)) {
			// 标准价格信息List
			List<Map<String, Object>> priceInfoList = (List<Map<String, Object>>) JSONUtil
					.deserialize(priceInfo);
			// =======2012-04-13 lipc 多时间段价格处理 开始============= //
			// if (null != priceInfoList) {
			// for (Map<String, Object> price : priceInfoList) {
			// Map<String, Object> priceMap = CherryUtil
			// .removeEmptyVal(price);
			// priceMap.putAll(map);
			// if (CherryChecker.isNullOrEmpty(priceMap
			// .get(ProductConstants.PRICEENDDATE))) {
			// priceMap.put(ProductConstants.PRICEENDDATE,
			// CherryConstants.longLongAfter);
			// }
			// // 插入产品价格表
			// binOLPTJCS23_Service.insertProductPrice(priceMap);
			// }
			// }
			if (null != priceInfoList) {
				int size = priceInfoList.size();
				for (int i = 0; i < size; i++) {
					Map<String, Object> priceMap = priceInfoList.get(i);
					priceMap = CherryUtil.removeEmptyVal(priceMap);
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
					// 插入产品价格表
					binOLPTJCS23_Service.insertProductPrice(priceMap);
				}
			}
			// ======2012-04-13 lipc 多时间段价格处理 结束=========== //
		}
	}

	/**
	 * 添加产品图片
	 * 
	 * @param map
	 */
	private void addImage(Map<String, Object> map) throws Exception {
		// 产品图片
		String[] imagePath = (String[]) map.get(ProductConstants.IMAGE_PATH);
		if (null != imagePath && imagePath.length > 0) {
			for (String path : imagePath) {
				Map<String, Object> imageMap = new HashMap<String, Object>();
				// 产品路径
				imageMap.put(CherryConstants.PATH, path);
				imageMap.putAll(map);
				// 插入产品信息扩展表
				binOLPTJCS23_Service.insertProductImage(imageMap);
			}
		}
	}
	
	/**
	 * 添加产品到方案明细中
	 * 
	 * @param map
	 */
	private void addSoluPrt(Map<String, Object> map) throws Exception {
		
		Map<String, Object> prtMap = binOLPTJCS23_Service.getProductInfo(map);
		if(!prtMap.isEmpty()){
			
			// 取得当前产品表的表版本号
			Map<String, Object> seqMap = new HashMap<String, Object>();
			seqMap.putAll(map);
			seqMap.put("type", "F");
			String tVersion = binOLCM15_BL.getCurrentSequenceId(seqMap);
			prtMap.put("tVersion", tVersion);
			
			prtMap.put("productPriceSolutionID", map.get("productPriceSolutionID"));
			prtMap.put("productID", map.get(ProductConstants.PRODUCTID));
			prtMap.put("soluProductName", prtMap.get("NameTotal"));
			
			Map<String, Object> mergeMap = binOLPTJCS16_Service.mergePrtPriceSoluDetail(prtMap);
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
		String sysDate = binOLPTJCS23_Service.getSYSDate();
		// 作成日时
		map.put(CherryConstants.CREATE_TIME, sysDate);
		// 更新日时
		map.put(CherryConstants.UPDATE_TIME, sysDate);
		// 作成程序名
		map.put(CherryConstants.CREATEPGM, "BINOLPTJCS23");
		// 更新程序名
		map.put(CherryConstants.UPDATEPGM, "BINOLPTJCS23");
		// 作成者
		map.put(CherryConstants.CREATEDBY, map.get(CherryConstants.USERID));
		// 更新者
		map.put(CherryConstants.UPDATEDBY, map.get(CherryConstants.USERID));
		return map;
	}

	/**
	 * 插入产品厂商表
	 * 
	 * @param map
	 */
	@SuppressWarnings("unchecked")
	private void insertProductVendor(Map<String, Object> map) throws Exception {
		String barCodeInfo = ConvertUtil.getString(map
				.get(CherryConstants.BARCODE));
		List<Map<String, Object>> barCodeList = (List<Map<String, Object>>) JSONUtil
				.deserialize(barCodeInfo);
		if (null != barCodeList) {
			for (Map<String, Object> barCodeMap : barCodeList) {
				map.put(CherryConstants.BARCODE, barCodeMap
						.get(CherryConstants.BARCODE).toString().toUpperCase());
				// 产品厂商ID
				int prtVendorId = binOLPTJCS23_Service.insertProductVendor(map);
				// 产品类型
				String mode = ConvertUtil.getString(map
						.get(ProductConstants.MODE));
				// BOM,套装类型
				if (mode.equals(ProductConstants.MODE_1)
						|| mode.equals(ProductConstants.MODE_3)) {
					// 插入产品BOM表
					insertProductBOM(map, prtVendorId);
				}
			}
		}
	}

	/**
	 * 插入产品BOM表
	 * 
	 * @param map
	 * @param prtVendorId
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private void insertProductBOM(Map<String, Object> map, int prtVendorId)
			throws Exception {
		String bomInfo = ConvertUtil.getString(map
				.get(ProductConstants.BOM_INFO));
		// BOM信息List
		List<Map<String, Object>> bomInfoList = (List<Map<String, Object>>) JSONUtil
				.deserialize(bomInfo);
		for (Map<String, Object> bom : bomInfoList) {
			bom.put(ProductConstants.PRT_VENDORID, prtVendorId);
			bom.putAll(map);
			// 插入产品BOM表
			binOLPTJCS23_Service.insertProductBOM(bom);
		}
	}

	/**
	 * 取得产品ID
	 * 
	 * @param map
	 * @return String
	 */
	@Override
	public int getProductId(Map<String, Object> map) {
		return binOLPTJCS23_Service.getProductId(map);
	}
	
	/**
	 * 取得产品ID集合（根据barcode）
	 * 
	 * @param map
	 * @return int
	 */
	public List<Integer> getProductIds(Map<String, Object> map){
		return binOLPTJCS23_Service.getProductIds(map);
	}
	

	/**
	 * 取得促销产品ID
	 * 
	 * @param map
	 * @return String
	 */
	@Override
	public int getPromotionId(Map<String, Object> map) {
		return binOLPTJCS23_Service.getPromotionId(map);
	}

	/**
	 * 取得产品分类List
	 * 
	 * @param map
	 * @return String
	 */
	@Override
	public List<Map<String, Object>> getCategoryList(Map<String, Object> map) {
		return binOLPTJCS23_Service.getCategoryList(map);
	}
	
	/**
	 * 取得产品分类
	 * 
	 * @param map
	 * @return map
	 */
	@Override
	public List<Map<String, Object>> getCateValList(Map<String, Object> map) {
		return binOLPTJCS23_Service.getCateValList(map);
	}

	@Override
	public List<Map<String, Object>> getSubCateList(Map<String, Object> map) {
		return binOLPTJCS23_Service.getSubCateList(map);
	}

	@Override
	public List<Map<String, Object>> getPatCateList(Map<String, Object> map) {
		return binOLPTJCS23_Service.getPatCateList(map);
	}
	
	/**
	 * 验证UnitCode
	 * @param userInfo
	 * @param unitCode
	 * @return
	 */
	public boolean checkUnitCode(UserInfo userInfo,String unitCode){
		// BAS编码规则
		String basPattern = binOLCM14_BL.getConfigValue("1103", String.valueOf(userInfo.getBIN_OrganizationInfoID()), String.valueOf(userInfo.getBIN_BrandInfoID()));
		Pattern p = Pattern.compile(basPattern);
		Matcher m = p.matcher(unitCode);
		if(!m.matches()){
			return false;
		}else {
			return true;
		}
	}
	
	/**
	 * 验证BarCode
	 * @param userInfo
	 * @param unitCode
	 * @return
	 */
	public boolean checkBarCode(UserInfo userInfo,String barCode){
		// BAS编码规则
		String basPattern = binOLCM14_BL.getConfigValue("1104", String.valueOf(userInfo.getBIN_OrganizationInfoID()), String.valueOf(userInfo.getBIN_BrandInfoID()));
		Pattern p = Pattern.compile(basPattern);
		Matcher m = p.matcher(barCode);
		if(!m.matches()){
			return false;
		}else {
			return true;
		}
	}
	
	/**
	 * 取得厂商编码(barcode+三位递增码)
	 * @param map
	 * @return
	 */
	public String getUnitCodeRightTree(Map<String, Object> map) {
		return binOLPTJCS23_Service.getUnitCodeRightTree(map);
	}
}
