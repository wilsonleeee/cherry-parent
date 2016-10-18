/*
 * @(#)BINOLSSPRM01_Service.java     1.0 2010/10/27
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
package com.cherry.ss.prm.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 促销品查询
 * @author Administrator
 *
 */
@SuppressWarnings("unchecked")
public class BINOLSSPRM01_Service extends BaseService {

	
	/**
	 * 取得促销产品总数
	 * 
	 * @param map
	 * @return
	 */
	public int getPrmCount(Map<String, Object> map) {
		
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM01.getPrmCount");
		return baseServiceImpl.getSum(map);
	}
	
	/**
	 * 取得促销产品信息List
	 * 
	 * @param map
	 * @return
	 */
	public List getPromotionProList (Map<String, Object> map) {
		
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM01.getPromotionProList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 取得促销产品信息
	 * 
	 * @param promotionProId
	 * @return
	 */
	public Map getPromotionPro (String promotionProId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(CherryConstants.PROMOTIONPROID, promotionProId);
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM01.getPromotionPro");
		return (Map<String, Object>)baseServiceImpl.get(map);
	}
	
	/**
	 * 伦理删除促销产品信息
	 * 
	 * @param map
	 * @return
	 */
	public int operatePromotionPro (Map<String, Object> map) {
		
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM01.invalidPromotionPro");
		return baseServiceImpl.update(map);
	}
	
	/**
	 * 伦理删除促销品价格信息
	 * 
	 * @param map
	 * @return
	 */
	public int operatePromotionProPrice (Map<String, Object> map) {
		
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM01.invalidPromotionProPrice");
		return baseServiceImpl.update(map);
	}
	
	/**
	 * 伦理删除促销产品BOM信息
	 * 
	 * @param map
	 * @return
	 */
	public int operatePromotionProBOM (Map<String, Object> map) {
		
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM01.invalidPromotionProBOM");
		return baseServiceImpl.update(map);
	}
	
	/**
	 * 伦理删除促销产品厂商信息
	 * 
	 * @param map
	 * @return
	 */
	public int operatePromotionProFac (Map<String, Object> map) {
		
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM01.invalidPromotionProFac");
		return baseServiceImpl.update(map);
	}
	 /**
     * 取得促销品信息List(Excel)
     * 
     * @param map 查询条件
     * 
     */
    public List getPromotionInfoListExcel(Map<String, Object> map) {
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM01.getPromotionInfoListExcel");
        return baseServiceImpl.getList(parameterMap);
    }
}
