package com.cherry.mb.cct.interfaces;

import java.util.List;
import java.util.Map;

public interface BINOLMBCCT02_IF {
	/**
	 * 根据客户ID获取客户来电问题记录列表
	 * 
	 * @param map
	 * @return 来电记录列表
	 */
	@SuppressWarnings("rawtypes")
	public List getIssueListByCustomer(Map<String, Object> map);
	
	/**
	 * 根据客户ID获取客户来电问题记录数量
	 * 
	 * @param map
	 * @return 来电记录数量
	 */
	public int getIssueCountByCustomer(Map<String, Object> map);
}
