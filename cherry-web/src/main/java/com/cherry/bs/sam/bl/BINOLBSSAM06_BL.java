package com.cherry.bs.sam.bl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.bs.sam.interfaces.BINOLBSSAM06_IF;
import com.cherry.bs.sam.service.BINOLBSSAM06_Service;
import com.cherry.cm.cmbussiness.bl.BINOLCM37_BL;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.ConvertUtil;

public class BINOLBSSAM06_BL implements BINOLBSSAM06_IF{

	@Resource
	private BINOLBSSAM06_Service binOLBSSAM06_Service;
	
	@Resource(name="binOLCM37_BL")
	private BINOLCM37_BL binOLCM37_BL;
	@Override
	public int getBAAttendanceCount(Map<String, Object> map) {
		return binOLBSSAM06_Service.getBAAttendanceCount(map);
	}
	@Override
	public List<Map<String, Object>> getBAAttendanceList(Map<String, Object> map) {
		return binOLBSSAM06_Service.getBAAttendanceList(map);
	}
	@Override
	public List<Map<String, Object>> getDataList(Map<String, Object> map) {
		String language = ConvertUtil.getString(map.get(CherryConstants.SESSION_LANGUAGE));
		List<Map<String, Object>> list =  binOLBSSAM06_Service.getBAAttendanceList(map);
		if(list!=null && list.size()>0){
			for(Map<String, Object> m : list){
				int stime = ConvertUtil.getInt(m.get("stime"));
				int time1 = ConvertUtil.getInt(m.get("time1"));
				int time2 = ConvertUtil.getInt(m.get("time2"));
				int time3 = ConvertUtil.getInt(m.get("time3"));
				int time4 = ConvertUtil.getInt(m.get("time4"));
				int xtime = ConvertUtil.getInt(m.get("xtime"));
				if(stime>0){
					m.put("stime", binOLCM37_BL.getResourceValue("BINOLBSSAM06", language, "BSSAM06_isCheck"));
				}else {
					m.put("stime", binOLCM37_BL.getResourceValue("BINOLBSSAM06", language, "BSSAM06_noCheck"));
				}
				if(time1>0){
					m.put("time1", binOLCM37_BL.getResourceValue("BINOLBSSAM06", language, "BSSAM06_isCheck"));
				}else {
					m.put("time1", binOLCM37_BL.getResourceValue("BINOLBSSAM06", language, "BSSAM06_noCheck"));
				}
				if(time2>0){
					m.put("time2", binOLCM37_BL.getResourceValue("BINOLBSSAM06", language, "BSSAM06_isCheck"));
				}else {
					m.put("time2", binOLCM37_BL.getResourceValue("BINOLBSSAM06", language, "BSSAM06_noCheck"));
				}
				if(time3>0){
					m.put("time3", binOLCM37_BL.getResourceValue("BINOLBSSAM06", language, "BSSAM06_isCheck"));
				}else {
					m.put("time3", binOLCM37_BL.getResourceValue("BINOLBSSAM06", language, "BSSAM06_noCheck"));
				}
				if(time4>0){
					m.put("time4", binOLCM37_BL.getResourceValue("BINOLBSSAM06", language, "BSSAM06_isCheck"));
				}else {
					m.put("time4", binOLCM37_BL.getResourceValue("BINOLBSSAM06", language, "BSSAM06_noCheck"));
				}
				if(xtime>0){
					m.put("xtime", binOLCM37_BL.getResourceValue("BINOLBSSAM06", language, "BSSAM06_isCheck"));
				}else {
					m.put("xtime", binOLCM37_BL.getResourceValue("BINOLBSSAM06", language, "BSSAM06_noCheck"));
				}
			}
		}
		return list;
	}
	@Override
	public Map<String, Object> getExportMap(Map<String, Object> map) {
		String language = ConvertUtil.getString(map.get(CherryConstants.SESSION_LANGUAGE));
		String[][] array = {
				{ "departName", binOLCM37_BL.getResourceValue("BINOLBSSAM06", language, "BSSAM06_departName"), "20", "", "" },
				{ "employeeName", binOLCM37_BL.getResourceValue("BINOLBSSAM06", language, "BSSAM06_employeeName"), "20", "", "" },
				{ "workDate", binOLCM37_BL.getResourceValue("BINOLBSSAM06", language, "BASAM06_workDate"), "20", "", "" },
				{ "stime", binOLCM37_BL.getResourceValue("BINOLBSSAM06", language, "BSSAM06_stime"), "19", "", "" },
				{ "time1", binOLCM37_BL.getResourceValue("BINOLBSSAM06", language, "BSSAM06_time1"), "19", "", "" },
				{ "time2", binOLCM37_BL.getResourceValue("BINOLBSSAM06", language, "BSSAM06_time2"), "19", "", "" },
				{ "time3", binOLCM37_BL.getResourceValue("BINOLBSSAM06", language, "BSSAM06_time3"), "19", "", "" },
				{ "time4", binOLCM37_BL.getResourceValue("BINOLBSSAM06", language, "BSSAM06_time4"), "19", "", "" },
				{ "xtime", binOLCM37_BL.getResourceValue("BINOLBSSAM06", language, "BSSAM06_xtime"), "19", "", "" }
		};
		int dataLen = binOLBSSAM06_Service.getBAAttendanceCount(map);
		map.put("dataLen", dataLen);
		map.put("sheetName", binOLCM37_BL.getResourceValue("BINOLBSSAM06", language, "BSSAM06_sheetName"));
		map.put("downloadFileName", binOLCM37_BL.getResourceValue("BINOLBSSAM06", language, "BSSAM06_downloadFileName"));
		map.put("titleRows", array);
		map.put(CherryConstants.SORT_ID, "workDate desc");
		return map;
	}
}
