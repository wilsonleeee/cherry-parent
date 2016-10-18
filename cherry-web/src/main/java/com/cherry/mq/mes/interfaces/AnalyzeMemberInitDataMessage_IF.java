/*		
 * @(#)AnalyzeMemberInitDataMessage_IF.java     1.0 2011/08/24		
 * 		
 * Copyright (c) 2010 SHANGHAI BINGKUN DIGITAL TECHNOLOGY CO.,LTD		
 * All rights reserved		
 * 		
 * This software is the confidential and proprietary information of 		
 * SHANGHAI BINGKUN.("Confidential Information").  You shall not		
 * disclose such Confidential Information and shall use it only in		
 * accordance with the terms of the license agreement you entered into		
 * with SHANGHAI BINGKUN.		
 */

package com.cherry.mq.mes.interfaces;

import java.util.Map;

/**
 * 
 * 会员初始数据采集信息接收处理接口
 * 
 * @author WangCT
 *
 */
public interface AnalyzeMemberInitDataMessage_IF extends BaseMessage_IF {
	
	/**
	 * 对会员初始数据采集信息进行处理
	 * @param map
	 */
	public void analyzeMemberInitData(Map<String,Object> map) throws Exception;
	
	/**
	 * 对会员化妆次数使用信息进行处理
	 * @param map
	 */
	public void analyzeUsedTimes(Map<String,Object> map) throws Exception;
	
	/**
	 * 会员修改属性处理
	 * @param map
	 */
	public void updateMemberExtInfo(Map<String,Object> map) throws Exception;

}
