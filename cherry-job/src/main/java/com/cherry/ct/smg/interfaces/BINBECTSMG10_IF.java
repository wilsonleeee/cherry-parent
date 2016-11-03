/*	
 * @(#)BINBECTSMG10_IF.java     1.0 2016/05/02		
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
package com.cherry.ct.smg.interfaces;

import java.util.List;
import java.util.Map;

/**
 * 短信模板管理IF
 * 
 * @author hub
 * @version 1.0 2016/05/02
 */
public interface BINBECTSMG10_IF {
	
	/**
     * 取得品牌的短信模板列表
     * 
     * @param map
     * @return String
     * 		短信模板列表
     */
    public List<Map<String, Object>> getSmsTemplateList(Map<String, Object> map);
    
    /**
     * 更新短信模板
     * 
     * @param map
     * @return int
     * 		执行结果
     */
	public int tran_upTemplate(Map<String, Object> map);
	
	/**
     * 取得模板编号
     * 
     * @param brandCode 品牌代码
     * @param content 模板内容
     * @return String 模板编号
     */
	public String getTemplateCode(String brandCode, String content);
	
	/**
     * 通过短信内容反推模板编号及变量值
     * 
     * @param brandCode 品牌代码
     * @param content 短信内容
     * @return Map 模板编号及变量值
     */
	public Map<String, Object> getTemplateInfo(String brandCode, String content);

}
