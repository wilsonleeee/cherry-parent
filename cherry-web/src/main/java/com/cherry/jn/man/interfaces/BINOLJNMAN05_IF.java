/*	
 * @(#)BINOLJNMAN05_IF.java     1.0 2012/10/30		
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
package com.cherry.jn.man.interfaces;

import java.util.List;
import java.util.Map;

/**
 * 积分规则配置一览IF
 * 
 * @author hub
 * @version 1.0 2012.10.30
 */
public interface BINOLJNMAN05_IF {
	
	/**
     * 取得规则配置List
     * 
     * @param map
     * @return
     * 		规则配置List
     */
    public List<Map<String, Object>> getCampGrpList(Map<String, Object> map);
    
    /**
     * 取得规则配置件数 
     * 
     * @param map
     * @return
     * 		规则配置件数
     */
    public int getCampGrpCount(Map<String, Object> map);
    
    /**
	 * 停用或者启用配置
	 * 
	 * @param map
	 * @throws Exception
	 */
	public void tran_editValid(Map<String, Object> map) throws Exception;
}
