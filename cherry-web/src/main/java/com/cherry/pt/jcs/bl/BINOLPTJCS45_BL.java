/*  
 * @(#)BINOLPTJCS45_BL.java     1.0 2014/08/31      
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
package com.cherry.pt.jcs.bl;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;

import com.cherry.pt.common.ProductConstants;
import com.cherry.pt.jcs.interfaces.BINOLPTJCS45_IF;
import com.cherry.pt.jcs.service.BINOLPTJCS17_Service;
import com.cherry.pt.jcs.service.BINOLPTJCS45_Service;
import com.cherry.cm.cmbussiness.bl.BINOLCM15_BL;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.ss.common.base.SsBaseBussinessLogic;
import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

/**
 * 
 * 渠道一览
 * @author weisc
 *
 */
@SuppressWarnings("unchecked")
public class BINOLPTJCS45_BL extends SsBaseBussinessLogic implements BINOLPTJCS45_IF{
		@Resource
		private BINOLPTJCS45_Service binOLPTJCS45_Service;
		
		/** 取得系统各类编号 */
		@Resource(name="binOLCM15_BL")
		private BINOLCM15_BL binOLCM15_BL;
		
		@Resource(name="binOLPTJCS17_Service")
		private BINOLPTJCS17_Service binOLPTJCS17_Service;
		
		/**
		 * 取得方案总数
		 * 
		 * @param map
		 * @return
		 */
		@Override
		public int searchSolutionCount(Map<String, Object> map) {
			return binOLPTJCS45_Service.getSolutionCount(map);
		}
		/**
		 * 取得方案List
		 * 
		 * @param map
		 * @return
		 */
		@Override
		public List searchSolutionList(Map<String, Object> map) {
			return binOLPTJCS45_Service.getSolutionList(map);
		}
	    /**
	     * 方案停用/启用
	     * 
	     * @param map
	     * @return
	     * @throws CherryException 
	     * @throws JSONException,Exception 
	     */
		@Override
	    public int tran_disOrEnableSolu(Map<String, Object> map) throws CherryException, JSONException,Exception{
			
			// 停用/启用方案地点关系表
			Map<String, Object> seqMap = new HashMap<String, Object>();
			seqMap.putAll(map);
			seqMap.put("type", "F");
			String tVersion = binOLCM15_BL.getCurrentSequenceId(seqMap);
			map.put("tVersion", tVersion);
			
			String [] solutionIdArr = (String[]) map.get("solutionIdArr");
			
			String psdValidFlag = (String)map.get("validFlag");
			
			if(solutionIdArr.length > 0){
				for(String productPriceSolutionID : solutionIdArr){
					map.put("productPriceSolutionID", productPriceSolutionID);
					
					
					// Step1：产品方案与原分配的柜台（区域、渠道、柜台）的产品方案部门关系表记录无效掉validFlag= 0,version=tVeresion+1
					Map<String, Object> oldDpcdMap = binOLPTJCS17_Service.getDPConfigDetailBySolu(map);
					
					if(null != oldDpcdMap && !oldDpcdMap.isEmpty()){
						oldDpcdMap.putAll(map);
						
						List<Map<String, Object>> cntList = binOLPTJCS17_Service.getPrtSoluWithDepartHis(map);
						if(null != cntList && !cntList.isEmpty()){
							for(Map<String,Object> cntMap : cntList){
								oldDpcdMap.put("CounterCode", cntMap.get("CounterCode"));
								oldDpcdMap.put("psdValidFlag", psdValidFlag);
								binOLPTJCS17_Service.updPrtSoluDepartRelation(oldDpcdMap);
							}
						}
						
						/*
						// 地点类型
						String placeType = (String)oldDpcdMap.get("PlaceType");
						String saveJson = (String)oldDpcdMap.get("SaveJson");
						List<String> saveJsonList = (List<String>) JSONUtil.deserialize(saveJson);
						
						// 区域城市
						if(placeType.equals(ProductConstants.LOTION_TYPE_REGION)){
							// 取得权限区域城市的柜台
							oldDpcdMap.put("city", 1);
							oldDpcdMap.put("cityList", saveJsonList);
							List<Map<String, Object>> cntList = binOLPTJCS17_Service.getCounterInfoList(oldDpcdMap);
							if(null != cntList && !cntList.isEmpty()){
								for(Map<String,Object> cntMap : cntList){
									oldDpcdMap.putAll(cntMap);
									// Step1.1：将方案原关联的柜台对应的关系表记录无效掉
									oldDpcdMap.put("psdValidFlag", psdValidFlag);
									binOLPTJCS17_Service.updPrtSoluDepartRelation(oldDpcdMap);
								}
							}
						}
						// 按区域并且指定柜台 或 按渠道并且指定柜台
						else if (placeType.equals(ProductConstants.LOTION_TYPE_CHANNELS_COUNTER)
								|| placeType.equals(ProductConstants.LOTION_TYPE_REGION_COUNTER) 
								|| placeType.equals(ProductConstants.LOTION_TYPE_IMPORT_COUNTER)) {
							
							for(String cntCode : saveJsonList){
								oldDpcdMap.put("CounterCode", cntCode);
								// Step1.1：将方案原关联的柜台对应的柜台产品记录无效掉
								oldDpcdMap.put("psdValidFlag", psdValidFlag);
								binOLPTJCS17_Service.updPrtSoluDepartRelation(oldDpcdMap);
							}
						}
						
						// 渠道
						else if(placeType.equals(ProductConstants.LOTION_TYPE_CHANNELS)){
							// 取得权限渠道的柜台
							oldDpcdMap.put("channel", 1);
							oldDpcdMap.put("channelList", saveJsonList);
							List<Map<String, Object>> channelList = binOLPTJCS17_Service.getCounterInfoList(oldDpcdMap);
							if(null != channelList && !channelList.isEmpty()){
								for(Map<String,Object> channelMap : channelList){
									oldDpcdMap.putAll(channelMap);
									// Step1.1：将方案原关联的柜台对应的柜台产品记录无效掉
									oldDpcdMap.put("psdValidFlag", psdValidFlag);
									binOLPTJCS17_Service.updPrtSoluDepartRelation(oldDpcdMap);
								}
							}
						}
						
						*/
					}
				}
			}
			
	        return binOLPTJCS45_Service.disOrEnableSolu(map);
	    }
	    
}