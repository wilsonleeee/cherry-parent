package com.cherry.bs.sam.interfaces;

import java.util.List;
import java.util.Map;

import com.cherry.cm.cmbussiness.interfaces.BINOLCM37_IF;

public interface BINOLBSSAM06_IF extends BINOLCM37_IF{

	public Map<String, Object> getExportMap(Map<String, Object> map);

	public int getBAAttendanceCount(Map<String, Object> map);

	public List<Map<String, Object>> getBAAttendanceList(Map<String, Object> map);

	public List<Map<String, Object>> getDataList(Map<String, Object> map);

}
