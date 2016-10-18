/*  
 * @(#)BINOLSTJCS04_BL.java    1.0 2011-8-23     
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
package com.cherry.st.jcs.bl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.st.jcs.interfaces.BINOLSTJCS04_IF;
import com.cherry.st.jcs.service.BINOLSTJCS04_Service;

@SuppressWarnings("unchecked")
public class BINOLSTJCS04_BL implements BINOLSTJCS04_IF {

	@Resource
	private BINOLSTJCS04_Service binOLSTJCS04_Service;

	/**
	 * 获取查询出的仓库部门关系总数
	 * 
	 * */
	@Override
	public int getInventoryCount(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return binOLSTJCS04_Service.getInventoryCount(map);
	}

	/**
	 * 根据查询条件获取仓库部门关系list
	 * 
	 * @param map
	 * @return list
	 * 
	 * */
	@Override
	public List<Map<String, Object>> getInventoryList(Map<String, Object> map) {
		// TODO Auto-generated method stub
		List<Map<String, Object>> list = binOLSTJCS04_Service.getInventoryList(map);
		return list;
	}

	/**
	 * 取得非柜台仓库list
	 * 
	 * @param map
	 * @return list
	 * 
	 * */
	@Override
	public List<Map<String, Object>> getDepotInfoList(Map<String, Object> map) {
		// TODO Auto-generated method stub
		List<Map<String, Object>> list = binOLSTJCS04_Service.getDepotInfoList(map);
		return list;
	}

	/**
	 * 取得非柜台部门树形结构节点
	 * 
	 * @param map
	 * @return list
	 * 
	 * */
	@Override
	public List<Map<String, Object>> getJsDepartList(Map<String, Object> map) {
		// TODO Auto-generated method stub
		// 调用service获取所有的非柜台部门信息，按照path排序
		List<Map<String, Object>> list = binOLSTJCS04_Service.getDepartList(map);
		// 将获取的list转换成树形结构需要的形式
		List<Map<String, Object>> resultList = ConvertUtil.getTreeList(list, "nodes");
		// 调用递归函数，处理resultList中的数据，将不需要节点清除
		resultList = dealJsDepartList(resultList);
		return resultList;
	}

	/**
	 * 递归处理list，将不需要的key清除
	 * 
	 * @param list
	 * @return list
	 * 
	 * */
	public List<Map<String, Object>> dealJsDepartList(
			List<Map<String, Object>> list) {
		if (list.size() > 0) {
			for (int index = 0; index < list.size(); index++) {
				list.get(index).remove("path");
				list.get(index).remove("level");
				if (null != list.get(index).get("nodes")) {
					List<Map<String, Object>> childList = (List<Map<String, Object>>) list
							.get(index).get("nodes");
					dealJsDepartList(childList);
				}
			}
		}
		return list;
	}

	/**
	 * 添加仓库部门关系
	 * 
	 * @param map
	 * @param list
	 * 
	 * */
	@Override
	public void tran_setInvDepRelation(Map<String, Object> map,
			List<Map<String, Object>> list) throws Exception {
		// TODO Auto-generated method stub
		//获取要配置的仓库信息
		List<Map<String, Object>> inventoryInfoList = binOLSTJCS04_Service
				.getDepotInfoList(map);
		Map<String, Object> inventoryMap = inventoryInfoList.get(0);
		//申明一个map，用于存放要设定关系的仓库与部门信息
		Map<String, Object> map1 = new HashMap<String, Object>();
		map1.put(CherryConstants.ORGANIZATIONINFOID, map
				.get(CherryConstants.ORGANIZATIONINFOID));
		map1.put(CherryConstants.BRANDINFOID, map
				.get(CherryConstants.BRANDINFOID));
		String inventoryInfoId = ConvertUtil.getString(inventoryMap
				.get("inventoryInfoId"));
		map1.put("inventoryInfoId", inventoryInfoId);
		for (int index = 0; index < list.size(); index++) {
			Map<String, Object> mapOfList = list.get(index);
			map1.put("organizationId", mapOfList.get("organizationId"));
			//判断要设定的关系是否已经存在
			List<Map<String, Object>> list1 = binOLSTJCS04_Service
					.isDepartExist(map1);
			if (!list1.isEmpty()) {
				//如果已经存在则不做任何操作
				list.remove(index);
				index--;
			} else {
				//否则
				mapOfList.putAll(inventoryMap);
				mapOfList.putAll(map);
			}
		}
		binOLSTJCS04_Service.insertToInventoryInfo(list);
	}
	
	/**
	 * 保存编辑信息
	 * @param map
	 * 
	 * 
	 * */
	@Override
	public void tran_saveEditInfo(Map<String, Object> map) throws Exception {
		// TODO Auto-generated method stub
		Map<String, Object> map1 = new HashMap<String, Object>();
		map1.put(CherryConstants.ORGANIZATIONINFOID, map
				.get(CherryConstants.ORGANIZATIONINFOID));
		map1.put(CherryConstants.BRANDINFOID, map
				.get(CherryConstants.BRANDINFOID));
		map1.put("inventoryInfoId", map.get("inventoryInfoId"));
		map1.put("organizationId", map.get("organizationId"));
		//判断编辑后的仓库部门对应关系是否已经存在
		List<Map<String, Object>> list1 = binOLSTJCS04_Service
				.isDepartExist(map1);
		if (list1.size() > 0) {
			String identityId1 = ConvertUtil.getString(list1.get(0).get(
					"identityId"));
			String identityId2 = ConvertUtil.getString(map.get("identityId"));
			//比较新的对应关系和之前的关系是否是同一条记录
			if (!identityId1.equals(identityId2)) {
				//如果不是同一条记录则将原先的记录删除
				binOLSTJCS04_Service.deleteInvDepRelation(list1);
			}
		}
		List<Map<String, Object>> inventoryInfoList = binOLSTJCS04_Service
				.getDepotInfoList(map);
		Map<String, Object> inventoryMap = inventoryInfoList.get(0);
		map.putAll(inventoryMap);
		binOLSTJCS04_Service.updateInventoryInfo(map);
	}

	/**
	 * 支持批量删除
	 * 
	 * 
	 * */
	@Override
	public void tran_deleteInvDepRelation(List<Map<String, Object>> list)
			throws Exception {
		// TODO Auto-generated method stub
		binOLSTJCS04_Service.deleteInvDepRelation(list);
	}

	@Override
	public List<Map<String, Object>> getDepartList(Map<String, Object> map) {
		// TODO Auto-generated method stub
		List<Map<String, Object>> list = binOLSTJCS04_Service.getDepartList(map);
		return list;
	}
}
