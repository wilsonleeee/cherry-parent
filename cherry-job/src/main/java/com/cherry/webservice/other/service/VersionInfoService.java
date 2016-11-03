/*  
 * @(#)VersionInfoService.java     1.0 2016/09/05      
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

package com.cherry.webservice.other.service;

import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.service.BaseService;

/**
 * 
 * 终端版本号变更Service
 * 
 */
public class VersionInfoService extends BaseService {
    
    /**
     * 添加终端版本号
     * @param map
     * @return
     */
	public void insertVersion(Map<String, Object> map) {
		map.put(CherryBatchConstants.IBATIS_SQL_ID,"VersionInfo.insertVersion");
		witBaseServiceImpl.save(map);
	}
    /**
     * 删除终端版本号
     * @param map
     * 
     */
	public void deleteVersion(List<Map<String, Object>> list) {
		witBaseServiceImpl.deleteAll(list, "VersionInfo.deleteVersion");
	}
}