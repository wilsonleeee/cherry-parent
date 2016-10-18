package com.cherry.mo.cio.interfaces;

import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryException;
import com.cherry.cm.core.ICherryInterface;

public interface BINOLMOCIO05_IF extends ICherryInterface {

	/**
	 * 根据问卷ID获取问卷主表信息
	 * @param map 存放的是要获取的问卷的ID
	 * @return map 返回问卷的信息
	 * 
	 * */
	public Map<String,Object> getPaperForEdit(Map<String,Object> map);
	
	public Map<String,Object> getPaperForShow(Map<String,Object> map);
	
	/**
	 * 根据问卷ID获取问卷问题信息
	 * @param map 存放的是要获取的问题所属的问卷ID
	 * @return list 返回问题List
	 * 
	 * */
	public List<Map<String,Object>> getPaperQuestion(Map<String,Object> map);
	
	/**
	 * 保存问卷
	 * 
	 * 
	 * */
	public void tran_savePaper(Map<String,Object> map) throws Exception; 
	
	/**
	 * 判断系统是否已经存在相同名称的问卷
	 * 
	 */
	public boolean isExsitSameNamePaper(Map<String, Object> map);
	
}
