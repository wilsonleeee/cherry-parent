/*  
 * @(#)BINBEMQMES96_Service.java     1.0 2016-1-4      
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
package com.cherry.mq.mes.service;

import java.util.Map;

import org.springframework.cache.annotation.Cacheable;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 
 * @ClassName: BINBEMQMES96_Service 
 * @Description: TODO(纯JSON格式MQ相关共通查询) 
 * @author menghao
 * @version v1.0.0 2016-1-4 
 *
 */
public class BINBEMQMES96_Service extends BaseService{
    
    /**
     * 查询部门信息
     * @param map
     * @return
     */
	@Cacheable(value="CherryDepartCache")
    public Map<String, Object> getDepartInfo(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES96.getDepartInfo");
        return (Map<String, Object>) baseServiceImpl.get(map);
    }

	/**
	 * 查询柜台是否有POS机
	 * @param map
	 * 		departCode，brandInfoID，organizationInfoID
	 * @return
	 *		departCode,posFlag
	 */
	public Map<String,Object> getCounterHasPosInfo(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES96.getCounterHasPosInfo");
		return (Map<String, Object>) baseServiceImpl.get(map);
	}
    
    /**
	 * 查询员工信息
	 * @param map
	 * @return
	 */
	@Cacheable(value="CherryEmpCache")
	public Map<String, Object> getEmployeeInfo (Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES96.getEmployeeInfo");
        return (Map<String, Object>) baseServiceImpl.get(map);
	}
	
}