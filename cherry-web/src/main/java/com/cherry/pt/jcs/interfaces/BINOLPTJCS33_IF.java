/*  
 * @(#)BINOLPTJCS03_IF.java     1.0 2011/05/31      
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

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.ICherryInterface;

public interface BINOLPTJCS33_IF extends ICherryInterface{
	
	/**
	 * 产品添加插表处理
	 * 
	 * @param map
	 * @return 无
	 * @throws Exception 
	 */
	public void tran_addProduct(Map<String, Object> map) throws Exception;
	
	/**
	 * 取得产品分类List
	 * 
	 * @param map
	 * @return List
	 */
	public List<Map<String, Object>> getCateValList(Map<String, Object> map);
	/**
	 * 取得产品分类List
	 * 
	 * @param map
	 * @return String
	 */
	public List<Map<String, Object>> getCategoryList(Map<String, Object> map);
	/**
	 * 取得产品ID
	 * 
	 * @param map
	 * @return int
	 */
	public int getProductId(Map<String, Object> map);
	/**
	 * 取得产品ID集合（根据barcode）
	 * 
	 * @param map
	 * @return int
	 */
	public List<Integer> getProductIds(Map<String, Object> map);
	
	/**
	 * 取得促销产品ID
	 * 
	 * @param map
	 * @return int
	 */
	public int getPromotionId(Map<String, Object> map);
	
	/**
	 * 取得非此分类下一级的分类属性值ID集合
	 * 
	 * @param map
	 * 
	 * @return
	 */
	public List<Map<String, Object>> getSubCateList(Map<String, Object> map);
	
	/**
	 * 取得此分类上一级的分类属性值ID 
	 * 
	 * @param map
	 * 
	 * @return
	 */
	public List<Map<String, Object>> getPatCateList(Map<String, Object> map);
	
	/**
	 * 验证UnitCode编码规则
	 * @param userInfo
	 * @param unitCode
	 * @return
	 */
	public boolean checkUnitCode(UserInfo userInfo,String unitCode);
	
	/**
	 * 验证BarCode编码规则
	 * @param userInfo
	 * @param unitCode
	 * @return
	 */
	public boolean checkBarCode(UserInfo userInfo,String barCode);
	
	/**
	 * 取得厂商编码(barcode+三位递增码)
	 * @param map
	 * @return
	 */
	public String getUnitCodeRightTree(Map<String, Object> map);
}
