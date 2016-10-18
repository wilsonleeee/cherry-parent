package com.cherry.mo.cio.interfaces;

import java.util.List;
import java.util.Map;

import com.cherry.cm.core.ICherryInterface;

public interface BINOLMOCIO10_IF extends ICherryInterface {

	/**
	 * 新增考核问卷
	 * @param map 存放的是操作员以及问卷的一些基本信息，包括所属组织，所属品牌，问卷名称等等
	 * @param list1 存放的是问卷分组信息
	 * @param list2 存放的是问卷问题的信息
	 * @param paperLevelList 存放的问卷评分水信息
	 * @throws Exception 抛出异常
	 * 
	 * */
	public void tran_addNewCheckPaper(Map<String,Object> map,List<Map<String,Object>> list1,List<Map<String,Object>> list2,List<Map<String,Object>> paperLevelList) throws Exception;

	/**
	 * 判断系统是否已经存在相同名称的问卷
	 * 
	 */
	public boolean isExsitSameNamePaper(Map<String, Object> map);

}
