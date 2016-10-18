package com.cherry.st.jcs.interfaces;

import java.util.List;
import java.util.Map;

import com.cherry.cm.core.ICherryInterface;

/*  
 * @(#)BINOLSTJCS03_IF.java    1.0 2012-6-18     
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
public interface BINOLSTJCS03_IF extends ICherryInterface {

	/**
	 * 根据实体仓库ID取得仓库信息
	 * 
	 * */
	public Map getDepotInfoById(Map map);
	
	/**
	 * 根据实体仓库ID取得部门仓库关系
	 * 
	 * 
	 * */
	public List<Map> getRelInfoByDepotId(Map map);
	
	/**
	 * 更新仓库信息
	 * 
	 * 
	 * */
	public int tran_updateDepotInfo(Map map) throws Exception;
	
	/**
	 * 验证编辑后的实体仓库是否已经存在
	 * 
	 * */
	public List<Map> isExist(Map map);
}
