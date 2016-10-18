/*
 * @(#)BINOLSSPRM011_Service.java     1.0 2010/10/29
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
package com.cherry.ss.prm.service;

import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 促销品类别编辑
 * @author Administrator
 *
 */

public class BINOLSSPRM11_Service extends BaseService {
	
	/**
	 * 更新促销产品类别信息
	 * 
	 * @param map
	 * @return int
	 */
	public int updatePrmCategory(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM11.updatePrmCategory");
		return baseServiceImpl.update(map);
		 
	}
	
	/**
	 * 
	 * 促销产品类别结构节点移动
	 * 
	 * @param map 更新条件
	 * @return 处理件数
	 */
	public int updatePrmCateNode(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM11.updatePrmCateNode");
		return baseServiceImpl.update(map);
	}

	/**
	 * 验证是否存在同样的促销品类别ID
	 * 
	 * @param map 查询条件
	 * @return 件数
	 */
	public String getPrmCategoryIdCheck(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM11.getPrmCategoryIdCheck");
		return (String)baseServiceImpl.get(map);
	}

}
