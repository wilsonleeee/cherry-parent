/*
 * @(#)BINOLBSREG01_BL.java     1.0 2011/11/23
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
package com.cherry.bs.reg.bl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.bs.reg.interfaces.BINOLBSREG01_IF;
import com.cherry.bs.reg.service.BINOLBSREG01_Service;
import com.googlecode.jsonplugin.JSONUtil;

/**
 * 区域一览画面BL
 * 
 * @author WangCT
 * @version 1.0 2011/11/23
 */
public class BINOLBSREG01_BL implements BINOLBSREG01_IF {
	
	/** 区域一览画面Service */
	@Resource
	private BINOLBSREG01_Service binOLBSREG01_Service;

	/**
     * 取得区域树结构信息
     * 
     * @param map 查询条件
     * @return 区域树结构信息
     */
	@Override
	public String getRegionTree(Map<String, Object> map) throws Exception {
		
		// 区域ID
		String regionId = (String)map.get("regionId");
		List<Map<String, Object>> regionList =  new ArrayList<Map<String, Object>>();
		// 区域ID不存在的场合，取得顶层区域List，否则的话取得下级区域List
		if(regionId == null || "".equals(regionId)) {
			// 取得顶层区域List
			regionList = binOLBSREG01_Service.getFirstRegionList(map);
		} else {
			// 取得下级区域List
			regionList = binOLBSREG01_Service.getNextRegionList(map);
		}
		StringBuffer jsonTree = new StringBuffer();
		// 把取得的区域List转换成树结构的字符串
		if(regionList != null && !regionList.isEmpty()) {
			jsonTree.append("[");
			for(int i = 0; i < regionList.size(); i++) {
				Map<String, Object> regionMap = regionList.get(i);
				// 区域名称
				String regionName = (String)regionMap.get("regionName");
				// 区域名称为空的场合，不显示该区域
				if(regionName == null || "".equals(regionName)) {
					continue;
				}
				// 区域code
				String regionCode = (String)regionMap.get("regionCode");
				// 区域code不为空的场合，同时显示区域code
				if(regionCode != null && !"".equals(regionCode)) {
					regionName = "(" + regionCode + ")" + regionName;
				}
				regionName = JSONUtil.serialize(regionName);
				String id = String.valueOf(regionMap.get("regionId"));
				int childCount = (Integer)regionMap.get("childCount");
				// 是否有子节点
				String child = "";
				// 有子节点的场合
				if(childCount > 0) {
					child = "closed";
				}
				String validFlag = (String)regionMap.get("validFlag");
				// 是否为无效部门
				String className = "";
				// 无效部门的场合
				if("0".equals(validFlag)) {
					className = "jstree-disable";
				}
				// 树结构作成
				jsonTree.append("{\"data\":{\"title\":"+regionName+",\"attr\":{\"id\":\""+id+"\", \"class\":\""+className+"\", \"title\":\""+id+"\"}},\"attr\":{\"id\":\""+"node_"+id+"\"},\"state\":\""+child+"\"},");
			}
			jsonTree = jsonTree.deleteCharAt(jsonTree.length()-1);
			jsonTree.append("]");
		}
		return jsonTree.toString();
	}
	
	/**
     * 取得定位到的区域的所有上级区域位置
     * 
     * @param map 查询条件
     * @return 上级区域位置
     */
	@Override
	public List<String> getLocationHigher(Map<String, Object> map) {
		
		List<String> resultList = new ArrayList<String>();	
		// 查询定位到的区域的所有上级区域位置
		List<String> locationHigher = binOLBSREG01_Service.getLocationHigher(map);
		if(locationHigher != null) {
			for(int i = 0; i < locationHigher.size(); i++) {
				resultList.add("#node_"+locationHigher.get(i));
			}
			return resultList;
		}
		return resultList;
	}
	
	/**
     * 取得定位到的区域ID
     * 
     * @param map 查询条件
     * @return 定位到的区域ID
     */
	@Override
	public String getLocationRegionId(Map<String, Object> map) {
		
		// 查询定位到的区域ID
		return binOLBSREG01_Service.getLocationRegionId(map);
	}

}
