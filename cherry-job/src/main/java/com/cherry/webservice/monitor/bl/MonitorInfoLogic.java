/*  
 * @(#)MonitorInfoLogic.java     1.0 2015/01/20      
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

package com.cherry.webservice.monitor.bl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.mq.mes.bl.BINBEMQMES97_BL;
import com.cherry.webservice.monitor.interfaces.MonitorInfo_IF;
import com.cherry.webservice.product.interfaces.ProductInspection_IF;
import com.cherry.webservice.product.service.ProductInspectionService;

/**
 * 
 * 监控业务 BL
 * 
 * @author niushunjie
 * @version 1.0 2015.014.20
 */
public class MonitorInfoLogic implements MonitorInfo_IF{
    
    private static Logger logger = LoggerFactory.getLogger(MonitorInfoLogic.class.getName());
    
    /**
     * 货品查询及记录
     * @param paramMap
     * @return
     */
    @Override
    public Map<String, Object> getTerminalConfig(Map<String, Object> paramMap) {
        Map<String, Object> retMap = new HashMap<String, Object>();
        try{
            Map<String,Object> resultMap = new HashMap<String,Object>();
            //TODO 暂时写死
            resultMap.put("Hotline", "400-881-1128");
            resultMap.put("Wechat", "WITPOS");
            retMap.put("ResultMap", resultMap);

            return retMap;
        }catch(Exception ex){
            logger.error("WS ERROR:", ex);
            logger.error("WS ERROR brandCode:"+ paramMap.get("BrandCode"));
            logger.error("WS ERROR paramData:"+ paramMap.get("OriginParamData"));
            retMap.put("ERRORCODE", "WSE9999");
            retMap.put("ERRORMSG", "处理过程中发生未知异常。");
            return retMap;
        }
    }

}