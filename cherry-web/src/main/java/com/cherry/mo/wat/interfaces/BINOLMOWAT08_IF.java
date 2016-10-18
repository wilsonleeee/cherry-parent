package com.cherry.mo.wat.interfaces;

import java.util.List;
import java.util.Map;

import com.cherry.cm.core.ICherryInterface;
import com.mongodb.DBObject;

public interface BINOLMOWAT08_IF extends ICherryInterface {
	
	/**
	 * 查询终端消息反馈消息LIST
	 * 
	 * @param map 查询条件
	 * @return 终端消息反馈日志List
	 */
	public List<DBObject> getMQNoticeInfoList(Map<String, Object> map) throws Exception;
	
	/**
	 * 查询终端消息反馈消息数量
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public int getMQNoticeInfoCount(Map<String, Object> map) throws Exception;
}
