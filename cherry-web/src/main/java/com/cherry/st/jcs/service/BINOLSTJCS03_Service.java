package com.cherry.st.jcs.service;

import java.util.List;
import java.util.Map;

import org.springframework.cache.annotation.CacheEvict;

import com.cherry.cm.service.BaseService;

/*  
 * @(#)BINOLSTJCS03_Service.java    1.0 2012-6-18     
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
public class BINOLSTJCS03_Service extends BaseService {

	/**
	 * 根据实体仓库ID取得仓库信息
	 * 
	 * */
	public Map getDepotInfoById(Map map){
		return (Map) baseServiceImpl.get(map, "BINOLSTJCS03.getDepotInfoById");
	}
	
	/**
	 * 根据实体仓库ID取得部门仓库关系
	 * 
	 * 
	 * */
	public List<Map> getRelInfoByDepotId(Map map){
		return baseServiceImpl.getList(map, "BINOLSTJCS03.getRelInfoByDepotId");
	}
	
	/**
	 * 更新仓库信息
	 * 
	 * 
	 * */
	@CacheEvict(value="CherryIvtCache",allEntries=true,beforeInvocation=false)
	public int updateDepotInfo(Map map){
		return baseServiceImpl.update(map, "BINOLSTJCS03.updateDepotInfo");
	}
	
	/**
	 * 删除部门仓库关系
	 * 
	 * */
	@CacheEvict(value="CherryIvtCache",allEntries=true,beforeInvocation=false)
	public void deleteRelInfo(Map map){
		baseServiceImpl.remove(map,"BINOLSTJCS03.deleteRelInfo");
	}
	
	/**
	 * 验证编辑后的实体仓库是否已经存在
	 * 
	 * */
	public List<Map> isExist(Map map){
		return baseServiceImpl.getList(map, "BINOLSTJCS03.isExist");
	}
}
