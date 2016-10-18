/*	
 * @(#)BINOLCM15_BL.java     1.0 2010/11/08		
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

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbussiness.service.BINOLCM15_Service;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CodeTable;
import com.cherry.cm.util.CherryUtil;

/**
 * 各类编号取号共通BL
 * @author WangCT
 *
 */
public class BINOLCM15_BL {
	
	@Resource
	private BINOLCM15_Service binOLCM15_Service;
	
	@Resource
	private CodeTable code;
	
	/**
	 * 取得新生成的编号
	 * @param map
	 * @return 返回新生成的编号
	 */
	public String getSequenceId(Map<String, Object> map) {
		
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		Object type = paramMap.get("type");
		// 编码类型为仓库
		if(type != null && "3".equals(type.toString())) {
			paramMap.put(CherryConstants.BRANDINFOID, CherryConstants.BRAND_INFO_ID_VALUE);
		}
		// 取得新生成的编号
		String sequenceId = binOLCM15_Service.getSequenceId(paramMap);
		int length = Integer.parseInt((String)paramMap.get("length"));
		return CherryUtil.padLeft(sequenceId, length, '0');
	}
	
	/**
	 * 取得当前的编号
	 * @param map
	 * @return 返回当前的编号
	 */
	public String getCurrentSequenceId(Map<String, Object> map) {
		
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		Object type = paramMap.get("type");
		// 编码类型为仓库
		if(type != null && "3".equals(type.toString())) {
			paramMap.put(CherryConstants.BRANDINFOID, CherryConstants.BRAND_INFO_ID_VALUE);
		}
		// 取得新生成的编号
		String sequenceId = binOLCM15_Service.getCurrentSequenceId(paramMap);
		return sequenceId;
	}
	
	/**
	 * 取得新生成的编号(无左侧补零)
	 * @param map
	 * @return 返回新生成的编号
	 */
	public String getNoPadLeftSequenceId(Map<String, Object> map) {
		
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		Object type = paramMap.get("type");
		// 编码类型为仓库
		if(type != null && "3".equals(type.toString())) {
			paramMap.put(CherryConstants.BRANDINFOID, CherryConstants.BRAND_INFO_ID_VALUE);
		}
		// 取得新生成的编号
		String sequenceId = binOLCM15_Service.getSequenceId(paramMap);
		return sequenceId;
	}
	
	/**
	 * 取得新生成的编号
	 * @param orgId 组织ID
	 * @param brandId 品牌ID
	 * @param type 编号类型
	 * @return 返回新生成的编号
	 */
	public String getSequenceId(int orgId, int brandId, String type) {
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(CherryConstants.ORGANIZATIONINFOID, orgId);
		map.put(CherryConstants.BRANDINFOID, brandId);
		map.put("type", type);
		// 取得新生成的编号
		String sequenceId = binOLCM15_Service.getSequenceId(map);
		// 根据code值表的定义生成相应编号
		Map codeMap = code.getCode("1120",type);
		int length = Integer.parseInt((String)codeMap.get("value2"));
		return (String)codeMap.get("value1") + CherryUtil.padLeft(sequenceId, length, '0');
	}

	/**
	 * 查询虚拟条码数量
	 * @param map
	 * @return
	 */
	public int getBarCodeCount (Map<String, Object> map){
		return binOLCM15_Service.getBarCodeCount(map);
	}
	
	/**
	 * 取得虚拟促销品
	 * @param map
	 * @return
	 */
	public String getVirtualPrmVendorId(Map<String, Object> map){
		return (String) binOLCM15_Service.getVirtualPrmVendorId(map);
	}
	
	/**
	 * 根据code取得促销品个数
	 * 
	 * @param map
	 * @param brandId
	 * @return
	 */
	public int getPrmCount (Map<String, Object> map,int brandId){
		return binOLCM15_Service.getPrmCount(map, brandId);
	}
	
	/**
	 * 插入促销产品表
	 * 
	 * @param map
	 * @return int
	 */
	public int addPrmInfo(Map<String, Object> map,int orgId,int brandId) {
		return binOLCM15_Service.addPrmInfo(map, orgId, brandId);
	}
	
	/**
	 * update促销产品表
	 * 
	 * @param map
	 * @return
	 */
	public void updPrmInfo(Map<String, Object> map,int prmVendorId) {
		binOLCM15_Service.updPrmInfo(map, prmVendorId);
	}
	
	/**
	 * 根据code取得促销品个数
	 * 
	 * @param map
	 * @param brandId
	 * @return
	 */
	public Integer getPrmVendorId (Map<String, Object> map){
		return binOLCM15_Service.getPrmVendorId(map);
	}
	
	/**
	 * 插入促销产品表并返回促销产品ID
	 * 
	 * @param map
	 * @return int
	 */
	public int insertPromotionProductBackId(Map<String, Object> map) {
		return binOLCM15_Service.insertPromotionProductBackId(map);
	}
	
	/**
	 * 插入促销产品厂商表
	 * 
	 * @param map
	 * @return
	 */
	public int insertPromProductVendor(Map<String, Object> map) {
		return binOLCM15_Service.insertPromProductVendor(map);
	}
}
