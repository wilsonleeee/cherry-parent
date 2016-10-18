package com.cherry.bs.sam.interfaces;

import java.util.List;
import java.util.Map;

public interface BINOLBSSAM01_IF {

	public int getScheduleCount(Map<String,Object> params);
	
	public List<Map<String,Object>> getScheduleList(Map<String,Object> params);
}
