
/*  
 * @(#)BINOLCM21_Service.java    1.0 2011-9-15     
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
package com.cherry.cm.cmbussiness.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

@SuppressWarnings("unchecked")
public class BINOLCM21_Service extends BaseService {

	/**
	 * 根据输入的字符串模糊查询出对应的产品信息
	 * 
	 * */
	public List<Map<String,Object>> getProductInfo(Map<String,Object> map){
		
		return baseServiceImpl.getList(map, "BINOLCM21.getProductInfo");
	}
	
	/**
	 * 取得特价产品方案对应的方案中的产品信息
	 * 
	 * */
	public List<Map<String,Object>> getPrtBySpeSoluDetail(Map<String,Object> map){
		
		return baseServiceImpl.getList(map, "BINOLCM21.getPrtBySpeSoluDetail");
	}
	
	/**
	 * 根据输入的字符串模糊查询出柜台对应的产品信息（特价产品方案）
	 * 
	 * */
	public List<Map<String,Object>> getSpeProductInfo(Map<String,Object> map){
		
		return baseServiceImpl.getList(map, "BINOLCM21.getSpeProductInfo");
	}
	
	
	/**
	 * 检查柜台是否有分配可用的方案
	 * 
	 * */
	public List<Map<String,Object>> chkCntSoluData(Map<String,Object> map){
		
		return baseServiceImpl.getList(map, "BINOLCM21.chkCntSoluData");
	}
	
	/**
	 * 根据输入的字符串模糊查询出柜台对应的产品信息
	 * 
	 * */
	public List<Map<String,Object>> getCntProductInfo(Map<String,Object> map){
		
		return baseServiceImpl.getList(map, "BINOLCM21.getCntProductInfo");
	}
	
    /**
     * 根据输入的字符串模糊查询出对应的促销品信息
     * 
     * */
    public List<Map<String,Object>> getPrmProductInfo(Map<String,Object> map){
        
        return baseServiceImpl.getList(map, "BINOLCM21.getPrmProductInfo");
    }
	
	/**
	 * 根据输入的字符串模糊查询出对应的柜台信息
	 * 
	 * */
	public List<Map<String,Object>> getCounterInfo(Map<String,Object> map){
		
		return baseServiceImpl.getList(map, "BINOLCM21.getCounterInfo");
	}
	
	/**
	 * 根据输入字符串模糊查询部门信息
	 * 
	 * 
	 * */
	public List<Map<String,Object>> getDepartInfo(Map<String,Object> map){
		return baseServiceImpl.getList(map, "BINOLCM21.getDepartInfo");
	}
	
	/**
	 * 根据输入字符串模糊查询产品分类
	 * 
	 * 
	 * */
	public List<Map<String,Object>> getProductCategory(Map<String,Object> map){
		return baseServiceImpl.getList(map, "BINOLCM21.getProductCategory");
	}
	
	/**
	 * 根據輸入字符串模糊查詢實體倉庫信息
	 * 
	 * 
	 * */
	public List<Map<String,Object>> getDeportInfo(Map<String,Object> map){
		return baseServiceImpl.getList(map, "BINOLCM21.getDeportInfo");
	}
	
	/**
	 * 根据输入的字符串模糊查询员工信息
	 * 
	 * 
	 * */
	public List<Map<String,Object>> getEmployeeInfo(Map<String,Object> map){
		return baseServiceImpl.getList(map, "BINOLCM21.getEmployeeInfo");
	}
	
	/**
	 * 根据输入的字符串模糊查询营业员信息
	 * 
	 * 
	 * */
	public List<Map<String,Object>> getBaInfo(Map<String,Object> map){
		return baseServiceImpl.getList(map, "BINOLCM21.getBaInfo");
	}
	
	/**
	 * 根据输入的字符串模糊查询代理商信息
	 * 
	 * 
	 * */
	public List<Map<String,Object>> getResellerInfo(Map<String,Object> map){
		return baseServiceImpl.getList(map, "BINOLCM21.getResellerInfo");
	}
	
	/**
	 * 根据输入的字符串模糊查询销售人员信息
	 * 
	 * 
	 * */
	public List<Map<String,Object>> getSalesStaffInfo(Map<String,Object> map){
		return baseServiceImpl.getList(map, "BINOLCM21.getSalesStaffInfo");
	}
	
	/**
	 * 根据输入的字符串模糊查询往来单位(合作伙伴)
	 * 
	 * 
	 * */
	public List<Map<String,Object>> getBussinessPartnerInfo(Map<String,Object> map){
		return baseServiceImpl.getList(map, "BINOLCM21.getBussinessPartnerInfo");
	}
	
	/**
	 * 根据输入字符串模糊查询区域信息
	 * 
	 * 
	 * */
	public List<Map<String,Object>> getRegionInfo(Map<String,Object> map){
		return baseServiceImpl.getList(map, "BINOLCM21.getRegionInfo");
	}
	
	/**
	 * 根据输入字符串模糊查询逻辑仓库信息
	 * 
	 * 
	 * */
	public List<Map<String,Object>> getLogicInventoryInfo(Map<String,Object> map){
		return baseServiceImpl.getList(map, "BINOLCM21.getLogicInventoryInfo");
	}
	
	/**
	 * 根据输入字符串模糊查询渠道信息
	 * 
	 * 
	 * */
	public List<Map<String,Object>> getChannelInfo(Map<String,Object> map){
		return baseServiceImpl.getList(map, "BINOLCM21.getChannelInfo");
	}
	
	/**
	 * 根据输入字符串模糊查询会员问题信息
	 * 
	 * 
	 * */
	public List<Map<String,Object>> getIssueInfo(Map<String,Object> map){
		return baseServiceImpl.getList(map, "BINOLCM21.getIssueInfo");
	}
	
	/**
	 * 根据输入字符串模糊查询组织详细信息
	 * 
	 * 
	 * */
	public List<Map<String,Object>> getOrganizationDetail(Map<String,Object> map){
		return baseServiceImpl.getList(map, "BINOLCM21.getOrganizationDetail");
	}
	
	/**
	 * 根据输入字符串模糊查询柜台详细信息
	 * 
	 * 
	 * */
	public List<Map<String,Object>> getCounterDetail(Map<String,Object> map){
		return baseServiceImpl.getList(map, "BINOLCM21.getCounterDetail");
	}
	
	/**
	 * 查询柜台营业员信息
	 * 
	 * @param map 查询条件
	 * @return 营业员信息List
	 */
	public List<Map<String, Object>> getBaByCounter(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM21.getBaByCounter");
		return baseServiceImpl.getList(paramMap);
	}
	
}
