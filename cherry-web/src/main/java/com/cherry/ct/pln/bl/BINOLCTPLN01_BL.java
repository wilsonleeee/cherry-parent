/*
 * @(#)BINOLCTTPL01_Action.java     1.0 2012/11/06
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
package com.cherry.ct.pln.bl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.ct.common.service.BINOLCTCOM03_Service;
import com.cherry.ct.pln.interfaces.BINOLCTPLN01_IF;
import com.cherry.ct.pln.service.BINOLCTPLN01_Service;


/**
 * 沟通计划一览BL
 * 
 * @author ZhangGS
 * @version 1.0 2012.12.06
 */
public class BINOLCTPLN01_BL implements BINOLCTPLN01_IF{
	@Resource
	private BINOLCTPLN01_Service binolctpln01_Service;
	
	@Resource
	private BINOLCTCOM03_Service binOLCTCOM03_Service;
	
	@Override
	public List<Map<String, Object>> getCommunicationPlanList(Map<String, Object> map) throws Exception{
		// 获取沟通模板List
		List<Map<String, Object>> templateList = binolctpln01_Service.getPlanList(map);
		return templateList;
	}
	
	@Override
	public int getCommunicationPlanCount(Map<String, Object> map){
		// 获取沟通模板数量
		return binolctpln01_Service.getPlanCount(map);
	}
	
	public void tran_stopCommunicationPlan(Map<String, Object> map){
		// 停用沟通计划的活动关联信息
		binolctpln01_Service.stopCommPlanActivityInfo(map);
		// 停用沟通计划
		binolctpln01_Service.stopCommPlan(map);
		// 停用沟通
		binOLCTCOM03_Service.stopCommInfo(map);
		// 停用沟通设置
		binolctpln01_Service.stopCommSet(map);
		// 删除不在运行的调度信息
		binOLCTCOM03_Service.deleteSchedulesInfo(map);
		// 将正在运行的调度信息置为无效
		binOLCTCOM03_Service.updateSchedulesFlag(map);
	}
}
