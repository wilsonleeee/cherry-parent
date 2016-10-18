/*
 * @(#)DayRuleCalCountHandler.java     1.0 2012/08/06
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

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.DateUtil;
import com.cherry.shindig.gadgets.service.mb.RuleCalStateService;
import com.googlecode.jsonplugin.JSONUtil;

/**
 * 
 * 按天统计会员积分计算笔数Handler
 * 
 * @author WangCT
 * @version 1.0 2012/08/06
 */
@Service(name = "dayRuleCalCount")
public class DayRuleCalCountHandler {
	
	/** 查看积分信息Service **/
	@Resource
	private RuleCalStateService ruleCalStateService;
	
	@SuppressWarnings("unchecked")
	@Operation(httpMethods = "POST", bodyParam = "data")
	public Future<DataCollection> getRuleCalState(RequestItem request) throws Exception {
		
		String bodyparams = request.getParameter("data");
		Map paramMap = (Map)JSONUtil.deserialize(bodyparams);
		// 用户信息
		Map userInfoMap = (Map)paramMap.get("userInfo");
		Map<String, Object> map = new HashMap<String, Object>();
		// 组织ID
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfoMap.get("BIN_OrganizationInfoID"));
		// 品牌ID
		Object brandInfoId = userInfoMap.get("BIN_BrandInfoID");
		if(brandInfoId != null && CherryConstants.BRAND_INFO_ID_VALUE != Integer.parseInt(brandInfoId.toString())) {
			map.put(CherryConstants.BRANDINFOID, userInfoMap.get("BIN_BrandInfoID"));
		}
		// 统计天数
		int days = Integer.parseInt(paramMap.get("days").toString());
		// 系统时间
		String sysDate = ruleCalStateService.getDateYMD();
		// 根据系统时间和统计天数算出统计开始日期
		String startDate = DateUtil.addDateByDays("yyyy/MM/dd",sysDate,1-days);
		map.put("changeDate", startDate);
		
		map.put(CherryConstants.USERID, userInfoMap.get("BIN_UserID"));
		map.put("businessType", "2");
		map.put("operationType", "1");
		// 按天统计会员积分计算笔数
		List<Map<String, Object>> _ruleCalCountByDayList = ruleCalStateService.getRuleCalCountByDay(map);
		
		String tempDate = startDate;
		// 从统计开始日期到系统日期的计算笔数List
		List<Map<String, Object>> ruleCalCountByDayList = new ArrayList<Map<String,Object>>();
		for(int i = 0; i < days; i++) {
			boolean exit = false;
			if(_ruleCalCountByDayList != null && _ruleCalCountByDayList.size() > 0) {
				String changeDate = (String)_ruleCalCountByDayList.get(0).get("changeDate");
				// 数据库中存在积分计算日期时，记录数据库统计的积分计算笔数
				if(tempDate.equals(changeDate)) {
					ruleCalCountByDayList.add(_ruleCalCountByDayList.get(0));
					_ruleCalCountByDayList.remove(0);
					exit = true;
				}
			}
			// 数据库不存在积分计算的日期时，记录该日期的积分计算笔数为0
			if(!exit) {
				Map<String, Object> ruleCalCountByDayMap = new HashMap<String, Object>();
				ruleCalCountByDayMap.put("changeDate", tempDate);
				ruleCalCountByDayMap.put("totalCount", 0);
				ruleCalCountByDayList.add(ruleCalCountByDayMap);
			}
			tempDate = DateUtil.addDateByDays("yyyy/MM/dd", tempDate, 1);
		}
		// 返回结果
		Map resultData = new HashMap();
		resultData.put("dayRuleCalCountMes", ruleCalCountByDayList);
		return ImmediateFuture.newInstance(new DataCollection(resultData));
	}

}
