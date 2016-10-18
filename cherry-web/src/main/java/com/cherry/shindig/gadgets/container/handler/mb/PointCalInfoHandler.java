/*
 * @(#)PointCalInfoHandler.java     1.0 2012/08/07
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

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.DateUtil;
import com.cherry.shindig.gadgets.service.mb.RuleCalStateService;
import com.cherry.shindig.gadgets.util.GadgetsConstants;
import com.googlecode.jsonplugin.JSONUtil;

/**
 * 
 * 积分计算信息Handler
 * 
 * @author WangCT
 * @version 1.0 2012/08/07
 */
@Service(name = "pointCalInfo")
public class PointCalInfoHandler {
	
	/** 查看积分信息Service **/
	@Resource
	private RuleCalStateService ruleCalStateService;
	
	@SuppressWarnings("unchecked")
	@Operation(httpMethods = "POST", bodyParam = "data")
	public Future<DataCollection> getRuleCalState(RequestItem request) throws Exception {
		
		String bodyparams = request.getParameter("data");
		Map paramMap = (Map)JSONUtil.deserialize(bodyparams);
		Map userInfoMap = (Map)paramMap.get("userInfo");
		String pageNo = (String)paramMap.get("pageNo");
		int length = GadgetsConstants.PAGE_LENGTH;
		int start = (Integer.parseInt(pageNo) - 1) * length + 1;
		int end = start + length -1;
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("SORT_ID", "changeDate DESC");
		map.put("START", start);
		map.put("END", end);
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfoMap.get("BIN_OrganizationInfoID"));
		Object brandInfoId = userInfoMap.get("BIN_BrandInfoID");
		if(brandInfoId != null && CherryConstants.BRAND_INFO_ID_VALUE != Integer.parseInt(brandInfoId.toString())) {
			map.put(CherryConstants.BRANDINFOID, userInfoMap.get("BIN_BrandInfoID"));
		}
		map.put(CherryConstants.USERID, userInfoMap.get("BIN_UserID"));
		map.put("businessType", "2");
		map.put("operationType", "1");
		String sysDate = ruleCalStateService.getDateYMD();
		map.put("changeDateStart", DateUtil.suffixDate(sysDate, 0));
		map.put("changeDateEnd", DateUtil.suffixDate(sysDate, 1));
		
		// 返回结果
		Map resultData = new HashMap();
		
		List<Map<String, Object>> pointCalInfoList = ruleCalStateService.getPointCalInfoList(map);
		if(pointCalInfoList != null && !pointCalInfoList.isEmpty()) {
			resultData.put("pointCalInfoMes", pointCalInfoList);
			if(pointCalInfoList.size() == length) {
				if(Integer.parseInt(pageNo) + 1 <= GadgetsConstants.PAGE_MAX_COUNT) {
					resultData.put("pageNo", Integer.parseInt(pageNo) + 1);
				}
			}
		}
		// 第一次请求的场合，统计会员当天积分计算笔数
		if("1".equals(pageNo)) {
			// 统计会员当天积分计算笔数
			int pointCalCount = ruleCalStateService.getPointCalCount(map);
			resultData.put("pointCalCount", pointCalCount);
		}
		resultData.put("sysDate", sysDate);
		
		return ImmediateFuture.newInstance(new DataCollection(resultData));
	}

}
