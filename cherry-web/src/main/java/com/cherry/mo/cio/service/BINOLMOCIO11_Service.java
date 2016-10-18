package com.cherry.mo.cio.service;

import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

@SuppressWarnings("unchecked")
public class BINOLMOCIO11_Service extends BaseService {

	/**
	 * 根据考核问卷ID获取问卷主表信息
	 * @param map 存放的是考核问卷的ID
	 * @return map 存放的查询出的结果
	 * 
	 * */
	public Map<String,Object> getCheckPaper(Map<String,Object> map){
		return (Map<String, Object>) baseServiceImpl.get(map, "BINOLMOCIO11.getCheckPaper");
	}
	
	/**
	 * 根据考核问卷ID获取问题分组信息list
	 * @param map 存放的考核问卷ID
	 * @return list 存放的是查询结果
	 * 
	 * */
	public List<Map<String,Object>> getCheckPaperGroup(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLMOCIO11.getCheckPaperGroup");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 根据考核问卷ID获取其中的问题list
	 * @param map 存放的考核问卷ID
	 * @return list 存放的查询的结果
	 * 
	 * */
	public List<Map<String,Object>> getCheckQuestion(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLMOCIO11.getCheckQuestion");
		return baseServiceImpl.getList(map);
	}
}
