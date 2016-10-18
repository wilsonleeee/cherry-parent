package com.cherry.bs.sam.bl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.bs.sam.interfaces.BINOLBSSAM07_IF;
import com.cherry.bs.sam.service.BINOLBSSAM07_Service;
import com.cherry.cm.cmbussiness.bl.BINOLCM37_BL;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.ConvertUtil;

public class BINOLBSSAM07_BL implements BINOLBSSAM07_IF{

	@Resource
	private BINOLBSSAM07_Service binOLBSSAM07_Service;
	
	@Resource(name="binOLCM37_BL")
	private BINOLCM37_BL binOLCM37_BL;
	@Override
	public int getBAAttendanceCount(Map<String, Object> map) {
		return binOLBSSAM07_Service.getBAAttendanceCount(map);
	}
	@Override
	public List<Map<String, Object>> getBAAttendanceList(Map<String, Object> map) {
		return binOLBSSAM07_Service.getBAAttendanceList(map);
	}
	@Override
	public List<Map<String, Object>> getDataList(Map<String, Object> map) {
		String language = ConvertUtil.getString(map.get(CherryConstants.SESSION_LANGUAGE));
		List<Map<String, Object>> list = binOLBSSAM07_Service.getBAAttendanceList(map);
		if(list!=null && list.size()>0){
			for(Map<String, Object> m : list){
				int attendanceType = ConvertUtil.getInt(m.get("attendanceType"));
				if(attendanceType==1){
					m.put("attendanceType", binOLCM37_BL.getResourceValue("BINOLBSSAM07", language, "BSSAM07_sCheck"));
				}else if(attendanceType==0){
					m.put("attendanceType", binOLCM37_BL.getResourceValue("BINOLBSSAM07", language, "BSSAM07_xCheck"));
				}else if(attendanceType==2){
					m.put("attendanceType", binOLCM37_BL.getResourceValue("BINOLBSSAM07", language, "BSSAM07_check"));
				}
			}
		}
		return list;
	}
	@Override
	public Map<String, Object> getExportDetailMap(Map<String, Object> map) {
		String language = ConvertUtil.getString(map.get(CherryConstants.SESSION_LANGUAGE));
		String[][] array = {
				{ "departName", binOLCM37_BL.getResourceValue("BINOLBSSAM07", language, "BSSAM07_departName"), "20", "", "" },
				{ "employeeName", binOLCM37_BL.getResourceValue("BINOLBSSAM07", language, "BSSAM07_employeeName"), "20", "", "" },
				{ "workDate", binOLCM37_BL.getResourceValue("BINOLBSSAM07", language, "BASAM07_workDate"), "20", "", "" },
				{ "attendanceType", binOLCM37_BL.getResourceValue("BINOLBSSAM07", language, "BSSAM07_attendanceType"), "19", "", "" },
				{ "attendanceDateTime", binOLCM37_BL.getResourceValue("BINOLBSSAM07", language, "BSSAM07_attendanceDateTime"), "19", "", "" }
		};
		int dataLen = binOLBSSAM07_Service.getBAAttendanceCount(map);
		map.put("dataLen", dataLen);
		map.put("sheetName", binOLCM37_BL.getResourceValue("BINOLBSSAM07", language, "BSSAM07_sheetDetailName"));
		map.put("downloadFileName", binOLCM37_BL.getResourceValue("BINOLBSSAM07", language, "BSSAM07_downloadFileDetailName"));
		map.put("titleRows", array);
		map.put(CherryConstants.SORT_ID, "workDate desc");
		return map;
	}
}
