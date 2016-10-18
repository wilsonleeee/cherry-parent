package com.cherry.mb.rpt.bl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CodeTable;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.mb.rpt.interfaces.BINOLMBRPT04_IF;
import com.cherry.mb.rpt.service.BINOLMBRPT04_Service;

/**
 * 
* @ClassName: BINOLMBRPT04_BL 
* @Description: TODO(会员发展统计BL) 
* @author menghao
* @version v1.0.0 2015-1-5 
*
 */
public class BINOLMBRPT04_BL implements BINOLMBRPT04_IF {
	
	/**会员发展统计报表Service **/
	@Resource(name="binOLMBRPT04_Service")
	private BINOLMBRPT04_Service binOLMBRPT04_Service;
	
	@Resource(name="CodeTable")
	private CodeTable codeTable;
	
	/**
	 * 取得汇总信息
	 * @param map
	 * @return
	 */
	public Map<String, Object> getSumInfo(Map<String, Object> map){
		return binOLMBRPT04_Service.getSumInfo(map);
	}
	
	/**
	 * 取得统计结果集数量
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public int getMemberDevelopRptCount(Map<String, Object> map){
		return binOLMBRPT04_Service.getMemberDevelopCount(map);
	}
	
	/**
	 * 取得会员发展统计信息LIST
	 * @param map
	 * @return
	 * @throws Exception 
	 */
	@Override
	public List<Map<String, Object>> getMemberDevelopRptList(Map<String, Object> map) throws Exception {
		//  会员销售统计结果
		return binOLMBRPT04_Service.getMemberDevelopList(map);
	}
	
	@Override
	public Map<String, Object> getExportParam(Map<String, Object> map) {
        String language = ConvertUtil.getString(map.get(CherryConstants.SESSION_LANGUAGE));
		String[][] array = {
				{"counterCode", CherryUtil.getResourceValue("mb", "BINOLMBRPT04", language, "RPT04_counterCode"), "20", "", ""},
		        {"counterName", CherryUtil.getResourceValue("mb", "BINOLMBRPT04", language, "RPT04_counterCode"), "20", "", ""},
		        {"busniessPrincipal", CherryUtil.getResourceValue("mb", "BINOLMBRPT04", language, "RPT04_busniessPrincipal"), "20", "", ""},
		        {"totalMemberNum", CherryUtil.getResourceValue("mb", "BINOLMBRPT04", language, "RPT04_totalMemberNum"), "20", "int", ""},
		        {"totalMemberSaleAmount", CherryUtil.getResourceValue("mb", "BINOLMBRPT04", language, "RPT04_totalMemberSaleAmount"), "20", "float", ""},
		        {"newMemberNum", CherryUtil.getResourceValue("mb", "BINOLMBRPT04", language, "RPT04_newMemberNum"), "20", "int", ""},
		        {"newMemberSaleAmount", CherryUtil.getResourceValue("mb", "BINOLMBRPT04", language, "RPT04_newMemSaleAmount"), "20", "float", ""},
		        {"buyBackMemberNum", CherryUtil.getResourceValue("mb", "BINOLMBRPT04", language, "RPT04_buyBackMemberNum"), "20", "int", ""},
		        {"buyBackMemSaleAmount", CherryUtil.getResourceValue("mb", "BINOLMBRPT04", language, "RPT04_buyBackMemSaleAmount"), "20", "float", ""},
		        {"newMemberProportion", CherryUtil.getResourceValue("mb", "BINOLMBRPT04", language, "RPT04_newMemberProportion"), "20", "float", ""},
		        {"newMemConsumeAverage", CherryUtil.getResourceValue("mb", "BINOLMBRPT04", language, "RPT04_newMemConsumeAverage"), "20", "float", ""}
		};
		int dataLen = this.getMemberDevelopRptCount(map);
		map.put("dataLen", dataLen);
		map.put("sheetName", CherryUtil.getResourceValue("mb", "BINOLMBRPT04", language, "sheetName"));
        map.put("downloadFileName", CherryUtil.getResourceValue("mb", "BINOLMBRPT04", language, "downloadFileName"));
		map.put("titleRows", array);
		map.put(CherryConstants.SORT_ID, "counterCode desc");
		return map;
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
			String paramName = CherryUtil.getResourceValue("mb", "BINOLMBRPT04", language, "RPT04_date");
			condition.append(paramName + "：" + paramValue + "\0\0\0\0\0");
		}
		
		String channelName = (String)map.get("channelName");
		if(channelName != null && !"".equals(channelName)) {
			String paramName = CherryUtil.getResourceValue("mb", "BINOLMBRPT04", language, "RPT04_channel");
			paramValue = channelName;
			condition.append(paramName + "：" + paramValue + "\0\0\0\0\0");
		}
		
		String organizationName = (String)map.get("organizationName");
		if(organizationName != null && !"".equals(organizationName)) {
			String paramName = CherryUtil.getResourceValue("mb", "BINOLMBRPT04", language, "RPT04_organizationId");
			paramValue = organizationName;
			condition.append(paramName + "：" + paramValue + "\0\0\0\0\0");
		}
		
		String belongFaction = (String)map.get("belongFaction");
		if(belongFaction != null && !"".equals(belongFaction)) {
			String paramName = CherryUtil.getResourceValue("mb", "BINOLMBRPT04", language, "RPT04_belongFaction");
			paramValue=codeTable.getVal("1309", belongFaction);
			condition.append(paramName + "：" + paramValue + "\0\0\0\0\0");
		}
		
		return condition.toString();
	}
	
	@Override
	public List<Map<String, Object>> getDataList(Map<String, Object> map)
			throws Exception {
		return this.getMemberDevelopRptList(map);
	}
	

}
