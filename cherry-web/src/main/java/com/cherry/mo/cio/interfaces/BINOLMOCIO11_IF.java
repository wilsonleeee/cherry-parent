package com.cherry.mo.cio.interfaces;

import java.util.List;
import java.util.Map;

import com.cherry.cm.core.ICherryInterface;

public interface BINOLMOCIO11_IF extends ICherryInterface {

	/**
	 * 根据考核问卷ID获取问卷主表信息
	 * @param map 存放的是考核问卷的ID
	 * @return map 存放的查询出的结果
	 * 
	 * */
	public Map<String,Object> getCheckPaper(Map<String,Object> map);
	
	/**
	 * 根据考核问卷ID获取问题分组信息list
	 * @param map 存放的考核问卷ID
	 * @return list 存放的是查询结果
	 * 
	 * */
	public List<Map<String,Object>> getCheckPaperGroup(Map<String,Object> map);
	
	/**
	 * 根据考核问卷ID获取其中的问题list
	 * @param map 存放的考核问卷ID
	 * @return list 存放的查询的结果
	 * 
	 * */
	public List<Map<String,Object>> getCheckQuestion(Map<String,Object> map);
}
