package com.cherry.wp.wr.mrp.bl;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbussiness.bl.BINOLCM37_BL;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherrySecret;
import com.cherry.cm.core.PropertiesUtil;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.DateUtil;
import com.cherry.wp.wr.mrp.interfaces.BINOLWRMRP03_IF;
import com.cherry.wp.wr.mrp.service.BINOLWRMRP99_Service;

/**
 * 会员生日情况统计BL
 * 
 * @author WangCT
 * @version 1.0 2014/09/10
 */
public class BINOLWRMRP03_BL implements BINOLWRMRP03_IF {
	
	/** 会员报表Service **/
	@Resource
	private BINOLWRMRP99_Service binOLWRMRP99_Service;
	
	/** 导出共通BL **/
	@Resource
	private BINOLCM37_BL binOLCM37_BL;

	@Override
	public int getMemberCount(Map<String, Object> map) {
		return binOLWRMRP99_Service.getMemberCount(map);
	}

	@Override
	public List<Map<String, Object>> getMemberList(Map<String, Object> map) throws Exception {
		List<Map<String,Object>> memberList=binOLWRMRP99_Service.getMemberList(map);
		for(Map<String,Object> memberInfo:memberList){
			memberInfo.put("mobilePhone", CherrySecret.decryptData(ConvertUtil.getString(map.get("brandCode")),ConvertUtil.getString(memberInfo.get("mobilePhone"))));
		}
		return memberList;
	}
	
	@Override
	public void setCondition(Map<String, Object> map) {
		
		// 取得 日结状态确定的业务日期
		String sysDate = binOLWRMRP99_Service.getBusDate(map);
		// 生日条件设置
		String birthDayMode = (String)map.get("birthDayMode");
		if(birthDayMode != null && !"".equals(birthDayMode)) {
			if("0".equals(birthDayMode)) {// 生日模式为本周生日
				String birthDayDateStart = DateUtil.getFirstDayOfWeek(sysDate.substring(0,10));
				String birthDayDateEnd = DateUtil.getLastDayOfWeek(sysDate.substring(0,10));
				map.put("birthDayStart", birthDayDateStart.substring(5,7)+birthDayDateStart.substring(8,10));
				map.put("birthDayEnd", birthDayDateEnd.substring(5,7)+birthDayDateEnd.substring(8,10));
			} else if("1".equals(birthDayMode)) {// 生日模式为本月生日
				String birthDayDateStart = DateUtil.getFirstOrLastDateYMD(sysDate.substring(0,10), 0);
				String birthDayDateEnd = DateUtil.getFirstOrLastDateYMD(sysDate.substring(0,10), 1);
				map.put("birthDayStart", birthDayDateStart.substring(5,7)+birthDayDateStart.substring(8,10));
				map.put("birthDayEnd", birthDayDateEnd.substring(5,7)+birthDayDateEnd.substring(8,10));
			} else if("2".equals(birthDayMode)) {// 生日模式为指定生日
				String birthDayMonth = (String)map.get("birthDayMonth");
				if(birthDayMonth != null && !"".equals(birthDayMonth)) {
					if(birthDayMonth.length() == 1) {
						birthDayMonth = "0" + birthDayMonth;
					}
					String birthDayDate = (String)map.get("birthDayDate");
					if(birthDayDate != null && !"".equals(birthDayDate)) {
						if(birthDayDate.length() == 1) {
							birthDayDate = "0" + birthDayDate;
						}
						map.put("birthDayStart", birthDayMonth + birthDayDate);
						map.put("birthDayEnd", birthDayMonth + birthDayDate);
					} else {
						map.put("birthDayStart", birthDayMonth + "01");
						map.put("birthDayEnd", birthDayMonth + DateUtil.getLastDateByMonth(birthDayMonth));
					}
				}
			} else if("3".equals(birthDayMode)) {// 生日模式为生日范围
				String birthDayMonthRangeStart = (String)map.get("birthDayMonthRangeStart");
				String birthDayMonthRangeEnd = (String)map.get("birthDayMonthRangeEnd");
				String birthDayDateRangeStart = (String)map.get("birthDayDateRangeStart");
				String birthDayDateRangeEnd = (String)map.get("birthDayDateRangeEnd");
				if(birthDayMonthRangeStart != null && !"".equals(birthDayMonthRangeStart)) {
					if(birthDayMonthRangeStart.length() == 1) {
						birthDayMonthRangeStart = "0" + birthDayMonthRangeStart;
					}
					if(birthDayDateRangeStart != null && !"".equals(birthDayDateRangeStart)) {
						if(birthDayDateRangeStart.length() == 1) {
							birthDayDateRangeStart = "0" + birthDayDateRangeStart;
						}
					} else {
						birthDayDateRangeStart = "01";
					}
					map.put("birthDayStart", birthDayMonthRangeStart+birthDayDateRangeStart);
				}
				if(birthDayMonthRangeEnd != null && !"".equals(birthDayMonthRangeEnd)) {
					if(birthDayMonthRangeEnd.length() == 1) {
						birthDayMonthRangeEnd = "0" + birthDayMonthRangeEnd;
					}
					if(birthDayDateRangeEnd != null && !"".equals(birthDayDateRangeEnd)) {
						if(birthDayDateRangeEnd.length() == 1) {
							birthDayDateRangeEnd = "0" + birthDayDateRangeEnd;
						}
					} else {
						birthDayDateRangeEnd = "01";
					}
					map.put("birthDayEnd", birthDayMonthRangeEnd+birthDayDateRangeEnd);
				}
			}
		}
	}

	@Override
	public List<Map<String, Object>> getDataList(Map<String, Object> map)
			throws Exception {
		List<Map<String, Object>> dataList = this.getMemberList(map);
		if(dataList != null && !dataList.isEmpty()) {
			for(Map<String, Object> dataMap : dataList) {
				String birthYear = (String)dataMap.get("birthYear");
				String birthMonth = (String)dataMap.get("birthMonth");
				String birthDay = (String)dataMap.get("birthDay");
				if(birthYear != null && !"".equals(birthYear) 
						&& birthMonth != null && !"".equals(birthMonth)
						&& birthDay != null && !"".equals(birthDay)) {
					dataMap.put("birth", birthYear+"-"+birthMonth+"-"+birthDay);
				}
			}
		}
		return dataList;
	}

	@Override
	public Map<String, Object> getExportParam(Map<String, Object> map) {
		
		Map<String, Object> exportMap = new HashMap<String, Object>();
		exportMap.put(CherryConstants.ORGANIZATIONINFOID, map.get(CherryConstants.ORGANIZATIONINFOID));
		exportMap.put(CherryConstants.BRANDINFOID, map.get(CherryConstants.BRANDINFOID));
		String language = (String)map.get(CherryConstants.SESSION_CHERRY_LANGUAGE);
		
		exportMap.put("conditionMap", map);
		
        exportMap.put("sheetName", CherryUtil.getResourceValue("wp", "BINOLWRMRP03", language, "sheetName"));
        
        String conditionContent = this.getConditionStr(map);
		if(conditionContent != null && !"".equals(conditionContent)) {
			exportMap.put("header", conditionContent);
		}
        
        List<String[]> titleRowList = new ArrayList<String[]>();
        titleRowList.add(new String[]{"memberCode", CherryUtil.getResourceValue("wp", "BINOLWRMRP03", language, "WRMRP03_memCode"), "20", "", ""});
        titleRowList.add(new String[]{"mobilePhone", CherryUtil.getResourceValue("wp", "BINOLWRMRP03", language, "WRMRP03_mobilePhone"), "20", "", ""});
        titleRowList.add(new String[]{"memberName", CherryUtil.getResourceValue("wp", "BINOLWRMRP03", language, "WRMRP03_memName"), "20", "", ""});
        titleRowList.add(new String[]{"gender", CherryUtil.getResourceValue("wp", "BINOLWRMRP03", language, "WRMRP03_gender"), "15", "", "1006"});
        titleRowList.add(new String[]{"birth", CherryUtil.getResourceValue("wp", "BINOLWRMRP03", language, "WRMRP03_birthDay"), "15", "", ""});
        titleRowList.add(new String[]{"levelName", CherryUtil.getResourceValue("wp", "BINOLWRMRP03", language, "WRMRP03_memLevel"), "15", "", ""});
        titleRowList.add(new String[]{"changablePoint", CherryUtil.getResourceValue("wp", "BINOLWRMRP03", language, "WRMRP03_changablePoint"), "15", "", ""});
        titleRowList.add(new String[]{"employeeName", CherryUtil.getResourceValue("wp", "BINOLWRMRP03", language, "WRMRP03_employeeId"), "20", "", ""});
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
        String downloadFileName = CherryUtil.getResourceValue("wp", "BINOLWRMRP03", language, "downloadFileName");
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
		
		// 生日
		String birthDayMode = (String)map.get("birthDayMode");
		if(birthDayMode != null && !"".equals(birthDayMode)) {
			StringBuffer birthDayCondition = new StringBuffer();
			if("0".equals(birthDayMode)) {
				birthDayCondition.append(CherryUtil.getResourceValue(null, language, "global.page.curWeek"));
			} else if("1".equals(birthDayMode)) {
				birthDayCondition.append(CherryUtil.getResourceValue(null, language, "global.page.curMonth"));
				
			} else if("2".equals(birthDayMode)) {
				String birthDayMonth = (String)map.get("birthDayMonth");
				String birthDayDate = (String)map.get("birthDayDate");
				if(birthDayMonth != null && !"".equals(birthDayMonth)) {
					if(birthDayDate != null && !"".equals(birthDayDate)) {
						birthDayCondition.append(birthDayMonth+CherryUtil.getResourceValue(null, language, "global.page.month")
								+birthDayDate+CherryUtil.getResourceValue(null, language, "global.page.date"));
					} else {
						birthDayCondition.append(birthDayMonth+CherryUtil.getResourceValue(null, language, "global.page.month"));
					}
				}
			} else if("3".equals(birthDayMode)) {
				String birthDayMonthRangeStart = (String)map.get("birthDayMonthRangeStart");
				String birthDayMonthRangeEnd = (String)map.get("birthDayMonthRangeEnd");
				String birthDayDateRangeStart = (String)map.get("birthDayDateRangeStart");
				String birthDayDateRangeEnd = (String)map.get("birthDayDateRangeEnd");
				String notLimit = CherryUtil.getResourceValue(null, language, "global.page.notLimit");
				if(birthDayDateRangeStart == null || "".equals(birthDayDateRangeStart)) {
					birthDayDateRangeStart = "1";
				}
				if(birthDayDateRangeEnd == null || "".equals(birthDayDateRangeEnd)) {
					birthDayDateRangeEnd = "1";
				}
				if(birthDayMonthRangeStart != null && !"".equals(birthDayMonthRangeStart)) {
					if(birthDayMonthRangeEnd != null && !"".equals(birthDayMonthRangeEnd)) {
						birthDayCondition.append(birthDayMonthRangeStart+CherryUtil.getResourceValue(null, language, "global.page.month")
								+birthDayDateRangeStart+CherryUtil.getResourceValue(null, language, "global.page.date")
								+"~"+birthDayMonthRangeEnd+CherryUtil.getResourceValue(null, language, "global.page.month")
								+birthDayDateRangeEnd+CherryUtil.getResourceValue(null, language, "global.page.date"));
					} else {
						birthDayCondition.append(birthDayMonthRangeStart+CherryUtil.getResourceValue(null, language, "global.page.month")
								+birthDayDateRangeStart+CherryUtil.getResourceValue(null, language, "global.page.date")
								+"~"+notLimit);
					}
				} else {
					if(birthDayMonthRangeEnd != null && !"".equals(birthDayMonthRangeEnd)) {
						birthDayCondition.append(notLimit
								+"~"+birthDayMonthRangeEnd+CherryUtil.getResourceValue(null, language, "global.page.month")
								+birthDayDateRangeEnd+CherryUtil.getResourceValue(null, language, "global.page.date"));
						
					}
				}
			}
			String paramValue = birthDayCondition.toString();
			if(paramValue != null && !"".equals(paramValue)) {
				String paramName = CherryUtil.getResourceValue("wp", "BINOLWRMRP03", language, "WRMRP03_birthDay");
				condition.append(paramName + "：" + paramValue + "\0\0\0\0\0");
			}
		}
		
		String levelName = (String)map.get("levelName");
		if(levelName != null && !"".equals(levelName)) {
			String paramName = CherryUtil.getResourceValue("wp", "BINOLWRMRP03", language, "WRMRP03_memLevel");
			String paramValue = levelName;
			condition.append(paramName + "：" + paramValue + "\0\0\0\0\0");
		}
		
		String employeeName = (String)map.get("employeeName");
		if(employeeName != null && !"".equals(employeeName)) {
			String paramName = CherryUtil.getResourceValue("wp", "BINOLWRMRP03", language, "WRMRP03_employeeId");
			String paramValue = employeeName;
			condition.append(paramName + "：" + paramValue + "\0\0\0\0\0");
		}
		
		return condition.toString();
	}

}
