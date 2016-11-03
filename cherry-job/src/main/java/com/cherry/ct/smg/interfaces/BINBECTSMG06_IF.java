package com.cherry.ct.smg.interfaces;

import java.util.List;
import java.util.Map;

public interface BINBECTSMG06_IF {
	/**
	 * 根据会员ID获取会员信息
	 * 
	 * @param Map 
	 * @throws Exception 
	 */
	public Map<String, Object> getMemberInfoById (Map<String, Object> map) throws Exception;
	
	/**
	 * 查询沟通信息是否已经发送过 
	 * 
	 * @param Map 
	 * @throws Exception 
	 */
	public int getSmsSendFlag (Map<String, Object> map) throws Exception;
	
	/**
	 * 查询实时接口配置信息
	 * 
	 * @param Map 
	 * @throws Exception 
	 */
	public List<Map<String, Object>> getConfigInfo (Map<String, Object> map) throws Exception;
	
	/**
	 * 根据搜索编号获取搜索记录信息
	 * 
	 * @param Map 
	 * @throws Exception 
	 */
	public Map<String, Object> getSearchInfo (Map<String, Object> map) throws Exception;
}
