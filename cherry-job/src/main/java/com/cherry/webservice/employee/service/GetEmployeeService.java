package com.cherry.webservice.employee.service;

import java.util.List;
import java.util.Map;
import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.service.BaseService;

public class GetEmployeeService extends BaseService {
	/**
	 * 取得所有员工数据
	 * @param map
	 * @return
	 */
	public List getEmployeeList(Map<String,Object> map){
		map.put(CherryBatchConstants.IBATIS_SQL_ID, "GetEmployee.getEmployeeList");
		return baseServiceImpl.getList(map);
	}
}
