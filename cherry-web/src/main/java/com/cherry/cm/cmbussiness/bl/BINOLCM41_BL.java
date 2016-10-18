/*
 * @(#)BINOLCM41_BL.java v1.0 2014-11-6
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
package com.cherry.cm.cmbussiness.bl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbussiness.interfaces.BINOLCM41_IF;
import com.cherry.cm.cmbussiness.service.BINOLCM21_Service;
import com.cherry.cm.cmbussiness.service.BINOLCM41_Service;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.CherryUtil;
import com.cherry.pt.common.ProductConstants;

/**
 * 产品基础信息   共通BL
 * 
 * @author JiJW
 * @version 1.0 2014-11-6
 */
public class BINOLCM41_BL implements BINOLCM41_IF {

	/** 系统配置项 共通BL */
	@Resource(name="binOLCM14_BL")
	private BINOLCM14_BL binOLCM14_BL;
	
	@Resource
	private BINOLCM41_Service binOLCM41_Service;
	
	@Resource
	private BINOLCM21_Service binOLCM21_Service;
	
	/**
	 * 取得柜台对应的产品列表
     * 返回的列表中按照OrderNO从小到大排序，且默认仓库在前
     * @param praMap
     * praMap参数说明：organizationInfoId（必填。所属组织ID）,
     * praMap参数说明：brandInfoId（必填。所属品牌ID）,
     * praMap参数说明：counterCode （必填。柜台编码。）
     * praMap参数说明：language（可选。语言，用于中英文对应，为空则按中文处理）
     * 
	 * @param pram
	 * @return
	 */
	@Override
	public List<Map<String, Object>> getCntProductList (Map<String, Object> praMap)throws Exception {
		
		Map<String,Object> selMap = new HashMap<String, Object>();
		selMap.putAll(praMap);
		selMap.put(CherryConstants.VALID_FLAG, CherryConstants.VALIDFLAG_ENABLE); // 柜台对应的产品都是有效的
		
		// 柜台产品使用模式 1:严格校验 2:补充校验 3:标准产品
		String cntPrtModeConf = binOLCM14_BL.getConfigValue("1294", String.valueOf(praMap.get(CherryConstants.ORGANIZATIONINFOID)), String.valueOf(praMap.get(CherryConstants.BRANDINFOID)));
		// 产品方案添加产品模式 1:标准模式 2:颖通模式
		String soluAddModeConf = binOLCM14_BL.getConfigValue("1288", String.valueOf(praMap.get(CherryConstants.ORGANIZATIONINFOID)), String.valueOf(praMap.get(CherryConstants.BRANDINFOID)));

		String businessDate = binOLCM41_Service.getBussinessDate(praMap);
		selMap.put("businessDate", businessDate);
		
		List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
		
		if(ProductConstants.CNT_PRT_MODE_CONIFG_3.equals(cntPrtModeConf)){
			// 提示标准模式，无法给到柜台对应的产品列表
//			throw new CherryException("ECM000103", new String[]{praMap.get("counterCode").toString()});
			// 柜台产品使用模式为“标准产品”时，则直接查询产品，与柜台无关
			resultList = binOLCM41_Service.getStandProductInfo(selMap);
		}else{
			
			// 检查柜台是否有分配可用的方案
			resultList = binOLCM21_Service.chkCntSoluData(selMap);
			if(CherryUtil.isBlankList(resultList)){
				// 柜台没有分配方案，返回空的List
				resultList = binOLCM41_Service.getStandProductInfo(selMap);
			}else{
				// 柜台产品使用模式 “严格校验”、“补充校验” 时，通过SQL动态拼接查询。
				selMap.put("cntPrtModeConf", cntPrtModeConf);
				selMap.put("soluAddModeConf", soluAddModeConf);
				resultList = binOLCM41_Service.getCntProductInfo(selMap);
			}
			
		}
		
		return resultList;
	}
	
	/**
	 * 查看柜台使用产品方案还是标准产品
	 * @param praMap
     * praMap参数说明：organizationInfoId（必填。所属组织ID）,
     * praMap参数说明：brandInfoId（必填。所属品牌ID）,
     * praMap参数说明：counterCode （必填。柜台编码。）
	 * @return cntPrt 1:使用柜台产品方案 0：使用标准产品
	 * @return cntPrt 1:使用柜台产品方案 0：使用标准产品
	 * @throws Exception
	 */
	public String getIsCntPrt(Map<String, Object> praMap)throws Exception {
		String result = "1";
		
		// 柜台产品使用模式 1:严格校验 2:补充校验 3:标准产品
		String cntPrtModeConf = binOLCM14_BL.getConfigValue("1294", String.valueOf(praMap.get(CherryConstants.ORGANIZATIONINFOID)), String.valueOf(praMap.get(CherryConstants.BRANDINFOID)));

		if(ProductConstants.CNT_PRT_MODE_CONIFG_3.equals(cntPrtModeConf)){
			result = "0";
		}
		// 检查柜台是否有分配可用的方案
		List<Map<String,Object>> resultList = binOLCM21_Service.chkCntSoluData(praMap);
		if(CherryUtil.isBlankList(resultList)){
			result = "0";
		}
		
		return result;
	}

}
