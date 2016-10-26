/*	
 * @(#)BINOLPTRPS46_BL.java     1.0 2016/10/18		
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

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbussiness.bl.BINOLCM37_BL;
import com.cherry.cm.cmbussiness.interfaces.BINOLCM37_IF;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.PropertiesUtil;
import com.cherry.mo.common.interfaces.BINOLMOCOM01_IF;
import com.cherry.pt.rps.interfaces.BINOLPTRPS46_IF;
import com.cherry.pt.rps.service.BINOLPTRPS46_Service;
/**
 * 产品加权平均价查询 BINOLPTRPS46_BL
 * @author lianmh
 * @version 1.0 2016/10/18
 * 
 * */
public class BINOLPTRPS46_BL implements BINOLPTRPS46_IF {
	
	@Resource(name="binOLMOCOM01_BL")
	private BINOLMOCOM01_IF binOLMOCOM01_BL;
	
	@Resource(name="binOLPTRPS46_Service")
	private BINOLPTRPS46_Service binOLPTRPS46_Service;
	
	/** 导出会员信息共通BL **/
	@Resource(name="binOLCM37_BL")
	private BINOLCM37_BL binOLCM37_BL;
	
	// Excel导出列数组
	private final static String[][] proArray = {
				{ "counterCode", "RPS46_counterCode", "", "", "" },
				{"counterName", "RPS46_counterName","20","",""},
				{ "barCode", "RPS46_barCode", "12", "", "" },
				{ "unitCode", "RPS46_unitCode", "12", "", "" },
				{ "nameTotal", "RPS46_nameTotal", "20", "", "" },
				{ "originalBrand", "RPS46_originalBrand", "", "", "1299" },
				{ "startQuantity", "RPS46_startQuantity", "10", "right", "" },
				{ "startAmountNet", "RPS46_startAmountNet", "15", "right", "" },
				{ "inQuantity", "RPS46_inQuantity", "10", "right", "" },
				{ "inAmountNet", "RPS46_inAmountNet", "15", "right", "" },
				{ "outQuantity", "RPS46_outQuantity", "10", "right", "" },
				{ "outAmountNet", "RPS46_outAmountNet", "15", "right", "" },
				{ "endQuantity", "RPS46_endQuantity", "10", "right", "" },
				{ "endAmountNet", "RPS46_endAmountNet", "15", "right", "" } 
			};
	
	@Override
	public int getPrtCheckoutInfoCount(Map<String, Object> map) {
		return binOLPTRPS46_Service.getPrtCheckoutInfoCount(map);
	}

	//获取预付单明细LIST
	@Override
	public List<Map<String, Object>> getPrtCheckoutInfoList(Map<String, Object> map) {
		return binOLPTRPS46_Service.getPrtCheckoutInfoList(map);
	}

	@Override
	public byte[] exportExcel(Map<String, Object> map) throws Exception {
		//需导出数据总量
		int dataLen = binOLPTRPS46_Service.getExportDetailCount(map);
		map.put("dataLen", dataLen);
		BINOLMOCOM01_IF.ExcelParam ep = new BINOLMOCOM01_IF.ExcelParam();
		//必须设置sqlID（必须）,用于批查询
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTRPS46.getPrtCheckoutInfoList");
		ep.setMap(map);
	    // 导出数据列数组
        ep.setArray(proArray);
		ep.setShowTitleStyle(true);
		// 导出数据列头国际化资源文件
		ep.setBaseName("BINOLPTRPS46");
		return binOLMOCOM01_BL.getBatchExportExcel(ep);
	}
	
	/**
     * 导出处理
     */
	@Override
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
        String downloadFileName = binOLCM37_BL.getResourceValue("BINOLPTRPS46", language, "RPS46_exportName");
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
     * 获取导出参数
     */
	public Map<String, Object> getExportParam(Map<String, Object> map) {
		
		Map<String, Object> exportMap = new HashMap<String, Object>();
		exportMap.put(CherryConstants.ORGANIZATIONINFOID, map.get(CherryConstants.ORGANIZATIONINFOID));
		exportMap.put(CherryConstants.BRANDINFOID, map.get(CherryConstants.BRANDINFOID));
		String language = (String)map.get(CherryConstants.SESSION_CHERRY_LANGUAGE);
		exportMap.put("charset", map.get("charset"));
		
		exportMap.put("conditionMap", map);
		
        exportMap.put("sheetName", binOLCM37_BL.getResourceValue("BINOLPTRPS46", language, "RPS46_exportName"));
        String[][] titleRows = {
        		{ "counterCode", binOLCM37_BL.getResourceValue("BINOLPTRPS46", language,"RPS46_counterCode"), "", "", "" },
				{"counterName", binOLCM37_BL.getResourceValue("BINOLPTRPS46", language,"RPS46_counterName"),"20","",""},
				{ "barCode", binOLCM37_BL.getResourceValue("BINOLPTRPS46", language,"RPS46_barCode"), "12", "", "" },
				{ "unitCode", binOLCM37_BL.getResourceValue("BINOLPTRPS46", language,"RPS46_unitCode"), "12", "", "" },
				{ "nameTotal", binOLCM37_BL.getResourceValue("BINOLPTRPS46", language,"RPS46_nameTotal"), "20", "", "" },
				{ "originalBrand", binOLCM37_BL.getResourceValue("BINOLPTRPS46", language,"RPS46_originalBrand"), "", "", "1299" },
				{ "startQuantity", binOLCM37_BL.getResourceValue("BINOLPTRPS46", language,"RPS46_startQuantity"), "10", "right", "" },
				{ "startAmountNet", binOLCM37_BL.getResourceValue("BINOLPTRPS46", language,"RPS46_startAmountNet"), "15", "right", "" },
				{ "inQuantity", binOLCM37_BL.getResourceValue("BINOLPTRPS46", language,"RPS46_inQuantity"), "10", "right", "" },
				{ "inAmountNet", binOLCM37_BL.getResourceValue("BINOLPTRPS46", language,"RPS46_inAmountNet"), "15", "right", "" },
				{ "outQuantity", binOLCM37_BL.getResourceValue("BINOLPTRPS46", language,"RPS46_outQuantity"), "10", "right", "" },
				{ "outAmountNet", binOLCM37_BL.getResourceValue("BINOLPTRPS46", language,"RPS46_outAmountNet"), "15", "right", "" },
				{ "endQuantity", binOLCM37_BL.getResourceValue("BINOLPTRPS46", language,"RPS46_endQuantity"), "10", "right", "" },
				{ "endAmountNet", binOLCM37_BL.getResourceValue("BINOLPTRPS46", language,"RPS46_endAmountNet"), "15", "right", "" } 
    	};
        exportMap.put("titleRows", titleRows);
        
        return exportMap;
	}
	
	/**
	 * 取得需要转成Excel文件的数据List
	 * @param map 查询条件
	 * @return 需要转成Excel文件的数据List
	 */
	@Override
	public List<Map<String, Object>> getDataList(Map<String, Object> map)
			throws Exception {
		return binOLPTRPS46_Service.getExportDetailList(map);
	}

	@Override
	public int getExportDetailCount(Map<String, Object> map) {
		return binOLPTRPS46_Service.getExportDetailCount(map);
	}
	



}
