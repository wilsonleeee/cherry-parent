package com.cherry.ct.pln.interfaces;

import java.util.List;
import java.util.Map;

public interface BINOLCTPLN02_IF {
	/**
	 * 获取沟通事件设置List
	 * 
	 * @param map
	 * @return 沟通事件设置List
	 * @throws Exception 
	 */
	public List<Map<String, Object>> getEventSetList(Map<String, Object> map) throws Exception;
	
	/**
	 * 获取沟通事件延时设置List
	 * 
	 * @param map
	 * @return 沟通事件延时设置List
	 * @throws Exception 
	 */
	public Map<String, Object> getDelaySetInfo(Map<String, Object> map) throws Exception;
	
	/**
	 * 保存事件设置
	 * 
	 * @param map
	 * @return null
	 * @throws Exception 
	 */
	public void tran_saveEventSet(Map<String, Object> map) throws Exception;
	
	/**
	 * 停用事件设置
	 * 
	 * @param map
	 * @return null
	 * @throws Exception 
	 */
	public void tran_stopEventSet(Map<String, Object> map) throws Exception;
}
