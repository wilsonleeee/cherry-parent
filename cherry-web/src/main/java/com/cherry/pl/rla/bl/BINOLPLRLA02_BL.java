/*
 * @(#)BINOLPLRLA02_BL.java     1.0 2010/11/01
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
package com.cherry.pl.rla.bl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.util.ConvertUtil;
import com.cherry.pl.rla.service.BINOLPLRLA02_Service;

/**
 * 岗位类别角色分配BL
 * 
 * @author WangCT
 * @version 1.0 2010.11.01
 */
public class BINOLPLRLA02_BL {

	/** 岗位类别角色分配Service */
	@Resource
	private BINOLPLRLA02_Service binOLPLRLA02_Service;

	/**
	 * 取得岗位类别信息List
	 * 
	 * @param Map
	 * @return List
	 */
	public List<Map<String, Object>> getPositionCategoryList(
			Map<String, Object> map) {

		// 取得岗位类别信息List
		List<Map<String, Object>> positionCtgList = binOLPLRLA02_Service
				.getPositionCategoryList(map);
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		String[] key0 = { "brandInfoId", "brandNameChinese" };
		String[] key1 = { "id", "name", "pId" };
		List<String[]> keysList = new ArrayList<String[]>();
		keysList.add(key0);
		keysList.add(key1);
		ConvertUtil.convertList2DeepList(positionCtgList, list, keysList, 0);
		convertDeepList2TreeList(list);
		return list;
	}

	/**
	 * 将层级list转换为 标准 zTreeNodes
	 * 
	 * @param Deeplist
	 */
	private void convertDeepList2TreeList(List<Map<String, Object>> Deeplist) {

		for (int i = 0; i < Deeplist.size(); i++) {
			Map<String, Object> listMap = Deeplist.get(i);
			List<Map<String, Object>> nodesList = (List<Map<String, Object>>) listMap
					.get("list");

			// 将岗位节点的“list”去掉
			for (int j = 0; j < nodesList.size(); j++) {
				Map<String, Object> nodesMap = nodesList.get(j);
				nodesMap.remove("list");
			}

			listMap.put("id",
					Integer.parseInt(listMap.get("brandInfoId").toString()));// 品牌自身节点id
			listMap.put("pId", -1);// 品牌的父节点Id为-1;
			listMap.put("nodes", listMap.get("list"));

			if (listMap.get("brandNameChinese") == null) {
				listMap.put("name", "null");
			} else {
				listMap.put("name", listMap.get("brandNameChinese").toString());
			}

			listMap.remove("brandNameChinese");
			listMap.remove("brandInfoId");
			listMap.remove("list");
		}
	}

}
