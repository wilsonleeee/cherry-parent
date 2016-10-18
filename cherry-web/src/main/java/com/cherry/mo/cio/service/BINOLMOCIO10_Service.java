package com.cherry.mo.cio.service;

import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

public class BINOLMOCIO10_Service extends BaseService {

	/**
	 * 向考核问卷中插入新增问卷，并返回自增长的问卷ID
	 * @param map 存放的是问卷信息
	 * @return int 返回问卷ID
	 * 
	 * */
	public int insertNewCheckPaper(Map<String,Object> map){
		return baseServiceImpl.saveBackId(map, "BINOLMOCIO10.insertPaper");
	}
	
	/**
	 * 向考核问卷问题分组信息表中插入新增分组，并返回自增长的分组ID
	 * @param map 存放的是分组信息
	 * @return int 返回分组ID
	 * 
	 * */
	public int insertNewGroup(Map<String,Object> map){
		return baseServiceImpl.saveBackId(map, "BINOLMOCIO10.insertQuestionGroup");
	}
	
	/**
	 * 向考核问卷问题表中插入新增问题
	 * @param list 存放的是新增的问题list
	 * @return
	 * 
	 * */
	public void insertNewQuestion(List<Map<String,Object>> list){
		baseServiceImpl.saveAll(list, "BINOLMOCIO10.insertPaperQuestion");
	}
	
	/**
	 * 向考核问卷评分级别表中插入级别标准
	 * @param list 存放的问题级别信息
	 * @return 
	 * 
	 * */
	public void insertCheckPaperLevel(List<Map<String,Object>> list){
		baseServiceImpl.saveAll(list, "BINOLMOCIO10.insertCheckPaperLevel");
	}
	
	/**
	 * 判断系统中是否已经存在相同名称的问卷
	 * 
	 * */
	public List<Map<String, Object>> isExsitSameNamePaper(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLMOCIO10.isExsitSameNamePaper");
		return baseServiceImpl.getList(map);
	}
}
