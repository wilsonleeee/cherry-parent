
/*  
 * @(#)BINOLCM21_IF.java    1.0 2011-9-16     
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
package com.cherry.cm.cmbussiness.interfaces;

import java.util.Map;

import com.cherry.cm.core.ICherryInterface;

public interface BINOLCM21_IF extends ICherryInterface {

	/**
	 * 取得产品信息
	 * 
	 * 
	 * */
	public String getProductInfo(Map<String,Object> map);
	
	/**
	 * 取得柜台产品信息
	 * 
	 * 
	 * */
	public String getCntProductInfo(Map<String,Object> map);
	
  /**
     * 取得促销品信息
     * 
     * 
     * */
    public String getPrmProductInfo(Map<String,Object> map);
	
	/**
	 * 取得柜台信息
	 * 
	 * */
	public String getCounterInfo(Map<String,Object> map);
	
	/**
	 * 获得部门信息
	 * 
	 * */
	public String getDepartInfo(Map<String,Object> map);
	
	/**
	 * 获得产品分类
	 * 
	 * */
	public String getProductCategory(Map<String,Object> map);
	
	/**
	 * 獲取倉庫信息
	 * 
	 * 
	 * */
	public String getDeportInfo(Map<String,Object> map);
	
	/**
	 * 获取销售人员信息
	 * 
	 * 
	 * */
	public String getSalesStaffInfo(Map<String,Object> map);
	
	/**
	 * 获取员工信息
	 * 
	 * 
	 * */
	public String getEmployeeInfo(Map<String,Object> map);
	
	/**
	 * 获取营业员信息
	 * @param map
	 * @return
	 */
	public String getBaInfo(Map<String, Object> map);
	
	/**
	 * 获取代理商信息
	 * @param map
	 * @return
	 */
	public String getResellerInfo(Map<String, Object> map);
	
	/**
	 * 获取往来单位(合作伙伴)
	 * 
	 * 
	 * */
	public String getBussinessPartnerInfo(Map<String,Object> map);
	
	/**
	 * 获取区域信息
	 * 
	 * 
	 * */
	public String getRegionInfo(Map<String,Object> map);
	
	/**
	 * 获取逻辑仓库信息
	 * 
	 * 
	 * */
	public String getLogicInventoryInfo(Map<String,Object> map);
	
	
	/**
	 * 获取渠道信息
	 * 
	 * 
	 * */
	public String getChannelInfo(Map<String,Object> map);
	
	/**
	 * 获取会员问题信息
	 * 
	 * 
	 * */
	public String getIssueInfo(Map<String,Object> map);
	
	/**
	 * 获取组织详细信息
	 * 
	 * 
	 * */
	public String getOrganizationDetail(Map<String,Object> map);
	
	/**
	 * 获取柜台详细信息
	 * 
	 * 
	 * */
	public String getCounterDetail(Map<String,Object> map);
	
	/**
	 * 指定codeType及values模糊查询获取Code集合
	 * @param map
	 * @return
	 */
	public String getCodes(Map<String,Object> map);
	/**
	 * 获得柜台产品信息包含库存
	 * @param session
	 * @param map
	 * @return
	 */
	public String getCntProductInfoAddSocket(String entitySocketId,Map<String, Object> map) throws Exception;
	
	/**
	 * 获取柜台的营业员信息
	 * 
	 * */
	public String getBaByCounter(Map<String, Object> map) throws Exception;


	/**
	 * 取得产品信息（浓妆淡抹）
	 * 
	 * */
	public String getProductInfoNZDM(Map<String, Object> paramMap);
	
}
