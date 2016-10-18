package com.cherry.mq.mes.interfaces;

import java.util.Map;

import com.cherry.mq.mes.common.CherryMQException;

public interface AnalyzeMemberMessage_IF extends BaseMessage_IF{
	/**
	 * 对会员数据进行处理
	 * @param map
	 * @throws CherryMQException 
	 */
	public void analyzeMemberData(Map<String,Object> map) throws Exception;
	
	/**
	 * 对BA数据进行处理
	 * @param map
	 * @throws Exception 
	 */
	public void analyzeBaInfoData(Map<String,Object> map) throws Exception;
	
	/**
	 * 对BAS考勤数据进行处理
	 * @param map
	 */
	public void analyzeBasAttInfoData(Map<String,Object> map);
	
    /**
     * 对普通考勤信息进行处理
     * @param map
     * @throws CherryMQException 
     * @throws Exception 
     */
    public void analyzeAttendanceData(Map<String,Object> map) throws Exception;
	
	/**
	 * 对会员回访数据进行处理
	 * @param map
	 */
	public void analyzeMemVisitInfoData(Map<String,Object> map);
	
	/**
	 * 对会员回访数据带问卷进行处理
	 * @param map
	 */
	public void analyzeMemberMV(Map<String,Object> map) throws CherryMQException;
	
    /**
     * 对终端设定销售目标数据进行处理
     * @param map
     */
    public void analyzeSaleTarget(Map<String,Object> map) throws CherryMQException;
	
    /**
     * 会员回访插入消息信息(MongoDB)
     * @param map
     * @throws CherryMQException
     */
    public void addMongoMsgInfoMV(Map map) throws CherryMQException;
    
    /**
     * 对会员积分数据进行处理
     * @param map
     * @throws CherryMQException 
     */
    public void analyzeMemberPointData(Map<String,Object> map) throws Exception;
    
    /**
     * 对会员激活数据进行处理
     * @param map
     * @throws CherryMQException 
     */
    public void analyzeMemberActive(Map<String,Object> map) throws Exception;
    
    /**
     * 短信回复信息采集
     * @param map
     * @throws CherryMQException 
     */
    public void analyzeMemberDXCJ(Map<String,Object> map) throws Exception;
}
