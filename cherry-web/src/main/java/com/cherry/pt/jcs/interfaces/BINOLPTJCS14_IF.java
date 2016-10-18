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
 * 柜台产品价格维护IF
 * 
 * @author JiJW
 * @version 1.0 2014-6-12
 */
public interface BINOLPTJCS14_IF extends ICherryInterface {
	
	// **************************************  柜台产品配置  Start  *************************************************************** //
	/**
	 * 取得柜台产品配置主表信息
	 * @param map
	 * @return
	 */
	public Map<String,Object> getDepartProductConfig(Map<String, Object> map);
	
	/**
	 * 插入柜台产品配置表
	 * 
	 * @param map
	 * @return int
	 */
	public int tran_addDepartProductConfig(Map<String, Object> map);
	
	/**
	 * 取得地点
	 * @param map
	 * @return
	 * @throws JSONException 
	 */
	public List getLocation(Map map) throws JSONException;
	
	/**
	 * 取得产品价格方案
	 * @param map
	 * @return
	 */
	public List getPrtPriceSolutionList(Map<String, Object> map);
	
	/**
	 * 取柜台对应的产品价格配置的方案
	 * @param map
	 * @return
	 */
	public Map<String,Object> getPrtPriceSoluInfoByCnt(Map<String, Object> map);
	
	/**
	 * 保存柜台产品配置信息
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public int tran_addConfigDetailSave(Map<String, Object> map) throws Exception;
	
	/**
	 * 保存产品价格方案主表
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public int tran_addPrtPriceSolu(Map<String, Object> map) throws Exception;
	
	/**
	 * 修改产品价格方案主表
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public int tran_updPrtPriceSolu(Map<String, Object> map) throws Exception;
	
	/**
	 * 保存产品价格方案明细表
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public int tran_addPrtPriceSoluDetail(Map<String, Object> map) throws Exception;
	
	/**
	 * 去除方案中当前编辑的产品
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public int tran_delPrtPriceSoluDetail(Map<String, Object> map) throws Exception;
	
	/**
	 * 取得产品树形
	 * @param map
	 * @return
	 * @throws Exception 
	 */
	public String getPrtTree(Map<String, Object> map) throws Exception;

	/**
	 * 取得方案中的产品信息
	 * @param map
	 * @return
	 * @throws JSONException 
	 */
	public Map<String,Object> getPrtDetailInfo(Map<String, Object> map) throws JSONException;
	
	/**
	 * 取得产品价格方案主表信息
	 * @param map
	 * @return
	 */
	public Map<String,Object> getPrtPriceSoluInfo(Map<String, Object> map);
	
	/**
	 * 取得业务日期
	 * 
	 * @param map
	 * @return
	 */
	public String getBussinessDate(Map<String, Object> map);
	
	/**
	 * 获取产品对应的按显示顺序排序的分类属性ID
	 * 
	 * */
	public String getPrtCateVal(Map<String,Object> map);
}
