/*  
 * @(#)BINOLPTJCS04_IF.java     1.0 2011/05/31      
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

public interface BINOLPTJCS04_IF extends ICherryInterface{
	/**
	 * 取得业务日期(通过日结状态计算)
	 * 
	 * @param map
	 * @return
	 */
	public String getBussinessDate(Map<String, Object> map);
	
	/**
	 * 取得业务日期
	 * 
	 * @param map
	 * @return
	 */
	public Map<String, Object> getBussinessDateMap(Map<String, Object> map);
	
	/**
	 * 取得产品总数
	 * 
	 * @param map
	 * @return
	 */
	public int getProCount(Map<String, Object> map);
	
	/**
	 * 取得产品信息List
	 * 
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getProList (Map<String, Object> map);
	
	/**
	 * 取得产品分类List
	 * 
	 * @param map 检索条件
	 * @return 树结构字符串
	 */
	public String getCategoryList(Map<String, Object> map) throws Exception;
	
    /**
     * 导出产品信息Excel
     * 
     * @param map
     * @return 返回产品信息List
     * @throws Exception 
     */
    public byte[] exportExcel(Map<String, Object> map) throws Exception;
    
    /**
     * 取得产品分类的上级分类
     * @param map
     * @return
     */
    public String getLocateCatHigher(Map<String, Object> map);
    
    /**
     * 取得同产品下非当前厂商ID的有效厂商记录集合
     * @param map
     * @return
     */
    public List<Map<String,Object>> getPrtVendorDetailList(Map<String, Object> map);
    
    /**
     * 取得同产品下有效或无效的厂商信息
     * @param map
     * @return
     */
    public Map<String,Object> getPrtDetailInfo(Map<String, Object> map);
    
    /**
     * 停用或启用
     * @param map
     * @return
     */
    public void tran_disOrEnable(Map<String, Object> map);
    
	/**
	 * 查询产品是否已存在有效的相同条码
	 * 
	 * @param map
	 * @return
	 */
	public Map<String, Object> getPrtBarCodeVF(Map<String, Object> map);
	
	/**
	 * 批量停用产品
	 * @param map
	 * @throws Exception
	 */
	public void tran_delProductInfo(Map<String, Object> map) throws Exception;
	
	/**
	 * 产品实时下发
	 * @param map
	 * @throws Exception
	 */
	public Map<String,Object> tran_issuedPrt(Map<String, Object> map) throws Exception;
	
	/**
	 * 通过WebService进行产品实时下发
	 * @param map
	 * @throws Exception
	 */
	public Map<String,Object> tran_issuedPrtByWS(Map<String, Object> map) throws Exception;
}
