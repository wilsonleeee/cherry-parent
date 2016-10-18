package com.cherry.bs.wem.interfaces;

import java.util.List;
import java.util.Map;

import com.cherry.cm.cmbussiness.interfaces.BINOLCM37_IF;

public interface BINOLBSWEM05_IF extends BINOLCM37_IF{

	// 获取分成微商统计数量
	public int getBonusCount(Map<String, Object> map);
	
	// 获取返点分成统计列表
	public List<Map<String, Object>> getBonusList (Map<String, Object> map) throws Exception;
	
	// 获取返点分成导出数据
	public Map<String, Object> getExportMap (Map<String, Object> map);

	/**
	 * 获取级别（总部、省代、市代、商城）
	 * @param codeList
	 * @return
	 */
	public List getAgentLevelList(List codeList);
}
