package com.cherry.st.bil.interfaces;

import java.util.List;
import java.util.Map;

public interface BINOLSTBIL01_IF {

    /**
     * 取得入库单总数
     * 
     * @param map
     * @return 
     */
	public int getPrtInDepotCount(Map<String,Object> map);
	
	/**
	 * 取得入库单list
	 * 
	 * @param map
	 * @return
	 */
	public List<Map<String,Object>> getPrtInDepotList(Map<String,Object> map);
	
	/**
	 * 汇总信息
	 * 
	 * @param map
	 * @return
	 */
	public Map<String, Object> getSumInfo(Map<String, Object> map);
	
	/**
	 * 获取导出Excel
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public byte[] exportExcel(Map<String, Object> map) throws Exception; 
}
