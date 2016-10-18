package com.cherry.wp.wr.crp.interfaces;

import java.util.List;
import java.util.Map;

import com.cherry.cm.cmbussiness.interfaces.BINOLCM37_IF;

/**
 * 客户预约登记查询IF
 * 
 * @author menghao
 * @version 1.0 2014/12/24
 */
public interface BINOLWRCRP01_IF extends BINOLCM37_IF {
	
	/**
	 * 获取客户预约登记记录数量
	 * @param map
	 * @return
	 */
	public int getCampaignOrderCount(Map<String, Object> map);
	
	/**
	 * 获取客户预约登记记录LIST
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getCampaignOrderList(Map<String, Object> map);
	
	/**
	 * 获取导出参数
	 * @param map
	 * @return
	 */
	public Map<String, Object> getExportParam(Map<String, Object> map);
	
	/**
	 * 导出CSV
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public String exportCSV(Map<String, Object> map) throws Exception;

}
