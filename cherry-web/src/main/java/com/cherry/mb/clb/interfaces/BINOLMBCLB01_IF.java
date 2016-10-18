/*	
 * @(#)BINOLMBCLB01_IF.java     1.0 2014/04/29	
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
package com.cherry.mb.clb.interfaces;

import java.util.List;
import java.util.Map;

/**
 * 会员俱乐部一览IF
 * 
 * @author hub
 * @version 1.0 2014.04.29
 */
public interface BINOLMBCLB01_IF {
	
	/**
     * 取得俱乐部List
     * 
     * @param map
     * @return
     * 		俱乐部List
     */
    public List<Map<String, Object>> getClubList(Map<String, Object> map);
	
    /**
     * 取得俱乐部件数 
     * 
     * @param map
     * @return
     * 		俱乐部件数
     */
    public int getClubCount(Map<String, Object> map);
    
    /**
	 * 停用俱乐部
	 * 
	 * @param map
	 * @throws Exception
	 */
	public void tran_editValid(Map<String, Object> map) throws Exception;
	
	/**
	 * 是否上一次下发处理还未执行完成
	 * 
	 * @param brandInfoId
	 * 			品牌ID
	 * @return true: 未完成   false: 已完成
	 */
	public boolean isBatchExec(String brandInfoId);
	
	/**
	 * 是否需要执行本次下发处理
	 * 
	 * @param brandInfoId
	 * 			品牌ID
	 * @return true: 需要   false: 不需要
	 */
	public boolean isNeedSend(String brandInfoId);
	
	/**
	 * 执行下发处理
	 * 
	 * @param map
	 * 			参数集合
	 */
	public Map<String, Object> sendClub(Map<String, Object> map) throws Exception;
	
	/**
     * 取得俱乐部List(带权限)
     * 
     * @param map
     * @return
     * 		俱乐部List
     */
    public List<Map<String, Object>> getClubWithPrivilList(Map<String, Object> map);
    
    /**
     * 取得俱乐部件数 (带权限)
     * 
     * @param map
     * @return
     * 		俱乐部件数
     */
    public int getClubWithPrivilCount(Map<String, Object> map);

}
