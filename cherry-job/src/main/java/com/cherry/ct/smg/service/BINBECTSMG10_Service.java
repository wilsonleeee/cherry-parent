/*	
 * @(#)BINBECTSMG10_Service.java     1.0 2016/05/02		
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
package com.cherry.ct.smg.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 短信模板管理Service
 * 
 * @author hub
 * @version 1.0 2016/05/02
 */
public class BINBECTSMG10_Service extends BaseService{
	/**
     * 取得品牌的短信模板列表
     * 
     * @param map
     * @return List
     * 		短信模板列表
     */
    public List<Map<String, Object>> getSmsTemplateList(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBECTSMG10.getSmsTemplateList");
        return baseServiceImpl.getList(map);
    }
    
    /**
     * 取得品牌的短信模板编号
     * 
     * @param map
     * @return String
     * 		短信模板编号
     */
    public Map<String, Object> getSmsTemplateCode(String brandCode, String content) {
    	Map<String, Object> map = new HashMap<String, Object>();
    	map.put("brandCode", brandCode);
    	map.put("content", content);
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBECTSMG10.getSmsTemplateCode");
        return (Map<String, Object>) baseServiceImpl.get(map);
    }
    
    /**
     * 取得品牌所有的短信模板
     * 
     * @param brandCode
     * 			品牌代码
     * @return List
     * 		短信模板列表
     */
    public List<Map<String, Object>> getBrandTemplateList(String brandCode) {
    	Map<String, Object> map = new HashMap<String, Object>();
    	map.put("brandCode", brandCode);
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBECTSMG10.getBrandTemplateList");
        return baseServiceImpl.getList(map);
    }
    
    /**
	 * 
	 * 更新短信模板
	 * 
	 * @param map 更新条件
	 * @return 更新件数
	 */
	public int updateTemplateInfo(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBECTSMG10.updateTemplateInfo");
		return baseServiceImpl.update(map);
	}
	
	/**
	 * 
	 * 新增短信模板
	 * 
	 * @param map 新增条件
	 * @return 
	 */
	public void addTemplateInfo(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBECTSMG10.addTemplateInfo");
		baseServiceImpl.save(map);
	}
}
