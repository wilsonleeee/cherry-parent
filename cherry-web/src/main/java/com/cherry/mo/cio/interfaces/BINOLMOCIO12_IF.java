package com.cherry.mo.cio.interfaces;

import java.util.List;
import java.util.Map;

import com.cherry.cm.core.ICherryInterface;

public interface BINOLMOCIO12_IF extends ICherryInterface {

	/**
	 * 更新考核问卷
	 * @param map 存放的是操作员以及问卷的一些基本信息，包括所属组织，所属品牌，问卷名称等等
	 * @param list1 存放的是问卷分组信息
	 * @param list2 存放的是问卷问题的信息
	 * @param paperLevelList 存放的问卷评分水信息
	 * @throws Exception 抛出异常
	 * 
	 * */
	public void tran_updateCheckPaper(Map<String,Object> map,List<Map<String,Object>> list1,List<Map<String,Object>> list2,List<Map<String,Object>> paperLevelList) throws Exception;
	
	/**
	 * 根据考核问卷的ID查询该问卷对应的评分水平
	 * @param map 存放的是考核问卷的ID信息
	 * @return list 存放的是返回的结果
	 * 
	 * */
	public List<Map<String,Object>> getCheckPaperLevel(Map<String,Object> map);
	
	/**
	 * 根据考核问卷ID获取问题分组信息list
	 * @param map 存放的考核问卷ID
	 * @return list 存放的是查询结果
	 * 
	 * */
	public List<Map<String,Object>> getCheckPaperGroup(Map<String,Object> map);
	
	public Map<String, Object> getCheckPaper(Map<String, Object> map);
	
	/**
	 * 判断系统是否已经存在相同名称的问卷
	 * 
	 */
	public boolean isExsitSameNamePaper(Map<String, Object> map);
}
