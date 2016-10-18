/*
 * @(#)MemSaleCountInfoHandler.java     1.0 2012/12/06
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
package com.cherry.shindig.gadgets.container.handler.mb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

import javax.annotation.Resource;

import org.apache.shindig.common.util.ImmediateFuture;
import org.apache.shindig.protocol.DataCollection;
import org.apache.shindig.protocol.Operation;
import org.apache.shindig.protocol.RequestItem;
import org.apache.shindig.protocol.Service;

import com.cherry.cm.util.DateUtil;
import com.cherry.shindig.gadgets.service.mb.MemDetailInfoService;
import com.googlecode.jsonplugin.JSONUtil;

/**
 * 
 * 会员销售分析报表Handler
 * 
 * @author WangCT
 * @version 1.0 2012/12/06
 */
@Service(name="memSaleCountInfo")
public class MemSaleCountInfoHandler {
	
	/** 查看会员详细信息Service **/
	@Resource
	private MemDetailInfoService memDetailInfoService;
	
	@SuppressWarnings("unchecked")
	@Operation(httpMethods = "POST", bodyParam = "data")
	public Future<DataCollection> getRuleCalState(RequestItem request) throws Exception {
		
		String bodyparams = request.getParameter("data");
		Map paramMap = (Map)JSONUtil.deserialize(bodyparams);
		Map gadgetParam = (Map)paramMap.get("gadgetParam");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("memberInfoId", gadgetParam.get("memberInfoId"));
		String sysDate = memDetailInfoService.getSYSDate();
		
		List<Map<String, Object>> saleCountInfoList = new ArrayList<Map<String,Object>>();
		int[] months = new int[]{3,6,12};
		for(int i = 0; i < months.length; i++) {
			Map<String, Object> saleCountInfoByTime = new HashMap<String, Object>();
			saleCountInfoByTime.put("saleDateStart", months[i]);
			String saleDateStart = DateUtil.addDateByMonth("yyyy-MM-dd", sysDate.substring(0,10), -months[i]);
			map.put("saleDateStart", saleDateStart);
			// 统计不同时间段的销售金额、数量、次数
			Map<String, Object> saleCountInfo = memDetailInfoService.getSaleCountInfoByTime(map);
			saleCountInfoByTime.put("saleCountInfo", saleCountInfo);
			saleCountInfoList.add(saleCountInfoByTime);
		}
		
		// 返回结果
		Map resultData = new HashMap();
		resultData.put("saleCountInfoList", saleCountInfoList);
		return ImmediateFuture.newInstance(new DataCollection(resultData));
	}

}
