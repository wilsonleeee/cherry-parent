package com.cherry.mb.rpt.bl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbussiness.interfaces.BINOLCM37_IF;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.CherryUtil;
import com.cherry.mb.rpt.service.BINOLMBRPT07_Service;

/**
 * 回柜会员销售明细报表BL
 * 
 * @author WangCT
 * @version 1.0 2015/01/09
 */
public class BINOLMBRPT10_BL implements BINOLCM37_IF {
	
	/** 会员活动统计报表Service **/
	@Resource
	private BINOLMBRPT07_Service binOLMBRPT07_Service;
	
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

	@Override
	public List<Map<String, Object>> getDataList(Map<String, Object> map)
			throws Exception {
		return this.getSaleDetailList(map);
	}
	
	public Map<String, Object> getExportParam(Map<String, Object> map) {
		
		Map<String, Object> exportMap = new HashMap<String, Object>();
		exportMap.put(CherryConstants.ORGANIZATIONINFOID, map.get(CherryConstants.ORGANIZATIONINFOID));
		exportMap.put(CherryConstants.BRANDINFOID, map.get(CherryConstants.BRANDINFOID));
		String language = (String)map.get(CherryConstants.SESSION_CHERRY_LANGUAGE);
		
		exportMap.put("conditionMap", map);
		
        exportMap.put("sheetName", CherryUtil.getResourceValue("mb", "BINOLMBRPT10", language, "sheetName"));
        
        String conditionContent = this.getConditionStr(map);
		if(conditionContent != null && !"".equals(conditionContent)) {
			exportMap.put("header", conditionContent);
		}
        
        List<String[]> titleRowList = new ArrayList<String[]>();
        titleRowList.add(new String[]{"memberName", CherryUtil.getResourceValue("mb", "BINOLMBRPT10", language, "mbrpt10_memberName"), "20", "", ""});
        titleRowList.add(new String[]{"memCode", CherryUtil.getResourceValue("mb", "BINOLMBRPT10", language, "mbrpt10_memCode"), "20", "", ""});
        titleRowList.add(new String[]{"saleType", CherryUtil.getResourceValue("mb", "BINOLMBRPT10", language, "mbrpt10_saleType"), "15", "", "1055"});
        titleRowList.add(new String[]{"departCode", CherryUtil.getResourceValue("mb", "BINOLMBRPT10", language, "mbrpt10_departCode"), "20", "", ""});
        titleRowList.add(new String[]{"departName", CherryUtil.getResourceValue("mb", "BINOLMBRPT10", language, "mbrpt10_departName"), "20", "", ""});
        titleRowList.add(new String[]{"unitCode", CherryUtil.getResourceValue("mb", "BINOLMBRPT10", language, "mbrpt10_unitCode"), "20", "", ""});
        titleRowList.add(new String[]{"barCode", CherryUtil.getResourceValue("mb", "BINOLMBRPT10", language, "mbrpt10_barCode"), "20", "", ""});
        titleRowList.add(new String[]{"productName", CherryUtil.getResourceValue("mb", "BINOLMBRPT10", language, "mbrpt10_productName"), "20", "", ""});
        titleRowList.add(new String[]{"saleTime", CherryUtil.getResourceValue("mb", "BINOLMBRPT10", language, "mbrpt10_saleTime"), "20", "", ""});
        titleRowList.add(new String[]{"buyAmount", CherryUtil.getResourceValue("mb", "BINOLMBRPT10", language, "mbrpt10_buyAmount"), "15", "float", ""});
        exportMap.put("titleRows", titleRowList.toArray(new String[][]{}));
        
        return exportMap;
	}
	
	private String getConditionStr(Map<String, Object> map) {
		StringBuffer condition = new StringBuffer();
		String language = (String)map.get(CherryConstants.SESSION_CHERRY_LANGUAGE);
		
		String campaignName = (String)map.get("campaignName");
		if(campaignName != null && !"".equals(campaignName)) {
			String paramName = CherryUtil.getResourceValue("mb", "BINOLMBRPT10", language, "mbrpt10_campaignCode");
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
			String paramName = CherryUtil.getResourceValue("mb", "BINOLMBRPT10", language, "mbrpt10_dateRange");
			condition.append(paramName + "：" + paramValue + "\0\0\0\0\0");
		}
		
		return condition.toString();
	}

}
