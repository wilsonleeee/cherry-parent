/*
 * @(#)BINOLSSPRM03_BL.java     1.0 2010/11/23
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.cache.annotation.CacheEvict;

import com.cherry.cm.cmbussiness.bl.BINOLCM15_BL;
import com.cherry.cm.cmbussiness.service.BINOLCM05_Service;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.DateUtil;
import com.cherry.ss.prm.service.BINOLSSPRM02_Service;
import com.cherry.ss.prm.service.BINOLSSPRM03_Service;
import com.googlecode.jsonplugin.JSONUtil;

/**
 * 促销产品编辑BL
 * 
 */
public class BINOLSSPRM03_BL {
	@Resource
	private BINOLSSPRM02_Service binolssprm02_Service;
	@Resource
	private BINOLSSPRM03_Service binolssprm03_Service;
	@Resource
	private BINOLCM05_Service binolcm05_Service;
	
	@Resource(name="binOLCM15_BL")
	private BINOLCM15_BL binOLCM15_BL;

	/**
	 * 更新促销产品信息
	 * 
	 * @param 促销产品ID
	 *            +更新日期+更新次数）
	 * @param （更新者ID）
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@CacheEvict(value="CherryPromotionCache",allEntries=true,beforeInvocation=false)
	public void tran_updatePrmProduct(Map<String, Object> map) throws Exception {
		map = CherryUtil.removeEmptyVal(map);
		
		// 取得促销品当前表版本号
		Map<String, Object> seqMap = new HashMap<String, Object>();
		seqMap.putAll(map);
		seqMap.put("type", "H");
		String tVersion = binOLCM15_BL.getCurrentSequenceId(seqMap);
		map.put("tVersion", tVersion);
		
		// 取得促销产品分类ID
		Map<String, Object> typeIdMap = binolcm05_Service.getPromPrtTypeId(map);
		if (null != typeIdMap && !typeIdMap.isEmpty()) {
			// 促销产品分类ID
			map.put("promPrtTypeId", typeIdMap.get("promPrtTypeId"));
		}
		/* *******************更新促销品表开始******************** */
		// 共通字段
		Map<String, Object> cmMap = new HashMap<String, Object>();
		String sysDate = binolssprm03_Service.getSYSDate();
		// 更新时间
		cmMap.put(CherryConstants.UPDATE_TIME, sysDate);
		// 更新模块
		cmMap.put(CherryConstants.UPDATEPGM, "BINOLSSPRM03");

		// 作成日时
		cmMap.put(CherryConstants.CREATE_TIME, binolssprm03_Service
				.getSYSDate());
		// 作成程序名
		cmMap.put(CherryConstants.CREATEPGM, "BINOLSSPRM03");
		map.putAll(cmMap);
		
		// 更新促销品信息
		int result = binolssprm03_Service.updatePrmInfo(map);
		// 更新失败
		if (0 == result) {
			throw new CherryException("ECM00038");
		}
		
		/* *******************更新促销品表结束******************** */
		
		/* *******************更新促销产品厂商表开始******************** */
		// 作成者
		cmMap.put(CherryConstants.CREATEDBY, map.get(CherryConstants.CREATEDBY));
		// 更新者
		cmMap.put(CherryConstants.UPDATEDBY, map.get(CherryConstants.UPDATEDBY));
		// 可编辑 状态
		if (!"1".equals(map.get("editFlag")) && !"ZDTL".equals(map.get("promCate"))) {
			// 取得促销品厂商信息
			String manuFactInfo = (String) map.get("manuFactInfo");
			// 促销品厂商List
			List<Map<String, Object>> manuFactList = null;
			// 当前启用的促销品
			List<Map<String, Object>> validCodeList = new ArrayList<Map<String, Object>>();
			// 当前停用的促销品
			List<Map<String, Object>> invalidCodeList = new ArrayList<Map<String, Object>>();
			
			if (null != manuFactInfo && !"".equals(manuFactInfo)) {
				manuFactList= (List<Map<String, Object>>) JSONUtil
						.deserialize(manuFactInfo);
			}
			
			if (null != manuFactList) {
				for (Map<String, Object> manuFact : manuFactList) {
					//启用list中，加入map格式的启用数据
					if ("1".equals(manuFact.get("facValidFlag"))) {
						validCodeList.add(manuFact);                       
					}
					//停用list中，加入map格式的停用数据
					else{
						invalidCodeList.add(manuFact);
					}
				}
			}
			// 业务日期
			String bussDate = binolssprm03_Service.getBusDate(map);
			String sysdateTime = binolssprm03_Service.getSYSDateTime();
			String sysHHSSMM = DateUtil.getSpecificDate(sysdateTime,DateUtil.TIME_PATTERN );
			// 停用时间
			String closingTime = bussDate + " " + sysHHSSMM;
			Map<String, Object> upMap = new HashMap<String, Object>(); 
			
			// 更新后的厂商编码
			String unitCode = (String) map.get("unitCode");
			upMap.putAll(cmMap);
			// 停用的时候
			if ("0".equals(map.get("validFlag"))) {
				if ("1".equals(map.get("prevValidFlag")) && null != validCodeList) {
					for (Map<String, Object> manuFact : validCodeList) {
						// 促销产品厂商ID
						upMap.put("prmVendorId", manuFact.get("prmVendorId"));
						// 促销品条码
						//upMap.put("newBarCode", manuFact.get("barCode"));
						// 厂商编码
						//upMap.put("unitCode", unitCode);
						upMap.put("closingTime", closingTime);
						// 更新促销产品条码对应关系信息
						//binolssprm03_Service.updatePromotionPrtBarCode(upMap);
						// 停用时，更新产品条码对应就系表的ClosingTime为当前业务时日 
						binolssprm03_Service.updateClosingTime(upMap);
					}
				}
				return;
			} else {
				// 启用的时候
				if ("0".equals(map.get("prevValidFlag")) && null != validCodeList) {
					for (Map<String, Object> manuFact : validCodeList) {
						// 促销产品厂商ID
						upMap.put("prmVendorId", manuFact.get("prmVendorId"));
						// 原条码
						String barCode = ConvertUtil.getString(manuFact.get("barCode"));
						// 新条码
						String newBarCode = ConvertUtil.getString(manuFact.get("newBarCode"));
						// 原编码
						String oldUnitCode = ConvertUtil.getString(map.get("oldUnitCode"));
						
						if(!barCode.equalsIgnoreCase(newBarCode) || !oldUnitCode.equalsIgnoreCase(unitCode)){
							// 原条码
							upMap.put("oldBarCode", barCode);
							// 新促销品条码
							upMap.put("newBarCode", newBarCode);
							// 原编码
							upMap.put("oldUnitCode", oldUnitCode);
							// 新厂商编码
							upMap.put("unitCode", unitCode);
							
						} else {
							upMap.put("oldBarCode", barCode);
							upMap.put("newBarCode", null);
							upMap.put("oldUnitCode", unitCode);
							upMap.put("unitCode", null);
						}
						
						// 启用日时
						String startTime = DateUtil.suffixDate(bussDate, 0);
						upMap.put("startTime", startTime);
						// 更新停用日时
//						binolssprm03_Service.updateClosingTime(upMap);
						// 插入促销产品条码对应关系表
						binolssprm03_Service.insertPromotionPrtBarCode(upMap);
					}
				}
			}
			
			// 新添加的促销产品条码
			List<String> newBarCodeList = new ArrayList<String>();
			// 更新的促销产品条码
			List<Map<String, Object>> updateCodeList = new ArrayList<Map<String, Object>>();
			// 不变的促销产品条码
			List<Map<String, Object>> unChangeCodeList = new ArrayList<Map<String, Object>>();
			// 删除的促销产品条码
			List<Map<String, Object>> deleteCodeList = new ArrayList<Map<String, Object>>();
			// 取添加条码中要新增的和要启用的数据
			if(null != map.get("barCodeAdd")){
				String[] barCodeArr = ConvertUtil.getString(map.get("barCodeAdd")).split(",");
				for(int i = 0; i < barCodeArr.length; i++){  
					//新增数据
					newBarCodeList.add(barCodeArr[i]);  
				}
			}
			// 提取list中要删除的、更新的和不变的数据
			if (null != manuFactList) {
				for(Map<String,Object> manuFact : manuFactList){
					// 要删除的促销品条码
					if("0".equals(manuFact.get("deleteFlag"))){
						deleteCodeList.add(manuFact);
					}
					if(null != manuFact.get("validPrmFlag") && "1".equals(manuFact.get("validPrmFlag"))){
						manuFact.put("validFlag", "1");
						unChangeCodeList.add(manuFact);
					}
					// 不变的和要更新的数据
					if("1".equals(manuFact.get("facValidFlag")) && "1".equals(manuFact.get("deleteFlag"))){
						// 新旧条码相同时，不改变
						if(ConvertUtil.getString(manuFact.get("newBarCode")).equalsIgnoreCase(ConvertUtil.getString(manuFact.get("barCode")))){
							unChangeCodeList.add(manuFact);
						}else{
							updateCodeList.add(manuFact);
						}
					}
				}
			}
			for	(Map<String, Object> updateCode : updateCodeList) {
				updateCode.putAll(cmMap);
				updateCode.put("unitCode", unitCode);
				// 更新后的厂商编码
				String newBarCode = (String) updateCode.get("newBarCode");
				updateCode.put("newBarCode", newBarCode);
				// 更新促销产品厂商信息
				int rst = binolssprm03_Service.updatePrmFac(updateCode);
				// 更新失败
				if (0 == rst) {
					throw new CherryException("ECM00038");
				}
				
				// 促销品启用时，已新增一条促销产品条码对应关系信息，无需再次更新 
				if(!("0".equals(map.get("prevValidFlag")) && "1".equals(map.get("validFlag")))){
					// 更新促销产品条码对应关系信息
					binolssprm03_Service.updatePromotionPrtBarCode(updateCode);
				}
			} 
			
			if(null != deleteCodeList){
				for	(Map<String, Object> deleteCode : deleteCodeList) {
					deleteCode.putAll(cmMap);
					deleteCode.put("unitCode", unitCode);
					// 删除促销产品厂商信息
					int rst = binolssprm03_Service.deletePrmFac(deleteCode);
					// 删除失败
					if (0 == rst) {
						throw new CherryException("ECM00038");
					}
					// 促销品条码
					deleteCode.put("newBarCode", deleteCode.get("barCode"));
					deleteCode.put("closingTime", closingTime);		
					// 更新促销产品条码对应关系信息
					binolssprm03_Service.updatePromotionPrtBarCode(deleteCode);	
				} 
			}
			if (!newBarCodeList.isEmpty()) {
				// 取得默认显示的生产厂商信息
				//Map factoryInfo = binolcm05_Service.getFactoryInfo(map);
				//if (null == factoryInfo || factoryInfo.isEmpty()) {
				//	throw new CherryException("ESS00050");
				//}
				Map vendorMap = new HashMap();
				vendorMap.putAll(cmMap);
				vendorMap.put("promProductId", map.get("promotionProId"));
				// 生产厂商ID
				vendorMap.put("manuFactId", "1");
				for (String barCode: newBarCodeList) {
					// 促销产品条码
					vendorMap.put("barCode", barCode);
					// 插入促销产品厂商表
					binolssprm02_Service.insertPromProductVendor(vendorMap);
				}
			}
			// 更新前的厂商编码
			String oldUnitCode = (String) map.get("oldUnitCode");
			if (!unChangeCodeList.isEmpty()) {
				for (Map<String, Object> unChangeCodeMap : unChangeCodeList) {
					// 已停用的时候
					if ("0".equals(unChangeCodeMap.get("facValidFlag"))) {
						unChangeCodeMap.put("validFlag", "1");
						// 启用促销产品厂商信息
						int rst = binolssprm03_Service.deletePrmFac(unChangeCodeMap);
						// 启用失败
						if (0 == rst) {
							throw new CherryException("ECM00038");
						}
						// 更新停用日时
						binolssprm03_Service.updateClosingTime(unChangeCodeMap);
					}
					if (null != oldUnitCode && !oldUnitCode.equals(unitCode)) {
						unChangeCodeMap.put("unitCode", unitCode);
						unChangeCodeMap.put("newBarCode", unChangeCodeMap.get("barCode"));
						unChangeCodeMap.putAll(cmMap);
						// 更新促销产品条码对应关系信息
						binolssprm03_Service.updatePromotionPrtBarCode(unChangeCodeMap);
					}
				}
			}
		}
		/* *******************更新促销产品厂商表结束******************** */
		/* *******************更新促销产品信息扩展表开始******************** */
		// 促销产品ID
		cmMap.put("promotionProId", map.get("promotionProId"));
		// 促销品图片
		String[] promImagePath = (String[]) map.get("promImagePath");
		if (null != promImagePath && promImagePath.length > 0) {
			for (String promImage : promImagePath) {
				Map<String, Object> promImageMap = new HashMap<String, Object>();
				// 促销产品路径
				promImageMap.put("promotionImagePath", promImage);
				promImageMap.putAll(cmMap);
				// 插入促销产品信息扩展表
				binolssprm03_Service.insertPrmExt(promImageMap);
			}
		}
		/* *******************更新促销产品信息扩展表结束******************** */
	}
	
	/**
	 * 取得促销活动使用的促销品件数
	 * 
	 * @param map
	 * @return List
	 */
	public int getActUsePrmCount(Map<String, Object> map){
		return binolssprm03_Service.getActUsePrmCount(map);
	}
	
	/**
	 * 取得促销活动使用的促销品件数
	 * 
	 * @param map
	 * @return List
	 */
	public int getActPrmCount(Map<String, Object> map){
		String bussDate = binolssprm03_Service.getBusDate(map);
		map.put("bussDate", bussDate); // 业务时间
		return binolssprm03_Service.getActPrmCount(map);
	}
}
