/*
 * @(#)MemSaleInfoHandler.java     1.0 2012/12/06
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

import com.cherry.shindig.gadgets.service.mb.MemDetailInfoService;
import com.googlecode.jsonplugin.JSONUtil;

/**
 * 
 * 会员销售记录Handler
 * 
 * @author WangCT
 * @version 1.0 2012/12/06
 */
@Service(name="memSaleInfo")
public class MemSaleInfoHandler {

	/** 查看会员详细信息Service **/
	@Resource
	private MemDetailInfoService memDetailInfoService;
	
	@SuppressWarnings("unchecked")
	@Operation(httpMethods = "POST", bodyParam = "data")
	public Future<DataCollection> getRuleCalState(RequestItem request) throws Exception {
		
		String bodyparams = request.getParameter("data");
		Map paramMap = (Map)JSONUtil.deserialize(bodyparams);
		Map gadgetParam = (Map)paramMap.get("gadgetParam");
		String pageNo = (String)paramMap.get("pageNo");
		String pageNumber = (String)paramMap.get("pageNumber");
		int length = Integer.parseInt(pageNumber);
		int start = (Integer.parseInt(pageNo) - 1) * length + 1;
		int end = start + length -1;
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("SORT_ID", "saleTime DESC");
		map.put("START", start);
		map.put("END", end);
		map.put("memberInfoId", gadgetParam.get("memberInfoId"));
		
		// 返回结果
		Map resultData = new HashMap();
		resultData.put("pageNo", Integer.parseInt(pageNo));
		resultData.put("pageNumber", length);
		// 统计销售总金额和总数量
		Map<String, Object> saleCountInfo = memDetailInfoService.getSaleAmountSum(map);
		resultData.put("saleCountInfo", saleCountInfo);
		if(saleCountInfo != null) {
			int totalCount = (Integer)saleCountInfo.get("totalCount");
			if(totalCount > 0) {
				// 查询销售信息List
				List<Map<String, Object>> saleInfoList = memDetailInfoService.getSaleInfoList(map);
				resultData.put("saleInfoList", saleInfoList);
			}
		}
		return ImmediateFuture.newInstance(new DataCollection(resultData));
	}
}
