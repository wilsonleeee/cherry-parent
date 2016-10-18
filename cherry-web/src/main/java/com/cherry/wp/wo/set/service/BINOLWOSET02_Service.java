package com.cherry.wp.wo.set.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 考勤管理Service
 * 
 * @author WangCT
 * @version 1.0 2014/10/22
 */
public class BINOLWOSET02_Service extends BaseService {
	
	/**
	 * 查询营业员考勤信息数量
	 * 
	 * @param map 查询条件
	 * @return 营业员考勤信息数量
	 */
	public int getBAAttendanceCount(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLWOSET02.getBAAttendanceCount");
		return baseServiceImpl.getSum(paramMap);
	}
	
	/**
	 * 查询营业员考勤信息List
	 * 
	 * @param map 查询条件
	 * @return 营业员考勤信息List
	 */
	public List<Map<String, Object>> getBAAttendanceList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLWOSET02.getBAAttendanceList");
		return baseServiceImpl.getList(paramMap);
	}
	
	/**
	 * 添加营业员考勤信息
	 * 
	 * @param map 插入内容
	 */
	public void insertBAAttendance(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLWOSET02.insertBAAttendance");
		baseServiceImpl.save(paramMap);
	}

}
