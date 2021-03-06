package com.cherry.bs.sam.service;

import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

public class BINOLBSSAM07_Service extends BaseService{
	
	public int getBAAttendanceCount(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSSAM07.getBAAttendanceCount");
		return baseServiceImpl.getSum(map);
	}

	public List<Map<String, Object>> getBAAttendanceList(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSSAM07.getBAAttendanceList");
		return baseServiceImpl.getList(map);
	}

}
