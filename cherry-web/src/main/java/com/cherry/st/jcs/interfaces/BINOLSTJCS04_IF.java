
/*  
 * @(#)BINOLSTJCS04_IF.java    1.0 2011-8-23     
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
package com.cherry.st.jcs.interfaces;

import java.util.List;
import java.util.Map;

import com.cherry.cm.core.ICherryInterface;

public interface BINOLSTJCS04_IF extends ICherryInterface {

	/**
	 * 取得部门仓库关系List
	 * 
	 * 
	 * */
	public List<Map<String,Object>> getInventoryList(Map<String,Object> map);
	
	/**
	 * 取得柜台部门关系总数
	 * 
	 * 
	 * */
	public int getInventoryCount(Map<String,Object> map);
	
	/**
	 * 取得非柜台实体仓库List
	 * 
	 * 
	 * */
	public List<Map<String,Object>> getDepotInfoList(Map<String,Object> map);
	
	/**
	 * 取得非柜台部门List,树形结构
	 * 
	 * */
	public List<Map<String,Object>> getJsDepartList(Map<String,Object> map);
	
	/**
	 * 设定仓库部门关系
	 * 
	 * 
	 * */
	public void tran_setInvDepRelation(Map<String,Object> map,List<Map<String,Object>> list) throws Exception;
	
	/**
	 * 保存编辑信息
	 * 
	 * */
	public void tran_saveEditInfo(Map<String,Object> map) throws Exception;
	
	/**
	 * 删除操作
	 * 
	 * 
	 * */
	public void tran_deleteInvDepRelation(List<Map<String,Object>> list) throws Exception;
	
	/**
	 * 取得非柜台部门List,线性结构
	 * 
	 * */
	public List<Map<String,Object>> getDepartList(Map<String,Object> map);
}
