/*	
 * @(#)BINOLPTJCS02_IF.java     1.0 2012-7-25 		
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
package com.cherry.pt.jcs.interfaces;

import java.util.List;
import java.util.Map;

import com.cherry.cm.core.ICherryInterface;

/**
 * 
 * 	产品扩展属性详细Interface
 * 
 * @author jijw
 * @version 1.0 2012-7-25
 */

public interface BINOLPTJCS02_IF extends ICherryInterface {
	
	/**
	 * 取得产品扩展属性List
	 * 
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getProductExtPropertyList (Map<String, Object> map);
	
	/**
	 * 查询产品扩展属性总数
	 * 
	 * @param map 查询条件
	 * @return int
	 */
	public int getProductExtPropertyCount(Map<String, Object> map);
	
	/**
	 * 更新产品扩展属性
	 * 
	 * @param map
	 * 
	 * @return
	 */
	public int tran_updateProductExtProperty(Map<String, Object> map) throws Exception;
	
	/**
	 * 增加产品扩展属性
	 * 
	 * @param map
	 * 
	 * @return
	 */
	public void tran_insertProductExtProperty(Map<String, Object> map) throws Exception;
	
	/**
	 * 
	 * 停用或启用产品扩展属性
	 * @param map
	 * @return
	 */
	public int tran_disOrEnableExtProp(Map<String, Object> map) throws Exception;
	
	/***  产品预设值 start   ***/
	
	/**
	 * 取得产品扩展属性预设值List
	 * 
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getProductExtDefValueList (Map<String, Object> map);
	
	/**
	 * 保存编辑
	 * @param codeList 编辑后的扩展属性预设值
	 * @param extDefValList 编辑后的扩展属性值
	 * @param sessionMap 用户及操作信息
	 * 
	 * */
	public String tran_savaEdit(List<String[]> extDefValList,Map<String,Object> extDefValMap,Map<String,Object> sessionMap) throws Exception;
	
	/**
	 * 取得产品扩展属性预设值List（checkbox）
	 * 
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getProductExtDefValueListForChk (Map<String, Object> map);

	/***  产品预设值 end   ***/

	/**
	 * 取得扩展属性key
	 * @param map
	 * @param codeKey Code管理中的key
	 * @return
	 */
	public String getPropertyId(Map<String,Object> map, String codeKey);
}
