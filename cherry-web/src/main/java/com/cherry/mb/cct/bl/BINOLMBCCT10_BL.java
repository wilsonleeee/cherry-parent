package com.cherry.mb.cct.bl;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbussiness.bl.BINOLCM37_BL;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.PropertiesUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.mb.cct.interfaces.BINOLMBCCT10_IF;
import com.cherry.mb.cct.service.BINOLMBCCT10_Service;

public class BINOLMBCCT10_BL implements BINOLMBCCT10_IF{
	@Resource(name="binOLMBCCT10_Service")
	private BINOLMBCCT10_Service binOLMBCCT10_Service;
	
	@Resource(name="binOLCM37_BL")
	private BINOLCM37_BL binOLCM37_BL;
	
	@Override
	@SuppressWarnings("rawtypes")
	public List getIssueList(Map<String, Object> map) {
		// 获取符合查询条件的问题记录列表
		List<Map<String, Object>> issueList = binOLMBCCT10_Service.getIssueList(map);
		return issueList;
	}

	@Override
	public int getIssueCount(Map<String, Object> map) {
		// 获取符合查询条件的问题记录数量
		return binOLMBCCT10_Service.getIssueCount(map);
	}

	@Override
	public Map<String, Object> getExportMap(Map<String, Object> map) {
		String language = ConvertUtil.getString(map.get(CherryConstants.SESSION_LANGUAGE));
		String[][] array = {
				{ "callId", binOLCM37_BL.getResourceValue("BINOLMBCCT10", language, "mbcct.callId"), "25", "", "" },
				{ "customerCode", binOLCM37_BL.getResourceValue("BINOLMBCCT10", language, "mbcct.customerCode"), "20", "", "" },
				{ "customerName", binOLCM37_BL.getResourceValue("BINOLMBCCT10", language, "mbcct.customerName"), "20", "", "" },
				{ "customerNumber", binOLCM37_BL.getResourceValue("BINOLMBCCT10", language, "mbcct.customerNumber"), "20", "", "" },
				{ "issueSummary", binOLCM37_BL.getResourceValue("BINOLMBCCT10", language, "mbcct.issueSummary"), "40", "", "" },
				{ "issueText", binOLCM37_BL.getResourceValue("BINOLMBCCT10", language, "mbcct.issueText"), "60", "", "" },
				{ "issueType", binOLCM37_BL.getResourceValue("BINOLMBCCT10", language, "mbcct.issueType"), "15", "", "1272" },
				{ "issueSubType", binOLCM37_BL.getResourceValue("BINOLMBCCT10", language, "mbcct.issueSubType"), "15", "", "1329" },
				{ "priority", binOLCM37_BL.getResourceValue("BINOLMBCCT10", language, "mbcct.priority"), "15", "", "1271" },
				{ "resolution", binOLCM37_BL.getResourceValue("BINOLMBCCT10", language, "mbcct.resolution"), "15", "", "1117" },
				{ "callTime", binOLCM37_BL.getResourceValue("BINOLMBCCT10", language, "mbcct.callTime"), "20", "", "" },
				{ "cNo", binOLCM37_BL.getResourceValue("BINOLMBCCT10", language, "mbcct.cNo"), "15", "", "" },
				{ "createTime", binOLCM37_BL.getResourceValue("BINOLMBCCT10", language, "mbcct.createTime"), "20", "", "" },
				{ "resolutionDate", binOLCM37_BL.getResourceValue("BINOLMBCCT10", language, "mbcct.resolutionDate"), "20", "", "" },
				{ "employeeName", binOLCM37_BL.getResourceValue("BINOLMBCCT10", language, "mbcct.employeeName"), "15", "", "" },
				{ "actionBoby", binOLCM37_BL.getResourceValue("BINOLMBCCT10", language, "mbcct.actionBoby"), "60", "", "" },
		};
		int dataLen = binOLMBCCT10_Service.getIssueExcelCount(map);
		map.put("dataLen", dataLen);
		map.put("sheetName", binOLCM37_BL.getResourceValue("BINOLMBCCT10", language, "mbcct.sheetName"));
		map.put("downloadFileName", binOLCM37_BL.getResourceValue("BINOLMBCCT10", language, "mbcct.downloadFileName"));
		map.put("titleRows", array);
		map.put(CherryConstants.SORT_ID, "createTime desc");
		return map;
	}
	
	@Override
	public List<Map<String, Object>> getDataList(Map<String, Object> map)
			throws Exception {
		// 获取符合查询条件的问题记录列表
		List<Map<String, Object>> issueList = binOLMBCCT10_Service.getIssueExcelList(map);
		return issueList;
	}

	@Override
	public String exportCsv(Map<String, Object> map) throws Exception {
		// 获取导出参数
		Map<String, Object> exportMap = map;
    	String tempFilePath = PropertiesUtil.pps.getProperty("tempFilePath");
        String sessionId = (String)map.get("sessionId");
        // 下载文件临时目录
        tempFilePath = tempFilePath + File.separator + sessionId;
        exportMap.put("tempFilePath", tempFilePath);
        // 下载文件名
        String downloadFileName =  ConvertUtil.getString(exportMap.get("downloadFileName"));
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
}
