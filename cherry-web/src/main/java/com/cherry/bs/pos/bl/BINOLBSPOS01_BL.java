/*
 * @(#)BINOLBSPOS01_BL.java     1.0 2010/10/27
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

package com.cherry.bs.pos.bl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.bs.pos.service.BINOLBSPOS01_Service;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.ConvertUtil;

/**
 * 岗位一览画面BL
 * 
 * @author WangCT
 * @version 1.0 2010.10.27
 */
public class BINOLBSPOS01_BL {
	
	/** 岗位一览画面Service */
	@Resource
	private BINOLBSPOS01_Service binOLBSPOS01_Service;
	
	/**
	 * 取得某一岗位的直属下级岗位
	 * 
	 * @param map 检索条件
	 * @return 树结构字符串
	 */
	@SuppressWarnings("unchecked")
	public String getNextPositionList(Map<String, Object> map) {
		// 岗位节点位置
		String path = (String)map.get(CherryConstants.PATH);
		List<Map<String, Object>> list =  new ArrayList<Map<String, Object>>();
		if(path == null || "".equals(path)) {
			// 取得某一用户能访问的顶层岗位级别
			Integer level = binOLBSPOS01_Service.getFirstPositionLevel(map);
			if(level != null) {
				// 顶层岗位级别
				map.put(CherryConstants.LEVEL, level);
				// 取得某一用户能访问的顶层岗位List
				list = binOLBSPOS01_Service.getFirstPositionList(map);
			}
		} else {
			String[] paths = path.split("_");
			// 上级岗位为柜台主管的场合
			if(paths.length > 1) {
				List<Map<String, Object>> counterList =  new ArrayList<Map<String, Object>>();
				StringBuffer jsonTree = new StringBuffer(); 
				
				// 上级岗位为空缺的场合
				if("0".equals(paths[paths.length-1])) {
					map.put("positionId", paths[0]);
					counterList =  binOLBSPOS01_Service.getCounterList(map);
				} else {
					map.put("employeeId", paths[0]);
					map.put("path", paths[1]);
					counterList =  binOLBSPOS01_Service.getCounterList(map);
				}
				if(counterList != null && !counterList.isEmpty()) {
					jsonTree.append("[");
					for(int i = 0; i < counterList.size(); i++) {
						String title = (String)counterList.get(i).get("counterNameIF");
						// 树结构作成
						jsonTree.append("{\"data\":{\"title\":\""+title+"\",\"attr\":{\"id\":\""+counterList.get(i).get("counterInfoId")+"_2\"}},\"attr\":{\"id\":\"\"},\"state\":\"\"},");
					}
					jsonTree = jsonTree.deleteCharAt(jsonTree.length()-1);
					jsonTree.append("]");
				}
				return jsonTree.toString();
			}
			// 取得某一岗位的直属下级岗位
			list = binOLBSPOS01_Service.getNextPositionList(map);
		}
		StringBuffer jsonTree = new StringBuffer();
		// 把取得的岗位List转换成树结构的字符串
		if(list != null && !list.isEmpty()) {
			List<Map<String, Object>> newList = new ArrayList<Map<String,Object>>();
			List<String[]> keyList = new ArrayList<String[]>();
			String[] key1 = {"positionId","child","path","departName","categoryCode","categoryName"};
			keyList.add(key1);
			ConvertUtil.convertList2DeepList(list,newList,keyList,0);
			jsonTree.append("[");
			Map<Object, Object> _map = new HashMap<Object, Object>();
			for(int i = 0; i < newList.size(); i++) {
				Map<String, Object> organizationMap = newList.get(i);
				// 是否有子节点
				String child = "";
				String employeeName = "空缺";
				if("02".equals(organizationMap.get("categoryCode"))) {
					child = "closed";
					List<Map<String, Object>> employeeList = (List)organizationMap.get("list");
					if(employeeList != null && !employeeList.isEmpty() && employeeList.get(0).get("employeeId") != null) {
						for(int j = 0; j < employeeList.size(); j++) {
							Map<String, Object> employeeMap = employeeList.get(j);
							if(_map.get(employeeMap.get("employeeId")) == null) {
								_map.put(employeeMap.get("employeeId"), employeeMap.get("organizationId"));
								employeeName = (String)employeeMap.get("employeeName");
								String title = employeeName+"("+organizationMap.get("categoryName")+"/"+organizationMap.get("departName")+")";
								// 树结构作成
								jsonTree.append("{\"data\":{\"title\":\""+title+"\",\"attr\":{\"id\":\""+employeeMap.get("employeeId")+"_1\"}},\"attr\":{\"id\":\""+employeeMap.get("employeeId")+"_"+path+"_1\"},\"state\":\""+child+"\"},");
							}
						}
					} else {
						String title = employeeName+"("+organizationMap.get("categoryName")+"/"+organizationMap.get("departName")+")";
						// 树结构作成
						jsonTree.append("{\"data\":{\"title\":\""+title+"\",\"attr\":{\"id\":\""+organizationMap.get("positionId")+"\"}},\"attr\":{\"id\":\""+organizationMap.get("positionId")+"_0\"},\"state\":\""+child+"\"},");
					}
				} else {
					// 有子节点的场合
					if(organizationMap.get("child") != null && !"".equals(organizationMap.get("child"))) {
						child = Integer.parseInt(organizationMap.get("child").toString()) > 1 ? "closed" : "";
					}
					List<Map<String, Object>> employeeList = (List)organizationMap.get("list");
					if(employeeList != null && !employeeList.isEmpty()) {
						if(employeeList.get(0).get("employeeId") != null && !"".equals(employeeList.get(0).get("employeeId"))) {
							employeeName = (String)employeeList.get(0).get("employeeName");
						}
					}
					String title = employeeName+"("+organizationMap.get("categoryName")+"/"+organizationMap.get("departName")+")";
					// 树结构作成
					jsonTree.append("{\"data\":{\"title\":\""+title+"\",\"attr\":{\"id\":\""+organizationMap.get("positionId")+"\"}},\"attr\":{\"id\":\""+organizationMap.get("path")+"\"},\"state\":\""+child+"\"},");
				}
			}
			jsonTree = jsonTree.deleteCharAt(jsonTree.length()-1);
			jsonTree.append("]");
		}
		
		return jsonTree.toString();
	}
	
	/**
	 * 取得岗位总数
	 * 
	 * @param map 检索条件
	 * @return 返回岗位总数
	 */
	public int getPositionCount(Map<String, Object> map) {
		
		// 取得岗位总数
		return binOLBSPOS01_Service.getPositionCount(map);
	}
	
	/**
	 * 取得岗位信息List
	 * 
	 * @param map 检索条件
	 * @return 岗位信息List
	 */
	public List<Map<String, Object>> getPositionList(Map<String, Object> map) {
		
		// 取得岗位信息List
		return binOLBSPOS01_Service.getPositionList(map);
	}
	
	/**
	 * 取得上级岗位信息List
	 * 
	 * @param map 检索条件
	 * @return 上级岗位信息List
	 */
	public List<Map<String, Object>> getHigherPositionList(Map<String, Object> map) {
		
		// 取得上级岗位信息List
		return binOLBSPOS01_Service.getHigherPositionList(map);
	}

}
