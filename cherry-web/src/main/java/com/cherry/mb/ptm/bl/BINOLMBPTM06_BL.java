/*
 * @(#)BINOLMBPTM06_BL.java     1.0 2012/08/08
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
package com.cherry.mb.ptm.bl;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbussiness.bl.BINOLCM37_BL;
import com.cherry.cm.cmbussiness.interfaces.BINOLCM37_IF;
import com.cherry.cm.cmbussiness.service.BINOLCM00_Service;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CodeTable;
import com.cherry.cm.core.PropertiesUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.mb.ptm.service.BINOLMBPTM06_Service;
import com.cherry.mo.common.interfaces.BINOLMOCOM01_IF;

/**
 * 查询积分信息BL
 * 
 * @author WangCT
 * @version 1.0 2012/08/08
 */
public class BINOLMBPTM06_BL implements BINOLCM37_IF{
	
	/** 查询积分信息Service */
	@Resource
	private BINOLMBPTM06_Service binOLMBPTM06_Service;
	
	@Resource(name="binOLMOCOM01_BL")
	private BINOLMOCOM01_IF binOLMOCOM01_BL;	
	
	// Excel导出数据查询条件数组
	private final static String[] proCondition = { "startDateShow", "endDateShow" };
	
	// Excel导出列数组
	private final static String[][] proArray = {
		{ "memberCode", "BINOLMBPTM06_memberCode", "20", "", "" },// 会员卡号
		{ "memberName", "BINOLMBPTM06_memberName", "40", "", "" },// 姓名
		{ "mobile", "BINOLMBPTM06_mobile", "15", "", "" },// 手机号码
		{ "grantCardTime", "BINOLMBPTM06_grantCardTime", "20", "", "" },// 发卡时间
		{ "grantPoints", "BINOLMBPTM06_grantPoints", "15", "", "" },// 奖励积分
		{ "grantPointTime", "BINOLMBPTM06_grantPointTime", "20", "", "" },// 奖励时间 
		{ "newOldMemberFlag", "BINOLMBPTM06_newOldMemberFlag", "15", "1414", "" },// 新老会员区分
		{ "region", "BINOLMBPTM06_region", "50", "", "" },// 区		
		{ "province", "BINOLMBPTM06_province", "50", "", "" },// 省		
		{ "city", "BINOLMBPTM06_city", "50", "", "" },// 城市		
		{ "counterCode", "BINOLMBPTM06_counterCode", "15", "", "" },// 柜台编号		
		{ "counterName", "BINOLMBPTM06_counterName", "50", "", "" }// 柜台名称		
	};
	
	/**
	 * 取得积分信息List
	 * 
	 * @param map 检索条件
	 * @return 积分信息List
	 */
	public Map<String, Object> getScanQRPointsReportSummary(Map<String, Object> map) {
		
		// 取得会员信息List
		return binOLMBPTM06_Service.getScanQRPointsReportSummary(map);
	}
	
	
	/**
	 * 导出Excel处理
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public byte[] exportExcel(Map<String, Object> map) throws Exception {
		//需导出数据总量
		int dataLen = binOLMBPTM06_Service.getExportDetailCount(map);
		map.put("dataLen", dataLen);
		BINOLMOCOM01_IF.ExcelParam ep = new BINOLMOCOM01_IF.ExcelParam();
		//必须设置sqlID（必须）,用于批查询
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBPTM06.getExportDetailList");
		ep.setMap(map);
		// 导出数据列数组
		ep.setArray(proArray);
		ep.setShowTitleStyle(true);
		// 导出数据列头国际化资源文件
		ep.setBaseName("BINOLMBPTM06");
		ep.setSearchCondition(getConditionStr(map));
		return binOLMOCOM01_BL.getBatchExportExcel(ep);
	}
	
	/**
	 * 取得条件字符串
	 * 
	 * @param map
	 * @return
	 */
	private String getConditionStr(Map<String, Object> map) {
		// 防止查询相关区域、城市、部门名称时篡改ibatis_sql_id
		Map<String, Object> conditionMap = new HashMap<String, Object>();
		conditionMap.putAll(map);
		String language = ConvertUtil.getString(conditionMap
				.get(CherryConstants.SESSION_LANGUAGE));
		StringBuffer condition = new StringBuffer();
		int lineNum = 0;
		for (String con : proCondition) {
			// 条件值
			String paramValue = ConvertUtil.getString(conditionMap.get(con));
			
			if (!"".equals(paramValue)) {
				// 条件名
				String paramName = "";
				if ("startDate".equals(con)) {
					// 开始日期
					paramName = binOLMOCOM01_BL.getResourceValue(
							"BINOLMBPTM06", language, "BINOLMBPTM06_startDate");
				} else if ("endDate".equals(con)) {
					// 结束日期
					paramName = binOLMOCOM01_BL.getResourceValue(
							"BINOLMBPTM06", language, "BINOLMBPTM06_endDate");
				}
				lineNum++;
				if (lineNum % 6 == 0) {
					condition.append("\n");
				}
				condition.append(paramName + "：" + paramValue + "\0\0\0\0\0");
			}
		}

		return condition.toString();
	}
	
	/**
	 * 积分记录明细总数
	 * @param map
	 * @return
	 */
	public int getDetailCount(Map<String, Object> map) {
		return binOLMBPTM06_Service.getExportDetailCount(map);
	}

	/**
	 * 积分记录明细
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getDetailList(Map<String, Object> map)
			throws Exception {
		return binOLMBPTM06_Service.getExportDetailList(map);
	}
	@Override
	public List<Map<String, Object>> getDataList(Map<String, Object> map)
			throws Exception {
		return binOLMBPTM06_Service.getExportDetailList(map);
	}
	

}
