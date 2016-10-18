package com.cherry.mo.cio.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.cherry.cm.service.BaseService;

@SuppressWarnings("unchecked")
public class BINOLMOCIO12_Service extends BaseService {

	/**
	 * 根据考核问卷ID更新考核问卷信息
	 * @param map 存放的问卷的更新信息
	 * 
	 * 
	 * */
	public void updateCheckPaper(Map<String,Object> map){
		baseServiceImpl.update(map, "BINOLMOCIO12.updateCheckPaper");
	}
	
	/**
	 * 根据考核问卷ID物理删除问题分组
	 * @param map 存放的问卷ID信息
	 * 
	 * 
	 * */
	public void deleteCheckPaperGroup(Map<String,Object> map){
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		list.add(map);
		baseServiceImpl.deleteAll(list, "BINOLMOCIO12.deleteCheckPaperGroup");
	}
	
	/**
	 * 根据考核问卷ID物理删除该问卷对应的问题
	 * @param map 存放的问卷ID信息
	 * 
	 * 
	 * */
	public void deleteCheckQuestion(Map<String,Object> map){
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		list.add(map);
		baseServiceImpl.deleteAll(list, "BINOLMOCIO12.deleteCheckQuestion");
	}
	
	/**
	 * 根据考核问卷ID删除该问卷对应的评分水平
	 * @param map 存放的问卷ID信息
	 * 
	 * 
	 * */
	public void deleteCheckPaperLevel(Map<String,Object> map){
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		list.add(map);
		baseServiceImpl.deleteAll(list, "BINOLMOCIO12.deleteCheckPaperLevel");
	}
	
	/**
	 * 根据考核问卷的ID查询该问卷对应的评分水平
	 * @param map 存放的是考核问卷的ID信息
	 * 
	 * 
	 * */
	public List<Map<String,Object>> getCheckPaperLevel(Map<String,Object> map){
		return baseServiceImpl.getList(map);
	}
}
