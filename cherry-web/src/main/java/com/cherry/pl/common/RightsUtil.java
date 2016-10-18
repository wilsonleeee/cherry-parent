/*  
 * @(#)RightsUtil.java     1.0 2011/05/31      
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
package com.cherry.pl.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RightsUtil {
	
	/**
	 * 
	 * 把树型结构List转换成JSON对象
	 * 
	 * @param 
	 * 		List 树型结构List
	 * @return 
	 * 		String JSON对象
	 */
	@SuppressWarnings("unchecked")
	public static String getJsonTree(List<Map<String, Object>> list) {
		if(list == null || list.isEmpty()) {
			return null;
		}
		StringBuffer jsonTree = new StringBuffer();
		jsonTree.append("[");
		// 循环遍历整棵树
		for(int i = 0; i < list.size(); i++) {
			Map<String, Object> map = list.get(i);
			// 转换一个节点为JSON对象
			jsonTree.append("{\"data\":{\"title\":\""+map.get("name")+"\",\"attr\":{\"id\":\""+map.get("id")+"\"}}");
			// 取点当前节点的子节点List
			List cList = (List)map.get("children");
			// 当前节点存在子节点时
			if(cList != null && !cList.isEmpty()) {
				jsonTree.append(",\"children\":");
				// 递归调用取得当前节点子节点的JSON对象
				jsonTree.append(getJsonTree(cList));
			}
			jsonTree.append("}");
			if(i + 1 != list.size()) {
				jsonTree.append(",");
			}
		}
		jsonTree.append("]");
		return jsonTree.toString();
	}
	
	/**
	 * 
	 * 根据树的层级关系把线性的数据转换成List树型结构
	 * 
	 * @param 
	 * 		List 线性数据List
	 * @return 
	 * 		List 树型结构List
	 */
	public static List<Map<String, Object>> getTreeList(List<Map<String, Object>> list) {
		if(list == null || list.isEmpty()) {
			return null;
		}
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		// 取得第一个节点的层级
		double d = Double.parseDouble(list.get(0).get("level").toString());
		// 循环遍历整棵树
		while(!list.isEmpty()) {
			// 取得当前节点的层级
			double _d = Double.parseDouble(list.get(0).get("level").toString());
			// 当前节点的层级与第一个节点的层级不相同时
			if(d != _d) {
				// 当前节点是上一层级的子节点时
				if(d < _d) {
					// 递归调用取得上一层级最后一个节点的所有子节点，然后添加到该节点下
					resultList.get(resultList.size()-1).put("children", getTreeList(list));
				} else {// 当前节点不是上一层级的子节点时退出循环
					break;
				}
			} else { // 当前节点的层级与第一个节点的层级相同时
				// 在当前层级上添加一个节点
				addMapToList(list.get(0), resultList);
				//resultList.add(list.get(0));
				// 从树中删除该节点
				list.remove(0);
			}
		}
		return resultList;
	}
	
	public static void addMapToList(Map<String, Object> map, List<Map<String, Object>> list) {
		if(map == null || map.isEmpty()) {
			return;
		}
		if(list == null || list.isEmpty()) {
			list = new ArrayList<Map<String, Object>>();
			list.add(map);
		} else {
			boolean exitMap = false;
			for(Map<String, Object> m: list) {
				if(m.get("path") != null && map.get("path") != null && m.get("path").toString().equals(map.get("path").toString())) {
					exitMap = true;
					break;
				}
			}
			if(!exitMap) {
				list.add(map);
			}
		}
	}
	
	/**
	 * 
	 * 根据树的层级关系把线性的数据转换成JSON树型结构
	 * 
	 * @param 
	 * 		List 线性数据List
	 * @return 
	 * 		String 树型结构JSON
	 */
	public static String getTreeJson(List<Map<String, Object>> list) {
		if(list == null || list.isEmpty()) {
			return null;
		}
		StringBuffer jsonTree = new StringBuffer();
		jsonTree.append("[");
		// 取得第一个节点的层级
		double d = Double.parseDouble(list.get(0).get("level").toString());
		// 循环遍历整棵树
		while(!list.isEmpty()) {
			Map<String, Object> map = list.get(0);
			// 取得当前节点的层级
			double _d = Double.parseDouble(list.get(0).get("level").toString());
			// 当前节点的层级与第一个节点的层级不相同时
			if(d != _d) {
				// 当前节点是上一层级的子节点时
				if(d < _d) {
					jsonTree.append(",\"children\":");
					// 递归调用取得上一层级最后一个节点的所有子节点，然后添加到该节点下
					jsonTree.append(getTreeJson(list));
				} else {// 当前节点不是上一层级的子节点时退出循环
					break;
				}
			} else { // 当前节点的层级与第一个节点的层级相同时
				if(!"[".equals(jsonTree.toString())) {
					jsonTree.append("},");
				}
				// 在当前层级上添加一个节点
				jsonTree.append("{\"data\":{\"title\":\""+map.get("name")+"\",\"attr\":{\"id\":\""+map.get("id")+"\"}}");
				// 从树中删除该节点
				list.remove(0);
			}
		}
		jsonTree.append("}]");
		return jsonTree.toString();
	}

	/**
	 * 
	 * test
	 * 
	 */
	public static void main(String[] args) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		for(int i = 0; i < 4; i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", "test"+i);
			map.put("name", "test"+i);
			map.put("level", i);
			list.add(map);
		}
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", "test4");
		map.put("name", "test4");
		map.put("level", 2);
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("id", "test5");
		map.put("name", "test5");
		map.put("level", 3);
		list.add(map);
		
		
		map = new HashMap<String, Object>();
		map.put("id", "test6");
		map.put("name", "test6");
		map.put("level", 3);
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("id", "test7");
		map.put("name", "test7");
		map.put("level", 1);
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("id", "test8");
		map.put("name", "test8");
		map.put("level", 2);
		list.add(map);
		
		List<Map<String, Object>> listTemp = new ArrayList<Map<String, Object>>();
		listTemp.addAll(list);
		System.out.println(getTreeJson(listTemp));
		System.out.println(getJsonTree(getTreeList(list)));
	}

}
