/*  
 * @(#)BINOLBSCHA02_Action.java     1.0 2011/05/31      
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
package com.cherry.bs.cha.action;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.bs.cha.interfaces.BINOLBSCHA02_IF;
import com.cherry.cm.core.BaseAction;

@SuppressWarnings("unchecked")
public class BINOLBSCHA02_Action extends BaseAction{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8204570523907177638L;
	@Resource
	private BINOLBSCHA02_IF binolbscha02if;
	
	private int channelId;
	
	private Map	channelDetail;
	
	public void setChannelDetail(Map channelDetail) {
		this.channelDetail = channelDetail;
	}

	public Map getChannelDetail() {
		return channelDetail;
	}
	public int getChannelId() {
		return channelId;
	}

	public void setChannelId(int channelId) {
		this.channelId = channelId;
	}

	public String init()  throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("channelId", channelId);
		
		setChannelDetail(binolbscha02if.channelDetail(map));
		return SUCCESS;
	}
}