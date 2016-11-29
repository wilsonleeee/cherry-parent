/*	
 * @(#)BINBEMQDRL01_BL.java     1.0 2011/8/18		
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

import com.cherry.dr.cmbussiness.interfaces.RuleHandler_IF;
import com.cherry.mq.mes.common.MessageConstants;
import com.cherry.mq.mes.common.MessageUtil;
import com.cherry.mq.mes.interfaces.CherryMessageHandler_IF;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import com.dianping.cat.Cat;
import com.dianping.cat.message.Transaction;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 处理规则消息 BL
 * 
 * @author hub
 * @version 1.0 2011.8.18
 */
public class BINBEMQMES07_BL implements CherryMessageHandler_IF{
	
	/** 管理MQ消息处理器和规则计算处理器共通 BL **/
	@Resource
	private BINBEMQMES98_BL binBEMQMES98_BL;
	
	
	/**
	 * 接收MQ消息处理
	 * 
	 * @param map 消息信息
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void handleMessage(Map<String, Object> map) throws Exception {
		
		List<Map<String, Object>> list = (List)map.get("detailDataDTOList");
		if(list != null && !list.isEmpty()) {
			String orgCode = (String)map.get("orgCode");
			String brandCode = (String)map.get("brandCode");
			List<Map<String, Object>> tmSyncInfoList = new ArrayList<Map<String, Object>>();
			for(int i = 0; i < list.size(); i++) {
				Map<String, Object> _map = list.get(i);
				String billType = (String)_map.get("tradeType");
				// 取得处理器
				RuleHandler_IF ruleHandlerIF = binBEMQMES98_BL.getHandler(orgCode, brandCode, billType);
				// 如果是销售数据
				if (null != ruleHandlerIF) {
					// 规则执行用
					Map<String, Object> ruleMap = new HashMap<String, Object>();
					ruleMap.put("brandCode", map.get("brandCode"));
					ruleMap.putAll(_map);
					Transaction transaction = Cat.newTransaction("BINBEMQMES07_BL", "handleMessage");
					try {
						// 执行会员等级和化妆次数规则文件
						ruleHandlerIF.executeRule(ruleMap);
						transaction.setStatus(Transaction.SUCCESS);
					} catch (Exception e) {
						transaction.setStatus(e);
						Cat.logError(e);
						throw e;
					} finally {
						transaction.complete();
					}
					if (ruleMap.containsKey("pointRuleCalInfo")) {
						map.put("pointRuleCalInfo", ruleMap.get("pointRuleCalInfo"));
					}
					if (ruleMap.containsKey("TmSyncInfo")) {
						tmSyncInfoList.add((Map<String, Object>) ruleMap.get("TmSyncInfo"));
					}
				} else {
					// 没有查询到相关的规则处理器
					MessageUtil.addMessageWarning(map,MessageConstants.MSG_ERROR_54);
				}
			}
			if (!tmSyncInfoList.isEmpty()) {
				map.put("TmSyncInfoList", tmSyncInfoList);
			}
		} else {
			// 没有查询到相关的规则处理器
			MessageUtil.addMessageWarning(map,MessageConstants.MSG_ERROR_55);
		}
		DBObject dbObject = new BasicDBObject();
		// 组织代号
		dbObject.put("OrgCode", map.get("orgCode"));
		// 品牌代码
		dbObject.put("BrandCode", map.get("brandCode"));
		// 业务类型
		dbObject.put("TradeType", map.get("tradeType"));
		// 单据号
		dbObject.put("TradeNoIF", map.get("tradeNoIF"));
		// 修改回数
		dbObject.put("ModifyCounts", map.get("modifyCounts"));
		// 业务主体
		dbObject.put("TradeEntity", "0");
		// 业务主体代号
		dbObject.put("TradeEntityCode", list.get(0).get("memberCode"));
		// 关联单据号
		dbObject.put("RelBillCode", list.get(0).get("tradeNoIF"));
		// 消息体
		dbObject.put("Content", map.get("messageBody"));
		map.put("dbObject", dbObject);
	}

}
