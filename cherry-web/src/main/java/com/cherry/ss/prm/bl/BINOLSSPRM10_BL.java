/*
 * @(#)BINOLSSPRM10_BL.java     1.0 2011/02/24
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
package com.cherry.ss.prm.bl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.core.CherryConstants;
import com.cherry.ss.prm.service.BINOLSSPRM10_Service;

/**
 * 促销品类别添加 BL
 * 
 *
 */
public class BINOLSSPRM10_BL {
	
	@Resource
	private BINOLSSPRM10_Service binolssprm10_Service;
	
	/**
	 * 查询上级促销品类别List
	 * 
	 * @param map 查询条件
	 * @return 上级促销品类别List
	 */
	@SuppressWarnings("unchecked")
	public List getHigherCategoryList(Map<String, Object> map) {
		
		// 查询促销品类别
		return binolssprm10_Service.getHigherCategoryList(map);
	}
	/**
	 * 促销品类别添加处理
	 * 
	 * @param map
	 * @return 无
	 */
	public void trans_addPrmCategory(Map<String, Object> map) {
		
		Map<String, Object> cateMap = new HashMap<String, Object>();
		// 上级类别不存在的场合，设上级类别为默认的根节点
		if(map.get("path") == null || "".equals(map.get("path"))) {
			cateMap.put("path", CherryConstants.DUMMY_VALUE);
		} else {
			cateMap.put("path", map.get("path"));
		}
		// 取得类别新节点
		String newNodeId = binolssprm10_Service.getNewNodeId(cateMap);
		cateMap.put("newNodeId", newNodeId);
		// 系统时间
		String sysDate = binolssprm10_Service.getSYSDate();
		// 所属组织
		cateMap.put(CherryConstants.ORGANIZATIONINFOID, map.get(CherryConstants.ORGANIZATIONINFOID));
		// 所属品牌
		cateMap.put(CherryConstants.BRANDINFOID, map.get(CherryConstants.BRANDINFOID));
		// 作成时间
		cateMap.put(CherryConstants.CREATE_TIME, sysDate);
		// 作成者
		cateMap.put(CherryConstants.CREATEDBY, map.get(CherryConstants.CREATEDBY));
		// 作成程序名
		cateMap.put(CherryConstants.CREATEPGM, "BINOLBSDEP04");
		// 更新者
		cateMap.put(CherryConstants.UPDATEDBY, map.get(CherryConstants.UPDATEDBY));
		// 更新程序名
		cateMap.put(CherryConstants.UPDATEPGM, "BINOLBSDEP04");
		// 更新时间
		cateMap.put(CherryConstants.UPDATE_TIME, sysDate);
		// 类别中文名
		if(map.get("itemClassNameCN") != null && !"".equals(map.get("itemClassNameCN"))) {
			cateMap.put("itemClassNameCN", map.get("itemClassNameCN"));
		}
		// 类别英文名
		if(map.get("itemClassNameEN") != null && !"".equals(map.get("itemClassNameEN"))) {
			cateMap.put("itemClassNameEN", map.get("itemClassNameEN"));
		}
		// 类别代码
		if(map.get("itemClassCode") != null && !"".equals(map.get("itemClassCode"))) {
			cateMap.put("itemClassCode", map.get("itemClassCode"));
		}
		// 类别特征码
		if(map.get("curClassCode") != null && !"".equals(map.get("curClassCode"))) {
			cateMap.put("curClassCode", map.get("curClassCode"));
		}
		// 添加促销品类别
		binolssprm10_Service.insertPrmCategory(cateMap);
		
	}
	
	/**
	 * 查询上级促销品类别
	 * 
	 * @param map 查询条件
	 * @return 上级促销品类别
	 */
	@SuppressWarnings("unchecked")
	public List getHigherClass(Map<String, Object> map) {
		
		// 查询上级部门信息
		return binolssprm10_Service.getHigherClass(map);
	}

}
