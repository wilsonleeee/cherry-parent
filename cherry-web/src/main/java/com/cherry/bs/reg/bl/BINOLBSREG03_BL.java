/*
 * @(#)BINOLBSREG03_BL.java     1.0 2011/11/23
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

import com.cherry.bs.reg.interfaces.BINOLBSREG03_IF;
import com.cherry.bs.reg.service.BINOLBSREG03_Service;
import com.cherry.bs.reg.service.BINOLBSREG04_Service;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;

/**
 * 区域更新画面BL
 * 
 * @author WangCT
 * @version 1.0 2011/11/23
 */
public class BINOLBSREG03_BL implements BINOLBSREG03_IF {

	/** 区域更新画面Service */
	@Resource
	private BINOLBSREG03_Service binOLBSREG03_Service;
	
	/** 区域添加画面Service */
	@Resource
	private BINOLBSREG04_Service binOLBSREG04_Service;
	
	/**
	 * 更新区域处理
	 * 
	 * @param map 更新内容
	 */
	@Override
	public void tran_updateRegion(Map<String, Object> map) throws Exception {
		
		// 更新区域信息
		int result = binOLBSREG03_Service.updateRegion(map);
		if(result == 0) {
			throw new CherryException("ECM00038");
		}
		
		// 取得区域原上级
		String oldHigherPath = (String)map.get("oldHigherPath");
		// 取得区域新上级
		String higherPath = (String)map.get("higherPath");
		// 区域上级是否变化
		boolean isMove = false;
		if(higherPath != null) {
			if(oldHigherPath != null && oldHigherPath.equals(higherPath)) {
				isMove = false;
			} else {
				isMove = true;
			}
		} else {
			if(oldHigherPath != null) {
				isMove = true;
			}
		}
		// 上级区域变更的场合
		if(isMove) {
			// 上级存在的场合
			if(higherPath != null && !"".equals(higherPath)) {
				// 取得新节点
				String nodeId = binOLBSREG04_Service.getNewRegNodeId(map);
				map.put("nodeId", nodeId);
			} else {
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
			}
			// 区域结构节点移动
			binOLBSREG03_Service.updateRegionNode(map);
		}
		
		//大区下添加新的省份节点
		// 取得区域path
		String regionPath = (String)map.get("regionPath");
		// 取得所属省份path
		List<String> provincePathList = (List<String>)map.get("provincePath");
		if(provincePathList != null && !provincePathList.isEmpty()) {
			Map<String, Object> provinceMap = new HashMap<String, Object>();
			provinceMap.putAll(map);
			provinceMap.put("higherPath", regionPath);
			for(String provincePath : provincePathList) {
				String newProvinceNodeId = binOLBSREG04_Service.getNewRegNodeId(provinceMap);
				provinceMap.put("newNodeId", newProvinceNodeId);
				provinceMap.put("regionPath", provincePath);
				// 区域结构节点移动
				binOLBSREG04_Service.updateRegionNode(provinceMap);
			}
		}
		//删除大区下的省份节点
		List<String> oldProvincePathList = (List<String>)map.get("oldProvincePath");
		if(oldProvincePathList != null && !oldProvincePathList.isEmpty()) {
			Map<String, Object> provinceMap = new HashMap<String, Object>();
			provinceMap.putAll(map);
			provinceMap.put("higherPath", CherryConstants.DUMMY_VALUE);
				for(String oldprovincePath : oldProvincePathList) {
					if(provincePathList != null &&!provincePathList.isEmpty()){
						for(String provincePath : provincePathList) {
							if(provincePath!=oldprovincePath){
								// 取得品牌下的新节点
								String newProvinceNodeId = binOLBSREG04_Service.getNewRegNodeId(provinceMap);
								provinceMap.put("newNodeId", newProvinceNodeId);
								provinceMap.put("regionPath", oldprovincePath);
								// 区域结构节点移动
								binOLBSREG04_Service.updateRegionNode(provinceMap);
								}
							}
						}else{
							   // 取得品牌下的新节点
								String newProvinceNodeId = binOLBSREG04_Service.getNewRegNodeId(provinceMap);
								provinceMap.put("newNodeId", newProvinceNodeId);
								provinceMap.put("regionPath", oldprovincePath);
								// 区域结构节点移动
								binOLBSREG04_Service.updateRegionNode(provinceMap);
						}
					}
				}
			}
	}
