/*  
 * @(#)BINOLMOWAT06_BL.java    1.0 2012-9-18     
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.mongo.domain.MQWarn;
import com.cherry.cm.mongo.domain.QMQWarn;
import com.cherry.cm.mongo.repositories.MQWarnRepository;
import com.mysema.query.BooleanBuilder;

/**
 * MQ消息错误日志查询BL
 * 
 * @author WangCT
 * @version 1.0 2012-9-18
 */
public class BINOLMOWAT06_BL {

	/** 操作MongoDB的消息警告表对象 **/
	@Resource
	private MQWarnRepository mQWarnRepository;
	
	/**
	 * 查询MQ消息错误日志信息
	 * 
	 * @param map 查询条件
	 * @return MQ消息错误日志信息
	 */
	public Map<String, Object> getMQWarnInfoList(Map<String, Object> map) {
		
		// 查询条件设置
		QMQWarn qMQWarn = QMQWarn.mQWarn;
		BooleanBuilder booleanBuilder = new BooleanBuilder();
		// 品牌代码
		String brandCode = (String)map.get("brandCode");
		if(brandCode != null && !"".equals(brandCode)) {
			booleanBuilder.and(qMQWarn.brandCode.eq(brandCode));
		} else {
			// 组织代码
			booleanBuilder.and(qMQWarn.orgCode.eq((String)map.get("orgCode")));
		}
		// 业务类型
		String tradeType = (String)map.get("tradeType");
		if(tradeType != null && !"".equals(tradeType)) {
			booleanBuilder.and(qMQWarn.tradeType.eq(tradeType));
		}
		// 单据号
		String tradeNoIF = (String)map.get("tradeNoIF");
		if(tradeNoIF != null && !"".equals(tradeNoIF)) {
			booleanBuilder.and(qMQWarn.tradeNoIF.eq(tradeNoIF));
		}
		// 消息体
		String messageBody = (String)map.get("messageBody");
		if(messageBody != null && !"".equals(messageBody)) {
			booleanBuilder.and(qMQWarn.messageBody.contains(messageBody));
		}
		// 错误信息
		String errInfo = (String)map.get("errInfo");
		if(errInfo != null && !"".equals(errInfo)) {
			booleanBuilder.and(qMQWarn.errInfo.contains(errInfo));
		}
		// 开始时间
		String putTimeStart = (String)map.get("putTimeStart");
		// 结束时间
		String putTimeEnd = (String)map.get("putTimeEnd");
		if(putTimeEnd != null && !"".equals(putTimeEnd)) {
			putTimeEnd = putTimeEnd + " 23:59:59";
		}
		if(putTimeStart != null && !"".equals(putTimeStart)) {
			if(putTimeEnd != null && !"".equals(putTimeEnd)) {
				booleanBuilder.and(qMQWarn.putTime.between(putTimeStart, putTimeEnd));
			} else {
				booleanBuilder.and(qMQWarn.putTime.goe(putTimeStart));
			}
		} else {
			if(putTimeEnd != null && !"".equals(putTimeEnd)) {
				booleanBuilder.and(qMQWarn.putTime.loe(putTimeEnd));
			}
		}
		// 错误类型
		String errType = (String)map.get("errType");
		if(errType != null && !"".equals(errType)) {
			booleanBuilder.and(qMQWarn.errType.eq(errType));
		}
		// 排序信息
		String sort = (String)map.get("SORT_ID");
		String[] sorts = sort.split(CherryConstants.SPACE);
		// 分页信息
		int start = (Integer)map.get("START");
		int size = (Integer)map.get("IDisplayLength");
		int page = (start-1)/size;
		// 查询MQ消息错误日志信息
		Page<MQWarn> result = mQWarnRepository.findAll(booleanBuilder, new PageRequest(page, size, "asc".equals(sorts[1]) ? Direction.ASC : Direction.DESC, sorts[0]));
		if(result != null && result.hasContent()) {
			Map<String, Object> resultMap = new HashMap<String, Object>();
			resultMap.put("totalCount", result.getTotalElements());
			resultMap.put("mqWarnList", result.getContent());
			return resultMap;
		} else {
			return null;
		}
	}
	
	/**
	 * 删除MQ消息错误日志信息
	 * 
	 * @param map 删除条件
	 */
	public void delMQWarnInfo(Map<String, Object> map) {
		
		// 需要删除的日志ID
		List<String> ids = (List)map.get("id");
		if(ids != null && !ids.isEmpty()) {
			List<MQWarn> mqWarnList = new ArrayList<MQWarn>();
			for(String id : ids) {
				MQWarn mqWarn = new MQWarn();
				mqWarn.setId(new ObjectId(id));
				mqWarnList.add(mqWarn);
			}
			// 删除MQ消息错误日志信息
			mQWarnRepository.delete(mqWarnList);
		}
	}
}
