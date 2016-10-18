package com.cherry.mo.buy.service;

import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/*  
 * @(#)BINOLMOBUY01_Service.java    1.0 2012-5-28     
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
public class BINOLMOBUY01_Service extends BaseService {

	/**
     * 取得U盘考勤统计信息，画面使用
     * 
     * 
     * */
    public List getUdiskAttendanceStatisticsList(Map<String,Object> map){
    	return baseServiceImpl.getList(map, "BINOLMOBUY01.getUdiskAttendanceStatisticsList");
    }
    
    /**
     * 取得U盘考勤统计信息，导出使用
     * 
     * 
     * */
    public List getUdiskAttendanceStatisticsForImport(Map<String,Object> map){
    	return baseServiceImpl.getList(map, "BINOLMOBUY01.getUdiskAttendanceStatisticsForImport");
    }
    
    /**
     * 取得U盘考勤统计信息总数
     * 
     * 
     * */
    public int getUdiskAttendanceStatisticsCount(Map<String,Object> map){
    	map.put(CherryConstants.IBATIS_SQL_ID, "BINOLMOBUY01.getUdiskAttendanceStatisticsCount");
    	return baseServiceImpl.getSum(map);
    }
	
}
