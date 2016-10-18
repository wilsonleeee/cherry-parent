/*
 * @(#)BINOLSSPRM11_BL.java     1.0 2010/11/29
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

import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.ss.prm.service.BINOLSSPRM11_Service;
import com.cherry.ss.prm.service.BINOLSSPRM10_Service;

/**
 * 促销产品类别编辑BL
 * 
 */
public class BINOLSSPRM11_BL {
	
	//促销产品类别编辑SERVICE
	@Resource
	private BINOLSSPRM11_Service binolssprm11_Service;
	
	//促销产品类别添加SERVICE
	@Resource
	private BINOLSSPRM10_Service binolssprm10_Service;
	
	/**
	 * 更新促销产品类别信息
	 * 
	 *            
	 * @return map
	 */
	public void tran_updatePrmCate(Map<String, Object> map) throws Exception {
		
			// 更新时间
			map.put(CherryConstants.UPDATE_TIME, binolssprm11_Service.getSYSDate());
			// 更新模块
			map.put(CherryConstants.UPDATEPGM, "BINOLSSPRM11");
			
			// 更新促销品分类信息
			int result = binolssprm11_Service.updatePrmCategory(map);
			// 更新失败
			if (0 == result) {
				throw new CherryException("ECM00038");
			}
			// 上级促销产品类别变更的场合
			if(map.get("higherCategoryPath") != null && map.get("path") != null && !map.get("higherCategoryPath").equals(map.get("path"))) {
				// 取得新促销产品类别节点
				String newNodeId = binolssprm10_Service.getNewNodeId(map);
				map.put("newNodeId", newNodeId);
				// 促销产品类别结构节点移动
				binolssprm11_Service.updatePrmCateNode(map);
			}
	}
	/**
	 * 验证是否存在同样的促销品类别ID
	 * 
	 * @param map 查询条件
	 * @return 件数
	 */
	public String getPrmCategoryIdCheck(Map<String, Object> map) {
		
		// 验证是否存在同样的促销品类别ID
		return binolssprm11_Service.getPrmCategoryIdCheck(map);
	}
}
