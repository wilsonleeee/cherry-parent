/*
 * @(#)BINOLMBPTM04_Action.java     1.0 2013/06/03
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

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.core.CodeTable;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.mb.ptm.service.BINOLMBPTM04_Service;
import com.cherry.mo.common.interfaces.BINOLMOCOM01_IF;

/**
 * 积分导入结果查询BL
 * 
 * @author LUOHONG
 * @version 1.0 2013/06/03
 */
public class BINOLMBPTM04_BL {
	
	/** 积分Excel导入Service */
	@Resource
	private BINOLMBPTM04_Service binOLMBPTM04_Service;
	
	@Resource
	private BINOLMOCOM01_IF binOLMOCOM01_BL;
	
	@Resource
	private CodeTable codeTable;
	/**
	 * 会员活动List
	 * 
	 * @param map 
	 * @return List
	 */
	public List<Map<String, Object>> getPointList(Map<String, Object> map) {
		
		// 会员主题活动List
		return binOLMBPTM04_Service.getPointList(map);
	}
	/**
	 * 会员主题活动数
	 * 
	 * @param map
	 * @return
	 */
	public int getPointCount(Map<String, Object> map) {
		// 会员主题活动数
		return binOLMBPTM04_Service.getPointCount(map);
	}
	/**
	 * 会员活动List
	 * 
	 * @param map 
	 * @return List
	 */
	public List<Map<String, Object>> getPointDetailList(Map<String, Object> map) {
		
		// 会员主题活动List
		return binOLMBPTM04_Service.getPointDetailList(map);
	}
	/**
	 * 会员主题活动数
	 * 
	 * @param map
	 * @return
	 */
	public int getPointDetailCount(Map<String, Object> map) {
		// 会员主题活动数
		return binOLMBPTM04_Service.getPointDetailCount(map);
	}
	
	/**
     * 取得导入结果数量
     */
    public Map getSumInfo(Map<String,Object> map){
        return binOLMBPTM04_Service.getSumInfo(map);
    }
    
	// Excel导出数据查询条件数组
	private final static String[] pointCondition = {"pointBillNo" , "memCode","resultFlag" };
	// Excel一览导出列数组
	private final static String[][] pointArray = {
			// 1
			{ "memCode", "binolmbptm04_memCode", "20", "", "" },
			// 2
			{ "memName", "binolmbptm04_memName", "20", "", "" },
			// 3
			{ "mobilePhone", "binolmbptm04_mobilePhone", "15", "", "" },
			// 4
			{ "point", "binolmbptm04_point", "12", "right", "" },
			// 5
			{ "pointTime", "binolmbptm04_pointTime", "25", "left", "" },
			// 6
			{ "resultFlag", "binolmbptm04_resultFlag", "20", "", "1250" },
			// 7
			{ "importResults", "binolmbptm04_common", "100", "", "" }

	};

	/**
	 * Excel导出一览
	 * 
	 * @param map
	 * @return 返回导出信息
	 * @throws CherryException
	 */
	public byte[] exportExcel(Map<String, Object> map) throws Exception {
		int dataLen = binOLMBPTM04_Service.getPointDetailCount(map);
		map.put("dataLen", dataLen);
		BINOLMOCOM01_IF.ExcelParam ep = new BINOLMOCOM01_IF.ExcelParam();
		// 排序字段(积分时间)
		map.put(CherryConstants.SORT_ID, "PointTime");
		//必须设置sqlID（必须）,用于批查询
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBPTM04.getPointDetailList");
		ep.setMap(map);
		// 导出数据列数组
		ep.setArray(pointArray);
		// 导出数据列头国际化资源文件
		ep.setBaseName("BINOLMBPTM04");
		ep.setShowTitleStyle(true);
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
		String language = ConvertUtil.getString(map
				.get(CherryConstants.SESSION_LANGUAGE));
		StringBuffer condition = new StringBuffer();
		for (String con : pointCondition) {
			// 条件值
			String paramValue = ConvertUtil.getString(map.get(con));
			if (!"".equals(paramValue)) {
				// 条件名
				String paramName = "";
				if ("pointBillNo".equals(con)) {
					// 流水号
					paramName = binOLMOCOM01_BL.getResourceValue(
							"BINOLMBPTM04", language, "binolmbptm04_pointImportNo");
				}else if ("memCode".equals(con)) {
					// 会员卡号
					paramName = binOLMOCOM01_BL.getResourceValue(
							"BINOLMBPTM04", language, "binolmbptm04_memCode");
				}else if ("resultFlag".equals(con)) {
					// 导入结果
					paramName = binOLMOCOM01_BL.getResourceValue(
							"BINOLMBPTM04", language, "binolmbptm04_importRes");
					paramValue = codeTable.getVal("1250", paramValue);
				}
				condition.append(paramName + "：" + paramValue + "\0\0\0\0\0");
			}
		}
		return condition.toString().trim();
	}
}
