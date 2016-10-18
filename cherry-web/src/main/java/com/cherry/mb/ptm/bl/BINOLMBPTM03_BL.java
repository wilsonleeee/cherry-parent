/*
 * @(#)BINOLMBPTM03_BL.java     1.0 2012/08/08
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
package com.cherry.mb.ptm.bl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.mb.ptm.service.BINOLMBPTM03_Service;

/**
 * 查询积分明细信息BL
 * 
 * @author WangCT
 * @version 1.0 2012/08/08
 */
public class BINOLMBPTM03_BL {
	
	/** 查询积分明细信息Service */
	@Resource
	private BINOLMBPTM03_Service binOLMBPTM03_Service;
	
	/**
	 * 取得积分明细信息
	 * 
	 * @param map 检索条件
	 * @return 积分明细信息
	 */
	public Map<String, Object> getPointInfoDetail(Map<String, Object> map) {
		// 取得积分信息
		Map<String, Object> pointInfo = binOLMBPTM03_Service.getPointInfo(map);
		if(pointInfo != null && !pointInfo.isEmpty()) {
			// 取得积分明细信息
			List<Map<String, Object>> pointInfoDetail = binOLMBPTM03_Service.getPointInfoDetail(map);
			if(pointInfoDetail != null && !pointInfoDetail.isEmpty()) {
				for(int i = 0; i < pointInfoDetail.size(); i++) {
					String relevantSRCode = (String)pointInfoDetail.get(i).get("relevantSRCode");
					if(relevantSRCode != null && !"".equals(relevantSRCode)) {
						pointInfo.put("hasRelevantSRCode", "1");
						break;
					}
				}
			}
			pointInfo.put("pointInfoDetail", pointInfoDetail);
			return pointInfo;
		} else {
			return null;
		}
	}

}
