/*
 * @(#)BINOLBSREG04_BL.java     1.0 2011/11/23
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.bs.reg.interfaces.BINOLBSREG04_IF;
import com.cherry.bs.reg.service.BINOLBSREG04_Service;
import com.cherry.cm.core.CherryConstants;

/**
 * 区域添加画面BL
 * 
 * @author WangCT
 * @version 1.0 2011/11/23
 */
public class BINOLBSREG04_BL implements BINOLBSREG04_IF {
	
	/** 区域添加画面Service */
	@Resource
	private BINOLBSREG04_Service binOLBSREG04_Service;

	/**
	 * 添加区域处理
	 * 
	 * @param map 添加内容
	 */
	@Override
	public void tran_addRegion(Map<String, Object> map) {
		// 上级不存在的场合，设上级为默认的根节点
		map.put("regionTypeBrand", "-1");
		// 取得品牌下的新节点
		String nodeId = binOLBSREG04_Service.getNewRegNodeIdInBrand(map);
		// 品牌节点不存在的场合,设置默认根节点为"/"
		if(nodeId == null) {
			map.put("higherPath", CherryConstants.DUMMY_VALUE);
			// 取得新节点
			nodeId = binOLBSREG04_Service.getNewRegNodeId(map);
		}
		map.put("nodeId", nodeId);
		map.put("joinDate", binOLBSREG04_Service.getSYSDate());
		// 添加区域
		binOLBSREG04_Service.addRegion(map);
		// 取得所属省份path
		List<String> provincePathList = (List<String>)map.get("provincePath");
		if(provincePathList != null && !provincePathList.isEmpty()) {
			Map<String, Object> provinceMap = new HashMap<String, Object>();
			provinceMap.putAll(map);
			provinceMap.put("higherPath", nodeId);
			for(String provincePath : provincePathList) {
				String newProvinceNodeId = binOLBSREG04_Service.getNewRegNodeId(provinceMap);
				provinceMap.put("newNodeId", newProvinceNodeId);
				provinceMap.put("regionPath", provincePath);
				// 区域结构节点移动
				binOLBSREG04_Service.updateRegionNode(provinceMap);
			}
		}
	}
}
