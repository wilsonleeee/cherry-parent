/*  
 * @(#)BINOLPTJCS01_IF.java     1.0 2011/05/31      
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
@SuppressWarnings("unchecked")
public interface BINOLPTJCS01_IF extends ICherryInterface{
	
	/**
	 * 根据属性值查询分类属性值ID
	 * 
	 * @param map
	 * 
	 * @return
	 */
	public int getCateValId1(Map<String, Object> map);
	
	/**
	 * 根据属性值名查询分类属性值ID
	 * 
	 * @param map
	 * 
	 * @return
	 */
	public int getCateValId2(Map<String, Object> map);

	/**
	 * 根据分类名取得分类ID
	 * 
	 * @param map
	 * 
	 * @return
	 */
	public int getPropId1(Map<String, Object> map);
	
	/**
	 * 根据分类终端下发取得分类ID
	 * 
	 * @param map
	 * 
	 * @return
	 */
	public int getPropId2(Map<String, Object> map);
	
	/**
	 * 取得分类Info
	 * 
	 * @param map
	 * 
	 * @return
	 */
	public Map getCategoryInfo(Map<String, Object> map);
	
	/**
	 * 取得分类选项值Info
	 * 
	 * @param map
	 * 
	 * @return
	 */
	public Map getCateVal(Map<String, Object> map);
	
	/**
	 * 取得产品分类List
	 * 
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getCategoryList(Map<String, Object> map);
	
	/**
	 * 取得分类选项值List
	 * 
	 * @param map
	 * 
	 * @return
	 */
	public List<Map<String, Object>> getCateValList(Map<String, Object> map);
	
	/**
	 * 移动分类树形显示顺序
	 * 
	 * @param map
	 * @return
	 */
	public void tran_move(Map<String, Object> map) throws Exception;
	
	/**
	 * 保存分类Info
	 * 
	 * @param map
	 * 
	 * @return
	 */
	public void tran_save(Map<String, Object> map)throws Exception;
	
	/**
	 * 保存分类选项值
	 * 
	 * @param map
	 * 
	 * @return
	 */
	public void tran_saveVal(Map<String, Object> map)throws Exception;
}
