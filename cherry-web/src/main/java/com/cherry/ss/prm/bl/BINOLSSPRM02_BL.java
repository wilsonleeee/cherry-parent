/*
 * @(#)BINOLSSPRM02_BL.java     1.0 2010/11/19
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

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.springframework.cache.annotation.CacheEvict;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM15_BL;
import com.cherry.cm.cmbussiness.service.BINOLCM05_Service;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.ss.prm.service.BINOLSSPRM02_Service;

/**
 * 促销品添加 BL
 * 
 * @author hub
 * @version 1.0 2010.11.19
 */
@SuppressWarnings("unchecked")
public class BINOLSSPRM02_BL {
	
	@Resource
	private BINOLSSPRM02_Service binOLSSPRM02_Service;
	
	@Resource
	private BINOLCM05_Service binOLCM05_Service;
	
	/** 系统配置项 共通BL */
	@Resource(name="binOLCM14_BL")
	private BINOLCM14_BL binOLCM14_BL;
	
	@Resource(name="binOLCM15_BL")
	private BINOLCM15_BL binOLCM15_BL;
	
	/**
	 * 促销品添加插表处理
	 * 
	 * @param map
	 * @return 无
	 * @throws Exception 
	 */
	@CacheEvict(value="CherryPromotionCache",allEntries=true,beforeInvocation=false)
	public void tran_addPromProduct(Map<String, Object> map) throws Exception {
		map = CherryUtil.removeEmptyVal(map);
		
		// 取得促销品当前表版本号
		Map<String, Object> seqMap = new HashMap<String, Object>();
		seqMap.putAll(map);
		seqMap.put("type", "H");
		String tVersion = binOLCM15_BL.getCurrentSequenceId(seqMap);
		map.put("tVersion", tVersion);
		
		
		// 取得促销产品分类ID
		Map typeIdMap = binOLCM05_Service.getPromPrtTypeId(map);
		if (null != typeIdMap && !typeIdMap.isEmpty()) {
			// 促销产品分类ID
			map.put("promPrtTypeId", typeIdMap.get("promPrtTypeId"));
		}
		// 插表时的共通字段
		Map<String, Object> insertMap = new HashMap<String, Object>();
		// 系统时间
		String sysDate = binOLSSPRM02_Service.getSYSDate();
		// 作成日时
		insertMap.put(CherryConstants.CREATE_TIME, sysDate);
		// 更新日时
		insertMap.put(CherryConstants.UPDATE_TIME, sysDate);
		// 作成程序名
		insertMap.put(CherryConstants.CREATEPGM, "BINOLSSPRM02");
		// 更新程序名
		insertMap.put(CherryConstants.UPDATEPGM, "BINOLSSPRM02");
		map.putAll(insertMap);
		// 插入促销产品表并返回促销产品ID
		String unitCode = ConvertUtil.getString(map.get("unitCode")).toUpperCase();
		map.put("unitCode", unitCode);
		int promProductId = binOLSSPRM02_Service.insertPromotionProduct(map);
		// 作成者
		insertMap.put(CherryConstants.CREATEDBY, map.get(CherryConstants.CREATEDBY));
		// 更新者
		insertMap.put(CherryConstants.UPDATEDBY, map.get(CherryConstants.UPDATEDBY));
		map.put("promProductId", promProductId);
		// 取得默认显示的生产厂商信息
		//Map factoryInfo = binOLCM05_Service.getFactoryInfo(map);
		//if (null == factoryInfo || factoryInfo.isEmpty()) {
		//	throw new CherryException("ESS00050");
		//}
		// 生产厂商ID
		map.put("manuFactId", "1");
		String[] barCodeArr = ConvertUtil.getString(map.get("barCode")).split(",");
		for (String barCode : barCodeArr) {
			// 促销产品条码
			map.put("barCode", barCode.toUpperCase());
			// 插入促销产品厂商表
			binOLSSPRM02_Service.insertPromProductVendor(map);
		}
		// 促销品图片
		String[] promImagePath = (String[]) map.get("promImagePath");
		if (null != promImagePath && promImagePath.length > 0) {
			// 促销产品ID
			insertMap.put("promProductId", promProductId);
			for (String promImage : promImagePath) {
				Map<String, Object> promImageMap = new HashMap<String, Object>();
				// 促销产品路径
				promImageMap.put("promotionImagePath", promImage);
				promImageMap.putAll(insertMap);
				// 插入促销产品信息扩展表
				binOLSSPRM02_Service.insertPromotionProductExt(promImageMap);
			}
		}
	}
	
	/**
	 * 验证UnitCode编码规则
	 * @param userInfo
	 * @param unitCode
	 * @return
	 */
	public boolean checkUnitCode(UserInfo userInfo,String unitCode){
		// BAS编码规则
		String basPattern = binOLCM14_BL.getConfigValue("1105", String.valueOf(userInfo.getBIN_OrganizationInfoID()), String.valueOf(userInfo.getBIN_BrandInfoID()));
		Pattern p = Pattern.compile(basPattern);
		Matcher m = p.matcher(unitCode);
		if(!m.matches()){
			return false;
		}else {
			return true;
		}
	}
	
	/**
	 * 验证BarCode编码规则
	 * @param userInfo
	 * @param unitCode
	 * @return
	 */
	public boolean checkBarCode(UserInfo userInfo,String barCode){
		// BAS编码规则
		String basPattern = binOLCM14_BL.getConfigValue("1106", String.valueOf(userInfo.getBIN_OrganizationInfoID()), String.valueOf(userInfo.getBIN_BrandInfoID()));
		Pattern p = Pattern.compile(basPattern);
		Matcher m = p.matcher(barCode);
		if(!m.matches()){
			return false;
		}else {
			return true;
		}
	}
}
