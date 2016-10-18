/*
 * @(#)BINOLPTJCS14_IF.java v1.0 2014-6-12
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
import com.googlecode.jsonplugin.JSONException;

/**
 * 产品方案柜台分配维护IF
 * 
 * @author JiJW
 * @version 1.0 2014-6-12
 */
public interface BINOLPTJCS17_IF extends ICherryInterface {

	/**
	 * 取得分配地点
	 */
	public List getAllotLocationOld(Map map) throws JSONException,Exception;
	
	/**
	 * 取得分配地点
	 */
	public Map<String,Object> getAllotLocation(Map map) throws JSONException,Exception;
	
	/**
	 * 取得分配地点(门店自设)
	 */
	public Map<String,Object> getAllotLocationCnt(Map map) throws JSONException,Exception;
	
	/**
	 * 取得方案对应的配置信息
	 */
	public Map<String, Object> getDPConfigDetailBySolu(Map<String, Object> map) throws Exception;
	
	/**
	 * 保存柜台产品配置信息
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public int tran_addConfigDetailSave(Map<String, Object> map) throws Exception;
	
	/**
	 * 取得方案对应的原柜台List
	 * @param map
	 */
	public List<Map<String, Object>> getPrtSoluWithDepartHis(Map<String, Object> map) throws Exception;
	
	/**
	 * 取得业务日期
	 * 
	 * @param map
	 * @return
	 */
	public Map<String, Object> getBussinessDateMap(Map<String, Object> map);

	/**
	 * 更新产品方案配置履历表  
	 * @param map
	 * @return 
	 */
	public String mergePrtSoluWithDepartHis(Map<String, Object> map);
	
	/**
	 * 更新指定产品方案配置履历表
	 * 
	 * 删除方案对应的履历数据，并将最新的方案配置信息插入到履历表
	 * 
     * @param praMap
     * praMap参数说明：productPriceSolutionID （方案ID）,
     * praMap参数说明：placeType（地点类型）,
     * praMap参数说明：SaveJson（地点集合）,
     * praMap参数说明：组织ID,品牌ID等共通信息,
	 * @throws JSONException,Exception 
	 */
	@SuppressWarnings("unchecked")
	public void updPrtSoluWithDepartHis(Map<String,Object> map) throws JSONException,Exception;
	
	
	/**
	 * 取得当前方案分配的地点是否已被其他方案分配过的List
	 * @param map
	 * @return
	 */
	public Map<String,Object> getExistCnt(Map<String, Object> map);
	
	/**
	 * 柜台产品实时下发
	 * @param map
	 * @throws Exception
	 */
	public Map<String,Object> tran_issuedCntPrt(Map<String, Object> map) throws Exception;
	
	
	/**
	 * 取得业务日期
	 * 
	 * @param map
	 * @return
	 */
	public String getBussinessDate(Map<String, Object> map);
	
}
