/*  
 * @(#)BINOLMOWAT08_BL.java    1.0 2014-10-28   
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
package com.cherry.mo.wat.bl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbussiness.bl.BINOLCM05_BL;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.mongo.MongoDB;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.DateUtil;
import com.cherry.mo.wat.interfaces.BINOLMOWAT08_IF;
import com.cherry.mq.mes.common.MessageConstants;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

/**
 * 终端消息反馈日志查询BL
 * 
 * @author menghao
 * @version 1.0 2014-10-28
 */
public class BINOLMOWAT08_BL implements BINOLMOWAT08_IF{
	
	@Resource(name="binOLCM05_BL")
	private BINOLCM05_BL binOLCM05_BL;
	
	@Override
	public int getMQNoticeInfoCount(Map<String, Object> map) throws Exception {
		// 查询条件
		DBObject condition = getCondition(map);
		int count = MongoDB.findCount(MessageConstants.MQ_MGO_MQNOTICELOG_NAME, condition);
		return count;
	}

	/**
	 * 查询终端消息反馈消息LIST
	 * 
	 * @param map 查询条件
	 * @return MQ消息错误日志信息
	 * @throws Exception 
	 */
	@Override
	public List<DBObject> getMQNoticeInfoList(Map<String, Object> map) throws Exception {
		// 查询条件
		DBObject condition = getCondition(map);
		//查询结果字段
        DBObject keys = new BasicDBObject();
        keys.put("messageBody", 1);
        keys.put("content", 1);
        keys.put("time", 1);
        keys.put("subType", 1);
        keys.put("errorCode", 1);
		
		// 排序字段 =1 升序，=-1 降序
        DBObject orderBy = new BasicDBObject();
        String sortID = ConvertUtil.getString(map.get("SORT_ID"));
        if(!"".equals(sortID)) {
        	String[] sortIDs = sortID.split(CherryConstants.SPACE);
        	// 默认是升序
        	if(sortIDs.length > 1) {
        		orderBy.put(sortIDs[0], sortIDs[1].toUpperCase().equals("DESC") ? -1 : 1);
        	} else {
        		orderBy.put(sortIDs[0], 1);
        	}
        }
        // 跳过的数量
        int skip = ConvertUtil.getInt(map.get("START"))-1;
        int limit = ConvertUtil.getInt(map.get("IDisplayLength"));
        List<DBObject> list = MongoDB.find(MessageConstants.MQ_MGO_MQNOTICELOG_NAME, condition, keys, orderBy, skip, limit);
		return list;
	}
	
	/**
	 * 页面查询条件转化为MongoDB查询条件
	 * @param map
	 * @return
	 */
	private DBObject getCondition(Map<String,Object> map){
		DBObject condition= new BasicDBObject();
		DBObject timeCond= new BasicDBObject();
		// 所属品牌CODE
		String brandCode = binOLCM05_BL.getBrandCode(ConvertUtil.getInt(map.get(CherryConstants.BRANDINFOID)));
		// 开始时间
		String timeSatrt = ConvertUtil.getString(map.get("timeStart"));
		// 结束时间
		String timeEnd = ConvertUtil.getString(map.get("timeEnd"));
		// 消息类型
		String subType = ConvertUtil.getString(map.get("subType"));
		// 错误码
		String errorCode = ConvertUtil.getString(map.get("errorCode"));
		// 活动码
		String code = ConvertUtil.getString(map.get("code"));
		
		if(null != brandCode && !"".equals(brandCode)) {
			// MongoDB中OR的查询参数
			List<DBObject> dbObjectList = new ArrayList<DBObject>();
			DBObject dbObject1 = new BasicDBObject();
			dbObject1.put("brandCode", brandCode);
			DBObject dbObject2 = new BasicDBObject();
			dbObject2.put("brandCode", "ALL");
			dbObjectList.add(dbObject1);
			dbObjectList.add(dbObject2);
			condition.put("$or",dbObjectList);
		}
		if(!"".equals(timeSatrt)) {
			timeCond.put("$gte", timeSatrt+" 00:00:00");
		}
		if(!"".equals(timeEnd)) {
			timeCond.put("$lte", timeEnd+" 23:59:59");
		}
		// 有时间段条件时才填写该查询条件
		if(!"".equals(timeSatrt) || !"".equals(timeEnd)) {
			condition.put("time", timeCond);
		}
		
		if(!"".equals(subType)) {
			condition.put("subType", subType);
		}
		
		if(!"".equals(errorCode)) {
			condition.put("errorCode", errorCode);
		}
		
		if(!"".equals(code)) {
			condition.put("code", code);
		}
		
		return condition;
	}

}
