package com.cherry.wp.wo.set.bl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.wp.wo.set.interfaces.BINOLWOSET02_IF;
import com.cherry.wp.wo.set.service.BINOLWOSET02_Service;

/**
 * 考勤管理BL
 * 
 * @author WangCT
 * @version 1.0 2014/10/22
 */
public class BINOLWOSET02_BL implements BINOLWOSET02_IF {
	
	/** 考勤管理Service **/
	@Resource
	private BINOLWOSET02_Service binOLWOSET02_Service;

	@Override
	public int getBAAttendanceCount(Map<String, Object> map) {
		return binOLWOSET02_Service.getBAAttendanceCount(map);
	}

	@Override
	public List<Map<String, Object>> getBAAttendanceList(Map<String, Object> map) {
		return binOLWOSET02_Service.getBAAttendanceList(map);
	}

	@Override
	public void insertBAAttendance(Map<String, Object> map) {
		binOLWOSET02_Service.insertBAAttendance(map);
	}

}
