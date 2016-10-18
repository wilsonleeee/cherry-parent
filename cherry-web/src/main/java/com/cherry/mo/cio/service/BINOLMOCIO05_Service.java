package com.cherry.mo.cio.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

public class BINOLMOCIO05_Service extends BaseService {
	
	/**
	 * 根据问卷ID获取问卷主表信息
	 * @param map 存放的是要获取的问卷的ID
	 * @return map 返回问卷的信息
	 * 
	 * */
	@SuppressWarnings("unchecked")
	public Map<String,Object> getPaperForEdit(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLMOCIO05.getPaperForEdit");
		List<Map<String,Object>> resultList = baseServiceImpl.getList(map);
		return resultList.get(0);
	}
	
	public List<Map<String,Object>> getPaperForShow(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLMOCIO05.getPaperForShow");
		List<Map<String,Object>> resultList = baseServiceImpl.getList(map);
		return resultList;
	}
	
	/**
	 * 根据问卷ID获取问卷问题信息
	 * @param map 存放的是要获取的问题所属的问卷ID
	 * @return list 返回问题List
	 * 
	 * */
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> getPaperQuestion(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLMOCIO05.getPaperQuestion");
		return baseServiceImpl.getList(map);
	}
	/**
	 * 根据问卷ID删除该问卷中的所有的问题
	 * @param map 存放的是要删除的问题所属的问卷ID
	 * @return
	 * 
	 * */
	public void deleteQuestion(Map<String,Object> map){
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		list.add(map);
		baseServiceImpl.deleteAll(list, "BINOLMOCIO05.deleteQuestion");
	}
	/**
	 * 根据问卷ID更新问卷信息
	 * @param map 存放的是要更新的问卷ID
	 * @retrun
	 * 
	 * */
	public int updatePaper(Map<String,Object> map){
		return baseServiceImpl.update(map, "BINOLMOCIO05.updatePaper");
	}
	
	
}
