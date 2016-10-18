package com.cherry.mq.mes.interfaces;

import java.util.List;
import java.util.Map;

import com.cherry.mq.mes.common.CherryMQException;

/**
 * 基类消息接口
 * @author huzude
 *
 */
@SuppressWarnings("unchecked")
public interface BaseMessage_IF {
	/**
	 * 查询主消息数据
	 * @param map
	 */
	public void selMessageInfo(Map map) throws Exception;
	
	/**
	 * 查询明细消息数据
	 * @param map
	 */
	public void setDetailDataInfo(List detailDataList, Map map) throws Exception;
	
	/**
	 * 设置操作程序名称
	 * @param map
	 */
	public void setInsertInfoMapKey (Map map);
	
	/**
	 * 插入消息信息(MongoDB)
	 * @param map
	 * @throws CherryMQException
	 */
	public void addMongoMsgInfo(Map map) throws CherryMQException;
	
}
