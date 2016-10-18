/*	
 * @(#)BINOLPLSCF17_IF.java     1.0 2013/08/27
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
package com.cherry.pl.scf.interfaces;

import java.util.List;
import java.util.Map;

/**
 * 业务处理器管理画面IF
 * 
 * @author hub
 * @version 1.0 2013/08/27
 */
public interface BINOLPLSCF17_IF {
	
	/**
     * 取得业务处理器列表
     * 
     * @param map
     * @return List
     * 		业务处理器列表
     */
    public List<Map<String, Object>> getHandlerList(Map<String, Object> map);
    
    /**
     * 取得业务处理器件数 
     * 
     * @param map
     * @return int
     * 		业务处理器件数 
     */
    public int getHandlerCount(Map<String, Object> map);
    
    /**
	 * 停用或者启用配置
	 * 
	 * @param map
	 * @throws Exception
	 */
	public void tran_editValid(Map<String, Object> map) throws Exception;
	
	/**
	 * 发送刷新业务处理器MQ
	 * 
	 * @param map 发送信息
	 * @throws Exception 
	 */
	public void sendRfHandlerMsg(Map<String, Object> map) throws Exception;

}
