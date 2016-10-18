package com.cherry.mb.rpt.bl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.mb.rpt.interfaces.BINOLMBRPT05_IF;
import com.cherry.mb.rpt.service.BINOLMBRPT05_Service;

/**
 * 
 * @ClassName: BINOLMBRPT05_BL 
 * @Description: TODO(会员销售统计BL) 
 * @author menghao
 * @version v1.0.0 2015-1-6 
 *
 */
public class BINOLMBRPT05_BL implements BINOLMBRPT05_IF {
	
	/**会员发展统计报表Service **/
	@Resource(name="binOLMBRPT05_Service")
	private BINOLMBRPT05_Service binOLMBRPT05_Service;
	
	/**
	 * 取得会员发展统计信息LIST【汇总信息放到最后一行】
	 * @param map
	 * @return
	 * @throws Exception 
	 */
	@Override
	public List<Map<String, Object>> getRegionStatisticsList(Map<String, Object> map) throws Exception {
		//  会员销售统计结果
		List<Map<String, Object>> resultList = binOLMBRPT05_Service.getRegionStatisticsList(map);
		Map<String, Object> summaryMap = new HashMap<String, Object>();
		if(resultList != null && !resultList.isEmpty()) {
			// 取得汇总行信息
			for(Map<String, Object> resultMap : resultList) {
				String summary = ConvertUtil.getString(resultMap.get("region"));
				if("summary".equals(summary)) {
					summaryMap.putAll(resultMap);
					resultList.remove(resultMap);
					break;
				}
			}
			Integer totalMemNum = (Integer)summaryMap.get("regionMemNum");
			BigDecimal totalMemConsumeAmount = (BigDecimal)summaryMap.get("regionMemConsumeAmount");
			// 计算出人数、金额占比
			for(Map<String, Object> rateMap : resultList) {
				Integer regionMemNum = (Integer)rateMap.get("regionMemNum");
				BigDecimal regionMemConsumeAmount = (BigDecimal)rateMap.get("regionMemConsumeAmount");
				if(regionMemNum == null) {
					regionMemNum = 0;
				}
				if(regionMemConsumeAmount == null) {
					regionMemConsumeAmount = new BigDecimal(0);
				}
				if(totalMemNum > 0) {
					// 购买会员人数占比
					rateMap.put("memberNumRate", CherryUtil.percent(regionMemNum, totalMemNum, 2));
				}
				if(totalMemConsumeAmount.doubleValue() > 0) {
					// 购买金额占比
					rateMap.put("consumeAmountRate", CherryUtil.percent(regionMemConsumeAmount.doubleValue(), totalMemConsumeAmount.doubleValue(), 2));
				}
			}
			resultList.add(summaryMap);
		} else {
			resultList = new ArrayList<Map<String, Object>>();
		}
		return resultList;
	}
	
	@Override
	public Map<String, Object> getExportParam(Map<String, Object> map) {
		Map<String, Object> exportMap = new HashMap<String, Object>();
		exportMap.put(CherryConstants.ORGANIZATIONINFOID, map.get(CherryConstants.ORGANIZATIONINFOID));
		exportMap.put(CherryConstants.BRANDINFOID, map.get(CherryConstants.BRANDINFOID));
		String language = (String)map.get(CherryConstants.SESSION_CHERRY_LANGUAGE);
		
		exportMap.put("conditionMap", map);
		
        exportMap.put("sheetName", CherryUtil.getResourceValue("mb", "BINOLMBRPT05", language, "sheetName"));
        
        String conditionContent = this.getConditionStr(map);
		if(conditionContent != null && !"".equals(conditionContent)) {
			exportMap.put("header", conditionContent);
		}
        String statisticsType = ConvertUtil.getString(map.get("statisticsType"));
        List<String[]> titleRowList = new ArrayList<String[]>();
        if(statisticsType.equals("1")){
        	titleRowList.add(new String[]{"region", CherryUtil.getResourceValue("mb", "BINOLMBRPT05", language, "RPT05_consumeTimes"), "10", "", ""});
        } else if(statisticsType.equals("2")) {
        	titleRowList.add(new String[]{"region", CherryUtil.getResourceValue("mb", "BINOLMBRPT05", language, "RPT05_consumeAmount"), "10", "", ""});
        } else {
        	titleRowList.add(new String[]{"region", CherryUtil.getResourceValue("mb", "BINOLMBRPT05", language, "RPT05_memberAge"), "10", "", ""});
        }
        titleRowList.add(new String[]{"regionMemNum", CherryUtil.getResourceValue("mb", "BINOLMBRPT05", language, "RPT05_regionMemNum"), "20", "int", ""});
        titleRowList.add(new String[]{"memberNumRate", CherryUtil.getResourceValue("mb", "BINOLMBRPT05", language, "RPT05_memberNumRate"), "20", "right", ""});
        titleRowList.add(new String[]{"regionMemConsumeAmount", CherryUtil.getResourceValue("mb", "BINOLMBRPT05", language, "RPT05_regionMemConsumeAmount"), "20", "float", ""});
        titleRowList.add(new String[]{"consumeAmountRate", CherryUtil.getResourceValue("mb", "BINOLMBRPT05", language, "RPT05_consumeAmountRate"), "20", "right", ""});
        titleRowList.add(new String[]{"memberAverageConsume", CherryUtil.getResourceValue("mb", "BINOLMBRPT05", language, "RPT05_memberAverageConsume"), "20", "float", ""});
        exportMap.put("titleRows", titleRowList.toArray(new String[][]{}));
        
        return exportMap;
	}
	
	/**
	 * 导出文件的头部信息
	 * @param map
	 * @return
	 */
	private String getConditionStr(Map<String, Object> map) {
		StringBuffer condition = new StringBuffer();
		String language = (String)map.get(CherryConstants.SESSION_CHERRY_LANGUAGE);
		
		String saleDateStart = (String)map.get("startDate");
		String saleDateEnd = (String)map.get("endDate");
		String notLimit = CherryUtil.getResourceValue(null, language, "global.page.notLimit");
		StringBuffer saleDateCondition = new StringBuffer();
		if(saleDateStart != null && !"".equals(saleDateStart)) {
			if(saleDateEnd != null && !"".equals(saleDateEnd)) {
				saleDateCondition.append(saleDateStart+"\0~\0"+saleDateEnd);
			} else {
				saleDateCondition.append(saleDateStart+"\0~\0"+notLimit);
			}
		} else {
			if(saleDateEnd != null && !"".equals(saleDateEnd)) {
				saleDateCondition.append(notLimit+"\0~\0"+saleDateEnd);
			}
		}
		String paramValue = saleDateCondition.toString();
		if(paramValue != null && !"".equals(paramValue)) {
			String paramName = CherryUtil.getResourceValue("mb", "BINOLMBRPT05", language, "RPT05_date");
			condition.append(paramName + "：" + paramValue + "\0\0\0\0\0");
		}
		
		String channelName = (String)map.get("channelName");
		if(channelName != null && !"".equals(channelName)) {
			String paramName = CherryUtil.getResourceValue("mb", "BINOLMBRPT05", language, "RPT05_channel");
			paramValue = channelName;
			condition.append(paramName + "：" + paramValue + "\0\0\0\0\0");
		}
		
		String organizationName = (String)map.get("organizationName");
		if(organizationName != null && !"".equals(organizationName)) {
			String paramName = CherryUtil.getResourceValue("mb", "BINOLMBRPT05", language, "RPT05_organizationId");
			paramValue = organizationName;
			condition.append(paramName + "：" + paramValue + "\0\0\0\0\0");
		}
		
		return condition.toString();
	}
	
	@Override
	public List<Map<String, Object>> getDataList(Map<String, Object> map)
			throws Exception {
		String language = (String)map.get(CherryConstants.SESSION_CHERRY_LANGUAGE);
		List<Map<String, Object>> resultList = this.getRegionStatisticsList(map);
		if(resultList != null && !resultList.isEmpty()){
			// 最后一行数据为汇总信息
			Map<String, Object> resultMap = resultList.get(resultList.size()-1);
			String summary = ConvertUtil.getString(resultMap.get("region"));
			if("summary".equals(summary)) {
				resultMap.put("region", CherryUtil.getResourceValue("mb", "BINOLMBRPT05", language, "RPT05_summary"));
			}
		} else {
			resultList.add(new HashMap<String, Object>());
		}
		return resultList;
	}
	
}
