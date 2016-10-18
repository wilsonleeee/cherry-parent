/*		
 * @(#)BINOLCM11_BL.java     1.0 2011/01/25		
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
package com.cherry.cm.cmbussiness.bl;

import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbussiness.service.BINOLCM11_Service;

/**
 * 促销品分类共通BL
 * @author zj
 *
 */
@SuppressWarnings("unchecked")
public class BINOLCM11_BL {
	
	@Resource
	private BINOLCM11_Service binOLCM11_Service;
	
	/**
	 * 取得大分类名称
	 * 
	 * @param Map
	 *			查询条件
	 * @return Map
	 *			大分类中英文名称
	 */
	public Map getPrimaryCategoryName(Map<String, Object> map) {
		return binOLCM11_Service.getPrimaryCategoryName(map);
	}
	 
	 /**
		 * 取得中分类名称Map
		 * 
		 * @param Map
		 *			查询条件
		 * @return Map
		 *			中分类中英文名称
		 */
		public Map getSecondryCategoryName(Map<String, Object> map) {
			return binOLCM11_Service.getSecondryCategoryName(map);
		}
		/**
		 * 取得小分类名称Map
		 * 
		 * @param Map
		 *			查询条件
		 * @return Map
		 *			小分类中英文名称
		 */
		public Map getSmallCategoryName(Map<String, Object> map) {
			return binOLCM11_Service.getSmallCategoryName(map);
		}
}
