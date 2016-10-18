/*	
 * @(#)BINOLCPACT12_BL.java     1.0 @2014-12-16		
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
package com.cherry.cp.act.bl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.util.ConvertUtil;
import com.cherry.cp.act.interfaces.BINOLCPACT12_IF;
import com.cherry.cp.act.service.BINOLCPACT12_Service;

/**
 * 活动产品库存一览BL
 * 
 * @author menghao
 * 
 */
public class BINOLCPACT12_BL implements BINOLCPACT12_IF {

	@Resource(name="binOLCPACT12_Service")
	private BINOLCPACT12_Service binOLCPACT12_Service;
	/**
	 * 活动产品库存Count
	 */
	@Override
	public int getCampaignStockCount(Map<String, Object> map) {
		return binOLCPACT12_Service.getCampaignStockCount(map);
	}
	/**
	 * 活动产品库存List
	 * @throws Exception 
	 */
	@Override
	public List<Map<String, Object>> getCampaignStockList(Map<String, Object> map) throws Exception {
		return binOLCPACT12_Service.getCampaignStockList(map);
	}
	
	@Override
	public List<Map<String, Object>> getCampaignStockDetailMain(
			Map<String, Object> map) throws Exception {
		return binOLCPACT12_Service.getCampaignStockDetail(map);
	}
	@Override
	public List<Map<String, Object>> getCampaignStockProductDetail(
			Map<String, Object> map) throws Exception {
		// TODO Auto-generated method stub
		return binOLCPACT12_Service.getCampaignStockProductDetail(map);
	}
	
	/**
	 * 取得主题活动名称
	 * @param map
	 * @return
	 */
	@Override
	public String getCampName(Map<String, Object> map) {
		List<Map<String,Object>> resultList = binOLCPACT12_Service.getCampName(map);
		return this.getString(map, resultList);
	}
	/**
	 * 取得活动名称
	 * @param map
	 * @return
	 */
	@Override
	public String getSubCampName(Map<String, Object> map) {
		List<Map<String,Object>> resultList = binOLCPACT12_Service.getSubCampName(map);
		return this.getString(map, resultList);
	}
	
	/**
	 * 根据输入字符串模糊查询会员活动名称信息
	 * @param map
	 * @param list
	 * @return
	 */
	private String getString(Map<String,Object> map,List<Map<String,Object>> list){
		StringBuffer sb = new StringBuffer();
		for(int i = 0 ; i < list.size() ; i++){
			Map<String,Object> tempMap = list.get(i);
			sb.append((String)tempMap.get("code"));
			sb.append("|");
			sb.append((String)tempMap.get("name"));
			sb.append("|");
			sb.append(String.valueOf(tempMap.get("id")));
			if(i != list.size()){
				sb.append("\n");
			}
		}
		return sb.toString();
	}
	
	@Override
	public void tran_save(Map<String, Object> map, List<String[]> list)
			throws Exception {
		//从list中获取各种参数数组
		 /**产品厂商ID*/
        String[] productVendorIdArr = list.get(0);
        /**分配数量*/
    	String[] totalQuantityArr = list.get(1);
    	/**安全数量*/
    	String[] safeQuantityArr = list.get(2);
    	
    	Map<String, Object> paramMap = new HashMap<String, Object>();
    	paramMap.putAll(map);
    	List<Map<String,Object>> detailList = new ArrayList<Map<String,Object>>();
    	
    	String productVendorId = "";
    	/**分配数量*/
    	int totalQuantity = 0;
    	/**安全数量*/
    	int safeQuantity = 0;
    	for(int i=0;i<productVendorIdArr.length;i++) {
    		productVendorId = ConvertUtil.getString(productVendorIdArr[i]);
    		totalQuantity = ConvertUtil.getInt(totalQuantityArr[i]);
    		safeQuantity = ConvertUtil.getInt(safeQuantityArr[i]);
    		Map<String, Object> detailMap = new HashMap<String, Object>();
    		detailMap.put("productVendorId", productVendorId);
    		detailMap.put("totalQuantity", totalQuantity);
    		detailMap.put("safeQuantity", safeQuantity);
    		detailMap.putAll(paramMap);
    		
    		detailList.add(detailMap);
    	}
    	binOLCPACT12_Service.updateCampaignStock(detailList);
    	
	}

}
