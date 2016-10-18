package com.cherry.mb.rpt.bl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbussiness.interfaces.BINOLCM37_IF;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.DateUtil;
import com.cherry.mb.rpt.service.BINOLMBRPT07_Service;

/**
 * 活动预约明细报表BL
 * 
 * @author WangCT
 * @version 1.0 2014/12/29
 */
public class BINOLMBRPT07_BL implements BINOLCM37_IF {
	
	/** 会员活动统计报表Service **/
	@Resource
	private BINOLMBRPT07_Service binOLMBRPT07_Service;
	
	/**
	 * 取得活动信息件数
	 * 
	 * @param map
	 *            检索条件
	 * @return 活动信息件数
	 */
	public int getCampaignInfoCount(Map<String, Object> map) {
		// 取得活动信息件数
		return binOLMBRPT07_Service.getCampaignInfoCount(map);
	}

	/**
	 * 取得活动信息List
	 * 
	 * @param map
	 *            检索条件
	 * @return 活动信息List
	 */
	public List<Map<String, Object>> getCampaignInfoList(
			Map<String, Object> map) {
		// 取得柜台预约统计信息List
		return binOLMBRPT07_Service.getCampaignInfoList(map);
	}
	
	/**
	 * 获取销售明细统计信息
	 * 
	 * @param map
	 *            检索条件
	 * @return 销售明细统计信息
	 */
	public Map<String, Object> getSaleDetailCountInfo(Map<String, Object> map) {
		// 获取销售明细记录单数量
		return binOLMBRPT07_Service.getSaleDetailCountInfo(map);
	}

	/**
	 * 获取销售明细记录list
	 * 
	 * @param map
	 *            检索条件
	 * @return 销售明细记录list
	 */
	public List<Map<String, Object>> getSaleDetailList(
			Map<String, Object> map) {
		// 获取销售明细记录list
		return binOLMBRPT07_Service.getSaleDetailList(map);
	}
	
	public void setSearchMap(Map<String, Object> map) {
		String startDate = (String)map.get("startDate");
		String endDate = (String)map.get("endDate");
		if(startDate != null && !"".equals(startDate)) {
			String startDateTime = DateUtil.suffixDate(startDate, 0);
			map.put("finishTimeStart", startDateTime);
			map.put("saleDateStart", startDate);
		}
		if(endDate != null && !"".equals(endDate)) {
			String endDateTime = DateUtil.suffixDate(endDate, 1);
			map.put("finishTimeEnd", endDateTime);
			map.put("saleDateEnd", endDate);
		}
	}

	@Override
	public List<Map<String, Object>> getDataList(Map<String, Object> map)
			throws Exception {
		List<Map<String, Object>> dataList = this.getCampaignInfoList(map);
		if(dataList != null && !dataList.isEmpty()) {
			String language = (String)map.get(CherryConstants.SESSION_CHERRY_LANGUAGE);
			for(int i = 0; i < dataList.size(); i++) {
				Integer state = (Integer)dataList.get(i).get("state");
				Integer memFlag = (Integer)dataList.get(i).get("memFlag");
				Integer backFlag = (Integer)dataList.get(i).get("backFlag");
				if(state == 1) {
					dataList.get(i).put("stateText", CherryUtil.getResourceValue("mb", "BINOLMBRPT07", language, "mbrpt07_state1"));
				} else {
					dataList.get(i).put("stateText", CherryUtil.getResourceValue("mb", "BINOLMBRPT07", language, "mbrpt07_state0"));
				}
				if(memFlag == 1) {
					dataList.get(i).put("memFlagText", CherryUtil.getResourceValue("mb", "BINOLMBRPT07", language, "mbrpt07_memFlag1"));
				} else if(memFlag == 2) {
					dataList.get(i).put("memFlagText", CherryUtil.getResourceValue("mb", "BINOLMBRPT07", language, "mbrpt07_memFlag2"));
				} else {
					dataList.get(i).put("memFlagText", CherryUtil.getResourceValue("mb", "BINOLMBRPT07", language, "mbrpt07_memFlag0"));
				}
				if(backFlag == 1) {
					dataList.get(i).put("backFlagText", CherryUtil.getResourceValue("mb", "BINOLMBRPT07", language, "mbrpt07_backFlag1"));
				} else {
					dataList.get(i).put("backFlagText", CherryUtil.getResourceValue("mb", "BINOLMBRPT07", language, "mbrpt07_backFlag0"));
				}
			}
		}
		return dataList;
	}
	
	public Map<String, Object> getExportParam(Map<String, Object> map) {
		
		Map<String, Object> exportMap = new HashMap<String, Object>();
		exportMap.put(CherryConstants.ORGANIZATIONINFOID, map.get(CherryConstants.ORGANIZATIONINFOID));
		exportMap.put(CherryConstants.BRANDINFOID, map.get(CherryConstants.BRANDINFOID));
		String language = (String)map.get(CherryConstants.SESSION_CHERRY_LANGUAGE);
		
		exportMap.put("conditionMap", map);
		
        exportMap.put("sheetName", CherryUtil.getResourceValue("mb", "BINOLMBRPT07", language, "sheetName"));
        
        String conditionContent = this.getConditionStr(map);
		if(conditionContent != null && !"".equals(conditionContent)) {
			exportMap.put("header", conditionContent);
		}
        
        List<String[]> titleRowList = new ArrayList<String[]>();
        titleRowList.add(new String[]{"campaignOrderTime", CherryUtil.getResourceValue("mb", "BINOLMBRPT07", language, "mbrpt07_campaignOrderTime"), "20", "", ""});
        titleRowList.add(new String[]{"finishTime", CherryUtil.getResourceValue("mb", "BINOLMBRPT07", language, "mbrpt07_finishTime"), "20", "", ""});
        titleRowList.add(new String[]{"nikeName", CherryUtil.getResourceValue("mb", "BINOLMBRPT07", language, "mbrpt07_nikeName"), "20", "", ""});
        titleRowList.add(new String[]{"memName", CherryUtil.getResourceValue("mb", "BINOLMBRPT07", language, "mbrpt07_memName"), "20", "", ""});
        titleRowList.add(new String[]{"memCode", CherryUtil.getResourceValue("mb", "BINOLMBRPT07", language, "mbrpt07_memCode"), "20", "", ""});
        titleRowList.add(new String[]{"mobile", CherryUtil.getResourceValue("mb", "BINOLMBRPT07", language, "mbrpt07_mobile"), "20", "", ""});
        titleRowList.add(new String[]{"stateText", CherryUtil.getResourceValue("mb", "BINOLMBRPT07", language, "mbrpt07_state"), "15", "", ""});
        titleRowList.add(new String[]{"memFlagText", CherryUtil.getResourceValue("mb", "BINOLMBRPT07", language, "mbrpt07_memFlag"), "15", "", ""});
        titleRowList.add(new String[]{"cityName", CherryUtil.getResourceValue("mb", "BINOLMBRPT07", language, "mbrpt07_cityName"), "20", "", ""});
        titleRowList.add(new String[]{"counterCode", CherryUtil.getResourceValue("mb", "BINOLMBRPT07", language, "mbrpt07_counterCode"), "20", "", ""});
        titleRowList.add(new String[]{"nameForeign", CherryUtil.getResourceValue("mb", "BINOLMBRPT07", language, "mbrpt07_nameForeign"), "20", "", ""});
        titleRowList.add(new String[]{"counterName", CherryUtil.getResourceValue("mb", "BINOLMBRPT07", language, "mbrpt07_counterName"), "20", "", ""});
        titleRowList.add(new String[]{"saleAmount", CherryUtil.getResourceValue("mb", "BINOLMBRPT07", language, "mbrpt07_saleAmount"), "20", "float", ""});
        titleRowList.add(new String[]{"backFlagText", CherryUtil.getResourceValue("mb", "BINOLMBRPT07", language, "mbrpt07_backFlag"), "15", "", ""});
        exportMap.put("titleRows", titleRowList.toArray(new String[][]{}));
        
        return exportMap;
	}
	
	private String getConditionStr(Map<String, Object> map) {
		StringBuffer condition = new StringBuffer();
		String language = (String)map.get(CherryConstants.SESSION_CHERRY_LANGUAGE);
		
		String campaignName = (String)map.get("campaignName");
		if(campaignName != null && !"".equals(campaignName)) {
			String paramName = CherryUtil.getResourceValue("mb", "BINOLMBRPT07", language, "mbrpt07_campaignCode");
			String paramValue = campaignName;
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
		String paramValue = saleDateCondition.toString();
		if(paramValue != null && !"".equals(paramValue)) {
			String paramName = CherryUtil.getResourceValue("mb", "BINOLMBRPT07", language, "mbrpt07_dateRange");
			condition.append(paramName + "：" + paramValue + "\0\0\0\0\0");
		}
		
		return condition.toString();
	}

}
