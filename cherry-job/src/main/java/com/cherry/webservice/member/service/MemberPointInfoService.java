/*  
 * @(#)MemberPointInfoService.java     1.0 2014/08/01      
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

package com.cherry.webservice.member.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 
 * 积分业务Service
 * 
 * @author niushunjie
 * @version 1.0 2014.08.01
 */
public class MemberPointInfoService extends BaseService {
    public List<Map<String,Object>> getMemCardInfo(Map<String,Object> map){
        Map<String,Object> paramMap = new HashMap<String,Object>();
        paramMap.putAll(map);
        paramMap.put(CherryConstants.IBATIS_SQL_ID, "MemberPointInfo.getMemCardInfo");
        return baseServiceImpl.getList(paramMap);
    }
    
    public List<Map<String,Object>> getMQ_Log(Map<String,Object> map){
        Map<String,Object> paramMap = new HashMap<String,Object>();
        paramMap.putAll(map);
        paramMap.put(CherryConstants.IBATIS_SQL_ID, "MemberPointInfo.getMQ_Log");
        return witBaseServiceImpl.getList(paramMap);
    }
}