/*	
 * @(#)BINOLSSPRM05_BL.java     1.0 2010/11/23		
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
import com.cherry.cm.core.CherryException;
import com.cherry.ss.prm.service.BINOLSSPRM05_Service;

/**
 * 促销产品分类查询 BL
 * 
 */

public class BINOLSSPRM05_BL {

	@Resource
	private BINOLSSPRM05_Service binolssprm05_Service;

	/**
	 * 取得促销品分类总数
	 * 
	 * @param map
	 * @return
	 */
	public int getPrmTypeCount(Map<String, Object> map) {

		return binolssprm05_Service.getPrmTypeCount(map);
	}

	/**
	 * 取得促销品分类信息List
	 * 
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List searchPrmTypeList(Map<String, Object> map) {

		// 取得促销品基本信息List
		return binolssprm05_Service.getPrmTypeList(map);
	}

	/**
	 * 伦理删除促销产品分类信息
	 * 
	 * @param prmTypeInfos
	 *            促销产品分类参数数组（有效区分+促销产品分类ID+更新日期+更新次数）
	 * @param updatedBy （更新者ID）
	 * 
	 * @param validFlag
	 *            有效区分1：启用, 0：停用           
	 * @return
	 */
	public void tran_operatePrmType(String[] prmTypeInfos, String validFlag,
			int updatedBy) throws CherryException, Exception {
		// 没有选择任何促销品分类
		if (null == prmTypeInfos || 0 == prmTypeInfos.length) {
			throw new CherryException("ESS00018");
		}
		for (String prmTypeInfo : prmTypeInfos) {
			String[] info = prmTypeInfo.split(CherryConstants.UNLINE);
			//参数Map
			Map<String, Object> map = new HashMap<String, Object>();
			// 促销产品分类ID
			map.put("prmTypeId", info[1]);
			// 修改时间
			map.put(CherryConstants.MODIFY_TIME, info[2]);
			// 修改次数
			map.put(CherryConstants.MODIFY_COUNT, info[3]);
			// 更新者
			map.put(CherryConstants.UPDATEDBY, updatedBy);
			//有效区分
			map.put(CherryConstants.VALID_FLAG, validFlag);
			// 更新时间
			map.put(CherryConstants.UPDATE_TIME, binolssprm05_Service.getSYSDate());
			// 更新模块
			map.put(CherryConstants.UPDATEPGM, "BINOLSSPRM05");
			
			// 伦理删除促销品分类信息
			binolssprm05_Service.operatePrmType(map);
		}
	}
	
}
