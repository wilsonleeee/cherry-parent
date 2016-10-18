/*	
 * @(#)BINOLCPACT12_IF.java     1.0 @2014-12-16		
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
package com.cherry.cp.act.interfaces;

import java.util.List;
import java.util.Map;

import com.cherry.cm.core.ICherryInterface;

/**
 * 活动产品库存Interface
 * 
 * @author menghao
 * 
 */
public interface BINOLCPACT12_IF extends ICherryInterface {

	/**
	 * 取得活动产品库存数量
	 * 
	 * @param map
	 * @return
	 */
	public int getCampaignStockCount(Map<String, Object> map);

	
	/**
	 * 取得活动产品库存详细List
	 * 
	 * @param map
	 * @return
	 * @throws Exception 
	 */
	public List<Map<String, Object>> getCampaignStockList(Map<String, Object> map) throws Exception;

	/**
	 * 取得活动指定柜台库存详细的主信息
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> getCampaignStockDetailMain(Map<String, Object> map) throws Exception;

	/**
	 * 取得活动指定柜台库存的产品详细
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> getCampaignStockProductDetail(Map<String, Object> map) throws Exception;

	/**
	 * 取得主题活动名称
	 * @param map
	 * @return
	 */
	public String getCampName(Map<String, Object> map);
	
	/**
	 * 取得活动名称
	 * @param map
	 * @return
	 */
	public String getSubCampName(Map<String, Object> map);
	
	/**
	 * 保存对活动产品库存的编辑
	 * @param map
	 * @param list
	 * @throws Exception
	 */
	public void tran_save(Map<String, Object> map, List<String[]> list) throws Exception;
}
