/*  
 * @(#)BINOLMOCIO02_Service.java     1.0 2011/05/31      
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
package com.cherry.mo.cio.service;

import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;
@SuppressWarnings("unchecked")
public class BINOLMOCIO02_Service extends BaseService {

	public List<Map<String,Object>> getPaperList(Map<String,Object> map)
	{
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLMOCIO02.getPaperList");
		return baseServiceImpl.getList(map);
	}
	
	public int getPaperCount(Map<String,Object> map)
	{
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLMOCIO02.getPaperCount");
		return baseServiceImpl.getSum(map);
	}
	
	/**
	 * 启用或者停用答卷
	 * @param map
	 */
	public void paperDisableOrEnable(Map<String,Object> map){
		baseServiceImpl.update(map, "BINOLMOCIO02.paperDisableOrEnable");
	}
	
	public Map<String,Object> getPaperForDisable(Map<String,Object> map){
		return (Map<String, Object>) baseServiceImpl.get(map, "BINOLMOCIO02.getPaperForDisable");
	}
	
	/**
	 * 删除所选问卷及问卷下的问题
	 * @param list
	 */
	public void deletePaperAndQuestion(Map<String,Object> map){
		baseServiceImpl.update(map, "BINOLMOCIO02.deletePaper");
		baseServiceImpl.update(map, "BINOLMOCIO02.deletePaperQuestion");
	}
	
	/**
	 * 检查是否存在某种类型某种状态的时间有效问卷
	 * 
	 * */
	public List<Map<String,Object>> isExistSomePaper(Map<String,Object> map){
		return baseServiceImpl.getList(map, "BINOLMOCIO02.isExistSomePaper");
	}
	
	/**
	 * 取得某张问卷的下发信息
	 * 
	 * */
	public List<Map<String,Object>> getPaperIssum(Map<String,Object> map){
		return baseServiceImpl.getList(map, "BINOLMOCIO02.getPaperIssum");
	}
	
	/**
	 * 取得某张问卷的下发类型：禁止到下发柜台/允许到下发柜台
	 * 
	 * */
	public int getControlFlag(Integer paperId){
		List<Map<String,Object>> list = baseServiceImpl.getList(paperId, "BINOLMOCIO02.getControlFlag");
		
		if(null == list || list.size() == 0){
			return -1;
		}else{
			return (Integer)list.get(0).get("ControlFlag");
		}
	}
}
