
/*  
 * @(#)BINOLSTJCS05_Service.java    1.0 2011-9-2     
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
package com.cherry.st.jcs.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

@SuppressWarnings("unchecked")
public class BINOLSTJCS05_Service extends BaseService {

	/**
	 * 取得仓库业务关系list
	 * 
	 * */
	public List<Map<String,Object>> getDepotBusinessList(Map<String,Object> map){
		return baseServiceImpl.getList(map, "BINOLSTJCS05.getDepotBusinessList");
	}
	
	/**
	 * 取得仓库业务总数
	 * 
	 * */
	public int getDepotBusinessCount(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTJCS05.getDepotBusinessCount");
		return baseServiceImpl.getSum(map);
	}
	
	/**
	 * 获取所有柜台仓库及其所属区域数据
	 * 
	 * 
	 * */
	public List<Map<String,Object>> getCounterDeportAndRegion(Map<String,Object> map){
		return baseServiceImpl.getList(map, "BINOLSTJCS05.getCounterDeportAndRegion"); 
	}
	
	/**
	 * 获取所有仓库所属区域信息
	 * 
	 * */
	public List<Map<String,Object>> getRegion(Map<String,Object> map){
		return baseServiceImpl.getList(map, "BINOLSTJCS05.getRegion"); 
	}
	
	/**
	 * 获取非柜台实体仓库
	 * 
	 * */
	public List<Map<String,Object>> getDepotInfoList(Map<String,Object> map){
		return baseServiceImpl.getList(map, "BINOLSTJCS05.getDepotInfoList");
	}
	
    /**
     * 获取所有部门
     * 
     * */
    public List<Map<String,Object>> getDepartInfoList(Map<String,Object> map){
        return baseServiceImpl.getList(map, "BINOLSTJCS05.getDepartInfoList");
    }
	
	/**
	 * 保存添加
	 * 
	 * 
	 * */
	public void insertToDepotBusiness(List<Map<String,Object>> list)
	{
		baseServiceImpl.saveAll(list, "BINOLSTJCS05.insertToDepotBusiness");
	}
	
	/**
	 * 判断仓库业务对应关系是否已经存在
	 * 
	 * 
	 * */
	public Map<String,Object> isExist(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTJCS05.isExist");
		return (Map<String, Object>) baseServiceImpl.get(map);
	}
	
	/**
	 * 物理删除仓库业务对应关系
	 * 
	 * 
	 * */
	public void deleteDepotBusiness(List<Map<String,Object>> list){
		baseServiceImpl.deleteAll(list, "BINOLSTJCS05.deleteDepotBusiness");
	}
	
	/**
	 * 根据入库ID删除相应的退货业务，逆向业务中的入库ID对应的都是仓库ID
	 * 
	 * */
	public void deleteReturnByInId(Map<String,Object> map){
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		list.add(map);
		baseServiceImpl.deleteAll(list, "BINOLSTJCS05.deleteReturnByInId");
	}
	
	/**
	 * 根据出库ID以及业务类型删除相应的记录
	 * 
	 * */
	public void deleteDeliverByOuId(Map<String,Object> map){
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		list.add(map);
		baseServiceImpl.deleteAll(list, "BINOLSTJCS05.deleteDeliverByOuId");
	}
	
	/**
	 * 根据入库ID以及业务类型删除相应的记录
	 * 
	 * */
	public void deleteDeliverByInId(Map<String,Object> map){
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		list.add(map);
		baseServiceImpl.deleteAll(list, "BINOLSTJCS05.deleteDeliverByInId");
	}
	
	/**
	 * 根据入库ID查询出所有的退货业务对应的出库ID及其出库ID区分标志，退货业务中的入库ID对应的都是仓库ID
	 * 
	 * */
	public List<Map<String,Object>> getReturnOutIdByInId(Map<String,Object> map){
		return baseServiceImpl.getList(map, "BINOLSTJCS05.getReturnOutIdByInId");
	}
	
	/**
	 * 根据出库ID查询出所有的发货业务对应的入库ID及其入库ID区分标志，发货和订货业务中的出库ID对应的都是仓库ID
	 * 
	 * */
	public List<Map<String,Object>> getDeliverInIdByOutId(Map<String,Object> map){
		return baseServiceImpl.getList(map, "BINOLSTJCS05.getDeliverInIdByOutId");
	}
	
	/**
	 * 根据入库ID查询出所有的退库业务对应的出库ID及其出库ID区分标志，退库业务中的入库ID对应的都是仓库ID
	 * 
	 * */
	public List<Map<String,Object>> getOutIdByInId(Map<String,Object> map){
		return baseServiceImpl.getList(map, "BINOLSTJCS05.getOutIdByInId");
	}
	
	/**
	 * 根据仓库业务关系ID查询出出库ID（编辑订货和发货业务时使用，出库方的ID对应的永远是非柜台的实体仓库ID）
	 * 
	 * 
	 * */
	public Map<String,Object>getOutIdById(Map<String,Object> map){
		return (Map<String, Object>) baseServiceImpl.get(map, "BINOLSTJCS05.getOutIdById");
	}
	
	/**
	 * 根据仓库业务关系ID查询出入库ID（编辑退库业务时使用，入库方的ID对应的永远是非柜台的实体仓库ID）
	 * 
	 * 
	 * */
	public Map<String,Object>getInIdById(Map<String,Object> map){
		return (Map<String, Object>) baseServiceImpl.get(map, "BINOLSTJCS05.getInIdById");
	}
	
	/**
	 * 取得仓库业务关系list,按组织层级关系
	 * 
	 * */
	public List<Map<String,Object>> getDepotBusListByOrgRel(Map<String,Object> map){
		return baseServiceImpl.getList(map, "BINOLSTJCS05.getDepotBusListByOrgRel");
	}
	
	/**
	 * 取得仓库业务关系总数,按组织层级关系
	 * 
	 * */
	public int getDepotBusCountByOrgRel(Map<String,Object> map){
		return (Integer)baseServiceImpl.get(map, "BINOLSTJCS05.getDepotBusCountByOrgRel");
	}
}
