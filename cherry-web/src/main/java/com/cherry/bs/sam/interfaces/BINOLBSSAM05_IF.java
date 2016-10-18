package com.cherry.bs.sam.interfaces;

import java.util.List;
import java.util.Map;

public interface BINOLBSSAM05_IF {
	
	public List<Map<String, Object>> ResolveExcel(Map<String, Object> sessionMap) throws Exception;

	public Map<String, Object> tran_excelHandle(List<Map<String, Object>> importDataList,Map<String, Object> sessionMap);

	public Map<String, Object> getEmployeeCode(Map<String, Object> map);
}
