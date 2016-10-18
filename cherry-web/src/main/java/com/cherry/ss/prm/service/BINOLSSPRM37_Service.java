/*  
 * @(#)BINOLSSPRM37_Service.java     1.0 2011/05/31      
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
package com.cherry.ss.prm.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;
import com.cherry.cm.util.ConvertUtil;

@SuppressWarnings("unchecked")
public class BINOLSSPRM37_Service extends BaseService{
	/**
	 * 取得促销活动信息
	 * @param map
	 * @return
	 */
	public HashMap getDetailActInfo(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM37.getDetailActInfo");
		return (HashMap)baseServiceImpl.get(map);
	}
	
	/**
	 * 取得促销活动详细条件信息
	 * @param map
	 * @return
	 */
	public List getDetailActConList (Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM37.getDetailActConList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 取得促销活动详细结果信息
	 * @param map
	 * @return
	 */
	public List getDetailActRelList (Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM37.getDetailActRelList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 取得柜台父节点信息
	 * @param map
	 * @return
	 */
	public List selCounterParentList (Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM37.selCounterParentList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 更新促销活动
	 * @param map
	 * @return
	 */
	public int updPrmActivity (Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM37.updPrmActivity");
		return baseServiceImpl.update(map);
	}
	
	/**
	 * 更新促销活动
	 * @param map
	 * @return
	 */
	public int updPrmActHis(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM37.updPrmActHis");
		return baseServiceImpl.update(map);
	}
	
	/**
	 * 取得有效活动件数
	 * @param map
	 * @return
	 */
	public HashMap getValidActCount (Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM37.getValidActCount");
		return (HashMap)baseServiceImpl.get(map);
	}
	
	/**
	 * 取得活动规则ID
	 * @param map
	 * @return
	 */
	public HashMap getActivityRuleID (Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM37.getActivityRuleID");
		return (HashMap)baseServiceImpl.get(map);
	}
	
	/**
	 * 伦理删除促销活动规则
	 * @param map
	 * @return
	 */
	public void disablePrmActivity (String activeId){
		baseServiceImpl.update(activeId,"BINOLSSPRM37.disablePrmActivity");
	}
	
	/**
	 * 停止促销活动
	 * @param map
	 * @return
	 */
	public void stopPrmActivity (String activeId){
		baseServiceImpl.update(activeId,"BINOLSSPRM37.stopPrmActivity");
	}
	
	/**
	 * 伦理删除促销活动规则
	 * @param map
	 * @return
	 */
	public int delPrmActivityRule (Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM37.delPrmActivityRule");
		return baseServiceImpl.update(map);
	}
	
	/**
	 * 伦理删除促销活动条件
	 * @param map
	 * @return
	 */
	public int delPrmActivityRuleCondition (Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM37.delPrmActivityRuleCondition");
		return baseServiceImpl.update(map);
	}
	
	/**
	 * 伦理删除促销活动结果 
	 * @param map
	 * @return
	 */
	public int delPrmActivityRuleResult (Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM37.delPrmActivityRuleResult");
		return baseServiceImpl.update(map);
	}
	
	/**
	 * 伦理删除促销活动规则
	 * @param map
	 * @return
	 */
	public int getActivityTransHisCount (Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM37.getActivityTransHisCount");
		return (Integer)baseServiceImpl.get(map);
	}
	
	/**
	 * 
	 * @param map
	 * @return
	 */
	public void delCampain(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM37.delCampain");
		baseServiceImpl.update(map);
	}
}
