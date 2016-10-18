/*  
 * @(#)BINOLSTCM05_IF.java    1.0 2011.09.02
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
package com.cherry.st.common.interfaces;

import java.util.List;
import java.util.Map;

import com.cherry.cm.core.ICherryInterface;

/**
 * 
 * 产品报损业务共通Interface
 * 
 * @author weisc
 * @version 1.0 2011.09.02
 */
public interface BINOLSTCM05_IF extends ICherryInterface{

	/**
	 * 将报损信息写入报损单据主从表。
	 * @param mainDate 报损单据主表数据；
	 * @param detailList 报损单据明细表数据，因为是多条，故以list形式提供；
	 * 
	 * */
	public int insertOutboundFreeAll(Map<String,Object> mainData,List<Map<String,Object>> detailList);
	
	/**
	 * 修改报损单据主表数据。
	 * @param praMap 报损单据主表数据；
	 * 
	 * */
	public int updateOutboundFreeMain(Map<String,Object> praMap);
	
	/**
	 * 根据报损单据来写入出库记录主从表，并修改库存。
	 * @param praMap
	 */
	public void changeStock(Map<String,Object> praMap);
	
	/**
	 * 给定报损单ID，取得概要信息。
	 * @param outboundFreeID
	 * @return
	 */
	public Map<String,Object> getOutboundFreeMainData(int outboundFreeID,String language);
	
	/**
	 * 给报损单ID，取得明细信息。
	 * @param outboundFreeID
	 * @return
	 */
	public List<Map<String,Object>> getOutboundFreeDetailData(int outboundFreeID,String language);
}
