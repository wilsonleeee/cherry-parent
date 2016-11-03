/*	
 * @(#)BINBECTSMG09_IF.java     1.0 2016/05/02		
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

import java.util.Map;

/**
 * 短信签名管理IF
 * 
 * @author hub
 * @version 1.0 2016/05/02
 */
public interface BINBECTSMG09_IF {
	
	/**
     * 取得品牌的短信签名
     * 
     * @param map
     * @return String
     * 		短信签名
     */
    public String getBrandSignName(String brandCode);
    
    /**
     * 更新短信签名
     * 
     * @param map
     * @return int
     * 		执行结果
     */
	public int tran_upSignName(Map<String, Object> map);

}
