/*
 * @(#)BINOLPLRLA03_BL.java     1.0 2010/11/01
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
import com.cherry.pl.rla.service.BINOLPLRLA03_Service;

/**
 * 岗位角色分配BL
 * 
 * @author WangCT
 * @version 1.0 2010.11.01
 */
public class BINOLPLRLA03_BL {
	
	/** 岗位角色分配Service */
	@Resource
	private BINOLPLRLA03_Service binOLPLRLA03_Service;
	
	/**
	 * 取得某一岗位的直属下级岗位
	 * 
	 * @param Map
	 * @return String 
	 */
	public String getNextPositionList(Map<String, Object> map) {
		// 岗位节点位置
		String path = (String)map.get(CherryConstants.PATH);
		List<Map<String, Object>> list =  new ArrayList<Map<String, Object>>();
		if(path == null || "".equals(path)) {
			// 取得品牌下的顶层岗位级别
			Integer level = binOLPLRLA03_Service.getFirstPositionLevel(map);
			if(level != null) {
				// 顶层岗位级别
				map.put(CherryConstants.LEVEL, level);
				// 取得品牌下的顶层岗位List
				list = binOLPLRLA03_Service.getFirstPositionList(map);
			}
		} else {
			// 取得某一岗位的直属下级岗位
			list = binOLPLRLA03_Service.getNextPositionList(map);
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
					child = Integer.parseInt(organizationMap.get("child").toString()) > 1 ? "closed" : "";
				}
				// 树结构作成
				jsonTree.append("{\"data\":{\"title\":\""+organizationMap.get("positionName")+"("+organizationMap.get("departName")+")"+"\",\"attr\":{\"id\":\""+organizationMap.get("positionId")+"_"+organizationMap.get("brandInfoId")+"\"}},\"attr\":{\"id\":\""+organizationMap.get("path")+"\"},\"state\":\""+child+"\"}");
				if(i+1 != list.size()) {
					jsonTree.append(",");
				}
			}
			jsonTree.append("]");
		}
		
		return jsonTree.toString();
	}

}
