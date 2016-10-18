package com.cherry.mo.buy.interfaces;

import java.util.Map;

import com.cherry.cm.core.ICherryInterface;

/*  
 * @(#)BINOLMOBUY01_IF.java    1.0 2012-5-28     
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
public interface BINOLMOBUY01_IF extends ICherryInterface {

	 /**
     * 导出考勤统计信息Excel
     * 
     * 
     * */
    public byte[] statisticsExportExcel(Map<String, Object> map) throws Exception;
    
    /**
     * 取得U盘考勤统计信息，画面使用
     * 
     * 
     * */
    public Map<String,Object> getUdiskAttendanceStatisticsList(Map<String,Object> map) throws Exception;
}
