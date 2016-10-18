package com.cherry.cm.gadget.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

public class GadgetPrivilegeService extends BaseService {
	
	/**
	 * 查询部门数据权限
	 * 
	 * @param map 查询条件
	 * @return 部门数据权限
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getDepartPrivilegeList(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "gadgetPrivilege.getDepartPrivilegeList");
		return baseServiceImpl.getList(parameterMap);
	}
	
	/**
	 * 查询人员数据权限
	 * 
	 * @param map 查询条件
	 * @return 人员数据权限
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getEmployeePrivilegeList(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "gadgetPrivilege.getEmployeePrivilegeList");
		return baseServiceImpl.getList(parameterMap);
	}

}
