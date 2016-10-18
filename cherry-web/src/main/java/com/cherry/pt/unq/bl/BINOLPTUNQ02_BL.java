/*  
 * @(#)BINOLPTUNQ02_BL    1.0 2016-06-06     
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

package com.cherry.pt.unq.bl;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.pt.unq.interfaces.BINOLPTUNQ02_IF;
import com.cherry.pt.unq.service.BINOLPTUNQ02_Service;

/**
 * 单码查询 BL
 * 
 * @author zw
 * @version 1.0 2016.06.06
 */
public class BINOLPTUNQ02_BL implements BINOLPTUNQ02_IF,Serializable{
	
	private static final long serialVersionUID = -4699840221575625314L;
	
	@Resource
	private BINOLPTUNQ02_Service binOLPTUNQ02_Service;		
	
	/** 
	 * 单码查询总数
	 * @param map
	 * @return int
	 * 
	 */
	public int getSingleCodeSearchCount(Map<String, Object> map) {
		return binOLPTUNQ02_Service.getSingleCodeSearchCount(map);
	}

	/** 
	 * 单码查询一览
	 * @param map
	 * @return list
	 * 
	 */
	public List<Map<String, Object>> getSingleCodeList(Map<String, Object> map) {
		return binOLPTUNQ02_Service.getSingleCodeList(map);
	}

}
