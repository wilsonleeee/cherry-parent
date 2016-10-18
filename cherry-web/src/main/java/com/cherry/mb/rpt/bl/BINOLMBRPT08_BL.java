package com.cherry.mb.rpt.bl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.DateUtil;
import com.cherry.mb.rpt.service.BINOLMBRPT13_Service;

/**
 * 活动统计信息报表BL
 * 
 * @author WangCT
 * @version 1.0 2014/12/29
 */
public class BINOLMBRPT08_BL {
	
	/** 会员活动统计报表Service **/
	@Resource
	private BINOLMBRPT13_Service binOLMBRPT13_Service;
	
	/**
	 * 取得活动统计信息
	 * 
	 * @param map 检索条件
	 * @return 活动统计信息
	 */
	public Map<String, Object> getCamCountInfo(Map<String, Object> map) {
		
		this.setSearchMap(map);
		
		Map<String, Object> campCountInfo = new HashMap<String, Object>();
		// 取得活动预期到柜人数
		int bookCount = binOLMBRPT13_Service.getCampBookCount(map);
		campCountInfo.put("bookCount", bookCount);
		// 取得活动预约人数
		int orderCount = binOLMBRPT13_Service.getCampOrderCount(map);
		campCountInfo.put("orderCount", orderCount);
		// 取得活动实际到柜人数（会员） 
		int campMemGetCount = binOLMBRPT13_Service.getCampMemArriveCount(map);
		// 取得活动实际到柜人数（非会员）
		int campNomemGetCount = binOLMBRPT13_Service.getCampNomemArriveCount(map);
		// 活动实际到柜人数
		int getCount = campMemGetCount+campNomemGetCount;
		campCountInfo.put("getCount", getCount);
		if(bookCount != 0) {
			// 到柜率
			campCountInfo.put("getPercent", CherryUtil.percent(getCount, bookCount, 2));
		}
		
		// 取得活动购买金额、购买人数、新会员购买人数、新会员购买金额
		Map<String, Object> campSaleInfo = binOLMBRPT13_Service.getCampSaleInfo(map);
		if(campSaleInfo != null) {
			// 购买人数
			Integer memCount = (Integer)campSaleInfo.get("memCount");
			if(memCount == null) {
				memCount = 0;
			}
			// 新会员购买人数
			Integer newMemCount = (Integer)campSaleInfo.get("newMemCount");
			if(newMemCount == null) {
				newMemCount = 0;
			}
			// 老会员购买人数
			int oldMemCount = memCount - newMemCount;
			// 购买金额
			BigDecimal saleAmount = (BigDecimal)campSaleInfo.get("saleAmount");
			if(saleAmount == null) {
				saleAmount = new BigDecimal(0);
			}
			// 新会员购买金额
			BigDecimal newMemSaleAmount = (BigDecimal)campSaleInfo.get("newMemSaleAmount");
			if(newMemSaleAmount == null) {
				newMemSaleAmount = new BigDecimal(0);
			}
			// 老会员购买金额
			double oldMemSaleAmount = saleAmount.doubleValue()-newMemSaleAmount.doubleValue();
			campCountInfo.put("memCount", memCount);
			campCountInfo.put("newMemCount", newMemCount);
			campCountInfo.put("oldMemCount", oldMemCount);
			campCountInfo.put("saleAmount", saleAmount);
			campCountInfo.put("newMemSaleAmount", newMemSaleAmount);
			campCountInfo.put("oldMemSaleAmount", oldMemSaleAmount);
			if(getCount != 0) {
				// 购买率
				campCountInfo.put("salePercent", CherryUtil.percent(memCount, getCount, 2));
			}
		}
		return campCountInfo;
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
	
	public Map<String, Object> getExportParam(Map<String, Object> map) {
		
		Map<String, Object> exportMap = new HashMap<String, Object>();
		exportMap.put(CherryConstants.ORGANIZATIONINFOID, map.get(CherryConstants.ORGANIZATIONINFOID));
		exportMap.put(CherryConstants.BRANDINFOID, map.get(CherryConstants.BRANDINFOID));
		String language = (String)map.get(CherryConstants.SESSION_CHERRY_LANGUAGE);
		
		exportMap.put("conditionMap", map);
		
        exportMap.put("sheetName", CherryUtil.getResourceValue("mb", "BINOLMBRPT08", language, "sheetName"));
        
        String conditionContent = this.getConditionStr(map);
		if(conditionContent != null && !"".equals(conditionContent)) {
			exportMap.put("header", conditionContent);
		}
		
		// 设定标题行
		List<String[][]> titleList = new ArrayList<String[][]>();
		List<String[]> title1 = new ArrayList<String[]>();
		title1.add(new String[]{CherryUtil.getResourceValue("mb", "BINOLMBRPT08", language, "mbrpt08_orderCount"), "1", "1"});
		title1.add(new String[]{CherryUtil.getResourceValue("mb", "BINOLMBRPT08", language, "mbrpt08_bookCount"), "1", "1"});
		title1.add(new String[]{CherryUtil.getResourceValue("mb", "BINOLMBRPT08", language, "mbrpt08_getCount"), "1", "1"});
		title1.add(new String[]{CherryUtil.getResourceValue("mb", "BINOLMBRPT08", language, "mbrpt08_getPercent"), "1", "1"});
		title1.add(new String[]{CherryUtil.getResourceValue("mb", "BINOLMBRPT08", language, "mbrpt08_memCount"), "1", "1"});
		title1.add(new String[]{CherryUtil.getResourceValue("mb", "BINOLMBRPT08", language, "mbrpt08_saleAmount"), "1", "1"});
		title1.add(new String[]{CherryUtil.getResourceValue("mb", "BINOLMBRPT08", language, "mbrpt08_salePercent"), "1", "1"});
		title1.add(new String[]{CherryUtil.getResourceValue("mb", "BINOLMBRPT08", language, "mbrpt08_newMemCount"), "1", "1"});
		title1.add(new String[]{CherryUtil.getResourceValue("mb", "BINOLMBRPT08", language, "mbrpt08_oldMemCount"), "1", "1"});
		title1.add(new String[]{CherryUtil.getResourceValue("mb", "BINOLMBRPT08", language, "mbrpt08_newMemSaleAmount"), "1", "1"});
		title1.add(new String[]{CherryUtil.getResourceValue("mb", "BINOLMBRPT08", language, "mbrpt08_oldMemSaleAmount"), "1", "1"});
		titleList.add(title1.toArray(new String[][]{}));
		exportMap.put("titleList", titleList);
        
        List<String[]> titleRowList = new ArrayList<String[]>();
        titleRowList.add(new String[]{"orderCount", CherryUtil.getResourceValue("mb", "BINOLMBRPT08", language, "mbrpt08_orderCount"), "15", "int", ""});
        titleRowList.add(new String[]{"bookCount", CherryUtil.getResourceValue("mb", "BINOLMBRPT08", language, "mbrpt08_bookCount"), "15", "int", ""});
        titleRowList.add(new String[]{"getCount", CherryUtil.getResourceValue("mb", "BINOLMBRPT08", language, "mbrpt08_getCount"), "15", "int", ""});
        titleRowList.add(new String[]{"getPercent", CherryUtil.getResourceValue("mb", "BINOLMBRPT08", language, "mbrpt08_getPercent"), "15", "", ""});
        titleRowList.add(new String[]{"memCount", CherryUtil.getResourceValue("mb", "BINOLMBRPT08", language, "mbrpt08_memCount"), "15", "int", ""});
        titleRowList.add(new String[]{"saleAmount", CherryUtil.getResourceValue("mb", "BINOLMBRPT08", language, "mbrpt08_saleAmount"), "15", "float", ""});
        titleRowList.add(new String[]{"salePercent", CherryUtil.getResourceValue("mb", "BINOLMBRPT08", language, "mbrpt08_salePercent"), "15", "", ""});
        titleRowList.add(new String[]{"newMemCount", CherryUtil.getResourceValue("mb", "BINOLMBRPT08", language, "mbrpt08_newMemCount"), "15", "int", ""});
        titleRowList.add(new String[]{"oldMemCount", CherryUtil.getResourceValue("mb", "BINOLMBRPT08", language, "mbrpt08_oldMemCount"), "15", "int", ""});
        titleRowList.add(new String[]{"newMemSaleAmount", CherryUtil.getResourceValue("mb", "BINOLMBRPT08", language, "mbrpt08_newMemSaleAmount"), "15", "float", ""});
        titleRowList.add(new String[]{"oldMemSaleAmount", CherryUtil.getResourceValue("mb", "BINOLMBRPT08", language, "mbrpt08_newMemSaleAmount"), "15", "float", ""});
        exportMap.put("titleRows", titleRowList.toArray(new String[][]{}));
        
        return exportMap;
	}
	
	private String getConditionStr(Map<String, Object> map) {
		StringBuffer condition = new StringBuffer();
		String language = (String)map.get(CherryConstants.SESSION_CHERRY_LANGUAGE);
		
		String campaignName = (String)map.get("campaignName");
		if(campaignName != null && !"".equals(campaignName)) {
			String paramName = CherryUtil.getResourceValue("mb", "BINOLMBRPT08", language, "mbrpt08_campaignCode");
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
			String paramName = CherryUtil.getResourceValue("mb", "BINOLMBRPT08", language, "mbrpt08_dateRange");
			condition.append(paramName + "：" + paramValue + "\0\0\0\0\0");
		}
		
		return condition.toString();
	}

}
