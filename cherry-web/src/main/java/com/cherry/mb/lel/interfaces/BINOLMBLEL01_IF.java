/*  
 * @(#)BINOLMBLEL01_IF.java     1.0 2011/07/20      
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
package com.cherry.mb.lel.interfaces;

import java.util.List;
import java.util.Map;

import com.cherry.cm.core.ICherryInterface;

/**
 * 会员等级维护IF
 * 
 * @author lipc
 * @version 1.0 2011/07/20
 */
public interface BINOLMBLEL01_IF extends ICherryInterface{
	
	/**
	 * 取得会员等级List
	 * 
	 * @param brandInfoId
	 * 
	 * @return
	 */
	public List<Map<String, Object>> getLevelList(Map<String, Object> map);
	
	/**
	 * 取得会员等级详细List
	 * 
	 * @param brandInfoId
	 * 
	 * @return
	 * @throws Exception 
	 */
	public List<Map<String, Object>> getLelDetailList(Map<String, Object> map) throws Exception;

	/**
	 * 会员等级按照有效期分组处理
	 * 
	 * @param list
	 * 
	 * @return
	 */
	public List<Map<String, Object>> doList(Map<String, Object> map,List<Map<String, Object>> list);
	
	/**
	 * 保存会员等级
	 * 
	 * @param map
	 * 
	 * @return
	 */
	public void tran_save(Map<String, Object> map) throws Exception;
	
	/**
	 * 取得业务日期
	 * 
	 * @param map
	 * @return String
	 */
	public String getBussinessDate(Map<String, Object> map);
	
	/**
	 * 取得会员等级ID
	 * 
	 * @param map
	 * 
	 * @return
	 */
	public int getLevelId(Map<String, Object> map);

	public int updMemberDate(Map<String, Object> map);

	public int getDefaultFlag(Map<String, Object> map);

	public int getMemberCount(Map<String, Object> map);

	/**
	 * 取得升降级规则个数
	 * 
	 * @param map
	 * 
	 * @return
	 */
	public int getUpLevelRuleCount(Map<String, Object> map);
	
	/**
	 * 取得默认等级
	 * 
	 * @param map
	 * 
	 * @return
	 */
	public int getDefaultLevel(Map<String, Object> map);
	
	/**
	 * 执行规则重算
	 * 
	 * @param map
	 * 
	 * @return
	 */
	public void tran_reCalc(Map<String, Object> map) throws Exception;
	
	/**
	 * 执行下发处理
	 * 
	 * @param map
	 * 			参数集合
	 */
	public Map<String, Object> sendLevel(Map<String, Object> map) throws Exception;
}
