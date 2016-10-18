/*
 * @(#)PrtUtil.java v1.0 2014-6-24
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
package com.cherry.pt.common;

import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cp.common.CampConstants;

/**
 * 产品共通
 * 
 * @author JiJW
 * @version 1.0 2014-6-24
 */
public class PrtUtil {
	/**
	 * 设置节点选中状态
	 * 
	 * @param nodes 节点数组
	 * @param checkedNode 选中节点
	 */
	@SuppressWarnings("unchecked")
	public static void setNodes(List<Map<String, Object>> nodes, Map<String, Object> checkedNode) {
		String checkedId = ConvertUtil.getString(checkedNode.get(CherryConstants.PATH));
		boolean half = (Boolean)checkedNode.get(CampConstants.HALF);
		if (null != nodes && nodes.size() > 0) {
			for (Map<String, Object> node : nodes) {
				String nodeId = ConvertUtil.getString(node.get(CherryConstants.PATH));
				// 当前节点为选中的节点
				if (nodeId.equals(checkedId)) {
					// 设置节点及子节点选中状态
					setChildrenNodes(node, half);
				} else {
					// 当前节点不等选中的节点
					// 取当前节点的所有子节点
					List<Map<String, Object>> nodeList = (List<Map<String, Object>>) node
							.get(CampConstants.NODES);
					setNodes(nodeList, checkedNode);
				}
			}
		}
	}

	/**
	 * 设置节点以及子节点状态
	 * 
	 * @param node
	 * @param half
	 */
	@SuppressWarnings("unchecked")
	public static void setChildrenNodes(Map<String, Object> node, boolean half) {
		if (null != node) {
			node.put(CampConstants.CHECKED, true);
			node.put("halfCheck", half);
			// 全选时，设置子节点状态
			if(!half){
				List<Map<String, Object>> nodes = (List<Map<String, Object>>) node
						.get(CampConstants.NODES);
				if (null != nodes) {
					for (Map<String, Object> n : nodes) {
						setChildrenNodes(n, false);
					}
				}
			}
		}
	}
}
