/*  
 * @(#)BINOLSSCM08_IF.java     1.0 2012/09/27       
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
package com.cherry.ss.common.interfaces;

import java.util.List;
import java.util.Map;

import com.cherry.cm.core.ICherryInterface;

/**
 * 
 * 促销品移库操作共通IF
 * @author niushunjie
 * @version 1.0 2012.09.27
 */
public interface BINOLSSCM08_IF extends ICherryInterface{

    /**
     * 将移库单据的信息写入移库单主从表
     * @param mainData
     * @param detailList 移库明细表数据，因为是多条，故以list形式提供
     * @return
     */
	public int insertPrmShiftAll(Map<String,Object> mainData,List<Map<String,Object>> detailList);
	
	/**
	 * 修改移库单据的主表
	 * @param mainData
	 * @return
	 */
	public int updatePrmShiftMain(Map<String,Object> mainData);
	
    /**
     * 废弃移库单据的从表
     * @param mainData
     * @return
     */
    public int deletePromotionShiftDetail(Map<String,Object> mainData);
	
	/**
	 * 写入出库表，更改库存
	 * @param praMap
	 */
	public void changeStock(Map<String,Object> praMap);
	
	/**
	 * 取得移库单概要信息
	 * @param prmShiftMainID
	 * @return
	 */
	public Map<String,Object> getPrmShiftMainData(int prmShiftMainID,String... language);
	
	/**
	 * 取得移库单明细信息
	 * @param prmShiftMainID
	 * @return
	 */
	public List<Map<String,Object>> getPrmShiftDetailData(int prmShiftMainID,String... language);
}