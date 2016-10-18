/*
 * @(#)BINOLBSPOS03_BL.java     1.0 2010/10/27
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
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.bs.pos.service.BINOLBSPOS03_Service;
import com.cherry.bs.pos.service.BINOLBSPOS04_Service;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;

/**
 * 
 * 更新岗位画面BL
 * 
 * @author WangCT
 * @version 1.0 2010.10.27
 */
public class BINOLBSPOS03_BL {
	
	/** 更新岗位画面Service */
	@Resource
	private BINOLBSPOS03_Service binOLBSPOS03_Service;
	
	/** 添加岗位画面Service */
	@Resource
	private BINOLBSPOS04_Service binOLBSPOS04_Service;
	
	/**
	 * 
	 * 更新岗位信息
	 * 
	 * @param map 更新条件
	 */
	public void tran_updatePositionInfo(Map<String, Object> map) throws Exception {
		
		Map<String, Object> paramMap = new HashMap<String, Object>();
		// 更新者
		paramMap.put(CherryConstants.UPDATEDBY, map.get(CherryConstants.UPDATEDBY));
		// 更新程序名
		paramMap.put(CherryConstants.UPDATEPGM, "BINOLBSPOS03");
		// 所属部门
		paramMap.put("organizationId", map.get("organizationId"));
		// 岗位ID
		paramMap.put("positionId", map.get("positionId"));
		// 岗位名称
		paramMap.put("positionName", map.get("positionName"));
		// 更新日时
		paramMap.put("modifyTime", map.get("modifyTime"));
		// 更新次数
		paramMap.put("modifyCount", map.get("modifyCount"));
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
		// 更新岗位信息
		int result = binOLBSPOS03_Service.updatePosition(paramMap);
		// 更新0件的场合
		if(result == 0) {
			throw new CherryException("ECM00038");
		}
		// 上级岗位变更的场合
		if(map.get("higherPositionPath") != null && map.get("path") != null && !map.get("higherPositionPath").equals(map.get("path"))) {
			// 取得新岗位节点
			String newNodeId = binOLBSPOS04_Service.getNewNodeId(map);
			map.put("newNodeId", newNodeId);
			// 岗位结构节点移动
			binOLBSPOS03_Service.updatePositionNode(map);
		}
		
	}

}
