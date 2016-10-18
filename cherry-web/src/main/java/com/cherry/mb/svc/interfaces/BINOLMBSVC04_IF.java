package com.cherry.mb.svc.interfaces;

import java.util.Map;

public interface BINOLMBSVC04_IF {
	/**
	 * 页面查询
	 */
	public Map<String, Object> getRangeInfo(Map<String, Object> map);
	/**
	 *保存范围值
	 */
	public void updateRangeInfo(Map<String, Object> map) throws Exception;
	/**
	 *新增范围值
	 */
	public void insertRangeInfo(Map<String, Object> map) throws Exception;
}
