package com.cherry.mb.rpt.interfaces;

import java.util.List;
import java.util.Map;

import com.cherry.cm.cmbussiness.interfaces.BINOLCM37_IF;

/**
 * 
* @ClassName: BINOLMBRPT04_IF 
* @Description: TODO(会员发展统计interfaces) 
* @author menghao
* @version v1.0.0 2015-1-5 
*
 */
public interface BINOLMBRPT04_IF extends BINOLCM37_IF {
	
	/**
	 * 取得汇总信息
	 * @param map
	 * @return
	 */
	public Map<String, Object> getSumInfo(Map<String, Object> map);

	/**
	 * 取得会员发展统计信息LIST
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> getMemberDevelopRptList(
			Map<String, Object> map) throws Exception;
	
	/**
	 * 导出参数
	 * @param map
	 * @return
	 */
	public Map<String, Object> getExportParam(Map<String, Object> map);
	
}
