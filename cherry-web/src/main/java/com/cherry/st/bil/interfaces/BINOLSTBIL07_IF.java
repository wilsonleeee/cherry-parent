package com.cherry.st.bil.interfaces;

import java.util.List;
import java.util.Map;

import com.cherry.cm.core.ICherryInterface;

public interface BINOLSTBIL07_IF extends ICherryInterface{
	
	public int searchShiftCount(Map<String, Object> map);
	
	public List<Map<String, Object>> searchShiftList(Map<String, Object> map);
	
	public Map<String, Object> getSumInfo(Map<String, Object> map);
	
	/**
	 * 获取导出Excel
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public byte[] exportExcel(Map<String, Object> map) throws Exception;
}
