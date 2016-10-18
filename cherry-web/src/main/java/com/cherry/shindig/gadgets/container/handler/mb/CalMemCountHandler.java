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
 * 会员数统计Handler
 * 
 * @author WangCT
 * @version 1.0 2014/09/03
 */
@Service(name = "calMemCount")
public class CalMemCountHandler {
	
	/** 查看积分信息Service **/
	@Resource
	private RuleCalStateService ruleCalStateService;
	
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
		// 统计日期范围设定
		List<Map<String, Object>> dateList = new ArrayList<Map<String,Object>>();
		String sysDate = ruleCalStateService.getDateYMD();
		String startDate = DateUtil.addDateByMonth("yyyy-MM-dd",sysDate,-1);
		Map<String, Object> dateMap = new HashMap<String, Object>();
		dateMap.put("startDate", startDate);
		dateMap.put("endDate", sysDate);
		dateMap.put("key", "M");
		dateList.add(dateMap);
		dateMap = new HashMap<String, Object>();
		startDate = DateUtil.addDateByYears("yyyy-MM-dd",sysDate,-1);
		dateMap.put("startDate", startDate);
		dateMap.put("endDate", sysDate);
		dateMap.put("key", "Y");
		dateList.add(dateMap);
		map.put("dateList", dateList);
		
		map.put(CherryConstants.USERID, userInfoMap.get("BIN_UserID"));
		map.put("businessType", "2");
		map.put("operationType", "1");
		// 取得会员总数和新会员数
		Map<String, Object> memCountMap = ruleCalStateService.getMemCount(map);
		
		for(int i = 0; i < dateList.size(); i++) {
			String key = (String)dateList.get(i).get("key");
			map.putAll(dateList.get(i));
			// 取得活跃会员数
			Map<String, Object> activeMemCountMap = ruleCalStateService.getActiveMemCount(map);
			memCountMap.put("activeMemCount_"+key, activeMemCountMap.get("activeMemCount"));
		}
		
		// 返回结果
		Map resultData = new HashMap();
		resultData.putAll(memCountMap);
		return ImmediateFuture.newInstance(new DataCollection(resultData));
	}

}
