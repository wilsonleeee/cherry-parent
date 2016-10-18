package com.cherry.mb.rpt.bl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbussiness.interfaces.BINOLCM37_IF;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.DateUtil;
import com.cherry.mb.rpt.service.BINOLMBRPT13_Service;

/**
 * 按柜台统计活动信息报表BL
 * 
 * @author WangCT
 * @version 1.0 2014/12/25
 */
public class BINOLMBRPT09_BL implements BINOLCM37_IF {
	
	/** 会员活动统计报表Service **/
	@Resource
	private BINOLMBRPT13_Service binOLMBRPT13_Service;
	
	/**
	 * 取得活动按柜台统计信息件数
	 * 
	 * @param map 检索条件
	 * @return 活动按柜台统计信息件数
	 */
	public int getCountByCounterCount(Map<String, Object> map) {
		// 取得活动按柜台统计信息件数
		return binOLMBRPT13_Service.getCampCountInfoByCounterCount(map);
	}
	
	/**
	 * 取得活动按柜台统计信息List
	 * 
	 * @param map 检索条件
	 * @return 活动按柜台统计信息List
	 */
	public List<Map<String, Object>> getCountByCounterList(Map<String, Object> map) {
		// 取得活动按柜台统计信息
		List<Map<String, Object>> countByCounterList = binOLMBRPT13_Service.getCampCountInfoByCounterList(map);
		if(countByCounterList != null && !countByCounterList.isEmpty()) {
			for(int i = 0; i < countByCounterList.size(); i++) {
				Map<String, Object> countInfo = countByCounterList.get(i);
				// 取得活动预期到柜人数
				int bookCount = (Integer)countInfo.get("bookCount");
				// 活动实际到柜人数
				int getCount = (Integer)countInfo.get("getCount");
				// 购买人数
				int memCount = (Integer)countInfo.get("memCount");
				// 新会员购买人数
				int newMemCount = (Integer)countInfo.get("newMemCount");
				// 购买金额
				BigDecimal saleAmount = (BigDecimal)countInfo.get("saleAmount");
				// 新会员购买金额
				BigDecimal newMemSaleAmount = (BigDecimal)countInfo.get("newMemSaleAmount");
				// 老会员购买人数
				int oldMemCount = memCount - newMemCount;
				// 老会员购买金额
				double oldMemSaleAmount = saleAmount.doubleValue()-newMemSaleAmount.doubleValue();
				countInfo.put("oldMemCount", oldMemCount);
				countInfo.put("oldMemSaleAmount", oldMemSaleAmount);
				if(bookCount != 0) {
					// 到柜率
					countInfo.put("getPercent", CherryUtil.percent(getCount, bookCount, 2));
				}
				if(getCount != 0) {
					// 购买率
					countInfo.put("salePercent", CherryUtil.percent(memCount, getCount, 2));
				}
			}
		}
		return countByCounterList;
	}
	
	/**
	 * 取得活动List
	 * 
	 * @param map
	 *            检索条件
	 * @return 活动List
	 */
	public List<Map<String, Object>> getCampaignList(
			Map<String, Object> map) {
		// 取得活动List
		return binOLMBRPT13_Service.getCampaignList(map);
	}
	
	/**
	 * 设置查询条件处理
	 * 
	 * @param map 检索条件
	 */
	public void setSearchMap(Map<String, Object> map) {
		String startDate = (String)map.get("startDate");
		String endDate = (String)map.get("endDate");
		if(startDate != null && !"".equals(startDate)) {
			String startDateTime = DateUtil.suffixDate(startDate, 0);
			map.put("startDateTime", startDateTime);
		}
		if(endDate != null && !"".equals(endDate)) {
			String endDateTime = DateUtil.suffixDate(endDate, 1);
			map.put("endDateTime", endDateTime);
		}
	}

	@Override
	public List<Map<String, Object>> getDataList(Map<String, Object> map)
			throws Exception {
		return this.getCountByCounterList(map);
	}
	
	public Map<String, Object> getExportParam(Map<String, Object> map) {
		
		Map<String, Object> exportMap = new HashMap<String, Object>();
		exportMap.put(CherryConstants.ORGANIZATIONINFOID, map.get(CherryConstants.ORGANIZATIONINFOID));
		exportMap.put(CherryConstants.BRANDINFOID, map.get(CherryConstants.BRANDINFOID));
		String language = (String)map.get(CherryConstants.SESSION_CHERRY_LANGUAGE);
		
		exportMap.put("conditionMap", map);
		
        exportMap.put("sheetName", CherryUtil.getResourceValue("mb", "BINOLMBRPT09", language, "sheetName"));
        
        String conditionContent = this.getConditionStr(map);
		if(conditionContent != null && !"".equals(conditionContent)) {
			exportMap.put("header", conditionContent);
		}
        
        List<String[]> titleRowList = new ArrayList<String[]>();
        titleRowList.add(new String[]{"cityName", CherryUtil.getResourceValue("mb", "BINOLMBRPT09", language, "mbrpt09_cityName"), "15", "", ""});
        titleRowList.add(new String[]{"departCode", CherryUtil.getResourceValue("mb", "BINOLMBRPT09", language, "mbrpt09_counterCode"), "20", "", ""});
        titleRowList.add(new String[]{"departName", CherryUtil.getResourceValue("mb", "BINOLMBRPT09", language, "mbrpt09_counterName"), "20", "", ""});
        titleRowList.add(new String[]{"orderCount", CherryUtil.getResourceValue("mb", "BINOLMBRPT09", language, "mbrpt09_orderCount"), "15", "int", ""});
        titleRowList.add(new String[]{"bookCount", CherryUtil.getResourceValue("mb", "BINOLMBRPT09", language, "mbrpt09_bookCount"), "15", "int", ""});
        titleRowList.add(new String[]{"getCount", CherryUtil.getResourceValue("mb", "BINOLMBRPT09", language, "mbrpt09_getCount"), "15", "int", ""});
        titleRowList.add(new String[]{"getPercent", CherryUtil.getResourceValue("mb", "BINOLMBRPT09", language, "mbrpt09_getPercent"), "15", "", ""});
        titleRowList.add(new String[]{"memCount", CherryUtil.getResourceValue("mb", "BINOLMBRPT09", language, "mbrpt09_memCount"), "15", "int", ""});
        titleRowList.add(new String[]{"saleAmount", CherryUtil.getResourceValue("mb", "BINOLMBRPT09", language, "mbrpt09_saleAmount"), "15", "float", ""});
        titleRowList.add(new String[]{"salePercent", CherryUtil.getResourceValue("mb", "BINOLMBRPT09", language, "mbrpt09_salePercent"), "15", "", ""});
        titleRowList.add(new String[]{"newMemCount", CherryUtil.getResourceValue("mb", "BINOLMBRPT09", language, "mbrpt09_newMemCount"), "15", "int", ""});
        titleRowList.add(new String[]{"oldMemCount", CherryUtil.getResourceValue("mb", "BINOLMBRPT09", language, "mbrpt09_oldMemCount"), "15", "int", ""});
        titleRowList.add(new String[]{"newMemSaleAmount", CherryUtil.getResourceValue("mb", "BINOLMBRPT09", language, "mbrpt09_newMemSaleAmount"), "15", "float", ""});
        titleRowList.add(new String[]{"oldMemSaleAmount", CherryUtil.getResourceValue("mb", "BINOLMBRPT09", language, "mbrpt09_oldMemSaleAmount"), "15", "float", ""});
        exportMap.put("titleRows", titleRowList.toArray(new String[][]{}));
        
        return exportMap;
	}
	
	private String getConditionStr(Map<String, Object> map) {
		StringBuffer condition = new StringBuffer();
		String language = (String)map.get(CherryConstants.SESSION_CHERRY_LANGUAGE);
		
		String campaignName = (String)map.get("campaignName");
		if(campaignName != null && !"".equals(campaignName)) {
			String paramName = CherryUtil.getResourceValue("mb", "BINOLMBRPT09", language, "mbrpt09_campaignCode");
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
			String paramName = CherryUtil.getResourceValue("mb", "BINOLMBRPT09", language, "mbrpt09_dateRange");
			condition.append(paramName + "：" + paramValue + "\0\0\0\0\0");
		}
		
		return condition.toString();
	}

}
