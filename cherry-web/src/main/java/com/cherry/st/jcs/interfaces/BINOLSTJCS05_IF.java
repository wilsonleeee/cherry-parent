
/*  
 * @(#)BINOLSTJCS05_IF.java    1.0 2011-9-5     
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

public interface BINOLSTJCS05_IF extends ICherryInterface {

	/**
	 * 取得仓库业务关系list
	 * 
	 * */
	public List<Map<String,Object>> getDepotBusinessList(Map<String,Object> map);
	
	/**
	 * 取得仓库业务总数
	 * 
	 * */
	public int getDepotBusinessCount(Map<String,Object> map);
	
	/**
	 * 获取添加时的树节点
	 * 
	 * 
	 * */
	public List<Map<String, Object>> getAddTree(Map<String,Object> map) throws Exception;
	
	/**
	 * 获取编辑时的树节点
	 * 
	 * 
	 * */
	public List<Map<String,Object>> getEditTree(Map<String,Object> map) throws Exception;
	
	/**
	 * 保存添加
	 * 
	 * */
	public void tran_saveAdd(Map<String,Object> map,List<Map<String,Object>> deportOrReginList)throws Exception;
	
	/**
	 * 删除仓库业务对应关系
	 * 
	 * 
	 * */
	public void tran_deleteDepotBusiness(List<Map<String, Object>> list,Map<String,Object> map)throws Exception;
	
	/**
	 * 根据仓库业务关系ID查询出出库ID
	 * 
	 * 
	 * */
	public Map<String,Object>getOutIdById(Map<String,Object> map);
	
	/**
	 * 取得编辑信息，包括出库方和入库方
	 * 
	 * */
	public Map<String,Object> getEditInfo(Map<String,Object> map)throws Exception;
	
	/**
	 * 保存编辑
	 * 
	 * 
	 * */
	public void tran_saveEdit(Map<String,Object> map,List<Map<String,Object>> deportOrReginList)throws Exception;
}
