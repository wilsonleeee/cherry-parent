/*
 * @(#)AttendanceHandler.java     1.0 2011/11/01
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
package com.cherry.shindig.gadgets.container.handler;

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
import com.cherry.shindig.gadgets.service.BasAttendanceService;
import com.cherry.shindig.gadgets.util.GadgetsConstants;
import com.googlecode.jsonplugin.JSONUtil;

/**
 * 
 * 考勤信息取得Handler
 * 
 * @author WangCT
 * @version 1.0 2011/11/01
 */
@Service(name = "attendance")
public class AttendanceHandler {
	
//	/** 小工具各种消息取得Service **/
//	@Resource
//	private GadgetsService gadgetsService;
	
	/** 考勤信息取得Service **/
	@Resource
	private BasAttendanceService basAttendanceService;
	
	@SuppressWarnings("unchecked")
	@Operation(httpMethods = "POST", bodyParam = "data")
	public Future<DataCollection> getAttendanceInfo(RequestItem request) throws Exception {
		
		String bodyparams = request.getParameter("data");
		Map paramMap = (Map)JSONUtil.deserialize(bodyparams);
		Map userInfoMap = (Map)paramMap.get("userInfo");
		String pageNo = (String)paramMap.get("pageNo");
		int length = GadgetsConstants.PAGE_LENGTH;
		int start = (Integer.parseInt(pageNo) - 1) * length + 1;
		int end = start + length - 1;
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("SORT_ID", "OccurTime DESC");
		map.put("START", start);
		map.put("END", end);
		map.put(CherryConstants.USERID, userInfoMap.get("BIN_UserID"));
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfoMap.get("BIN_OrganizationInfoID"));
		Object brandInfoId = userInfoMap.get("BIN_BrandInfoID");
		if(brandInfoId != null && CherryConstants.BRAND_INFO_ID_VALUE != Integer.parseInt(brandInfoId.toString())) {
			map.put(CherryConstants.BRANDINFOID, userInfoMap.get("BIN_BrandInfoID"));
		}
		map.put("businessType", "0");
		map.put("operationType", "1");
		// 查询考勤信息List
		List<Map<String, Object>> attendanceList = basAttendanceService.getAttendanceList(map);
		
//		DBObject dbObject = new BasicDBObject();
//		// 业务类型
//		dbObject.put("TradeType", "CQ");
////		Calendar ca = Calendar.getInstance();
////		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
////		String sysDate = gadgetsService.getDateYMD();
////		ca.setTime(sf.parse(sysDate));
////		ca.add(Calendar.DAY_OF_MONTH, GadgetsConstants.DAY_COUNT);
////		String limitDate = sf.format(ca.getTime());
////		// 发生时间为最近3天的记录
////		dbObject.put("OccurTime", new BasicDBObject("$gte",limitDate));
//		DBObject orderBy = new BasicDBObject();
//		// 按发生时间排序
//		orderBy.put("OccurTime", -1);
//		// 查询考勤信息List
//		List<DBObject> attendanceList = gadgetsService.getInfoByMongoDBList(dbObject, orderBy, start, length);
		
		// 返回结果
		Map resultData = new HashMap();
		if(attendanceList != null && !attendanceList.isEmpty()) {
			resultData.put("attendanceMes",attendanceList);
			if(attendanceList.size() == length) {
				if(Integer.parseInt(pageNo) + 1 <= GadgetsConstants.PAGE_MAX_COUNT) {
					resultData.put("pageNo", Integer.parseInt(pageNo) + 1);
				}
			}
		}
		return ImmediateFuture.newInstance(new DataCollection(resultData));
	}

}
