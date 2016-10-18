/*
 * @(#)BINOLMBPTM02_BL.java     1.0 2012/08/08
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
import com.cherry.mb.ptm.service.BINOLMBPTM02_Service;
import com.cherry.mo.common.interfaces.BINOLMOCOM01_IF;

/**
 * 查询积分信息BL
 * 
 * @author WangCT
 * @version 1.0 2012/08/08
 */
public class BINOLMBPTM02_BL implements BINOLCM37_IF{
	
	/** 查询积分信息Service */
	@Resource
	private BINOLMBPTM02_Service binOLMBPTM02_Service;
	
	@Resource(name="binOLMOCOM01_BL")
	private BINOLMOCOM01_IF binOLMOCOM01_BL;
	
	@Resource(name="binOLCM00_Service")
	private BINOLCM00_Service binOLCM00_Service;
	
	@Resource(name="CodeTable")
	private CodeTable codeTable;
	
	@Resource(name="binOLCM37_BL")
	private BINOLCM37_BL binOLCM37_BL;
	
	// Excel导出数据查询条件数组
	private final static String[] proCondition = { "memCode", "mobilePhone",
			"departCode", "nameTotal", "billCode", "changeDateStartShow",
			"changeDateEndShow", "memPointStart", "memPointEnd", "tradeType",
			"subCampaignId", "relevantSRCode" };
	
	// Excel导出列数组
	private final static String[][] proArray = {
			{ "billCode", "binolmbptm02_billCode", "40", "", "" },// 单据号
//			{ "relevantSRCode", "binolmbptm02_relevantSRCode", "40", "", "" },// 退货关联单据号
			{ "billType", "binolmbptm02_tradeType", "15", "", "1213" },// 业务类型
			{ "departCodeOnly", "binolmbptm02_departCode", "15", "", "" },// 柜台号
			{ "departNameOnly", "binolmbptm02_departName", "20", "", "" },// 柜台名称
			{ "employeeCode", "binolmbptm02_employeeCode", "15", "", "" },// 操作人员号
			{ "employeeName", "binolmbptm02_employeeName", "15", "", "" },// 操作人员名称 
			{ "memCodeOnly", "binolmbptm02_memCode", "20", "", "" },// 会员卡号
			{ "memNameOnly", "binolmbptm02_memName", "15", "", "" },// 会员名称
			{ "unitCode", "binolmbptm02_unitCode", "15", "", "" },// 厂商编码
			{ "barCode", "binolmbptm02_barCode", "15", "", "" },// 产品条码
			{ "productName", "binolmbptm02_prtName", "20", "", "" },// 产品名称
			{ "saleType", "binolmbptm02_saleType", "", "", "1106" }, // 销售类型
			{ "quantity", "binolmbptm02_quantity", "", "", "" },// 数量
			{ "point", "binolmbptm02_point", "", "", "" },// 积分
			{ "pointType", "binolmbptm02_pointType", "15", "", "1214" },// 积分类型
			{ "combCampaignName", "binolmbptm02_combCampaignName", "15", "", "" },// 组合规则
			{ "mainCampaignName", "binolmbptm02_mainCampaignName", "15", "", "" },// 宿主规则
			{ "subCampaignName", "binolmbptm02_subCampaignName", "15", "", "" },// 附属规则
			{ "reason", "binolmbptm02_reason", "25", "", "" }, // 理由
			{ "changeDate", "binolmbptm02_changeDate", "25", "", "" } // 单据日期
	};
	/**
	 * 取得积分信息总数
	 * 
	 * @param map 检索条件
	 * @return 积分信息总数
	 */
	public int getPointInfoCount(Map<String, Object> map) {
		
		// 取得会员总数
		return binOLMBPTM02_Service.getPointInfoCount(map);
	}
	
	/**
	 * 取得积分信息List
	 * 
	 * @param map 检索条件
	 * @return 积分信息List
	 */
	public List<Map<String, Object>> getPointInfoList(Map<String, Object> map) {
		
		// 取得会员信息List
		return binOLMBPTM02_Service.getPointInfoList(map);
	}
	
	/**
	 * 取得积分规则信息List
	 * 
	 * @param map 检索条件
	 * @return 积分规则信息List
	 */
	public List<Map<String, Object>> getCampaignNameList(Map<String, Object> map) {
		
		// 取得积分规则信息List
		return binOLMBPTM02_Service.getCampaignNameList(map);
	}
	
	/**
	 * 导出Excel处理
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public byte[] exportExcel(Map<String, Object> map) throws Exception {
		//需导出数据总量
		int dataLen = binOLMBPTM02_Service.getExportDetailCount(map);
		map.put("dataLen", dataLen);
		BINOLMOCOM01_IF.ExcelParam ep = new BINOLMOCOM01_IF.ExcelParam();
		//必须设置sqlID（必须）,用于批查询
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBPTM02.getExportDetailList");
		ep.setMap(map);
		// 导出数据列数组
		ep.setArray(proArray);
		ep.setShowTitleStyle(true);
		// 导出数据列头国际化资源文件
		ep.setBaseName("BINOLMBPTM02");
		ep.setSearchCondition(getConditionStr(map));
		return binOLMOCOM01_BL.getBatchExportExcel(ep);
	}
	
	/**
	 * 导出CSV处理
	 */
	public String exportCSV(Map<String, Object> map) throws Exception {
		// 获取导出参数
		Map<String, Object> exportMap = this.getExportParam(map);
		
		String language = (String)map.get(CherryConstants.SESSION_CHERRY_LANGUAGE);
		
    	String tempFilePath = PropertiesUtil.pps.getProperty("tempFilePath");
        String sessionId = (String)map.get("sessionId");
        // 下载文件临时目录
        tempFilePath = tempFilePath + File.separator + sessionId;
        exportMap.put("tempFilePath", tempFilePath);
        
        // 下载文件名
        String downloadFileName = binOLCM37_BL.getResourceValue("BINOLMBPTM02", language, "binolmbptm02_exportName");
        exportMap.put("tempFileName", downloadFileName);
        
        // 导出CSV处理
        boolean result = binOLCM37_BL.exportCSV(exportMap, this);
        if(result) {
        	// 压缩包名
        	String zipName = downloadFileName+".zip";
        	// 压缩文件处理
        	result = binOLCM37_BL.fileCompression(new File(tempFilePath+File.separator+downloadFileName+".csv"), zipName);
        	if(result) {
        		return tempFilePath+File.separator+zipName;
        	}
        }
		return null;
	}
	
	/**
	 * CSV导出参数
	 * @param map
	 * @return
	 */
	private Map<String, Object> getExportParam(Map<String, Object> map) {
		Map<String, Object> exportMap = new HashMap<String, Object>();
		exportMap.put(CherryConstants.ORGANIZATIONINFOID, map.get(CherryConstants.ORGANIZATIONINFOID));
		exportMap.put(CherryConstants.BRANDINFOID, map.get(CherryConstants.BRANDINFOID));
		String language = (String)map.get(CherryConstants.SESSION_CHERRY_LANGUAGE);
		exportMap.put("charset", map.get("charset"));
		// 把搜索条件转换为文字说明
		String conditionContent = this.getConditionStr(map);
		if(conditionContent != null && !"".equals(conditionContent)) {
			exportMap.put("header", conditionContent);
		}
		
		exportMap.put("conditionMap", map);
		
		exportMap.put("sheetName", binOLCM37_BL.getResourceValue("BINOLMBPTM02", language, "binolmbptm02_exportName"));
		String[][] titleRows = {
				{ "billCode", binOLCM37_BL.getResourceValue("BINOLMBPTM02", language, "binolmbptm02_billCode"), "40", "", "" },// 单据号
//				{ "relevantSRCode", binOLCM37_BL.getResourceValue("BINOLMBPTM02", language, "binolmbptm02_relevantSRCode"), "40", "", "" },// 关联单号
				{ "billType", binOLCM37_BL.getResourceValue("BINOLMBPTM02", language,"binolmbptm02_tradeType"), "15", "", "1213" },// 业务类型
				{ "departCodeOnly", binOLCM37_BL.getResourceValue("BINOLMBPTM02", language,"binolmbptm02_departCode"), "15", "", "" },// 柜台号
				{ "departNameOnly", binOLCM37_BL.getResourceValue("BINOLMBPTM02", language,"binolmbptm02_departName"), "20", "", "" },// 柜台名称
				{ "employeeCode", binOLCM37_BL.getResourceValue("BINOLMBPTM02", language,"binolmbptm02_employeeCode"), "15", "", "" },// 操作人员号
				{ "employeeName", binOLCM37_BL.getResourceValue("BINOLMBPTM02", language,"binolmbptm02_employeeName"), "15", "", "" },// 操作人员名称 
				{ "memCodeOnly", binOLCM37_BL.getResourceValue("BINOLMBPTM02", language,"binolmbptm02_memCode"), "20", "", "" },// 会员卡号
				{ "memNameOnly", binOLCM37_BL.getResourceValue("BINOLMBPTM02", language,"binolmbptm02_memName"), "15", "", "" },// 会员名称
				{ "unitCode", binOLCM37_BL.getResourceValue("BINOLMBPTM02", language,"binolmbptm02_unitCode"), "15", "", "" },// 厂商编码
				{ "barCode", binOLCM37_BL.getResourceValue("BINOLMBPTM02", language,"binolmbptm02_barCode"), "15", "", "" },// 产品条码
				{ "productName", binOLCM37_BL.getResourceValue("BINOLMBPTM02", language,"binolmbptm02_prtName"), "20", "", "" },// 产品名称
				{ "saleType", binOLCM37_BL.getResourceValue("BINOLMBPTM02", language,"binolmbptm02_saleType"), "", "", "1106" }, // 销售类型
				{ "quantity", binOLCM37_BL.getResourceValue("BINOLMBPTM02", language,"binolmbptm02_quantity"), "", "", "" },// 数量
				{ "point", binOLCM37_BL.getResourceValue("BINOLMBPTM02", language,"binolmbptm02_point"), "", "", "" },// 积分
				{ "pointType", binOLCM37_BL.getResourceValue("BINOLMBPTM02", language,"binolmbptm02_pointType"), "15", "", "1214" },// 积分类型
				{ "combCampaignName", binOLCM37_BL.getResourceValue("BINOLMBPTM02", language,"binolmbptm02_combCampaignName"), "15", "", "" },// 组合规则
				{ "mainCampaignName", binOLCM37_BL.getResourceValue("BINOLMBPTM02", language,"binolmbptm02_mainCampaignName"), "15", "", "" },// 宿主规则
				{ "subCampaignName", binOLCM37_BL.getResourceValue("BINOLMBPTM02", language,"binolmbptm02_subCampaignName"), "15", "", "" },// 附属规则
				{ "reason", binOLCM37_BL.getResourceValue("BINOLMBPTM02", language,"binolmbptm02_reason"), "25", "", "" }, // 理由
				{ "changeDate", binOLCM37_BL.getResourceValue("BINOLMBPTM02", language,"binolmbptm02_changeDate"), "25", "", "" }, // 单据日期
		};
		exportMap.put("titleRows", titleRows);
		
		return exportMap;
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
				if ("billCode".equals(con)) {
					// 销售单据
					paramName = binOLMOCOM01_BL.getResourceValue(
							"BINOLMBPTM02", language, "binolmbptm02_billCode");
				} else if ("memCode".equals(con)) {
					// 会员卡号
					paramName = binOLMOCOM01_BL.getResourceValue(
							"BINOLMBPTM02", language, "binolmbptm02_memCode");
				} else if ("mobilePhone".equals(con)) {
					// 会员手机号
					paramName = binOLMOCOM01_BL.getResourceValue(
							"BINOLMBPTM02", language, "binolmbptm02_mobilePhone");
				} else if ("departCode".equals(con)) {
					// 柜台号
					paramName = binOLMOCOM01_BL.getResourceValue(
							"BINOLMBPTM02", language, "binolmbptm02_departCode");
				} else if ("changeDateStartShow".equals(con)) {
					// 单据开始日期
					paramName = binOLMOCOM01_BL.getResourceValue(
							"BINOLMBPTM02", language, "binolmbptm02_changeDateStart");
				} else if ("changeDateEndShow".equals(con)) {
					// 单据结束日期
					paramName = binOLMOCOM01_BL.getResourceValue(
							"BINOLMBPTM02", language, "binolmbptm02_changeDateEnd");
				} else if ("nameTotal".equals(con)) {
					// 产品名称
					paramName = binOLMOCOM01_BL.getResourceValue(
							"BINOLMBPTM02", language, "binolmbptm02_prtName");
				} else if ("memPointStart".equals(con)) {
					// 会员积分下限
					paramName = binOLMOCOM01_BL.getResourceValue(
							"BINOLMBPTM02", language, "binolmbptm02_memPointStart");
				} else if ("memPointEnd".equals(con)) {
					// 会员积分上限
					paramName = binOLMOCOM01_BL.getResourceValue(
							"BINOLMBPTM02", language, "binolmbptm02_memPointEnd");
				} else if ("tradeType".equals(con)) {
					// 业务类型
					paramName = binOLMOCOM01_BL.getResourceValue(
							"BINOLMBPTM02", language, "binolmbptm02_tradeType");
					paramValue = codeTable.getVal("1213", paramValue);
				} else if ("subCampaignId".equals(con)) {
					// 积分规则
					paramName = binOLMOCOM01_BL.getResourceValue(
							"BINOLMBPTM02", language, "binolmbptm02_campaignName");
					List<Map<String, Object>> campaignNameList = this.getCampaignNameList(map);
					if(null != campaignNameList && campaignNameList.size() != 0) {
						paramValue = ConvertUtil.getString(campaignNameList.get(0).get("campaignName"));
					}
				} else if ("relevantSRCode".equals(con)) {
					// 关联退货单据号
					paramName = binOLMOCOM01_BL.getResourceValue(
							"BINOLMBPTM02", language, "binolmbptm02_relevantSRCode");
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
	public int getExportDetailCount(Map<String, Object> map) {
		return binOLMBPTM02_Service.getExportDetailCount(map);
	}

	@Override
	public List<Map<String, Object>> getDataList(Map<String, Object> map)
			throws Exception {
		// TODO Auto-generated method stub
		return binOLMBPTM02_Service.getExportDetailList(map);
	}
	

}
