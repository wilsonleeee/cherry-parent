/*
 * @(#)BINOLPTRPS28_BL.java     1.0 2013/08/12
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
package com.cherry.pt.rps.bl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbussiness.interfaces.BINOLCM37_IF;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.DateUtil;
import com.cherry.pt.rps.interfaces.BINOLPTRPS28_IF;
import com.cherry.pt.rps.service.BINOLPTRPS28_Service;

/**
 * 
 * 销售完成进度报表BL
 * 
 * @author WangCT
 * @version 1.0 2013/08/12
 */
public class BINOLPTRPS28_BL implements BINOLPTRPS28_IF, BINOLCM37_IF {
	
	/** 销售完成进度报表Service **/
	@Resource
	private BINOLPTRPS28_Service binOLPTRPS28_Service;

	/**
	 * 查询销售完成进度报表信息总件数
	 * 
	 * @param map 查询条件
	 * @return 销售完成进度报表信息总件数
	 */
	@Override
	public int getSaleTargetRptCount(Map<String, Object> map) {
		
		// 查询销售完成进度报表信息总件数
		return binOLPTRPS28_Service.getSaleTargetRptCount(map);
	}

	/**
	 * 查询销售完成进度报表信息List
	 * 
	 * @param map 查询条件
	 * @return 销售完成进度报表信息List
	 */
	@Override
	public List<Map<String, Object>> getSaleTargetRptList(
			Map<String, Object> map) {
		
		// 取得业务日期
		String bussinessDate = binOLPTRPS28_Service.getBussinessDate(map);
		String minDateValue = (String)map.get("minDateValue");
		String maxDateValue = (String)map.get("maxDateValue");
		boolean datePercentFlag = false;
		double datePercent = 0;
		double predictPer = 0;
		if(DateUtil.compareDate(maxDateValue, bussinessDate) >= 0 
				&& DateUtil.compareDate(minDateValue, bussinessDate) <= 0) {
			int dayNum = DateUtil.getIntervalDays(DateUtil.coverString2Date(minDateValue), DateUtil.coverString2Date(bussinessDate))+1;
			int dayCount = DateUtil.getIntervalDays(DateUtil.coverString2Date(minDateValue), DateUtil.coverString2Date(maxDateValue))+1;
			datePercent = CherryUtil.div(dayNum*100, dayCount, 2);
			predictPer = CherryUtil.div(dayNum, dayCount, 4);
			datePercentFlag = true;
		}
		
		// 查询销售完成进度报表信息List
		List<Map<String, Object>> saleTargetRptList = binOLPTRPS28_Service.getSaleTargetRptList(map);
		if(saleTargetRptList != null && !saleTargetRptList.isEmpty()) {
			for(Map<String, Object> saleTargetRptMap : saleTargetRptList) {
				Object amount = saleTargetRptMap.get("amount");
				Object targetMoney = saleTargetRptMap.get("targetMoney");
				if(targetMoney != null && !"".equals(targetMoney.toString())) {
					if(Double.parseDouble(targetMoney.toString()) != 0) {
						double percent = CherryUtil.div(Double.parseDouble(amount.toString())*100, Double.parseDouble(targetMoney.toString()), 2);
						saleTargetRptMap.put("percent", Double.toString(percent)+"%");
					}
				}
				if(datePercentFlag) {
					saleTargetRptMap.put("datePercent", Double.toString(datePercent)+"%");
					if(predictPer != 0) {
						saleTargetRptMap.put("predict", CherryUtil.div(Double.parseDouble(amount.toString()), predictPer, 2));
					}
				}
			}
		}
		return saleTargetRptList;
	}

	/**
	 * 取得需要转成Excel文件的数据List
	 * @param map 查询条件
	 * @return 需要转成Excel文件的数据List
	 */
	@Override
	public List<Map<String, Object>> getDataList(Map<String, Object> map)
			throws Exception {
		
		// 查询销售完成进度报表信息List
		return this.getSaleTargetRptList(map);
	}

}
