/*
 * @(#)BINOLSTJCS01_BL.java     1.0 2011/08/25
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
/**
 * 
 * 逻辑仓库与业务关联
 * @author LuoHong
 * @version 1.0 2011.08.25
 * 
 **/
package com.cherry.st.jcs.service;

import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;
@SuppressWarnings("unchecked")
public class BINOLSTJCS09_Service extends BaseService{
	/**
	 * 取得仓库逻辑关系List
	 * 
	 * @param map
	 * 
	 * @return
	 */
       public List<Map<String, Object>> getLogicDepotList(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTJCS09.getLogicDepotList");
		return baseServiceImpl.getList(map);
	}
    /**
   	 * 取得仓库逻辑关系总数
   	 * 
   	 * @param map
   	 * 
   	 * @return
   	 */
       public int getLogicDepotCount(Map<String, Object> map) {
   		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTJCS09.getLogicDepotCount");
   		return baseServiceImpl.getSum(map);
   	}
       /**
        * 删除逻辑仓库配置关系
        * 
        * */
       public int deleteLogicDepot(Map<String, Object> map) {
//   		baseServiceImpl.deleteAll(list, "BINOLSTJCS09.deleteLogicDepot");
   		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTJCS09.deleteLogicDepot");
   		return baseServiceImpl.remove(map);
   	}
       /**
        * 新增逻辑仓库配置关系
        * 
        * @param map
        * @return 
        */
       public int addLogicDepot(Map<String, Object> map) {
    	   map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTJCS09.addLogicDepot");
           return baseServiceImpl.saveBackId(map);
       }
	
		public Map getLogicDepots(Map<String, Object> map) {
			map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTJCS09.getLogicDepot");
	 
			return(Map) baseServiceImpl.get(map);
		}
		
		public void updateLogicDepotInfo(Map<String, Object> map) {
			baseServiceImpl.update(map, "BINOLSTJCS09.updateLogicDepotInfo");
		}
		
		/**
		 * 检验指定的逻辑仓库配置信息是否已经存在
		 * 
		 * */
		public List<Map<String,Object>> isExist(Map<String,Object> map){
			return baseServiceImpl.getList(map, "BINOLSTJCS09.isExist");
		}
		
		/**
		 * 检验某种类型的逻辑仓库配置信息是否已经存在
		 * 
		 * */
		public List<Map<String,Object>> isExistSomeLogiDepotBusiness(Map<String,Object> map){
			return baseServiceImpl.getList(map, "BINOLSTJCS09.isExistSomeLogiDepotBusiness");
		}
		
		/**
		 * 取得供发送MQ消息的配置数据
		 * 
		 * */
		public List<Map<String,Object>> getConfigForSend(Map<String,Object> map){
			return baseServiceImpl.getList(map, "BINOLSTJCS09.getConfigForSend");
		}
		
		/**
		 * 从【BIN_LogicInventory逻辑仓库表】中取得符合参数条件的逻辑仓库信息
		 * 
		 * */
		public List<Map<String,Object>> getLogicDepotByPraMap(Map<String,Object> map){
			return baseServiceImpl.getList(map, "BINOLSTJCS09.getLogicDepotByPraMap");
		}
		
		/**
		 * 取得逻辑仓库业务配置List(WS结构组装使用)
		 * 
		 * @param map
		 * 
		 * @return
		 */
		public List<Map<String, Object>> getLogInvBusByBrandWithWS(Map<String, Object> map) {
			map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTJCS09.getLogInvBusByBrandWithWS");
			return baseServiceImpl.getList(map);
		}
		
	

}
