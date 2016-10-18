/*
 * @(#)BINOLSSPRM07_BL.java     1.0 2010/11/29
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
import com.cherry.ss.prm.service.BINOLSSPRM07_Service;

/**
 * 促销产品分类编辑BL
 * 
 */
public class BINOLSSPRM07_BL {
	
	@Resource
	private BINOLSSPRM07_Service binolssprm07_Service;
	
	/**
	 * 更新促销产品分类信息
	 * 
	 * @param prmTypeInfos
	 *            促销产品分类参数数组（促销产品分类ID+更新日期+更新次数）
	 * @param updatedBy （更新者ID）
	 *            
	 * @return
	 */
	public void tran_updatePrmType(Map<String, Object> map) throws CherryException, Exception {
			// 更新时间
			map.put(CherryConstants.UPDATE_TIME, binolssprm07_Service.getSYSDate());
			// 更新模块
			map.put(CherryConstants.UPDATEPGM, "BINOLSSPRM07");
			// 更新促销品分类信息
			int result = binolssprm07_Service.updatePrmTypeInfo(map);
			// 更新失败
			if (0 == result) {
				throw new CherryException("ECM00038");
			}
			//更新促销品分类大分类名称
			binolssprm07_Service.updatePrmTypePrimary(map);
			//更新促销品分类中分类名称
			binolssprm07_Service.updatePrmTypeSecondry(map);
			//更新促销品分类小分类名称
			binolssprm07_Service.updatePrmTypeSmall(map);
	}
}
