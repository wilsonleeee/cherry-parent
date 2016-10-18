package com.cherry.mb.vis.bl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.core.CodeTable;
import com.cherry.mb.vis.service.BINOLMBVIS05_Service;

public class BINOLMBVIS05_BL  {
	
	@Resource
	private BINOLMBVIS05_Service binOLMBVIS05_Service;
	
	
	/**
	 * 取得回访信息总数
	 * 
	 * @param map 检索条件
	 * @return 回访信息总数
	 */
	public int getVisitTaskCount(Map<String, Object> map) {
		return binOLMBVIS05_Service.getVisitTaskCount(map);
	}

	/**
	 * 取得回访信息List
	 * 
	 * @param map 检索条件
	 * @return 回访信息List
	 */
	public List<Map<String, Object>> getVisitTaskList(Map<String, Object> map) {
		return binOLMBVIS05_Service.getVisitTaskList(map);
	}
	
	/**
	 * 取得该会员的第一次与最后一次的销售相关信息
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getSaleDetailByMemberCodeFL(Map<String, Object> map) {
		return binOLMBVIS05_Service.getSaleDetailByMemberCodeFL(map);
	}
	
	public void insertMemberVisit(Map<String, Object> map){
		binOLMBVIS05_Service.insertMemberVisit(map);
	}
	
	public void updateVisitTask(Map<String,Object> map){
		binOLMBVIS05_Service.updateVisitTask(map);
	}
	
	public Map<String,Object> getMemberInfo(Map<String,Object> map){
		return binOLMBVIS05_Service.getMemberInfo(map);
	}
	
}
