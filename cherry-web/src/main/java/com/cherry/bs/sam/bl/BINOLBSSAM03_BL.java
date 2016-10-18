package com.cherry.bs.sam.bl;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.bs.sam.interfaces.BINOLBSSAM03_IF;
import com.cherry.bs.sam.service.BINOLBSSAM03_Service;
import com.cherry.cm.cmbussiness.bl.BINOLCM37_BL;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.PropertiesUtil;
import com.cherry.cm.util.CherryUtil;


public class BINOLBSSAM03_BL  implements BINOLBSSAM03_IF{
	@Resource(name="binOLBSSAM03_Service")
	private BINOLBSSAM03_Service bINOLBSSAM03_Service;
	/** 导出共通BL **/
	@Resource
	private BINOLCM37_BL binOLCM37_BL;
	

	@Override
	public int getPayrollCount(Map<String, Object> param) {
		return bINOLBSSAM03_Service.getPayrollCount(param);
	}

	@Override
	public List<Map<String, Object>> getPayrollList(Map<String, Object> param) {
		return bINOLBSSAM03_Service.getPayrollList(param);
	}

	@Override
	public List<Map<String, Object>> getDataList(Map<String, Object> map)
			throws Exception {
		return bINOLBSSAM03_Service.getPayrollList(map);
	}

	@Override
	public Map<String, Object> getExportParam(Map<String, Object> map) {
		Map<String, Object> exportMap = new HashMap<String, Object>();
		exportMap.put(CherryConstants.ORGANIZATIONINFOID, map.get(CherryConstants.ORGANIZATIONINFOID));
		exportMap.put(CherryConstants.BRANDINFOID, map.get(CherryConstants.BRANDINFOID));
		String language = (String)map.get(CherryConstants.SESSION_CHERRY_LANGUAGE);
		
		exportMap.put("conditionMap", map);
		
	    exportMap.put("sheetName", CherryUtil.getResourceValue("bs", "BINOLBSSAM03", language, "sheetName"));
	    
	    String conditionContent = this.getConditionStr(map);
		if(conditionContent != null && !"".equals(conditionContent)) {
			exportMap.put("header", conditionContent);
		}
	    
	    List<String[]> titleRowList = new ArrayList<String[]>();
	    titleRowList.add(new String[]{"departName", CherryUtil.getResourceValue("bs", "BINOLBSSAM03", language, "SAM03_organization"), "30", "", ""});
	    titleRowList.add(new String[]{"employeeName", CherryUtil.getResourceValue("bs", "BINOLBSSAM03", language, "SAM03_employeeName"), "20", "", ""});
	    titleRowList.add(new String[]{"workingHours", CherryUtil.getResourceValue("bs", "BINOLBSSAM03", language, "SAM03_workingHours"), "20", "float", ""});
	    titleRowList.add(new String[]{"workingDays", CherryUtil.getResourceValue("bs", "BINOLBSSAM03", language, "SAM03_workingDays"), "20", "float", ""});
	    titleRowList.add(new String[]{"usualOvertime", CherryUtil.getResourceValue("bs", "BINOLBSSAM03", language, "SAM03_usualOvertime"), "20", "", ""});
	    titleRowList.add(new String[]{"specialOvertime", CherryUtil.getResourceValue("bs", "BINOLBSSAM03", language, "SAM03_specialOverTime"), "20", "", ""});
	    titleRowList.add(new String[]{"saleTarget", CherryUtil.getResourceValue("bs", "BINOLBSSAM03", language, "SAM03_saleTarget"), "20", "float", ""});
	    titleRowList.add(new String[]{"saleAmount", CherryUtil.getResourceValue("bs", "BINOLBSSAM03", language, "SAM03_saleAmount"), "20", "float", ""});
	    titleRowList.add(new String[]{"bonusRate", CherryUtil.getResourceValue("bs", "BINOLBSSAM03", language, "SAM03_bonusRate"), "20", "float", ""});
	    titleRowList.add(new String[]{"completionRate", CherryUtil.getResourceValue("bs", "BINOLBSSAM03", language, "SAM03_completionRate"), "20", "float", ""});
	    titleRowList.add(new String[]{"score", CherryUtil.getResourceValue("bs", "BINOLBSSAM03", language, "SAM03_score"), "20", "float", ""});
	    titleRowList.add(new String[]{"wagesAmount", CherryUtil.getResourceValue("bs", "BINOLBSSAM03", language, "SAM03_wagesAmount"), "20", "float", ""});
	    titleRowList.add(new String[]{"memo", CherryUtil.getResourceValue("bs", "BINOLBSSAM03", language, "SAM03_memo"), "20", "", ""});
	    exportMap.put("titleRows", titleRowList.toArray(new String[][]{}));
	    
	    return exportMap;
	}

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
	    String downloadFileName = CherryUtil.getResourceValue("bs", "BINOLBSSAM03", language, "downloadFileName");
	    exportMap.put("tempFileName", downloadFileName);
	    
	    exportMap.put("charset", map.get("charset"));
	    
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
	
	
	private String getConditionStr(Map<String, Object> map) {
		StringBuffer condition = new StringBuffer();
		String language = (String)map.get(CherryConstants.SESSION_CHERRY_LANGUAGE);
		
		//员工名字
		String employeeName = (String)map.get("employeeName");
		if(employeeName != null && !"".equals(employeeName)) {
			String paramName = CherryUtil.getResourceValue("bs", "BINOLBSSAM03", language, "SAM03_employeeName");
			String paramValue = employeeName;
			condition.append(paramName + "：" + paramValue + "\0\0\0\0\0");
		}
		
		// 工资年份
		String wagesYear = (String)map.get("wagesYear");
		if(wagesYear != null && !"".equals(wagesYear)) {
			String paramName = CherryUtil.getResourceValue("bs", "BINOLBSSAM03", language, "SAM03_wagesYear");
			String paramValue = wagesYear;
			condition.append(paramName + "：" + paramValue + "\0\0\0\0\0");
		}
		
		// 工资月份
		String wagesMonth = (String)map.get("wagesMonth");
		if(wagesMonth != null && !"".equals(wagesMonth)) {
			String paramName = CherryUtil.getResourceValue("bs", "BINOLBSSAM03", language, "SAM03_wagesMonth");
			String paramValue = wagesMonth;
			condition.append(paramName + "：" + paramValue + "\0\0\0\0\0");
		}

		return condition.toString();
	}


}
