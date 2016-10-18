package com.cherry.mb.vis.bl;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbussiness.bl.BINOLCM37_BL;
import com.cherry.cm.cmbussiness.interfaces.BINOLCM37_IF;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CodeTable;
import com.cherry.cm.core.PropertiesUtil;
import com.cherry.cm.util.CherryUtil;
import com.cherry.mb.vis.service.BINOLMBVIS04_Service;

public class BINOLMBVIS04_BL implements BINOLCM37_IF {
	
	@Resource
	private BINOLMBVIS04_Service binOLMBVIS04_Service;
	
	/** 导出共通BL **/
	@Resource
	private BINOLCM37_BL binOLCM37_BL;
	
	/** CodeTable **/
	@Resource
    private CodeTable code;
	
	/**
	 * 取得回访信息总数
	 * 
	 * @param map 检索条件
	 * @return 回访信息总数
	 */
	public int getVisitTaskCount(Map<String, Object> map) {
		return binOLMBVIS04_Service.getVisitTaskCount(map);
	}

	/**
	 * 取得回访信息List
	 * 
	 * @param map 检索条件
	 * @return 回访信息List
	 */
	public List<Map<String, Object>> getVisitTaskList(Map<String, Object> map) {
		return binOLMBVIS04_Service.getVisitTaskList(map);
	}
	
	/**
	 * 取消回访任务
	 * 
	 * @param list 条件
	 */
	public void updateVisitTaskState(Map<String, Object> map) {
		List<String> visitTaskIdList = (List<String>)map.get("visitTaskIdList");
		if(visitTaskIdList != null && !visitTaskIdList.isEmpty()) {
			List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
			Object updateBy = map.get(CherryConstants.UPDATEDBY);
			for(String visitTaskId : visitTaskIdList) {
				Map<String, Object> paramMap = new HashMap<String, Object>();
				paramMap.put("visitTaskId", visitTaskId);
				// 更新者
				paramMap.put(CherryConstants.UPDATEDBY, updateBy);
				// 更新程序名
				paramMap.put(CherryConstants.UPDATEPGM, "BINOLMBVIS04");
				list.add(paramMap);
			}
			binOLMBVIS04_Service.updateVisitTaskState(list);
		}
	}

	@Override
	public List<Map<String, Object>> getDataList(Map<String, Object> map)
			throws Exception {
		return this.getVisitTaskList(map);
	}
	
	public Map<String, Object> getExportParam(Map<String, Object> map) {
		
		Map<String, Object> exportMap = new HashMap<String, Object>();
		exportMap.put(CherryConstants.ORGANIZATIONINFOID, map.get(CherryConstants.ORGANIZATIONINFOID));
		exportMap.put(CherryConstants.BRANDINFOID, map.get(CherryConstants.BRANDINFOID));
		String language = (String)map.get(CherryConstants.SESSION_CHERRY_LANGUAGE);
		
		exportMap.put("conditionMap", map);
		
        exportMap.put("sheetName", CherryUtil.getResourceValue("mb", "BINOLMBVIS04", language, "sheetName"));
        
        String conditionContent = this.getConditionStr(map);
		if(conditionContent != null && !"".equals(conditionContent)) {
			exportMap.put("header", conditionContent);
		}
        
        List<String[]> titleRowList = new ArrayList<String[]>();
        titleRowList.add(new String[]{"visitTypeName", CherryUtil.getResourceValue("mb", "BINOLMBVIS04", language, "mbvis04_visitTypeCode"), "20", "", ""});
        titleRowList.add(new String[]{"memCode", CherryUtil.getResourceValue("mb", "BINOLMBVIS04", language, "mbvis04_memCode"), "20", "", ""});
        titleRowList.add(new String[]{"memName", CherryUtil.getResourceValue("mb", "BINOLMBVIS04", language, "mbvis04_memName"), "20", "", ""});
        titleRowList.add(new String[]{"birthDay", CherryUtil.getResourceValue("mb", "BINOLMBVIS04", language, "mbvis04_birthDay"), "20", "", ""});
        titleRowList.add(new String[]{"counterName", CherryUtil.getResourceValue("mb", "BINOLMBVIS04", language, "mbvis04_counterCode"), "20", "", ""});
        titleRowList.add(new String[]{"employeeName", CherryUtil.getResourceValue("mb", "BINOLMBVIS04", language, "mbvis04_employeeCode"), "20", "", ""});
        titleRowList.add(new String[]{"startTime", CherryUtil.getResourceValue("mb", "BINOLMBVIS04", language, "mbvis04_startDate"), "20", "", ""});
        titleRowList.add(new String[]{"endTime", CherryUtil.getResourceValue("mb", "BINOLMBVIS04", language, "mbvis04_endDate"), "20", "", ""});
        titleRowList.add(new String[]{"visitFlag", CherryUtil.getResourceValue("mb", "BINOLMBVIS04", language, "mbvis04_visitResult"), "20", "", "1209"});
        titleRowList.add(new String[]{"visitTime", CherryUtil.getResourceValue("mb", "BINOLMBVIS04", language, "mbvis04_visitTime"), "20", "", ""});
        titleRowList.add(new String[]{"taskState", CherryUtil.getResourceValue("mb", "BINOLMBVIS04", language, "mbvis04_taskState"), "20", "", "1207"});
        titleRowList.add(new String[]{"synchroFlag", CherryUtil.getResourceValue("mb", "BINOLMBVIS04", language, "mbvis04_synchroFlag"), "20", "", "1206"});
        exportMap.put("titleRows", titleRowList.toArray(new String[][]{}));
        
        return exportMap;
	}
	
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
        String downloadFileName = CherryUtil.getResourceValue("mb", "BINOLMBVIS04", language, "downloadFileName");
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
		
		String visitTypeName = (String)map.get("visitTypeName");
		if(visitTypeName !=null && !"".equals(visitTypeName)) {
			String paramName = CherryUtil.getResourceValue("mb", "BINOLMBVIS04", language, "mbvis04_visitTypeCode");
			String paramValue = visitTypeName;
			condition.append(paramName + "：" + paramValue + "\0\0\0\0\0");
		}
		
		String startDate = (String)map.get("startDate");
		String endDate = (String)map.get("endDate");
		String notLimit = CherryUtil.getResourceValue(null, language, "global.page.notLimit");
		StringBuffer saleDateCondition = new StringBuffer();
		if(startDate != null && !"".equals(startDate)) {
			if(endDate != null && !"".equals(endDate)) {
				saleDateCondition.append(startDate+"\0~\0"+endDate);
			} else {
				saleDateCondition.append(startDate+"\0~\0"+notLimit);
			}
		} else {
			if(endDate != null && !"".equals(endDate)) {
				saleDateCondition.append(notLimit+"\0~\0"+endDate);
			}
		}
		String saleDateValue = saleDateCondition.toString();
		if(saleDateValue != null && !"".equals(saleDateValue)) {
			String paramName = CherryUtil.getResourceValue("mb", "BINOLMBVIS04", language, "mbvis04_visitTaskDate");
			String paramValue = saleDateValue;
			condition.append(paramName + "：" + paramValue + "\0\0\0\0\0");
		}
		
		String memCode = (String)map.get("memCode");
		if(memCode !=null && !"".equals(memCode)) {
			String paramName = CherryUtil.getResourceValue("mb", "BINOLMBVIS04", language, "mbvis04_memCode");
			String paramValue = memCode;
			condition.append(paramName + "：" + paramValue + "\0\0\0\0\0");
		}
		
		String birthDayMonth = (String)map.get("birthDayMonth");
		String birthDayDate = (String)map.get("birthDayDate");
		StringBuffer birthDayCondition = new StringBuffer();
		if(birthDayMonth !=null && !"".equals(birthDayMonth)) {
			if(birthDayDate != null && !"".equals(birthDayDate)) {
				birthDayCondition.append(birthDayMonth+CherryUtil.getResourceValue("mb", "BINOLMBVIS04", language, "mbvis04_birthDay1")
						+birthDayDate+CherryUtil.getResourceValue("mb", "BINOLMBVIS04", language, "mbvis04_birthDay2"));
			} else {
				birthDayCondition.append(birthDayMonth+CherryUtil.getResourceValue("mb", "BINOLMBVIS04", language, "mbvis04_birthDay1"));
			}
		}
		String birthDayValue = birthDayCondition.toString();
		if(birthDayValue != null && !"".equals(birthDayValue)) {
			String paramName = CherryUtil.getResourceValue("mb", "BINOLMBVIS04", language, "mbvis04_birthDay");
			String paramValue = birthDayValue;
			condition.append(paramName + "：" + paramValue + "\0\0\0\0\0");
		}
		
		String counterCode = (String)map.get("counterCode");
		if(counterCode !=null && !"".equals(counterCode)) {
			String paramName = CherryUtil.getResourceValue("mb", "BINOLMBVIS04", language, "mbvis04_counterCode");
			String paramValue = counterCode;
			condition.append(paramName + "：" + paramValue + "\0\0\0\0\0");
		}
		
		String employeeCode = (String)map.get("employeeCode");
		if(employeeCode !=null && !"".equals(employeeCode)) {
			String paramName = CherryUtil.getResourceValue("mb", "BINOLMBVIS04", language, "mbvis04_employeeCode");
			String paramValue = employeeCode;
			condition.append(paramName + "：" + paramValue + "\0\0\0\0\0");
		}
		
		String visitTimeStart = (String)map.get("visitTimeStart");
		String visitTimeEnd = (String)map.get("visitTimeEnd");
		StringBuffer visitTimeCondition = new StringBuffer();
		if(visitTimeStart != null && !"".equals(visitTimeStart)) {
			if(visitTimeEnd != null && !"".equals(visitTimeEnd)) {
				visitTimeCondition.append(visitTimeStart+"\0~\0"+visitTimeEnd);
			} else {
				visitTimeCondition.append(visitTimeStart+"\0~\0"+notLimit);
			}
		} else {
			if(visitTimeEnd != null && !"".equals(visitTimeEnd)) {
				visitTimeCondition.append(notLimit+"\0~\0"+visitTimeEnd);
			}
		}
		String visitTimeValue = visitTimeCondition.toString();
		if(visitTimeValue != null && !"".equals(visitTimeValue)) {
			String paramName = CherryUtil.getResourceValue("mb", "BINOLMBVIS04", language, "mbvis04_visitTime");
			String paramValue = visitTimeValue;
			condition.append(paramName + "：" + paramValue + "\0\0\0\0\0");
		}
		
		String visitResult = (String)map.get("visitResult");
		if(visitResult !=null && !"".equals(visitResult)) {
			String paramName = CherryUtil.getResourceValue("mb", "BINOLMBVIS04", language, "mbvis04_visitResult");
			String paramValue = code.getVal("1209", visitResult);
			condition.append(paramName + "：" + paramValue + "\0\0\0\0\0");
		}
		
		return condition.toString();
	}

}
