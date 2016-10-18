/*
 * @(#)BINOLMOWAT10_Action.java     1.0 2015/7/1 
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
package com.cherry.mo.wat.interfaces;

import java.util.List;
import java.util.Map;
import com.cherry.cm.core.ICherryInterface;

/**
 * 
 * Job运行履历查询
 * 
 * @author ZCF
 * @version 1.0 2015.06.26
 */
public interface BINOLMOWAT10_IF extends ICherryInterface{
	
	 /**
     * Job运行履历信息总数
     * 
     * @param map 查询条件
     * @return Job运行履历信息总数
     */
    public int getCountJobRun(Map<String, Object> map);
    
    /**
     * Job运行履历信息List
     * 
     * @param map 查询条件
     * @return Job运行履历信息List
     */
    public List<Map<String,Object>> getJobRunList(Map<String, Object> map);
}
