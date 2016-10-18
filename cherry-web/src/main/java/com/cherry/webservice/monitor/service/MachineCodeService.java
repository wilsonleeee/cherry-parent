package com.cherry.webservice.monitor.service;

import java.util.HashMap;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

public class MachineCodeService extends BaseService {

	/**
	 * 根据Mac地址查询机器信息表中的机器号
	 * @param map
	 * @return
	 */
	public Map<String, Object> getMachineCodeByMac(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID,  "MachineCode.getMachineCodeByMac");
		return (Map<String, Object>)baseServiceImpl.get(paramMap);
	}
	
	/**
	 * 更新机器信息
	 * @param map
	 * @return
	 */
	public int updateMachineInfo(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID,  "MachineCode.updateMachineInfo");
		return baseServiceImpl.update(paramMap);
	}
}
