
/*  
 * @(#)BINOLSTCM11_IF.java    1.0 2011-12-2     
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
 * 产品收货接口
 * 
 * @author zhanggl
 * @date 2011-12-2
 * 
 * */

public interface BINOLSTCM11_IF extends ICherryInterface{

	/**
	 * 插入产品收货主从表
	 * 
	 * */
	public int insertProductReceiveAll(Map<String,Object> mainData,List<Map<String,Object>> detailList);
	
	
	/**
	 * 根据收货单ID取得收货单主表信息
	 * 
	 * */
	public Map<String,Object> getProductReceiveMainData(int productReceiveID,String language);
	
	/**
	 * 根据收货单ID取得收货单明细信息
	 * 
	 * */
	 public List<Map<String, Object>> getProductReceiveDetailData(int productReceiveID,String language);
	
	 public int createProductInOutByReceiveID(Map<String,Object> praMap);
	

    /**
     * 修改订单主表数据。
     * @param praMap
     * @return
    */
    public int updateProductReceiveMain(Map<String,Object> praMap);
}
