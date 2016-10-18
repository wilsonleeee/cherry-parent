package com.cherry.pt.rps.interfaces;

import java.util.List;
import java.util.Map;

import com.cherry.cm.cmbussiness.interfaces.BINOLCM37_IF;

/**
 * 
 * @ClassName: BINOLPTRPS36_IF 
 * @Description: TODO(柜台月度人效明细interfaces) 
 * @author menghao
 * @version v1.0.0 2015-1-13 
 *
 */
public interface BINOLPTRPS36_IF extends BINOLCM37_IF {

	/**
	 * 获取柜台月度人效明细数
	 * @param map
	 * @return
	 */
	public int getMonthPeopleEffectCount(Map<String, Object> map);
	
	/**
	 * 获取柜台月度人效明细list
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getMonthPeopleEffectList(Map<String, Object> map);
	
	/**
	 * 导出参数
	 * @param map
	 * @return
	 */
	public Map<String, Object> getExportParam(Map<String, Object> map);
}
