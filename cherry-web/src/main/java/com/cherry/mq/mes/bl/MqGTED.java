/*		
 * @(#)MqGTED.java     1.0 2016-11-30
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
package com.cherry.mq.mes.bl;

import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherrySecret;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.mq.mes.common.CherryMQException;
import com.cherry.mq.mes.common.MessageUtil;
import com.cherry.mq.mes.interfaces.MqReceiver_IF;
import com.cherry.mq.mes.service.BINBEMQMES99_Service;
import com.cherry.mq.mes.service.MqGTED_Service;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.*;

/**
 * 
 * @ClassName: MqGTED
 * @Description: TODO(经销商额度变更)
 * @author chenkuan
 * @version v1.0.0 2016-11-30
 *
 */
public class MqGTED implements MqReceiver_IF {


	@Resource(name="binBEMQMES99_Service")
	private BINBEMQMES99_Service binBEMQMES99_Service;

	@Resource(name ="mqGTED_Service")
	private MqGTED_Service MqGTED_Service;
	
	
	@Override
	public void tran_execute(Map<String, Object> map) throws Exception {
		
		// 1：校验并设置相关参数
		this.checkAndSetData(map);
		
		// 2：设置操作程序名称
		this.setUpdateInfoMapKey(map);

		// 3:往新后台写数据
		if(!CherryUtil.isEmpty(ConvertUtil.getString(map.get("PointChange")))){
			double pointChange = Double.valueOf(ConvertUtil.getString(map.get("PointChange")));
			if (pointChange !=0) {//额度为0不处理
				MqGTED_Service.updateCounterPointPlan(map);
				MqGTED_Service.insertCounterLimitInfo(map);
			}
		}
		// 4:插入MQLog日志表
		this.addMessageLog(map);
        
        // 5：插入MongoDB
        this.addMongoDBBusLog(map);
        
        // 标记当前BL已经将MQ信息写入MongoDB与MQ收发日志表
        map.put("IsInsertMongoDBBusLog", "1");
        map.put("content", "经销商额度变更");
	}
	
	/**
	 * 接收数据写入MQ收发日志表
	 * @param map
	 * @throws Exception
	 */
	private void addMessageLog(Map<String, Object> map) throws Exception {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("organizationInfoID", map.get("BIN_OrganizationInfoID"));
		paramMap.put("brandInfoID", map.get("BIN_BrandInfoID"));
		paramMap.put("tradeType", map.get("TradeType"));
		paramMap.put("tradeNoIF", map.get("TradeNoIF"));
		paramMap.put("modifyCounts", "0");
		paramMap.put("counterCode", map.get("DepartCode"));
		paramMap.put("isPromotionFlag",0);//表示非促销品
		paramMap.put("createdBy", "MqGTED");
		paramMap.put("createPGM", "MqGTED");
		paramMap.put("updatedBy", "MqGTED");
		paramMap.put("updatePGM", "MqGTED");
		// 插入MQ日志表（数据库SqlService）
        binBEMQMES99_Service.addMessageLog(paramMap);
	}
	
	
	/**
	 * 接收数据写入MongoDB
	 * @param map
	 * @throws Exception
	 */
	private void addMongoDBBusLog(Map<String, Object> map) throws CherryMQException {
		// 插入MongoDB
        DBObject dbObject = new BasicDBObject();
		// 组织代码
		dbObject.put("OrgCode", map.get("OrgCode"));
		// 品牌代码，即品牌简称
		dbObject.put("BrandCode", map.get("BrandCode"));
		// 业务类型
		dbObject.put("TradeType", map.get("TradeType"));
		// 单据号
		dbObject.put("TradeNoIF", map.get("TradeNoIF"));
		// 修改次数
		dbObject.put("ModifyCounts", "0");
		// 业务主体
	    dbObject.put("TradeEntity", "1");
	    // 柜台号
	    dbObject.put("CounterCode", map.get("DepartCode"));
		//员工代码
//		dbObject.put("UserCode", map.get("employeeCode"));
		// 发生时间
		dbObject.put("OccurTime", (String)map.get("TradeTime"));
        // 日志正文
		dbObject.put("Content", "经销商额度变更");
		// 
		map.put("addMongoDBFlag", "0");
	    binBEMQMES99_Service.addMongoDBBusLog(dbObject);
	    map.put("addMongoDBFlag", "1");
		
	}

	/**
	 * 设置操作程序名称
	 * @param map
	 */
	private void setUpdateInfoMapKey(Map<String, Object> map) {
		map.put("createdBy", "MqGTED");
		map.put("createPGM", "MqGTED");
		map.put("updatedBy", "MqGTED");
		map.put("updatePGM", "MqGTED");
	}
	
	
	
	/**
	 * 校验并设置相关参数
	 * @param map
	 * @throws Exception
	 */
	private void checkAndSetData(Map<String, Object> map) throws Exception {

		//1:验证柜台信息
		if(!CherryUtil.isEmpty(String.valueOf(map.get("DepartCode")))){
			Map<String, Object> counterInfo = MqGTED_Service.getCounterByCode(map);//获取柜台信息
			if (null == counterInfo || counterInfo.isEmpty()) {
				MessageUtil.addMessageWarning(map, "柜台编号为\"" + map.get("DepartCode") + "\"" + "没有查询到相关柜台信息");
			} else {
				// 设定柜台组织id
				map.put("BIN_OrganizationID", counterInfo.get("BIN_OrganizationID"));
				// 用后即焚
				counterInfo.clear();
				counterInfo = null;
			}
		}

		//2: 验证会员持卡信息
		if(!CherryUtil.isEmpty(String.valueOf(map.get("MemberCode")))){
			Map<String, Object> memCardInfo = MqGTED_Service.getMemCardInfo(map);//获取会员持卡信息
			if (null == memCardInfo || memCardInfo.isEmpty()) {
				MessageUtil.addMessageWarning(map, "会员卡号为\"" + map.get("MemberCode") + "\"" + "没有查询到相关会员信息");
			} else {
				// 设定持卡会员id
				map.put("BIN_MemberInfoID", memCardInfo.get("BIN_MemberInfoID"));
				// 用后即焚
				memCardInfo.clear();
				memCardInfo = null;
			}
		}

	}

		
}
