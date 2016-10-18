/*
 * @(#)BINOLCTRPT06_BL.java     1.0 2014/11/11
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
package com.cherry.ct.rpt.bl;

import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import com.cherry.ct.rpt.interfaces.BINOLCTRPT06_IF;
import com.cherry.ct.rpt.service.BINOLCTRPT06_Service;

/**
 * 会员沟通效果统计 BL
 * @author menghao
 * @version 1.0 2014/11/11
 *
 */
public class BINOLCTRPT06_BL implements BINOLCTRPT06_IF{
	
	@Resource(name="binOLCTRPT06_Service")
	private BINOLCTRPT06_Service binOLCTRPT06_Service;

	@Override
	public Map<String, Object> getMemCommunResultInfo(Map<String, Object> map) {
		return binOLCTRPT06_Service.getMemCommunResultInfo(map);
	}

	@Override
	public int getMemCommunStatisticsCount(Map<String, Object> map) {
		return binOLCTRPT06_Service.getMemCommunStatisticsCount(map);
	}

	@Override
	public List<Map<String, Object>> getMemCommunStatisticsList(
			Map<String, Object> map) throws Exception {
		return binOLCTRPT06_Service.getMemCommunStatisticsList(map);
	}

	@Override
	public int getCommunEffectDetailCount(Map<String, Object> map) {
		return binOLCTRPT06_Service.getCommunEffectDetailCount(map);
	}

	@Override
	public List<Map<String, Object>> getCommunEffectDetailList(
			Map<String, Object> map) throws Exception {
		return binOLCTRPT06_Service.getCommunEffectDetailList(map);
	}
	
}
