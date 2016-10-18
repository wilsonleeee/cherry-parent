/*		
 * @(#)BINOLCM12_BL.java     1.0 2011/03/22		
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
package com.cherry.cm.cmbussiness.bl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbussiness.service.BINOLCM12_Service;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;

/**
 * 扩展属性共通BL
 * 
 * @author lipc
 * 
 */
public class BINOLCM12_BL {

	@Resource(name="binOLCM12_Service")
	private BINOLCM12_Service binOLCM12_Service;

	/**
	 * 取得扩展属性List
	 * 
	 * @param paramMap
	 * @return
	 */
	public List<Map<String, Object>> getExtPropertyList(
			Map<String, Object> paramMap) {
		// productId为空:添加页面,反之:编辑画面
		Object productId = paramMap.get("productId");
		List<Map<String, Object>> resultList = new ArrayList<Map<String,Object>>();
		// 属性分组ID记录
		int id = -1;
		List<Map<String, Object>> groupList = null;
		Map<String, Object> groupMap = null;
		// 取得扩展属性List
		List<Map<String, Object>> list = binOLCM12_Service
				.getExtPropertyList(paramMap);
		if (null != list) {
			for (Map<String, Object> map: list) {
				Map<String, Object> tempMap = new HashMap<String, Object>();
				// 属性分组ID
				Object groupId = map.get("groupId");
				if (null == groupId) {
					tempMap
							.put("extendPropertyId", map
									.get("extendPropertyId"));
					// 取得扩展属性选项值List
					List<Map<String, Object>> list1 = binOLCM12_Service
							.getItemList(tempMap);
					if (null != list1 && list1.size() > 0) {
						// 选项值list放入结果集
						map.put("itemList", list1);
						if(null != productId){
							map.put("id",productId);
						}
					}
					resultList.add(map);
				} else {
					if (id != (Integer)groupId) {
						groupMap = new HashMap<String, Object>();
						groupList = new ArrayList<Map<String, Object>>();
						groupMap.put("groupList", groupList);
						groupMap.put("groupName", map.get("groupName"));
						id = (Integer)groupId;
						resultList.add(groupMap);
					}
					groupList.add(map);
				}
			}
		}
		return resultList;
	}

	/**
	 * 取得产品扩展属性及属性值List
	 * 
	 * @param paramMap
	 * @return
	 */
	public List<Map<String, Object>> getExtValList(Map<String, Object> paramMap) {
		List<Map<String, Object>> resultList = new ArrayList<Map<String,Object>>();
		// 分组ID
		int id = -1;
		List<Map<String, Object>> groupList = null;
		Map<String, Object> groupMap = null;
		List<Map<String, Object>> prtExtList = binOLCM12_Service.getExtValList(paramMap);
		if(null != prtExtList){
			for(Map<String, Object> map : prtExtList){
				int groupId = CherryUtil.obj2int(map.get("groupId"));
				// 属性分组
				if(groupId > 0){
					if(id != groupId){
						groupMap = new HashMap<String, Object>();
						groupList = new ArrayList<Map<String, Object>>();
						groupMap.put("groupList", groupList);
						groupMap.put("groupName", map.get("groupName"));
						id = groupId;
						resultList.add(groupMap);
					}
					if("" != ConvertUtil.getString(map.get("propertyValue"))){
						groupList.add(map);
					}
				}else{
					resultList.add(map);
				}
			}
		}
		return resultList;
	}

	/**
	 * 插入产品扩展属性值表
	 * 
	 * @param paramMap
	 * @return
	 */
	public void tran_insertPrtExtValue(Map<String, Object> paramMap) throws Exception{

		binOLCM12_Service.insertPrtExtValue(paramMap);
	}
	
	/**
	 * 查询扩展属性个数
	 * @param paramMap
	 * @return
	 */
	public int getExtProCount(Map<String, Object> paramMap){
		return binOLCM12_Service.getExtProCount(paramMap);
	}
}
