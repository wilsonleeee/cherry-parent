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
import com.cherry.cm.core.CodeTable;
import com.cherry.cm.core.PropertiesUtil;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.DateUtil;
import com.cherry.wp.wr.mrp.interfaces.BINOLWRMRP02_IF;
import com.cherry.wp.wr.mrp.service.BINOLWRMRP99_Service;

/**
 * 新会员入会统计BL
 * 
 * @author WangCT
 * @version 1.0 2014/09/10
 */
public class BINOLWRMRP02_BL implements BINOLWRMRP02_IF {
	
	/** 会员报表Service **/
	@Resource
	private BINOLWRMRP99_Service binOLWRMRP99_Service;
	
	/** 导出共通BL **/
	@Resource
	private BINOLCM37_BL binOLCM37_BL;
	
	/** CodeTable **/
	@Resource
	private CodeTable code;

	@Override
	public int getMemberCount(Map<String, Object> map) {
		return binOLWRMRP99_Service.getMemberCount(map);
	}

	@Override
	public List<Map<String, Object>> getMemberList(Map<String, Object> map) throws Exception {
		List<Map<String,Object>> memberList=binOLWRMRP99_Service.getMemberList(map);
		for(Map<String,Object> memInfo:memberList){
			memInfo.put("mobilePhone", CherrySecret.decryptData(ConvertUtil.getString(map.get("brandCode")),ConvertUtil.getString(memInfo.get("mobilePhone"))));
		}
		return memberList;
	}
	
	@Override
	public void setCondition(Map<String, Object> map) {
		
		String joinDateStart = (String)map.get("joinDateStart");
		String joinDateEnd = (String)map.get("joinDateEnd");
		// 取得 日结状态确定的业务日期
		String sysDate = binOLWRMRP99_Service.getBusDate(map);
		// 入会日期处理
		String joinDateMode = (String)map.get("joinDateMode");
		map.remove("joinDateStart");
		map.remove("joinDateEnd");
		// 存在入会时间模式查询的场合，按入会时间模式设置入会时间查询条件
		if(joinDateMode != null && !"".equals(joinDateMode)) {
			if("0".equals(joinDateMode)) {// 入会时间模式为当月入会
				// 系统时间所在月份的第一天
				map.put("joinDateStart", DateUtil.getFirstOrLastDateYMD(sysDate.substring(0,10), 0));
				// 系统时间所在月份的最后天
				map.put("joinDateEnd", DateUtil.getFirstOrLastDateYMD(sysDate.substring(0,10), 1));
			} else if("1".equals(joinDateMode)) {// 入会时间模式为动态模式（例如：几年、几月、几天内入会的会员）
				String joinDateRange = (String)map.get("joinDateRange");
				if(joinDateRange != null && !"".equals(joinDateRange)) {
					String joinDateUnit = (String)map.get("joinDateUnit");
					String joinDateUnitFlag = (String)map.get("joinDateUnitFlag");
					if("0".equals(joinDateUnit)) {
						String preJoinDate = DateUtil.addDateByYears("yyyy-MM-dd", sysDate.substring(0,10), -Integer.parseInt(joinDateRange));
						if("1".equals(joinDateUnitFlag)) {
							map.put("joinDateStart", preJoinDate);
							map.put("joinDateEnd", sysDate.substring(0,10));
						} else {
							map.put("joinDateStart", preJoinDate);
							map.put("joinDateEnd", preJoinDate);
						}
					} else if("1".equals(joinDateUnit)) {
						String preJoinDate = DateUtil.addDateByMonth("yyyy-MM-dd", sysDate.substring(0,10), -Integer.parseInt(joinDateRange));
						if("1".equals(joinDateUnitFlag)) {
							map.put("joinDateStart", preJoinDate);
							map.put("joinDateEnd", sysDate.substring(0,10));
						} else {
							map.put("joinDateStart", preJoinDate);
							map.put("joinDateEnd", preJoinDate);
						}
					} else if("2".equals(joinDateUnit)) {
						String preJoinDate = DateUtil.addDateByDays("yyyy-MM-dd", sysDate.substring(0,10), 1-Integer.parseInt(joinDateRange));
						if("1".equals(joinDateUnitFlag)) {
							map.put("joinDateStart", preJoinDate);
							map.put("joinDateEnd", sysDate.substring(0,10));
						} else {
							map.put("joinDateStart", preJoinDate);
							map.put("joinDateEnd", preJoinDate);
						}
					}
				}
			} else if("2".equals(joinDateMode)) {// 入会时间模式为指定日期范围
				map.put("joinDateStart", joinDateStart);
				map.put("joinDateEnd", joinDateEnd);
			}
		}
	}

	@Override
	public List<Map<String, Object>> getDataList(Map<String, Object> map)
			throws Exception {
		return this.getMemberList(map);
	}

	@Override
	public Map<String, Object> getExportParam(Map<String, Object> map) {
		
		Map<String, Object> exportMap = new HashMap<String, Object>();
		exportMap.put(CherryConstants.ORGANIZATIONINFOID, map.get(CherryConstants.ORGANIZATIONINFOID));
		exportMap.put(CherryConstants.BRANDINFOID, map.get(CherryConstants.BRANDINFOID));
		String language = (String)map.get(CherryConstants.SESSION_CHERRY_LANGUAGE);
		
		exportMap.put("conditionMap", map);
		
        exportMap.put("sheetName", CherryUtil.getResourceValue("wp", "BINOLWRMRP02", language, "sheetName"));
        
        String conditionContent = this.getConditionStr(map);
		if(conditionContent != null && !"".equals(conditionContent)) {
			exportMap.put("header", conditionContent);
		}
        
        List<String[]> titleRowList = new ArrayList<String[]>();
        titleRowList.add(new String[]{"memberCode", CherryUtil.getResourceValue("wp", "BINOLWRMRP02", language, "WRMRP02_memCode"), "20", "", ""});
        titleRowList.add(new String[]{"mobilePhone", CherryUtil.getResourceValue("wp", "BINOLWRMRP02", language, "WRMRP02_mobilePhone"), "20", "", ""});
        titleRowList.add(new String[]{"memberName", CherryUtil.getResourceValue("wp", "BINOLWRMRP02", language, "WRMRP02_memName"), "20", "", ""});
        titleRowList.add(new String[]{"gender", CherryUtil.getResourceValue("wp", "BINOLWRMRP02", language, "WRMRP02_gender"), "15", "", "1006"});
        titleRowList.add(new String[]{"joinDate", CherryUtil.getResourceValue("wp", "BINOLWRMRP02", language, "WRMRP02_joinDate"), "20", "", ""});
        titleRowList.add(new String[]{"levelName", CherryUtil.getResourceValue("wp", "BINOLWRMRP02", language, "WRMRP02_memLevel"), "15", "", ""});
        titleRowList.add(new String[]{"changablePoint", CherryUtil.getResourceValue("wp", "BINOLWRMRP02", language, "WRMRP02_changablePoint"), "15", "", ""});
        titleRowList.add(new String[]{"employeeName", CherryUtil.getResourceValue("wp", "BINOLWRMRP02", language, "WRMRP02_employeeId"), "20", "", ""});
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
        String downloadFileName = CherryUtil.getResourceValue("wp", "BINOLWRMRP02", language, "downloadFileName");
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
		
		// 入会日期
		String joinDateMode = (String)map.get("joinDateMode");
		if(joinDateMode != null && !"".equals(joinDateMode)) {
			StringBuffer joinDateCondition = new StringBuffer();
			if("0".equals(joinDateMode)) {
				joinDateCondition.append(CherryUtil.getResourceValue(null, language, "global.page.curJoinDate"));
			} else if("1".equals(joinDateMode)) {
				String joinDateRange = (String)map.get("joinDateRange");
				if(joinDateRange != null && !"".equals(joinDateRange)) {
					String joinDateUnit = (String)map.get("joinDateUnit");
					String joinDateUnitFlag = (String)map.get("joinDateUnitFlag");
					joinDateCondition.append(CherryUtil.getResourceValue(null, language, "global.page.joinDateDes")
							+joinDateRange+code.getVal("1239", joinDateUnit));
					if("1".equals(joinDateUnitFlag)) {
						joinDateCondition.append(CherryUtil.getResourceValue(null, language, "global.page.joinDateUnitFlag1"));
					} else if("2".equals(joinDateUnitFlag)) {
						joinDateCondition.append(CherryUtil.getResourceValue(null, language, "global.page.joinDateUnitFlag2"));
					}
				}
			} else if("2".equals(joinDateMode)) {
				String joinDateStart = (String)map.get("joinDateStart");
				String joinDateEnd = (String)map.get("joinDateEnd");
				String notLimit = CherryUtil.getResourceValue(null, language, "global.page.notLimit");
				if(joinDateStart != null && !"".equals(joinDateStart)) {
					if(joinDateEnd != null && !"".equals(joinDateEnd)) {
						joinDateCondition.append(joinDateStart+"\0~\0"+joinDateEnd);
					} else {
						joinDateCondition.append(joinDateStart+"\0~\0"+notLimit);
					}
				} else {
					if(joinDateEnd != null && !"".equals(joinDateEnd)) {
						joinDateCondition.append(notLimit+"\0~\0"+joinDateEnd);
					}
				}
			}
			String paramValue = joinDateCondition.toString();
			if(paramValue != null && !"".equals(paramValue)) {
				String paramName = CherryUtil.getResourceValue("wp", "BINOLWRMRP02", language, "WRMRP02_joinDate");
				condition.append(paramName + "：" + paramValue + "\0\0\0\0\0");
			}
		}
		
		String levelName = (String)map.get("levelName");
		if(levelName != null && !"".equals(levelName)) {
			String paramName = CherryUtil.getResourceValue("wp", "BINOLWRMRP02", language, "WRMRP02_memLevel");
			String paramValue = levelName;
			condition.append(paramName + "：" + paramValue + "\0\0\0\0\0");
		}
		
		String employeeName = (String)map.get("employeeName");
		if(employeeName != null && !"".equals(employeeName)) {
			String paramName = CherryUtil.getResourceValue("wp", "BINOLWRMRP02", language, "WRMRP02_employeeId");
			String paramValue = employeeName;
			condition.append(paramName + "：" + paramValue + "\0\0\0\0\0");
		}
		
		return condition.toString();
	}

}
