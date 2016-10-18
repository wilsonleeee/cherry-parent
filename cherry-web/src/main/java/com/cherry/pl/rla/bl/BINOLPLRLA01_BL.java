/*
 * @(#)BINOLPLRLA01_BL.java     1.0 2010/10/27
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

import com.cherry.cm.core.CherryConstants;
import com.cherry.pl.rla.service.BINOLPLRLA01_Service;
import com.googlecode.jsonplugin.JSONUtil;

/**
 * 组织角色分配BL
 * 
 * @author WangCT
 * @version 1.0 2010.10.27
 */
public class BINOLPLRLA01_BL {
	
	/** 组织角色分配Service */
	@Resource
	private BINOLPLRLA01_Service binOLPLRLA01_Service;
	
	/**
	 * 取得某一组织的直属下级组织
	 * 
	 * @param Map
	 * @return String 
	 */
	public String getNextOrganizationList(Map<String, Object> map) throws Exception {
		// 组织节点位置
		String path = (String)map.get(CherryConstants.PATH);
		List<Map<String, Object>> list =  new ArrayList<Map<String, Object>>();
		if(path == null || "".equals(path)) {
			// 取得品牌下的顶层部门级别
			Integer level = binOLPLRLA01_Service.getFirstOrganizationLevel(map);
			if(level != null) {
				// 顶层部门级别
				map.put(CherryConstants.LEVEL, level);
				// 取得品牌下的顶层部门List
				list = binOLPLRLA01_Service.getFirstOrganizationList(map);
			}
		} else {
			// 取得某一组织的直属下级组织
			list = binOLPLRLA01_Service.getNextOrganizationInfoList(map);
		}
		StringBuffer jsonTree = new StringBuffer();
		if(list != null && !list.isEmpty()) {
			jsonTree.append("[");
			for(int i = 0; i < list.size(); i++) {
				Map<String, Object> organizationMap = list.get(i);
				// 是否有子节点
				String child = "";
				// 有子节点的场合
				if(organizationMap.get("child") != null && !"".equals(organizationMap.get("child"))) {
					child = Integer.parseInt(organizationMap.get("child").toString()) > 0 ? "closed" : "";
				}
				String departName = "(" + (String)organizationMap.get("departCode") + ")" + (String)organizationMap.get(CherryConstants.DEPARTNAME);
				departName = JSONUtil.serialize(departName);
				// 树结构作成
				jsonTree.append("{\"data\":{\"title\":"+departName+",\"attr\":{\"id\":\""+organizationMap.get("organizationId")+"_"+organizationMap.get("brandInfoId")+"\"}},\"attr\":{\"id\":\""+organizationMap.get("path")+"\"},\"state\":\""+child+"\"}");
				if(i+1 != list.size()) {
					jsonTree.append(",");
				}
			}
			jsonTree.append("]");
		}
		return jsonTree.toString();
	}

}
