package com.cherry.pt.rps.bl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.pt.rps.interfaces.BINOLPTRPS35_IF;
import com.cherry.pt.rps.service.BINOLPTRPS35_Service;

public class BINOLPTRPS35_BL implements BINOLPTRPS35_IF {
	
	@Resource(name="binOLPTRPS35_Service")
	private BINOLPTRPS35_Service binOLPTRPS35_Service;

	@Override
	public List<Map<String, Object>> getDataList(Map<String, Object> map)
			throws Exception {
		// TODO Auto-generated method stub
		return this.getMonthPingEffectList(map);
	}

	@Override
	public int getMonthPingEffectCount(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return binOLPTRPS35_Service.getMonthPingEffectCount(map);
	}

	@Override
	public List<Map<String, Object>> getMonthPingEffectList(
			Map<String, Object> map) {
		// TODO Auto-generated method stub
		return binOLPTRPS35_Service.getMonthPingEffectList(map);
	}

	@Override
	public Map<String, Object> getExportParam(Map<String, Object> map) {
		String language = ConvertUtil.getString(map.get(CherryConstants.SESSION_LANGUAGE));
		String[][] array = {
				{"counterCode", CherryUtil.getResourceValue("pt", "BINOLPTRPS35", language, "RPS35_counterCode"), "20", "", ""},
		        {"counterName", CherryUtil.getResourceValue("pt", "BINOLPTRPS35", language, "RPS35_counterName"), "20", "", ""},
		        {"monthSaleAmount", CherryUtil.getResourceValue("pt", "BINOLPTRPS35", language, "RPS35_monthSaleAmount"), "20", "float", ""},
		        {"businessArea", CherryUtil.getResourceValue("pt", "BINOLPTRPS35", language, "RPS35_businessArea"), "20", "right", ""},
		        {"numberOfDay", CherryUtil.getResourceValue("pt", "BINOLPTRPS35", language, "RPS35_numberOfDay"), "20", "int", ""},
		        {"monthPingEffect", CherryUtil.getResourceValue("pt", "BINOLPTRPS35", language, "RPS35_monthPingEffect"), "20", "right", ""}
		};
		map.put("titleRows", array);
		map.put(CherryConstants.SORT_ID, "monthSaleAmount desc");
		return map;
	}

}
