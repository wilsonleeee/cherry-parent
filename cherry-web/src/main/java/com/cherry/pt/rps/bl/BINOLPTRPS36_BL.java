package com.cherry.pt.rps.bl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.pt.rps.interfaces.BINOLPTRPS36_IF;
import com.cherry.pt.rps.service.BINOLPTRPS36_Service;

/**
 * 
 * @ClassName: BINOLPTRPS36_BL 
 * @Description: TODO(柜台月度人效明细BL) 
 * @author menghao
 * @version v1.0.0 2015-1-13 
 *
 */
public class BINOLPTRPS36_BL implements BINOLPTRPS36_IF {
	
	@Resource(name="binOLPTRPS36_Service")
	private BINOLPTRPS36_Service binOLPTRPS36_Service;

	@Override
	public List<Map<String, Object>> getDataList(Map<String, Object> map)
			throws Exception {
		// TODO Auto-generated method stub
		return this.getMonthPeopleEffectList(map);
	}

	@Override
	public int getMonthPeopleEffectCount(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return binOLPTRPS36_Service.getMonthPeopleEffectCount(map);
	}

	@Override
	public List<Map<String, Object>> getMonthPeopleEffectList(
			Map<String, Object> map) {
		// TODO Auto-generated method stub
		return binOLPTRPS36_Service.getMonthPeopleEffectList(map);
	}

	@Override
	public Map<String, Object> getExportParam(Map<String, Object> map) {
		String language = ConvertUtil.getString(map.get(CherryConstants.SESSION_LANGUAGE));
		String[][] array = {
				{"counterCode", CherryUtil.getResourceValue("pt", "BINOLPTRPS36", language, "RPS36_counterCode"), "20", "", ""},
		        {"counterName", CherryUtil.getResourceValue("pt", "BINOLPTRPS36", language, "RPS36_counterName"), "20", "", ""},
		        {"monthSaleAmount", CherryUtil.getResourceValue("pt", "BINOLPTRPS36", language, "RPS36_monthSaleAmount"), "20", "float", ""},
		        {"employeeNum", CherryUtil.getResourceValue("pt", "BINOLPTRPS36", language, "RPS36_employeeNum"), "20", "", ""},
		        {"numberOfDay", CherryUtil.getResourceValue("pt", "BINOLPTRPS36", language, "RPS36_numberOfDay"), "20", "int", ""},
		        {"monthPeopleEffect", CherryUtil.getResourceValue("pt", "BINOLPTRPS36", language, "RPS36_monthPeopleEffect"), "20", "", ""}
		};
		map.put("titleRows", array);
		map.put(CherryConstants.SORT_ID, "monthSaleAmount desc");
		return map;
	}

}
