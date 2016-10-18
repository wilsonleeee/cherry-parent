package com.cherry.mo.cio.service;

import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

@SuppressWarnings("unchecked")
public class BINOLMOCIO09_Service extends BaseService {
	/**
	 * 取得考核问卷list
	 * @param map 存放的是查询条件
	 * @return list 返回的问题list
	 * 
	 * */
	public List<Map<String,Object>> getPaperList(Map<String,Object> map)
	{
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLMOCIO09.getPaperList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 取得考核问卷总数
	 * @param map 存放的是查询条件
	 * @return int 返回的是问题总数
	 * 
	 * */
	
	public int getPaperCount(Map<String,Object> map)
	{
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLMOCIO09.getPaperCount");
		return baseServiceImpl.getSum(map);
	}
	
	/**
	 * 根据考核问卷ID获取考核问卷主要信息
	 * @param map 存放的是考核问卷ID信息
	 * @return map 返回的是考核问卷主要信息
	 * 
	 * */
	public Map<String,Object> getCheckPaper(Map<String,Object> map){
		return (Map<String, Object>) baseServiceImpl.get(map,"BINOLMOCIO09.getCheckPaper");
	}
	
	/**
	 * 根据考核问卷ID获取考核问卷分组信息
	 * @param map 存放的是考核问卷ID
	 * @return list 返回考核问卷的分组信息
	 * 
	 * */
	public List<Map<String,Object>> getCheckPaperGroup(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLMOCIO09.getCheckPaperGroup");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 根据考核问卷ID获取问题
	 * @param map 存放的是考核问卷ID
	 * @return list 返回的考核问卷中包含的问题
	 * 
	 * */
	public List<Map<String,Object>> getCheckPaperQuestion(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLMOCIO09.getCheckPaperQuestion");
		return baseServiceImpl.getList(map);
	}
	
	/**根据考核问卷ID获取考核问卷评分标准
	 * @param map 存放的是考核问卷ID
	 * @return list 返回的考核问卷的评分标准list
	 * 
	 * */
	public List<Map<String,Object>> getCheckPaperLevel(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLMOCIO09.getCheckPaperLevel");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 根据考核问卷ID进行考核问卷的启用操作
	 * @param map 存放的是考核问卷ID
	 * 
	 * 
	 * */
	public void enableCheckPaper(Map<String,Object> map){
		baseServiceImpl.update(map, "BINOLMOCIO09.enableCheckPaper");
	}
	
	/**
	 * 根据考核问卷ID进行考核问卷的停用操作
	 * @param map 存放的是考核问卷ID
	 * 
	 * 
	 * */
	public void disableCheckPaper(Map<String,Object> map){
		baseServiceImpl.update(map, "BINOLMOCIO09.disableCheckPaper");
	}
	
	/**
	 * 根据考核问卷ID进行考核问卷的停用操作
	 * @param map 存放的是考核问卷ID
	 * 
	 * 
	 * */
	public void deleteCheckPaper(List<Map<String,Object>> list){
		baseServiceImpl.deleteAll(list, "BINOLMOCIO09.deleteCheckPaper");
	}
}
