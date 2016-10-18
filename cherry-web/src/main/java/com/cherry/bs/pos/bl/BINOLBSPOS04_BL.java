/*
 * @(#)BINOLBSPOS04_BL.java     1.0 2010/10/27
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.bs.pos.service.BINOLBSPOS04_Service;
import com.cherry.cm.core.CherryConstants;

/**
 * 添加岗位画面BL
 * 
 * @author WangCT
 * @version 1.0 2010.10.27
 */
public class BINOLBSPOS04_BL {
	
	/** 添加岗位画面Service */
	@Resource
	private BINOLBSPOS04_Service binOLBSPOS04_Service;
	
	/**
	 * 添加岗位
	 * 
	 * @param map 添加内容
	 */
	public void tran_addPosition(Map<String, Object> map) {
		
		// 上级岗位不存在的场合，设上级岗位为默认的顶层节点
		if(map.get("path") == null || "".equals(map.get("path"))) {
			map.put("path", CherryConstants.DUMMY_VALUE);
		}
		// 取得新岗位节点
		String newNodeId = binOLBSPOS04_Service.getNewNodeId(map);
		
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("newNodeId", newNodeId);
		// 作成者
		paramMap.put(CherryConstants.CREATEDBY, map.get(CherryConstants.CREATEDBY));
		// 作成程序名
		paramMap.put(CherryConstants.CREATEPGM, "BINOLBSPOS04");
		// 更新者
		paramMap.put(CherryConstants.UPDATEDBY, map.get(CherryConstants.UPDATEDBY));
		// 更新程序名
		paramMap.put(CherryConstants.UPDATEPGM, "BINOLBSPOS04");
		// 所属部门
		paramMap.put("organizationId", map.get("organizationId"));
		// 岗位名称
		paramMap.put("positionName", map.get("positionName"));
		// 岗位的外文名称
		if(map.get("positionNameForeign") != null && !"".equals(map.get("positionNameForeign"))) {
			paramMap.put("positionNameForeign", map.get("positionNameForeign"));
		}
		// 岗位描述
		if(map.get("positionDESC") != null && !"".equals(map.get("positionDESC"))) {
			paramMap.put("positionDESC", map.get("positionDESC"));
		}
		// 岗位类别
		if(map.get("positionCategoryId") != null && !"".equals(map.get("positionCategoryId"))) {
			paramMap.put("positionCategoryId", map.get("positionCategoryId"));
		}
		// 是否负责人
		if(map.get("isManager") != null && !"".equals(map.get("isManager"))) {
			paramMap.put("isManager", map.get("isManager"));
		}
		// 成立日期
		if(map.get("foundationDate") != null && !"".equals(map.get("foundationDate"))) {
			paramMap.put("foundationDate", map.get("foundationDate"));
		}
		// 岗位类型
		if(map.get("positionType") != null && !"".equals(map.get("positionType"))) {
			paramMap.put("positionType", map.get("positionType"));
			if("1".equals(map.get("positionType")) || "2".equals(map.get("positionType"))) {
				// 所属经销商
				if(map.get("resellerInfoId") != null && !"".equals(map.get("resellerInfoId"))) {
					paramMap.put("resellerInfoId", map.get("resellerInfoId"));
				}
			}
		}
		// 添加岗位
		binOLBSPOS04_Service.addPosition(paramMap);
	}
	
	/**
	 * 取得岗位类别信息
	 * 
	 * @param map 查询条件
	 * @return 岗位类别信息List
	 */
	public List<Map<String, Object>> getPositionCategoryList(Map<String, Object> map) {
		
		// 取得岗位类别信息
		return binOLBSPOS04_Service.getPositionCategoryList(map);
	}

	/**
	 * 取得某一部门的所有上级部门岗位List
	 * 
	 * @param map 查询条件
	 * @return 岗位List
	 */
	public List<Map<String, Object>> getPositionByOrg(Map<String, Object> map) {
		
		// 取得某一部门的所有上级部门岗位List
		return binOLBSPOS04_Service.getPositionByOrg(map);
	}
	
	/**
	 * 判断组织中是否存在岗位
	 * 
	 * @param map 查询条件
	 * @return 岗位数
	 */
	public int getPositionIdByOrgInfo(Map<String, Object> map) {
		
		// 判断组织中是否存在岗位
		return binOLBSPOS04_Service.getPositionIdByOrgInfo(map);
	}
	
	/**
	 * 取得组织中的最上级部门ID
	 * 
	 * @param map 查询条件
	 * @return 最上级部门IDList
	 */
	public List<String> getOrgIdByOrgInfo(Map<String, Object> map) {
		
		// 取得组织中的最上级部门ID
		return binOLBSPOS04_Service.getOrgIdByOrgInfo(map);
	}

}
