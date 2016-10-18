/*		
 * @(#)MqReceiver_IF.java     1.0 2015.12.29
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
 * @ClassName: MqReceiver_IF 
 * @Description: TODO(MQ接收处理接口) 
 * @author menghao
 * @version v1.0.0 2015-12-29 
 *
 */
public interface MqReceiver_IF {
	
	/**
	 * 接收MQ消息处理(不同业务有不同的实现)
	 * 
	 * @param msg MQ消息
	 * @throws Exception 
	 */
    public void tran_execute(Map<String, Object> map) throws Exception;
    
}
