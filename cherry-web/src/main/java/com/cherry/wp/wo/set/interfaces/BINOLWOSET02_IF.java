package com.cherry.wp.wo.set.interfaces;

import java.util.List;
import java.util.Map;

/**
 * 考勤管理IF
 * 
 * @author WangCT
 * @version 1.0 2014/10/22
 */
public interface BINOLWOSET02_IF {
	
	public int getBAAttendanceCount(Map<String, Object> map);
	
	public List<Map<String, Object>> getBAAttendanceList(Map<String, Object> map);
	
	public void insertBAAttendance(Map<String, Object> map);

}
